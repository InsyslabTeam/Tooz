package com.insyslab.tooz.models;

import com.insyslab.tooz.ui.customui.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by TaNMay on 28/11/17.
 */

public class Reminder {

    private String title;

    private Boolean isExpanded = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }
}
