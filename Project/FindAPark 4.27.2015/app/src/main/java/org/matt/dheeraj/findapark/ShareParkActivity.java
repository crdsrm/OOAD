package org.matt.dheeraj.findapark;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ShareParkActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Park park = getIntent().getExtras().getParcelable("park");
        //setContentView(R.layout.activity_share_park);

        Button shareButton = new Button(this);
        shareButton.setText("Share Park");
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parkMessage =
                        "I just visited " +
                        park.getName() +
                        " at " +
                        park.getPostalAddress() +
                        ". Join me next time!";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, parkMessage);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,"Share park with..."));
                //done();
                finish();
            }
        });

        Button quitButton = new Button(this);
        quitButton.setText("Home");
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout ll = new LinearLayout(this);
        ll.addView(shareButton);
        ll.addView(quitButton);
        setContentView(ll);
    }

    //public void done(){this.finish();}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_share_park, menu);
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
}
