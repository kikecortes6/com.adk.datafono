package com.ci24.datafono;
import com.ci24.Interfaces.IfaceCallbackDatafono;
import com.ci24.functions.Datafono;
import com.ingenico.pclutilities.*;
import com.ingenico.pclservice.*;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Set;

import static android.R.attr.name;
import static org.apache.cordova.device.Device.TAG;

public class Comdata extends CordovaPlugin implements IfaceCallbackDatafono {

  public CallbackContext transactionCB;
  public CallbackContext connectionCB;
  public  CallbackContext disconnectCB;
  public CallbackContext devicesCB;


 private String start="Conexion Iniciada";
 private String finish="Servicio Finalizado";
 byte[] extDataOut = new byte[5000];
 public Activity getActivity() {    return this.cordova.getActivity();  }

  private static Comdata comdata;
  public static Comdata getInstance(){
  if(comdata == null){ comdata = new Comdata(); }
    return comdata;
  }

 @Override
    public boolean execute(String action, final JSONArray args,final  CallbackContext callbackContext) throws JSONException {


   // Funcion de conectar el dispositivo, se ingresa la mac del mini datafono
   // retorna un callback de error o de exito si el dispositivo esta conectado
       if (action.equals("connect")) {
         try {
          cordova.getThreadPool().execute(new Runnable() {
             @Override
             public void run() {
               String mac= null;
               try {
                 mac = (String)args.getString(0);
               } catch (JSONException e) {
                 Log.d(TAG,e.toString());
                 return;
               }
               try {
                 Datafono.getInstance().pairCompanion(getActivity(),mac,Comdata.this);
               } catch (Exception e) {
                 e.printStackTrace();
                 return;
               }
             }
           });
           this.connectionCB=callbackContext;
         }catch (Exception e){
           callbackContext.error(e.toString());
         }
        }

       //Función que chequea el estado del dispositivo sei esta conectado o desconectado.
       else if(action.equals("check")){
          boolean check= Datafono.getInstance().isCompanionConnected();
            if(check==true){
              callbackContext.success("Connected");
            }else{
              callbackContext.error("Not Connected");
            }
       }

       //función que termina la conexión del dispositivo
       // retorna un callback de error o de exito
       else if(action.equals("finish")){
        try {
           this.disconnectCB=callbackContext;
           cordova.getThreadPool().execute(new Runnable() {
             @Override
             public void run() {
               Datafono.getInstance().releaseService(getActivity(), Comdata.this);
             }
             });
         }catch (Exception e){
           callbackContext.success(e.toString());
         }
       }

       //Función que retorna los seriales del dispositivo y del producto
       else if(action.equals("getInfo")){
         
         cordova.getThreadPool().execute(new Runnable() {
           @Override
           public void run() {
              String info = Datafono.getInstance().getTermInfo();
             if(info.equals("0")){
               callbackContext.error("No se pudo obterner la informació del dispositivo");
             }else {
               callbackContext.success(info);
             }
           }
         });
        
       }
       //Función que retorna el nivel de la bateria actual del dispositivo
       else if(action.equals("getBatteryLevel")){
         cordova.getThreadPool().execute(new Runnable() {
           @Override
           public void run() {
             int level = Datafono.getInstance().getBatteryLevel();
             if (level == -1) {
               callbackContext.error("no se peude obtener el nivel de bateria");
             } else {
               callbackContext.success(level);
             }
           }
         });
       }
       // Función de devuelve un string con la hora y fecha del dispositivo
       else if(action.equals("getTime")){
         cordova.getThreadPool().execute(new Runnable() {
           @Override
           public void run() {
             String time = Datafono.getInstance().getTime();
             if (time.equals("0")) {
               callbackContext.error("No se puede obtener la fecha");
             } else {
               callbackContext.success(time);
             }
           }
         });
       }

       //Función que inicializa parametros necesarios para la ejecución del programa
       else if(action.equals("init")){
             String appName=null;
             try {
               appName=(String) args.getString(0);
             } catch (JSONException e) {
               callbackContext.error(e.toString());
               return true;
             }
             Datafono.getInstance().initialize(getActivity(),appName);
             callbackContext.success("Init Success");
       }
       //función que recibe una trama y ejecuta la transacción retorna una trama de respuesta
       else if(action.equals("transactionEX")){
         this.transactionCB = callbackContext;
         cordova.getThreadPool().execute(new Runnable() {
           @Override
           public void run() {
             TransactionIn transIn = new TransactionIn();
             TransactionOut transOut = new TransactionOut();
             long[] sizebuff = new long[5000];
             String amount = new String();
             String currencyCode = new String();
             String operation = new String();
             String termNum = new String();
             String authorizationType = new String();
             String ctrlCheque = new String();
             String userData1 = new String();
             JSONArray trama = new JSONArray();
             int appNumber = 0;
             int sizeIn = 0;
             JSONArray obj = new JSONArray();
             obj = args;
             try {
               for (int i = 0; i < obj.length(); i++) {
                 JSONObject jsonobject = obj.getJSONObject(i);
                 amount = jsonobject.getString("Amount");
                 currencyCode = jsonobject.getString("CurrencyCode");
                 operation = jsonobject.getString("Operation");
                 termNum = jsonobject.getString("TermNum");
                 authorizationType = jsonobject.getString("AuthorizationType");
                 ctrlCheque = jsonobject.getString("CtrlCheque");
                 userData1 = jsonobject.getString("UserData1");
                 appNumber = jsonobject.getInt("AppNumber");
                 trama = jsonobject.getJSONArray("Trama");
               }
             } catch (Exception e) {
               Log.d(TAG, e.toString());
               callbackContext.error(e.toString());
               }
             sizeIn = trama.length();
             if (!amount.equals("")) { transIn.setAmount(amount); }
             if (!currencyCode.equals("")) { transIn.setCurrencyCode(currencyCode);}
             if (!operation.equals("")) { transIn.setOperation(operation); }
             if (!termNum.equals("")) { transIn.setTermNum(amount); }
             if (!authorizationType.equals("")) { transIn.setAuthorizationType(authorizationType); }
             if (!ctrlCheque.equals("")) { transIn.setCtrlCheque(ctrlCheque); }
             if (!userData1.equals("")) { transIn.setUserData1(userData1); }
             byte tramaIn[] = new byte[sizeIn];
             for (int i = 0; i < sizeIn; i++) {
               try {
                 tramaIn[i] = (byte) Integer.parseInt(trama.getString(i));
                 if (tramaIn[i] < 0) {
                   tramaIn[i] =  tramaIn[i];
                 }
               } catch (Exception e) {
                 Log.d(TAG, e.toString());
                 callbackContext.error(e.toString());
               }
             }
             boolean check = Datafono.getInstance().isCompanionConnected();
             if (check == true) {
               try {
                 Log.d(TAG, "Amount:" + transIn.getAmount() + " Currency:" + transIn.getCurrencyCode() + " Operation:" + transIn.getOperation());
                 Log.d(TAG, "TermNum:" + transIn.getTermNum() + " AuthoType:" + transIn.getAuthorizationType() + " CtrlCheque:" + transIn.getCtrlCheque());
                 Log.d(TAG, "UserData:" + transIn.getUserData1());
                 Datafono.getInstance().doTransactionEx(Comdata.this, transIn, transOut, appNumber, tramaIn, sizeIn, extDataOut, sizebuff);
               } catch (Exception e) {
                 Log.d(TAG, e.toString());
                 callbackContext.error(e.toString());
                }
             } else {
               callbackContext.error("Device Not Connected");
             }
           }
         });
         return true;
       }
       //función que retorna las mac y nlos nombres de los dispositivos compatibles ya emparejados
     else if(action.equals("devices")){
         this.devicesCB=callbackContext;
         cordova.getThreadPool().execute(new Runnable() {
           @Override
           public void run() {
             try{
               Datafono.getInstance().getDevices(getActivity(),Comdata.this);

             }catch (Exception e){
               callbackContext.error(e.toString());
             }
           }
         });
       }
        return true;
    }



  //Funciones de callback para funciones syncornas
  @Override
  public void responseDatafono(JSONArray response) {
    transactionCB.success(response);
  }
  @Override
  public void disconnect(){
   disconnectCB.success(finish);
  }
  @Override
  public void getDevices(JSONArray response){
    devicesCB.success(response);
  };
  @Override
  public  void connect(){
    connectionCB.success(start);
  }
}
