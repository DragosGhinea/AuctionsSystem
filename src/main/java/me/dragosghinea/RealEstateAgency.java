package me.dragosghinea;

import me.dragosghinea.apartments.Apartment;
import me.dragosghinea.apartments.BasicApartment;
import me.dragosghinea.apartments.DuplexApartment;
import me.dragosghinea.apartments.OpenSpaceApartment;
import me.dragosghinea.rooms.RoomType;

import java.util.*;
import java.util.function.Predicate;

public class RealEstateAgency {

    private static RealEstateAgency instance = new RealEstateAgency();

    private Map<Integer, Apartment> map = new HashMap<>();

    private RealEstateAgency(){

    }

    public Optional<Apartment> getApartment(Integer id){
        return Optional.ofNullable(map.getOrDefault(id, null));
    }

    public Map<Integer, Apartment> getApartments() {
        return map;
    }

    public void showAllApartments(){
        for(Apartment ap : getApartments().values())
            System.out.println(ap.toString());
    }

    public void showAllApartments(Predicate<Apartment> toTest){
        for(Apartment ap : getApartments().values())
            if(toTest.test(ap))
                System.out.println(ap.toString());
    }

    public void showSortedApartments(Comparator<? super Apartment> comparator){
        RealEstateAgency.getInstance().getApartments().values().stream()
                .sorted(comparator)
                .forEach(ap -> System.out.println(ap.toString()));
    }

    public void showNumberOfPurchasesTop(){
        System.out.println("Most sold types:");
        HashMap<String, Integer> purchases = new HashMap<>();
        for(Apartment ap : map.values()){
            if(ap instanceof BasicApartment && ap.getBusinessAction().isPurchased())
                purchases.compute("Basic", (k,v) -> v == null ? 1 : v+1);
            else if(ap instanceof OpenSpaceApartment && ap.getBusinessAction().isPurchased())
                purchases.compute("OpenSpace", (k,v) -> v == null ? 1 : v+1);
            else if(ap instanceof DuplexApartment && ap.getBusinessAction().isPurchased())
                purchases.compute("Duplex", (k,v) -> v == null ? 1 : v+1);
            else if(ap.getBusinessAction().isPurchased()){
                purchases.compute("Custom", (k,v) -> v == null ? 1 : v+1);
            }
        }

        purchases.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(5)
                .forEach(entry -> System.out.println(entry.getKey()+": "+entry.getValue()));
        System.out.println("");
    }

    //if the id does not have an apartment associated, it gets
    //the parameter apartment, otherwise, it keeps the old one
    public Boolean addApartment(Apartment ap){
        return map.merge(ap.getID(), ap, (old, newA) -> old) == ap;
    }

    public static RealEstateAgency getInstance() {
        return instance;
    }
}
