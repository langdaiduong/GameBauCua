package com.example.baucuatom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    Custom_GridView_BanCo adapter;
    Integer[] dsHinh = {R.drawable.nai,R.drawable.bau,R.drawable.ga,R.drawable.ca,R.drawable.cua,R.drawable.tom};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gvBanCo);
        adapter = new Custom_GridView_BanCo(this,R.layout.custom_banco,dsHinh);
        gridView.setAdapter(adapter);
    }
}
