package com.example.adhoctry;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CollectionAD extends Fragment {
	
	private View rootView;
	
	public CollectionAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_collection_ad, container, false);
    	//�]�w�e���ҹ�����XML��
    	
    	return rootView;
    }
}
