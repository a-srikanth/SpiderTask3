package com.example.android.spiderintask3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class FavouriteMovies extends Fragment {

    private GridView mGridView;
    private ArrayList<GridItem> mGridData;
    private FavouritesGridAdapter mGridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite_movies, container, false);

        mGridView = (GridView)rootView.findViewById(R.id.favourite_movies_grid);

        mGridData = new ArrayList<>();
        mGridAdapter = new FavouritesGridAdapter(getActivity(),getContext(),R.layout.favourites_grid_item, mGridData);
        mGridAdapter.setGridData(mGridData);

        Cursor c = new MovieDbUtilities().query(getContext(),MovieDbUtilities.TABLE_NAME, new String[]{MovieDbUtilities.COLUMN_POSTER_PATH,
                        MovieDbUtilities.COLUMN_TITLE, MovieDbUtilities.COLUMN_GENRE} ,MovieDbUtilities.COLUMN_TYPE+"=?",
                new String[]{"movie"},null);
        if(c!=null){
            if(c.moveToFirst()){
                while(c.moveToNext()){
                    GridItem item = new GridItem();
                    String path = c.getString(c.getColumnIndex(MovieDbUtilities.COLUMN_POSTER_PATH));
                    item.setImagePath(path);

                    String title = c.getString(c.getColumnIndex(MovieDbUtilities.COLUMN_TITLE));
                    item.setMovieTitle(title);

                    String genre = c.getString(c.getColumnIndex(MovieDbUtilities.COLUMN_GENRE));
                    item.setMovieGenre(genre);

                    mGridData.add(item);
                }
            }
        }

        mGridAdapter.setGridData(mGridData);
        mGridView.setAdapter(mGridAdapter);
        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                String title = item.getMovieTitle();

                Cursor c = new MovieDbUtilities().query(getContext(), MovieDbUtilities.TABLE_NAME, null,
                        MovieDbUtilities.COLUMN_TITLE+"=?",new String[]{title},null);
                if(c.moveToFirst()){
                    String genre = item.getMovieGenre();
                    String year = c.getString(c.getColumnIndex(MovieDbUtilities.COLUMN_YEAR));
                    String plot = c.getString(c.getColumnIndex(MovieDbUtilities.COLUMN_PLOT));
                    String rating = c.getString(c.getColumnIndex(MovieDbUtilities.COLUMN_RATING));
                    String poster = c.getString(c.getColumnIndex(MovieDbUtilities.COLUMN_POSTER_PATH));

                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    String[] allDetails = {title,year,genre,rating,poster,plot};
                    intent.putExtra("allMovieDetails",allDetails);
                    intent.putExtra("fromFavourites",1);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }
}
