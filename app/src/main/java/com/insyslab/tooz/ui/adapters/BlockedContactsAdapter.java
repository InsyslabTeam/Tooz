package com.insyslab.tooz.ui.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnBlockedContactsClickListener;
import com.insyslab.tooz.models.ContactItem;
import com.insyslab.tooz.ui.customui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by TaNMay on 19/02/17.
 */
public class BlockedContactsAdapter extends RecyclerView.Adapter<BlockedContactsAdapter.ViewHolder> {

    private OnBlockedContactsClickListener onBlockedContactsClickListener;
    private List<ContactItem> contactItems;

    public BlockedContactsAdapter(OnBlockedContactsClickListener onBlockedContactsClickListener, List<ContactItem> contactItems) {
        this.onBlockedContactsClickListener = onBlockedContactsClickListener;
        this.contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ContactItem contactItem = contactItems.get(position);

        Picasso.with(holder.image.getContext())
                .load("abcdefghijklmnopqrstuvwxyz")
                .placeholder(R.drawable.ic_default_user)
                .error(R.drawable.ic_default_user)
                .resize(100, 100)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.image);

        holder.name.setText(contactItem.getName());
        holder.number.setText(contactItem.getNumber());

        if (contactItem.isBlocked()) {
            holder.unblock.setText("Unblock");
            holder.unblock.setBackground(ContextCompat.getDrawable(holder.unblock.getContext(), R.drawable.ic_blue_button));
        }
        else {
            holder.unblock.setText("Block");
            holder.unblock.setBackground(ContextCompat.getDrawable(holder.unblock.getContext(), R.drawable.ic_red_button));
        }
        holder.unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBlockedContactsClickListener.onUnblockClick(position);
            }
        });

        if (position == contactItems.size() - 1) holder.divider.setVisibility(View.GONE);
        else holder.divider.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, number, unblock;
        public ImageView image;
        public View divider;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ibc_name);
            number = itemView.findViewById(R.id.ibc_number);
            image = itemView.findViewById(R.id.ibc_image);
            unblock = itemView.findViewById(R.id.ibc_unblock);
            divider = itemView.findViewById(R.id.ibc_divider);
        }
    }
}
