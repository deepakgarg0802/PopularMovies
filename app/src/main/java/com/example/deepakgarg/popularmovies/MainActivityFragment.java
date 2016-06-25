package com.example.deepakgarg.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    /********* ADD YOUR API KEY HERE*************/
    public static final String TMDB_API_KEY="7a664ba0a7b646035b306141da6eeeb4";
    /*************add api key above*********************/

    //String name []={"Refresh to see"};
    ArrayList<Thumnail>thumnails=new ArrayList<Thumnail>();

    String sortorder="popular";
    String nextcall="top_rated";
    int moviescount=0;
    //String movie_id[],image_link[];
    GridView gridView;
    gridviewadapter myAdapter;
    final String TOPRATED="Top Rated";
    final String POPULAR= "Popular";
    String moviesJsonStr;
    Fetchposters fetchposters;
    FetchFavourites fetchFavourites;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("mainactfrag","oncrt");
        fetchposters=new Fetchposters();
        fetchposters.execute(sortorder);
        //myAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment,menu);
        menu.findItem(R.id.sortby).setTitle("Sort by Top Rated");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.refresh:
                Toast.makeText(getActivity(),"Refreshing...", Toast.LENGTH_LONG).show();
                fetchposters=new Fetchposters();
                fetchposters.execute(sortorder);
                myAdapter.notifyDataSetInvalidated();
                return true;

            case R.id.sortby :
                String x= nextcall;
                nextcall=sortorder;
                sortorder=x;
                if(nextcall.equals("popular"))
                {
                    Toast.makeText(getActivity(),"Sorting by "+TOPRATED+"..", Toast.LENGTH_LONG).show();
                    String menutitle="Sort by "+POPULAR;
                    item.setTitle(menutitle);
                    getActivity().setTitle(TOPRATED);

                }
                else
                {
                    Toast.makeText(getActivity(),"Sorting by "+POPULAR+"..", Toast.LENGTH_LONG).show();
                    String menutitle="Sort by "+TOPRATED;
                    item.setTitle(menutitle);
                    getActivity().setTitle(POPULAR);

                }
                fetchposters=new Fetchposters();
                fetchposters.execute(sortorder);
                myAdapter.notifyDataSetInvalidated();
                return true;

            case R.id.favorites :
                Toast.makeText(getActivity(),"Showing favourites..", Toast.LENGTH_LONG).show();
                getActivity().setTitle("Favourites");
                fetchFavourites=new FetchFavourites();
                fetchFavourites.execute();
                myAdapter.notifyDataSetInvalidated();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        if(rootView==null)Log.d("mainactfr","infltr");

        gridView= (GridView)rootView.findViewById(R.id.gridView);
        if(gridView==null)Log.d("mainactfr","grid");

        myAdapter= new gridviewadapter(getActivity(),thumnails);
        if(myAdapter==null)Log.d("mainactfr","myadptr");

        gridView.setAdapter(myAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v("bugs","after onitemclk");
                if(thumnails.get(position).getImage_url()==null )   //to refresh when download image is clicked
                {
                    Toast.makeText(getActivity(),"Refreshing..", Toast.LENGTH_LONG).show();
                    fetchposters=new Fetchposters();
                    fetchposters.execute(sortorder);
                    myAdapter.notifyDataSetInvalidated();
                    Log.v("bugs","nulls");

                }
                else
                {
                    if(MainActivity.mTwoPane==false)
                    {
                        Intent intent = new Intent(getContext(), DetailMovieN.class);// changed to new detail movie code
                        Toast.makeText(getActivity(), "Opening " + thumnails.get(position).getName(), Toast.LENGTH_LONG).show();
                        intent.putExtra("id", thumnails.get(position).getMovie_id());
                        startActivity(intent);
                        Log.v("bugs","false twopane");

                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", thumnails.get(position).getMovie_id() );

                        Log.v("bugs",thumnails.get(position).getMovie_id());

                        DetailMovieFragment fragment = new DetailMovieFragment();
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.movie_detail_container,fragment, "DFtag").commit();
                    }
                }
            }
        });
        return rootView;
    }

    public class Fetchposters extends AsyncTask<String,Void,Integer>
    {
        private final String LOG_TAG = Fetchposters.class.getSimpleName();
        public ProgressDialog pd;
        void getDatafromJson(String jsonString)throws JSONException
        {
            final String RESULTS_JSON="results";
            final String ID_JSON="id";
            final String POSTER_PATH_JSON="poster_path";
            final String TITLE_JSON="title";
            if(jsonString==null)
            {
                Log.v("gettingdata","null data");
                return;
            }
            JSONObject full_data= new JSONObject(jsonString);
            JSONArray movie_result= full_data.getJSONArray(RESULTS_JSON);
            moviescount= movie_result.length();

            thumnails= new ArrayList<Thumnail>();

            for(int i=0;i<moviescount; ++i)
            {
                JSONObject movie_object= movie_result.getJSONObject(i);
                Thumnail tile = new Thumnail();
                tile.setMovie_id( movie_object.getString(ID_JSON));
                tile.setImage_url(movie_object.getString(POSTER_PATH_JSON));
                tile.setName(movie_object.getString(TITLE_JSON));

                thumnails.add(tile);
            }
        }
        @Override
        protected Integer doInBackground(String... params)
        {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            moviesJsonStr = null;
            moviescount=0;
            try {

                final String MOVIEDATABASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] ;

                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIEDATABASE_URL).buildUpon().appendQueryParameter(API_KEY, TMDB_API_KEY).build();

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
                moviesJsonStr = buffer.toString();
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
            Log.v("message",moviesJsonStr);
            try {
                getDatafromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return new Integer(moviescount);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(getContext());
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(integer);
            if(integer!=null)
            {
                myAdapter.updateAdapter(getContext(),thumnails);
                gridView.setAdapter(myAdapter);
                //myAdapter.notifyDataSetInvalidated();
                Log.d("mainact",thumnails.get(0).getName());
            }
            if(pd!=null)
            {
                pd.dismiss();
            }
        }
    }

    public class FetchFavourites extends AsyncTask<Void,Void,Integer>
    {
        private final String LOG_TAG = FetchFavourites.class.getSimpleName();
        public ProgressDialog pd;
        int i;
        void getDatafromJson(String jsonString)throws JSONException
        {
            final String RESULTS_JSON="results";
            final String ID_JSON="id";
            final String POSTER_PATH_JSON="poster_path";
            final String TITLE_JSON="title";
            if(jsonString==null)
            {
                Log.v("gettingdata","null data");
                return;
            }
            JSONObject full_data= new JSONObject(jsonString);
            /*movie_id[i]=full_data.getString(ID_JSON);
            image_link[i]=full_data.getString(POSTER_PATH_JSON);
            name[i]=full_data.getString(TITLE_JSON);*/
            Thumnail tile = new Thumnail();
            tile.setMovie_id( full_data.getString(ID_JSON));
            tile.setImage_url(full_data.getString(POSTER_PATH_JSON));
            tile.setName(full_data.getString(TITLE_JSON));

            thumnails.add(i,tile);
        }


        @Override
        protected Integer doInBackground(Void... params) {
            String fav_list[] = MyUtility.getFavoriteList(getContext());
            if (fav_list==null || moviescount==0 || fav_list[0].equals(""))
            {
                return new Integer(0);
            }
            moviescount = fav_list.length;
            /*movie_id=new String[moviescount];
            image_link=new String[moviescount];
            name= new String[moviescount];*/
            thumnails=new ArrayList<Thumnail>();
            for (i = 0; i < moviescount; ++i) {
                if (fav_list[i].equals(""))
                    return new Integer(0);

                Log.d("msg",fav_list[i]);
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                moviesJsonStr = null;
                try {

                    final String MOVIEDETAIL_URL =
                            "http://api.themoviedb.org/3/movie/" + fav_list[i];

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
                    moviesJsonStr = buffer.toString();
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
                Log.v("message", moviesJsonStr);
                try {
                    getDatafromJson(moviesJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            return new Integer(moviescount);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(getContext());
            pd.setMessage("Loading Favourites...");
            pd.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(integer);
            if(integer==null || integer.intValue()==0)
            {
                Toast.makeText(getContext(),"Sorry, no favourites assigned",Toast.LENGTH_SHORT).show();
                getActivity().setTitle("Movies");
                /*Intent intent=new Intent(getContext(),MainActivity.class);
                getActivity().finish();
                startActivity(intent);*/

            }
            if(integer!=null)
            {
                myAdapter.updateAdapter(getContext(),thumnails);
                gridView.setAdapter(myAdapter);
                //myAdapter.notifyDataSetInvalidated();
            }
            if(pd!=null)
            {
                pd.dismiss();
            }
        }
    }
}
