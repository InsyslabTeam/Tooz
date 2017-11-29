package com.insyslab.tooz.models;

/**
 * Created by TaNMay on 12/10/17.
 */

public class FragmentState {

    private String visibleFragment;

    private String fragmentDetailedName;

    public FragmentState(String visibleFragment) {
        this.visibleFragment = visibleFragment;
    }

    public String getVisibleFragment() {
        return visibleFragment;
    }

    public void setVisibleFragment(String visibleFragment) {
        this.visibleFragment = visibleFragment;
    }

    public String getFragmentDetailedName() {
        return fragmentDetailedName;
    }

    public void setFragmentDetailedName(String fragmentDetailedName) {
        this.fragmentDetailedName = fragmentDetailedName;
    }
}
