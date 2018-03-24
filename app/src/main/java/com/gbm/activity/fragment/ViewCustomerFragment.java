package com.gbm.activity.fragment;

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
import com.gbm.vo.CustomerVO;
import com.gbm.vo.InvoiceVO;
import com.gbm.vo.ProductVO;

import java.io.File;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class ViewCustomerFragment extends Fragment {

    private static final String TAG = "ViewCustomerFragment";
    private View customerView;
    CustomerVO customer;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        customerView = inflater.inflate(R.layout.fragment_customer_view, container, false);
        dbUtils = new GBMDBUtils(getContext());
        customer = (CustomerVO) getArguments().getSerializable("customer");

        final EditText _receiverName = (EditText) customerView.findViewById(R.id.input_receiverName);
        final EditText _receiverAddress = (EditText) customerView.findViewById(R.id.input_receiverAddress);
        final EditText _phone = (EditText) customerView.findViewById(R.id.input_receiverPhoneNo);
        final EditText _gstin = (EditText) customerView.findViewById(R.id.input_receiverGSTIN);
        final EditText _aadhar = (EditText) customerView.findViewById(R.id.input_receiverAadharNo);
        final EditText _pan = (EditText) customerView.findViewById(R.id.input_receiverPAN);

        _receiverName.setText(customer.getReceiverName());
        _receiverAddress.setText(customer.getAddress());
        _phone.setText(customer.getPhone());
        _gstin.setText(customer.getGstin());
        _aadhar.setText(customer.getAadhar());
        _pan.setText(customer.getPan());

        Button _deleteButton = (Button) customerView.findViewById(R.id.btn_delete);

        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm")
                        .setMessage("Do you want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dbUtils.deleteCustomer(customer);
                                Toast.makeText(getContext(), "Customer deleted successfully", Toast.LENGTH_LONG).show();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                Fragment fragment = new SearchCustomerFragment();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        Button _closeButton = (Button) customerView.findViewById(R.id.btn_close);

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

        return customerView;
    }

}

