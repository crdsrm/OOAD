package org.matt.dheeraj.findapark;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Matt on 4/9/2015.
 */
public class ParkComment implements Parcelable{
    //DATA MEMBERS
    private String author;
    private String comment;

    //PUBLIC METHODS
    public String getAuthor() {return author;}
    public String getComment() { return comment;}
    public LinearLayout createView(Context context) {
        LinearLayout ll = new LinearLayout(context);
        TextView a = new TextView(context);
        TextView c = new TextView(context);
        ll.addView(a);
        ll.addView(c);
        return ll;
    }

    //  CONSTRUCTORS
    public ParkComment(String a, String c) {author = a; comment = c;}


    //PARCELABLE INTERFACE
    public ParkComment(Parcel in) {
        author = in.readString();
        comment = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(comment);
    }
    public static final Parcelable.Creator<ParkComment> CREATOR
            = new Parcelable.Creator<ParkComment>() {
        public ParkComment createFromParcel(Parcel in) {
            return new ParkComment(in);
        }

        public ParkComment[] newArray(int size) {
            return new ParkComment[size];
        }
    };
}
