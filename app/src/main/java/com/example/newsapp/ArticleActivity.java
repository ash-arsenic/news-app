package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity implements LoadDataFromApi.ApiListener {
    ArrayList<NewsModal> news;
    RecyclerView articleNews;
    ArticleAdapter adapter;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        LoadDataFromApi api = new LoadDataFromApi(this);
        api.load("https://newsapi.org/v2/top-headlines?country=in&apiKey=22087220960646158d9a0bc78c99334f", "home");
        articleNews = findViewById(R.id.article_news);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bo_article);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bo_home:
                        Intent intent = new Intent(ArticleActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.bo_article:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void send(String json, String type) {
        if (!type.equals("")) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    news = parseNews(json);
                    loadRecyclerView();
                }
            });
        }
    }
    void loadRecyclerView() {
        adapter = new ArticleAdapter(news);
        articleNews.setLayoutManager(new GridLayoutManager(this, 2));
        articleNews.setHasFixedSize(true);
        articleNews.setAdapter(adapter);
    }
    ArrayList<NewsModal> parseNews(String json) {
        ArrayList<NewsModal> news = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray articleArray = jsonObject.getJSONArray("articles");

            for(int i=0; i<articleArray.length(); i++) {
                JSONObject obj = new JSONObject(articleArray.get(i).toString());
                news.add(new NewsModal(obj.getString("title"), obj.getString("author"), obj.getJSONObject("source").getString("name"), obj.getString("description"), obj.getString("urlToImage"), obj.getString("url"), obj.getString("publishedAt")));
            }
        } catch(JSONException e) {
            Toast.makeText(ArticleActivity.this, "Some Error occurred", Toast.LENGTH_SHORT).show();
        }
        return news;
    }

    private class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
        ArrayList<NewsModal> news;

        public ArticleAdapter(ArrayList<NewsModal> news) {
            this.news = news;
        }

        @NonNull
        @Override
        public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.article_news_viewholder, parent, false);
            return new ArticleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
            NewsModal n = news.get(position);
            holder.title.setText(n.getTitle());
            holder.source.setText(n.getSource());
            holder.date.setText(n.getPublished());
            Picasso.get().load(n.getImage()).into(holder.img);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(n.getRedirect()));
                    startActivity(webIntent);
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, n.getTitle() + "\nRead more at: \n" + n.getRedirect());
                    Intent shareIntent = Intent.createChooser(share, "News App");
                    startActivity(shareIntent);
                }
            });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.news_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.not_interested:
                                    Toast.makeText(ArticleActivity.this, "We won't show you this again", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.report:
                                    Toast.makeText(ArticleActivity.this, "Reported", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return news.size();
        }
    }

    private class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private TextView source;
        private ImageView img;
        private ImageButton save;
        private ImageButton share;
        private ImageButton more;
        MaterialCardView cardView;
        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_news_title);
            date = itemView.findViewById(R.id.article_news_date);
            source = itemView.findViewById(R.id.article_news_source);
            img = itemView.findViewById(R.id.article_news_image);
            save = itemView.findViewById(R.id.article_news_save);
            share = itemView.findViewById(R.id.article_news_share);
            cardView = itemView.findViewById(R.id.article_card);
            more = itemView.findViewById(R.id.article_news_more);
        }
    }
}