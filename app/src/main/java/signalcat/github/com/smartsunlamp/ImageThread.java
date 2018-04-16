package signalcat.github.com.smartsunlamp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by hezhang on 4/15/18.
 */

public class ImageThread extends Thread {
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap temp_bitmap;
    private Canvas canvas;
    private Paint paint;
    private ColorMatrix colorMatrix;
    private ColorMatrixColorFilter colorMatrixColorFilter;
    private Handler handler;
    private boolean running = false;

    public ImageThread(ImageView imageView, Bitmap bitmap) {
        this.imageView = imageView;
        this.bitmap = bitmap;
        temp_bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        canvas = new Canvas(temp_bitmap);
        paint = new Paint();
        handler = new Handler();
    }

    /**
     * This function adjust the brightness level of the target bitmap by the difference
     * value taken from seekBar
     * @param amount
     */
    public void adjustBrightness(int amount) {
        // 4x5 matrix for transforming the color and alpha components of a Bitmap
        // [ a, b, c, d, e,
        // f, g, h, i, j,
        // k, l, m, n, o,
        // p, q, r, s, t ]
        // R’ = a*R + b*G + c*B + d*A + e;
        // G’ = f*R + g*G + h*B + i*A + j;
        // B’ = k*R + l*G + m*B + n*A + o;
        // A’ = p*R + q*G + r*B + s*A + t;
        colorMatrix = new ColorMatrix(new float[] {
                1,0,0,0, amount,
                0,1f,0,0, amount,
                0,0,1f,0, amount,
                0,0,0,1f, 0
        });
        colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        running = true;
    }

    @Override
    public void run() {
        while (true) {
            if (running) {
                canvas.drawBitmap(bitmap, 0, 0, paint);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(temp_bitmap);
                        running = false;
                    }
                });
            }
        }
    }
}
