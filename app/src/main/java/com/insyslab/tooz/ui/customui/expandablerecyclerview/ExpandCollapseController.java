package com.insyslab.tooz.ui.customui.expandablerecyclerview;

import com.insyslab.tooz.ui.customui.expandablerecyclerview.listeners.ExpandCollapseListener;
import com.insyslab.tooz.ui.customui.expandablerecyclerview.models.ExpandableGroup;
import com.insyslab.tooz.ui.customui.expandablerecyclerview.models.ExpandableList;
import com.insyslab.tooz.ui.customui.expandablerecyclerview.models.ExpandableListPosition;

/**
 * This class is the sits between the backing {@link ExpandableList} and the {@link
 * ExpandableRecyclerViewAdapter} and mediates the expanding and collapsing of {@link
 * ExpandableGroup}
 */
class ExpandCollapseController {

    private ExpandCollapseListener listener;
    private ExpandableList expandableList;

    ExpandCollapseController(ExpandableList expandableList, ExpandCollapseListener listener) {
        this.expandableList = expandableList;
        this.listener = listener;
    }

    /**
     * Collapse a group
     *
     * @param listPosition position of the group to collapse
     */
    private void collapseGroup(ExpandableListPosition listPosition) {
        expandableList.expandedGroupIndexes[listPosition.groupPos] = false;
        if (listener != null) {
            listener.onGroupCollapsed(expandableList.getFlattenedGroupIndex(listPosition) + 1,
                    expandableList.groups.get(listPosition.groupPos).getItemCount());
        }
    }

    /**
     * Expand a group
     *
     * @param listPosition the group to be expanded
     */
    private void expandGroup(ExpandableListPosition listPosition) {
        expandableList.expandedGroupIndexes[listPosition.groupPos] = true;
        if (listener != null) {
            listener.onGroupExpanded(expandableList.getFlattenedGroupIndex(listPosition) + 1,
                    expandableList.groups.get(listPosition.groupPos).getItemCount());
        }
    }

    /**
     * @param group the {@link ExpandableGroup} being checked for its collapsed state
     * @return true if {@code group} is expanded, false if it is collapsed
     */
    boolean isGroupExpanded(ExpandableGroup group) {
        int groupIndex = expandableList.groups.indexOf(group);
        return expandableList.expandedGroupIndexes[groupIndex];
    }

    /**
     * @param flatPos the flattened position of an item in the list
     * @return true if {@code group} is expanded, false if it is collapsed
     */
    boolean isGroupExpanded(int flatPos) {
        ExpandableListPosition listPosition = expandableList.getUnflattenedPosition(flatPos);
        return expandableList.expandedGroupIndexes[listPosition.groupPos];
    }

    /**
     * @param flatPos The flat list position of the group
     * @return false if the group is expanded, *after* the toggle, true if the group is now collapsed
     */
    boolean toggleGroup(int flatPos) {
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(flatPos);
        boolean expanded = expandableList.expandedGroupIndexes[listPos.groupPos];
        if (expanded) {
            collapseGroup(listPos);
        } else {
            expandGroup(listPos);
        }
        return expanded;
    }

    boolean toggleGroup(ExpandableGroup group) {
        ExpandableListPosition listPos =
                expandableList.getUnflattenedPosition(expandableList.getFlattenedGroupIndex(group));
        boolean expanded = expandableList.expandedGroupIndexes[listPos.groupPos];
        if (expanded) {
            collapseGroup(listPos);
        } else {
            expandGroup(listPos);
        }
        return expanded;
    }

}