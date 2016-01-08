package opengl.glexamples.glActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import opengl.glexamples.R;
import opengl.glexamples.UserEntity;
import opengl.glexamples.surfaceView.IDCardSurfaceView;
import opengl.glexamples.surfaceView.SingleIDCardSurfaceView;

public class ShowIDCardActivity extends AppCompatActivity {
    private SingleIDCardSurfaceView sv;
    private ImageButton return_btn;
    private ImageButton del_btn;
    private ImageButton send_btn;
    private UserEntity user;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getSupportActionBar().hide();
        setContentView(R.layout.create_id_card);

        user = getIntent().getParcelableExtra("user");

        this.sv         = (SingleIDCardSurfaceView)findViewById(R.id.single_id_card_surface_view);
        this.sv.setUser(user);

        this.return_btn = (ImageButton)findViewById(R.id.id_card_return_btn);
        this.del_btn    = (ImageButton)findViewById(R.id.id_card_delete_btn);
        this.send_btn   = (ImageButton)findViewById(R.id.id_card_send_btn);
        this.sv.requestFocus();
        this.sv.setFocusableInTouchMode(true);
        bindBtnFunction();
    }

    private void bindBtnFunction(){
        this.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.setStartExplosion();
                Toast.makeText(ShowIDCardActivity.this, "删除成功", Toast.LENGTH_LONG).show();
            }
        });
        this.return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.resetCardPosition();
            }
        });
        this.send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putParcelable("user",user);
                intent.putExtras(bundle);
                intent.setClass(ShowIDCardActivity.this, BluetoothChat.class);
                startActivity(intent);
            }
        });
    }


}

