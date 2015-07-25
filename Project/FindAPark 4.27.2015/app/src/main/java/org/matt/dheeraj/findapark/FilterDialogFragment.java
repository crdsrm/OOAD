package org.matt.dheeraj.findapark;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Matt on 4/18/2015.
 */
public class FilterDialogFragment extends DialogFragment{
    FeatureList mSelectedItems;
    FilterDialogListener mListener;
    String[] featureNames;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        featureNames = getResources().getStringArray(R.array.park_features);
        mSelectedItems = new FeatureList();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Filter method")
                .setMultiChoiceItems(featureNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(new ParkFeature(featureNames[which]));
                        } else {
                            mSelectedItems.remove(new ParkFeature(featureNames[which]));
                        }
                    }
                })
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFilterPositiveClick(FilterDialogFragment.this, mSelectedItems);
                    }
                })
                .setNegativeButton("Clear Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFilterNegativeClick();
                    }
                });
        return builder.create();
    }


    public interface FilterDialogListener {
        public void onFilterPositiveClick(DialogFragment dialog, FeatureList features);
        public void onFilterNegativeClick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FilterDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FilterDialogListener");
        }
    }
}
