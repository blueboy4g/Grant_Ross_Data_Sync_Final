package com.company;

import com.company.Mocks.MockDatabase;
import com.company.Mocks.MockECRM;
import com.company.Processes.Discover;
import com.company.Processes.Sync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {
    private JButton btnShowDatabase;
    private JButton btnShowEcrm;
    private JButton btnStartDiscovery;
    private JButton btnStartSync;
    private JButton btnQuit;

    private MockDatabase mockDatabase;
    private MockECRM mockECRM;
    private Discover discover;
    private Sync sync;



    public Main(MockDatabase mockDatabase, MockECRM mockECRM, Discover discover, Sync sync) {
        this.mockDatabase = mockDatabase;
        this.mockECRM = mockECRM;
        this.discover = discover;
        this.sync = sync;


        // Set up the frame
        setTitle("Data Synchronization GUI");
        setSize(775, 80);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout());

        // Initialize buttons
        btnShowDatabase = new JButton("Show Database");
        btnShowEcrm = new JButton("Show ECRM");
        btnStartDiscovery = new JButton("Start Discovery");
        btnStartSync = new JButton("Start Sync");
        btnQuit = new JButton("Quit");

        // Add action listeners
        btnShowDatabase.addActionListener(this);
        btnShowEcrm.addActionListener(this);
        btnStartDiscovery.addActionListener(this);
        btnStartSync.addActionListener(this);
        btnQuit.addActionListener(this);

        // Add buttons to frame
        add(btnShowDatabase);
        add(btnShowEcrm);
        add(btnStartDiscovery);
        add(btnStartSync);
        add(btnQuit);

        // Display the frame
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button click events
        if (e.getSource() == btnShowDatabase) {
            mockDatabase.printDatabase();
        } else if (e.getSource() == btnShowEcrm) {
            mockECRM.printECRM();
        } else if (e.getSource() == btnStartDiscovery) {
            discover.start();
        } else if (e.getSource() == btnStartSync) {
            sync.start();
        } else if (e.getSource() == btnQuit) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Run the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MockDatabase mockDatabase = new MockDatabase();
                MockECRM mockECRM = new MockECRM();
                Discover discover = new Discover(mockDatabase);
                Sync sync = new Sync(mockDatabase, mockECRM);
                new Main(mockDatabase, mockECRM, discover, sync);
            }
        });
    }
}
