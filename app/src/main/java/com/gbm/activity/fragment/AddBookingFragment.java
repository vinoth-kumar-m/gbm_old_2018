package com.gbm.activity.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.gbm.vo.BookingVO;
import com.gbm.vo.CustomerVO;
import com.gbm.vo.ProductVO;
import com.gbm.vo.SettingsVO;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Vinoth on 9/12/2017.
 */

public class AddBookingFragment extends Fragment {

    private static final String TAG = "AddBookingFragment";
    private String roleSelected;
    private View addBookingView;
    private GBMDBUtils dbUtils;
    private List<ProductVO> products;

    private boolean validate() {
        final EditText _receiverName = (EditText) addBookingView.findViewById(R.id.input_receiverName);
        final AutoCompleteTextView _receiverAddress = (AutoCompleteTextView) addBookingView.findViewById(R.id.input_receiverAddress);
        final EditText _total = (EditText) addBookingView.findViewById(R.id.input_total);
        final EditText _rent = (EditText) addBookingView.findViewById(R.id.input_rent);
        final EditText _advance = (EditText) addBookingView.findViewById(R.id.input_advance);

        boolean valid = true;

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

        if(products.isEmpty()) {
            valid = false;
            Toast.makeText(getContext(), "No Product / Commodity added", Toast.LENGTH_LONG).show();
        }

        if (_rent.getText().toString().isEmpty()) {
            _rent.setError("Rent is required");
            valid = false;
        } else {
            _rent.setError(null);
        }

        if (_advance.getText().toString().isEmpty()) {
            _advance.setError("Advance is required");
            valid = false;
        } else {
            _advance.setError(null);
        }

        if(!_advance.getText().toString().isEmpty()) {
            BigDecimal total = new BigDecimal(_total.getText().toString());
            BigDecimal advance = new BigDecimal(_advance.getText().toString());
            if (advance.compareTo(total) > 0) {
                Toast.makeText(getContext(), "Advance Amount cannot be greater than Total Amount", Toast.LENGTH_LONG).show();
                valid = false;
            }
        }
        return valid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the dropdown_item for this fragment
        addBookingView = inflater.inflate(R.layout.fragment_booking_add, container, false);
        products = new ArrayList<ProductVO>();
        dbUtils = new GBMDBUtils(getContext());

        final EditText _bookingNo = (EditText) addBookingView.findViewById(R.id.input_bookingNo);
        final EditText _bookingDate = (EditText) addBookingView.findViewById(R.id.input_bookingDate);
        final EditText _receiverName = (EditText) addBookingView.findViewById(R.id.input_receiverName);
        final AutoCompleteTextView _receiverAddress = (AutoCompleteTextView) addBookingView.findViewById(R.id.input_receiverAddress);
        final EditText _total = (EditText) addBookingView.findViewById(R.id.input_total);
        final EditText _rent = (EditText) addBookingView.findViewById(R.id.input_rent);
        final EditText _advance = (EditText) addBookingView.findViewById(R.id.input_advance);
        final EditText _phone = (EditText) addBookingView.findViewById(R.id.input_receiverPhoneNo);

        _bookingDate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()));

        final SettingsVO settings = dbUtils.getSettings();
        if(settings != null) {
            _bookingNo.setText(GBMConstants.PREFIX_BOOKING + String.format("%07d", Integer.valueOf(settings.getBookingNo())));
            _receiverAddress.setAdapter(new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, settings.getPlaces().split(",")));
            _receiverAddress.setDropDownBackgroundResource(R.color.white);
        }

        _rent.setOnFocusChangeListener(getFocusChangeListener());
        _advance.setOnFocusChangeListener(getFocusChangeListener());

        _rent.addTextChangedListener(getTextWatcher());
        _advance.addTextChangedListener(getTextWatcher());

        final Button _saveButton = (Button) addBookingView.findViewById(R.id.btn_save);

        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()) {
                    return;
                }
                saveBooking(false);
            }
        });

        final Button _savePrintButton = (Button) addBookingView.findViewById(R.id.btn_save_print);

        _savePrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()) {
                    return;
                }
                saveBooking(true);
            }
        });


        Button _addProductButton = (Button) addBookingView.findViewById(R.id.btn_addProduct);
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        _addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog productDialog = new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                        .setTitle("Add Product / Commodity")
                        .setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_product_add, null))
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.no, null)
                        .create();

                productDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        final EditText _hsnCodeText = (EditText) productDialog.findViewById(R.id.input_hsnCode);
                        final EditText _quantityText = (EditText) productDialog.findViewById(R.id.input_quantity);
                        final EditText _priceText = (EditText) productDialog.findViewById(R.id.input_price);
                        final Button okButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        final Button cancelButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                        final AutoCompleteTextView _productText = (AutoCompleteTextView) productDialog.findViewById(R.id.input_product);

                        if(settings != null) {
                            _productText.setAdapter(new ArrayAdapter<String>(getContext(),
                                    android.R.layout.simple_dropdown_item_1line, settings.getProducts().split(",")));
                            _productText.setDropDownBackgroundResource(R.color.white);

                        }

                        _quantityText.setOnFocusChangeListener(getFocusChangeListener());
                        _priceText.setOnFocusChangeListener(getFocusChangeListener());

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String product = _productText.getText().toString();
                                String hsnCode = _hsnCodeText.getText().toString();
                                String quantity = _quantityText.getText().toString();
                                String price = _priceText.getText().toString();

                                boolean valid = true;

                                if (product.isEmpty()) {
                                    _productText.setError("Name of Product / Commodity is required");
                                    valid = false;
                                } else {
                                    _productText.setError(null);
                                }

                                if (quantity.isEmpty()) {
                                    _quantityText.setError("Quantity is required");
                                    valid = false;
                                } else {
                                    _quantityText.setError(null);
                                }

                                if (price.isEmpty()) {
                                    _priceText.setError("Price is required");
                                    valid = false;
                                } else {
                                    _priceText.setError(null);
                                }

                                if(!valid){
                                    return;
                                }

                                ProductVO vo = new ProductVO();
                                vo.setName(product);
                                vo.setHsnCode(hsnCode);
                                vo.setQuantity(new BigDecimal(quantity).setScale(2, BigDecimal.ROUND_HALF_UP));
                                vo.setPrice(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP));
                                BigDecimal total = vo.getQuantity().multiply(vo.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                                vo.setTotal(total);
                                addProduct(vo);

                                productDialog.dismiss();
                                _saveButton.requestFocus();
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                productDialog.dismiss();
                                _saveButton.requestFocus();
                            }
                        });

                        _productText.requestFocus();

                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });
                productDialog.show();
            }
        });

        Button _closeButton = (Button) addBookingView.findViewById(R.id.btn_close);

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
                                        _receiverAddress.setText(customer.getAddress());
                                        _phone.setText(customer.getPhone());

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

        return addBookingView;
    }

    private void addProduct(ProductVO product) {
        products.add(product);
        populateProducts();
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
                    calculateAmount();
                }
            }
        };
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                calculateAmount();
            }
        };
    }


    private void calculateAmount() {
        final EditText _rent = (EditText) addBookingView.findViewById(R.id.input_rent);
        final EditText _total = (EditText) addBookingView.findViewById(R.id.input_total);
        final EditText _advance = (EditText) addBookingView.findViewById(R.id.input_advance);
        final EditText _balance = (EditText) addBookingView.findViewById(R.id.input_balance);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal rent = BigDecimal.ZERO;
        BigDecimal advance = BigDecimal.ZERO;

        if(products.size() > 0) {
            for (ProductVO product : products) {
                total = total.add(product.getTotal());
            }
        }

        if(!_rent.getText().toString().isEmpty()) {
            rent = new BigDecimal(_rent.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        if(!_advance.getText().toString().isEmpty()) {
            advance = new BigDecimal(_advance.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        total = total.add(rent);
        BigDecimal balance = total.subtract(advance);
        _total.setText(total.toString());
        _balance.setText(balance.toString());
    }

    private void populateProducts() {
        TableLayout _productsTblLayout = (TableLayout) addBookingView.findViewById(R.id.tbl_products);
        for (int i = 0; i < _productsTblLayout.getChildCount(); i++) {
            View child = _productsTblLayout.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        Log.d(TAG, "Populating Products...");
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.setBackgroundResource(R.drawable.cell_shape);

        ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.ic_menu_cancel);
        image.setPadding(5, 5, 5, 5);
        row.addView(image);

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
        for (ProductVO product : products) {
            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.cell_shape);

            image = new ImageView(getContext());
            image.setId(idx++);
            image.setTag(product);
            image.setImageResource(R.drawable.ic_menu_cancel);
            image.setPadding(5, 5, 5, 5);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    products.remove(v.getId());

                    Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_LONG).show();
                    populateProducts();
                }
            });
            row.addView(image);

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
            tv.setText(String.valueOf(product.getQuantity()));
            row.addView(tv);

            tv = new TextView(getContext());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(product.getPrice().toString());
            row.addView(tv);

            tv = new TextView(getContext());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(product.getTotal().toString());
            row.addView(tv);

            _productsTblLayout.addView(row);
        }
        calculateAmount();
    }

    private void saveBooking(boolean canPrintBooking) {
        final EditText _bookingNo = (EditText) addBookingView.findViewById(R.id.input_bookingNo);
        final EditText _bookingDate = (EditText) addBookingView.findViewById(R.id.input_bookingDate);
        final EditText _receiverName = (EditText) addBookingView.findViewById(R.id.input_receiverName);
        final EditText _receiverAddress = (EditText) addBookingView.findViewById(R.id.input_receiverAddress);
        final EditText _phone = (EditText) addBookingView.findViewById(R.id.input_receiverPhoneNo);
        final EditText _rent = (EditText) addBookingView.findViewById(R.id.input_rent);
        final EditText _total = (EditText) addBookingView.findViewById(R.id.input_total);
        final EditText _advance = (EditText) addBookingView.findViewById(R.id.input_advance);
        final EditText _balance = (EditText) addBookingView.findViewById(R.id.input_balance);

        BookingVO booking = new BookingVO();
        booking.setBookingNo(_bookingNo.getText().toString());
        booking.setBookingDt(_bookingDate.getText().toString());
        booking.setReceiverName(_receiverName.getText().toString());
        booking.setAddress(_receiverAddress.getText().toString());
        booking.setPhone(_phone.getText().toString());
        List<String> itemLst = new ArrayList<String>();
        for(ProductVO product : products) {
            itemLst.add(product.getName());
        }
        booking.setItem(itemLst.toString());
        booking.setProducts(products);
        booking.setRent(new BigDecimal(_rent.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
        booking.setTotal(new BigDecimal(_total.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
        booking.setAdvance(new BigDecimal(_advance.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
        booking.setBalance(new BigDecimal(_balance.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_UP));

        long id = dbUtils.saveBooking(booking);
        if(id > 0) {
            Toast.makeText(getContext(), "Booking saved successfully", Toast.LENGTH_LONG).show();
            Fragment fragment = new SearchBookingFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            if(canPrintBooking) {
                try {
                    PDFCreator pdfCreator = PDFCreatorFactory.getInstance(GBMConstants.BOOKING);
                    File file = pdfCreator.createPDF(getContext(), booking.getBookingNo() + ".pdf", booking);
                    pdfCreator.viewPDF(getContext(), file);
                } catch(Exception ex) {
                    Log.e(TAG, ex.getLocalizedMessage());
                }
            }
        }
    }
}

