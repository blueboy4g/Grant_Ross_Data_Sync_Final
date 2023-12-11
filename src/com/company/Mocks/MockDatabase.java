package com.company.Mocks;

import com.company.Models.CustomerRecordDatabase;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MockDatabase {

    private List<CustomerRecordDatabase> syncRecords;
    private List<CustomerRecordDatabase> syncRecordsStaged;
    private List<CustomerRecordDatabase> syncRecordsAfterLastRanTimestamp;
    private int batchNumDiscover = 0;
    private int batchNumSync = 0;
    private boolean isRunningDiscover = false;
    private boolean isRunningSync = false;
    private Timestamp lastRanTimestamp = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5));

    public MockDatabase(){
        //Fake customer data that simulates how a database might look at any given time
        syncRecords = new ArrayList<>();
        syncRecordsAfterLastRanTimestamp = new ArrayList<>();
        syncRecords.add(new CustomerRecordDatabase(
                123499, "Grant","Ross", randomTimestampFromLastFewMin(), "good"));
        syncRecords.add(new CustomerRecordDatabase(
                128452, "Rachel","Ross", randomTimestampFromLastFewMin(), "good"));
        syncRecords.add(new CustomerRecordDatabase(
                123055, "John","Smith", randomTimestampFromLastFewMin(), "good"));
        syncRecords.add(new CustomerRecordDatabase(
                123350, "Grant","Smith", randomTimestampFromLastFewMin(), "good"));
        syncRecords.add(new CustomerRecordDatabase(
                123159, "Smith","Senior", randomTimestampFromLastFewMin(), "good"));
        syncRecords.add(new CustomerRecordDatabase(
                112171, "Tyler","Swift",
                new Timestamp(System.currentTimeMillis()- TimeUnit.MINUTES.toMillis(6)), "completed"));
        syncRecords.add(new CustomerRecordDatabase(
                123954, "Lisa","Mei", randomTimestampFromLastFewMin(), "good"));
        syncRecords.add(new CustomerRecordDatabase(
                123400, "Ashley","Scott", randomTimestampFromLastFewMin(), "good"));
        syncRecords.add(new CustomerRecordDatabase(
                127457, "Tim","Nelson", randomTimestampFromLastFewMin(), "good"));
        syncRecords.add(new CustomerRecordDatabase(
                122177, "Mike","Miller", randomTimestampFromLastFewMin(), "good"));
    }

    public Timestamp randomTimestampFromLastFewMin(){
        int randomNumOfMin = ThreadLocalRandom.current().nextInt(1, 3);
        return new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(randomNumOfMin));
    }

    public Timestamp getLastRanTimestampForDiscover(){
        return lastRanTimestamp;
    }

    public void updateLastRanTimestampForDiscover(){
        lastRanTimestamp = new Timestamp(System.currentTimeMillis());
    }

    public boolean isDiscoverRunning(){
        return isRunningDiscover;
    }
    public boolean isSyncRunning(){
        return isRunningSync;
    }
    public void setDiscoverRunning(boolean isRunning){
        this.isRunningDiscover = isRunning;
    }
    public void setSyncRunning(boolean isRunning){
        this.isRunningSync = isRunning;
    }
    public List<CustomerRecordDatabase> getNextSyncRecordBatch(Timestamp timestamp, int limit) {
        //For a real database the timestamp param would be used to only query records after said timestamp
        List<CustomerRecordDatabase> batch = new ArrayList<>();
        for (int i = 0; i < limit && batchNumDiscover < syncRecordsAfterLastRanTimestamp.size(); i++, batchNumDiscover++) {
            batch.add(syncRecordsAfterLastRanTimestamp.get(batchNumDiscover));
        }
        return batch;
    }

    public boolean hasMoreRecordsDiscover(int numOfStagedRecords) {
        return batchNumDiscover < numOfStagedRecords;
    }

    public int queryStagedRecords() {
        syncRecordsStaged = new ArrayList<>();
        for (CustomerRecordDatabase customerRecordDatabase : syncRecords) {
            if (customerRecordDatabase.getStatus() == "staged") {
                syncRecordsStaged.add(customerRecordDatabase);
            }
        }
        System.out.println("The amount of staged records to process is: " + syncRecordsStaged.size());
        return syncRecordsStaged.size();
    }

    public List<CustomerRecordDatabase> queryCustomerRecords(Timestamp timestamp) {
        syncRecordsAfterLastRanTimestamp = new ArrayList<>();
        for (CustomerRecordDatabase customerRecordDatabase : syncRecords) {
            if (customerRecordDatabase.getTimestamp().after(timestamp) || (customerRecordDatabase.getStatus() == "staged")) {
                syncRecordsAfterLastRanTimestamp.add(customerRecordDatabase);
            }
        }
        System.out.println("The amount of records discovered by discovery to process is: " + syncRecordsAfterLastRanTimestamp.size());
        return syncRecordsAfterLastRanTimestamp;
    }

    public boolean hasMoreRecordsSync(int numOfStagedRecords) {
//        System.out.println(numOfStagedRecords);
//        System.out.println(batchNumSync);
//        System.out.println(syncRecords);
//        System.out.println(syncRecordsStaged);
//        System.out.println(syncRecordsAfterLastRanTimestamp);
        return batchNumSync < numOfStagedRecords;
    }
    public void updateSyncRecordStatus(int custId, String status) {
        for (CustomerRecordDatabase customerRecordDatabase : syncRecords){
            if (customerRecordDatabase.getCustId() == custId && (customerRecordDatabase.getStatus() != "staged")) {
                customerRecordDatabase.setStatus(status);
                System.out.println("Updated customer id: " + custId + " to have status of: " + status);
                break;
            }
        }
    }
    public List<CustomerRecordDatabase> getNextSyncRecordBatchStaged(int limit) {
        List<CustomerRecordDatabase> batch = new ArrayList<>();
        for (int i = 0; i < limit && batchNumSync < syncRecordsAfterLastRanTimestamp.size(); i++, batchNumSync++) {
            if (syncRecordsAfterLastRanTimestamp.get(batchNumSync).getStatus() == "staged") {
                batch.add(syncRecordsAfterLastRanTimestamp.get(batchNumSync));
            }
        }
        return batch;
    }
    public void printDatabase() {
        System.out.println("");
        System.out.println("------------------");
        System.out.println("Showing Database");
        for (CustomerRecordDatabase customer : syncRecords) {
            System.out.println(customer.toString());
        }
    }
    public void resetBatchNumDiscover() {
        batchNumDiscover = 0;
    }

    public void resetBatchNumSync() {
        batchNumSync=0;
    }

}
