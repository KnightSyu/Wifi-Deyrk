package com.example.adhoctry;

import com.example.adhoctry.ReceiveMain.ListCursorAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReceiveAD extends Fragment {
	 private DB mDbHelper; //實作DB類別
	 private Cursor mCursor; //放資料庫資料的容器
	 private View rootView;
	
	public ReceiveAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_receive_ad, container, false);
    	//設定畫面所對應的XML檔
    	
    	return rootView;
    }
	/*private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //打開DB
        
        mCursor = mDbHelper.getAll();
        //呼叫DB的getAll函式，取得資料放進mCursor(資料庫資料的容器)
        
        ListCursorAdapter cadapter = new ListCursorAdapter(this.getActivity(), mCursor);
        //實作ListCursorAdapter接口(設定要在畫面上顯示的ListView樣式，傳入ListView所在的activity跟mCursor來設定)
        
        setListAdapter(cadapter);
        //執行接口
        
	}*/
    
	public class ListCursorAdapter extends CursorAdapter{
		
		public ListCursorAdapter(Context context,Cursor c){
			super(context,c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			//從cursor裡拿資料出來放到畫面上
			
			TextView adtitle =(TextView)view.findViewById(R.id.ad_receive_title);
			TextView adtime = (TextView)view.findViewById(R.id.ad_receive_time);
			TextView adcontext = (TextView)view.findViewById(R.id.ad_receive_context);
			//ImageView image = (ImageView)view.findViewById(R.id.image_ad);
			
			adtitle.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TITLE)));
			adtime.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TIME)));
			adcontext.setText(cursor.getString(
					cursor.getColumnIndex(DB.KEY_CONTEXT)));
			//byte[] bb = cursor.getBlob(cursor.getColumnIndex(DB.KEY_IMAGE));
			//image.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
			//從cursor撈圖片出來轉檔放在ImageView的程式碼
			
			cursor.getColumnIndex(DB.KEY_TIME);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			View v =inflater.inflate(R.layout.listview_receive_collection,null);
			//設定ListView樣式
			bindView(v,context,cursor);
			
			return v;
		}
	}
}
