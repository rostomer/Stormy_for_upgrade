package rost.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 37.3737;
        final double longitude = -122.122;


        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);
            }
        });

        getForecast(latitude,longitude);
    }

    private void getForecast(double latitude, double longitude) {
        String forecastUrl = getString(R.string.forecast_URL)
                + getString(R.string.api_key) +
                "/" + latitude + ","
                + longitude;
        if (isNetworkAvailable())
        {
            toggleRefresh();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url(forecastUrl).
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
        ;
        temperatureLabel.setText((mCurrentWeather.getmTemperature() +""));
        timeLabel.setText("At " + mCurrentWeather.getFormattedTime() +" it will be");
        humidityValue.setText(mCurrentWeather.getmHumanity() +"");
        precipeValue.setText(mCurrentWeather.getMprecipChance() +"%");
        statusLabel.setText(mCurrentWeather.getSummary());

        Drawable drawable = ContextCompat.getDrawable(this, mCurrentWeather.getIconId());
        iconImageView.setImageDrawable(drawable);
        Log.d(TAG, mCurrentWeather.getmTemperature()+"Temperature");
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws  JSONException {
            JSONObject forecast = new JSONObject(jsonData);
        //тут местоположение считывается, нужно просмотреть апи, чтобы понять, в какой строке его искать.
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
