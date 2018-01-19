package com.insyslab.tooz.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by TaNMay on 26/12/17.
 */

public class CustomShareIntent {

    private Context context;
    private String shareText;

    public CustomShareIntent(Context context, String shareText) {
        this.context = context;
        this.shareText = shareText;
    }

    public void shareToAllApps() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(shareIntent, "Share Tooz..."));
    }
}
