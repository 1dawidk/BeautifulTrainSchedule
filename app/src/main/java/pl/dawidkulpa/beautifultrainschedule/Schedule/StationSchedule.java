package pl.dawidkulpa.beautifultrainschedule.Schedule;

import java.util.ArrayList;

import pl.dawidkulpa.beautifultrainschedule.Stations.TrainStation;
import pl.dawidkulpa.beautifultrainschedule.Trains.Train;

public class StationSchedule {
    public String name;

    public StationSchedule(String stationName){
        name= stationName;
    }

    public StationSchedule(String stationName, ArrayList<Train> trains){
        name= stationName;
        fillUp(trains);
    }

    public void fillUp(ArrayList<Train> trains){
        for(int i=0; i<trains.size(); i++){

        }
    }
}
