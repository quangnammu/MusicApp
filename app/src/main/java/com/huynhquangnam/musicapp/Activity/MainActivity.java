package com.huynhquangnam.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.huynhquangnam.musicapp.Adapter.ElementListAdapter;
import com.huynhquangnam.musicapp.Data.BaseListElement;
import com.huynhquangnam.musicapp.Data.ListSong;
import com.huynhquangnam.musicapp.Data.SongsElement;
import com.huynhquangnam.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ElementListAdapter elementListAdapter;
    List<BaseListElement> offlineListElements;
    View listHeader,listFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_element);
        offlineListElements = new ArrayList<>();
        addElement();
        listView.setAdapter(elementListAdapter);
        setHeaderAndFooter();
    }

    private void setHeaderAndFooter() {
        listHeader = View.inflate(this, R.layout.home_list_header,null);
        listFooter = View.inflate(this, R.layout.home_list_footer,null);
        listView.addHeaderView(listHeader);
        listView.addFooterView(listFooter);
    }

    private void addElement() {
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));
        offlineListElements.add(new SongsElement(this));

        elementListAdapter = new ElementListAdapter(this, offlineListElements);
    }
}
