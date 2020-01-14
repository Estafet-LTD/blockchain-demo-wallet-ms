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

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ITWalletTest {


    @Before
    public void before() {
        RestAssured.baseURI = PropertyUtils.instance().getProperty("WALLET_MS_SERVICE_URI");
    }

    @After
    public void after() {
    }

    @Test
    @DatabaseSetup("ITWalletTest-data.xml")
    public void testGetWallet() {
        get("/wallet/adr").then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("adr"))
                .body("accountName", is("Iryna"))
                .body("balance",is(100))
                .body("status",is("CLEARED"));
    }

    @Test
    @DatabaseSetup("ITWalletTest-data.xml")
    public void testCreateWallet() {
        given().contentType(ContentType.JSON)
                .body("{\"walletAddress\": \"abcd\", \"walletName\": \"Bill\", \"currency\": \"EUR\" }")
                .when()
                .post("/wallet")
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("walletAddress", is("abcd"))
                .body("accountName", is("Bill"))
                .body("accountName", is("EUR"));
    }
}
