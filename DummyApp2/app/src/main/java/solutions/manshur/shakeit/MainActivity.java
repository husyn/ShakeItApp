package solutions.manshur.shakeit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;

    // Declare the MediaPlayer object
    private MediaPlayer mMediaPlayer;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        // For example to set the volume of played media to maximum.
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        // Initialize media player
        mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound);

        // Add OnCompletionListener to release the
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                mMediaPlayer.reset();
//                mMediaPlayer.release();
            }
        });

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.popup);
        dialog.setTitle("Suggestion");
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.button_clear);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageButton dialogButton2 = (ImageButton) dialog.findViewById(R.id.button_filter);
        dialogButton2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final Dialog filter_dialog = new Dialog(MainActivity.this);
                filter_dialog.setContentView(R.layout.filter);
                filter_dialog.setTitle("Filter");
                filter_dialog.show();
            }
        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            public void onShake() {

                try {

                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(200);
                    }

                    mMediaPlayer.start();
                    dialog.show();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }//onShake()
        });//setOnShakeListener()

        mSensorManager.registerListener(mShakeDetector,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}


