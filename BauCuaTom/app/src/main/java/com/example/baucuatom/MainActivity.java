package com.example.baucuatom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private Custom_GridView_BanCo adapter;
    private Integer[] dsHinh = {R.drawable.nai, R.drawable.bau, R.drawable.ga, R.drawable.ca, R.drawable.cua, R.drawable.tom};
    private AnimationDrawable cdXiNgau1, cdXiNgau2, cdXiNgau3;
    private ImageView hinhXiNgau1, hinhXiNgau2, hinhXiNgau3;
    private Random randomXiNgau;
    private TextView tvTien,tvThoiGian;
    private int tongTienCu, tongTienMoi;
    private int tienThuong, kiemTra, id_amthanh;
    private int giatriXiNgau1, giatriXiNgau2, giatriXiNgau3;
    public static Integer[] gtDatCuoc = new Integer[6];
    private Timer timer = new Timer();
    private Handler handler;
    private int tien;
    private CountDownTimer demthoigian;

//    private CheckBox ktAmThanh;
//    private MediaPlayer nhacnen = new MediaPlayer();
    private SoundPool amThanhXiNgau = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    //(luong toi da,loai am thanh,do uu tien)


    Handler.Callback callback = new Handler.Callback() {
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
            } else {
                Toast.makeText(getApplicationContext(), "Ôi xui quá mất " + tienThuong + "  rồi!", Toast.LENGTH_SHORT).show();
            }


            Log.d("KetQua", " " + giatriXiNgau1 + " " + giatriXiNgau2 + " " + giatriXiNgau3 + "  " + "TienThuong:" + tienThuong);
            LuuDuLieuNguoiDung(tienThuong);
            tvTien.setText(String.valueOf(tongTienMoi));

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hinhXiNgau1 = findViewById(R.id.xingau1);
        hinhXiNgau2 = findViewById(R.id.xingau2);
        hinhXiNgau3 = findViewById(R.id.xingau3);
        tvTien = findViewById(R.id.tvTien);
        tvThoiGian = findViewById(R.id.tvThoiGian);
//        ktAmThanh = findViewById(R.id.checkBox);

        gridView = findViewById(R.id.gvBanCo);
        adapter = new Custom_GridView_BanCo(this, R.layout.custom_banco, dsHinh);
        gridView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
        tongTienCu = sharedPreferences.getInt("TongTien", 1000);
        tien = tongTienCu + tienThuong;
        tvTien.setText(String.valueOf(tien));



        id_amthanh = amThanhXiNgau.load(this, R.raw.lac, 1);

        demthoigian = new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long milis = millisUntilFinished;
                long gio = TimeUnit.MILLISECONDS.toHours(milis);
                long phut = TimeUnit.MILLISECONDS.toMinutes(milis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milis));
                long giay = TimeUnit.MILLISECONDS.toSeconds(milis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milis));

                String giophutgiay = String.format("%02d:%02d:%02d",gio,phut,giay);
                tvThoiGian.setText(giophutgiay);

            }

            @Override
            public void onFinish() {
                SharedPreferences sharedPreferences = getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // SharedPreferences.Editor edit = luuTru.edit();
                SharedPreferences sharedPreference = getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
                tongTienCu = sharedPreference.getInt("TongTien", 1000);
                tongTienMoi = tongTienCu + 1000;
                editor.putInt("TongTien", tongTienMoi);
                editor.commit();
                tvTien.setText(String.valueOf(tongTienMoi));
                demthoigian.cancel();
                demthoigian.start();

            }
        };

        demthoigian.start();


        handler = new Handler(callback);
    }

    private void LuuDuLieuNguoiDung(int tienThuong) {
        SharedPreferences sharedPreferences = getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // SharedPreferences.Editor edit = luuTru.edit();
        SharedPreferences sharedPreference = getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
        tongTienCu = sharedPreference.getInt("TongTien", 1000);
        tongTienMoi = tongTienCu + tienThuong;
        editor.putInt("TongTien", tongTienMoi);
        editor.commit();
    }

    public void LacXiNgau(View v) {
        hinhXiNgau1.setImageResource(R.drawable.hinhdongxingau);
        hinhXiNgau2.setImageResource(R.drawable.hinhdongxingau);
        hinhXiNgau3.setImageResource(R.drawable.hinhdongxingau);

        cdXiNgau1 = (AnimationDrawable) hinhXiNgau1.getDrawable();
        cdXiNgau2 = (AnimationDrawable) hinhXiNgau2.getDrawable();
        cdXiNgau3 = (AnimationDrawable) hinhXiNgau3.getDrawable();

        kiemTra = 0;
        for (int i = 0; i < gtDatCuoc.length; i++) {
            kiemTra += gtDatCuoc[i];
        }
        if (kiemTra == 0) {
            Toast.makeText(getApplicationContext(), "Bạn vui lòng đặt cược !", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreference = getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
            tongTienCu = sharedPreference.getInt("TongTien", 1000);
            if (kiemTra > tongTienCu) {
                Toast.makeText(getApplicationContext(), "Bạn không đủ tiền để đặt cược !", Toast.LENGTH_SHORT).show();
            } else {

                amThanhXiNgau.play(id_amthanh, 1.0f, 1.0f, 1, 0, 1.0f);
                cdXiNgau1.start();
                cdXiNgau2.start();
                cdXiNgau3.start();

                tienThuong = 0;

                timer.schedule(new LacXiNgau(), 2000);
            }
        }
    }

    class LacXiNgau extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private void RandomXiNgau1() {
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd) {
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

    private void RandomXiNgau2() {
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd) {
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

    private void RandomXiNgau3() {
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd) {
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
