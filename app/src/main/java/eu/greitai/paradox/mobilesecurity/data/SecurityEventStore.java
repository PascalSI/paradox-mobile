package eu.greitai.paradox.mobilesecurity.data;

import android.text.format.DateFormat;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.QueryResultPage;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class SecurityEventStore {

    private final AmazonDynamoDBClient db;
    private final int DEFAULT_PARTITION = 0;

    public SecurityEventStore(String accessKey, String secretKey, String region) {
        this.db = new AmazonDynamoDBClient(new BasicAWSCredentials(accessKey, secretKey));
        this.db.setRegion(Region.getRegion(Regions.valueOf(region)));
    }


    public List<SecurityEvent> getEvents(
            Long fromTimestamp,
            Long toTimestamp,
            boolean onlyImportant) {

        Condition timeCondition = new Condition()
                .withComparisonOperator(getComparisonOperator(fromTimestamp, toTimestamp))
                .withAttributeValueList(getTimestampValues(fromTimestamp, toTimestamp));

        DynamoDBQueryExpression<SecurityEvent> query = new DynamoDBQueryExpression<SecurityEvent>()
                .withHashKeyValues(new SecurityEvent(DEFAULT_PARTITION))
                .withRangeKeyCondition("timestamp", timeCondition)
                .withScanIndexForward(false);
        if (onlyImportant) {
            Map<String, Condition> queryFilter = new HashMap<>();
            queryFilter.put("eventGroup", new Condition()
                    .withAttributeValueList(new AttributeValue().withN("1"))
                    .withComparisonOperator(ComparisonOperator.GT));
            query.setQueryFilter(queryFilter);
        } else {
            query.setLimit(500);
        }
        QueryResultPage<SecurityEvent> qr = new DynamoDBMapper(this.db)
                .queryPage(
                SecurityEvent.class,
                query,
                new DynamoDBMapperConfig(getTableName(fromTimestamp)));
        return qr.getResults();
    }

    private List<AttributeValue> getTimestampValues(Long fromTimestamp, Long toTimestamp) {
        List<AttributeValue> vals = new ArrayList<>();
        if (fromTimestamp != null) {
            vals.add(new AttributeValue().withN(String.valueOf(fromTimestamp)));
        }
        if (toTimestamp != null) {
            vals.add(new AttributeValue().withN(String.valueOf(toTimestamp)));
        }
        return vals;
    }

    private ComparisonOperator getComparisonOperator(Long fromTimestamp, Long toTimestamp) {
        if (fromTimestamp != null && toTimestamp != null) {
            return ComparisonOperator.BETWEEN;
        } else if (fromTimestamp != null) {
            return ComparisonOperator.GE;
        } else if (toTimestamp != null) {
            return ComparisonOperator.LE;
        } else {
            throw new IllegalArgumentException("must provide fromTimestamp or toTimestamp");
        }
    }

    private TableNameOverride getTableName(long timeStamp) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timeStamp);
        return new TableNameOverride("events-" + DateFormat.format("yyyy-MM", cal).toString());
    }
}
