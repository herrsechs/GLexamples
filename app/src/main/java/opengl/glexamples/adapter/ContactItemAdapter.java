package opengl.glexamples.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import opengl.glexamples.R;

/**
 * Created by Angel on 15/12/16.
 */
public class ContactItemAdapter extends ArrayAdapter<String> {
    protected static final int[] itemColor=new int[]{
            0xFFF7A639,
            0xFFF7A07D,
            0xFFF9A9A1,
            0xFFFCB7B7,
            0xFFFAD2D2,
            0xFFF7D5F3,
            0xFFD8D6F6,
            0xFFB5C7E5,
            0xFFA3C9E1
    };


    public ContactItemAdapter(Context context,ArrayList<String> names) {
        super(context,0,names);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent){

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item,parent,false);
        }



        String name=getItem(position);
        TextView tvName=(TextView)convertView.findViewById(R.id.tvName);
        for(int i=0;i<9;i++){
            if(i==position%9){
                convertView.setBackgroundColor(itemColor[i]);
            }
        }

        tvName.setText(name);

        return convertView;
    }

}
