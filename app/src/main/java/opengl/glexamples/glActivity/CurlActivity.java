package opengl.glexamples.glActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import opengl.glexamples.R;
import opengl.glexamples.surfaceView.CurlView;

/**
 *
 * @author delia
 */
public class CurlActivity extends Activity {

    private CurlView mCurlView;
    private SharedPreferences sp;
    private Context context;
    private SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl);
        context = this.getApplicationContext();
        sp = context.getSharedPreferences("card_data", Context.MODE_PRIVATE);


        int index = 0;
        if (getLastNonConfigurationInstance() != null) {
            index = (Integer) getLastNonConfigurationInstance();
        }
        mCurlView = (CurlView) findViewById(R.id.curl);
        mCurlView.setBitmapProvider(new BitmapProvider());
        mCurlView.setSizeChangedObserver(new SizeChangedObserver());
        mCurlView.setCurrentIndex(index);
        mCurlView.setBackgroundColor(0xFF202830);
        mCurlView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editor = sp.edit();
                editor.putInt("skinID", mCurlView.getCurrentIndex());
                editor.apply();
                String skin_name = "";
                switch (mCurlView.getCurrentIndex()){
                    case 0:
                        skin_name = "圣诞颂歌";
                        break;
                    case 1:
                        skin_name = "和风樱花";
                        break;
                    case 2:
                        skin_name = "复古木纹";
                        break;
                    case 3:
                        skin_name = "商务简约";
                        break;
                }
                Toast.makeText(context, skin_name, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // This is something somewhat experimental. Before uncommenting next
        // line, please see method comments in CurlView.
        // mCurlView.setEnableTouchPressure(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCurlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurlView.onResume();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mCurlView.getCurrentIndex();
    }

    /**
     * Bitmap provider.
     */
    private class BitmapProvider implements CurlView.BitmapProvider {

        private int[] mBitmapIds = { R.drawable.whole_christmas, R.drawable.whole_green, R.drawable.whole_yellow};

        @Override
        public Bitmap getBitmap(int width, int height, int index) {
            Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            b.eraseColor(0xFFFFFFFF);
            Canvas c = new Canvas(b);
            Drawable d = getResources().getDrawable(mBitmapIds[index]);

            int margin = 7;
            int border = 3;
            Rect r = new Rect(margin, margin, width - margin, height - margin);

            int imageWidth = r.width() - (border);
            int imageHeight = imageWidth * d.getIntrinsicHeight()
                    / d.getIntrinsicWidth();
            if (imageHeight > r.height() - (border)) {
                imageHeight = r.height() - (border);
                imageWidth = imageHeight * d.getIntrinsicWidth()
                        / d.getIntrinsicHeight();
            }

            r.left += ((r.width() - imageWidth) / 2) - border;
            r.right = r.left + imageWidth + border + border;
            r.top += ((r.height() - imageHeight) / 2) - border;
            r.bottom = r.top + imageHeight + border + border;

            Paint p = new Paint();
            p.setColor(0xFFC0C0C0);
            c.drawRect(r, p);
            r.left += border;
            r.right -= border;
            r.top += border;
            r.bottom -= border;

            d.setBounds(r);
            d.draw(c);
            return b;
        }

        @Override
        public int getBitmapCount() {
            return mBitmapIds.length;
        }
    }

    /**
     * CurlView size changed observer.
     */
    private class SizeChangedObserver implements CurlView.SizeChangedObserver {
        @Override
        public void onSizeChanged(int w, int h) {
            if (w > h) {
                mCurlView.setViewMode(CurlView.SHOW_TWO_PAGES);
                mCurlView.setMargins(.1f, .05f, .1f, .05f);
            } else {
                mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
                mCurlView.setMargins(.1f, .1f, .1f, .1f);
            }
        }
    }

}
