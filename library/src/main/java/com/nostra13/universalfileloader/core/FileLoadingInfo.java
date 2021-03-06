/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
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
package com.nostra13.universalfileloader.core;

import java.util.concurrent.locks.ReentrantLock;

import com.nostra13.universalfileloader.core.imageaware.ImageAware;
import com.nostra13.universalfileloader.core.listener.ImageLoadingListener;
import com.nostra13.universalfileloader.core.listener.ImageLoadingProgressListener;

/**
 * Information for load'n'display image task
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see com.nostra13.universalfileloader.utils.MemoryCacheUtils
 * @see DisplayFileOptions
 * @see ImageLoadingListener
 * @see com.nostra13.universalfileloader.core.listener.ImageLoadingProgressListener
 * @since 1.3.1
 */
final class FileLoadingInfo {

	final String uri;
	final String memoryCacheKey;
	final ImageAware imageAware;
	final DisplayFileOptions options;
	final ImageLoadingListener listener;
	final ImageLoadingProgressListener progressListener;
	final ReentrantLock loadFromUriLock;

	public FileLoadingInfo(String uri, ImageAware imageAware, String memoryCacheKey,
			DisplayFileOptions options, ImageLoadingListener listener,
			ImageLoadingProgressListener progressListener, ReentrantLock loadFromUriLock) {
		this.uri = uri;
		this.imageAware = imageAware;
		this.options = options;
		this.listener = listener;
		this.progressListener = progressListener;
		this.loadFromUriLock = loadFromUriLock;
		this.memoryCacheKey = memoryCacheKey;
	}
}
