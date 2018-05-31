package com.insyslab.tooz.models.eventbus;

public class FragmentState {

    private String visibleFragment;

    private String fragmentDetailedName;

    public FragmentState(String visibleFragment) {
        this.visibleFragment = visibleFragment;
    }

    public String getVisibleFragment() {
        return visibleFragment;
    }

    public String getFragmentDetailedName() {
        return fragmentDetailedName;
    }

    public void setFragmentDetailedName(String fragmentDetailedName) {
        this.fragmentDetailedName = fragmentDetailedName;
    }
}
