package com.insyslab.tooz.ui.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnSelectContactItemClickListener;
import com.insyslab.tooz.interfaces.OnSyncContactItemClickListener;
import com.insyslab.tooz.models.ContactItem;
import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.ui.customui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by TaNMay on 19/02/17.
 */
public class SelectContactsAdapter extends RecyclerView.Adapter<SelectContactsAdapter.ViewHolder> {

    private OnSelectContactItemClickListener onSelectContactItemClickListener;
    private List<ContactItem> contactItems;

    public SelectContactsAdapter(OnSelectContactItemClickListener onSelectContactItemClickListener, List<ContactItem> contactItems) {
        this.onSelectContactItemClickListener = onSelectContactItemClickListener;
        this.contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ContactItem contactItem = contactItems.get(position);

        Picasso.with(holder.image.getContext())
                .load(contactItem.getImage())
                .placeholder(R.drawable.ic_default_user)
                .error(R.drawable.ic_default_user)
                .resize(100, 100)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.image);

        if (contactItem.getName() != null)
            holder.name.setText(contactItem.getName());
        if (contactItem.getNumber() != null)
            holder.number.setText(contactItem.getNumber());

        if (contactItem.isSelected()){
            holder.selector.setImageDrawable(ContextCompat.getDrawable(holder.selector.getContext(), R.drawable.ic_checkbox_selected));
        } else {
            holder.selector.setImageDrawable(ContextCompat.getDrawable(holder.selector.getContext(), R.drawable.ic_checkbox_not_selected));
        }

        holder.selectorSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectContactItemClickListener.onContactSelectorClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image, selector;
        public TextView name, number;
        public RelativeLayout selectorSec;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.islc_image);
            name = itemView.findViewById(R.id.islc_name);
            number = itemView.findViewById(R.id.islc_number);
            selector = itemView.findViewById(R.id.islc_selector);
            selectorSec = itemView.findViewById(R.id.islc_selector_sec);
        }
    }
}
