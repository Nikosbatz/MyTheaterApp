package com.example.mytheaterapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private ArrayList<Ticket> ticketList;

    public TicketAdapter(ArrayList<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.performanceTextView.setText(ticket.getPerformance());
        holder.seatsTextView.setText(ticket.getSeats().toString().replaceAll("[\\[\\],]", ""));
        holder.idTextView.setText(ticket.getId());
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }


    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView performanceTextView, idTextView, seatsTextView;

        public TicketViewHolder(View itemView) {
            super(itemView);
            performanceTextView = itemView.findViewById(R.id.ticket_performance);
            idTextView = itemView.findViewById(R.id.ticket_id);
            seatsTextView = itemView.findViewById(R.id.ticket_seats);
        }
    }
}