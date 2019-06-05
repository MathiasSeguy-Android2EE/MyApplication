package android2ee.com.myapplication;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /***********************************************************
     *  Attributes
     **********************************************************/
    private Gpio mLed;
    private ObjectAnimator redLedANimator;
    /***********************************************************
    *  Managing LifeCycle
    **********************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redLedANimator=ObjectAnimator.ofInt(this, "flash",0,10000);
        PeripheralManagerService pioServ=new PeripheralManagerService();
        try{
            mLed=pioServ.openGpio("BCM6");//because BCM6 is the pin of the red led (yoiu have the board of rapserty that tell you thaht, look at the card)
            //define the direction of the data (it can not be both I/O you have to choose
            mLed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            //
            mLed.setValue(true);

        }catch (IOException e){
            Log.e(TAG,"a fuck happens");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        redLedANimator.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        redLedANimator.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            //
            mLed.setValue(true);
            mLed.close();

        }catch (IOException e){
            Log.e(TAG,"a fuck happens");
            throw new RuntimeException(e);
        }
    }
    /***********************************************************
     *  Business methods
     **********************************************************/

    public void setFlash(int i){
        Log.e(TAG,"in setFlash with "+i+" and value is "+(i%20==0));
        try {
            mLed.setValue((i%20==0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
