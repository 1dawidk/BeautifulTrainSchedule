package pl.dawidkulpa.beautifultrainschedule.Algorithms.EA;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import pl.dawidkulpa.beautifultrainschedule.Stations.TrainStation;

public class EA extends AsyncTask<String, String, Integer> {

    public interface OnFinishListener{
        void onFinish(ArrayList<Organism> organisms);
    }

    private Population population;
    private OnFinishListener onFinishListener;
    private ArrayList<TrainStation> trainStations;

    public EA(ArrayList<TrainStation> trainStations, OnFinishListener onFinishListener){
        this.trainStations= trainStations;
        this.onFinishListener= onFinishListener;
    }


    @Override
    protected Integer doInBackground(String... strings) {
        EAParameters eap = new EAParameters();

        //TODO: Define organism start parameters
        eap.trainStations= trainStations;
        eap.trainsNo=5;

        population= new Population();
        population.init(100, eap);

        for(int i=0; i<100; i++){
            Log.i("EA", "Epoch "+i);
            population.runEpoch();
        }


        return (int)Math.round(population.getOrganisms().get(0).evaluate());
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        onFinishListener.onFinish(population.getOrganisms());
    }

    public Organism getBestOrganism(){
        return population.getBestOrganism();
    }
}
