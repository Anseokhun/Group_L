package code;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;


public class GUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().createAndShowGUI());
    }
    CardLayout cardLayout;
    JPanel cardPanel;

    private void createAndShowGUI() {
        JFrame frame = new JFrame("projectMate");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // First screen (Home screen)
        JPanel homePanel = new JPanel(new BorderLayout());
        ImageIcon logoIcon = new ImageIcon("src/img/logo.png");
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            System.out.println("Image loaded successfully!");
        } else {
            System.out.println("Failed to load image.");
        }

        Image img = logoIcon.getImage(); // Get the original image
        Image scaledImg = img.getScaledInstance(400, 400, Image.SCALE_SMOOTH); // Resize the image
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel logoLabel = new JLabel(scaledIcon, SwingConstants.CENTER);
        homePanel.add(logoLabel, BorderLayout.CENTER);


        // Start button panel
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton endButton = new JButton("Exit");
        buttonPanel.add(startButton);
        buttonPanel.add(endButton);
        homePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Switch screens on button click
        startButton.addActionListener(e -> cardLayout.show(cardPanel, "Features"));
        endButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                return;
            }
        });


        // Second screen (Feature selection screen)
        JPanel featurePanel = new JPanel(new GridLayout(3, 1));
        JButton func1 = new JButton("Team Scheduler");
        featurePanel.add(func1);

        JButton func2 = new JButton("Station Selector");
        featurePanel.add(func2);

        JButton func3 = new JButton("Back");
        featurePanel.add(func3);


        func1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSchedulerInputGUI();  // Open SchedulerInputGUI on button click
            }
        });

        func2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openStationSelector();  // Open StationSelector on button click
            }
        });

        func3.addActionListener(e -> cardLayout.show(cardPanel, "Home"));




        // Add panels to CardLayout
        cardPanel.add(homePanel, "Home");
        cardPanel.add(featurePanel, "Features");


        frame.add(cardPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openSchedulerInputGUI() {
        // Pass MainGUI object to SchedulerInputGUI constructor
        new ScheduleInputGUI(this);  // this refers to the MainGUI object
    }

    private void openStationSelector() {
        new StationSelector();
    }

    public void showScheduleResults(String result, List<TeamScheduler.TimeSlot> validTimes, int requiredHour) {

        // Text area to display results
        JTextArea textArea = new JTextArea(result);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Create the "View Recommended Library Reservation Time" button
        JButton libraryButton = new JButton("View Recommended Library Reservation Time");
        libraryButton.addActionListener(e -> showLibraryRecommendation(validTimes, requiredHour)); // Pass validTimes

        // Create the "Back" button
        JButton returnButton = new JButton("Back");
        returnButton.addActionListener(e -> cardLayout.show(cardPanel, "Features")); // Return to the feature selection screen

        // Configure the result panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(new JScrollPane(textArea), BorderLayout.CENTER); // Add result text

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(libraryButton); // Add button
        buttonPanel.add(returnButton); // Add button
        resultPanel.add(buttonPanel, BorderLayout.SOUTH); // Add button panel

        // Add to CardLayout
        cardPanel.add(resultPanel, "result");
        cardLayout.show(cardPanel, "result");
    }

    private void showLibraryRecommendation(List<TeamScheduler.TimeSlot> validTimes, int requiredHour) {
        try {
            LibraryScheduleManager manager = new LibraryScheduleManager("src/librarySchedule.txt");

            // Calculate recommended times
            List<TeamScheduler.TimeSlot> recommendedTimes = manager.findRecommendedTimes(validTimes);

            // Generate recommendation results
            List<String> results = manager.generateLibraryResults(recommendedTimes, requiredHour);
            String resultText = String.join("\n", results);

            // Display results in a popup
            JOptionPane.showMessageDialog(null, resultText, "Recommended Library Reservation Time", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading library schedule information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
