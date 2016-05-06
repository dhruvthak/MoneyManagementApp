package app.buzzboy.com.moneymanagement.Ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import app.buzzboy.com.moneymanagement.R;
import app.buzzboy.com.moneymanagement.Utils.Constants;
import app.buzzboy.com.moneymanagement.Utils.DialogContainer;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    View submit_btn;
    EditText fgt_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        submit_btn = findViewById(R.id.forgot_pass_submit);
        submit_btn.setOnClickListener(this);
        fgt_email = (EditText) findViewById(R.id.forgot_email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_pass_submit:
                Firebase ref = new Firebase(Constants.FIREBASE_URL);

                ref.resetPassword(fgt_email.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        DialogContainer dc;
                        // password reset email sent
                        dc = new DialogContainer(ForgotPassword.this, R.string.check_mail);
                        dc.show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // error encountered
                        firebaseError.getMessage();

                        DialogContainer dc;
                        switch (firebaseError.getCode()) {
                            case FirebaseError.USER_DOES_NOT_EXIST:
                                // handle a non existing user
                                dc = new DialogContainer(ForgotPassword.this, R.string.user_does_not_exist);
                                dc.show();
                                break;
                            default:
                                dc = new DialogContainer(ForgotPassword.this, R.string.some_error);
                                dc.show();
                                // handle other errors
                                break;
                        }
                    }
                });
        }
    }
}