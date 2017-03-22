package com.bignerdranch.android.teapot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by AREG on 18.01.2017.
 */

public class TeapotDialogFragment extends DialogFragment {

    private static final String TITLE = "title";
    private static final String BODY = "body";

    public static TeapotDialogFragment newInstance(int title, int body) {
        TeapotDialogFragment frag = new TeapotDialogFragment();
        frag.setCancelable(false); // do dialog modal
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        args.putInt(BODY, body);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(TITLE);
        int body = getArguments().getInt(BODY);

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(body);

        builder.setPositiveButton(R.string.dialogOkButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent());
            }
        });

        builder.setNegativeButton(R.string.dialogCancelButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, new Intent());
            }
        });

        return builder.create();
    }
}
