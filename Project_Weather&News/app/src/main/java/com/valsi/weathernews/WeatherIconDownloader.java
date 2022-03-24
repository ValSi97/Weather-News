package com.valsi.weathernews;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WeatherIconDownloader extends AsyncTask<String, Void, Bitmap> {
    public ImageView imageView; // для вывода миниатюры
    public Map<String, Bitmap> bitmaps = new HashMap<>();

    // сохранение ImageView для загруженного объекта Bitmap
    public WeatherIconDownloader(ImageView imageView,HashMap bitmaps) {
        this.imageView = imageView;
        this.bitmaps = bitmaps;
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
