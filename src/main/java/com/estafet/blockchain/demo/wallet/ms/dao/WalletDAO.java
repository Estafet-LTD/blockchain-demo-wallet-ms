package com.estafet.blockchain.demo.wallet.ms.dao;

import com.estafet.blockchain.demo.wallet.ms.model.Account;
import com.estafet.blockchain.demo.wallet.ms.model.Wallet;

import java.util.List;

public interface WalletDAO {

    Wallet getWallet(String walletAddress);

    List<Wallet> getWallets();

    Wallet createWallet(Account account);

    Wallet walletToWalletTransfer(String walletAddress, String toWalletAddress, int cryptoAmount);

    void updateWallet(Wallet wallet);

    Wallet bankToWalletTransfer(String walletAddress, double amount);

}