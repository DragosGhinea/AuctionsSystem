package me.dragosghinea.rooms.handlers;

import me.dragosghinea.apartments.Apartment;
import me.dragosghinea.rooms.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DuplexRoomHandler implements RoomHandler<List<Room>>{
    private Apartment apartment1;
    private Apartment apartment2;

    public DuplexRoomHandler(Apartment app1, Apartment app2){
        this.apartment1 = app1;
        this.apartment2 = app2;
    }

    @Override
    public List<Room> getRooms() {
        List<Room> toReturn = new ArrayList<>();
        for(Object room : apartment1.getRoomHandler().getRooms())
            toReturn.add((Room)room);
        for(Object room : apartment2.getRoomHandler().getRooms())
            toReturn.add((Room)room);
        return toReturn;
    }

    @Override
    public void addRoom(Room toAdd) {
        try {
            apartment1.getRoomHandler().addRoom((Room) toAdd.clone());
            apartment2.getRoomHandler().addRoom((Room) toAdd.clone());
        }catch(Exception x){
            x.printStackTrace();
        }
    }

    //returns true if the room was removed
    //from both apartments or just one
    @Override
    public Boolean removeRoom(Room toRemove) {
        boolean removed = false;
        if(apartment1.getRoomHandler().removeRoom(toRemove))
            removed = true;
        if(apartment2.getRoomHandler().removeRoom(toRemove))
            removed = true;
        return removed;
    }

    @Override
    public Integer getRoomsNumber() {
        return apartment1.getRoomHandler().getRoomsNumber()+apartment2.getRoomHandler().getRoomsNumber();
    }

    public Apartment getApartment1() {
        return apartment1;
    }

    public Apartment getApartment2() {
        return apartment2;
    }
}
