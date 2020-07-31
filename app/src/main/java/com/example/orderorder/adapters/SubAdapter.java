package com.example.orderorder.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderorder.R;
import com.example.orderorder.models.mainData.Sub;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class SubAdapter extends FirestoreRecyclerAdapter<Sub, SubAdapter.SubHolder> {
    private OnSubItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SubAdapter(@NonNull FirestoreRecyclerOptions<Sub> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SubHolder holder, int position, @NonNull Sub model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewPrice.setText(model.convertFloatToString(model.getPrice()) + model.getCurrency());
        holder.textViewDueDate.setText(model.convertDataToString(model.getBillingDueDate()) );
    }

    @NonNull
    @Override
    public SubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item,
                parent, false);
        return new SubHolder(v);
    }

    class SubHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewPrice;
        TextView textViewDueDate;

        public SubHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            textViewDueDate = itemView.findViewById(R.id.text_view_due_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }

    public interface OnSubItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

    }

    public void setOnSubItemClickListener(OnSubItemClickListener listener) {
        this.listener = listener;
    }

}
