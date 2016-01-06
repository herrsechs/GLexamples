package opengl.glexamples.glActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import opengl.glexamples.R;
import opengl.glexamples.surfaceView.IDCardSurfaceView;
import opengl.glexamples.surfaceView.SingleIDCardSurfaceView;

public class ShowIDCardActivity extends AppCompatActivity {
    private SingleIDCardSurfaceView sv;
    private Button return_btn;
    private Button del_btn;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.create_id_card);
        this.sv         = (SingleIDCardSurfaceView)findViewById(R.id.single_id_card_surface_view);
        this.return_btn = (Button)findViewById(R.id.id_card_return_btn);
        this.del_btn    = (Button)findViewById(R.id.id_card_delete_btn);
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
    }


}

