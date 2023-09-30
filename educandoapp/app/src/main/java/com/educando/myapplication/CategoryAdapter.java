package com.educando.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    ArrayList<CategoryDomain> categories;
    Context context;

    public CategoryAdapter(ArrayList<CategoryDomain> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        context = parent.getContext();
        return new CategoryAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title_categoryTxt.setText(categories.get(position).getCategoryName());
        int drawableResourceId = holder.itemView.getResources().getIdentifier(categories.get(position).getImageResourceId(),
                "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_categoryTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_categoryTxt = itemView.findViewById(R.id.title_categoryTxt);
            pic = itemView.findViewById(R.id.pic);

            // Configura un OnClickListener para el elemento en el constructor del ViewHolder
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtén la categoría seleccionada
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CategoryDomain selectedCategory = categories.get(position);

                        // Inicia la nueva actividad y pasa la categoría seleccionada como parámetro
                        Intent intent = new Intent(context, CategoryCourseActivity.class);
                        intent.putExtra("categoryName", selectedCategory.getCategoryName());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}