package com.insyslab.tooz.ui.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

/**
 * Created by TaNMay on 12/10/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void getFragmentStatus() {
        EventBus.getDefault().registerSticky(this);
    }

    protected void showToastMessage(String message, boolean isLong) {
        Toast toast = null;

        if (isLong) toast = toast.makeText(this, message, Toast.LENGTH_LONG);
        else toast = toast.makeText(this, message, Toast.LENGTH_SHORT);

        toast.show();
    }

    protected void showSnackbarMessage(View view, String message, boolean isLong, String action,
                                       View.OnClickListener onClickListener, boolean isDismissed) {
        Snackbar snackbar = null;

        if (isLong) snackbar = snackbar.make(view, message, Snackbar.LENGTH_LONG);
        else if (!isLong) snackbar = snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        if (action != null && isDismissed == true) {
            final Snackbar finalSnackbar = snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
            snackbar = snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalSnackbar.dismiss();
                }
            });
        } else if (action != null)
            snackbar = snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(action, onClickListener);

        snackbar.show();
    }

    protected void showNetworkErrorSnackbar(View view, String message, String action, View.OnClickListener mOnClickListener) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(action, mOnClickListener);
        snackbar.show();
    }

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(BaseActivity.this, null, message, true, false, null);
        } else {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected void showConfirmationDialog(
            String prompt, String positiveButton, String negativeButton,
            DialogInterface.OnClickListener positiveClick,
            DialogInterface.OnClickListener negativeClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(prompt)
                .setPositiveButton(positiveButton, positiveClick);
        if (negativeClick != null) {
            builder.setNegativeButton(negativeButton, negativeClick);
        } else {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        builder.show();
    }
}
