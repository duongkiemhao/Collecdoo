package com.collecdoo.fragment.home.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Constant;
import com.collecdoo.control.AsteriskPassword;
import com.collecdoo.control.SimpleProgressDialog;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.UserManager;
import com.collecdoo.fragment.main.RegisterDriverPhotoFragment;
import com.collecdoo.helper.DateHelper;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, OnBackListener,
        DatePickerDialog.OnDateSetListener,HomeNavigationListener {
    private final String TAG = "--register--";
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.ediFirstName)
    EditText ediFirstName;
    @BindView(R.id.ediName)
    EditText ediName;
    @BindView(R.id.ediEmail)
    EditText ediEmail;
    @BindView(R.id.ediPhone)
    EditText ediPhone;
    @BindView(R.id.ediPassword)
    EditText ediPassword;
    @BindView(R.id.ediConfirmPass)
    EditText ediConfirmPass;
    @BindView(R.id.txtYearOfBirth)
    TextView txtYearOfBirth;
    @BindView(R.id.btnOk)
    Button btnOk;
//    @BindView(R.id.rdgGender)
//    RadioGroup rdgGender;
    boolean isPassenger = false;
    boolean isDriver = false;
    boolean isProDriver = false;

    //private int userType;
    private Unbinder unbinder;
    private Context context;

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnOk.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setColorSpan(txtTitle, 0, 9);

        ediPassword
                .setTransformationMethod(new AsteriskPassword());
        ediConfirmPass
                .setTransformationMethod(new AsteriskPassword());
        txtYearOfBirth.setOnClickListener(this);

        UserInfo userInfo = UserManager.getInstance().getUserInfo();
        if (userInfo != null) {
            ediEmail.setText(userInfo.getEmail());
            ediFirstName.setText(userInfo.getFirst_name());
            ediName.setText(userInfo.getLast_name());
            ediPhone.setText(userInfo.getPhoneNo());
            ediPassword.setText(userInfo.getPassword());
            ediConfirmPass.setText(userInfo.getPassword());
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
            if (!TextUtils.isEmpty(userInfo.birthday)) {
                Date date = null;
                try {
                    date = fromFormat.parse(userInfo.birthday);
                    txtYearOfBirth.setText(toFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

//            if (TextUtils.isEmpty(userInfo.getGent()) || userInfo.getGent().equals("0"))
//                ((RadioButton) rdgGender.getChildAt(1)).setChecked(true);
//            else ((RadioButton) rdgGender.getChildAt(1)).setChecked(false);


        }
    }

    private void setColorSpan(TextView textView, int fromPos, int toPos) {
        SpannableStringBuilder sb = new SpannableStringBuilder(textView.getText());
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
        sb.setSpan(fcs, fromPos, toPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtYearOfBirth:
                showDatePicker();
                break;
            default:
                if (validate()) {

                    String dob = "";
                    SimpleDateFormat fromFormat = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                    try {
                        Date date = fromFormat.parse(txtYearOfBirth.getText().toString());
                        dob = toFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    UserInfo userInfo=UserManager.getInstance().getUserInfo();
                    userInfo.first_name=UIHelper.getStringFromEditText(ediFirstName);
                    userInfo.last_name=UIHelper.getStringFromEditText(ediName);
                    userInfo.email=UIHelper.getStringFromEditText(ediEmail);
                    userInfo.password=UIHelper.getStringFromEditText(ediPassword);
                    userInfo.phoneNo=UIHelper.getStringFromEditText(ediPhone);
                    userInfo.birthday=dob;
                    //userInfo.gent=(rdgGender.getCheckedRadioButtonId() == R.id.rdbMan) ? "1" : "0";

                    userInfo.user_id=UserManager.getInstance().getUserInfo().getUser_id();

                    updateProfile(userInfo);


                }
                break;
        }

    }


    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();
        String birthDay = txtYearOfBirth.getText().toString();
        if (!TextUtils.isEmpty(birthDay)) {
            try {
                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(birthDay);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else calendar.setTime(new Date());

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private boolean validate() {
        StringBuffer mesError = new StringBuffer();
        if (!UIHelper.validateEmailAddress(ediEmail.getText().toString().trim())) {
            mesError.append(getString(R.string.email_invalid) + "\n");
        }
        if (TextUtils.isEmpty(UIHelper.getStringFromEditText(ediPassword))
                || TextUtils.isEmpty(UIHelper.getStringFromEditText(ediConfirmPass))
                || !ediPassword.getText().toString().trim().equals(ediConfirmPass.getText().toString().trim())) {
            mesError.append("Password in invalid");
        }

        if (!TextUtils.isEmpty(mesError.toString())) {
            Utility.showMessage(context, mesError.toString());
            return false;
        }
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(txtYearOfBirth.getText().toString());
            if (DateHelper.compare2DatePart(date, new Date()) >= 0) {
                Utility.showMessage(context, "Please enter a valid birthday");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(mesError.toString())) {
            Utility.showMessage(context, mesError.toString());
            return false;
        }
        return true;
    }

    @Override
    public void onBackPress() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        txtYearOfBirth.setText(formatCalendarOutput(monthOfYear + 1) + "/" + formatCalendarOutput(dayOfMonth) + "/" + year);
    }

    private String formatCalendarOutput(int value) {
        if (value < 10)
            return "0" + value;
        else return value + "";
    }

    private void updateProfile(UserInfo userInfo) {
        Log.d(TAG, new Gson().toJson(userInfo).toString());
        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);


        Call<ResponseInfo> call = taskService.updateProfile(userInfo);
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseInfo responseInfo = response.body();
                    Log.d(TAG, response.message());

                    if (responseInfo.status.toLowerCase().equals("ok")) {
                        UserInfo userInfo = new Gson().fromJson(responseInfo.data, UserInfo.class);
                        UserManager.getInstance().setUserInfo(userInfo);

                        Utility.showMessage(context,responseInfo.message);

                        if (userInfo.driver_type.contains("1") || userInfo.prof_driver_type.contains("1")) {
//                            getFragmentManager().beginTransaction().
//                                    setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
//                                    replace(R.id.fragment, RegisterDriverFragment.init(false, userInfo), RegisterDriverFragment.class.getName()).commit();

                            getFragmentManager().beginTransaction().add(R.id.fragment, RegisterDriverPhotoFragment.init(userInfo, true,true),RegisterDriverPhotoFragment.class.getName())
                                    .addToBackStack(RegisterDriverPhotoFragment.class.getName()).commit();


                        }




                    } else Utility.showMessage(context, responseInfo.message);

                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                simpleProgressDialog.dismiss();
                Utility.showMessage(context, t.getMessage());
                Log.d(Constant.DEBUG_TAG, "error" + t.getMessage());
            }
        });

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
}
