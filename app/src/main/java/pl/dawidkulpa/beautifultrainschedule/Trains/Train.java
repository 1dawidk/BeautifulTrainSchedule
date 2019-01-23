package pl.dawidkulpa.beautifultrainschedule.Trains;

import java.util.ArrayList;
import java.util.Random;

import pl.dawidkulpa.beautifultrainschedule.Stations.TrainStation;
import pl.dawidkulpa.beautifultrainschedule.Schedule.Time;

public class Train {
    private static Random r= new Random();

    private static String[] trainNames= {
            "Alfa", "Bravo", "Charlie", "Delta", "Echo",
            "Foxtrot", "Golf", "Hotel", "India", "Juliet",
            "Kilo", "Lima", "Mike", "November", "Oscar",
            "Papa", "Quebec", "Romeo", "Sierra", "Tango",
            "Uniform", "Victor", "Whiskey", "X-ray",
            "Yankee", "Zulu"};

    private static int lastUsedName=0;

    private static final int MIN_VELOCITY=100;
    private static final int MAX_VELOCITY=200;

    private static final int MIN_CAPACITY=100;
    private static final int MAX_CAPACITY=200;

    ArrayList<TrainStation> trainStations; // Rozklad jazdy czyli lista kolejno odwiedzanych stacji
                                            // w ciagu 24h
    ArrayList<Time> timesToNext;
    private String name;
    private int velocity;
    private int capacity;
    private int kwPerKm;
    private int kwPerStart;
    private int repeatNo;
    private int rLength;

    public static Train createRandom(ArrayList<TrainStation> trainStations){
        Random r= new Random();
        Train train= new Train();
        ArrayList<TrainStation> tmpStations= new ArrayList<>(trainStations);

        //train.velocity= r.nextInt(MAX_VELOCITY-MIN_VELOCITY)+MIN_VELOCITY;
        train.velocity=150;
        train.kwPerKm=10;
        train.kwPerStart=100;
        train.capacity= r.nextInt(MAX_CAPACITY-MIN_CAPACITY)+MIN_CAPACITY;
        train.name= trainNames[lastUsedName];
        lastUsedName++;
        if(lastUsedName>=26){
            lastUsedName=0;
        }

        train.trainStations= new ArrayList<>();
        int tsNo= r.nextInt(trainStations.size()-1)+2;
        int sIdx;
        int rLength=0;

        for(int i=0; i<tsNo; i++){
            sIdx= r.nextInt(tmpStations.size());
            train.trainStations.add(tmpStations.get(sIdx));

            if(train.trainStations.size()>1){
                rLength+= train.trainStations.get(train.trainStations.size()-2).getDistanceTo(
                        train.trainStations.get(train.trainStations.size()-1));
            }

            if((float)rLength/train.velocity>12) {
                train.trainStations.remove(train.trainStations.size()-1);
                break;
            }
            tmpStations.remove(sIdx);
        }

        double routeTimeLength= ((double)rLength/train.velocity)*2;
        train.repeatNo= (int)(24/routeTimeLength);
        if(train.repeatNo<1){
            train.repeatNo=1;
        }

        train.rLength= rLength*train.repeatNo;

        return train;
    }

    public static void zeroNames(){
        lastUsedName=0;
    }

    public void mutate(){
        int type= r.nextInt(2);

        if(type==0 && trainStations.size()>2){
            //trainStations.remove(r.nextInt(trainStations.size()));
        } else {
            //TODO: Add new random station
        }
    }

    private Time findTimeToNext(int idx){
        int len= trainStations.get(idx).getDistanceTo(trainStations.get(idx+1));

        Time time= new Time();
        double t= (double)len/velocity;
        time.h= (int)t;
        time.m= (int)((t*60)%60);

        return time;
    }
    public void findTimesToNext() {
        timesToNext = new ArrayList<>();

        for (int i = 0; i < trainStations.size() - 1; i++) {
            timesToNext.add(findTimeToNext(i));
        }
    }

    public ArrayList<TrainStation> getTrainStations() {
        return trainStations;
    }
    public int getRepeatNo() {
        return repeatNo;
    }
    public ArrayList<Time> getToNextTimes(){
        return timesToNext;
    }

    public int getKwPerKm() {
        return kwPerKm;
    }

    public int getKwPerStart() {
        return kwPerStart;
    }

    public int getrLength() {
        return rLength;
    }
}
