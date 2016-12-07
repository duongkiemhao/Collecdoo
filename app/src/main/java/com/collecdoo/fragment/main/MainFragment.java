package com.collecdoo.fragment.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.collecdoo.MyPreference;
import com.collecdoo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    public final static String LIST_USER_TYPE = "listUserType";
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    @BindView(R.id.txtChoice)
    TextView txtChoice;
    @BindView(R.id.btnOk)
    Button btnOk;
    MyArrayAdapter myArrayAdapter;
    AdapterView.OnItemClickListener myOnItemClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            myArrayAdapter.toggleChecked(position);

        }
    };
    private Context context;
    private ArrayList<String> dayOfWeekList = new ArrayList<String>();

    public static MainFragment init() {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();

        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        btnOk.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        setColorSpan(txtTitle, 0, 9);
//        setColorSpan(txtDesc, 0, 9);
//        setColorSpan(txtChoice, txtChoice.getText().length() - 13, txtChoice.getText().length() - 4);

        ListView myListView = (ListView) getView().findViewById(R.id.listView);

        initDayOfWeekList();
        myArrayAdapter = new MyArrayAdapter(
                context,
                R.layout.main_lv_item,
                android.R.id.text1,
                dayOfWeekList
        );

        myListView.setAdapter(myArrayAdapter);
        myListView.setOnItemClickListener(myOnItemClickListener);

    }

    private void setColorSpan(TextView textView, int fromPos, int toPos) {
        SpannableStringBuilder sb = new SpannableStringBuilder(textView.getText());
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
        sb.setSpan(fcs, fromPos, toPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        processRadioGroup();
    }

    private void processRadioGroup() {
        List<Integer> resultList = myArrayAdapter.getCheckedItems();

        //int userType=0;


        MyPreference.setListObject(LIST_USER_TYPE, resultList);
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                replace(R.id.fragment, LoginFragment.init(), LoginFragment.class.getName()).
                commit();
    }

    private void initDayOfWeekList() {
        String[] arr = getResources().getStringArray(R.array.main_login_type);
        dayOfWeekList = new ArrayList<>(Arrays.asList(arr));
    }

    private class MyArrayAdapter extends ArrayAdapter<String> {

        private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();

        public MyArrayAdapter(Context context, int resource,
                              int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);

            for (int i = 0; i < objects.size(); i++) {
                myChecked.put(i, false);
            }
        }

        public void toggleChecked(int position) {
            if (myChecked.get(position)) {
                myChecked.put(position, false);
            } else {
                myChecked.put(position, true);
            }

            notifyDataSetChanged();
        }

        public List<Integer> getCheckedItemPositions() {
            List<Integer> checkedItemPositions = new ArrayList<Integer>();

            for (int i = 0; i < myChecked.size(); i++) {
                if (myChecked.get(i)) {
                    (checkedItemPositions).add(i);
                }
            }

            return checkedItemPositions;
        }

        public List<Integer> getCheckedItems() {
            List<Integer> checkedItems = new ArrayList<Integer>();

            for (int i = 0; i < myChecked.size(); i++) {
                if (myChecked.get(i)) {
                    (checkedItems).add(i);
                }
            }

            return checkedItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {

                row = LayoutInflater.from(context).inflate(R.layout.main_lv_item, parent, false);
            }

            CheckedTextView checkedTextView = (CheckedTextView) row.findViewById(R.id.checkTextView);
            checkedTextView.setText(dayOfWeekList.get(position));

            TextView textView = (TextView) row.findViewById(R.id.textView);
            textView.setText(dayOfWeekList.get(position));
            Boolean checked = myChecked.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
            }

            return row;
        }

    }

}
