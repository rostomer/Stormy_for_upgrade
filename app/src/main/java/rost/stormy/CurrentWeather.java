package rost.stormy;

import android.content.res.Resources;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Asus on 03.09.2017.
 */

public class CurrentWeather {
    private String mIcon;
    private double mprecipChance;
    private String summary;
    private double mHumanity;
    private long mTime;
    private double mTemperature;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    private String mTimeZone;
    public String getmIcon() {
        return mIcon;
    }
    public int getIconId()
    {
        Resources resources = MainActivity.getContext().getResources();

        int iconId = R.drawable.clear_day;

        if (mIcon.equals(resources.getString(R.string.clear_day))) {
            summary = resources.getString(R.string.summury_clear_day);
            iconId = R.drawable.clear_day;
        }
        else if (mIcon.equals(resources.getString(R.string.clear_night))) {
            summary = resources.getString(R.string.summury_clear_night);
            iconId = R.drawable.clear_night;
        }
        else if (mIcon.equals(resources.getString(R.string.rain))) {
            summary = resources.getString(R.string.summury_rain);
            iconId = R.drawable.rain;
        }
        else if (mIcon.equals(resources.getString(R.string.snow))) {
            summary = resources.getString(R.string.summury_snow);
            iconId = R.drawable.snow;
        }
        else if (mIcon.equals(resources.getString(R.string.sleet))) {
            summary = resources.getString(R.string.summury_sleet);
            iconId = R.drawable.sleet;
        }
        else if (mIcon.equals(resources.getString(R.string.wind))) {
            summary = resources.getString(R.string.summury_wind);
            iconId = R.drawable.wind;
        }
        else if (mIcon.equals(resources.getString(R.string.fog))) {
            summary = resources.getString(R.string.summury_fog);
            iconId = R.drawable.fog;
        }
        else if (mIcon.equals(resources.getString(R.string.cloudy))) {
            summary = resources.getString(R.string.summury_cloudy);
            iconId = R.drawable.cloudy;
        }
        else if (mIcon.equals(resources.getString(R.string.partly_cloudy_day))) {
            summary = resources.getString(R.string.summury_partly_cloudy_day);
            iconId = R.drawable.partly_cloudy;
        }
        else if (mIcon.equals(resources.getString(R.string.partly_cloudy_night))) {
            summary = resources.getString(R.string.summury_partly_cloudy_night);
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
    public String getFormattedTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getmTime() *1000);
        return formatter.format(dateTime);
    }
    void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public long getmTime() {
        return mTime;
    }

    void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public int getmTemperature() {
        return (int)Math.round((mTemperature - 32)/2 + (mTemperature - 32)/20);
    }

    public void setmTemperature(double mTemperature) {
        this.mTemperature = mTemperature;
    }

    public double getmHumanity() {
        return mHumanity;
    }

    public void setmHumanity(double mHumanity) {
        this.mHumanity = mHumanity;
    }



    public int getMprecipChance() {
        double precipPercentage = mprecipChance * 100;
        return (int)Math.round(precipPercentage);
    }

    public void setMprecipChance(double mprecipChance) {
        this.mprecipChance = mprecipChance;
    }

    public String getSummary()
    {
        String translatedSummary;
        if(Locale.getDefault().getLanguage() == "ru")
        {
            translatedSummary = summary;
        }
         return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private String summaryTranslator(String summaryForTranslating)
    {

        return summaryForTranslating;
    }


}
