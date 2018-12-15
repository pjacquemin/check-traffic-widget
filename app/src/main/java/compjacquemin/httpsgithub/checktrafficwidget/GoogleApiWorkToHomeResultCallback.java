package compjacquemin.httpsgithub.checktrafficwidget;

import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;

import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;


public class GoogleApiWorkToHomeResultCallback implements PendingResult.Callback<DirectionsResult> {
    private RemoteViews remote_views;
    private AppWidgetManager app_widget_manager;
    private int[] app_widget_ids;
    private Duration work_to_home_duration;
    private Distance work_to_home_distance;
    private String work_to_home_info_text;

    public GoogleApiWorkToHomeResultCallback(RemoteViews remote_views, AppWidgetManager app_widget_manager, int[] app_widget_ids) {
        this.remote_views = remote_views;
        this.app_widget_manager = app_widget_manager;
        this.app_widget_ids = app_widget_ids;
    }

    @Override
    public void onResult(DirectionsResult work_to_home_result) {
        work_to_home_duration = work_to_home_result.routes[0].legs[0].durationInTraffic;
        work_to_home_distance = work_to_home_result.routes[0].legs[0].distance;
        work_to_home_info_text = String.format("T->M : %s - %s - %s", work_to_home_result.routes[0].summary, work_to_home_duration.humanReadable, work_to_home_distance);

        this.remote_views.setTextViewText(R.id.work_to_home_textview, work_to_home_info_text);

        for (int app_widget_id : app_widget_ids) {
            app_widget_manager.updateAppWidget(app_widget_id, remote_views);
        }
    }

    @Override
    public void onFailure(Throwable e) {
        this.remote_views.setTextViewText(R.id.work_to_home_textview, e.toString());
    }
}
