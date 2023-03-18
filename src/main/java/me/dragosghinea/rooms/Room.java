package me.dragosghinea.rooms;

import java.util.ArrayList;
import java.util.List;

public class Room implements Cloneable{

    RoomType roomType;
    Float size;
    List<String> utilities = new ArrayList<>();

    public Room(Float size){
        this.size = size;
        this.roomType = RoomType.CUSTOM;
    }

    public Room(Float size, RoomType roomType){
        this.size = size;
        this.roomType = roomType;
    }

    public List<String> getRoomUtilities() {
        return utilities;
    }

    public void addUtility(String utility) {
        utilities.add(utility);
    }

    public Boolean removeUtility(String utility) {
        return utilities.remove(utility);
    }

    public Float getRoomSize() {
        return size;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Room clone = (Room)super.clone();
        clone.utilities = new ArrayList<>(utilities);
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder()
                .append("------ ").append(roomType.toString()).append(" ------\n")
                .append("> Size: ").append(size.toString()).append("\n");

        if(!utilities.isEmpty()){
            s.append("> Utilities:\n");
            for(String utility : utilities)
                s.append(" - ").append(utility).append("\n");
        }

        return s.toString();
    }
}
