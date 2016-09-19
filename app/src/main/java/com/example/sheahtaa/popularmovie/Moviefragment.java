package com.example.sheahtaa.popularmovie;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Sheahtaa on 7/31/2016.
 */
public class Moviefragment extends Fragment {



    private  GridView gridView;
    public String[] resultString;
    public ArrayList<Movie>MovarrayList;
    Movie m;
    int i=1;



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_main,container,false);
        gridView= (GridView) view.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie mov = (Movie) parent.getItemAtPosition(position);
                ((MainActivity) getActivity()).selectMovie(mov);
//                    Movie mov = (Movie) parent.getItemAtPosition(position);
//                    Intent intent =new Intent(getActivity(),DetailActivity.class);
//
//                    intent.putExtra("mov", mov);
//                    startActivity(intent);
            }
        });
        setHasOptionsMenu(true);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.action_fav){
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.main_actiivity_id, new FavoriteFragment()).addToBackStack(null)
                    .commit();

            return true;
        }else if(id==R.id.action_Rated){
            i=2;
            FetchMovieData fetchMovieData=new FetchMovieData();
            fetchMovieData.execute("top_rated.desc");
        }
        else if(id==R.id.action_popularity)
        {
            i=1;
            updateActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    public interface Callback{
        public void selectMovie(Movie mov);
    }


    @Override
    public void onStart() {
        super.onStart();

        updateActivity();
    }
    private void updateActivity() {
        if (i == 2) {
            FetchMovieData fetchMovieData = new FetchMovieData();
            fetchMovieData.execute("top_rated.desc");
        } else if (i == 1) {
            FetchMovieData fetchMovieData = new FetchMovieData();
            fetchMovieData.execute("popularity.desc");
        }
//
//        else if(i==android.R.id.home){
//            i=1;
//        }
    }

    public class FetchMovieData extends AsyncTask<String,Void,ArrayList<Movie>> {

        private final String LOG_TAG = FetchMovieData.class.getSimpleName();


        private ArrayList getMoviesFromJSON(String movieJsonString) throws JSONException {
            final String TMDB_List = "results";
            final String TMDB_poster = "poster_path";
            final String TMDB_overView = "overview";
            final String TMDB_Date = "release_date";
            final String TMDB_Title = "original_title";

            final String TMDB_Vote = "vote_average";
            final String TMDB_id = "id";

            if(movieJsonString!=null) {


                JSONObject MovieJSON = new JSONObject(movieJsonString);
                JSONArray MoviesArray = MovieJSON.getJSONArray(TMDB_List);

                MovarrayList = new ArrayList<>(MoviesArray.length());

                //resultString = new String[MoviesArray.length()];
                for (int i = 0; i < MoviesArray.length(); i++) {
                    JSONObject MovieIndex = MoviesArray.getJSONObject(i);
                    String poster = MovieIndex.getString(TMDB_poster);
                    String title = MovieIndex.getString(TMDB_Title);
                    String overView = MovieIndex.getString(TMDB_overView);
                    String data = MovieIndex.getString(TMDB_Date);
                    String vote = MovieIndex.getString(TMDB_Vote);
                    int id = MovieIndex.getInt(TMDB_id);

                    m = new Movie();

                    m.setPoster(poster);
                    m.setTitle(title);
                    m.setOverView(overView);
                    m.setDate(data);
                    m.setVote("[ "+vote+"/10 ]");
                    m.setId(id);
                    MovarrayList.add(m);
                }
            }

            return MovarrayList;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonString = null;

            try {


                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String SORT = "sort_by";
                final String KEY_PARAM = "api_key";


                Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(SORT, params[0]).appendQueryParameter(KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY).build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "/n");
                }
                if (stringBuffer.length() == 0) {
                    return null;
                }
                movieJsonString = stringBuffer.toString();
                Log.v(LOG_TAG, "Movies string" + movieJsonString);


            } catch (IOException e) {
                Log.e("MovieFragment", "error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("movieFragment", "error close stream", e);
                    }
                }
            }
            try {
                return getMoviesFromJSON(movieJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> strings) {
            super.onPostExecute(strings);
            if(strings!=null) {

                final ImageAdapter imageAdapter = new ImageAdapter(getActivity(), strings);
                gridView.setAdapter(imageAdapter);
            }

        }
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

            Picasso.with(getActivity())
                    .load(Base+s)
                    .placeholder(R.drawable.image)
                            //.noFade()
                    //.resize(300,250)
                            //.centerCrop()
                    .into(holder.imageView);

            return convertView;
        }
        public class Holder{
            ImageView imageView;
        }
    }
}
