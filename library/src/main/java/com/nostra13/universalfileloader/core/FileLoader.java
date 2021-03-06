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

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalfileloader.cache.disc.DiscCacheAware;
import com.nostra13.universalfileloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalfileloader.core.assist.FailReason;
import com.nostra13.universalfileloader.core.assist.FlushedInputStream;
import com.nostra13.universalfileloader.core.assist.LoadedFrom;
import com.nostra13.universalfileloader.core.imageaware.ImageAware;
import com.nostra13.universalfileloader.core.imageaware.ImageNonViewAware;
import com.nostra13.universalfileloader.core.listener.ImageLoadingListener;
import com.nostra13.universalfileloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalfileloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalfileloader.core.listener.SyncImageLoadingListener;
import com.nostra13.universalfileloader.utils.L;
import com.nostra13.universalfileloader.utils.MemoryCacheUtils;

/**
 * Singletone for image loading and displaying at {@link ImageView ImageViews}<br />
 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before any other method.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public class FileLoader {

	public static final String TAG = FileLoader.class.getSimpleName();

	static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";
	static final String LOG_DESTROY = "Destroy ImageLoader";
	static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";

	private static final String WARNING_RE_INIT_CONFIG = "Try to initialize ImageLoader which had already been initialized before. " + "To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.";
	private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference must not be null)";
	private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
	private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";

	private FileLoaderConfiguration configuration;
	private FileLoaderEngine engine;

	private final ImageLoadingListener emptyListener = new SimpleImageLoadingListener();

	private volatile static FileLoader instance;

	/** Returns singleton class instance */
	public static FileLoader getInstance() {
		if (instance == null) {
			synchronized (FileLoader.class) {
				if (instance == null) {
					instance = new FileLoader();
				}
			}
		}
		return instance;
	}

	protected FileLoader() {
	}

	/**
	 * Initializes ImageLoader instance with configuration.<br />
	 * If configurations was set before ( {@link #isInited()} == true) then this method does nothing.<br />
	 * To force initialization with new configuration you should {@linkplain #destroy() destroy ImageLoader} at first.
	 *
	 * @param configuration {@linkplain FileLoaderConfiguration ImageLoader configuration}
	 * @throws IllegalArgumentException if <b>configuration</b> parameter is null
	 */
	public synchronized void init(FileLoaderConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
		}
		if (this.configuration == null) {
			if (configuration.writeLogs) L.d(LOG_INIT_CONFIG);
			engine = new FileLoaderEngine(configuration);
			this.configuration = configuration;
		} else {
			L.w(WARNING_RE_INIT_CONFIG);
		}
	}

	/**
	 * Returns <b>true</b> - if ImageLoader {@linkplain #init(FileLoaderConfiguration) is initialized with
	 * configuration}; <b>false</b> - otherwise
	 */
	public boolean isInited() {
		return configuration != null;
	}

	/**
	 * Adds display image task to execution pool. Image will be set to ImageAware when it's turn. <br/>
	 * Default {@linkplain DisplayFileOptions display image options} from {@linkplain FileLoaderConfiguration
	 * configuration} will be used.<br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri        Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageAware {@linkplain com.nostra13.universalfileloader.core.imageaware.ImageAware Image aware view}
	 *                   which should display image
	 * @throws IllegalStateException    if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 * @throws IllegalArgumentException if passed <b>imageAware</b> is null
	 */
	public void displayImage(String uri, ImageAware imageAware) {
		displayImage(uri, imageAware, null, null, null);
	}

	/**
	 * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
	 * Default {@linkplain DisplayFileOptions display image options} from {@linkplain FileLoaderConfiguration
	 * configuration} will be used.<br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri        Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageAware {@linkplain com.nostra13.universalfileloader.core.imageaware.ImageAware Image aware view}
	 *                   which should display image
	 * @param listener   {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on
	 *                   UI thread.
	 * @throws IllegalStateException    if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 * @throws IllegalArgumentException if passed <b>imageAware</b> is null
	 */
	public void displayImage(String uri, ImageAware imageAware, ImageLoadingListener listener) {
		displayImage(uri, imageAware, null, listener, null);
	}

	/**
	 * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri        Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageAware {@linkplain com.nostra13.universalfileloader.core.imageaware.ImageAware Image aware view}
	 *                   which should display image
	 * @param options    {@linkplain com.nostra13.universalfileloader.core.DisplayFileOptions Options} for image
	 *                   decoding and displaying. If <b>null</b> - default display image options
	 *                   {@linkplain FileLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayFileOptions)
	 *                   from configuration} will be used.
	 * @throws IllegalStateException    if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 * @throws IllegalArgumentException if passed <b>imageAware</b> is null
	 */
	public void displayImage(String uri, ImageAware imageAware, DisplayFileOptions options) {
		displayImage(uri, imageAware, options, null, null);
	}

	/**
	 * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri        Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageAware {@linkplain com.nostra13.universalfileloader.core.imageaware.ImageAware Image aware view}
	 *                   which should display image
	 * @param options    {@linkplain com.nostra13.universalfileloader.core.DisplayFileOptions Options} for image
	 *                   decoding and displaying. If <b>null</b> - default display image options
	 *                   {@linkplain FileLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayFileOptions)
	 *                   from configuration} will be used.
	 * @param listener   {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on
	 *                   UI thread.
	 * @throws IllegalStateException    if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 * @throws IllegalArgumentException if passed <b>imageAware</b> is null
	 */
	public void displayImage(String uri, ImageAware imageAware, DisplayFileOptions options,
							 ImageLoadingListener listener) {
		displayImage(uri, imageAware, options, listener, null);
	}

	/**
	 * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri              Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageAware       {@linkplain com.nostra13.universalfileloader.core.imageaware.ImageAware Image aware view}
	 *                         which should display image
	 * @param options          {@linkplain com.nostra13.universalfileloader.core.DisplayFileOptions Options} for image
	 *                         decoding and displaying. If <b>null</b> - default display image options
	 *                         {@linkplain FileLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayFileOptions)
	 *                         from configuration} will be used.
	 * @param listener         {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
	 *                         events on UI thread.
	 * @param progressListener {@linkplain com.nostra13.universalfileloader.core.listener.ImageLoadingProgressListener
	 *                         Listener} for image loading progress. Listener fires events on UI thread.
	 * @throws IllegalStateException    if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 * @throws IllegalArgumentException if passed <b>imageAware</b> is null
	 */
	public void displayImage(String uri, ImageAware imageAware, DisplayFileOptions options,
							 ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		checkConfiguration();
		if (imageAware == null) {
			throw new IllegalArgumentException(ERROR_WRONG_ARGUMENTS);
		}
		if (listener == null) {
			listener = emptyListener;
		}
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}

		if (TextUtils.isEmpty(uri)) {
			engine.cancelDisplayTaskFor(imageAware);
			return;
		}

		String memoryCacheKey = MemoryCacheUtils.generateKey(uri);
		engine.prepareDisplayTaskFor(imageAware, memoryCacheKey);

		listener.onLoadingStarted(uri, imageAware.getWrappedView());

		byte[] bmp = configuration.memoryCache.get(memoryCacheKey);
		if (bmp != null) {
			if (configuration.writeLogs) L.d(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, memoryCacheKey);

			if (options.shouldPostProcess()) {
				FileLoadingInfo imageLoadingInfo = new FileLoadingInfo(uri, imageAware, memoryCacheKey,
						options, listener, progressListener, engine.getLockForUri(uri));
				ProcessAndDisplayImageTask displayTask = new ProcessAndDisplayImageTask(engine, bmp, imageLoadingInfo,
						defineHandler(options));
				if (options.isSyncLoading()) {
					displayTask.run();
				} else {
					engine.submit(displayTask);
				}
			} else {
				options.getDisplayer().display(bmp, imageAware, LoadedFrom.MEMORY_CACHE);
				listener.onLoadingComplete(uri, imageAware.getWrappedView(), bmp);
			}
		} else {
			FileLoadingInfo imageLoadingInfo = new FileLoadingInfo(uri, imageAware, memoryCacheKey,
					options, listener, progressListener, engine.getLockForUri(uri));
			LoadAndDisplayImageTask displayTask = new LoadAndDisplayImageTask(engine, imageLoadingInfo,
					defineHandler(options));
			if (options.isSyncLoading()) {
				displayTask.run();
			} else {
				engine.submit(displayTask);
			}
		}
	}

	/**
	 * Adds load image task to execution pool. Image will be returned with
	 * {@link ImageLoadingListener#onLoadingComplete(String, android.view.View, android.graphics.Bitmap)} callback}.
	 * <br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri             Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param targetImageSize Minimal size for {@link Bitmap} which will be returned in
	 *                        {@linkplain ImageLoadingListener#onLoadingComplete(String, android.view.View,
	 *                        android.graphics.Bitmap)} callback}. Downloaded image will be decoded
	 *                        and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
	 *                        larger) than incoming targetImageSize.
	 * @param options         {@linkplain com.nostra13.universalfileloader.core.DisplayFileOptions Options} for image
	 *                        decoding and displaying. If <b>null</b> - default display image options
	 *                        {@linkplain FileLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayFileOptions)
	 *                        from configuration} will be used.<br />
	 * @param listener        {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
	 *                        events on UI thread.
	 * @throws IllegalStateException if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 */
	public void loadImage(String uri, DisplayFileOptions options,
						  ImageLoadingListener listener) {
		loadImage(uri, options, listener, null);
	}

	/**
	 * Adds load image task to execution pool. Image will be returned with
	 * {@link ImageLoadingListener#onLoadingComplete(String, android.view.View, android.graphics.Bitmap)} callback}.
	 * <br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri              Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param targetImageSize  Minimal size for {@link Bitmap} which will be returned in
	 *                         {@linkplain ImageLoadingListener#onLoadingComplete(String, android.view.View,
	 *                         android.graphics.Bitmap)} callback}. Downloaded image will be decoded
	 *                         and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
	 *                         larger) than incoming targetImageSize.
	 * @param options          {@linkplain com.nostra13.universalfileloader.core.DisplayFileOptions Options} for image
	 *                         decoding and displaying. If <b>null</b> - default display image options
	 *                         {@linkplain FileLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayFileOptions)
	 *                         from configuration} will be used.<br />
	 * @param listener         {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
	 *                         events on UI thread.
	 * @param progressListener {@linkplain com.nostra13.universalfileloader.core.listener.ImageLoadingProgressListener
	 *                         Listener} for image loading progress. Listener fires events on UI thread.
	 * @throws IllegalStateException if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 */
	public void loadImage(String uri, DisplayFileOptions options,
						  ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		checkConfiguration();
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}

		ImageNonViewAware imageAware = new ImageNonViewAware(uri);
		displayImage(uri, imageAware, options, listener, progressListener);
	}

	/**
	 * Loads and decodes image synchronously.<br />
	 * Default display image options
	 * {@linkplain FileLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayFileOptions) from
	 * configuration} will be used.<br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri             Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param targetImageSize Minimal size for {@link Bitmap} which will be returned. Downloaded image will be decoded
	 *                        and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
	 *                        larger) than incoming targetImageSize.
	 * @return Result image Bitmap. Can be <b>null</b> if image loading/decoding was failed or cancelled.
	 * @throws IllegalStateException if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 */
	public byte[] loadImageSync(String uri) {
		return loadImageSync(uri, null);
	}

	/**
	 * Loads and decodes image synchronously.<br />
	 * <b>NOTE:</b> {@link #init(FileLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri             Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param targetImageSize Minimal size for {@link Bitmap} which will be returned. Downloaded image will be decoded
	 *                        and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
	 *                        larger) than incoming targetImageSize.
	 * @param options         {@linkplain com.nostra13.universalfileloader.core.DisplayFileOptions Options} for image
	 *                        decoding and scaling. If <b>null</b> - default display image options
	 *                        {@linkplain FileLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayFileOptions)
	 *                        from configuration} will be used.
	 * @return Result image Bitmap. Can be <b>null</b> if image loading/decoding was failed or cancelled.
	 * @throws IllegalStateException if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 */
	public byte[] loadImageSync(String uri, DisplayFileOptions options) {
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}
		options = new DisplayFileOptions.Builder().cloneFrom(options).syncLoading(true).build();

		SyncImageLoadingListener listener = new SyncImageLoadingListener();
		loadImage(uri, options, listener);
		return listener.getLoadedBitmap();
	}

	/**
	 * Checks if ImageLoader's configuration was initialized
	 *
	 * @throws IllegalStateException if configuration wasn't initialized
	 */
	private void checkConfiguration() {
		if (configuration == null) {
			throw new IllegalStateException(ERROR_NOT_INIT);
		}
	}

	/**
	 * Returns memory cache
	 *
	 * @throws IllegalStateException if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 */
	public MemoryCacheAware<String, byte[]> getMemoryCache() {
		checkConfiguration();
		return configuration.memoryCache;
	}

	/**
	 * Clears memory cache
	 *
	 * @throws IllegalStateException if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 */
	public void clearMemoryCache() {
		checkConfiguration();
		configuration.memoryCache.clear();
	}

	/**
	 * Returns disc cache
	 *
	 * @throws IllegalStateException if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 */
	public DiscCacheAware getDiscCache() {
		checkConfiguration();
		return configuration.discCache;
	}

	/**
	 * Clears disc cache.
	 *
	 * @throws IllegalStateException if {@link #init(FileLoaderConfiguration)} method wasn't called before
	 */
	public void clearDiscCache() {
		checkConfiguration();
		configuration.discCache.clear();
	}

	/**
	 * Returns URI of image which is loading at this moment into passed
	 * {@link com.nostra13.universalfileloader.core.imageaware.ImageAware ImageAware}
	 */
	public String getLoadingUriForView(ImageAware imageAware) {
		return engine.getLoadingUriForView(imageAware);
	}

	/**
	 * Cancel the task of loading and displaying image for passed
	 * {@link com.nostra13.universalfileloader.core.imageaware.ImageAware ImageAware}.
	 *
	 * @param imageAware {@link com.nostra13.universalfileloader.core.imageaware.ImageAware ImageAware} for
	 *                   which display task will be cancelled
	 */
	public void cancelDisplayTask(ImageAware imageAware) {
		engine.cancelDisplayTaskFor(imageAware);
	}

	/**
	 * Denies or allows ImageLoader to download images from the network.<br />
	 * <br />
	 * If downloads are denied and if image isn't cached then
	 * {@link ImageLoadingListener#onLoadingFailed(String, View, FailReason)} callback will be fired with
	 * {@link FailReason.FailType#NETWORK_DENIED}
	 *
	 * @param denyNetworkDownloads pass <b>true</b> - to deny engine to download images from the network; <b>false</b> -
	 *                             to allow engine to download images from network.
	 */
	public void denyNetworkDownloads(boolean denyNetworkDownloads) {
		engine.denyNetworkDownloads(denyNetworkDownloads);
	}

	/**
	 * Sets option whether ImageLoader will use {@link FlushedInputStream} for network downloads to handle <a
	 * href="http://code.google.com/p/android/issues/detail?id=6066">this known problem</a> or not.
	 *
	 * @param handleSlowNetwork pass <b>true</b> - to use {@link FlushedInputStream} for network downloads; <b>false</b>
	 *                          - otherwise.
	 */
	public void handleSlowNetwork(boolean handleSlowNetwork) {
		engine.handleSlowNetwork(handleSlowNetwork);
	}

	/**
	 * Pause ImageLoader. All new "load&display" tasks won't be executed until ImageLoader is {@link #resume() resumed}.
	 * <br />
	 * Already running tasks are not paused.
	 */
	public void pause() {
		engine.pause();
	}

	/** Resumes waiting "load&display" tasks */
	public void resume() {
		engine.resume();
	}

	/**
	 * Cancels all running and scheduled display image tasks.<br />
	 * ImageLoader still can be used after calling this method.
	 */
	public void stop() {
		engine.stop();
	}

	/**
	 * {@linkplain #stop() Stops ImageLoader} and clears current configuration. <br />
	 * You can {@linkplain #init(FileLoaderConfiguration) init} ImageLoader with new configuration after calling this
	 * method.
	 */
	public void destroy() {
		if (configuration != null && configuration.writeLogs) L.d(LOG_DESTROY);
		stop();
		engine = null;
		configuration = null;
	}

	private static Handler defineHandler(DisplayFileOptions options) {
		Handler handler = options.getHandler();
		if (options.isSyncLoading()) {
			handler = null;
		} else if (handler == null && Looper.myLooper() == Looper.getMainLooper()) {
			handler = new Handler();
		}
		return handler;
	}
}
