package com.gbm.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.SettingsVO;

import java.math.BigDecimal;

/**
 * Created by Sri on 9/12/2017.
 */

public class ViewSettingsFragment extends Fragment {

    private static final String TAG = "ViewSettingsFragment";
    private View settingsView;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        settingsView = inflater.inflate(R.layout.fragment_settings_view, container, false);

        dbUtils = new GBMDBUtils(getContext());

        SettingsVO settings = dbUtils.getSettings();

        final EditText _invoiceNo = (EditText) settingsView.findViewById(R.id.input_invoiceNo);
        final EditText _bookingNo = (EditText) settingsView.findViewById(R.id.input_bookingNo);
        final EditText _products = (EditText) settingsView.findViewById(R.id.input_products);
        final EditText _places = (EditText) settingsView.findViewById(R.id.input_places);
        final EditText _vehicles = (EditText) settingsView.findViewById(R.id.input_vehicles);

        if(settings != null) {
            _invoiceNo.setText(settings.getInvoiceNo());
            _bookingNo.setText(settings.getBookingNo());
            _products.setText(settings.getProducts());
            _places.setText(settings.getPlaces());
            _vehicles.setText(settings.getVehicles());

        }

        final Button _saveButton = (Button) settingsView.findViewById(R.id.btn_save);

        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                if (_invoiceNo.getText().toString().isEmpty()) {
                    _invoiceNo.setError("Invoice Number required");
                    valid = false;
                } else {
                    _invoiceNo.setError(null);
                }

                if (_bookingNo.getText().toString().isEmpty()) {
                    _bookingNo.setError("Booking Number required");
                    valid = false;
                } else {
                    _bookingNo.setError(null);
                }

                if (_products.getText().toString().isEmpty()) {
                    _products.setError("Product(s) required");
                    valid = false;
                } else {
                    _products.setError(null);
                }

                if (_places.getText().toString().isEmpty()) {
                    _places.setError("Place(s) required");
                    valid = false;
                } else {
                    _places.setError(null);
                }

                if(!valid) {
                    return;
                }

                saveSettings();
            }
        });


        Button _closeButton = (Button) settingsView.findViewById(R.id.btn_close);

        _closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return settingsView;
    }

    private void saveSettings() {
        final EditText _invoiceNo = (EditText) settingsView.findViewById(R.id.input_invoiceNo);
        final EditText _bookingNo = (EditText) settingsView.findViewById(R.id.input_bookingNo);
        final EditText _products = (EditText) settingsView.findViewById(R.id.input_products);
        final EditText _places = (EditText) settingsView.findViewById(R.id.input_places);
        final EditText _vehicles = (EditText) settingsView.findViewById(R.id.input_vehicles);

        SettingsVO settings = new SettingsVO();
        settings.setInvoiceNo(_invoiceNo.getText().toString());
        settings.setBookingNo(_bookingNo.getText().toString());
        settings.setProducts(_products.getText().toString());
        settings.setPlaces(_places.getText().toString());
        settings.setVehicles(_vehicles.getText().toString());

        long id = dbUtils.saveSettings(settings);
        if(id > 0) {
            Toast.makeText(getContext(), "Settings saved successfully", Toast.LENGTH_LONG).show();
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}

