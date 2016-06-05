/*******************************************************************************
 * Copyright 2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalfileloader.core.decode;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory.Options;
import android.os.Build;

import com.nostra13.universalfileloader.core.DisplayFileOptions;
import com.nostra13.universalfileloader.core.download.ImageDownloader;

/**
 * Contains needed information for decoding image to Bitmap
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.3
 */
public class ImageDecodingInfo {

	private final String imageKey;
	private final String imageUri;

	private final ImageDownloader downloader;
	private final Object extraForDownloader;

	private final boolean considerExifParams;
	private final Options decodingOptions;

	public ImageDecodingInfo(String imageKey, String imageUri,
							 ImageDownloader downloader, DisplayFileOptions displayOptions) {
		this.imageKey = imageKey;
		this.imageUri = imageUri;

		this.downloader = downloader;
		this.extraForDownloader = displayOptions.getExtraForDownloader();

		considerExifParams = displayOptions.isConsiderExifParams();
		decodingOptions = new Options();
		copyOptions(displayOptions.getDecodingOptions(), decodingOptions);
	}

	private void copyOptions(Options srcOptions, Options destOptions) {
		destOptions.inDensity = srcOptions.inDensity;
		destOptions.inDither = srcOptions.inDither;
		destOptions.inInputShareable = srcOptions.inInputShareable;
		destOptions.inJustDecodeBounds = srcOptions.inJustDecodeBounds;
		destOptions.inPreferredConfig = srcOptions.inPreferredConfig;
		destOptions.inPurgeable = srcOptions.inPurgeable;
		destOptions.inSampleSize = srcOptions.inSampleSize;
		destOptions.inScaled = srcOptions.inScaled;
		destOptions.inScreenDensity = srcOptions.inScreenDensity;
		destOptions.inTargetDensity = srcOptions.inTargetDensity;
		destOptions.inTempStorage = srcOptions.inTempStorage;
		if (Build.VERSION.SDK_INT >= 10) copyOptions10(srcOptions, destOptions);
		if (Build.VERSION.SDK_INT >= 11) copyOptions11(srcOptions, destOptions);
	}

	@TargetApi(10)
	private void copyOptions10(Options srcOptions, Options destOptions) {
		destOptions.inPreferQualityOverSpeed = srcOptions.inPreferQualityOverSpeed;
	}

	@TargetApi(11)
	private void copyOptions11(Options srcOptions, Options destOptions) {
		destOptions.inBitmap = srcOptions.inBitmap;
		destOptions.inMutable = srcOptions.inMutable;
	}

	/** @return Original {@linkplain com.nostra13.universalfileloader.utils.MemoryCacheUtils#generateKey(String, ImageSize) image key} (used in memory cache). */
	public String getImageKey() {
		return imageKey;
	}

	/** @return Image URI for decoding (usually image from disc cache) */
	public String getImageUri() {
		return imageUri;
	}

	/** @return Downloader for image loading */
	public ImageDownloader getDownloader() {
		return downloader;
	}

	/** @return Auxiliary object for downloader */
	public Object getExtraForDownloader() {
		return extraForDownloader;
	}

	/** @return <b>true</b> - if EXIF params of image should be considered; <b>false</b> - otherwise */
	public boolean shouldConsiderExifParams() {
		return considerExifParams;
	}

	/** @return Decoding options */
	public Options getDecodingOptions() {
		return decodingOptions;
	}
}