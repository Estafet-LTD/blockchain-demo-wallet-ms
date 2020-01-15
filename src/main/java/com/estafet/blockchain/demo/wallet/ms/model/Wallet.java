package com.estafet.blockchain.demo.wallet.ms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "WALLET")
public class Wallet {

	@Id
	@Column(name = "WALLET_ADDRESS", nullable = false)
	private String walletAddress;

	@Column(name = "WALLET_NAME", nullable = false)
	private String walletName;

	@Column(name = "BALANCE", nullable = false)
	private int balance = 0;

	// status can be cleared or pending
	@Column(name = "STATUS", nullable = false)
	private String status = "CLEARED";

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

}
