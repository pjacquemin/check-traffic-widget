package compjacquemin.httpsgithub.checktrafficwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;

import org.joda.time.DateTime;

public class GoogleAPIAsyncRequest extends AsyncTask<String, Void, Void>{

    private Context context;
    private AppWidgetManager app_widget_manager;
    private int[] app_widget_ids;
    private RemoteViews remote_views;
    private String api_key;
    private String home_address;
    private String work_address;
    private GeoApiContext geo_api_context;
    private DirectionsResult home_to_work_result;
    private DirectionsResult work_to_home_result;
    private Duration home_to_work_duration;
    private Distance home_to_work_distance;
    private Duration work_to_home_duration;
    private Distance work_to_home_distance;
    private String home_to_work_info_text;
    private String work_to_home_info_text;
    private int app_widget_id;

    public GoogleAPIAsyncRequest(Context context, AppWidgetManager app_widget_manager, int[] app_widget_ids, RemoteViews remote_views) {
        super();

        this.context = context;
        this.app_widget_manager = app_widget_manager;
        this.app_widget_ids = app_widget_ids;
        this.remote_views = remote_views;

        remote_views.setTextViewText(R.id.home_to_work_textview, "Refresh ...");
        remote_views.setTextViewText(R.id.work_to_home_textview, "Refresh ...");
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            for (int id : app_widget_ids) {
                this.app_widget_id = id;
                app_widget_manager.updateAppWidget(app_widget_id, remote_views);
                api_key = context.getResources().getString(R.string.google_api_key);
                work_address = context.getResources().getString(R.string.work_address);
                home_address = context.getResources().getString(R.string.home_address);
                geo_api_context = GoogleAPIClientSingleton.getInstance().getGeoApiContext();
                home_to_work_result = DirectionsApi.getDirections(geo_api_context, home_address, work_address)
                        .departureTime(new DateTime())
                        .await();
                home_to_work_duration = home_to_work_result.routes[0].legs[0].durationInTraffic;
                home_to_work_distance = home_to_work_result.routes[0].legs[0].distance;
                work_to_home_result = DirectionsApi.getDirections(geo_api_context, work_address, home_address)
                        .departureTime(new DateTime())
                        .await();
                work_to_home_duration = work_to_home_result.routes[0].legs[0].durationInTraffic;
                work_to_home_distance = work_to_home_result.routes[0].legs[0].distance;
                home_to_work_info_text = "M->T : " + home_to_work_result.routes[0].summary + " - " + home_to_work_duration.humanReadable + " - " + home_to_work_distance;
                work_to_home_info_text = "T->M : " + work_to_home_result.routes[0].summary + " - " + work_to_home_duration.humanReadable + " - " + work_to_home_distance;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            remote_views.setTextViewText(R.id.home_to_work_textview, "Error");
            remote_views.setTextViewText(R.id.work_to_home_textview, "Error");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        remote_views.setTextViewText(R.id.home_to_work_textview, home_to_work_info_text);
        remote_views.setTextViewText(R.id.work_to_home_textview, work_to_home_info_text);

        Intent intent = new Intent(context, CheckTrafficWidgetProvider.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, app_widget_ids);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        remote_views.setOnClickPendingIntent(R.id.home_to_work_textview, pendingIntent);
        remote_views.setOnClickPendingIntent(R.id.work_to_home_textview, pendingIntent);
        app_widget_manager.updateAppWidget(app_widget_id, remote_views);

        super.onPostExecute(result);
    }
}