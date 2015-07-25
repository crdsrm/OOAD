package org.matt.dheeraj.findapark;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Matt on 4/19/2015.
 */
public class SortDialogFragment extends DialogFragment{
    SortDialogListener mListener;
    String[] choices;
    public interface SortDialogListener{
        public void onSortClick(DialogFragment dialogFragment, String choice);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SortDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement SortDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        choices = getResources().getStringArray(R.array.sort_choices);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose sort method")
                .setItems(choices,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onSortClick(SortDialogFragment.this, choices[which]);
                    }
                });
        return builder.create();
    }
}
