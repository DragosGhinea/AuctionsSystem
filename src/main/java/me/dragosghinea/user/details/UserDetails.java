package me.dragosghinea.user.details;

public interface UserDetails extends Cloneable {

    String getEmail();
    String getUsername();
    Long getBirthDateEpoch();
    String getFirstName();
    String getLastName();

    Boolean checkPassword(String password);

    Object clone();
}
