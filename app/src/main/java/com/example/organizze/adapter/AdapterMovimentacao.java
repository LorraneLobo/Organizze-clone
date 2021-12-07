package com.example.organizze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.organizze.R;
import com.example.organizze.model.Movimentacao;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jamilton Damasceno
 */

public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

    List<Movimentacao> movimentacoes;
    Context context;

    public AdapterMovimentacao(List<Movimentacao> movimentacoes, Context context) {
        this.movimentacoes = movimentacoes;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movimentacao movimentacao = movimentacoes.get(position);

        boolean isDespesa = movimentacao.getTipo().equals("d");

        holder.titulo.setText(movimentacao.getDescricao());

        double valorMovimentacao = movimentacao.getValor();
        if (isDespesa)  {
            valorMovimentacao = valorMovimentacao * -1;
        }

        holder.valor.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorMovimentacao));
        holder.categoria.setText(movimentacao.getCategoria());

        int cor = R.color.colorAccentReceita;
        if (isDespesa) {
            cor = R.color.colorAccentDespesa;
        }

        holder.valor.setTextColor(context.getResources().getColor(cor));

    }


    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, valor, categoria;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            valor = itemView.findViewById(R.id.textAdapterValor);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
        }

    }

}
