package compjacquemin.httpsgithub.checktrafficwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class CheckTrafficWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager app_widget_manager, int[] app_widget_ids) {
        RemoteViews remote_views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        new GoogleAPIAsyncRequest(context, app_widget_manager, app_widget_ids, remote_views).execute("");

        super.onUpdate(context, app_widget_manager, app_widget_ids);
    }

}