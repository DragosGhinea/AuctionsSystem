package me.dragosghinea.repository;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.model.*;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.repository.impl.postgres.AuctionRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.RewardRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.UserRepositoryImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("Database integrity tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseIntegrityTest {
    //generating objects
    static SingleReward singleReward = SingleReward.builder()
            .rewardInfo("1kg Gold")
            .rewardName("Gold Package")
            .rewardDescription("Good for the economy we are in")
            .build();
    static MultiReward multiReward = MultiReward.builder()
            .rewardInfo(List.of("Mona Lisa", "The Last Supper", "The Creation of Adam", "David"))
            .rewardName("Famous Art")
            .rewardDescription("Famous art pieces for delicate tastes")
            .build();

    static BundleReward bundleReward = BundleReward.builder()
            .rewards(List.of(
                    singleReward,
                    multiReward
            ))
            .rewardName("A bundle of rewards")
            .rewardDescription("A bundle of rewards for the lucky winner")
            .build();

    static BlitzAuction blitzAuction;
    static LongAuction longAuction;
    static {
        LocalDateTime now = LocalDateTime.now();
        blitzAuction = new BlitzAuction().toBuilder()
                .startDate(now)
                .preparingDuration(Duration.ofMinutes(1))
                .bidDuration(Duration.ofSeconds(20))
                .endDate(now.plusMinutes(1).plusSeconds(20))
                .startingBidAmount(BigDecimal.valueOf(10))
                .minimumBidGap(BigDecimal.valueOf(3))
                .reward(bundleReward)
                .build();

        longAuction = new LongAuction().toBuilder()
                .startDate(now.minusMinutes(1))
                .endDate(now.plusMinutes(10))
                .overTime(now.plusMinutes(10))
                .extendTime(Duration.ofMinutes(5))
                .startingBidAmount(BigDecimal.valueOf(1000))
                .minimumBidGap(BigDecimal.valueOf(200))
                .reward(multiReward)
                .build();
    }
    static User user;
    static {
        UserDetails details = new UserDetails(
                null,
                "maria.an@yahoo.com",
                "_mary",
                LocalDate.of(2000, 5, 7),
                "Maria",
                "Anghel-Diaconu",
                null
        );
        user = new User(details);
        user.getUserDetails().setPassword("User5!");
    }


    @BeforeAll
    static void databaseCleanup(){
        try {
            DatabaseConnection.resetAllData();
            DatabaseConnection.generateTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Test foreign keys")
    class ForeignKeyTests{
        private final RewardRepository<Reward> rewardRepository = new RewardRepositoryImpl();

        static Stream<Reward> getRewards(){
            return Stream.of(
                    multiReward,
                    bundleReward
            );
        }

        @ParameterizedTest(name = "[{index}] rewardToTryRemove=''{0}''")
        @MethodSource("getRewards")
        @DisplayName("Test Auction Depending on Reward")
        void testAuctionDependingOnReward(Reward rewardToTryRemove){
            Assertions.assertThrows(SQLException.class, () -> rewardRepository.removeReward(rewardToTryRemove.getRewardId()));
        }
    }

    @Nested
    @Order(1)
    @DisplayName("Test primary keys")
    class PrimaryKeyTests{
        static Stream<Reward> getRewards(){
            return Stream.of(
                    singleReward,
                    multiReward,
                    bundleReward
            );
        }

        static Stream<Auction> getAuctions(){
            return Stream.of(
                    blitzAuction,
                    longAuction
            );
        }

        static Stream<User> getUsers(){
            return Stream.of(
                    user
            );
        }

        private final RewardRepository<Reward> rewardRepository = new RewardRepositoryImpl();
        private final AuctionRepository<Auction> auctionRepository = new AuctionRepositoryImpl();

        private final UserRepository userRepository = new UserRepositoryImpl();

        @ParameterizedTest(name = "[{index}] rewardToAdd=''{0}''")
        @MethodSource("getRewards")
        @DisplayName("Test Reward Table")
        void testRewardTable(Reward rewardToAdd){
            Assertions.assertTrue(() -> {
                try {
                    return rewardRepository.addReward(rewardToAdd);
                } catch (SQLException e) {
                    return false;
                }
            });
            Assertions.assertThrows(SQLException.class, () -> rewardRepository.addReward(rewardToAdd));
        }

        @ParameterizedTest(name = "[{index}] auctionToAdd=''{0}''")
        @MethodSource("getAuctions")
        @DisplayName("Test Auction Table")
        void testAuctionTable(Auction auctionToAdd){
            Assertions.assertTrue(() -> {
                try {
                    return auctionRepository.addAuction(auctionToAdd);
                } catch (SQLException e) {
                    return false;
                }
            });
            Assertions.assertThrows(SQLException.class, () -> auctionRepository.addAuction(auctionToAdd));
        }

        @ParameterizedTest(name = "[{index}] userToAdd=''{0}''")
        @MethodSource("getUsers")
        @DisplayName("Test User Table")
        void testUserTable(User userToAdd){
            Assertions.assertTrue(() -> {
                try {
                    return userRepository.addUser(userToAdd);
                } catch (SQLException e) {
                    return false;
                }
            });
            Assertions.assertThrows(SQLException.class, () -> userRepository.addUser(userToAdd));
        }
    }

    /* Cleanup method
    @AfterAll
    static void databaseCleanupAfterAll(){
        try {
            DatabaseConnection.resetAllData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     */

}
