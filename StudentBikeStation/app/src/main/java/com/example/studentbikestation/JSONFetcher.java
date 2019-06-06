package com.example.studentbikestation;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class JsonFetcher extends AsyncTask<Void, Void, Void> {

    String data = "";

    String temp;
    String weather;
    String weatherdesc;
    String city;
    String icon;
    String iconUrl;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=55.86066&lon=9.85034&units=metric&APPID=e2d1b64d7a7338dd389dfff6a7fbc17b");
            HttpURLConnection URLconnexion = (HttpURLConnection) url.openConnection();
            InputStream inputStream = URLconnexion.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data= data + line;
            }
            JSONObject JO = new JSONObject(data);
            JSONObject mainJO = JO.getJSONObject("main");
            JSONArray weatherJA = JO.getJSONArray("weather");


            Log.d("mainJO obtained",mainJO.toString());
            Log.d("weatherJA obtained",weatherJA.toString());

            city = JO.getString("name");
            temp = mainJO.getString("temp")+"°C";
            weather = weatherJA.getJSONObject(0).getString("main");
            weatherdesc = weatherJA.getJSONObject(0).getString("description");
            icon = weatherJA.getJSONObject(0).getString("icon");
            iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
            Log.d("data from main", data);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        WeatherActivity.tempView.setText(this.temp);
        WeatherActivity.cityView.setText(this.city);
        WeatherActivity.weatherView.setText(this.weather);
        WeatherActivity.descView.setText(this.weatherdesc);
        Picasso.with(WeatherActivity.tempView.getContext()).load(iconUrl).resize(200,200)
                .into(WeatherActivity.weatherImageView);
        WeatherActivity.weatherImageView.setVisibility(View.VISIBLE);
        WeatherActivity.cityView.setVisibility(View.VISIBLE);
        WeatherActivity.tempView.setVisibility(View.VISIBLE);
        WeatherActivity.weatherView.setVisibility(View.VISIBLE);
        WeatherActivity.descView.setVisibility(View.VISIBLE);


    }
}