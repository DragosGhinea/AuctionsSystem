package me.dragosghinea.service;

import me.dragosghinea.model.Wallet;
import me.dragosghinea.model.enums.Currency;
import me.dragosghinea.repository.WalletRepository;
import me.dragosghinea.services.impl.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

@DisplayName("Money operations tests")
@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {
    @Mock
    private WalletRepository walletRepository;

    @Spy
    private Wallet wallet = Wallet.builder()
            .ownerId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .preferredCurrency(Currency.PNT)
            .points(BigDecimal.valueOf(150))
            .build();

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    @DisplayName("Add money to wallet")
    void testAddMoney() throws SQLException {
        Mockito.when(walletRepository.setWalletPointsBalance(Mockito.any(), Mockito.any())).thenReturn(true);

        Assertions.assertTrue(walletService.addPointsToWallet(BigDecimal.valueOf(100)));
    }

    @Test
    @DisplayName("Remove money from wallet")
    void testRemoveMoney() throws SQLException {
        Mockito.when(walletRepository.setWalletPointsBalance(Mockito.any(), Mockito.any())).thenReturn(true);

        Assertions.assertTrue(walletService.removePointsFromWallet(BigDecimal.valueOf(100)));
        Assertions.assertTrue(walletService.removePointsFromWallet(BigDecimal.valueOf(50)));
        Assertions.assertFalse(walletService.removePointsFromWallet(BigDecimal.valueOf(200)));
    }

    @Test
    @DisplayName("Set money balance")
    void testSetMoneyBalance() throws SQLException {
        Mockito.when(walletRepository.setWalletPointsBalance(Mockito.any(), Mockito.any())).thenReturn(true);

        Assertions.assertTrue(walletService.setPointsBalance(BigDecimal.valueOf(100)));
        Assertions.assertEquals(BigDecimal.valueOf(100), walletService.getPointsBalance());

        Assertions.assertTrue(walletService.setPointsBalance(BigDecimal.valueOf(-100)));
        Assertions.assertEquals(BigDecimal.valueOf(-100), walletService.getPointsBalance());
    }

    @Test
    @DisplayName("Currency change test")
    void testCurrencyChange() throws SQLException {
        Mockito.when(walletRepository.updatePreferredCurrency(Mockito.any(), Mockito.any())).thenReturn(true);

        Assertions.assertTrue(walletService.setPreferredCurrency(Currency.USD));
        Assertions.assertEquals(Currency.USD, wallet.getPreferredCurrency());
    }

}
