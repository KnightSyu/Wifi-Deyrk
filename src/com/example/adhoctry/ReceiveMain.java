package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ReceiveMain extends ListFragment {
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    private DB mDbHelper;
    private Cursor mCursor;
    View rootView;

    public ReceiveMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_receive_main, container, false);
        
        Button btn = (Button) rootView.findViewById(R.id.insertdata);
        btn.setOnClickListener(mbtn1);
        //建構按鈕功能
        
        //Button btn2 = (Button) rootView.findViewById(R.id.openad);
    	//btn2.setOnClickListener(mbtn2);
    	//按下ad跳出ad畫面，以後是按下listview的ad跳出ad
        
        setAdapter(rootView);
        //建構DB
    	
    	return rootView;
    }

	private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //打開DB
        
        mCursor = mDbHelper.getAll();
        //取得資料
        
        ListCursorAdapter cadapter = new ListCursorAdapter(this.getActivity(), mCursor);
        //設定接口
        
        setListAdapter(cadapter);
        //執行接口
        
	}
	public class ListCursorAdapter extends CursorAdapter{
		public ListCursorAdapter(Context context,Cursor c){
			super(context,c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			TextView title =(TextView)view.findViewById(R.id.title_ad);
			TextView time = (TextView)view.findViewById(R.id.time);
			ImageView image = (ImageView)view.findViewById(R.id.image_ad);
			title.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TITLE)));
			time.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TIME)));
			byte[] bb = cursor.getBlob(cursor.getColumnIndex(DB.KEY_IMAGE));
			image.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
			cursor.getColumnIndex(DB.KEY_TIME);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(context);
			View v =inflater.inflate(R.layout.receive_collection_listview,null);
			bindView(v,context,cursor);
			
			return v;
		}
	}
	
	private OnClickListener mbtn1 = new OnClickListener() {
	    public void onClick(View v) {
	    	mDbHelper = new DB(getActivity());
            mDbHelper.open();
            //dummyTextView.setText("123");
            //mDbHelper.create("77.77",null);
            setAdapter(rootView);
            
            //按鈕按下時新增一筆資料("77.77")給資料庫並關閉DB
	    }
	    
	};
	
	/*private OnClickListener mbtn2 = new OnClickListener() {
	    public void onClick(View v) {
            FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_receice, new ReceiveAD());  
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack("ReceiveMain");  
            trans.commit();
	    }
	};*/
	//點選條目刪除資料
	/*@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    mDbHelper.delete(id);
		
		setAdapter(rootView);
	}*/
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    FragmentTransaction trans = getFragmentManager().beginTransaction();  
        trans.replace(R.id.root_receice, new ReceiveAD());  
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //trans.addToBackStack("ReceiveMain");  
        trans.commit();
	    
	}
	
}