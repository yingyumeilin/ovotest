package com.example.oto01.wheelDialog;

public class StringUtils {
	
	public static String formateDateString(String dateString){
		
		String[] parts = dateString.split("-");
		if(parts.length>2){
			parts[1] = (parts[1].length()==1? "0"+parts[1] : parts[1]);
			parts[2] = (parts[2].length()==1? "0"+parts[2] : parts[2]);
			return (parts[0] + "-" +parts[1] + "-" + parts[2]);
		}else{
			parts[0] = (parts[0].length()==1? "0"+parts[0] : parts[0]);
			parts[1] = (parts[1].length()==1? "0"+parts[1] : parts[1]);
			return (parts[0] + "-" +parts[1]);
		}
	}
}
