package ru.magnat.android.service.openvpn.client;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import de.blinkt.openvpn.api.APIVpnProfile;
import de.blinkt.openvpn.api.IOpenVPNAPIService;

public class MainActivity extends Activity {
	
	private IOpenVPNAPIService mOpenVPNAPIService;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
	    public void onServiceConnected(ComponentName className, IBinder service) {
	    	Log.d("", "Connected to external service");
	    	
	    	mOpenVPNAPIService = IOpenVPNAPIService.Stub.asInterface(service);
	    }

	    public void onServiceDisconnected(ComponentName className) {
	    	Log.d("", "Disconnected from external service");
	    	
	    	mOpenVPNAPIService = null;
	    }
	    
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bindService(new Intent("de.blinkt.openvpn.api.IOpenVPNAPIService"), mConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		unbindService(mConnection); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem item1 = menu.add(R.string.connect);
		item1.setIcon(android.R.drawable.ic_media_play);
		item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		MenuItem item2 = menu.add(R.string.disconnect);
		item2.setIcon(android.R.drawable.ic_media_rew);
		item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getTitle().equals(getResources().getString(R.string.connect))) {
			try {
				List<APIVpnProfile> profiles = mOpenVPNAPIService.getProfiles();
				mOpenVPNAPIService.startProfile(profiles.get(0).mUUID); 
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
		if (item.getTitle().equals(getResources().getString(R.string.disconnect))) {
			try {
				mOpenVPNAPIService.disconnect();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
		
		return super.onMenuItemSelected(featureId, item);
	}
	
}
