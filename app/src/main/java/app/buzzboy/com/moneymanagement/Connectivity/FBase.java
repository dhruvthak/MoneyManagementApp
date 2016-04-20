package app.buzzboy.com.moneymanagement.Connectivity;

import android.content.Context;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import app.buzzboy.com.moneymanagement.Utils.Constants;
import app.buzzboy.com.moneymanagement.Utils.LogUtils;

/**
 * Created by Dhruv Thakkar on 4/18/2016.
 */
public class FBase extends android.app.Application {

    public static Firebase myFireBRef;
    public static String status;


    public static void FirebaseInstance(Context ctx) {

        Firebase.setAndroidContext(ctx);
        status = null;
        myFireBRef = new Firebase(Constants.myFireBRef);

    }

    public String FireBaseAddUser(String user, String pass){

        myFireBRef.createUser(user, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {

            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                status = "ok";
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                status = "error";
            }
        });
        LogUtils.log("status",status);
        return status;

    }
}