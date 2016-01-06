package opengl.glexamples.glActivity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import opengl.glexamples.ContactDAO;
import opengl.glexamples.R;
import opengl.glexamples.ResideMenu.ResideMenu;
import opengl.glexamples.ResideMenu.ResideMenuItem;
import opengl.glexamples.UserEntity;

/**
 * Created by Angel on 15/12/25.
 */
public class MenuActivity extends FragmentActivity implements View.OnClickListener {
    MenuActivity mContext;
    ContactDAO contactdb;
    ResideMenu resideMenu;
    ResideMenuItem itemDefault;
    ResideMenuItem itemColleague;
    ResideMenuItem itemFamily;
    ResideMenuItem itemClassmate;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        contactdb=new ContactDAO(this);
        setUpMenu();
        synContactdb(this);
        if( savedInstanceState == null ){
            changeFragment(new DisplayContactFragment(),"0");
        }
    }

    public void setUpMenu(){
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.setScaleValue(1.0f);

        //create menu items
        itemDefault=new ResideMenuItem(mContext,R.drawable.icon_home,"默 认");
        itemColleague=new ResideMenuItem(mContext,R.drawable.icon_home,"同 事");
        itemFamily=new ResideMenuItem(mContext,R.drawable.icon_home,"家 人");
        itemClassmate=new ResideMenuItem(mContext,R.drawable.icon_home,"同 学");

        itemDefault.setOnClickListener(this);
        itemColleague.setOnClickListener(this);
        itemFamily.setOnClickListener(this);
        itemClassmate.setOnClickListener(this);

        itemDefault.setTag("0");
        itemColleague.setTag("1");
        itemFamily.setTag("2");
        itemClassmate.setTag("3");

        resideMenu.addMenuItem(itemDefault, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemColleague, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFamily, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemClassmate, ResideMenu.DIRECTION_LEFT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(mContext,AddContactActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
            return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        String position= (String) view.getTag();
        changeFragment(new DisplayContactFragment(),position);

        resideMenu.closeMenu();
    }





    private void changeFragment(Fragment targetFragment,String category){
        Bundle bundle=new Bundle();
        bundle.putString("category", category);

        resideMenu.clearIgnoredViewList();
        targetFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }


    public void synContactdb(Context context){
        //
        //contactdb.delete();
        //contactdb.update();

        int contactIdIndex=0;
        int nameIndex=0;
        String contactId; // Id of phone contacts
        String name;
        String phone=null;
        String email=null;


        //访问手机通讯录
        Cursor cursor= context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        //应用通讯录＝手机通讯录，则返回
        if(cursor.getCount()==contactdb.getCount()) return;

        if(cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }

        while (cursor.moveToNext()){
            contactId=cursor.getString(contactIdIndex);
            name=cursor.getString(nameIndex);

            /*
             * 查找该联系人的phone信息
             */
            Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if(phoneCursor.getCount() > 0) {
                phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while(phoneCursor.moveToNext()) {
                phone = phoneCursor.getString(phoneIndex);
            }

            /*
             * 查找该联系人的email信息
             */
            Cursor emailCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
                    null, null);
            int emailIndex = 0;
            if(emailCursor.getCount() > 0) {
                emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            }
            while(emailCursor.moveToNext()) {
                email = emailCursor.getString(emailIndex);
            }

            phoneCursor.close();
            emailCursor.close();

            //Id in application contacts
            int id=contactdb.getCount()+1;

            UserEntity user=new UserEntity();
            user.setId(Integer.toString(id));
            user.setName(name);
            user.setPhone(phone);
            user.setEmail(email);
            user.setCategory("0");
            user.setStyle("0");
            contactdb.insert(user);

        }
    }
}
