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
import com.insyslab.tooz.interfaces.OnSelectGroupItemClickListener;
import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.ui.customui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectGroupsAdapter extends RecyclerView.Adapter<SelectGroupsAdapter.ViewHolder> {

    private OnSelectGroupItemClickListener onSelectGroupItemClickListener;
    private List<UserGroup> groupItems;

    public SelectGroupsAdapter(OnSelectGroupItemClickListener onSelectGroupItemClickListener, List<UserGroup> groupItems) {
        this.onSelectGroupItemClickListener = onSelectGroupItemClickListener;
        this.groupItems = groupItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_group, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        UserGroup groupItem = groupItems.get(position);

        Picasso.get()
                .load(groupItem.getProfileImage())
                .placeholder(R.drawable.ic_default_user)
                .error(R.drawable.ic_default_user)
                .resize(100, 100)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.image);

        if (groupItem.getName() != null)
            holder.name.setText(groupItem.getName());

        if (groupItem.isSelected()) {
            holder.selector.setImageDrawable(ContextCompat.getDrawable(holder.selector.getContext(), R.drawable.ic_checkbox_selected));
        } else {
            holder.selector.setImageDrawable(ContextCompat.getDrawable(holder.selector.getContext(), R.drawable.ic_checkbox_not_selected));
        }

        holder.selectorSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectGroupItemClickListener.onGroupSelectorClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, selector;
        TextView name;
        RelativeLayout selectorSec;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.islg_image);
            name = itemView.findViewById(R.id.islg_name);
            selector = itemView.findViewById(R.id.islg_selector);
            selectorSec = itemView.findViewById(R.id.islg_selector_sec);
        }
    }
}
