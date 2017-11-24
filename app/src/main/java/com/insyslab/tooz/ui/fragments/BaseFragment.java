package com.insyslab.tooz.ui.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.insyslab.tooz.models.FragmentState;

import de.greenrobot.event.EventBus;

/**
 * Created by TaNMay on 12/10/17.
 */

public abstract class BaseFragment extends Fragment {

    protected ProgressDialog mProgressDialog = null;

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(getContext(), null, message, true, false, null);
        } else {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    protected void showToastMessage(String message, boolean isLong) {
        Toast toast = null;

        if (isLong) toast = toast.makeText(getContext(), message + "", Toast.LENGTH_LONG);
        else toast = toast.makeText(getContext(), message + "", Toast.LENGTH_SHORT);

        toast.show();
    }

    protected void showSnackbarMessage(View view, String message, boolean isLong, String action,
                                       View.OnClickListener onClickListener, boolean isDismissed) {
        Snackbar snackbar = null;

        if (action != null && isDismissed == true) {
            snackbar = snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
            final Snackbar finalSnackbar = snackbar;
            snackbar = snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalSnackbar.dismiss();
                }
            });
        } else if (action != null) {
            snackbar = snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(action, onClickListener);
        } else if (isLong) {
            snackbar = snackbar.make(view, message, Snackbar.LENGTH_LONG);
        } else if (!isLong) {
            snackbar = snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        }
        snackbar.show();
    }

    protected void showNetworkErrorSnackbar(View view, String message, String action, View.OnClickListener mOnClickListener) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(action, mOnClickListener);
        snackbar.show();
    }

    protected void updateFragment(FragmentState fragmentState) {
        EventBus.getDefault().postSticky(fragmentState);
    }

    protected void showConfirmationDialog(
            String prompt, String positiveButton, String negativeButton,
            DialogInterface.OnClickListener positiveClick,
            DialogInterface.OnClickListener negativeClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
