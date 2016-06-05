package com.ppma.webkit;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PWebChromeClient extends WebChromeClient {

	private String TAG = this.getClass().getSimpleName();
	
    public static final int REQUEST_SELECT_FILE = 100;
    public static final int FILECHOOSER_RESULTCODE = 200;

    public ValueCallback<Uri[]> mUploadMessages;
    public ValueCallback<Uri> mUploadMessage;
    
	private Fragment mFragment;
	private ProgressBar mProgressBar;

	public PWebChromeClient(Fragment fragment) {
		mFragment = fragment;
	}
	
	public Fragment getFragemnt() {
		return mFragment;
	}
	
	@Override
	public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
		return super.onCreateWindow(view, dialog, userGesture, resultMsg);
	}
	
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
		
		if (mProgressBar == null) {
			return;
		}

		mProgressBar.setProgress(newProgress);

		if (newProgress == 100) {
			 mProgressBar.setVisibility(View.GONE);
		} else {
			 mProgressBar.setVisibility(View.VISIBLE);
		}
	}
	
	public void setProgressBar(ProgressBar progressBar) {
		mProgressBar = progressBar; 
		mProgressBar.setProgress(0); // 탭이 바뀔때 초기화를 시킨다.
	}
	
	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
		return super.onConsoleMessage(consoleMessage);
	}
	
	//For Android 5.0
    @SuppressLint("NewApi")
	public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        // make sure there is no existing message
        if (mUploadMessages != null) {
            mUploadMessages.onReceiveValue(null);
            mUploadMessages = null;
        }

        mUploadMessages = filePathCallback;

        Intent intent = fileChooserParams.createIntent();
        try {
            mFragment.startActivityForResult(intent, REQUEST_SELECT_FILE);
        } catch (ActivityNotFoundException e) {
            mUploadMessages = null;
            Toast.makeText(mFragment.getActivity(), "사진을 선택할 수 없습니다.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    
	// For Android < 3.0
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		openFileChooser(uploadMsg, "");
	}

	// For Android 3.0+
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
    	mUploadMessage = uploadMsg;  
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
        i.addCategory(Intent.CATEGORY_OPENABLE);  
        i.setType("image/*");
        mFragment.startActivityForResult( Intent.createChooser( i, "Select File" ), FILECHOOSER_RESULTCODE );
	}

	// For Android 4.1+
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		openFileChooser(uploadMsg, "");
	}
}	
