package com.example.sensors;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final Map<Integer, String> SIAMSEE_MAP = new TreeMap<>();
    static {
        SIAMSEE_MAP.put(1, "ใบที่หนึ่งนั้นดีอย่าคิดฟุ้ง\r\n" + //
                "ดั่งดวงจันทร์ลอยเด่นอยู่เป็นสุข\r\n" + //
                "หมายสิ่งใดที่ในจิตต์คิดปรารถนา\r\n" + //
                "ว่าคงจะสมดังได้ตั้งใจ\r\n" + //
                "ทั้งพวกพ้องเพื่อนฝูงต่างห่วงใย\r\n" + //
                "ว่าอยู่เย็นเป็นสุขทุกเดือนปี\r\n" + //
                "แม้นใบนี้ถามโรคที่โศกศัลย์\r\n" + //
                "ว่าคงจะหายดีดังที่หวัง");

        SIAMSEE_MAP.put(2, "ใบที่สองนี้ร้ายอย่าท้อใจ\r\n" + //
                "เปรียบดังเรือน้อยลอยทวนกระแสไหล\r\n" + //
                "ต้องฝ่าคลื่นลมแรงแข็งขืนไป\r\n" + //
                "ยากเย็นแสนเข็ญใจในยามนี้\r\n" + //
                "แม้นจะทำการใดให้ระวัง\r\n" + //
                "คนคิดร้ายหมายหวังดังราคี\r\n" + //
                "จงรอเวลาอย่าได้รีบรี่\r\n" + //
                "รอให้พ้นวันนี้จะดีเอง");

        SIAMSEE_MAP.put(3, "ใบที่สามชื่นบานดั่งบุปผา\r\n" + //
                "ผลิดอกออกใบในสวนทอง\r\n" + //
                "แม้นจะทำการใดในครั้งนี้\r\n" + //
                "มีแต่คนดีคอยเกื้อหนุน\r\n" + //
                "ถามลาภผลนั้นว่าจะได้\r\n" + //
                "ถามการค้าขายว่าเพิ่มพูน\r\n" + //
                "ถามคู่ครองว่าจะมีผู้อุปถัมภ์\r\n" + //
                "ทุกสิ่งล้วนสมหวังดังตั้งใจ");

        SIAMSEE_MAP.put(4, "ใบที่สี่ว่าร้ายอย่าใจร้อน\r\n" + //
                "เหมือนต้นไม้โค่นล้มในคืนมืด\r\n" + //
                "จะทำการสิ่งใดให้คิดหนัก\r\n" + //
                "มีคนคิดอิจฉาและริษยา\r\n" + //
                "ทั้งเจ็บไข้ได้ป่วยรวยความทุกข์\r\n" + //
                "แม้นถามสุขภาพว่าต้องรักษา\r\n" + //
                "ถามคู่ครองว่ายังต้องรอมา\r\n" + //
                "ควรทำบุญภาวนาจึงจะดี");

        SIAMSEE_MAP.put(5, "ใบที่ห้าสุขล้ำดั่งน้ำใส\r\n" + //
                "ไหลเย็นชื่นใจในขุนเขา\r\n" + //
                "แม้นถามหาลาภผลจะพบเจอ\r\n" + //
                "ถามการงานเจริญก้าวหน้าไกล\r\n" + //
                "มีคนดีมีบุญมาโปรดปราน\r\n" + //
                "ทั้งการค้าการขายได้กำไร\r\n" + //
                "ถามคู่ครองว่าจะได้พบใจ\r\n" + //
                "ทุกข์โศกโรคภัยจะหายไป");

        SIAMSEE_MAP.put(6, "ใบที่หกอับโชคโศกเศร้า\r\n" + //
                "เหมือนเรือแตกกลางทะเลเวิ้งว้าง\r\n" + //
                "จะทำการสิ่งใดให้ระวัง\r\n" + //
                "มีคนคิดขัดขวางทางดำเนิน\r\n" + //
                "ถามหาลาภว่ายังไม่ถึงที\r\n" + //
                "ถามคู่ครองนั้นมีแต่ความเมิน\r\n" + //
                "ควรหยุดพักสักระยะอย่าเพลิน\r\n" + //
                "รอให้ผ่านความเดือดเนื้อร้อนใจ");

        SIAMSEE_MAP.put(7, "ใบที่เจ็ดประเสริฐดั่งทองคำ\r\n" + //
                "จะทำการสิ่งใดได้สมหวัง\r\n" + //
                "มีเทวาเทพยดาคอยค้ำชู\r\n" + //
                "อีกผู้ใหญ่ใจดีมีเมตตา\r\n" + //
                "ถามการค้าว่าจะได้ลาภผล\r\n" + //
                "ถามคู่ครองว่าจะพบคู่หา\r\n" + //
                "แม้นเจ็บไข้ได้ป่วยด้วยโรคา\r\n" + //
                "ว่าจะหายดีพลันในไม่ช้า");

        SIAMSEE_MAP.put(8, "ใบที่แปดระทมขมขื่นนัก\r\n" + //
                "เหมือนนกน้อยพลัดรังในวันฝน\r\n" + //
                "จะทำการสิ่งใดให้รอไว้\r\n" + //
                "อย่าเพิ่งใจร้อนร้นจนวุ่นวาย\r\n" + //
                "ถามหาลาภว่ายังไม่ถึงที\r\n" + //
                "ถามคู่ครองยังมีแต่ห่างหาย\r\n" + //
                "ควรทำบุญสวดมนต์อย่าท้อถอย\r\n" + //
                "รอวันผ่อนคลายในภายหน้า");

        SIAMSEE_MAP.put(9, "ใบที่เก้าเข้าทีดังใจหมาย\r\n" + //
                "เหมือนพบขุมทรัพย์ในป่าใหญ่\r\n" + //
                "จะทำการสิ่งใดล้วนสำเร็จ\r\n" + //
                "มีแต่คนเมตตาและภักดี\r\n" + //
                "ถามการค้าว่าจะได้กำไร\r\n" + //
                "ถามคู่ครองว่าจะได้คนดี\r\n" + //
                "แม้นเจ็บป่วยก็จะหายในไม่ช้า\r\n" + //
                "ทุกสิ่งล้วนดีดีตามที่หวัง");

        SIAMSEE_MAP.put(10, "ใบที่สิบทบทุกข์ถูกอิจฉา\r\n" + //
                "เหมือนเรือล่มกลางมหาสมุทรกว้าง\r\n" + //
                "จะทำการสิ่งใดให้ระวัง\r\n" + //
                "มีคนคอยขัดขวางทางดำเนิน\r\n" + //
                "ถามการค้าว่ายังไม่ถึงที\r\n" + //
                "ถามคู่ครองนั้นมีแต่ความเมิน\r\n" + //
                "ควรรอเวลาอย่าใจเร็ว\r\n" + //
                "รอให้พ้นเคราะห์กรรมนำพาไป");

        SIAMSEE_MAP.put(11, "ใบที่สิบเอ็ดเสร็จสมดังตั้งใจ\r\n" + //
                "เหมือนพบน้ำใสในทะเลทราย\r\n" + //
                "จะทำการสิ่งใดได้สำเร็จ\r\n" + //
                "มีผู้ใหญ่ใจดีคอยเกื้อหนุน\r\n" + //
                "ถามการค้าว่าจะได้กำไร\r\n" + //
                "ถามคู่ครองจะได้คนเพิ่มพูน\r\n" + //
                "ทั้งโรคาพาธาจะบรรเทา\r\n" + //
                "ทุกสิ่งเบาคลายดังใจปอง");

        SIAMSEE_MAP.put(12, "ใบที่สิบสองต้องระวังภัยอันตราย\r\n" + //
                "เหมือนเดินทางกลางป่าในยามค่ำ\r\n" + //
                "จะทำการสิ่งใดให้ยั้งคิด\r\n" + //
                "มีคนคิดริษยาและอิจฉา\r\n" + //
                "ถามการค้าว่ายังไม่ถึงที\r\n" + //
                "ถามคู่ครองนั้นมีแต่ห่างหา\r\n" + //
                "ควรทำบุญภาวนาและรอคอย\r\n" + //
                "ให้พ้นร้อนผ่อนคลายในภายหน้า");

        SIAMSEE_MAP.put(13, "ใบที่สิบสามงามสง่าในยามนี้\r\n" + //
                "เหมือนดวงจันทร์อันมีในราตรี\r\n" + //
                "ส่องสว่างกระจ่างใจให้ชื่นบาน\r\n" + //
                "จะทำการสิ่งใดได้สมหวัง\r\n" + //
                "ถามการค้าว่าจะได้ทรัพย์สิน\r\n" + //
                "ถามคู่ครองจะได้คนถูกใจ\r\n" + //
                "ทั้งโรคาพาธาจะหายไป\r\n" + //
                "สมดังใจปรารถนาทุกสิ่งสรรพ์");

        SIAMSEE_MAP.put(14, "ใบที่สิบสี่มีภัยให้ระวัง\r\n" + //
                "เหมือนเรือน้อยลอยล่องในมหรรณพ\r\n" + //
                "พบคลื่นลมระทมทุกข์หนักหนา\r\n" + //
                "จะทำการสิ่งใดให้ยั้งคิด\r\n" + //
                "ถามการค้าว่ายังไม่ถึงที\r\n" + //
                "ถามคู่ครองยังมีแต่ผิดหวัง\r\n" + //
                "ควรรอเวลาอย่าใจเร็ว\r\n" + //
                "รอให้พ้นเคราะห์ร้ายในภายหน้า");

        SIAMSEE_MAP.put(15, "ใบที่สิบห้าฟ้าสางสว่างใส\r\n" + //
                "เหมือนพบแสงตะวันในยามเช้า\r\n" + //
                "จะทำการสิ่งใดได้สมหวัง\r\n" + //
                "มีผู้ใหญ่ใจดีคอยเกื้อหนุน\r\n" + //
                "ถามการค้าว่าจะได้กำไร\r\n" + //
                "ถามคู่ครองจะได้คนเพิ่มพูน\r\n" + //
                "ทั้งโรคาพาธาจะบรรเทา\r\n" + //
                "ทุกสิ่งเบาคลายดังใจปรารถนา");

        SIAMSEE_MAP.put(16, "ใบที่สิบหกอกตรมระทมทุกข์\r\n" + //
                "เหมือนนกน้อยบินหลงในพงไพร\r\n" + //
                "จะทำการสิ่งใดให้ระวัง\r\n" + //
                "มีคนคอยขัดขวางทางดำเนิน\r\n" + //
                "ถามการค้าว่ายังไม่ถึงที\r\n" + //
                "ถามคู่ครองนั้นมีแต่ความเมิน\r\n" + //
                "ควรทำบุญสวดมนต์อย่าท้อแท้\r\n" + //
                "รอให้แคล้วคลาดพ้นผจญภัย");

        SIAMSEE_MAP.put(17, "ใบที่สิบเจ็ดเสร็จสมดังหมายปอง\r\n" + //
                "เหมือนพบขุมทองในป่าใหญ่\r\n" + //
                "จะทำการสิ่งใดล้วนสำเร็จ\r\n" + //
                "มีแต่คนเมตตาและช่วยเหลือ\r\n" + //
                "ถามการค้าว่าจะได้รุ่งเรือง\r\n" + //
                "ถามคู่ครองเหมือนเนื้อคู่รอคอย\r\n" + //
                "ทั้งโรคภัยไข้เจ็บจะคลายหาย\r\n" + //
                "สมดังหมายทุกประการในคราวนี้");
    }
    SensorManager sensorManager;
    private static final int MAX_POLL_INTERVAL = 1000;
    private static final int MIN_POLL_INTERVAL = 500;
    private static final int SHAKE_THRESHOLD = 15;
    private static final int MAX_SHAKE_AMPLITUDE = 30; // Maximum expected shake amplitude
    private static final int MIN_VIBRATION_AMPLITUDE = 1; // Minimum vibration amplitude
    private static final int MAX_VIBRATION_AMPLITUDE = 255; // Maximum vibration amplitude
    private int currentPollInterval = MAX_POLL_INTERVAL;
    private Handler hdr = new Handler();
    private PowerManager.WakeLock wl;
    SensorInfo sensor_info = new SensorInfo();
    Boolean shown_dialog = false;
    private ImageView siamseeImage;
    private ImageView siamseeStick;
    private Animation shakeAnimation;
    private Animation slideOutAnimation;
    private Vibrator vibrator;
    private long lastVibrateTime = 0;
    private boolean isAnimating = false;
    private static final long VIBRATE_COOLDOWN = 100; // Reduced cooldown for more responsive vibration
    private static final long RESULT_VIBRATION_DURATION = 1000; // Duration for result vibration

    private final Runnable pollTask = new Runnable() {
        public void run() {
            showDialog();
            hdr.postDelayed(pollTask, currentPollInterval);
        }
    };

    private int normalizeVibrationAmplitude(float acceleration) {
        // Calculate base amplitude from acceleration
        float normalizedAcceleration = Math.min(MAX_SHAKE_AMPLITUDE, Math.max(SHAKE_THRESHOLD, acceleration));
        float amplitudePercentage = (normalizedAcceleration - SHAKE_THRESHOLD)
                / (MAX_SHAKE_AMPLITUDE - SHAKE_THRESHOLD);

        // Convert to vibration amplitude range (1-255)
        int amplitude = (int) (MIN_VIBRATION_AMPLITUDE +
                (MAX_VIBRATION_AMPLITUDE - MIN_VIBRATION_AMPLITUDE) * amplitudePercentage);

        // Ensure amplitude is within valid range
        return Math.min(MAX_VIBRATION_AMPLITUDE, Math.max(MIN_VIBRATION_AMPLITUDE, amplitude));
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        siamseeImage = findViewById(R.id.siamseeImage);
        siamseeStick = findViewById(R.id.siamseeStick);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        setupAnimationListeners();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Sensors Info");
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TO DO
    }// end onAccuracyChanged

    private void setupAnimationListeners() {
        shakeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Start slide out animation for stick
                siamseeStick.startAnimation(slideOutAnimation);

                // Vibrate with full intensity when showing result
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(
                                RESULT_VIBRATION_DURATION,
                                MAX_VIBRATION_AMPLITUDE));
                    } else {
                        vibrator.vibrate(RESULT_VIBRATION_DURATION);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                siamseeStick.setVisibility(View.INVISIBLE);
                showDialog();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            sensor_info.accX = event.values[0];
            sensor_info.accY = event.values[1];
            sensor_info.accZ = event.values[2];

            float acceleration = (float) Math.sqrt(
                    Math.pow(sensor_info.accX, 2) +
                            Math.pow(sensor_info.accY, 2) +
                            Math.pow(sensor_info.accZ, 2));

            if (acceleration > SHAKE_THRESHOLD) {
                long now = System.currentTimeMillis();
                if (now - lastVibrateTime > VIBRATE_COOLDOWN) {
                    if (vibrator != null && vibrator.hasVibrator() && !shown_dialog) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            // Get normalized vibration amplitude
                            int amplitude = normalizeVibrationAmplitude(acceleration);

                            // Create vibration effect with validated amplitude
                            vibrator.vibrate(VibrationEffect.createOneShot(50, amplitude));
                        } else {
                            vibrator.vibrate(50);
                        }
                    }
                    lastVibrateTime = now;
                }

                currentPollInterval = (int) Math.max(
                        MIN_POLL_INTERVAL,
                        MAX_POLL_INTERVAL - (acceleration - SHAKE_THRESHOLD) * 20);
            } else {
                currentPollInterval = MAX_POLL_INTERVAL;
            }
        }
    }

    public void showDialog() {
        if ((Math.abs(sensor_info.accX) > SHAKE_THRESHOLD) ||
                (Math.abs(sensor_info.accY) > SHAKE_THRESHOLD) ||
                (Math.abs(sensor_info.accZ) > SHAKE_THRESHOLD)) {

            if (!shown_dialog) {
                shown_dialog = true;
                siamseeImage.startAnimation(shakeAnimation);

                int randomNumber = (int) (Math.random() * 17) + 1;
                final AlertDialog.Builder viewDialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
                String title = getString(R.string.dialog_title, randomNumber);
                SpannableString spannableTitle = new SpannableString(title);
                spannableTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
                spannableTitle.setSpan(new RelativeSizeSpan(1.5f), 0, title.length(), 0);
                
                viewDialog.setTitle(spannableTitle);
                viewDialog.setMessage(SIAMSEE_MAP.get(randomNumber));
                viewDialog.setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                shown_dialog = false;
                            }
                        });

                AlertDialog dialog = viewDialog.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

                new Handler().postDelayed(() -> {
                    if (!isFinishing()) {
                        dialog.show();

                        // ปรับแต่ง style ของ TextView ใน dialog
                        TextView messageView = dialog.findViewById(android.R.id.message);
                        if (messageView != null) {
                            messageView.setGravity(Gravity.CENTER);
                            messageView.setTextSize(18);
                            messageView.setLineSpacing(0f, 1.2f);
                            messageView.setTextColor(getResources().getColor(R.color.dialog_text));
                        }

                        TextView titleView = dialog.findViewById(getResources()
                                .getIdentifier("alertTitle", "id", "android"));
                        if (titleView != null) {
                            titleView.setGravity(Gravity.CENTER);
                            titleView.setTextColor(getResources().getColor(R.color.dialog_title));
                        }
                    }
                }, shakeAnimation.getDuration() * 6);
            }
        }
    }

    @SuppressLint("WakelockTimeout")
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        if (!wl.isHeld()) {
            wl.acquire();
        }
        hdr.postDelayed(pollTask, currentPollInterval);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        if (vibrator != null) {
            vibrator.cancel();
        }

        if (wl.isHeld()) {
            wl.release();
        }
        hdr.removeCallbacks(pollTask);
    }

    static class SensorInfo {
        float accX, accY, accZ;
        float graX, graY, graZ;
        float gyrX, gyrY, gyrZ;
        float light;
        float laccX, laccY, laccZ;
        float magX, magY, magZ;
        float orX, orY, orZ;
        float proximity;
        float rotX, rotY, rotZ;
    }
}// end MainActivity