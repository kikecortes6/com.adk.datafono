
module.exports = (function() {

  var _test = function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "test", []);
    };
  var _opengps =function (args,successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "testing", [args]);
    };
  var _subscribe =function (successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "Comdata", "pcl", []);
    };
  
   return {
    test: _test,
    testing: _opengps,
    subscribe:_subscribe
  

  };

})();
