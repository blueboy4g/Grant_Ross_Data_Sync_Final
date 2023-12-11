package com.company.Processes;

import com.company.Mocks.MockDatabase;
import com.company.Models.CustomerRecordDatabase;

import java.sql.Timestamp;
import java.util.List;

public class Discover {
    private MockDatabase mockDatabase;

    public Discover(MockDatabase mockDatabase) {
        this.mockDatabase = mockDatabase;
    }
    private int BATCH_SIZE_DISCOVER = 2; //pull from a config server in real world

    public void start(){
        System.out.println("");
        System.out.println("------------------");
        System.out.println("Starting the Discover process.");
        //Checks if the discover process is already running, therefore no need to process simultaneously
        if (mockDatabase.isDiscoverRunning() == false) {
            //Since discover is not already running we will set indicator to true and start processing
            mockDatabase.setDiscoverRunning(true);
            System.out.println("Looking for records in the database that need synchronization...");
            //Mocks a call to a database that grabs the 'lastRanTimestamp (The mock returns 5min ago)'
            Timestamp timestamp = mockDatabase.getLastRanTimestampForDiscover();
            System.out.println("Discover last ran at: " + timestamp);
            getBatchOfRecordsThatNeedSync(timestamp);
        }
        else {
            //Since discover is already running we will log a message out for additional info
            System.out.println("The discover process is already running...");
        }
    }
    public void getBatchOfRecordsThatNeedSync(Timestamp timestamp){
        List<CustomerRecordDatabase> numOfRecords = mockDatabase.queryCustomerRecords(timestamp);
        //While there are more records that fit the criteria to sync loop this
        while (mockDatabase.hasMoreRecordsDiscover(numOfRecords.size())) {
            //Get a batch of customers in batchedList that need sync'ing
            List<CustomerRecordDatabase> batchedList = mockDatabase.getNextSyncRecordBatch(timestamp, BATCH_SIZE_DISCOVER);
            System.out.println("Discover has a batch job of size: " + batchedList.size() + " Max size: " + BATCH_SIZE_DISCOVER);
            updateStatusToStaged(batchedList);
        }
        System.out.println("Discover process has finished changing all customer records to staged status.");
        //We are done running, set the running indicator back to false
        mockDatabase.resetBatchNumDiscover();
        mockDatabase.updateLastRanTimestampForDiscover();
        mockDatabase.setDiscoverRunning(false);
    }
    public void updateStatusToStaged(List<CustomerRecordDatabase> batchedList){
        //For each record in the batch update the status to "staged" for sync to pull later
        for (CustomerRecordDatabase customerRecordDatabase : batchedList){
            mockDatabase.updateSyncRecordStatus(customerRecordDatabase.getCustId(), "staged");
        }
    }
}
