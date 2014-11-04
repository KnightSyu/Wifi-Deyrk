package com.example.Circle;

import com.example.Circle.R;
import com.example.Circle.ReceiveMain.ListCursorAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveAD extends Fragment {
	 private DB mDbHelper; //實作DB類別
	 private Cursor mCursor; //放資料庫資料的容器
	 private View rootView;
	 private TextView adtitle;
	 private TextView adtime;
	 private TextView adcontext;
	 private ImageView adimage;
	 private TextView adkind;
	 private ImageButton pre_btn;
	 private ImageButton next_btn;
	 private Long adid;
	 private CheckBox mylovecheck;
	
	public ReceiveAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_receive_ad, container, false);
    	//設定畫面所對應的XML檔
    	adtitle =(TextView)rootView.findViewById(R.id.ad_receive_title);
		adtime = (TextView)rootView.findViewById(R.id.ad_receive_time);
		adcontext = (TextView)rootView.findViewById(R.id.ad_receive_context);
		adimage = (ImageView)rootView.findViewById(R.id.ad_receive_image);
		adkind = (TextView)rootView.findViewById(R.id.ad_receive_kind);
		mylovecheck = (CheckBox)rootView.findViewById(R.id.ad_receive_mylove);
		mylovecheck.setOnCheckedChangeListener(chklistener);
		pre_btn = (ImageButton) rootView.findViewById(R.id.imageButton_receive_pre);
    	pre_btn.setOnClickListener(prebtn);
    	next_btn = (ImageButton) rootView.findViewById(R.id.imageButton_receive_next);
    	next_btn.setOnClickListener(nextbtn);
		//ImageView image = (ImageView)view.findViewById(R.id.image_ad);
		Intent intent = getActivity().getIntent();
		
		setAdapter(rootView);
		if(adid==1){
			next_btn.setVisibility(View.INVISIBLE);
			
		}
    	
    	return rootView;
    }
	private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //打開DB
        //Toast.makeText(this.getActivity(), "d123456", Toast.LENGTH_LONG).show();
        Bundle extras = this.getArguments();
        
        mCursor = mDbHelper.getlistad_receive(extras.getLong("section_id")+1);
        //mCursor = mDbHelper.getAll();
        //mCursor.moveToNext();
        //mCursor.getPosition();
        //mCursor.moveToFirst();
        //Log.e("moveToFirst getPosition=",mCursor.getPosition()+"");
        
        //if(!mCursor.moveToNext()){
        	//Log.e("moveToNext=",mCursor.moveToNext()+"");
        	
        	//next_btn.setVisibility(View.INVISIBLE);
       // }
       // Log.e("moveToNext KEY_TITLE=",mCursor.getString(
		//		mCursor.getColumnIndex(DB.KEY_TITLE)));
        /*
        if(!mCursor.moveToPrevious()){
        	//Log.e("moveToNext=",mCursor.moveToNext()+"");
        	pre_btn.setVisibility(View.INVISIBLE);
        }*/
        
        /*while(mCursor.getInt(mCursor.getColumnIndex(DB.KEY_MYLOVE))==1){
        	adid+=1;
        	mCursor = mDbHelper.getlistad(adid);
        }*/
        
        if(mCursor.getCount()==0){
        	pre_btn.setVisibility(View.INVISIBLE);
        }
       
        
        
        /*mCursor = mDbHelper.getlistad(extras.getLong("section_id")-1);
        if(mCursor.getCount()==0){
        	next_btn.setVisibility(View.INVISIBLE);
        }*/
        
        mCursor = mDbHelper.getlistad_receive(extras.getLong("section_id" ));
        adid=extras.getLong("section_id" );
        //Log.d("test","e04"+adid);
        
        //呼叫DB的getAll函式，取得資料放進mCursor(資料庫資料的容器)
        if(mCursor != null){
        	mCursor.moveToFirst();
        }
        
		
		adtitle.setText(mCursor.getString(
						mCursor.getColumnIndex(DB.KEY_TITLE)));
		adtime.setText(mCursor.getString(
						mCursor.getColumnIndex(DB.KEY_TIME)));
		adcontext.setText(mCursor.getString(
				mCursor.getColumnIndex(DB.KEY_CONTEXT)));
		byte[] bb = mCursor.getBlob(mCursor.getColumnIndex(DB.KEY_IMAGE));
		adimage.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
		
		adkind.setText(mCursor.getString(
				mCursor.getColumnIndex(DB.KEY_KIND)));
		
        //執行接口
		//mCursor.getColumnIndex(DB.KEY_TIME);
        
	}
	private OnClickListener nextbtn = new OnClickListener(){
		public void onClick(View v){
		   //mDbHelper = new DB(this.getActivity());
	        //mDbHelper.open();
			pre_btn.setVisibility(View.VISIBLE);
			adid-=1;
			mCursor = mDbHelper.getlistad_receive(adid);
	        //呼叫DB的getAll函式，取得資料放進mCursor(資料庫資料的容器)
	        if(mCursor != null){
	        	mCursor.moveToFirst();
	        }
	        if(mCursor.getInt(mCursor.getColumnIndex(DB.KEY_MYLOVE))!=1){
	        	Log.e("testmylove", ""+1);
	        	mylovecheck.setChecked(false);
	        }else{
	        	Log.e("testmylove", ""+2);
	        	mylovecheck.setChecked(true);
	        }
	        
			
			adtitle.setText(mCursor.getString(
							mCursor.getColumnIndex(DB.KEY_TITLE)));
			adtime.setText(mCursor.getString(
							mCursor.getColumnIndex(DB.KEY_TIME)));
			adcontext.setText(mCursor.getString(
					mCursor.getColumnIndex(DB.KEY_CONTEXT)));
			byte[] bb = mCursor.getBlob(mCursor.getColumnIndex(DB.KEY_IMAGE));
			adimage.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
			
			adkind.setText(mCursor.getString(
					mCursor.getColumnIndex(DB.KEY_KIND)));
			if(adid==1){
				next_btn.setVisibility(View.INVISIBLE);
				
			}
		}
	};
	private OnClickListener prebtn = new OnClickListener(){
		public void onClick(View v){
		   //mDbHelper = new DB(this.getActivity());
	        //mDbHelper.open();
			next_btn.setVisibility(View.VISIBLE);
			adid+=1;
			mCursor = mDbHelper.getlistad_receive(adid+1);
		        
		        if(mCursor.getCount()==0){
		        	pre_btn.setVisibility(View.INVISIBLE);
		    }
			mCursor = mDbHelper.getlistad_receive(adid);
	        //呼叫DB的getAll函式，取得資料放進mCursor(資料庫資料的容器)
	        if(mCursor != null){
	        	mCursor.moveToFirst();
	        }
	        //Log.e("string",mCursor.getCount()+"");
	        if(mCursor.getInt(mCursor.getColumnIndex(DB.KEY_MYLOVE))!=1){
	        	Log.e("testmylove", ""+1);
	        	mylovecheck.setChecked(false);
	        }else{
	        	Log.e("testmylove", ""+2);
	        	mylovecheck.setChecked(true);
	        }
			adtitle.setText(mCursor.getString(
							mCursor.getColumnIndex(DB.KEY_TITLE)));
			adtime.setText(mCursor.getString(
							mCursor.getColumnIndex(DB.KEY_TIME)));
			adcontext.setText(mCursor.getString(
					mCursor.getColumnIndex(DB.KEY_CONTEXT)));
			byte[] bb = mCursor.getBlob(mCursor.getColumnIndex(DB.KEY_IMAGE));
			adimage.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
			
			adkind.setText(mCursor.getString(
					mCursor.getColumnIndex(DB.KEY_KIND)));
			
			
		}
	};
	private CheckBox.OnCheckedChangeListener chklistener = new CheckBox.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(mylovecheck.isChecked()){
				mDbHelper.loveupdate(adid,1);
				//Log.e("string",""+1);
			}
		}
		
	};
	//返回鍵用法
	//@Override
	/*public boolean onKeyDown(int KeyCode,KeyEvent event){
		Log.e("string","back");
		if(KeyCode == KeyEvent.KEYCODE_BACK){
			
			FragmentTransaction trans = getFragmentManager().beginTransaction();  
	        trans.replace(R.id.root_receice, new ReceiveMain());  
	        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        trans.commit();
	        return true;
		}
		return false;
	}*/
    

}
