package com.ebaba.shadivenues;

import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {
    RecyclerView listView;

    //the hero list where we will store all the hero objects after parsing json
    List<Venues> blogList;


    public static final String JSON_URL="http://www.e-baba.in/wp-json/venues/v1/list";



    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("All Venues");
        //list
        listView = findViewById(R.id.listview1);
        blogList = new ArrayList<Venues>();

        adapter = new CustomRecyclerAdapter1(getApplicationContext(), blogList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1,GridLayoutManager.VERTICAL,false);


        listView.setHasFixedSize(true);
        listView.setLayoutManager(mLayoutManager);
        listView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        listView.setAdapter(adapter);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.nprogbar);

        //this method will fetch and parse the data
        loadHeroList(progressBar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        );



        //creating a request queue


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(jsonArrayRequest);

    }
}
