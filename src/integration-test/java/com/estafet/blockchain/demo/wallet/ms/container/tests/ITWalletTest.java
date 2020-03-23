package com.estafet.blockchain.demo.wallet.ms.container.tests;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.net.HttpURLConnection;

import com.estafet.blockchain.demo.wallet.ms.model.Account;
import com.estafet.blockchain.demo.wallet.ms.model.Wallet;
import com.estafet.blockchain.demo.wallet.ms.repository.WalletRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.estafet.openshift.boost.commons.lib.properties.PropertyUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test")
@SpringBootTest
public class ITWalletTest {

    @Autowired
    private WalletRepository walletRepository;
    WalletPaymentConsumer topic = new WalletPaymentConsumer();

    @Before
    public void before() {
        RestAssured.baseURI = PropertyUtils.instance().getProperty("WALLET_MS_SERVICE_URI");

        Wallet wallet = new Wallet();
        wallet.setWalletAddress("adr");
        wallet.setWalletName("Iryna");
        wallet.setBalance(350);
        wallet.setStatus("CLEARED");
        walletRepository.save(wallet);

        Account account = new Account();
        account.setAccountName("Peter");
        account.setWalletAddress("qqq");
        walletRepository.save(Wallet.instance(account));
    }

    @After
    public void after() {
        topic.closeConnection();
    }

    @Test
    public void testGetWallet() {
        get("/wallet/adr").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("adr"))
                .body("walletName", is("Iryna"))
                .body("balance",is(350))
                .body("status",is("CLEARED"));
    }

    @Test
    public void testCreateWallet() {
        given().contentType(ContentType.JSON)
                .body("{\"accountName\": \"Bill\", \"walletAddress\": \"qwe\" }")
                .when()
                .post("/wallet")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("qwe"))
                .body("balance", is(0))
                .body("status", is("CLEARED"))
                .body("walletName", is("Bill"));
    }

    @Test
    public void testBankToWalletTransfer() {
        given().contentType(ContentType.JSON)
                .body("{ \"walletAddress\": \"qqq\",\"currencyTransfer\": 250 }")
                .when()
                .post("/wallet/qqq/currency-transfer/10")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("qqq"))
                .body("walletName", is("Peter"))
                .body("balance", is(0))
                .body("status", is("PENDING"));
        UpdateWalletBalanceTopicProducer.send("{\"walletAddress\":\"qqq\",\"signature\":\"sign\",\"balance\":250}");
        get("/wallet/qqq").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("qqq"))
                .body("walletName", is("Peter"))
                .body("balance", is(250))
                .body("status", is("CLEARED"));
    }

    @Test
    public void testWalletToWalletTransfer() {
        given().contentType(ContentType.JSON)
                .body("{ \"walletAddress\": \"adr\",\"cryptoAmount\": 150 }")
                .when()
                .post("/wallet/from/adr/to/qqq/crypto-transfer/150")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("adr"))
                .body("walletName", is("Iryna"))
                .body("balance", is(350))
                .body("status", is("PENDING"));

        UpdateWalletBalanceTopicProducer.send("{\"walletAddress\":\"adr\",\"signature\":\"sign\",\"balance\":200}");
        get("/wallet/adr").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("adr"))
                .body("walletName", is("Iryna"))
                .body("balance", is(200))
                .body("status", is("CLEARED"));

        UpdateWalletReceiverBalanceTopicProducer.send("{\"walletAddress\":\"qqq\",\"signature\":\"sign\",\"balance\":150}");
        get("/wallet/qqq").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("qqq"))
                .body("walletName", is("Peter"))
                .body("balance", is(150))
                .body("status", is("CLEARED"));
    }

    @Test
	public void testConsumeNewAccount() {
    NewAccountProducer.send("{\"accountName\":\"Misha\",\"walletAddress\":\"ppp\",\"currency\": \"USD\"}");

        get("/wallet/ppp").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("walletAddress", is("ppp"))
            .body("walletName",is("Misha"))
            .body("balance", is(0))
            .body("status", is("CLEARED"));
	}
}
