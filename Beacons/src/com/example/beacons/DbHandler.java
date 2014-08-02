package com.example.beacons;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_MINOR = "minor";
	public static final String KEY_INFO = "info";
	
	private static final String DATABASE_NAME = "lightbeacons";
	private static final String DATABASE_TABLE = "beacons";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
						KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						KEY_MINOR + " TEXT NOT NULL, " +
						KEY_INFO + " TEXT NOT NULL);");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	
		
	}
	
	public DbHandler(Context c){
		
		ourContext = c;
	} 

	
	public DbHandler open() throws SQLException{
		
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		ourHelper.close();
	}

	public long createEntry(String minor, String info) {
		// TODO Auto-generated method stub
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_MINOR, minor);
		cv.put(KEY_INFO, info);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
		
	}

	public String getData() {
		// TODO Auto-generated method stub
		
		String[] columns = new String[]{ KEY_ROWID, KEY_MINOR, KEY_INFO };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String rezult = "";
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iMinor = c.getColumnIndex(KEY_MINOR);
		int iInfo = c.getColumnIndex(KEY_INFO);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			rezult += c.getString(iRow) + " " + c.getString(iMinor) + " " + c.getString(iInfo) + "\n";
		} 
		
		return rezult;
	}
	
	public String getInfo(String minor){
		
		String info = "";
		Cursor c = ourDatabase.query(DATABASE_TABLE, new String[] { KEY_ROWID,
	            KEY_MINOR, KEY_INFO }, KEY_MINOR + "=?",
	            new String[] { minor }, null, null, null, null);
		
		int iInfo = c.getColumnIndex(KEY_INFO);
		
		
		if (c.moveToFirst()) {
		    info += c.getString(iInfo);
		} else {
			return null;
		}
		
		return info;
	}

}
