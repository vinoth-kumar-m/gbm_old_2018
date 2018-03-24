package com.gbm.activity;

/**
 * Creted by Vinoth on 9/7/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gbm.db.GBMDBUtils;
import com.gbm.utils.SessionManager;
import com.gbm.vo.UserVO;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;

    GBMDBUtils dbUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dbUtils = new GBMDBUtils(getApplicationContext());

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        Log.d(TAG, "Login");

        boolean valid = true;

        _loginButton.setEnabled(false);

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError("Enter a valid username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Enter a valid password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if(!valid) {
            //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            _loginButton.setEnabled(true);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        UserVO user = dbUtils.validate(username, password);

        if(user == null) {
            Log.d(TAG, "Login Failed");
            Toast.makeText(getBaseContext(), "Incorrect username / password", Toast.LENGTH_LONG).show();
            _loginButton.setEnabled(true);
            progressDialog.dismiss();
            return;
        }
        else {
            Log.d(TAG, "Login Successful");

            SessionManager session =  new SessionManager(getApplicationContext());
            session.createLoginSession(user.getFullName(), user.getRole());
            progressDialog.dismiss();

            _loginButton.setEnabled(true);
            finish();
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
        }
    }

}
