package cl.jsalgado.smartboxtest;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import cl.jsalgado.smartboxtest.adapter.EventAdapter;
import cl.jsalgado.smartboxtest.util.Util;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager lManager;
    ProgressBar pbLoad;
    String accessToken;
    String expires;
    SwipeRefreshLayout swipeRefresh;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLayout = findViewById(android.R.id.content);
        accessToken = getIntent().getExtras().getString("token");
        expires = getIntent().getExtras().getString("expires");
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary
        );

        recyclerLoad();
    }

    private void recyclerLoad() {
        recycler = (RecyclerView) findViewById(R.id.rv_events);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lManager = new LinearLayoutManager(this);
        adapter = new EventAdapter(this, accessToken, recycler, pbLoad, swipeRefresh);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }

    /**
     * Método para refrescar la lista con swipeRefresh
     */
    @Override
    public void onRefresh() {
        if(Util.isConnected(this)){
            ((EventAdapter) adapter).loadEvents(true);
        }else {
            swipeRefresh.setRefreshing(false);
            Snackbar.make(parentLayout,
                    getResources().getString(R.string.refresh_msg), Snackbar.LENGTH_LONG).show();
        }
    }

}