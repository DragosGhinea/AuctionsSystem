package me.dragosghinea.mapper;

import me.dragosghinea.model.Wallet;
import me.dragosghinea.model.enums.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class WalletMapper {

    private static final WalletMapper instance = new WalletMapper();

    public static WalletMapper getInstance() {
        return instance;
    }

    private WalletMapper(){}

    private Wallet directMapToWallet(ResultSet set) throws SQLException {
        return new Wallet(
                set.getObject("user_id", UUID.class),
                Currency.valueOf(set.getString("preferred_currency")),
                set.getBigDecimal("points")
        );
    }

    public Wallet mapToWallet(ResultSet set) throws SQLException {
        if (!set.next())
            return null;

        return directMapToWallet(set);
    }
}
