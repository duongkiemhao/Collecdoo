package com.collecdoo.fragment.pickup;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Config;
import com.collecdoo.config.Constant;
import com.collecdoo.dto.PathOfRouteInfo;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.home.driver.DriverHomeFragment;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.util.ArrayList;
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
public class DriverPickupDropFragment extends Fragment implements View.OnClickListener, OnBackListener,
        HomeNavigationListener, OnMapReadyCallback {

    public final int CALL_PERMISSION_REQUEST_CODE = 1;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtMobile)
    TextView txtMobile;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.btnRing1)
    ImageView btnRing1;
    @BindView(R.id.btnRing2)
    ImageView btnRing2;
    @BindView(R.id.btnSos)
    TextView btnSos;
    @BindView(R.id.txtPickupTitle)
    TextView txtPickupTitle;
    private GoogleMap googleMap;
    private boolean isPickup;
    private Unbinder unbinder;
    private Context context;
    private List<PathOfRouteInfo> pathOfRouteInfoList;
    private int viewIndex;

    public static DriverPickupDropFragment init(boolean isPickup, ArrayList<Parcelable> pathOfRouteInfoList, int viewIndex) {
        DriverPickupDropFragment registerFragment = new DriverPickupDropFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPickup", isPickup);
        bundle.putInt("viewIndex", viewIndex);
        bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) pathOfRouteInfoList);
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
        pathOfRouteInfoList = getArguments().getParcelableArrayList("list");
        isPickup = getArguments().getBoolean("isPickup");
        viewIndex = getArguments().getInt("viewIndex");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_pickup_drop_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnSos.setOnClickListener(this);
        btnRing1.setOnClickListener(this);
        btnRing2.setOnClickListener(this);

        txtPickupTitle.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        updateUIAll();


        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        supportMapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment, supportMapFragment, "map").commit();


    }

    private void setUIPickup() {
        if (isPickup) {
            txtTitle.setText(getResources().getString(R.string.pickup_drop_title_pickup));
            txtPickupTitle.setText(getResources().getString(R.string.pickup_drop_send_pickup));
        } else {
            txtTitle.setText(getResources().getString(R.string.pickup_drop_title_drop));
            txtPickupTitle.setText(getResources().getString(R.string.pickup_drop_send_drop));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSos:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PERMISSION_REQUEST_CODE);
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+Config.PHONE_NO));
                    startActivity(callIntent);
                }


                break;
            case R.id.txtPickupTitle:
                updateRouteDetail(pathOfRouteInfoList.get(viewIndex).routeDetailId);
                break;
            case R.id.btnRing1:
            case R.id.btnRing2:
                updateRouteDetail(pathOfRouteInfoList.get(viewIndex).routeDetailId);
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+ Config.PHONE_NO));

                    startActivity(callIntent);
                } else {
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPress() {
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                replace(R.id.fragment, DriverHomeFragment.init(),
                        DriverHomeFragment.class.getName()).
                commit();
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
        btnSos.performClick();

    }

    @Override
    public void onButton2() {

//        if (isPickup)
//            return;
        if (getFragmentManager().findFragmentByTag(DriverSignatureFragment.class.getName()) == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, DriverSignatureFragment.init(pathOfRouteInfoList.get(viewIndex)), DriverSignatureFragment.class.getName()).
                    addToBackStack(DriverSignatureFragment.class.getName()).commit();
        }
    }

    @Override
    public void onButton3() {

        if (getChildFragmentManager().findFragmentByTag(DriverPickupDropStopsFragment.class.getName()) == null)
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment, DriverPickupDropStopsFragment.init(pathOfRouteInfoList), DriverPickupDropStopsFragment.class.getName()).commit();

    }

    @Override
    public void onMapClick() {
        if (getChildFragmentManager().findFragmentByTag(DriverPickupDropStopsFragment.class.getName()) != null)
            getChildFragmentManager().beginTransaction()
                    .remove(getChildFragmentManager().findFragmentByTag(DriverPickupDropStopsFragment.class.getName())).commit();
    }

    @Override
    public void onNextClick() {
        showNextDialog();
    }


    private void showNextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getResources().getString(R.string.pickup_drop_dialog_message));

        String positiveText = getResources().getString(R.string.yes);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cancelRouteDetail(pathOfRouteInfoList.get(viewIndex).routeDetailId);
                        viewIndex++;
                        updateUIAll();
                        drawMap();

                    }
                });

        String negativeText = getResources().getString(R.string.no);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    //------------map------------
    private void drawMap() {

        googleMap.clear();


        // Creating MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_pin_from));
        LatLng latLng = new LatLng(Double.parseDouble(pathOfRouteInfoList.get(viewIndex).lat),
                Double.parseDouble(pathOfRouteInfoList.get(viewIndex).lon));
        markerOptions.position(latLng);
        markerOptions.title(pathOfRouteInfoList.get(viewIndex).destinationInfo);
        googleMap.addMarker(markerOptions);
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        googleMap.animateCamera(cu);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.setPadding(0, 0, 0, (int) Utility.convertDPtoPIXEL(TypedValue.COMPLEX_UNIT_DIP, 40));
        drawMap();

    }


    private void updateRouteDetail(String routeDetailId) {

        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("route_detail_id", routeDetailId);
        Call<ResponseInfo> call = taskService.updateRouteDetail(jsonObject);

        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {

                if (response.isSuccessful()) {
                    Utility.showMessage(context, response.body().message);
                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {

                Utility.showMessage(context, t.getMessage());
                Log.d(Constant.DEBUG_TAG, "error" + t.getMessage());
            }
        });

    }

    private void cancelRouteDetail(String routeDetailId) {

        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("route_detail_id", routeDetailId);
        Call<ResponseInfo> call = taskService.cancelRouteDetail(jsonObject);

        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {

                if (response.isSuccessful()) {
                    Utility.showMessage(context, response.body().message);
                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {

                Utility.showMessage(context, t.getMessage());
                Log.d(Constant.DEBUG_TAG, "error" + t.getMessage());
            }
        });

    }

    private void updateUIAll() {
        PathOfRouteInfo pathOfRouteInfo = pathOfRouteInfoList.get(viewIndex);
        txtMobile.setText(pathOfRouteInfo.telephone);
        txtAddress.setText(pathOfRouteInfo.destinationInfo);
        txtName.setText(pathOfRouteInfo.customer_name);
        isPickup = pathOfRouteInfo.is_delivery.equals("0") ? true : false;
        setUIPickup();
    }


}
