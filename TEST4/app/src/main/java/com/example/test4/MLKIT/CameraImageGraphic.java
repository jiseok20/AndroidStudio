package com.example.test4.MLKIT;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.test4.GraphicOverlay;
import com.example.test4.GraphicOverlay.Graphic;

/** Draw camera image to background. */
public class CameraImageGraphic extends GraphicOverlay.Graphic {

    private final Bitmap bitmap;

    public CameraImageGraphic(GraphicOverlay overlay, Bitmap bitmap) {
        super(overlay);
        this.bitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, getTransformationMatrix(), null);
    }
}
