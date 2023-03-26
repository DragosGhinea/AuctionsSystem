package me.dragosghinea.user.details.builder;

import me.dragosghinea.user.details.UserDetails;

public interface UserDetailsAbstractBuilder {

    UserDetailsAbstractBuilder setEmail(String email);

    UserDetailsAbstractBuilder setUsername(String username);

    UserDetailsAbstractBuilder setBirthDateEpoch(Long epoch);

    UserDetailsAbstractBuilder setFirstName(String firstName);

    UserDetailsAbstractBuilder setLastName(String lastName);

    UserDetailsAbstractBuilder setPassword(String password);

    UserDetailsAbstractBuilder setPasswordHash(String passwordHash);

    UserDetails build();
}
