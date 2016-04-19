package app.buzzboy.com.moneymanagement.Connectivity;

import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import app.buzzboy.com.moneymanagement.Ui.LoginActivity;
import app.buzzboy.com.moneymanagement.Utils.Constants;

/**
 * Created by Dhruv Thakkar on 4/18/2016.
 */
public class FireBase extends LoginActivity{

    Firebase myFireBRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFireBRef = new Firebase(Constants.myFireBRef);
        myFireBRef.createUser("", "", new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {

            }

            @Override
            public void onError(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
