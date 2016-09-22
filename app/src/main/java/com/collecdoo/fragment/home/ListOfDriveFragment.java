package com.collecdoo.fragment.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.collecdoo.MyPreference;
import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.activity.HomeActivity;
import com.collecdoo.config.Constant;
import com.collecdoo.control.SimpleProgressDialog;
import com.collecdoo.dto.BookingHistoryInfo;
import com.collecdoo.dto.BookingHistoryPostInfo;
import com.collecdoo.dto.PathOfRouteInfo;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.main.RegisterDriverFragment;
import com.collecdoo.helper.DateHelper;
import com.collecdoo.helper.UserHelper;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ListOfDriveDetailDialogFragment dialogFragment;

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
        if(TextUtils.isEmpty(txtDatePickerFrom.getText().toString())
                || TextUtils.isEmpty(txtDatePickerTo.getText().toString()))
            return;
        BookingHistoryPostInfo postInfo=new BookingHistoryPostInfo();
        try {
            Date date=new SimpleDateFormat(DateHelper.MM_DD_YYYY).parse(txtDatePickerFrom.getText().toString());
            postInfo.fromTime=new SimpleDateFormat("yyyy-MM-dd").format(date);
            Date dateTo=new SimpleDateFormat(DateHelper.MM_DD_YYYY).parse(txtDatePickerTo.getText().toString());
            postInfo.toTime=new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
            postInfo.userId= UserHelper.getUserId();
            postInfo.pageNumber="1";
            postInfo.pageSize="300";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getBookingtHistory(postInfo);

        searchAdapter=new SearchAdapter(context);
        recyclerView.setAdapter(searchAdapter);


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
        private List<BookingHistoryInfo> list;
        private Context context;
        private SimpleDateFormat serverFormat=new SimpleDateFormat(DateHelper.SERVER_DATE_FORMAT);
         private SimpleDateFormat toFormat=new SimpleDateFormat("dd.MM");

        public SearchAdapter(Context context) {
            this.context = context;
            this.list = new ArrayList<>();

        }

        public void setList(List<BookingHistoryInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_of_drive_list_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int pos) {
            //songViewHolder.textView.setText(list.get(pos));

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogFragment=ListOfDriveDetailDialogFragment.init(list.get(pos));
                    dialogFragment.show(getFragmentManager(),ListOfDriveDetailDialogFragment.class.getName());
                }
            });
            try {
                Date date=serverFormat.parse(list.get(pos).createdOn);
                myViewHolder.txtDate.setText(toFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            myViewHolder.txtFrom.setText(list.get(pos).pickupInfo);
            myViewHolder.txtTo.setText(list.get(pos).dropInfo);
            myViewHolder.txtPrice.setText(list.get(pos).getFare());

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

    private void getBookingtHistory( BookingHistoryPostInfo postInfo){
        Log.d(this.getClass().getName(),new Gson().toJson(postInfo).toString());
        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);


        Call<ResponseInfo> call = taskService.bookingHistory(postInfo);
        final SimpleProgressDialog simpleProgressDialog=new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                simpleProgressDialog.dismiss();
                if(response.isSuccessful()){
                    ResponseInfo responseInfo= response.body();

                    if(responseInfo.status.toLowerCase().equals("ok")) {
                        Type listType = new TypeToken<List<BookingHistoryInfo>>() {
                        }.getType();
                        Gson gson = new Gson();
                        List<BookingHistoryInfo> lodDetailInfoList = gson.fromJson(
                                new JsonParser().parse(responseInfo.data.toString())
                                        .getAsJsonArray(), listType);

                        Log.d(ListOfDriveFragment.class.getName(),response.message());
                        searchAdapter=new SearchAdapter(context);
                        searchAdapter.setList(lodDetailInfoList);
                        recyclerView.setAdapter(searchAdapter);

                    }


                }
                else Utility.showMessage(context,response.message());
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                simpleProgressDialog.dismiss();
                Utility.showMessage(context,t.getMessage());
                Log.d(Constant.DEBUG_TAG,"error"+t.getMessage());
            }
        });

    }


}
