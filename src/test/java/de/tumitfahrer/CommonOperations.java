package de.tumitfahrer;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.*;


public class CommonOperations {
    public static final Operation DELETE_ALL =
            deleteAllFrom("NOTIFICATIONS","RELATIONSHIPS","REQUESTS", "MESSAGES", "CONV_PARTICIPANTS", "CONVERSATIONS", "RIDES", "RIDE_SEARCHES", "USERS");

    public static final Operation INSERT_USER =
            insertInto("USERS").columns("id", "api_key", "api_key_expires", "enabled", "email", "password", "salt")
                    .values(1, ITVariables.VALID_API_KEY, "2020-01-01", true, ITVariables.VALID_EMAIL, "2720f70c4261d025bf3aa8d141fb069f13a54c21001b0ef97a3c7f694ae35e98107b061129bf6bf69bc6dc09410650fbfe068dc1999079d28ba23ea63920434e", "yL5dmCDl")
                    .build();

    public static final Operation INSERT_RIDES =
            insertInto("RIDES")
                    .columns("id", "departure_place", "destination", "departure_time", "free_seats", "user_id", "ride_type")
                    .values(1, "Garching, Forschungszentrum, Garching, Germany", "Neuperlach Süd, Munich, Germany", "2020-05-09", 1, 1, 0)
                    .build();

    public static final Operation INSERT_RIDE_RELATIONSHIP =
            insertInto("RELATIONSHIPS")
                    .columns("id","user_id", "ride_id", "is_driving")
                    .values(1, 1, 1, true)
                    .build();

    public static final Operation INSERT_RIDE_CONVERSATION =
            insertInto("CONVERSATIONS")
                    .columns("id","ride_id", "created_at", "updated_at", "user_id", "deleted_at")
                    .values(1, 1, "2020-05-09", "2020-05-09", 1, null)
                    .build();


    public static final Operation INSERT_RIDE_CONV_PARTICIPANTS =
            insertInto("CONV_PARTICIPANTS")
            .columns("id","conv_id", "user_id", "created_at", "updated_at")
            .values(1, 1, 1,"2020-05-09", "2020-05-09")
            .build();


}
