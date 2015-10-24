package opengl.glexamples.glActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import opengl.glexamples.R;

/**
 * Created by LLLLLyj on 2015/10/20.
 */
public class CreateIDCardActivity extends AppCompatActivity {
    ImageView idImgView;
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.create_id_card);
        idImgView = (ImageView)findViewById(R.id.id_card_image_view);
        Bitmap bmp = Bitmap.createBitmap(1200, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setTextSize(60);
        //Drawable doge = getResources().getDrawable(R.drawable.doge, null);
        Bitmap doge = BitmapFactory.decodeResource(getResources(), R.drawable.doge);
        Rect photo = new Rect(0,0, 400, 400);
        String name = "姓名：Doge";
        String gender = "性别：雄";
        String position = "职业：娱乐明星";
        String contact = "联系方式：与Kabosu酱一起散步";
        canvas.drawText(name, 450, 60, paint);
        canvas.drawText(gender, 450, 120, paint);
        canvas.drawText(position, 450, 180, paint);
        canvas.drawText(contact, 450, 240, paint);

        canvas.drawBitmap(doge, null, photo, paint);

        //canvas.setBitmap(bmp);
        if(bmp == null){
            Log.e("DEBUG", "bmp is null");
        }else {
            idImgView.setImageBitmap(bmp);
        }
    }

}

