package com.estafet.blockchain.demo.wallet.ms.service;

import com.estafet.blockchain.demo.wallet.ms.dao.WalletDAO;
import com.estafet.blockchain.demo.wallet.ms.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private WalletDAO walletDAO;

    @Override
    @Transactional(readOnly = true)
    public Wallet getWallet(String walletAddress) {
        return walletDAO.getWallet(walletAddress);
    }

    @Override
    public List<Wallet> getWallets() {
        return walletDAO.getWallets();
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        return walletDAO.createWallet(wallet);
    }

    @Override
    public void deleteWallet(String address) {
        walletDAO.deleteWallet(address);
    }

    @Autowired
    public void setWalletDAO(WalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }
}
