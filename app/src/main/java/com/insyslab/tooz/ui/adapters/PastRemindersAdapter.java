package com.insyslab.tooz.ui.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnPastReminderClickListener;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.utils.Util;

import java.util.Calendar;
import java.util.List;

import static com.insyslab.tooz.utils.Util.getReminderFormatedDate;
import static com.insyslab.tooz.utils.Util.getReminderFormatedTime;

public class PastRemindersAdapter extends RecyclerView.Adapter<PastRemindersAdapter.ViewHolder> {

    private OnPastReminderClickListener onPastReminderClickListener;
    private List<Reminder> reminders;

    public PastRemindersAdapter(OnPastReminderClickListener onPastReminderClickListener, List<Reminder> reminders) {
        this.onPastReminderClickListener = onPastReminderClickListener;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_reminder, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPastReminderClickListener.onPastReminderClick(view);
            }
        });
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        if (reminder.isExpanded()) {
            holder.task.setText(reminder.getTask());
//            holder.optionsSec.setVisibility(View.VISIBLE);
        } else {
//            holder.optionsSec.setVisibility(View.GONE);
            if (reminder.getTask().length() > 20)
                holder.task.setText(reminder.getTask().substring(0, 20) + "...");
            else holder.task.setText(reminder.getTask());
        }

        Calendar cal = Util.getCalenderFormatDate(reminder.getDate());

        holder.date.setText(getReminderFormatedDate(cal));
        holder.time.setText(getReminderFormatedTime(cal));
        holder.status.setText("Completed");
        holder.setter.setText(Html.fromHtml("Sent by <B>" + reminder.getUser().getName() + "</B>"));

        if (position == reminders.size() - 1) holder.divider.setVisibility(View.GONE);
        else holder.divider.setVisibility(View.VISIBLE);

//        holder.editSec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onPastReminderClickListener.onPastReminderEditClick(position);
//            }
//        });
//
//        holder.deleteSec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onPastReminderClickListener.onPastReminderDeleteClick(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public View divider;
        TextView task, date, time, status, setter;

        public ViewHolder(View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.ipr_task);
            date = itemView.findViewById(R.id.ipr_date);
            time = itemView.findViewById(R.id.ipr_time);
            status = itemView.findViewById(R.id.ipr_status);
            setter = itemView.findViewById(R.id.ipr_setter);
            divider = itemView.findViewById(R.id.ipr_divider);
        }
    }
}
