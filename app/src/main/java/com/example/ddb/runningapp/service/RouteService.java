package com.example.ddb.runningapp.service;

import android.content.Context;

import com.example.ddb.runningapp.DatabaseClass;
import com.example.ddb.runningapp.pojo.Route;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RouteService {
    List<Route> routes;
    DatabaseClass db;
    int routeNo;
    Context context;
    LatLng currentPosition;
    double distance = 0;
    boolean trackingActive;

    protected RouteService(Context context) {
        this.context=context;
        db = new DatabaseClass(context);
        routes = db.getRoutes();


    }


    public ArrayList<Route> getRoutes(){
        return (ArrayList<Route>) db.getRoutes();
    }

    public void newTrack(){
        this.distance=0;
        routeNo = db.getNewTrackNo();
        trackingActive=true;
        db.addRoute(routeNo);
        db.addLocation(routeNo, currentPosition,(float)distance);

    }

    public void track() {
        db.addLocation(routeNo, currentPosition,(float)distance);
        System.out.println("trackDist: " +(float) distance);
    }

    public void endTrack(){
        db.updateTime(routeNo);
        List<LatLng> locs = db.getLocationOfRoute(routeNo);
        if(locs.isEmpty() || locs.size()<=1){ //delete's route again if there's no location entry
            db.removeRoute(routeNo);
        }
        trackingActive=false;
    }

    public LatLng getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(LatLng currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void removeRoute(int no){
        db.removeRoute(no);
    }

    public void setDistance(int dist){
        if(dist>0){
            this.distance= (dist)/1000.0 +distance;
        }



    }

    public boolean isTrackingActive() {
        return trackingActive;
    }

    public Route getRoute(int routeNo){
        routes=db.getRoutes();
        Route route = null;
        for(Route r: routes){
            if(r.getNo() ==routeNo){
                route= r;
            }

        }
        return route;
    }

    public Route getCurrentRoute(){
        return getRoute(routeNo);
    }

    public List<LatLng> getLocationsOfRoute(int routeNo){
       List<LatLng> list = db.getLocationOfRoute(routeNo);
        System.out.println("ListSize: " + list.size());
        System.out.println("List: " +list);
        return list;
    }

    public void setTrackingActive(boolean trackingActive) {
        this.trackingActive = trackingActive;
    }

    public double getDistance(){
        return this.distance;
    }
}
