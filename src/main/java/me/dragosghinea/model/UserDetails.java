package me.dragosghinea.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class UserDetails {
    private UUID userId;
    @Setter
    private String email;
    @Setter
    private String username;
    @Setter
    private LocalDate birthDate;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    private String passwordHash;

    public void setPassword(String password){
        String salt = BCrypt.gensalt(12);
        this.passwordHash = BCrypt.hashpw(password, salt);
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, passwordHash);
    }
}
