package com.example.deepakgarg.popularmovies;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.zip.Inflater;

/**
 * Created by Deepak Garg on 23-06-2016.
 */
public class DetailMovieFragment extends android.support.v4.app.Fragment {

    TextView title,plot,rating,releasedate;
    ImageView imageView;
    String id,name,synopsis,user_rating,release_date,link;
    Button fav_button,review_button;
    View rootView;
    String detailsJsonString,trailersJsonString;
    int trailerscount;
    FetchDetails fetchDetails;
    FetchTrailers fetchTrailers;
    String trailerkeys[];
    LinearLayout linearLayout;

    public DetailMovieFragment()
    {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView=  inflater.inflate(R.layout.fragment_detailmovie,container,false);
        title=(TextView)rootView.findViewById(R.id.textView_title);
        plot=(TextView)rootView.findViewById(R.id.plot);
        rating=(TextView)rootView.findViewById(R.id.rating);
        releasedate=(TextView)rootView.findViewById(R.id.date);
        fav_button=(Button)rootView.findViewById(R.id.fav_button);
        linearLayout=(LinearLayout)rootView.findViewById(R.id.mylayout);
        review_button=(Button)rootView.findViewById(R.id.review);
        Bundle bundle=getArguments();

        if(bundle==null)
        {
            Log.v("bugs","bundle null");
        }
        else {
            id = bundle.getString("id", "default");

            /************check if this movie is already in favourites**********/
            boolean marked = MyUtility.checkFav(getContext(), id);
            if (marked==false) {
                fav_button.setText("Mark as favourite");
            }
            else
            {
               fav_button.setText("Marked as favourite");
            }
            fetchDetails=new FetchDetails();
            fetchDetails.execute(id);
            fetchTrailers=new FetchTrailers();
            fetchTrailers.execute(id);
        }
        return rootView;
    }

    public class FetchDetails extends AsyncTask<String,Void,Void>
    {
        private final String LOG_TAG = FetchDetails.class.getSimpleName();
        public ProgressDialog pd;
        void getDatafromJson(String jsonString)throws JSONException
        {
            final String POSTER_PATH_JSON="poster_path";
            final String TITLE_JSON="title";
            final String OVERVIEW_JSON="overview";
            final String VOTE_AVG_JSON="vote_average";
            final String RELEASE_JSON="release_date";
            if(jsonString==null)
            {
                Log.v("gettingdata","null data");
                return;
            }
            JSONObject full_data= new JSONObject(jsonString);

            link=full_data.getString(POSTER_PATH_JSON);
            name=full_data.getString(TITLE_JSON);
            synopsis=full_data.getString(OVERVIEW_JSON);
            user_rating=full_data.getString(VOTE_AVG_JSON);
            release_date=full_data.getString(RELEASE_JSON);
        }


        @Override
        protected Void doInBackground(String... params) {


            if (params==null || params.length == 0 || params[0].equals("")) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            detailsJsonString = null;
            //moviescount=0;
            try {

                final String MOVIEDETAIL_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] ;

                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIEDETAIL_URL).buildUpon().appendQueryParameter(API_KEY, MainActivityFragment.TMDB_API_KEY).build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                detailsJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            Log.v("message",detailsJsonString);
            try {
                getDatafromJson(detailsJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }
        protected void onPreExecute() {
            super.onPreExecute();
            title.setText("");
            releasedate.setText("");
            plot.setText("");
            rating.setText("");
            pd=new ProgressDialog(getContext());
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            title.setText(name);
            releasedate.setText(release_date);
            plot.setText(synopsis);
            rating.setText(user_rating);

            imageView = (ImageView) rootView.findViewById(R.id.movieimage);

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + link)
                    .error(R.drawable.download)
                    .into(imageView);
            review_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(getContext(),ReviewActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            });
            fav_button.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    boolean marked = MyUtility.checkFav(getContext(), id);
                    if (marked==false) {
                        MyUtility.addFavoriteItem(getContext(), id);
                        Toast.makeText(getActivity(), "movie added to favourites", Toast.LENGTH_SHORT).show();
                        fav_button.setText("Marked as favourite");
                    }
                    else
                    {
                        MyUtility.removeFavoriteItem(getContext(), id);
                        Toast.makeText(getActivity(), "movie removed from favourites", Toast.LENGTH_SHORT).show();
                        fav_button.setText("Mark as favourite");
                    }
                }
            });
            if(pd!=null)
            {
                pd.dismiss();
            }
        }
    }

    /************** TO FETCH TRAILERS******************/
    public class FetchTrailers extends AsyncTask<String,Void,Integer>
    {
        private final String LOG_TAG = FetchTrailers.class.getSimpleName();

        void getDatafromJson(String jsonString)throws JSONException
        {
            final String RESULTS_JSON="results";
            final String KEY_JSON="key";
            if(jsonString==null)
            {
                Log.v("gettingdata","null data");
                return;
            }
            JSONObject full_data= new JSONObject(jsonString);
            JSONArray trailer_result= full_data.getJSONArray(RESULTS_JSON);
            trailerscount= trailer_result.length();
            trailerkeys=new String[trailerscount];
            for(int i=0;i<trailerscount; ++i)
            {
                JSONObject trailer_object= trailer_result.getJSONObject(i);
                trailerkeys[i]= trailer_object.getString(KEY_JSON);
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            if (params==null || params.length == 0 || params[0].equals("")) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            trailersJsonString = null;
            //moviescount=0;
            try {

                final String MOVIEDETAIL_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0]+"/videos" ;

                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIEDETAIL_URL).buildUpon().appendQueryParameter(API_KEY, MainActivityFragment.TMDB_API_KEY).build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                trailersJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            Log.v("message",trailersJsonString);
            try {
                getDatafromJson(trailersJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return new Integer(trailerscount);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"fetching trailer uri",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Integer integer) {

            if (integer!=null && integer.intValue()!=0)
            {
                for(int i=0;i<integer.intValue();++i)
                {
                    Button button= new Button(getContext());
                    button.setText("Trailer "+(i+1));
                    final String key=trailerkeys[i];
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v="+key));
                            startActivity(intent);*/

                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + key));
                                startActivity(intent);
                            }
                        }
                    });
                    linearLayout.addView(button,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
        }
    }

}
