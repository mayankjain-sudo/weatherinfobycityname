package searchweatherbycitynameexample.mayank.com.weatherinfobycityname;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather extends AppCompatActivity {

    private static final String APP_ID = "4cf7fb9718cb99d5b93edcfeca3b74c4";

    private String cityname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        cityname = intent.getStringExtra(MainActivity.CITY_NAME);

        String city = cityname;

        String units = "imperial";
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
                city, APP_ID);

        TextView temp = (TextView) findViewById(R.id.textView);
        TextView humi =(TextView) findViewById(R.id.humi);
        TextView press =(TextView) findViewById(R.id.press);
        TextView cityname =(TextView) findViewById(R.id.cityname);
        TextView countryname =(TextView) findViewById(R.id.countryname);

        TextView textView = (TextView) findViewById(R.id.textView);
        new GetWeatherTask(cityname,countryname,temp,humi,press).execute(url);
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {
        private TextView temp,humi,press,cityname,countryname;
        String pressure,humidity,temperature,cname,coname;

        public GetWeatherTask(TextView cityname,TextView countryname,TextView temp,TextView humi,TextView press) {
            this.temp = temp;
            this.humi = humi;
            this.press = press;
            this.cityname = cityname;
            this.countryname = countryname;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject topLevel = new JSONObject(builder.toString());
                JSONObject main = topLevel.getJSONObject("main");
                JSONObject sys = topLevel.getJSONObject("sys");
                String name = topLevel.getString("name");

                temperature = String.valueOf(main.getDouble("temp"));
                pressure = String.valueOf(main.getDouble("pressure"));
                humidity = String.valueOf(main.getDouble("humidity"));
                cname = String.valueOf(topLevel.getString("name"));
                coname = String.valueOf(sys.getString("country"));

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            temp.setText("Current Temp: " + temperature + " °C");
            humi.setText("Humidity: " + humidity + "% ");
            press.setText("Pressure: "+ pressure);
            cityname.setText(cname +",");
            countryname.setText(coname);
        }
    }
}
