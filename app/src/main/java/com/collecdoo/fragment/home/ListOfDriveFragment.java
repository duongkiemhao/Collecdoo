package com.collecdoo.fragment.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListOfDriveFragment extends Fragment implements View.OnClickListener,OnBackListener,
        HomeNavigationListener {

    @BindView(R.id.txtDatePickerFrom) TextView txtDatePickerFrom;
    @BindView(R.id.txtDatePickerTo) TextView txtDatePickerTo;
    @BindView(R.id.btnSearch) View btnSearch;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private SearchAdapter searchAdapter;

    private Unbinder unbinder;

    public static ListOfDriveFragment init(){
        ListOfDriveFragment registerFragment=new ListOfDriveFragment();
        Bundle bundle=new Bundle();

        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    private Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.list_of_drive_fragment, container, false);
        unbinder=ButterKnife.bind(this, view);
        txtDatePickerFrom.setOnClickListener(this);
        txtDatePickerTo.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

         searchAdapter=new SearchAdapter(context);
        recyclerView.setAdapter(searchAdapter);




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnSearch:
                doSearch();
                break;
            case R.id.txtDatePickerFrom:
                showDatePicker(txtDatePickerFrom);
                break;
            case R.id.txtDatePickerTo:
                showDatePicker(txtDatePickerTo);
                break;

        }

    }

    private void doSearch(){
        List<LODDetailInfo> list=new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(new LODDetailInfo("21.3", "A"+i, "B"+i, "4.5 EUR"));
        }
        searchAdapter.setList(list);
        searchAdapter.notifyDataSetChanged();

    }

    private void showDatePicker(final TextView txtDatePicker)
    {
        Calendar calendar=Calendar.getInstance();
        String birthDay=txtDatePicker.getText().toString();
        if(!TextUtils.isEmpty(birthDay)) {
            try {
                Date date=new SimpleDateFormat("MM/dd/yyyy").parse(birthDay);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else calendar.setTime(new Date());

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                txtDatePicker.setText(formatCalendarOutput(monthOfYear + 1) + "/" + formatCalendarOutput(dayOfMonth) + "/" + year);
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    @Override
    public void onBackPress() {
        getFragmentManager().popBackStack();
//        ParentFragmentListener homeTagListener= (ParentFragmentListener) getParentFragment();
//        FragmentManager fm=getFragmentManager();
//        FragmentTransaction ft=fm.beginTransaction();
//        ft.remove(this);
//        ft.show(fm.findFragmentByTag(homeTagListener.getCurrentTag()));
//        ft.commit();
    }


    private String formatCalendarOutput(int value){
        if(value<10)
            return "0"+value;
        else return value+"";
    }

    @Override
    public void onBackClick() {
        onBackPress();

    }

    @Override
    public void onButton1() {

    }

    @Override
    public void onButton2() {

    }

    @Override
    public void onButton3() {

    }

    @Override
    public void onMapClick() {

    }

    @Override
    public void onNextClick() {


    }




     class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
        private List<LODDetailInfo> list;
        private Context context;


        public SearchAdapter(Context context) {
            this.context = context;
            this.list = new ArrayList<>();

        }

        public void setList(List<LODDetailInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_of_drive_list_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int pos) {
            //songViewHolder.textView.setText(list.get(pos));

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListOfDriveDetailDialogFragment dialog=new ListOfDriveDetailDialogFragment();
                    dialog.show(getFragmentManager(),"listOfDriveDetail");
                }
            });
            myViewHolder.txtDate.setText(list.get(pos).date);
            myViewHolder.txtFrom.setText(list.get(pos).from);
            myViewHolder.txtTo.setText(list.get(pos).to);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public @BindView(R.id.txtDate) TextView txtDate;
            public @BindView(R.id.txtFrom) TextView txtFrom;
            public @BindView(R.id.txtTo) TextView txtTo;
            public @BindView(R.id.txtPrice) TextView txtPrice;
            public @BindView(R.id.btnDetail) View btnDetail;

            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }

    class LODDetailInfo{
        public String date;
        public String from;
        public String to;
        public String price;

        public LODDetailInfo(String date, String from, String to, String price) {
            this.date = date;
            this.from = from;
            this.to = to;
            this.price = price;
        }
    }

}
