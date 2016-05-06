package app.buzzboy.com.moneymanagement.Connectivity;

import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by Dhruv Thakkar on 4/18/2016.
 */
public class AppSupport extends android.app.Application {

    // Only things global to application reside in the class extended using Application
    public static Context context;
    public Firebase ref;
    public Firebase transaction_ref;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        Firebase.setAndroidContext(context);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        //ref = new Firebase(Constants.FIREBASE_URL);
    }

}