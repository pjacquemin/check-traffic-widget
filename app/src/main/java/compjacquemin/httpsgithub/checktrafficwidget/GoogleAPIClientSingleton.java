package compjacquemin.httpsgithub.checktrafficwidget;

import com.google.maps.GeoApiContext;

/**
 * Created by patrick on 5/01/18.
 */

public class GoogleAPIClientSingleton {
    private static GoogleAPIClientSingleton instance = null;

    private GeoApiContext geo_api_context;

    private GoogleAPIClientSingleton(){
    }

    public static GoogleAPIClientSingleton getInstance(){
        if(instance == null)
        {
            instance = new GoogleAPIClientSingleton();
        }

        return instance;
    }

    public GeoApiContext getGeoApiContext(){
        return this.geo_api_context;
    }

    public void setGeoApiContext(GeoApiContext geo_api_context){
        this.geo_api_context = geo_api_context;
    }
}
