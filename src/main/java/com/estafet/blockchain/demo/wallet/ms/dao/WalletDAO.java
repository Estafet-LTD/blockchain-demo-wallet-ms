package com.estafet.blockchain.demo.wallet.ms.dao;

import com.estafet.blockchain.demo.wallet.ms.model.Wallet;

import java.util.List;

public interface WalletDAO {

    Wallet getWallet(String walletAddress);

    List<Wallet> getWallets();

    Wallet createWallet(Wallet wallet);

    Wallet walletToWalletTransfer(String walletAddress, int cryptoAmount);

    void updateWallet(Wallet wallet);

    Wallet bankToWalletTransfer(String walletAddress, double amount);
}
