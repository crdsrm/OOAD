package org.matt.dheeraj.findapark;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Matt on 4/11/2015.
 */
public class ParkView extends LinearLayout implements OnClickListener{
    //DATA MEMBERS
    private Park park;

    //PUBLIC METHODS
    public Park getPark() {return park;}
    public void setPark(Park p) {this.park = p;}

    //CONSTRUCTORS
    private void constructorHelper(Context context) {
        TextView name = new TextView(context);
        name.setText(park.getName());
        name.setTextColor(getResources().getColor(R.color.abc_primary_text_material_light));
        name.setTextSize(name.getTextSize()+2);

        TextView location = new TextView(context);
        location.setText(park.getPostalAddress());

        super.setOrientation(VERTICAL);
        super.addView(name);
        super.addView(location);
    }
    public ParkView(Context context, Park p) {
        super(context);
        this.park = p;
        constructorHelper(context);
    }
    public ParkView(Context context, AttributeSet attrs, Park p) {
        super(context, attrs);
        this.park = p;
        constructorHelper(context);
    }
    public ParkView(Context context, AttributeSet attrs, int defStyleAttr, Park p) {
        super(context, attrs, defStyleAttr);
        this.park = p;
        constructorHelper(context);
    }

    //OnClickListener INTERFACE
    @Override
    public void onClick(View v) {/*Needs to be implemented within activity, or somehow passed a context*/}
}
