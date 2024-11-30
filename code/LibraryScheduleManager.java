package code;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryScheduleManager {

    private int[][] librarySchedule;

    // Constructor: Initialize library reservation information
    public LibraryScheduleManager(String filePath) throws IOException {
        librarySchedule = readLibrarySchedule(filePath);
    }

    // Read library reservation information
    public static int[][] readLibrarySchedule(String filePath) throws IOException {
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

    // Recommend available time slots
    public List<TeamScheduler.TimeSlot> findRecommendedTimes(List<TeamScheduler.TimeSlot> validTimes) {
        List<TeamScheduler.TimeSlot> recommendedTimes = new ArrayList<>();

        for (TeamScheduler.TimeSlot time : validTimes) {
            int day = time.day;
            int startTime = time.startTime;

            // Check library reservation availability
            if (librarySchedule[startTime - 9][day] == 1) {
                recommendedTimes.add(time);
            }
        }

        return recommendedTimes;
    }

    // Generate result string list
    public List<String> generateLibraryResults(List<TeamScheduler.TimeSlot> times, int requiredHour) {
        List<String> results = new ArrayList<>();
        if (times.isEmpty()) {
            results.add("Don't have a time available for reservation.");
        } else {
            results.add("Library reservation recommendation time zone:");
            for (TeamScheduler.TimeSlot time : times) {
                String dayOfWeek = TeamScheduler.getDayOfWeek(time.day);
                results.add("- " + dayOfWeek + " " + time.startTime + " ~ " + (time.startTime + requiredHour));
            }
        }
        return results;
    }
}
