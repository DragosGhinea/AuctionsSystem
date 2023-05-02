package me.dragosghinea.config.databases.postgres;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.config.databases.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfigPostgre implements DatabaseConfig {
    private static DatabaseConfigPostgre instance;

    public static DatabaseConfigPostgre getInstance() {
        if(instance == null){
            instance = new DatabaseConfigPostgre();
        }
        return instance;
    }

    private final HikariDataSource ds;

    private DatabaseConfigPostgre(){
        HikariConfig config = new HikariConfig();

        config.setMaxLifetime(60000); // 60 Sec
        config.setMinimumIdle(10);
        config.setIdleTimeout(45000); // 45 Sec
        config.setMaximumPoolSize(50);

        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("serverName", "127.0.0.1");
        config.addDataSourceProperty("portNumber", "5432");
        config.addDataSourceProperty("databaseName", "auctions");
        config.addDataSourceProperty("user", "dev");
        config.addDataSourceProperty("password", "dev123");

        ds = new HikariDataSource(config);
    }


    @Override
    public HikariDataSource getDataSource() {
        return ds;
    }

    @Override
    public void resetAllData() throws SQLException {
        String usersSQL = "DROP TABLE IF EXISTS UserDetails";
        String userAuctionsSQL = "DROP TABLE IF EXISTS UserAuctions";
        String walletSQL = "DROP TABLE IF EXISTS Wallet";
        String blitzAuctionsSQL = "DROP TABLE IF EXISTS BlitzAuction";
        String longAuctionsSQL = "DROP TABLE IF EXISTS LongAuction";
        String auctionsSQL = "DROP TABLE IF EXISTS Auction";
        String bidsSQL = "DROP TABLE IF EXISTS Bid";
        String singleRewardsSQL = "DROP TABLE IF EXISTS SingleReward";
        String multiRewardsSQL = "DROP TABLE IF EXISTS MultiReward";
        String bundleRewardsSQL = "DROP TABLE IF EXISTS BundleReward";
        String rewardsSql = "DROP TABLE IF EXISTS Reward";


        try(
                Connection conn = DatabaseConnection.getConnection();
                Statement delete = conn.createStatement()
        ){
            delete.addBatch(userAuctionsSQL);
            delete.addBatch(walletSQL);
            delete.addBatch(blitzAuctionsSQL);
            delete.addBatch(longAuctionsSQL);
            delete.addBatch(bidsSQL);
            delete.addBatch(auctionsSQL);
            delete.addBatch(singleRewardsSQL);
            delete.addBatch(multiRewardsSQL);
            delete.addBatch(bundleRewardsSQL);
            delete.addBatch(rewardsSql);
            delete.addBatch(usersSQL);

            delete.executeBatch();
        }
    }

    @Override
    public void generateTables() throws SQLException {
        String usersSQL = "CREATE TABLE IF NOT EXISTS UserDetails (" +
                "    user_id UUID PRIMARY KEY," +
                "    email TEXT UNIQUE NOT NULL," +
                "    username TEXT UNIQUE NOT NULL," +
                "    birth_date DATE NOT NULL," +
                "    first_name TEXT NOT NULL," +
                "    last_name TEXT NOT NULL," +
                "    password_hash TEXT NOT NULL," +
                "    CONSTRAINT userdetails_unique_fullname UNIQUE (first_name, last_name)" +
                ")";

        String walletSQL = "CREATE TABLE IF NOT EXISTS Wallet (" +
                "    user_id UUID PRIMARY KEY REFERENCES UserDetails(user_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "    preferred_currency VARCHAR(40)," +
                "    points NUMERIC(15, 3) NOT NULL DEFAULT 0" +
                ")";

        String rewardSQL = "CREATE TABLE IF NOT EXISTS Reward (" +
                "    reward_id UUID PRIMARY KEY," +
                "    reward_name VARCHAR(255) NOT NULL," +
                "    reward_description TEXT NOT NULL," +
                "    reward_type VARCHAR(20)" +
                ")";

        String singleRewardSQL = "CREATE TABLE IF NOT EXISTS SingleReward(" +
                "    reward_id UUID PRIMARY KEY REFERENCES Reward(reward_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "    reward_info TEXT NOT NULL" +
                ")";

        String multiRewardSQL = "CREATE TABLE IF NOT EXISTS MultiReward(" +
                "    reward_id UUID REFERENCES Reward(reward_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "    reward_info TEXT NOT NULL," +
                "    PRIMARY KEY (reward_id, reward_info)" +
                ")";

        String bundleRewardSQL = "CREATE TABLE IF NOT EXISTS BundleReward(" +
                "    reward_id UUID REFERENCES Reward(reward_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "    included_reward_id UUID REFERENCES Reward(reward_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "    PRIMARY KEY (reward_id, included_reward_id)," +
                "    CHECK (reward_id <> included_reward_id)" +
                ")";

        String auctionSQL = "CREATE TABLE IF NOT EXISTS Auction(" +
                "    auction_id UUID PRIMARY KEY," +
                "    start_date TIMESTAMP NOT NULL," +
                "    end_date TIMESTAMP NOT NULL," +
                "    auction_state VARCHAR(40) NOT NULL," +
                "    starting_bid_amount NUMERIC(15, 3) NOT NULL," +
                "    minimum_bid_gap NUMERIC(15, 3) NOT NULL," +
                "    reward_id UUID REFERENCES Reward(reward_id) ON UPDATE CASCADE ON DELETE SET NULL," +
                "    auction_type VARCHAR(40) NOT NULL" +
                ")";

        String blitzAuctionSQL = "CREATE TABLE IF NOT EXISTS BlitzAuction(" +
                "    auction_id UUID PRIMARY KEY REFERENCES Auction(auction_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "    bid_duration BIGINT NOT NULL," +
                "    preparing_duration BIGINT NOT NULL" +
                ")";

        String longAuctionSQL = "CREATE TABLE IF NOT EXISTS LongAuction(" +
                "    auction_id UUID PRIMARY KEY REFERENCES Auction(auction_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "    extend_time BIGINT NOT NULL," +
                "    overtime TIMESTAMP" +
                ")";

        String bidsSQL = "CREATE TABLE IF NOT EXISTS Bid(" +
                "    user_id UUID REFERENCES UserDetails(user_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "    auction_id UUID REFERENCES Auction(auction_id) ON UPDATE CASCADE," +
                "    bid_date TIMESTAMP NOT NULL," +
                "    points_bid NUMERIC(15, 3) NOT NULL," +
                "    total_bid_value NUMERIC(15, 3) NOT NULL," +
                "    PRIMARY KEY (user_id, auction_id, bid_date)" +
                ")";


        String bundleRewardFunctionCreate = """
                CREATE OR REPLACE FUNCTION get_bundle_reward_info(reward_id_param UUID)
                RETURNS TABLE(reward_id UUID, reward_name TEXT, reward_description TEXT, reward_type TEXT, reward_info TEXT) AS $$
                DECLARE
                    included_reward_ids UUID[];
                BEGIN
                    SELECT ARRAY(SELECT included_reward_id FROM BundleReward c WHERE c.reward_id = reward_id_param) INTO included_reward_ids;

                    RETURN QUERY (
                        SELECT reward_id, reward_name, reward_description, reward_type, reward_info
                        FROM Reward a
                        LEFT JOIN SingleReward b ON a.reward_id = b.reward_id
                        WHERE a.reward_id = ANY(included_reward_ids)
                        UNION
                        SELECT reward_id, reward_name, reward_description, reward_type, STRING_AGG(reward_info, E'\\t')
                        FROM Reward a
                        LEFT JOIN MultiReward b ON a.reward_id = b.reward_id
                        WHERE a.reward_id = ANY(included_reward_ids)
                        GROUP BY reward_id, reward_name, reward_description, reward_type
                    );
                END;
                $$ LANGUAGE plpgsql;""";

        try(
                Connection conn = DatabaseConnection.getConnection();
                Statement create = conn.createStatement()
        ){
            create.addBatch(usersSQL);
            create.addBatch(walletSQL);
            create.addBatch(rewardSQL);
            create.addBatch(singleRewardSQL);
            create.addBatch(multiRewardSQL);
            create.addBatch(bundleRewardSQL);
            create.addBatch(auctionSQL);
            create.addBatch(blitzAuctionSQL);
            create.addBatch(longAuctionSQL);
            create.addBatch(bidsSQL);

            create.addBatch(bundleRewardFunctionCreate);

            create.executeBatch();
        }
    }
}
