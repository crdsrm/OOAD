package org.matt.dheeraj.findapark;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Matt on 4/9/2015.
 *
 */
public class ParkFeature implements Parcelable{
    //DATA MEMBERS
    private String name;

    //PUBLIC METHODS
    public String getName() {return name;}

    //CONSTRUCTOR
    public ParkFeature(String n) {name = n;}

    //PARCELABLE INTERFACE METHODS
    @Override
    public boolean equals(Object o) {
        if (o.getClass() == ParkFeature.class)
            return name.equals(((ParkFeature) o).getName());
        else
            throw new IllegalArgumentException();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
    public ParkFeature(Parcel in) {
        name = in.readString();
    }
    public static final Parcelable.Creator<ParkFeature> CREATOR
            = new Parcelable.Creator<ParkFeature>() {
        public ParkFeature createFromParcel(Parcel in) {
            return new ParkFeature(in);
        }

        public ParkFeature[] newArray(int size) {
            return new ParkFeature[size];
        }
    };
}
