package com.example.baucuatom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    Custom_GridView_BanCo adapter;
    Integer[] dsHinh = {R.drawable.nai,R.drawable.bau,R.drawable.ga,R.drawable.ca,R.drawable.cua,R.drawable.tom};
    AnimationDrawable cdXiNgau1, cdXiNgau2, cdXiNgau3;
    ImageView hinhXiNgau1,hinhXiNgau2,hinhXiNgau3;
    Random randomXiNgau;
    TextView tvTien;
    int tongTienCu,tongTienMoi;
    int tienThuong , kiemTra, id_amthanh;
    SharedPreferences luuTru;
    SoundPool AmThanhXiNgau = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    MediaPlayer nhacnen = new MediaPlayer();
    CheckBox ktAmThanh;

    private int giatriXiNgau1,giatriXiNgau2,giatriXiNgau3;
    public static Integer[] gtDatCuoc = new Integer[6];
    Timer timer = new Timer();
    Handler handler;


    Handler.Callback callback = new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            RandomXiNgau1();
            RandomXiNgau2();
            RandomXiNgau3();

            for (int i = 0; i < gtDatCuoc.length; i++) {
//                Log.d("KetQua", " " + i + " - " + gtDatCuoc[i]);
                if (gtDatCuoc[i] != 0) {
                    if (i == giatriXiNgau1) {
                        tienThuong += gtDatCuoc[i];
                    }
                    if (i == giatriXiNgau2) {
                        tienThuong += gtDatCuoc[i];
                    }
                    if (i == giatriXiNgau3) {
                        tienThuong += gtDatCuoc[i];
                    }
                    if (i != giatriXiNgau1 && i != giatriXiNgau2 && i != giatriXiNgau3) {
                        tienThuong -= gtDatCuoc[i];
                    }
                }
            }
            if (tienThuong > 0) {
                Toast.makeText(getApplicationContext(), "Quá dữ , Bạn đã trúng " + tienThuong, Toast.LENGTH_SHORT).show();
            } else if (tienThuong == 0) {
                Toast.makeText(getApplicationContext(), "Hên quá mém chết! ", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(), "Ôi xui quá mất " + tienThuong + "  rồi!", Toast.LENGTH_SHORT).show();
            }



            Log.d("KetQua", " " + giatriXiNgau1 + " " + giatriXiNgau2 + " " + giatriXiNgau3 +"  " + "TienThuong:" + tienThuong );
            LuuDuLieuNguoiDung(tienThuong);
            tvTien.setText(String.valueOf(tongTienMoi));

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hinhXiNgau1 =  findViewById(R.id.xingau1);
        hinhXiNgau2 =  findViewById(R.id.xingau2);
        hinhXiNgau3 =  findViewById(R.id.xingau3);
        tvTien = findViewById(R.id.tvTien);

        gridView = findViewById(R.id.gvBanCo);
        adapter = new Custom_GridView_BanCo(this,R.layout.custom_banco,dsHinh);
        gridView.setAdapter(adapter);


        luuTru = getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
        tongTienCu = luuTru.getInt("TongTien",1000);
        tvTien.setText(String.valueOf(tongTienCu));

        id_amthanh = AmThanhXiNgau.load(this, R.raw.music, 1);
        nhacnen = MediaPlayer.create(this,R.raw.nhacnen);
        nhacnen.start();
        ktAmThanh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean kt) {
                if(kt){
                    nhacnen.stop();
                }else {
                    try{
                        nhacnen.prepare();
                        nhacnen.start();
                    } catch (IllegalStateException e){
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

            }
        });

        handler = new Handler(callback);
    }

    private void LuuDuLieuNguoiDung(int tienThuong){
        SharedPreferences.Editor edit = luuTru.edit();
        tongTienMoi = tongTienCu + tienThuong;
        edit.putInt("Tong Tien:",tongTienMoi);
        edit.commit();
    }

    public void LacXiNgau(View v){
        hinhXiNgau1.setImageResource(R.drawable.hinhdongxingau);
        hinhXiNgau2.setImageResource(R.drawable.hinhdongxingau);
        hinhXiNgau3.setImageResource(R.drawable.hinhdongxingau);

        cdXiNgau1 = (AnimationDrawable) hinhXiNgau1.getDrawable();
        cdXiNgau2 = (AnimationDrawable) hinhXiNgau2.getDrawable();
        cdXiNgau3 = (AnimationDrawable) hinhXiNgau3.getDrawable();

         kiemTra = 0;
        for (int i = 0; i < gtDatCuoc.length; i++){
            kiemTra += gtDatCuoc[i];
        }
        if(kiemTra == 0){
            Toast.makeText(getApplicationContext(),"Bạn vui lòng đặt cược !",Toast.LENGTH_SHORT).show();
        }else {
            if(kiemTra > tongTienCu){
                Toast.makeText(getApplicationContext(),"Bạn không đủ tiền để đặt cược !",Toast.LENGTH_SHORT).show();
            }else{
                AmThanhXiNgau.play(id_amthanh,1.0f, 1.0f,1, 0, 1.0f);
                cdXiNgau1.start();
                cdXiNgau2.start();
                cdXiNgau3.start();

                tienThuong = 0;

                timer.schedule(new LacXiNgau(),1000);
            }
        }
    }

    class LacXiNgau extends TimerTask{
        @Override
        public void run(){
            handler.sendEmptyMessage(0);
        }
    }

    private void RandomXiNgau1(){
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd){
            case 0:
                hinhXiNgau1.setImageResource(dsHinh[0]);
                giatriXiNgau1 = rd;
                break;
            case 1:
                hinhXiNgau1.setImageResource(dsHinh[1]);
                giatriXiNgau1 = rd;
                break;
            case 2:
                hinhXiNgau1.setImageResource(dsHinh[2]);
                giatriXiNgau1 = rd;
                break;
            case 3:
                hinhXiNgau1.setImageResource(dsHinh[3]);
                giatriXiNgau1 = rd;
                break;
            case 4:
                hinhXiNgau1.setImageResource(dsHinh[4]);
                giatriXiNgau1 = rd;
                break;
            case 5:
                hinhXiNgau1.setImageResource(dsHinh[5]);
                giatriXiNgau1 = rd;
                break;
        }
    }

    private void RandomXiNgau2(){
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd){
            case 0:
                hinhXiNgau2.setImageResource(dsHinh[0]);
                giatriXiNgau2 = rd;
                break;
            case 1:
                hinhXiNgau2.setImageResource(dsHinh[1]);
                giatriXiNgau2 = rd;
                break;
            case 2:
                hinhXiNgau2.setImageResource(dsHinh[2]);
                giatriXiNgau2 = rd;
                break;
            case 3:
                hinhXiNgau2.setImageResource(dsHinh[3]);
                giatriXiNgau2 = rd;
                break;
            case 4:
                hinhXiNgau2.setImageResource(dsHinh[4]);
                giatriXiNgau2 = rd;
                break;
            case 5:
                hinhXiNgau2.setImageResource(dsHinh[5]);
                giatriXiNgau2 = rd;
                break;
        }
    }

    private void RandomXiNgau3(){
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd){
            case 0:
                hinhXiNgau3.setImageResource(dsHinh[0]);
                giatriXiNgau3 = rd;
                break;
            case 1:
                hinhXiNgau3.setImageResource(dsHinh[1]);
                giatriXiNgau3 = rd;
                break;
            case 2:
                hinhXiNgau3.setImageResource(dsHinh[2]);
                giatriXiNgau3 = rd;
                break;
            case 3:
                hinhXiNgau3.setImageResource(dsHinh[3]);
                giatriXiNgau3 = rd;
                break;
            case 4:
                hinhXiNgau3.setImageResource(dsHinh[4]);
                giatriXiNgau3 = rd;
                break;
            case 5:
                hinhXiNgau3.setImageResource(dsHinh[5]);
                giatriXiNgau3 = rd;
                break;
        }
    }
}
