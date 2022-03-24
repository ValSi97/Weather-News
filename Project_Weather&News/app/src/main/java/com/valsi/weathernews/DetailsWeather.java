package com.valsi.weathernews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailsWeather extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_weather_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView icon = (ImageView) findViewById(R.id.weather_icon);
        TextView date = (TextView) findViewById(R.id.date_text);
        TextView city  = (TextView) findViewById(R.id.city_textView);
        TextView descriptoin = (TextView) findViewById(R.id.description_text);
        TextView tday_temp = (TextView) findViewById(R.id.day_text);
        TextView tnight_temp = (TextView) findViewById(R.id.night_text);
        TextView tmin_temp = (TextView) findViewById(R.id.min_text);
        TextView tmax_temp = (TextView) findViewById(R.id.max_text);
        TextView thumidity = (TextView) findViewById(R.id.humidity_text);
        TextView tpressure = (TextView) findViewById(R.id.pressure_text);
        TextView tspeed = (TextView) findViewById(R.id.speed_text);
        TextView tdeg = (TextView) findViewById(R.id.deg_text);

        String day_temp = "";
        String night_temp = "";
        String min_temp = "";
        String max_temp = "";
        String humidity = "";
        String pressure = "";
        String speed = "";
        String deg = "";

        String city_name = (String)getIntent().getStringExtra("city_name");
        Weather weather = (Weather)getIntent().getParcelableExtra("list_item");


        String icon_URL = weather.iconURL;
        date.setText((String)weather.dayOfWeek);
        city.setText(city_name);
        descriptoin.setText((String)weather.description);
        day_temp = weather.dayTemp;
        night_temp = weather.nightTemp;
        min_temp = weather.minTemp;
        max_temp = weather.maxTemp;
        humidity = weather.humidity;
        pressure = weather.pressure;
        speed = weather.speed;
        deg = weather.deg;

        tday_temp.setText(R.string.daily_weather + day_temp);
        tnight_temp.setText(R.string.night_temp + night_temp);
        tmin_temp.setText("Мин.:" + min_temp);
        tmax_temp.setText("Макс.:" + max_temp);
        thumidity.setText("Влажность:" + humidity);
        tpressure.setText("Давление:" + pressure);
        tspeed.setText("Скорость ветра:" + speed);
        tdeg.setText("Направление:" + deg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
