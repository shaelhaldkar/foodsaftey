package com.itgc.foodsafety;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.utils.AppUtils;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class MySingleton extends Application {

	private static MySingleton mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
		Stetho.initializeWithDefaults(this);
        Fabric.with(this, new Crashlytics());
		File f = new File(AppUtils.FOOD_DIR);
		f.mkdirs();


		if (f.exists()) {
			try {
				DbManager.initializeInstance(new DBHelper(getApplicationContext(), AppUtils.FOOD_DIR + "FOOD.sqlite"), getApplicationContext());

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			DbManager.initializeInstance(new DBHelper(getApplicationContext(), "FOOD.sqlite"), getApplicationContext());
		}
    }



	/**
	 * Enum used to identify the tracker that needs to be used for tracking.
	 * 
	 * A single tracker is usually enough for most purposes. In case you do need
	 * multiple trackers, storing them all in Application object helps ensure
	 * that they are created only once per application instance.
	 */



	public MySingleton() {
		super();
	}


	private MySingleton(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();

		mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
				LruBitmapCache.getCacheSize(mCtx)));
	}

	public static synchronized MySingleton getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MySingleton(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext() is key, it keeps you from leaking the
			// Activity or BroadcastReceiver if someone passes one in.
			mRequestQueue = Volley
					.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
}
