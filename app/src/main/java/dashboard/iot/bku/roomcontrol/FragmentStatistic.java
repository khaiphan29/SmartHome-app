package dashboard.iot.bku.roomcontrol;

import android.database.sqlite.SQLiteDatabase;
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
        graphView.addSeries(((RoomService)getActivity()).dataProc.tempSeries);
        //graphView.getViewport().setXAxisBoundsManual(true);
        //graphView.getViewport().setScalable(true);
        //graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        graphView.getViewport().scrollToEnd();


        graphView1 = view.findViewById(R.id.HumiLine);
        addSeries(graphView1, ((RoomService) getActivity()).dataProc.humidSeries);
        //graphView1.getViewport().setScrollable(true);
        graphView1.getViewport().scrollToEnd();


        graphView2 = view.findViewById(R.id.AirLine);
        addSeries(graphView2, ((RoomService) getActivity()).dataProc.airSeries);
        //graphView2.getViewport().setScrollable(true);
        graphView2.getViewport().scrollToEnd();

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


        return view;
    }

    private void addSeries(GraphView graphView, LineGraphSeries<DataPoint> series)
    {
        graphView.addSeries(series);
    }
}
