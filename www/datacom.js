

module.exports = (function() {

  var _connect = function (args,successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "connect", [args]);
    };
  var _checkConnection =function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "check", []);
    };
  var _disconnect =function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "finish", []);
    };
  var _init = function (args,successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "init", [args]);
    };
  var _transactionEX =function (args,successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "transactionEX", [args]);
    };
  var _getDevices =function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "devices", []);
    };
  var _getInfo =function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Comdata", "getInfo", []);
  };
  var _getBatteryLevel =function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Comdata", "getBatteryLevel", []);
  };
  var _getTime =function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Comdata", "getTime", []);
  };

   return {
    connect: _connect,
    checkConnection: _checkConnection,
    disconnect:_disconnect,
    init:_init,
    transactionEX:_transactionEX,
    getDevices:_getDevices,
    getInfo:_getInfo,
    getBatteryLevel:_getBatteryLevel,
    getTime:_getTime


   };

})();

