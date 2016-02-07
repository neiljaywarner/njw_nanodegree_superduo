package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by neil on 11/7/15.
 */
public class ScoresWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        final SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
            Date now = new Date();

            String today = mformat.format(now);
            String[] selection = {today};
            Cursor cursor = context.getContentResolver().query(uri, null ,null, selection,null);

            //For now just show the latest score of the day.
            cursor.moveToLast();
            int home_goals = cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
            int away_goals = cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
            String time = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
            String homeTeamName  = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
            String awayTeamName = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
            Log.e("NJW", "home/away" + homeTeamName + "/" + awayTeamName);

            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
            views.setTextViewText(R.id.score_textview, Utilies.getScores(home_goals,away_goals));
            views.setTextViewText(R.id.time_textview, time);
            views.setTextViewText(R.id.home_name, homeTeamName);
            views.setTextViewText(R.id.away_name, awayTeamName);

            views.setImageViewResource(R.id.home_crest, R.drawable.abc_btn_check_material);

            // Tell the AppWidgetManager to perform an update on the current app widget

                    //TODO: Consider allowing them one "last score" widget for each team if they want

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


}
