package org.matt.dheeraj.findapark;

import android.app.DialogFragment;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.List;


public class DisplayParkCollectionActivity
        extends ActionBarActivity
        implements FilterDialogFragment.FilterDialogListener,SortDialogFragment.SortDialogListener{

    private ParkCollection pc;
    private Address address;
    LinearLayout parkContainer;
    List<ParkView> parkClickers;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == 0 && resultCode == RESULT_OK && intent != null) {
            Park newPark = (Park) intent.getParcelableExtra("park");

            for (int i = 0; i < pc.size(); i++){
                if (newPark.getName().equals(pc.getPark(i).getName())){
                    pc.setPark(i,newPark);
                    break;
                }
            }
            parkContainer.removeAllViews();
            addParkClickers(parkContainer);
        }

    }
    private void displayPark(Park p){
        Intent intent = new Intent(this, DisplayParkActivity.class);
        intent.putExtra("park",p);
        startActivityForResult(intent,0);
    }

    private void addParkClickers(LinearLayout view){
        if (pc.size() != 0) {
            parkClickers = pc.getParkClickers(this);
            for (final ParkView parkView : parkClickers) {
                parkView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displayPark(parkView.getPark());
                    }
                });
                view.addView(parkView);
            }
        } else {
            Toast.makeText(this,"Error retrieving park data",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pc = (ParkCollection) getIntent().getParcelableExtra("pc");
        address = (Address) getIntent().getParcelableExtra("address");

        ScrollView mainView = (ScrollView) View.inflate(this, R.layout.activity_display_park_collection, null);
        parkContainer = (LinearLayout) mainView.findViewById(R.id.park_list);
        addParkClickers(parkContainer);

        setContentView(mainView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_park_collection, menu);
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

    public void done(View view){
        this.finish();
    }

    public void sort(View view) {
        DialogFragment sort = new SortDialogFragment();
        sort.show(getFragmentManager(),"SortDialogFragment");
    }
    public void filter(View view) {
        DialogFragment filter = new FilterDialogFragment();
        filter.show(getFragmentManager(),"FilterDialogFragment");
    }

    @Override
    public void onFilterPositiveClick(DialogFragment dialog, FeatureList features) {
        pc.filter(features);

        parkContainer.removeAllViews();
        addParkClickers(parkContainer);
    }

    @Override
    public void onFilterNegativeClick() {
        pc.removeFilter();

        parkContainer.removeAllViews();
        addParkClickers(parkContainer);
    }

    @Override
    public void onSortClick(DialogFragment dialogFragment, String choice){
        if (choice.equals("Distance")){
            pc.sortByDistance(address);
        } else if (choice.equals("Rating")){
            pc.sortByRating();
        } else {
            Toast.makeText(this,"Sort method " + choice + " is not found.",Toast.LENGTH_SHORT).show();
            return;
        }

        parkContainer.removeAllViews();
        addParkClickers(parkContainer);
    }

}
