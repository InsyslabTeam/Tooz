package com.insyslab.tooz.models;

/**
 * Created by TaNMay on 12/10/17.
 */

public class FragmentState {

    private String visibleFragment;

    public FragmentState(String visibleFragment) {
        this.visibleFragment = visibleFragment;
    }

    public String getVisibleFragment() {
        return visibleFragment;
    }

    public void setVisibleFragment(String visibleFragment) {
        this.visibleFragment = visibleFragment;
    }
}
