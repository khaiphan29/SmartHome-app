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
/*    MQTTHelper mqttHelper;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

    *//*private TextView tempView, humidView, airView;
    private ToggleButton ledButton, fanButton, doorButton;
    private GraphView tempGraph, humidGraph, airGraph;*//*

    int tempViewID, humidViewID, airVIewID, ledButtonID, fanButtonID, doorButtonID, tempGraphID, humidGraphID, airGraphID;


    //Graph
    DBHandler dbHandler;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[0]);
    LineGraphSeries<DataPoint> humidSeries = new LineGraphSeries<>(new DataPoint[0]);
    LineGraphSeries<DataPoint> dseries = new LineGraphSeries<>(new DataPoint[0]);*/

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

/*
    //Graphtest
    public void insertData(){

    }

    public void addSeries(GraphView graphView, LineGraphSeries<DataPoint> series)
    {
        graphView.addSeries(series);
    }
    //Graph test
    public void addSer(GraphView graphView)
    {
        graphView.addSeries(dataProc.tempSeries);
    }
    public void addSer1(GraphView graphView)
    {
        graphView.addSeries(dataProc.humidSeries);
    }
    public void addSer2(GraphView graphView)
    {
        graphView.addSeries(dataProc.airSeries);
    }*/

    /*public void sendDataMQTT(String topic, String value)
    {
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);


        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){

        }
    }

    */
/*private DataPoint[] getDataPoint(String tableName)
    {
        String[] columns = {"xValues", "yValues"};
        Cursor cursor = sqLiteDatabase.query("tempTable" ,columns, null,null,null,null,null );
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i ++){
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getLong(0), cursor.getFloat(1));
        }
        return dp;
    }*//*


    String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString =  "0123456789";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private void startMQTT()
    {
        mqttHelper = new MQTTHelper(getApplicationContext(), getAlphaNumericString(6));
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d("mqtt", "Connection is successful");
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("mqtt", "Received: " + message.toString());
                if(topic.equals("izayazuna/feeds/temperature")){
                    ((TextView)findViewById(tempViewID)).setText(message.toString() + " °C");
                    long xValue = new Date().getTime();
                    float yValue = Float.parseFloat(message.toString());
                    dbHandler.insertData("tempTable", xValue, yValue);
                    series.resetData(dbHandler.getDataPoint("tempTable"));
                    ((GraphView)findViewById(tempGraphID)).getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
                    {
                        @Override
                        public String formatLabel(double value, boolean isValuex)
                        {
                            if(isValuex)
                            {
                                return sdf.format(new Date((long)value));
                            }
                            else
                            {
                                return super.formatLabel(value, isValuex);
                            }
                        }
                    });
                }
                if(topic.equals("izayazuna/feeds/humidity")){
                    ((TextView)findViewById(humidViewID)).setText(message.toString() + " %");
                    //Graph test
                    long xValue = new Date().getTime();
                    float yValue = Float.parseFloat(message.toString());
                    dbHandler.insertData("humidTable", xValue, yValue);
                    humidSeries.resetData(dbHandler.getDataPoint("humidTable"));
                    ((GraphView)findViewById(humidGraphID)).getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
                    {
                        @Override
                        public String formatLabel(double value, boolean isValuex)
                        {
                            if(isValuex)
                            {
                                return sdf.format(new Date((long)value));
                            }
                            else
                            {
                                return super.formatLabel(value, isValuex);
                            }
                        }
                    });

                }
                if(topic.equals("izayazuna/feeds/air quaility")){
                    ((TextView)findViewById(airVIewID)).setText(message.toString() + " ppm");
                    long xValue = new Date().getTime();
                    float yValue = Float.parseFloat(message.toString());
                    dbHandler.insertData("airTable", xValue, yValue);
                    dseries.resetData(dbHandler.getDataPoint("airTable"));
                    ((GraphView)findViewById(airGraphID)).getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
                    {
                        @Override
                        public String formatLabel(double value, boolean isValuex)
                        {
                            if(isValuex)
                            {
                                return sdf.format(new Date((long)value));
                            }
                            else
                            {
                                return super.formatLabel(value, isValuex);
                            }
                        }
                    });
                }
                if(topic.equals("izayazuna/feeds/led")){
                    if(message.toString().equals("1"))
                    {
                        ((ToggleButton)findViewById(ledButtonID)).setChecked(true);
                    }
                    else
                    {
                        ((ToggleButton)findViewById(ledButtonID)).setChecked(false);
                    }
                }
                if(topic.equals("izayazuna/feeds/fan")){
                    if(message.toString().equals("5"))
                    {
                        ((ToggleButton)findViewById(fanButtonID)).setChecked(true);
                    }
                    else
                    {
                        ((ToggleButton)findViewById(fanButtonID)).setChecked(false);
                    }
                }
                if(topic.equals("izayazuna/feeds/door")){
                    if(message.toString().equals("3"))
                    {
                        ((ToggleButton)findViewById(doorButtonID)).setChecked(true);
                    }
                    else
                    {
                        ((ToggleButton)findViewById(doorButtonID)).setChecked(false);
                    }
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
*/


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
