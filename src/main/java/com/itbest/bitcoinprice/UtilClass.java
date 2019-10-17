package com.itbest.bitcoinprice;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * This class is used for extra utility services such as sorting, initializing the data etc. 
 */

public class UtilClass {

	// this class is created for extra utility activities.

	@SuppressWarnings("unchecked")
	public static HashMap<String, String> loadCurrencyJson() {

		// Initialize an empty HashMap
		HashMap<String, String> countryMap = new HashMap<String, String>();

		// JSONParser is used to load currency json file and to load in an object.
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader("src\\main\\resources\\supportedcurrencies.json"));

			// Object is converted as a JSONArray.
			JSONArray array = (JSONArray) obj;

			Iterator<Object> it = array.listIterator();

			// Used an Iterator to load Currency/Country key value pairs into a MAP.
			while (it.hasNext()) {
				JSONObject obj1 = (JSONObject) it.next();
				countryMap.put(obj1.get("currency").toString().toUpperCase(),
						obj1.get("country").toString().toUpperCase());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return countryMap;

	}

	// The below functions sorts the entire Map based on values

	static Map<String, Double> sortByValue(Map<String, Double> dataMap) {

		// Convert map to list of maps
		List<Map.Entry<String, Double>> listedDataMap = new LinkedList<Map.Entry<String, Double>>(dataMap.entrySet());

		Collections.sort(listedDataMap, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}

		});

		// Loop the sorted list and put it into a new insertion order Map LinkedHashMap
		// LinkedHashMap keeps the insertion order
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> entry : listedDataMap) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

}
