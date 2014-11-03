package com.example.Circle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.Circle.R;

public class PushMain extends ListFragment {
    
    private DB mDbHelper; //��@DB���O
    private Cursor mCursor; //���Ʈw��ƪ��e��
    private View rootView;
    private Button btn;
    
    public PushMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        rootView = inflater.inflate(R.layout.fragment_push_main, container, false);
        //�]�w�e���ҹ�����XML��
        
        /*
        dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
    	dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
    	//�qbundle���X��ƨӥ�
    	*/
        
    	btn = (Button) rootView.findViewById(R.id.createpushad);
    	btn.setOnClickListener(mbtn);
    	//�s�W�����T�������s
    	
    	setAdapter(rootView);
    	//��s�e����ListView
    	
    	return rootView;
    }
    
    //�s�W�����T�����s����ť��
    private OnClickListener mbtn = new OnClickListener() {
	    public void onClick(View v) {
	    	
	    	//���U�ɶ}��PushCreateAD
	    	
            FragmentTransaction trans = getFragmentManager().beginTransaction();

            //trans.replace(R.id.root_push, new PushCreateAD());
            Fragment fragment = new PushCreateAD();
            trans.replace(R.id.root_push, fragment);
            Bundle args = new Bundle();
            args.putLong("section_id", 0);
            fragment.setArguments(args);
            

            //trans.addToBackStack("PushMain");
            

            //root_push�O�Ӯe�� ,�Ψө�fragment
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.commit();
	    }
	};
	
	private void setAdapter(View rootView) {
		mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //���}DB
        
        mCursor = mDbHelper.getAll();
        //�I�sDB��getAll�禡�A���o��Ʃ�imCursor(��Ʈw��ƪ��e��)
        
        ListCursorAdapter cadapter = new ListCursorAdapter(this.getActivity(), mCursor);
        //��@ListCursorAdapter���f(�]�w�n�b�e���W��ܪ�ListView�˦��A�ǤJListView�Ҧb��activity��mCursor�ӳ]�w)
        
        /*
        this.getActivity().startManagingCursor(mCursor);
        
        String[] from_column = new String[]{DB.KEY_TITLE, DB.KEY_TIME};
        int[] to_layout = new int[]{android.R.id.text1, android.R.id.text2};
        
        SimpleCursorAdapter cadapter = new SimpleCursorAdapter(
        	getActivity(),
            android.R.layout.simple_list_item_2,
            mCursor, from_column, to_layout, 0);
        
        //(�ƥ�)SimpleCursorAdapter���f���g�k
        */
        
        setListAdapter(cadapter);
        //���汵�f
        
	}
	
	//ListCursorAdapter���f
	public class ListCursorAdapter extends CursorAdapter{
		
		public ListCursorAdapter(Context context,Cursor c){
			super(context,c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			//�qcursor�̮���ƥX�ө��e���W
			
			TextView title =(TextView)view.findViewById(R.id.title_ad_push);
			TextView time = (TextView)view.findViewById(R.id.time_push);
			TextView kind = (TextView)view.findViewById(R.id.kind_push);
			//ImageView image = (ImageView)view.findViewById(R.id.image_ad);
			
			title.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TITLE)));
			time.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TIME)));
			kind.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_KIND)));
			
			//byte[] bb = cursor.getBlob(cursor.getColumnIndex(DB.KEY_IMAGE));
			//image.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
			//�qcursor���Ϥ��X�����ɩ�bImageView���{���X
			cursor.getColumnIndex(DB.KEY_TIME);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			View v =inflater.inflate(R.layout.listview_push,null);
			//�]�wListView�˦�
			bindView(v,context,cursor);
			
			return v;
		}
	}
	
	//��ListView���Q�I���ɹB�檺�禡
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    

	    FragmentTransaction trans = getFragmentManager().beginTransaction();  
	    Fragment fragment = new PushCreateAD();
        trans.replace(R.id.root_push, fragment);
        Bundle args = new Bundle();
        args.putLong("section_id", id);
        
        fragment.setArguments(args);

	   

        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
        Log.d("test",""+id);
        //�Nroot_push(�����T�������h�e��)��e��fragment������PushCreateAD
	}
}
