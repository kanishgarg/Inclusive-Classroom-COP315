package com.cop315.inclusiveclassroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu to the action bar if present
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles the click event on the options of the menu
        int id = item.getItemId();

        //switch
        switch (id) {
            case R.id.about :
                final Intent intent1 = new Intent(this, About.class);
                startActivity(intent1);
                return true;

            case R.id.repository :
                final Intent intent2 = new Intent(this, Repository.class);
                startActivity(intent2);
                return true;

            case R.id.settings :
                final Intent intent3 = new Intent(this, Settings.class);
                startActivity(intent3);
                return true;

            case R.id.slides :
                final Intent intent4 = new Intent(this, Slides.class);
                startActivity(intent4);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
