package com.zhaw.catiejo.whatsforlunch.mensa_picker;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.common.base.Optional;
import com.squareup.otto.Bus;
import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.menu_display.MenuDisplayActivity;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch.WhatsForLunchApplication;
import com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;

import org.joda.time.LocalDate;

import javax.inject.Inject;

public class MensaPickerActivity extends AppCompatActivity {
    // Injections required for syncing with server
    @Inject
    ICateringController cateringController;
    @Inject
    Bus bus;

    private CanteenContentObserver canteenContentObserver;
    private LoadCanteensTask loadCanteensTask;
    private MensaPickerAdapter mMensaPickerAdapter; //used for LoadCanteensTask and CanteenContentObserver

    // The mensa from the previous task. Needed in case user hits cancel button in toolbar
    private static MensaContainer mMensa;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Required to access the catering servers
        ((WhatsForLunchApplication) getApplication()).inject(this);

        this.setContentView(R.layout.activity_mensa_picker);
        setCurrentMensa((MensaContainer) getIntent().getSerializableExtra(Constants.MENU_SELECTOR));

        // Set up RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.mensaRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMensaPickerAdapter = new MensaPickerAdapter(this, null);
        mRecyclerView.setAdapter(mMensaPickerAdapter);

        setUpToolbar();
    }

    // Adapted from CampusInfo app to handle fetching the mensas
    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);

        canteenContentObserver = new CanteenContentObserver(new Handler(Looper.getMainLooper()));
        getContentResolver().registerContentObserver(CateringContentProvider.CONTENT_URI,
                true, canteenContentObserver);
        loadCanteensTask = new LoadCanteensTask();
        loadCanteensTask.execute();
    }

    // Adapted from CampusInfo app to handle fetching the mensas
    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);

        getContentResolver().unregisterContentObserver(canteenContentObserver);

        if (loadCanteensTask != null && !loadCanteensTask.isCancelled()) {
            loadCanteensTask.cancel(true);
            loadCanteensTask = null;
        }
    }

    public static MensaContainer getCurrentMensa() {
        if (mMensa == null) {
            return null;
        }
        return mMensa;
    }
    private static void setCurrentMensa(MensaContainer mensa) {
        mMensa = mensa;
    }


    private void setUpToolbar() {
        ActionBar toolbar = getSupportActionBar();
        if (mMensa != null) {
            // This activity is the default activity. Only display the back
            // button if the user is coming from another activity.
            toolbar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle(R.string.mensaPicker);
    }

    // credit: https://stackoverflow.com/questions/35810229/how-to-display-and-set-click-event-on-back-arrow-on-toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MenuDisplayActivity.class);
            intent.putExtra(Constants.MENU_SELECTOR, mMensa);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Adapted from CampusInfo app to handle fetching the mensas
    private class LoadCanteensTask extends AsyncTask<Void, Void, Optional<Cursor>> {
        @Override
        protected Optional<Cursor> doInBackground(Void... params) {
            return Optional.fromNullable(cateringController.getCanteens(LocalDate.now(Constants.LocalTimeZone)));
        }

        @Override
        protected void onPostExecute(Optional<Cursor> optionalCursor) {
            if (this.isCancelled() || mMensaPickerAdapter == null) {
                return;
            }
            mMensaPickerAdapter.swapCursor(optionalCursor.orNull());
        }
    }

    // Adapted from CampusInfo app to handle fetching the mensas
    private class CanteenContentObserver extends ContentObserver {

        public CanteenContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            loadCanteensTask = new LoadCanteensTask();
            loadCanteensTask.execute();
        }
    }
}
