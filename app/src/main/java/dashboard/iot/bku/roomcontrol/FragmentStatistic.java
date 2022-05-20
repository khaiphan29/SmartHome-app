package dashboard.iot.bku.roomcontrol;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ADMIN on 3/22/2022.
 */

public class FragmentStatistic extends Fragment {

//    LineChart lineChart;
    GraphView graphView;
    GraphView graphView1;
    GraphView graphView2;
    Button preButton, nextButton, resetButton, preButton1, nextButton1, resetButton1, preButton2, nextButton2, resetButton2;
    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ((RoomService)getActivity()).dataProc.setStatID(R.id.TempLine, R.id.HumiLine, R.id.AirLine);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_fragment_statistic, container, false);

        graphView = view.findViewById(R.id.TempLine);
        ((RoomService)getActivity()).dataProc.tempSeries.setDrawBackground(true);
        ((RoomService)getActivity()).dataProc.tempSeries.setDrawDataPoints(true);
        ((RoomService)getActivity()).dataProc.tempSeries.setColor(Color.rgb(232, 98, 53));
        ((RoomService)getActivity()).dataProc.tempSeries.setBackgroundColor(Color.argb(100,228, 150, 125));
        ((RoomService)getActivity()).dataProc.tempSeries.setThickness(8);

        graphView.getViewport().setXAxisBoundsManual(true);
        //graphView.getGridLabelRenderer().setNumHorizontalLabels(4);
        graphView.getViewport().setMaxX(((RoomService)getActivity()).dataProc.tempSeries.getHighestValueX());
        graphView.getViewport().setMinX(((RoomService)getActivity()).dataProc.tempSeries.getLowestValueX());
        graphView.getViewport().setScalable(true);

        graphView.addSeries(((RoomService)getActivity()).dataProc.tempSeries);

        graphView1 = view.findViewById(R.id.HumiLine);
        ((RoomService)getActivity()).dataProc.humidSeries.setDrawBackground(true);
        ((RoomService)getActivity()).dataProc.humidSeries.setDrawDataPoints(true);
        ((RoomService)getActivity()).dataProc.humidSeries.setColor(Color.rgb(50, 144, 227));
        ((RoomService)getActivity()).dataProc.humidSeries.setBackgroundColor(Color.argb(100,125, 180, 228));
        ((RoomService)getActivity()).dataProc.humidSeries.setThickness(8);

        graphView1.getViewport().setXAxisBoundsManual(true);
        //graphView1.getGridLabelRenderer().setNumHorizontalLabels(4);
        graphView1.getViewport().setMaxX(((RoomService)getActivity()).dataProc.humidSeries.getHighestValueX());
        graphView1.getViewport().setMinX(((RoomService)getActivity()).dataProc.humidSeries.getLowestValueX());
        graphView1.getViewport().setScalable(true);

        graphView1.addSeries(((RoomService)getActivity()).dataProc.humidSeries);


        graphView2 = view.findViewById(R.id.AirLine);
        ((RoomService)getActivity()).dataProc.airSeries.setDrawBackground(true);
        ((RoomService)getActivity()).dataProc.airSeries.setDrawDataPoints(true);
        ((RoomService)getActivity()).dataProc.airSeries.setColor(Color.rgb(120, 204, 47));
        ((RoomService)getActivity()).dataProc.airSeries.setBackgroundColor(Color.argb(100,151, 202, 107));
        ((RoomService)getActivity()).dataProc.airSeries.setThickness(8);

        graphView2.getViewport().setXAxisBoundsManual(true);
        //graphView1.getGridLabelRenderer().setNumHorizontalLabels(4);
        graphView2.getViewport().setMaxX(((RoomService)getActivity()).dataProc.airSeries.getHighestValueX());
        graphView2.getViewport().setMinX(((RoomService)getActivity()).dataProc.airSeries.getLowestValueX());
        graphView2.getViewport().setScalable(true);

        graphView2.addSeries(((RoomService)getActivity()).dataProc.airSeries);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValuex) {
                if (isValuex) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValuex);
                }
            }
        });

        graphView1.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValuex) {
                if (isValuex) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValuex);
                }
            }
        });

        graphView2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValuex) {
                if (isValuex) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValuex);
                }
            }
        });

        preButton = view.findViewById(R.id.temp_pre_button);
        nextButton = view.findViewById(R.id.temp_next_button);
        resetButton = view.findViewById(R.id.temp_latest_button);

        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("mqtt", "Start Click");
                ((RoomService) getActivity()).dataProc.setTempPos(1);
                Log.d("mqtt", "Complete Click");
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoomService) getActivity()).dataProc.setTempPos(-1);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoomService) getActivity()).dataProc.setTempPos(0);
            }
        });

        preButton1 = view.findViewById(R.id.humid_pre_button);
        nextButton1 = view.findViewById(R.id.humid_next_button);
        resetButton1 = view.findViewById(R.id.humid_latest_button);

        preButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("mqtt", "Start Click");
                ((RoomService) getActivity()).dataProc.setHumidPos(1);
                Log.d("mqtt", "Complete Click");
            }
        });

        nextButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoomService) getActivity()).dataProc.setHumidPos(-1);
            }
        });

        resetButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoomService) getActivity()).dataProc.setHumidPos(0);
            }
        });

        preButton2 = view.findViewById(R.id.air_pre_button);
        nextButton2 = view.findViewById(R.id.air_next_button);
        resetButton2 = view.findViewById(R.id.air_latest_button);

        preButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("mqtt", "Start Click");
                ((RoomService) getActivity()).dataProc.setAirPos(1);
                //Log.d("mqtt", "Complete Click");
            }
        });

        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoomService) getActivity()).dataProc.setAirPos(-1);
            }
        });

        resetButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoomService) getActivity()).dataProc.setAirPos(0);
            }
        });


        return view;
    }

    private void addSeries(GraphView graphView, LineGraphSeries<DataPoint> series)
    {
        graphView.addSeries(series);
    }
}
