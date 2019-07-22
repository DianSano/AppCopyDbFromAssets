package id.co.blogspot.diansano.appcopydbfromassets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "nama";
    static final String KEY_ALAMAT = "alamat ";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "anggota.db";
    static final String DATABASE_TABLE = "anggota";
    static final int DATABASE_VERSION = 1;
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    //opens the database
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //closes the database
    // public void close() {
    // DBHelper.close();
    // }
    // insert a contact into the database
    public long insertContact(String name, String alamat) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_ALAMAT, alamat);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //deletes a particular contact
    public boolean deleteContact(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //retrieves all the contacts
    public Cursor getAllContacts() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_ALAMAT},
                null, null, null, null, null);
    }

    //retrieves a particular contact
    public Cursor getContact(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_NAME, KEY_ALAMAT},KEY_ROWID + "=" + rowId,
                null, null, null, null, null);
       /* Cursor mCursor = db.rawQuery("select * from " + DATABASE_TABLE +
                " where _id = " + rowId, null);*/
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    //update a contact
    public boolean updateContact(long rowId, String name, String alamat) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_ALAMAT, alamat);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId,
                null) > 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            /* try {                db.execSQL(DATABASE_CREATE);
            }
            catch (SQLException e) {
                         e.printStackTrace();            }*/
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
                    newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS anggota");
            onCreate(db);
        }
    }

}
