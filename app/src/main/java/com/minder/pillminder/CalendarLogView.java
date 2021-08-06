package com.minder.pillminder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarLogView extends AppCompatActivity {

    TextView patientView, recordDateView, amView, pmView, notesView;
    String na = "n/a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_log_view);
        patientView = (TextView) findViewById(R.id.patient);
        recordDateView = (TextView) findViewById(R.id.recordDate);
        amView = (TextView) findViewById(R.id.am);
        pmView = (TextView) findViewById(R.id.pm);
        notesView = (TextView) findViewById(R.id.notes);

        MaterialCalendarView calendarView = findViewById(R.id.calendarView1);

        String patientName = "Jack";

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (date.getYear() == 2021 && date.getMonth() == 8) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, date.getMonth());
                    cal.set(Calendar.YEAR, date.getYear());
                    cal.set(Calendar.DAY_OF_MONTH, date.getDay());
                    cal.add(Calendar.MONTH, -1);
                    Date date1 = new Date(cal.getTime().getTime());
                    String recordDate = dateFormat.format(date1);
                    String todaysPatientDataUrl = "https://ap-south-1.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/pillminderrealm-gtzym/service/getDailyPatientData/incoming_webhook/webhook0?patientName=" + patientName + "&recordDate=" + recordDate;

                    new CalendarLogView.OkHttpAync().execute(CalendarLogView.this.getApplicationContext(), "get", todaysPatientDataUrl);
                    Toast.makeText(CalendarLogView.this, "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CalendarLogView.this, "Not Available", Toast.LENGTH_SHORT).show();
                    new CalendarLogView.OkHttpAync().execute(CalendarLogView.this.getApplicationContext(), "na", na);
                }
            }
        });
    }

    public void viewWeek(View v) {
        startActivity(new Intent(CalendarLogView.this, WeekView.class));
    }

    public void logout(View v) {
        startActivity(new Intent(CalendarLogView.this, MainActivity.class));
    }

    public void getHttp(View v, String url) {
        Object name = new CalendarLogView.OkHttpAync().execute(this.getApplicationContext(), "get", url);
        //myText.setText(name.toString());
    }

    private class OkHttpAync extends AsyncTask<Object, Void, Object> {

        private Context contx;

        @Override
        protected Object doInBackground(Object... params) {
            contx = (Context) params[0];
            String requestType = (String) params[1];
            String url = (String) params[2];

            if ("get".equals(requestType)) {
                return getHttpResponse(url);
            }
            return null;
        }
    }

    public String getHttpResponse(String url) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response;
            try {
                response = client.newCall(request).execute();
                // This the the text obtained from GET request
                final String myResponse = response.body().string();
                final String patient, recordDate, am, pm, note;
                JSONObject jsonObject = new JSONObject(myResponse);
                // Values
                patient = jsonObject.getString("patientName");
                recordDate = jsonObject.getString("recordDate");
                am = jsonObject.getString("am");
                pm = jsonObject.getString("pm");
                note = jsonObject.getString("note");
                // Output to activity
                CalendarLogView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        patientView.setText("Patient: " + patient);
                        recordDateView.setText("Date: " + recordDate);
                        amView.setText("AM: " + am);
                        pmView.setText("PM: " + pm);
                        notesView.setText("Note: " + note);
                    }
                });
                return am;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
