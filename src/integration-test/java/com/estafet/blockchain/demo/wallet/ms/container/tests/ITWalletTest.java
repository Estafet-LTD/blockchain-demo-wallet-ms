package com.estafet.blockchain.demo.wallet.ms.container.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.estafet.microservices.scrum.lib.commons.properties.PropertyUtils;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import java.net.HttpURLConnection;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
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
    @DatabaseSetup("ITWalletTest-data.xml")
    public void testGetWallet() {
        get("/wallet/adr").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("adr"))
                .body("walletName", is("Iryna"))
                .body("balance",is(100))
                .body("status",is("CLEARED"));
    }

    @Test
    @DatabaseSetup("ITWalletTest-data.xml")
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
    @DatabaseSetup("ITWalletTest-data.xml")
    public void testWalletToWalletTransfer() {
        given().contentType(ContentType.JSON)
                .body("{ \"walletAddress\": \"adr\",\"cryptoAmount\": 5 }")
                .when()
                .post("/wallet/adr/crypto-transfer/5")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("adr"))
                .body("walletName", is("Iryna"))
                .body("balance", is(100))
                .body("status", is("PENDING"));

        UpdateWalletBalanceTopicProducer.send("{\"walletAddress\":\"adr\",\"signature\":\"sign\",\"balance\":95}");
        get("/wallet/adr").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("adr"))
                .body("walletName", is("Iryna"))
                .body("balance", is(95))
                .body("status", is("CLEARED"));
    }

    @Test
    @DatabaseSetup("ITWalletTest-data.xml")
    public void testBankToWalletTransfer() {
        given().contentType(ContentType.JSON)
                .body("{ \"walletAddress\": \"qqq\",\"currencyTransfer\": 10 }")
                .when()
                .post("/wallet/qqq/currency-transfer/10")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("qqq"))
                .body("walletName", is("Peter"))
                .body("balance", is(15))
                .body("status", is("PENDING"));
        UpdateWalletBalanceTopicProducer.send("{\"walletAddress\":\"qqq\",\"signature\":\"sign\",\"balance\":5}");
        get("/wallet/qqq").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("qqq"))
                .body("walletName", is("Peter"))
                .body("balance", is(5))
                .body("status", is("CLEARED"));
    }

    @Test
    @DatabaseSetup("ITWalletTest-data.xml")
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
