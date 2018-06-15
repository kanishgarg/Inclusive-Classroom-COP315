package com.cop315.inclusiveclassroom;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

public class Repository extends AppCompatActivity {
    String SCAN_PATH;
    File[] allFiles ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        File folder = new File(Slides.directory.getName());
        System.out.println(Slides.directory.getName());
        allFiles = folder.listFiles();
        if(allFiles == null) System.out.println("null array");
        else if (allFiles.length != 0) new SingleMediaScanner(Repository.this, allFiles[0]);
        /*((Button) findViewById(R.id.button1))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        new SingleMediaScanner(Repository.this, allFiles[0]);
                    }
                });*/
    }
    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        public void onScanCompleted(String path, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
            mMs.disconnect();
        }

    }


}
