package org.matt.dheeraj.findapark;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;


/*** Created by Matt on 3/31/2015. ***/


public class Park implements Parcelable{
    //DATA MEMBERS
    private String name;
    private ParkRating rating;
    private FeatureList features;
    private CommentList comments;
    private Address address;

    //PUBLIC METHODS
    public String getName() {return name;}
    public float getRating() {
        return rating.getRating();
    }
    public void addRating(Integer r) {
        if (r >= 1 && r <= 5)
            rating.addRating(r);
        else
            throw new IllegalArgumentException("Rating must be between 1 and 5");
    }
    public String getPostalAddress(){
        return address.getAddressLine(0) + " " + address.getAddressLine(1);
    }
    public Address getAddress() {
        return address;
    }
    public FeatureList getFeatures() {return features;}
    public Boolean hasFeature(ParkFeature f) { return features.exists(f); }
    public void addFeature(ParkFeature f) { features.add(f);}
    public CommentList getComments() { return comments; }
    public void addComment(ParkComment c) { comments.add(c); }

    //PARCELABLE INTERFACE METHODS
    public Park(Parcel in){
        name = in.readString();
        rating = (ParkRating) in.readParcelable(ParkRating.class.getClassLoader());
        features = (FeatureList) in.readParcelable(FeatureList.class.getClassLoader());
        comments = (CommentList) in.readParcelable(CommentList.class.getClassLoader());
        address = in.readParcelable(Address.class.getClassLoader());
    }
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(name);
        dest.writeParcelable(rating, 0);
        dest.writeParcelable(features, 0);
        dest.writeParcelable(comments, 0);
        dest.writeParcelable(address, 0);
    }
    public int describeContents(){
        return CONTENTS_FILE_DESCRIPTOR ;
    }
    public static final Parcelable.Creator<Park> CREATOR = new Parcelable.Creator<Park>() {
        public Park createFromParcel(Parcel in) {
            return new Park(in);
        }
        public Park[] newArray(int size) {
            return new Park[size];
        }
    };

    //CONSTRUCTORS
    public Park (String n, Address a){
        name = n;
        address = a;
        rating = new ParkRating(0);
        features = new FeatureList();
        comments = new CommentList();
    }
    public Park (){throw new UnsupportedOperationException();}
}