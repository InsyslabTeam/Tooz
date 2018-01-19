package com.insyslab.tooz.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class NonAppUserContactsAdapter extends RecyclerView.Adapter<NonAppUserContactsAdapter.ViewHolder> {

    private OnUserContactClickListener onAllContactClickListener;
    private List<User> contactItems;

    public NonAppUserContactsAdapter(OnUserContactClickListener onAllContactClickListener, List<User> contactItems) {
        this.onAllContactClickListener = onAllContactClickListener;
        this.contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_non_app_user_contact, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAllContactClickListener.onNonAppUserContactClick(view);
            }
        });
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        User contactItem = contactItems.get(position);

        Picasso.with(holder.image.getContext())
                .load("abcdefghijklmnopqrstuvwxyz")
                .placeholder(R.drawable.ic_default_user)
                .error(R.drawable.ic_default_user)
                .resize(100, 100)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.image);

        holder.name.setText(contactItem.getName());

        if (position == contactItems.size() - 1) holder.divider.setVisibility(View.GONE);
        else holder.divider.setVisibility(View.VISIBLE);

        holder.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAllContactClickListener.onNonAppUserInviteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, invite;
        public ImageView image;
        public View divider;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.inauc_name);
            invite = itemView.findViewById(R.id.inauc_invite);
            image = itemView.findViewById(R.id.inauc_image);
            divider = itemView.findViewById(R.id.inauc_divider);
        }
    }
}
