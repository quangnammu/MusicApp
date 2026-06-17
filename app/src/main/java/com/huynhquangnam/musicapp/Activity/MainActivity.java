package com.huynhquangnam.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.huynhquangnam.musicapp.Adapter.ElementListAdapter;
import com.huynhquangnam.musicapp.Data.BaseListElement;
import com.huynhquangnam.musicapp.Data.ListSong;
import com.huynhquangnam.musicapp.Data.SongsElement;
import com.huynhquangnam.musicapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String SONG_NAME = "songName";

    ListView listView;
    ElementListAdapter elementListAdapter;
    List<BaseListElement> offlineListElements;
    View listHeader,listFooter;
    SimpleCursorAdapter suggestionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_element);

        final String[] from = new String[]{SONG_NAME};
        final int[] to = new int[]{android.R.id.text1};
        suggestionAdapter = new SimpleCursorAdapter (this,
                android.R.layout.simple_list_item_1,
                null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(suggestionAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSuggestion(newText);
                return true;
            }
        });

       searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
           @Override
           public boolean onSuggestionSelect(int position) {
               return true;
           }

           @Override
           public boolean onSuggestionClick(int position) {
               suggestionAdapter.getCursor().moveToPosition(position);
               String query = suggestionAdapter.getCursor().getString(1);
               Intent intent = new Intent(MainActivity.this, ListSongActivity.class);
               intent.setAction(Intent.ACTION_SEARCH);
               intent.putExtra(SONG_NAME,query);
               startActivity(intent);
               return true;
           }
       });

        return true;
    }

    private void getSuggestion(String text){
        MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID,SONG_NAME});
        for (int i = 0; i<ListSong.getListSong().size();i++){
            if(ListSong.getListSong().get(i).getSongName().toLowerCase().contains(text.toLowerCase())){
                c.addRow(new Object[]{i, ListSong.getListSong().get(i).getSongName()});
            }
        }
        suggestionAdapter.changeCursor(c);
    }
}
