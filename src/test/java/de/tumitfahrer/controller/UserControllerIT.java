package de.tumitfahrer.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.*;
import static com.jayway.restassured.path.json.JsonPath.*;

import de.tumitfahrer.CommonOperations;
import de.tumitfahrer.ITVariables;
import de.tumitfahrer.daos.IUserDao;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.enums.UniversityDepartment;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@WebAppConfiguration
public class UserControllerIT {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private IUserDao userDao;


    public final Operation INSERT_USER2 =
            insertInto("USERS").columns("id", "api_key", "api_key_expires", "enabled", "email", "password", "salt")
                    .values(2, "apikey2", "2020-01-01", true,"test_valid2@mytum.de", "2720f70c4261d025bf3aa8d141fb069f13a54c21001b0ef97a3c7f694ae35e98107b061129bf6bf69bc6dc09410650fbfe068dc1999079d28ba23ea63920434e", "yL5dmCDl")
                    .build();

    public final Operation INSERT_USER3 =
            insertInto("USERS").columns("id", "api_key", "api_key_expires", "enabled", "email", "password", "salt")
                    .values(3, "apikey3", "2020-01-01", true, "test_valid3@mytum.de", "2720f70c4261d025bf3aa8d141fb069f13a54c21001b0ef97a3c7f694ae35e98107b061129bf6bf69bc6dc09410650fbfe068dc1999079d28ba23ea63920434e", "yL5dmCDl")
                    .build();
    public final Operation INSERT_USER4 =
            insertInto("USERS").columns("id", "api_key", "api_key_expires", "enabled", "email", "password", "salt")
                    .values(4, "apikey4", "2020-01-01", true, "test_valid4@mytum.de", "2720f70c4261d025bf3aa8d141fb069f13a54c21001b0ef97a3c7f694ae35e98107b061129bf6bf69bc6dc09410650fbfe068dc1999079d28ba23ea63920434e", "yL5dmCDl")
                    .build();

    public final Operation INSERT_RIDES =
            insertInto("RIDES")
                    .columns("id","departure_place", "destination", "departure_time", "free_seats", "user_id", "ride_type")
                    .values(2,"Garching, Forschungszentrum, Garching, Germany", "Neuperlach Süd, Munich, Germany", "2018-05-09", 1,2, 0)
                    .build();

    public final Operation INSERT_RIDE_RELATIONSHIP =
            insertInto("RELATIONSHIPS")
                    .columns("id","user_id", "ride_id", "is_driving")
                    .values(2, 2, 2, true)
                    .build();



