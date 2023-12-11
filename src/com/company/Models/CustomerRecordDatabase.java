package com.company.Models;

import java.sql.Timestamp;

public class CustomerRecordDatabase {
    private int custId;
    private String firstName;
    private String lastName;
    private Timestamp timestamp;
    private String status;

    public CustomerRecordDatabase(int custId, String firstName, String lastName, Timestamp timestamp, String status) {
        this.custId = custId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.timestamp = timestamp;
        this.status = status;
    }
    public int getCustId() {
        return custId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    @Override
    public String toString() {
        return "CustomerRecordDatabase{" +
                "custId=" + custId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
