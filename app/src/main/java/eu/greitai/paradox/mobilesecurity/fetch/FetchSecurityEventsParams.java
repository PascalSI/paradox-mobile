package eu.greitai.paradox.mobilesecurity.fetch;

public class FetchSecurityEventsParams {

    private long fromTime;
    private boolean onlyImportant;

    public FetchSecurityEventsParams(long fromTime, boolean onlyImportant) {
        this.fromTime = fromTime;
        this.onlyImportant = onlyImportant;
    }

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }

    public boolean isOnlyImportant() {
        return onlyImportant;
    }

    public void setOnlyImportant(boolean onlyImportant) {
        this.onlyImportant = onlyImportant;
    }
}
