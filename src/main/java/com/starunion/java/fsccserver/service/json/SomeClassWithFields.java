package com.starunion.java.fsccserver.service.json;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @author Lings
 * @date Mar 25, 2016 4:51:37 PM
 * 
 */
public class SomeClassWithFields {
	@SerializedName("name")
	private final String someField;
	private final String someOtherField;

	public SomeClassWithFields(String a, String b) {
		this.someField = a;
		this.someOtherField = b;
	}

	// The following shows the output that is generated when serializing an
	// instance of the above example class:

	public static void main(String[] args){
		SomeClassWithFields objectToSerialize = new SomeClassWithFields("a", "b");
		Gson gson = new Gson();
		String jsonRepresentation = gson.toJson(objectToSerialize);
		System.out.println(jsonRepresentation);

//		=====OUTPUT=====
//
//		{"name":"a","someOtherField":"b"}
	}
}
	

