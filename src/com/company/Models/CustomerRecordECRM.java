package com.company.Models;

import java.sql.Timestamp;

public class CustomerRecordECRM {
    private int custId;
    private String firstName;
    private String lastName;

    public CustomerRecordECRM(int custId, String firstName, String lastName) {
        this.custId = custId;
        this.firstName = firstName;
        this.lastName = lastName;
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
    public void setCustId(int custId) {
        this.custId = custId;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "CustomerRecordECRM{" +
                "custId=" + custId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

}

