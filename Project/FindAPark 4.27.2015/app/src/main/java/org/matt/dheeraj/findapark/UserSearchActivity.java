package org.matt.dheeraj.findapark;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;


public class UserSearchActivity extends ActionBarActivity {

    private DBBridge dbBridge;
    private LocationWrapper lw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dbBridge = new DBBridge();
        lw = new LocationWrapper(this);
        setContentView(R.layout.activity_user_search);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_user_search, menu);
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

    public void findParks(View view) {
        ParkCollection pc;
        Address address;
        try {
            if (((RadioButton) findViewById(R.id.use_device_location)).isChecked())
                address = (lw.getDeviceLocation());
            else if (((RadioButton) findViewById(R.id.use_manual_location)).isChecked())
                address = (lw.getLocationFromString(((EditText) findViewById(R.id.address)).getText().toString()));
            else
                throw new IllegalArgumentException("Select a method.");
            pc = dbBridge.findParks(address, this);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        if (pc != null) {
            Intent intent = new Intent(this, DisplayParkCollectionActivity.class);
            intent.putExtra("pc", pc);
            intent.putExtra("address",address);
            startActivity(intent);
        } else
            Toast.makeText(this, "No parks found", Toast.LENGTH_SHORT).show();

    }


}
