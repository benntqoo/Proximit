package tw.com.lccnet.app.proximit;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mgr;
    private Sensor proxmity;
    private Vibrator vibrator;
    private float lastval = -1;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //感應器介面
        this.mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
//        取得距離感應器
        this.proxmity = this.mgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//        //以震動與距離產生反應
        this.vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(1000);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mgr.registerListener(this, this.proxmity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mgr.unregisterListener(this, this.proxmity);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float thisVal = event.values[0];
        if (this.lastval == -1) {
            this.lastval = thisVal;
        } else {
            if (thisVal < this.lastval) {
//                //接近 單一次震動
//                this.vibrator.vibrate(1000);
                this.vibrator.vibrate(new long[]{1000,100,200,2000,300,400},-1);//設定旋律振動頻率
            } else {
                //離開
                this.vibrator.vibrate(100);
            }
            this.lastval = thisVal;
        }
        String msg = "Val" + this.lastval;
        Log.i("TAG", msg);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
