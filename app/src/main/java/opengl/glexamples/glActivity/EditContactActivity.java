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
import android.widget.TextView;
import android.widget.Toast;

import opengl.glexamples.ContactDAO;
import opengl.glexamples.R;
import opengl.glexamples.UserEntity;

public class EditContactActivity extends AppCompatActivity {
    private Context context;
    private EditText name;
    private EditText phone;
    private EditText email;
    private Spinner category;
    private String[] categoryType;
    private ArrayAdapter adapter;
    private UserEntity user;
    private Button done;
    private Button cancel;
    private ContactDAO contactdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        context=this;
        contactdb=new ContactDAO(this);
        user = (UserEntity)getIntent().getParcelableExtra("user");
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
        category= (Spinner) findViewById(R.id.spinner_category);
        done= (Button) findViewById(R.id.button_done);
        cancel= (Button) findViewById(R.id.button_cancel);

        //Get name from EditText
        name.setText(user.getName(), TextView.BufferType.EDITABLE);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String _name = name.getText().toString();
                user.setName(_name);
            }
        });

        //Get phone from EditText
        phone.setText(user.getPhone(), TextView.BufferType.EDITABLE);
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
        email.setText(user.getEmail(), TextView.BufferType.EDITABLE);
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

        //Get category form Spinner
        categoryType=new String[]{"默认","同事","家人","同学"};
        adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,categoryType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        category.setAdapter(adapter);
        category.setVisibility(View.VISIBLE);//设置默认值
        category.setOnItemSelectedListener(new SpinnerSelectedListener());

        //Submit and create new user
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                contactdb.update(user);
                Toast.makeText(context, "Edit Successfully!", Toast.LENGTH_LONG).show();
                finish();

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
            Integer _category=position;
            user.setCategory(_category.toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }
}
