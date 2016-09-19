package com.example.sheahtaa.popularmovie;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lenovo on 03/01/2016.
 */
public class PosterImage {
    String poster;
    public PosterImage(String poster){
        this.poster = poster;

    }

   public  Target traget = new Target() {

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/popularMoviesImage");
                    try {
                        if(!file.exists()){
                            file.mkdir();
                        }
                        File poster_file = new File(file,poster);

                        Log.i("image",poster_file.getAbsolutePath());

                        poster_file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(poster_file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {
            }
        }

    };

}
