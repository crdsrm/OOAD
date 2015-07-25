package org.matt.dheeraj.findapark;

import android.content.Context;
import android.location.Address;
import android.widget.Toast;

/**
 * Created by Matt on 4/10/2015.
 *
 */
public class DBBridge {
    //private Context context;

    public boolean nameExists(String name) {
        return true;
    }
    public void createPark(Park park, Context context) {
        //Need to fill in code to add a new park in the SQL Database
        Toast.makeText(context,"Just assume this works for now.",Toast.LENGTH_SHORT).show();
    }

    public ParkCollection findParks(Address address, Context context){
        //Need to fill in code that creates a ParkCollection based on query to SQL Database

        //EXAMPLE CASE 4/13/2014
        ParkCollection pc = new ParkCollection();
        LocationWrapper lw = new LocationWrapper(context);

        for (int i = 0; i < 4; i++){
            Park park;
            try {
                if (i == 0)
                    park = new Park("Waneka", lw.getLocationFromString("80026"));
                else if (i == 1)
                    park = new Park("Pioneer",  lw.getLocationFromString("46158"));
                else if (i == 2)
                    park = new Park("Beverly Hills",  lw.getLocationFromString("90210"));
                else if (i == 3)
                    park = new Park("Scott Carpenter",  lw.getLocationFromString("80503"));
                else
                    park = new Park("RMNP", lw.getLocationFromString("Estes Park, CO"));

                park.addFeature(new ParkFeature("Playground"));
                park.addRating(5);
                park.addRating(3);
                park.addComment(new ParkComment("Matt", "YOLO YO"));
                park.addComment(new ParkComment("Dheeraj", "Parks are awesome."));
                pc.add(park);

            } catch (Exception e){
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        return pc;
    }

    public void updatePark(Park park, Context context){
        //Need to develop method for updating existing data for a record in SQL
        Toast.makeText(context,"Just assume this works for now.",Toast.LENGTH_SHORT).show();


        park.getAddress().getLatitude();
        park.getAddress().getLongitude();
    }

    public FeatureList getAllFeatures(){
        //eventually this will connect to the DB and get all the possible features a park can contain

        FeatureList fl = new FeatureList();
        fl.add(new ParkFeature("Playground"));
        fl.add(new ParkFeature("Shelter"));
        fl.add(new ParkFeature("Basketball Goal"));
        fl.add(new ParkFeature("Skate Park"));
        return fl;
    }

    public DBBridge(){

    }

    //Methods and members to implement interaction with DB

}
