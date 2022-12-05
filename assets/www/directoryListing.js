var DataBaseHelper = function() {};
var dbflag=false;
DataBaseHelper.prototype.open = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'DataBaseHelper', 'open', [params]);
};
DataBaseHelper.prototype.close  = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'DataBaseHelper', 'close', [params]);
};
DataBaseHelper.prototype.insert = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'DataBaseHelper', 'insert', [params]);
};
DataBaseHelper.prototype.copy2SdCard = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'DataBaseHelper', 'copy2SdCard', [params]);
};
DataBaseHelper.prototype.select = function(params, successCallback, failureCallback) 
{
    return cordova.exec(successCallback,failureCallback, 'DataBaseHelper', 'select', [params]);
};



/*cordova.addConstructor(function() 
{
    cordova.addPlugin('DataBaseHelper', new DataBaseHelper());
    PluginManager.addService("DataBaseHelper","com.phonegap.sfa.DataBaseHelper");
});*/


var ZebraHelper = function() {};

if(!window.plugins) {
	window.plugins = {};
}
if (!window.plugins.DataBaseHelper) {
	window.plugins.DataBaseHelper = new DataBaseHelper();
}

if (!window.plugins.PrinterHelper) {
	//window.plugins.PrinterHelper = new PrinterHelper();
	window.plugins.ZebraHelper = new ZebraHelper();	
}


/*PrinterHelper.prototype.print = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
    	//alert(args.status);
        success(args);
    }, 
    function(args) 
    {    	
        fail(args);
    }, 'PrinterHelper', '', params);
};*/

ZebraHelper.prototype.print = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
    	//alert(args.status);
        success(args);
    }, 
    function(args) 
    {    	
        fail(args);
    }, 'ZebraHelper', '', params);
};

//For dot matrix
var DotmatHelper = function() {};


DotmatHelper.prototype.print = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
    
        success(args);
    }, 
    function(args) 
    {
    	
        fail(args);
    }, 'DotmatHelper', '', params);
};



if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.DotmatHelper) {
    window.plugins.DotmatHelper = new DotmatHelper();
}
//Pb51 Helper Class 

//For dot matrix
var PB51Helper = function() {};


PB51Helper.prototype.print = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
    
        success(args);
    }, 
    function(args) 
    {
    	
        fail(args);
    }, 'PB51Helper', '', params);
};



if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.PB51Helper) {
    window.plugins.PB51Helper = new PB51Helper();
}








//--------Bluetood device helper
var BluetoothHelper = function() {};


BluetoothHelper.prototype.fetch = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'BluetoothHelper', params, [params]);
};



if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.BluetoothHelper) {
    window.plugins.BluetoothHelper = new BluetoothHelper();
}


//--------download device helper
var DownloadPlugin = function() {};


DownloadPlugin.prototype.download = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'DownloadPlugin', params, [params]);
};



if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.DownloadPlugin) {
    window.plugins.DownloadPlugin = new DownloadPlugin();
}


//------Map Helper to call MapScreen in native android


var MapHelper = function() {};


MapHelper.prototype.getMap = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'MapHelper', '', [params]);
};



if(!window.plugins) {
	window.plugins = {};
}
if (!window.plugins.MapHelper) {
	window.plugins.MapHelper = new MapHelper();
}


//------Chart Helper to call ChartScreen in native android


var ChartHelper = function() {};


ChartHelper.prototype.getChart = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'ChartHelper', '', [params]);
};



if(!window.plugins) {
	window.plugins = {};
}
if (!window.plugins.ChartHelper) {
	window.plugins.ChartHelper = new ChartHelper();
}

//Location Track Helper to start service to send location update to server

//------Chart Helper to call ChartScreen in native android


var LocationTrackHelper = function() {};


LocationTrackHelper.prototype.startservice = function(params, success, fail) 
{
    return cordova.exec(function(args) 
    {
        success(args);
    }, 
    function(args) 
    {
        fail(args);
    }, 'LocationTrackHelper', '', [params]);
};



if(!window.plugins) {
	window.plugins = {};
}
if (!window.plugins.LocationTrackHelper) {
	window.plugins.LocationTrackHelper = new LocationTrackHelper();
}



//Log remover plugin


//--------download device helper
var LogremoverPlugin = function() {};


LogremoverPlugin.prototype.ClearLog = function(params, success, fail) 
{
  return cordova.exec(function(args) 
  {
      success(args);
  }, 
  function(args) 
  {
      fail(args);
  }, 'LogremoverPlugin', params, [params]);
};



if(!window.plugins) {
  window.plugins = {};
}
if (!window.plugins.LogremoverPlugin) {
  window.plugins.LogremoverPlugin = new LogremoverPlugin();
}

/*
var HoneywellScannerPlugin = function() {};


HoneywellScannerPlugin.prototype.listenForScans = function(params, success, fail) 
{
  return cordova.exec(function(args) 
  {
      success(args);
  }, 
  function(args) 
  {
      fail(args);
  }, 'HoneywellScannerPlugin', params, [params]);
};

if(!window.plugins) {
	  window.plugins = {};
	}
	if (!window.plugins.HoneywellScannerPlugin) {
	  window.plugins.HoneywellScannerPlugin = new HoneywellScannerPlugin();
	}
*/


