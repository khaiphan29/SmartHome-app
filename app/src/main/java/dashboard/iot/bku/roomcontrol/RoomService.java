package dashboard.iot.bku.roomcontrol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

import static dashboard.iot.bku.roomcontrol.Constants.ROOM_FRAGMENT_INDEX;
import static dashboard.iot.bku.roomcontrol.Constants.STATISTIC_FRAGMENT_INDEX;

public class RoomService extends FragmentActivity {

    Fragment fragment;
    FragmentRoom fragmentRoom;
    FragmentStatistic fragmentStatistic;
    Button btnRoom, btnStatistic;

    public Model dataProc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_service);

        //delete database
        //getApplicationContext().deleteDatabase("MyDB");

        dataProc = new Model(getApplicationContext(), this, this);


        fragmentRoom = new FragmentRoom();
        fragmentStatistic = new FragmentStatistic();

/*        addSeries(fragmentStatistic.graphView, series);
        addSeries(fragmentStatistic.graphView1, humidSeries);
        addSeries(fragmentStatistic.graphView2, dseries);*/

/*        dbHandler = new DBHandler(this);

        tempViewID = R.id.Temp;
        humidViewID = R.id.Humi;
        airVIewID = R.id.Air;
        ledButtonID = R.id.led;
        fanButtonID = R.id.fan;
        doorButtonID = R.id.door;
        tempGraphID = R.id.TempLine;
        humidGraphID = R.id.HumiLine;
        airGraphID = R.id.AirLine;


        startMQTT();*/

        dataProc.startMQTT();


        btnRoom = findViewById(R.id.RoomState);
        btnStatistic = findViewById(R.id.Statistic);


        btnRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRoom.setBackground(getDrawable(R.drawable.main_rounded_corner));
                btnStatistic.setBackground(getDrawable(R.drawable.rounded_corner));
                selectFragment(ROOM_FRAGMENT_INDEX);
            }
        });

        btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStatistic.setBackground(getDrawable(R.drawable.main_rounded_corner));
                btnRoom.setBackground(getDrawable(R.drawable.rounded_corner));
    selectFragment(STATISTIC_FRAGMENT_INDEX);
}
        });

                }




public void selectFragment(int pos)
    {
        Class fragmentClass = null;
        switch (pos)
        {
            case ROOM_FRAGMENT_INDEX:
                fragmentClass = FragmentRoom.class;
                fragment = fragmentRoom;
                break;
            case STATISTIC_FRAGMENT_INDEX:
                fragmentClass = FragmentStatistic.class;
                fragment = fragmentStatistic;
                break;
            default:
                break;
        }

        try{


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commitAllowingStateLoss();
        }
        catch (Exception e){}
    }
}
