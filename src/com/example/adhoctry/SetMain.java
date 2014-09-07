package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SetMain extends Fragment {
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    TextView dummyTextView;
    public NumberPicker numPicker;

    public SetMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_set_main, container, false);
        /*
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
    	*/
    	
    	numPicker =  (NumberPicker) rootView.findViewById(R.id.delete_ad_time);
    	
    	numPicker.setMaxValue(48);  
        numPicker.setMinValue(0);    
        numPicker.setValue(24);
    	
    	return rootView;
    }
}
