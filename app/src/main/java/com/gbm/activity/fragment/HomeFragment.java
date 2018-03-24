package com.gbm.activity.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.LoginActivity;
import com.gbm.activity.R;
import com.gbm.db.GBMDBUtils;
import com.gbm.utils.BackupAndRestore;
import com.gbm.utils.SessionManager;
import com.gbm.vo.BookingVO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Creted by Vinoth on 9/12/2017.
 */

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private SessionManager session;
    private View homeView;
    GBMDBUtils dbUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        session =  new SessionManager(getContext());
        session.checkLogin();

        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        TableLayout _iconsTblLayout = (TableLayout) homeView.findViewById(R.id.tbl_icons);
        TableRow row;
        Button button;
        if("Administrator".equals(session.getRole())) {

            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.cell_shape);

            button = new Button(getContext());
            button.setText("Users");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_users, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SearchUserFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Users");
                }
            });

            row.addView(button);

            button = new Button(getContext());
            button.setText("Backup");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_backup, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm")
                            .setMessage("Are you sure to take backup?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    BackupAndRestore.backup(getContext());
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });
            row.addView(button);


            _iconsTblLayout.addView(row);

            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.color.white);

            button = new Button(getContext());
            button.setText("Restore");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_restore, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm")
                            .setMessage("Current data will be restored with backup data. Do you want to continue?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    BackupAndRestore.restore(getContext());
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });

            row.addView(button);

            button = new Button(getContext());
            button.setText("Customers");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_customers, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SearchCustomerFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Customers");
                }
            });

            row.addView(button);

            _iconsTblLayout.addView(row);

            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.color.white);

            button = new Button(getContext());
            button.setText("Settings");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_settings, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new ViewSettingsFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Settings");
                }
            });

            row.addView(button);

            row.addView(getLogoutButton());

            _iconsTblLayout.addView(row);

        } else if("Invoice".equals(session.getRole())) {
            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.cell_shape);

            button = new Button(getContext());
            button.setText("Invoice");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_invoice, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SearchInvoiceFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Invoice");
                }
            });

            row.addView(button);

            button = new Button(getContext());
            button.setText("Invoice Report");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_report, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new ReportInvoiceFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Invoice Report");
                }
            });

            row.addView(button);

            _iconsTblLayout.addView(row);

            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.color.white);

            row.addView(getLogoutButton());

            _iconsTblLayout.addView(row);

        } else if("Booking".equals(session.getRole())) {
            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.cell_shape);

            button = new Button(getContext());
            button.setText("Invoice");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_invoice, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SearchInvoiceFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Invoice");
                }
            });

            row.addView(button);

            button = new Button(getContext());
            button.setText("Booking");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_booking, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SearchBookingFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Booking");
                }
            });

            row.addView(button);

            _iconsTblLayout.addView(row);

            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.cell_shape);

            button = new Button(getContext());
            button.setText("Credits");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_credit, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SearchCreditFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Credits");
                }
            });

            row.addView(button);

            button = new Button(getContext());
            button.setText("Invoice Report");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_report, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new ReportInvoiceFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Invoice Report");
                }
            });

            row.addView(button);

            _iconsTblLayout.addView(row);

            row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.color.white);

            button = new Button(getContext());
            button.setText("Cash / Credit Report");
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_report, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new ReportCreditFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    setTitle("Cash / Credit Report");
                }
            });

            row.addView(button);

            row.addView(getLogoutButton());

            _iconsTblLayout.addView(row);
        }

        return homeView;
    }

    private Button getLogoutButton() {
        Button button = new Button(getContext());
        button.setText("Logout");
        button.setBackgroundColor(getResources().getColor(R.color.white));
        button.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_logout, 0, 0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session != null) {
                    session.logoutUser();
                }
                Toast.makeText(getContext(), "User logged out successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getActivity().finish();
                getContext().startActivity(intent);
            }
        });
        return button;
    }

    private void setTitle(String title) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }

}

