package rost.stormy.Weather;

import android.content.res.Resources;

import rost.stormy.MainActivity;
import rost.stormy.R;

/**
 * Created by Asus on 18.01.2018.
 */

public class Forecast {
    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }
    public static int getIconId(String IconString) {
        Resources resources = MainActivity.getContext().getResources();

        int iconId = R.drawable.clear_day;

        if (IconString.equals(resources.getString(R.string.clear_day))) {

            iconId = R.drawable.clear_day;
        }
        else if (IconString.equals(resources.getString(R.string.clear_night))) {

            iconId = R.drawable.clear_night;
        }
        else if (IconString.equals(resources.getString(R.string.rain))) {

            iconId = R.drawable.rain;
        }
        else if (IconString.equals(resources.getString(R.string.snow))) {

            iconId = R.drawable.snow;
        }
        else if (IconString.equals(resources.getString(R.string.sleet))) {

            iconId = R.drawable.sleet;
        }
        else if (IconString.equals(resources.getString(R.string.wind))) {

            iconId = R.drawable.wind;
        }
        else if (IconString.equals(resources.getString(R.string.fog))) {

            iconId = R.drawable.fog;
        }
        else if (IconString.equals(resources.getString(R.string.cloudy))) {

            iconId = R.drawable.cloudy;
        }
        else if (IconString.equals(resources.getString(R.string.partly_cloudy_day))) {

            iconId = R.drawable.partly_cloudy;
        }
        else if (IconString.equals(resources.getString(R.string.partly_cloudy_night))) {

            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
}
