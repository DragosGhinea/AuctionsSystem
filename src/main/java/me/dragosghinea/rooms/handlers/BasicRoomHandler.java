package me.dragosghinea.rooms.handlers;

import me.dragosghinea.rooms.Room;

import java.util.ArrayList;
import java.util.List;

public class BasicRoomHandler implements RoomHandler<List<Room>>{
    private List<Room> rooms = new ArrayList<>();

    public BasicRoomHandler(){

    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public void addRoom(Room toAdd) {
        rooms.add(toAdd);
    }

    @Override
    public Boolean removeRoom(Room toRemove) {
        return rooms.remove(toRemove);
    }

    @Override
    public Integer getRoomsNumber() {
        return rooms.size();
    }
}
