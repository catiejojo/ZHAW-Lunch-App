package com.zhaw.catiejo.whatsforlunch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Boilerplate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // In activity_main.xml, there's a Recycler View with the id recyclerView.
        // This gets a reference to that.
        mRecyclerView = (RecyclerView) findViewById(R.id.menuRecycler);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        // Horizontally scrolling layout manager
        // Influenced from https://goo.gl/B9CsiA and https://goo.gl/fQbj5E
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set up the adapter...this connects the views (layouts?) to the data (my custom class, MenuCard)
        mAdapter = new MenuCardAdapter(GetMenuCardList());
        mRecyclerView.setAdapter(mAdapter);
    }
    private List<MenuCard> GetMenuCardList() {
        List<MenuCard> cards = new ArrayList<>();
        cards.add(new MenuCard(R.string.foodCounter_1, R.string.menuItem_1, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        cards.add(new MenuCard(R.string.foodCounter_2, R.string.menuItem_2, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        cards.add(new MenuCard(R.string.foodCounter_3, R.string.menuItem_3, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        cards.add(new MenuCard(R.string.foodCounter_4, R.string.menuItem_4, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        cards.add(new MenuCard(R.string.foodCounter_5, R.string.menuItem_5, R.string.filler, R.string.studentPrice, R.string.employeePrice, R.string.externalPrice));
        return cards;
    }
}
