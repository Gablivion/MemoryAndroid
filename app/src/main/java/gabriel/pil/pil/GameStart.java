package gabriel.pil.pil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.CountDownTimer;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameStart extends AppCompatActivity {
    private  int []button  = new int[]{
            R.id.btn11,R.id.btn12,R.id.btn13,R.id.btn14,
            R.id.btn21,R.id.btn22,R.id.btn23,R.id.btn24,
            R.id.btn31,R.id.btn32,R.id.btn33,R.id.btn34,
            R.id.btn41,R.id.btn42,R.id.btn43,R.id.btn44};
    private boolean pasikat = false;
    private String []letra;
    private View pressed = null;
    private boolean []humana  = new boolean[16];
    private int GAME_TIME = 3000;
    CountDownTimer countDownTimer;
    Timer stopwatch = new Timer();
    TextView t;
    TextView m;
    int seconds = 0;
    boolean countDownRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_start);
        letra  = this.getResources().getStringArray(R.array.alphabet);
        letra = shuffleArray(letra);
        pasikat = true;
        t = findViewById(R.id.tamaTxt);
        m = findViewById(R.id.maliTxt);
        fillin();
        for (int x:button
             ) {
            Button holder = findViewById(x);
            holder.setEnabled(false);
        }
        startTimer();
        startStopwatch();
    }
    public void startStopwatch() {
        final TextView tv = findViewById(R.id.timer);
        stopwatch.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(formatter(seconds));
                        seconds++;
                    }
                });
            }
        }, GAME_TIME, 1000);
    }
    public void startTimer(){
        final TextView tv = findViewById(R.id.timer);
        new CountDownTimer(GAME_TIME, 1000) {

            public void onTick(long millisUntilFinished) {
                tv.setText("00:0" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                for (int x:button
                        ) {
                    Button holder = findViewById(x);
                    holder.setEnabled(true);
                }
                pasikat = false;
                fillin();
            }
        }.start();
    }



    private String formatter(int seconds){
        String sec = String.format("%02d", seconds % 60);
        String min = String.format("%02d", seconds / 60);
        return min + ":" + sec;
    }

    public void guess(View v)
    {
        v.setEnabled(false);
        fillin();
        if(pressed == null)
            pressed = v;
        else
        {
            checker(v);
            pressed = null;
        }
    }

    protected void fillin()
    {
        for(int i = 0; i < 16; i++)
        {
            Button pisliton  = findViewById(button[i]);
            if(humana[i] || !pisliton.isEnabled() || pasikat)
                pisliton.setText(letra[i]);
            else
                pisliton.setText("???");

        }
    }
    int tama;
    int mali;
    protected void checker(View v)
    {
        Button first = findViewById(v.getId());
        Button second = findViewById(pressed.getId());
        if(first.getText().equals(second.getText()))
        {
            humana[Arrays.binarySearch(button,v.getId())] = true;
            humana[Arrays.binarySearch(button,pressed.getId())] = true;
            tama++;
            t.setText(tama + " ");
            if(tama == 8)
                stopwatch.cancel();
        }
        else
        {
            first.setEnabled(true);
            second.setEnabled(true);
            mali++;
            m.setText(mali + " ");
        }
    }
    private static String[] shuffleArray(String[] array)
    {
        int index;
        String temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }


}
