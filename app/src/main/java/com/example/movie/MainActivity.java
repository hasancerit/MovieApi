package com.example.movie;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    private String totalPage="1",page = "1";
    MyItemRecyclerViewAdapter listAdapter;
    ArrayList<MainFilm> filmler;
    View footer;
    TextView textView;
    EditText editText;
    Button ileri,geri,git;
    String aranacak = "";
    Context context = this;
    View fr0,fr1,fr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        footer = getLayoutInflater().inflate(R.layout.footer,null);

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab);
        recyclerView = findViewById(R.id.araList);
        ileri = footer.findViewById(R.id.ileriButton);
        geri = footer.findViewById(R.id.geriButton);
        git = footer.findViewById(R.id.git);


        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);

        textView = footer.findViewById(R.id.sayfaText);
        editText = footer.findViewById(R.id.sayfaGiris);
        filmler = new ArrayList<>();
        listAdapter = new MyItemRecyclerViewAdapter(filmler,this,footer,null);
        recyclerView.setAdapter(listAdapter);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if(i == listAdapter.getItemCount()-1)
                    return 2;
                return 1;

            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);


        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BUTON İLERİ", "tıklandı");
                int tempPage = Integer.valueOf(page);
                tempPage++;
                page = "" + tempPage;
                filmGetir(aranacak);
                textView.setText("/"+totalPage);
                editText.setText(page);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView
                        .getLayoutManager();
                layoutManager.scrollToPosition(0);

                if(page == totalPage){
                    ileri.setVisibility(View.INVISIBLE);
                }
                if(Integer.parseInt(page)>1){
                    geri.setVisibility(View.VISIBLE);
                }
            }
        });
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempPage = Integer.valueOf(page);
                tempPage--;
                page = ""+tempPage;
                filmGetir(aranacak);
                textView.setText("/"+totalPage);
                editText.setText(page);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView
                        .getLayoutManager();
                layoutManager.scrollToPosition(0);

                if(page.equals("1")){
                    geri.setVisibility(View.GONE);
                }
                if(Integer.parseInt(page)<Integer.parseInt(totalPage)){
                    ileri.setVisibility(View.INVISIBLE);
                }
            }
        });

        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gidilecekPage = editText.getText().toString();
                if(Integer.parseInt(gidilecekPage)<=0 ||  Integer.parseInt(gidilecekPage)>Integer.parseInt(totalPage)){
                    Toast.makeText(context,"Lütfen Geçerli Bir Sayfa Girin",Toast.LENGTH_LONG).show();
                }else{
                    page = gidilecekPage;
                    filmGetir(aranacak);
                    textView.setText("/"+totalPage);
                    editText.setText(page);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        final MenuItem araItem = menu.findItem(R.id.ara);
        SearchView searchView = (SearchView) araItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                tabLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                page = "1";
                aranacak = s.replace(" ","%20");
                filmGetir(aranacak);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void filmGetir(String isim){

        String url = "https://api.themoviedb.org/3/search/movie?api_key=47cbbbccb755ef6db434c085338e951d&" +
                "language=tr&query="+isim+"&page="+page+"&include_adult=false";

        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    filmler.clear();
                    recyclerView.scrollTo(0,0);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");

                    for (int i = 0 ; i < array.length() ; i++){
                        MainFilm film = new MainFilm();
                        JSONObject object = array.getJSONObject(i);
                        film.setId(object.getString("id"));
                        film.setAvarage(""+object.getString("vote_average"));
                        film.setImage("http://image.tmdb.org/t/p/w185/"+ object.getString("poster_path"));
                        film.setName(object.getString("title"));
                        film.setOverview(object.getString("overview"));
                        film.setTarih(object.getString("release_date"));
                        filmler.add(film);
                        Log.d("FİLM",film.getName());
                    }

                    totalPage = jsonObject.getString("total_pages");
                    page = jsonObject.getString("page");
                    listAdapter.notifyDataSetChanged();

                    textView.setText("/"+totalPage);
                    editText.setText(page);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(istek);
    }

}
