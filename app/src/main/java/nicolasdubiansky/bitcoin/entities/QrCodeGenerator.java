package nicolasdubiansky.bitcoin.entities;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by Nicolas on 26/9/2017.
 */

public class QrCodeGenerator {
    public static Bitmap generateQrCodeImage(String text, Integer dimension) {
        BitMatrix result = null;
        try {
            result = new MultiFormatWriter().encode(text,
                    BarcodeFormat.QR_CODE, dimension, dimension, null);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, dimension, 0, 0, w, h);
            return bitmap;
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
