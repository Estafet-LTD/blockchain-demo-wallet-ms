package com.estafet.blockchain.demo.wallet.ms.service;

import com.estafet.blockchain.demo.messages.lib.wallet.UpdateWalletBalanceMessage;
import com.estafet.blockchain.demo.wallet.ms.model.Wallet;

import java.util.List;

public interface WalletService {
    Wallet getWallet(String walletAddress);

    List<Wallet> getWallets();

    Wallet createWallet(Wallet wallet);

    Wallet walletToWalletTransfer(String walletAddress, int cryptoAmount);

    Wallet bankToWalletTransfer(String walletAddress, double amount);

    void handleUpdateWalletBalanceMessage(UpdateWalletBalanceMessage message);
}
