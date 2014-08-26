package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ActivityReceive extends ListFragment {
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    TextView dummyTextView;
    
    private DB mDbHelper;
    private Cursor mCursor;

    public ActivityReceive() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_receive, container, false);
        
        Button btn = (Button) rootView.findViewById(R.id.button1);
        btn.setOnClickListener(mbtn1);
        //建構按鈕功能
        
        Button btn2 = (Button) rootView.findViewById(R.id.button2);
    	btn2.setOnClickListener(mbtn2);
    	//按下ad跳出ad畫面，以後是按下listview的ad跳出ad
        
        setAdapter(rootView);
        //建構DB
        
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	//dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
        //顯示頁面數字
    	
    	return rootView;
    }

	private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //打開DB
        
        mCursor = mDbHelper.getAll();
        //取得資料
        
        this.getActivity().startManagingCursor(mCursor);
        
        String[] from_column = new String[]{DB.KEY_ITEM, DB.KEY_CREATED};
        int[] to_layout = new int[]{android.R.id.text1, android.R.id.text2};
        
        SimpleCursorAdapter cadapter = new SimpleCursorAdapter(
        	getActivity(),
            android.R.layout.simple_list_item_2,
            mCursor, from_column, to_layout, 0);
        //設定接口
        
        setListAdapter(cadapter);
        //執行接口
        
	}
	
	private OnClickListener mbtn1 = new OnClickListener() {
	    public void onClick(View v) {
	    	mDbHelper = new DB(getActivity());
            mDbHelper.open();
            dummyTextView.setText("123");
            mDbHelper.create("77.77");
            mDbHelper.close();
            //按鈕按下時新增一筆資料("77.77")給資料庫並關閉DB
	    }
	};
	
	private OnClickListener mbtn2 = new OnClickListener() {
	    public void onClick(View v) {
	    	
            FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_frame, new ReceiveAD());  
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);  
            trans.commit();
	    }
	};
}