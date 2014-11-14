package com.example.Circle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Circle.R;

public class CollectionAD extends Fragment {
	private DB mDbHelper; //��@DB���O
	private Cursor mCursor; //���Ʈw��ƪ��e��
	private View rootView;
	private TextView adtitle;
	private TextView adtime;
	private TextView adcontext;
	private ImageView adimage;
	private TextView adkind;
	private ImageButton pre_btn;
	private ImageButton next_btn;
	private Button delete;
	private Long adid;
	
	public CollectionAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_collection_ad, container, false);
    	//�]�w�e���ҹ�����XML��
    	adtitle =(TextView)rootView.findViewById(R.id.ad_collection_title);
		adtime = (TextView)rootView.findViewById(R.id.ad_collection_time);
		adcontext = (TextView)rootView.findViewById(R.id.ad_collection_context);
		adimage = (ImageView)rootView.findViewById(R.id.ad_collection_image);
		adkind = (TextView)rootView.findViewById(R.id.ad_collection_kind);
		pre_btn = (ImageButton) rootView.findViewById(R.id.imageButton_collection_pre);
    	pre_btn.setOnClickListener(prebtn);
    	next_btn = (ImageButton) rootView.findViewById(R.id.imageButton_collection_next);
    	next_btn.setOnClickListener(nextbtn);
    	delete = (Button) rootView.findViewById(R.id.ad_collection_delete);
    	delete.setOnClickListener(de);
    	//Intent intent = getActivity().getIntent();
    	setAdapter(rootView);
    	
    	rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();

		rootView.setOnKeyListener(new OnKeyListener() {
		        @Override
		        public boolean onKey(View v, int keyCode, KeyEvent event) {
		                if (event.getAction() == KeyEvent.ACTION_DOWN) {
		                    if (keyCode == KeyEvent.KEYCODE_BACK) {
		                        //Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
		                    	FragmentTransaction trans = getFragmentManager().beginTransaction();  
		                        trans.replace(R.id.root_collection, MainDeyrk.CM);  
		                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		                        //trans.addToBackStack("PushMain");  
		                        trans.commit();
		                    	
		                    return true;
		                    }
		                }
		                return false;
		            }
		        });
    	
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
        		Log.e("pre����",x+"");
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
        		Log.e("next����",x+"");
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
	
	private OnClickListener de = new OnClickListener(){
		public void onClick(View v){
			
			//���U�R�����ʧ@
			
		}
	};
	
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
