package com.example.deepakgarg.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ReviewActivity extends AppCompatActivity {

    String id,reviewJsonString;
    String author[],content[];
    int reviewCount;
    LinearLayout linearLayout;
    Fetchreview fetchreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        linearLayout=(LinearLayout)findViewById(R.id.reviewlayout);
        fetchreview= new Fetchreview();
        fetchreview.execute(id);
    }

    public class Fetchreview extends AsyncTask<String,Void,Integer>
    {
        private final String LOG_TAG = Fetchreview.class.getSimpleName();
        ProgressDialog pd;
        void getDatafromJson(String jsonString)throws JSONException
        {
            final String RESULTS_JSON="results";
            final String AUTHOR_JSON="author";
            final String CONTENT_JSON="content";
            if(jsonString==null)
            {
                Log.v("gettingdata","null data");
                return;
            }
            JSONObject full_data= new JSONObject(jsonString);
            JSONArray review_result= full_data.getJSONArray(RESULTS_JSON);
            reviewCount= review_result.length();
            author=new String[reviewCount];
            content=new String[reviewCount];

            for(int i=0;i<reviewCount; ++i)
            {
                JSONObject review_object= review_result.getJSONObject(i);
                author[i]= review_object.getString(AUTHOR_JSON);
                content[i]=review_object.getString(CONTENT_JSON);
                Log.d("msg",author[i]+content[i]);
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            reviewJsonString = null;
            //moviescount=0;
            try {

                final String MOVIEDETAIL_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0]+"/reviews" ;

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
                reviewJsonString = buffer.toString();
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
            Log.v("message",reviewJsonString);
            try {
                getDatafromJson(reviewJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return new Integer(reviewCount);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(ReviewActivity.this);
            pd.setMessage("Loading...");
            pd.show();
            /*try {
                pd.show();
            }
            catch (Exception e)
            {}*/
        }

        @Override
        protected void onPostExecute(Integer integer) {

            if(integer.intValue()==0)
            {
                Toast.makeText(getApplicationContext(),"Sorry, no reviews found for movie",Toast.LENGTH_SHORT).show();
                finish();
            }
            for(int i=0;i<integer.intValue();++i)
            {
                TextView textView= new TextView(getApplicationContext());
                textView.setText(content[i]);
                Log.d("msg",content[i]);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                textView.setTextSize(20);
                //textView.setGravity(Gravity.RIGHT);
                linearLayout.addView(textView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView textView2= new TextView(getApplicationContext());
                textView2.setText("-"+author[i]);
                textView2.setTextSize(15);
                textView2.setGravity(Gravity.RIGHT);
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(5,5,5,15);
                textView2.setTextColor(Color.WHITE);
                linearLayout.addView(textView2,layoutParams);

            }
            if(pd!=null)
            {
                pd.dismiss();
            }
        }
    }

}

