package com.streamingopentok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.View;

import com.opentok.android.BaseVideoCapturer;

public class ScreensharingCapturer extends BaseVideoCapturer {

  private Context mContext;

    private boolean capturing = false;
    private View contentView;

    private int fps = 100;
    private int width = 0;
    private int height = 0;
    private byte[] frame;
    private boolean newFrameAvailable = false;
    private FPSCounter counter = new FPSCounter();

    private Bitmap bmp;
    private Canvas canvas;

    private Handler mHandler = new Handler();

    private Runnable newFrame = new Runnable() {
        @Override
        public void run() {
            if (newFrameAvailable) {
                provideIntArrayFrame(RGBA2ARGB(frame), ARGB, 640, 800, 0, false);
                newFrameAvailable = false;
                counter.logFrame();
            }
            mHandler.postDelayed(newFrame, 1000 / fps);
        }
    };

    public void setFrame(byte[] frame) {
        this.frame = frame;
        newFrameAvailable = true;
    }

    public static int[] RGBA2ARGB(byte[] frame)
    {

        int[] pixelsOut = new int[frame.length];


        int pixel=0;
        int count=frame.length;

        int pixelCount = count / 4;

        for (int i = 0; i < count; i += 4) {
            byte r = frame[i];
            byte g = frame[i + 1];
            byte b = frame[i + 2];
            byte a = frame[i + 3];

            pixelsOut[pixel++] = ( a << 24 | r << 16 | g << 8 | b );
        }
/*
        while(count-->0){
            byte r =
            int inVal = frame[pixel];

            //Get and set the pixel channel values from/to int  //TODO OPTIMIZE!
            int r = (int)( (inVal & 0xff000000)>>24 );
            int g = (int)( (inVal & 0x00ff0000)>>16 );
            int b = (int)( (inVal & 0x0000ff00)>>8  );
            int a = (int)(  inVal & 0x000000ff)      ;

            pixelsOut[pixel++] = (int)( a << 24 | r << 16 | g << 8 | b );

        }
*/        return pixelsOut;
    }

    public ScreensharingCapturer(Context context) {
        this.mContext = context;
    }

    @Override
    public void init() {

    }

    @Override
    public int startCapture() {
        capturing = true;

        mHandler.postDelayed(newFrame, 1000 / fps);
        return 0;
    }

    @Override
    public int stopCapture() {
        capturing = false;
        mHandler.removeCallbacks(newFrame);
        return 0;
    }

    @Override
    public boolean isCaptureStarted() {
        return capturing;
    }

    @Override
    public CaptureSettings getCaptureSettings() {

        CaptureSettings settings = new CaptureSettings();
        settings.fps = fps;
        settings.width = width;
        settings.height = height;
        settings.format = ARGB;
        return settings;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

}