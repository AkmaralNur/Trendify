package com.example.shoppingmallsystem.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingmallsystem.R;
import com.example.shoppingmallsystem.bean.StoreBean;
import java.util.List;

public class AllStoresAdapter extends RecyclerView.Adapter<AllStoresAdapter.ViewHolder> {
    // Устанавливаем источник данных
    private LayoutInflater inflater;
    private List<StoreBean> storeBeans;
    private OnItemClickListener onItemClickListener;

    // Устанавливаем слушатель кликов
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Конструктор адаптера
    public AllStoresAdapter(List<StoreBean> storeBeans) {
        this.storeBeans = storeBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.store_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Устанавливаем картинку магазина в зависимости от его ID
        switch (storeBeans.get(position).getIv_store_pic()) {
            case "0":
                holder.iv_store_pic.setImageResource(R.mipmap.store_1);
                break;
            case "1":
                holder.iv_store_pic.setImageResource(R.mipmap.store_2);
                break;
            case "2":
                holder.iv_store_pic.setImageResource(R.mipmap.store_3);
                break;
            case "3":
                holder.iv_store_pic.setImageResource(R.mipmap.store_4);
                break;
            case "4":
                holder.iv_store_pic.setImageResource(R.mipmap.store_5);
                break;
            case "5":
                holder.iv_store_pic.setImageResource(R.mipmap.store_6);
                break;
            case "6":
                holder.iv_store_pic.setImageResource(R.mipmap.store_7);
                break;
            case "7":
                holder.iv_store_pic.setImageResource(R.mipmap.store_8);
                break;
        }

        // Устанавливаем текстовые данные магазина
        holder.tv_storeName.setText(storeBeans.get(position).getStoreName());
        holder.tv_storeScore.setText(storeBeans.get(position).getStoreScore() + " баллов");
        holder.tv_storeSell.setText(storeBeans.get(position).getStoreSell() + " продаж");
        holder.tv_storeSign.setText(storeBeans.get(position).getStoreSign());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return storeBeans.size();
    }

    // Класс для хранения элементов представления
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_store_pic;
        private TextView tv_storeName;
        private TextView tv_storeScore;
        private TextView tv_storeSell;
        private TextView tv_storeSign;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Привязка элементов управления
            iv_store_pic = itemView.findViewById(R.id.iv_store_pic);
            tv_storeName = itemView.findViewById(R.id.tv_storeName);
            tv_storeScore = itemView.findViewById(R.id.tv_store_score);
            tv_storeSell = itemView.findViewById(R.id.tv_store_sell);
            tv_storeSign = itemView.findViewById(R.id.tv_store_sign);

            // Обработка кликов по элементам
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(view, getLayoutPosition(), storeBeans.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    // Интерфейс для обработки кликов по элементам
    public interface OnItemClickListener {
        void onClick(View v, int position, StoreBean storeBean);
    }
}
