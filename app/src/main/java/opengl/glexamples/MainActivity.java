package opengl.glexamples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import opengl.glexamples.glActivity.CreateIDCardActivity;
import opengl.glexamples.glActivity.IDCardActivity;
import opengl.glexamples.glActivity.SkyBoxActivity;
import opengl.glexamples.glActivity.TextureTriangleActivity;

public class MainActivity extends AppCompatActivity {
    Button textureTriangle;
    Button idcard;
    Button createIDCard;
    Button skybox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureTriangle = (Button)findViewById(R.id.texture_triangle);
        textureTriangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TextureTriangleActivity.class);
                startActivity(intent);
            }
        });

        idcard = (Button)findViewById(R.id.id_card);
        idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, IDCardActivity.class);
                startActivity(intent);
            }
        });

        createIDCard = (Button)findViewById(R.id.creat_id_card);
        createIDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CreateIDCardActivity.class);
                startActivity(intent);
            }
        });

        skybox = (Button)findViewById(R.id.sky_box);
        skybox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SkyBoxActivity.class);
                startActivity(intent);
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
}
