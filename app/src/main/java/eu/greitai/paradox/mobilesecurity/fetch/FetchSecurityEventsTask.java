package eu.greitai.paradox.mobilesecurity.fetch;

import android.os.AsyncTask;
import java.util.List;

import eu.greitai.paradox.mobilesecurity.data.SecurityEvent;
import eu.greitai.paradox.mobilesecurity.data.SecurityEventStore;

public class FetchSecurityEventsTask extends
        AsyncTask<FetchSecurityEventsParams, Void, List<SecurityEvent>> {

    private OnSecurityEventsFetched callback;
    private SecurityEventStore store;

    public FetchSecurityEventsTask(
            OnSecurityEventsFetched callback,
            SecurityEventStore store) {
        this.callback = callback;
        this.store = store;
    }

    @Override
    protected  List<SecurityEvent> doInBackground(FetchSecurityEventsParams... params) {
        FetchSecurityEventsParams param = params[0];
        return store.getEvents(param.getFromTime(), param.getToTime(), param.isOnlyImportant());
    }

    @Override
    protected void onPostExecute(List<SecurityEvent> result) {
        this.callback.onSecurityEventsFetched(result);
    }

}

