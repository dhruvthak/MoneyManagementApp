package app.buzzboy.com.moneymanagement.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import app.buzzboy.com.moneymanagement.R;

/**
 * Created by Dhruv Thakkar on 4/21/2016.
 */
public class DialogContainer {

    AlertDialog alertDialog;

    public DialogContainer(final Context ctx, int message) {

        alertDialog = new AlertDialog.Builder(ctx).create();
        // Setting Dialog Title
        alertDialog.setTitle("Error");
        // Setting Dialog Message
        alertDialog.setMessage(ctx.getString(message));
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.password_button);
        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        // Showing Alert Message
    }

    public void show() {
        alertDialog.show();
    }
}

