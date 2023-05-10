package me.dragosghinea.config.databases;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public interface DatabaseConfig {

    HikariDataSource getDataSource();

    void resetAllData() throws SQLException;

    void generateTables() throws SQLException;
}
