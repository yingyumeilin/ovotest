package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.example.oto01.model.PersonDto;

public class GetNumber {
	public static List<PersonDto> lists = new ArrayList<PersonDto>();

	public static String getNumber(Context context) {
		Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI,
				null, null, null, null);
		String phoneNumber;
		String phoneName;
		while (cursor.moveToNext()) {
			phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));// 电话号码
			phoneName = cursor.getString(cursor
					.getColumnIndex(Phone.DISPLAY_NAME));// 姓名

			PersonDto info = new PersonDto();

			String phoneNumber1 = phoneNumber.replaceAll(" ", "");

			if (phoneNumber1.contains("+")) {
				if (phoneNumber1.startsWith("+86")) {
					info.setPhone(phoneNumber1.substring(3).toString().trim());
				} else {
					info.setPhone(phoneNumber1.substring(1).toString().trim());
				}
			} else {
				info.setPhone(phoneNumber1);
			}
			info.setName(phoneName);

			lists.add(info);
			System.out.println(phoneName + phoneNumber1);
		}
		return null;
	}
}