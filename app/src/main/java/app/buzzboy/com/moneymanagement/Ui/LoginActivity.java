package app.buzzboy.com.moneymanagement.Ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import app.buzzboy.com.moneymanagement.Application.Person;
import app.buzzboy.com.moneymanagement.Connectivity.FBase;
import app.buzzboy.com.moneymanagement.R;
import app.buzzboy.com.moneymanagement.Utils.Constants;
import app.buzzboy.com.moneymanagement.Utils.LogUtils;
import app.buzzboy.com.moneymanagement.Utils.UserDataGrabberUtils;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mRegister;
    private View mForgotPassView;
    private View mEmailSignInView;
    Firebase myFireBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the login form.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Email Field
        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_Email);

        myFireBRef.setAndroidContext(this);
        // Register Button
        mRegister = findViewById(R.id.login_register);

        // Password Field
        mPasswordView = (EditText) findViewById(R.id.login_Password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    // Call the sign in function alternative to clicking the button
                    //attemptLogin();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == EditorInfo.IME_ACTION_NEXT) {

                }
                return false;
            }
        });

        mEmailSignInView = (Button) findViewById(R.id.login_signin);
        mEmailSignInView.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mRegister.setOnClickListener(this);
        mForgotPassView = (TextView) findViewById(R.id.login_forgotpass);
        mForgotPassView.setOnClickListener(this);

        populateAutoComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Register Button
            case R.id.login_register:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.login_signin:
                attemptLogin();
                break;
            case R.id.login_forgotpass:
                i = new Intent(this, ForgotPassword.class);
                startActivity(i);
                break;
        }
    }

    private boolean mayRequestContacts() {
        // Check for permission if the version is loew than Android M or check if the permission already accepted or ask now
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return true;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        mEmailView.setAdapter(UserDataGrabberUtils.getUserDetails(LoginActivity.this));
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Use UserDataGrabberUtility to populate Emails in drop down.
                mEmailView.setAdapter(UserDataGrabberUtils.getUserDetails(LoginActivity.this));
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Person p = new Person();
            p.setEmail(email);
            p.setPass(password);


            myFireBRef = new Firebase(Constants.myFireBRef);
            myFireBRef.child("Person").setValue(p);
            /*mAuthTask = new UserLoginTask(email, password, getApplicationContext());
            if(Build.VERSION.SDK_INT >= 11){
                mAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else{
                mAuthTask.execute((Void) null);
            }*/

        }
    }

    private boolean isEmailValid(String email) {
        // Check if Email id is valid and return result
        return email.equals("null") || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        // Check if the passwd is > 4 chrs.
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        Person p;
        FBase fobject;
        String result;
        String mail;
        String pass;
        Firebase myFireBRef;
        String status = null;

        UserLoginTask(String email, String password, Context ctx) {
            //mail = p.getEmail();
            //pass = p.getPass();
        }

        @Override
        protected Boolean doInBackground(Void... String) {
            // TODO: attempt authentication against a network service.

            try {

            } catch (Exception e) {
                LogUtils.log("Error", e.getMessage());
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Toast.makeText(getApplicationContext(), "It Worked", Toast.LENGTH_LONG).show();
            } else {
            }
        }
    }
}