package app.buzzboy.com.moneymanagement.Ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import app.buzzboy.com.moneymanagement.Application.Transaction;
import app.buzzboy.com.moneymanagement.R;
import app.buzzboy.com.moneymanagement.Ui.CategorySelection.CategorySelectionMainActivity;

public class AddTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText amt;
    int month;
    EditText cat_selection;
    public static final String PARENT_CLASS_SOURCE = "com.buzzboy.com.moneymanagement.Ui.AddTransactionActivity.java";
    public static final String TITLE = "Amount";
    DatePicker datePicker;
    EditText displayDate;
    Handler handler = new Handler();
    EditText note;
    EditText loc;
    boolean status_flag = false;
    String query;
    Button destruct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        query = null;
        loc = (EditText) findViewById(R.id.trans_location);
        amt = (EditText) findViewById(R.id.trans_amt);
        cat_selection = (EditText) findViewById(R.id.category_selection);
        cat_selection.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent i = new Intent(AddTransactionActivity.this, CategorySelectionMainActivity.class);
                    startActivityForResult(i, 100);
                }
            }
        });
        destruct = (Button) findViewById(R.id.delete);
        destruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEntry();
            }
        });
        note = (EditText) findViewById(R.id.note_multi_line);
        cat_selection.setKeyListener(null);
        displayDate = (EditText) findViewById(R.id.trans_date);

        displayDate.setKeyListener(null);
        displayDate.setText(currentDate());

        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDate.setText(currentDate());
            }
        });

        displayDate.setOnClickListener(showDatePickerDialog);

        Intent i = getIntent();
        if (i.hasExtra("state")) {
            status_flag = true;
            Toast.makeText(getApplicationContext(), i.getExtras().getString("pos"), Toast.LENGTH_LONG).show();
            query = i.getExtras().getString("pos");
            cat_selection.setText(i.getExtras().getString("category"));
            note.setText(i.getExtras().getString("note"));
            displayDate.setText(i.getExtras().getString("date"));
            loc.setText(i.getExtras().getString("location"));
            amt.setText(i.getExtras().getString("amount"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                cat_selection.setText(result);
                //cat_selection.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_location_black_36dp, 0, 0, 0);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_transaction_add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                run_transaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run_transaction() {

        if (!TextUtils.isEmpty(amt.getText())) {
            if (!TextUtils.isEmpty(cat_selection.getText())) {
                finish();

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            //LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                            if (status_flag) {


                                //handler.post(this);
                                Transaction t = new Transaction(Integer.parseInt(amt.getText().toString()), cat_selection.getText().toString(), note.getText().toString(), displayDate.getText().toString(), null);
                                t.modify(query);
                            } else {
                                Map location = new HashMap();
                                double lon = 00;//location.getLongitude();
                                double lat = 00;//location.getLatitude();
                                location.put("lat", Double.toString(lat));
                                location.put("lon", Double.toString(lon));

                                //handler.post(this);
                                Transaction t = new Transaction(Integer.parseInt(amt.getText().toString()), cat_selection.getText().toString(), note.getText().toString(), displayDate.getText().toString(), location);
                                boolean result = t.postTransaction();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();
            } else {
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                Toast.makeText(getApplicationContext(), "Category cannot be empty", Toast.LENGTH_LONG).show();
            }
        } else {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Toast.makeText(getApplicationContext(), "Amount cannot be 0", Toast.LENGTH_LONG).show();
        }
    }

    public String currentDate() {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DATE) + "/" + c.get(Calendar.YEAR);
    }

    public View.OnClickListener showDatePickerDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment df = new DatePickerFragment();
            df.show(getSupportFragmentManager(), "datePicker");
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // do something with the date choosen by the user.
        displayDate.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
    }

    public static class DatePickerFragment extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstance) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (AddTransactionActivity) getActivity(), year, month, day);
        }
    }

    public void deleteEntry() {
        if (status_flag) {
            Transaction t = new Transaction(Integer.parseInt(amt.getText().toString()), cat_selection.getText().toString(), note.getText().toString(), displayDate.getText().toString(), null);
            t.remove(query);
            finish();
        } else {
            finish();
        }
    }
}