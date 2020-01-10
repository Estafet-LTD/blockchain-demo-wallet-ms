package com.estafet.blockchain.demo.wallet.ms.dao;

import com.estafet.blockchain.demo.wallet.ms.jms.NewWalletProducer;
import com.estafet.blockchain.demo.wallet.ms.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class WalletDAOImpl implements WalletDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private NewWalletProducer newWalletProducer;

    @Override
    public Wallet getWallet(String walletAddress) {
        return entityManager.find(Wallet.class, walletAddress);
    }

    @Override
    public List<Wallet> getWallets() {
        return entityManager.createQuery("Select w from Wallet w").getResultList();
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        entityManager.persist(wallet);
        newWalletProducer.sendMessage(wallet);
        return wallet;
    }

    @Override
    public void deleteWallet(String address) {
        Wallet wallet = getWallet(address);
        entityManager.remove(wallet);
    }
}
