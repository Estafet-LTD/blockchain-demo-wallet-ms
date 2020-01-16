package com.estafet.blockchain.demo.wallet.ms.service;

import com.estafet.blockchain.demo.messages.lib.wallet.UpdateWalletBalanceMessage;
import com.estafet.blockchain.demo.wallet.ms.model.Account;
import com.estafet.blockchain.demo.wallet.ms.model.Wallet;

import java.util.List;

public interface WalletService {
    Wallet getWallet(String walletAddress);

    List<Wallet> getWallets();

    Wallet createWallet(Account account);

    Wallet walletToWalletTransfer(String walletAddress, String toWalletAddress, int cryptoAmount);

    Wallet bankToWalletTransfer(String walletAddress, double amount);

    void handleUpdateWalletBalanceMessage(UpdateWalletBalanceMessage message);

}
