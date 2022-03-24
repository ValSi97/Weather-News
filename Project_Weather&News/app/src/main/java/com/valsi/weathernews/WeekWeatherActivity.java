
package com.valsi.weathernews;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WeekWeatherActivity extends AppCompatActivity {
   private String TAG = "Жизненный цикл";
   // список объектов Weather
   private ArrayList<Weather> weatherList = new ArrayList<>();

   // ArrayAdapter связывает объекты Weather с элементами ListView
   private WeekWeatherArrayAdapter weekWeatherArrayAdapter;
   private ListView weatherListView; // Для вывода инфы

   @Override
   public void onSaveInstanceState(Bundle savedInstanceState) {
      savedInstanceState.putParcelableArrayList("LIST", weatherList);
      super.onSaveInstanceState(savedInstanceState);
      Log.i(TAG,"onSaveInstanceState()");
   }

   @Override
   protected void onResume() {
      super.onResume();
      Log.i(TAG,"onResume()");
   }

   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {

      super.onRestoreInstanceState(savedInstanceState);
      Log.i(TAG,"onRestore()");
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      Log.i(TAG,"onDestroy()");
   }

   @Override
   public void onBackPressed() {
      super.onBackPressed();
   }

   @Override
   protected void onPause() {

      super.onPause();
      Log.i(TAG,"onPause()");
   }

   @Override
   protected void onStop() {
      super.onStop();
      Log.i(TAG,"onStop()");
   }

   @Override
   protected void onStart() {
      super.onStart();
      Log.i(TAG,"onStart()");
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_weekweather);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      weatherListView = (ListView) findViewById(R.id.weatherListView);
      final EditText locationEditText =
              (EditText) findViewById(R.id.locationEditText);
      if (savedInstanceState != null){
         weatherList = savedInstanceState.getParcelableArrayList("LIST");
         weekWeatherArrayAdapter = new WeekWeatherArrayAdapter(this, weatherList);
         weatherListView.setAdapter(weekWeatherArrayAdapter);
         weekWeatherArrayAdapter.notifyDataSetChanged(); // rebind to ListView
         weatherListView.smoothScrollToPosition(0); // scroll to top
      }

      // ArrayAdapter для связывания weatherList с weatherListView

      weekWeatherArrayAdapter = new WeekWeatherArrayAdapter(this, weatherList);
      weatherListView.setAdapter(weekWeatherArrayAdapter);
      weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (weatherList != null && weatherList.size() > 0) {

               Intent list_item_click = new Intent(WeekWeatherActivity.this, DetailsWeather.class);
               list_item_click.putExtra("city_name",locationEditText.getText().toString());
               list_item_click.putExtra("list_item", weatherList.get(i));

               startActivity(list_item_click);
            }
         }
      });
      // FAB скрывает клаву и выдает запрос веб-сервису
      FloatingActionButton fab =
         (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            String text = locationEditText.getText().toString();
            URL url = createURL(locationEditText.getText().toString());
            if (text.equals("")) {
               Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.empty_string, Snackbar.LENGTH_LONG).show();
            }
            else {
               // скрыть клаву и запустить GetWeatherTask для загрузки данных о погоде
               if (url != null && !text.equals("")) {
                  dismissKeyboard(locationEditText);
                  GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
                  getLocalWeatherTask.execute(url);
               } else {
                  Snackbar.make(findViewById(R.id.coordinatorLayout),
                          R.string.invalid_url, Snackbar.LENGTH_LONG).show();
               }
            }
         }
      });
      Log.i(TAG,"onCreate()");
   }

   // закрытие клавы при нажатии tab
   private void dismissKeyboard(View view) {
      InputMethodManager imm = (InputMethodManager) getSystemService(
         Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
   }

   // create openweathermap.org web service URL using city
   private URL createURL(String city) {
      String apiKey = getString(R.string.api_key);
      String baseUrl = getString(R.string.web_service_url);

      try {
         // create URL for specified city and imperial units (Fahrenheit)
         String urlString = baseUrl + URLEncoder.encode(city, "UTF-8") +
            "&units=Metric&cnt=8&APPID=" + apiKey;
         return new URL(urlString);
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return null; // URL was malformed
   }

   // makes the REST web service call to get weather data and
   // saves the data to a local HTML file
   private class GetWeatherTask
      extends AsyncTask<URL, Void, JSONObject> {

      @Override
      protected JSONObject doInBackground(URL... params) {
         HttpURLConnection connection = null;

         try {
            connection = (HttpURLConnection) params[0].openConnection();
            int response = connection.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
               StringBuilder builder = new StringBuilder();

               try (BufferedReader reader = new BufferedReader(
                  new InputStreamReader(connection.getInputStream()))) {

                  String line;

                  while ((line = reader.readLine()) != null) {
                     builder.append(line);
                  }
               }
               catch (IOException e) {
                  Snackbar.make(findViewById(R.id.coordinatorLayout),
                     R.string.read_error, Snackbar.LENGTH_LONG).show();
                  e.printStackTrace();
               }

               return new JSONObject(builder.toString());
            }
            else {
               Snackbar.make(findViewById(R.id.coordinatorLayout),
                  R.string.connect_error, Snackbar.LENGTH_LONG).show();
            }
         }
         catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout),
               R.string.connect_error, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
         }
         finally {
            connection.disconnect(); // close the HttpURLConnection
         }

         return null;
      }

      // process JSON response and update ListView
      @Override
      protected void onPostExecute(JSONObject weather) {
         convertJSONtoArrayList(weather); // repopulate weatherList
         weekWeatherArrayAdapter.notifyDataSetChanged(); // rebind to ListView
         weatherListView.smoothScrollToPosition(0); // scroll to top
      }
   }

   // create Weather objects from JSONObject containing the forecast
   private void convertJSONtoArrayList(JSONObject forecast) {
      weatherList.clear(); // clear old weather data

      try {
         // get forecast's "list" JSONArray
         JSONArray list = forecast.getJSONArray("list");

         // convert each element of list to a Weather object
         for (int i = 1; i < list.length(); ++i) {
            JSONObject day = list.getJSONObject(i); // get one day's data

            // get the day's temperatures ("temp") JSONObject
            JSONObject temperatures = day.getJSONObject("temp");

            // get day's "weather" JSONObject for the description and icon
            JSONObject weather =
               day.getJSONArray("weather").getJSONObject(0);

            // add new Weather object to weatherList
            weatherList.add(new Weather(
               day.getLong("dt"), // date/time timestamp
               temperatures.getDouble("min"), // minimum temperature
               temperatures.getDouble("max"), // maximum temperature
                    temperatures.getDouble("day"),
                    temperatures.getDouble("night"),
                    day.getDouble("pressure"),
                    day.getDouble("speed"),
                   day.getDouble("deg"),
               day.getDouble("humidity"), // percent humidity
               weather.getString("description"), // weather conditions
               weather.getString("icon"))); // icon name

         }
      }
      catch (JSONException e) {
         e.printStackTrace();
      }
      catch(NullPointerException e){
         Toast.makeText(getApplicationContext(), "Ошибка, такого города нет!", Toast.LENGTH_SHORT).show();
      }
   }
}

