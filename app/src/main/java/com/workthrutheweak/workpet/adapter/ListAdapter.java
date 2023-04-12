package com.workthrutheweak.workpet.adapter;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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
    MediaPlayer mp;
    Context ctx;
    int gold;

    public ListAdapter(Context ctx, List<String> titles, List<Integer> images, List<String> prices, String mode ){
        this.titles = titles;
        this.images = images;
        this.prices = prices;
        this.mode = mode;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
    }

    public ListAdapter(Context ctx, List<String> titles, List<Integer> images, List<String> prices, String mode, int gold ){
        this.titles = titles;
        this.images = images;
        this.prices = prices;
        this.mode = mode;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.gold = gold;
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

                    if(mode.equals("customize")){
                        Toast.makeText(view.getContext(),"Change pet to "+ title, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ctx, AvatarActivity.class);
                        i.putExtra("avatar", title.toLowerCase());
                        ctx.startActivity(i);

                    }else if(mode.equals("shop")){
                        int itemPrice = Integer.parseInt(description.split(" ")[0]);
                        if(itemPrice>gold){
                            mp = MediaPlayer.create(ctx, R.raw.nov3);
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.release();
                                }
                            });
                            Toast.makeText(view.getContext(),"Not enough gold!", Toast.LENGTH_SHORT).show();
                            mp.start();
                        }else{
                            mp = MediaPlayer.create(ctx, R.raw.buy_item);
                            mp.start();
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.release();
                                    Item item = new Item(title,"",Integer.parseInt(description.split(" ")[0]),25);
                                    Intent i = new Intent(ctx, AvatarActivity.class);
                                    i.putExtra("newitem", item);
                                    mp.release();
                                    ctx.startActivity(i);
                                }
                            });
                            Toast.makeText(view.getContext(),"Buy "+ title, Toast.LENGTH_SHORT).show();
                        }

                    }else if(mode.equals("inventory")){
                        Toast.makeText(view.getContext(),"Use "+ title+" ! +25exp", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ctx, AvatarActivity.class);
                        i.putExtra("useditem", title);
                        ctx.startActivity(i);
                    }

                }
            });

        }
    }

}
