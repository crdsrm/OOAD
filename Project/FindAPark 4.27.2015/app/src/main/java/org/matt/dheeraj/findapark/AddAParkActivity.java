package org.matt.dheeraj.findapark;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddAParkActivity extends ActionBarActivity {


    private EditText nameET;
    private EditText locationET;
    private RadioButton use_device_location;
    private RadioButton use_manual_location;
    private EditText authorET;
    private EditText commentET;
    private RatingBar ratingBar;
    private List<CheckBox> featureBoxes;
    private DBBridge dbBridge;
    private LocationWrapper lw;

    private void buildView(){
        TextView nameTV = new TextView(this);
        nameTV.setText("Park Name");
        nameET = new EditText(this);
        nameET.setHint("Enter name here");

        use_device_location = new RadioButton(this);
        use_device_location.setText("Use device's location");
        use_manual_location = new RadioButton(this);
        use_manual_location.setText("Enter an address:");
        RadioGroup locSwitch = new RadioGroup(this);
        locSwitch.addView(use_device_location);
        locSwitch.addView(use_manual_location);
        locationET = new EditText(this);
        locationET.setHint("Enter the address");

        ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        ratingBar.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));


        FeatureList features = dbBridge.getAllFeatures();
        LinearLayout featureContainer = new LinearLayout(this);
        featureContainer.setOrientation(LinearLayout.VERTICAL);
        featureBoxes = new ArrayList<CheckBox>(features.size());
        for (int i = 0; i < features.size();i++){
            CheckBox cb = new CheckBox(this);
            cb.setText(features.get(i).getName());
            featureBoxes.add(cb);
            featureContainer.addView(cb);
        }

        TextView commentTV = new TextView(this);
        commentTV.setText("Comment");
        authorET = new EditText(this);
        authorET.setHint("Your name");
        commentET = new EditText(this);
        commentET.setHint("Comment");


        Button saveButton = new Button(this);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePark(v);
            }
        });
        saveButton.setText("Save Park");



        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(nameTV);
        ll.addView(nameET);
        ll.addView(locSwitch);
        ll.addView(locationET);
        ll.addView(ratingBar);
        ll.addView(featureContainer);
        ll.addView(commentTV);
        ll.addView(authorET);
        ll.addView(commentET);
        ll.addView(saveButton);

        ScrollView sv = new ScrollView(this);
        sv.addView(ll);
        setContentView(sv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbBridge = new DBBridge();
        lw = new LocationWrapper(this);
        buildView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_apark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Address getLocation() throws IOException{
        if (use_device_location.isChecked())
                return lw.getDeviceLocation();
        else if (use_manual_location.isChecked())
                return lw.getLocationFromString(locationET.getText().toString());
        else
                throw new IllegalArgumentException("Error. Please select a location lookup method.");

    }


    public void savePark(View view){

        String n = nameET.getText().toString();
        if (n.length() < 1){
            Toast.makeText(this,"Enter a name",Toast.LENGTH_SHORT).show();
            return;
        }

        Address a;
        try {
            a =  this.getLocation();
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }

        Park park = new Park(n,a);

        if (ratingBar.getNumStars() > 0)
            park.addRating(ratingBar.getNumStars());

        for (int i = 0; i < featureBoxes.size(); i++){
            if (featureBoxes.get(i).isChecked()){
                park.addFeature(new ParkFeature(featureBoxes.get(i).getText().toString()));
            }
        }

        if (commentET.getText().toString().length() > 0) {
            if (authorET.getText().toString().length() == 0) {
                authorET.setText("Anonymous");
            }
            park.addComment(new ParkComment(authorET.getText().toString(), commentET.getText().toString()));
        }

        dbBridge.createPark(park, this);

        Intent intent = new Intent(this, DisplayParkActivity.class);
        intent.putExtra("park",park);
        startActivity(intent);
        this.finish();
    }
}
