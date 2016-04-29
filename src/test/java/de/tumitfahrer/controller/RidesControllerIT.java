package de.tumitfahrer.controller;


import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import de.tumitfahrer.CommonOperations;
import de.tumitfahrer.ITVariables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;

import static com.jayway.restassured.RestAssured.delete;
import static com.jayway.restassured.RestAssured.given;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@WebAppConfiguration
public class RidesControllerIT {

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        Operation operations = sequenceOf(CommonOperations.DELETE_ALL,
                CommonOperations.INSERT_USER,
                CommonOperations.INSERT_RIDES,
                CommonOperations.INSERT_RIDE_CONVERSATION,
                CommonOperations.INSERT_RIDE_CONV_PARTICIPANTS);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operations);
        dbSetup.launch();
    }

    @Test
    public void testDeleteRide_notAuthorised() {
        delete("/rides/1").then().statusCode(401);
    }

    @Test
     public void testGetRideById_authenticated() {
        given().header("Authorization", ITVariables.VALID_API_KEY).when().get("/rides/1").then().statusCode(200);
    }
}
