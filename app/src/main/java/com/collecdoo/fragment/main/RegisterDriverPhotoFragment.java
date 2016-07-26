package com.collecdoo.fragment.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.collecdoo.MyApplicationContext;
import com.collecdoo.MyPreference;
import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.activity.HomeActivity;
import com.collecdoo.config.Constant;
import com.collecdoo.control.SimpleProgressDialog;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.home.StatusLoginFragment;
import com.collecdoo.helper.ImageHelper;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.interfaces.HomeListener;
import com.collecdoo.interfaces.OnBackListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class RegisterDriverPhotoFragment extends Fragment implements View.OnClickListener,OnBackListener {
    private final String TAG="--register driver--";

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtDesc) TextView txtDesc;
    @BindView(R.id.txtPicture) TextView txtPicture;
    @BindView(R.id.txtDriveLicense) TextView txtDriveLicense;
    @BindView(R.id.txtBusinessRegis) TextView txtBusinessRegis;
    @BindView(R.id.checkbox) CheckBox checkBox;
    @BindView(R.id.btnOk) Button btnOk;
    @BindView(R.id.txtPolicy) TextView txtPolicy;
    private UserInfo userInfo;
    private String strAvatar;
    private boolean wasPassenger ;



    private Unbinder unbinder;
    private Uri cameraOutputFileUri;

    public static RegisterDriverPhotoFragment init(UserInfo userInfo,boolean wasPassenger){
        RegisterDriverPhotoFragment registerDriverFragment=new RegisterDriverPhotoFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable("userInfo",userInfo);
        bundle.putBoolean("wasPassenger",wasPassenger);
        registerDriverFragment.setArguments(bundle);
        return registerDriverFragment;
    }

    private Context context;

    public RegisterDriverPhotoFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo= getArguments().getParcelable("userInfo");
        wasPassenger =getArguments().getBoolean("wasPassenger");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.register_driver_photo_fragment, container, false);
        unbinder=ButterKnife.bind(this, view);
        btnOk.setOnClickListener(this);
        txtPicture.setOnClickListener(this);
        txtDriveLicense.setOnClickListener(this);
        txtBusinessRegis.setOnClickListener(this);
        txtPolicy.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setColorSpan(txtTitle, 0, 9);
        setColorSpan(txtDesc, txtDesc.getText().toString().length() - 10, txtDesc.getText().toString().length());
        if(getParentFragment() instanceof  HomeListener)
        ((HomeListener)getParentFragment()).hideNavigationBar();


    }

    private void setColorSpan(TextView textView,  int fromPos,int toPos){
        SpannableStringBuilder sb = new SpannableStringBuilder(textView.getText());
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
        sb.setSpan(fcs, fromPos, toPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(getParentFragment() instanceof HomeListener)
            ((HomeListener)getParentFragment()).showNavigationBar();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtPicture:

                showDialogDelete(1,UIHelper.getStringFromTextView(txtPicture));
                break;
            case R.id.txtDriveLicense:
                showDialogDelete(2,UIHelper.getStringFromTextView(txtDriveLicense));
                break;
            case R.id.txtBusinessRegis:
                showDialogDelete(3,UIHelper.getStringFromTextView(txtBusinessRegis));
                break;
            case R.id.txtPolicy:
                checkBox.performClick();
                break;
            default:
                if(validate()){
                    userInfo.image_file_path=(strAvatar==null?"":strAvatar);
                    //register(userInfo);
                    userInfo.driver_type="1";
                    upgradeDriver(userInfo);
                    //MyPreference.setObject("userInfo",userInfo);

                }
                break;
        }

    }




    private void upgradeDriver( UserInfo userInfo){
        Log.d(TAG,new Gson().toJson(userInfo));
        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);
        Call<ResponseInfo> call = taskService.updateDriver(userInfo);
        final SimpleProgressDialog simpleProgressDialog=new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                simpleProgressDialog.dismiss();
                if(response.isSuccessful()){
                    UserInfo userInfo= new Gson().fromJson( response.body().data,UserInfo.class);
                    MyPreference.setObject("userInfo",userInfo);
                    if(wasPassenger)
                        getFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                            replace(R.id.fragment, StatusLoginFragment.init(),StatusLoginFragment.class.getName()).
                            commit();
                    else{

                        startActivity(new Intent(context, HomeActivity.class));
                        getActivity().finish();
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

    private boolean validate(){
        StringBuffer mesError=new StringBuffer();
        if(!checkBox.isChecked()){
            return false;
        }
        if(!TextUtils.isEmpty(mesError.toString())) {
            Utility.showMessage(context, mesError.toString());
            return false;
        }
        return true;
    }

    @Override
    public void onBackPress() {
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                replace(R.id.fragment, RegisterDriverFragment.init(getArguments().getBoolean("wasPassenger"),userInfo),RegisterDriverFragment.class.getName()).
                commit();
    }



    private void openImageIntent(int uploadIndex) {
        //EasyImage.openChooserWithGallery(this,"Please choose",0);

        File imageFile = ImageHelper.createFile(context, "extend_picture.jpg");
        String cameraImageFullPath = imageFile.getAbsolutePath();
        cameraOutputFileUri = Uri.fromFile(imageFile);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
                captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent,
                "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[] {}));

        startActivityForResult(chooserIntent, uploadIndex);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {


            if (resultCode == AppCompatActivity.RESULT_OK) {

                if (resultCode == Activity.RESULT_OK) {
                    final boolean isCamera;
                    if (data == null) {
                        isCamera = true;
                    } else {
                        final String action = data.getAction();
                        if (action == null) {
                            isCamera = false;
                        } else {
                            isCamera = action
                                    .equals(MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }
                    Uri selectedImageUri;
                    if (isCamera) {
                        selectedImageUri = cameraOutputFileUri;

                    } else {
                        selectedImageUri = data == null ? null : data.getData();

                    }
                    try {
                        Bitmap bitmap = decodeUri(
                                selectedImageUri);

                        String strPhoto = null;
                        try {
                            strPhoto = URLEncoder.encode(
                                    encodeBitmapBase64(bitmap), "utf-8");
                            Log.d(Constant.DEBUG_TAG,strPhoto);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        switch (requestCode) {
                            case 1:
                                txtPicture.setText("uploaded");
                                strAvatar=strPhoto;
                                break;
                            case 2:
                                txtDriveLicense.setText("uploaded");
                                //strDriverLicense=strPhoto;
                                break;
                            case 3:
                                txtBusinessRegis.setText("uploaded");
                                //strBusiness=strPhoto;
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }




            }
        }
    }

    private void showDialogDelete(final int uploadIndex,String fileText)
    {
        if(TextUtils.isEmpty(fileText)){
            openImageIntent(uploadIndex);
        }
        else{
            final CharSequence[] items = {"Change picture", "Remove"};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Please choose");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if(item==0)
                        openImageIntent(uploadIndex);
                    else {
                        switch (uploadIndex){
                            case 1:
                                txtPicture.setText("");
                                break;
                            case 2:
                                txtDriveLicense.setText("");
                                break;
                            case 3:
                                txtBusinessRegis.setText("");
                                break;
                        }
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                MyApplicationContext.getInstance().getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                MyApplicationContext.getInstance().getContentResolver().openInputStream(selectedImage), null, o2);
    }

    private String encodeBitmapBase64(Bitmap bitmap) {
        if (bitmap == null)
            return "";

        return Base64.encodeToString(UIHelper.convertBitmapToByteArray(bitmap),
                Base64.NO_WRAP);
    }
}
