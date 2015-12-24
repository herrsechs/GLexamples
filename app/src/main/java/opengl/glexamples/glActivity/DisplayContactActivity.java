package opengl.glexamples.glActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.twotoasters.jazzylistview.effects.SlideInEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;

import opengl.glexamples.R;
import opengl.glexamples.adapter.ContactItemAdapter;

/**
 * Created by Angel on 15/12/16.
 */
public class DisplayContactActivity extends Activity{
    public RecyclerView contactRecyclerView;
    public EditText inputSearchQuery;
    private ArrayList<String> usernames;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usernames=getUsernames(this);
        linearLayoutManager=new LinearLayoutManager(this);
        setContentView(R.layout.layout_display_contact);


        contactRecyclerView = (RecyclerView)findViewById(R.id.contact_recycler_view);
        contactRecyclerView.setLayoutManager(linearLayoutManager);
        contactRecyclerView.setAdapter(new ContactItemAdapter(this, usernames));
        contactRecyclerView.setOnScrollListener(new JazzyRecyclerViewScrollListener());
        //contactRecyclerView.setTransitionEffect(new SlideInEffect());


        inputSearchQuery=(EditText)findViewById(R.id.input_search_query);
        inputSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = inputSearchQuery.getText().toString();
                searchAndDisplayUser(username);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<String> getUsernames(Context context){
        ArrayList<String> usernames=new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        //int contactIdIndex = 0;
        int nameIndex = 0;

        if(cursor.getCount() > 0) {
            //contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }


        while(cursor.moveToNext()) {
            //String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            usernames.add(name);
        }
        return usernames;
    }

    public int findUserByName(String username){
        int idx=0;
        for(;idx<usernames.size();idx++){
            // Find User
            if(usernames.get(idx).equals(username)) break;
        }
        //Cannot Find User
        if(idx==usernames.size()) idx=-1;
        return idx;
    }

    public void searchAndDisplayUser(String username){
        int idx=findUserByName(username);

        if(-1==idx){
                    //找不到联系人
        }

        //找到联系人
        linearLayoutManager.scrollToPositionWithOffset(idx,20);

    }

}
