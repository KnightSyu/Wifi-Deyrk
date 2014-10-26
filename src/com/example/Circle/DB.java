package com.example.Circle;

import java.io.ByteArrayInputStream;
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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.Toast;


public class DB {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_TIME = "time";
	public static final String KEY_CONTEXT ="context";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_MYLOVE = "mylove";
	public static final String KEY_KIND = "kind";
	//�ŧi��쪺�W��
	
	private static final String DATABASE_TABLE = "push_table";
	private static final String DATABASE_TABLE2 = "receive_collection_table";
	//�ŧi��ƪ��W��
	
	private Context mContext = null; //�ŧi����(�q�`���O���ө�Yactivity�A��ܲ{�b�����O�b�Yactivity)
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	//�ŧi�ϥθ�Ʈw�����U�禡(SQLiteOpenHelper�BSQLiteDatabase)
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME = "deyrkbase.db";
		private static final int DATABASE_VERSION = 1;
		//�ŧi��Ʈw���W�ٸ򪩥�
		
		private static final String DATABASE_CREATE = 
				"CREATE TABLE "+DATABASE_TABLE+"("
				+KEY_ROWID+" INTEGER PRIMARY KEY,"
				+KEY_TITLE+" TEXT NOT NULL,"
				+KEY_TIME+" TIMESTAMP,"
				+KEY_CONTEXT+" TEXT,"
				+KEY_IMAGE+" BLOB,"
				+KEY_MYLOVE+" INTEGER,"
				+KEY_KIND+" TEXT"
				+");"
				+
				"CREATE TABLE "+DATABASE_TABLE2+"("
				+KEY_ROWID+" INTEGER PRIMARY KEY,"
				+KEY_TITLE+" TEXT NOT NULL,"
				+KEY_TIME+" TIMESTAMP,"
				+KEY_CONTEXT+" TEXT,"
				+KEY_IMAGE+" BLOB,"
				+KEY_MYLOVE+" INTEGER,"
				+KEY_KIND+" TEXT"
				+");";
		//�ŧi�Ыظ�ƪ��r��
		
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//���Ʈw�Q�سy�ɩҰ�����
			db.execSQL(DATABASE_CREATE); //�Ыظ�ƪ�
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//���Ʈw������s�ɩҰ�����
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE); //�R��DATABASE_TABLE��ƪ�
			onCreate(db); //�I�sonCreate���ظ�Ʈw
		}
		
	}
	
	//DB�غc�l(�]�w����)
	public DB(Context context){
		this.mContext = context;
	}
	
	//���}��Ʈw
	public DB open() throws SQLException {
		dbHelper = new DatabaseHelper(mContext, null, null, 0);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	//������Ʈw
	public void close(){
		dbHelper.close();
	}
	
	//�۩w�q�禡(���O���{���өI�s�ϥ�)
	public Cursor getAll(){
		
		//���oDATABASE_TABLE���Ҧ�����ƨåHKEY_TIME����Ƨ�
		
		return db.query(DATABASE_TABLE,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				KEY_MYLOVE +"="+ 0,
				null,
				null,
				null,
				KEY_TIME + " DESC");
	}
	public Cursor getMylove(){
		return db.query(DATABASE_TABLE,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				KEY_MYLOVE +"="+ 1,
				null,
				null,
				null,
				KEY_TIME + " DESC");
		
	}
	//���oDATABASE_TABLE�����wID�@�����
	public Cursor getlistad(Long receive_id){
		
		return db.query(DATABASE_TABLE,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				KEY_ROWID + "=" + receive_id,
				null,
				null,
				null,
				null
				
				);
		
				
	}

	
	
	//�۩w�q�禡(���O���{���өI�s�ϥ�)
	public long create(String titlerecord, String contextrecord, Bitmap imagerecord, String kindrecord){
		
		//�s�W�@�����
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.ENGLISH);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Date now = new Date();
		//�ŧi&�]�w�Gdf(����榡��)�Bbos(�G���}�C�ഫ�禡)�Bnow(���)
		
		imagerecord.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		//�N�Ƕi�Ӫ�imagerecord�নBitmap�榡
		int options = 100;  
        while ( bos.toByteArray().length/1024 > 2000) {
        	bos.reset();
        	imagerecord.compress(Bitmap.CompressFormat.JPEG, options, bos);
        	options -= 10;
        	Toast.makeText(this.mContext, ""+options, Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this.mContext, "bos.toByteArray().length: "+bos.toByteArray().length, Toast.LENGTH_SHORT).show();
		ContentValues args = new ContentValues(); //�ŧi�@�Ӯe��(args)
		args.put(KEY_TITLE, titlerecord);
		args.put(KEY_TIME, df.format(now.getTime()));
		args.put(KEY_IMAGE, bos.toByteArray());
		args.put(KEY_CONTEXT, contextrecord);
		args.put(KEY_MYLOVE, 0);
		args.put(KEY_KIND, kindrecord);
		//��put���覡�N�U�Ӹ�Ʃ�i�U�����
		
		return db.insert(DATABASE_TABLE, null, args);
		//�N�H�W�]�w�n���@����Ʒs�W��DATABASE_TABLE��
	}
	
	//�۩w�q�禡(���O���{���өI�s�ϥ�)
	//��Ʈw�R�����
	public boolean delete(long rowId){
		
		//�R���@�����
		
		return db.delete(DATABASE_TABLE,KEY_ROWID + "=" + rowId, null) > 0;
		//�R��DATABASE_TABLE��ƪ��@�����
	}
	//��Ʈw�ק���
	public boolean update(long rowId,String titlerecord, String contextrecord, Bitmap imagerecord, String kindrecord){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		imagerecord.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		//byte[] b = bos.toByteArray();
		int options = 100;  
        while ( (bos.toByteArray().length / 1024)>2000) {
        	bos.reset();
        	imagerecord.compress(Bitmap.CompressFormat.JPEG, options, bos);
        	options -= 10;
        }
        //ByteArrayInputStream isBm = new ByteArrayInputStream(bos.toByteArray());
        //Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        //byte[] b = bos.toByteArray();
	
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE,titlerecord);
		args.put(KEY_CONTEXT, contextrecord);
		args.put(KEY_IMAGE, bos.toByteArray());
		
		args.put(KEY_KIND, kindrecord);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	public boolean loveupdate(long rowId,int myloverecord){
		ContentValues args = new ContentValues();
		args.put(KEY_MYLOVE,myloverecord);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	
}
