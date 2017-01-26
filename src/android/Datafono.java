
package com.ci24.functions;

import com.ingenico.pclutilities.*;
import com.ingenico.pclservice.*;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.IntentFilter;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.Context;
import android.content.Intent;
import 	android.os.Binder;
import android.provider.ContactsContract;
import android.content.BroadcastReceiver;



public class Datafono {
  // Declare PclService interface

protected PclService mPclService = null;







private static Datafono datafono;

  public static Datafono getInstance(){

    if(datafono == null){
        datafono = new Datafono();
    }
    return datafono;

  }
  public boolean isCompanionConnected()
  {
    boolean bRet = false;
    if (mPclService != null)
    {
      byte result[] = new byte[1];
      {
        if (mPclService.serverStatus(result) == true)
        {
          if (result[0] == 0x10)
            bRet = true;
        }
      }
    }
    return bRet;
  }





  private PclServiceConnection mServiceConnection;

  // Implement ServiceConnection

public class PclServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder boundService ){

                          // We've bound to LocalService, cast the IBinder and get LocalService instance


          PclService.LocalBinder binder = (PclService.LocalBinder) boundService;
          mPclService = (PclService) binder.getService();

           }
 /*  public class LocalBinder extends Binder {
        PclServiceConnection getService() {
            // Return this instance of LocalService so clients can call public methods
            return PclServiceConnection.this;
        }
    }*/




        public void onServiceDisconnected(ComponentName className) {

                mPclService = null;

            }

    };

  // You can call this method in onCreate for instance

public  void  initService(Activity act)

{

mServiceConnection = new PclServiceConnection();

Intent intent = new Intent(act, PclService.class);

act.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

}
  // You can call this method in onDestroy for instance

public void releaseService(Activity act)

{

  act.unbindService(mServiceConnection);

}




};

