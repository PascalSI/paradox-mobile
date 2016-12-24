package eu.greitai.paradox.mobilesecurity;

import android.app.Activity;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.greitai.paradox.mobilesecurity.data.SecurityEvent;

public class EventDisplayAdapter extends ArrayAdapter<SecurityEvent> {

    private Activity context;
    private List<SecurityEvent> events;
    private int layout;

    private static final Set<Integer> nonTranslatableEvents =
            new HashSet<>(Arrays.asList(new Integer[]{29, 31, 32, 33}));


    public EventDisplayAdapter(Activity context, int layout, List<SecurityEvent> events) {
        super(context, layout, events);
        this.context = context;
        this.events = events;
        this.layout = layout;
    }

    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        public TextView tvTime;
        public TextView tvEventGroup;
        public TextView tvEvent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View row = convertView;
        if (row == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            row = layoutInflater.inflate(layout, null, true);
            viewHolder = new ViewHolder();
            viewHolder.tvTime = (TextView) row.findViewById(R.id.tvTime);
            viewHolder.tvEventGroup = (TextView) row.findViewById(R.id.tvEventGroup);
            viewHolder.tvEvent = (TextView) row.findViewById(R.id.tvEvent);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        SecurityEvent item = events.get(position);
        viewHolder.tvTime.setText(formatDate(item.getTimestamp()));
        viewHolder.tvEventGroup.setText(getEventName(item.getEventGroup(), null));
        viewHolder.tvEvent.setText(getEventName(item.getEventGroup(), item.getEvent()));
        return row;
    }


    private String formatDate(long timeStamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStamp);
        String time = DateFormat.format("HH:mm", cal).toString(); //yyyy-MM-dd
        return time;
    }

    private String getEventName(int eventGroup, Integer event) {


        String property = "event_group_" + eventGroup;
        if (event != null) {
            property += "_" + event;
        }
        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        int id = resources.getIdentifier(property, "string", packageName);
        if (id == 0) {
            if (event != null && !nonTranslatableEvents.contains(Integer.valueOf(eventGroup))) {
                // for this to work you must define zone names in strings file, eg:
                //  <string name="zone_1">Living room</string>
                id = resources.getIdentifier("zone_" + event, "string", packageName);
            } else {
                return event != null ? String.valueOf(event) : String.valueOf(eventGroup);
            }
        }
        return resources.getString(id);
    }

}
