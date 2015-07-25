package org.matt.dheeraj.findapark;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/9/2015.
 */

public class FeatureList implements Parcelable{

    // DATA MEMBERS
    private List<ParkFeature> features;

    //PUBLIC METHODS
    public void add(ParkFeature f) {features.add(f);}
    public int size() {return features.size();}
    public ParkFeature get(int index) {return features.get(index);}
    public boolean exists(ParkFeature f) { return features.contains(f); }
    public void remove(ParkFeature f) { features.remove(f); }
    //Possible improvement: add method for generating list of Feature checkboxes

    //CONSTRUCTORS
    public FeatureList() {
        features = new ArrayList<ParkFeature>();
    }

    //PARCELABLE INTERFACE METHODS
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(features);
    }
    public FeatureList(Parcel in) {
        features = in.readArrayList(ParkFeature.class.getClassLoader());
    }
    public static final Parcelable.Creator<FeatureList> CREATOR
            = new Parcelable.Creator<FeatureList>() {
        public FeatureList createFromParcel(Parcel in) {
            return new FeatureList(in);
        }

        public FeatureList[] newArray(int size) {
            return new FeatureList[size];
        }
    };
}
