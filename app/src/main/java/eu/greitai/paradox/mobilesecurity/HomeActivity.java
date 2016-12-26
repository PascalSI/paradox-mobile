package eu.greitai.paradox.mobilesecurity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import eu.greitai.paradox.mobilesecurity.data.SecurityEvent;
import eu.greitai.paradox.mobilesecurity.data.SecurityEventStore;
import eu.greitai.paradox.mobilesecurity.fetch.FetchSecurityEventsParams;
import eu.greitai.paradox.mobilesecurity.fetch.FetchSecurityEventsTask;
import eu.greitai.paradox.mobilesecurity.fetch.OnSecurityEventsFetched;
import eu.greitai.paradox.mobilesecurity.helpers.DateUtils;

public class HomeActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        CompoundButton.OnCheckedChangeListener,
        OnSecurityEventsFetched,
        View.OnClickListener,
        OnDateSelectedListener {

    private SecurityEventStore store;

    private SwipeRefreshLayout refreshLayout;

    private Switch swOnlyImportant;

    private ListView lvEvents;

    private TextView etDate;
    private ImageView ivDate;
    private long selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(this);

        lvEvents = (ListView) findViewById(R.id.lvEvents);

        swOnlyImportant = (Switch) findViewById(R.id.swOnlyImportant);
        swOnlyImportant.setChecked(true);
        swOnlyImportant.setOnCheckedChangeListener(this);

        etDate = (EditText) findViewById(R.id.etDate);
        ivDate = (ImageView) findViewById(R.id.ivDate);
        etDate.setOnClickListener(this);
        ivDate.setOnClickListener(this);

        // define these in gradle.properties:
        // AccessKey="XXXXX"
        // AccessKeySecret="XXXXXXX"
        // RegionId="EU_CENTRAL_1"
        store = new SecurityEventStore(
                    BuildConfig.ACCESS_KEY,
                    BuildConfig.ACCESS_KEY_SECRET,
                    BuildConfig.REGION_ID);

        selectDate(DateUtils.getStarOfDayInUtc().getTimeInMillis());
    }

    private void selectDate(long timeStamp) {
        selectedDate = timeStamp;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStamp);
        etDate.setText(DateFormat.format("yyyy-MM-dd", cal).toString());
        onRefresh();
    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        FetchSecurityEventsParams params =
                new FetchSecurityEventsParams();
        params.setFromTime(selectedDate);
        params.setToTime(selectedDate + 24*60*60*1000);
        params.setOnlyImportant(swOnlyImportant.isChecked());
        new FetchSecurityEventsTask(this, store)
        .execute(params);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.onRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSecurityEventsFetched(List<SecurityEvent> events) {
        refreshLayout.setRefreshing(false);
        List<SecurityEvent> filtered =  EventDisplayFilter.removeNoisyEvents(events);
        lvEvents.setAdapter(new EventDisplayAdapter(
                HomeActivity.this,
                R.layout.events_display_row,
                filtered));
        if (filtered.size() == 0) {
            Snackbar
                .make(findViewById(android.R.id.content),
                        getString(R.string.no_events_founds),
                        Snackbar.LENGTH_LONG)
                .show();
        }
    }


    @Override
    public void onClick(View v) {
        SelectDateFragment selectDate = new SelectDateFragment();

        Bundle arguments = new Bundle();
        arguments.putLong("date", selectedDate);
        selectDate.setArguments(arguments);

        selectDate.show(getSupportFragmentManager(), "selectDate");
    }

    @Override
    public void onDateSelected(long timeStamp) {
        selectDate(timeStamp);
    }
}
