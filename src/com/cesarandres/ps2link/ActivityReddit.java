package com.cesarandres.ps2link;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.webkit.WebView;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityReddit extends FragmentActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reddit);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		webView = (WebView) findViewById(R.id.webViewReddit);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://www.reddit.com/r/Planetside/");
	}
	
	  @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if(event.getAction() == KeyEvent.ACTION_DOWN){
	            switch(keyCode)
	            {
	            case KeyEvent.KEYCODE_BACK:
	                if(webView.canGoBack() == true){
	                	webView.goBack();
	                }else{
	                    finish();
	                }
	                return true;
	            }

	        }
	        return super.onKeyDown(keyCode, event);
	    }
}
