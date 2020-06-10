package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.ScatteringByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    EditText name;
    Button btn;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
    public static final String Shared_pref="SharedPref";

    public static final String cityname="cityname";
    public static final String address1="address";
    public static final String description1="Description";
    public static final String temperature1="temperature";
    public static final String pressure1="pressure";
    public static final String windspeed1="windspeed";
    public static final String sunrise2="sunrise";
    public static final String sunset2="sunset";
    public static final String humidity1="humidity";
    private String address2,description2,temperature2,pressure2,windspeed2,sunrise3,sunset3,humidity2,cityname2;


    class WeatherInfo extends AsyncTask<String, Void ,String>
    {
        @Override
        protected String doInBackground(String... param)
        {
            try{
                URL url=new URL(param[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(is);
                int data=reader.read();
                String apidetails="";
                char current;
                while(data!=-1)
                {
                    current=(char)data;
                    apidetails+=current;
                    data=reader.read();
                }
                return apidetails;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void GetWeatherInfo(View view)
    {
        WeatherInfo weatherInfo = new WeatherInfo();
        try{

            String weatherapidetails=weatherInfo.execute("https://api.openweathermap.org/data/2.5/weather?q="+name.getText().toString()+"&appid=8e66ceb6672cdc44a5d16e2e55fc75f1").get();
            Log.i("Weather Info ",weatherapidetails );
            JSONObject jsonObj= new JSONObject(weatherapidetails);
            JSONObject main = jsonObj.getJSONObject("main");
            JSONObject sys = jsonObj.getJSONObject("sys");
            JSONObject wind = jsonObj.getJSONObject("wind");
            JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

            Long updatedAt = jsonObj.getLong("dt");
            String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
            String temp = (main.getString("temp"));
            double num=Math.round(Double.parseDouble(temp)-273);
            temp=String.valueOf(  num  )+"°C";
            String pressure = main.getString("pressure")+" hPa";
            String humidity = main.getString("humidity")+"%";


            Long sunrise = sys.getLong("sunrise");
            String sunrise1 =new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000));
            sunrise1=sunrise1.substring(11,sunrise1.length());
            Long sunset = sys.getLong("sunset");
            String sunset1 = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000));
            sunset1=sunset1.substring(11,sunset1.length());
            String windSpeed = wind.getString("speed")+" m/s";
            String weatherDescription = weather.getString("description");
            StringBuilder description=new StringBuilder(weatherDescription);
            description.setCharAt(0,Character.toUpperCase(weatherDescription.charAt(0)));



            String address = jsonObj.getString("name") + ", " + sys.getString("country");



            tv1.setText(address);
            tv2.setText(description);
            tv3.setText("Temperature:  "+temp);
            tv4.setText("Pressure:  "+pressure);
            tv5.setText("Wind Speed:  "+windSpeed);
            tv6.setText("Sunrise:  "+sunrise1);
            tv7.setText("Sunset:  "+sunset1);
            tv8.setText("Humidity:  "+humidity);

            saveData();
            //updateView();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         name=(EditText)findViewById(R.id.et_cityname);
         btn=(Button)findViewById(R.id.btn);
         tv1=(TextView)findViewById(R.id.tv1);
         tv2=(TextView)findViewById(R.id.tv2);
         tv3=(TextView)findViewById(R.id.tv3);
         tv4=(TextView)findViewById(R.id.tv4);
         tv5=(TextView)findViewById(R.id.tv5);
         tv6=(TextView)findViewById(R.id.tv6);
         tv7=(TextView)findViewById(R.id.tv7);
         tv8=(TextView)findViewById(R.id.tv8);
         loadData();
         updateView();

}

    public void saveData(){
        SharedPreferences sharedPreferences=getSharedPreferences(Shared_pref,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(cityname,name.getText().toString());
        editor.putString(address1,tv1.getText().toString());
        editor.putString(description1,tv2.getText().toString());
        editor.putString(temperature1,tv3.getText().toString());
        editor.putString(pressure1,tv4.getText().toString());
        editor.putString(windspeed1,tv5.getText().toString());
        editor.putString(sunrise2,tv6.getText().toString());
        editor.putString(sunset2,tv7.getText().toString());
        editor.putString(humidity1,tv8.getText().toString());
        editor.apply();
}
    public void loadData(){
        SharedPreferences sharedPreferences=getSharedPreferences(Shared_pref,MODE_PRIVATE);
        cityname2=sharedPreferences.getString(cityname,"");
        address2=sharedPreferences.getString(address1,"");
        description2=sharedPreferences.getString(description1,"");
        temperature2=sharedPreferences.getString(temperature1,"");
        pressure2=sharedPreferences.getString(pressure1,"");
        windspeed2=sharedPreferences.getString(windspeed1,"");
        sunrise3=sharedPreferences.getString(sunrise2,"");
        sunset3=sharedPreferences.getString(sunset2,"");
        humidity2=sharedPreferences.getString(humidity1,"");
    }
    public void updateView(){

        name.setText(cityname2);
        tv1.setText(address2);
        tv2.setText(description2);
        tv3.setText(temperature2);
        tv4.setText(pressure2);
        tv5.setText(windspeed2);
        tv6.setText(sunrise3);
        tv7.setText(sunset3);
        tv8.setText(humidity2);
    }
}

