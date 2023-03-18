package me.dragosghinea.rooms.handlers;

import me.dragosghinea.rooms.Room;

public interface RoomHandler<T extends Iterable<? extends Room>> {

    T getRooms();

    void addRoom(Room toAdd);

    Boolean removeRoom(Room toRemove);

    Integer getRoomsNumber();
}
