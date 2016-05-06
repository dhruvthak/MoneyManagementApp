package app.buzzboy.com.moneymanagement.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import app.buzzboy.com.moneymanagement.Application.Transaction;
import app.buzzboy.com.moneymanagement.R;

public class TransactionsView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransactionsView.this, AddTransactionActivity.class);
                startActivity(i);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String FIREBASE_URL = "https://managemymoney.firebaseio.com/Transactions/";
        Firebase ref = new Firebase(FIREBASE_URL);
        recycleview = (RecyclerView) findViewById(R.id.recycler_view);
        recycleview.setHasFixedSize(true);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        final FirebaseRecyclerAdapter<Transaction, TransactionViewHolder> fadapter = new FirebaseRecyclerAdapter<Transaction, TransactionViewHolder>(Transaction.class, R.layout.recycler_layout, TransactionViewHolder.class, ref) {

            @Override
            public void populateViewHolder(TransactionViewHolder tv, Transaction t, int position) {

                tv.nameText2.setText("$" + Integer.toString(t.getAmount()), TextView.BufferType.NORMAL);
                tv.nameText4.setText(t.getCategory(), TextView.BufferType.NORMAL);
                tv.nameTextHidden.setText(String.valueOf(this.getRef(position)), TextView.BufferType.NORMAL);
            }

            @Override
            public Transaction getItem(int position) {
                return super.getItem(position);
            }

        };

        recycleview.setAdapter(fadapter);
        recycleview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycleview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Transaction t = fadapter.getItem(position);
                //t.getpos();

                //Toast.makeText(getApplicationContext(),   t.getAmount()+ " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TransactionsView.this, AddTransactionActivity.class);
                i.putExtra("amount", Integer.toString(t.getAmount()));
                i.putExtra("category", t.getCategory());
                i.putExtra("note", t.getNote());
                i.putExtra("date", t.getDate());
                //i.putExtra("location", t.getLocation().toString());
                //i.putExtra("uid", ((TextView)view.findViewById(R.id.hidden)).getText().toString());
                i.putExtra("pos", ((TextView) view.findViewById(R.id.hidden)).getText().toString());
                i.putExtra("state", "edit");
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transcations_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } //
        // else if (id == R.id.nav_gallery) {

        //}
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static class TransactionViewHolder extends RecyclerView.ViewHolder {


        TextView nameText2;
        TextView nameText4;
        TextView nameTextHidden;


        public TransactionViewHolder(View itemView) {
            super(itemView);
            nameText2 = (TextView) itemView.findViewById(R.id.amt);
            nameText4 = (TextView) itemView.findViewById(R.id.cat);
            nameTextHidden = (TextView) itemView.findViewById(R.id.hidden);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TransactionsView.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TransactionsView.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}