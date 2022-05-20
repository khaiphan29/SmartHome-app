package dashboard.iot.bku.roomcontrol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by ADMIN on 3/22/2022.
 */

public class FragmentRoom extends Fragment {

    TextView temp, humid, air;
    ToggleButton led, fan, door;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ((RoomService)getActivity()).dataProc.setRoomID(R.id.Temp, R.id.Humi, R.id.Air, R.id.led, R.id.fan, R.id.door);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_fragment_room, container, false);

        //find ID
        temp = view.findViewById(R.id.Temp);
        humid = view.findViewById(R.id.Humi);
        air = view.findViewById(R.id.Air);
        led = view.findViewById(R.id.led);
        fan = view.findViewById(R.id.fan);
        door = view.findViewById(R.id.door);

        temp.setText(((RoomService)getActivity()).dataProc.getTempData());
        humid.setText(((RoomService)getActivity()).dataProc.getHumidData());
        air.setText(((RoomService)getActivity()).dataProc.getAirData());

        //((RoomService)getActivity()).dataProc.updateFragmentRoom();

        //Do

        led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.d(" mqtt ", " Button is checked ");
                    ((RoomService)getActivity()).dataProc.sendDataMQTT("izayazuna/feeds/led","1");
                }
                else {
                    Log .d(" mqtt :", " Button is not checked ");
                    ((RoomService)getActivity()).dataProc.sendDataMQTT("izayazuna/feeds/led","0");
                }
            }
        });

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.d(" mqtt ", " Button is checked ");
                    ((RoomService)getActivity()).dataProc.sendDataMQTT("izayazuna/feeds/fan","5");
                }
                else {
                    Log .d(" mqtt :", " Button is not checked ");
                    ((RoomService)getActivity()).dataProc.sendDataMQTT("izayazuna/feeds/fan","4");
                }
            }
        });
        door.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.d(" mqtt ", " Button is checked ");
                    ((RoomService)getActivity()).dataProc.sendDataMQTT("izayazuna/feeds/door","3");
                }
                else {
                    Log .d(" mqtt :", " Button is not checked ");
                    ((RoomService)getActivity()).dataProc.sendDataMQTT("izayazuna/feeds/door","2");
                }
            }
        });




        return view;
    }
}
