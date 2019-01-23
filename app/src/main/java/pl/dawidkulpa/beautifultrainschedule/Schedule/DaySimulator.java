package pl.dawidkulpa.beautifultrainschedule.Schedule;

import java.util.ArrayList;
import java.util.Random;

import pl.dawidkulpa.beautifultrainschedule.Trains.Train;

public class DaySimulator {
    private Schedule schedule;

    public DaySimulator(Schedule schedule){
        this.schedule= schedule;
    }

    public void run(){
        ArrayList<Integer> travelersLeftTo= new ArrayList<>(schedule.getStationsNo());
        ArrayList<Train> trains= new ArrayList<>();
        ArrayList<Time> trainsAt= new ArrayList<>();

        //Przejdz przez wszystkie stacje
        for(int i=0; i<schedule.getStationsNo(); i++){
            //Znajdz rozklad dla stacji (jakie pociagi kolejno o ktorych godzinach


            //Znajdz ilu pasazerow zabieraly pociagi
            //Jezeli pociag wyjechal z pustymi miejscami -> odejmij punktow

            //Im wieksze ilosci pasazerow po przejezdzie pociagow na stacji zostaly tym mniej pkt
        }
    }

    public double getAllTraveledPoints(){
        Random r= new Random();
        return r.nextInt(200);
    }

    public double getHowOftenEmptyPoints(){
        Random r= new Random();
        return r.nextInt(200);
    }
}
