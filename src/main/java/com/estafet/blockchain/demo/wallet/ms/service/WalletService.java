package com.estafet.blockchain.demo.wallet.ms.service;

import com.estafet.blockchain.demo.wallet.ms.model.Wallet;

import java.util.List;

public interface WalletService {
    Wallet getWallet(String walletAddress);

    List<Wallet> getWallets();

    Wallet createWallet(Wallet wallet);

    void deleteWallet(String address);
}
