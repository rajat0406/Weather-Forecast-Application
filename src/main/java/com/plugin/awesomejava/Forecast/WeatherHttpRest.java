package com.plugin.awesomejava.Forecast;

import com.plugin.awesomejava.Location.LocationInfo;
import java.util.ArrayList;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;
import com.plugin.awesomejava.UIApp.DynJLabelObject;
import com.plugin.awesomejava.UIApp.DynamicJLabelList;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class WeatherHttpRest {

    private static final boolean isMetric = true ;
    private static final String DEGREE = "\u00b0";

    private final FeedEntry entry;
    private final WeatherImp weather;
    private ForecastValues forecastValue;
    private ForecastValues forecastValue1;

    private HashMap<DynJLabelObject, ForecastValues> WeatherMaphm;

    public WeatherHttpRest(FeedEntry entry, final DynamicJLabelList DynJLabelList) {
        this.entry = entry;
        this.weather = new WeatherImp(DynJLabelList);
        WeatherMaphm = new HashMap<DynJLabelObject, ForecastValues>();
    }

    public HashMap<DynJLabelObject, ForecastValues> HttpRestRequest() {

        final ArrayList<ForecastValues> list = new ArrayList<ForecastValues>();
        final OpenWeatherMap.Units units = (isMetric) ? OpenWeatherMap.Units.METRIC : OpenWeatherMap.Units.IMPERIAL;
        //final OpenWeatherMap.Units units1 = (isMetric) ? OpenWeatherMap.Units.IMPERIAL : OpenWeatherMap.Units.METRIC;
        final OpenWeatherMap owm = new OpenWeatherMap(units, entry.getApiKey());
       // final OpenWeatherMap owm1 = new OpenWeatherMap(units1, entry.getApiKey());

        final byte forecastDays = Byte.valueOf(String.valueOf(entry.getDays()));
        try {

            final DailyForecast forecast = owm.dailyForecastByCityName(entry.getLocation(), entry.getCountryCode(), forecastDays);
            //final DailyForecast forecast1 = owm1.dailyForecastByCityName(entry.getLocation(), entry.getCountryCode(), forecastDays);

            System.out.println("Raw Response: " + forecast.getRawResponse());
            //System.out.println("Raw Response: " + forecast1.getRawResponse());
            int numForecasts = forecast.getForecastCount();
            //int numForecasts1 = forecast1.getForecastCount();

            for (int i = 0; i < numForecasts; i++) {
                final DailyForecast.Forecast dayForecast = forecast.getForecastInstance(i);

                IntializeForecastValues(forecast.getForecastInstance(i),
                        dayForecast.getTemperatureInstance(),
                        dayForecast.getWeatherInstance(0));
                }
           /* for (int j = 0; j < numForecasts; j++) {
            final DailyForecast.Forecast dayForecast1 = forecast1.getForecastInstance(j);
                IntializeForecastValues(forecast1.getForecastInstance(j),
                        dayForecast1.getTemperatureInstance(),
                        dayForecast1.getWeatherInstance(0));
            }
*/
        } catch (Exception e) {
            System.out.println(e.toString());
            WeatherMaphm.clear();
        }
        return WeatherMaphm;
    }

    private void IntializeForecastValues(final DailyForecast.Forecast dayForecast,
            final DailyForecast.Forecast.Temperature temperature,
           // final DailyForecast.Forecast.Temperature temperature1,
            final DailyForecast.Forecast.Weather weather) {
        forecastValue = new ForecastValues();
        forecastValue1 = new ForecastValues();


        forecastValue.setDateInformations("Last Updated: " + String.valueOf(dayForecast.getDateTime()));

      forecastValue1.setDateInformations(String.valueOf(dayForecast.getDateTime()));

        forecastValue.setMinTemperature(TemperatureRoundSplit.SplitStringValue(temperature.getMinimumTemperature()) + DEGREE + "C");
        forecastValue.setMaxTemperature(TemperatureRoundSplit.SplitStringValue(temperature.getMaximumTemperature()) + DEGREE + "C");
        forecastValue.setDateTemperature(TemperatureRoundSplit.SplitStringValue(temperature.getDayTemperature()) + DEGREE + "C");
        forecastValue.setMin1Temperature(TemperatureRoundSplit.SplitStringValue(temperature.getMinimumTemperature()) + DEGREE + "F");
        forecastValue.setMax1Temperature(TemperatureRoundSplit.SplitStringValue(temperature.getMaximumTemperature()) + DEGREE + "F");

        forecastValue.setHumidity("Humidity: " + String.valueOf(dayForecast.getHumidity()) + "%");
        forecastValue.setPressure("Pressure: " + String.valueOf(dayForecast.getPressure()) + " mbar ");
        forecastValue.setClouds(String.valueOf(dayForecast.getPercentageOfClouds()) + "%");
        forecastValue.setWind_Speed("Wind Speed : " + String.valueOf(dayForecast.getWindSpeed()) + "m/s");
        forecastValue.setDescription(weather.getWeatherDescription());
        forecastValue.setDayofWeek(dayForecast.getDateTime());

        this.weather.setForecastValue(forecastValue);
        final int day = this.weather.GetDayCode();

        if (day == LocationInfo.DayCode()) {
            forecastValue.setCurrentDay(true);
            forecastValue.setMainWeatherIcon(this.weather
                    .WeatherIcon(forecastValue.getDescription(), true));
        }

        ImageIcon WeatherIcon = this.weather.WeatherIcon(forecastValue.getDescription());
        forecastValue.setWeatherIcon(WeatherIcon);

        PutMapValues(this.weather.DayOfWeekWeather(day));

        System.out.println(weather.getWeatherDescription());
        System.out.println(forecastValue.toString());
    }


    private void PutMapValues(final DynJLabelObject labelObj) {
        WeatherMaphm.put(labelObj, forecastValue);
    }

}
