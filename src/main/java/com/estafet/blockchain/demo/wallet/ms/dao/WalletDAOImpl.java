package com.estafet.blockchain.demo.wallet.ms.dao;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentMessage;
import com.estafet.blockchain.demo.messages.lib.wallet.WalletPaymentMessage;
import com.estafet.blockchain.demo.wallet.ms.jms.BankToWalletPaymentProducer;
import com.estafet.blockchain.demo.wallet.ms.jms.WalletToWalletPaymentProducer;
import com.estafet.blockchain.demo.wallet.ms.model.Account;
import com.estafet.blockchain.demo.wallet.ms.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
public class WalletDAOImpl implements WalletDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private WalletToWalletPaymentProducer toWalletPaymentProducer;
    private BankToWalletPaymentProducer bankToWalletPaymentProducer;

    @Override
    public Wallet getWallet(String walletAddress) {
        return entityManager.find(Wallet.class, walletAddress);
    }

    @Override
    public List<Wallet> getWallets() {
        return entityManager.createQuery("Select w from Wallet w").getResultList();
    }

    @Override
    public Wallet createWallet(Account account) {
        Wallet wallet = Wallet.instance(account);
        entityManager.persist(wallet);
        return wallet;
    }

    @Override
    public Wallet walletToWalletTransfer(String fromWalletAddress, String toWalletAddress, int cryptoAmount) {
        Wallet wallet = getWallet(fromWalletAddress);
        if(wallet.getStatus()!=null && wallet.getStatus().equals("CLEARED")){
            toWalletPaymentProducer.sendMessage(new WalletPaymentMessage(cryptoAmount,fromWalletAddress,toWalletAddress,"sign", UUID.randomUUID().toString()));
            wallet.setStatus("PENDING");
        }
        return wallet;
    }

    @Override
    public Wallet bankToWalletTransfer(String walletAddress, double amount) {
        Wallet wallet = getWallet(walletAddress);
        if(wallet.getStatus()!=null && wallet.getStatus().equals("CLEARED")){
            bankToWalletPaymentProducer.sendMessage(new BankPaymentMessage(amount,walletAddress,"qwwee",UUID.randomUUID().toString()));
            wallet.setStatus("PENDING");
        }
        return wallet;
    }

    @Override
    public void updateWallet(Wallet wallet) {
        entityManager.merge(wallet);
    }

    @Autowired
    public void setToWalletPaymentProducer(WalletToWalletPaymentProducer toWalletPaymentProducer) {
        this.toWalletPaymentProducer = toWalletPaymentProducer;
    }

    @Autowired
    public void setBankToWalletPaymentProducer(BankToWalletPaymentProducer bankToWalletPaymentProducer) {
        this.bankToWalletPaymentProducer = bankToWalletPaymentProducer;
    }
}
