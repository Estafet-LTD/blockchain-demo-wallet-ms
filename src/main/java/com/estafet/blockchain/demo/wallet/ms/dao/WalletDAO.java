package com.estafet.blockchain.demo.wallet.ms.dao;

import com.estafet.blockchain.demo.wallet.ms.model.Wallet;

import java.util.List;

public interface WalletDAO {

    Wallet getWallet(String walletAddress);

    List<Wallet> getWallets();

    Wallet createWallet(Wallet wallet);

    void deleteWallet(String address);
}
