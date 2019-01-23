package pl.dawidkulpa.beautifultrainschedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.dawidkulpa.beautifultrainschedule.Views.ScheduleView;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if(MainActivity.bestSchedule!=null)
            ((ScheduleView)findViewById(R.id.schedule_view)).setSchedule(MainActivity.bestSchedule);
    }
}
