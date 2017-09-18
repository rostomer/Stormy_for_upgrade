package rost.stormy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Address;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;

     @BindView(R.id.temperatureLabel) TextView temperatureLabel;
     @BindView(R.id.timeLabel) TextView timeLabel;
     @BindView(R.id.humidityValue) TextView humidityValue;
     @BindView(R.id.LocationLabel) TextView locationLabel;
     @BindView(R.id.precipeValue) TextView precipeValue;
     @BindView(R.id.statusLabel) TextView statusLabel;

     @BindView(R.id.iconImageView) ImageView iconImageView;
    @BindView(R.id.refreshImageView) ImageView refreshImageView;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    GPSTracker gps;
    private double latitude;
    private double longitude;
    private Geocoder mGeocoder;
    private String city;
    private static Context context;
    private String ForecastUrl;
    private String CurrentLocalisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
      //  double minskLatitude = 53.89;
      //  double minskLongitude = 27.59;
        mProgressBar.setVisibility(View.INVISIBLE);
        gps = new GPSTracker(MainActivity.this);
        mGeocoder = new Geocoder(this, Locale.getDefault());
        if(gps.canGetLocation) {
             latitude = gps.getLatitude();
             longitude = gps.getLongitude();
          //  latitude = minskLatitude;
          //  longitude = minskLongitude;
        }

              city = gps.findAddressFromLatLng( mGeocoder,latitude,longitude);
             Log.d(TAG, city  + " it's your information from geocoder.");

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);

            }
        });

        getForecast(latitude,longitude);
    }

    private void getForecast(double latitude, double longitude) {
        //Попытаться добавить этот кусочек в запрос.
        CurrentLocalisation = Locale.getDefault().getLanguage();
        if(CurrentLocalisation.equals("en")) {
            ForecastUrl = getString(R.string.forecast_URL)
                    + getString(R.string.api_key) +
                    "/" + latitude + ","
                    + longitude + "?lang=en&units=si";
        }
        else if(CurrentLocalisation.equals("ru"))
        {
            ForecastUrl = getString(R.string.forecast_URL)
                    + getString(R.string.api_key) +
                    "/" + latitude + ","
                    + longitude + "?lang=ru&units=si";
        }
        else
        {
            ForecastUrl = getString(R.string.forecast_URL)
                    + getString(R.string.api_key) +
                    "/" + latitude + ","
                    + longitude + "?units=si";
        }
        if (isNetworkAvailable())
        {
            toggleRefresh();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url(ForecastUrl).
                build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleRefresh();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    String jsonData = response.body().string();
                    if(response.isSuccessful())
                    {
                        mCurrentWeather = getCurrentDetails(jsonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();
                            }
                        });
                    }
                    else
                        {
                            alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception caught:",e);
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Exception caught:",e);
                }
            }
            });
        Log.d(TAG, "Main UI code is running");
        }
        else{
           AlertNetworkConnectionDialogFragment dialog = new AlertNetworkConnectionDialogFragment();
            dialog.show(getFragmentManager(), "network_error_dialog");
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        locationLabel.setText(city);
        temperatureLabel.setText((mCurrentWeather.getmTemperature() +""));
        timeLabel.setText(mCurrentWeather.getFormattedTime() + getString(R.string.for_time) + "");
        humidityValue.setText(mCurrentWeather.getmHumanity() +"");
        precipeValue.setText(mCurrentWeather.getMprecipChance() +"%");
        statusLabel.setText(mCurrentWeather.getSummary());

        Drawable drawable = ContextCompat.getDrawable(this, mCurrentWeather.getIconId());
        iconImageView.setImageDrawable(drawable);
        Log.d(TAG, mCurrentWeather.getmTemperature()+"Temperature");
    }
    public static Context getContext()
    {
        return  context;
    }
    private CurrentWeather getCurrentDetails(String jsonData) throws  JSONException {
            JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);
        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setmHumanity(currently.getDouble("humidity"));
        currentWeather.setmTime(currently.getLong("time"));
        currentWeather.setmIcon(currently.getString("icon"));
        currentWeather.setMprecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setmTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);

        Log.d(TAG, currentWeather.getFormattedTime());
        return  currentWeather;

    }

  //  @Nullable



    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvaliable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvaliable = true;
        }
        return isAvaliable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
