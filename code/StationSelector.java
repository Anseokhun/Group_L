package code;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StationSelector {

    int numberOfPeople;
    public StationSelector() {
        // Read CSV file and retrieve station data
        CSVReader reader = new CSVReader();
        reader.readCSV("src\\서울교통공사_1_8호선 역사 좌표(위경도) 정보_20231031.csv");

        // Set up JFrame
        JFrame frame = new JFrame("Select Team Members' Stations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panels = new JPanel();
        panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));


        // Input the number of team members
        String teamSizeStr = JOptionPane.showInputDialog(frame, "Enter the number of team members:");
        if (teamSizeStr == null) { // If the cancel button is pressed
            frame.dispose();
            return; // Exit the method
        }
        try {
            numberOfPeople = Integer.parseInt(teamSizeStr);
            if (numberOfPeople <= 0) {
                JOptionPane.showMessageDialog(frame, "The number of team members must be at least 1.");
                frame.dispose();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.");
            frame.dispose();
            return;
        }

        // Retrieve station information
        List<CSVReader.Station> stations = reader.getStations();
        // Extract only station names and store them in the stationNames array
        String[] stationNames = stations.stream().map(CSVReader.Station::getStationName).toArray(String[]::new);

        // Dynamically create JComboBoxes to allow station selection
        JComboBox<String>[] stationComboBoxes = new JComboBox[numberOfPeople];

        // Allow each team member to select a station
        for (int i = 0; i < numberOfPeople; i++) {
            JPanel panel = new JPanel(); // Add JPanel to group the label and JComboBox
            panel.add(new JLabel("Select station for team member " + (i + 1) + ":"));
            stationComboBoxes[i] = new JComboBox<>(stationNames); // Create JComboBox to display station names
            panel.add(stationComboBoxes[i]);
            panels.add(panel);
        }


        // "Submit" button to display selected station info and calculate midpoint
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<CSVReader.Station> selectedStations = new ArrayList<>();
                for (int i = 0; i < numberOfPeople; i++) {
                    String selectedStationName = (String) stationComboBoxes[i].getSelectedItem();
                    CSVReader.Station selectedStation = reader.findStationByName(selectedStationName);
                    if (selectedStation != null) {
                        selectedStations.add(selectedStation);
                    }
                }

                // Calculate midpoint latitude and longitude
                double[] midpoint = reader.calculateMidpoint(selectedStations);

                // Find the nearest station to the midpoint
                CSVReader.Station nearestStation = reader.findNearestStation(midpoint[0], midpoint[1]);
                JOptionPane.showMessageDialog(frame, "The nearest station to the midpoint is: " + nearestStation.getStationName());
                frame.dispose();
            }

        });
        frame.add(panels, BorderLayout.NORTH);
        frame.add(submitButton, BorderLayout.SOUTH);

        // Set JFrame size
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new StationSelector();
    }
}
