package org.matt.dheeraj.findapark;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Matt on 4/9/2015.
 */
public class ParkRating implements Parcelable {
    //DATA MEMBERS
    private Integer counter;
    private Integer sum;

    //PUBLIC METHODS
    public float getRating() {
        if (counter > 0) {
            return (float) sum / counter;
        } else {
            return 0;
        }
    }
    public void addRating(Integer r) {counter++; sum += r;}

    //CONSTRUCTOR
    public ParkRating(Integer r) { counter = 0; sum = r; }

    //PARCELABLE INTERFACE METHODS
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(counter);
        dest.writeInt(sum);
    }
    public ParkRating(Parcel in){
        counter = in.readInt();
        sum = in.readInt();
    }
    public static final Parcelable.Creator<ParkRating> CREATOR
            = new Parcelable.Creator<ParkRating>() {
        public ParkRating createFromParcel(Parcel in) {
            return new ParkRating(in);
        }

        public ParkRating[] newArray(int size) {
            return new ParkRating[size];
        }
    };
}
