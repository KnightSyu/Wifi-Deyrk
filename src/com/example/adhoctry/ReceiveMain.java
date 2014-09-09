package com.example.adhoctry;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ReceiveMain extends ListFragment {
	
    private DB mDbHelper; //實作DB類別
    private Cursor mCursor; //放資料庫資料的容器
    private View rootView;

    public ReceiveMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_receive_main, container, false);
    	//設定畫面所對應的XML檔
    	
        setAdapter(rootView);
        //更新畫面的ListView
    	
    	return rootView;
    }

	private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //打開DB
        
        mCursor = mDbHelper.getAll();
        //呼叫DB的getAll函式，取得資料放進mCursor(資料庫資料的容器)
        
        ListCursorAdapter cadapter = new ListCursorAdapter(this.getActivity(), mCursor);
        //實作ListCursorAdapter接口(設定要在畫面上顯示的ListView樣式，傳入ListView所在的activity跟mCursor來設定)
        
        setListAdapter(cadapter);
        //執行接口
        
	}
	
	//ListCursorAdapter接口
	public class ListCursorAdapter extends CursorAdapter{
		
		public ListCursorAdapter(Context context,Cursor c){
			super(context,c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			//從cursor裡拿資料出來放到畫面上
			
			TextView title =(TextView)view.findViewById(R.id.title_ad);
			TextView time = (TextView)view.findViewById(R.id.time);
			//ImageView image = (ImageView)view.findViewById(R.id.image_ad);
			
			title.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TITLE)));
			time.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TIME)));
			
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
	
	//點選條目刪除資料
	/*@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    mDbHelper.delete(id);
		
		setAdapter(rootView);
	}*/
	
	//當ListView有被點擊時運行的函式
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    
	    FragmentTransaction trans = getFragmentManager().beginTransaction();  
        trans.replace(R.id.root_receice, new ReceiveAD());  
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //trans.addToBackStack("ReceiveMain");  
        trans.commit();
        //將root_receice(收藏區的底層容器)當前的fragment替換成ReceiveAD
	    
	}
	
}