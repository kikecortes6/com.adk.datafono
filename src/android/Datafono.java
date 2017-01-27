
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
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import static org.apache.cordova.device.Device.TAG;


public class Datafono {
  // Declare PclService interface

  protected PclService mPclService = null;
  private static Boolean m_BarCodeActivated = false;






private static Datafono datafono;

  public static Datafono getInstance(){

    if(datafono == null){
        datafono = new Datafono();
    }
    return datafono;

  }
  public boolean isCompanionConnected()   {
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

  protected _SYSTEMTIME sysTime;
  public class _SYSTEMTIME   {
    // WORD = UInt16
    public short wYear;
    public short wMonth;
    public short wDayOfWeek;
    public short wDay;
    public short wHour;
    public short wMinute;
    public short wSecond;
    public short wMilliseconds;
  }
  public String getTime() {
    boolean ret = false;
    byte[] time = new byte[16];
    if( mPclService != null ) {
      {
        sysTime = new _SYSTEMTIME();
        ret = mPclService.getTerminalTime(time);
        ByteBuffer bbTime = ByteBuffer.wrap(time);
        bbTime.order(ByteOrder.LITTLE_ENDIAN);
        sysTime.wYear = bbTime.getShort();
        sysTime.wMonth = bbTime.getShort();
        sysTime.wDayOfWeek = bbTime.getShort();
        sysTime.wDay = bbTime.getShort();
        sysTime.wHour = bbTime.getShort();
        sysTime.wMinute = bbTime.getShort();
        sysTime.wSecond = bbTime.getShort();
        sysTime.wMilliseconds = bbTime.getShort();



      }


    }


     return sysTime.toString();
  }
  int SN, PN;
  public boolean getTermInfo() {
    boolean ret = false;
    byte[] serialNbr = new byte[4];
    byte[] productNbr = new byte[4];
    if( mPclService != null ) {
      {
        ret = mPclService.getTerminalInfo(serialNbr, productNbr);
        ByteBuffer bbSN = ByteBuffer.wrap(serialNbr);
        ByteBuffer bbPN = ByteBuffer.wrap(productNbr);
        bbSN.order(ByteOrder.LITTLE_ENDIAN);
        bbPN.order(ByteOrder.LITTLE_ENDIAN);
        SN = bbSN.getInt();
        PN = bbPN.getInt();
        Log.d(TAG,"valores de info ");
        Log.d(TAG,String.valueOf(SN));
        Log.d(TAG,String.valueOf(PN));
      }
    }
    return ret;

  }


  public boolean doTransaction(TransactionIn transIn, TransactionOut transOut) {
    boolean ret = false;

    if( mPclService != null ) {
      {
        ret = mPclService.doTransaction(transIn, transOut);
      }
    }
    return ret;

  }


  public boolean doTransactionEx(TransactionIn transIn, TransactionOut transOut, int appNumber, byte[] inBuffer, int inBufferSize, byte[] outBuffer, long[] outBufferSize) {
    boolean ret = false;

    if( mPclService != null ) {
      try {
        ret = mPclService.doTransactionEx(transIn, transOut, appNumber, inBuffer, inBufferSize, outBuffer, outBufferSize);
      } catch( IllegalArgumentException iae) {
        iae.printStackTrace();
      }
    }
    return ret;

  }













   public boolean getFullSerialNumber(byte[] serialNbr) {
    boolean ret = false;

    if( mPclService != null ) {
      {
        ret = mPclService.getFullSerialNumber(serialNbr);
        ByteBuffer bbSN = ByteBuffer.wrap(serialNbr);
        bbSN.order(ByteOrder.LITTLE_ENDIAN);
        SN = bbSN.getInt();
      }
    }
    return ret;

  }


  public boolean openBarCode()
  {
    Log.d(TAG, "openBarCode" );
    if((mPclService != null) && !m_BarCodeActivated)
      m_BarCodeActivated = setBarCodeActivation(true);

    return m_BarCodeActivated;
  }
  private boolean setBarCodeActivation(boolean activateBarCode)
  {
    boolean result = false;
    byte array [] = null;

    if(mPclService != null)
    {
      array = new byte[1];
      {
        if(activateBarCode)
        {
          result = mPclService.openBarcode(array);
          if (result == true)
          {
            if (array[0] != 0)
              result = false;
          }
        }
        else
        {
          mPclService.closeBarcode(array);
          result = true;
        }
      }

    }

    return result;
  }





  private PclServiceConnection mServiceConnection;

  // Implement ServiceConnection

public class PclServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder boundService ){

                          // We've bound to LocalService, cast the IBinder and get LocalService instance


          PclService.LocalBinder binder = (PclService.LocalBinder) boundService;
          mPclService = (PclService) binder.getService();
          Log.d(TAG, "onServiceConnected" );

           }





        public void onServiceDisconnected(ComponentName className) {

                mPclService = null;
          Log.d(TAG, "onServiceDisconnected" );
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
  Log.d(TAG, "onServiceDisconnected" );
}




};

