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

import opengl.glexamples.ContactDatabase;
import opengl.glexamples.R;
import opengl.glexamples.UserEntity;

/**
 * Created by Angel on 15/12/25.
 */
public class DisplayContactFragment extends Fragment {
    private View parentView;
    //private JazzyListView listView;
    private SwipeMenuListView listView;
    private EditText inquireContact;
    private ContactDatabase contactdb;
    private String group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.contact, container, false);
        listView   = (SwipeMenuListView) parentView.findViewById(R.id.listView);
        inquireContact= (EditText) parentView.findViewById(R.id.input_search_query);
        contactdb=new ContactDatabase();
        group=getArguments().getString("group");
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
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(150);
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(150);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                findUserByGroup(group));

        // set creator
        listView.setMenuCreator(creator);
        listView.setAdapter(arrayAdapter);
        //listView.setTransitionEffect(new FanEffect());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getContext(), CurlActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // edit
                        // Get User which is selected
                        String id=Integer.toString(position);
                        UserEntity user=contactdb.findUserById(id);
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

        private ArrayList<String> findUserByGroup(String group){
        ArrayList<String> usernames;
        usernames=contactdb.findUserByGroup(group);
        return usernames;
    }

        public void searchAndDisplayUser(String username){
        int idx=contactdb.findUserByName(username);

        if(-1==idx){}

        //找到联系人
        listView.smoothScrollToPositionFromTop(idx,20);
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
