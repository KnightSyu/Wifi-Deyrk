package com.example.adhoctry;

import java.io.FileNotFoundException;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PushCreateAD extends Fragment {
	private static final int RESULT_OK = 1;
	private DB mDbHelper;
    private Cursor mCursor;
    private EditText push_title,push_context;
    View rootView;
    private ImageView imageView;
    private Bitmap bitmap;
	public PushCreateAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_push_create, container, false);
    	
    	Button addimage = (Button)rootView.findViewById(R.id.button_addimage);
    	addimage.setOnClickListener(ibtn);
    	Button save = (Button)rootView.findViewById(R.id.addad_ok);
    	save.setOnClickListener(sbtn);
    	Button cancel = (Button)rootView.findViewById(R.id.addad_cancel);
    	cancel.setOnClickListener(cbtn); 
    	push_title = (EditText)rootView.findViewById(R.id.input_adtitle);
    	push_context = (EditText)rootView.findViewById(R.id.input_adtext);
    	
    	
    	
    	return rootView;
    }
    private OnClickListener sbtn = new OnClickListener(){
    	public void onClick(View v){
    		//push_title.getText().toString();
    		mDbHelper = new DB(getActivity());
            mDbHelper.open();
            mDbHelper.create(push_title.getText().toString(),bitmap);
            FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_push, new PushMain());  
            //trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack("PushMain");  
            trans.commit();
    		
    		
    	}
    };
    private OnClickListener cbtn = new OnClickListener(){
    	public void onClick(View v){
    		FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_push, new PushMain());  
            //trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack("PushMain");  
            trans.commit();
    	}
    	
    };
    private OnClickListener ibtn = new OnClickListener() {
	    public void onClick(View v) {
	    	Intent intent = new Intent();
	    	intent.setType("image/*");
	    	intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
           
	    }
	    
	};
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == RESULT_OK){
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			ContentResolver cr = this.getActivity().getContentResolver();
		
			try{
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				imageView = (ImageView)rootView.findViewById(R.id.image_ad);
				imageView.setImageBitmap(bitmap);
				//mDbHelper = new DB(getActivity());
	            //mDbHelper.open();
	            //mDbHelper.create("¼ÐÃD",bitmap);
	            
			} catch (FileNotFoundException e){
				Log.e("Exception", e.getMessage(),e);
			}
		
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
