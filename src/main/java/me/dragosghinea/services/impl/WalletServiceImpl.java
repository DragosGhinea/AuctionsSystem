package me.dragosghinea.services.impl;

import lombok.RequiredArgsConstructor;
import me.dragosghinea.model.Wallet;
import me.dragosghinea.model.enums.Currency;
import me.dragosghinea.repository.WalletRepository;
import me.dragosghinea.services.WalletService;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final Wallet wallet;

    @Override
    public boolean addPointsToWallet(BigDecimal points) {
        if(walletRepository.setWalletPointsBalance(wallet.getOwnerId(), wallet.getPoints().add(points))){
            wallet.addPoints(points);
            return true;
        }

        return false;
    }

    @Override
    public boolean removePointsFromWallet(BigDecimal points) {
        if(wallet.removePoints(points)){
            if(!walletRepository.setWalletPointsBalance(wallet.getOwnerId(), wallet.getPoints())){
                wallet.addPoints(points);
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean setPointsBalance(BigDecimal points) {
        if(walletRepository.setWalletPointsBalance(wallet.getOwnerId(), wallet.getPoints())){
            wallet.setPoints(points);
            return true;
        }

        return false;
    }

    @Override
    public BigDecimal getPointsBalance() {
        return wallet.getPoints();
    }

    @Override
    public boolean setPreferredCurrency(Currency currency) {
        if(walletRepository.updatePreferredCurrency(wallet.getOwnerId(), currency)){
            wallet.setPreferredCurrency(currency);
            return false;
        }

        return true;
    }
}
