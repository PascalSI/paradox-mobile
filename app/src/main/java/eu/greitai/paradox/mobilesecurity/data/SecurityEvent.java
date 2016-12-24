package eu.greitai.paradox.mobilesecurity.data;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "event")
public class SecurityEvent {
    private int partition;
    private long timestamp;
    private int eventGroup;
    private int event;
    private int status;

    public SecurityEvent() {

    }

    public SecurityEvent(int partition) {
        this.partition = partition;
    }

    @DynamoDBHashKey(attributeName = "partition")
    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    @DynamoDBRangeKey(attributeName = "timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDBAttribute(attributeName = "eventGroup")
    public int getEventGroup() {
        return eventGroup;
    }

    public void setEventGroup(int eventGroup) {
        this.eventGroup = eventGroup;
    }

    @DynamoDBAttribute(attributeName = "event")
    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    @DynamoDBAttribute(attributeName = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}