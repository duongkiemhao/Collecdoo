package com.collecdoo.fragment.home;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.collecdoo.R;
import com.collecdoo.dto.BookingHistoryInfo;
import com.collecdoo.helper.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ListOfDriveDetailDialogFragment extends DialogFragment implements
        View.OnClickListener {

    @BindView(R.id.btnClose)
    TextView btnClose;
    @BindView(R.id.txtDetail)
    TextView txtDetail;
    @BindView(R.id.txtFrom)
    TextView txtFrom;
    @BindView(R.id.txtTo)
    TextView txtTo;
    @BindView(R.id.txtPickup)
    TextView txtPickup;
    @BindView(R.id.txtDrop)
    TextView txtDrop;
    @BindView(R.id.txtCost)
    TextView txtCost;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.txtNote)
    TextView txtNote;
    private Context context;
    private Unbinder unbinder;
    private BookingHistoryInfo detailInfo;

    public static ListOfDriveDetailDialogFragment init(BookingHistoryInfo detailInfo) {
        ListOfDriveDetailDialogFragment dialogFragment = new ListOfDriveDetailDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("detailInfo", detailInfo);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailInfo = getArguments().getParcelable("detailInfo");
        // setStyle(DialogFragment.STYLE_NO_FRAME,
        // R.style.TransparentFragmentDialog);
        // DialogFragment.STYLE_NO_TITLE, 0
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                // disable search button action
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;

                }
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_of_drive_detail_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        btnClose.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            Date date = new SimpleDateFormat(DateHelper.SERVER_DATE_FORMAT).parse(detailInfo.createdOn);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
            SimpleDateFormat timeormat = new SimpleDateFormat("HH:mm");

            txtDetail.setText(dateFormat.format(date));
            txtFrom.setText(detailInfo.pickupInfo);
            txtTo.setText(detailInfo.dropInfo);
            Date timeFrom = new SimpleDateFormat(DateHelper.SERVER_DATE_FORMAT).parse(detailInfo.getDesiredPickupTime());
            txtPickup.setText(timeormat.format(timeFrom));

            txtCost.setText(detailInfo.getFare());
            txtNote.setText(detailInfo.getNotes());
            Date timeTo = new SimpleDateFormat(DateHelper.SERVER_DATE_FORMAT).parse(detailInfo.getDesiredDropTime());
            txtDrop.setText(timeormat.format(timeTo));
            if (!TextUtils.isEmpty(detailInfo.getRating()))
                ratingBar.setNumStars(Integer.parseInt(detailInfo.getRating()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                dismiss();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
