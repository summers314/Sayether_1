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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class BankDetail extends Activity implements OnItemClickListener
{
	ListView lv1;
	int count;
	ArrayList<BankItem> bankItem = new ArrayList<BankItem>();
	BankAdapter adapter;
	
	//DialogBank
	AlertDialog alertDialog1;
	Spinner sp_bank_categories;
	EditText et_bank_newCategory, et_bank_amount, et_bank_details;
	ArrayList<String> list_bank_category = new ArrayList<String>();
	
	//SharedPreferences
	public static final String filename="Budget";
	public static SharedPreferences stats;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_bank);
		
		stats = this.getSharedPreferences(filename, 0);
		count = stats.getInt("countBank", 0);
		
		generateData();
		
		lv1 = (ListView) findViewById(R.id.lvBank);
		adapter = new BankAdapter(this, bankItem);
		lv1.setAdapter(adapter);
		lv1.setOnItemClickListener(this);
	}
	
	public void generateData()
	{
		String category, amount, details, date;
		
		for(int i=0;i<count;i++)
		{
			category = stats.getString("categoryBank"+i, "CATEGORY");
			amount   = stats.getString("amountBank"+i, "AMOUNT");
			details  = stats.getString("detailsBank"+i, "DETAILS");
			date     = stats.getString("timeBank"+i, "DATE");
			
			bankItem.add( new BankItem(category, amount, details, date) );
		}
	}
	
	public void createDialogBank(final int position)
	{
		LayoutInflater li = LayoutInflater.from(this);
		View bankView = li.inflate(R.layout.dialog_bank, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setView(bankView);

		// Set dialog message
		alertDialogBuilder.setCancelable(true);
		
		alertDialogBuilder.setNegativeButton("DEPOSITE", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultBank(0, position);
			}
		});
		
		alertDialogBuilder.setPositiveButton("WITHDRAW", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultBank(1, position);
			}
		});
		
		alertDialogBuilder.setNeutralButton("SWIPE", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultBank(2, position);
			}
		});
		
		// Adding values to Spinner-List
		list_bank_category.clear();
		list_bank_category.add("Travel");
		list_bank_category.add("Food");
		list_bank_category.add("Stationary");
		list_bank_category.add("Friend");
		
		// Initialize DialogChoice elements
		sp_bank_categories  = (Spinner)  bankView.findViewById(R.id.sp_bank_categories);
		et_bank_newCategory = (EditText) bankView.findViewById(R.id.et_bank_newcategory);
		et_bank_amount      = (EditText) bankView.findViewById(R.id.et_bank_amt);
		et_bank_details     = (EditText) bankView.findViewById(R.id.et_bank_details);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, list_bank_category);
		sp_bank_categories.setAdapter(adapter);
		
		// Set initial values
		et_bank_newCategory.setText(bankItem.get(position).getCategory());
		et_bank_amount.setText(bankItem.get(position).getAmount());
		et_bank_details.setText(bankItem.get(position).getDetails());
		
		// Create alert dialog
		alertDialog1 = alertDialogBuilder.create();
		
		// Show it
		alertDialog1.show(); 
	
	}
	
	public void dialogResultBank(int type, int position)
	{
		String s_category = null;
		double amt_original, amt_spent = 0, amt_left=0;
		double amt_before_edit, amt_after_edit;
		
		amt_original = Double.parseDouble( stats.getString("amtBank", "100") );
		
		amt_before_edit = Double.parseDouble( bankItem.get(position).getAmount() );
		amt_after_edit  = Double.parseDouble( et_bank_amount.getText().toString() );
		
		SharedPreferences.Editor editor = stats.edit();
		
		//0 = DEPOSITE
		//1 = WITHDRAW
		//2 = SWIPE
		switch(type)
		{
		case 0:
			s_category = "DEPOSITE";
			amt_spent = amt_after_edit - amt_before_edit;
			amt_left = amt_original + amt_spent;
			editor.putString("categoryBank" +position, "DEPOSITE" );
			break;
			
		case 1:
			s_category = "WITHDRAW";
			amt_spent = amt_before_edit - amt_after_edit;
			amt_left = amt_original + amt_spent;
			editor.putString("categoryBank" +position, "WITHDRAW" );
			break;
			
		case 2:
			s_category = "SWIPE";
			amt_spent = amt_before_edit - amt_after_edit;
			amt_left = amt_original + amt_spent;
			editor.putString("categoryBank" +position, "SWIPE" );
			break;
		}
		
		editor.putString("amountBank"   +position, et_bank_amount.getText().toString() );
		editor.putString("detailsBank"  +position, et_bank_details.getText().toString() );
		
		editor.putString("amtBank", Double.toString(amt_left) );
		
		editor.commit();
		
		bankItem.set(position, new BankItem(s_category, et_bank_amount.getText().toString(), et_bank_details.getText().toString(), bankItem.get(position).getDate()));
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
	{
		createDialogBank(position);
	}
	
	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent(BankDetail.this, MainActivity.class);
        startActivity(intent);
        finish();
	}
}
