package opengl.glexamples.glActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

import opengl.glexamples.ContactDAO;
import opengl.glexamples.R;
import opengl.glexamples.UserEntity;
import opengl.glexamples.adapter.ContactItemAdapter;

/**
 * Created by Angel on 15/12/25.
 */
public class DisplayContactFragment extends Fragment {
    private View parentView;
    //private JazzyListView listView;
    private SwipeMenuListView listView;
    private EditText inquireContact;
    private ContactDAO contactdb;
    private String category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.contact, container, false);
        listView   = (SwipeMenuListView) parentView.findViewById(R.id.listView);
        inquireContact= (EditText) parentView.findViewById(R.id.input_search_query);
        contactdb=new ContactDAO(this.getContext());
        category=getArguments().getString("category");
        initView();
        return parentView;
    }

    private void initView(){
        //Initial ListView and Swipe Menu
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF5, 0x92,
                        0x0F)));
                openItem.setWidth(150);
                openItem.setTitle("Edit");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0x32,
                        0x6B, 0x92)));
                deleteItem.setWidth(150);
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        ContactItemAdapter arrayAdapter=new ContactItemAdapter(
                getContext(),
                findUserByCategory(category));


        // set creator
        listView.setMenuCreator(creator);
        listView.setAdapter(arrayAdapter);
        //listView.setTransitionEffect(new FanEffect());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // edit
                        // Get User which is selected
                        String id=Integer.toString(position+1); //position starts from 0 while contact starts from 1
                        UserEntity user=contactdb.getUserById(id);
                        // Bind in intent
                        Intent intent=new Intent();
                        Bundle bundle=new Bundle();

                        bundle.putParcelable("user",user);
                        intent.putExtras(bundle);
                        intent.setClass(getContext(), EditContactActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        // delete
                        deleteUser(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        // Left
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        //Initial Search Bar
        inquireContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = inquireContact.getText().toString();
                searchAndDisplayUser(username);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        }

        private ArrayList<String> findUserByCategory(String category){
        ArrayList<String> usernames;
        usernames=contactdb.getUserByCategory(category);
        return usernames;
    }

        public void searchAndDisplayUser(String username){
        int idx=contactdb.getUserByName(username);

        if(-1==idx){ return;}

        //找到联系人
        listView.smoothScrollToPosition(idx+1);
        }

        public void deleteUser(final int position){
            //Confirm whether to delete
            new AlertDialog.Builder(getContext())
                    .setTitle("确认")
                    .setMessage("确认删除")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            contactdb.delete(position);
                            int d=contactdb.getCount();
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }
}
