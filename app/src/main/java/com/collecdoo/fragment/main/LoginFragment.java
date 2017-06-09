package com.collecdoo.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.TextView;

import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.activity.HomeActivity;
import com.collecdoo.config.Constant;
import com.collecdoo.control.AsteriskPassword;
import com.collecdoo.control.SimpleProgressDialog;
import com.collecdoo.dto.MyJsonObject;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.UserManager;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.helper.UserHelper;
import com.collecdoo.interfaces.OnBackListener;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, OnBackListener {
    private final String TAG = "--login--";
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.ediEmail)
    EditText ediEmail;

    @BindView(R.id.ediPassword)
    EditText ediPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnForgetPass)
    TextView btnForgetPass;
    @BindView(R.id.btnRegister)
    TextView btnRegister;


    private Unbinder unbinder;
    private Context context;

    public LoginFragment() {
    }

    public static LoginFragment init() {
        LoginFragment registerFragment = new LoginFragment();
        Bundle bundle = new Bundle();

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
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgetPass.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setColorSpan(txtTitle, 0, 9);

        ediPassword
                .setTransformationMethod(new AsteriskPassword());
        UserInfo userInfo = UserHelper.getUserInfo();
        if (userInfo != null) {
            ediEmail.setText(userInfo.email);
            ediPassword.setText("");
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
            case R.id.btnLogin:
                //sendNotification("hello");
                if (validate()) {
                    login(UIHelper.getStringFromEditText(ediEmail), UIHelper.getStringFromEditText(ediPassword));


                }
                break;
            case R.id.btnRegister:
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                        replace(R.id.fragment, RegisterFragment.init(), RegisterFragment.class.getName()).commit();
                break;
            case R.id.btnForgetPass:

                break;

        }

    }


    private boolean validate() {
        StringBuffer mesError = new StringBuffer();
        if (!UIHelper.validateEmailAddress(ediEmail.getText().toString().trim())) {
            mesError.append(getString(R.string.email_invalid) + "\n");
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
                replace(R.id.fragment, MainFragment.init(), MainFragment.class.getName()).
                commit();
    }


    private void login(String email,
                       String password) {

        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);
        Call<MyJsonObject<UserInfo>> call = taskService.login(jsonObject);
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<MyJsonObject<UserInfo>>() {

            @Override
            public void onResponse(Call<MyJsonObject<UserInfo>> call, Response<MyJsonObject<UserInfo>> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    MyJsonObject responseInfo = response.body();

                    if (responseInfo.status.toLowerCase().equals("ok")) {

                        UserInfo userInfo = (UserInfo) responseInfo.data;
                        UserManager.getInstance().setUserInfo(userInfo);


                        startActivity(new Intent(context, HomeActivity.class));
                        getActivity().finish();
                    } else Utility.showMessage(context, responseInfo.message);
                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<MyJsonObject<UserInfo>> call, Throwable t) {
                simpleProgressDialog.dismiss();
                Utility.showMessage(context, t.getMessage());
                Log.d(Constant.DEBUG_TAG, "error" + t.getMessage());
            }
        });

    }

}
