package com.insyslab.tooz.ui.adapters;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnUpcomingReminderClickListener;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.utils.ToozApplication;
import com.insyslab.tooz.utils.Util;

import java.util.Calendar;
import java.util.List;

import static com.insyslab.tooz.utils.Util.getDistanceBetweenTwoCoords;
import static com.insyslab.tooz.utils.Util.getReminderFormatedDate;
import static com.insyslab.tooz.utils.Util.getReminderFormatedTime;
import static com.insyslab.tooz.utils.Util.getReminderRemainingTime;

public class UpcomingRemindersAdapter extends RecyclerView.Adapter<UpcomingRemindersAdapter.ViewHolder> {

    private OnUpcomingReminderClickListener onUpcomingReminderClickListener;
    private List<Reminder> reminders;
    private Calendar currentTime;
    private Location currentLocation;

    public UpcomingRemindersAdapter(OnUpcomingReminderClickListener onUpcomingReminderClickListener, List<Reminder> reminders) {
        this.onUpcomingReminderClickListener = onUpcomingReminderClickListener;
        this.reminders = reminders;
        currentTime = Calendar.getInstance();
        currentLocation = ToozApplication.getInstance().getLastLocation();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upcoming_reminder, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpcomingReminderClickListener.onUpcomingReminderClick(view);
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
            holder.optionsSec.setVisibility(View.VISIBLE);
        } else {
            holder.optionsSec.setVisibility(View.GONE);
            if (reminder.getTask().length() > 20)
                holder.task.setText(reminder.getTask().substring(0, 20) + "...");
            else holder.task.setText(reminder.getTask());
        }

        if (reminder.getDate() != null) {
            Calendar cal = Util.getCalenderFormatDate(reminder.getDate());
            holder.date.setText(getReminderFormatedDate(cal));
            holder.time.setText(getReminderFormatedTime(cal));
            holder.remainingTime.setText("After " + getReminderRemainingTime(cal, currentTime));
        } else if (reminder.getLatitude() != null && reminder.getLongitude() != null) {
            Double dLat = Double.parseDouble(reminder.getLatitude());
            Double dLng = Double.parseDouble(reminder.getLongitude());
            String address = Util.getShortAddressFromCoords(holder.date.getContext(), new LatLng(dLat, dLng));
            if (address.length() > 25) {
                holder.date.setText(address.substring(0, 25) + "\n" + address.substring(25, address.length() > 50 ? 48 : address.length()));
            } else {
                holder.date.setText(address);
            }
            if (currentLocation != null) {
                Double distanceFromPointInMeters = getDistanceBetweenTwoCoords(currentLocation.getLatitude(), currentLocation.getLongitude(), dLat, dLng);
                String distance = Util.getFormattedDistance(distanceFromPointInMeters);
                holder.remainingTime.setText(distance + " from here");
            }
        }

        holder.setter.setText(Html.fromHtml("Sent by <B>" + reminder.getUser().getName() + "</B>"));

        if (position == reminders.size() - 1) holder.divider.setVisibility(View.GONE);
        else holder.divider.setVisibility(View.VISIBLE);

        holder.editSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpcomingReminderClickListener.onUpcomingReminderEditClick(holder.getAdapterPosition());
            }
        });

        holder.deleteSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpcomingReminderClickListener.onUpcomingReminderDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View divider;
        TextView task, date, time, remainingTime, setter;
        LinearLayout optionsSec;
        RelativeLayout editSec, deleteSec;

        public ViewHolder(View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.iur_task);
            date = itemView.findViewById(R.id.iur_date);
            time = itemView.findViewById(R.id.iur_time);
            remainingTime = itemView.findViewById(R.id.iur_remaining_time);
            setter = itemView.findViewById(R.id.iur_setter);
            divider = itemView.findViewById(R.id.iur_divider);
            optionsSec = itemView.findViewById(R.id.iur_options_sec);
            editSec = itemView.findViewById(R.id.iur_edit_sec);
            deleteSec = itemView.findViewById(R.id.iur_delete_sec);
        }
    }
}
