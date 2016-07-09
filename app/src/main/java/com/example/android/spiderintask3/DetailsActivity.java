package com.example.android.spiderintask3;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    String[] allDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        allDetails = intent.getStringArrayExtra("allMovieDetails");

        Log.i("Details Activity",allDetails[0]);

        TextView textView = (TextView) findViewById(R.id.movie_title);
        textView.setText(allDetails[0]);

        textView = (TextView) findViewById(R.id.movie_year);
        textView.setText(allDetails[1]);

        textView = (TextView) findViewById(R.id.movie_genre);
        textView.setText(allDetails[2]);

        textView = (TextView) findViewById(R.id.movie_rating);
        textView.setText("IMDB Rating: " + allDetails[3]);

        textView = (TextView) findViewById(R.id.movie_plot);
        textView.setText(allDetails[5]);

        ImageView imageView = (ImageView) findViewById(R.id.movie_image);
        if(allDetails[4].equals("N/A"))
            Picasso.with(this).load(R.drawable.poster_unavailable).into(imageView);
        else
            Picasso.with(this).load(allDetails[4]).into(imageView);

        int flag = intent.getIntExtra("fromFavourites",1);
        if(flag==1)
            changeButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_favourites){
            startActivity(new Intent(this, FavouritesActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void insert(View view){
        ContentValues values = new ContentValues();

        values.put("imdb_id", allDetails[7]);
        values.put("title" , allDetails[0]);
        values.put("year" , allDetails[1]);
        values.put("genre" , allDetails[2]);
        values.put("rating" , allDetails[3]);
        values.put("poster_path" , allDetails[4]);
        values.put("plot" , allDetails[5]);
        values.put("type" , allDetails[6]);

        long insertedRowId = new MovieDbUtilities().insert(this, MovieDbUtilities.TABLE_NAME, values);

        if(insertedRowId>-1||insertedRowId==-3){
            changeButton();
        }
    }

    public void changeButton(){
        Button button = (Button) findViewById(R.id.add_to_fav);
        button.setVisibility(View.GONE);
    }

}
