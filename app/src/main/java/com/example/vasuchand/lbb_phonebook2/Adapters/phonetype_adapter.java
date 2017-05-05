package com.example.vasuchand.lbb_phonebook2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vasuchand.lbb_phonebook2.Import_contact;
import com.example.vasuchand.lbb_phonebook2.R;
import com.example.vasuchand.lbb_phonebook2.phonetype_data;
import com.example.vasuchand.lbb_phonebook2.share_contact;

import java.util.List;
import android.graphics.Bitmap;
import android.widget.Toast;

/**
 * Created by Vasu Chand on 5/4/2017.
 */

public class phonetype_adapter extends RecyclerView.Adapter {
    private List<phonetype_data> List;
    private Context mContext;
    private String name;
    private Bitmap image;
    private phonetype_data currentItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView t1, t2;
        ImageView imageView,imageView2;

        public MyViewHolder(final View view) {
            super(view);
            t1 = (TextView)view.findViewById(R.id.number);
            t2 = (TextView)view.findViewById(R.id.type);
            imageView = (ImageView)view.findViewById(R.id.phoneicon);
            imageView2 = (ImageView)view.findViewById(R.id.select);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            call(pos);
                        }

                }
            });

        }


    }

    public void call(int position)
    {
        currentItem = List.get(position);
        if(currentItem.getContacttype()==0) {

            Intent returnIntent = new Intent(this.mContext, Import_contact.class);
            returnIntent.putExtra("contact", currentItem.getNumber());
            returnIntent.putExtra("username", name);
            this.mContext.startActivity(returnIntent);
        }
        else
        {
            Toast.makeText(this.mContext, "Select Any Mobile Number Please ", Toast.LENGTH_SHORT).show();
        }

    }

    public static class MyViewHolderheader extends RecyclerView.ViewHolder {
        public TextView t1;
        ImageView imageView;



        public MyViewHolderheader(View view) {
            super(view);
            t1 = (TextView)view.findViewById(R.id.user_name);
            imageView = (ImageView)view.findViewById(R.id.image);


        }


    }
    public phonetype_adapter(Context context, List<phonetype_data> List, String name,Bitmap image)
    {
        this.name=name;
        this.List = List;
        this.mContext = context;
        this.image = image;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_contact_profile, parent, false);
                return new MyViewHolderheader(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_type_item, parent, false);
                return new MyViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        phonetype_data data = List.get(position);


        if(data.getView()==0)
        {
            ((MyViewHolderheader) holder).t1.setText(name);
            if(image!=null)
            ((MyViewHolderheader) holder).imageView.setImageBitmap(image);

        }
        else {
            // if email found
            ((MyViewHolder) holder).t1.setText(data.getNumber());
            ((MyViewHolder) holder).t2.setText(data.getPhonetype());
            if (data.getContacttype() == 2) {

                ((MyViewHolder) holder).imageView.setImageResource(R.drawable.email);
                ((MyViewHolder) holder).imageView2.setImageBitmap(null);
            }

            if (data.getContacttype() == 1) {

                ((MyViewHolder) holder).imageView.setImageResource(R.drawable.address);
                ((MyViewHolder) holder).imageView2.setImageBitmap(null);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        System.out.println(List.get(position).getView());
        switch (List.get(position).getView()) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }
    }




    @Override
    public int getItemCount() {
        return List.size();
    }
}
