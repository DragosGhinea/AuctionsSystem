package me.dragosghinea;

import me.dragosghinea.apartments.Apartment;
import me.dragosghinea.apartments.BasicApartment;
import me.dragosghinea.apartments.DuplexApartment;
import me.dragosghinea.apartments.OpenSpaceApartment;
import me.dragosghinea.business.BusinessAction;
import me.dragosghinea.business.Rental;
import me.dragosghinea.business.Sale;
import me.dragosghinea.rooms.Room;
import me.dragosghinea.rooms.RoomType;

import java.math.BigDecimal;

public class Main {


    public static void main(String[] args) {
        Apartment ap1 = new BasicApartment(22.3f);
        ap1.getRoomHandler().addRoom(new Room(4f, RoomType.BEDROOM));
        ap1.getRoomHandler().addRoom(new Room(2f, RoomType.BATHROOM));
        ap1.getRoomHandler().addRoom(new Room(4f, RoomType.LIVINGROOM));
        ap1.addUtility("2 rooms");
        ap1.addUtility("1 bath");

        Apartment ap2 = new BasicApartment(10.2f);
        Room r2 = new Room(4f, RoomType.BEDROOM);
        r2.addUtility("Bed");
        r2.addUtility("Work desk");
        ap2.getRoomHandler().addRoom(r2);
        ap2.getRoomHandler().addRoom(new Room(2f, RoomType.BATHROOM));
        ap2.addUtility("Balcony");

        Apartment ap3 = new OpenSpaceApartment(21.1f);
        ap3.getRoomHandler().addRoom(new Room(4f, RoomType.CUSTOM));
        ap3.getRoomHandler().addRoom(new Room(2f, RoomType.BATHROOM));
        ap3.getRoomHandler().addRoom(new Room(4f, RoomType.BEDROOM));

        Apartment ap4 = new DuplexApartment(new BasicApartment(10f), new BasicApartment(10f));
        ap4.getRoomHandler().addRoom(new Room(4f, RoomType.CUSTOM));
        ap4.getRoomHandler().addRoom(new Room(2f, RoomType.BATHROOM));
        ap4.getRoomHandler().addRoom(new Room(4f, RoomType.BEDROOM));



        BusinessAction ap1B = new Sale(100L, BigDecimal.valueOf(100));
        ap1B.markPurchased();
        ap1.setBusinessAction(ap1B);

        BusinessAction ap2B = new Rental(1000L, 1230L, BigDecimal.valueOf(10));
        ap2B.markPurchased();
        ap2.setBusinessAction(ap2B);

        BusinessAction ap3B = new Rental(10L, 120L, BigDecimal.valueOf(20));
        ap3.setBusinessAction(ap3B);

        BusinessAction ap4B = new Sale(50L, BigDecimal.valueOf(320));
        ap4B.markPurchased();
        ap4.setBusinessAction(ap4B);


        RealEstateAgency.getInstance().addApartment(ap1);
        RealEstateAgency.getInstance().addApartment(ap2);
        RealEstateAgency.getInstance().addApartment(ap3);
        RealEstateAgency.getInstance().addApartment(ap4);

        RealEstateAgency.getInstance().showSortedApartments((apartment1, apartment2) ->
                apartment1.getRoomHandler().getRoomsNumber().compareTo(apartment2.getRoomHandler().getRoomsNumber()));

        System.out.println("---------- NEXT -------------\n");

        RealEstateAgency.getInstance().showAllApartments();

        System.out.println("---------- NEXT -------------\n");

        RealEstateAgency.getInstance().showNumberOfPurchasesTop();

        System.out.println("---------- NEXT -------------\n");

        final Integer numberOfRoomsToSearchFor = 3;
        RealEstateAgency.getInstance().showAllApartments(ap -> ap.getRoomHandler().getRoomsNumber().equals(numberOfRoomsToSearchFor));
    }
}
