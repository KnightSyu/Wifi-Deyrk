package com.example.adhoctry;

import java.io.FileNotFoundException;

import android.support.v4.app.Fragment;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectionMain extends Fragment {
    
    public static final String ARG_SECTION_NUMBER = "section_number";

	private static final int RESULT_OK = 1;
	private DB mDbHelper;
    private Cursor mCursor;
    
    TextView dummyTextView;
    View rootView;

    public CollectionMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_collection_main, container, false);
        
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
        
    	Button ibtn = (Button)rootView.findViewById(R.id.imageget);
    	ibtn.setOnClickListener(imagebtn);
    	return rootView;
    }
    private OnClickListener imagebtn = new OnClickListener() {
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
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				ImageView imageView = (ImageView)rootView.findViewById(R.id.iv01);
				imageView.setImageBitmap(bitmap);
				mDbHelper = new DB(getActivity());
	            mDbHelper.open();
	            mDbHelper.create("¼ÐÃD",bitmap);
	            
			} catch (FileNotFoundException e){
				Log.e("Exception", e.getMessage(),e);
			}
		
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
    
    	
    	
}
