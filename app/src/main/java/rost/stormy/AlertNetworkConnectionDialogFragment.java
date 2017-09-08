package rost.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Asus on 03.09.2017.
 */

public class AlertNetworkConnectionDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context =getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Network problems!")
                    .setMessage("You don't have connection to the Internet, please check the connection.")
                    .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
