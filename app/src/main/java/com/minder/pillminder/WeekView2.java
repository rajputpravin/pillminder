package com.minder.pillminder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.minder.pillminder.utils.GenericCalendarUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeekView2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView patientView, recordDateView, statusView, amView, pmView, notesView;
    TextView Sunday_AM, Monday_AM, Tuesday_AM, Wednesday_AM, Thursday_AM, Friday_AM, Saturday_AM;
    TextView Sunday_PM, Monday_PM, Tuesday_PM, Wednesday_PM, Thursday_PM, Friday_PM, Saturday_PM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHttp(view);
                Snackbar.make(view, "Updating virtual pillbox...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout_week);
        layout.setAnchorPoint(0.55f);

        patientView = (TextView) findViewById(R.id.patient);
        recordDateView = (TextView) findViewById(R.id.recordDate);
        statusView = (TextView) findViewById(R.id.status);
        amView = (TextView) findViewById(R.id.am);
        pmView = (TextView) findViewById(R.id.pm);
        notesView = (TextView) findViewById(R.id.notes);

        Sunday_AM = (TextView) findViewById(R.id.Sunday_AM);
        Monday_AM = (TextView) findViewById(R.id.Monday_AM);
        Tuesday_AM = (TextView) findViewById(R.id.Tuesday_AM);
        Wednesday_AM = (TextView) findViewById(R.id.Wednesday_AM);
        Thursday_AM = (TextView) findViewById(R.id.Thursday_AM);
        Friday_AM = (TextView) findViewById(R.id.Friday_AM);
        Saturday_AM = (TextView) findViewById(R.id.Saturday_AM);

        Sunday_PM = (TextView) findViewById(R.id.Sunday_PM);
        Monday_PM = (TextView) findViewById(R.id.Monday_PM);
        Tuesday_PM = (TextView) findViewById(R.id.Tuesday_PM);
        Wednesday_PM = (TextView) findViewById(R.id.Wednesday_PM);
        Thursday_PM = (TextView) findViewById(R.id.Thursday_PM);
        Friday_PM = (TextView) findViewById(R.id.Friday_PM);
        Saturday_PM = (TextView) findViewById(R.id.Saturday_PM);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.week_view2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_week) {
            Toast.makeText(this, "Already opened", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_calendar) {
            startActivity(new Intent(WeekView2.this, CalendarView.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_call) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(WeekView2.this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getHttp(View v) {
        Object name = new WeekView2.OkHttpAync().execute(this, "get", "");
        //myText.setText(name.toString());
    }

    private class OkHttpAync extends AsyncTask<Object, Void, Object> {

        private Context contx;

        @Override
        protected Object doInBackground(Object... params) {
            contx = (Context) params[0];
            String requestType = (String) params[1];
            String requestParam = (String) params[2];

            if ("get".equals(requestType)) {
                return getHttpResponse();
            }
            return null;
        }
    }

    public String getHttpResponse() {
        try {
            OkHttpClient client = new OkHttpClient();

            String patientName = "Jack";
            Calendar cal = Calendar.getInstance();
            Date recordDate = new Date(cal.getTime().getTime());
            String week = GenericCalendarUtils.getWeek(recordDate);

            String todaysPatientDataUrl = "https://ap-south-1.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/pillminderrealm-gtzym/service/getDailyPatientData/incoming_webhook/webhook0?patientName=" + patientName + "&recordDate=" + recordDate;
            Request todaysPatientDataRequest = new Request.Builder().url(todaysPatientDataUrl).build();
            String weeklyPillBoxUrl = "https://ap-south-1.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/pillminderrealm-gtzym/service/getWeeklyPatientPillBox/incoming_webhook/webhook0?patientName=" + patientName + "&recordWeek=" + week;
            Request weeklyPillBoxRequest = new Request.Builder().url(weeklyPillBoxUrl).build();

            Response dailyPatientDataResponse;
            Response weeklyPillBoxResponse;

            try {
                final String patient, am, pm, note, recordDateOutput;
                final JSONArray daysJSONArray;
                final String[] daysArray;
                final TextView[] textViewArray = new TextView[14];

                dailyPatientDataResponse = client.newCall(todaysPatientDataRequest).execute();
                String dailyPatientData = dailyPatientDataResponse.body().string();
                JSONObject dailyPatientDataJsonObject = new JSONObject(dailyPatientData);

                //daily data
                patient = dailyPatientDataJsonObject.getString("patientName");
                recordDateOutput = dailyPatientDataJsonObject.getString("recordDate");
                am = dailyPatientDataJsonObject.getString("am");
                pm = dailyPatientDataJsonObject.getString("pm");
                note = dailyPatientDataJsonObject.getString("note");

                //weekly data
                weeklyPillBoxResponse = client.newCall(weeklyPillBoxRequest).execute();
                String weeklyPillBox = weeklyPillBoxResponse.body().string();
                JSONObject weeklyPillBoxJsonObject = new JSONObject(weeklyPillBox);

                daysJSONArray = weeklyPillBoxJsonObject.getJSONArray("alerts");
                int length = daysJSONArray.length();
                daysArray = new String[length];

                for (int i = 0; i < length; i++) {
                    daysArray[i] = daysJSONArray.getString(i);
                }

                textViewArray[0] = Sunday_AM;
                textViewArray[1] = Sunday_PM;
                textViewArray[2] = Monday_AM;
                textViewArray[3] = Monday_PM;
                textViewArray[4] = Tuesday_AM;
                textViewArray[5] = Tuesday_PM;
                textViewArray[6] = Wednesday_AM;
                textViewArray[7] = Wednesday_PM;
                textViewArray[8] = Thursday_AM;
                textViewArray[9] = Thursday_PM;
                textViewArray[10] = Friday_AM;
                textViewArray[11] = Friday_PM;
                textViewArray[12] = Saturday_AM;
                textViewArray[13] = Saturday_PM;

                // Output to activity
                WeekView2.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        patientView.setText("Patient: " + patient);
                        recordDateView.setText("Today: " + recordDateOutput);
                        amView.setText("AM: " + am);
                        pmView.setText("PM: " + pm);
                        notesView.setText("Note: " + note);

                        for (int j = 0; j < 14; j++) {
                            populateVirtualPillBox(daysArray[j], textViewArray[j]);
                        }

                        String s, status = "Good";
                        boolean b;
                        try {
                            for (int i = 0; i < 14; i++) {
                                s = daysJSONArray.getString(i);
                                if (s.equalsIgnoreCase("missed") || s.equalsIgnoreCase("overdosed")) {
                                    status = "Bad";
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (status.equalsIgnoreCase("Bad")) {
                                statusView.setText("Status: " + status);
                                statusView.setTextColor(getResources().getColor(R.color.textColor));
                            } else {
                                statusView.setText("Status: " + status);
                                statusView.setTextColor(getResources().getColor(R.color.greenTextColor));
                            }

                        }


                    }
                });
                return patient;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void populateVirtualPillBox(String val, TextView t) {
        switch (val) {
            case "taken":
                t.setBackgroundResource(R.drawable.greenbox);
                t.setText("");
                t.setTextSize(24);
                break;
            case "scheduled":
                t.setBackgroundResource(R.drawable.greenbox);
                t.setText("o");
                t.setTextSize(24);
                break;
            case "missed":
                t.setBackgroundResource(R.drawable.redbox);
                t.setText("o");
                t.setTextSize(24);
                break;
            case "overdose":
                t.setBackgroundResource(R.drawable.redbox);
                t.setText("");
                t.setTextSize(24);
                break;
            default:
                break;
        }
    }
}
