package app.buzzboy.com.moneymanagement.Ui.CategorySelection;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import app.buzzboy.com.moneymanagement.R;


public class CategorySelectionView2 extends Fragment implements CustomAdapter.AdapterCallback {

    ListView list;
    int[] img_list = {
            R.mipmap.ic_shopping,
            R.mipmap.ic_friends,
            R.mipmap.ic_health,
            R.mipmap.ic_education
    };
    CustomAdapter.AdapterCallback mAdapterCallback;
    IFragmentToActivity mCallback;

    public String[] names = {"Shopping", "Friends", "Health", "Education"};

    public CategorySelectionView2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_selection_page2, container, false);
        list = (ListView) view.findViewById(R.id.list2);
        list.setAdapter(new CustomAdapter(getActivity(), names, img_list, this));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (IFragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onMethodCallback(String msg) {
        // do something
        mCallback.getSelectedItem(msg);
    }
}
