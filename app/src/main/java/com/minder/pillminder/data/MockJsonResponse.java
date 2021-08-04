package com.minder.pillminder.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MockJsonResponse {

    public static JSONObject getMockWeekViewData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("am", "Taken at 11:30 AM");
            jsonObject.put("pm", "Scheduled at 08:30 PM");
            jsonObject.put("note", "Doctor's advice. Take twice daily 8 hours apart.");
            jsonObject.put("patient", "Test");

            JSONArray alerts = new JSONArray();
            alerts.put(0, "taken");
            alerts.put(1, "missed");
            alerts.put(2, "scheduled");
            alerts.put(3, "taken");
            alerts.put(4, "taken");
            alerts.put(5, "scheduled");
            alerts.put(6, "overdose");
            alerts.put(7, "taken");
            alerts.put(8, "taken");
            alerts.put(9, "overdose");
            alerts.put(10, "taken");
            alerts.put(11, "missed");
            alerts.put(12, "taken");
            alerts.put(13, "taken");
            jsonObject.put("alerts", alerts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
