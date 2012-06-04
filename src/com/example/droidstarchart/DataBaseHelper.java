package com.example.droidstarchart;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DB_PATH = "/data/data/com.example.droidstarchart/";
	private static final String DB_NAME = "star_lite.db";
	//private static final String DB_TABLE = "ppm";
	private static final int DB_VERSION = 1;
	private static final String TAG = "DataBaseHelper";
	int id = 0;
	Random random = new Random();
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.myContext = context;
		
		if(!dbExists()){
			Log.w(TAG, "Database not found. Replacing with default database...");
			createDataBase();
		}
			openDataBase();
	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDataBase();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
/*		Log.w(TAG, "Upgrading DB from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
		onCreate(db);
*/	}

	public void createDataBase() {
			try {
			String myPath = DB_PATH + DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.CREATE_IF_NECESSARY);
			myDataBase.close();
			
			} catch (SQLiteException e) {
				Log.e(TAG, "Cannot create empty database.",e);
			}
		   copyDataBase();
	}

	private boolean dbExists() {
		try {
			openDataBase();
		} catch (SQLiteException e) {}
		if (myDataBase != null) {
			myDataBase.close();
			return true;
		}
		return false;
	}

	private void copyDataBase() {
	try {
			// Path to the just created empty db
			String outfilename = DB_PATH + DB_NAME;

			// Open the empty db as the output stream
			OutputStream myoutput = new FileOutputStream(outfilename);

			// For each 1MB file fragment, ...
			for(int i=0;i<=1;i++)
			{
				// Open your local db as the input stream
				InputStream myinput = myContext.getAssets().open("s0"+i+".db");

			// transfer byte to inputfile to outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myinput.read(buffer)) > 0) {
				myoutput.write(buffer, 0, length);
			}
			myinput.close();
			}

			// Close the streams
			myoutput.flush();
			myoutput.close();
		} catch (IOException e) {
			Log.e(TAG, "Cannot overwrite database.",e);
		}
	}

	public Cursor getStars(float mag) {
		return myDataBase.rawQuery(
				"SELECT ra,de,mag FROM ppm WHERE mag<="+mag, null);
	}
	
	public Cursor getConstellationLine(float mag){
		return myDataBase.rawQuery(
				"SELECT A.ra,A.de,B.ra,B.de FROM constellation_line L, ppm A, ppm B WHERE max(A.mag,B.mag)<="+mag+
				" AND L.star1=A._id AND L.star2=B._id"
				, null);
	}
	
	public Cursor getConstellationLabel(){
		return myDataBase.rawQuery("SELECT abbr,avg(ra),avg(de) from constellation_boundary B GROUP BY abbr",null);
	}
}