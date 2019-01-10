package compjacquemin.httpsgithub.checktrafficwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;

import org.joda.time.DateTime;

public class CheckTrafficWidgetProvider extends AppWidgetProvider {
    private RemoteViews remote_views;
    private SharedPreferences shared_pref;
    private String home_address;
    private String work_address;
    private GeoApiContext geo_api_context;
    private DirectionsApiRequest home_to_work_result_api_request;
    private DirectionsApiRequest work_to_home_result_api_request;
    private GoogleApiHomeToWorkResultCallback home_to_work_result_callback;
    private GoogleApiWorkToHomeResultCallback work_to_home_result_callback;

    @Override
    public void onUpdate(Context context, AppWidgetManager app_widget_manager, int[] app_widget_ids) {
        remote_views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent intent = new Intent(context, CheckTrafficWidgetProvider.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, app_widget_ids);

        PendingIntent pending_intent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        remote_views.setOnClickPendingIntent(R.id.home_to_work_textview, pending_intent);
        remote_views.setOnClickPendingIntent(R.id.work_to_home_textview, pending_intent);
        remote_views.setTextViewText(R.id.home_to_work_textview, "Refresh ...");
        remote_views.setTextViewText(R.id.work_to_home_textview, "Refresh ...");

        for (int id : app_widget_ids) {
            app_widget_manager.updateAppWidget(id, remote_views);
        }

        try {
            shared_pref = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            home_address = shared_pref.getString("home_address", "");
            work_address = shared_pref.getString("work_address", "");
            geo_api_context = GoogleAPIClientSingleton.getInstance(context).getGeoApiContext();
            home_to_work_result_api_request = DirectionsApi.getDirections(geo_api_context, home_address, work_address)
                    .departureTime(new DateTime());
            home_to_work_result_callback = new GoogleApiHomeToWorkResultCallback(remote_views, app_widget_manager, app_widget_ids);

            home_to_work_result_api_request.setCallback(home_to_work_result_callback);

            work_to_home_result_api_request = DirectionsApi.getDirections(geo_api_context, work_address, home_address)
                    .departureTime(new DateTime());
            work_to_home_result_callback = new GoogleApiWorkToHomeResultCallback(remote_views, app_widget_manager, app_widget_ids);

            work_to_home_result_api_request.setCallback(work_to_home_result_callback);
        } catch (Exception e) {
            remote_views.setTextViewText(R.id.home_to_work_textview, "Error");
            remote_views.setTextViewText(R.id.work_to_home_textview, e.toString());
        }

        super.onUpdate(context, app_widget_manager, app_widget_ids);
    }
}