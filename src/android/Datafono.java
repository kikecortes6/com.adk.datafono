
package com.ci24.functions;

import com.ci24.Interfaces.IfaceCallbackDatafono;
import com.ingenico.pclutilities.*;
import com.ingenico.pclservice.*;
import com.ci24.datafono.Comdata;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.IntentFilter;
import android.content.pm.LauncherApps;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.Context;
import android.content.Intent;
import 	android.os.Binder;
import android.provider.ContactsContract;
import android.content.BroadcastReceiver;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import static org.apache.cordova.device.Device.TAG;


public class Datafono {


  protected PclService mPclService = null;
  private IfaceCallbackDatafono callback;
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
  public boolean printText( String strText ) {

    boolean Result = false;



    byte[] PrintResult = new byte[1];

    try {

      Result = mPclService.printText(strText, PrintResult);

    } catch (Exception e) {

      e.printStackTrace();

    }
    return Result;

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

  private IPclServiceCallback mCallback = new IPclServiceCallback() {

    public int shouldEndReceipt() {
      return 0;
    }
    @Override
    public void signatureTimeoutExceeded() {

    }
    public void shouldCutPaper() {

    }
    @Override
    public void shouldDoSignatureCapture(int pos_x, int pos_y, int width,
                                         int height, int timeout) {

    }

    public int shouldStartReceipt(byte type) {
      return 0;
    }
    @Override
    public void shouldAddSignature() {

    }

    public void shouldFeedPaper(int lines) {

// Update application TextView for instance

    }
    public void shouldPrintRawText(byte[] text, byte charset, byte font, byte justification, byte xfactor, byte yfactor, byte underline, byte bold) {

    }

//Implement all other callbacks

    public void shouldPrintText(String text, byte font, byte justification, byte xfactor, byte yfactor, byte underline, byte bold) {

    }
    public void shouldPrintImage(Bitmap image, byte justification){

    }




  };

  public void doTransactionEx(IfaceCallbackDatafono callback, TransactionIn transIn, TransactionOut transOut, int appNumber, byte[] extDataIn, int inBufferSize, byte[] extDataOut, long[] extDataOutSize) {

    this.callback = callback;
    if( mPclService != null ) {
      try {
       new DoTransactionExTask(transIn, transOut, appNumber, extDataIn, extDataOut).execute();
      } catch( IllegalArgumentException iae) {
        iae.printStackTrace();
      }
    }


  }

  class DoTransactionExTask extends AsyncTask<Void, Void, Boolean> {
    private TransactionIn transIn;
    private TransactionOut transOut;
    private byte[] extDataIn;
    private byte[] extDataOut;
    private long[] extDataOutSize;
    private int appNumber;
    public DoTransactionExTask(TransactionIn transIn, TransactionOut transOut, int appNumber, byte[] extDataIn, byte[] extDataOut) {
      this.transIn = transIn;
      this.transOut = transOut;
      this.extDataIn = extDataIn;
      this.extDataOut = extDataOut;
      this.appNumber = appNumber;
      this.extDataOutSize = new long[1];
      this.extDataOutSize[0] = extDataOut.length;
    }

    protected Boolean doInBackground(Void... tmp) {
      Boolean ret=mPclService.doTransactionEx(transIn, transOut, appNumber, extDataIn, extDataIn.length, extDataOut, extDataOutSize);
      return ret;
    }
    protected void onPostExecute(Boolean result) {

      JSONArray respuesta = new JSONArray();
      int responseSize=0;
      int len = extDataOut[1];
      if(len<0){
        len= len+256;
      }
      if(len<129){
        responseSize=len+2;
      }
      else if(len==129){
        responseSize=extDataOut[2]+3;
      }else if(len==130){
        responseSize=extDataOut[2]*10+extDataOut[3]+4;
      }
      if(responseSize<0){
        responseSize=responseSize+256;
      }
      for (int i = 0; i < responseSize; i++) {
        try {
          Byte b = extDataOut[i];
          int k = b.intValue();
          if (k < 0) {
            k = k + 256;
          }
          respuesta.put(i, k);
        } catch (Exception e) {
          Log.d(TAG, e.toString());
        }
      }
      try{
           callback.responseDatafono(respuesta);
        Log.d(TAG, String.valueOf(respuesta.length()));
      }
      catch (Exception e) {
        Log.d(TAG, e.toString());
      }
    }
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


  public boolean openBarCode() {
    Log.d(TAG, "openBarCode" );
    if((mPclService != null) && !m_BarCodeActivated)
      m_BarCodeActivated = setBarCodeActivation(true);

    return m_BarCodeActivated;
  }

  private boolean setBarCodeActivation(boolean activateBarCode){
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

public class PclServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder boundService ){
          PclService.LocalBinder binder = (PclService.LocalBinder) boundService;
          mPclService = (PclService) binder.getService();
          Log.d(TAG, "onServiceConnected" );
           }

        public void onServiceDisconnected(ComponentName className) {
                mPclService = null;
                Log.d(TAG, "onServiceDisconnected" );
            }
    };



public  void  initService(Activity act){

mServiceConnection = new PclServiceConnection();

Intent intent = new Intent(act, PclService.class);

act.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

}
  // You can call this method in onDestroy for instance

public void releaseService(Activity act){

  act.unbindService(mServiceConnection);
  Log.d(TAG, "onServiceDisconnected" );
}




};

