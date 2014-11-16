package com.example.savingninja;

public class PocketItem 
{
	
	String category;
	String amount;
	String details;
	String date;
	
	public PocketItem(String category, String amount, String details, String date) 
	{
		this.category = category;
		this.amount   = amount;
		this.details  = details;
		this.date     = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
