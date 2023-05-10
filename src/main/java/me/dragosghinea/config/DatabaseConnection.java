package me.dragosghinea.config;

import me.dragosghinea.config.databases.DatabaseConfig;
import me.dragosghinea.config.databases.postgres.DatabaseConfigPostgre;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConfig databaseConfig;
    static {
        setDatabaseConfig(DatabaseConfigPostgre.getInstance());
    }

    private static void setDatabaseConfig(DatabaseConfig config){
        databaseConfig = config;
    }


    private DatabaseConnection(){

    }

    public static void resetAllData() throws SQLException {
        databaseConfig.resetAllData();
    }

    public static void generateTables() throws SQLException {
        databaseConfig.generateTables();
    }

    public static Connection getConnection() throws SQLException {
        return databaseConfig.getDataSource().getConnection();
    }
}
