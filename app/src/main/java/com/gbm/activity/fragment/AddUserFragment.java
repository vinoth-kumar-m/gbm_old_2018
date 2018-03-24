package com.gbm.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.UserVO;

import java.util.List;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class AddUserFragment extends Fragment {

    private static final String TAG = "AddUserFragment";
    private String roleSelected;
    private View addUserView;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the dropdown_item for this fragment
        addUserView = inflater.inflate(R.layout.fragment_user_add, container, false);

        Spinner _roleSpinner = (Spinner) addUserView.findViewById(R.id.spinner_role);

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(getContext(), R.array.roles, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        _roleSpinner.setAdapter(roleAdapter);

        _roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleSelected = (String) parent.getItemAtPosition(position);
                Log.v("Role Selected: ", roleSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                roleSelected = (String) parent.getItemAtPosition(0);
                Log.v("Role Selected: ", roleSelected);
            }
        });

        Button _saveButton = (Button) addUserView.findViewById(R.id.btn_save);

        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        Button _closeButton = (Button) addUserView.findViewById(R.id.btn_close);

        _closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SearchUserFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        dbUtils = new GBMDBUtils(getContext());

        return addUserView;
    }

    private void saveUser() {
        boolean valid = true;
        EditText _fullNameText = (EditText) addUserView.findViewById(R.id.input_fullName);
        EditText _usernameText = (EditText) addUserView.findViewById(R.id.input_username);
        EditText _passwordText = (EditText) addUserView.findViewById(R.id.input_password);
        EditText _reconfirmText = (EditText) addUserView.findViewById(R.id.input_confirm_password);
        Spinner _roleSpinner = (Spinner) addUserView.findViewById(R.id.spinner_role);

        String fullName = _fullNameText.getText().toString();
        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();
        String reconfirm = _reconfirmText.getText().toString();

        if (fullName.isEmpty()) {
            _fullNameText.setError("Name is required");
            valid = false;
        } else {
            _fullNameText.setError(null);
        }

        if (username.isEmpty()) {
            _usernameText.setError("Username is required");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Password is required");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reconfirm.isEmpty()) {
            _reconfirmText.setError("Re-confirm Password is required");
            valid = false;
        } else {
            _reconfirmText.setError(null);
        }

        if(!password.isEmpty() && !reconfirm.isEmpty() && !password.equals(reconfirm)) {
            Toast.makeText(getContext(), "Password & Reconfirm does not match", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (roleSelected.isEmpty()) {
            ((TextView) _roleSpinner.getSelectedView()).setError("Select the role");
            valid = false;
        } else {
            ((TextView) _roleSpinner.getSelectedView()).setError(null);
        }

        if(!valid) {
            return;
        }
        else {
            List<UserVO> users = dbUtils.getUser(username, null, null);
            if(!users.isEmpty()) {
                Toast.makeText(getContext(), "Username already exists", Toast.LENGTH_LONG).show();
                return;
            }
            UserVO user = new UserVO();
            user.setFullName(fullName);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(roleSelected);
            long id = dbUtils.saveUser(user);
            if(id > 0) {
                Toast.makeText(getContext(), "User saved successfully", Toast.LENGTH_LONG).show();
                Fragment fragment = new SearchUserFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }
}

