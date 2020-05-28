package com.ebaba.shadivenues.ui.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.ebaba.shadivenues.CustomRecyclerAdapter;
import com.ebaba.shadivenues.Details;
import com.ebaba.shadivenues.MyCustomPagerAdapter;
import com.ebaba.shadivenues.MySliderPagerAdapter;
import com.ebaba.shadivenues.MyTesimonialAdapter;
import com.ebaba.shadivenues.R;
import com.ebaba.shadivenues.ScrollingActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import  com.ebaba.shadivenues.Venues;
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    LinearLayoutManager HorizontalLayout;
    public  ViewPager viewPager,viewPager1;
   public   int currentPage = 0;
    public final long DELAY = 1000;//delay in milliseconds before auto sliding starts.
   public  final long PERIOD = 4000;
    RecyclerView listView;

    //the hero list where we will store all the hero objects after parsing json
    List<Venues> blogList;
    MySliderPagerAdapter myCustomPagerAdapter;
    MyTesimonialAdapter myTestimonailAdaptor;
   public ArrayList<Integer> gals;

    public static final String JSON_URL="http://www.e-baba.in/wp-json/venues/v1/list";



    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView.Adapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        gals = new ArrayList<Integer>();
        gals.add(R.drawable.img2);
        gals.add(R.drawable.img3);


        // top slider
        viewPager = (ViewPager) root.findViewById(R.id.imgSlider);

        myCustomPagerAdapter = new MySliderPagerAdapter(getActivity(), gals);
        viewPager.setAdapter(myCustomPagerAdapter);
        TabLayout tabLayout = root.findViewById(R.id.idicator);
        tabLayout.setupWithViewPager(viewPager, true);

        autoScroll();
// testimonial
        ArrayList<String> clients = new ArrayList<String>();
        ArrayList<String> ccomments = new ArrayList<String>();
        clients.add("Ravi");
        clients.add("Prakash");
        clients.add("Neeta");

        ccomments.add("Nice Place it was");
        ccomments.add("very affordable pricing");
        ccomments.add("Nice location and decoration");
        viewPager1 = (ViewPager) root.findViewById(R.id.imgSlider2);

        myTestimonailAdaptor = new MyTesimonialAdapter(getActivity(), clients,ccomments);
        viewPager1.setAdapter(myTestimonailAdaptor);
        TabLayout tabLayout1 = root.findViewById(R.id.idicator2);
        tabLayout1.setupWithViewPager(viewPager1, true);


        //list
        listView = root.findViewById(R.id.listview);
        blogList = new ArrayList<Venues>();

        adapter = new CustomRecyclerAdapter(getActivity().getApplicationContext(), blogList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.HORIZONTAL,false);


       listView.setHasFixedSize(true);
        listView.setLayoutManager(mLayoutManager);
        listView.addItemDecoration(new DividerItemDecoration(getActivity(),
                        DividerItemDecoration.VERTICAL));
        listView.setAdapter(adapter);
        final ProgressBar progressBar = (ProgressBar) root.findViewById(R.id.nprogbar);

        //this method will fetch and parse the data
        loadHeroList(progressBar);
       ImageButton seeAll=root.findViewById(R.id.seeall);
       seeAll.setOnClickListener(new View.OnClickListener() {
           @Override           public void onClick(View view) {
               Intent seeallint=new Intent(getActivity(), ScrollingActivity.class);
               startActivity(seeallint);
           }
       });



        return root;
    }


    //auto time

    private void autoScroll(){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == gals.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer timer = new Timer(); // creating a new thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 4000);
    }



    private  void loadHeroList(final ProgressBar progressBar){


        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                JSON_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        Log.d("JSON",response.toString());
                        //mTextView.setText(response.toString());
progressBar.setVisibility(View.INVISIBLE);
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = response.getJSONObject(i);
                                // Log.d("JSONob", heroObject.toString());

                                //creating a hero object and giving them the values from json object
                                Venues hero = new Venues(heroObject.getString("title"),
                                        heroObject.optString("image"),heroObject.getString("id"),heroObject.getString("address"),heroObject.getString("capacity"));

                                //adding the hero to herolist
                                blogList.add(hero);
                            }

                            //creating custom adapter object

                            adapter.notifyDataSetChanged();
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.e("ERROR",e.getMessage());
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("Error",error.toString());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
        )
        {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };



        //creating a request queue


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(jsonArrayRequest);

    }


}
