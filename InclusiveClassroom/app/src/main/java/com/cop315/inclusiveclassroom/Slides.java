package com.cop315.inclusiveclassroom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

public class Slides extends AppCompatActivity {
    private static boolean firstRun = true;
    private final int PICK_IMAGE = 1;
    Bitmap galleryImage;
    int current_color = Color.BLACK;
    TouchImageView mImage;
    static File directory;
    int k = 1;
    int l = 1;

    public void  isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            return;
        }
        else {
            return ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides);
        mImage = findViewById(R.id.touchImageView);
        isStoragePermissionGranted(); // seek storage permissions
        if(firstRun)
        {
            Toast.makeText(getApplicationContext(), "FIRST RUN", Toast.LENGTH_SHORT).show();
            //YOUR FIRST RUN CODE HERE
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            //if there is no SD card, create new directory objects to make directory on device

            if (Environment.getExternalStorageState() == null) {

                directory = new File(Environment.getDataDirectory()
                        + "/InclusiveClassrooom"+currentDateTimeString);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // if phone DOES have sd card
            } else if (Environment.getExternalStorageState() != null) {

                directory = new File(Environment.getExternalStorageDirectory()
                        + "/InclusiveClassroom/"+currentDateTimeString);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }
        }
        firstRun = false;

        final Thread thread = new Thread((new Runnable() {
            @Override
            public void run() {
                File resolveMeSDCard;
                String imageUrl = "";
                String fileUrl = "http://192.168.10.1:7000/file.txt";
                URL url2 = null;
                URL url1 = null;
                try {
                    url1 = new URL(fileUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection urlcon1;
                HttpURLConnection urlcon2;
                InputStream is;
                InputStream is2;
                OutputStream os;
                byte[] b;
                String str="";
                String prevUrl="";
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        imageUrl = "";
                        urlcon1 = (HttpURLConnection) url1.openConnection();
                        urlcon1.setConnectTimeout(30000);
                        urlcon1.setReadTimeout(5000);
                        is = urlcon1.getInputStream();
                        b = new byte[2048];
                        int length;
                        while ((length = is.read(b)) != -1) {
                            imageUrl = imageUrl + new String(b, 0, length, "UTF-8");
                        }
                        if(!imageUrl.equals(prevUrl)) {
                            url2 = new URL(imageUrl);
                            urlcon2 = (HttpURLConnection) url2.openConnection();
                            urlcon2.setConnectTimeout(30000);
                            urlcon2.setReadTimeout(30000);
                            is2 = urlcon2.getInputStream();
                            str = directory + "/image" + k + ".jpg";
                            resolveMeSDCard = new File(str);
                            os = new FileOutputStream(resolveMeSDCard, true);
                            while ((length = is2.read(b)) != -1) {
                                os.write(b, 0, length);
                            }
                            is.close();
                            is2.close();
                            os.close();
                            k++;
                            prevUrl=imageUrl;
                        }
                        /*if(k==2) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap bitmap = BitmapFactory.decodeFile(str, options);
                            mImage.setImageDrawable(Drawable.createFromPath(str));
                            System.out.println("new Image set");
                            l=1;
                        }*/
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    File file = new File(directory+ "/image" + k + ".jpg");
                                    if(file.exists()) {
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                        Bitmap bitmap = BitmapFactory.decodeFile(directory+ "/image" + k +".jpg", options);
                                        mImage.setImageBitmap(bitmap);
                                        l++;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread.sleep(5000);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));

        thread.start();

        /*https://stackoverflow.com/questions/10292792/getting-image-from-url-java*/

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        File file = new File(directory+ "/image" + l + ".jpg");
                        if(file.exists()) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap bitmap = BitmapFactory.decodeFile(directory+ "/image" + l +".jpg", options);
                            mImage.setImageBitmap(bitmap);
                            l++;
                            Thread.sleep(5000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });*/

        FloatingActionButton resume = findViewById(R.id.resumeButton);

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.start();
//                Intent gallIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                gallIntent.setType("image");
//                startActivityForResult(Intent.createChooser(gallIntent, "Select Picture"), PICK_IMAGE);
            }
        });
        FloatingActionButton red = findViewById(R.id.red);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changecolor(Color.RED);
            }
        });
        FloatingActionButton green = findViewById(R.id.green);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changecolor(Color.GREEN);
            }
        });
        FloatingActionButton blue = findViewById(R.id.blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changecolor(Color.BLUE);
            }
        });
        FloatingActionButton contrast = findViewById(R.id.contrastButton);
        contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(galleryImage == null) {
                    System.out.println("gallery image is null");
                    return;
                }
                Bitmap bitmap = ((BitmapDrawable)mImage.getDrawable()).getBitmap();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int pixel;
                Bitmap newImage = Bitmap.createBitmap(width, height,bitmap.getConfig());
                for(int i=0;i<height;i++)
                {
                    for(int j=0;j<width;j++)
                    {
                        pixel=bitmap.getPixel(j,i);
                        if(pixel != Color.WHITE) {
                            pixel = Color.YELLOW;
                        } else pixel = Color.BLACK;
                        newImage.setPixel(j, i, pixel);
                    }
                }
            }
        });
        FloatingActionButton exit = findViewById(R.id.exitButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                galleryImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mImage.setImageBitmap(Bitmap.createScaledBitmap(galleryImage, galleryImage.getWidth(), (int) (galleryImage.getHeight()*1.3), true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void changecolor(int targetColor) {
        if(galleryImage == null) {
            System.out.println("gallery image is null");
            return;
        }
        Bitmap bitmap = ((BitmapDrawable)mImage.getDrawable()).getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixel;
        Bitmap newImage = Bitmap.createBitmap(width, height,bitmap.getConfig());
        for(int i=0;i<height;i++)
        {
            for(int j=0;j<width;j++)
            {
                pixel=bitmap.getPixel(j,i);
                if(pixel != Color.WHITE) {
                    pixel = targetColor;
                }
                newImage.setPixel(j, i, pixel);
            }
        }

        mImage.setImageBitmap(newImage);
        current_color=targetColor;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println(Arrays.toString(grantResults));
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
        }
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

