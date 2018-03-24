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
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.CustomerVO;
import com.gbm.vo.InvoiceVO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class SearchCustomerFragment extends Fragment {

    private static final String TAG = "SearchCustomerFragment";
    private View searchCustomerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the dropdown_item for this fragment
        searchCustomerView = inflater.inflate(R.layout.fragment_customer_search, container, false);

        Button _addButton = (Button) searchCustomerView.findViewById(R.id.btn_add);

        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddCustomerFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching...");
        progressDialog.show();
        GBMDBUtils dbUtils = new GBMDBUtils(getContext());
        List<CustomerVO> customers = dbUtils.getCustomers();

        TableLayout _customersTblLayout = (TableLayout) searchCustomerView.findViewById(R.id.tbl_customers);
        if(customers == null || customers.isEmpty()) {
            _customersTblLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        else {
            _customersTblLayout.setVisibility(View.VISIBLE);
            populateCustomers(customers);
            progressDialog.dismiss();
        }

        return searchCustomerView;
    }

    private void populateCustomers(List<CustomerVO> customers) {
        TableLayout _customersTblLayout = (TableLayout) searchCustomerView.findViewById(R.id.tbl_customers);
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

        tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Phone");
        row.addView(tv);

        _customersTblLayout.addView(row);

        for (CustomerVO customer : customers) {
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
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("customer", (CustomerVO) v.getTag());
                    Fragment fragment = new ViewCustomerFragment();
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
            tv.setText(customer.getPhone());
            row.addView(tv);

            _customersTblLayout.addView(row);
        }
    }
}

