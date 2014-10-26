package com.example.Circle;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Circle.R;

public class CollectionAD extends Fragment {
	
	private View rootView;
	
	public CollectionAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_collection_ad, container, false);
    	//設定畫面所對應的XML檔
    	
    	return rootView;
    }
}
