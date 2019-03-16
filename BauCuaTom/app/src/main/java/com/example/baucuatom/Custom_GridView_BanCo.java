package com.example.baucuatom;

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.Spinner;

public class Custom_GridView_BanCo extends ArrayAdapter<Integer> {

    private Context context;
    private int resource;
    private Integer[] objects;
    private Integer[] giatien={0,100,200,300,400,500};
    ArrayAdapter<Integer> adapter;


    public Custom_GridView_BanCo(Context context, int resource, Integer[] objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
        adapter = new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,giatien);

    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.custom_banco, parent, false);

        ImageView imBanCo =convertView.findViewById(R.id.imgBanCo);
        Spinner spGiaTien=convertView.findViewById(R.id.spinGiatien);

        imBanCo.setImageResource(objects[position]);//position vi tri cua view truyen vao VD: Hươu position = 0;
        spGiaTien.setAdapter(adapter);
        return convertView;

    }
}
