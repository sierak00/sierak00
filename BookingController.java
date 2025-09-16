package controller;

import model.Booking;
import model.Vehicle;
import model.Client;
import model.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookingController {
    private static final String BOOKING_FILE = "data/bookings.ser";
    private ArrayList<Booking> bookingRecords;

    public BookingController() {
        bookingRecords = loadBookings();
    }

    // Ngarkon rezervimet nga skedari ose krijon nj� list� t� re n�se ndodh nj� gabim
    private ArrayList<Booking> loadBookings() {
        try {
            return Handler.loadData(BOOKING_FILE);
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Failed to retrieve booking records: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    // Regjistron nj� rezervim t� ri n�se makina �sht� e disponueshme
    public void addBooking(Client client, Vehicle car, int duration) {
        if (!car.isAvailable()) {
            System.out.println("Selected car is currently not available.");
            return;
        }
        Booking newBooking = new Booking(client.getName(), car, duration);
        bookingRecords.add(newBooking);
        car.setAvailable(false);
        saveBookings();
        System.out.println("Booking has been successfully created.");
    }

    // Merr list�n e t� gjitha rezervimeve
    public List<Booking> fetchAllBookings() {
        return new ArrayList<>(bookingRecords);
    }

    // P�rdit�son nj� rezervim ekzistues
    public void editBooking(Booking booking, int newDuration) {
        if (!bookingRecords.contains(booking)) {
            System.out.println("Booking does not exist.");
            return;
        }
        booking.setRentalDays(newDuration);
        booking.setTotalCost(booking.getCar().getDailyRate() * newDuration * 1.20);
        saveBookings();
        System.out.println("Booking details successfully updated.");
    }

    // Llogarit koston e p�rgjithshme t� rezervimit
    public double getTotalCost(Vehicle car, int rentalPeriod) {
        return car.getDailyRate() * rentalPeriod;
    }

    // Fshin nj� rezervim ekzistues
    public void removeBooking(Booking booking) {
        if (!bookingRecords.contains(booking)) {
            System.out.println("Booking not found.");
            return;
        }
        bookingRecords.remove(booking);
        booking.getCar().setAvailable(true);
        saveBookings();
        System.out.println("Booking has been successfully canceled.");
    }

    // Ruajtja e rezervimeve n� skedar
    private void saveBookings() {
        try {
            Handler.saveData(BOOKING_FILE, bookingRecords);
        } catch (IOException ex) {
            System.err.println("Error while storing booking data: " + ex.getMessage());
        }
    }

	public void cancelBooking(Booking selectedBooking) {
		// TODO Auto-generated method stub
		
	}
}
