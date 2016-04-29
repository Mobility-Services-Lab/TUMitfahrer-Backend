package de.tumitfahrer.controller;

import com.jayway.restassured.response.Response;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import de.tumitfahrer.CommonOperations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@WebAppConfiguration
public class ConversationControllerIT {

    @Autowired
    private DataSource dataSource;

    public final Operation INSERT_USER2 =
            insertInto("USERS").columns("id", "api_key", "api_key_expires", "enabled", "email", "password", "salt")
                    .values(2, "apikey2", "2020-01-01", true,"test_valid2@mytum.de", "2720f70c4261d025bf3aa8d141fb069f13a54c21001b0ef97a3c7f694ae35e98107b061129bf6bf69bc6dc09410650fbfe068dc1999079d28ba23ea63920434e", "yL5dmCDl")
                    .build();

    @Before
    public void setUp() throws Exception {

        Operation operations = sequenceOf(CommonOperations.DELETE_ALL,
                CommonOperations.INSERT_USER, INSERT_USER2,
                CommonOperations.INSERT_RIDES, CommonOperations.INSERT_RIDE_RELATIONSHIP,
                CommonOperations.INSERT_RIDE_CONVERSATION, CommonOperations.INSERT_RIDE_CONV_PARTICIPANTS);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operations);
        dbSetup.launch();
    }


    @Test
    public void getConversationUnauthorized() {

        Response response = given().header("Authorization", "apikey2")
                .when().get("rides/1/conversations/1").then().statusCode(401).extract().response();

    }

    @Test
    public void getConversationAuthorized() {

        Response response = given().header("Authorization","apikey")
                .when().get("rides/1/conversations/1").then().statusCode(200).extract().response();

    }
}
