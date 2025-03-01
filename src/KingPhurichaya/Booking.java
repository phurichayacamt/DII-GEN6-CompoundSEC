package src.KingPhurichaya;

public class Booking {
    private final String timestamp;
    private final String roomNumber;
    private final String status;

    public Booking(String timestamp, String roomNumber, String status) {
        this.timestamp = timestamp;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getStatus() {
        return status;
    }
}
