package dashboard.iot.bku.roomcontrol;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by khaiphan on 30/04/2022.
 */

public class Model {

    private Context appContext;
    private Context actContext;
    private Activity activity;

    private MQTTHelper mqttHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

    private String airData = "-- ppm";
    private String tempData = "-- °C";
    private String humidData = "-- %";

    private int tempViewID, humidViewID, airVIewID, ledButtonID, fanButtonID, doorButtonID, tempGraphID, humidGraphID, airGraphID;

    //Graph
    private DBHandler dbHandler;
    public LineGraphSeries<DataPoint> tempSeries = new LineGraphSeries<>(new DataPoint[0]);
    public LineGraphSeries<DataPoint> humidSeries = new LineGraphSeries<>(new DataPoint[0]);
    public LineGraphSeries<DataPoint> airSeries = new LineGraphSeries<>(new DataPoint[0]);

    Model (Context _appContext, Context _actContext , Activity _activity) {
        this.appContext = _appContext;
        this.activity = _activity;
        this.actContext = _actContext;
        dbHandler = new DBHandler(actContext);
        dbHandler.resetData();
    }

    public void setRoomID (int _tempViewID, int _humidViewID, int _airVIewID, int _ledButtonID, int _fanButtonID, int _doorButtonID) {
        tempViewID = _tempViewID;
        humidViewID = _humidViewID;
        airVIewID = _airVIewID;
        ledButtonID = _ledButtonID;
        fanButtonID = _fanButtonID;
        doorButtonID = _doorButtonID;
    }

    public void setStatID (int _tempGraphID, int _humidGraphID, int _airGraphID) {
        tempGraphID = _tempGraphID;
        humidGraphID = _humidGraphID;
        airGraphID = _airGraphID;
    }

    public void updateStatFrag () {
        addSer((GraphView)activity.findViewById(tempGraphID));
        addSer1((GraphView)activity.findViewById(humidGraphID));
        addSer2((GraphView)activity.findViewById(airGraphID));
    }


    private void addSeries(GraphView graphView, LineGraphSeries<DataPoint> series)
    {
        graphView.addSeries(series);
    }

    //Graph test
    private void addSer(GraphView graphView)
    {
        graphView.addSeries(tempSeries);
    }
    private void addSer1(GraphView graphView)
    {
        graphView.addSeries(humidSeries);
    }
    private void addSer2(GraphView graphView)
    {
        graphView.addSeries(airSeries);
    }

    public String getTempData() {return tempData;}
    public String getHumidData() {return humidData;}
    public String getAirData() {return airData;}

    private void setTempData(String data) { this.tempData = data;}

    public void sendDataMQTT(String topic, String value)
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

    private String getAlphaNumericString(int n)
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

    public void startMQTT()
    {
        mqttHelper = new MQTTHelper( appContext, getAlphaNumericString(6));
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
                    try {
                        ((TextView) activity.findViewById(tempViewID)).setText(message.toString() + " °C");
                    } catch (Throwable str) {}
                    setTempData( message.toString() + " °C");
                    //Log.d("mqtt", "Temp: " + getTempData());
                    long xValue = new Date().getTime();
                    float yValue = Float.parseFloat(message.toString());
                    dbHandler.insertData("tempTable", xValue, yValue);
                    tempSeries.resetData(dbHandler.getDataPoint("tempTable"));
                }
                if(topic.equals("izayazuna/feeds/humidity")){
                    try {
                        ((TextView)activity.findViewById(humidViewID)).setText(message.toString() + " %");
                    } catch (Throwable str) {}

                    humidData = message.toString() + " %";
                    //Graph test
                    long xValue = new Date().getTime();
                    float yValue = Float.parseFloat(message.toString());
                    dbHandler.insertData("humidTable", xValue, yValue);
                    humidSeries.resetData(dbHandler.getDataPoint("humidTable"));

                }
                if(topic.equals("izayazuna/feeds/air quaility")){
                    try {
                        ((TextView)activity.findViewById(humidViewID)).setText(message.toString() + " %");
                    } catch (Throwable str) {}
                    airData = message.toString() + " ppm";
                    long xValue = new Date().getTime();
                    float yValue = Float.parseFloat(message.toString());
                    dbHandler.insertData("airTable", xValue, yValue);
                    airSeries.resetData(dbHandler.getDataPoint("airTable"));
                }
                if(topic.equals("izayazuna/feeds/led")){
                    if(message.toString().equals("1"))
                    {
                        ((ToggleButton)activity.findViewById(ledButtonID)).setChecked(true);
                    }
                    else
                    {
                        ((ToggleButton)activity.findViewById(ledButtonID)).setChecked(false);
                    }
                }
                if(topic.equals("izayazuna/feeds/fan")){
                    if(message.toString().equals("5"))
                    {
                        ((ToggleButton)activity.findViewById(fanButtonID)).setChecked(true);
                    }
                    else
                    {
                        ((ToggleButton)activity.findViewById(fanButtonID)).setChecked(false);
                    }
                }
                if(topic.equals("izayazuna/feeds/door")){
                    if(message.toString().equals("3"))
                    {
                        ((ToggleButton)activity.findViewById(doorButtonID)).setChecked(true);
                    }
                    else
                    {
                        ((ToggleButton)activity.findViewById(doorButtonID)).setChecked(false);
                    }
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
