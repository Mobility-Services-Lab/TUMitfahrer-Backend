package de.tumitfahrer.controller;

import com.jayway.restassured.response.Response;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@WebAppConfiguration
public class SearchesControllerIT {

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {

        Calendar cal1 = Calendar.getInstance();
        cal1.set(2015, Calendar.OCTOBER, 9, 13, 50);

        Calendar cal2 = Calendar.getInstance();
        cal2.set(2015, Calendar.OCTOBER, 9, 14, 00);

        Operation operations = sequenceOf(
                CommonOperations.DELETE_ALL,
                CommonOperations.INSERT_USER,
                insertInto("RIDES")
                        .columns("id", "user_id", "departure_time", "departure_place", "destination", "departure_latitude", "departure_longitude", "destination_latitude", "destination_longitude", "ride_type", "free_seats")
                        .values(100, ITVariables.VALID_USER_ID, cal1, "departure_place", "destination", 48.264671, 11.671391, 48.264671, 11.671391, 0, 3)
                        .values(200, ITVariables.VALID_USER_ID, cal2, "departure_place", "destination", 48.264671, 11.671391, 48.264671, 11.671391, 0, 3)
                        .build(),
                insertInto("Conversations")
                        .columns("id","ride_id", "created_at", "updated_at", "user_id", "deleted_at")
                        .values(1, 100, "2020-05-09", "2020-05-09", ITVariables.VALID_USER_ID, null)
                        .build(),
                insertInto("Conversations")
                        .columns("id","ride_id", "created_at", "updated_at", "user_id", "deleted_at")
                        .values(2,200, "2020-05-09", "2020-05-09", ITVariables.VALID_USER_ID, null)
                        .build()
        );

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operations);
        dbSetup.launch();
    }

    @Test
    public void testGetSearchResult_singleResult() throws Exception {

        Map<String, Object> searchJson = new HashMap<>();

        searchJson.put("departure_place", "Garching, Forschungszentrum, Garching, Germany");
        searchJson.put("departure_latitude", 48.264671);
        searchJson.put("departure_longitude", 11.671391);

        searchJson.put("ride_type", "0");
        searchJson.put("departure_time", "2015-10-09 14:00");

        searchJson.put("destination", "Neuperlach Süd, Munich, Germany");
        searchJson.put("destination_latitude", 48.264671);
        searchJson.put("destination_longitude", 11.671391);

        searchJson.put("destination_threshold", 1.0);
        searchJson.put("departure_place_threshold", 1.0);

        Response response = given().header("Authorization", ITVariables.VALID_API_KEY).body(searchJson)
                .with().contentType("application/json").when().post("/search").then().statusCode(200).extract().response();

        List jsonResponse = from(response.asString()).getList("$");
        assertThat(jsonResponse.size(), is(1));
    }

    @Test
    public void testGetSearchResult_bothResults() throws Exception {

        Map<String, Object> searchJson = new HashMap<>();

        searchJson.put("departure_place", "Garching, Forschungszentrum, Garching, Germany");
        searchJson.put("departure_latitude", 48.264671);
        searchJson.put("departure_longitude", 11.671391);

        searchJson.put("ride_type", "0");
        searchJson.put("departure_time", "2015-10-09 14:00");

        searchJson.put("destination", "Neuperlach Süd, Munich, Germany");
        searchJson.put("destination_latitude", 48.264671);
        searchJson.put("destination_longitude", 11.671391);

        searchJson.put("destination_threshold", 1.0);
        searchJson.put("departure_place_threshold", 1.0);

        searchJson.put("departure_time_offset_before", 10);
        searchJson.put("departure_time_offset_after", 1440);

        Response response = given().header("Authorization", ITVariables.VALID_API_KEY).body(searchJson)
                .with().contentType("application/json").when().post("/search").then().statusCode(200).extract().response();

        List jsonResponse = from(response.asString()).getList("$");
        assertThat(jsonResponse.size(), is(2));
    }

    @Test
    public void testGetSearchResult_noResults() throws Exception {

        Map<String, Object> searchJson = new HashMap<>();

        searchJson.put("departure_place", "Garching, Forschungszentrum, Garching, Germany");
        searchJson.put("departure_latitude", 48.264671);
        searchJson.put("departure_longitude", 11.671391);

        searchJson.put("ride_type", "0");
        searchJson.put("departure_time", "2015-10-09 13:55");

        searchJson.put("destination", "Neuperlach Süd, Munich, Germany");
        searchJson.put("destination_latitude", 48.264671);
        searchJson.put("destination_longitude", 11.671391);

        searchJson.put("destination_threshold", 1.0);
        searchJson.put("departure_place_threshold", 1.0);

        searchJson.put("departure_time_offset_before", 4);
        searchJson.put("departure_time_offset_after", 4);

        Response response = given().header("Authorization", ITVariables.VALID_API_KEY).body(searchJson)
                .with().contentType("application/json").when().post("/search").then().statusCode(200).extract().response();

        List jsonResponse = from(response.asString()).getList("$");
        assertThat(jsonResponse.size(), is(0));
    }
}