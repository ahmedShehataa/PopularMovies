package com.example.sheahtaa.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheahtaa on 9/10/2016.
 */
public  class DetailFragment extends android.support.v4.app.Fragment {

    ListView trailerList;
    int id;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Trailer> MovarrayList;
    ArrayList<Review>MovarrayListRev;
    View view;
    Trailer t;
    ToggleButton Fav;
    Movie mo;
    Movie extraBundel;
    private TextView tit,overv,dat;
    private TextView vot;
    private TextView trailer   ;
    private ImageView post;
    File file;


    public DetailFragment() {
    }


    public static DetailFragment getInstance(Movie m  ){
        DetailFragment fragment=new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("mov",m);
        fragment.setArguments(bundle);

        return fragment ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);

         tit= (TextView) view.findViewById(R.id.title);
         overv= (TextView)view.findViewById(R.id.overView);
         dat= (TextView) view.findViewById(R.id.date);
         vot= (TextView) view.findViewById(R.id.vote);
         trailer= (TextView) view.findViewById(R.id.trailerText);
         post= (ImageView)view.findViewById(R.id.poster);
        Fav = (ToggleButton) view.findViewById(R.id.favButton);

        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mo = getArguments().getParcelable("mov");
        Log.i("isnull",getArguments().getParcelable("mov")==null?"y":"n");
        String overView=mo.getOverView();
        String title=mo.getTitle();
        String date=mo.getDate();
        String vote=mo.getVote();
        String poster=mo.getPoster();
        id=mo.getId();


        tit.setText(title);


        overv.setText(overView);
        dat.setText(date);

        vot.setText(vote);



        //trailerList.addHeaderView(trailer);

        String Base="http://image.tmdb.org/t/p/w185/";
        String posterURL=Base+poster;


         file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .getAbsolutePath() + "/popularMoviesImage" +
                poster);
        Log.i("ahq", String.valueOf(file));





        if (!MainActivity.flag) {
            Picasso.with(getActivity())
                    .load(posterURL)
                    .placeholder(R.drawable.image)
                    .into(post);
        }else {
            Picasso.with(getActivity())
                    .load(file)
                    .placeholder(R.drawable.image)
                    .into(post);

        }


        String [] titlez = {"col_title"};
        String selction = "col_title"+"=?";
        String [] args = {mo.getTitle()};
        Cursor c = getActivity().getContentResolver().query(MovieTable.CONTENT_URI, titlez, selction, args , null);



        if (c.moveToFirst()){

            Fav.setChecked(true);
        }

        Fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().getContentResolver().insert(MovieTable.CONTENT_URI, MovieTable.getContentValues(mo, false));

                   PosterImage posterImage = new PosterImage(mo.getPoster());
                    Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/"+mo.getPoster()).into(posterImage.traget);


                    Toast.makeText(getActivity(), "movie add to ur Favorite", Toast.LENGTH_SHORT).show();
                } else if (!isChecked) {
                    String selction = "col_title" + "=?";
                    String[] args = {mo.getTitle()};
                    getActivity().getContentResolver().delete(MovieTable.CONTENT_URI, selction, args);
                    Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        excute();

    }
    void excute(){
        FetchMovieTraData fetchMovieTraData=new FetchMovieTraData();
        FetchMovieReview fetchMovieReview=new FetchMovieReview();
        fetchMovieTraData.execute();
        fetchMovieReview.execute();
    }

    public class FetchMovieTraData extends AsyncTask<String,Void,ArrayList<Trailer>> {

        private final String LOG_TAG = FetchMovieTraData.class.getSimpleName();


        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonString = null;

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/movie/"+id+"/videos?api_key=5f9fb4330e9e865bd5956c52057939a3";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();
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
                return getMovieTrailerFromJSON(movieJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        List<String> list=new ArrayList<>();

        @Override
        protected void onPostExecute(final ArrayList<Trailer> arrayList) {
            super.onPostExecute(arrayList);
            trailerList= (ListView) view.findViewById(R.id.listView);

            if(arrayList!=null) {
                String tr="Trailers";
                trailer.setText(tr);

                for (int i = 0; i < arrayList.size(); i++) {
        list.add(arrayList.get(i).getTrailerName());
        Log.i("Check", String.valueOf(list));
    }
    arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_adapter, R.id.list_item_textView, list);
    trailerList.setAdapter(arrayAdapter);
    ListUtil.setListViewHeightBasedOnItems(trailerList);
    trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intentPlay = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(position).getTrailrtKey()));
            startActivity(intentPlay);
        }
    });

            }

        }


        //String trailer;
        private ArrayList getMovieTrailerFromJSON(String movieJsonString) throws JSONException {
            final String TMDB_List = "results";
            final String TMDB_trailer="key";
            final String TMDB_trailer_name="name";
            if(movieJsonString!=null) {

                JSONObject MovieJSON = new JSONObject(movieJsonString);
                JSONArray MoviesArray = MovieJSON.getJSONArray(TMDB_List);
                MovarrayList = new ArrayList<>(MoviesArray.length());

                for (int i = 0; i < MoviesArray.length(); i++) {
                    JSONObject Index = MoviesArray.getJSONObject(i);
                    String key = Index.getString(TMDB_trailer);
                    String name = Index.getString(TMDB_trailer_name);
                    //trailer="https://www.youtube.com/watch?v="+key;
                    t = new Trailer();
                    t.setTrailrtKey(key);
                    t.setTrailerName(name);
                    MovarrayList.add(t);

                }
            }
            return MovarrayList;
        }
    }
    public class FetchMovieReview extends AsyncTask<String,Void,ArrayList<Review>> {


        private final String LOG_TAG = FetchMovieReview.class.getSimpleName();



        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonString = null;

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=5f9fb4330e9e865bd5956c52057939a3";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();
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
                return getMovieReviewFromJSON(movieJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }


        TextView review;
        TextView authorA;
        String author;
        String cont;
        @Override
        protected void onPostExecute(ArrayList<Review> arrayList) {
            super.onPostExecute(arrayList);
            if(arrayList!=null){
            review = (TextView) view.findViewById(R.id.review);
            authorA = (TextView) view.findViewById(R.id.author);

            for (int i = 0; i < arrayList.size(); i++) {
                author = arrayList.get(i).getAuthor();
                cont = arrayList.get(i).getReview();
            }
            review.setText(cont);
                if(author!=null) {
                    authorA.setText("A movie review by " + author);
                }
            Log.i("Check", String.valueOf(arrayList));
        }

        }
        Review r;


        private ArrayList<Review> getMovieReviewFromJSON(String movieJsonString) throws JSONException {
            final String TMDB_List = "results";
            final String TMDB_author = "author";
            final String TMDB_Review = "content";

            if(movieJsonString!=null){
            JSONObject MovieJSON = new JSONObject(movieJsonString);
            JSONArray MoviesArray = MovieJSON.getJSONArray(TMDB_List);
            MovarrayListRev = new ArrayList<>(MoviesArray.length());


            for (int i = 0; i < MoviesArray.length(); i++) {
                JSONObject Index = MoviesArray.getJSONObject(i);
                String author = Index.getString(TMDB_author);
                String key = Index.getString(TMDB_Review);
                r = new Review();
                r.setAuthor(author);
                r.setReview(key);
                MovarrayListRev.add(r);
            }
            Log.i("Check", String.valueOf(MovarrayListRev));
        }
            return MovarrayListRev;
        }
    }

}


