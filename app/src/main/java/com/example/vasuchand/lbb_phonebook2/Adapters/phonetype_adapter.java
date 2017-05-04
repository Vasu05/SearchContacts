package com.example.vasuchand.lbb_phonebook2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vasuchand.lbb_phonebook2.R;
import com.example.vasuchand.lbb_phonebook2.phonetype_data;

import java.util.List;

/**
 * Created by Vasu Chand on 5/4/2017.
 */

public class phonetype_adapter extends RecyclerView.Adapter<phonetype_adapter.MyViewHolder> {
    private List<phonetype_data> List;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView t1, t2;
        ImageView imageView;
        protected RecyclerView recycler_view_list;


        public MyViewHolder(View view) {
            super(view);
            t1 = (TextView)view.findViewById(R.id.number);
            t2 = (TextView)view.findViewById(R.id.type);

        }


    }
    public phonetype_adapter(Context context, List<phonetype_data> List)
    {
        this.List = List;
        this.mContext = context;

    }

    @Override
    public phonetype_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_type_item, parent, false);


        return new phonetype_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(phonetype_adapter.MyViewHolder holder, int position) {

        phonetype_data data = List.get(position);
        holder.t1.setText(data.getNumber());
        holder.t2.setText(data.getPhonetype());

    }


    @Override
    public int getItemCount() {
        return List.size();
    }
}
