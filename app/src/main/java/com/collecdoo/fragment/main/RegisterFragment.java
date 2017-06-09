package com.collecdoo.fragment.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.collecdoo.MyPreference;
import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.activity.HomeActivity;
import com.collecdoo.config.Constant;
import com.collecdoo.control.AsteriskPassword;
import com.collecdoo.control.SimpleProgressDialog;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.UserManager;
import com.collecdoo.helper.DateHelper;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.interfaces.OnBackListener;
import com.collecdoo.service.gcm.QuickstartPreferences;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class RegisterFragment extends Fragment implements View.OnClickListener, OnBackListener,
        DatePickerDialog.OnDateSetListener {
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
    @BindView(R.id.spiGender)
    Spinner spiGender;

    boolean isPassenger = false;
    boolean isDriver = false;
    boolean isProDriver = false;

    //private int userType;
    private Unbinder unbinder;
    private Context context;

    public RegisterFragment() {
    }

    public static RegisterFragment init() {
        RegisterFragment registerFragment = new RegisterFragment();
        Bundle bundle = new Bundle();

        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    public static RegisterFragment init(UserInfo userInfo) {
        RegisterFragment registerFragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("userInfo", userInfo);
        registerFragment.setArguments(bundle);
        return registerFragment;
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

        UserInfo userInfo = getArguments().getParcelable("userInfo");
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

            if (TextUtils.isEmpty(userInfo.getGent()) || userInfo.getGent().equals("0"))
                spiGender.setSelection(0);
            else spiGender.setSelection(1);


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
                    List<Integer> userTypeList = MyPreference.getListObject(MainFragment.LIST_USER_TYPE, Integer.class);

                    if (userTypeList.contains(2))
                        isProDriver = true;
                    if (userTypeList.contains(1))
                        isDriver = true;
                    if (userTypeList.contains(0))
                        isPassenger = true;
                    String dob = "";
                    SimpleDateFormat fromFormat = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                    try {
                        Date date = fromFormat.parse(txtYearOfBirth.getText().toString());
                        dob = toFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    UserInfo userInfo = new UserInfo(UIHelper.getStringFromEditText(ediFirstName),
                            UIHelper.getStringFromEditText(ediName),
                            UIHelper.getStringFromEditText(ediEmail),
                            UIHelper.getStringFromEditText(ediPassword),
                            UIHelper.getStringFromEditText(ediPhone),
                            dob,
                            (spiGender.getSelectedItemPosition() == 1) ? "1" : "0",
                            isPassenger ? "1" : "0",
                            isDriver ? "1" : "0",
                            isProDriver ? "1" : "0", "", "", "", "", "", "", "",
                            MyPreference.getString(QuickstartPreferences.TOKEN_STRING));
                    //MyPreference.setObject("userInfo",userInfo);

                    register(userInfo);


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
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                replace(R.id.fragment, LoginFragment.init(), LoginFragment.class.getName()).
                commit();
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

    private void register(UserInfo userInfo) {
        Log.d(TAG, new Gson().toJson(userInfo).toString());
        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);


        Call<ResponseInfo> call = taskService.register(userInfo);
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

                        if (isDriver || isProDriver) {
                            getFragmentManager().beginTransaction().
                                    setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                                    replace(R.id.fragment, RegisterDriverFragment.init(false, userInfo), RegisterDriverFragment.class.getName()).commit();

                        } else {

                            UserManager.getInstance().setUserInfo(userInfo);
                            startActivity(new Intent(context, HomeActivity.class));
                            getActivity().finish();
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

}
