package opengl.glexamples.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import opengl.glexamples.R;

/**
 * Created by Angel on 15/12/16.
 */
public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ContactItemViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private ArrayList<String> usernames=new ArrayList<>();
    private static final String TAG = "ContactAdapter";

    public static String[] CONTACT_PROJECTION=new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
    };


    public ArrayList<String > getContact(){

        ArrayList<String> usernames=new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        Cursor phones = null;
        Cursor emails=  null;
        int contactIdIndex = 0;
        int nameIndex = 0;

        if(cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }


        while(cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);

            Log.i(TAG, contactId);
            Log.i(TAG, name);
            usernames.add(name);

            /*
             * 查找该联系人的phone信息
             */
            phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if(phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while(phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                Log.i(TAG, phoneNumber);
            }

            /*
             * 查找该联系人的email信息
             */
            emails = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
                    null, null);
            int emailIndex = 0;
            if(emails.getCount() > 0) {
                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            }
            while(phones.moveToNext()) {
                String email = emails.getString(emailIndex);
                Log.i(TAG,email);
            }
        }
        cursor.close();
        phones.close();
        emails.close();
        return usernames;
    }


    public ContactItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.usernames=getContact();
    }

    public int getItemCount(){
        return this.usernames.size();
    }

    @Override
    public ContactItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView cardView=(CardView)layoutInflater.inflate(R.layout.contact_item,viewGroup,false);
        return new ContactItemViewHolder(cardView);
    }

    public void onBindViewHolder(ContactItemViewHolder holder,int position){
        holder.nameView.setText(this.usernames.get(position));
    }


    public static class ContactItemViewHolder extends RecyclerView.ViewHolder{
        private TextView nameView;
        private ImageView userImg;

        public ContactItemViewHolder(View view){
            super(view);
            this.nameView=(TextView)view.findViewById(R.id.nameView);
            this.userImg=(ImageView)view.findViewById(R.id.userImg);
        }
    }
}
