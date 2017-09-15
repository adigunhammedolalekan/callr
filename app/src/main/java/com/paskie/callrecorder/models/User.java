package com.paskie.callrecorder.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created By Adigun Hammed Olalekan
 * 7/12/2017.
 * Beem24, Inc
 */

public class User {

    public String ID = "";
    public String name = "";
    public String email = "";

    public static User parse(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.email = jsonObject.getString("email");
        user.ID = jsonObject.getString("id");
        user.name = jsonObject.getString("username");

        return user;
    }
}
