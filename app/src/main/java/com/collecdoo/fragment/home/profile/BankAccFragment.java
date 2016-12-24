package com.collecdoo.fragment.home.profile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Constant;
import com.collecdoo.control.SimpleProgressDialog;
import com.collecdoo.dto.BankInfo;
import com.collecdoo.dto.CountryInfo;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.UserManager;
import com.collecdoo.fragment.adapter.MySimpleSpinnerAdapter;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.interfaces.HomeListener;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class BankAccFragment extends Fragment implements View.OnClickListener, OnBackListener ,HomeNavigationListener {

    @BindView(R.id.ediBankAcc)
    EditText ediBankAcc;
    @BindView(R.id.ediBankName)
    EditText ediBankName;
    @BindView(R.id.ediBankCode)
    EditText ediBankCode;

    @BindView(R.id.spiCountry)
    Spinner spiCountry;
    @BindView(R.id.btnOk)
    Button btnOk;

    BankInfo bankInfo;
    private Unbinder unbinder;
    private Context context;


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
        View view = inflater.inflate(R.layout.profile_bank_acc_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnOk.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        bankInfo=UserManager.getInstance().getBankInfo();
        if(bankInfo.user_id.equals(UserManager.getInstance().getUserInfo().getUser_id())) {
            ediBankAcc.setText(bankInfo.bank_account);
            ediBankName.setText(bankInfo.bank_name);
            ediBankCode.setText(bankInfo.bank_code);
        }

        if (getParentFragment() instanceof HomeListener)
            ((HomeListener) getParentFragment()).hideNavigationBar();


        spiCountry.setAdapter(new MySimpleSpinnerAdapter(context, R.layout.simple_spinner_view,
                listAll()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (spiCountry != null) {
                    spiCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View view,
                                                   int arg2, long arg3) {
                            ((TextView) view.findViewById(R.id.textView))
                                    .setText(((CountryInfo) arg0.getAdapter()
                                            .getItem(arg2)).value);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });
                    if(bankInfo.user_id.equals(UserManager.getInstance().getUserInfo().getUser_id())) {
                        spiCountry.setSelection(((MySimpleSpinnerAdapter) spiCountry.getAdapter()).findPosition(bankInfo.bank_country));
                    }
                }
            }
        }, 1000);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (getParentFragment() instanceof HomeListener)
            ((HomeListener) getParentFragment()).showNavigationBar();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                if (validate()) {
                    bankInfo.bank_account = UIHelper.getStringFromEditText(ediBankAcc);
                    bankInfo.bank_name = UIHelper.getStringFromEditText(ediBankName);
                    bankInfo.bank_country = ((CountryInfo) spiCountry.getSelectedItem()).value;
                    bankInfo.bank_code = UIHelper.getStringFromEditText(ediBankCode);
                    bankInfo.user_id=UserManager.getInstance().getUserInfo().getUser_id();
                    updateBankInfo(bankInfo);
                }
                break;
        }

    }


    private boolean validate() {
        StringBuffer mesError = new StringBuffer();
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


    public List<CountryInfo> listAll() {
        Set<MyLocale> byLocale = new TreeSet();
        // Gather them all up.
        for (Locale locale : Locale.getAvailableLocales()) {
            final String isoCountry = locale.getCountry();
            if (isoCountry.length() > 0) {
                //System.out.println(locale.getCountry() + ":" + isoCountry + ":" + locale.getDisplayName());
                byLocale.add(new MyLocale(locale));
            }
        }
        // Roll them out of the set.
        ArrayList<CountryInfo> list = new ArrayList<>();
        for (MyLocale l : byLocale) {
            if (!TextUtils.isEmpty(l.getCountry()))
                list.add(new CountryInfo(l.getCountry(), l.getCountry()));
        }
        return list;
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


    private class MyLocale implements Comparable<MyLocale> {
        // My Locale.
        private final Locale me;

        public MyLocale(Locale me) {
            this.me = me;
        }

        // Convenience
        public String getCountry() {
            return me.getDisplayCountry();
        }

        @Override
        public int compareTo(MyLocale it) {
            // No duplicates in the country field.
            if (getCountry().equals(it.getCountry())) {
                return 0;
            }

            // Default to straight comparison.
            return getCountry().compareTo(it.getCountry());
        }
    }


    private void updateBankInfo(BankInfo bankInfo) {
        Log.d(this.getClass().getName(),new Gson().toJson(bankInfo));
        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);

        Call<ResponseInfo> call = taskService.updateBank(bankInfo);
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseInfo responseInfo = response.body();
                    Log.d(BankAccFragment.class.getName(), response.message());

                    if (responseInfo.status.toLowerCase().equals("ok")) {
                        BankInfo bankInfo1 = new Gson().fromJson(responseInfo.data, BankInfo.class);
                        UserManager.getInstance().setBankInfo(bankInfo1);
                    }
                    else Utility.showMessage(context, responseInfo.message);
                    onBackPress();

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
