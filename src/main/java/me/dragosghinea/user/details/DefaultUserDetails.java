package me.dragosghinea.user.details;

import org.mindrot.jbcrypt.BCrypt;

public class DefaultUserDetails implements UserDetails{
    private String email;
    private String username;
    private Long birthDateEpoch;
    private String firstName;
    private String lastName;
    private String passwordHash;

    public DefaultUserDetails(){

    }

    public DefaultUserDetails(String email, String username, Long birthDateEpoch, String firstName, String lastName){
        this.email = email;
        this.username = username;
        this.birthDateEpoch = birthDateEpoch;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private static final int WORKLOAD = 12;
    public void setPassword(String password) {
        String salt = BCrypt.gensalt(WORKLOAD);
        this.passwordHash = BCrypt.hashpw(password, salt);
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Long getBirthDateEpoch() {
        return birthDateEpoch;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBirthDateEpoch(Long birthDateEpoch) {
        this.birthDateEpoch = birthDateEpoch;
    }

    //no password set doesn't mean every password is allowed
    //since you do need a password
    //therefore it will deny every password if not set
    public Boolean checkPassword(String password) {
        if(password == null)
            return Boolean.FALSE;
        return BCrypt.checkpw(password, passwordHash);
    }

}
