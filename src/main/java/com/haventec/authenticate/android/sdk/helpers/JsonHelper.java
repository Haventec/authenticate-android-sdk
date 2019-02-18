package com.haventec.authenticate.android.sdk.helpers;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {

    public static String getJSONString(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getString(name);
        } catch ( JSONException je ) {
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getJSONObject(name);
        } catch ( JSONException je ) {
            return null;
        }
    }
}
