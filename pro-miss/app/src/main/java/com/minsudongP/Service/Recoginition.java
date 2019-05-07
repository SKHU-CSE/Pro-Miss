package com.minsudongP.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat.Builder;
import com.minsudongP.MainActivity;
import com.minsudongP.R;
import com.minsudongP.Singletone.UrlConnection;
import com.minsudongP.Singletone.UserInfor;
import com.minsudongP.SiriActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Recoginition extends RecognitionService {

    public static final int MSG_VOICE_RECO_READY=0;
    public static final int MSG_VOICE_RECO_END=1;
    public static final int MSG_VOICE_RECO_RESTART=2;
    private SpeechRecognizer mSrRecognizer;
    boolean mBoolVoiceRecoStarted;
    boolean hasQuestion;
    protected AudioManager mAudioManager;
    MediaPlayer mediaPlayer;
    Intent intent;
     ResultReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        startListening();
        hasQuestion=false;
        intent=new Intent(this, SiriActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent==null)
        {
            return Service.START_STICKY;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendMessage(String send,String message){
        Log.d("messageService", "Broadcasting message");
        Intent intent = new Intent("Promiss-event-name");
        intent.putExtra("send",send);
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service","destory");
    }

    @Override
    protected void onStartListening(Intent recognizerIntent, Callback listener) {

    }

    private Handler mHdrVoiceRecoState = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case MSG_VOICE_RECO_READY	: break;
                case MSG_VOICE_RECO_END		:
                {
                    stopListening();
                    sendEmptyMessageDelayed(MSG_VOICE_RECO_RESTART, 1000);
                    break;
                }
                case MSG_VOICE_RECO_RESTART	: startListening();	break;
                default:
                    super.handleMessage(msg);
            }

        }
    };
    public void startListening()
    {

        if(mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            mHdrVoiceRecoState.sendEmptyMessageDelayed(MSG_VOICE_RECO_RESTART,500);
        }else {
            mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

            if (mBoolVoiceRecoStarted == false) {
                if (mSrRecognizer == null) {

                    mSrRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                    mSrRecognizer.setRecognitionListener(mClsRecoListener);
                }
                if (mSrRecognizer.isRecognitionAvailable(getApplicationContext())) {
                    Intent itItent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    itItent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
                    itItent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN.toString());
                    itItent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 50);
                    mSrRecognizer.startListening(itItent);
                }
            }
            mBoolVoiceRecoStarted = true;
        }
    }

    public void stopListening()
    {

        try
        {

            if (mSrRecognizer != null && mBoolVoiceRecoStarted == true)
            {
                mSrRecognizer.stopListening();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        mBoolVoiceRecoStarted = false;
    }



    @Override
    protected void onCancel(Callback listener) {
        mSrRecognizer.cancel();
    }

    @Override
    protected void onStopListening(Callback listener) {
        try
        {
            mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            if (mSrRecognizer != null && mBoolVoiceRecoStarted == true)
            {
                mSrRecognizer.stopListening();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        mBoolVoiceRecoStarted = false;
    }
    private RecognitionListener mClsRecoListener = new RecognitionListener() {
        @Override
        public void onRmsChanged(float rmsdB) {
            if(rmsdB==8.5&&hasQuestion)
            {
                sendMessage("start","");
            }

            Log.d("sound",""+rmsdB);
        }


        @Override
        public void onResults(Bundle results) {
            mHdrVoiceRecoState.sendEmptyMessage(MSG_VOICE_RECO_END);
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            final String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            Log.d("key",rs[0]);

            if(rs[0].equals("프로미스")||rs[0].equals("프루미스")||rs[0].equals("포로미스")||hasQuestion) {
                if(rs[0].equals("프로미스")||rs[0].equals("프루미스")||rs[0].equals("포로미스")) {
                    hasQuestion = true;
                    startActivity(intent);


                }
                sendMessage("start","");
                sendMessage("my",rs[0]);
                new Thread() {

                    @Override
                    public void run() {
                        UserInfor userInfor=UserInfor.shared;
                        UrlConnection connection = UrlConnection.shardUrl;
                        connection.PostSpeekRequest(rs[0],userInfor.getID(), new

                                okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        String s=response.body().string();
                                        Log.d("code", "" + response.code());
                                        if (response.code() == 200) {

                                            try {
                                                JSONObject jsonObject = new JSONObject(s);



                                                JSONObject data=jsonObject.getJSONObject("data");

                                                sendMessage("chatbot",data.getString("message"));
                                                // response.body().string()은 한번밖에 호출 못함

                                                if(jsonObject.getInt("result")==1000) {
                                                    hasQuestion = false;
                                                    sendMessage("reset","");
                                                }
                                                byte[] bytes = Base64.decode(data.getString("voice"), 0);
                                                Log.d("code", "" + bytes.length);
                                                File f = new File(getApplicationContext().getFilesDir().getPath().toString() + "speek" + ".mp3");

                                                FileOutputStream outputStream = new FileOutputStream(f);

                                                Log.d("result", "" + bytes.length);
                                                outputStream.write(bytes, 0, bytes.length);
                                                outputStream.flush();
                                                outputStream.close();

                                                FileInputStream fs = new FileInputStream(f);


                                                mediaPlayer = new MediaPlayer();
                                                mediaPlayer.setDataSource(fs.getFD());
                                                //mediaPlayer.prepare();
                                                mediaPlayer.prepare();
                                                fs.close();
                                                // mediaPlayer.prepareAsync();
                                                mediaPlayer.start();

                                            }catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                    }
                }.start();
            }

            //((TextView)(findViewById(R.id.text))).setText("" + rs[index]);
        }


        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int intError) {
            mHdrVoiceRecoState.sendEmptyMessage(MSG_VOICE_RECO_END);
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }
    };
}
