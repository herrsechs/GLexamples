package opengl.glexamples.glActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import opengl.glexamples.surfaceView.ParticleSystemSurfaceView;

/**
 * Created by LLLLLyj on 2015/10/26.
 */
public class ParticleSystemActivity extends AppCompatActivity {
    private ParticleSystemSurfaceView mGLSurfaceView;
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //设置为全屏

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //初始化GLSurfaceView
        mGLSurfaceView = new ParticleSystemSurfaceView(this);
        setContentView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控
    }


}
