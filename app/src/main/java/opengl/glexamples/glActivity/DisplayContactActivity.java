//package opengl.glexamples.glActivity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//
//import com.twotoasters.jazzylistview.effects.SlideInEffect;
//import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;
//import java.util.ArrayList;
//
//import opengl.glexamples.ContactDatabase;
//import opengl.glexamples.R;
//import opengl.glexamples.ResideMenu.ResideMenu;
//import opengl.glexamples.ResideMenu.ResideMenuItem;
//import opengl.glexamples.adapter.ContactItemAdapter;
//
///**
// * Created by Angel on 15/12/16.
// */
//public class DisplayContactActivity extends Activity{
//    public RecyclerView contactRecyclerView;
//    public EditText inputSearchQuery;
//    public ResideMenu resideMenu;
//
//    private ContactDatabase contactdb;
//    private ArrayList<String> usernames;
//    LinearLayoutManager linearLayoutManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_display_contact);
//
//        contactdb=new ContactDatabase();
//        synContact(this);    //如果手机通讯录中有新添加的联系人，同步到本应用的数据库中
//        usernames=contactdb.getAllNames();
//        linearLayoutManager=new LinearLayoutManager(this);
//
//
//
//        contactRecyclerView = (RecyclerView)findViewById(R.id.contact_recycler_view);
//        contactRecyclerView.setLayoutManager(linearLayoutManager);
//        contactRecyclerView.setAdapter(new ContactItemAdapter(this, usernames));
//        contactRecyclerView.setOnScrollListener(new JazzyRecyclerViewScrollListener());
//        //contactRecyclerView.setTransitionEffect(new SlideInEffect());
//
//
//        inputSearchQuery=(EditText)findViewById(R.id.input_search_query);
//        inputSearchQuery.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String username = inputSearchQuery.getText().toString();
//                searchAndDisplayUser(username);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        // attach to current activity;
//        resideMenu = new ResideMenu(this);
//        resideMenu.setBackground(R.drawable.menu_background);
//        resideMenu.attachToActivity(this);
//
//        // create menu items;
//        ResideMenuItem[] items=new ResideMenuItem[4];
//        String titles[] = { "Default", "Colleagues", "Families", "Schoolmates" };
//        int icon[] = { R.drawable.icon_home, R.drawable.icon_profile, R.drawable.icon_calendar, R.drawable.icon_settings };
//
//        for (int i = 0; i < titles.length; i++){
//            items[i] = new ResideMenuItem(this, icon[i], titles[i]);
//            items[i].setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    //Todo:Get item id and find group
//                    int group = (Integer) v.getTag();
//                    ArrayList<String> usernames=contactdb.getNamesByGroup(group);
//                    contactRecyclerView.setAdapter(new ContactItemAdapter(this, usernames));
//                    contactRecyclerView.setOnScrollListener(new JazzyRecyclerViewScrollListener());
//                }
//            });
//            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
//        }
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void synContact(Context context){
//        //
////        contactdb.delete();
//        contactdb.update();
//
//        int contactIdIndex=0;
//        int nameIndex=0;
//        String contactId;
//        String name;
//        String phone=null;
//        String email=null;
//        int group;
//
//        //访问手机通讯录
//        Cursor cursor= context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null, null);
//
//        //应用通讯录＝手机通讯录，则返回
//        Integer count=cursor.getCount();
//        Integer count_db=contactdb.getCount(); //count items in ContactDatabase
//        if(count==contactdb.getCount()) return;
//
//        if(count > 0) {
//            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//        }
//
//        while (cursor.moveToNext()){
//            contactId=cursor.getString(contactIdIndex);
//            name=cursor.getString(nameIndex);
//
//            //联系人在应用的数据库中
//            if(contactdb.findUserByName(name)!=-1) break;
//
//
//            /*
//             * 查找该联系人的phone信息
//             */
//            Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
//                    null, null);
//            int phoneIndex = 0;
//            if(phoneCursor.getCount() > 0) {
//                phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            }
//            while(phoneCursor.moveToNext()) {
//                phone = phoneCursor.getString(phoneIndex);
//            }
//
//            /*
//             * 查找该联系人的email信息
//             */
//            Cursor emailCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
//                    null, null);
//            int emailIndex = 0;
//            if(emailCursor.getCount() > 0) {
//                emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//            }
//            while(emailCursor.moveToNext()) {
//                email = emailCursor.getString(emailIndex);
//            }
//
//            phoneCursor.close();
//            emailCursor.close();
//            contactdb.insert(count_db.toString(),name,phone,email,0);
//
//            count_db++;
//
//        }
//    }
//
//
//    public void searchAndDisplayUser(String username){
//        int idx=contactdb.findUserByName(username);
//
//        if(-1==idx){
//                    //找不到联系人
//        }
//
//        //找到联系人
//        linearLayoutManager.scrollToPositionWithOffset(idx,20);
//
//    }
//
//}
