package com.example.sheahtaa.popularmovie;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import com.example.sheahtaa.popularmovie.Movie;
import com.example.sheahtaa.popularmovie.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {


    GridView favGrid;
    ArrayList<Movie> listMovie;
    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View view= inflater.inflate(R.layout.fragment_favorite, container, false);
         favGrid= (GridView) view.findViewById(R.id.gridView2);
        setHasOptionsMenu(true);


        return view;
    }
    private void movieFav(){
        Cursor cursor=getActivity().getContentResolver().query(MovieTable.CONTENT_URI, null, null, null, null);
        listMovie= (ArrayList<Movie>) MovieTable.getRows(cursor, false);


        final ImageAdapter imageAdapter=new ImageAdapter(getActivity(),listMovie);
        favGrid.setAdapter(imageAdapter);
        favGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie=null;
                 movie = (Movie) parent.getItemAtPosition(position);

                ((MainActivity) getActivity()).selectMovie(movie);
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        movieFav();

    }


    public class ImageAdapter extends BaseAdapter {
        private Context context;

        private ArrayList<Movie>clist;

        public ImageAdapter (Context c,ArrayList mov)
        {

            context=c;
            clist=mov;
            Log.d("clist", clist == null ? "y" : "n");
        }



        @Override
        public int getCount() {
            return clist.size();
        }

        @Override
        public Object getItem(int position) {
            return clist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //ImageView imageView;
            String Base="http://image.tmdb.org/t/p/w185/";
            String s= clist.get(position).getPoster();
            Holder holder=null;

            ImageView image;


            if(convertView==null){
                // image=new ImageView(context);
                LayoutInflater layoutInflater= (LayoutInflater)parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView=layoutInflater.inflate(R.layout.adapter,parent,false);
                holder=new Holder();
                convertView.setTag(holder);

            }else {
                holder= (Holder) convertView.getTag();

            }


            holder.imageView= new ImageView(context);
            holder.imageView= (ImageView) convertView;

            File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/popularMoviesImage"+
                    clist.get(position).getPoster());
            Picasso.with(getActivity())
                    .load(file)

                    .placeholder(R.drawable.image)
                            //.noFade()
                            //.resize(300,250)
                            //.centerCrop()
                    .into(holder.imageView);
            Log.i("aaaaa", String.valueOf(new File(file.getAbsolutePath())));

            return convertView;
        }
        public class Holder{
            ImageView imageView;
        }
    }

}
