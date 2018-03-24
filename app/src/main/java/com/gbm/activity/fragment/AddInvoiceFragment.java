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
import com.gbm.utils.NamesAdapter;
import com.gbm.utils.PDFCreator;
import com.gbm.utils.PDFCreatorFactory;
import com.gbm.vo.CustomerVO;
import com.gbm.vo.InvoiceVO;
import com.gbm.vo.ProductVO;
import com.gbm.vo.SettingsVO;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sri on 9/12/2017.
 */

public class AddInvoiceFragment extends Fragment {

    private static final String TAG = "AddInvoiceFragment";
    private String roleSelected;
    private View addInvoiceView;
    private GBMDBUtils dbUtils;
    private List<ProductVO> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        products = new ArrayList<ProductVO>();
        // Inflate the dropdown_item for this fragment
        addInvoiceView = inflater.inflate(R.layout.fragment_invoice_add, container, false);

        dbUtils = new GBMDBUtils(getContext());
        final EditText _invoiceNo = (EditText) addInvoiceView.findViewById(R.id.input_invoiceNo);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText _invoiceDate = (EditText) addInvoiceView.findViewById(R.id.input_invoiceDate);
        _invoiceDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));

        final EditText _supplyDate = (EditText) addInvoiceView.findViewById(R.id.input_supplyDate);
        _supplyDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
        _supplyDate.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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

        final AutoCompleteTextView _addressText = (AutoCompleteTextView) addInvoiceView.findViewById(R.id.input_receiverAddress);
        final AutoCompleteTextView _vehicleNo = (AutoCompleteTextView) addInvoiceView.findViewById(R.id.input_vehicleNo);
        final SettingsVO settings = dbUtils.getSettings();
        if(settings != null) {
            _invoiceNo.setText(String.format("%07d", Integer.valueOf(settings.getInvoiceNo())));
            if(settings.getPlaces() != null) {
                _addressText.setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, settings.getPlaces().split(",")));
                _addressText.setDropDownBackgroundResource(R.color.white);
            }
            if(settings.getVehicles() != null) {
                /*_vehicleNo.setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, settings.getVehicles().split(",")));*/

                NamesAdapter namesAdapter = new NamesAdapter(
                        getContext(),
                        R.layout.fragment_invoice_add,
                        R.id.lbl_name,
                        Arrays.asList(settings.getVehicles().split(","))
                );
                //set adapter into listStudent
                _vehicleNo.setAdapter(namesAdapter);
                //_vehicleNo.showDropDown();
                _vehicleNo.setDropDownBackgroundResource(R.color.white);
            }
        }

        final Button _saveButton = (Button) addInvoiceView.findViewById(R.id.btn_save);

        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                EditText _receiverName = (EditText) addInvoiceView.findViewById(R.id.input_receiverName);

                if (_receiverName.getText().toString().isEmpty()) {
                    _receiverName.setError("Receiver / Buyer Name is required");
                    valid = false;
                } else {
                    _receiverName.setError(null);
                }

                EditText _receiverAddress = (EditText) addInvoiceView.findViewById(R.id.input_receiverAddress);

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

                if(!valid) {
                    return;
                }

                saveInvoice();
            }
        });

        Button _addProductButton = (Button) addInvoiceView.findViewById(R.id.btn_addProduct);
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        _addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Dialog productDialog = new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                        .setTitle("Add Product / Commodity")
                        .setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_product_add, null))
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.no, null).create();

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

                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                productDialog.dismiss();
                                _saveButton.requestFocus();
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            }
                        });

                        _productText.requestFocus();


                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });
                productDialog.show();
            }
        });

        Button _closeButton = (Button) addInvoiceView.findViewById(R.id.btn_close);

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

        final EditText _receiverName = (EditText) addInvoiceView.findViewById(R.id.input_receiverName);
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
                                        final EditText _receiverAddress = (EditText) addInvoiceView.findViewById(R.id.input_receiverAddress);
                                        final EditText _phone = (EditText) addInvoiceView.findViewById(R.id.input_receiverPhoneNo);
                                        final EditText _gstin = (EditText) addInvoiceView.findViewById(R.id.input_receiverGSTIN);
                                        final EditText _aadhar = (EditText) addInvoiceView.findViewById(R.id.input_receiverAadharNo);
                                        final EditText _pan = (EditText) addInvoiceView.findViewById(R.id.input_receiverPAN);

                                        _receiverName.setText(customer.getReceiverName());
                                        _receiverAddress.setText(customer.getAddress());
                                        _phone.setText(customer.getPhone());
                                        _gstin.setText(customer.getGstin());
                                        _aadhar.setText(customer.getAadhar());
                                        _pan.setText(customer.getPan());
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

        return addInvoiceView;
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

    private void addProduct(ProductVO product) {
        products.add(product);
        populateProducts();
    }

    private void calculateInvoiceTotal() {
        BigDecimal totalBeforeTax = BigDecimal.ZERO;
        BigDecimal sGST = BigDecimal.ZERO;
        BigDecimal cGST = BigDecimal.ZERO;
        BigDecimal totalAfterTax = BigDecimal.ZERO;
        if(products.size() > 0) {
            for (ProductVO product : products) {
                totalBeforeTax = totalBeforeTax.add(product.getTotal());
            }
            sGST = totalBeforeTax.multiply(new BigDecimal(0.025)).setScale(2, BigDecimal.ROUND_HALF_UP);
            cGST = totalBeforeTax.multiply(new BigDecimal(0.025)).setScale(2, BigDecimal.ROUND_HALF_UP);
            totalAfterTax = totalAfterTax.add(totalBeforeTax);
            totalAfterTax = totalAfterTax.add(sGST);
            totalAfterTax = totalAfterTax.add(cGST);
        }

        EditText _totalBeforeTax = (EditText) addInvoiceView.findViewById(R.id.input_totalBeforeTax);
        EditText _sGST = (EditText) addInvoiceView.findViewById(R.id.input_sGST);
        EditText _cGST = (EditText) addInvoiceView.findViewById(R.id.input_cGST);
        EditText _totalAfterTax = (EditText) addInvoiceView.findViewById(R.id.input_totalAfterTax);

        _totalBeforeTax.setText(totalBeforeTax.toString());
        _sGST.setText(sGST.toString());
        _cGST.setText(cGST.toString());
        _totalAfterTax.setText(totalAfterTax.toString());
    }

    private void populateProducts() {
        TableLayout _productsTblLayout = (TableLayout) addInvoiceView.findViewById(R.id.tbl_products);
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
        calculateInvoiceTotal();
    }

    private void saveInvoice() {
        final EditText _invoiceNo = (EditText) addInvoiceView.findViewById(R.id.input_invoiceNo);
        final EditText _invoiceDate = (EditText) addInvoiceView.findViewById(R.id.input_invoiceDate);
        final EditText _supplyDate = (EditText) addInvoiceView.findViewById(R.id.input_supplyDate);
        final EditText _vehicleNo = (EditText) addInvoiceView.findViewById(R.id.input_vehicleNo);
        final EditText _receiverName = (EditText) addInvoiceView.findViewById(R.id.input_receiverName);
        final EditText _receiverAddress = (EditText) addInvoiceView.findViewById(R.id.input_receiverAddress);
        final EditText _phone = (EditText) addInvoiceView.findViewById(R.id.input_receiverPhoneNo);
        final EditText _gstin = (EditText) addInvoiceView.findViewById(R.id.input_receiverGSTIN);
        final EditText _aadhar = (EditText) addInvoiceView.findViewById(R.id.input_receiverAadharNo);
        final EditText _pan = (EditText) addInvoiceView.findViewById(R.id.input_receiverPAN);
        final EditText _totalBeforeTax = (EditText) addInvoiceView.findViewById(R.id.input_totalBeforeTax);
        final EditText _sGST = (EditText) addInvoiceView.findViewById(R.id.input_sGST);
        final EditText _cGST = (EditText) addInvoiceView.findViewById(R.id.input_cGST);
        final EditText _totalAfterTax = (EditText) addInvoiceView.findViewById(R.id.input_totalAfterTax);

        InvoiceVO invoice = new InvoiceVO();
        invoice.setInvoiceNo(_invoiceNo.getText().toString());
        invoice.setInvoiceDt(_invoiceDate.getText().toString());
        invoice.setSupplyDt(_supplyDate.getText().toString());
        invoice.setVehicleNo(_vehicleNo.getText().toString());
        invoice.setReceiverName(_receiverName.getText().toString());
        invoice.setAddress(_receiverAddress.getText().toString());
        invoice.setPhone(_phone.getText().toString());
        invoice.setGstin(_gstin.getText().toString());
        invoice.setAadhar(_aadhar.getText().toString());
        invoice.setPan(_pan.getText().toString());
        invoice.setProducts(products);
        invoice.setTotalBeforeTax(new BigDecimal(_totalBeforeTax.getText().toString()));
        invoice.setsGST(new BigDecimal(_sGST.getText().toString()));
        invoice.setcGST(new BigDecimal(_cGST.getText().toString()));
        invoice.setTotalAfterTax(new BigDecimal(_totalAfterTax.getText().toString()));

        long id = dbUtils.saveInvoice(invoice);
        if(id > 0) {
            Toast.makeText(getContext(), "Invoice saved successfully", Toast.LENGTH_LONG).show();
            Fragment fragment = new SearchInvoiceFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            try {
                PDFCreator pdfCreator = PDFCreatorFactory.getInstance(GBMConstants.INVOICE);
                File file = pdfCreator.createPDF(getContext(), invoice.getInvoiceNo() + ".pdf", invoice);
                pdfCreator.viewPDF(getContext(), file);
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }
        }
    }
}

