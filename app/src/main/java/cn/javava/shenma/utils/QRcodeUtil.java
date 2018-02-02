package cn.javava.shenma.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/31
 * Todo {TODO}.
 */

public class QRcodeUtil {
    private static int IMAGE_HALFWIDTH = 50;

    public static Bitmap createQRCode(String text) {
        int size=400;

        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,BarcodeFormat.QR_CODE, size, size, hints);
            //将logo图片按martix设置的信息缩放

            int[] pixels=new int[size*size];

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            for (int y =0 ;y< height;y++) {
                for (int x= 0 ;x<width;x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = -0x1000000;
                    } else {
                        pixels[y * width + x] = -0x1;
                    }
                }
            }

            Bitmap bitmap1 = Bitmap.createBitmap(size, size,Bitmap.Config.ARGB_4444);
            bitmap1.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap1;
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Bitmap createQRCode(String text, Bitmap bitmap){
        int size=400;

        Bitmap mBitmap = bitmap;
        try {
            IMAGE_HALFWIDTH = size / 10;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,BarcodeFormat.QR_CODE, size, size, hints);
            //将logo图片按martix设置的信息缩放
            mBitmap = Bitmap.createScaledBitmap(mBitmap, size, size, false);
            int[] pixels=new int[size*size];
            int color =-0x1;
            for(int y=0;y<size;y++){
                for (int x = 0; x<size;x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = mBitmap.getPixel(x, y);
                    } else {
                        pixels[y * size + x] = color;
                    }
                }
            }

            Bitmap bitmap1 = Bitmap.createBitmap(size, size,Bitmap.Config.ARGB_4444);
            bitmap1.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static Bitmap createQRCodeWithLogo5(String text,Bitmap bitmap ){
//        int size=400;
//        Bitmap mBitmap = bitmap;
//        try {
//            IMAGE_HALFWIDTH = size / 10;
//
//
//            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            BitMatrix bitMatrix = new QRCodeWriter().encode(text,BarcodeFormat.QR_CODE, size, size, hints);
//            //将logo图片按martix设置的信息缩放
//            mBitmap = Bitmap.createScaledBitmap(mBitmap, size, size, false);
//
//
//
//            int width = bitMatrix.getWidth();//矩阵高度
//            int height = bitMatrix.getHeight();//矩阵宽度
//            int halfW = width / 2;
//            int halfH = height / 2;
//
//            Matrix m = new Matrix();
//            val sx = 2.toFloat() * IMAGE_HALFWIDTH / mBitmap.getWidth();
//            val sy = 2.toFloat() * IMAGE_HALFWIDTH / mBitmap.getHeight();
//            m.setScale(sx, sy);
//            //设置缩放信息
//            //将logo图片按martix设置的信息缩放
//            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,mBitmap.getWidth(), mBitmap.getHeight(), m, false);
//
//            val pixels = IntArray(size * size)
//            for (y in 0 until size) {
//                for (x in 0 until size) {
//                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH&& y > halfH - IMAGE_HALFWIDTH&& y < halfH + IMAGE_HALFWIDTH) {
//                        //该位置用于存放图片信息
//                        //记录图片每个像素信息
//                        pixels[y * width + x] = mBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH)
//                    } else {
//                        if (bitMatrix.get(x, y)) {
//                            pixels[y * size + x] = -0xc84e62
//                        } else {
//                            pixels[y * size + x] = -0x1
//                        }
//                    }
//                }
//            }
//            val bitp = Bitmap.createBitmap(size, size,
//                    Bitmap.Config.ARGB_4444)
//            bitp.setPixels(pixels, 0, size, 0, 0, size, size)
//            return bitp
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null
//        }


}
