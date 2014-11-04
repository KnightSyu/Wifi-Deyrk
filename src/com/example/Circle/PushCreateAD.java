package com.example.Circle;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.example.Circle.R;

public class PushCreateAD extends Fragment {
	
	private View rootView;
	private static final int RESULT_OK = 1; //選完圖片回傳的判斷值
	private DB mDbHelper; //實作DB類別
	private Cursor mCursor;
    private EditText push_title, push_context;
    private ImageView imageView;
    private Bitmap bitmap;
    private Spinner spinner;
    private String[] kind = {"不分類", "餐廳類", "速食類", "小吃類", "服飾類", "3C電子類",
    						"生活家具類", "電玩遊戲類", "休閒健身類", "保養、精品類", "文具書籍類", "其它"};
    private ArrayAdapter<String> kindList;
    private Button addimage, save, cancel;
    private Long adid;
    //宣告畫面元件變數
    
	public PushCreateAD() {
    }

    @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_push_create, container, false);
    	//設定畫面所對應的XML檔
    	
    	
    	addimage = (Button)rootView.findViewById(R.id.button_addimage);
    	addimage.setOnClickListener(ibtn);
    	//增加圖片的按鈕
    	
    	save = (Button)rootView.findViewById(R.id.addad_ok);
    	save.setOnClickListener(sbtn);
    	//儲存的按鈕
    	
    	cancel = (Button)rootView.findViewById(R.id.addad_cancel);
    	cancel.setOnClickListener(cbtn); 
    	//取消的按鈕
    	
    	push_title = (EditText)rootView.findViewById(R.id.input_adtitle);
    	push_context = (EditText)rootView.findViewById(R.id.input_adtext);
    	//取得標題跟內文
    	
    	imageView = (ImageView)rootView.findViewById(R.id.image_ad);
    	
    	spinner = (Spinner)rootView.findViewById(R.id.spinner_kind);
    	kindList = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, kind);
    	spinner.setAdapter(kindList);
    	//分類元件
    	Bundle extras = this.getArguments();
    	adid=extras.getLong("section_id" );
    	if((int)extras.getLong("section_id")!=0){
        adid=extras.getLong("section_id" );
    	setAdapter(rootView);
    	}
    	return rootView;
    }
     private void  setAdapter(View rootView){
    	 mDbHelper = new DB(this.getActivity());
         mDbHelper.open();
         int i=0,j=0;
         //Bundle extras = this.getArguments();
         mCursor = mDbHelper.getlistad_push(adid);
         if(mCursor != null){
         	mCursor.moveToFirst();
         }
         
         push_title.setText(mCursor.getString(
						mCursor.getColumnIndex(DB.KEY_TITLE)));
         push_context.setText(mCursor.getString(
				mCursor.getColumnIndex(DB.KEY_CONTEXT)));
         while(!mCursor.getString(mCursor.getColumnIndex(DB.KEY_KIND)).equals(kind[i])){
        	 
        	 i+=1;
        	 j=i;
        	 //Log.e("j=",j+"");
         }
         //Log.e("kind=",kind[2]+"");
         //Log.e("kindstring=",mCursor.getString(mCursor.getColumnIndex(DB.KEY_KIND))+"");
         //mCursor.getInt(mCursor.getColumnIndex(DB.KEY_KIND
         spinner.setSelection(j);
         byte[] bb = mCursor.getBlob(mCursor.getColumnIndex(DB.KEY_IMAGE));
         imageView.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
    	 
     }
    
    //儲存按鈕的傾聽器
     
     private OnClickListener sbtn = new OnClickListener(){
    	public void onClick(View v){
    		
    		//存入一筆資料
    		
    		String kind = spinner.getSelectedItem().toString();
            //kind是所選的分類
    		Log.e("spinnerkind=",kind+"");
    		
    		mDbHelper = new DB(getActivity());
            mDbHelper.open();
            if(adid==0){
            	if(bitmap==null){
            		Log.e("bitmap=","null");
            		mDbHelper.createPush(push_title.getText().toString(),push_context.getText().toString(), null,kind);
            	}else{
            		Log.e("bitmap=","true");
            		mDbHelper.createPush(push_title.getText().toString(),push_context.getText().toString(), bitmap,kind);
            	}
            //呼叫create傳入參數給DB來儲存一筆資料
            }else{
            	if(bitmap==null){
            		mDbHelper.update(adid,push_title.getText().toString(),push_context.getText().toString(), null,kind);	
            	}else{
            		mDbHelper.update(adid,push_title.getText().toString(),push_context.getText().toString(), bitmap,kind);	
            	}
            
            }
            	
            
            
            
            FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_push, new PushMain());  
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack("PushMain");  
            trans.commit();
    		//切換畫面回PushMain
    		
    	}
    };
     
    
    //取消按鈕的傾聽器
      private OnClickListener cbtn = new OnClickListener(){
    	public void onClick(View v){
    		
    		FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_push, new PushMain());  
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack("PushMain");  
            trans.commit();
            //切換畫面回PushMain
    	}
    	
    };
    
    //增加圖片按鈕的傾聽器
      private OnClickListener ibtn = new OnClickListener() {
	    public void onClick(View v) {
	    	
	    	//選取圖片
	    	
	    	Intent intent = new Intent();
	    	intent.setType("image/*");
	    	intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,1);
            //開啟選取圖片的視窗
	    }
	    
	};
	
	//選取圖片的功能
	  public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(data!=null){
		//如果requestCode=1，就開始動作
			if(requestCode == RESULT_OK){
			
				Uri uri = data.getData();
				Log.e("uri", uri.toString());
				//取得圖片的路徑
				ContentResolver cr = this.getActivity().getContentResolver();
			
		
				try{
					bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
					imageView = (ImageView)rootView.findViewById(R.id.image_ad);
					imageView.setImageBitmap(bitmap);
				//取得圖片轉成bitmap格式放進imageView
	            
				} catch (FileNotFoundException e){
				Log.e("Exception", e.getMessage(),e);
				}
		
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
