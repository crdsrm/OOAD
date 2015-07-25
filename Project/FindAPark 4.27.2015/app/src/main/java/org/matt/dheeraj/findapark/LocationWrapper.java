package org.matt.dheeraj.findapark;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Matt on 4/10/2015.
 */
public class LocationWrapper implements ConnectionCallbacks, OnConnectionFailedListener{

    //DATA MEMBERS
    private GoogleApiClient googleApiClient;
    private Geocoder geocoder;
    private Location location;
    private Context context;

    //PUBLIC METHODS
    public static double getDistance(Address here, Address there) {
        float[] results = {};
        Location.distanceBetween(here.getLatitude(),here.getLongitude(),there.getLatitude(),there.getLongitude(), results);
        return results[0];
    }
    public Address getDeviceLocation() throws IOException{
        if (location == null) {
            throw new IOException("Unable to find location");
        }else {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
            if (addresses == null || addresses.size() == 0) {
                throw new IOException("Unable to find location");
            }
            return addresses.get(0);
        }
    }
    public Address getLocationFromString(String s) throws IOException{
        List<Address> addresses = geocoder.getFromLocationName(s, 5);
        if (addresses == null || addresses.size() == 0) {
            throw new IOException("Address not found");
        }
        return addresses.get(0);
    }

    //CONSTRUCTORS
    public LocationWrapper(Context c){
        context = c;
        buildGoogleApiClient();
        googleApiClient.connect();
        geocoder = new Geocoder(context, Locale.US);

    }

    //INTERFACE METHODS
    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(context,"Connected",Toast.LENGTH_SHORT).show();
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        //Toast.makeText(context,location.getLatitude() + " " + location.getLongitude(),Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(context,"Suspended",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
    }
}
