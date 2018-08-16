package com.insyslab.tooz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnSyncContactItemClickListener;
import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.ui.customui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SyncContactsAdapter extends RecyclerView.Adapter<SyncContactsAdapter.ViewHolder>
implements Filterable{

    private OnSyncContactItemClickListener onSyncContactItemClickListener;
    private List<PhoneContact> phoneContacts;
    private List<PhoneContact> contactListFiltered;

    public SyncContactsAdapter(OnSyncContactItemClickListener onSyncContactItemClickListener, List<PhoneContact> phoneContacts) {
        this.onSyncContactItemClickListener = onSyncContactItemClickListener;
        this.phoneContacts = phoneContacts;
        this.contactListFiltered = phoneContacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sync_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PhoneContact phoneContact = contactListFiltered.get(position);

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
                onSyncContactItemClickListener.onContactSelectorClick(holder.getAdapterPosition(), phoneContact.getPhoneNumber());
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
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = phoneContacts;
                } else {
                    List<PhoneContact> filteredList = new ArrayList<>();
                    for (PhoneContact row : phoneContacts) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhoneNumber().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<PhoneContact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
