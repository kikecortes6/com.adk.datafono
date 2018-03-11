
package com.adk.functions;

import com.adk.Interfaces.IfaceCallbackDatafono;
import com.ingenico.pclutilities.*;
import com.ingenico.pclservice.*;
import com.adk.datafono.Comdata;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import com.ingenico.pclutilities.PclUtilities;
import com.ingenico.pclutilities.PclUtilities.BluetoothCompanion;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Set;
import static org.apache.cordova.device.Device.TAG;


//clase la cual utilizaremos para todos los procesos de comunicacióny ejecución
public class Datafono  {
  //inicialización de variables
  protected PclService mPclService = null;
  private IfaceCallbackDatafono callback;
  private static Boolean m_BarCodeActivated = false;
  private static Datafono datafono;
  private PclUtilities mPclUtil;
  boolean mServiceStarted=false;
  private PclServiceConnection mServiceConnection;
  int SN, PN;
  String pack="com.ionicframework.plugindata691535";
  public boolean dis=true;
  protected _SYSTEMTIME sysTime;
  protected boolean mBound = false;

  //función que retorna la instancia de la clase para poder ser utilizada
  public static Datafono getInstance(){
    if(datafono == null){
        datafono = new Datafono();
    }
    return datafono;
  }
   // Función que empieza el servicio de pcl
   public void startPclService(Activity act) {
    if (!mServiceStarted){
      Intent i = new Intent(act, PclService.class);
      i.putExtra("PACKAGE_NAME", pack);
      i.putExtra("FILE_NAME", "pairing_addr.txt");
      if (act.getApplicationContext().startService(i) != null)
      {
        mServiceStarted = true;
      }
    }

  }
    //Función de chequeo de conexión
  public boolean isCompanionConnected()   {
    boolean bRet = false;
      if (mPclService != null) {
        byte result[] = new byte[1];
        {
          if (mPclService.serverStatus(result) == true) {
            if (result[0] == 0x10)
              bRet = true;
          }
        }
      }
      return bRet;
  }

  //función que retorna los dispositivos
  public void getDevices(Activity act,IfaceCallbackDatafono callback) {
    try{
      Set<PclUtilities.BluetoothCompanion> btComps = mPclUtil.GetPairedCompanions();
      JSONArray devicesResponse=new JSONArray();
      this.callback=callback;
      String data=null;
      if (btComps != null && (btComps.size() > 0)) {
       int i=0;
        for (PclUtilities.BluetoothCompanion comp : btComps) {
          Log.d(TAG, comp.getBluetoothDevice().getName()+"//"+comp.getBluetoothDevice().getAddress());
          data=comp.getBluetoothDevice().getName()+"//"+comp.getBluetoothDevice().getAddress();
          devicesResponse.put(i,data);
            i++;
         }
      }
      callback.getDevices(devicesResponse);
    }catch (Exception e){
      Log.d(TAG,e.toString());
    }
  }
  //Función que unicializa la el estado de conexión
  public void initialize(Activity activity,String appName) {
    pack=appName;
    mPclUtil=new PclUtilities(activity,pack,"pairing_addr.txt");
  }

  //clase para la lectura de la fecha y hora
  public class _SYSTEMTIME   {
    public short wYear;
    public short wMonth;
    public short wDayOfWeek;
    public short wDay;
    public short wHour;
    public short wMinute;
    public short wSecond;
    public short wMilliseconds;
  }


    //Función para obterner fecha y hora
  public String getTime() {
    boolean ret = false;
    byte[] time = new byte[16];
    String dateTime=null;
    if( mPclService != null ) {
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
        dateTime= String.valueOf(sysTime.wHour)+":"+String.valueOf(sysTime.wMinute)+":"+String.valueOf(sysTime.wSecond)+" "+String.valueOf(sysTime.wYear)+"/"+String.valueOf(sysTime.wMonth)+"/"+String.valueOf(sysTime.wDay);
      return dateTime;
    }else{
      return "0";
    }
  }

  //función que retorna los seriales del dispositivo
  public String getTermInfo() {
    boolean ret = false;
    byte[] serialNbr = new byte[4];
    byte[] productNbr = new byte[4];
    String info=null;
    if( mPclService != null ) {
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
        info=String.valueOf(SN)+"//"+String.valueOf(PN);
      return info;
    }else {
      return "0";
    }
  }

  //Función que retorna el porcentaje de bateria restante
  public int getBatteryLevel(){
    int level=0;
    int t[]= new int[1];
    if(mPclService!=null) {
      mPclService.getBatteryLevel(t);
      level = t[0];
      return level;
    }else {
      return -1;
    }
  }
  //Funcion sin implementar
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

  //Función sin implementar
  public boolean doTransaction(TransactionIn transIn, TransactionOut transOut) {
    boolean ret = false;
    if( mPclService != null ) {
      {
        ret = mPclService.doTransaction(transIn, transOut);
      }
    }
    return ret;
  }

  //Función que realiza la transacción
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
   //Clase para realizar la transacción
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
        responseSize=extDataOut[2]*256+extDataOut[3]+4;
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


   //Función sin implementar
  public boolean openBarCode() {
    Log.d(TAG, "openBarCode" );
    if((mPclService != null) && !m_BarCodeActivated)
      m_BarCodeActivated = setBarCodeActivation(true);
    return m_BarCodeActivated;
  }

  //Función sin implementar
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
    //Clase para los servición y conecxión pcl
  public class PclServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder boundService ){
          PclService.LocalBinder binder = (PclService.LocalBinder) boundService;
          mPclService = (PclService) binder.getService();
          Log.d(TAG, "onServiceConnected" );
          callback.connect();
           }
        public void onServiceDisconnected(ComponentName className) {
                mPclService = null;
                Log.d(TAG, "onServiceDisconnected" );
            }
    };

    //Conectar un dispositivo
public void pairCompanion(Activity act,String macAddress,IfaceCallbackDatafono callback){
try{
  this.callback=callback;
  int f= mPclUtil.ActivateCompanion(macAddress);
  Log.d(TAG, String.valueOf(f));
  if(f==0){
    startPclService(act);
    initService(act);
  }

}catch (Exception e){
  Log.d(TAG,e.toString());
}
}
  //Función para inicializar el servicio
public  void  initService(Activity act){
  if (!mBound) {
    try {
      mServiceConnection = new PclServiceConnection();
    } catch (Exception e) {
      Log.d(TAG, e.toString());
    }
    Intent intent = new Intent(act, PclService.class);
    intent.putExtra("PACKAGE_NAME", pack);
    intent.putExtra("FILE_NAME", "pairing_addr.txt");
    try {
      mBound=act.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    } catch (Exception e) {
      Log.d(TAG, e.toString());
    }
  }
}
  //Función para liberar el servicio
public void releaseService(Activity act,IfaceCallbackDatafono callback){
   this.callback=callback;
  if(mBound){
    Intent i = new Intent(act, PclService.class);
    if (act.getApplicationContext().stopService(i)) {
      mServiceStarted=false;
      mBound=false;
    }
    act.unbindService(mServiceConnection);
    mBound=false;
  }
  Log.d(TAG, "onServiceDisconnected" );
  dis=false;
  mPclService=null;
  callback.disconnect();
}

}

