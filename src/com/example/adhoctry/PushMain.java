package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PushMain extends Fragment {
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    TextView dummyTextView;

    public PushMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_push_main, container, false);
        
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
        
    	Button btn = (Button) rootView.findViewById(R.id.createpushad);
    	btn.setOnClickListener(mbtn);
    	
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
}
