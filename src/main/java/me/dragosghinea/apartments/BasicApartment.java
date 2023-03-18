package me.dragosghinea.apartments;

import me.dragosghinea.rooms.handlers.BasicRoomHandler;

import java.util.ArrayList;
import java.util.List;

public class BasicApartment extends Apartment{
    List<String> utilities = new ArrayList<>();

    @Override
    public List<String> getApartmentUtilities() {
        return utilities;
    }

    public BasicApartment(Float size){
        super(size);
        setRoomHandler(new BasicRoomHandler());
    }

    @Override
    public Boolean addUtility(String utility) {
        utilities.add(utility);
        return true;
    }

    @Override
    public Boolean removeUtility(String utility) {
        return utilities.remove(utility);
    }

    @Override
    public String toString() {
        return "===== Apartment =====\n"+super.toString();
    }
}
