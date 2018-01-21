package rost.stormy.Weather;

import android.content.res.Resources;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import rost.stormy.MainActivity;
import rost.stormy.R;

/**
 * Created by Asus on 03.09.2017.
 */

public class Current {
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
        return  Forecast.getIconId(mIcon);
    }
    public String getFormattedTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getmTime() *1000);
        return formatter.format(dateTime);
    }
   public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public long getmTime() {
        return mTime;
    }

   public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public int getmTemperature() {
        //return (int)Math.round((mTemperature - 32) + (mTemperature - 32)/20);
        return (int)Math.round(mTemperature);
    }

    public void setmTemperature(double mTemperature) {
        this.mTemperature = mTemperature;
    }

    public double getmHumanity() {
        return mHumanity*100;
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
