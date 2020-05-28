package com.ebaba.shadivenues;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Details extends AppCompatActivity {
  public  ViewPager viewPager;

    MyCustomPagerAdapter myCustomPagerAdapter;
    public TextView vtitle,vlocation,vcap,vprice,vpurpose,phone,email,vrooms,fcap;
    public ChipGroup vamaties,vservices;
    MaterialButton map,quote;
    public WebView vabout;
    ImageButton youtube;
    ArrayList<String> gals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        vtitle=findViewById(R.id.venue_title);
        vlocation=findViewById(R.id.venue_location);
        vcap =findViewById(R.id.venue_cap);
        vprice=findViewById(R.id.venue_price);
        vamaties=findViewById(R.id.amenities);
        vservices=findViewById(R.id.services);
     vabout=findViewById(R.id.venue_about);
     vpurpose=findViewById(R.id.venue_purpose);
     phone=findViewById(R.id.phoneid);
     email=findViewById(R.id.emailid);
     map=findViewById(R.id.gmap);
     quote=findViewById(R.id.getQuote);
     fcap=findViewById(R.id.venue_fcap);
     vrooms=findViewById(R.id.venue_room);
     youtube=findViewById(R.id.youtube);

//        ScrollView linearLayout =  findViewById(R.id.linearLayout);
//
//        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
//
//        animationDrawable.setEnterFadeDuration(2500);
//        animationDrawable.setExitFadeDuration(5000);
//
//        animationDrawable.start();

        getData();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        MaterialButton mapbtn=findViewById(R.id.gmap);

        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locurl=view.getTag().toString();
                Uri mapUri = Uri.parse(locurl);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String surl= view.getTag().toString();
                Intent vintent=new Intent(Details.this,VideoActivity.class);
                vintent.putExtra("vurl", surl);
                startActivity(vintent);
            }
        });

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myint1=new Intent(Details.this,CheckAvail.class);
                startActivity(myint1);
            }
        });
    }


    //auto time

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            Details.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < gals.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }


    //get json data
    public void getData(){
        Intent myin=getIntent();
       String id= myin.getStringExtra("postid");
        final ProgressBar pbar=findViewById(R.id.progressbar);

            pbar.setVisibility(View.VISIBLE);
            String url = "http://www.e-baba.in/wp-json/venues/v1/venue-details?id="+id;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Log.d("LOG-Data", response.toString());
                                vtitle.setText(response.getString("title"));
                                setTitle(response.getString("title"));
                                vlocation.setText(response.getString("address"));
                                vcap.setText(response.getString("capacity")+" (sitting)");
                                fcap.setText(response.getString("capacity")+"( Floating)");
                                vrooms.setText(response.getString("rooms")+"( Rooms)");

                                vprice.setText(response.getString("pricing"));
                                JSONArray amenties = response.getJSONArray("amenities");
                                for (int x = 0; x < amenties.length(); x++) {
                                    Chip chip = new Chip(vamaties.getContext());
                                    chip.setText(amenties.getString(x));
                                    // chip.setBackground(R.color.colorPrimaryDark);
                                    vamaties.addView(chip);
                                }
                                JSONArray services = response.getJSONArray("services");

                                for (int x = 0; x < services.length(); x++) {
                                    Chip chip = new Chip(vservices.getContext());
                                    chip.setText(services.getString(x));
                                    vservices.addView(chip);
                                }

                                vabout.loadData(response.getString("about"), "text/html; charset=utf-8", "utf-8");
                                vpurpose.setText(response.getString("purpose"));
                                email.setText(response.getString("email"));
                                phone.setText(response.getString("phone"));
                                map.setTag(response.getString("map"));
                                quote.setTag(response.getString("email"));
                                String vurl=response.getString("youtube");
                                if(vurl.isEmpty()){

                                    youtube.setVisibility(View.GONE);
                                }else{
                                    youtube.setTag(vurl);
                                }

                                JSONArray gal = response.getJSONArray("gallery");
                                gals = new ArrayList<String>();
                                for (int i = 0; i < gal.length(); i++) {
                                    gals.add(gal.getString(i));
                                }
                                viewPager = (ViewPager) findViewById(R.id.imgSlider);

                                myCustomPagerAdapter = new MyCustomPagerAdapter(Details.this, gals);
                                viewPager.setAdapter(myCustomPagerAdapter);
                                TabLayout tabLayout = findViewById(R.id.idicator);
                                tabLayout.setupWithViewPager(viewPager, true);
                                pbar.setVisibility(View.INVISIBLE);
                            } catch (JSONException e) {
                                Log.d("JSONE", e.getMessage());
                                pbar.setVisibility(View.INVISIBLE);
                            }


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Log.d("Error", error.toString());
                            pbar.setVisibility(View.INVISIBLE);

                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(jsonObjectRequest);

    }
}
