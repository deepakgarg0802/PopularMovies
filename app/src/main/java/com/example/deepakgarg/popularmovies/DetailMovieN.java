package com.example.deepakgarg.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Deepak Garg on 23-06-2016.
 */
public class DetailMovieN extends AppCompatActivity {

    String id,name,synopsis,user_rating,release_date,link;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_n);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("title");
        synopsis=intent.getStringExtra("plot");
        release_date=intent.getStringExtra("date");
        user_rating=intent.getStringExtra("rate");
        link=intent.getStringExtra("imagelink");

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle bundle = new Bundle();
            bundle.putString("id", id );

            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container,fragment, "DFtag").commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.exit :
                Toast.makeText(getApplicationContext(),"Closing app..", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
