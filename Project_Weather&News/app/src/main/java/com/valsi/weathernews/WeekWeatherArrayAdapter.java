// WeatherArrayAdapter.java
// для отображения List<Weather> в ListView
package com.valsi.weathernews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class WeekWeatherArrayAdapter extends ArrayAdapter<Weather> {
   // класс для повторного исп-я представлений списка при прокрутке
   private static class ViewHolder {
      ImageView conditionImageView;
      TextView dayTextView;
      TextView dailyTextView;
      TextView nightlyTextView;

   }

   // Кэш для уже загруженных Битмапов
   private Map<String, Bitmap> bitmaps = new HashMap<>();

   // конструктор для инициализации унаследованных членов суперкласса
   public WeekWeatherArrayAdapter(Context context, List<Weather> forecast) {
      super(context, -1, forecast);
   }

   // создание представлений для элементов ListView
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      // олучение объекта Weather для заданной позиции ListView
      Weather day = getItem(position);

      ViewHolder viewHolder; // Объект, содержащий ссылки на представления элемента списка

      // проверить возможность повторного использования ViewHolder
      // для элемента, вышедшего за границы экрана
      if (convertView == null) { // объекта ViewHolder нет, нужно создать его
         viewHolder = new ViewHolder();
         LayoutInflater inflater = LayoutInflater.from(getContext());
         convertView =
            inflater.inflate(R.layout.list_item_weather, parent, false);
         viewHolder.conditionImageView =
            (ImageView) convertView.findViewById(R.id.conditionImageView);
         viewHolder.dayTextView =
            (TextView) convertView.findViewById(R.id.dayTextView);
         viewHolder.dailyTextView =
            (TextView) convertView.findViewById(R.id.minTextView);
         viewHolder.nightlyTextView =
            (TextView) convertView.findViewById(R.id.maxTextView);
         convertView.setTag(viewHolder);
      }
      else { // существующий ViewHolder используется заного
         viewHolder = (ViewHolder) convertView.getTag();
      }

      // если картинка погодных условий загружена, использовать ее
      // иначе загрузить в другом потоке
      if (bitmaps.containsKey(day.iconURL)) {
         viewHolder.conditionImageView.setImageBitmap(
            bitmaps.get(day.iconURL));
      }
      else {
         // загрузить и вывести картинку погодных условий
         new LoadImageTask(viewHolder.conditionImageView).execute(
            day.iconURL);
      }

      // получить данные из объекта Weather и заполнить представления
      Context context = getContext(); // для загрузки строковых ресурсов
      viewHolder.dayTextView.setText(context.getString(
         R.string.day_description, day.dayOfWeek,"\n", day.description));
      viewHolder.dailyTextView.setText(
         context.getString(R.string.day_temp, day.minTemp));
      viewHolder.nightlyTextView.setText(
         context.getString(R.string.night_temp, day.maxTemp));

      return convertView; // Вернуть готовое представление элемента
   }

   // AsyncTask для загрузки изображения в отдельно потоке
   private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
      private ImageView imageView; // для вывода миниатюры

      // сохранение ImageView для загруженного объекта Bitmap
      public LoadImageTask(ImageView imageView) {
         this.imageView = imageView;
      }

      // загрузить изображение; params[0] содержит URL изображения
      @Override
      protected Bitmap doInBackground(String... params) {
         Bitmap bitmap = null;
         HttpURLConnection connection = null;

         try {
            URL url = new URL(params[0]); // Задаем URL для изображения

            // открыть HttpURLConnection, получить InputStream
            // и загрузить картинку
            connection = (HttpURLConnection) url.openConnection();

            try (InputStream inputStream = connection.getInputStream()) {
               bitmap = BitmapFactory.decodeStream(inputStream);
               bitmaps.put(params[0], bitmap); // кэширование
            }
            catch (Exception e) {
               e.printStackTrace();
            }
         }
         catch (Exception e) {
            e.printStackTrace();
         }
         finally {
            connection.disconnect(); // закрыть HttpURLConnection
         }

         return bitmap;
      }

      // связать значок погодных услови с элементом списка
      @Override
      protected void onPostExecute(Bitmap bitmap) {
         imageView.setImageBitmap(bitmap);
      }
   }
}


