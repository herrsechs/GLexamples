package opengl.glexamples.glActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import opengl.glexamples.MainActivity;
import opengl.glexamples.R;

public class StartupAnimationActivity extends AppCompatActivity {
    private ImageView image;
    private Handler handler;
    private final long SPLASH_LENGTH = 4000;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getSupportActionBar().hide();
        setContentView(R.layout.anim);
        image = (ImageView)findViewById(R.id.startup_animation_view);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartupAnimationActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_LENGTH);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        image.setBackgroundResource(R.drawable.frame_anim);
        AnimationDrawable anim = (AnimationDrawable)image.getBackground();
        anim.start();
    }

}
