package com.estafet.blockchain.demo.wallet.ms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.blockchain.demo.wallet.ms.model.API;

@RestController
public class WalletController {

	@Value("${app.version}")
	private String appVersion;
	
	@GetMapping("/api")
	public API getAPI() {
		return new API(appVersion);
	}
	
}
