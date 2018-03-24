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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.CreditVO;
import com.gbm.vo.CustomerVO;
import com.gbm.vo.SettingsVO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Vinoth on 9/12/2017.
 */

public class AddCreditFragment extends Fragment {

    private static final String TAG = "AddCreditFragment";
    private View addCreditView;
    private GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addCreditView = inflater.inflate(R.layout.fragment_credit_add, container, false);
        dbUtils = new GBMDBUtils(getContext());

        final RadioGroup _radioSaleType = (RadioGroup) addCreditView.findViewById(R.id.saleType);
        final EditText _bookingNo = (EditText) addCreditView.findViewById(R.id.input_bookingNo);
        final EditText _bookingDate = (EditText) addCreditView.findViewById(R.id.input_bookingDate);
        final EditText _receiverName = (EditText) addCreditView.findViewById(R.id.input_receiverName);
        final AutoCompleteTextView _productName = (AutoCompleteTextView) addCreditView.findViewById(R.id.input_product);
        final EditText _quantity = (EditText) addCreditView.findViewById(R.id.input_quantity);
        final EditText _amount = (EditText) addCreditView.findViewById(R.id.input_amount);
        final EditText _vehicleNo = (EditText) addCreditView.findViewById(R.id.input_vehicleNo);

        final Calendar myCalendar = Calendar.getInstance();
        _bookingDate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(myCalendar.getTime()));
        _quantity.setOnFocusChangeListener(getFocusChangeListener());
        _amount.setOnFocusChangeListener(getFocusChangeListener());

        final SettingsVO settings = dbUtils.getSettings();

        if(settings != null) {
            _productName.setAdapter(new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, settings.getProducts().split(",")));
            _productName.setDropDownBackgroundResource(R.color.white);

        }

        final Button _saveButton = (Button) addCreditView.findViewById(R.id.btn_save);

        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                RadioButton _lastRadioBtn = (RadioButton) addCreditView.findViewById(R.id.radio_credit);
                String saleType = null;
                int selectedId = _radioSaleType.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton radioButtonSelected = (RadioButton) addCreditView.findViewById(selectedId);
                    saleType = radioButtonSelected.getText().toString();
                    _lastRadioBtn.setError(null);
                } else {
                    _lastRadioBtn.setError("Choose Select Type");
                    valid = false;
                }

                if (_bookingNo.getText().toString().isEmpty()) {
                    _bookingNo.setError("Booking Number is required");
                    valid = false;
                } else {
                    _bookingNo.setError(null);
                }

                if (_receiverName.getText().toString().isEmpty()) {
                    _receiverName.setError("Receiver / Buyer Name is required");
                    valid = false;
                } else {
                    _receiverName.setError(null);
                }

                if (_productName.getText().toString().isEmpty()) {
                    _productName.setError("Product Name is required");
                    valid = false;
                } else {
                    _productName.setError(null);
                }

                if (_quantity.getText().toString().isEmpty()) {
                    _quantity.setError("Quantity is required");
                    valid = false;
                } else {
                    _quantity.setError(null);
                }

                if (_amount.getText().toString().isEmpty()) {
                    _amount.setError("Amount is required");
                    valid = false;
                } else {
                    _amount.setError(null);
                }

                if (_vehicleNo.getText().toString().isEmpty()) {
                    _vehicleNo.setError("Vehicle No is required");
                    valid = false;
                } else {
                    _vehicleNo.setError(null);
                }

                if (!valid) {
                    return;
                }

                CreditVO credit = new CreditVO();
                credit.setSaleType(saleType);
                credit.setBookingNo(_bookingNo.getText().toString());
                credit.setBookingDt(_bookingDate.getText().toString());
                credit.setReceiverName(_receiverName.getText().toString());
                credit.setProductName(_productName.getText().toString());
                credit.setQuantity(new BigDecimal(_quantity.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                credit.setAmount(new BigDecimal(_amount.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                credit.setVehicleNo(_vehicleNo.getText().toString());

                long id = dbUtils.saveCredit(credit);
                if (id > 0) {
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_LONG).show();
                    Fragment fragment = new SearchCreditFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        Button _closeButton = (Button) addCreditView.findViewById(R.id.btn_close);

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
                            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            row.setBackgroundResource(R.drawable.cell_shape);

                            TextView tv = new TextView(getContext());
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setTypeface(Typeface.DEFAULT_BOLD);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                            tv.setTextColor(getResources().getColor(R.color.primary_dark));
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText("Name");
                            row.addView(tv);

                            _customersTblLayout.addView(row);

                            for (final CustomerVO customer : customers) {
                                row = new TableRow(getContext());
                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                row.setBackgroundResource(R.drawable.cell_shape);

                                tv = new TextView(getContext());
                                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
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

        return addCreditView;
    }

    private View.OnFocusChangeListener getFocusChangeListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    EditText _amount = (EditText) view;
                    if (!_amount.getText().toString().isEmpty()) {
                        BigDecimal amount = new BigDecimal(_amount.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        _amount.setText(amount.toString());
                    }
                }
            }
        };
    }
}

