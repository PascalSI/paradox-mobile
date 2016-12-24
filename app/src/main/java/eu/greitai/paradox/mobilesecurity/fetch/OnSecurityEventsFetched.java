package eu.greitai.paradox.mobilesecurity.fetch;

import java.util.List;

import eu.greitai.paradox.mobilesecurity.data.SecurityEvent;

public interface OnSecurityEventsFetched {
    void onSecurityEventsFetched(List<SecurityEvent> events);
}
