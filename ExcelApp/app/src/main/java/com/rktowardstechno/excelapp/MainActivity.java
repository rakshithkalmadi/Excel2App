package com.rktowardstechno.excelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    AsyncHttpClient client;
    Workbook workbook;
    List<String> titles,description,imageUrl;
//    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "https://github.com/rakshithkalmadi/Excel2App/blob/master/exceldata.xls?raw=true";

        recyclerView = findViewById(R.id.listdata);


        titles = new ArrayList<>();
        description = new ArrayList<>();
        imageUrl = new ArrayList<>();




        client = new AsyncHttpClient();
        //progressBar.setVisibility(View.VISIBLE);
        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to download", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Download Successful", Toast.LENGTH_SHORT).show();

                WorkbookSettings ws = new WorkbookSettings();
                ws.setGCDisabled(true);

                if(file != null){
                    try {
                        workbook = Workbook.getWorkbook(file);
                        Sheet sheet = workbook.getSheet(0);

                        for(int i = 0; i<sheet.getRows() ; i++){
                            Cell[] row = sheet.getRow(i);
                            titles.add(row[0].getContents());
                            description.add(row[1].getContents());
                            imageUrl.add(row[2].getContents());

                        }
                        showData();

                        Log.d("TAG", "Success "+ titles);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }

                }
            }
        });



    }

    private void showData() {
        adapter = new Adapter(this,titles,description,imageUrl);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}