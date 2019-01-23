package pl.dawidkulpa.beautifultrainschedule.Stations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

import pl.dawidkulpa.beautifultrainschedule.R;

public class TrainStation {
    private static Random r= new Random();

    private int travelersDensity;  // how many travelers per day
    private Marker googleMarker;
    private String name;
    private LatLng position;

    private View dialogView;

    public TrainStation(String name, LatLng latLng, GoogleMap googleMap, Context context){
        this.name= name;
        travelersDensity= r.nextInt(300)+100;

        BitmapDrawable bitmapdraw= (BitmapDrawable)context.getResources().getDrawable(R.drawable.station_marker);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 175, 175, false);

        googleMarker= googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(name)
                .snippet(String.valueOf(travelersDensity) + " travelers/day")
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .anchor(0.5f, 1f));

        position= googleMarker.getPosition();
    }

    public void hideInfo(){
        googleMarker.hideInfoWindow();
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name= name;
        googleMarker.setTitle(name);
    }
    public void setTravelersDensity(int travelersDensity){
        this.travelersDensity= travelersDensity;
    }

    public int getDistanceTo(TrainStation second){
        float earthR= 6371;
        double latOne= Math.toRadians(position.latitude);
        double latTwo= Math.toRadians(second.position.latitude);
        double dLat= Math.toRadians(second.position.latitude-position.latitude);
        double dLong= Math.toRadians(second.position.longitude-position.longitude);

        double a= Math.sin(dLat/2)*Math.sin(dLat/2)+
                Math.cos(latOne)*Math.cos(latTwo)*
                        Math.sin(dLong/2)*Math.sin(dLong/2);
        double c= 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (int)(earthR*c);
    }

    public boolean compareMarker(Marker marker){
        return this.googleMarker.equals(marker);
    }

    public void openEditDialog(Context context){
        AlertDialog.Builder adb= new AlertDialog.Builder(context);

        adb.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onEditApply();
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        dialogView= inflater.inflate(R.layout.dialog_train_station_edit, null);

        adb.setView(dialogView);

        ((EditText)dialogView.findViewById(R.id.name_edit)).setText(name);
        ((EditText)dialogView.findViewById(R.id.tpd_edit)).setText(String.valueOf(travelersDensity));

        adb.create().show();
    }

    private void onEditApply(){
        name= ((EditText)dialogView.findViewById(R.id.name_edit)).getText().toString();
        travelersDensity= Integer.valueOf(((EditText)dialogView.findViewById(R.id.tpd_edit)).getText().toString());

        googleMarker.setTitle(name);
        googleMarker.setSnippet(String.valueOf(travelersDensity) + " travelers/day");
        googleMarker.hideInfoWindow();
        googleMarker.showInfoWindow();
    }

    public LatLng getPosition() {
        return position;
    }
}
