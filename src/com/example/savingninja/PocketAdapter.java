package com.example.savingninja;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PocketAdapter extends ArrayAdapter<PocketItem>
{
	Context context;
	ArrayList<PocketItem> pocketItem;
	
	
	public PocketAdapter(Context context, ArrayList<PocketItem> pocketItem) 
	{
		super(context, R.layout.detail_pocket_listitem, pocketItem);
		
		this.context = context;
		this.pocketItem = pocketItem;
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
		return pocketItem.size();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.detail_pocket_listitem,parent, false);
		
		TextView tvAmount   = (TextView) rowView.findViewById(R.id.tvItem_pocket_amt);
		TextView tvCategory = (TextView) rowView.findViewById(R.id.tvItem_pocket_category);
		TextView tvDetails  = (TextView) rowView.findViewById(R.id.tvItem_pocket_details);
		TextView tvDate     = (TextView) rowView.findViewById(R.id.tvItem_pocket_date);
		
		tvAmount.setText( pocketItem.get(position).getAmount() );
		tvCategory.setText( pocketItem.get(position).getCategory() );
		tvDetails.setText( pocketItem.get(position).getDetails() );
		tvDate.setText( pocketItem.get(position).getDate() );
		
		return rowView;
	}
	


}
