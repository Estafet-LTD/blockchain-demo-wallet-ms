package com.estafet.blockchain.demo.wallet.ms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonIgnoreProperties
public class Account {

    private String walletAddress;

    private String accountName;

    public Account() {
    }

    public Account(String walletAddress, String accountName) {
        this.walletAddress = walletAddress;
        this.accountName = accountName;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public static Account fromJSON(String message) {
        try {
            return new ObjectMapper().readValue(message, Account.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toJSON() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
