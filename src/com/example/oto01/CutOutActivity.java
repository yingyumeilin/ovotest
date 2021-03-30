package com.example.oto01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CutOutActivity extends BaseActivity {

	private com.example.oto01.imgcrop.CropImageView cropImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cut_out);
		TextView tv_ok = (TextView) findViewById(R.id.tv_ok);
		TextView tv_cancle = (TextView) findViewById(R.id.tv_cancle);
		cropImageView = (com.example.oto01.imgcrop.CropImageView) findViewById(R.id.CropImageView);
		Uri uri = getIntent().getData();
		Bitmap bitmap;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(
					this.getContentResolver(), uri);
			cropImageView.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		tv_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bitmap croppedImage = cropImageView.getCroppedImage();
				Intent intent = new Intent();
				intent.putExtra("data", croppedImage);
				setResult(RESULT_OK, intent);
				finish();
				// CropImageView.setImageBitmap(croppedImage);
			}
		});

	}

}
