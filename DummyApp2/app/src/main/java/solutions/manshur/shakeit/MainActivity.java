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
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Dialog;
import android.widget.Button;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



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
        dialog.setTitle("Image");


        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            public void onShake() {

                Toast.makeText(MainActivity.this, "shake", Toast.LENGTH_SHORT).show();


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


