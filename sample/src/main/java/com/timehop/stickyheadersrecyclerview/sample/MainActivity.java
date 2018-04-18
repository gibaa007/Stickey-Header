package com.timehop.stickyheadersrecyclerview.sample;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> abcd = new ArrayList();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        // Set adapter populated with example dummy data
        final AnimalsHeadersAdapter adapter = new AnimalsHeadersAdapter();
        adapter.add("Contacts");
        adapter.addAll(getDummyDataSet());
        recyclerView.setAdapter(adapter);

        // Set layout manager
        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
        recyclerView.setLayoutManager(layoutManager);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
        recyclerView.addItemDecoration(new DividerDecoration(this));

        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        Toast.makeText(MainActivity.this, "Header position: " + position + ", id: " + headerId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.remove(adapter.getItem(position));
            }
        }));
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

    }

    private ArrayList getDummyDataSet() {
        abcd = new ArrayList();
        try {
            JSONArray abc = new JSONArray("[{\"location\":\"A.J. McClung Stadium,Columbus,Georgia,USA\"},{\"location\":\"A.J. Simeon Stadium,High Point,North Carolina,USA\"},{\"location\":\"A.J. Walton Stadium,Warrensburg,Missouri,USA\"},{\"location\":\"b.W. Mumford Stadium,Baton Rouge,Louisiana,USA\"},{\"location\":\"bbbott Memorial Stadium,Tuskegee,Alabama,USA\"},{\"location\":\"bbe Martin Stadium,Lufkin,Texas,USA\"},{\"location\":\"cC Bible Memorial Stadium,Leander,Texas,USA\"},{\"location\":\"cggie Memorial Stadium,Las Cruces,New Mexico,USA\"},{\"location\":\"cggie Stadium,Davis,California,USA\"},{\"location\":\"gggie Stadium,Greensboro,North Carolina,USA\"},{\"location\":\"hlamo Stadium,San Antonio,Texas,USA\"},{\"location\":\"hlamodome,San Antonio,Texas,USA\"},{\"location\":\"hlaska Airlines Arena,Seattle,Washington,USA\"},{\"location\":\"llbertsons Stadium,Boise,Idaho,USA\"},{\"location\":\"llerus Center,Grand Forks,North Dakota,USA\"},{\"location\":\"plex G. Spanos Stadium,S.L. Obispo,California,USA\"},{\"location\":\"pllen County WM Coliseum,Fort Wayne,Indiana,USA\"},{\"location\":\"xllen E. Paulson Stadium,Statesboro,Georgia,USA\"},{\"location\":\"xllen Fieldhouse,Lawrence,Kansas,USA\"},{\"location\":\"xllstate Arena,Rosemont,Illinois,USA\"}]");
            for (int i = 0; i < abc.length(); i++) {
                JSONObject feedJObj = new JSONObject(abc.get(i).toString());
                abcd.add(feedJObj.getString("location"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return abcd;
    }
    public void scrollClick(View v) {
        String firstLetter = (String) v.getTag();
        int index =0;
        if (abcd != null) {

            for (int i = 0; i < abcd.size(); i++) {
                String abcdef= String.valueOf(abcd.get(i).charAt(0));
                if (abcdef.equalsIgnoreCase(firstLetter)) {
                    index = abcd.indexOf(abcd.get(i));
                    break;
                }
            }
        }
        recyclerView.scrollToPosition(index);
    }


    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
        }
    }

    private class AnimalsHeadersAdapter extends AnimalsAdapter<RecyclerView.ViewHolder>
            implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(getItem(position));
        }

        @Override
        public long getHeaderId(int position) {
            if (position == 0) {
                return -1;
            } else {
                return getItem(position).charAt(0);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_header, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(String.valueOf(getItem(position).charAt(0)));
        }

        private int getRandomColor() {
            SecureRandom rgen = new SecureRandom();
            return Color.HSVToColor(150, new float[]{
                    rgen.nextInt(359), 1, 1
            });
        }

    }
}
