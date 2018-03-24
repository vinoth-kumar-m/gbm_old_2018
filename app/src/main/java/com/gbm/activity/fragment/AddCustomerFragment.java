package com.gbm.activity.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.constants.GBMConstants;
import com.gbm.db.GBMDBUtils;
import com.gbm.utils.PDFCreator;
import com.gbm.utils.PDFCreatorFactory;
import com.gbm.vo.CustomerVO;
import com.gbm.vo.InvoiceVO;
import com.gbm.vo.ProductVO;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class AddCustomerFragment extends Fragment {

    private static final String TAG = "AddCustomerFragment";
    private View addCustomerView;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the dropdown_item for this fragment
        addCustomerView = inflater.inflate(R.layout.fragment_customer_add, container, false);

        dbUtils = new GBMDBUtils(getContext());

        final Button _saveButton = (Button) addCustomerView.findViewById(R.id.btn_save);

        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                final EditText _receiverName = (EditText) addCustomerView.findViewById(R.id.input_receiverName);
                final EditText _receiverAddress = (EditText) addCustomerView.findViewById(R.id.input_receiverAddress);
                final EditText _phone = (EditText) addCustomerView.findViewById(R.id.input_receiverPhoneNo);
                final EditText _gstin = (EditText) addCustomerView.findViewById(R.id.input_receiverGSTIN);
                final EditText _aadhar = (EditText) addCustomerView.findViewById(R.id.input_receiverAadharNo);
                final EditText _pan = (EditText) addCustomerView.findViewById(R.id.input_receiverPAN);

                if (_receiverName.getText().toString().isEmpty()) {
                    _receiverName.setError("Receiver / Buyer Name is required");
                    valid = false;
                } else {
                    _receiverName.setError(null);
                }

                if (_receiverAddress.getText().toString().isEmpty()) {
                    _receiverAddress.setError("Address is required");
                    valid = false;
                } else {
                    _receiverAddress.setError(null);
                }

                if (_phone.getText().toString().isEmpty()) {
                    _phone.setError("Phone is required");
                    valid = false;
                } else {
                    _phone.setError(null);
                }

                if (!valid) {
                    return;
                }

                CustomerVO customer = new CustomerVO();

                customer.setReceiverName(_receiverName.getText().toString());
                customer.setAddress(_receiverAddress.getText().toString());
                customer.setPhone(_phone.getText().toString());
                customer.setGstin(_gstin.getText().toString());
                customer.setAadhar(_aadhar.getText().toString());
                customer.setPan(_pan.getText().toString());


                long id = dbUtils.saveCustomer(customer);
                if (id > 0) {
                    Toast.makeText(getContext(), "Customer saved successfully", Toast.LENGTH_LONG).show();
                    Fragment fragment = new SearchCustomerFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        Button _closeButton = (Button) addCustomerView.findViewById(R.id.btn_close);

        _closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SearchCustomerFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return addCustomerView;
    }

}

