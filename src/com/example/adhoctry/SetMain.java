package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SetMain extends Fragment {
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    TextView dummyTextView;
    public NumberPicker numPicker;

    public SetMain() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_set_main, container, false);
        /*
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
    	*/
    	
    	Button btn = (Button) rootView.findViewById(R.id.tellous);
    	Button alertbtn = (Button) rootView.findViewById(R.id.aboutous);
    	
    	
		
    	btn.setOnClickListener(new OnClickListener(){
    		
    		@Override
    		public void onClick(View v)	{
    			Intent emailIntent = new Intent(Intent.ACTION_SEND);
    			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String []{"Cjcu4402@godlike.com"});
    			//�ƥΦ^���H�c
    			//emailIntent.putExtra(Intent.EXTRA_CC, new String[]{"Cjcu4402@godlike.com"});
    			//emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{"Cjcu4402@godlike.com"});
    			
    			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "���D");
    			emailIntent.putExtra(Intent.EXTRA_TEXT, "���D���e");
    			
    			emailIntent.setType("message/rfc822");
    			startActivity(Intent.createChooser(emailIntent, "�п�ܱH�H�覡..."));
    		}
    		
    	});
    	
    	alertbtn.setOnClickListener(new OnClickListener(){
    		
    		public void onClick(View v)
    		{
    			new AlertDialog.Builder(getActivity())
    			.setTitle("����ڭ�!")
    			.setMessage("����������������������\n" +
    						"�խ��A���ɦѮv���ܫӡI\n" +
    						"����������������������")
    			//.setNeutralButton("�T�w", null)
    			.show();
    					
    		}
    		/*
    		@SuppressWarnings("unused")
			public void alertbtn(View v)
    		{
    			new AlertDialog.Builder(getActivity())
    			.setTitle("����ڭ�!")
    			.setMessage("����������������������\n" +
    						"�խ��A���ɦѮv���ܫӡI\n" +
    						"����������������������")
    			.setNeutralButton("�T�w", null)
    			.show();
    					
    		}
			*/
    		
    	});
    	
    	
    	numPicker =  (NumberPicker) rootView.findViewById(R.id.delete_ad_time);
    	numPicker.setMaxValue(48);  
        numPicker.setMinValue(0);    
        numPicker.setValue(24);
    	
    	return rootView;
    }
}
