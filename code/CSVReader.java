package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVReader {

    private List<Station> stations = new ArrayList<>();
    public static void main(String[] args) {
        // Read CSV file
        CSVReader reader = new CSVReader();
        reader.readCSV("src\\서울교통공사_1_8호선 역사 좌표(위경도) 정보_20231031.csv");

        /*
        // User input
        Scanner scanner = new Scanner(System.in);

        // Input the number of team members
        System.out.print("Enter the number of team members: ");
        int numberOfPeople = scanner.nextInt();
        scanner.nextLine(); // Handle the newline character

        // Input the station names
        List<Station> selectedStations = new ArrayList<>();
        for (int i = 0; i < numberOfPeople; i++) {
            System.out.print("Enter the station name for team member " + (i + 1) + ": ");
            String stationName = scanner.nextLine();

            // Find Station by name
            Station station = reader.findStationByName(stationName);
            if (station != null) {
                selectedStations.add(station);
            } else {
                System.out.println("The station name you entered could not be found: " + stationName);
                i--; // Prompt for input again
            }
        }

        // Calculate the midpoint latitude and longitude
        double[] midpoint = reader.calculateMidpoint(selectedStations);
        System.out.println("Midpoint Latitude: " + midpoint[0] + ", Longitude: " + midpoint[1]);

        // Find the nearest station to the midpoint
        Station nearestStation = reader.findNearestStation(midpoint[0], midpoint[1]);
        System.out.println("The nearest station to the midpoint is: " + nearestStation.getStationName());
        */
    }

    // Read CSV file
    public void readCSV(String csvFile) {
        String line;
        String cvsSplitBy = ",";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip the first line (header)

            while ((line = br.readLine()) != null) {
                String[] stationData = line.split(cvsSplitBy);
                int id = Integer.parseInt(stationData[0]);
                String lineName = stationData[1];
                String uniqueStationId = stationData[2];
                String stationName = stationData[3];
                double latitude = Double.parseDouble(stationData[4]);
                double longitude = Double.parseDouble(stationData[5]);
                LocalDate createdDate = LocalDate.parse(stationData[6], formatter);

                stations.add(new Station(id, lineName, uniqueStationId, stationName, latitude, longitude, createdDate));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Find Station by name
    public Station findStationByName(String name) {
        for (Station station : stations) {
            if (station.getStationName().equals(name)) {
                return station;
            }
        }
        return null;
    }

    // Calculate midpoint latitude and longitude
    public double[] calculateMidpoint(List<Station> stations) {
        double totalLatitude = 0;
        double totalLongitude = 0;

        for (Station station : stations) {
            totalLatitude += station.getLatitude();
            totalLongitude += station.getLongitude();
        }

        double midpointLatitude = totalLatitude / stations.size();
        double midpointLongitude = totalLongitude / stations.size();

        return new double[]{midpointLatitude, midpointLongitude};
    }

    // Find the nearest station to the midpoint
    public Station findNearestStation(double latitude, double longitude) {
        Station nearestStation = null;
        double shortestDistance = Double.MAX_VALUE;

        for (Station station : stations) {
            double distance = calculateDistance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestStation = station;
            }
        }

        return nearestStation;
    }

    // Calculate the distance between two points (using the Haversine formula)
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance (km)
    }

    // Return all stations
    public List<Station> getStations() {
        return stations;
    }

    // Station class
    public static class Station {
        private int id;
        private String line;
        private String uniqueStationId;
        private String stationName;
        private double latitude;
        private double longitude;
        private LocalDate createdDate;

        public Station(int id, String line, String uniqueStationId, String stationName,
                       double latitude, double longitude, LocalDate createdDate) {
            this.id = id;
            this.line = line;
            this.uniqueStationId = uniqueStationId;
            this.stationName = stationName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.createdDate = createdDate;
        }

        public String getLine() {
            return line;  // Return line name
        }

        public String getStationName() {
            return stationName;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return "Station{" +
                    "id=" + id +
                    ", line='" + line + '\'' +
                    ", uniqueStationId='" + uniqueStationId + '\'' +
                    ", stationName='" + stationName + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", createdDate=" + createdDate +
                    '}';
        }
    }
}
