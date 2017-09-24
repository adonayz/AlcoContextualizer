package edu.wpi.alcoholcontextualizer.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by tupac on 1/24/2017.
 */

public class Location {

    private int locationId;
    private String locationName;
    private double longitude;
    private double latitude;

    /* Number of times visited to this location */
    private int frequencyAmount;

    /* Most recent visit date to this location */
    //TODO: data type for date?
    private String recentDate;


    public Location() {
        this.locationId = 0;
        this.locationName = "";
        this.longitude = 0.0;
        this.latitude = 0.0;
    }

    public Location(int id, String locationName, double latitude, double longitude, int frequencyAmount, String date) {
        this.locationId = id;
        this.recentDate = date;
        this.frequencyAmount = frequencyAmount;
        this.latitude = latitude;
        this.locationName = locationName;
        this.longitude = longitude;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getRecentDate() {
        return recentDate;
    }

    public void setRecentDate(String recentDate) {
        this.recentDate = recentDate;
    }

    public int getFrequencyAmount() {
        return frequencyAmount;
    }

    public void setFrequencyAmount(int frequencyAmount) {
        this.frequencyAmount = frequencyAmount;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public android.location.Location lastKnownDeviceLocation(Context context) {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context
        );
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        final android.location.Location[] result = new android.location.Location[1];

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if (location != null) {
                            result[0] = location;
                        }
                    }
                });

        return result[0];
    }
}
