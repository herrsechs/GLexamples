package opengl.glexamples.glActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import opengl.glexamples.ContactDatabase;
import opengl.glexamples.R;
import opengl.glexamples.UserEntity;

public class EditProfileActivity extends AppCompatActivity {
    private Context context;
    private EditText name;
    private EditText phone;
    private EditText email;
    private Spinner group;
    private String[] groupType;
    private ArrayAdapter adapter;
    private UserEntity user;
    private Button done;
    private Button cancel;
    private ContactDatabase contactdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        context=this;
        user=new UserEntity();
        contactdb=new ContactDatabase();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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

    public void initView(){
        //Find view by id
        name= (EditText) findViewById(R.id.edit_name);
        phone= (EditText) findViewById(R.id.edit_phone);
        email= (EditText) findViewById(R.id.edit_email);
        group= (Spinner) findViewById(R.id.spinner_group);
        done= (Button) findViewById(R.id.button_done);
        cancel= (Button) findViewById(R.id.button_cancel);

        //Get name from EditText
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String _name=name.getText().toString();
                user.setName(_name);
            }
        });

        //Get phone from EditText
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String _phone=phone.getText().toString();
                user.setPhone(_phone);
            }
        });

        //Get email from EditText
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String _email=email.getText().toString();
                user.setEmail(_email);
            }
        });

        //Get group form Spinner
        groupType=new String[]{"默认","同事","家人","同学"};
        adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,groupType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        group.setAdapter(adapter);
        group.setVisibility(View.VISIBLE);//设置默认值
        group.setOnItemSelectedListener(new SpinnerSelectedListener());

        //Submit and create new user
        done.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Integer id=contactdb.getCount()+1;
                contactdb.insert(id.toString(), user.getName(), user.getPhone(), user.getEmail());
                Toast.makeText(context, "Add Successfully!", Toast.LENGTH_LONG).show();

            }
        });

        // Cancel Creating User
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Integer _group=position;
            user.setGroup(_group.toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }
}
