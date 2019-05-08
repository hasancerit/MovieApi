package com.example.movie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ItemFragment extends Fragment {
    //mFragment
    View v;
    private RecyclerView listView;
    public int id = 0;
    private String page = "1",totalPage = "0";
    private ArrayList<MainFilm> filmler;
    private MyItemRecyclerViewAdapter listAdapter;

    //FOOTER HEADER
    private View footer,header;
    private Button ileri,geri,git;
    private TextView textView;
    private EditText editText;


    public ItemFragment(){
        Log.d("HASAN CERİT","Item Fragment cons");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list,container,false);
        Log.d("HASAN CERİT","Item Fragment onCreate");

        this.v = v;

        filmler = new ArrayList<>();
        //HEADER FOOTER
        header = getLayoutInflater().inflate(R.layout.header,container,false);
        footer = getLayoutInflater().inflate(R.layout.footer,container,false);

        ileri = footer.findViewById(R.id.ileriButton);
        textView = footer.findViewById(R.id.sayfaText);
        editText = footer.findViewById(R.id.sayfaGiris);
        git = footer.findViewById(R.id.git);
        geri = footer.findViewById(R.id.geriButton);

        geri.setVisibility(View.INVISIBLE);

        headerAyarla(header);

        //OnClicks
        onClicks();
    return v;
    }

    private void onClicks(){
        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempPage = Integer.valueOf(page);
                tempPage++;
                page = "" + tempPage;
                filmleriGetir();
                textView.setText("/"+totalPage);
                editText.setText(page);

                GridLayoutManager layoutManager = (GridLayoutManager) listView
                        .getLayoutManager();
                layoutManager.scrollToPosition(0);

                sayfaKontrol();
            }
        });
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BUTON GERİ","tıklandı");
                int tempPage = Integer.valueOf(page);
                tempPage--;
                page = ""+tempPage;
                filmleriGetir();
                textView.setText("/"+totalPage);
                editText.setText(page);

                GridLayoutManager layoutManager = (GridLayoutManager) listView
                        .getLayoutManager();
                layoutManager.scrollToPosition(0);

                sayfaKontrol();
            }
        });

        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gidilecekPage = editText.getText().toString();
                if(Integer.parseInt(gidilecekPage)<=0 ||  Integer.parseInt(gidilecekPage)>Integer.parseInt(totalPage)){
                    Toast.makeText(getContext(),"Lütfen Geçerli Bir Sayfa Girin",Toast.LENGTH_LONG).show();
                }else{
                    page = gidilecekPage;
                    filmleriGetir();
                    textView.setText("/"+totalPage);
                    editText.setText(page);

                    sayfaKontrol();
                }
            }
        });

    }

    private void sayfaKontrol(){
        if(page.equals("1")){
            geri.setVisibility(View.GONE);
            ileri.setVisibility(View.VISIBLE);
        }
        else if(page.equals(totalPage)){
            ileri.setVisibility(View.GONE);
            geri.setVisibility(View.VISIBLE);
        }else{
            geri.setVisibility(View.VISIBLE);
            ileri.setVisibility(View.VISIBLE);
        }
    }

    private void filmleriGetir(){
        Log.d("HASAN CERİT","Item Fragment film Getir Çağrıldı");
        String url;
        switch (id){
            case 0 :
                url = "https://api.themoviedb.org/3/movie/now_playing?api_key=47cbbbccb755ef6db434c085338e951d&language=tr&page="+page;
                break;
            case 1 :
                url = "https://api.themoviedb.org/3/movie/popular?api_key=47cbbbccb755ef6db434c085338e951d&language=tr&page=="+page;
                break;
            case 2 :
                url = "https://api.themoviedb.org/3/movie/top_rated?api_key=47cbbbccb755ef6db434c085338e951d&language=tr&page="+page;
                break;
             default:
                 url = null;
        }

        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    filmler.clear();
                    listView.scrollTo(0,0);
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
                    }

                    totalPage = jsonObject.getString("total_pages");
                    page = jsonObject.getString("page");

                    textView.setText("/"+totalPage);
                    editText.setText(page);

                   // listAdapter.notifyDataSetChanged();
                    Log.d("HASAN CERİT","Item Fragment PAGER ADAPTER DATA HAS CHANGED FİLM GETİR");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(istek);
    }

    private void headerAyarla(View header){
        Log.d("HASAN CERİT","Item Fragment Header Ayarla Çağırıldı");
        final ArrayList<MainFilm> filmHeader = new ArrayList<>();
        final ViewPager viewPager = header.findViewById(R.id.viewPager);

        /*String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=47cbbbccb755ef6db434c085338e951d&language=tr&page=1";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {*/
                try {
                    filmHeader.clear();
                    JSONObject jsonObject = new JSONObject("{\"results\":[{\"vote_count\":673,\"id\":491418,\"video\":false,\"vote_average\":7.5,\"title\":\"Şipşak Aile\",\"popularity\":78.407,\"poster_path\":\"\\/dic3GdmMpxxfkCQfvZnasb5ZkSG.jpg\",\"original_language\":\"en\",\"original_title\":\"Instant Family\",\"genre_ids\":[35,18],\"backdrop_path\":\"\\/lwICpzZudw8BZ0bODaHgRWCdioB.jpg\",\"adult\":false,\"overview\":\"Pete ve Ellie birbirlerini seven, uyumlu bir çifttir. Tek istekleri ise çocuk sahibi olmaktır. Sevimli çift bir aile kurmaya karar verdiğinde koruyucu aile olarak evlat edinme yoluna başvurur. Amaçları küçük bir çocuğu evlat edinmektir ancak birbirinden tatlı üç kardeş görünce hepsini evlat edinmeye karar verirler. Kardeşlerin en büyüğü, asi bir genç kız olan 15 yaşındaki Lizzy’dir. Çift kendilerini bir gecede üç çocuklu bir aile olarak bulur. Pete ve Ellie, artık bir aile olma umuduyla, nasıl ebeveynlik yapılması gerektiğini öğrenmek zorundadır. Bu süreçte birbirlerinden farklı karakterlerde olan bu 5 kişi, birbirlerine alışmalı ve uyum sağlamayı öğrenmelidir. Bu süreçte aiile dinamikleri, evlat edinme sisteminin kağıt işleri, aksaklıklar, aile sahibi olmanın ne demek olduğunu unutan çocuklar ve hassas duygular da işin içine girerek her şeyi zorlaştıracaktır.\",\"release_date\":\"2018-11-16\"},{\"vote_count\":122,\"id\":457799,\"video\":false,\"vote_average\":6.9,\"title\":\"Extremely Wicked, Shockingly Evil and Vile\",\"popularity\":75.586,\"poster_path\":\"\\/zSuJ3r5zr5T26tTxyygHhgkUAIM.jpg\",\"original_language\":\"en\",\"original_title\":\"Extremely Wicked, Shockingly Evil and Vile\",\"genre_ids\":[53,80,18,36],\"backdrop_path\":\"\\/roAfKxzL6DQ35DlD4idFB5hjVuy.jpg\",\"adult\":false,\"overview\":\"Zac Efron'un tarihin en ünlü seri katili Ted Bundy'ye hayat verdiği film, Bundy'nin uzun dönemli kız arkadaşı Elizabeth (Lily Collins)'in gözünden anlatılacak. 1974 ve 1978 yılları arasında birçok kadına tecavüz edip öldüren, kurbanlarının tam sayısı bilinmemekle birlikte, 10 yılı aşkın inkar süreci sonunda, 30'dan fazla cinayet işlediğini itiraf eden, 1989'da da Florida'da idam edilen ünlü seri katil Ted Bundy'nin hayatında uzun süre yer alan Elizabeth, katilin aktif olduğu yıllarda da onun hayatındaydı ve manipülasyonlarla kontrol ediliyordu. Yıllarca süren şüphe ve inkarın ardından Elizabeth, sonunda Bundy'yi polise ihbar etmişti. Ancak polis, bu ihbarı değerlendirmemişti.\",\"release_date\":\"2019-05-02\"},{\"vote_count\":98,\"id\":376865,\"video\":false,\"vote_average\":5.9,\"title\":\"Riskli Hayat\",\"popularity\":74.218,\"poster_path\":\"\\/wElOvH7H6sLElsTOLu1MY6oWRUx.jpg\",\"original_language\":\"en\",\"original_title\":\"High Life\",\"genre_ids\":[878,18,9648,53,27],\"backdrop_path\":\"\\/9M18vLHaaS3SNdHYrIsIWIHCm1W.jpg\",\"adult\":false,\"overview\":\"Bir grup suçlu insan uzayda yer alacakları tehlikeli görevi kabul eder. Bu görev, insanların uzayda üremeleriyle ilgilidir. Ama uzay gemilerini bir kozmik fırtına alaşağı eder ve çoğu kişi bu fırtınadan kurtulamaz. Robert Pattinson'ın canlandırdığı Monte karakteri ve minik kızı Güneş Sistemi'ne ulaşmayla ilgili tehlikeli görevde hayatta kalan son insanlardır. Ölüm sırası gelen mahkumları belirleyen kişi ise Juliette Binoche'un hayat verdiği doktorun ta kendisidir. Uzay gemisinin başına gelenler birer sır olarak kalıyorken, Monte ve kızı büyük bir kara deliğe karşı hayatta kalma mücadelesi vermek zorundadırlar.\",\"release_date\":\"2018-11-07\"},{\"vote_count\":141,\"id\":532671,\"video\":false,\"vote_average\":6,\"title\":\"The Prodigy\",\"popularity\":72.93,\"poster_path\":\"\\/yyejodyk3lWncVjVhhrEkPctY9o.jpg\",\"original_language\":\"en\",\"original_title\":\"The Prodigy\",\"genre_ids\":[27,53],\"backdrop_path\":\"\\/oxTgovZ3TQ8LLxifUm16ROMULP5.jpg\",\"adult\":false,\"overview\":\"\",\"release_date\":\"2019-02-06\"},{\"vote_count\":1238,\"id\":458723,\"video\":false,\"vote_average\":7.2,\"title\":\"Biz\",\"popularity\":70.532,\"poster_path\":\"\\/ux2dU1jQ2ACIMShzB3yP93Udpzc.jpg\",\"original_language\":\"en\",\"original_title\":\"Us\",\"genre_ids\":[53,27],\"backdrop_path\":\"\\/jNUCddkM1fjYcFIcEwFjc7s2H4V.jpg\",\"adult\":false,\"overview\":\"Gabe ve Adelaide Wilson, çocukları Zora ve Jason ile birlikte deniz kenarındaki yazlıklarına tatile giderler. Niyetleri orada arkadaşları ile birlikte keyifli vakit geçirmektir. Ancak beklenmeyen misafirler yazlıklarına konuk olduklarında huzurlu hayatları cehenneme dönecektir.\",\"release_date\":\"2019-03-14\"},{\"vote_count\":441,\"id\":280217,\"video\":false,\"vote_average\":6.6,\"title\":\"Lego Filmi 2\",\"popularity\":56.768,\"poster_path\":\"\\/6hoh4aHcld8OEejwMhyRWOhYhDx.jpg\",\"original_language\":\"en\",\"original_title\":\"The Lego Movie 2: The Second Part\",\"genre_ids\":[28,12,16,35,10751,878,14],\"backdrop_path\":\"\\/8kPozGb4BDrcWBSsGPrkULG2tP9.jpg\",\"adult\":false,\"overview\":\"Her şeyin muhteşem olduğu günlerin üzerinden beş yıl geçmiştir ve şimdi şehir sakinleri yepyeni bir tehditle karşı karşıyadırlar. Uzaydan gelen LEGO DUPLO istilacıları her şeyi yeniden inşa edilebileceklerinden daha hızlı bir şekilde paramparça etmektedirler. İstilacıları yenme ve LEGO evrenine uyumu geri getirme sorumluluğu Emmet, Lucy, Batman ve dostlarını çok uzak, keşfedilmemiş dünyalara, hatta her şeyin müzikal olduğu tuhaf bir galaksiye bile götürecektir. Bu görev onların cesaretini, yaratıcılığını, Usta Yapıcılık becerilerini sınayacak ve gerçekten ne kadar özel olduklarını ortaya çıkaracaktır.\",\"release_date\":\"2019-01-26\"}],\"page\":1,\"total_results\":1080,\"dates\":{\"maximum\":\"2019-05-04\",\"minimum\":\"2019-03-17\"},\"total_pages\":54}");
                    JSONArray array = jsonObject.getJSONArray("results");

                    for (int i = 0 ; i < array.length() ; i++){
                        MainFilm film = new MainFilm();
                        JSONObject object = array.getJSONObject(i);
                        film.setId(object.getString("id"));
                        film.setAvarage(""+object.getString("vote_average"));
                        film.setImage("http://image.tmdb.org/t/p/w185/"+ object.getString("backdrop_path"));
                        film.setName(object.getString("title"));
                        film.setOverview(object.getString("overview"));
                        film.setTarih(object.getString("release_date"));
                        filmHeader.add(film);
                    }

                    final VizyonPager vizyonPagerAdapter = new VizyonPager(getFragmentManager(),filmHeader);
                    viewPager.setAdapter(vizyonPagerAdapter);
                    filmleriGetir();

                    //mFragment
                    listView = v.findViewById(R.id.list);

                    listAdapter = new MyItemRecyclerViewAdapter(filmler,getContext(),footer,header);
                    listView.setAdapter(listAdapter);
                    final GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),2);
                    linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                    linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int i) {
                            if(i == listAdapter.getItemCount()-1 || i == 0)
                                return 2;
                            return 1;

                        }
                    });
                    listView.setLayoutManager(linearLayoutManager);
                } catch (JSONException e) {
                    e.printStackTrace();
                }/*
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(istek);*/
    }
}
