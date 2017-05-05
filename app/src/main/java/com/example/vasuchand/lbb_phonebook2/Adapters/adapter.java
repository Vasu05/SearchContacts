package com.example.vasuchand.lbb_phonebook2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vasuchand.lbb_phonebook2.MainActivity;
import com.example.vasuchand.lbb_phonebook2.R;
import com.example.vasuchand.lbb_phonebook2.contacts_data;
import com.example.vasuchand.lbb_phonebook2.share_contact;

import java.io.IOException;
import java.util.List;

/**
 * Created by Vasu Chand on 5/3/2017.
 */

public class adapter  extends RecyclerView.Adapter<adapter.MyViewHolder>  {
    private List<contacts_data> moviesList;
    private Context mContext;

    private contacts_data currentItem;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView t1, t2,t3;
        ImageView imageView;
        protected RecyclerView recycler_view_list;


        public MyViewHolder(View view) {
            super(view);
            t1 = (TextView)view.findViewById(R.id.user_name);
            t2 = (TextView)view.findViewById(R.id.tvPhoneNumber);
            t3 = (TextView)view.findViewById(R.id.contactid);
            imageView = (ImageView)view.findViewById(R.id.image);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        call(pos);
                    }
                }
            });
        }

        }



    public adapter(Context context, List<contacts_data> moviesList)
    {
        this.moviesList = moviesList;
        this.mContext = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_contact, parent, false);


        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final contacts_data data = moviesList.get(position);
        holder.t1.setText(data.getName());
        holder.t2.setText(data.getNumber());
        holder.t3.setText(data.getId());
        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getImage());

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap!=null) {
            holder.imageView.setImageBitmap(bitmap);
        }
        else
        holder.imageView.setImageResource(R.drawable.user);
        //if(data.ge)


    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void call(int position)
    {
        currentItem = moviesList.get(position);


        Intent myIntent = new Intent(this.mContext, share_contact.class);
        myIntent.putExtra("contactid", currentItem.getId() ); //Optional parameters
        myIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);

        this.mContext.startActivity(myIntent);
        ((MainActivity)mContext).finish();

    }



}
