cordova.define("com.ci24.datafono.datacom", function(require, exports, module) {

module.exports = (function() {

  var _connect = function (args,successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "connect", [args]);
    };
  var _checkConnection =function (args,successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "check", [args]);
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

   return {
    connect: _connect,
    checkConnection: _checkConnection,
    disconnect:_disconnect,
    init:_init,
    transactionEX:_transactionEX,
    getDevices:_getDevices


  };

})();

});
