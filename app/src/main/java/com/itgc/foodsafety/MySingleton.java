package com.itgc.foodsafety;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.VolleyLogger;

import java.io.File;

public class MySingleton extends Application {

	private static MySingleton mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();


		Stetho.initializeWithDefaults(this);
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
			mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

			// Add global request listener
			mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
				@Override
				public void onRequestFinished(Request<Object> request) {
					VolleyLogger.getInstance().logRequest(request);
				}
			});
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
