package opengl.glexamples.glActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import opengl.glexamples.surfaceView.TriTextureSurfView;

/**
 * Created by LLLLLyj on 2015/9/27.
 */
public class TextureTriangleActivity extends AppCompatActivity {

    private TriTextureSurfView mGLSurfaceView;
    static boolean threadFlag;//纹理矩形绕X轴旋转工作标志位

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为竖屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //切换到主界面

        //初始化GLSurfaceView
        mGLSurfaceView = new TriTextureSurfView(this);
        setContentView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控
    }
}
