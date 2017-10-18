package cl.jsalgado.smartboxtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import cl.jsalgado.smartboxtest.R;
import cl.jsalgado.smartboxtest.data.Data;
import cl.jsalgado.smartboxtest.data.Event;
import cl.jsalgado.smartboxtest.data.Item;
import cl.jsalgado.smartboxtest.util.Util;

/**
 * Created by joels on 17-10-2017.
 *
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Event event;
    private Context context;
    private String accessToken;
    private RecyclerView recycler;
    private ProgressBar pbLoad;
    private int page = 1;
    private Boolean isLoading;
    private SwipeRefreshLayout refreshLayout;

    public EventAdapter(Context context, String accessToken, RecyclerView recycler, ProgressBar pbLoad, SwipeRefreshLayout refreshLayout) {
        this.context = context;
        this.accessToken = accessToken;
        this.recycler = recycler;
        this.pbLoad = pbLoad;
        this.refreshLayout = refreshLayout;
        event = new Event();
        isLoading = false;
        loadEvents(false);
    }

    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapter.EventViewHolder holder, int position) {
        final Item item = event.getData().getItems().get(position);

        // Diferenciar las celdas pares de las impares
        if(position % 2 == 0){
            holder.llTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }else {
            holder.llTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        holder.tvTitle.setText(item.getLocation().getOriginal() + " - " + item.getMatchDay().getPhase().getOriginal());
        holder.tvScore.setText(item.getHomeScore() + " - " + item.getAwayScore());
        holder.tvDate.setText(Util.getDate(item.getMatchDay().getStart()));
        holder.tvStatus.setText(item.getEventStatus().getName().getOriginal());
        holder.tvAwayTeam.setText(item.getAwayTeam().getName());
        holder.tvHomeTeam.setText(item.getHomeTeam().getName());
        holder.ivHome.setImageDrawable(ContextCompat.getDrawable(context, Util.getDrawable(
                item.getHomeTeam().getShortName(), context)));
        holder.ivAway.setImageDrawable(ContextCompat.getDrawable(context, Util.getDrawable(
                item.getAwayTeam().getShortName(), context)));
        holder.tvGroup.setText(item.getMatchDay().getName().getOriginal());

        holder.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateTime = new DateTime(item.getMatchDay().getStart());
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", dateTime.getMillis());
                intent.putExtra("title", item.getHomeTeam().getName() + " vs " + item.getAwayTeam().getName());
                context.startActivity(intent);
            }
        });

        if((holder.getAdapterPosition() + 1) == event.getData().getItems().size() && !isLoading){
            if(Util.isConnected(context)){
                page++;
                isLoading = true;
                loadEvents(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        try {
            return  event.getData().getItems().size();
        }catch (Exception ex){
            Log.e("Exeption", ex.getMessage());
            return  0;
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llTitle;
        TextView tvTitle;
        TextView tvScore;
        TextView tvDate;
        TextView tvStatus;
        TextView tvAwayTeam;
        TextView tvHomeTeam;
        TextView tvGroup;
        ImageView ivHome;
        ImageView ivAway;
        ImageView ivCalendar;

        EventViewHolder(View itemView) {
            super(itemView);
            llTitle = (LinearLayout) itemView.findViewById(R.id.ll_title);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvAwayTeam = (TextView) itemView.findViewById(R.id.tv_away_team);
            tvHomeTeam = (TextView) itemView.findViewById(R.id.tv_home_team);
            tvGroup = (TextView) itemView.findViewById(R.id.tv_group);
            ivHome = (ImageView) itemView.findViewById(R.id.iv_home);
            ivAway = (ImageView) itemView.findViewById(R.id.iv_away);
            ivCalendar = (ImageView) itemView.findViewById(R.id.iv_calendar);
        }
    }

    /**
     * Método para obtener la lista de partidos de fútbol
     * @param isRefresh si es "true" significa que se cargará la lista desde cero
     */
    public void loadEvents(Boolean isRefresh){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Util.URL2 + "?page=" + page;
        if(isRefresh){
            page = 1;
            event = new Event();
            notifyDataSetChanged();
        }else {
            pbLoad.setVisibility(View.VISIBLE);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(page == 1){
                            event = new Gson().fromJson(response, Event.class);
                            isLoading = false;
                        }else {
                            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
                            Data data = new Gson().fromJson(jsonObject.get("data") , Data.class);
                            if(data.getItems() != null){
                                event.getData().getItems().addAll(data.getItems());
                                isLoading = false;
                            }else {
                                isLoading = true;
                            }

                        }
                        refreshLayout.setRefreshing(false);
                        pbLoad.setVisibility(View.GONE);
                        recycler.setVisibility(View.VISIBLE);

                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "";
                if (error instanceof NetworkError) {
                    message = context.getString(R.string.network_error_msg);
                } else if (error instanceof ServerError) {
                    message = context.getString(R.string.server_error_msg);
                } else if (error instanceof ParseError) {
                    message = context.getString(R.string.parse_error_msg);
                } else if (error instanceof TimeoutError) {
                    message = context.getString(R.string.time_out_error_msg);
                }
                Log.e("Error: ", message);
                refreshLayout.setRefreshing(false);
                pbLoad.setVisibility(View.GONE);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        queue.add(stringRequest);

    }

}