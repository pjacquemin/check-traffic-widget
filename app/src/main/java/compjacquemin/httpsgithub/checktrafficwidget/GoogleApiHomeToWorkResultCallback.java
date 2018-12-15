package compjacquemin.httpsgithub.checktrafficwidget;

import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;

import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;


public class GoogleApiHomeToWorkResultCallback implements PendingResult.Callback<DirectionsResult> {
    private RemoteViews remote_views;
    private AppWidgetManager app_widget_manager;
    private int[] app_widget_ids;
    private Duration home_to_work_duration;
    private Distance home_to_work_distance;
    private String home_to_work_info_text;

    public GoogleApiHomeToWorkResultCallback(RemoteViews remote_views, AppWidgetManager app_widget_manager, int[] app_widget_ids) {
        this.remote_views = remote_views;
        this.app_widget_manager = app_widget_manager;
        this.app_widget_ids = app_widget_ids;
    }

    @Override
    public void onResult(DirectionsResult home_to_work_result) {
        home_to_work_duration = home_to_work_result.routes[0].legs[0].durationInTraffic;
        home_to_work_distance = home_to_work_result.routes[0].legs[0].distance;
        home_to_work_info_text = String.format("M->T : %s - %s - %s", home_to_work_result.routes[0].summary, home_to_work_duration.humanReadable, home_to_work_distance);;

        this.remote_views.setTextViewText(R.id.home_to_work_textview, home_to_work_info_text);

        for (int app_widget_id : app_widget_ids) {
            app_widget_manager.updateAppWidget(app_widget_id, remote_views);
        }
    }

    @Override
    public void onFailure(Throwable e) {
        this.remote_views.setTextViewText(R.id.home_to_work_textview, e.toString());
    }
}
