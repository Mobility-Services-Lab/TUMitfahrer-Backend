package de.tumitfahrer.controller;

import com.jayway.restassured.response.Response;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import de.tumitfahrer.CommonOperations;
import de.tumitfahrer.ITVariables;
import de.tumitfahrer.daos.IUserDao;
import de.tumitfahrer.entities.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;

import java.util.Date;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@WebAppConfiguration
public class SessionsControllerIT {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private IUserDao userDao;

    @Before
    public void setUp() throws Exception {
        Operation operations = sequenceOf(CommonOperations.DELETE_ALL, CommonOperations.INSERT_USER);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operations);
        dbSetup.launch();
    }

    @Test
    public void testGenerateApiKey_validCredentials() throws Exception {
        final String authToken = "Basic dGVzdF92YWxpZEBteXR1bS5kZTo0d1F4VWhLMA==";

        final String response = given().header("Authorization", authToken)
                .when().post("/sessions").then().statusCode(200).extract().response().asString();

        final Map<String, Object> mapResponse = from(response).getMap("user");

        assertEquals(ITVariables.VALID_USER_ID, mapResponse.get("id"));

        // Check if returnes values are also set in the database
        User user = userDao.load(ITVariables.VALID_USER_ID);

        final String apiKey = (String) mapResponse.get("api_key");

        assertEquals(user.getApiKey(), apiKey);
        assertTrue("ApiKeyExpires should be in the future", user.getApiKeyExpires().after(new Date()));
    }

    @Test
    public void testGenerateApiKey_invalidCredentials() throws Exception {
        final String authToken = "Basic asd";

        given().header("Authorization", authToken)
                .when().post("/sessions").then().statusCode(400).extract();
    }

    @Test
    public void testCheckApiKey_validKey() throws Exception {
        given().header("Authorization", ITVariables.VALID_API_KEY)
                .when().put("/sessions").then().statusCode(200);
    }

    @Test
    public void testCheckApiKey_inValidKey() throws Exception {
        final String invalidApiKey = "";
        given().header("Authorization", invalidApiKey)
                .when().put("/sessions").then().statusCode(401);
    }

    @Test
    public void testGenerateApiKey_validCredentialsAndCheckValid() throws Exception {
        final String authToken = "Basic dGVzdF92YWxpZEBteXR1bS5kZTo0d1F4VWhLMA==";

        final String response = given().header("Authorization", authToken)
                .when().post("/sessions").then().statusCode(200).extract().response().asString();

        final Map<String, Object> mapResponse = from(response).getMap("user");

        final int receivedUserId = (int) mapResponse.get("id");
        final String receivedApiKey = (String) mapResponse.get("api_key");

        given().header("Authorization", receivedApiKey)
                .when().put("/sessions").then().statusCode(200);
    }
}