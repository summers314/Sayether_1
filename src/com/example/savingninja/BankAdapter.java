package com.example.savingninja;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BankAdapter extends ArrayAdapter<BankItem>
{
	Context context;
	ArrayList<BankItem> bankItem;
	
	
	public BankAdapter(Context context, ArrayList<BankItem> bankItem) 
	{
		super(context, R.layout.detail_bank_listitem, bankItem);
		
		this.context  = context;
		this.bankItem = bankItem;
	}
	
	@Override
	public boolean isEnabled(int position) 
	{
		return true;
	}
	
	@Override
	public boolean hasStableIds() 
	{
		return true;
	}
	
	@Override
	public int getCount() 
	{
		return bankItem.size();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.detail_bank_listitem,parent, false);
		
		TextView tvAmount   = (TextView) rowView.findViewById(R.id.tvItem_bank_amt);
		TextView tvCategory = (TextView) rowView.findViewById(R.id.tvItem_bank_category);
		TextView tvDetails  = (TextView) rowView.findViewById(R.id.tvItem_bank_details);
		TextView tvDate     = (TextView) rowView.findViewById(R.id.tvItem_bank_date);
		
		tvAmount.setText( bankItem.get(position).getAmount() );
		tvCategory.setText( bankItem.get(position).getCategory() );
		tvDetails.setText( bankItem.get(position).getDetails() );
		tvDate.setText( bankItem.get(position).getDate() );
		
		return rowView;
	}
}
