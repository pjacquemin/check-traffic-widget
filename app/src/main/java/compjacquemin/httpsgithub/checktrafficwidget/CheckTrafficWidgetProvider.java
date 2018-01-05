package compjacquemin.httpsgithub.checktrafficwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.google.maps.GeoApiContext;

public class CheckTrafficWidgetProvider extends AppWidgetProvider {
    private String api_key;
    private GeoApiContext geo_api_context;

    @Override
    public void onUpdate(Context context, AppWidgetManager app_widget_manager, int[] app_widget_ids) {
        RemoteViews remote_views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        new GoogleAPIAsyncRequest(context, app_widget_manager, app_widget_ids, remote_views).execute("");

        super.onUpdate(context, app_widget_manager, app_widget_ids);
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        api_key = context.getResources().getString(R.string.google_api_key);
        geo_api_context = new GeoApiContext.Builder()
                .apiKey(api_key)
                .build();

        GoogleAPIClientSingleton.getInstance().setGeoApiContext(geo_api_context);
    }
}