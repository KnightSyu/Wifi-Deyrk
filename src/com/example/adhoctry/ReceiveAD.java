package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceiveAD extends Fragment {
	
	private View rootView;
	
	public ReceiveAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_receive_ad, container, false);
    	//設定畫面所對應的XML檔
    	
    	return rootView;
    }
}
