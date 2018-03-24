package com.gbm.activity.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.CustomerVO;
import com.gbm.vo.InvoiceVO;
import com.gbm.vo.ProductVO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sri on 9/12/2017.
 */

public class SearchInvoiceFragment extends Fragment {

    private static final String TAG = "SearchInvoiceFragment";
    private View searchInvoiceView;
    GBMDBUtils dbUtils;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the dropdown_item for this fragment
        searchInvoiceView = inflater.inflate(R.layout.fragment_invoice_search, container, false);
        dbUtils = new GBMDBUtils(getContext());
        Button _searchButton = (Button) searchInvoiceView.findViewById(R.id.btn_search);

        _searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInvoice();
            }
        });

        Button _addButton = (Button) searchInvoiceView.findViewById(R.id.btn_add);

        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddInvoiceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();

        final EditText _invoiceDate = (EditText) searchInvoiceView.findViewById(R.id.input_invoiceDate);
        //_invoiceDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
        _invoiceDate.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    _invoiceDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                }
            };

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText _supplyDate = (EditText) searchInvoiceView.findViewById(R.id.input_supplyDate);
        _supplyDate.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    _supplyDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                }
            };

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText _receiverName = (EditText) searchInvoiceView.findViewById(R.id.input_receiverName);
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

        searchInvoice();

        return searchInvoiceView;
    }

    private void populateInvoices(List<InvoiceVO> invoices) {
        TableLayout _invoicesTblLayout = (TableLayout) searchInvoiceView.findViewById(R.id.tbl_invoices);
        for (int i = 0; i < _invoicesTblLayout.getChildCount(); i++) {
            View child = _invoicesTblLayout.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        Log.d(TAG, "Populating Invoices...");
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        row.setBackgroundResource(R.drawable.cell_shape);


        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Invoice No");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Receiver Name");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Amount");
        row.addView(tv);

        _invoicesTblLayout.addView(row);

        for (InvoiceVO invoice : invoices) {
            row = new TableRow(getContext());
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.cell_shape);

            tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD_ITALIC);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(invoice.getInvoiceNo());
            tv.setTag(invoice);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("invoice", (InvoiceVO) v.getTag());
                    Fragment fragment = new ViewInvoiceFragment();
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
            tv.setText(invoice.getReceiverName());
            row.addView(tv);

            tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(String.valueOf(invoice.getTotalAfterTax().setScale(2)));
            row.addView(tv);

            _invoicesTblLayout.addView(row);
        }
    }

    public void searchInvoice() {
        Log.d(TAG, "Searching Invoice...");

        final EditText _invoiceNo = (EditText) searchInvoiceView.findViewById(R.id.input_invoiceNo);
        final EditText _invoiceDate = (EditText) searchInvoiceView.findViewById(R.id.input_invoiceDate);
        final EditText _supplyDate = (EditText) searchInvoiceView.findViewById(R.id.input_supplyDate);
        final EditText _receiverName = (EditText) searchInvoiceView.findViewById(R.id.input_receiverName);

        InvoiceVO invoice = new InvoiceVO();
        invoice.setInvoiceNo(_invoiceNo.getText().toString());
        invoice.setInvoiceDt(_invoiceDate.getText().toString());
        invoice.setSupplyDt(_supplyDate.getText().toString());
        invoice.setReceiverName(_receiverName.getText().toString());

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        List<InvoiceVO> invoices = dbUtils.getInvoices(invoice);

        TableLayout _invoicesTblLayout = (TableLayout) searchInvoiceView.findViewById(R.id.tbl_invoices);
        if(invoices == null || invoices.isEmpty()) {
            _invoicesTblLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        else {
            _invoicesTblLayout.setVisibility(View.VISIBLE);
            populateInvoices(invoices);
            progressDialog.dismiss();
        }
    }
}

