package com.example.adhoctry;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;


public class DB {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_TIME = "time";
	public static final String KEY_CONTEXT ="context";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_MYLOVE = "mylove";
	
	
	private static final String DATABASE_TABLE = "history";
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME = "notes.db";
		private static final int DATABASE_VERSION = 1;
		
		private static final String DATABASE_CREATE = 
				"CREATE TABLE "+DATABASE_TABLE+"("
				+KEY_ROWID+" INTEGER PRIMARY KEY,"
				+KEY_TITLE+" TEXT NOT NULL,"
				+KEY_TIME+" TIMESTAMP,"
				+KEY_CONTEXT+" TEXT,"
				+KEY_IMAGE+" BLOB"
				+");";
		
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
	private Context mContext = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public DB(Context context){
		this.mContext = context;
	}
	
	public DB open() throws SQLException {
		dbHelper = new DatabaseHelper(mContext, null, null, 0);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Cursor getAll(){
		return db.query(DATABASE_TABLE,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE},
				null,
				null,
				null,
				null,
				KEY_TIME + " DESC"); 
		//return db.rawQuery("SELECT * FROM "+DATABASE_TABLE+
		//		" ORDER BY "+KEY_TIME+" DESC", null);
	}
	//新增DB資料
	public long create(String record, Bitmap imagerecord){
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.ENGLISH);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		imagerecord.compress(Bitmap.CompressFormat.PNG, 100, bos);
		
		Date now = new Date();
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, record);
		args.put(KEY_TIME, df.format(now.getTime()));
		args.put(KEY_IMAGE, bos.toByteArray());
		args.put(KEY_CONTEXT, record);
		
		return db.insert(DATABASE_TABLE, null, args);
	}
	//刪除DB資料
	public boolean delete(long rowId){
		return db.delete(DATABASE_TABLE,KEY_ROWID + "=" + rowId, null) > 0;
	}
	
}
