package code;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TeamScheduler {

    public TeamScheduler(GUI gui, int Requiredhour) {
        // Read schedule files
        List<int[][]> schedules = new ArrayList<>();
        String baseFilePath = "src/schedules/schedule";

        int fileCount = 1;

        while (true) {
            try {
                String filePath = baseFilePath + fileCount++ + ".txt";
                schedules.add(readSchedule(filePath));
            } catch (IOException e) {
                System.err.println("File read error: " + e.getMessage());
                break;
            }
        }

        // Create DP table: 12 hours x 7 days
        int[][] dp = new int[12][7]; // Table for the number of team members available at each time slot
        int[][] dp2 = new int[12][7]; // Table for calculating continuous time slots

        // Add team members' schedules to the DP table (number of team members available at each time)
        for (int[][] schedule : schedules) {
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 7; j++) {
                    dp[i][j] += schedule[i][j];
                }
            }
        }

        // Calculate optimal time slots
        List<TimeSlot> times = findBestTimeWithDP(dp, dp2, schedules.size(), Requiredhour);
        String result = String.join("\n", generateResults(times, dp2)); // Convert list to a single string
        printResults(times, dp2);
        gui.showScheduleResults(result, times, Requiredhour);
    }

    // Class to store time slot information
    static class TimeSlot {
        int day; // Day of the week (0: Monday, 1: Tuesday, ...)
        int startTime; // Start time (starting from 9 AM)

        public TimeSlot(int day, int startTime) {
            this.day = day;
            this.startTime = startTime;
        }
    }

    // Convert a number to a day of the week name
    public static String getDayOfWeek(int day) {
        switch (day) {
            case 0: return "Mon";
            case 1: return "Tue";
            case 2: return "Wed";
            case 3: return "Thu";
            case 4: return "Fri";
            case 5: return "Sat";
            case 6: return "Sun";
            default: throw new IllegalArgumentException("Invalid day: " + day);
        }
    }

    // Read schedule data from a file
    public static int[][] readSchedule(String filePath) throws IOException {
        int[][] schedule = new int[12][7];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < 12; i++) {
                String[] line = reader.readLine().split(" ");
                for (int j = 0; j < 7; j++) {
                    schedule[i][j] = Integer.parseInt(line[j]);
                }
            }
        }
        return schedule;
    }

    // Calculate optimal time slots using DP (DP: number of team members available at each time slot)
    public static List<TimeSlot> findBestTimeWithDP(int[][] dp, int[][] dp2, int teamSize, int Requiredhour) {
        List<TimeSlot> validTimes = new ArrayList<>();

        // Fill DP2 table (DP2: stores continuous available times)
        for (int j = 0; j < 7; j++) { // Per day
            for (int i = 11; i >= 0; i--) { // Process times in reverse order
                if (dp[i][j] == teamSize) { // When all team members are available (DP: number of team members available at the time)
                    if (i == 11) { // Last time slot
                        dp2[i][j] = 1;
                    } else {
                        dp2[i][j] = dp2[i + 1][j] + 1;
                    }
                } else {
                    dp2[i][j] = 0; // Time slot is not available for teamwork
                }
            }
        }
        // Calculate time slots that meet the required hours (Requiredhour = required time)
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 12; i++) {
                if (dp2[i][j] >= Requiredhour) { // Save if it meets the required hours
                    validTimes.add(new TimeSlot(j, i + 9)); // Save the time and day in TimeSlot and store it as a list
                    i += dp2[i][j] - 1; // Skip continuous time slots to avoid duplication
                }
            }
        }

        return validTimes;
    }

    // Print results
    public static void printResults(List<TimeSlot> times, int[][] dp2) {
        if (times.isEmpty()) {
            System.out.println("The required time is not satisfied."); // When the list is empty
        } else {
            System.out.println("Available time slots: ");
            for (TimeSlot time : times) {
                String dayOfWeek = getDayOfWeek(time.day); // Convert the day stored as an integer
                int continuousHours = dp2[time.startTime - 9][time.day]; // Continuous available hours
                // Print day, start time ~ (start time + continuous hours)
                System.out.println("- " + dayOfWeek + time.startTime + "~" + (time.startTime + continuousHours));
            }
        }
    }

    // Save results as a list
    public static List<String> generateResults(List<TimeSlot> times, int[][] dp2) {
        List<String> results = new ArrayList<>();
        if (times.isEmpty()) {
            results.add("The required time is not satisfied.");
        } else {
            results.add("Available time slots:");
            for (TimeSlot time : times) {
                String dayOfWeek = getDayOfWeek(time.day);
                int continuousHours = dp2[time.startTime - 9][time.day];
                results.add("- " + dayOfWeek + " " + time.startTime + " ~ " + (time.startTime + continuousHours));
            }
        }
        return results;
    }

    public static void main(String[] args) {
    }
}
