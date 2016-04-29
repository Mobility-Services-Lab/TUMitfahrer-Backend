package de.tumitfahrer.controller;

import com.jayway.restassured.response.Response;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import de.tumitfahrer.CommonOperations;
import de.tumitfahrer.ITVariables;
import org.junit.Before;
import org.junit.Ignore;
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
public class RideRequestControllerIT {

    @Autowired
    private DataSource dataSource;

    public final Operation INSERT_USER2 =
            insertInto("USERS").columns("id", "api_key", "api_key_expires", "enabled", "email", "password", "salt")
                    .values(2, "apikey2", "2020-01-01", true,"test_valid2@mytum.de", "2720f70c4261d025bf3aa8d141fb069f13a54c21001b0ef97a3c7f694ae35e98107b061129bf6bf69bc6dc09410650fbfe068dc1999079d28ba23ea63920434e", "yL5dmCDl")
                    .build();

    public final Operation INSERT_USER3 =
            insertInto("USERS").columns("id", "api_key", "api_key_expires", "enabled", "email", "password", "salt")
                    .values(3, "apikey3", "2020-01-01", true, "test_valid3@mytum.de", "2720f70c4261d025bf3aa8d141fb069f13a54c21001b0ef97a3c7f694ae35e98107b061129bf6bf69bc6dc09410650fbfe068dc1999079d28ba23ea63920434e", "yL5dmCDl")
                    .build();

    public final Operation INSERT_RIDES =
            insertInto("RIDES")
                    .columns("id","departure_place", "destination", "departure_time", "free_seats", "user_id", "ride_type")
                    .values(2,"Garching, Forschungszentrum, Garching, Germany", "Neuperlach Süd, Munich, Germany", "2020-05-09", 1, 1, 0)
                    .build();

    public final Operation INSERT_RIDE_RELATIONSHIP =
            insertInto("RELATIONSHIPS")
                    .columns("id","user_id", "ride_id", "is_driving")
                    .values(2, 1, 2, false)
                    .build();

    public final Operation INSERT_RIDE_CONVERSATION2 =
            insertInto("CONVERSATIONS")
                    .columns("id","ride_id", "created_at", "updated_at", "user_id", "deleted_at")
                    .values(2, 2, "2020-05-09", "2020-05-09", 1, null)
                    .build();


    public final Operation INSERT_RIDE_CONV_PARTICIPANTS2 =
            insertInto("CONV_PARTICIPANTS")
                    .columns("id","conv_id", "user_id", "created_at", "updated_at")
                    .values(2, 2, 1,"2020-05-09", "2020-05-09")
                    .build();



    @Before
    public void setUp() throws Exception {

        Operation operations = sequenceOf(CommonOperations.DELETE_ALL,
                CommonOperations.INSERT_USER,
                CommonOperations.INSERT_RIDES,
                CommonOperations.INSERT_RIDE_RELATIONSHIP,
                CommonOperations.INSERT_RIDE_CONVERSATION,
                CommonOperations.INSERT_RIDE_CONV_PARTICIPANTS,
                INSERT_USER2, INSERT_USER3,
                INSERT_RIDES,INSERT_RIDE_RELATIONSHIP, INSERT_RIDE_CONVERSATION2, INSERT_RIDE_CONV_PARTICIPANTS2);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operations);
        dbSetup.launch();
    }

    @Test
    @Ignore
    public void RideRequestsuccessfull_whenDriver(){
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("passenger_id",2);


        Response response = given().header("Authorization","apikey2").body(jsonAsMap).with().contentType("application/json")
                .when().post("/rides/1/requests").then().statusCode(200).extract().response();

        Map<String, Object> jsonAsMapApprove = new HashMap<>();
        jsonAsMapApprove.put("passenger_id",2);
        jsonAsMapApprove.put("confirmed",true);

        response = given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMapApprove).with().contentType("application/json")
                .when().put("/rides/1/requests").then().statusCode(200).extract().response();


        jsonAsMap.put("passenger_id",3);

       response = given().header("Authorization", "apikey3").body(jsonAsMap).with().contentType("application/json")
                .when().post("/rides/1/requests").then().statusCode(200).extract().response();


        jsonAsMapApprove.put("passenger_id",3);
        jsonAsMapApprove.put("confirmed",true);

        response = given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMapApprove).with().contentType("application/json")
                .when().put("/rides/1/requests").then().statusCode(400).extract().response();

    }



    @Test
    @Ignore
    public void RideRequestsuccessfull_whenPassenger(){
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("passenger_id",2);


        Response response = given().header("Authorization","apikey2").body(jsonAsMap).with().contentType("application/json")
                .when().post("/rides/2/requests").then().statusCode(200).extract().response();

        Map<String, Object> jsonAsMapApprove = new HashMap<>();
        jsonAsMapApprove.put("passenger_id",2);
        jsonAsMapApprove.put("confirmed",true);

        response = given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMapApprove).with().contentType("application/json")
                .when().put("/rides/2/requests").then().statusCode(200).extract().response();


        jsonAsMap.put("passenger_id",3);

        response = given().header("Authorization", "apikey3").body(jsonAsMap).with().contentType("application/json")
                .when().post("/rides/2/requests").then().statusCode(200).extract().response();


        jsonAsMapApprove.put("passenger_id",3);
        jsonAsMapApprove.put("confirmed",true);

        response = given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMapApprove).with().contentType("application/json")
                .when().put("/rides/2/requests").then().statusCode(400).extract().response();

    }



    @Test
    public void getAllRideRequests(){

        Response response = given().header("Authorization", ITVariables.VALID_API_KEY )
                .when().get("/rides/1/requests").then().statusCode(200).extract().response();

    }
}
