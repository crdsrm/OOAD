package org.matt.dheeraj.findapark;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/10/2015.
 */

public class ParkCollection implements Parcelable{

    //DATA MEMBERS
    private List<Park> parkList;
    private List<Park> visible;
    private FeatureList filterList;

    //PUBLIC METHODS
    public int size() { return parkList.size(); }
    public void add(Park p) {
        parkList.add(p);
        filter(filterList);
    }
    public Park getPark(int index) {return parkList.get(index); }
    public void setPark(int index, Park p) {
        parkList.set(index, p);
        filter(filterList);
    }
    public List<ParkView> getParkClickers(Context context) {
        List<ParkView> cvList = new ArrayList<>(visible.size());
        for (Park park:visible)
            cvList.add( new ParkView(context,  park));
        return cvList;
    }
    public void sortByDistance(Address address) {

        float [] results = new float[2];
        results[0] = 0f; results[1] = 0f;
        ArrayList<Float> distances = new ArrayList<Float>();

        for (int i = 0; i < parkList.size(); i++){

            if (!address.hasLatitude() || !address.hasLongitude() || !parkList.get(i).getAddress().hasLatitude() || !parkList.get(i).getAddress().hasLongitude()) {
                distances.add(i,Float.MAX_VALUE);
            } else{
                Location.distanceBetween(address.getLatitude(), address.getLongitude(), parkList.get(i).getAddress().getLatitude(), parkList.get(i).getAddress().getLongitude(), results);
                distances.add(results[0]);
            }

        }

        Park tempPark;
        Float tempDistance;
        for (int i =0; i < parkList.size()-1;i++){
            for (int j = i+1; j < parkList.size(); j++){
                if (distances.get(i) > distances.get(j)){
                    tempPark = parkList.get(i);
                    tempDistance = distances.get(i);
                    parkList.set(i,parkList.get(j));
                    distances.set(i,distances.get(j));
                    parkList.set(j,tempPark);
                    distances.set(j,tempDistance);
                }
            }
        }

        filter(filterList);
    }
    public void sortByRating() {
        Park temp;
        for(int i = 0; i < parkList.size()-1; i++){
            for(int j = i+1; j < parkList.size(); j++){
                if (parkList.get(i).getRating() < parkList.get(j).getRating()) {
                    temp = parkList.get(i);
                    parkList.set(i,parkList.get(j));
                    parkList.set(j,temp);
                }
            }
        }
        filter(filterList);
    }
    public void filter(FeatureList selectedFeatures) {
        filterList = selectedFeatures;
        visible = new ArrayList<Park>();
        for (Park p : parkList) {
            for (int i = 0; i < selectedFeatures.size(); i++) {
                if (p.hasFeature(selectedFeatures.get(i)) && !visible.contains(p)) {
                    visible.add(p);
                }
            }
        }
    }
    public void removeFilter(){
        filterList = this.getDefaultFeatures();
        filter(filterList);
    }

    //CONSTRUCTORS
    public ParkCollection(){
        parkList  = new ArrayList<>(0);
        removeFilter();
    }
    public ParkCollection(List<Park> pl){
        this.parkList = pl;
        removeFilter();
    }

    //PRIVATE HELPER METHODS
    private FeatureList getDefaultFeatures(){
        return(new DBBridge()).getAllFeatures();
    }

    //PARCELABLE METHODS
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(parkList);
    }
    public ParkCollection(Parcel in) {
        parkList = new ArrayList<Park>();
        in.readList(parkList,Park.class.getClassLoader());
        removeFilter();
    }
    public static final Parcelable.Creator<ParkCollection> CREATOR
            = new Parcelable.Creator<ParkCollection>() {
        public ParkCollection createFromParcel(Parcel in) {
            return new ParkCollection(in);
        }

        public ParkCollection[] newArray(int size) {
            return new ParkCollection[size];
        }
    };
}
