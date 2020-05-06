package com.estafet.blockchain.demo.wallet.ms.container.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.net.HttpURLConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.estafet.openshift.boost.commons.lib.properties.PropertyUtils;
import com.estafet.openshift.boost.couchbase.lib.annotation.BucketSetup;
import com.estafet.openshift.boost.couchbase.lib.spring.CouchbaseTestExecutionListener;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, CouchbaseTestExecutionListener.class })
public class ITWalletTest {

    WalletPaymentConsumer topic = new WalletPaymentConsumer();

    @Before
    public void before() {
        RestAssured.baseURI = PropertyUtils.instance().getProperty("WALLET_MS_SERVICE_URI");
    }

    @After
    public void after() {
        topic.closeConnection();
    }

    @Test
    @BucketSetup("ITWalletTest.json")
    public void testGetWallet() {
        get("/wallet/adr").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("adr"))
                .body("walletName", is("Iryna"))
                .body("balance",is(350))
                .body("status",is("CLEARED"));
    }

    @Test
    @BucketSetup("ITWalletTest.json")
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
    @BucketSetup("ITWalletTest.json")
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
    @BucketSetup("ITWalletTest.json")
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
    @BucketSetup("ITWalletTest.json")
	public void testConsumeNewAccount() {
    	NewAccountProducer.send("{\"accountName\":\"Misha\",\"walletAddress\":\"ppp\",\"currency\": \"USD\"}");
        get("/wallet/ppp").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("walletAddress", is("ppp"))
            .body("walletName",is("Misha"))
            .body("balance", is(0))
            .body("status", is("CLEARED"));
	}
    
    @Test
    @BucketSetup("ITWalletTest.json")
	public void testConsumeDeleteAccount() {
    	DeleteAccountProducer.send("{\"accountName\":\"Misha\",\"walletAddress\":\"qqq\",\"currency\": \"USD\"}");
        get("/wallets").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("walletAddress", hasItems("adr"));
	}
    
    @Test
    @BucketSetup("ITWalletTest.json")
	public void testGetWallets() {
        get("/wallets").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("walletAddress", hasItems("adr", "qqq"));
	}
}
