package com.gbm.activity.fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.UserVO;

import java.util.List;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class SearchUserFragment extends Fragment {

    private static final String TAG = "SearchUserFragment";
    private String roleSelected;
    private View searchUserView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchUserView = inflater.inflate(R.layout.fragment_user_search, container, false);

        Spinner _roleSpinner = (Spinner) searchUserView.findViewById(R.id.spinner_role);

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

        Button _searchButton = (Button) searchUserView.findViewById(R.id.btn_search);

        _searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser(false);
            }
        });

        Button _addButton = (Button) searchUserView.findViewById(R.id.btn_add);

        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddUserFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        searchUser(true);

        return searchUserView;
    }

    private void populateUsers(List<UserVO> users) {
        TableLayout _usersTblLayout = (TableLayout) searchUserView.findViewById(R.id.tbl_users);
        for (int i = 0; i < _usersTblLayout.getChildCount(); i++) {
            View child = _usersTblLayout.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        Log.d(TAG, "Populating Users...");
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        row.setBackgroundResource(R.drawable.cell_shape);

        /*ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.ic_menu_edit);
        image.setPadding(5, 5, 5, 5);
        row.addView(image);*/

        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Full Name");
        row.addView(tv);

        tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Username");
        row.addView(tv);

        _usersTblLayout.addView(row);

        for (UserVO user : users) {
            row = new TableRow(getContext());
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.cell_shape);

            /*image = new ImageView(getContext());
            image.setId((int) user.getId());
            image.setTag(user);
            image.setImageResource(R.drawable.ic_menu_edit);
            image.setPadding(5, 5, 5, 5);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("user", (UserVO) v.getTag());
                    Fragment fragment = new ViewUserFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            row.addView(image);*/

            tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD_ITALIC);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(user.getFullName());
            tv.setTag(user);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("user", (UserVO) v.getTag());
                    Fragment fragment = new ViewUserFragment();
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
            tv.setText(user.getUsername());
            row.addView(tv);

            _usersTblLayout.addView(row);
        }
    }

    public void searchUser(boolean defaultSearch) {
        Log.d(TAG, "Searching User...");

        EditText _fullNameText = (EditText) searchUserView.findViewById(R.id.input_fullName);
        String fullName = _fullNameText.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching...");
        progressDialog.show();
        GBMDBUtils dbUtils = new GBMDBUtils(getContext());
        List<UserVO> users;
        if(defaultSearch) {
            users = dbUtils.getAllUsers();
        }
        else {
            users = dbUtils.getUser(null, fullName, roleSelected);
        }

        TableLayout _usersTblLayout = (TableLayout) searchUserView.findViewById(R.id.tbl_users);
        if(users == null || users.isEmpty()) {
            _usersTblLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        else {
            _usersTblLayout.setVisibility(View.VISIBLE);
            populateUsers(users);
            progressDialog.dismiss();
        }

    }
}

