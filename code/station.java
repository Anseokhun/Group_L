package code;
import java.time.LocalDate;

public class station {
    private int id;                // ID
    private String line;           // Line
    private String uniqueStationId; // Unique Station ID
    private String stationName;    // Station Name
    private double latitude;       // Latitude
    private double longitude;      // Longitude
    private LocalDate createdDate; // Created Date (using LocalDate)

    // Constructor
    public station(int id, String line, String uniqueStationId, String stationName,
                   double latitude, double longitude, LocalDate createdDate) {
        this.id = id;
        this.line = line;
        this.uniqueStationId = uniqueStationId;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Station{id=" + id + ", line='" + line + "', uniqueStationId='" + uniqueStationId + "', " +
                "stationName='" + stationName + "', latitude=" + latitude + ", longitude=" + longitude + ", " +
                "createdDate=" + createdDate + "}";
    }

    // Getter and Setter methods
    public int getId() { return id; }
    public String getLine() { return line; }
    public String getUniqueStationId() { return uniqueStationId; }
    public String getStationName() { return stationName; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public LocalDate getCreatedDate() { return createdDate; }
}
