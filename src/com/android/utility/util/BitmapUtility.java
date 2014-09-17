/*
 * Copyright (C) 2014 Minhaz Rafi Chowdhury.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.android.utility.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;

public class BitmapUtility {
	/**
	 * Convert bitmap to base 64 string
	 * @param bitmap to parse as base 64
	 * @return
	 */
	public static String toBase64(Bitmap bitmap) {
		if (bitmap == null)
		{
			throw new IllegalArgumentException();
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		try
		{
			byteArrayOutputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return Base64.encodeToString(byteArray, Base64.DEFAULT);
	}
	/**
	 * Convert Bitmap to an Drawable object
	 * @param context Application context
	 * @param bitmap Bitmap to convert
	 * @return
	 */
	public static Drawable toDrawable(Context context, Bitmap bitmap) {
		if (bitmap == null)
		{
			throw new IllegalArgumentException();
		}
		return new BitmapDrawable(context.getResources(), bitmap);
	}

	public static Bitmap toBitmap(Drawable drawable) {
		if (drawable == null)
		{
			throw new IllegalArgumentException();
		}
		if (drawable instanceof BitmapDrawable)
		{
			return ((BitmapDrawable) drawable).getBitmap();
		}

		int width = drawable.getIntrinsicWidth();
		width = width > 0 ? width : 1;
		int height = drawable.getIntrinsicHeight();
		height = height > 0 ? height : 1;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap toBitmap(Context context, int resource) {
		if ((context == null) || (resource == 0))
		{
			throw new IllegalArgumentException();
		}
		return BitmapFactory.decodeResource(context.getResources(), resource);
	}

	/**
	 * Convert a base 64 encoded Bitmap String to Bitmap object
	 * @param bitmap as bas 64 encoded
	 * @return Bitmap object
	 */
	public final static Bitmap Bitmap(String bitmap) {
		if (TextUtils.isEmpty(bitmap))
		{
			throw new IllegalArgumentException();
		}
		byte[] bytes = Base64.decode(bitmap, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

}
