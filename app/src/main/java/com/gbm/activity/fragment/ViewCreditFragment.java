package com.gbm.activity.fragment;

import android.app.Dialog;
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
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.constants.GBMConstants;
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.CreditVO;

import java.math.BigDecimal;

/**
 * Created by Sri on 9/12/2017.
 */

public class ViewCreditFragment extends Fragment {

    private static final String TAG = "ViewCreditFragment";
    private View creditView;
    CreditVO credit;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        creditView = inflater.inflate(R.layout.fragment_credit_view, container, false);
        dbUtils = new GBMDBUtils(getContext());
        credit = (CreditVO) getArguments().getSerializable("credit");

		final RadioGroup radioSaleType = (RadioGroup) creditView.findViewById(R.id.saleType);
        final EditText _bookingNo = (EditText) creditView.findViewById(R.id.input_bookingNo);
        final EditText _bookingDate = (EditText) creditView.findViewById(R.id.input_bookingDate);
        final EditText _receiverName = (EditText) creditView.findViewById(R.id.input_receiverName);
		final EditText _productName = (EditText) creditView.findViewById(R.id.input_product);
		final EditText _quantity = (EditText) creditView.findViewById(R.id.input_quantity);
		final EditText _amount = (EditText) creditView.findViewById(R.id.input_amount);
        final EditText _vehicleNo = (EditText) creditView.findViewById(R.id.input_vehicleNo);
		
		if(GBMConstants.SALE_TYPE_CASH.equals(credit.getSaleType())) {
			radioSaleType.check(R.id.radio_cash);
        } else if(GBMConstants.SALE_TYPE_CREDIT.equals(credit.getSaleType())) {
			radioSaleType.check(R.id.radio_credit);
        }
		
		for (int i = 0; i < radioSaleType.getChildCount(); i++) {
			radioSaleType.getChildAt(i).setEnabled(false);
		}

        _bookingNo.setText(credit.getBookingNo());
        _bookingDate.setText(credit.getBookingDt());
        _receiverName.setText(credit.getReceiverName());
        _productName.setText(credit.getProductName());
		_quantity.setText(credit.getQuantity().setScale(2).toString());
		_amount.setText(credit.getAmount().setScale(2).toString());
        _vehicleNo.setText(credit.getVehicleNo());

        Button _deleteButton = (Button) creditView.findViewById(R.id.btn_delete);

        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm")
                        .setMessage("Do you want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dbUtils.deleteCredit(credit);
                                Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_LONG).show();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                Fragment fragment = new SearchCreditFragment();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        Button _closeButton = (Button) creditView.findViewById(R.id.btn_close);

        _closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SearchCreditFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return creditView;
    }
}

