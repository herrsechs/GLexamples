package opengl.glexamples.glActivity;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import opengl.glexamples.surfaceView.IDCardSurfaceView;
import opengl.glexamples.surfaceView.TriTextureSurfView;

/**
 * Created by LLLLLyj on 2015/10/18.
 */
public class IDCardActivity extends AppCompatActivity {
    private IDCardSurfaceView mGLSurfaceView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mGLSurfaceView = new IDCardSurfaceView(this);
        setContentView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控
    }
}
