package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;

import java.util.*;

public class HotelManagementRepository {

    HashMap<String, Hotel> hotelmap = new HashMap<>();
    HashMap<String, User> userMap = new HashMap<>();
    HashMap<String, Booking> bookingMap = new HashMap<>();
    public String addHotel(Hotel hotel) {
        if(hotelmap.containsKey(hotel.getHotelName())) {
            return "FAILURE";
        }
        hotelmap.put(hotel.getHotelName(), hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        userMap.put(user.getName(), user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        if(hotelmap.isEmpty()) {
            return "";
        }
        int maxFac = 0;
        String hotelName = "";
        for(String hotel : hotelmap.keySet()) {
            if(hotelmap.get(hotel).getFacilities().size() > maxFac) {
                maxFac = hotelmap.get(hotel).getFacilities().size();
                hotelName = hotel;
            } else if(hotelmap.get(hotel).getFacilities().size() == maxFac) {
                List<String> hotelsList = new ArrayList<>();
                hotelsList.add(hotelName);
                hotelsList.add(hotel);
                Collections.sort(hotelsList);
                hotelName = hotelsList.get(0);
            }
        }
        if(maxFac == 0) {
            return "";
        } else {
            return hotelName;
        }
    }

    public int bookARoom(Booking booking) {
        if(!hotelmap.containsKey(booking.getHotelName())) {
            return -1;
        }
        String UUID = String.valueOf(java.util.UUID.randomUUID());
        booking.setBookingId(UUID);
        Hotel hotel = hotelmap.get(booking.getHotelName());
        int price = hotel.getPricePerNight();
        int availableRooms = hotel.getAvailableRooms();
        int requiredRooms = booking.getNoOfRooms();
        if(availableRooms < requiredRooms) {
            return -1;
        }
        availableRooms -= requiredRooms;
        hotel.setAvailableRooms(availableRooms);
        int totalPrice = price * requiredRooms;
        booking.setAmountToBePaid(totalPrice);
        bookingMap.put(UUID, booking);
        return totalPrice;

    }

    public int getBookings(Integer aadharCard) {
        int bookings = 0;
        for(String bid : bookingMap.keySet()) {
            if(bookingMap.get(bid).getBookingAadharCard() == aadharCard) {
                bookings++;
            }
        }
        return bookings;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        if(!hotelmap.containsKey(hotelName)) {
            return null;
        }
        Hotel hotel = hotelmap.get(hotelName);
        HashSet<Facility> set = new HashSet<>();
        set.addAll(hotel.getFacilities());
        set.addAll(newFacilities);
        List<Facility> allFacilities = new ArrayList<>(set);
        hotel.setFacilities(allFacilities);
        return hotel;
    }
}
