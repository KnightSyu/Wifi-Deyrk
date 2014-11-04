package com.example.Circle;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Circle.R;

public class CollectionAD extends Fragment {
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
	
	public CollectionAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_collection_ad, container, false);
    	//設定畫面所對應的XML檔
    	adtitle =(TextView)rootView.findViewById(R.id.ad_collection_title);
		adtime = (TextView)rootView.findViewById(R.id.ad_collection_time);
		adcontext = (TextView)rootView.findViewById(R.id.ad_collection_context);
		adimage = (ImageView)rootView.findViewById(R.id.ad_collection_image);
		adkind = (TextView)rootView.findViewById(R.id.ad_collection_kind);
		pre_btn = (ImageButton) rootView.findViewById(R.id.imageButton_collection_pre);
    	pre_btn.setOnClickListener(prebtn);
    	next_btn = (ImageButton) rootView.findViewById(R.id.imageButton_collection_next);
    	next_btn.setOnClickListener(nextbtn);
    	//Intent intent = getActivity().getIntent();
    	setAdapter(rootView);
    	
    	return rootView;
    }

	private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        int x=1;
        int y=1;
        Bundle extras = this.getArguments();
        
        do
        {
        	mCursor = mDbHelper.getlistad_receive(extras.getLong("section__collection_id")+x);
        		Log.e("getCount()==",mCursor.getCount()+"");
        		Log.e("pre次數",x+"");
        		if(mCursor != null){
    	        	mCursor.moveToFirst();
    	        }
        	if(mCursor.getCount()==0){
        		
        		pre_btn.setVisibility(View.INVISIBLE);
        		break;
        	}
        	else if(mCursor.getInt(5)==1){
        		break;
        	}
        	x+=1;
        	
        }while(true);
        do
        {
        	mCursor = mDbHelper.getlistad_receive(extras.getLong("section__collection_id")-y);
        		Log.e("next次數",x+"");
        		Log.e("next_getCount()==",mCursor.getCount()+"");
        		if(mCursor != null){
    	        	mCursor.moveToFirst();
    	        }
        	if(mCursor.getCount()==0){
            	next_btn.setVisibility(View.INVISIBLE);
            	break;
            }
            else if(mCursor.getInt(5)==1){
            	break;
            }
        	
        	y+=1;
        }while(true);
        mCursor = mDbHelper.getlistlovead(extras.getLong("section__collection_id" ));
        adid=extras.getLong("section__collection_id" );
        
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

        }
	private OnClickListener nextbtn = new OnClickListener(){
		public void onClick(View v){
		   //mDbHelper = new DB(this.getActivity());
	        //mDbHelper.open();
			int y=1;
			do
	        {
	        	mCursor = mDbHelper.getlistad_receive(adid-y);
	        		
	        	if(mCursor != null){
	    	        	mCursor.moveToFirst();
	    	        }
	        	if(mCursor.getCount()==0){
	            	next_btn.setVisibility(View.INVISIBLE);
	            	break;
	            }
	            else if(mCursor.getInt(5)==1){
	            	
	            	mCursor = mDbHelper.getlistlovead(adid-y);
	            	adid = adid-y;
	            	 if(mCursor != null){
	     	        	mCursor.moveToFirst();
	     	        }
	            	 pre_btn.setVisibility(View.VISIBLE);
	     
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
	     			int k = 1;
	     			
	     			do
	     	        {
	     	        	mCursor = mDbHelper.getlistad_receive(adid-k);
	     	        		
	     	        	if(mCursor != null){
	     	    	        	mCursor.moveToFirst();
	     	    	        }
	     	        	if(mCursor.getCount()==0){
	     	            	next_btn.setVisibility(View.INVISIBLE);
	     	            	break;
	     	            }
	     	            else if(mCursor.getInt(5)==1){
	     	            	break;
	     	            }
	     	        	
	     	        	k+=1;
	     	        }while(true);
	     			
	            	break;
	            }
	        	
	        	y+=1;
	        }while(true);
			
			
			
			
		}
	};
	private OnClickListener prebtn = new OnClickListener(){
		public void onClick(View v){
		   //mDbHelper = new DB(this.getActivity());
	        //mDbHelper.open();
			int x=1;
			do
	        {
	        	mCursor = mDbHelper.getlistad_receive(adid+x);
	        		
	        	if(mCursor != null){
	    	        	mCursor.moveToFirst();
	    	        }
	        	if(mCursor.getCount()==0){
	            	pre_btn.setVisibility(View.INVISIBLE);
	            	break;
	            }
	            else if(mCursor.getInt(5)==1){
	            	
	            	mCursor = mDbHelper.getlistlovead(adid+x);
	            	adid = adid+x;
	            	 if(mCursor != null){
	     	        	mCursor.moveToFirst();
	     	        }
	            	 next_btn.setVisibility(View.VISIBLE);
	     
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
	     			
	     			int k = 1;
	     			
	     			do
	     	        {
	     	        	mCursor = mDbHelper.getlistad_receive(adid+k);
	     	        		
	     	        	if(mCursor != null){
	     	    	        	mCursor.moveToFirst();
	     	    	        }
	     	        	if(mCursor.getCount()==0){
	     	            	pre_btn.setVisibility(View.INVISIBLE);
	     	            	break;
	     	            }
	     	            else if(mCursor.getInt(5)==1){
	     	            	break;
	     	            }
	     	        	
	     	        	k+=1;
	     	        }while(true);
	     			
	     			
	            	break;
	            }
	        	
	        	x+=1;
	        }while(true);
			
			
			
			
		}
	};
}
