package com.insyslab.tooz.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnSettingItemClickListener;
import com.insyslab.tooz.models.SettingsItem;

import java.util.List;


/**
 * Created by TaNMay on 19/02/17.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private OnSettingItemClickListener onSettingItemClickListener;
    private List<SettingsItem> settingsItems;

    public SettingsAdapter(OnSettingItemClickListener onSettingItemClickListener, List<SettingsItem> settingsItems) {
        this.onSettingItemClickListener = onSettingItemClickListener;
        this.settingsItems = settingsItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSettingItemClickListener.onSettingItemClick(v);
            }
        });
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SettingsItem settingsItem = settingsItems.get(position);

        holder.title.setText(settingsItem.getSetting());
    }

    @Override
    public int getItemCount() {
        return settingsItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.is_title);
        }
    }
}
