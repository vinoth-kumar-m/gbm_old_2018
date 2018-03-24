package com.gbm.activity.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.constants.GBMConstants;
import com.gbm.db.GBMDBUtils;
import com.gbm.utils.PDFCreator;
import com.gbm.utils.PDFCreatorFactory;
import com.gbm.vo.InvoiceVO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class ReportInvoiceFragment extends Fragment {

    private static final String TAG = "ReportInvoiceFragment";
    private String roleSelected;
    private View reportInvoiceView;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the dropdown_item for this fragment
        reportInvoiceView = inflater.inflate(R.layout.fragment_invoice_report, container, false);
        dbUtils = new GBMDBUtils(getContext());
        final Calendar myCalendar = Calendar.getInstance();

        final EditText _fromDate = (EditText) reportInvoiceView.findViewById(R.id.input_fromDate);
        _fromDate.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    _fromDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                }
            };

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText _toDate = (EditText) reportInvoiceView.findViewById(R.id.input_toDate);
        _toDate.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    _toDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                }
            };

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button _generateButton = (Button) reportInvoiceView.findViewById(R.id.btn_generate);

        _generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                if (_fromDate.getText().toString().isEmpty()) {
                    _fromDate.setError("From Date is required");
                    valid = false;
                } else {
                    _fromDate.setError(null);
                }

                if (_toDate.getText().toString().isEmpty()) {
                    _toDate.setError("From Date is required");
                    valid = false;
                } else {
                    _toDate.setError(null);
                }

                if(!valid) {
                    return;
                }

                InvoiceVO invoice = new InvoiceVO();
                invoice.setFromDate(_fromDate.getText().toString());
                invoice.setToDate(_toDate.getText().toString());
                List<InvoiceVO> invoices = dbUtils.getInvoices(invoice);

                if(invoices != null && !invoices.isEmpty()) {
                    try {
                        PDFCreator pdfCreator = PDFCreatorFactory.getInstance(GBMConstants.REPORT_INVOICE);
                        File file = pdfCreator.createPDF(getContext(),
                                new SimpleDateFormat("ddMMyyyy").format(myCalendar.getTime()) + ".pdf", invoices);
                        pdfCreator.viewPDF(getContext(), file);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getLocalizedMessage());
                    }
                } else {
                    Toast.makeText(getContext(), "No data found for the given period", Toast.LENGTH_LONG).show();
                }
            }
        });

        return reportInvoiceView;
    }
}

