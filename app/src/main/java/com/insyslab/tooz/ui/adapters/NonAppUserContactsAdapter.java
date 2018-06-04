package com.insyslab.tooz.ui.adapters;

import android.support.annotation.NonNull;
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

public class NonAppUserContactsAdapter extends RecyclerView.Adapter<NonAppUserContactsAdapter.ViewHolder> {

    private OnUserContactClickListener onAllContactClickListener;
    private List<User> contactItems;

    public NonAppUserContactsAdapter(OnUserContactClickListener onAllContactClickListener, List<User> contactItems) {
        this.onAllContactClickListener = onAllContactClickListener;
        this.contactItems = contactItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_non_app_user_contact, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAllContactClickListener.onNonAppUserContactClick(view);
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        User contactItem = contactItems.get(position);

        Picasso.get()
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
                onAllContactClickListener.onNonAppUserInviteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, invite;
        ImageView image;
        View divider;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.inauc_name);
            invite = itemView.findViewById(R.id.inauc_invite);
            image = itemView.findViewById(R.id.inauc_image);
            divider = itemView.findViewById(R.id.inauc_divider);
        }
    }
}
