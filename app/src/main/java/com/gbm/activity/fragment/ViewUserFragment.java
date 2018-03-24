package com.gbm.activity.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.R;
import com.gbm.db.GBMDBUtils;
import com.gbm.vo.UserVO;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class ViewUserFragment extends Fragment {

    private static final String TAG = "ViewUserFragment";
    private String roleSelected;
    private View userView;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final UserVO user = (UserVO) getArguments().getSerializable("user");

        userView = inflater.inflate(R.layout.fragment_user_view, container, false);

        TextView _fullNameText = (TextView) userView.findViewById(R.id.input_fullName);
        _fullNameText.setText(user.getFullName());

        TextView _usernameText = (TextView) userView.findViewById(R.id.input_username);
        _usernameText.setText(user.getUsername());

        TextView _roleText = (TextView) userView.findViewById(R.id.input_role);
        _roleText.setText(user.getRole());

        TextView _createdDtText = (TextView) userView.findViewById(R.id.input_createdDt);
        _createdDtText.setText(user.getCreatedDt());

        Button _deleteButton = (Button) userView.findViewById(R.id.btn_delete);

        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm")
                        .setMessage("Do you want to delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dbUtils.deleteUser(user.getId());
                                Toast.makeText(getContext(), "User deleted successfully", Toast.LENGTH_LONG).show();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                Fragment fragment = new SearchUserFragment();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        Button _closeButton = (Button) userView.findViewById(R.id.btn_close);

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

        return userView;
    }

}

