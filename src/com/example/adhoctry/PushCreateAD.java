package com.example.adhoctry;

import java.io.FileNotFoundException;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class PushCreateAD extends Fragment {
	
	private View rootView;
	private static final int RESULT_OK = 1; //�粒�Ϥ��^�Ǫ��P�_��
	private DB mDbHelper; //��@DB���O
	
    private EditText push_title, push_context;
    private ImageView imageView;
    private Bitmap bitmap;
    private Spinner spinner;
    private String[] kind = {"�\�U��", "�t����", "�p�Y��", "�A����", "3C�q�l��",
    						"�ͬ��a����", "�q���C����", "�𶢰�����", "�O�i�B��~��", "�����y��"};
    private ArrayAdapter<String> kindList;
    private Button addimage, save, cancel;
    //�ŧi�e�������ܼ�
    
	public PushCreateAD() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.fragment_push_create, container, false);
    	//�]�w�e���ҹ�����XML��
    	
    	addimage = (Button)rootView.findViewById(R.id.button_addimage);
    	addimage.setOnClickListener(ibtn);
    	//�W�[�Ϥ������s
    	
    	save = (Button)rootView.findViewById(R.id.addad_ok);
    	save.setOnClickListener(sbtn);
    	//�x�s�����s
    	
    	cancel = (Button)rootView.findViewById(R.id.addad_cancel);
    	cancel.setOnClickListener(cbtn); 
    	//���������s
    	
    	push_title = (EditText)rootView.findViewById(R.id.input_adtitle);
    	push_context = (EditText)rootView.findViewById(R.id.input_adtext);
    	//���o���D�򤺤�
    	
    	spinner = (Spinner)rootView.findViewById(R.id.spinner_kind);
    	kindList = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, kind);
    	spinner.setAdapter(kindList);
    	//��������
    	
    	return rootView;
    }
    
    //�x�s���s����ť��
    private OnClickListener sbtn = new OnClickListener(){
    	public void onClick(View v){
    		
    		//�s�J�@�����
    		
    		mDbHelper = new DB(getActivity());
            mDbHelper.open();
            mDbHelper.create(push_title.getText().toString(), bitmap);
            //�I�screate�ǤJ�ѼƵ�DB���x�s�@�����
            
            String kind = spinner.getSelectedItem().toString();
            //kind�O�ҿ諸����
            
            FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_push, new PushMain());  
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack("PushMain");  
            trans.commit();
    		//�����e���^PushMain
    		
    	}
    };
    
    //�������s����ť��
    private OnClickListener cbtn = new OnClickListener(){
    	public void onClick(View v){
    		
    		FragmentTransaction trans = getFragmentManager().beginTransaction();  
            trans.replace(R.id.root_push, new PushMain());  
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //trans.addToBackStack("PushMain");  
            trans.commit();
            //�����e���^PushMain
    	}
    	
    };
    
    //�W�[�Ϥ����s����ť��
    private OnClickListener ibtn = new OnClickListener() {
	    public void onClick(View v) {
	    	
	    	//����Ϥ�
	    	
	    	Intent intent = new Intent();
	    	intent.setType("image/*");
	    	intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
            //�}�ҿ���Ϥ�������
	    }
	    
	};
	
	//����Ϥ����\��
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		
		//�p�GrequestCode=1�A�N�}�l�ʧ@
		if(requestCode == RESULT_OK){
			
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			//���o�Ϥ������|
			ContentResolver cr = this.getActivity().getContentResolver();
			
		
			try{
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				imageView = (ImageView)rootView.findViewById(R.id.image_ad);
				imageView.setImageBitmap(bitmap);
				//���o�Ϥ��নbitmap�榡��iimageView
	            
			} catch (FileNotFoundException e){
				Log.e("Exception", e.getMessage(),e);
			}
		
		}
		
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
