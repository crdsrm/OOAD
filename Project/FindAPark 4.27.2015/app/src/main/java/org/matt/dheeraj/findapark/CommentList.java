package org.matt.dheeraj.findapark;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/9/2015.
 */

public class CommentList implements Parcelable{

    //DATA MEMBERS
    private List<ParkComment> comments;

    //PUBLIC METHODS
    public void add(ParkComment c) {comments.add(c);}
    public LinearLayout createView(Context context) {
        LinearLayout ll = new LinearLayout(context);
        for (int i = 0; i < comments.size(); i++) {
            ll.addView(comments.get(i).createView(context));
        }
        return ll;
    }

    //CONSTRUCTORS
    public CommentList() { comments = new ArrayList<ParkComment>();}

    //PARCELABLE METHODS
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(comments);
    }
    public CommentList(Parcel in){
        comments = in.readArrayList(ParkComment.class.getClassLoader());
    }
    public static final Parcelable.Creator<CommentList> CREATOR
            = new Parcelable.Creator<CommentList>() {
        public CommentList createFromParcel(Parcel in) {
            return new CommentList(in);
        }

        public CommentList[] newArray(int size) {
            return new CommentList[size];
        }
    };
}
