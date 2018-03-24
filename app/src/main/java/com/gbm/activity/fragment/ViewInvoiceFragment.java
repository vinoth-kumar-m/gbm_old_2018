package com.gbm.activity.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.gbm.vo.InvoiceVO;
import com.gbm.vo.ProductVO;

import java.io.File;

/**
 * Created by Sri on 9/12/2017.
 */

public class ViewInvoiceFragment extends Fragment {

    private static final String TAG = "ViewInvoiceFragment";
    private String roleSelected;
    private View invoiceView;
    InvoiceVO invoice;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        invoiceView = inflater.inflate(R.layout.fragment_invoice_view, container, false);
        dbUtils = new GBMDBUtils(getContext());
        invoice = (InvoiceVO) getArguments().getSerializable("invoice");

        final EditText _invoiceNo = (EditText) invoiceView.findViewById(R.id.input_invoiceNo);
        final EditText _invoiceDate = (EditText) invoiceView.findViewById(R.id.input_invoiceDate);
        final EditText _supplyDate = (EditText) invoiceView.findViewById(R.id.input_supplyDate);
        final EditText _vehicleNo = (EditText) invoiceView.findViewById(R.id.input_vehicleNo);
        final EditText _receiverName = (EditText) invoiceView.findViewById(R.id.input_receiverName);
        final EditText _receiverAddress = (EditText) invoiceView.findViewById(R.id.input_receiverAddress);
        final EditText _phone = (EditText) invoiceView.findViewById(R.id.input_receiverPhoneNo);
        final EditText _gstin = (EditText) invoiceView.findViewById(R.id.input_receiverGSTIN);
        final EditText _aadhar = (EditText) invoiceView.findViewById(R.id.input_receiverAadharNo);
        final EditText _pan = (EditText) invoiceView.findViewById(R.id.input_receiverPAN);
        final EditText _totalBeforeTax = (EditText) invoiceView.findViewById(R.id.input_totalBeforeTax);
        final EditText _sGST = (EditText) invoiceView.findViewById(R.id.input_sGST);
        final EditText _cGST = (EditText) invoiceView.findViewById(R.id.input_cGST);
        final EditText _totalAfterTax = (EditText) invoiceView.findViewById(R.id.input_totalAfterTax);

        _invoiceNo.setText(invoice.getInvoiceNo());
        _invoiceDate.setText(invoice.getInvoiceDt());
        _supplyDate.setText(invoice.getSupplyDt());
        _vehicleNo.setText(invoice.getVehicleNo());
        _receiverName.setText(invoice.getReceiverName());
        _receiverAddress.setText(invoice.getAddress());
        _phone.setText(invoice.getPhone());
        _gstin.setText(invoice.getGstin());
        _aadhar.setText(invoice.getAadhar());
        _pan.setText(invoice.getPan());
        _totalBeforeTax.setText(invoice.getTotalBeforeTax().setScale(2).toString());
        _sGST.setText(invoice.getsGST().setScale(2).toString());
        _cGST.setText(invoice.getcGST().setScale(2).toString());
        _totalAfterTax.setText(invoice.getTotalAfterTax().setScale(2).toString());

        populateProducts();

        Button _printButton = (Button) invoiceView.findViewById(R.id.btn_print);

        _printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PDFCreator pdfCreator = PDFCreatorFactory.getInstance(GBMConstants.INVOICE);
                    File file = pdfCreator.createPDF(getContext(), invoice.getInvoiceNo() + ".pdf", invoice);
                    pdfCreator.viewPDF(getContext(), file);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getLocalizedMessage());
                }
            }
        });

        Button _deleteButton = (Button) invoiceView.findViewById(R.id.btn_delete);

        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm")
                        .setMessage("Do you want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dbUtils.deleteInvoice(invoice);
                                Toast.makeText(getContext(), "Invoice deleted successfully", Toast.LENGTH_LONG).show();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                Fragment fragment = new SearchInvoiceFragment();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        Button _closeButton = (Button) invoiceView.findViewById(R.id.btn_close);

        _closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SearchInvoiceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return invoiceView;
    }

    private void populateProducts() {
        TableLayout _productsTblLayout = (TableLayout) invoiceView.findViewById(R.id.tbl_products);
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
        if(invoice != null) {
            for (ProductVO product : invoice.getProducts()) {
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

