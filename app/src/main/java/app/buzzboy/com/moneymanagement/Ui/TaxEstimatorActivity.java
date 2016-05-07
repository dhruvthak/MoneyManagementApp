package app.buzzboy.com.moneymanagement.Ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

import app.buzzboy.com.moneymanagement.Application.Transaction;
import app.buzzboy.com.moneymanagement.R;

public class TaxEstimatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tax_estimator);


        String FIREBASE_URL = "https://managemymoney.firebaseio.com/";
        Firebase trans_ref = new Firebase(FIREBASE_URL);
        Firebase ref = trans_ref.child("Transactions/" + trans_ref.getAuth().getUid());
        //Map<String,Integer> m = new HashMap<String,Integer>();

        // Attach an listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //String cat = snapshot.getValue(String.class);
                //Log.d("data",cat);
                //DataSnapshot elections = snapshot.child("Transaction");
                for (DataSnapshot data : snapshot.getChildren()) {
                    String electionName = data.toString();
                    Log.d("Value", electionName);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("failed", firebaseError.toString());
                //System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void calculate_tax(List<Transaction> td) {
        for (Transaction item : td) {
            Log.d("data is", item.getCategory());
            // body
        }
    }
}