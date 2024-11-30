package code;

import javax.swing.*;
import java.awt.*;
import java.io.*;


public class ScheduleInputGUI {
    private JFrame frame;
    private JButton[][] buttons; // 12x7 button array
    private int[][] schedule; // 12x7 schedule data
    private int studentCount; // Number of students to input
    private int requiredHour; // Required continuous hours
    private int currentStudent = 1; // Current student number being input
    private JLabel currentStudentLabel; // Label to display the current student number
    private GUI gui;

    public ScheduleInputGUI(GUI gui) {
        this.gui = gui;

        // Input the number of students
        String input = JOptionPane.showInputDialog("Enter the number of students:");
        if (input == null) { // If the cancel button is pressed
            return; // Exit the method
        }
        try {
            studentCount = Integer.parseInt(input);
            if (studentCount <= 0) {
                JOptionPane.showMessageDialog(frame, "The number of students must be at least 1.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.");
            return;
        }

        // Input the required hours
        String hourInput = JOptionPane.showInputDialog("Enter the required hours:");
        if (hourInput == null) { // If the cancel button is pressed
            return; // Exit the method
        }
        try {
            requiredHour = Integer.parseInt(hourInput);
            if (requiredHour <= 0) {
                JOptionPane.showMessageDialog(frame, "Required hours must be at least 1.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.");
            return;
        }

        // Build GUI
        frame = new JFrame("Set Team Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        schedule = new int[12][7]; // Initialize schedule
        buttons = new JButton[12][7];

        // Button array and layout setup
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        JPanel gridPanel = new JPanel(new GridLayout(12, 7));

        // Label to display the current student number
        currentStudentLabel = new JLabel("Current Student: 1 / " + studentCount, SwingConstants.CENTER);

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 7; j++) {
                final int row = i;
                final int col = j;

                // Create and initialize buttons
                buttons[i][j] = new JButton(days[j] + " " + (i + 9));
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                buttons[i][j].setOpaque(true);
                buttons[i][j].setBorderPainted(false);

                // Handle button click event
                buttons[i][j].addActionListener(e -> toggleButton(row, col));

                gridPanel.add(buttons[i][j]);
            }
        }

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> saveSchedule());


        // Build the frame
        frame.setLayout(new BorderLayout());
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(submitButton, BorderLayout.SOUTH);
        frame.add(currentStudentLabel, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);


        frame.setVisible(true);
    }

    // Toggle button state on click
    private void toggleButton(int row, int col) {
        if (schedule[row][col] == 0) {
            schedule[row][col] = 1;
            buttons[row][col].setBackground(Color.GREEN);
        } else {
            schedule[row][col] = 0;
            buttons[row][col].setBackground(Color.LIGHT_GRAY);
        }
    }

    // Save schedule
    private void saveSchedule() {
        // Save file as schedule1.txt, schedule2.txt, etc.
        String fileName = "src/schedules/schedule" + currentStudent + ".txt";

        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 7; j++) {
                    writer.write(schedule[i][j] + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "File save error: " + e.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(frame, "Schedule saved!");

        // Proceed based on the number of students
        currentStudent++;
        if (currentStudent <= studentCount) {
            resetSchedule(); // Reset schedule for the next student
            updateCurrentStudentLabel(); // Update student number
        } else {
            JOptionPane.showMessageDialog(frame, "All student schedules have been saved.");

            frame.dispose(); // Close the window when all inputs are done
            new TeamScheduler(gui, requiredHour);
            deleteAllScheduleFiles(); // Delete saved schedule files
        }
        // After file processing, pass the result to MainGUI
    }

    // Reset schedule
    private void resetSchedule() {
        schedule = new int[12][7]; // Initialize schedule
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 7; j++) {
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    // Update the label for the current student number
    private void updateCurrentStudentLabel() {
        currentStudentLabel.setText("Current Student: " + currentStudent + " / " + studentCount);
    }

    // Delete all inputted schedule files
    private void deleteAllScheduleFiles() {
        String baseFilePath = "src/schedules/schedule";

        int fileCount = 1;
        while (true) {
            File file = new File(baseFilePath + fileCount++ + ".txt");
            if (file.exists()) {
                if (!file.delete()) {
                    System.err.println("Failed to delete file: " + file.getAbsolutePath());
                } else {
                    System.out.println("Successfully deleted file: " + file.getAbsolutePath());
                }
            } else {
                break; // Exit when no more files
            }
        }
    }

}
