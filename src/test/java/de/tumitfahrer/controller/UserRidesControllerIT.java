package de.tumitfahrer.controller;


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
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@WebAppConfiguration
public class UserRidesControllerIT {

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        Operation operations = sequenceOf(CommonOperations.DELETE_ALL, CommonOperations.INSERT_USER, CommonOperations.INSERT_RIDES);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operations);
        dbSetup.launch();
    }

    @Test
    public void testCreateRide_missingDeparturePlace() {

        //no departure place
        Map<String, Object> jsonAsMap = new HashMap<>();
        Map<String, Object> rideMap = new HashMap<>();
        jsonAsMap.put("ride", rideMap);

        rideMap.put("driver", true);
        rideMap.put("is_ride_request", true);
        rideMap.put("destination", "string");
        rideMap.put("ride_type", 0);
        rideMap.put("departure_time", "string");
        rideMap.put("departure_latitude", 0);
        rideMap.put("departure_longitude", 0);
        rideMap.put("destination_latitude", 0);
        rideMap.put("destination_longitude", 0);
        rideMap.put("free_seats", 2);
        rideMap.put("price", 0);
        rideMap.put("meeting_point", "string");
        rideMap.put("car", "string");
        rideMap.put("title", "string");

        given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMap).with().contentType("application/json")
                .when().post("/users/1/rides").then().statusCode(400);
    }

    @Test
    public void testCreateRide_noFreeSeats() {

        Map<String, Object> jsonAsMap = new HashMap<>();
        Map<String, Object> rideMap = new HashMap<>();
        jsonAsMap.put("ride", rideMap);

        rideMap.put("driver", true);
        rideMap.put("is_ride_request", true);
        rideMap.put("destination", "string");
        rideMap.put("departure_place", "string");
        rideMap.put("ride_type", 0);
        rideMap.put("departure_time", "string");
        rideMap.put("departure_latitude", 0);
        rideMap.put("departure_longitude", 0);
        rideMap.put("destination_latitude", 0);
        rideMap.put("destination_longitude", 0);
        rideMap.put("free_seats", 0);
        rideMap.put("price", 0);
        rideMap.put("meeting_point", "string");
        rideMap.put("car", "string");
        rideMap.put("title", "string");

        given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMap).with().contentType("application/json")
                .when().post("/users/1/rides").then().statusCode(400);
    }


    @Test
    public void testCreateRide_successfull() {


        Map<String, Object> jsonAsMap = new HashMap<>();
        Map<String, Object> rideMap = new HashMap<>();
        jsonAsMap.put("ride", rideMap);

        rideMap.put("driver", true);
        rideMap.put("is_ride_request",true);
        rideMap.put("departure_place","Munich, Germany");
        rideMap.put("destination", "Berlin, Germany");
        rideMap.put("ride_type", 0);
        rideMap.put("departure_time", "2115-11-27 09:40:00");
        rideMap.put("departure_latitude", 48.1351253);
        rideMap.put("departure_longitude", 11.5819806);
        rideMap.put("destination_latitude", 52.5200066);
        rideMap.put("destination_longitude", 13.404954);
        rideMap.put("free_seats", 2);
        rideMap.put("price", 0);
        rideMap.put("meeting_point", "Munich, Germany");
        rideMap.put("car", "string");
        rideMap.put("title", "string");

        given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMap).with().contentType("application/json")
                .when().post("/users/1/rides").then().statusCode(200);
    }


}
