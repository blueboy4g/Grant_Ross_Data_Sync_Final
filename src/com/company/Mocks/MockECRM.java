package com.company.Mocks;

import com.company.Models.CustomerRecordDatabase;
import com.company.Models.CustomerRecordECRM;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MockECRM {

    private List<CustomerRecordECRM> syncRecords;

    public MockECRM() {
        //Fake customer data that simulates how a ECRM might look prior to a sync
        syncRecords = new ArrayList<>();
        syncRecords.add(new CustomerRecordECRM(
                123499, "Grant", "invalid_lastname"));
        syncRecords.add(new CustomerRecordECRM(
                128452, "invalid_firstname", "invalid_lastname"));
        syncRecords.add(new CustomerRecordECRM(
                123055, "invalid_firstname", "invalid_lastname"));
        syncRecords.add(new CustomerRecordECRM(
                123350, "Grant", "invalid_lastname"));
        syncRecords.add(new CustomerRecordECRM(
                123159, "Smith", "invalid_lastname"));
        syncRecords.add(new CustomerRecordECRM(
                112171, "Tyler", "Swift"));
        syncRecords.add(new CustomerRecordECRM(
                123954, "invalid_firstname", "Mei"));
        syncRecords.add(new CustomerRecordECRM(
                123400, "invalid_firstname", "Scott"));
        syncRecords.add(new CustomerRecordECRM(
                127457, "invalid_firstname", "Nelson"));
        syncRecords.add(new CustomerRecordECRM(
                122177, "invalid_firstname", "Miller"));
    }

    public void updateSyncRecordsToECRM(List<CustomerRecordECRM> customerRecordECRM_updated) {
        for (CustomerRecordECRM customer : customerRecordECRM_updated){
            int custId = customer.getCustId();
            for (CustomerRecordECRM customerRecordECRM_outdated : syncRecords) {
                if (customerRecordECRM_outdated.getCustId() == custId) {
                    System.out.println("The ECRM platform has this outdated info: " + customerRecordECRM_outdated.toString());
                    System.out.println("The Database requests to updated to this info: " + customer.toString());
                    customerRecordECRM_outdated.setFirstName(customer.getFirstName());
                    customerRecordECRM_outdated.setLastName(customer.getLastName());
                    System.out.println("The ECRM platform has updated to the info: " + customerRecordECRM_outdated.toString());
                    break;
                }
            }
        }
    }
    public void printECRM() {
        System.out.println("");
        System.out.println("------------------");
        System.out.println("Showing ECRM");
        for (CustomerRecordECRM customer : syncRecords) {
            System.out.println(customer.toString());
        }
    }
}
