package com.estafet.blockchain.demo.wallet.ms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

}
