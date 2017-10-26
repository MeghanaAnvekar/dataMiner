package com.example.meghana.data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String url = "https://www.google.co.in/search?q=things+to+do+in+london";
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
        getWebsite();
    }

    //div [class='rc'] h3 [class='r'] a [href='https://www.tripadvisor.in/Attractions']
    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect(url).get();
                    String title = doc.title();
                    String site_url =doc.select("a[href^=https://www.tripadvisor.in/Attractions] ").attr("href");

                    builder.append(title).append("\n");


                   // builder.append("\n").append("Link : ").append(link.attr("href"));

                    Document main_site = Jsoup.connect(site_url).get();
                    Elements links = main_site.select("div.attraction_clarity_cell");

                    for( Element link  : links)
                    {
                        String listing_title = link.attr("div.listing_title > a[href^=Attraction_Review]");
                        int start = listing_title.indexOf('>');
                        int end = listing_title.indexOf('<');
                        String place = listing_title.substring(start+1,end);
                        Elements img =  main_site.select("div.photo_booking non_generic > img");
                        builder.append("\n----------------------------").append("\n"+place+"\n").append(img.attr("src")).append("\n-----------------------------------\n");
                        break;
                    }



                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
    /*
    private class ScrapeData extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {
                // Connect to the web site
                Document googlePage = Jsoup.connect(url).get();
                // Using Elements to get the class data
                Elements part = googlePage.select("h3 [class='r'] a [href='https://www.tripadvisor.in/Attractions']");
                // Locate the src attribute
                String siteUrl = part.attr("href");
                // Download image from URL
                Document document = Jsoup.connect(url).get();

                String listings = document.attr("div [class='poi']");


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }*/

}
