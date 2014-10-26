package com.example.Circle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Circle.R;

public class CollectionRoot extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.root_collection, container, false);
		//�]�w�H���e��

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		Fragment fm = new CollectionMain();
		fm.setArguments(getArguments());
		//�~��ǻ�bundle
		transaction.replace(R.id.root_collection, fm);
		transaction.commit();
		//�b�e���W�ЫزĤ@����fragment(�U����������)
		
		return view;
	}
}
