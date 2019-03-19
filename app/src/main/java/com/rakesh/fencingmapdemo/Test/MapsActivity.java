package com.rakesh.fencingmapdemo.Test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.rakesh.fencingmapdemo.R;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    //private GoogleMap mMap;
    private GoogleMap mMap;

    private String TAG = "MapsActivity";
    Handler handler = new Handler();
    private int countown = -1;

    ////
    private GoogleMap googleMap1;
    private ArrayList<LatLng> arrayPoints = null;
    PolylineOptions polylineOptions;
    private boolean checkClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getApiData();
/////

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //   googleMap = fm.getMap();
        fm.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap1.setMyLocationEnabled(true);

//        googleMap1.setOnMapClickListener(this);
//        googleMap1.setOnMapLongClickListener(this);
//        googleMap1.setOnMarkerClickListener(this);


    }

    private void addCirclestaticData() {
        LatLng latLng = new LatLng(17.3984, 78.5583);
        if (latLng != null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            //   circleOptions.radius(3.5);
            // circleOptions.fillColor(Color.BLUE);
            circleOptions.fillColor(Color.TRANSPARENT);
            // circleOptions.strokeWidth(8);
            mMap.addCircle(circleOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                    .title("test Demo"));
        }
        addCircle(latLng, 100);
        setRunnablehandler(latLng);

    }


    private void setRunnablehandler(final LatLng present) {
        countown++;
        Runnable runnable = new Runnable() {
            public void run() {
                getApiData();

            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void getApiData() {

        StringRequest strReq = new StringRequest(Request.Method.GET,
                "http://192.168.0.190:1010/vehicleStatusInformation/getVehicleDetails/123/05-12-2018", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d(TAG, "Login Response: " + response.toString());
                //   hideDialog();

                Log.e("response", response);
                if (!response.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    HistoryCore historyCore = gson.fromJson(response, HistoryCore.class);
                    if (historyCore != null && historyCore.getHistory() != null && historyCore.getHistory().size() > 0) {
                        addDataToViews(historyCore.getHistory());
                    }
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Login Error: " + error.getMessage());

//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//
                addCirclestaticData();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Volley.newRequestQueue(this).add(strReq);


    }

    private void addDataToViews(ArrayList<HistoryData> history) {

        for (int i = 0; i < history.size(); i++) {
            Log.e("lattitude", history.get(i).getLatitude() + "");
            Log.e("Login tude", history.get(i).getLongitude() + "");

            LatLng latLng = new LatLng(history.get(i).getLatitude(), history.get(i).getLongitude());
            //   LatLng latLng = new LatLng(17.3984,78.5583);

            if (latLng != null) {
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(latLng);
                //   circleOptions.radius(3.5);
                // circleOptions.fillColor(Color.BLUE);
                circleOptions.fillColor(Color.TRANSPARENT);
                // circleOptions.strokeWidth(8);
                mMap.addCircle(circleOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                        .title("test Demo"));
            }
            addCircle(latLng, 100);
            setRunnablehandler(latLng);

        }


    }

    private void addCircle(LatLng latLng, double radius) {
        double R = 6371d; // earth's mean radius in km
        double d = radius / R; //radius given in km
        double lat1 = Math.toRadians(latLng.latitude);
        double lon1 = Math.toRadians(latLng.longitude);
        PolylineOptions options = new PolylineOptions();
        for (int x = 0; x <= 360; x++) {
            double brng = Math.toRadians(x);
            double latitudeRad = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1) * Math.sin(d) * Math.cos(brng));
            double longitudeRad = (lon1 + Math.atan2(Math.sin(brng) * Math.sin(d) * Math.cos(lat1), Math.cos(d) - Math.sin(lat1) * Math.sin(latitudeRad)));
            options.add(new LatLng(Math.toDegrees(latitudeRad), Math.toDegrees(longitudeRad)));
        }
        mMap.addPolyline(options.color(Color.BLACK).width(2));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap1=googleMap;
        googleMap1.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap1.setMyLocationEnabled(true);
        googleMap1.setTrafficEnabled(true);
        googleMap1.setIndoorEnabled(true);
        googleMap1.setBuildingsEnabled(true);
        googleMap1.getUiSettings().setZoomControlsEnabled(true);

    }




    // for click


    /*
    @Override
    public void onMapClick(LatLng latLng) {
        if (checkClick == false) {
            googleMap1.addMarker(new MarkerOptions().position(latLng).icon( BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));
            arrayPoints.add(latLng); }

       }

    @Override
    public void onMapLongClick(LatLng latLng) {

        googleMap1.clear();
        arrayPoints.clear();
        checkClick = false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("Marker lat long=" + marker.getPosition());
        System.out.println("First postion check" + arrayPoints.get(0));
        System.out .println("**********All arrayPoints***********" + arrayPoints);
        if (arrayPoints.get(0).equals(marker.getPosition()))
        { System.out.println("********First Point choose************");
        countPolygonPoints();
        } return false;
    }

*/
    public void countPolygonPoints() {
        if (arrayPoints.size() >= 3) {
            checkClick = true; PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.addAll(arrayPoints);
            polygonOptions.strokeColor(Color.BLUE);
            polygonOptions.strokeWidth(7);
            polygonOptions.fillColor(Color.CYAN);
            Polygon polygon = googleMap1.addPolygon(polygonOptions); }
    }
    }
