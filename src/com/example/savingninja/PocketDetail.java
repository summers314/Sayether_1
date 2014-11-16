package com.example.savingninja;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PocketDetail extends Activity implements OnItemClickListener
{
	ListView lv1;
	int count;
	ArrayList<PocketItem> pocketItem = new ArrayList<PocketItem>();
	PocketAdapter adapter;
	
	//DialogPocket
	AlertDialog alertDialog1;
	Spinner sp_categories;
	EditText et_newCategory, et_amount, et_details;
	ArrayList<String> list_category = new ArrayList<String>();
	
	//SharedPreferences
	public static final String filename="Budget";
	public static SharedPreferences stats;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_pocket);
		
		stats = this.getSharedPreferences(filename, 0);
		count = stats.getInt("countPocket", 0);
		
		generateData();
		
		lv1 = (ListView) findViewById(R.id.lvPocket);
		adapter = new PocketAdapter(this, pocketItem);
		lv1.setAdapter(adapter);
		lv1.setOnItemClickListener(this);
	}
	
	public void generateData()
	{
		String category, amount, details, date;
		
		for(int i=0;i<count;i++)
		{
			category = stats.getString("categoryPocket"+i, "CATEGORY");
			amount   = stats.getString("amountPocket"+i, "AMOUNT");
			details  = stats.getString("detailsPocket"+i, "DETAILS");
			date     = stats.getString("timePocket"+i, "DATE");
			
			pocketItem.add( new PocketItem(category, amount, details, date) );
		}
	}
	
	public void createDialogPocket(final int position)
	{
		LayoutInflater li = LayoutInflater.from(this);
		View pocketView = li.inflate(R.layout.dialog_pocket, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setView(pocketView);

		// Set dialog message
		alertDialogBuilder.setCancelable(true);
		
		alertDialogBuilder.setNegativeButton("OWE", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultPocket(0,position);
			}
		});
		
		alertDialogBuilder.setPositiveButton("SPENT", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultPocket(1,position);
			}
		});
		
		// Adding values to Spinner-List
		list_category.clear();
		list_category.add("Travel");
		list_category.add("Food");
		list_category.add("Stationary");
		list_category.add("Friend");
		
		// Initialize DialogChoice elements
		sp_categories  = (Spinner)  pocketView.findViewById(R.id.sp_pocket_categories);
		et_newCategory = (EditText) pocketView.findViewById(R.id.et_pocket_newcategory);
		et_amount      = (EditText) pocketView.findViewById(R.id.et_pocket_amt);
		et_details     = (EditText) pocketView.findViewById(R.id.et_pocket_details);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, list_category);
		sp_categories.setAdapter(adapter);
		
		// Set initial values
		et_newCategory.setText(pocketItem.get(position).getCategory());
		et_amount.setText(pocketItem.get(position).getAmount());
		et_details.setText(pocketItem.get(position).getDetails());
		
		// Create alert dialog
		alertDialog1 = alertDialogBuilder.create();
		
		// Show it
		alertDialog1.show(); 
	}
	
	public void dialogResultPocket(int type, int position)
	{
		double amt_original, amt_spent, amt_left=0;
		double amt_before_edit, amt_after_edit;
		
		amt_original = Double.parseDouble( stats.getString("amtPocket", "10") );
		
		amt_before_edit = Double.parseDouble( pocketItem.get(position).getAmount() );
		amt_after_edit  = Double.parseDouble( et_amount.getText().toString() );
		
		SharedPreferences.Editor editor = stats.edit();
		
		//0 = OWE
		//1 = SPENT
		switch(type)
		{
		case 0:
			amt_left = amt_original;
			break;
			
		case 1:
			amt_spent = amt_before_edit - amt_after_edit;
			amt_left  = amt_original + amt_spent;
			break;
		}
		
		editor.putString("categoryPocket" +position, list_category.get(position) );
		editor.putString("amountPocket"   +position, et_amount.getText().toString() );
		editor.putString("detailsPocket"  +position, et_details.getText().toString() );
		editor.putString("timePocket"     +position, "" );
		
		editor.putString("amtPocket", Double.toString(amt_left) );
		
		editor.commit();
		
		pocketItem.set(position, new PocketItem(list_category.get(position), et_amount.getText().toString(), et_details.getText().toString(), pocketItem.get(position).getDate()));
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
	{
		createDialogPocket(position);
	}
	
	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent(PocketDetail.this, MainActivity.class);
        startActivity(intent);
        finish();
	}
}
