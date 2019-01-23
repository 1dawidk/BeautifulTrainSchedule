package pl.dawidkulpa.beautifultrainschedule.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import pl.dawidkulpa.beautifultrainschedule.Schedule.Schedule;
import pl.dawidkulpa.beautifultrainschedule.Schedule.Time;
import pl.dawidkulpa.beautifultrainschedule.Stations.TrainStation;
import pl.dawidkulpa.beautifultrainschedule.Trains.Train;

public class ScheduleView extends View {

    private static int[] rs= {
            0xff, 0x00, 0x00, 0x00, 0xff, 0xba, 0xff
    };
    private static int[] gs= {
            0x00, 0xff, 0x00, 0xff, 0xcc, 0x00, 0x00
    };
    private static int[] bs= {
            0x00, 0x00, 0xff, 0xa2, 0x00, 0xff, 0xd8
    };

    private Schedule schedule;

    private Paint textPaint;
    private Paint trainPaint;
    private Paint stationPaint;
    private Paint timePaint;

    private int w;
    private int h;

    private int leftShift;
    private int rightShift;
    private int btmShift;
    private int topShift;

    public ScheduleView(Context context) {
        super(context);
        init();
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setSchedule(Schedule schedule){
        this.schedule= schedule;
        this.invalidate();
    }

    private void init(){
        textPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        trainPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        stationPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        timePaint= new Paint(Paint.ANTI_ALIAS_FLAG);


        stationPaint.setARGB(255, 255, 193, 7);
        stationPaint.setStrokeWidth(5);

        timePaint.setARGB(40, 0, 0, 0);
        timePaint.setStrokeWidth(3);

        textPaint.setARGB(250, 0, 0, 0);
        textPaint.setTextSize(20);

        trainPaint.setARGB(255, 0, 255, 0);
        trainPaint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = getSuggestedMinimumHeight() + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        this.w= w;
        this.h= h;

        leftShift=100;
        btmShift=25;
        rightShift=30;
        topShift=10;

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float sSep; //Station lines separation (px)
        int hLines; //Station lines no
        float tSep= (w-leftShift-rightShift)/(float)24; //Time lines separation (px)

        //If schedule is set
        if(schedule!=null) {
            hLines= schedule.getStationsNo(); // Get station number
        } else {
            hLines=5; //Set some value
        }

        //Calculate station lines separation
        sSep = (h-topShift-btmShift) / (float)(hLines-1);
        //Draw station lines
        for (int i = 0; i < hLines; i++) {
            canvas.drawLine(leftShift, topShift+(i*sSep), w-rightShift, topShift+(i*sSep), stationPaint);
            if(schedule!=null){
                canvas.drawText(schedule.getStationName(i), leftShift-textPaint.measureText(schedule.getStationName(i))-10, topShift+(i*sSep)+5, textPaint);
            }
        }

        //Draw time lines
        for(int i=0; i<25; i++){
            canvas.drawLine(leftShift+(i*tSep), topShift, leftShift+(i*tSep), h-btmShift, timePaint);
            if(i<20)
                canvas.drawText(String.valueOf(i+4)+":00",leftShift+(i*tSep)-20,h-btmShift+25,textPaint);
            else
                canvas.drawText(String.valueOf(i-20)+":00",leftShift+(i*tSep)-20,h-btmShift+25,textPaint);
        }

        //If schedule is set
        if(schedule!=null){
            //Get schedule trains
            ArrayList<Train> trains= schedule.getTrains();

            //Train station to station line parameters
            float startX;
            float startY;
            float endX;
            float endY;
            //Station from to indexes
            int thisStationIdx;
            int nextStationIdx;

            //Loop for every train draw route lines
            for(int i=0; i<trains.size(); i++){
                //Get train's stations
                ArrayList<TrainStation> stations= trains.get(i).getTrainStations();
                ArrayList<Time> timesToNext;

                //Set train route line color
                trainPaint.setARGB(255, rs[i], gs[i], bs[i]);

                //Calculate and get times between neighbors stations
                trains.get(i).findTimesToNext();
                timesToNext= trains.get(i).getToNextTimes();

                //Time at "current" station
                Time lastTime= new Time();

                //For every route repeat
                for(int h=0; h<trains.get(i).getRepeatNo(); h++) {
                    //Draw forward lines
                    for(int j=0; j<stations.size()-1; j++){
                        thisStationIdx = schedule.getStationIdx(stations.get(j).getName());
                        nextStationIdx = schedule.getStationIdx(stations.get(j + 1).getName());
                        startY = topShift + (thisStationIdx * sSep);
                        endY = topShift + (nextStationIdx * sSep);

                        startX = leftShift + (lastTime.h * tSep);
                        startX += ((float) lastTime.m / 60) * tSep;

                        lastTime.add(timesToNext.get(j));

                        endX = leftShift + (lastTime.h * tSep);
                        endX += ((float) lastTime.m / 60) * tSep;

                        canvas.drawLine(startX, startY, endX, endY, trainPaint);
                    }

                    //Draw backward lines
                    for(int j=stations.size()-1; j>0; j--){
                        thisStationIdx = schedule.getStationIdx(stations.get(j).getName());
                        nextStationIdx = schedule.getStationIdx(stations.get(j - 1).getName());
                        startY = topShift + (thisStationIdx * sSep);
                        endY = topShift + (nextStationIdx * sSep);

                        startX = leftShift + (lastTime.h * tSep);
                        startX += ((float) lastTime.m / 60) * tSep;

                        lastTime.add(timesToNext.get(j-1));

                        endX = leftShift + (lastTime.h * tSep);
                        endX += ((float) lastTime.m / 60) * tSep;

                        canvas.drawLine(startX, startY, endX, endY, trainPaint);
                    }
                }
            }
        }

    }
}
