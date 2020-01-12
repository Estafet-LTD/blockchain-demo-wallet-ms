package com.estafet.blockchain.demo.wallet.ms.controller;

import com.estafet.blockchain.demo.wallet.ms.model.Wallet;
import com.estafet.blockchain.demo.wallet.ms.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estafet.blockchain.demo.wallet.ms.model.API;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WalletController {

	@Value("${app.version}")
	private String appVersion;

	@Autowired
	private WalletService walletService;
	
	@GetMapping("/api")
	public API getAPI() {
		return new API(appVersion);
	}

	@GetMapping("/wallet/{address}")
	public Wallet getWallet(@PathVariable String walletAddress) {
		return walletService.getWallet(walletAddress);
	}

	@GetMapping(value = "/wallets")
	public List<Wallet> getWallets() {
		return walletService.getWallets();
	}

	@PostMapping("/wallet")
	public ResponseEntity createWallet(@RequestBody Wallet wallet) {
		return new ResponseEntity(walletService.createWallet(wallet), HttpStatus.OK);
	}

	@DeleteMapping("/wallet/{address}")
	public ResponseEntity deleteWallet(@PathVariable String address) {
		walletService.deleteWallet(address);
		return new ResponseEntity(address, HttpStatus.OK);
	}
	
}
