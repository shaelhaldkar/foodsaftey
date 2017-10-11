package com.itgc.foodsafety.dao;

import org.json.JSONObject;

/**
 * Created by Farhan on 5/31/17.
 */

public class AuditJson
{
    private static JSONObject object;

    public static JSONObject getObject() {
        return object;
    }

    public static void setObject(JSONObject object) {
        AuditJson.object = object;
    }
}
