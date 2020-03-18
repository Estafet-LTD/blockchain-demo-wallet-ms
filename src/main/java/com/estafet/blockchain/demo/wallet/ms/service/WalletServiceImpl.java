package com.estafet.blockchain.demo.wallet.ms.service;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentMessage;
import com.estafet.blockchain.demo.messages.lib.wallet.UpdateWalletBalanceMessage;
import com.estafet.blockchain.demo.messages.lib.wallet.WalletPaymentMessage;
import com.estafet.blockchain.demo.wallet.ms.jms.BankToWalletPaymentProducer;
import com.estafet.blockchain.demo.wallet.ms.jms.WalletToWalletPaymentProducer;
import com.estafet.blockchain.demo.wallet.ms.model.Account;
import com.estafet.blockchain.demo.wallet.ms.model.Wallet;
import com.estafet.blockchain.demo.wallet.ms.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private WalletRepository walletRepository;
    private WalletToWalletPaymentProducer toWalletPaymentProducer;
    private BankToWalletPaymentProducer bankToWalletPaymentProducer;

    @Override
    @Transactional(readOnly = true)
    public Wallet getWallet(String walletAddress) {
        return walletRepository.findOne(walletAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wallet> getWallets() {
        return walletRepository.findAll();
    }

    @Override
    @Transactional
    public Wallet createWallet(Account account) {
        return walletRepository.save(Wallet.instance(account));
    }

    @Override
    @Transactional
    public Wallet walletToWalletTransfer(String fromWalletAddress, String toWalletAddress, int cryptoAmount) {
        Wallet wallet = getWallet(fromWalletAddress);
        if(wallet.getStatus()!=null && wallet.getStatus().equals("CLEARED")){
            toWalletPaymentProducer.sendMessage(new WalletPaymentMessage(cryptoAmount,fromWalletAddress,toWalletAddress,"sign", UUID.randomUUID().toString()));
            wallet.setStatus("PENDING");
        }
        return wallet;
    }

    @Override
    @Transactional
    public Wallet bankToWalletTransfer(String walletAddress, double amount) {
        Wallet wallet = getWallet(walletAddress);
        if(wallet.getStatus()!=null && wallet.getStatus().equals("CLEARED")){
            bankToWalletPaymentProducer.sendMessage(new BankPaymentMessage(amount,walletAddress,"qwwee",UUID.randomUUID().toString()));
            wallet.setStatus("PENDING");
        }
        return wallet;
    }

    @Override
    public void handleUpdateWalletBalanceMessage(UpdateWalletBalanceMessage message) {
        Wallet wallet = getWallet(message.getWalletAddress());
        wallet.setStatus("CLEARED");
        wallet.setBalance(message.getBalance());
        walletRepository.getCouchbaseOperations().update(wallet);
    }

    @Autowired
    public void setWalletRepository(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
