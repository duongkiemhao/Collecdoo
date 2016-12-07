package com.collecdoo.fragment.pickup;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
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
import com.collecdoo.config.Constant;
import com.collecdoo.dto.PathOfRouteInfo;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DriverSignatureFragment extends Fragment implements View.OnClickListener, OnBackListener,
        HomeNavigationListener {


    @BindView(R.id.txtMobile)
    TextView txtMobile;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtNeighborName)
    EditText txtNeighborName;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.signature_pad)
    SignaturePad signature_pad;


    private Unbinder unbinder;
    private Context context;
    private PathOfRouteInfo pathOfRouteInfo;


    public static DriverSignatureFragment init(PathOfRouteInfo pathOfRouteInfo) {
        DriverSignatureFragment registerFragment = new DriverSignatureFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("object", pathOfRouteInfo);
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
        pathOfRouteInfo = getArguments().getParcelable("object");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_signature_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        updateUIAll();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnClear:
                signature_pad.clear();
                break;
            case R.id.btnCancel:
                onBackPress();
                break;
            case R.id.btnConfirm:
                if (!signature_pad.isEmpty())
                    updateSignature();
                break;
        }

    }

    public String encodeBitmapBase64(Bitmap bitmap) {
        if (bitmap == null)
            return "";

        return Base64.encodeToString(UIHelper.convertBitmapToByteArray(bitmap),
                Base64.NO_WRAP);
    }

    @Override
    public void onBackPress() {
        getFragmentManager().popBackStack();
    }


    private String formatCalendarOutput(int value) {
        if (value < 10)
            return "0" + value;
        else return value + "";
    }

    @Override
    public void onBackClick() {
        onBackPress();
    }

    @Override
    public void onButton1() {
        Utility.showMessage(context, "call clicked");

    }

    @Override
    public void onButton2() {
        Utility.showMessage(context, "no action");

    }

    @Override
    public void onButton3() {
        Utility.showMessage(context, "no action");

    }

    @Override
    public void onMapClick() {
        Utility.showMessage(context, "no action");
    }

    @Override
    public void onNextClick() {
        Utility.showMessage(context, "no action");
    }

    private void updateUIAll() {

        txtMobile.setText(pathOfRouteInfo.telephone);
        txtAddress.setText(pathOfRouteInfo.destinationInfo);
        txtName.setText(pathOfRouteInfo.customer_name);

    }


    private void updateSignature() {

        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("route_detail_id", pathOfRouteInfo.routeDetailId);

        String strPhoto = "";
        try {
            strPhoto = URLEncoder.encode(
                    encodeBitmapBase64(signature_pad.getSignatureBitmap()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        jsonObject.addProperty("signature", strPhoto);
        jsonObject.addProperty("neighbor_name", UIHelper.getStringFromEditText(txtNeighborName));
        Call<ResponseInfo> call = taskService.updateSignature(jsonObject);

        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {

                if (response.isSuccessful()) {
                    //Utility.showMessage(context, response.body().message);
                    onBackPress();
                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {

                Utility.showMessage(context, t.getMessage());
                Log.d(Constant.DEBUG_TAG, "error" + t.getMessage());
            }
        });

    }

}
