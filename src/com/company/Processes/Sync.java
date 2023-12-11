package com.company.Processes;

import com.company.Mocks.MockDatabase;
import com.company.Mocks.MockECRM;
import com.company.Models.CustomerRecordDatabase;
import com.company.Models.CustomerRecordECRM;

import java.util.ArrayList;
import java.util.List;

public class Sync {
    private MockDatabase mockDatabase;
    private MockECRM mockECRM;

    public Sync(MockDatabase mockDatabase, MockECRM mockECRM) {
        this.mockDatabase = mockDatabase;
        this.mockECRM = mockECRM;
    }

    private int BATCH_SIZE_SYNC = 2; //pull from a config server in real world

    public void start() {
        System.out.println("");
        System.out.println("------------------");
        System.out.println("Starting the Sync process.");
        //Checks if the sync process is already running, therefore no need to process simultaneously
        if (mockDatabase.isSyncRunning() == false) {
            mockDatabase.setSyncRunning(true);
            //Since discover is not already running we will start processing
            System.out.println("Looking for records in the database that are marked staged...");
            getBatchOfStagedRecords();
        } else {
            //Since sync is already running we will log a message out for additional info
            System.out.println("The sync process is already running...");
        }
    }

    public void getBatchOfStagedRecords() {
        //Get the number of records marked "staged"
        int numOfStagedRecords = mockDatabase.queryStagedRecords();
        if (mockDatabase.hasMoreRecordsSync(numOfStagedRecords) != true) {
            System.out.println("There are no staged records to process at the moment.");
            mockDatabase.setSyncRunning(false);
        } else {
            //While there are more records that needs sync'ing loop this
            while (mockDatabase.hasMoreRecordsSync(numOfStagedRecords)) {
                //Grab a batch of records
                List<CustomerRecordDatabase> batchedList = mockDatabase.getNextSyncRecordBatchStaged(BATCH_SIZE_SYNC);
                if (batchedList.size() == 0){
                    break;
                }
                System.out.println("Sync has a batch job of size: " + batchedList.size() + " Max size: " + BATCH_SIZE_SYNC);
                //Map the database model to the ECRM model (Some fields won't be needed)
                List<CustomerRecordECRM> batchedListECRM = mapCustomerValues(batchedList);
                //Update the records in ECRM to the correct values for the custId via a batch list
                mockECRM.updateSyncRecordsToECRM(batchedListECRM);
                //Change the status of the records in the database to be "good" and not "staged"
                revertStagedStatus(batchedListECRM);
            }
            //Change the running indicator to be false since we are done running
            mockDatabase.resetBatchNumSync();
            mockDatabase.setSyncRunning(false);
            System.out.println("Sync process has finished processing all customers to the ECRM.");
        }
    }
    public List<CustomerRecordECRM> mapCustomerValues(List<CustomerRecordDatabase> customerRecordDatabase) {
        List<CustomerRecordECRM> batchedListECRM = new ArrayList<>();
        //for each customerRecordDatabase model map it to a customerRecordECRM and add it to a list
        for (CustomerRecordDatabase customer : customerRecordDatabase) {
            int custId = (customer.getCustId());
            String firstName = (customer.getFirstName());
            String lastName = (customer.getLastName());
            CustomerRecordECRM customerRecordECRM = new CustomerRecordECRM(custId, firstName, lastName);
            batchedListECRM.add(customerRecordECRM);
        }
        return batchedListECRM;
    }
    public void revertStagedStatus(List<CustomerRecordECRM> batchedListECRM){
        for (CustomerRecordECRM customer : batchedListECRM){
            //flips the value to be "good" status in the database
            mockDatabase.updateSyncRecordStatus(customer.getCustId(), "completed");
        }
    }
}