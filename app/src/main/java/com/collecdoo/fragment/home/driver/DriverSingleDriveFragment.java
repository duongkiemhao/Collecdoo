package com.collecdoo.fragment.home.driver;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.collecdoo.MyApplicationContext;
import com.collecdoo.MyPreference;
import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Constant;
import com.collecdoo.control.InstantAutoComplete;
import com.collecdoo.control.SimpleProgressDialog;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.dto.ShareDriveInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.BaseFragment;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.home.TextToSpeedManager;
import com.collecdoo.fragment.parser.DirectionsJSONParser;
import com.collecdoo.fragment.parser.PlaceDetailsJSONParser;
import com.collecdoo.fragment.parser.PlaceJSONParser;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A placeholder fragment containing a simple view.
 */
public class DriverSingleDriveFragment extends BaseFragment implements View.OnClickListener, OnBackListener,
        OnMapReadyCallback, DatePickerDialog.OnDateSetListener, HomeNavigationListener {
    private final int PLACES = 0;
    private final int PLACES_DETAILS = 1;
    private final String TAG = "--driver single--";
    @BindView(R.id.txtFrom)
    InstantAutoComplete txtFrom;
    @BindView(R.id.txtTo)
    InstantAutoComplete txtTo;
    @BindView(R.id.imaSearch)
    ImageView imaSearchFrom;
    @BindView(R.id.imaSearchTo)
    ImageView imaSearchTo;
    @BindView(R.id.imaQuestion)
    ImageView imaQuestionFrom;
    @BindView(R.id.imaQuestionTo)
    ImageView imaQuestionTo;
    @BindView(R.id.ediFreeSeat)
    EditText ediFreeSeat;

    @BindView(R.id.txtDatePicker)
    TextView txtDatePicker;
    @BindView(R.id.btnOk)
    Button btnOk;
    private GoogleMap googleMap;
    private LatLng startLat;
    private LatLng endLat;
    private ArrayList<LatLng> markerPoints;
    private String browserKey = "AIzaSyBwRYVxhnE8LGKvve6Mq75ke0dkaVp39hQ";
    private boolean isStartClick;
    private int index;
    private Unbinder unbinder;
    private Context context;
    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            SimpleDateFormat mFormatter = new SimpleDateFormat("MMM dd yyyy HH:mm");
            txtDatePicker.setText(mFormatter.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };

    public static DriverSingleDriveFragment init(int index) {
        DriverSingleDriveFragment fragment = new DriverSingleDriveFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getArguments().getInt("index");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_single_drive_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        imaSearchFrom.setOnClickListener(this);
        imaSearchTo.setOnClickListener(this);

        txtDatePicker.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        supportMapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment, supportMapFragment, "map").commit();

        // Getting a reference to the AutoCompleteTextView
        setupAutoCompleteTextView(txtFrom);
        setupAutoCompleteTextView(txtTo);

        txtFrom.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextToSpeedManager.startTTS(DriverSingleDriveFragment.this, FROM_REQUEST_CODE);
                return false;
            }
        });
        txtTo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextToSpeedManager.startTTS(DriverSingleDriveFragment.this,TO_REQUEST_CODE);
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FROM_REQUEST_CODE :
                processTTSResult(requestCode,resultCode,data);
                break;
            case TO_REQUEST_CODE:
                processTTSResult(requestCode,resultCode,data);
                break;
        }
    }

    private void processTTSResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK && null != data) {
            ArrayList<String> text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            (requestCode == FROM_REQUEST_CODE ? txtFrom : txtTo).setText(text.get(0));
        }
    }

    private void setupAutoCompleteTextView(final AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setThreshold(1);
        Point pointSize = new Point();

        ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getSize(pointSize);

        autoCompleteTextView.setDropDownWidth(pointSize.x);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (autoCompleteTextView.getId()) {
                    case R.id.txtFrom:
                        isStartClick = true;
                        break;
                    case R.id.txtTo:
                        isStartClick = false;
                        break;
                }
                autoCompleteTask(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long id) {
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();
                HashMap<String, String> hm = new HashMap<String, String>();
                try {
                    hm = (HashMap<String, String>) adapter.getItem(index);
                } catch (Exception exp) {
                    LinkedTreeMap<String, String> tmap = (LinkedTreeMap<String, String>) adapter.getItem(index);
                    for (String key : tmap.keySet()) {
                        hm.put(key, tmap.get(key));
                    }
                }
                HashSet<HashMap<String, String>> hashSet = (HashSet<HashMap<String, String>>) MyPreference.getObjectHashsetMap(Constant.PRE_LIST_SUGGESTION, HashSet.class);
                hashSet.add(hm);
                MyPreference.setObject(Constant.PRE_LIST_SUGGESTION, hashSet);

                autoCompleteTextView.setText(hm.get("description"));
                String url = getPlaceDetailsUrl(hm.get("reference"));
                placeTask(url);

            }
        });


