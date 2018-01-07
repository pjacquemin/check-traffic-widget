package compjacquemin.httpsgithub.checktrafficwidget;

import android.content.Context;

import com.google.maps.GeoApiContext;

public class GoogleAPIClientSingleton {
    private static GoogleAPIClientSingleton instance = null;
    private String api_key;
    private GeoApiContext geo_api_context;

    private GoogleAPIClientSingleton(Context context){
        this.api_key = context.getResources().getString(R.string.google_api_key);
        this.geo_api_context = new GeoApiContext.Builder()
                .apiKey(this.api_key)
                .build();
    }

    public static GoogleAPIClientSingleton getInstance(Context context){
        if(instance == null)
        {
            instance = new GoogleAPIClientSingleton(context);
        }

        return instance;
    }

    public GeoApiContext getGeoApiContext(){
        return this.geo_api_context;
    }
}
