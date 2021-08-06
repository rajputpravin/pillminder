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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView patientView, recordDateView, amView, pmView, notesView;
    String na = "n/a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Updating calendar logs...", Snackbar.LENGTH_LONG)
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
        navigationView.getMenu().getItem(1).setChecked(true);

        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout_calendar);
        layout.setAnchorPoint(0.4f);

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
                    new CalendarView.OkHttpAync().execute(CalendarView.this.getApplicationContext(), "get", todaysPatientDataUrl);
                    Toast.makeText(CalendarView.this, "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CalendarView.this, "Not Available", Toast.LENGTH_SHORT).show();
                    new CalendarView.OkHttpAync().execute(CalendarView.this.getApplicationContext(), "na", na);
                }
            }
        });
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
        getMenuInflater().inflate(R.menu.calendar_view, menu);
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
            startActivity(new Intent(CalendarView.this, WeekView2.class));
        } else if (id == R.id.nav_calendar) {
            Toast.makeText(this, "Already opened", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_call) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(CalendarView.this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getHttp(View v, String url) {
        Object name = new CalendarView.OkHttpAync().execute(this.getApplicationContext(), "get", url);
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
            } else if ("na".equals(requestType))
                return loadUnavailable();
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
                CalendarView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        patientView.setText("Patient: " + patient);
                        recordDateView.setText("Date: " + recordDate);
                        amView.setText("AM: " + am);
                        pmView.setText("PM: " + pm);
                        notesView.setText("Note: " + note);
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

    public String loadUnavailable() {
        try {
            // Output to activity
            CalendarView.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    patientView.setText("Patient: " + na);
                    recordDateView.setText("Date: " + na);
                    amView.setText("AM: " + na);
                    pmView.setText("PM: " + na);
                    notesView.setText("Note: " + na);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
