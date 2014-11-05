package com.example.Circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.Circle.R;

public class PushMain extends ListFragment {
    
    private DB mDbHelper; //實作DB類別
    private Cursor mCursor; //放資料庫資料的容器
    private View rootView;
    private Button btn;
    List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
    
    public PushMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        rootView = inflater.inflate(R.layout.fragment_push_main, container, false);
        //設定畫面所對應的XML檔
        
        /*
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
    	//從bundle拿出資料來用
    	*/
        
    	btn = (Button) rootView.findViewById(R.id.createpushad);
    	btn.setOnClickListener(mbtn);
    	//新增推播訊息的按鈕
    	
    	setAdapter(rootView);
    	//更新畫面的ListView
    	
    	return rootView;
    }
    
    //新增推播訊息按鈕的傾聽器
    private OnClickListener mbtn = new OnClickListener() {
	    public void onClick(View v) {
	    	
	    	//按下時開啟PushCreateAD
	    	
            FragmentTransaction trans = getFragmentManager().beginTransaction();

            //trans.replace(R.id.root_push, new PushCreateAD());
            Fragment fragment = new PushCreateAD();
            trans.replace(R.id.root_push, fragment);
            Bundle args = new Bundle();
            args.putLong("section_id", 0);
            fragment.setArguments(args);
            

            //trans.addToBackStack("PushMain");
            

            //root_push是個容器 ,用來放fragment
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.commit();
	    }
	};
	
	private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //打開DB
        
        mCursor = mDbHelper.getAll();
        //呼叫DB的getAll函式，取得資料放進mCursor(資料庫資料的容器)
        /*
        data.clear();
    	
    	//存陣列內容(連線的裝置名稱)
    	for(int i=0; i<peers.size(); i++)
    	{
    		Map<String,Object> item = new HashMap<String,Object>();
    		WifiP2pDevice device = peers.get(i);
    		item.put("deviceName",device.deviceName);
    		item.put("devicePosition",i);
        	data.add(item);
    	}*/
        
        ListCursorAdapter cadapter = new ListCursorAdapter(this.getActivity(), mCursor){
        	
        	@Override
            public View getView (int position, View convertView, ViewGroup parent)
            {
        		View v = super.getView(position, convertView, parent);
        		
                //deviceNumber = position;  //收集device的編號
        		
                final ToggleButton b=(ToggleButton)v.findViewById(R.id.toggleButton_push);
                Map<String,Object> item = new HashMap<String,Object>();
                item.put("Position",position);
            	data.add(item);
                item = data.get(position);
                b.setTag(item.get("Position"));
                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                    	if(b.isChecked()){
                    		MainDeyrk.pushAD_count+=1;
                    		Toast.makeText(arg0.getContext().getApplicationContext(),"ischeck "+arg0.getTag(),Toast.LENGTH_SHORT).show();
                    	}
                    	else{
                    		MainDeyrk.pushAD_count-=1;
                    		Toast.makeText(arg0.getContext().getApplicationContext(),"NOTcheck "+arg0.getTag(),Toast.LENGTH_SHORT).show();
                    	}
                    }
                });
                return v;
            }
        };
        //實作ListCursorAdapter接口(設定要在畫面上顯示的ListView樣式，傳入ListView所在的activity跟mCursor來設定)
        
        /*
        this.getActivity().startManagingCursor(mCursor);
        
        String[] from_column = new String[]{DB.KEY_TITLE, DB.KEY_TIME};
        int[] to_layout = new int[]{android.R.id.text1, android.R.id.text2};
        
        SimpleCursorAdapter cadapter = new SimpleCursorAdapter(
        	getActivity(),
            android.R.layout.simple_list_item_2,
            mCursor, from_column, to_layout, 0);
        
        //(備份)SimpleCursorAdapter接口的寫法
        */
        
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
			
			TextView title =(TextView)view.findViewById(R.id.title_ad_push);
			TextView time = (TextView)view.findViewById(R.id.time_push);
			TextView kind = (TextView)view.findViewById(R.id.kind_push);
			//ImageView image = (ImageView)view.findViewById(R.id.image_ad);
			
			title.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TITLE)));
			time.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TIME)));
			kind.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_KIND)));
			
			//byte[] bb = cursor.getBlob(cursor.getColumnIndex(DB.KEY_IMAGE));
			//image.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
			//從cursor撈圖片出來轉檔放在ImageView的程式碼
			cursor.getColumnIndex(DB.KEY_TIME);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			View v =inflater.inflate(R.layout.listview_push,null);
			//設定ListView樣式
			bindView(v,context,cursor);
			
			return v;
		}
	}
	
	//當ListView有被點擊時運行的函式
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    
	    FragmentTransaction trans = getFragmentManager().beginTransaction();  
	    Fragment fragment = new PushCreateAD();
        trans.replace(R.id.root_push, fragment);
        Bundle args = new Bundle();
        args.putLong("section_id", id);
        
        fragment.setArguments(args);
        
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
        Log.d("test",""+id);
        //將root_push(推播訊息的底層容器)當前的fragment替換成PushCreateAD
	}
}
