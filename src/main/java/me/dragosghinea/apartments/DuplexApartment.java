package me.dragosghinea.apartments;

import me.dragosghinea.rooms.handlers.DuplexRoomHandler;

import java.util.List;
import java.util.stream.Stream;

public class DuplexApartment extends Apartment{
    private Apartment apartment1;
    private Apartment apartment2;

    private List<String> commonUtilities;

    public DuplexApartment(Apartment app1, Apartment app2){
        super();
        this.apartment1 = app1;
        this.apartment2 = app2;
        setRoomHandler(new DuplexRoomHandler(app1, app2));
        commonUtilities = Stream.concat(apartment1.getApartmentUtilities().stream(), apartment2.getApartmentUtilities().stream()).toList();
    }

    @Override
    public List<String> getApartmentUtilities() {
        return commonUtilities;
    }

    public Apartment getApartment1() {
        return apartment1;
    }

    public Apartment getApartment2() {
        return apartment2;
    }

    @Override
    public Float getSize() {
        return apartment1.getSize()+apartment2.getSize();
    }

    @Override
    public Boolean addUtility(String utility) {
        commonUtilities.add(utility);
        return true;
    }

    @Override
    public Boolean removeUtility(String utility) {
        return commonUtilities.remove(utility);
    }

    @Override
    public String toString() {
        return "===== Duplex =====\n"+super.toString();
    }
}
