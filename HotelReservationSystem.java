#task 2- HotelReservationSystem

import java.io.*;
import java.util.*;

// =====================================
// ROOM CLASS
// =====================================
class Room {
    String roomId;
    String category;
    double price;
    boolean available;

    public Room(String roomId, String category, double price, boolean available) {
        this.roomId = roomId;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    @Override
    public String toString() {
        return roomId + "," + category + "," + price + "," + available;
    }

    public static Room fromString(String line) {
        String[] p = line.split(",");
        return new Room(p[0], p[1], Double.parseDouble(p[2]), Boolean.parseBoolean(p[3]));
    }
}


// =====================================
// RESERVATION CLASS
// =====================================
class Reservation {
    String reservationId;
    String roomId;
    String customer;
    String checkIn;
    String checkOut;
    double price;

    public Reservation(String reservationId, String roomId, String customer,
                       String checkIn, String checkOut, double price) {
        this.reservationId = reservationId;
        this.roomId = roomId;
        this.customer = customer;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.price = price;
    }

    @Override
    public String toString() {
        return reservationId + "," + roomId + "," + customer + "," + checkIn + "," + checkOut + "," + price;
    }

    public static Reservation fromString(String line) {
        String[] p = line.split(",");
        return new Reservation(p[0], p[1], p[2], p[3], p[4], Double.parseDouble(p[5]));
    }
}


// =====================================
// HOTEL SYSTEM CLASS
// =====================================
class HotelSystem {
    private HashMap<String, Room> rooms = new HashMap<>();
    private HashMap<String, Reservation> reservations = new HashMap<>();

    private final String ROOMS_FILE = "rooms.txt";
    private final String RESERVATIONS_FILE = "reservations.txt";

    public HotelSystem() {
        loadRooms();
        loadReservations();
    }

    // ---------------------- FILE I/O ----------------------

    private void loadRooms() {
        File f = new File(ROOMS_FILE);
        if (!f.exists()) {
            // Default rooms
            rooms.put("101", new Room("101", "Standard", 80, true));
            rooms.put("102", new Room("102", "Standard", 90, true));
            rooms.put("201", new Room("201", "Deluxe", 120, true));
            rooms.put("202", new Room("202", "Deluxe", 140, true));
            rooms.put("301", new Room("301", "Suite", 200, true));
            saveRooms();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ROOMS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Room r = Room.fromString(line);
                rooms.put(r.roomId, r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveRooms() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOMS_FILE))) {
            for (Room r : rooms.values()) {
                pw.println(r.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadReservations() {
        File f = new File(RESERVATIONS_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(RESERVATIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Reservation res = Reservation.fromString(line);
                reservations.put(res.reservationId, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveReservations() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation r : reservations.values()) {
                pw.println(r.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------- SEARCH ----------------------

    public void searchRooms(String category) {
        System.out.println("\n== Available Rooms ==");
        for (Room r : rooms.values()) {
            if (r.available && (category == null || r.category.equalsIgnoreCase(category))) {
                System.out.println("Room " + r.roomId + " | " + r.category + " | $" + r.price);
            }
        }
    }

    // ---------------------- PAYMENT SIMULATION ----------------------

    private boolean processPayment(String customer, double amount) {
        System.out.println("\nProcessing payment for " + customer + "...");
        System.out.println("Charge: $" + amount);
        System.out.println("Payment successful!");
        return true;
    }

    // ---------------------- BOOKING ----------------------

    public void bookRoom(String roomId, String customer, String checkIn, String checkOut) {
        Room r = rooms.get(roomId);

        if (r == null) {
            System.out.println("Room not found.");
            return;
        }

        if (!r.available) {
            System.out.println("Room already booked.");
            return;
        }

        processPayment(customer, r.price);

        String resId = UUID.randomUUID().toString();
        Reservation res = new Reservation(resId, roomId, customer, checkIn, checkOut, r.price);

        reservations.put(resId, res);
        r.available = false;

        saveRooms();
        saveReservations();

        System.out.println("Booking successful! Reservation ID: " + resId);
    }

    // ---------------------- CANCEL RESERVATION ----------------------

    public void cancelReservation(String resId) {
        Reservation res = reservations.get(resId);

        if (res == null) {
            System.out.println("Reservation not found.");
            return;
        }

        Room r = rooms.get(res.roomId);
        r.available = true;

        reservations.remove(resId);

        saveRooms();
        saveReservations();

        System.out.println("Reservation canceled successfully.");
    }

    // ---------------------- VIEW RESERVATION ----------------------

    public void viewReservation(String resId) {
        Reservation r = reservations.get(resId);

        if (r == null) {
            System.out.println("Reservation not found.");
            return;
        }

        System.out.println("\n== Reservation Details ==");
        System.out.println("Reservation ID: " + r.reservationId);
        System.out.println("Room: " + r.roomId);
        System.out.println("Customer: " + r.customer);
        System.out.println("Check-in: " + r.checkIn);
        System.out.println("Check-out: " + r.checkOut);
        System.out.println("Price: $" + r.price);
    }
}


// =====================================
// MAIN PROGRAM
// =====================================
public class HotelReservationSystem {
    public static void main(String[] args) {

        HotelSystem hotel = new HotelSystem();

        hotel.searchRooms(null);          // show all available rooms
        hotel.bookRoom("201", "Alice", "2025-06-01", "2025-06-05");

        // Copy the printed reservation ID here to test:
        // hotel.viewReservation("paste-id-here");
        // hotel.cancelReservation("paste-id-here");
    }
}
