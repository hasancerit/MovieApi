package com.example.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class FragmentVizyon extends Fragment {
    ImageView ımageView;
    MainFilm film;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vizyon,container,false);
        Log.d("HASAN CERİT","Fragment Vizyon On Create");
        ımageView = v.findViewById(R.id.vizyonImage);


        String filmS = getArguments().getString("film");
        film = new Gson().fromJson(filmS, MainFilm.class);

        Picasso.with(getContext())
                .load(film.getImage())
                .into(ımageView);


        ımageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent = new Intent(getContext(),MovieDetail.class);
                ıntent.putExtra("id",film.getId());
                startActivity(ıntent);

            }
        });

        return v;

    }

    public static FragmentVizyon newInstance(MainFilm film){
        Log.d("HASAN CERİT","Fragment Vizyon new Instance");
        FragmentVizyon myFragment = new FragmentVizyon();

        Bundle args = new Bundle();
        args.putString("film", new Gson().toJson(film));
        myFragment.setArguments(args);

        return myFragment;
    }

}
