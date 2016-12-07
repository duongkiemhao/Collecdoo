package com.collecdoo.fragment.pickup;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.collecdoo.R;
import com.collecdoo.config.Constant;
import com.collecdoo.dto.PathOfRouteInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class DriverPickupDropStopsFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private StopsAdapter stopsAdapter;
    private Unbinder unbinder;
    private Context context;

    public static DriverPickupDropStopsFragment init(List<PathOfRouteInfo> pathOfRouteInfoList) {
        DriverPickupDropStopsFragment registerFragment = new DriverPickupDropStopsFragment();
        Bundle bundle = new Bundle();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_pickup_drop_stops_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constant.DEBUG_TAG, "in stops");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        stopsAdapter = new StopsAdapter(context);
        recyclerView.setAdapter(stopsAdapter);

        loadData();

    }

    private void loadData() {
        List<PathOfRouteInfo> pathOfRouteInfoList = getArguments().getParcelableArrayList("list");

        stopsAdapter.setList(pathOfRouteInfoList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnProfil:

                break;
        }

    }


//    @Override
//    public void onBackPress() {
//        getFragmentManager().beginTransaction().
//                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
//                replace(R.id.fragment, RegisterDriverPhotoFragment.init(), ConstantTabTag.REGISTER_DRIVER_PHOTO).
//                commit();
//    }


    class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.MyViewHolder> {
        private List<PathOfRouteInfo> list;
        private Context context;


        public StopsAdapter(Context context) {
            this.context = context;
            this.list = new ArrayList<>();

        }

        public void setList(List<PathOfRouteInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.driver_pickup_drop_list_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int pos) {
            //songViewHolder.textView.setText(list.get(pos));

//            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ListOfDriveDetailDialogFragment dialog=new ListOfDriveDetailDialogFragment();
//                    dialog.show(getFragmentManager(),"listOfDriveDetail");
//                }
//            });
            PathOfRouteInfo pathOfRouteInfo = list.get(pos);
            myViewHolder.txtName.setText(pathOfRouteInfo.customer_name);
            myViewHolder.txtAddress.setText(pathOfRouteInfo.destinationInfo);
            myViewHolder.txtMobile.setText(pathOfRouteInfo.telephone);
            myViewHolder.imaPin.setImageResource(pathOfRouteInfo.is_delivery.equals("0") ? R.drawable.ico_pin_from : R.drawable.ico_pin_to);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public
            @BindView(R.id.txtName)
            TextView txtName;
            public
            @BindView(R.id.txtMobile)
            TextView txtMobile;
            public
            @BindView(R.id.txtAddress)
            TextView txtAddress;
            public
            @BindView(R.id.imaPin)
            ImageView imaPin;


            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }

    class StopsInfo {
        public String name;
        public String mobile;
        public String address;

        public StopsInfo(String name, String mobile, String address) {
            this.name = name;
            this.mobile = mobile;
            this.address = address;
        }
    }


}
