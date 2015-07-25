package org.matt.dheeraj.findapark;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DisplayParkActivity extends ActionBarActivity {

    EditText userName;
    EditText userComment;
    List<CheckBox> featureCBList;
    float newRating;
    private Park park;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        park = (Park) getIntent().getParcelableExtra("park");

        TextView nameTV = new TextView(this);
        nameTV.setText(park.getName());
        nameTV.setTextSize(nameTV.getTextSize()+4);
        nameTV.setTextColor(getResources().getColor(R.color.primary_text_default_material_light));

        TextView locTV = new TextView(this);
        locTV.setText(park.getPostalAddress());

        final RatingBar rBar = new RatingBar(this);
        rBar.setNumStars(5);
        rBar.setStepSize(1);
        newRating = 0f;
        rBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                newRating = rBar.getRating();
            }
        });
        rBar.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        rBar.setRating(park.getRating());

        FeatureList allFeatures = (new DBBridge()).getAllFeatures();
        FeatureList parkFeatures = park.getFeatures();
        LinearLayout checkBoxes = new LinearLayout(this);
        checkBoxes.setOrientation(LinearLayout.VERTICAL);
        CheckBox cb;
        featureCBList = new ArrayList<CheckBox>();
        for (int i = 0; i < allFeatures.size(); i++){
            cb = new CheckBox(this);
            cb.setText(allFeatures.get(i).getName());
            if (parkFeatures.exists(allFeatures.get(i))) {
                cb.setChecked(true);
                cb.setEnabled(false);
                cb.setTextColor(getResources().getColor(R.color.primary_text_default_material_light));
            } else {
                featureCBList.add(cb);
            }
            checkBoxes.addView(cb);
        }

        userName = new EditText(this);
        userName.setHint("Your name");
        userComment = new EditText(this);
        userComment.setHint("Add new commment here");


        CommentList cl = park.getComments();
        LinearLayout commentViews = cl.createView(this);

        Button shareButton = new Button(this);
        shareButton.setText("Share park");
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePark();
            }
        });
        Button updateButton = new Button(this);
        updateButton.setText("Update park");
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePark();
            }
        });
        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.addView(shareButton);
        buttons.addView(updateButton);

        LinearLayout thisView = new LinearLayout(this);
        thisView.setOrientation(LinearLayout.VERTICAL);
        thisView.addView(nameTV);
        thisView.addView(locTV);
        thisView.addView(rBar);
        thisView.addView(checkBoxes);
        thisView.addView(userName);
        thisView.addView(userComment);
        thisView.addView(commentViews);
        thisView.addView(buttons);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(thisView);

        setContentView(scrollView);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_park, menu);
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

    public void sharePark(){
        Intent intent = new Intent(this,ShareParkActivity.class);
        intent.putExtra("park",park);
        startActivity(intent);
    }

    public void updatePark(){
        if (newRating != 0) {
            park.addRating((int)  newRating);
        }

        if (userComment.getText().toString().length() > 0){
            String a, c;
            if (userName.getText().toString().length() == 0)
                userName.setText("Anonymous");
            ParkComment pc = new ParkComment(userName.getText().toString(),userComment.getText().toString() );
            park.addComment(pc);
        }

        for (CheckBox cb : featureCBList) {
            if (cb.isChecked()){
                park.addFeature(new ParkFeature(cb.getText().toString()));
                Toast.makeText(this,cb.getText().toString() + " added",Toast.LENGTH_SHORT).show();
            }
        }

        (new DBBridge()).updatePark(park, this);

        Intent intent = new Intent();
        intent.putExtra("park",park);
        setResult(RESULT_OK, intent);

        finish();
    }
}
