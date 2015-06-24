package com.dkhs.portfolio.ui;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.dkhs.portfolio.utils.ShakeDetector;

/**
 * Created by zjz on 2015/6/24.
 */
public class ShakeActivity extends ModelAcitivity implements ShakeDetector.Listener {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle("摇一摇");


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);


    }

    @Override
    public void hearShake() {

        Toast.makeText(this, "Don't shake me, bro!", Toast.LENGTH_SHORT).show();


    }
}
