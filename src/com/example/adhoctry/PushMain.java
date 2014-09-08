package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PushMain extends ListFragment {
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    private DB mDbHelper;
    private Cursor mCursor;
    
    public PushMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_push_main, container, false);
        
        /*
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
    	*/
        
    	Button btn = (Button) rootView.findViewById(R.id.createpushad);
    	btn.setOnClickListener(mbtn);
    	
    	setAdapter(rootView);
    	//建構DB
    	
    	return rootView;
    }
    
    private OnClickListener mbtn = new OnClickListener() {
	    public void onClick(View v) {
	    	
            FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_push, new PushCreateAD());  
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack("PushMain");  
            trans.commit();
	    }
	};
	
	private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //打開DB
        
        mCursor = mDbHelper.getAll();
        //取得資料
        
        this.getActivity().startManagingCursor(mCursor);
        
        String[] from_column = new String[]{DB.KEY_TITLE, DB.KEY_TIME};
        int[] to_layout = new int[]{android.R.id.text1, android.R.id.text2};
        
        SimpleCursorAdapter cadapter = new SimpleCursorAdapter(
        	getActivity(),
            android.R.layout.simple_list_item_2,
            mCursor, from_column, to_layout, 0);
        //設定接口
        
        setListAdapter(cadapter);
        //執行接口
        
	}
}
