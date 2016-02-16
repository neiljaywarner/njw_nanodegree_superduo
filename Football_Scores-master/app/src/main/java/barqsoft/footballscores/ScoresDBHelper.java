package barqsoft.footballscores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.DatabaseContract.SCORES_TABLE;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresDBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 2;
    public ScoresDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CreateScoresTable = "CREATE TABLE " + DatabaseContract.SCORES_TABLE_NAME + " ("
                + DatabaseContract.SCORES_TABLE._ID + " INTEGER PRIMARY KEY,"
                + SCORES_TABLE.DATE_COL + " TEXT NOT NULL,"
                + SCORES_TABLE.TIME_COL + " INTEGER NOT NULL,"
                + SCORES_TABLE.HOME_COL + " TEXT NOT NULL,"
                + SCORES_TABLE.AWAY_COL + " TEXT NOT NULL,"
                + SCORES_TABLE.LEAGUE_COL + " INTEGER NOT NULL,"
                + SCORES_TABLE.HOME_GOALS_COL + " TEXT NOT NULL,"
                + SCORES_TABLE.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + SCORES_TABLE.MATCH_ID + " INTEGER NOT NULL,"
                + SCORES_TABLE.MATCH_DAY + " INTEGER NOT NULL,"
                + " UNIQUE ("+ SCORES_TABLE.MATCH_ID+") ON CONFLICT REPLACE"
                + " );";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.SCORES_TABLE_NAME);
    }
}
