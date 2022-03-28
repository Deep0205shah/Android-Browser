package deep.myappcompany.samplebrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class myDbHandlerBookmarks extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="bookmarks.db";
    private static final String TABLE_BOOKMARK="bookmarks";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_NAME="url";


    public myDbHandlerBookmarks(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_BOOKMARK + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_NAME + " TEXT " + ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BOOKMARK);
        onCreate(db);

    }

    public void addurl(websites website){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, website.get_url());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_BOOKMARK,null, values);
        db.close();
    }

    public void deleteUrl(String urlName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BOOKMARK + " WHERE "+ COLUMN_NAME+ "=\"" +urlName +"\";");
    }

    public List<String> databaseTOString(){
        SQLiteDatabase db=getWritableDatabase();
        String query = "Select * FROM "+TABLE_BOOKMARK;
        List<String> dbstring = new ArrayList<>();

        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        int i=0;
        if(c.moveToNext()){
            do{
                if(c.getString(c.getColumnIndex(COLUMN_NAME))!=null ){
                    String bString="";
                    bString+=c.getString(c.getColumnIndex("url"));
                    dbstring.add(bString);
                }
            } while(c.moveToNext());
        }
        return dbstring;
    }
}
