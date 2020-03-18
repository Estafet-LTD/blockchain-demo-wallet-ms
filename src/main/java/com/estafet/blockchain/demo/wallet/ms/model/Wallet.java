package com.estafet.blockchain.demo.wallet.ms.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Document
public class Wallet implements Serializable {

	@NotNull
	@Id
	private String walletAddress;

	@Field
	private String walletName;

	@NotNull
	@Field
	private int balance = 0;

	// status can be cleared or pending
	@NotNull
	@Field
	private String status = "CLEARED";

	public Wallet(){

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static Wallet instance(Account account) {
		Wallet wallet = new Wallet();
		wallet.setWalletAddress(account.getWalletAddress());
		wallet.setWalletName(account.getAccountName());
		wallet.setBalance(0);
		wallet.setStatus("CLEARED");
		return wallet;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Wallet newWallet = (Wallet) o;
		return balance == newWallet.balance &&
				Objects.equals(walletAddress, newWallet.walletAddress) &&
				Objects.equals(walletName, newWallet.walletName) &&
				Objects.equals(status, newWallet.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(walletAddress, walletName, balance, status);
	}
}
