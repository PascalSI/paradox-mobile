package eu.greitai.paradox.mobilesecurity.fetch;

public class FetchSecurityEventsParams {

    private Long fromTime = null;
    private Long toTime = null;
    private boolean onlyImportant = true;

    public FetchSecurityEventsParams() {
    }

    public Long getFromTime() {
        return fromTime;
    }

    public void setFromTime(Long fromTime) {
        this.fromTime = fromTime;
    }

    public boolean isOnlyImportant() {
        return onlyImportant;
    }

    public void setOnlyImportant(boolean onlyImportant) {
        this.onlyImportant = onlyImportant;
    }

    public Long getToTime() {
        return toTime;
    }

    public void setToTime(Long toTime) {
        this.toTime = toTime;
    }
}
