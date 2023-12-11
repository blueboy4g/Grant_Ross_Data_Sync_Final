package com.company;

import com.company.Mocks.MockDatabase;
import com.company.Mocks.MockECRM;
import com.company.Processes.Discover;
import com.company.Processes.Sync;

public class TestAll {

    public static void main(String[] args) {
        MockDatabase mockDatabase = new MockDatabase();
        MockECRM mockECRM = new MockECRM();
        mockECRM.printECRM();
        Discover discover = new Discover(mockDatabase);
        discover.start();
        Sync sync = new Sync(mockDatabase, mockECRM);
        sync.start();
        mockECRM.printECRM();
    }
}
