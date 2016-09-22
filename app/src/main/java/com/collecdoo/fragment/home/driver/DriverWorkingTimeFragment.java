package com.collecdoo.fragment.home.driver;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.collecdoo.MyPreference;
import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Constant;

import com.collecdoo.control.SimpleProgressDialog;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.dto.ShareTimeInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A placeholder fragment containing a simple view.
 */
public class DriverWorkingTimeFragment extends Fragment implements View.OnClickListener,OnBackListener,
        HomeNavigationListener {

    @BindView(R.id.txtDatePickerFrom) TextView txtDatePickerFrom;
    @BindView(R.id.txtDatePickerTo) TextView txtDatePickerTo;
    @BindView(R.id.btnDateTimePickerFrom) TextView btnDateTimePickerFrom;
    @BindView(R.id.btnDateTimePickerTo) TextView btnDateTimePickerTo;
    @BindView(R.id.ediFreeSeat) EditText ediFreeSeat;
    private final String TAG="--driver next7day--";
    @BindView(R.id.btnOk) Button btnOk;


    private Unbinder unbinder;

    public static DriverWorkingTimeFragment init(){
        DriverWorkingTimeFragment registerFragment=new DriverWorkingTimeFragment();
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
        View view=inflater.inflate(R.layout.driver_working_time_fragment, container, false);
        unbinder=ButterKnife.bind(this, view);

        btnDateTimePickerFrom.setOnClickListener(this);
        btnDateTimePickerTo.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnDateTimePickerFrom:
                showDatePicker(true);
                break;
            case R.id.btnDateTimePickerTo:
                showDatePicker(false);
                break;
            case R.id.btnOk:
                if(!validate())
                    return;
                UserInfo userInfo= (UserInfo) MyPreference.getObject("userInfo",UserInfo.class);
                ShareTimeInfo shareInfo=new ShareTimeInfo();
                shareInfo.setUserId(userInfo.user_id);


                shareInfo.setLat1("");
                shareInfo.setLon1("");
                shareInfo.setDesiredStartTime("");
                shareInfo.setDesiredEndTime("");
                shareInfo.setPaymentMethod("");
                shareInfo.setStartPoint("");
                shareInfo.setFromTime("");
                shareInfo.setToTime("");
                shareInfo.setSeatCapacity(UIHelper.getStringFromEditText(ediFreeSeat));
                SimpleDateFormat fromFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
                SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                try {
                    Date fromDate=fromFormat.parse( txtDatePickerFrom.getText().toString());
                    shareInfo.setFromTime(toFormat.format(fromDate));
                    Date toDate=fromFormat.parse( txtDatePickerTo.getText().toString());
                    shareInfo.setToTime(toFormat.format(toDate));

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                shareDrive(shareInfo);
                break;
        }

    }

    private boolean validate(){
        if(TextUtils.isEmpty(UIHelper.getStringFromTextView(txtDatePickerFrom))
                || TextUtils.isEmpty(UIHelper.getStringFromTextView(txtDatePickerTo))
                || TextUtils.isEmpty(UIHelper.getStringFromEditText(ediFreeSeat)))
            return false;
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
            SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date fromDate=fromFormat.parse( txtDatePickerFrom.getText().toString());
            Date toDate=fromFormat.parse( txtDatePickerTo.getText().toString());
            if(fromDate.compareTo(new Date())<=0
                    || toDate.compareTo(new Date())<=0)
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void shareDrive(ShareTimeInfo shareInfo){
        Log.d(TAG,shareInfo.toString());
        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);

        Call<ResponseInfo> call = taskService.shareTime(shareInfo);
        final SimpleProgressDialog simpleProgressDialog=new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, retrofit2.Response<ResponseInfo> response) {
                simpleProgressDialog.dismiss();
                if(response.isSuccessful()){
                    ResponseInfo shareWrapperInfo= response.body();
                    Log.d(TAG,shareWrapperInfo.toString());
                    if(shareWrapperInfo.status.toLowerCase().equals("ok")) {
                        showNextDialog();
                    }
                    else Utility.showMessage(context,shareWrapperInfo.message);
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

    private void showNextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Do you like to require more drive? Yes/No");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtDatePickerFrom.setText("");
                        txtDatePickerTo.setText("");
                        ediFreeSeat.setText("");


                    }
                });

        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                                replace(R.id.fragment, DriverHomeFragment.init(), DriverHomeFragment.class.getName()).
                                commit();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void showDatePicker(boolean isFrom)
    {
        SimpleDateFormat mFormatter = new SimpleDateFormat("MMM dd yyyy HH:mm");
        Date date=new Date();
        try {
            date=mFormatter.parse( (isFrom?txtDatePickerFrom:txtDatePickerTo).getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SlideDateTimePicker slideDateTimePicker=new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                .setListener(isFrom?fromListener:toListener)
                .setInitialDate(date)
                .setMinDate(new Date())

                .build();

        slideDateTimePicker.show();

    }

    private SlideDateTimeListener fromListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            SimpleDateFormat mFormatter = new SimpleDateFormat("MMM dd yyyy HH:mm");
            txtDatePickerFrom.setText(mFormatter.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

    private SlideDateTimeListener toListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            SimpleDateFormat mFormatter = new SimpleDateFormat("MMM dd yyyy HH:mm");
            txtDatePickerTo.setText(mFormatter.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };
    @Override
    public void onBackPress() {
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                replace(R.id.fragment, DriverHomeFragment.init(), DriverHomeFragment.class.getName()).
                commit();
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
        if(validate())
        showNextDialog();
    }




}
