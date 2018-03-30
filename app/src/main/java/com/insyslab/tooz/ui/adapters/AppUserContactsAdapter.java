package com.insyslab.tooz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnUserContactClickListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.ui.customui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by TaNMay on 19/02/17.
 */
public class AppUserContactsAdapter extends RecyclerView.Adapter<AppUserContactsAdapter.ViewHolder> {

    private OnUserContactClickListener onAllContactClickListener;
    private List<User> contactItems;

    public AppUserContactsAdapter(OnUserContactClickListener onAllContactClickListener, List<User> contactItems) {
        this.onAllContactClickListener = onAllContactClickListener;
        this.contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_contact, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAllContactClickListener.onAppUserContactClick(view);
            }
        });
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User contactItem = contactItems.get(position);

        Picasso.with(holder.image.getContext())
                .load(contactItem.getProfileImage())
                .placeholder(R.drawable.ic_default_user)
                .error(R.drawable.ic_default_user)
                .resize(100, 100)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.image);

        if (contactItem.getName() != null) holder.name.setText(contactItem.getName());
        else holder.name.setText("No Name");
        holder.status.setText("Sharing # reminders");

        if (position == contactItems.size() - 1) holder.divider.setVisibility(View.GONE);
        else holder.divider.setVisibility(View.VISIBLE);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuOnItem(holder.item.getContext(), view, contactItem.isBlocked(), position, holder.name);
            }
        });
    }

    private void showPopupMenuOnItem(Context context, View view, Boolean isBlocked, final int position, View anchorView) {
        PopupMenu popup = new PopupMenu(context, anchorView);
        popup.inflate(R.menu.menu_contact);
        popup.getMenu().add(1, 1, 1, "Send Reminder");
        popup.getMenu().add(1, 2, 2, isBlocked ? "Unblock" : "Block");
        popup.setGravity(Gravity.RIGHT);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        onAllContactClickListener.onAppUserSendReminderClick(position);
                        break;
                    case 2:
                        onAllContactClickListener.onAppUserBlockClick(position);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout item;
        public TextView name, status;
        public ImageView image;
        public View divider;

        public ViewHolder(View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.iac_item);
            name = itemView.findViewById(R.id.iac_name);
            status = itemView.findViewById(R.id.iac_status);
            image = itemView.findViewById(R.id.iac_image);
            divider = itemView.findViewById(R.id.iac_divider);
        }
    }
}
