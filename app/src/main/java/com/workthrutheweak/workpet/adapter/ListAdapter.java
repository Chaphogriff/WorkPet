package com.workthrutheweak.workpet.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workthrutheweak.workpet.AvatarActivity;
import com.workthrutheweak.workpet.ListActivity;
import com.workthrutheweak.workpet.R;
import com.workthrutheweak.workpet.model.Item;

import java.io.Serializable;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    List<String> titles;
    List<Integer> images;
    List<String> prices;
    LayoutInflater inflater;
    String mode;
    Context ctx;

    public ListAdapter(Context ctx, List<String> titles, List<Integer> images, List<String> prices, String mode ){
        this.titles = titles;
        this.images = images;
        this.prices = prices;
        this.mode = mode;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.icon.setImageResource(images.get(position));
        holder.price.setText(prices.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView icon;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String title = titles.get(getAbsoluteAdapterPosition());
                    String description = prices.get(getAbsoluteAdapterPosition());
                    Toast.makeText(view.getContext(),"Click on "+ title, Toast.LENGTH_SHORT).show();


                    if(mode.equals("customize")){
                        Intent i = new Intent(ctx, AvatarActivity.class);
                        i.putExtra("avatar", title.toLowerCase());
                        ctx.startActivity(i);
                    }else if(mode.equals("shop")){
                        Item item = new Item(title,"",Integer.parseInt(description.split(" ")[0]),25);
                        Intent i = new Intent(ctx, AvatarActivity.class);
                        i.putExtra("newitem", item);
                        ctx.startActivity(i);
                    }


                }
            });

        }
    }

}
