package me.dragosghinea.application;

import me.dragosghinea.model.*;
import me.dragosghinea.model.abstracts.Auction;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DefaultEntitiesGenerator {

    private static User buildUser(String email, LocalDate birthDate, String firstName, String lastName, String username, String password) {
        UserDetails details;
        User user;

        details = new UserDetails(null, email, username, birthDate, firstName, lastName, null);

        user = new User(details);
        user.getUserDetails().setPassword(password);

        return user;
    }

    public static List<User> getDefaultUsers() {
        return List.of(
                buildUser("monica.g@yahoo.com", LocalDate.of(2002, 11, 20), "Monica", "Gali", "moni12", "User1!"),
                buildUser("alex.a@gmail.com", LocalDate.of(1999, 4, 11), "Alex", "Pacig", "alex.a", "User2!"),
                buildUser("mihai.duncea@gmail.com", LocalDate.of(2001, 1, 25), "Mihai-Bogdan", "Duncea", "mihaita", "User3!"),
                buildUser("melis@gmail.com", LocalDate.of(2002, 12, 4), "Melissa", "Ionescu", "mel_", "User4!"),
                buildUser("maria.an@yahoo.com", LocalDate.of(2000, 5, 7), "Maria", "Anghel-Diaconu", "_mary", "User5!")
        );
    }


    public static List<Auction> getDefaultAuctions() {
        LocalDateTime now = LocalDateTime.now();
        return List.of(
            new LongAuction().toBuilder()
                    .startDate(now.plusMinutes(2))
                    .endDate(now.plusMinutes(5))
                    .overTime(now.plusMinutes(5))
                    .extendTime(Duration.ofMinutes(2))
                    .startingBidAmount(BigDecimal.valueOf(100))
                    .minimumBidGap(BigDecimal.valueOf(20))
                    .reward(
                            SingleReward.builder()
                                    .rewardInfo("1kg Gold")
                                    .rewardName("Gold Package")
                                    .rewardDescription("Good for the economy we are in")
                                    .build()
                    )
                    .build(),
            new LongAuction().toBuilder()
                    .startDate(now.minusMinutes(1))
                    .endDate(now.plusMinutes(10))
                    .overTime(now.plusMinutes(10))
                    .extendTime(Duration.ofMinutes(5))
                    .startingBidAmount(BigDecimal.valueOf(1000))
                    .minimumBidGap(BigDecimal.valueOf(200))
                    .reward(
                            MultiReward.builder()
                                    .rewardInfo(List.of("Mona Lisa", "The Last Supper", "The Creation of Adam", "David"))
                                    .rewardName("Famous Art")
                                    .rewardDescription("Famous art pieces for delicate tastes")
                                    .build()
                    )
                    .build(),
            new BlitzAuction().toBuilder()
                    .startDate(now)
                    .preparingDuration(Duration.ofMinutes(1))
                    .bidDuration(Duration.ofSeconds(20))
                    .endDate(now.plusMinutes(1).plusSeconds(20))
                    .startingBidAmount(BigDecimal.valueOf(10))
                    .minimumBidGap(BigDecimal.valueOf(3))
                    .reward(
                            BundleReward.builder()
                                    .rewards(List.of(
                                            SingleReward.builder()
                                                    .rewardInfo("2kg Chocolate")
                                                    .rewardName("Huge Chocolate")
                                                    .rewardDescription("A huge chocolate bar")
                                                    .build(),
                                            MultiReward.builder()
                                                    .rewardInfo(List.of("Orange Jellies", "Blueberry Jellies", "Strawberry Jellies"))
                                                    .rewardName("Jelly Mix")
                                                    .rewardDescription("Different flavour jellies")
                                                    .build()
                                    ))
                                    .rewardName("Sweets!")
                                    .rewardDescription("All kind of sweets you might want")
                                    .build()
                    )
                    .build()
        );
    }
}
