package me.dragosghinea.user.details.builder;

import me.dragosghinea.user.details.DefaultUserDetails;
import me.dragosghinea.user.details.UserDetails;

public class DefaultUserDetailsBuilder implements UserDetailsAbstractBuilder{
    private DefaultUserDetails userDetails;

    public DefaultUserDetailsBuilder(){
        userDetails = new DefaultUserDetails();
    }

    public DefaultUserDetailsBuilder(DefaultUserDetails userDetails){
        try {
            this.userDetails = (DefaultUserDetails) userDetails.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            this.userDetails = new DefaultUserDetails();
        }
    }

    public UserDetailsAbstractBuilder setEmail(String email) {
        userDetails.setEmail(email);
        return this;
    }

    public UserDetailsAbstractBuilder setUsername(String username) {
        userDetails.setUsername(username);
        return this;
    }

    public UserDetailsAbstractBuilder setBirthDateEpoch(Long epoch) {
        userDetails.setBirthDateEpoch(epoch);
        return this;
    }

    public UserDetailsAbstractBuilder setFirstName(String firstName) {
        userDetails.setFirstName(firstName);
        return this;
    }

    public UserDetailsAbstractBuilder setLastName(String lastName) {
        userDetails.setLastName(lastName);
        return this;
    }

    public UserDetailsAbstractBuilder setPassword(String password) {
        userDetails.setPassword(password);
        return this;
    }

    public UserDetails build() {
        return userDetails;
    }
}
