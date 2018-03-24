package com.gbm.activity.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.constants.GBMConstants;
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.CreditVO;
import com.gbm.vo.CustomerVO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sri on 9/12/2017.
 */

public class SearchCreditFragment extends Fragment {

    private static final String TAG = "SearchCreditFragment";
    private View searchCreditView;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the dropdown_item for this fragment
        searchCreditView = inflater.inflate(R.layout.fragment_credit_search, container, false);
        dbUtils = new GBMDBUtils(getContext());
        Button _searchButton = (Button) searchCreditView.findViewById(R.id.btn_search);

        _searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCredit();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();

        final EditText _bookingDate = (EditText) searchCreditView.findViewById(R.id.input_bookingDate);
        //_bookingDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
        _bookingDate.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    _bookingDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                }
            };

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button _addButton = (Button) searchCreditView.findViewById(R.id.btn_add);

        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddCreditFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        final EditText _receiverName = (EditText) searchCreditView.findViewById(R.id.input_receiverName);
        _receiverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<CustomerVO> customers = dbUtils.getCustomers();
                if(!customers.isEmpty()) {
                    final Dialog customerDialog = new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                            .setTitle("Customers")
                            .setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_customer_lookup, null))
                            .setNegativeButton(android.R.string.no, null).create();

                    customerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            TableLayout _customersTblLayout = (TableLayout) customerDialog.findViewById(R.id.tbl_customers);
                            for (int i = 0; i < _customersTblLayout.getChildCount(); i++) {
                                View child = _customersTblLayout.getChildAt(i);
                                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
                            }

                            Log.d(TAG, "Populating Customers...");
                            TableRow row = new TableRow(getContext());
                            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            row.setBackgroundResource(R.drawable.cell_shape);

                            TextView tv = new TextView(getContext());
                            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            tv.setTypeface(Typeface.DEFAULT_BOLD);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                            tv.setTextColor(getResources().getColor(R.color.primary_dark));
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText("Name");
                            row.addView(tv);

                            _customersTblLayout.addView(row);

                            for (final CustomerVO customer : customers) {
                                row = new TableRow(getContext());
                                row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                row.setBackgroundResource(R.drawable.cell_shape);

                                tv = new TextView(getContext());
                                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                                tv.setTypeface(tv.getTypeface(), Typeface.BOLD_ITALIC);
                                tv.setPadding(5, 5, 5, 5);
                                tv.setText(customer.getReceiverName());
                                tv.setTag(customer);
                                tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        _receiverName.setText(customer.getReceiverName());
                                        customerDialog.dismiss();

                                    }
                                });
                                row.addView(tv);

                                _customersTblLayout.addView(row);
                            }
                        }
                    });
                    customerDialog.show();
                }
            }
        });

        searchCredit();

        return searchCreditView;
    }

    private void populateCredits(List<CreditVO> credits) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal cashAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;
        TableLayout _creditsTblLayout = (TableLayout) searchCreditView.findViewById(R.id.tbl_credits);
        for (int i = 0; i < _creditsTblLayout.getChildCount(); i++) {
            View child = _creditsTblLayout.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        Log.d(TAG, "Populating Bookings...");
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        row.setBackgroundResource(R.drawable.cell_shape);


        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Booking Date");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Name");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Sale Type");
        row.addView(tv);

        _creditsTblLayout.addView(row);

        for (CreditVO credit : credits) {
            if(GBMConstants.SALE_TYPE_CASH.equals(credit.getSaleType())) {
                cashAmount = cashAmount.add(credit.getAmount());
            }
            else if(GBMConstants.SALE_TYPE_CREDIT.equals(credit.getSaleType())) {
                creditAmount = creditAmount.add(credit.getAmount());
            }
            totalAmount = totalAmount.add(credit.getAmount());

            row = new TableRow(getContext());
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.cell_shape);

            tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD_ITALIC);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(credit.getBookingDt());
            tv.setTag(credit);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("credit", (CreditVO) v.getTag());
                    Fragment fragment = new ViewCreditFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            row.addView(tv);

            tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(credit.getReceiverName());
            row.addView(tv);

            tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(String.valueOf(credit.getSaleType()));
            row.addView(tv);

            _creditsTblLayout.addView(row);
        }
        final TextView _total = (TextView) searchCreditView.findViewById(R.id.input_total);
        final TextView _cash = (TextView) searchCreditView.findViewById(R.id.input_cash);
        final TextView _credit = (TextView) searchCreditView.findViewById(R.id.input_credit);

        _total.setText(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        _cash.setText(cashAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        _credit.setText(creditAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

    public void searchCredit() {
        Log.d(TAG, "Searching Booking...");

        final EditText _bookingNo = (EditText) searchCreditView.findViewById(R.id.input_bookingNo);
        final EditText _bookingDate = (EditText) searchCreditView.findViewById(R.id.input_bookingDate);
        final EditText _receiverName = (EditText) searchCreditView.findViewById(R.id.input_receiverName);
		final RadioGroup radioSaleType = (RadioGroup) searchCreditView.findViewById(R.id.saleType);

        CreditVO credit = new CreditVO();
        credit.setBookingNo(_bookingNo.getText().toString());
        credit.setBookingDt(_bookingDate.getText().toString());
        credit.setReceiverName(_receiverName.getText().toString());
		
		int selectedId = radioSaleType.getCheckedRadioButtonId();
		if(selectedId != -1) {
			RadioButton radioButtonSelected = (RadioButton) searchCreditView.findViewById(selectedId);
            credit.setSaleType(radioButtonSelected.getText().toString());
		} else {
			credit.setSaleType(null);
		}

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        List<CreditVO> credits = dbUtils.getCredits(credit);

        TableLayout _creditsTblLayout = (TableLayout) searchCreditView.findViewById(R.id.tbl_credits);
        TableLayout _totalTblLayout = (TableLayout) searchCreditView.findViewById(R.id.tbl_total);
        if(credits == null || credits.isEmpty()) {
            _creditsTblLayout.setVisibility(View.INVISIBLE);
            _totalTblLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        else {
            _creditsTblLayout.setVisibility(View.VISIBLE);
            _totalTblLayout.setVisibility(View.VISIBLE);
            populateCredits(credits);
            progressDialog.dismiss();
        }

    }
}

