package opengl.glexamples.glActivity;

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
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.FanEffect;
import com.twotoasters.jazzylistview.effects.FlyEffect;

import java.util.ArrayList;

import opengl.glexamples.ContactDatabase;
import opengl.glexamples.R;

/**
 * Created by Angel on 15/12/25.
 */
public class DisplayContactFragment extends Fragment {
    private View parentView;
    private JazzyListView listView;
    private EditText editText;
    private ContactDatabase contactdb;
    private String group;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.contact, container, false);
        listView   = (JazzyListView) parentView.findViewById(R.id.listView);
        editText= (EditText) parentView.findViewById(R.id.input_search_query);
        contactdb=new ContactDatabase();
        group=getArguments().getString("group");
        initView();
        return parentView;
    }

    private void initView(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                findUserByGroup(group));
        listView.setAdapter(arrayAdapter);
        listView.setTransitionEffect(new FanEffect());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = editText.getText().toString();
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

        if(-1==idx){
                    //找不到联系人
        }

        //找到联系人
        listView.smoothScrollToPositionFromTop(idx,20);

    }
}
