package com.example.sheahtaa.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Moviefragment.Callback {


    boolean mtwo;
    boolean connect=true;
    static boolean flag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isOnline()) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_actiivity_id, new Moviefragment())
                        .commit();
                flag=false;

            } }else {


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_actiivity_id, new FavoriteFragment())
                    .commit();
                Toast.makeText(MainActivity.this,"Check internet connection",Toast.LENGTH_LONG).show();
            flag=true;

        }


        if (findViewById(R.id.container) != null) {
            mtwo = true;
        } else {
            mtwo = false;
        }

    }

    @Override
    public void selectMovie(Movie mov) {
        if (!mtwo) {
            Intent M_I = new Intent(MainActivity.this, DetailActivity.class);
            M_I.putExtra("Copy_From_Movie", mov);
            startActivity(M_I);
        } else {
            DetailFragment Fragment = DetailFragment.getInstance(mov);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, Fragment).commit();
        }

    }

    public boolean isOnline() {


        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i("onnn", cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)==null?"y":"n");

        if (
        //cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            connect=true;
            return connect;
        }
        connect=false;
        return connect;

    }
}