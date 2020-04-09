package com.example.blackjackv2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.Toast;

public class GraphicsHelper
{
    public static Bitmap resizeImage(Bitmap getBitmap, int maxSize){
        int width = getBitmap.getWidth();
        int height = getBitmap.getHeight();
        double x;

        if (width >= height && width > maxSize) {
            x = width / height;
            width = maxSize;
            height = (int) (maxSize / x);
        } else if (height >= width && height > maxSize) {
            x = height / width;
            height = maxSize;
            width = (int) (maxSize / x);
        }
        return Bitmap.createScaledBitmap(getBitmap, width, height, false);
    }

    public static Bitmap createCardImage(Card card, Resources res){
        return GraphicsHelper.resizeImage(card.getCardImage(res), 300);
    }

    public static void showToastAdLoaded(Context context){
        Toast toast =Toast.makeText(context,"Ad Loaded",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
    }

    public static void showToastBlackJack(Context context){

        Toast toast =Toast.makeText(context,"BlackJack!",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 20);
        toast.show();
    }

    public static void showToastBurned(Context context){
        Toast toast =Toast.makeText(context,"Burned!",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 20);
        toast.show();
    }
}
