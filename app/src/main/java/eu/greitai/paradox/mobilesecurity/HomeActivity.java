package eu.greitai.paradox.mobilesecurity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


import eu.greitai.paradox.mobilesecurity.data.SecurityEvent;
import eu.greitai.paradox.mobilesecurity.data.SecurityEventStore;
import eu.greitai.paradox.mobilesecurity.fetch.FetchSecurityEventsParams;
import eu.greitai.paradox.mobilesecurity.fetch.FetchSecurityEventsTask;
import eu.greitai.paradox.mobilesecurity.fetch.OnSecurityEventsFetched;

public class HomeActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        CompoundButton.OnCheckedChangeListener,
        OnSecurityEventsFetched {

    private SecurityEventStore store;

    private SwipeRefreshLayout refreshLayout;

    private Switch swOnlyImportant;

    private ListView lvEvents;


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


        // define these in gradle.properties:
        // AccessKey="XXXXX"
        // AccessKeySecret="XXXXXXX"
        // RegionId="EU_CENTRAL_1"
        store = new SecurityEventStore(
                    BuildConfig.ACCESS_KEY,
                    BuildConfig.ACCESS_KEY_SECRET,
                    BuildConfig.REGION_ID);
        this.onRefresh();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        new FetchSecurityEventsTask(this, store)
        .execute(new FetchSecurityEventsParams(getStartOfDay(), swOnlyImportant.isChecked()));
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
        List<String> mapped = new ArrayList<>();
        for (SecurityEvent event: events) {
            mapped.add(
                event.getTimestamp() + " " +
                event.getEventGroup() + " " +
                event.getEvent()
            );
        }
        lvEvents.setAdapter(new ArrayAdapter<>(
                HomeActivity.this,
                android.R.layout.simple_list_item_1,
                mapped));
    }

    private long getStartOfDay() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
