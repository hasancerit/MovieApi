package com.example.movie;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends AppCompatActivity {
    ImageView poster;
    TextView overview,site,tarih,süre,puan,dil,bütce;
    String id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        context = this;
        id = getIntent().getExtras().getString("id");

        filmDetayGetir();

        poster = findViewById(R.id.poster);
        overview = findViewById(R.id.overview);
        site = findViewById(R.id.site);
        tarih = findViewById(R.id.tarih);
        süre = findViewById(R.id.süre);
        puan = findViewById(R.id.puan);
        dil = findViewById(R.id.dil);
        bütce = findViewById(R.id.butce);

    }

    private void filmDetayGetir(){
        String url = "https://api.themoviedb.org/3/movie/"+id+"?api_key=47cbbbccb755ef6db434c085338e951d&language=tr";

        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    overview.setText(object.getString("overview"));
                    site.setText(object.getString("homepage"));
                    tarih.setText(object.getString("release_date"));
                    süre.setText(object.getString("runtime"));
                    puan.setText(object.getString("vote_average"));
                    dil.setText(object.getString("original_language"));
                    bütce.setText(object.getString("budget"));

                    Picasso.with(context)
                            .load("http://image.tmdb.org/t/p/w185/"+ object.getString("poster_path"))
                            .into(poster);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(context).add(istek);
    }

}
