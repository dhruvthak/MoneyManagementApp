package app.buzzboy.com.moneymanagement.Ui.CategorySelection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.buzzboy.com.moneymanagement.R;

/**
 * Created by Dhruv Thakkar on 5/4/2016.
 */
public class CustomAdapter extends BaseAdapter {

    String[] result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;
    View rowView;
    private AdapterCallback mAdapterCallback;


    public interface AdapterCallback {
        void onMethodCallback(String msg);
    }

    public CustomAdapter(Activity activity, String[] prgmNameList, int[] prgmImages, AdapterCallback callback) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        context = activity;
        imageId = prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mAdapterCallback = callback;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();

        rowView = inflater.inflate(R.layout.list_layout, null);
        holder.tv = (TextView) rowView.findViewById(R.id.names);
        holder.img = (ImageView) rowView.findViewById(R.id.icon);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mAdapterCallback.onMethodCallback(result[position]);
            }
        });
        return rowView;
    }

    public void sendSelectedItemtoFragment() {

    }
}