    @Before
    public void setUp() throws Exception {
        Operation operations = sequenceOf(CommonOperations.DELETE_ALL, CommonOperations.INSERT_USER,CommonOperations.INSERT_RIDES, CommonOperations.INSERT_RIDE_RELATIONSHIP,INSERT_USER2, INSERT_USER3, INSERT_USER4, INSERT_RIDES, INSERT_RIDE_RELATIONSHIP);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operations);
        dbSetup.launch();
    }

    @Test
    public void testGetUser_notAuthenticated() {
        get("/users/0").then().statusCode(401);
    }

    @Test
    public void testGetUser_notFound() {
        given().header("Authorization", ITVariables.VALID_API_KEY).when().get("/users/123123123").then().statusCode(404);
    }

    @Test
    public void testGetUser_authenticated() {
        given().header("Authorization", ITVariables.VALID_API_KEY).when().get("/users/1").then().statusCode(200);
    }

    @Test
    public void testCreateUser() throws Exception {

        final String email = "createuseremail@mytum.de";
        final String car = "BMW";
        final String firstName = "FirstName";
        final String lastName = "LastName";
        final UniversityDepartment department = UniversityDepartment.INFORMATICS;
        final String phoneNumber = "1234 / 123556";

        Map<String, Object> jsonAsMap = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        jsonAsMap.put("user", userMap);

        userMap.put("email", email);
        userMap.put("car", car);
        userMap.put("first_name", firstName);
        userMap.put("last_name", lastName);
        userMap.put("department", department);
        userMap.put("phone_number", phoneNumber);

        Response response = given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMap).with().contentType("application/json")
                .when().post("/users").then().statusCode(200).extract().response();

        // Test contents of database
        int userId = from(response.asString()).getInt("user.id");
        assertThat(userId, not(0));

        User user = userDao.load(userId);
        assertNotNull(user);

        assertEquals(email, user.getEmail());
        assertEquals(car, user.getCar());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(department, user.getDepartment());
        assertEquals(phoneNumber, user.getPhoneNumber());
    }

    @Test
    public void testCreateUser_notWhitelistedEmail() throws Exception {

        Map<String, Object> jsonAsMap = getValidUserMap();
        Map<String, Object> foo = (Map<String, Object>) jsonAsMap.get("user");

        foo.put("email", "test@test.de");

        given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMap).with().contentType("application/json")
                .when().post("/users").then().statusCode(400).contentType(ContentType.JSON)
                .body("errors", contains(startsWith("Not a valid mail address. Use one of these: ")));
    }

    @Test
    public void testGetAvailableDepartments() throws Exception {
        when().get("/users/departments").then().statusCode(200).contentType(ContentType.JSON).body("departments", hasSize(greaterThanOrEqualTo(1))).header(HttpHeaders.CACHE_CONTROL, not(empty()));
    }

    private Map<String, Object> getValidUserMap() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        jsonAsMap.put("user", userMap);

        userMap.put("email", "test");
        userMap.put("car", "BMW");
        userMap.put("first_name", "FirstName");
        userMap.put("last_name", "LastName");
        userMap.put("department", UniversityDepartment.INFORMATICS.name());
        userMap.put("phone_number", "1234 / 123556");

        return jsonAsMap;
    }

    @Test
    @Ignore
    public void testDeleteUser() throws Exception {
        //we want to delete user2..
        //in this user case user2 has a ride who is driver and ride 1 is approved (relationship table) and ride 2 is pending (requests table)
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("passenger_id",2);

        //first he joins a ride and is being approved
        Response response = given().header("Authorization","apikey2").body(jsonAsMap).with().contentType("application/json")
                .when().post("/rides/1/requests").then().statusCode(200).extract().response();

        Map<String, Object> jsonAsMapApprove = new HashMap<>();
        jsonAsMapApprove.put("passenger_id",2);
        jsonAsMapApprove.put("confirmed",true);

        response = given().header("Authorization", ITVariables.VALID_API_KEY).body(jsonAsMapApprove).with().contentType("application/json")
                .when().put("/rides/1/requests").then().statusCode(200).extract().response();

        //now it requests for ride 2 but his approval is pending...
        jsonAsMap = new HashMap<>();
        jsonAsMap.put("passenger_id",2);

        //first he joins a ride and is being approved
        response = given().header("Authorization","apikey2").body(jsonAsMap).with().contentType("application/json")
                .when().post("/rides/2/requests").then().statusCode(200).extract().response();

        //and now we delete user 2
        //first he joins a ride and is being approved
        response = given().header("Authorization","apikey2").contentType("application/json")
                .when().delete("/users/2").then().statusCode(200).extract().response();

    }


    @Test
    public void testAccessesToDeletedUser() throws Exception {
        //first check if user not deleted
        Response response = given().header("Authorization", "apikey4")
                .when().get("/users/4").then().statusCode(200).extract().response();

        //check getDeletedUserById
        response = given().header("Authorization", "apikey4")
                .when().get("/users/deleted/4").then().statusCode(404).extract().response();

        //delete user id 3
        response = given().header("Authorization", "apikey4")
                .when().delete("/users/4").then().statusCode(200).extract().response();

        //the rest are for checking not allowed access to deleted user
        response = given().header("Authorization", "apikey4")
                .when().get("/users/4").then().statusCode(401).extract().response();

        response = given().header("Authorization", "apikey4")
                .when().get("/users/4/requests").then().statusCode(401).extract().response();

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("ride_id",1);
        jsonAsMap.put("to_user_id",1);
        jsonAsMap.put("rating_type",0);
        response = given().header("Authorization","apikey4").body(jsonAsMap).with().contentType("application/json")
                .when().post("/users/4/ratings").then().statusCode(401).extract().response();


        jsonAsMap = new HashMap<>();
        Map<String, Object> rideMap = new HashMap<>();
        jsonAsMap.put("ride", rideMap);

        rideMap.put("driver", true);
        rideMap.put("is_ride_request",true);
        rideMap.put("departure_place","Munich, Germany");
        rideMap.put("destination", "Berlin, Germany");
        rideMap.put("ride_type", 0);
        rideMap.put("departure_time", "2015-11-27 09:40:00");
        rideMap.put("departure_latitude", 48.1351253);
        rideMap.put("departure_longitude", 11.5819806);
        rideMap.put("destination_latitude", 52.5200066);
        rideMap.put("destination_longitude", 13.404954);
        rideMap.put("free_seats", 2);
        rideMap.put("price", 0);
        rideMap.put("meeting_point", "Munich, Germany");
        rideMap.put("car", "string");
        rideMap.put("title", "string");

        given().header("Authorization", "apikey4").body(jsonAsMap).with().contentType("application/json")
                .when().post("/users/4/rides").then().statusCode(401);
    }
}