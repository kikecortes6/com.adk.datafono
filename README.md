# com.ci24.datafono

plugin for Bluetooth payment communication

## Getting Started

This proyect is to acomplish bluetooth communication between genico miniDatafono and ionic framework.

### Prerequisites

Cordova 5.0 or Later
Ionic 1.2 or Later
for Android 4.4 or later

### Installing

cordova plugin add https://github.com/kikecortes6/com.ci24.datafono.git

## Methods

These are functions that you can call into your ionic project

### datacom.connect

```
datacom.connect(
function (data) {
 //Conecction successfull 
 },function (err) { 
 //Error trying to connect console.log(err); 
 });

```
### datacom.checkConnection

```
datacom.checkConnection(
function (data) { 
//Connected 
},function (err) { 
//not Conected console.log(err); 
})

```

###  datacom.disconnect

```
datacom.disconnect(
function (data) { 
//Disconecction successfull 
},function (err) { 
//Can not Disconnect console.log(err); 
});

```
## datacom.transactionEX

```
var args= {
"Amount": "",
"CurrencyCode": "",
"Operation": "",
"TermNum": "",
"AuthorizationType": "",
"CtrlCheque": "",
"UserData1": "",
"AppNumber": 0,
"Trama":["252","50","220","1","10","223","254","64","6","0","0","0","50","130","89","223","255","34","6","0","0","0","5","36","17","223","254","130","6","0","0","0","0","0","0","223","255","37","4","49","50","53","48","223","255","43","5","52","48","53","55","50"]};


datacom.transactionEX(args,function (data) {
  //return a json with the outbuffer 
  console.log(data);
},function (err) {
//Error running transaction
  console.log(err);
});

```

## datacom.init
```
datacom.init(args,function (data) {
          Callback("Datafono: "+data);
        },function (error) {
          Error(error);
        })

```
### datacom.getDevices
```
datacom.getDevices(function (data) {
          Callback(data);
var Macs=data;
        },function (error) {
          Error(error);
        })
```
### datacom.getTime
```
datacom.getTime(function (data) {
          console.log(data);
          Callback(data);
        },function (error) {
          Error(error);
        })
  ```      
### datacom.getBatteryLevel
```
datacom.getBatteryLevel(function (level) {
          Callback(level);
          var Nivel=level;
        },function (error) {
          Error(error);
        })
  ```      
### datacom.getInfo
```
datacom.getInfo(function (serial) {
          Callback(serial);
          console.log(serial);
        },function (error) {
          Error(error);
        })

```
## Authors

* **Oscar Cortes** - 


