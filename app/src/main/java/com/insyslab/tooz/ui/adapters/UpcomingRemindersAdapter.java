package com.insyslab.tooz.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnUpcomingReminderClickListener;
import com.insyslab.tooz.models.Reminder;

import java.util.List;


/**
 * Created by TaNMay on 19/02/17.
 */
public class UpcomingRemindersAdapter extends RecyclerView.Adapter<UpcomingRemindersAdapter.ViewHolder> {

    private OnUpcomingReminderClickListener onUpcomingReminderClickListener;
    private List<Reminder> reminders;

    public UpcomingRemindersAdapter(OnUpcomingReminderClickListener onUpcomingReminderClickListener, List<Reminder> reminders) {
        this.onUpcomingReminderClickListener = onUpcomingReminderClickListener;
        this.reminders = reminders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upcoming_reminder, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpcomingReminderClickListener.onUpcomingReminderClick(view);
            }
        });
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Reminder reminder = reminders.get(position);

        if (reminder.isExpanded()) {
            holder.task.setText(reminder.getTitle());
        } else {
            holder.task.setText(reminder.getTitle().substring(0, 20) + "...");
        }

        holder.date.setText("88 Nov (Wed), 2088");
        holder.time.setText("88:88 AM");
        holder.remainingTime.setText("After 88 mins");
        holder.setter.setText("Sent by Developer");

        if (position == reminders.size() - 1) holder.divider.setVisibility(View.GONE);
        else holder.divider.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView task, date, time, remainingTime, setter;
        public View divider;

        public ViewHolder(View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.iur_task);
            date = itemView.findViewById(R.id.iur_date);
            time = itemView.findViewById(R.id.iur_time);
            remainingTime = itemView.findViewById(R.id.iur_remaining_time);
            setter = itemView.findViewById(R.id.iur_setter);
            divider = itemView.findViewById(R.id.iur_divider);
        }
    }
}