//
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imaSearch:
                drawMap();
                break;
            case R.id.imaSearchTo:
                drawMap();
                break;
            case R.id.txtDatePicker:
                showDatePicker();
                break;
            case R.id.btnOk:
                if (!validate())
                    return;
                UserInfo userInfo = (UserInfo) MyPreference.getObject("userInfo", UserInfo.class);
                ShareDriveInfo shareInfo = new ShareDriveInfo();
                shareInfo.setUserId(userInfo.user_id);
                shareInfo.setFromAddress(txtFrom.getText().toString());
                shareInfo.setLat1(startLat.latitude + "");
                shareInfo.setLon1(startLat.longitude + "");
                shareInfo.setToAddress(txtTo.getText().toString());
                shareInfo.setLat2(endLat.latitude + "");
                shareInfo.setLon2(endLat.longitude + "");
                shareInfo.setOwnDistance(Utility.calculationByDistance(startLat, endLat) + "");
                shareInfo.setFreeSeats(UIHelper.getStringFromEditText(ediFreeSeat));
                shareInfo.setType(index + "");

                SimpleDateFormat fromFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
                SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                try {
                    Date date = fromFormat.parse(txtDatePicker.getText().toString());
                    shareInfo.setDesiredPickupTime(toFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                shareInfo.setDesiredDropTime("");

                shareDrive(shareInfo);
                break;
        }

    }

    @OnClick(R.id.imaQuestion)
    void onQuesionFromClick() {
        isStartClick = true;
        onProcessQuestionClick();
    }

    @OnClick(R.id.imaQuestionTo)
    void onQuesionToClick() {
        isStartClick = false;
        onProcessQuestionClick();
    }

    private void onProcessQuestionClick() {
        HashSet<HashMap<String, String>> lHMFrom = (HashSet<HashMap<String, String>>) MyPreference.getObject(Constant.PRE_LIST_SUGGESTION, HashSet.class);
        if (lHMFrom != null) {
            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};
            List<HashMap<String, String>> hashMapList = new ArrayList<HashMap<String, String>>(lHMFrom);
            SimpleAdapter adapter = new SimpleAdapter(context, hashMapList, android.R.layout.simple_list_item_1, from, to);

            if (isStartClick) {
                txtFrom.showDropDown();
                txtFrom.setAdapter(adapter);
            } else {
                txtTo.showDropDown();
                txtTo.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private boolean validate() {
        if (startLat == null || endLat == null ||
                TextUtils.isEmpty(UIHelper.getStringFromEditText(ediFreeSeat))
                || TextUtils.isEmpty(UIHelper.getStringFromTextView(txtDatePicker)))
            return false;
        SimpleDateFormat fromFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
        try {
            Date date = fromFormat.parse(txtDatePicker.getText().toString());
            if (date.compareTo(new Date()) <= 0)
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
                        txtFrom.setText("");
                        txtTo.setText("");
                        ediFreeSeat.setText("");
                        txtDatePicker.setText("");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        txtDatePicker.setText(formatCalendarOutput(calendar.get(Calendar.MONTH) + 1) + "/" +
                                formatCalendarOutput(calendar.get(Calendar.DAY_OF_MONTH)) + "/" +
                                calendar.get(Calendar.YEAR));

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

    private void showDatePicker() {
        SimpleDateFormat mFormatter = new SimpleDateFormat("MMM dd yyyy HH:mm");
        Date date = new Date();
        try {
            date = mFormatter.parse(txtDatePicker.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SlideDateTimePicker slideDateTimePicker = new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(date)
                .setMinDate(new Date())
                .build();
        if (index == 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 7);
            Log.d(TAG, mFormatter.format(calendar.getTime()));
            slideDateTimePicker.setMaxDate(calendar.getTime());
        }
        slideDateTimePicker.show();

    }


    private void shareDrive(ShareDriveInfo shareInfo) {
        Log.d(TAG, shareInfo.toString());
        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);

        Call<ResponseInfo> call = taskService.shareDrive(shareInfo);
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, retrofit2.Response<ResponseInfo> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseInfo shareWrapperInfo = response.body();
                    Log.d(TAG, shareWrapperInfo.toString());
                    if (shareWrapperInfo.status.toLowerCase().equals("ok")) {
                        showNextDialog();
                    } else Utility.showMessage(context, shareWrapperInfo.message);
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
    public void onBackPress() {
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                replace(R.id.fragment, DriverHomeFragment.init(), DriverHomeFragment.class.getName()).
                commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.setPadding(0, 0, 0, (int) Utility.convertDPtoPIXEL(TypedValue.COMPLEX_UNIT_DIP, 40));
        markerPoints = new ArrayList<>();
//        startLat=new LatLng(43.6533103,-79.3827675);
//        endLat=new LatLng(45.5017123,-73.5672184);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        txtDatePicker.setText(formatCalendarOutput(monthOfYear + 1) + "/" + formatCalendarOutput(dayOfMonth) + "/" + year);
    }

    private String formatCalendarOutput(int value) {
        if (value < 10)
            return "0" + value;
        else return value + "";
    }

    //------------map------------
    private void drawMap() {
        if (startLat == null || endLat == null)
            return;
        // Already two locations
        if (markerPoints.size() > 1) {
            markerPoints.clear();
            googleMap.clear();
        }

        // Adding new item to the ArrayList
        markerPoints.add(startLat);
        markerPoints.add(endLat);

        // Creating MarkerOptions
        MarkerOptions markerStart = new MarkerOptions();
        markerStart.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_pin_from));
        markerStart.position(startLat);
        markerStart.title(txtFrom.getText().toString().trim());
        googleMap.addMarker(markerStart);

        MarkerOptions markerEnd = new MarkerOptions();
        markerEnd.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_pin_to));
        markerEnd.title(txtTo.getText().toString().trim());
        markerEnd.position(endLat);
        googleMap.addMarker(markerEnd);


        // Add new marker to the Google Map Android API V2


        // Checks, whether start and end locations are captured
        if (markerPoints.size() >= 2) {
            LatLng origin = markerPoints.get(0);
            LatLng dest = markerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d(Constant.DEBUG_TAG, url);
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("error while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
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
        if (validate())
            showNextDialog();
    }

    //this is method to help us fit the Markers into specific bounds for camera position
    public void zoomAnimateLevelToFitMarkers(int padding) {
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        b.include(startLat);
        b.include(endLat);
        LatLngBounds bounds = b.build();

        // Change the padding as per needed
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }

    //-------google place----------
    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyBwRYVxhnE8LGKvve6Mq75ke0dkaVp39hQ";

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }

    public void autoCompleteTask(String place) {
        String input = "";
        try {
            input = "input=" + URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String output = "json";
        String parameter = input + "&types=geocode&sensor=false&key="
                + browserKey;
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
                + output + "?" + parameter;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    ParserPlaceTask placesParserTask = new ParserPlaceTask(PLACES);
                    placesParserTask.execute(response.toString());
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplicationContext.getInstance().addToRequestQueue(jsonObjReq, "jreq");
    }

    public void placeTask(String url) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    ParserPlaceTask placesParserTask = new ParserPlaceTask(PLACES_DETAILS);
                    placesParserTask.execute(response.toString());
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplicationContext.getInstance().addToRequestQueue(jsonObjReq, "jreq");
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            Log.d(Constant.DEBUG_TAG, "---ParserTask----");
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            Log.d(Constant.DEBUG_TAG, "---onPostExecute----");
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }
            Log.d(Constant.DEBUG_TAG, "---end----");
            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);
            zoomAnimateLevelToFitMarkers(120);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserPlaceTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        int parserType = 0;

        public ParserPlaceTask(int type) {
            this.parserType = type;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserPlaceTask", "---ParserPlaceTask---" + jObject.toString());

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch (parserType) {
                case PLACES:
                    String[] from = new String[]{"description"};
                    int[] to = new int[]{android.R.id.text1};

                    SimpleAdapter adapter = new SimpleAdapter(context, result, android.R.layout.simple_list_item_1, from, to);

                    if (isStartClick)
                        txtFrom.setAdapter(adapter);
                    else txtTo.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case PLACES_DETAILS:
                    HashMap<String, String> hm = result.get(0);

                    // Getting latitude from the parsed data
                    double latitude = Double.parseDouble(hm.get("lat"));

                    // Getting longitude from the parsed data
                    double longitude = Double.parseDouble(hm.get("lng"));

                    // Getting reference to the SupportMapFragment of the activity_main.xml

                    if (isStartClick)
                        startLat = new LatLng(latitude, longitude);
                    else endLat = new LatLng(latitude, longitude);

//                    CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
//                    CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(5);
//
//                    // Showing the user input location in the Google Map
//                    googleMap.moveCamera(cameraPosition);
//                    googleMap.animateCamera(cameraZoom);
//
//                    MarkerOptions options = new MarkerOptions();
//                    options.position(point);
//                    options.title("Position");
//                    options.snippet("Latitude:"+latitude+",Longitude:"+longitude);
//
//                    // Adding the marker in the Google Map
//                    googleMap.addMarker(options);

                    break;
            }
        }
    }


}
