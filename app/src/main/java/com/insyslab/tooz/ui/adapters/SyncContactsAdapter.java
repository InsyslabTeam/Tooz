package com.insyslab.tooz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnSyncContactItemClickListener;
import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.ui.customui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SyncContactsAdapter extends RecyclerView.Adapter<SyncContactsAdapter.ViewHolder> {

    private OnSyncContactItemClickListener onSyncContactItemClickListener;
    private List<PhoneContact> phoneContacts;

    public SyncContactsAdapter(OnSyncContactItemClickListener onSyncContactItemClickListener, List<PhoneContact> phoneContacts) {
        this.onSyncContactItemClickListener = onSyncContactItemClickListener;
        this.phoneContacts = phoneContacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sync_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PhoneContact phoneContact = phoneContacts.get(position);

        Picasso.get()
                .load(phoneContact.getContactImageUri())
                .placeholder(R.drawable.ic_default_user)
                .error(R.drawable.ic_default_user)
                .resize(100, 100)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.image);

        if (phoneContact.getName() != null)
            holder.name.setText(phoneContact.getName());
        if (phoneContact.getPhoneNumber() != null)
            holder.number.setText(phoneContact.getPhoneNumber());

        setSelectionAs(phoneContact.getSelected(), holder.selector);

        holder.selectorSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectionAs(!phoneContact.getSelected(), holder.selector);
                onSyncContactItemClickListener.onContactSelectorClick(holder.getAdapterPosition());
            }
        });
    }

    private void setSelectionAs(Boolean selected, ImageView selector) {
        if (selected) {
            selector.setImageDrawable(ContextCompat.getDrawable(selector.getContext(), R.drawable.ic_checkbox_selected));
        } else {
            selector.setImageDrawable(ContextCompat.getDrawable(selector.getContext(), R.drawable.ic_checkbox_not_selected));
        }
    }

    @Override
    public int getItemCount() {
        return phoneContacts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, selector;
        TextView name, number;
        RelativeLayout selectorSec;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.isc_image);
            name = itemView.findViewById(R.id.isc_name);
            number = itemView.findViewById(R.id.isc_number);
            selector = itemView.findViewById(R.id.isc_selector);
            selectorSec = itemView.findViewById(R.id.isc_selector_sec);
        }
    }
}
