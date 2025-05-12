package com.example.healthup.domain;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.healthup.MainMenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class FetchWeatherForecast extends AsyncTask<String, Void, String[]>{
    private final String LOG_TAG = FetchWeatherForecast.class.getSimpleName();
    private double lat;
    private double lon;

    public FetchWeatherForecast(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedLow + "°C έως " + roundedHigh + "°C";
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray("list");

        String[] resultStrs = new String[numDays];
        for(int i = 0; i < weatherArray.length()-1; i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            //create a Gregorian Calendar, which is in current date
            GregorianCalendar gc = new GregorianCalendar();
            //add i dates to current date of calendar
            gc.add(GregorianCalendar.DATE, i);
            //get that date, format it, and "save" it on variable day
            Date time = gc.getTime();
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            day = shortenedDateFormat.format(time);

            // description is in a child array called "weather", which is 1 element long.
            description = dayForecast.getJSONArray("weather").getJSONObject(0).getString("description");

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            double high = dayForecast.getJSONObject("temp").getDouble("max");
            double low  = dayForecast.getJSONObject("temp").getDouble("min");

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        for (String s : resultStrs) {
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }
        Log.w("Results", String.valueOf(resultStrs));

        return resultStrs;

    }

    @Override
    protected String[] doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        if (params.length == 0) {
            return null;
        }

        int numDays = 7;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            //            String baseUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=38.04&lon=23.54&units=metric&cnt=7";
            String baseUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?lat="+lat+"&lon="+lon+"&units=metric&exclude=minutely,current,alerts,hourly";
            // Here insert your unique APPID
            String apiKey = "&APPID=612ce9a4c7726a3a0a00a69b84b9a01a";
            URL url = new URL(baseUrl.concat(apiKey));

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getWeatherDataFromJson(forecastJsonStr, numDays);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }


}
