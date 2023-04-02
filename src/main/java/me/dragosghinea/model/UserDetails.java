package me.dragosghinea.model;

import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class UserDetails implements Cloneable{
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

    void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        }catch(Exception x){
            x.printStackTrace(); //should never happen
            return null;
        }
    }
}
