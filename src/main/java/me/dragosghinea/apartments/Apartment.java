package me.dragosghinea.apartments;

import me.dragosghinea.business.BusinessAction;
import me.dragosghinea.rooms.Room;
import me.dragosghinea.rooms.handlers.RoomHandler;

import java.math.BigDecimal;
import java.util.List;

public abstract class Apartment {

    private BusinessAction businessAction;
    private RoomHandler roomHandler;
    private static Integer IDSequenceGenerator = 0;
    private final Integer ID;
    private Float size;
    {
        ID = IDSequenceGenerator;
        IDSequenceGenerator++;
    }

    public RoomHandler getRoomHandler() {
        return roomHandler;
    }

    public BusinessAction getBusinessAction() {
        return businessAction;
    }

    public void setBusinessAction(BusinessAction businessAction) {
        this.businessAction = businessAction;
    }

    public void setRoomHandler(RoomHandler roomHandler) {
        this.roomHandler = roomHandler;
    }

    public Apartment(){

    }

    public Apartment(Float size){
        this.size = size;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Iterable<Room> getRooms(){
        return getRoomHandler().getRooms();
    }

    public Integer getID() {
        return ID;
    }

    public BigDecimal getPrice(){
        return getBusinessAction().getPrice();
    }

    public abstract List<String> getApartmentUtilities();

    public abstract Boolean addUtility(String utility);

    public abstract Boolean removeUtility(String utility);

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder()
                .append("> Apartment Size: ").append(getSize()).append("\n")
                .append("> Price: ").append(getPrice().toString()).append("\n")
                .append("> Purchased: ").append(getBusinessAction().isPurchased() ? "Yes\n" : "No\n")
                .append("> Total Rooms: ").append(roomHandler.getRoomsNumber()).append("\n");

        if(!getApartmentUtilities().isEmpty()){
            s.append("> Utilities:\n");
            for(String utility : getApartmentUtilities())
                s.append(" - ").append(utility).append("\n");
        }

        if(roomHandler.getRoomsNumber()>0){
            s.append("> Rooms:\n");
            for(Object room : roomHandler.getRooms())
                s.append(room.toString());
        }

        return s.toString();
    }
}
