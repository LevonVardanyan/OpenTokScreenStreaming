package com.streamingopentok;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

public class LiveService extends IntentService implements
        Session.SessionListener, Publisher.PublisherListener,
        Subscriber.VideoListener, Subscriber.SubscriberListener{
    private final IBinder binder = new LocalBinder();
    private byte[] sameData;
    private final Object lock = new Object();
    private boolean imageAvailable = false;
    private Handler mHandler;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ScreensharingCapturer screenCapturer;
    private byte[] rgbaData;

    public LiveService() {
        this("name");
    }

    public LiveService(String name) {
        super(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mHandler = new Handler();
        mStatusChecker.run();
        return binder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private void sessionConnect() {
        if (mSession == null) {
            mSession = new Session(this, OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID);
            mSession.setSessionListener(this);
            mSession.connect(OpenTokConfig.TOKEN);
        }
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
                    if (sameData != null && screenCapturer != null) {
                        screenCapturer.setFrame(sameData);
                    }
            mHandler.postDelayed(mStatusChecker, 0);

        }
    };

    public boolean isImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(boolean imageAvailable) {
        this.imageAvailable = imageAvailable;
    }

    public void startStreaming() {
        sessionConnect();
    }

    public void encode(byte[] rgbaData) {
        sameData = rgbaData.clone();
        if (screenCapturer != null) {
            screenCapturer.setFrame(rgbaData);
            setImageAvailable(false);
        }
    }

    private void subscribeToStream(Stream stream) {
        mSubscriber = new Subscriber(this, stream);
        mSubscriber.setVideoListener(this);
        mSubscriber.setSubscriberListener(this);
        mSession.subscribe(mSubscriber);
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        if (OpenTokConfig.SUBSCRIBE_TO_SELF) {
            if (mSubscriber == null) {
                subscribeToStream(stream);
            }
        }
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {
        if (mPublisher == null) {
            mPublisher = new Publisher(this, "publisher");
            mPublisher.setPublisherListener(this);
            mPublisher.setPublisherVideoType(PublisherKit.PublisherKitVideoType.PublisherKitVideoTypeScreen);
            mPublisher.setAudioFallbackEnabled(false);
            screenCapturer = new ScreensharingCapturer(this);
            mPublisher.setCapturer(screenCapturer);
            mSession.publish(mPublisher);
        }
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (!OpenTokConfig.SUBSCRIBE_TO_SELF) {
            if (mSubscriber == null) {
                subscribeToStream(stream);
            }
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

    }

    @Override
    public void onVideoDataReceived(SubscriberKit subscriberKit) {

    }

    @Override
    public void onVideoDisabled(SubscriberKit subscriberKit, String s) {

    }

    @Override
    public void onVideoEnabled(SubscriberKit subscriberKit, String s) {

    }

    @Override
    public void onVideoDisableWarning(SubscriberKit subscriberKit) {

    }

    @Override
    public void onVideoDisableWarningLifted(SubscriberKit subscriberKit) {

    }

    public class LocalBinder extends Binder {
        LiveService getService() {
            return LiveService.this;
        }
    }
}
