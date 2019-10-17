package com.itbest.bitcoinprice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This Application retrieves Bit coin price from and external API.
 *
 * Author : Syam Voleti
 */

public class App {

	public static void main(String[] args) {
		
		// Supported Currency Format 
		String currencyFileJson = args[0] ; 

		String currencyCode; // Declare a currency code ;

		// The below initializes the Currency / Country pairs
		HashMap<String, String> countryMap = new HashMap<String, String>();

		// Loading all available countries to provide Currency validation
		countryMap = UtilClass.loadCurrencyJson(currencyFileJson);

		// Initialize a scanner to receive inputs
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		String c = ""; // This variable is just used to

		do {
			System.out.print("Please enter currency format to fetch Bitcoin price(For ex., USD, EUR or GBP :  ");

			currencyCode = scanner.next().toUpperCase();

			System.out.println(String.format("You have entered  %s as your preffered currency.", currencyCode));

			if (!countryMap.containsKey(currencyCode)) {
				System.out.println("Unfortunately there is no such currency :( ");
			} else {
				getBitCoinPriceDetails(currencyCode);
			}

			System.out.println("Do you want to Search Bitcoin Price again (Y/N) ?  ");
			c = scanner.next();

		} while (c.equalsIgnoreCase("Y"));

		System.out.println("Thank you for using of our Service. Good Bye :)  ");

	}

	private static void getBitCoinPriceDetails(String currencyCode) {

		// URL for current price
		String currentPriceURL = "https://api.coindesk.com/v1/bpi/currentprice/" + currencyCode + ".json";

		try {
			JSONObject currentPriceJson = new JSONObject(
					IOUtils.toString(new URL(currentPriceURL), Charset.forName("UTF-8")));
			// System.out.println(currentPriceJson);

			// retrieve all key value pairs from bpi element node
			JSONObject bpiJsonObject = (JSONObject) currentPriceJson.get("bpi");

			// now get the currency specific Bitcoin price
			JSONObject currencyNode = (JSONObject) bpiJsonObject.get(currencyCode);

			// Get the extact Bit coin price rate
			Double bcPrice = (Double) currencyNode.get("rate_float");

			System.out.println("Current Price of Bitcoin in \"  " + currencyCode + " \" currency is : \"" + bcPrice
					+ " " + currencyCode + " \"");

		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// URL for Currency specific API which fetches last one month data
		String historicPriceListURL = "https://api.coindesk.com/v1/bpi/historical/close.json?currency=" + currencyCode;

		try {
			JSONObject historcalJsonData = new JSONObject(
					IOUtils.toString(new URL(historicPriceListURL), Charset.forName("UTF-8")));

			// retrieve all key value pairs from bpi element node
			JSONObject test = (JSONObject) historcalJsonData.get("bpi");

			// Declare a Map to store Date/Price key values.
			Map<String, Double> dataMap = new HashMap<String, Double>();

			// Create an Iterator to scan all keys and load them into dataMap.
			Iterator<String> keys = test.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				dataMap.put(key, test.getDouble(key));
			}

			// Now need to sort the data as prices are scattered as per the market value.
			// Created an Extra function in "UtilClass" to sort all price values using
			// "sortByValue" method

			Map<String, Double> sortedDataMap = UtilClass.sortByValue(dataMap);

			// Set is used to retrieve first and last prices values.
			Set<Map.Entry<String, Double>> set = sortedDataMap.entrySet();

			// Find the lenghth of the set
			int len = set.size();

			// Convert set into an Array
			Entry[] mapArray = new Entry[len];
			set.toArray(mapArray);

			// Retrieve lowest and highest prices.

			System.out.println("The Lowest price in last 31 days is recorded on \" " + mapArray[0].getKey()
					+ " \" and the value is : \"" + mapArray[0].getValue() + " " + currencyCode + " \" ");
			System.out.println("The Highest price in last 31 days is recorded on \" " + mapArray[len - 1].getKey()
					+ " \" and the value is : \"" + mapArray[len - 1].getValue() + " " + currencyCode + " \" ");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
