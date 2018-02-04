package com.example.ddb.runningapp;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ddb.runningapp.pojo.Route;
import com.example.ddb.runningapp.service.GMapV2Direction;
import com.example.ddb.runningapp.service.GMapV2DirectionAsyncTask;
import com.example.ddb.runningapp.service.RouteService;
import com.example.ddb.runningapp.service.ServiceInstantiator;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment implements  OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;
    private final int ZOOMLEVEL = 10;
    Context context;
    Marker marker;
    LatLng latLng;
    RouteService service;
    private int routeNo;
    Route r;
    List<LatLng> locs ;
    GMapV2DirectionAsyncTask asyncTask;
    Marker markStart;
    public InfoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        getActivity().setTitle("Tracking Information");
        context = getActivity().getBaseContext();
        service= ServiceInstantiator.getInstance(context);
        routeNo=getActivity().getIntent().getIntExtra("RouteNo", 0);
        System.out.println(routeNo);
        r = service.getRoute(routeNo);
        locs = service.getLocationsOfRoute(routeNo);
        setTexts(view);
        mapView = (MapView) view.findViewById(R.id.info_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        locs = service.getLocationsOfRoute(routeNo);
        latLng = locs.get(0);
        markStart = map.addMarker(new MarkerOptions().position(latLng).title("Start"));

        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        map.setMyLocationEnabled(false);

        map.getUiSettings().setZoomControlsEnabled(true);
        MapsInitializer.initialize(this.getActivity());
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(55.854049, 13.661331));
        LatLngBounds bounds = builder.build();
        int padding = 5;
        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
         //map.moveCamera(cameraUpdate);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOMLEVEL));
        //route(locs.get(0), locs.get(0+1));
        draw();
    }

    private  void draw(){
        for(int i = 0; i<locs.size(); i++){
            System.out.println("drawing");
            if(i==0){
                route(new LatLng(0,0), locs.get(i));
            }
            if(i == locs.size()-1){
                map.addMarker(new MarkerOptions().position(locs.get(i)).title("End"));
                return;
            }
            else{
                route(locs.get(i), locs.get(i+1));
            }
        }

    }

    protected void route(final LatLng sourcePosition, final LatLng destPosition) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    Document doc = (Document) msg.obj;
                    GMapV2Direction md = new GMapV2Direction();
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.GREEN);

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    Polyline polylin = map.addPolyline(rectLine);
                    md.getDurationText(doc);
                    map.addPolyline(rectLine);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        };

            asyncTask= (GMapV2DirectionAsyncTask) new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, GMapV2Direction.MODE_WALKING).execute();

        }



    private void setTexts(View view){
        TextView distance = (TextView) view.findViewById(R.id.distanceView);
        TextView start = (TextView) view.findViewById(R.id.startView);
        TextView end = (TextView) view.findViewById(R.id.endView);
        TextView duration= (TextView) view.findViewById(R.id.durationView);

        System.out.println(r);
        if(r!=null){
            distance.setText(String.valueOf(r.getDistance()));
            start.setText(r.getFormattedDate(r.getDate()));
            end.setText(r.getFormattedDate(r.getDuration()));
            duration.setText(r.getDur());
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
