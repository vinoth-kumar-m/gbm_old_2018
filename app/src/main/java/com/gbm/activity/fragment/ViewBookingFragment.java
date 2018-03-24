package com.gbm.activity.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.constants.GBMConstants;
import com.gbm.db.GBMDBUtils;
import com.gbm.utils.PDFCreator;
import com.gbm.utils.PDFCreatorFactory;
import com.gbm.vo.BookingVO;
import com.gbm.vo.ProductVO;

import java.io.File;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class ViewBookingFragment extends Fragment {

    private static final String TAG = "ViewBookingFragment";
    private String roleSelected;
    private View bookingView;
    BookingVO booking;
    GBMDBUtils dbUtils;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the dropdown_item for this fragment
        bookingView = inflater.inflate(R.layout.fragment_booking_view, container, false);
        dbUtils = new GBMDBUtils(getContext());
        booking = (BookingVO) getArguments().getSerializable("booking");

        final EditText _bookingNo = (EditText) bookingView.findViewById(R.id.input_bookingNo);
        final EditText _bookingDate = (EditText) bookingView.findViewById(R.id.input_bookingDate);
        final EditText _receiverName = (EditText) bookingView.findViewById(R.id.input_receiverName);
        final EditText _receiverAddress = (EditText) bookingView.findViewById(R.id.input_receiverAddress);
        final EditText _phone = (EditText) bookingView.findViewById(R.id.input_receiverPhoneNo);
        final EditText _rent = (EditText) bookingView.findViewById(R.id.input_rent);
        final EditText _total = (EditText) bookingView.findViewById(R.id.input_total);
        final EditText _advance = (EditText) bookingView.findViewById(R.id.input_advance);
        final EditText _balance = (EditText) bookingView.findViewById(R.id.input_balance);

        _bookingNo.setText(booking.getBookingNo());
        _bookingDate.setText(booking.getBookingDt());
        _receiverName.setText(booking.getReceiverName());
        _receiverAddress.setText(booking.getAddress());
        _phone.setText(booking.getPhone());
        _rent.setText(String.valueOf(booking.getRent().setScale(2)));
        _total.setText(String.valueOf(booking.getTotal().setScale(2)));
        _advance.setText(String.valueOf(booking.getAdvance().setScale(2)));
        _balance.setText(String.valueOf(booking.getBalance().setScale(2)));

        populateProducts();

        Button _deleteButton = (Button) bookingView.findViewById(R.id.btn_delete);

        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm")
                        .setMessage("Do you want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dbUtils.deleteBooking(booking);
                                Toast.makeText(getContext(), "Booking deleted successfully", Toast.LENGTH_LONG).show();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                Fragment fragment = new SearchBookingFragment();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        Button _printButton = (Button) bookingView.findViewById(R.id.btn_print);

        _printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PDFCreator pdfCreator = PDFCreatorFactory.getInstance(GBMConstants.BOOKING);
                    File file = pdfCreator.createPDF(getContext(), booking.getBookingNo() + ".pdf", booking);
                    pdfCreator.viewPDF(getContext(), file);
                } catch(Exception ex) {
                    Log.e(TAG, ex.getLocalizedMessage());
                }
            }
        });

        Button _closeButton = (Button) bookingView.findViewById(R.id.btn_close);

        _closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SearchBookingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return bookingView;
    }

    private void populateProducts() {
        TableLayout _productsTblLayout = (TableLayout) bookingView.findViewById(R.id.tbl_products);
        for (int i = 0; i < _productsTblLayout.getChildCount(); i++) {
            View child = _productsTblLayout.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        Log.d(TAG, "Populating Products...");
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.setBackgroundResource(R.drawable.cell_shape);

        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Product");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("HSN");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Qty");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Price");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Total");
        row.addView(tv);

        _productsTblLayout.addView(row);
        int idx = 0;
        if(booking != null) {
            for (ProductVO product : booking.getProducts()) {
                row = new TableRow(getContext());
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                row.setBackgroundResource(R.drawable.cell_shape);

                tv = new TextView(getContext());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(product.getName());
                row.addView(tv);

                tv = new TextView(getContext());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(product.getHsnCode());
                row.addView(tv);

                tv = new TextView(getContext());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(String.valueOf(product.getQuantity().setScale(2)));
                row.addView(tv);

                tv = new TextView(getContext());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(String.valueOf(product.getPrice().setScale(2)));
                row.addView(tv);

                tv = new TextView(getContext());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(String.valueOf(product.getTotal().setScale(2)));
                row.addView(tv);

                _productsTblLayout.addView(row);
            }
        }
    }

}

