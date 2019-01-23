package pl.dawidkulpa.beautifultrainschedule.Schedule;

import java.util.ArrayList;
import java.util.Random;

import pl.dawidkulpa.beautifultrainschedule.Stations.TrainStation;
import pl.dawidkulpa.beautifultrainschedule.Trains.Train;

public class Schedule {
    private static Random r= new Random();

    private ArrayList<StationSchedule> stationSchedules;
    private ArrayList<Train> trains;

    private double travelersPoints;
    private double powerUsagePoints;


    public Schedule(){
        stationSchedules= new ArrayList<>();
    }

    public void parse(ArrayList<Train> trains, ArrayList<TrainStation> trainStations){
        this.trains= trains;

        for(int i=0; i<trainStations.size(); i++){
            stationSchedules.add(new StationSchedule(trainStations.get(i).getName(), trains));
        }
    }

    public void evaluate(){
        //TODO: Check if: All travelers have traveled, how often train was driving empty
        DaySimulator daySimulator= new DaySimulator(this);
        daySimulator.run();

        travelersPoints= daySimulator.getAllTraveledPoints()+daySimulator.getHowOftenEmptyPoints();

        powerUsagePoints=0;
        for(int i=0; i<trains.size(); i++){
            powerUsagePoints+= trains.get(i).getrLength()*trains.get(i).getKwPerKm();
            powerUsagePoints+= trains.get(i).getTrainStations().size()*trains.get(i).getKwPerStart();
        }
        powerUsagePoints= 1/powerUsagePoints;
        powerUsagePoints*= 10000;
    }

    public double getTravelersPoints() {
        return travelersPoints;
    }

    public double getPowerUsagePoints() {
        return powerUsagePoints;
    }

    public int getStationsNo(){
        return stationSchedules.size();
    }
    public String getStationName(int idx){
        return stationSchedules.get(idx).name;
    }

    public ArrayList<Train> getTrains(){
        return trains;
    }

    public int getStationIdx(String name){
        for(int i=0; i<stationSchedules.size(); i++){
            if(stationSchedules.get(i).name.equals(name))
                return i;
        }

        return 0;
    }
}
