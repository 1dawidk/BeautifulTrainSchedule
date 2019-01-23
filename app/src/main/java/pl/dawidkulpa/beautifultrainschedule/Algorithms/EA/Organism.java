package pl.dawidkulpa.beautifultrainschedule.Algorithms.EA;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import pl.dawidkulpa.beautifultrainschedule.Schedule.Schedule;
import pl.dawidkulpa.beautifultrainschedule.Trains.Train;

public class Organism {
    private static Random r= new Random();

    private ArrayList<Train> trains;
    private EAParameters eap;

    private double points;


    public Organism(Organism mother, Organism father){
        this.trains= new ArrayList<>();
        this.eap= mother.eap;

        int from;

        for(int i=0; i<mother.trains.size(); i++){
            from= r.nextInt(2);

            if(from==0)
                trains.add(father.trains.get(i));
            else
                trains.add(mother.trains.get(i));
        }

        points=-1;
    }

    public Organism(){
        this.trains= new ArrayList<>();
        this.points=-1;
    }

    public static Organism createRandom(EAParameters eap){
        Organism organism = new Organism();

        organism.eap= eap;

        Train.zeroNames();
        for(int i=0; i<eap.trainsNo; i++){
            organism.trains.add(Train.createRandom(eap.trainStations));
        }

        organism.points=-1;

        return organism;
    }

    public void mutation(){
        int level= r.nextInt(20);

        //Soft mutation (remove or add one station to train)
        if(level<19){
            trains.get(r.nextInt(trains.size())).mutate();
        }
        //Hard mutation (remove one train and add random)
        else {
            int at= r.nextInt(trains.size());
            trains.remove(at);
            trains.add(Train.createRandom(eap.trainStations));
        }

        points=-1;
    }

    public Organism compare(Organism with){
        this.evaluate();
        with.evaluate();

        if(this.points>with.points)
            return this;
        else
            return with;
    }

    public void show(String name){
        Log.e("Organism "+name, String.valueOf(evaluate()));
    }

    public double evaluate(){
        if(points<0){
            Schedule schedule = new Schedule();
            schedule.parse(trains, eap.trainStations);

            schedule.evaluate();

            points= schedule.getTravelersPoints() + schedule.getPowerUsagePoints();
        }

        return points;
    }

    public ArrayList<Train> getTrains() {
        return trains;
    }

    public void setTrains(ArrayList<Train> trains) {
        this.trains = trains;
    }
}
