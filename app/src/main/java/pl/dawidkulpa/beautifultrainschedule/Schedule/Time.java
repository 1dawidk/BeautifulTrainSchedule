package pl.dawidkulpa.beautifultrainschedule.Schedule;

public class Time {
    public int h;
    public int m;

    public Time(){
        h=0;
        m=0;
    }

    public void add(Time t){
        h+= t.h;

        if(m+t.m>60)
            h++;

        m= (m+t.m)%60;
    }
}
