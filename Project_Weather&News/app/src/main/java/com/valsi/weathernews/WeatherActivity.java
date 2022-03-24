package com.valsi.weathernews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button dailyButton = (Button) findViewById(R.id.dailyButton);
        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(WeatherActivity.this,DayWeatherActivity.class);
                startActivity(intentLoadNewActivity);
            }
        });
        Button weekButton = (Button) findViewById(R.id.weekButton);
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(WeatherActivity.this,WeekWeatherActivity.class);
                startActivity(intentLoadNewActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_activity, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_daily:
                startActivity(new Intent(this, DayWeatherActivity.class));
                return true;
            case R.id.action_week:
                startActivity(new Intent(this, WeekWeatherActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "Выберите, какой тип прогноза вас интересует", Toast.LENGTH_LONG).show();
    }
}

