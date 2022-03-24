// Weather.java
// Инфорация о погоде за один день
package com.valsi.weathernews;

import android.os.Parcelable;
import android.os.Parcel;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

class Weather implements Parcelable{
   public final String dayOfWeek;
   public final String minTemp;
   public final String maxTemp;
   public final String dayTemp;
   public final String nightTemp;
   public final String pressure;
   public final String speed;
   public final String deg;
   public final String humidity;
   public final String description;
   public final String iconURL;

   // Конструктор
   public Weather(long timeStamp, double minTemp, double maxTemp, double dayTemp, double nightTemp, double pressure, double speed, double deg,
      double humidity, String description, String iconName) {
      // NumberFormat для форматирования температур в число
      NumberFormat numberFormat = NumberFormat.getInstance();
      numberFormat.setMaximumFractionDigits(0);
      this.dayOfWeek = convertTimeStampToDay(timeStamp);
      this.minTemp = numberFormat.format(minTemp) + "\u00B0C";
      this.maxTemp = numberFormat.format(maxTemp) + "\u00B0C";
      this.dayTemp = numberFormat.format(dayTemp) + "\u00B0C";
      this.nightTemp = numberFormat.format(nightTemp) + "\u00B0C";
      this.pressure = numberFormat.format(pressure*0.75) + "мм. рт. ст";
      this.speed = numberFormat.format(speed) + "М/сек";

      if (deg>315 || deg<= 45) {
         this.deg = "Север";
      }
      else
      if (deg>45 && deg<= 135) {
         this.deg = "Восток";
      }
      else
      if (deg>135 && deg<= 225) {
         this.deg = "Юг";
      }
      else
      if (deg>225 && deg<= 315) {
         this.deg = "Запад";
      }
      else this.deg = "Empty";

      this.humidity =
         NumberFormat.getPercentInstance().format(humidity / 100.0);
      this.description = description;
      this.iconURL =
         "http://openweathermap.org/img/w/" + iconName + ".png";
   }
// конструктор, считывающие данные из Парсел
   public Weather(Parcel in)
   {
      String[] data = new String[11];
      in.readStringArray(data);
      this.dayOfWeek = data[0];
      this.minTemp = data[1];
      this.maxTemp = data[2];
      this.dayTemp = data[3];
      this.nightTemp = data[4];
      this.pressure = data[5];
      this.speed = data[6];
      this.deg = data[7];
      this.humidity = data[8];
      this.description = data[9];
      this.iconURL = data[10];
   }
// создание экземпляра Weather и заполнение его данными из Parcel
   public static final Parcelable.Creator<Weather> CREATOR =new Parcelable.Creator<Weather>() {
      //распаковка объекта из Parcel
      @Override
      public Weather createFromParcel(Parcel parcel) {
         return new Weather(parcel);
      }

   @Override
   public Weather[] newArray(int size) {
      return new Weather[size];
   }

};


   @Override
   public int describeContents() {
      return 0;
   }

   //запись объекта в Parcel
   @Override
   public void writeToParcel(Parcel parcel, int i) {
      parcel.writeStringArray(new String[] {dayOfWeek,minTemp,maxTemp,dayTemp, nightTemp, pressure, speed, deg,
              humidity, description, iconURL});
   }


   // милисекунды в день недели
   private static String convertTimeStampToDay(long timeStamp) {
      Calendar calendar = Calendar.getInstance(); // создать Calendar
      calendar.setTimeInMillis(timeStamp * 1000); // получить время
      TimeZone tz = TimeZone.getDefault(); // опредетить часовой пояс

      // поправка на часовой пояс
      calendar.add(Calendar.MILLISECOND,
         tz.getOffset(calendar.getTimeInMillis()));

      // SimpleDateFormat возвращает день недели
      SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, dd.MM.yy");
      return dateFormatter.format(calendar.getTime());
   }
}

