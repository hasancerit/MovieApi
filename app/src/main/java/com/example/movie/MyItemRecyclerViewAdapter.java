package com.example.movie;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private View header;
    private Context context;
    private ArrayList<MainFilm> filmler;

    View footer;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    public MyItemRecyclerViewAdapter(ArrayList<MainFilm> filmler, Context context,View footer,View header) {
        this.context = context;
        this.filmler = filmler;
        this.footer = footer;
        this.header = header;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
            return new ViewHolder(view);
        }else if (viewType == TYPE_FOOTER){
            return new FooterHolder(footer);
        }
        else if(viewType == TYPE_HEADER){
            return new HeaderHolder(header);
        }else
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i != 0 && i!=getItemCount()-1 )
            ((ViewHolder) viewHolder).setData(filmler.get(i-1));
    }

    @Override
    public int getItemCount() {
        return filmler.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1)
            return TYPE_FOOTER;
        else if(position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView tarih, puan, isim;
        LinearLayout card;

        public ViewHolder(View view) {
            super(view);
            poster = view.findViewById(R.id.poster);
            tarih = view.findViewById(R.id.tarih);
            puan = view.findViewById(R.id.puan);
            isim = view.findViewById(R.id.isim);
            card = view.findViewById(R.id.card);
        }

        public void setData(final MainFilm film) {
            Picasso.with(context)
                    .load(film.getImage())
                    .into(poster);

            tarih.setText(film.getTarih());
            puan.setText(film.getAvarage());
            isim.setText(film.getName());


            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra("id", film.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {;
        public HeaderHolder(View header) {
            super(header);
        }
    }
}

