package com.example.savingninja;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnLongClickListener
{

	TextView tvBank,tvPocket;
	TextView tvTitleBank, tvTitlePocket;
	AlertDialog alertDialog1;
	int count_bank,count_pocket;
	
	//DialogBank
	Spinner sp_bank_categories;
	EditText et_bank_newCategory, et_bank_amount, et_bank_details;
	ArrayList<String> list_bank_category = new ArrayList<String>();
	
	//DialogPocket
	Spinner sp_categories;
	EditText et_newCategory, et_amount, et_details;
	ArrayList<String> list_category = new ArrayList<String>();
	
	//DialogEditAmount
	EditText et_newAmount;
	
	//SharedPreferences
	public static final String filename="Budget";
	public static SharedPreferences stats;
	
	//Date
	String date;
	Calendar cal;
	SimpleDateFormat fmt;
	
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//alertDialog1 = new AlertDialog(this);
		
		tvBank   = (TextView) findViewById(R.id.tv_bank_amt);
		tvPocket = (TextView) findViewById(R.id.tv_pocket_amt);
		
		tvTitleBank   = (TextView) findViewById(R.id.textView1);
		tvTitlePocket = (TextView) findViewById(R.id.textView2);
		
		tvTitleBank.setOnClickListener(this);
		tvTitlePocket.setOnClickListener(this);
		
		tvBank.setOnClickListener(this);
		tvPocket.setOnClickListener(this);
		
		tvBank.setOnLongClickListener(this);
		tvPocket.setOnLongClickListener(this);
		
		stats= this.getSharedPreferences(filename, 0);
		
		String s_bank   = stats.getString("amtBank",   "100");
		String s_pocket = stats.getString("amtPocket", "10");
		
		count_bank   = stats.getInt("countBank", 0);
		count_pocket = stats.getInt("countPocket", 0);
		
		tvBank.setText(s_bank);
		tvPocket.setText(s_pocket);
		
		cal = Calendar.getInstance();
		
		fmt = new SimpleDateFormat("MMM");
		String month_name = fmt.format(cal.getTime());
		
		fmt = new SimpleDateFormat("dd");
		String date_no = fmt.format(cal.getTime());
		
		date = date_no + " " + month_name;
		
	}
	
	public void createDialogBank()
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
				dialogResultBank(0);
			}
		});
		
		alertDialogBuilder.setPositiveButton("WITHDRAW", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultBank(1);
			}
		});
		
		alertDialogBuilder.setNeutralButton("SWIPE", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultBank(2);
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
		
		// Create alert dialog
		alertDialog1 = alertDialogBuilder.create();
		
		// Show it
		alertDialog1.show(); 
	
	}
	
	public void dialogResultBank(int type)
	{

		int position = sp_bank_categories.getSelectedItemPosition();
		Log.i("DAKSH", Integer.toString(position) ) ;
		Log.i("DAKSH", list_bank_category.get(position));
		Log.i("DAKSH", et_bank_newCategory.getText().toString() );
		Log.i("DAKSH", et_bank_amount.getText().toString() );
		Log.i("DAKSH", et_bank_details.getText().toString() );
		
		double amt_original, amt_spent, amt_left=0;
		double amt_new_pocket;
		
		amt_original = Double.parseDouble( tvBank.getText().toString()  );
		amt_spent    = Double.parseDouble( et_bank_amount.getText().toString() );
		
		SharedPreferences.Editor editor = stats.edit();
		
		//0 = DEPOSITE
		//1 = WITHDRAW
		//2 = SWIPE
		switch(type)
		{
		case 0:
			amt_left = amt_original + amt_spent;
			editor.putString("categoryBank" +count_bank, "DEPOSITE" );
			break;
			
		case 1:
			amt_left = amt_original - amt_spent;
			editor.putString("categoryBank" +count_bank, "WITHDRAW" );
			
			amt_new_pocket = Double.parseDouble(tvPocket.getText().toString()) + amt_spent;
			tvPocket.setText( Double.toString(amt_new_pocket) );
			
			editor.putString("categoryPocket" +count_pocket, "ADDED AMOUNT" );
			editor.putString("amountPocket"   +count_pocket, Double.toString(amt_spent) );
			editor.putString("detailsPocket"  +count_pocket, "Withdrew money from bank and added to Pocket" );
			editor.putString("timePocket"     +count_pocket, date );
			
			editor.putString("amtPocket", Double.toString(amt_new_pocket) );
			
			count_pocket +=1;
			editor.putInt("countPocket", count_pocket);
			
			break;
			
		case 2:
			amt_left = amt_original - amt_spent;
			editor.putString("categoryBank" +count_bank, "SWIPE" );
			break;
		}
		
		//editor.putString("categoryBank" +count_bank, list_bank_category.get(position) );
		editor.putString("amountBank"   +count_bank, et_bank_amount.getText().toString() );
		editor.putString("detailsBank"  +count_bank, et_bank_details.getText().toString() );
		editor.putString("timeBank"     +count_bank, date );
		
		editor.putString("amtBank", Double.toString(amt_left) );
		
		count_bank +=1;
		editor.putInt("countBank", count_bank);
		
		editor.commit();
		
		tvBank.setText( Double.toString(amt_left) );
	
	}
	
	
	public void createDialogPocket()
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
				dialogResultPocket(0);
			}
		});
		
		alertDialogBuilder.setPositiveButton("SPENT", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultPocket(1);
			}
		});
		
		alertDialogBuilder.setNeutralButton("ADD", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialogResultPocket(2);
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
		
		// Create alert dialog
		alertDialog1 = alertDialogBuilder.create();
		
		// Show it
		alertDialog1.show(); 
	}
	
	public void dialogResultPocket(int type)
	{
		int position = sp_categories.getSelectedItemPosition();
		Log.i("DAKSH", Integer.toString(position) ) ;
		Log.i("DAKSH", list_category.get(position));
		Log.i("DAKSH", et_newCategory.getText().toString() );
		Log.i("DAKSH", et_amount.getText().toString() );
		Log.i("DAKSH", et_details.getText().toString() );
		
		double amt_original, amt_spent, amt_left=0;
		
		amt_original = Double.parseDouble( tvPocket.getText().toString()  );
		amt_spent    = Double.parseDouble( et_amount.getText().toString() );
		
		SharedPreferences.Editor editor = stats.edit();
		
		//0 = OWE
		//1 = SPENT
		//2 = ADD
		switch(type)
		{
		case 0:
			amt_left = amt_original;
			break;
			
		case 1:
			amt_left = amt_original - amt_spent;
			break;
			
		case 2: 
			amt_left = amt_original + amt_spent;
		}
		
		editor.putString("categoryPocket" +count_pocket, list_category.get(position) );
		editor.putString("amountPocket"   +count_pocket, et_amount.getText().toString() );
		editor.putString("detailsPocket"  +count_pocket, et_details.getText().toString() );
		editor.putString("timePocket"     +count_pocket, date );
		
		editor.putString("amtPocket", Double.toString(amt_left) );
		
		count_pocket +=1;
		editor.putInt("countPocket", count_pocket);
		
		editor.commit();
		
		tvPocket.setText( Double.toString(amt_left) );
	}
	
	
	public void dialogEditBank()
	{
		LayoutInflater li = LayoutInflater.from(this);
		View editView = li.inflate(R.layout.dialog_edit_amount, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setView(editView);

		// Set dialog message
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setNegativeButton("CANCEL", null);
		
		alertDialogBuilder.setPositiveButton("SET", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				String amt = et_newAmount.getText().toString();
				tvBank.setText( amt );
				
				SharedPreferences.Editor editor = stats.edit();
				editor.putString("amtBank", amt);
				editor.commit();
			}
		});
		
		
		// Initialize DialogChoice elements
		et_newAmount = (EditText) editView.findViewById(R.id.et_newAmount);
		
		// Create alert dialog
		alertDialog1 = alertDialogBuilder.create();
		
		// Show it
		alertDialog1.show(); 
	}
	
	public void dialogEditPocket()
	{

		LayoutInflater li = LayoutInflater.from(this);
		View editView = li.inflate(R.layout.dialog_edit_amount, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setView(editView);

		// Set dialog message
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setNegativeButton("CANCEL", null);
		
		alertDialogBuilder.setPositiveButton("SET", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				String amt = et_newAmount.getText().toString();
				tvPocket.setText( amt );
				
				SharedPreferences.Editor editor = stats.edit();
				editor.putString("amtPocket", amt);
				editor.commit();
			}
		});
		
		
		// Initialize DialogChoice elements
		et_newAmount = (EditText) editView.findViewById(R.id.et_newAmount);
		
		// Create alert dialog
		alertDialog1 = alertDialogBuilder.create();
		
		// Show it
		alertDialog1.show(); 
	
	}

	
	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.tv_bank_amt:
			createDialogBank();
			break;
			
		case R.id.tv_pocket_amt:
			createDialogPocket();
			break;
			
		case R.id.textView1:
			//Bank
			Intent intentB = new Intent(MainActivity.this, BankDetail.class);
            startActivity(intentB);
            finish();
			break;
			
		case R.id.textView2:
			//Pocket
			Intent intentP = new Intent(MainActivity.this, PocketDetail.class);
            startActivity(intentP);
            finish();
			break;
		}
	}
	
	@Override
	public boolean onLongClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.tv_bank_amt:
			dialogEditBank();
			break;
			
		case R.id.tv_pocket_amt:
			dialogEditPocket();
			break;
		}
		
		return false;
	}
	
}
