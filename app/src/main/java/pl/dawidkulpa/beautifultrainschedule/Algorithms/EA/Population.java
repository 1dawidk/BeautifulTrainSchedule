package pl.dawidkulpa.beautifultrainschedule.Algorithms.EA;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    ArrayList<Organism> organisms;

    public void init(int startSize, EAParameters eap){
        organisms = new ArrayList<>();

        for(int i=0; i<startSize; i++){
            organisms.add(Organism.createRandom(eap));
        }
    }

    public void runEpoch(){
        Random r= new Random();
        ArrayList<Organism> newOrganisms= new ArrayList<>();
        Organism mother;
        Organism father;

        int o_one;
        int o_two;

        for(int i=0; i<organisms.size(); i++){
            o_one= r.nextInt(organisms.size());
            o_two= r.nextInt(organisms.size());
            mother= organisms.get(o_one).compare(organisms.get(o_two));

            o_one= r.nextInt(organisms.size());
            o_two= r.nextInt(organisms.size());
            father= organisms.get(o_one).compare(organisms.get(o_two));


            newOrganisms.add(new Organism(mother, father));
        }

        int mutateNo= r.nextInt(organisms.size()/5);
        for (int i=0; i<mutateNo; i++){
            newOrganisms.get(r.nextInt(organisms.size())).mutation();
        }

        this.organisms= newOrganisms;
    }

    public ArrayList<Organism> getOrganisms(){
        return organisms;
    }

    public Organism getBestOrganism(){
        Organism best= organisms.get(0);

        for(int i=1; i<organisms.size(); i++){
            if(organisms.get(i).evaluate()>best.evaluate()){
                best= organisms.get(i);
            }
        }

        return best;
    }
}
