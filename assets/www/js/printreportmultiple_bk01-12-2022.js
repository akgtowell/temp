var platform = sessionStorage.getItem("platform");
var ArrPrint = [];
var data = {};
var ReportName = {};
var customercode = "";
var alternatecode="";
var customername = "";
var customeradd1 = "";
var customeradd2 = "";
var customeradd3 = "";
var customeraddress = "";
var customerOutlet=0;
var paymenttype = "";
var invoicenumber = "";
var documentnumber = "";
var totalpromoamount = 0;
var totalinvoiceamount = 0;
var comments = "";
var printstatus = 0;
var splitfree=0;
var copyStatus=0;
var invheadermsg = "";
var invtrailormsg = "";
var headerAmount=0;
var printoutletitemcode=0;
var invoicepaymentterms="";
var excesspayment = "";
var splitfree=0;
var totalvaramount = 0;
var tcamount = 0;
var netinvalue= 0;
var netoutvalue = 0;
var itempromoamount=0;
var tranferflag = 0;
var manualfreediscount=0;
var transactionkey;
var arrRpt,currpoint;
var totalfinalamount=0;
var totaltax=0;
var totalTaxesAmount=0;
var itemlinetaxamount=0;
var totalSalesQty=0;
var totalDamagedQty=0;
var totalReturnQty=0;
var printtax=0;
var applytax=0;
var taxregistrationnumber=0;
var printbarcode=1;
var totalFreeQty=0;
var manualfreeflag=0;
var freesampleflag=0;
var manualfreeqty=0;
var freesampleqty=0;
var ReportName = new Object();
var totalTaxesAmount=0;
var itemlinetaxamount=0;
var totalSalesQty=0;
var totalDamagedQty=0;
var totalReturnQty=0;
var printtax=0;
var taxpercentage=1;
var totalFreeQty=0;
var manualfreeflag=0;
var freesampleflag=0;
var manualfreeqty=0;
var freesampleqty=0;
var totalsalesamount=0;

var PromoQty=0;
var FreeSampleQty=0;
var ManualFreeQty=0;
var ReturnFreeQty=0;
ReportName.OpeningLoad = "LoadSummary";
ReportName.LoadReport = "LoadSummary2";
ReportName.LoadTransfer = "Transfer_In";
ReportName.EndInventoryReport = "EndInventoryReport";
ReportName.Sales = "Sales";
ReportName.Order = "Order";
ReportName.Collection = "Collection";
ReportName.Deposit = "Deposit";
ReportName.DepositSlip = "DepositSlip";
ReportName.UnloadDamage = "UnloadDamage";
ReportName.SalesReport = "SalesReport";
ReportName.RouteActivity="RouteActivity";
ReportName.RouteSummary="RouteSummary";
ReportName.VanStockReport ="VanStockReport";
ReportName.VanStock = "VanStock";
ReportName.EndInventory = "EndInventory";
ReportName.LoadRequst = "LoadRequst";
ReportName.CreditSummary = "CreditSummary";
ReportName.CreditTempSummary = "CreditTempSummary";
ReportName.AdvancePayment = "AdvancePayment";
ReportName.ItemSalesSummary = "ItemSalesSummary";
ReportName.AgingAnalysis = "AgingAnalysis";
ReportName.AgingAnalysis = "AgingAnalysis";
ReportName.ReturnSummary = "ReturnSummary";   
ReportName.FreeSummary = "FreeSummary"; 
ReportName.StockMoveReport = "StockMoveReport"; 
ReportName.Order = "Order"; 

var printval = sessionStorage.getItem("printerval");
if(printval=='' || printval==0)
    getdecimal();
var decimalplace = sessionStorage.getItem("decimalplace");
if(decimalplace=='' || decimalplace==0)
getdecimal();
var arbcustomeraddress="";
function getdecimal() {
    platform = sessionStorage.getItem("platform");
            var qry ="SELECT currencymaster.decimalplaces,routemaster.routeprinter FROM routemaster INNER JOIN currencymaster ON routemaster.amountdecimaldigits = currencymaster.currencycode WHERE routemaster.routecode = " + sessionStorage.getItem("RouteCode");
           
            if (platform == 'iPad') {
                Cordova.exec(function(result) {
                    if (result.length > 0) {
			decimalplace = result[0][0];
			printval=result[0][1];
                        sessionStorage.setItem("decimalplace", result[0][0]);
                        sessionStorage.setItem("printerval", result[0][1]);//ADded for printer value by mirnah
                    }
                    else {
			decimalplace = 0;
			printval=0;
                        sessionStorage.setItem("decimalplace", 0);
                        sessionStorage.setItem("printerval", 0);//ADded for printer value by mirnah
                    }
                },
                        function(error) {
                            alert("Error in getting setup : " + error);
                        },
                        "PluginClass",
                        "GetdataMethod",
                        [qry]);
            }
            else if (platform == 'Android') {
                window.plugins.DataBaseHelper.select(qry, function(result) {
                
                    if (result.array != undefined) {
                        decimalplace = result.array[0].decimalplaces;
                        printval= result.array[0].routeprinter;
                        sessionStorage.setItem("decimalplace", result.array[0].decimalplaces);
                        sessionStorage.setItem("printerval", result.array[0].routeprinter);//ADded for printer value by mirnah
                    }
                    else {
                    	decimalplace =0;
                    	printval=0;
                        sessionStorage.setItem("decimalplace", 0);
                        sessionStorage.setItem("printerval", 0);//ADded for printer value by mirnah
                    }
                },
				function() {
				    console.warn("Error calling plugin");
				});
            }
        }
function ReportsToPrint(arrReport,i)
{
	 
    arrRpt = arrReport;
 
    currpoint = i
    console.log("Report Length : " + arrReport.length)
    console.log("current i : " + i);
    if(i <= (arrReport.length-1))
    {    	
        invoicenumber = (((arrReport[i].invoicenumber) != undefined) ? arrReport[i].invoicenumber : "");
        var key = (((arrReport[i].key) != undefined) ? arrReport[i].key : "");
        
        switch(arrReport[i].ReportName)
        {
            case "Load":
                PrintLoad(invoicenumber);
                break;
            case "Load Transfer":
                getLoadTransferInfo(key);
                break;
            case "Sales":                
                PrintSales()
                break;
            case "Collection":
            	getCollectionInfo();
                break;
            case "advance":
            	getAdvancePaymentInfo();
                break;
            case "SalesSummary":
                PrintSalesReport();
                break;
            case "Deposit":
                PrintDeposit(1);
                break;
            case "DepositSlip":
                PrintDeposit(2);
                break;
            case "UnloadDamage":
                PrintUnloadDamage();
                break;    
            case "Unload":
                getUnloadEndInventoryInfo();
                break;
            case "RouteActivity":
                PrintRouteActivityReport();
                break;
            case "RouteSummary":
                PrintRouteSummaryReport();
                break;
            case "EndInventoryReport":
                PrintEndInventoryReport();
                break;
            case "VanStockReport":
                PrintVanStockReport();
                break;
            case "Load Request":
                getRequestInfo(invoicenumber);
                break;
            case "CreditSummary":
                PrintCreditSummaryReport(1);
                break;
			case "CreditTempSummary":
                PrintCreditSummaryReport(2);
                break;
			case "ItemSalesSummary":
				GetItemSalesReport();
                break;	    
			case "AgingAnalysis":
				PrintAgingAnalysis(arrReport[i].customercode,arrReport[i].customername);
                break;
			case "ReturnSummary":
				getReturnSummary();
                break;  
			case "FreeSummary":
				getFreeSummary();
                break;  
			case "StockMoveReport":
				getStockMoveReport();
                break;  
			case "Order":
				printOrder();
				break;
        }        
    }
    
    if((arrRpt.length) == currpoint)
    {
        console.log("****** ARRPRINT ******");
        console.log(JSON.stringify(ArrPrint));
        PrintReport();
		//delet();
		//deviceall();
		//setTimeout(function() { PairedPrinter(); }, 3000);	
		
    }
}
//-----Checking Paired Devices-------
function PairedPrinter()
{

window.plugins.BluetoothHelper.fetch('1',function(data)
	{
	//alert(data.address);
	if(data.address == undefined)
	{
		alert("No Printer Is Paired");
		return false;
	}else
	{
		uproutemaster(data.address);
		
	}
	},
	function()
	{
   	 
	});
}
//Item Sales Summary Report
function GetItemSalesReport()
{
   // alert("test");

    data = {};
    //GetSalesData('sales');
    PrintReportItemSales();
   // alert("sel");
}

function PrintReportItemSales()
{
    //alert("v2");
    //getroutedate();
    var routekey = sessionStorage.getItem("RouteKey");
    //var Qry="SELECT CASE WHEN  '"+ sessionStorage.getItem("CheckCode")+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',description,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(invoicedqty AS INT)) ELSE (CAST(invoicedqty/unitspercase AS INT) || '/' || CAST(invoicedqty%unitspercase AS INT)) END AS saleqty ,((CAST(invoicedqty /unitspercase AS INT)*caseprice)+  (CAST(invoicedqty %unitspercase AS INT)*defaultsalesprice)) AS total FROM ( SELECT CASE WHEN '"+ sessionStorage.getItem("CheckCode")+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemshortdescription END AS 'description',unitspercase,IFNULL((IFNULL(isd.saleqty,0)),0) AS invoicedqty,im.caseprice as caseprice,im.defaultsalesprice as defaultsalesprice FROM inventorysummarydetail isd INNER JOIN itemmaster im ON im.actualitemcode = isd.itemcode where routekey ="+ sessionStorage.getItem("RouteKey") + ") order by itemcode";
    var Qry="SELECT itemcode,description,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(invoicedqty AS INT)) ELSE (CAST(invoicedqty/unitspercase AS INT) || '/' || CAST(invoicedqty%unitspercase AS INT)) END AS saleqty ,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(freeqty AS INT)) ELSE (CAST(freeqty/unitspercase AS INT) || '/' || CAST(freeqty%unitspercase AS INT)) END AS freesmplqty ,((CAST((invoicedqty+freeqty) /unitspercase AS INT)*caseprice)+  (CAST((invoicedqty+freeqty) %unitspercase AS INT)*defaultsalesprice)) AS total FROM ( SELECT CASE WHEN '"+ sessionStorage.getItem("CheckCode")+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemshortdescription END AS 'description',unitspercase,IFNULL((IFNULL(isd.saleqty,0)),0) AS invoicedqty,IFNULL((IFNULL(isd.freesampleqty,0)),0) AS freeqty,im.caseprice as caseprice,im.defaultsalesprice as defaultsalesprice FROM inventorysummarydetail isd INNER JOIN itemmaster im ON im.actualitemcode = isd.itemcode where routekey ="+ sessionStorage.getItem("RouteKey") + ") order by itemcode";
	// alert(Qry);
    console.log(Qry);
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length < 0)
                result = []
            ProcessReportVanStock(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == "Android") {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if (result.array != undefined) {  
                //alert("v2");
                result = $.map(result.array, function(item, index) {
                   // return [[item.itemcode,item.description,item.upc,item.avlqty,item.caseprice,item.defaultsalesprice]];
                    return [[item.itemcode,item.description,item.saleqty,item.freesmplqty,item.total]];
                });
            }
            else
                result = [];
            ProcessReportItemSales(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessReportItemSales(result) {
  
    var Headers = ["Item#","Description","Sale Qty","Free Qty","Total"];
    var Total = {"Sale Qty" : "0/0","Free Qty" : "0/0","Total":"0"};
    for (i = 0; i < result.length; i++) {
       
        for (j = 0; j < result[i].length; j++) {
            
            if (j == 0) {
            	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                    result[i][j] = parseInt(result[i][j]);
            	
                result[i][j] = (result[i][j]).toString();
            }
            if(j >= 2 && j<=4)
            {
	            var qtykey = "";
	            
	            if(j == 2){
	            	qtykey = "Sale Qty";
	            	 QuantityTotal(Total, qtykey, result[i][j]);
	            }else if(j == 3){
	            	qtykey = "Free Qty";
	            	 QuantityTotal(Total, qtykey, result[i][j]);
	            }
	            else{
	            	result[i][j] = eval(result[i][j]).toFixed(decimalplace);
					Total.Total = (eval(Total.Total) + eval(result[i][j])).toFixed(decimalplace);
	            }
            
            }
            
        }
    }
    //data["Load Number"] = loadperiodnumber.toString();
 
    data["HEADERS"] = Headers;
    data["TOTAL"] = [Total];
    data["totalamount"] = Total.Total;
    data["printstatus"] = getPrintStatus();
    data["data"] = result;
    getCommonData();
    console.log(JSON.stringify(data));
    //alert("v4");
    SetArray(ReportName.ItemSalesSummary);
    ReportsToPrint(arrRpt,currpoint+1);
    
   // PrintReport(ReportName.VanStock,data);   
}


//------Start Update Route master
function uproutemaster(macid)
{
//alert(macid);
var qry="Select memo1 FROM routemaster WHERE memo1='"+macid+"' and routecode=" + sessionStorage.getItem("RouteCode");
//alert(qry);
		window.plugins.DataBaseHelper.select(qry, function(result) {
						
                     if (result.array != undefined) {
						//alert("tes");	
						alldeviceinsert(macid);
						}
                    else {
					
						var Qry = "UPDATE routemaster SET memo1 = '"+macid+"' WHERE routecode=" + sessionStorage.getItem("RouteCode");
						//alert(Qry);
										window.plugins.DataBaseHelper.insert(Qry, function(result) 
										{   
										//alert("testsss");
											alldeviceinsert(macid);
										},
										function()
										{
											console.warn("Error calling plugin");
										});
			
                    }
                },
				function() {
				    console.warn("Error calling plugin");
				});

}
function alldeviceinsert(macid)
{

		var qry1="Select macid FROM printer WHERE macid='"+macid+"'";
		//alert(qry1);
		window.plugins.DataBaseHelper.select(qry1, function(result) {
						
                     if (result.array != undefined) {
						//alert("tes");	
						PrintReport();
						}
                    else {
						alert("Printer is not paired");
						return false;
                    }
                },
				function() {
				    console.warn("Error calling plugin");
				});
}
function selection()
{
	//alert("tsdfsdf");
}
function delet()
{
							var Qry = "Delete From printer";
									//alert(Qry);
										window.plugins.DataBaseHelper.insert(Qry, function(result) 
										{   
											deviceall();
										},
										function()
										{
											console.warn("Error calling plugin");
										});

}
function deviceall()
{
var flag=1;
window.plugins.BluetoothHelper.fetch('2',function(data)
			{
			
				//alert(data.address);
					//coords.push(data.address);
			       var Qry = "Insert into printer (macid) values ('"+data.address+"')";
						//alert(Qry);
										window.plugins.DataBaseHelper.insert(Qry, function(result) 
										{   
											//alldeviceinsert(macid);
										},
										function()
										{
											console.warn("Error calling plugin");
										});
					
			},
			function()
			{
			 
			});
}
//----End Update Route master

//----------End---------------

function PrintLoad(invoicenumber)
{
       
	data = {}
    var loadperiodnumber = "";
    var QryInv = "SELECT loadnumber,documentnumber,printstatus FROM inventorytransactionheader WHERE hhcdocumentnumber=" + invoicenumber;
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length > 0) {
                loadperiodnumber = result[0][0];
                documentnumber = parseInt(eval(result[0][1]));
                printstatus = result[0][2];
                data["DOCUMENT NO"] = documentnumber;
                data["Load Number"] = loadperiodnumber.toString();
                if (loadperiodnumber.toString() == "1") {
                    PrintOpeningLoad(invoicenumber);
                }
                else {
                    PrintCurrentLoad(invoicenumber);
                }
            }
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [QryInv]);
    }
    else if (platform == "Android") {
        window.plugins.DataBaseHelper.select(QryInv, function(result) {
            if (result.array != undefined) {
            	try
            	{
                result = $.map(result.array, function(item, index) {
                    return [[item.loadnumber,item.documentnumber,item.printstatus]];
                });
                loadperiodnumber = result[0][0];
                documentnumber = parseInt(eval(result[0][1]));
                printstatus = result[0][2];
                data["DOCUMENT NO"] = documentnumber;
                data["Load Number"] = loadperiodnumber.toString();
               
                Printvalue(invoicenumber,loadperiodnumber);
                
                
            	}
            	catch(ex)
            	{
            		alert(ex);
            	}
            }
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
function Printvalue(invoicenumber,loadperiodnumber)
{
   // alert("tst");       
    //var QryInv = "SELECT loadnumber,documentnumber,printstatus FROM inventorytransactionheader WHERE hhcdocumentnumber=" + invoicenumber;
   // var QryInv="SELECT DISTINCT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',im.caseprice as openingvalue,im.defaultsalesprice as loadvalue,CAST((((IFNULL(loadadjustqty/im.unitspercase, 0)) * im.caseprice) + (IFNULL(loadadjustqty % im.unitspercase, 0) * im.defaultsalesprice)) AS FLOAT )as adjustqtyvalue,CAST((((IFNULL((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))/im.unitspercase, 0)) * im.caseprice) + (IFNULL((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0)) % im.unitspercase, 0) * im.defaultsalesprice)) AS FLOAT )as netvalue FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invth.loadnumber=" + loadperiodnumber+" or invth.loadnumber=0 order by itemcode"
    var QryInv="SELECT DISTINCT im.actualitemcode,(CAST((COALESCE(beginstockqty, 0)/im.unitspercase) AS INT) * im.caseprice) + ((COALESCE(beginstockqty, 0) % im.unitspercase) * im.defaultsalesprice) AS openingvalue,(CAST((COALESCE(quantity, 0)/im.unitspercase) AS INT) * im.caseprice) + ((COALESCE(quantity, 0) % im.unitspercase) * im.defaultsalesprice) AS loadvalue,(CAST((COALESCE(loadadjustqty, 0)/im.unitspercase) AS INT) * im.caseprice) + ((COALESCE(loadadjustqty, 0) % im.unitspercase) * im.defaultsalesprice) AS adjustqtyvalue,((CAST((COALESCE(beginstockqty, 0)+COALESCE(quantity, 0)+COALESCE(loadadjustqty, 0)) AS INT) / im.unitspercase)* im.caseprice) + (((COALESCE(beginstockqty, 0)+COALESCE(quantity, 0)+COALESCE(loadadjustqty, 0)) % im.unitspercase)* im.defaultsalesprice) AS netvalue FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invth.loadnumber=" + loadperiodnumber+"  order by actualitemcode";
   
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length > 0) {
                loadperiodnumber = result[0][0];
                documentnumber = parseInt(eval(result[0][1]));
                printstatus = result[0][2];
                data["DOCUMENT NO"] = documentnumber;
                data["Load Number"] = loadperiodnumber.toString();
                if (loadperiodnumber == "1") {
                    PrintOpeningLoad(invoicenumber);
                }
                else {
                    PrintCurrentLoad(invoicenumber);
                }
            }
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [QryInv]);
    }
    else if (platform == "Android") {
        var openingvalue= 0;
        var loadvalue= 0;
        var adjustvalue= 0;
        var netvalue= 0;
        window.plugins.DataBaseHelper.select(QryInv, function(result) {
            if (result.array != undefined) {
                try
                {
                result = $.map(result.array, function(item, index) {
                    return [[item.itemcode,item.openingvalue,item.loadvalue,item.adjustqtyvalue,item.netvalue]];
                });
                for(i = 0; i < result.length; i++) {
                    
                    for (j = 0; j < result[i].length; j++) {
                       
                            if (j == 4)
                                {
                                 result[i][j] = (eval(result[i][4])).toFixed(decimalplace);
                                 netvalue = (eval(netvalue) + eval(result[i][j])).toFixed(decimalplace);
                                }           
                             if(j==1){
                            	 result[i][j] = (eval(result[i][1])).toFixed(decimalplace);
                            	 openingvalue = (eval(openingvalue) + eval(result[i][j])).toFixed(decimalplace);
                            	 
                             }
                             if(j==2){
                            	 result[i][j] = (eval(result[i][2])).toFixed(decimalplace);
                            	 loadvalue = (eval(loadvalue) + eval(result[i][j])).toFixed(decimalplace);
                            	 
                             }
                    }
                }
              
                data["OpenValue"] = openingvalue.toString();
                data["LoadValue"] = loadvalue.toString();
                data["netvalue"] = netvalue.toString();
                //alert(sessionStorage.getItem("CheckCode"));
                //data["DOCUMENT NO"] = documentnumber;
               // data["Load Number"] = loadperiodnumber.toString();
                if (loadperiodnumber == "1") {
                 //   alert("ues");
                    PrintOpeningLoad(invoicenumber,loadperiodnumber);
                }
                else {
                    PrintCurrentLoad(invoicenumber);
                }
                }
                catch(ex)
                {
                    alert(ex);
                }
            }
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
function PrintOpeningLoad(invoicenumber,loadperiodnumber) {    
     // alert("test");
     //var Qry = "SELECT DISTINCT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',itemdescription AS 'description',(IFNULL(CAST((beginstockqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((beginstockqty%unitspercase) AS INT),0)) AS 'openqty',(IFNULL(CAST((invtd.quantity/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((invtd.quantity%unitspercase) AS INT),0)) AS 'loadqty',(IFNULL(CAST((loadadjustqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((loadadjustqty%unitspercase) AS INT),0)) AS 'adjustqty', (CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))/unitspercase) AS INT) ||  '/' || CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))%unitspercase) AS INT)) AS 'netqty' FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE (invth.loadnumber=" + loadperiodnumber+") or invth.loadnumber=0 order by itemcode";
    
    
	// var Qry = "SELECT CASE WHEN 'ancode' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', itemdescription AS 'description',IFNULL(CAST((SUM(inv.openqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.openqty)%im.unitspercase) AS INT),0) AS 'openqty',IFNULL(CAST((SUM(inv.loadqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.loadqty)%im.unitspercase) AS INT),0) AS 'loadqty',	IFNULL(CAST((SUM(inv.adjustqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.adjustqty)%im.unitspercase) AS INT),0) AS 'adjustqty', IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))%im.unitspercase) AS INT),0) AS 'netqty' FROM (SELECT id.itemcode, id.quantity openqty, 0 loadqty, 0 adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 1 AND ih.loadnumber = 0 AND id.transactiontypecode = 12 UNION ALL SELECT id.itemcode, 0 openqty, id.quantity loadqty, 0 adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 1 AND ih.loadnumber = 1 AND id.transactiontypecode = 1 UNION ALL SELECT id.itemcode, 0 openqty, 0 loadqty, id.quantity adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 5 AND ih.loadnumber = 0 AND id.transactiontypecode = 23 ) inv INNER JOIN itemmaster im ON im.Actualitemcode = inv.itemcode GROUP BY inv.itemcode, im.itemdescription, im.unitspercase order by itemcode";
	
	//Query With UPC and net Value
	
	//	var Qry="SELECT CASE WHEN  '"
	//			+ sessionStorage.getItem("CheckCode")
	//			+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', itemdescription AS 'description',im.unitspercase AS UPC, IFNULL(CAST((SUM(inv.openqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.openqty)%im.unitspercase) AS INT),0) AS 'openqty', IFNULL(CAST((SUM(inv.loadqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.loadqty)%im.unitspercase) AS INT),0) AS 'loadqty', IFNULL(CAST((SUM(inv.adjustqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.adjustqty)%im.unitspercase) AS INT),0) AS 'adjustqty', IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))%im.unitspercase) AS INT),0) AS 'netqty', ((CAST((COALESCE(SUM(inv.openqty), 0)+COALESCE(SUM(inv.loadqty), 0)+COALESCE(SUM(adjustqty), 0)) AS INT) / im.unitspercase)* im.caseprice) + (((COALESCE(SUM(inv.openqty), 0)+COALESCE(SUM(inv.loadqty), 0)+COALESCE(SUM(adjustqty), 0)) % im.unitspercase)* im.defaultsalesprice) AS 'netvalue' FROM (SELECT id.itemcode, id.quantity openqty, 0 loadqty, 0 adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 1 AND ih.loadnumber = 0 AND id.transactiontypecode = 12 UNION ALL SELECT id.itemcode, 0 openqty, id.quantity loadqty, 0 adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 1 AND ih.loadnumber = 1 AND id.transactiontypecode = 1 UNION ALL SELECT id.itemcode, 0 openqty, 0 loadqty, id.quantity adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 5 AND ih.loadnumber = 0 AND id.transactiontypecode = 23) inv INNER JOIN itemmaster im ON im.Actualitemcode = inv.itemcode GROUP BY inv.itemcode, im.itemdescription, im.unitspercase order by itemcode";
	//	var Qry="SELECT CASE WHEN  '"
	//		+ sessionStorage.getItem("CheckCode")
	//		+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN itemdescription ELSE arbitemdescription END AS 'description',im.unitspercase AS UPC, IFNULL(CAST((SUM(inv.openqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.openqty)%im.unitspercase) AS INT),0) AS 'openqty', IFNULL(CAST((SUM(inv.loadqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.loadqty)%im.unitspercase) AS INT),0) AS 'loadqty', IFNULL(CAST((SUM(inv.adjustqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.adjustqty)%im.unitspercase) AS INT),0) AS 'adjustqty', IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))%im.unitspercase) AS INT),0) AS 'netqty', ((CAST((COALESCE(SUM(inv.loadqty), 0)) AS INT) / im.unitspercase)* im.caseprice) + (((COALESCE(SUM(inv.loadqty), 0)) % im.unitspercase)* im.defaultsalesprice) AS 'netvalue',0 as sl FROM (SELECT id.itemcode, id.quantity openqty, 0 loadqty, 0 adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 1 AND ih.loadnumber = 0 AND id.transactiontypecode = 12 UNION ALL SELECT id.itemcode, 0 openqty, id.quantity loadqty, 0 adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 1 AND ih.loadnumber = 1 AND id.transactiontypecode = 1 UNION ALL SELECT id.itemcode, 0 openqty, 0 loadqty, id.quantity adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 5 AND ih.loadnumber = 0 AND id.transactiontypecode = 23) inv INNER JOIN itemmaster im ON im.Actualitemcode = inv.itemcode GROUP BY inv.itemcode, im.itemdescription, im.unitspercase order by itemcode";
	
	// org qry sujee commented 04/03/2020
	//var Qry="SELECT CASE WHEN  '"+ sessionStorage.getItem("CheckCode")+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',itemdescription  AS 'description',arbitemshortdescription AS 'arbitemdescription',im.unitspercase AS UPC,CASE WHEN im.unitspercase=1 THEN 0 || '/' || IFNULL(CAST((SUM(inv.openqty)) AS INT),0) ELSE IFNULL(CAST((SUM(inv.openqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.openqty)%im.unitspercase) AS INT),0) END AS 'openqty',CASE WHEN im.unitspercase=1 THEN 0 || '/' || IFNULL(CAST((SUM(inv.loadqty)) AS INT),0) ELSE IFNULL(CAST((SUM(inv.loadqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.loadqty)%im.unitspercase) AS INT),0) END AS 'loadqty',CASE WHEN im.unitspercase=1 THEN 0 || '/' || IFNULL(CAST((SUM(inv.adjustqty)) AS INT),0) ELSE IFNULL(CAST((SUM(inv.adjustqty)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST((SUM(inv.adjustqty)%im.unitspercase) AS INT),0) END AS 'adjustqty',CASE WHEN im.unitspercase=1 THEN  0 || '/' || IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))) AS INT),0) ELSE IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SUM(inv.openqty) + SUM(inv.loadqty) + SUM(adjustqty))%im.unitspercase) AS INT),0) END AS 'netqty', ((CAST((COALESCE(SUM(inv.loadqty), 0)) AS INT) / im.unitspercase)* im.caseprice) + (((COALESCE(SUM(inv.loadqty), 0)) % im.unitspercase)* im.defaultsalesprice) AS 'netvalue',0 as sl FROM (SELECT id.itemcode, id.quantity openqty, 0 loadqty, 0 adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 1 AND ih.loadnumber = 0 AND id.transactiontypecode = 12 UNION ALL SELECT id.itemcode, 0 openqty, id.quantity loadqty, 0 adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 1 AND ih.loadnumber = 1 AND id.transactiontypecode = 1 UNION ALL SELECT id.itemcode, 0 openqty, 0 loadqty, id.quantity adjustqty FROM inventorytransactionheader ih INNER JOIN inventorytransactiondetail id ON id.detailkey = ih.detailkey WHERE ih.transactiontype = 5 AND ih.loadnumber = 0 AND id.transactiontypecode = 23) inv INNER JOIN itemmaster im ON im.Actualitemcode = inv.itemcode GROUP BY inv.itemcode, im.itemdescription, im.unitspercase order by itemcode";
	
	Qry=" SELECT DISTINCT CASE WHEN '"+ sessionStorage.getItem("CheckCode")+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',itemdescription AS 'description',arbitemshortdescription AS 'arbitemdescription',im.unitspercase AS UPC, CASE WHEN unitspercase=1 THEN 0 ||  '/' || IFNULL(CAST((beginstockqty) AS INT),0) ELSE (IFNULL(CAST((beginstockqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((beginstockqty%unitspercase) AS INT),0)) END AS 'openqty', CASE WHEN unitspercase=1 THEN 0 ||  '/' || IFNULL(CAST((loadqty) AS INT),0) ELSE (IFNULL(CAST((loadqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((loadqty%unitspercase) AS INT),0)) END AS 'loadqty', CASE WHEN unitspercase=1 THEN (0 ||  '/' || IFNULL(CAST((loadadjustqty) AS INT),0)) ELSE (IFNULL(CAST((loadadjustqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((loadadjustqty%unitspercase) AS INT),0)) END AS 'adjustqty',  CASE WHEN unitspercase=1 THEN 0 ||  '/' || CAST(IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0) AS INT) ELSE (CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))/unitspercase) AS INT) ||  '/' || CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))%unitspercase) AS INT)) END AS 'netqty', (((ifnull(beginstockqty,0) +  ifnull(loadqty,0)) / im.unitspercase)* im.caseprice) + (((ifnull(beginstockqty,0) +  ifnull(loadqty,0)) % im.unitspercase)* im.defaultsalesprice) as netvalue, 0 as sl FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode in(1,12) and im.itemtype=1 group by invtd.itemcode ";

    console.log(Qry);
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length < 0)
                result = []
            ProcessOpeningLoad(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == "Android") {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if (result.array != undefined) {            
                result = $.map(result.array, function(item, index) {
                   // return [[item.sl,item.itemcode, item.description,item.UPC, item.openqty, item.loadqty, item.adjustqty, item.netqty,item.netvalue,item.arbitemdescription]];
                    return [[item.sl,item.itemcode, item.description,item.UPC, item.openqty, item.loadqty, item.adjustqty, item.netqty,item.netvalue]];
                });
            }
            else
                result = [];
            ProcessOpeningLoad(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessOpeningLoad(result) {
    var Headers = ["Sl#","Item#", "Description","UPC", "Begin Inv", "Load Qty", "Adjust Qty", "Net Qty","VALUE"];
    var Total = { "Begin Inv": "0/0", "Load Qty": "0/0", "Adjust Qty": "0/0", "Net Qty": "0/0","VALUE" : "0"}
    var TotalAmount = 0;
    for (i = 0; i < result.length; i++) {
        for (j = 0; j < result[i].length; j++) {
            if (j == 1) {
            	
            	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                    result[i][j] = parseInt(result[i][j],10);
            	
                result[i][j] = (result[i][j]).toString();
              
            }
            if (j >=4 && j <= 7) {
                var qtykey = "";
                if (j == 4)
                    qtykey = "Begin Inv";
                if (j == 5)
                    qtykey = "Load Qty";
                if (j == 6)
                    qtykey = "Adjust Qty";
                if (j == 7)
                    qtykey = "Net Qty";
               
                
                QuantityTotal(Total, qtykey, result[i][j]);
            }
            
            if(j == 8){
            	
            	 result[i][j] = eval(result[i][j]).toFixed(decimalplace);
            	 TotalAmount = (eval(TotalAmount) + eval(result[i][j])).toFixed(decimalplace);
            }
               
           
        }
    }
    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
    Total["VALUE"] = TotalAmount.toString();
    data["HEADERS"] = Headers;
    data["TOTAL"] = [Total]; 
    data["printstatus"] = getPrintStatus();
    data["data"] = result;    
    getCommonData();
    SetArray(ReportName.OpeningLoad);    
    //console.log(JSON.stringify(data));
    ReportsToPrint(arrRpt,currpoint+1);
}
//--------------------
//---------Start Van Stock By Sujitv 9/1/2014
function getRequestInfo(docnumber)
{
    data = {};
    
    var Qry = "SELECT documentnumber,printstatus,requestdate FROM inventorytransactionheader WHERE hhcdocumentnumber = " + docnumber + "";
    
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {   
            if(result.length > 0)
            {
                documentnumber = parseInt(eval(result[0][0]));
                printstatus = result[0][1];
                requestdate=result[0][2];
            }
            PrintRequestStock(docnumber);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                documentnumber = parseInt(result.array[0].documentnumber);
                printstatus = result.array[0].printstatus;
                requestdate=result.array[0].requestdate;
            }    
            //
            var mystring = requestdate;
            var splits = mystring.split("-");
            /*var month = splits[1].length > 1 ? splits[1] : '0' + splits[1];
            var day = splits[2].length > 1 ? splits[2] : '0' + splits[2];
            var year = splits[0];*/
            //alert(splits[0]);
            //alert(splits[1]);
            //alert(splits[2]);
          //  rdate = 
            //
            var day=splits[2];
            var month=splits[1];
            var year=splits[0];
            var dt1 = day + "/" + month + "/" + year;
            data["Requestdate"] = dt1;
            PrintLoadReqValue(docnumber);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

//
function PrintLoadReqValue(docnumber)
{    
    
   
    var Qry = "SELECT  itemcode,CAST(((CAST((IFNULL((invtd.quantity)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((invtd.quantity) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as netvalue FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN itemmaster im ON im.actualitemcode = invtd.itemcode WHERE invth.transactiontype = 4  AND invth.hhcdocumentnumber = " + docnumber + "";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {
            if(result.length <= 0)
                result = [];
            
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        var netvalue=0;
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                    return [[item.itemcode,item.netvalue]];    
                });
                for (i = 0; i < result.length; i++) {
                    for (j = 0; j < result[i].length; j++) {    
                            if (j == 1)
                                {
                                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                                
                                    netvalue = (eval(netvalue) + eval(result[i][j])).toFixed(decimalplace);
                                    
                                
                                }           
                             
                    }
                }
                data["netvalue"]=netvalue;
                getTripDate(docnumber);
                
            }
            else
                result = [];           
            
        
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
function getTripDate(docnumber){
	
	var qry = "select strftime('%d/%m/%Y',routestartdate) as routestartdate from startendday where routekey="
		+ sessionStorage.getItem("RouteKey");
	if (platform == 'iPad') {
		Cordova.exec(function(result) {
			var startdate = result[0][0];
			data["TRIP START DATE"] = startdate;
			
		}, function(error) {
			alert(error);
		}, "PluginClass", "GetdataMethod", [ qry ]);
	} else if (platform == 'Android') {
		window.plugins.DataBaseHelper.select(qry, function(result) {
			if (result.array != undefined) {
				result = $.map(result.array, function(item, index) {
					return [ [ item.routestartdate ] ];
				});
				var startdate = result[0][0];
				data["TRIP START DATE"] = startdate;
			}
			PrintRequestStock(docnumber);
		}, function() {
			console.warn("Error calling plugin");
		});
	}
	
	
}

function PrintRequestStock(docnumber)
{
  
   
   
    var routekey = sessionStorage.getItem("RouteKey");
    //var availqty="ifnull(inventorysummarydetail.loadqty,0)+ifnull(inventorysummarydetail.loadadjustqty,0)+ifnull(inventorysummarydetail.beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0)";
    // Qry = "SELECT DISTINCT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',itemdescription AS 'description',(IFNULL(CAST((loadqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((loadqty%unitspercase) AS INT),0)) AS 'loadqty',(IFNULL(CAST((loadadjustqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((loadadjustqty%unitspercase) AS INT),0)) AS 'soldqty', (CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))/unitspercase) AS INT) ||  '/' || CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))%unitspercase) AS INT)) AS 'avlqty' FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invth.loadnumber=" + loadperiodnumber+" or invth.loadnumber=0" ;
    //Qry="select CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN itemmaster.alternatecode ELSE itemmaster.actualitemcode END as itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN itemdescription ELSE arbitemdescription END AS 'description',(IFNULL(CAST((("+availqty+")/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((("+availqty+")%unitspercase) AS INT),0)) AS 'avlqty',caseprice,defaultsalesprice,unitspercase as upc,itemmaster.actualitemcode from itemmaster left join inventorysummarydetail on itemmaster.actualitemcode=inventorysummarydetail.itemcode where routekey="+routekey+" and ("+availqty+") > 0 order by CASE printsequenceroute WHEN 0 THEN 100000 ELSE printsequenceroute END";
    var Qry ="SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN itemdescription ELSE arbitemshortdescription END AS 'description',unitspercase,caseprice,defaultsalesprice,(CASE WHEN unitspercase=1 THEN 0 ELSE CAST(invtd.quantity/unitspercase AS INT) END) || '/' || (CASE WHEN unitspercase=1 THEN CAST(invtd.quantity AS INT) ELSE CAST(invtd.quantity%unitspercase AS INT) END) AS reqstockqty,0 as sl FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN itemmaster im ON im.actualitemcode = invtd.itemcode WHERE invth.transactiontype = 4 AND invth.hhcdocumentnumber = " + docnumber + "";
    console.log(Qry);
    
    
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length < 0)
                result = []
            ProcessRequestStock(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == "Android") {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if (result.array != undefined) {            
                result = $.map(result.array, function(item, index) {
                   // return [[item.itemcode,item.description,item.upc,item.avlqty,item.caseprice,item.defaultsalesprice]];
                    return [[item.sl,item.itemcode,item.description,item.unitspercase,item.caseprice,item.defaultsalesprice,item.reqstockqty]];
                    
                });
            }
            else
                result = [];
            ProcessRequestStock(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessRequestStock(result) {
 
    var Headers = ["Sl#","Item#","Description","UPC","Case Price","Unit Price","Request Qty"];
    var Total = {"Request Qty" : "0/0"};
    for (i = 0; i < result.length; i++) {
        
        for (j = 0; j < result[i].length; j++) {
            
            if (j == 1) {
            	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                    result[i][j] = parseInt(result[i][j]);
            	
                result[i][j] = (result[i][j]).toString();
            }
            if(j ==4 || j == 5)
            {                
                if(result[i][j] != "")
                    result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
            }
            if(j == 6)
            {
            var qtykey = "";
           // if(j == 2)
                qtykey = "Request Qty";        
            
            QuantityTotal(Total,qtykey,result[i][j]);
            
            }
            
        }
    }
   // data["Load Number"] = loadperiodnumber.toString();
    data["DOCUMENT NO"] = documentnumber;
    
    data["HEADERS"] = Headers;
    data["TOTAL"] = [Total];
    data["printstatus"] = getPrintStatus();
    data["data"] = result;
   
    getCommonData();
    
    console.log(JSON.stringify(data));
  //  alert(ReportName.VanStock);
       
    SetArray(ReportName.LoadRequst);    
    //console.log(JSON.stringify(data));
    ReportsToPrint(arrRpt,currpoint+1);
}

//---------End Van Stock 
//-------------------End Load Request
//--------------------
//--------------------Start


function PrintUnloadDamage() { 
   // alert("test");
    data = {}
     
    //var Qry = "SELECT DISTINCT CASE WHEN 'ancode' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', im.itemdescription AS description, im.unitspercase as unitspercase,IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%EXP%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%EXP%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT),0) AS 'unloadexpiryqty',IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%EXP%' WHERE ret.itemcode = rxd.itemcode) AS INT),0) AS 'ExpiredQty',IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT),0) AS 'undamageqty',IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode) AS INT),0) AS 'damageqty',IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%EXP%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%EXP%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT),0) AS 'otherqty',IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%EXP%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode) AS INT),0) AS 'otheredQty' FROM invoicerxddetail rxd INNER JOIN itemmaster im ON im.actualitemcode = rxd.itemcode WHERE itemtransactiontype = 2 ORDER BY im.actualitemcode";
    //var Qry="SELECT DISTINCT CASE WHEN 'ancode' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', im.itemdescription AS description, im.unitspercase AS unitspercase, IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0))%im.unitspercase) AS INT),0) AS 'unloadexpiryqty', IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0)) AS INT),0) AS 'ExpiredQty',  IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0))%im.unitspercase) AS INT),0) AS 'undamageqty', IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0)) AS INT),0) AS 'damageqty',  IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0))%im.unitspercase) AS INT),0) AS 'otherqty', IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0)) AS INT),0) AS 'otheredQty'  FROM invoicerxddetail rxd INNER JOIN itemmaster im ON im.actualitemcode = rxd.itemcode WHERE itemtransactiontype = 2 ORDER BY im.actualitemcode";
    //sujee commented 15/04/2020 for void transaction 
    //var Qry="SELECT DISTINCT CASE WHEN '"+sessionStorage.getItem("CheckCode")+"' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemdescription END AS 'description',im.unitspercase AS unitspercase,IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0))%im.unitspercase) AS INT),0) AS 'undamageqty',IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE COALESCE(ih.voidflag,0) = 0)) AS INT),0) AS 'damageqty' FROM invoicerxddetail rxd INNER JOIN itemmaster im ON im.actualitemcode = rxd.itemcode WHERE itemtransactiontype = 2 ORDER BY im.actualitemcode";
    
    // sujee commented 15/09/2020
 //   var Qry="SELECT DISTINCT CASE WHEN '"+sessionStorage.getItem("CheckCode")+"' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemdescription END AS 'description',im.unitspercase AS unitspercase,IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE ih.voidflag is null))/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE ih.voidflag is null))%im.unitspercase) AS INT),0) AS 'undamageqty', IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE ih.voidflag is null)) AS INT),0) AS 'damageqty' FROM invoicerxddetail rxd INNER JOIN itemmaster im ON im.actualitemcode = rxd.itemcode WHERE itemtransactiontype = 2 and damageqty>0 group BY im.actualitemcode";
    
    var Qry="SELECT DISTINCT CASE WHEN '"+sessionStorage.getItem("CheckCode")+"' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemdescription END AS 'description',im.unitspercase AS unitspercase, IFNULL(CAST((SELECT SUM(ret.quantity)/im.unitspercase FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE err.code=2 and ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE ih.voidflag is null)) AS INT),0) || '/' ||  IFNULL(CAST((SELECT SUM(ret.quantity)%im.unitspercase FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE err.code=2 and ret.itemcode = rxd.itemcode AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE ih.voidflag is null)) AS INT),0) AS 'expiryqty',IFNULL(CAST((SELECT SUM(ret.quantity)/im.unitspercase FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE err.code=1 and ret.itemcode = rxd.itemcode and ret.itemtransactiontype = 2  AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE ih.voidflag is null)) AS INT),0) || '/' ||  IFNULL(CAST((SELECT SUM(ret.quantity)%im.unitspercase FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode WHERE err.code=1 and ret.itemcode = rxd.itemcode and ret.itemtransactiontype = 2 AND ret.transactionkey IN (SELECT ih.transactionkey FROM invoiceheader ih WHERE ih.voidflag is null)) AS INT),0) AS 'damageqty' FROM invoicerxddetail rxd INNER JOIN itemmaster im ON im.actualitemcode = rxd.itemcode WHERE itemtransactiontype = 2  group BY im.actualitemcode";
    console.log(Qry);
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length < 0)
                result = []
            ProcessUnloadDamage(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == "Android") {
        //alert("andin");
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if (result.array != undefined) {            
                result = $.map(result.array, function(item, index) {
                    
                    //return [[item.itemcode,item.description,item.unitspercase,item.damageqty]];
                	return [[item.itemcode,item.description,item.unitspercase,item.expiryqty,item.damageqty]];
                });
            }
            else
                result = [];
            ProcessUnloadDamage(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function getReasons(result){
	
	 var Qry = "select * from expiryreturnreasons order by code"; 
	    console.log(Qry);
	    if (platform == "iPad") {
	        Cordova.exec(function(result) {
	            if (result.length < 0)
	                result = []
	            ProcessUnloadDamage(result);
	        },
	        function(error) {
	            alert(error);
	        },
	        "PluginClass",
	        "GetdataMethod",
	        [Qry]);
	    }
	    else if (platform == "Android") {
	        //alert("andin");
	        window.plugins.DataBaseHelper.select(Qry, function(result) {
	            if (result.array != undefined) {            
	                result = $.map(result.array, function(item, index) {
	                    
	                    return [[item.code,item.description]];
	                });
	                ProcessUnloadDamage(result,result[0][0],result[1][0],result[2][0]);
	            }
	            else
	                result = [];
	            
	          
	        },
	        function() {
	            console.warn("Error calling plugin");
	        });
	    }
	
	
}

function ProcessUnloadDamage(result) {
    //var Headers = ["ITEM#", "DESCRIPTION","UPC","DAMAGE IN PCS"]
    //var Total = {"DAMAGE IN PCS":"0"}
	 var Headers = ["ITEM#", "DESCRIPTION","UPC","EXPIRY QTY","DAMAGE QTY"]
	    var Total = {"EXPIRY QTY":"0/0","DAMAGE QTY":"0/0"}
    for (i = 0; i < result.length; i++) {
        for (j = 0; j < result[i].length; j++) {
            if (j == 0) {
            	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                    result[i][j] = parseInt(result[i][j],10);
                
                result[i][j] = (result[i][j]).toString();
            }
            var qtykey1 = "";
               
             
            if (j == 3)
            {
	        	qtykey1 = "EXPIRY QTY";
	        	QuantityTotal(Total, qtykey1, result[i][j]);
            }
	        if (j == 4)
            {
	        	qtykey1 = "DAMAGE QTY";
	        	QuantityTotal(Total, qtykey1, result[i][j]);
            }
            
            
               
           
        }
    }
    data["HEADERS"] = Headers;
    data["TOTAL"] = [Total]; 
    data["printstatus"] = getPrintStatus();
    data["data"] = result;   
  
    processUnloadDamageTotal()
   
   
}
function processUnloadDamageTotal(){
	
	// commented 15/04/2020 void flad added 
//	var Qry="SELECT SUM(IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)%im.unitspercase) AS INT)*im.defaultsalesprice,0)) AS unloadexpiryvalue, SUM(IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)%im.unitspercase) AS INT)*im.defaultsalesprice,0)) AS damagevalue, SUM(IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)%im.unitspercase) AS INT)*im.defaultsalesprice,0)) AS Othervalue FROM itemmaster im WHERE im.actualitemcode IN (SELECT DISTINCT rxd.itemcode FROM invoicerxddetail rxd WHERE rxd.itemtransactiontype = 2)";
	
	var Qry="SELECT SUM(IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)%im.unitspercase) AS INT)*im.defaultsalesprice,0)) AS unloadexpiryvalue, SUM(IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)%im.unitspercase) AS INT)*im.defaultsalesprice,0)) AS damagevalue, SUM(IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = im.actualitemcode AND ret.itemtransactiontype=2)%im.unitspercase) AS INT)*im.defaultsalesprice,0)) AS Othervalue FROM itemmaster im WHERE im.actualitemcode IN (SELECT DISTINCT rxd.itemcode FROM invoicerxddetail rxd  inner join invoiceheader ih on ih.transactionkey=rxd.transactionkey WHERE ih.voidflag is null and  rxd.itemtransactiontype = 2)";
    
    //Query Chnaged on 21-10-2014 for Total of Stales
    //var Qry="SELECT DISTINCT CASE WHEN 'ancode' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', im.itemdescription AS description, im.unitspercase AS unitspercase,im.caseprice,im.defaultsalesprice, IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT),0) AS 'unloadexpiryqty', IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode) AS INT),0) AS 'ExpiredQty', IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT)*im.defaultsalesprice,0) AS 'unloadexpiryvalue',  IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT),0) AS 'undamageqty', IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode) AS INT),0) AS 'damageqty', IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT)*im.defaultsalesprice,0) AS 'damagevalue',  IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT),0) || '/' || IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT),0) AS 'otherqty', IFNULL(CAST((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description NOT LIKE '%STA%' AND err.description NOT LIKE '%DAM%' WHERE ret.itemcode = rxd.itemcode) AS INT),0) AS 'otheredQty' , IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode)/im.unitspercase) AS INT)*im.caseprice,0) + IFNULL(CAST(((SELECT SUM(ret.quantity) FROM invoicerxddetail ret INNER JOIN expiryreturnreasons err ON err.code = ret.reasoncode AND err.description LIKE '%STA%' WHERE ret.itemcode = rxd.itemcode)%im.unitspercase) AS INT)*im.defaultsalesprice,0) AS 'Othervalue'  FROM invoicerxddetail rxd INNER JOIN itemmaster im ON im.actualitemcode = rxd.itemcode WHERE itemtransactiontype = 2 ORDER BY im.actualitemcode";
    console.log(Qry);
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length < 0)
                result = []
            ProcessUnloadDamage(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == "Android") {
        //alert("andin");
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if (result.array != undefined) {            
                result = $.map(result.array, function(item, index) {
                    
                    return [[item.unloadexpiryvalue,item.damagevalue,item.Othervalue]];
                });
                data["TOTAL_EXPIRY_VALUE"] = eval(result[0][0]).toFixed(decimalplace); 
                data["TOTAL_DAMAGE_VALUE"] =  eval(result[0][1]).toFixed(decimalplace);
                data["TOTAL_OTHER_VALUE"] = eval(result[0][2]).toFixed(decimalplace);
                data["TOTAL_STALES_VAR"] = eval(0).toFixed(decimalplace);
                
            }
            else
                result = [];
            
            getDamageVariance();
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
	
	
}



function getDamageVariance()
{
	var calculateqty = "ifnull(badreturnvariance,0)";
	var varianceAmount=0;
	var CaseEnabled;
	if (sessionStorage.getItem("CaseEnabled") == "0" || sessionStorage.getItem("CaseEnabled") == null)
	{
		CaseEnabled = false;
	}else if (sessionStorage.getItem("CaseEnabled") == "1")
	{
	    CaseEnabled = true;
	}	
     
    if (CaseEnabled) {
        var Qry ="select sum((("+calculateqty+"%unitspercase)*defaultsalesprice)+(("+calculateqty+"/unitspercase)*caseprice)) as amount from transactiondetailtemp join itemmaster on itemmaster.actualitemcode=transactiondetailtemp.itemcode";
    }
    else
    {
        var Qry ="select sum((("+calculateqty+"/unitspercase)*caseprice)) as amount from transactiondetailtemp join itemmaster on itemmaster.actualitemcode=transactiondetailtemp.itemcode";
    }
   
    if (platform == 'iPad') {
        Cordova.exec(function(result) {
                     	//need to develope for ios
                      },
                      function(error) {
                      alert(error);
                      },
                      "PluginClass",
                      "GetdataMethod",
                      [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
        	
        									if (result.array != undefined) {     	
                                             var array = $.map(result.array, function(item, index) {
                                                              
                                            	 				return [[item.amount]];
                                                             
                                                               });
                                             	varianceAmount=parseInt(CheckIsNaN(array[0][0]));
        									}else{
        										
        										varianceAmount=0;
        									}	
                                                data["damagevariance"] =  eval(varianceAmount).toFixed(decimalplace);
                                                
                                               
                                             	console.warn("VarianceAmount"+varianceAmount);
                                             	getCommonData();
                                                SetArray(ReportName.UnloadDamage);    
                                                //console.log(JSON.stringify(data));
                                                ReportsToPrint(arrRpt,currpoint+1);
                                             },
                                             function() {
                                            	 	console.warn("Error calling plugin");
                                             });
    }
    
    

}
function CheckIsNaN(val)
{
    if(isNaN(val))
    return 0;
    else
    return val;
}
//--------------------End
function PrintCurrentLoad(invoicenumber) {
	
    //var Qry = "SELECT DISTINCT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',itemdescription AS 'description',im.unitspercase AS UPC,(IFNULL(CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0)-IFNULL(invtd.quantity,0))/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0)-IFNULL(invtd.quantity,0))%unitspercase) AS INT),0)) AS 'vanqty',(IFNULL(CAST((IFNULL(invtd.quantity,0)/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((invtd.quantity%unitspercase) AS INT),0)) AS 'loadqty',(CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))/unitspercase) AS INT) ||  '/' || CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))%unitspercase) AS INT)) AS 'netqty',((CAST(COALESCE(invtd.quantity, 0) AS INT) / im.unitspercase)* im.caseprice) + ((COALESCE(invtd.quantity, 0) % im.unitspercase)* im.defaultsalesprice) AS 'netvalue',0 as sl FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invth.hhcdocumentnumber=" + invoicenumber+" order by itemcode";
	var Qry="SELECT DISTINCT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',itemdescription  AS 'description',arbitemshortdescription AS 'arbitemdescription',im.unitspercase AS UPC,CASE WHEN unitspercase=1 THEN (0 ||  '/' || IFNULL(CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0)-IFNULL(invtd.quantity,0))) AS INT),0)) ELSE (IFNULL(CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0)-IFNULL(invtd.quantity,0))/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0)-IFNULL(invtd.quantity,0))%unitspercase) AS INT),0)) END AS 'vanqty',CASE WHEN unitspercase=1 THEN (0 ||  '/' || IFNULL(CAST((invtd.quantity) AS INT),0)) ELSE (IFNULL(CAST((invtd.quantity/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((invtd.quantity%unitspercase) AS INT),0)) END AS 'loadqty',CASE WHEN unitspercase=1 THEN (0 ||  '/' || CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))) AS INT)) ELSE (CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))/unitspercase) AS INT) ||  '/' || CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))%unitspercase) AS INT)) END AS 'netqty',((CAST(COALESCE(invtd.quantity, 0) AS INT) / im.unitspercase)* im.caseprice) + ((COALESCE(invtd.quantity, 0) % im.unitspercase)* im.defaultsalesprice) AS 'netvalue',0 as sl FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invth.hhcdocumentnumber=" + invoicenumber+" order by itemcode";
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length < 0)
                result = []
            ProcessCurrentLoad(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == "Android") {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if (result.array != undefined) {
                result = $.map(result.array, function(item, index) {
                    //return [[item.sl,item.itemcode, item.description,item.UPC, item.vanqty, item.loadqty, item.netqty,item.netvalue,item.arbitemdescription]];
                	  return [[item.sl,item.itemcode, item.description,item.UPC, item.vanqty, item.loadqty, item.netqty,item.netvalue]];
                });
            }
            else
                result = [];
            ProcessCurrentLoad(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessCurrentLoad(result) {
    var Headers = ["Sl#","Item#", "Description","UPC","Van Qty", "Load Qty", "Net Qty","VALUE"];
    var Total = { "Van Qty": "0/0", "Load Qty": "0/0", "Net Qty": "0/0","VALUE" : "0"};
    
    var TotalAmount=0;
    for (i = 0; i < result.length; i++) {
        for (j = 0; j < result[i].length; j++) {
            if (j == 1) {
            	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                    result[i][j] = parseInt(result[i][j],10);
            	
                result[i][j] = (result[i][j]).toString();
            }
            if (j >= 4 && j <= 6) {
                var qtykey = "";
                if (j == 4)
                    qtykey = "Van Qty";
                if (j == 5)
                    qtykey = "Load Qty";
                if (j == 6)
                    qtykey = "Net Qty";                
                QuantityTotal(Total, qtykey, result[i][j]);
            }
            if(j == 7){
            	 result[i][j] = eval(result[i][j]).toFixed(decimalplace);
            	 TotalAmount = (eval(TotalAmount) + eval(result[i][j])).toFixed(decimalplace);
            }
               
            
        }
    }
    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
    Total["VALUE"] = TotalAmount.toString();
    data["HEADERS"] = Headers;
    data["TOTAL"] = [Total];
    data["printstatus"] = getPrintStatus();
    data["data"] = result;
    getCommonData();
    SetArray(ReportName.LoadReport);    
    //console.log(JSON.stringify(data));
    ReportsToPrint(arrRpt,currpoint+1);
}
//-----------

//------------

function getLoadTransferInfo(key)
{
	data = {};
    var Qry = "SELECT documentnumber,printstatus,transferlocationcode FROM inventorytransactionheader WHERE istemp='false' AND  detailkey = " + key;
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {   
            if(result.length > 0)
            {
                documentnumber = parseInt(eval(result[0][0]));
                printstatus = result[0][1];
		transferroutecode = parseInt(eval(result[0][2]));
            }
	    gettransferrouteinfo(transferroutecode);
            PrintLoadTransfer(key,"transferin");
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                documentnumber = parseInt(eval(result.array[0].documentnumber));
                printstatus = result.array[0].printstatus;
                transferroutecode = parseInt(eval(result.array[0].transferlocationcode));
            }
	    gettransferrouteinfo(transferroutecode);
	    //PrintLoadTransferValue(key,"transferin"); 
	    PrintLoadTransfer(key,"transferin");
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
//----------Start
function PrintLoadTransferValue(key,type)
{    
   
    if(type == "transferin")
        //var Qry = "SELECT itemcode,CAST(((CAST((IFNULL((transferinqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((transferinqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as netvalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE detailkey = " + key + " AND transactiontype = 2 AND istemp = 'false') AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 2 AND istemp = 'false') AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,im.unitspercase AS 'unitspercase',im.caseprice as 'caseprice',im.defaultsalesprice as 'defaultsalesprice' FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE transferinqty > 0 order by itemcode";
        var Qry = "SELECT itemcode,CAST(((CAST((IFNULL((transferinqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((transferinqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as netvalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE detailkey = " + key + " AND transactiontype = 2 AND istemp = 'false') AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 2 AND istemp = 'false') AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,im.unitspercase AS 'unitspercase',im.caseprice as 'caseprice',im.defaultsalesprice as 'defaultsalesprice' FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE transferinqty > 0 order by itemcode";
    else if(type == "transferout")
        //var Qry = "SELECT itemcode,CAST(((CAST((IFNULL((transferoutqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((transferoutqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as netvalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,im.unitspercase AS 'unitspercase',im.caseprice as 'caseprice',im.defaultsalesprice as 'defaultsalesprice' FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE transferoutqty > 0 order by itemcode";
    var Qry = "SELECT itemcode,CAST(((CAST((IFNULL((transferoutqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((transferoutqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as netvalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,im.unitspercase AS 'unitspercase',im.caseprice as 'caseprice',im.defaultsalesprice as 'defaultsalesprice' FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE transferoutqty > 0 order by itemcode";
    else if(type == "damage")
        //var Qry = "SELECT itemcode,CAST(((CAST((IFNULL((damagetransferoutqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((damagetransferoutqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as netvalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 2 AND istemp = 'false') AND detailkey = " + key + " AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 2 AND istemp = 'false') AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 2 AND istemp = 'false') AND transactiontypecode = 4 AND istemp = 'false' AND itemcode = invsum.itemcode),0) damagetransferoutqty,unitspercase AS 'unitspercase',caseprice,defaultsalesprice FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE damagetransferoutqty > 0 order by itemcode";
    var Qry = "SELECT itemcode,CAST(((CAST((IFNULL((damagetransferoutqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((damagetransferoutqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as netvalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 2 AND istemp = 'false') AND detailkey = " + key + " AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 2 AND istemp = 'false') AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 2 AND istemp = 'false') AND transactiontypecode = 4 AND istemp = 'false' AND itemcode = invsum.itemcode),0) damagetransferoutqty,unitspercase AS 'unitspercase',caseprice,defaultsalesprice FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE damagetransferoutqty > 0 order by itemcode";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {
            if(result.length <= 0)
                result = [];
                      
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        var netvalue= 0;
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                    return [[item.itemcode,item.netvalue]];    
                });
                for (i = 0; i < result.length; i++) {
                    for (j = 0; j < result[i].length; j++) {    
                            if (j == 1)
                                {
                               
                                
                                  result[i][j] = (eval(result[i][j])).toFixed(decimalplace); 
                                  if(type == "transferin")
                                      {
                                netinvalue = (eval(netinvalue) + eval(result[i][j])).toFixed(decimalplace);
                                      }
                             if(type == "transferout")
                                {
                               
                                netoutvalue = (eval(netoutvalue) + eval(result[i][j])).toFixed(decimalplace);
                                }
                                }           
                             
                    }
                }
                             
                //data["netvalue"] = netvalue.toString();
                
            }
            else
                result = [];           
           
            
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
//-----------End
function gettransferrouteinfo(transferroutecode)
{
    var Qry = "SELECT routecode,routename FROM routemaster WHERE routecode = "+transferroutecode;
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {   
            if(result.length > 0)
            {
                routecode = parseInt(eval(result[0][0]));
                routename = result[0][1];
		
            }
	    tranferflag =1;
	  data["TO ROUTE"] = routecode + "-" + routename;
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
               routecode = parseInt(eval(result.array[0].routecode));
                routename = result.array[0].routename;
            }
	    tranferflag =1;
	   data["TO ROUTE"] = routecode + "-" + routename;
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
function PrintLoadTransfer(key,type)
{   
    PrintLoadTransferValue(key,type);
  
    var Qry = "";
    
    if(type == "transferin"){
    	//Qry = "SELECT itemcode,description,(CAST(transferinqty/unitspercase AS INT) || '/' || CAST(transferinqty%unitspercase AS INT)) transferqty,(CAST((vanqty - (transferinqty - transferoutqty) + transferinqty)/unitspercase AS INT) || '/' || CAST((vanqty -(transferinqty - transferoutqty)+transferinqty)%unitspercase AS INT)) netqty FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,unitspercase AS 'unitspercase',caseprice,defaultsalesprice FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE transferinqty > 0 order by itemcode";
    	Qry = "SELECT 0 as sl,itemcode,description,unitspercase,(CASE WHEN unitspercase=1 THEN ( 0 || '/' || CAST(transferinqty AS INT))  ELSE (CAST(transferinqty/unitspercase AS INT) || '/' || CAST(transferinqty%unitspercase AS INT)) END) as transferqty,CAST(transferinqty AS INT) AS transftertotalqty ,(CAST((vanqty - (transferinqty - transferoutqty) + transferinqty)/unitspercase AS INT) || '/' || CAST((vanqty -(transferinqty - transferoutqty)+transferinqty)%unitspercase AS INT)) netqty,CAST(((CAST((IFNULL((transferinqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((transferinqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as value,reasoncode,expirydate FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,unitspercase AS 'unitspercase',caseprice,defaultsalesprice,IFNULL((SELECT reasoncode FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) reasoncode,IFNULL((SELECT expirydate FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) expirydate FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE transferinqty > 0 order by itemcode";

    }
    else if(type == "transferout"){
    	
    	//Qry = "SELECT 0 as sl,itemcode,description,unitspercase,(CAST(transferoutqty/unitspercase AS INT) || '/' || CAST(transferoutqty%unitspercase AS INT)) transferqty,CAST(transferoutqty AS INT) AS transftertotalqty,(CAST((vanqty - (transferinqty - transferoutqty) - transferoutqty)/unitspercase AS INT) || '/' || CAST((vanqty -(transferinqty - transferoutqty)-transferoutqty)%unitspercase AS INT)) netqty,CAST(((CAST((IFNULL((transferoutqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((transferoutqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as value,reasoncode,expirydate FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,unitspercase AS 'unitspercase',caseprice,defaultsalesprice,IFNULL((SELECT reasoncode FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) reasoncode,IFNULL((SELECT expirydate FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) expirydate FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE transferoutqty > 0 order by itemcode";
    	Qry="SELECT 0 as sl,itemcode,description,unitspercase,(CASE WHEN unitspercase=1 THEN ( 0 || '/' || CAST(transferoutqty AS INT)) ELSE (CAST(transferoutqty/unitspercase AS INT) || '/' || CAST(transferoutqty%unitspercase AS INT)) END) as transferqty,CAST(transferoutqty AS INT) AS transftertotalqty,(CAST((vanqty - (transferinqty - transferoutqty) - transferoutqty)/unitspercase AS INT) || '/' || CAST((vanqty -(transferinqty - transferoutqty)-transferoutqty)%unitspercase AS INT)) netqty,CAST(((CAST((IFNULL((transferoutqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((transferoutqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as value,reasoncode,expirydate FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,unitspercase AS 'unitspercase',caseprice,defaultsalesprice,IFNULL((SELECT rm.description AS reasoncode  FROM inventorytransactiondetail itd join retitmreasons rm on itd.reasoncode=rm.code WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) reasoncode,IFNULL((SELECT expirydate FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) expirydate FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE transferoutqty > 0 order by itemcode";
    	
    }
    else if(type == "damage"){
    	//Qry = "SELECT itemcode,description,(CAST(damagetransferoutqty/unitspercase AS INT) || '/' || CAST(damagetransferoutqty%unitspercase AS INT)) transferqty,(CAST((vanqty - (transferinqty - transferoutqty) - damagetransferoutqty)/unitspercase AS INT) || '/' || CAST((vanqty -(transferinqty - transferoutqty)-damagetransferoutqty)%unitspercase AS INT)) netqty FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 4 AND istemp = 'false' AND itemcode = invsum.itemcode),0) damagetransferoutqty,unitspercase AS 'unitspercase',caseprice,defaultsalesprice FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE damagetransferoutqty > 0 order by itemcode";
    	Qry = "SELECT 0 as sl,itemcode,description,unitspercase,(CAST(damagetransferoutqty/unitspercase AS INT) || '/' || CAST(damagetransferoutqty%unitspercase AS INT)) transferqty,CAST(damagetransferoutqty AS INT) AS transftertotalqty,(CAST((vanqty - (transferinqty - transferoutqty) - damagetransferoutqty)/unitspercase AS INT) || '/' || CAST((vanqty -(transferinqty - transferoutqty)-damagetransferoutqty)%unitspercase AS INT)) netqty,CAST(((CAST((IFNULL((damagetransferoutqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((damagetransferoutqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as value,reasoncode,expirydate FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription as description,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 3 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferinqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 2 AND istemp = 'false' AND itemcode = invsum.itemcode),0) transferoutqty,IFNULL((SELECT quantity FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 4 AND istemp = 'false' AND itemcode = invsum.itemcode),0) damagetransferoutqty,unitspercase AS 'unitspercase',caseprice,defaultsalesprice,IFNULL((SELECT reasoncode FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 4 AND istemp = 'false' AND itemcode = invsum.itemcode),0) reasoncode,IFNULL((SELECT expirydate FROM inventorytransactiondetail WHERE detailkey = " + key + " AND transactiontypecode = 4 AND istemp = 'false' AND itemcode = invsum.itemcode),0) expirydate FROM inventorysummarydetail invsum JOIN itemmaster im on im.actualitemcode = invsum.itemcode) WHERE damagetransferoutqty > 0 order by itemcode";
        
    }
  
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {
            if(result.length <= 0)
                result = [];
            ProcessTransferData(result,type);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                	if(type == "transferout")
                		return [[item.sl,item.itemcode,item.description,item.unitspercase,item.transferqty,item.transftertotalqty,item.reasoncode,item.expirydate]];
                	else
                		return [[item.sl,item.itemcode,item.description,item.unitspercase,item.transferqty,item.transftertotalqty, item.netqty,item.value]];
                });
            }
            else
                result = [];
            ProcessTransferData(result,type);
            if(type == "transferin")
    			PrintLoadTransfer(key,"transferout");
			else if(type == "transferout")
				PrintLoadTransfer(key,"damage");
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessTransferData(result,type)
{   
	var Header;
	
	var Header;
	if(type == "transferout"){
		data["HEADERS"] = ["Sl#","Item#","Description","UPC","Transfer Qty","Qty","Reason","Expiry Date"];
		Header= ["Sl#","Item#","Description","UPC","Transfer Qty","Qty","Reason","Expiry Date"];
	}
	else{
		Header= ["Sl#","Item#","Description","UPC","Transfer Qty","Qty","Net Qty","Value"];
		data["HEADERS"] = ["Sl#","Item#","Description","UPC","Transfer Qty","Qty","Net Qty","Value"];
		
	}	
    var total = {"Transfer Qty" : "0/0","Qty":"0","Net Qty" : "0/0","Value":"0" }; 

    var totalQty=0,TotalAmount=0;	
    for(i=0;i<result.length;i++)
    {                            
        for(j=0;j<result[i].length;j++)
        {
            if(j == 1)
            {
            	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                	result[i][j] = parseInt(result[i][j]);
            	
                result[i][j] = (result[i][j]).toString();
            }
            if(j == 4 || j == 6 )
            {
                var qtykey = "";
               
                if(j == 4){
                	qtykey = "Transfer Qty";	
                	QuantityTotal(total,qtykey,result[i][j]);
                }
                    
                if(j == 6  && type=="transferin"){
                	 qtykey = "Net Qty";
                	 QuantityTotal(total,qtykey,result[i][j]);
                }
                
                   
                
            }
            if(j==5 || j==7){
            	
            	 if(j == 5)
            		 totalQty = (eval(totalQty) + eval(result[i][j]));
            	  if(j == 7 && type=="transferin")
                      TotalAmount = (eval(TotalAmount) + eval(result[i][j])).toFixed(decimalplace);
            }
            
        }                            
    }
    totalQty = eval(parseFloat(totalQty));
    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
    total["Qty"] = totalQty.toString();
    total["Value"] = TotalAmount.toString();
    if(data["data"] == undefined)
        data["data"] = [];        
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : total});
	console.log("DATA OF TYPE " + type + JSON.stringify(data["data"]));
	
    if(type == "damage")
    {
    //getCommonData();    
    data["DOCUMENT NO"] = documentnumber;
    data["netvalue"] = eval(netinvalue - netoutvalue).toFixed(decimalplace);
    //data["data"] = result;
    data["TOTAL"] = [total];
    data["printstatus"] = getPrintStatus();
    getCommonData();
    //console.log(JSON.stringify(data)); 
    SetArray(ReportName.LoadTransfer);
    ReportsToPrint(arrRpt,currpoint+1);
    }   
}

function getUnloadEndInventoryInfo()
{
    
    var Qry = "SELECT documentnumber,printstatus FROM inventorytransactionheader WHERE detailkey = (SELECT MAX(invth.detailkey) FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey AND invtd.transactiontypecode IN(5,6) WHERE invth.istemp = 'false')";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {   
            if(result.length > 0)
            {
                documentnumber = parseInt(eval(result[0][0]));
                printstatus = result[0][1];
            }            
            PrintUnloadEndInventoryValue();
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                documentnumber = parseInt(eval(result.array[0].documentnumber));
                printstatus = result.array[0].printstatus;
            }    
            PrintUnloadEndInventoryValue();        
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
//---------Start Close value
function PrintUnloadEndInventoryValue()
{  
    data = {};
   
    //var Qry = "SELECT itemcode,CAST(((CAST((IFNULL((vanqty-freshunloadqty-truckdamageqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((vanqty-freshunloadqty-truckdamageqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as closevalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription AS description,unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,caseprice,defaultsalesprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6)) order by itemcode";
    
    var Qry="SELECT itemcode,CAST(((CAST((IFNULL((vanqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((vanqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as Available,CAST(((CAST((IFNULL((truckdamageqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((truckdamageqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as Unload, CAST(((CAST((IFNULL((vanqty-freshunloadqty-truckdamageqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((vanqty-freshunloadqty-truckdamageqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as closevalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription AS description,unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,caseprice,defaultsalesprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6)) order by itemcode";
     console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {
            if(result.length <= 0)
                result = [];
            
            PrintUnloadEndInventory();
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        
        var closevalue= 0;
        var AvailableVal=0;
        var UnloadVal=0;
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                    return [[item.itemcode,item.Available,item.Unload,item.closevalue]];    
                });
                for (i = 0; i < result.length; i++) {
                    for (j = 0; j < result[i].length; j++) {    
                    	 if (j == 1)
                         {
                         
                         result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                         AvailableVal = (eval(AvailableVal) + eval(result[i][j])).toFixed(decimalplace);
                         }  
                    	 if (j == 2)
                         {
                         
                         result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                         UnloadVal = (eval(UnloadVal) + eval(result[i][j])).toFixed(decimalplace);
                         }  
                    	 if (j == 3)
                         {
                    		 	result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                                closevalue = (eval(closevalue) + eval(result[i][j])).toFixed(decimalplace);
                         }           
                             
                    }
                }
                data["availvalue"] = AvailableVal.toString();
                data["unloadvalue"] = UnloadVal.toString();
                data["closevalue"] = closevalue.toString();
                
            }
            else
                result = [];           
            PrintUnloadEndInventory();
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
//--------End

function PrintUnloadEndInventory()
{
				//var Qry = "SELECT itemcode,description,unitspercase,(CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) AS truckstockqty,(CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) AS freshunloadqty,(CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) AS truckdamageqty,(CAST((vanqty-freshunloadqty-truckdamageqty)/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty)%unitspercase AS INT)) as closingstock,(CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))/unitspercase AS INT) || '/' || CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))%unitspercase AS INT)) AS varianceqty,CAST(((CAST((IFNULL((endstockqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((endstockqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT ) as EndValue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription AS description,unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,IFNULL(endstockqty,0) as endstockqty,defaultsalesprice,caseprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6)) order by itemcode";
				//var Qry="SELECT itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemdescription END AS 'description',unitspercase,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS truckstockqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(freshunloadqty AS INT)) ELSE (CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) END AS freshunloadqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(truckdamageqty AS INT)) ELSE (CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) END AS truckdamageqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-freshunloadqty-truckdamageqty) AS INT)) ELSE (CAST((vanqty-freshunloadqty-truckdamageqty)/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty)%unitspercase AS INT)) END as closingstock,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty))) AS INT)) ELSE (CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))/unitspercase AS INT) || '/' || CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))%unitspercase AS INT)) END AS varianceqty,CAST(((CAST((IFNULL((endstockqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((endstockqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT ) as EndValue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription AS description,unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,IFNULL(endstockqty,0) as endstockqty,defaultsalesprice,caseprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6)) order by itemcode";
				//var Qry="SELECT itemcode,description,unitspercase,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS truckstockqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(freshunloadqty AS INT)) ELSE (CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) END AS freshunloadqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(truckdamageqty AS INT)) ELSE (CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) END AS truckdamageqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-freshunloadqty-truckdamageqty) AS INT)) ELSE (CAST((vanqty-freshunloadqty-truckdamageqty)/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty)%unitspercase AS INT)) END as closingstock,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty))) AS INT)) ELSE (CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))/unitspercase AS INT) || '/' || CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))%unitspercase AS INT)) END AS varianceqty,CAST(((CAST((IFNULL((endstockqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((endstockqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT ) as EndValue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemdescription END AS 'description',unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,IFNULL(endstockqty,0) as endstockqty,defaultsalesprice,caseprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6) and IFNULL(unloadqty,0) + IFNULL(endstockqty,0) > 0) order by itemcode";
				
	// org qry sujee commented 04/12/2019 
	          //var Qry="SELECT itemcode,description,arbdescription,unitspercase,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS truckstockqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(freshunloadqty AS INT)) ELSE (CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) END AS freshunloadqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(truckdamageqty AS INT)) ELSE (CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) END AS truckdamageqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-freshunloadqty-truckdamageqty) AS INT)) ELSE (CAST((vanqty-freshunloadqty-truckdamageqty)/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty)%unitspercase AS INT)) END as closingstock,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty))) AS INT)) ELSE (CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))/unitspercase AS INT) || '/' || CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))%unitspercase AS INT)) END AS varianceqty,CAST(((CAST((IFNULL((endstockqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((endstockqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT ) as EndValue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,im.itemdescription AS 'description',im.arbitemshortdescription AS 'arbdescription',unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,IFNULL(endstockqty,0) as endstockqty,defaultsalesprice,caseprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6) and IFNULL(unloadqty,0) + IFNULL(endstockqty,0) > 0) order by itemcode";
	
	var Qry="SELECT 0 as sl,itemcode,description,unitspercase, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS invcalculate,'0/0' as rettostk, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(truckdamageqty AS INT)) ELSE (CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) END AS truckdamageqty, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS truckstockqty, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(freshunloadqty AS INT)) ELSE (CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) END AS nonsales, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty) AS INT)) ELSE (CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty)/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty)%unitspercase AS INT)) END AS varianceqty, printf('%.3f',((CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty)/unitspercase AS INT) * caseprice) +  (CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty)%unitspercase AS INT) * defaultsalesprice)) ) AS varianceval,CAST(((CAST((IFNULL((endstockqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((endstockqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as EndValue  FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,im.itemdescription AS 'description',unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,'0/0' as rettostk,IFNULL(truckdamagedunloadqty,0) as truckdamageqty, ((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(endstockqty,0) as endstockqty,caseprice,defaultsalesprice  FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6) and IFNULL(unloadqty,0) + IFNULL(endstockqty,0) > 0) order by itemcode ";
                console.log(Qry);
                if (platform == 'iPad') {
                    Cordova.exec(function(result) {
                        if(result.length <= 0)                        
                            result = [];                        
                       ProcessUnloadEndInventory(result);
                    },
                    function(error) {
                        alert(error);
                    },
                    "PluginClass",
                    "GetdataMethod",
                    [Qry]);
                }
                else if (platform == 'Android') {
                    window.plugins.DataBaseHelper.select(Qry, function(result) {
                        if(result.array != undefined)
                        {
                           
                            result = $.map(result.array, function(item, index) {
                               // return [[item.itemcode,item.description,item.unitspercase, item.truckstockqty, item.freshunloadqty,item.closingstock,item.varianceqty,item.arbdescription]];
                             return [[item.itemcode,item.description,item.unitspercase, item.invcalculate, item.rettostk,item.truckdamageqty,item.truckstockqty,item.nonsales,item.varianceqty,item.varianceval,item.EndValue]];
                            });
                        }
                        else
                            result = [];
                        ProcessUnloadEndInventory(result);
                    },
					function() {
					    console.warn("Error calling plugin");
					});
                }
}

function ProcessUnloadEndInventory(result)
{   
   
	 //data["HEADERS"] = ["Item#","Description","UPC","Truck Stock","Fresh Unload","Closing Stock","Variance Qty"];
	  data["HEADERS"] = ["Item#","Description","UPC","Inventory Calculated","Return Stock","Truck Spoil","Actual on Truck","Non Sales","Variance Qty","Variance Value","Total Value"]; 
//	 var total = {"Truck Stock" : "0/0","Fresh Unload" : "0/0", "Closing Stock" : "0/0","Variance Qty" : "0/0"}; 
	   var total = {"Inventory Calculated" : "0/0","Return Stock" : "0/0", "Truck Spoil" : "0/0","Actual on Truck" : "0/0","Non Sales" : "0/0","Variance Qty" : "0/0","Variance Value" : "0.000"}; 
	 var totalValue=0;
    
    for(i=0;i<result.length;i++)
    {                            
        for(j=0;j<result[i].length;j++)
        {
            if(j == 0)
            {
                result[i][j] = (result[i][j]);
                result[i][j] = (result[i][j]).toString();
            }
            if(j >= 3 && j<=8)
            {
                var qtykey = "";
                if(j == 3)
                    qtykey = "Inventory Calculated";
                if(j == 4)
                    qtykey = "Return Stock";
                if(j == 5)
                    qtykey = "Truck Spoil";
                if(j == 6)
                    qtykey = "Actual on Truck";
                if(j == 7)
                    qtykey = "Non Sales";
                if(j == 8)
                    qtykey = "Variance Qty";
                if(j == 9)
                    qtykey = "Variance Value";
                QuantityTotal(total,qtykey,result[i][j]);
            }
            
        if(j==10){
           	 result[i][j] = eval(result[i][j]).toFixed(decimalplace);
              	 totalValue = (eval(totalValue) + eval(result[i][j])).toFixed(decimalplace);
        
              }
        }
    }
    
    totalValue = eval(parseFloat(totalValue)).toFixed(decimalplace);
   

    total["Total Value"] = totalValue.toString();
    getCommonData();                      
    data["data"] = result;                        
    data["TOTAL"] = [total];  
    data["printstatus"] = getPrintStatus();   
    data["DOCUMENT NO"] = documentnumber;
    //data["closevalue"] = 0;
    console.log(JSON.stringify(data));
    SetArray(ReportName.EndInventory);    
    ReportsToPrint(arrRpt,currpoint+1);
}

function PrintSales()
{
    data = {};
    //GetSalesData('sales');
    getInvoiceInfo();
   // alert("sel");
}

function getInvoiceInfo()
{
    //var Qry = "SELECT cm.customercode,customername,customeraddress1 as address,paymenttype,IFNULL(totalpromoamount,0) totalpromoamount,(IFNULL(totalsalesamount,0) - IFNULL(totalreturnamount,0)) totalinvoiceamount,documentnumber,invoicenumber,comments,printstatus,(SELECT CASE cm.messagekey5 WHEN 0 THEN '' ELSE messagedescription END FROM customermessages WHERE messagekey = messagekey5) AS invheadermsg,(SELECT CASE cm.messagekey6 WHEN 0 THEN '' ELSE messagedescription END FROM customermessages WHERE messagekey = messagekey6) AS invtrailormsg FROM customermaster cm JOIN invoiceheader inv ON cm.customercode = inv.customercode AND inv.invoicenumber=" + invoicenumber;
//	var Qry = "SELECT cm.customercode,customername  || '-' || arbcustomername  AS 'customername',customeraddress1  AS 'address',cm.roundnetamount,inv.paymenttype,IFNULL(totalpromoamount,0) totalpromoamount,(IFNULL(totalsalesamount,0) - IFNULL(totalreturnamount,0)-IFNULL(totaldamagedamount,0)+(select IFNULL(sum(diffround),0) from invoicedetail where visitkey=inv.visitkey)) totalinvoiceamount,documentnumber,invoicenumber,comments,printstatus,(SELECT CASE cm.messagekey5 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey5) AS invheadermsg,(SELECT CASE cm.messagekey6 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey6) AS invtrailormsg,(SELECT (IFNULL(sum(amount),0) - IFNULL(sum(promoamount),0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as tcamount,cm.invoicepaymentterms,CAST(IFNULL(amount,0) AS VARCHAR) amount,bankname,checkdate,checknumber,(SELECT sum(IFNULL(promoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as promoamount,inv.transactionkey,cm.outletsubtype,CAST(inv.totalinvoiceamount AS VARCHAR) as headerAmount,customeraddress2  AS 'customeraddress2',customeraddress3,printoutletitemcode,cm.alternatecode,splitfree,inv.totalmanualfree as totalmanualfree,inv.totaldiscountamount as totaldiscountamount,arbcustomeraddress1,arbcustomeraddress2,arbcustomeraddress3,CAST(IFNULL(totalsalesamount,0) AS VARCHAR) tsalesamt,CAST(IFNULL(totalreturnamount,0) AS VARCHAR) treturnsamt,CAST(IFNULL(totaldamagedamount,0) AS VARCHAR) tdamagesamt,(select IFNULL(sum(diffround),0) from invoicedetail where visitkey=inv.visitkey) AS diffround,cm.invoicepriceprint as invoicepriceprint FROM customermaster cm JOIN invoiceheader inv ON cm.customercode = inv.customercode AND inv.invoicenumber=" + invoicenumber + " LEFT JOIN cashcheckdetail ccd ON ccd.visitkey = inv.visitkey AND ccd.routekey = inv.routekey LEFT JOIN bankmaster bm ON bm.bankcode = ccd.bankcode";
	

	
	
	/*var Qry = "SELECT cm.customercode,(CASE WHEN (traname IS NULL OR traname='') THEN customername ELSE traname END) AS 'customername',customeraddress1  AS 'address',cm.roundnetamount,inv.paymenttype,IFNULL(totalpromoamount,0) totalpromoamount,(IFNULL(totalsalesamount,0) - IFNULL(totalreturnamount,0)-IFNULL(totaldamagedamount,0)) totalinvoiceamount,documentnumber,invoicenumber,comments,printstatus,(SELECT CASE cm.messagekey5 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey5) AS invheadermsg,(SELECT CASE cm.messagekey6 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey6) AS invtrailormsg,(SELECT (IFNULL(sum(amount),0) - IFNULL(sum(promoamount),0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as tcamount,cm.invoicepaymentterms,CAST(IFNULL(amount,0) AS VARCHAR) amount,bankname,checkdate,checknumber,(SELECT sum(IFNULL(promoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as promoamount,(SELECT sum(IFNULL(promoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as salespromo,(SELECT sum(IFNULL(returnpromoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as returnpromo,inv.transactionkey,cm.outletsubtype,CAST(inv.totalinvoiceamount AS VARCHAR) as headerAmount,customeraddress2  AS 'customeraddress2',customeraddress3,printoutletitemcode,cm.alternatecode,splitfree,inv.totalmanualfree as totalmanualfree,inv.totaldiscountamount as totaldiscountamount,arbcustomeraddress1,arbcustomeraddress2,arbcustomeraddress3,CAST(IFNULL(totalsalesamount,0) AS VARCHAR) tsalesamt,CAST(IFNULL(totalreturnamount,0) AS VARCHAR) treturnsamt,CAST(IFNULL(totaldamagedamount,0) AS VARCHAR) tdamagesamt,(select IFNULL(sum(diffround),0) from invoicedetail where visitkey=inv.visitkey) AS diffround,cm.invoicepriceprint as invoicepriceprint,itemlinetaxamount,totaltaxesamount,"
	+"(select sum(salesqty) from invoicedetail  invt where invt.transactionkey = inv.transactionkey) as totalSalesQty ,"
	+"(select sum(COALESCE(returnqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey)  as totalReturnQty,"
	+"(select sum(COALESCE(damagedqty,0))  from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totalDamagedQty,"
	+"(select sum(COALESCE(promoqty,0))+sum(COALESCE(freesampleqty,0))+sum(COALESCE(manualfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey)  as totalFreeQty,inv.totalfreesampleamount as totalfreesampleamount,inv.transactionkey as transactionkey, "
	+"(select sum(COALESCE(promoqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as promoqty,"
	+"(select sum(COALESCE(freesampleqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as freesampleqty,"
	+"(select sum(COALESCE(manualfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as manualfreeqty,"
	+"(select sum(COALESCE(returnfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as returnfreeqty,ifnull(cm.customertaxidoptions,0) printtax,ifnull(cm.applytax,0) applytax,ifnull(cm.taxregistrationnumber,0) taxregistrationnumber,(select sum(COALESCE(salesitemexcisetax,0)+COALESCE(salesitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as salestax,"
    +"(select sum(COALESCE(returnitemexcisetax,0)+COALESCE(returnitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as returntax,"
    +"(select sum(COALESCE(damageditemexcisetax,0)+COALESCE(damageditemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as damagetax,"
    +"(select sum(COALESCE(fgitemexcisetax,0)+ COALESCE(fgitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as freetax , "
    +"(select sum(COALESCE(salesitemexcisetax,0)-COALESCE(returnitemexcisetax,0)-COALESCE(damageditemexcisetax,0)+COALESCE(fgitemexcisetax,0)+COALESCE(promoitemexcisetax,0)-COALESCE(buybackexcisetax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totExcTax, "
    +"(select sum(COALESCE(salesitemgsttax,0)-COALESCE(returnitemgsttax,0)-COALESCE(damageditemgsttax,0)+COALESCE(fgitemgsttax,0)+COALESCE(promoitemgsttax,0)-COALESCE(buybackgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totVatTax ,ifnull((select amount from cashcheckdetail where routekey=" + sessionStorage.getItem("RouteKey") + " and visitkey=inv.visitkey and typecode=0),0) cashamt,ifnull((select amount from cashcheckdetail where routekey=" + sessionStorage.getItem("RouteKey") + " and visitkey=inv.visitkey and typecode=1),0) chequeamt,(select distinct tm.taxpercentage from invoicedetail id inner join  itemmaster im on id.[itemcode]=im.actualitemcode inner join  taxmaster tm on tm.[taxcode]=im.itemtaxkey2) as taxpercentage, (select GROUP_CONCAT(cast(ppad.promotionamount as INT) ) as invoicediscount from promokeyheader pkh inner join promokeydetail pkd on pkh.[promotionkey]=pkd.promotionkey "
    +" inner join promoplandetail ppd on ppd.plannumber=pkd.plannumber "
    +" inner join promotionassignmentadvanced ppad on ppad.assignmentnumber=ppd.assignmentnumber "
    +" inner join customermaster cm on cm.promotionkey=pkh.promotionkey and  cm.customercode=inv.customercode) as rebate "
	+" FROM customermaster cm JOIN invoiceheader inv ON cm.customercode = inv.customercode AND inv.invoicenumber=" + invoicenumber + " LEFT JOIN taxmaster tm ON tm.taxcode=cm.custtaxkey1 LEFT JOIN cashcheckdetail ccd ON ccd.visitkey = inv.visitkey AND ccd.routekey = inv.routekey LEFT JOIN bankmaster bm ON bm.bankcode = ccd.bankcode";

*/
	
	// sujee commneted 20/06/2021 added expirytax 
//	var Qry = "SELECT cm.customercode,cm. customername AS 'customername',customeraddress1  AS 'address',cm.roundnetamount,inv.paymenttype,IFNULL(totalpromoamount,0) totalpromoamount,(IFNULL(totalsalesamount,0) - IFNULL(totalreturnamount,0)-IFNULL(totaldamagedamount,0)-IFNULL(totalexpiryamount,0)) totalinvoiceamount,documentnumber,invoicenumber,comments,printstatus,(SELECT CASE cm.messagekey5 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey5) AS invheadermsg,(SELECT CASE cm.messagekey6 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey6) AS invtrailormsg,(SELECT (IFNULL(sum(amount),0) - IFNULL(sum(promoamount),0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as tcamount,cm.invoicepaymentterms,CAST(IFNULL(amount,0) AS VARCHAR) amount,bankname,checkdate,checknumber,(SELECT sum(IFNULL(promoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as promoamount,(SELECT sum(IFNULL(promoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as salespromo,(SELECT sum(IFNULL(returnpromoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as returnpromo,inv.transactionkey,cm.outletsubtype,CAST(inv.totalinvoiceamount AS VARCHAR) as headerAmount,customeraddress2  AS 'customeraddress2',customeraddress3,printoutletitemcode,cm.alternatecode,splitfree,inv.totalmanualfree as totalmanualfree,inv.totaldiscountamount as totaldiscountamount,arbcustomeraddress1,arbcustomeraddress2,arbcustomeraddress3,CAST(IFNULL(totalsalesamount,0) AS VARCHAR) tsalesamt,CAST(IFNULL(totalreturnamount,0) AS VARCHAR) treturnsamt, cast(sum(COALESCE(totaldamagedamount,0)+COALESCE(totalexpiryamount,0)) as varchar) tdamagesamt,(select IFNULL(sum(diffround),0) from invoicedetail where visitkey=inv.visitkey) AS diffround,cm.invoicepriceprint as invoicepriceprint,itemlinetaxamount,totaltaxesamount,"
//	+"(select sum(salesqty) from invoicedetail  invt where invt.transactionkey = inv.transactionkey) as totalSalesQty ,"
//	+"(select sum(COALESCE(returnqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey)  as totalReturnQty,"
//	+"(select sum(COALESCE(damagedqty,0))+sum(COALESCE(expiryqty,0))  from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totalDamagedQty,"
//	+"(select sum(COALESCE(promoqty,0))+sum(COALESCE(freesampleqty,0))+sum(COALESCE(manualfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey)  as totalFreeQty,inv.totalfreesampleamount as totalfreesampleamount,inv.transactionkey as transactionkey, "
//	+"(select sum(COALESCE(promoqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as promoqty,"
//	+"(select sum(COALESCE(freesampleqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as freesampleqty,"
//	+"(select sum(COALESCE(manualfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as manualfreeqty,"
//	+"(select sum(COALESCE(returnfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as returnfreeqty,ifnull(cm.customertaxidoptions,0) printtax,ifnull(cm.applytax,0) applytax,ifnull(cm.taxregistrationnumber,0) taxregistrationnumber,(select sum(COALESCE(salesitemexcisetax,0)+COALESCE(salesitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as salestax,"
//    +"(select sum(COALESCE(returnitemexcisetax,0)+COALESCE(returnitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as returntax,"
//    +"(select sum(COALESCE(damageditemexcisetax,0)+COALESCE(damageditemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as damagetax,"
//    +"(select sum(COALESCE(fgitemexcisetax,0)+ COALESCE(fgitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as freetax , "
//    +"(select sum(COALESCE(salesitemexcisetax,0)-COALESCE(returnitemexcisetax,0)-COALESCE(damageditemexcisetax,0)+COALESCE(fgitemexcisetax,0)+COALESCE(promoitemexcisetax,0)-COALESCE(buybackexcisetax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totExcTax, "
//    +"(select sum(COALESCE(salesitemgsttax,0)-COALESCE(returnitemgsttax,0)-COALESCE(damageditemgsttax,0)+COALESCE(fgitemgsttax,0)+COALESCE(promoitemgsttax,0)-COALESCE(buybackgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totVatTax ,ifnull((select amount from cashcheckdetail where routekey=" + sessionStorage.getItem("RouteKey") + " and visitkey=inv.visitkey and typecode=0),0) cashamt,ifnull((select amount from cashcheckdetail where routekey=" + sessionStorage.getItem("RouteKey") + " and visitkey=inv.visitkey and typecode=1),0) chequeamt,(select distinct tm.taxpercentage from invoicedetail id inner join  itemmaster im on id.[itemcode]=im.actualitemcode inner join  taxmaster tm on tm.[taxcode]=im.itemtaxkey2) as taxpercentage, (select GROUP_CONCAT(cast(ppad.promotionamount as INT) ) as invoicediscount from promokeyheader pkh inner join promokeydetail pkd on pkh.[promotionkey]=pkd.promotionkey "
//    +" inner join promoplandetail ppd on ppd.plannumber=pkd.plannumber "
//    +" inner join promotionassignmentadvanced ppad on ppad.assignmentnumber=ppd.assignmentnumber "
//    +" inner join customermaster cm on cm.promotionkey=pkh.promotionkey and  cm.customercode=inv.customercode) as rebate,ifnull(cm.deliveryflag,0) as deliveryflag,ifnull(cm.duedateflag,0) as duedateflag ,ifnull(cm.statementcust,0) as  statementcust,ifnull(cm.creditlimitdays,0) as creditdays,ifnull(cm.taxcardno,' ') as taxcard,ifnull(cm.crno,' ') as crno,ifnull(cm.email,'') as email,ifnull(cm.contactname,'') as contactname,ifnull(cm.customerphone,'') as customerphone"
//	+" FROM customermaster cm JOIN invoiceheader inv ON cm.customercode = inv.customercode AND inv.invoicenumber=" + invoicenumber + " LEFT JOIN taxmaster tm ON tm.taxcode=cm.custtaxkey1 LEFT JOIN cashcheckdetail ccd ON ccd.visitkey = inv.visitkey AND ccd.routekey = inv.routekey LEFT JOIN bankmaster bm ON bm.bankcode = ccd.bankcode";


	
	var Qry = "SELECT cm.customercode,cm. customername AS 'customername',customeraddress1  AS 'address',cm.roundnetamount,inv.paymenttype,IFNULL(totalpromoamount,0) totalpromoamount,(IFNULL(totalsalesamount,0) - IFNULL(totalreturnamount,0)-IFNULL(totaldamagedamount,0)-IFNULL(totalexpiryamount,0)) totalinvoiceamount,documentnumber,invoicenumber,comments,printstatus,(SELECT CASE cm.messagekey5 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey5) AS invheadermsg,(SELECT CASE cm.messagekey6 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey6) AS invtrailormsg,(SELECT (IFNULL(sum(amount),0) - IFNULL(sum(promoamount),0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as tcamount,cm.invoicepaymentterms,CAST(IFNULL(amount,0) AS VARCHAR) amount,bankname,checkdate,checknumber,(SELECT sum(IFNULL(promoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as promoamount,(SELECT sum(IFNULL(promoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as salespromo,(SELECT sum(IFNULL(returnpromoamount,0)) from invoicedetail invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as returnpromo,inv.transactionkey,cm.outletsubtype,CAST(inv.totalinvoiceamount AS VARCHAR) as headerAmount,customeraddress2  AS 'customeraddress2',customeraddress3,printoutletitemcode,cm.alternatecode,splitfree,inv.totalmanualfree as totalmanualfree,inv.totaldiscountamount as totaldiscountamount,arbcustomeraddress1,arbcustomeraddress2,arbcustomeraddress3,CAST(IFNULL(totalsalesamount,0) AS VARCHAR) tsalesamt,CAST(IFNULL(totalreturnamount,0) AS VARCHAR) treturnsamt, cast(sum(COALESCE(totaldamagedamount,0)+COALESCE(totalexpiryamount,0)) as varchar) tdamagesamt,(select IFNULL(sum(diffround),0) from invoicedetail where visitkey=inv.visitkey) AS diffround,cm.invoicepriceprint as invoicepriceprint,itemlinetaxamount,totaltaxesamount,"
	+"(select sum(salesqty) from invoicedetail  invt where invt.transactionkey = inv.transactionkey) as totalSalesQty ,"
	+"(select sum(COALESCE(returnqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey)  as totalReturnQty,"
	+"(select sum(COALESCE(damagedqty,0))+sum(COALESCE(expiryqty,0))  from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totalDamagedQty,"
	+"(select sum(COALESCE(promoqty,0))+sum(COALESCE(freesampleqty,0))+sum(COALESCE(manualfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey)  as totalFreeQty,inv.totalfreesampleamount as totalfreesampleamount,inv.transactionkey as transactionkey, "
	+"(select sum(COALESCE(promoqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as promoqty,"
	+"(select sum(COALESCE(freesampleqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as freesampleqty,"
	+"(select sum(COALESCE(manualfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as manualfreeqty,"
	+"(select sum(COALESCE(returnfreeqty,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as returnfreeqty,ifnull(cm.customertaxidoptions,0) printtax,ifnull(cm.applytax,0) applytax,ifnull(cm.taxregistrationnumber,0) taxregistrationnumber,(select sum(COALESCE(salesitemexcisetax,0)+COALESCE(salesitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as salestax,"
    +"(select sum(COALESCE(returnitemexcisetax,0)+COALESCE(returnitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as returntax,"
    +"(select sum(COALESCE(damageditemexcisetax,0)+COALESCE(damageditemgsttax,0)+COALESCE(expiryitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as damagetax,"
    +"(select sum(COALESCE(fgitemexcisetax,0)+ COALESCE(fgitemgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as freetax , "
    +"(select sum(COALESCE(salesitemexcisetax,0)-COALESCE(returnitemexcisetax,0)-COALESCE(damageditemexcisetax,0)+COALESCE(fgitemexcisetax,0)+COALESCE(promoitemexcisetax,0)-COALESCE(buybackexcisetax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totExcTax, "
    +"(select sum(COALESCE(salesitemgsttax,0)-COALESCE(returnitemgsttax,0)-COALESCE(damageditemgsttax,0)-COALESCE(expiryitemgsttax,0)+COALESCE(fgitemgsttax,0)+COALESCE(promoitemgsttax,0)-COALESCE(buybackgsttax,0)) from invoicedetail invt where invt.transactionkey = inv.transactionkey) as totVatTax ,ifnull((select amount from cashcheckdetail where routekey=" + sessionStorage.getItem("RouteKey") + " and visitkey=inv.visitkey and typecode=0),0) cashamt,ifnull((select amount from cashcheckdetail where routekey=" + sessionStorage.getItem("RouteKey") + " and visitkey=inv.visitkey and typecode=1),0) chequeamt,(select distinct tm.taxpercentage from invoicedetail id inner join  itemmaster im on id.[itemcode]=im.actualitemcode inner join  taxmaster tm on tm.[taxcode]=im.itemtaxkey2) as taxpercentage, (select GROUP_CONCAT(cast(ppad.promotionamount as INT) ) as invoicediscount from promokeyheader pkh inner join promokeydetail pkd on pkh.[promotionkey]=pkd.promotionkey "
    +" inner join promoplandetail ppd on ppd.plannumber=pkd.plannumber "
    +" inner join promotionassignmentadvanced ppad on ppad.assignmentnumber=ppd.assignmentnumber "
    +" inner join customermaster cm on cm.promotionkey=pkh.promotionkey and  cm.customercode=inv.customercode) as rebate,ifnull(cm.deliveryflag,0) as deliveryflag,ifnull(cm.duedateflag,0) as duedateflag ,ifnull(cm.statementcust,0) as  statementcust,ifnull(cm.creditlimitdays,0) as creditdays,ifnull(cm.taxcardno,' ') as taxcard,ifnull(cm.crno,' ') as crno,ifnull(cm.email,'') as email,ifnull(cm.contactname,'') as contactname,ifnull(cm.customerphone,'') as customerphone"
	+" FROM customermaster cm JOIN invoiceheader inv ON cm.customercode = inv.customercode AND inv.invoicenumber=" + invoicenumber + " LEFT JOIN taxmaster tm ON tm.taxcode=cm.custtaxkey1 LEFT JOIN cashcheckdetail ccd ON ccd.visitkey = inv.visitkey AND ccd.routekey = inv.routekey LEFT JOIN bankmaster bm ON bm.bankcode = ccd.bankcode";

	
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {
            if(result.length > 0)
            {
                customercode = result[0][0];
                customername = result[0][1];
                customeraddress = result[0][2];
                paymenttype = result[0][3];                
                totalpromoamount = eval(result[0][4]).toFixed(decimalplace);
                totalinvoiceamount = eval(result[0][5]).toFixed(decimalplace);
                documentnumber = parseInt(eval(result[0][6]));
                invoicenumber = parseInt(eval(result[0][7]));
                comments = result[0][8];
                printstatus = result[0][9];
                invheadermsg = result[0][10];
                invtrailormsg = result[0][11];
                itempromoamount = (eval(result[0][18])).toFixed(decimalplace);
                transactionkey = parseInt(eval(result[0][19]));
                tcamount = eval(result[0][12]).toFixed(decimalplace);
				invoicepaymentterms = result[0][13];
				data["Cash"] = {"Amount" : eval(result[0][14]).toFixed(decimalplace)};
				 if(result[0][16]!=''){
				 var d = new Date(result[0][16]);
				 result[0][16] = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
				 }
                data["Cheque"] = [{"Cheque Date" : (result[0][16]),"Cheque No" : (result[0][17]), "Bank" : result[0][15], "Amount" : (result[0][14]).toString()}];
               // GetSalesData('sales');
              //0 For display Discount and 1 For Report have UPC
                GetSalesData('sales','1');
            }
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
    	var customeraddress1,customeraddress2,customeraddress3;
    	
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                    return [[item.customercode,item.customername, item.address, item.paymenttype,item.totalpromoamount,item.totalinvoiceamount,
                             item.documentnumber,item.invoicenumber,item.comments,item.printstatus,item.invheadermsg,item.invtrailormsg,
                             item.tcamount,item.invoicepaymentterms,item.amount,item.bankname,item.checkdate,item.checknumber,item.promoamount,
                             item.transactionkey,item.outletsubtype,item.headerAmount,item.customeraddress2,item.customeraddress3,
                             item.printoutletitemcode,item.alternatecode,item.splitfree,item.totalmanualfree,item.totaldiscountamount,
                             item.arbcustomeraddress1,item.arbcustomeraddress2,item.arbcustomeraddress3,item.tsalesamt,item.treturnsamt,
                             item.tdamagesamt,item.diffround,item.invoicepriceprint,item.roundnetamount,item.itemlinetaxamount,item.totaltaxesamount,
    						 item.totalFreeQty,
    							item.totalfreesampleamount,item.transactionkey,
    							item.promoqty,item.freesampleqty,item.manualfreeqty,item.returnfreeqty
    							,item.printtax,item.applytax,item.taxregistrationnumber,item.totalSalesQty,item.totalReturnQty,item.totalDamagedQty,
    							item.salestax,item.returntax,item.damagetax,item.freetax,item.totExcTax,item.totVatTax,item.cashamt,item.chequeamt,item.salespromo,
    							item.returnpromo,item.taxpercentage,item.rebate,item.deliveryflag,item.duedateflag,item.statementcust,item.creditdays,item.taxcard,item.crno,item.email,item.contactname,item.customerphone]];    
                });
                
                
                customeraddress = "";
                customeraddress2 = "";
                customeraddress3 = "";
                //alert(JSON.stringify(result));
                customercode = result[0][0];
              
                customername = result[0][1];
                customeraddress1 =result[0][2];
                 
                paymenttype = result[0][3];                
                totalpromoamount = eval(result[0][4]).toFixed(decimalplace);
                totalinvoiceamount = eval(result[0][5]).toFixed(decimalplace);
                documentnumber = parseInt(eval(result[0][6]));
                invoicenumber = parseInt(eval(result[0][7]));
                comments = result[0][8];
                printstatus = result[0][9];
                invheadermsg = result[0][10];
                invtrailormsg = result[0][11];
                itempromoamount = eval(eval(result[0][18])).toFixed(decimalplace);
                transactionkey = parseInt(eval(result[0][19]));
                tcamount = eval(result[0][12]).toFixed(decimalplace) - eval(result[0][14]).toFixed(decimalplace);
				invoicepaymentterms = result[0][13];
				customerOutlet=result[0][20];
				headerAmount=result[0][21];
				customeraddress2=result[0][22];
				customeraddress3=result[0][23];
				printoutletitemcode=result[0][24];
				alternatecode=result[0][25];
				splitfree=result[0][26];
				var manualfree=result[0][27]
				manualfreediscount=eval(result[0][28]);
				 customeraddress = customeraddress1; // org line  sujee commented print only address2 & 3 11/06/2018
				 customeradd1 = customeraddress1;
				 
                 if(customeraddress2 !='')
                 customeraddress =customeraddress+", "+customeraddress2;
                 customeradd2 = customeraddress2;
                 if(customeraddress3 !='')
                 customeraddress =customeraddress+", "+customeraddress3;
                 customeradd3 = customeraddress3;

				if(customerOutlet==null || customerOutlet=='' || customerOutlet==undefined){
					
					customerOutlet=0;
					
				}

				printtax=eval(result[0][47]);
				applytax=eval(result[0][48]);
				taxregistrationnumber=result[0][49];
				
				if(result[0][47]==null || result[0][47]=='' || result[0][47]==undefined){
	                printtax=0;
                }
				if(result[0][48]==null || result[0][48]=='' || result[0][48]==undefined){
					applytax=0;
                }
				if(result[0][49]==null || result[0][49]=='' || result[0][49]==undefined){
					taxregistrationnumber=0;
                }
				
				var arbcustomeraddress1=result[0][31];
                var arbcustomeraddress2=result[0][30];
				var arbcustomeraddress3=result[0][29];
				
				
				if(eval(result[0][37])==1){
					// org line sujee commented 01/07/2018
					//totalfinalamount=eval(result[0][32])-eval(result[0][33])-eval(result[0][34])-eval(result[0][35]);
					totalfinalamount=eval(result[0][32])-eval(result[0][33])-eval(result[0][34]);
				}else{
					totalfinalamount=eval(result[0][32])-eval(result[0][33])-eval(result[0][34]);
				}
				
				
				
				/*data["TOTSALES"]=eval(result[0][32]);
				data["TOTGOOD"]=eval(result[0][33]);
				data["TOTBAD"]=eval(result[0][34]);
				data["TOTFREE"]=eval(result[0][27]);
				data["TOTTAX"]=Math.abs(eval(result[0][38]));*/
				
				data["TOTSALES"]=eval(result[0][32])-eval(result[0][61]).toFixed(decimalplace);
				
			
				data["TOTSALES"]=eval(data["TOTSALES"]).toFixed(decimalplace)
				data["TOTGOOD"]=eval(result[0][33])-eval(result[0][62]).toFixed(decimalplace);
				data["TOTGOOD"]=eval(data["TOTGOOD"]).toFixed(decimalplace)
				data["TOTBAD"]=eval(result[0][34])-eval(result[0][62]).toFixed(decimalplace);
				data["TOTBAD"]=eval(data["TOTBAD"]).toFixed(decimalplace);
				
				data["TOTRETURNAMT"] =(parseFloat(data["TOTBAD"])+parseFloat(data["TOTGOOD"])).toFixed(decimalplace);
				
				data["TOTFREE"]=0;
				data["TOTFREE"]=eval(data["TOTFREE"]).toFixed(decimalplace)
				data["TOTTAX"]=Math.abs(eval(result[0][38]));
				data["TOTTAX"]=eval(data["TOTTAX"]).toFixed(decimalplace)
			
	
				data["totalSalesQty"]=eval(result[0][50]);
				data["totalReturnQty"]=eval(result[0][51]);
				data["totalDamagedQty"]=eval(result[0][52]);
				data["totalFreeQty"]=eval(result[0][40]);
				
				data["SALESTAX"]=eval(result[0][53]).toFixed(decimalplace);
				data["RETURNTAX"]=eval(result[0][54]).toFixed(decimalplace);
				data["DAMAGEDTAX"]=eval(result[0][55]).toFixed(decimalplace);
				data["FREETAX"]=eval(result[0][56]).toFixed(decimalplace);
				
				data["TOTEXC"]=eval(result[0][57]).toFixed(decimalplace);
				// org line sujee commented temp
				data["TOTVAT"]=eval(result[0][58]).toFixed(decimalplace);
			
				data["REBATE"] = result[0][64];
				
		
				
				// sujee added for ATYAB 09/09/2019
				data["DELIVERYFLAG"] = result[0][65];
				data["duedateflag"] = result[0][66];
				data["statementflag"] = result[0][67];
				var crdtdays= result[0][68];
				//alert('crdtdays' +crdtdays);
				//alert(data["statementflag"]);
				//alert('invoicepaymentterms' +invoicepaymentterms);
				if(invoicepaymentterms == 2 )
					{
					if(result[0][66] == 1)
					{
							if(result[0][67] == 0)
								{
										var Invoiceduedate=addDaysToDate(getTabletDate(),eval(crdtdays));
		
										console.log(Invoiceduedate);
									//	alert("Invoiceduedate"+Invoiceduedate);
										data["Invoiceduedate"] = Invoiceduedate;
								} else {
								var stdays= eval(crdtdays) + eval(crdtdays);
								//	var stdays= 0;
									//alert('stdays' +stdays);
									var Invoiceduedate=addDaysToDate(getTabletDate(),eval(stdays));
									//alert("Invoiceduedate"+Invoiceduedate);
									data["Invoiceduedate"] = Invoiceduedate;
								}
					} else {
						data["Invoiceduedate"]  = "";
					}
				
					}
				
				 data["rpcustcode"]=    result[0][0];
				 

	               var cashamt=eval(result[0][59]).toFixed(decimalplace);
                   var chequeamt=eval(result[0][60]).toFixed(decimalplace);
                   if(cashamt==0){
                   	
                   	data["ptype"]=1;
                   }else if(chequeamt==0){
                   	data["ptype"]=0;
                   	
                   }else if(cashamt>0 && chequeamt>0){
                   	data["ptype"]=2;
                   	
                   }
				
				arbcustomeraddress = arbcustomeraddress1;
				invoicepriceprint=eval(result[0][36]);
                if(arbcustomeraddress2 !='')
                	arbcustomeraddress =arbcustomeraddress+", "+arbcustomeraddress2;
                if(arbcustomeraddress3 !='')
                	arbcustomeraddress =arbcustomeraddress+", "+arbcustomeraddress3;
				data["Cash"] = {"Amount" : eval(result[0][14]).toFixed(decimalplace)};
				 if(result[0][16]!=''){
				 var d = new Date(result[0][16]);
				 result[0][16] = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
				 }
                data["Cheque"] = [{"Cheque Date" : (result[0][16]),"Cheque No" : (result[0][17]), "Bank" : result[0][15], "Amount" : (eval(result[0][14]).toFixed(decimalplace))}];
                
                data["TAXCARDNO"] = result[0][69];
                data["CRNO"] = result[0][70];
                data["EMAIL"] = result[0][71];
                data["CONTACTNAME"] = result[0][72];
                data["CUSTOMERPHONE"] = result[0][73];
                
               // GetSalesData('sales');
              //0 For display Discount and 1 For Report have UPC 
                checkInvoiceHeader('sales', '1',manualfree,splitfree);
            }
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}


function checkInvoiceHeader(trans, val,manualfree,splitfree){
	
	
	if(eval(manualfree)>0 && splitfree=='1'){
		
		GetSalesData('sales', '1',true);
	}else{
		
		GetSalesData('sales', '1',false);
	}
	
	
}
function GetSalesData(trans, val,isFree)
{ 	
   // alert(val);
    //Change for display UPC or discount ----Start
    if(val == '1')
    {
	    var total = {"QTY CAS/PCS" : "0/0","TOTAL PCS" : "0","DISCOUNT" : "0","TAX" : "0","AMOUNT" : "0" ,"AMOUNTAV" : "0"}; 
	    var Header = ["SL#","ITEM#","OUTLET CODE","DESCRIPTION","UPC","QTY CAS/PCS","TOTAL PCS","CASE PRICE","UNIT PRICE","DISCOUNT","EXC TAX","VAT","AMOUNT","AMOUNTAV","BARCODE"];

    }
    else
    {
        var total = {"QTY CAS/PCS" : "0/0","DISCOUNT" : "0","TAX" : "0","AMOUNT" : "0","AMOUNTAV" : "0" }; 
        var Header = ["SL#","ITEM#","OUTLET CODE","DESCRIPTION","QTY CAS/PCS","TOTAL PCS","CASE PRICE","UNIT PRICE","DISCOUNT","EXC TAX","VAT","AMOUNT","AMOUNTAV","BARCODE"];

    }
    //----------End
    // var total = {"Quantity" : "0/0","Discount" : "0","Amount" : "0" }; 
    // var Header = ["Item#","Description","Quantity","Case Price","Unit Price","Discount","Amount"];
	total["EXC TAX"]="0.00";
	total["VAT"]="0.00";
    var HeaderFree = ["SL#","ITEM#","OUTLET CODE","DESCRIPTION","UPC","QTY CAS/PCS","TOTAL PCS","CASE PRICE","UNIT PRICE","DISCOUNT","EXC TAX","VAT","AMOUNT","AMOUNTAV","BARCODE"]
    
    // org qry sujee commented removed tax% from all below qry 07/06/2018
    //var QrySales = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((salesqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((salesqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((salesqty%unitspercase) AS INT),0)) END AS 'quantity',salesqty AS 'tqty',IFNULL(salescaseprice,0) AS caseprice,IFNULL(salesprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(returnpromoamount,0) as discount,(((IFNULL(CAST((salesqty/unitspercase) AS INT),0) * IFNULL(salescaseprice,0)) + (IFNULL(CAST((salesqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0)) - IFNULL(promoamount,0) +IFNULL(inv.diffround,0))+ COALESCE(salesitemexcisetax,0) + COALESCE(salesitemgsttax,0) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,COALESCE(salesitemexcisetax,0) as excisetax, COALESCE(salesitemgsttax,0) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.salesqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
    
   // var QrySales = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((salesqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((salesqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((salesqty%unitspercase) AS INT),0)) END AS 'quantity',salesqty AS 'tqty',IFNULL(salescaseprice,0) AS caseprice,IFNULL(salesprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(returnpromoamount,0) as discount,(((IFNULL(CAST((salesqty/unitspercase) AS INT),0) * IFNULL(salescaseprice,0)) + (IFNULL(CAST((salesqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0)) - IFNULL(promoamount,0) +IFNULL(inv.diffround,0))+ COALESCE(salesitemexcisetax,0) + COALESCE(salesitemgsttax,0) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,COALESCE(salesitemexcisetax,0) as excisetax, COALESCE(salesitemgsttax,0)  as vat FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.salesqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
    
    var QrySales = "SELECT DISTINCT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((salesqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((salesqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((salesqty%unitspercase) AS INT),0)) END AS 'quantity',salesqty AS 'tqty',IFNULL(salescaseprice,0) AS caseprice,IFNULL(salesprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(returnpromoamount,0) as discount,(((IFNULL(CAST((salesqty/unitspercase) AS INT),0) * IFNULL(salescaseprice,0)) + (IFNULL(CAST((salesqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0)) - IFNULL(promoamount,0) +IFNULL(inv.diffround,0)) AS 'amount',  '*' || arbitemshortdescription || '!'  AS arbdescription,im.barcode1 as barcode,COALESCE(salesitemexcisetax,0) as excisetax, COALESCE(salesitemgsttax,0) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)   END || '%)' vat,(((IFNULL(CAST((salesqty/unitspercase) AS INT),0) * IFNULL(salescaseprice,0)) + (IFNULL(CAST((salesqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0)) - IFNULL(promoamount,0) +IFNULL(inv.diffround,0) + IFNULL(inv.salesitemgsttax,0)) AS 'amountav' FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.salesqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
    var QryFree = "SELECT DISTINCT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((manualfreeqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((manualfreeqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((manualfreeqty%unitspercase) AS INT),0)) END AS 'quantity',manualfreeqty AS 'tqty',0 AS caseprice,0 AS unitprice,(((IFNULL(CAST((manualfreeqty/unitspercase) AS INT),0) * IFNULL(salescaseprice,0)) + (IFNULL(CAST((manualfreeqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0))) AS 'discount',COALESCE(fgitemexcisetax,0)+COALESCE(fgitemgsttax,0) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,COALESCE(fgitemexcisetax,0)as excisetax, COALESCE(fgitemgsttax,0) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat,0 AS 'amountav' FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.manualfreeqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
    var QryPromoFree = "SELECT DISTINCT  0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((freesampleqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((freesampleqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((freesampleqty%unitspercase) AS INT),0)) END AS 'quantity',freesampleqty AS 'tqty',IFNULL(salescaseprice,0) AS caseprice,IFNULL(salesprice,0) AS unitprice,(((IFNULL(CAST((freesampleqty/unitspercase) AS INT),0) * IFNULL(salescaseprice,0)) + (IFNULL(CAST((freesampleqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0))) AS 'discount',COALESCE(promoitemexcisetax,0)+COALESCE(promoitemgsttax,0) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,COALESCE(promoitemexcisetax,0)as excisetax, COALESCE(promoitemgsttax,0)  as vat,0 AS 'amountav' FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and freesampleqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";	
    
    var QryBuyBack ="SELECT DISTINCT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((returnfreeqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((returnfreeqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((returnfreeqty%unitspercase) AS INT),0)) END AS 'quantity',returnfreeqty AS 'tqty',IFNULL(goodreturncaseprice,0) AS caseprice,IFNULL(goodreturnprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(returnpromoamount,0) as discount,(COALESCE(buybackexcisetax,0)+ COALESCE(buybackgsttax,0))*-1 AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,ABS(COALESCE(buybackexcisetax,0))as excisetax,ABS(COALESCE(buybackgsttax,0)) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat,0 AS 'amountav' FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.returnfreeqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
   
   /* var QryGood = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((quantity/unitspercase) AS INT),0) ELSE (IFNULL(CAST((quantity/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((quantity%unitspercase) AS INT),0)) END AS 'quantity',quantity AS 'tqty',IFNULL(goodreturncaseprice,0) AS caseprice,IFNULL(goodreturnprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(returnpromoamount,0) as discount,(((IFNULL(CAST((quantity/unitspercase) AS INT),0) * IFNULL(goodreturncaseprice,0)) + (IFNULL(CAST((quantity%unitspercase) AS INT),0)) * IFNULL(goodreturnprice,0)) - IFNULL(returnpromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0))+COALESCE(returnitemexcisetax,0)+COALESCE(returnitemgsttax,0) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode ,exp.description as reasoncode,ABS(COALESCE(returnitemexcisetax,0))as excisetax, ABS(COALESCE(returnitemgsttax,0))as vat FROM invoicerxddetail id  inner join invoicedetail inv  on inv.itemcode=id.itemcode JOIN itemmaster im ON im.actualitemcode = id.itemcode and quantity > 0 JOIN invoiceheader invh ON id.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 join retitmreasons exp on exp.code=id.reasoncode LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
    var QryBad = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((quantity/unitspercase) AS INT),0) ELSE (IFNULL(CAST((quantity/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((quantity%unitspercase) AS INT),0)) END AS 'quantity',quantity AS 'tqty',IFNULL(inv.returncaseprice,0) AS caseprice,IFNULL(inv.returnprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(returnpromoamount,0) as discount,(((IFNULL(CAST((quantity/unitspercase) AS INT),0) * IFNULL(inv.returncaseprice,0)) + (IFNULL(CAST((quantity%unitspercase) AS INT),0)) * IFNULL(inv.returnprice,0)) - IFNULL(returnpromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0))+COALESCE(damageditemexcisetax,0)+COALESCE(damageditemgsttax,0) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode ,exp.description as reasoncode,ABS(COALESCE(damageditemexcisetax,0))as excisetax, ABS(COALESCE(damageditemgsttax,0))as vat FROM invoicerxddetail id  inner join invoicedetail inv  on inv.itemcode=id.itemcode JOIN itemmaster im ON im.actualitemcode = id.itemcode and id.quantity > 0 JOIN invoiceheader invh ON id.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 join expiryreturnreasons exp on exp.code=id.reasoncode LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
   */
    var QryGood = "SELECT DISTINCT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((returnqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((returnqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((returnqty%unitspercase) AS INT),0)) END AS 'quantity',returnqty AS 'tqty',IFNULL(goodreturncaseprice,0) AS caseprice,IFNULL(goodreturnprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(returnpromoamount,0) as discount,(((IFNULL(CAST((returnqty/unitspercase) AS INT),0) * IFNULL(goodreturncaseprice,0)) + (IFNULL(CAST((returnqty%unitspercase) AS INT),0)) * IFNULL(goodreturnprice,0)) - IFNULL(returnpromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0)) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,ABS(COALESCE(returnitemexcisetax,0))as excisetax, ABS(COALESCE(returnitemgsttax,0)) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat,(((IFNULL(CAST((returnqty/unitspercase) AS INT),0) * IFNULL(goodreturncaseprice,0)) + (IFNULL(CAST((returnqty%unitspercase) AS INT),0)) * IFNULL(goodreturnprice,0)) - IFNULL(returnpromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0) + ABS(COALESCE(returnitemgsttax,0))) AS 'amountav' FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.returnqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
   
    // sujee commneted 05/02/2020  added expiry qty 
    //var QryBad = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((damagedqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((damagedqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((damagedqty%unitspercase) AS INT),0)) END AS 'quantity',damagedqty AS 'tqty',IFNULL(inv.returncaseprice,0) AS caseprice,IFNULL(inv.returnprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(returnpromoamount,0) as discount,(((IFNULL(CAST((damagedqty/unitspercase) AS INT),0) * IFNULL(inv.returncaseprice,0)) + (IFNULL(CAST((damagedqty%unitspercase) AS INT),0)) * IFNULL(inv.returnprice,0)) - IFNULL(returnpromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0))+COALESCE(damageditemexcisetax,0)+COALESCE(damageditemgsttax,0) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,ABS(COALESCE(damageditemexcisetax,0))as excisetax, ABS(COALESCE(damageditemgsttax,0))  as vat FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.damagedqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
    
    var QryBad = "SELECT DISTINCT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((damagedqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((damagedqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((damagedqty%unitspercase) AS INT),0)) END AS 'quantity',damagedqty AS 'tqty',IFNULL(inv.returncaseprice,0) AS caseprice,IFNULL(inv.returnprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(damagepromoamount,0) as discount,(((IFNULL(CAST((damagedqty/unitspercase) AS INT),0) * IFNULL(inv.returncaseprice,0)) + (IFNULL(CAST((damagedqty%unitspercase) AS INT),0)) * IFNULL(inv.returnprice,0)) - IFNULL(damagepromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0)) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,ABS(COALESCE(damageditemexcisetax,0))as excisetax, ABS(COALESCE(damageditemgsttax,0)) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat,(((IFNULL(CAST((damagedqty/unitspercase) AS INT),0) * IFNULL(inv.returncaseprice,0)) + (IFNULL(CAST((damagedqty%unitspercase) AS INT),0)) * IFNULL(inv.returnprice,0)) - IFNULL(damagepromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0) + ABS(COALESCE(damageditemgsttax,0))) AS 'amountav' FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.damagedqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  group by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END UNION ALL  SELECT DISTINCT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription AS 'description',unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((expiryqty/unitspercase) AS INT),0) ELSE (IFNULL(CAST((expiryqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((expiryqty%unitspercase) AS INT),0)) END AS 'quantity',expiryqty AS 'tqty',IFNULL(inv.returncaseprice,0) AS caseprice,IFNULL(inv.returnprice,0) AS unitprice,IFNULL(promoamount,0)+IFNULL(damagepromoamount,0) as discount,(((IFNULL(CAST((expiryqty/unitspercase) AS INT),0) * IFNULL(inv.returncaseprice,0)) + (IFNULL(CAST((expiryqty%unitspercase) AS INT),0)) * IFNULL(inv.returnprice,0)) - IFNULL(damagepromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0)) AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,ABS(COALESCE(damageditemexcisetax,0))as excisetax,  ABS(COALESCE(expiryitemgsttax,0)) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat,(((IFNULL(CAST((expiryqty/unitspercase) AS INT),0) * IFNULL(inv.returncaseprice,0)) + (IFNULL(CAST((expiryqty%unitspercase) AS INT),0)) * IFNULL(inv.returnprice,0)) - IFNULL(damagepromoamount,0) - IFNULL(roundsalesamount,0)+  IFNULL(inv.diffround,0) + ABS(COALESCE(expiryitemgsttax,0))) AS 'amountav' FROM invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.expiryqty > 0 JOIN invoiceheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2  group by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
     
    var Qry = "";    
      if(trans == 'sales')
        Qry = QrySales;
      else if(trans == 'free')
      {
        Qry = QryFree;
        Header = HeaderFree;
        delete total["Discount"];
     }
     else if(trans == 'promofree')
     {
        Qry = QryPromoFree;
        Header = HeaderFree;
        delete total["Discount"];
    }
    else if(trans == 'good')
        Qry = QryGood;
    else if(trans == 'buyback')
        Qry = QryBuyBack;
    else if(trans == 'bad')
        Qry = QryBad;
    console.log(Qry);                
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {
            SetSalesTransaction(trans,result,Header,total);
            if(trans == 'sales')
                //GetSalesData('free');
                GetSalesData('free','1'); //Added for display UPC
            if(trans == 'free')
            //    GetSalesData('promofree');
                GetSalesData('promofree','1'); //Added for display UPC
            if(trans == 'promofree')
              //  GetSalesData('good');
                GetSalesData('good','1'); //Added for display UPC
            if(trans == 'good')
               // GetSalesData('bad');
                GetSalesData('bad','1'); //Added for display UPC
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                if(trans == 'free' || trans == 'promofree')
                {
                	
                	if(eval(printbarcode)==1){
                		result = $.map(result.array, function(item, index) {
                            return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc,item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.amountav,item.barcode]];    
                        });
                		
                	}else{
                		result = $.map(result.array, function(item, index) {
                            return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc,item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.amountav,item.barcode]];    
                        });
                		
                		
                	}
                	
                    
                }
                else
                {
                	if(eval(printbarcode)==1){
                		 result = $.map(result.array, function(item, index) {
                             //Start for display UPC or Discount
                             if(val == '1')
                             {
                                 return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc,item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.amountav,item.barcode]];
                             }
                             else
                             {
                                 return [[item.sl,item.itemcode,item.outletcode,item.description, item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.amountav,item.barcode]];                               
                             }
                		  });
                	}else{
                		 result = $.map(result.array, function(item, index) {
                             //Start for display UPC or Discount
                             if(val == '1')
                             {
                                 return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc,item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.amountav,item.barcode]];
                             }
                             else
                             {
                                 return [[item.sl,item.itemcode,item.outletcode,item.description, item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.amountav,item.barcode]];                               
                             }
                		  });
                	}
                	
                   
                        //End
                       // return [[item.itemcode,item.description, item.quantity, item.caseprice, item.unitprice,item.discount,item.amount]];     
                  
                }
            }
            else
                result = [];
                
               // alert(JSON.stringify(result));
                
            SetSalesTransaction(trans, result, Header, total,isFree);
			if (trans == 'sales')
				// GetSalesData('free');
				GetSalesData('good', '1',isFree); // Added for display UPC
			if (trans == 'good')
				// GetSalesData('bad');
				GetSalesData('buyback', '1',isFree); // Added for display UPC
			if (trans == 'buyback')
				// GetSalesData('bad');
				GetSalesData('bad', '1',isFree); // Added for display UPC
			
			if (trans == 'bad')
				// GetSalesData('promofree');
				GetSalesData('promofree', '1',isFree); // Added for display UPC
			
			if (trans == 'promofree' && !isFree)
				// GetSalesData('good');
				GetSalesData('free', '1',isFree); // Added for display UPC
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function SetSalesTransaction(trans,result,Header,Total,isFree)
{   //alert(JSON.stringify(result));
		
        for(i=0;i<result.length;i++)
        {
            for(j=0;j<result[i].length;j++)
            {
                if(j ==1)
                {
                	
                	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                	{
                		result[i][j] = parseInt(result[i][j],10);
	                }
                	result[i][j] = (result[i][j]).toString();
                	
                }   
                if(j==2){
                	if(!isNaN(result[i][j]) && result[i][j]!='-')
                	{
                		result[i][j] = parseInt(eval(result[i][j]));
                		result[i][j] = (result[i][j]).toString();
                    }
                	else
                    {
                		result[i][j] = ("  ").toString();
                    }
                	
                }
                if(j == 5)
                {
                    var qtykey = "QTY CAS/PCS";
                    QuantityTotal(Total,qtykey,result[i][j]);
                }
                if(j == 6)
                {
                    var qtykey = "TOTAL PCS";
                    QTotal(Total,qtykey,result[i][j]);
                }
                else if(j == 9 && trans != 'free' && trans != 'promofree')
                    Total.DISCOUNT = (eval(Total.DISCOUNT) + eval(result[i][j])).toString();
                else if(j == 9 && (trans == 'free'))
                    Total.AMOUNT = (eval(Total.AMOUNT) + eval(result[i][j])).toFixed(decimalplace).toString();
                else if (j == 10) {
    				result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
    				Total.TAX = (eval(Total.TAX) + eval(result[i][j]))
    						.toString();
    				Total["EXC TAX"] = (eval(Total["EXC TAX"]) + eval(result[i][j]));
    			}
    			 else if (j == 11) {
    				/*result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
    				Total.TAX = (eval(Total.TAX) + eval(result[i][j]))
    						.toString();
    				Total["VAT"] = (eval(Total["VAT"]) + eval(result[i][j]));*/
    				 if(result[i][j].indexOf("(")!=-1){
    					 var tax=result[i][j].substring(0,result[i][j].indexOf("("));
    					 var vatrate=result[i][j].substring(result[i][j].indexOf("("),result[i][j].length);
    					 
    					 tax = (eval(tax));
    					 result[i][j] = tax+vatrate;
    					 Total.TAX = (eval(Total.TAX) + eval(tax))
    							.toString();
    					Total["VAT"] = (eval(Total["VAT"]) + eval(tax));
    				}else{
    					result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
    					Total.TAX = (eval(Total.TAX) + eval(result[i][j]))
    							.toString();
    					Total["VAT"] = (eval(Total["VAT"]) + eval(result[i][j]));
    				} 
    				}
                else if(j == 12 && trans != 'free'){
                    Total.AMOUNT = (eval(Total.AMOUNT) + eval(result[i][j])).toFixed(decimalplace).toString();
                   
                }
                else if(j == 12 && trans == 'free'){
                    Total.AMOUNT = (eval(Total.AMOUNT) + eval(result[i][j])).toFixed(decimalplace).toString();
                   
                } else if(j==13)
                	{
                	Total.AMOUNTAV = (eval(Total.AMOUNTAV) + eval(result[i][j])).toFixed(decimalplace).toString();
                	}
                if(j > 6 && j<=13){
                	if(j!=11){
                  		 result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
       				}
                	
                }
                  
            }
        }
        if(isNaN(Total.DISCOUNT))
            Total.DISCOUNT = 0;
        if(isNaN(Total.AMOUNT))
            Total.AMOUNT = 0;
        Total.DISCOUNT = eval(parseFloat(Total.DISCOUNT)).toFixed(decimalplace);
        Total.AMOUNT = eval(parseFloat(Total.AMOUNT)).toFixed(decimalplace);
    //SetData(trans,result,Header,total);
        Total["VAT"]=eval(Total["VAT"]).toFixed(decimalplace);	
      
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"TITLE":trans,"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});    
    if ((isFree && trans == "promofree") || trans=="free")
    {
    getCommonData();
    var invoicetype = ""
    var invtype="";
   
//    if(invoicepaymentterms == 0 || invoicepaymentterms == 1)
//        invoicetype = "CASH INVOICE: " +  invoicenumber;
//    else if(invoicepaymentterms == 3 || invoicepaymentterms == 4)
//        invoicetype = "CASH/TC INVOICE: " +  invoicenumber;
//    else if(invoicepaymentterms == 2)
//        invoicetype = "CREDIT INVOICE: " +  invoicenumber;
    	
    	
    	var  companyTaxStng=sessionStorage.getItem("enabletax");
        
    
    if(companyTaxStng==1){
    	if(eval(headerAmount)>=0){
			invoicetype = "TAX INVOICE ";
		}else{
			invoicetype = "TAX  INVOICE ";
		}
    	if(invoicepaymentterms == 0 || invoicepaymentterms == 1)
    	{
    	if(eval(headerAmount)>=0)
		{
    		invtype = "CASH INVOICE";
		}
	  else 
		{
		    invtype = "CREDIT NOTE";
		} 
    	}
    	
    	if(invoicepaymentterms == 2)
    	{
    	if(eval(headerAmount)>=0)
    		{
    		   invtype = "CREDIT INVOICE";
    		}
    	else 
    		{
    		   invtype = "CREDIT NOTE ";
    		}
    	}
    	
    }else{
    /*	org line sujee commented 18/06/2019 
     * if(eval(headerAmount)>=0){
			invoicetype = "SALES INVOICE " + invoicenumber;
		}else{
			invoicetype = "CREDIT NOTE " + invoicenumber;
		}*/
    	// sujee added18/06/2019 
   	 if(invoicepaymentterms == 0 || invoicepaymentterms == 1)
  	{
  	if(eval(headerAmount)>=0)
		{
		 invoicetype = "CASH INVOICE: " +  invoicenumber;
		}
	  else 
		{
        invoicetype = "CASH NOTE: " +  invoicenumber;
		}
  	
     
  	}
  else if(invoicepaymentterms == 3 || invoicepaymentterms == 4)
  	
  	{
  	if(eval(headerAmount)>=0)
		{
		 invoicetype = "CASH/TC INVOICE: " +  invoicenumber;
		}
	else 
		{
        invoicetype = "CASH/TC NOTE: " +  invoicenumber;
		}
    
  	}
  else if(invoicepaymentterms == 2)
  	{
  	if(eval(headerAmount)>=0)
  		{
  		 invoicetype = "CREDIT INVOICE: " +  invoicenumber;
  		}
  	else 
  		{
            invoicetype = "CREDIT NOTE: " +  invoicenumber;
  		}
  	}
 
    }
    
    	

    data["rpcustcode"]=    sessionStorage.getItem("customercode");
    data["enabletax"]=sessionStorage.getItem("enabletax");		
    data["companytaxregistrationnumber"]=sessionStorage.getItem("companytaxregistrationnumber");		
    data["invoicepaymentterms"]=invoicepaymentterms;	
    data["invoicenumber"]=invoicenumber;	
    data["INVOICETYPE"] = invoicetype;
    data["INVTYPE"] = invtype;
   
   // data["CUSTOMER"] = alternatecode +"/" +customercode  + "  "; 
    
    
    data["CUSTOMER"] = customername +"/" +alternatecode  + "  "; 
   //alert( data["CUSTOMER"]);

	data["CUSTOMERID"] = alternatecode +"";
   	data["CUSTOMERNAME"] = customername+""; 
   	
    data["printbarcode"]=eval(printbarcode);
    data["ADDRESS"] = customeraddress; //"Suite 808, Burjuman Business Tower";
    
    data["ADDRESS1"] =customeradd1;
    data["ADDRESS2"] =customeradd2;
    data["ADDRESS3"] =customeradd3;
    
    
	data["ARBADDRESS"] = arbcustomeraddress.substring(0, 25);  //"Suite 808, Burjuman Business Tower";
	//data["arbcustomername"]=arbcustomername.substring(0, 25); 
	
    if(comments == 0 || comments == "0")
    	comments = "";
    data["comments"] = unescape(comments);
    data["printstatus"] = getPrintStatus();
    data["printoutletitemcode"]=printoutletitemcode;
    data["printtax"] = printtax;
    data["applytax"] = applytax;
    data["taxregistrationnumber"] = taxregistrationnumber;
   
//    data["SUB TOTAL"] = eval(headerAmount).toFixed(decimalplace).toString();
  
    data["SUB TOTAL"] = eval(headerAmount).toFixed(
			decimalplace).toString();
  
    // org line sujee commented 10/09/2018
	/*data["INVOICE DISCOUNT"] = (totalpromoamount - itempromoamount + eval(manualfreediscount))
	.toFixed(decimalplace).toString();
	*/
	data["INVOICE DISCOUNT"] = eval(totalpromoamount  + manualfreediscount).toFixed(decimalplace).toString();
		
	
  //  data["NET SALES"] = eval(totalfinalamount).toFixed(decimalplace).toString();
	data["NET SALES"] = eval(totalfinalamount - (totalpromoamount  + manualfreediscount)).toFixed(decimalplace).toString();
    data["DOCUMENT NO"] = documentnumber;
    if(invheadermsg == 0 || invheadermsg == "0")
    	invheadermsg = "";
    data["invheadermsg"] = invheadermsg;
    data["invoicepriceprint"]=invoicepriceprint;
    
    if(invtrailormsg == 0 || invtrailormsg == "0")
    	invtrailormsg = "";
    data["invtrailormsg"] = invtrailormsg;
    data["isTwice"] = "0";
    data["TCCHARGED"] = (tcamount-totalpromoamount).toFixed(decimalplace).toString();
  
    if(paymenttype == 0)
    {
    	data["PaymentType"] = "0";
	}
	else if(paymenttype == 4)
	{
		data["PaymentType"] = "1";
	}
	else if(paymenttype == 1)
	{
		data["PaymentType"] = "2";
	}
	else
		data["PaymentType"] = "0";
	
	if(invoicepaymentterms > 2)
		data["TCALLOWED"] = "1";
	else
		data["TCALLOWED"] = "0";
    console.log(JSON.stringify(data)); 
    SetArray(ReportName.Sales);
    if(isFree && trans == 'promofree'){
		
    	var paymentdata={};
		paymentdata["Cash"]=data["Cash"];
		paymentdata["Cheque"]=data["Cheque"];
		data={};
		data["Cash"]=paymentdata["Cash"];
		data["Cheque"]=paymentdata["Cheque"];
		paymentdata={};
		GetSalesData('free', '1',isFree);
		
	}
	if(trans=="free"){
		ReportsToPrint(arrRpt,currpoint+1); 
		
	}
    
    
    }
}

function getCollectionInfo()
{
    data = {};
    var Qry = "SELECT cm.customercode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN customername ELSE arbcustomername END AS 'customername',CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN customeraddress1 ELSE arbcustomeraddress1 END AS 'address',typecode,CAST(amount AS VARCHAR) amount,bankname,checkdate,checknumber,invoicenumber,comments,printstatus,(SELECT CASE cm.messagekey5 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey5) AS invheadermsg,IFNULL(excesspayment,'') AS excesspayment,cm.alternatecode FROM arheader arh JOIN customermaster cm ON cm.customercode = arh.customercode JOIN cashcheckdetail ccd ON ccd.visitkey = arh.visitkey LEFT JOIN bankmaster bm ON bm.bankcode = ccd.bankcode WHERE arh.invoicenumber = " + invoicenumber;
    console.log(Qry);                
    if (platform == 'iPad') {
        Cordova.exec(function(result) 
        {
            if(result.length > 0)
            {
                customercode = (result[0][0]).toString();
                customername = (result[0][1]).toString();
                customeraddress = (result[0][2]).toString();                
                data["PaymentType"] = (result[0][3]).toString();
                //var amount = (result[0][4]).toString();
                var amount = eval(parseFloat(result[0][4])).toFixed(decimalplace);
                data["Cash"] = {"Amount" : amount};
		 if(result[0][6]!=''){
		 var d = new Date(result[0][6]);
    result[0][6] = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
		 }
                data["Cheque"] = [{"Cheque Date" : (result[0][6]),"Cheque No" : (result[0][7]).toString(), "Bank" : result[0][5].toString(), "Amount" : (amount).toString()}]; 
                invoicenumber = (result[0][8]).toString();
                comments = result[0][9];
                printstatus = result[0][10];
                invheadermsg = result[0][11];
                excesspayment = result[0][12];                
                getCollection();
            }
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.customercode,item.customername, item.address,item.typecode,item.amount,item.bankname,item.checkdate,item.checknumber,item.invoicenumber,item.comments,item.printstatus,item.invheadermsg,item.excesspayment,item.alternatecode]];
                });
                customercode = (result[0][0]).toString();
                customername = (result[0][1]).toString();
                
                customeraddress = (result[0][2]).toString();
                data["PaymentType"] = (result[0][3]).toString();
                //var amount = (result[0][4]).toString();
                var amount = eval(parseFloat(result[0][4])).toFixed(decimalplace);
                data["Cash"] = {"Amount" : amount};
                if(result[0][6]!=''){
                		var d = new Date(result[0][6]);
                		result[0][6] = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
                }
                data["Cheque"] = [{"Cheque Date" : (result[0][6]),"Cheque No" : (result[0][7]).toString(), "Bank" : result[0][5].toString(), "Amount" : (amount).toString()}]; 
                invoicenumber = (result[0][8]).toString();
                comments = result[0][9];
                printstatus = result[0][10];
                invheadermsg = result[0][11];
                excesspayment = result[0][12];
                alternatecode=result[0][13];
                getCollection();
            }
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function getCollection()
{    
    var Qry = "SELECT CASE WHEN '" + localStorage.getItem("alternatepending") + "' = '1' THEN ard.alternateinvoicenumber ELSE ard.invoicenumber END as invoicenumber,(SELECT STRFTIME('%d/%m/%Y',transactiondate) FROM customerinvoice WHERE invoicenumber=ard.invoicenumber) AS invoicedate,ard.totalinvoiceamount,ard.amountpaid,(ard.invoicebalance) AS invoicebalance FROM ardetail ard JOIN arheader arh ON ard.transactionkey = arh.transactionkey WHERE arh.invoicenumber = " + invoicenumber;
    console.log(Qry);                
    if (platform == 'iPad') {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)            
                result = [];
            ProcessCollection(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.invoicenumber,item.invoicedate, item.totalinvoiceamount,item.amountpaid, item.invoicebalance]];
                });
            }
            else
                result = [];
            
            ProcessCollection(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessCollection(result)
{    
    getCommonData();
    data["RECEIPT"] = invoicenumber;
    if(sessionStorage.getItem("Language")=='en'){
    	
   	 data["CUSTOMER"] = alternatecode + "   " + customername+""; //"5416-SWITZ MASTER BAKERS (Cr)";
   }else{
   	
   	 data["CUSTOMER"] = alternatecode + "   *" + customername+"!"; //"5416-SWITZ MASTER BAKERS (Cr)";
   }
   
    
    if(sessionStorage.getItem("Language")=='en'){
  	  data["ADDRESS"] = customeraddress; //"Suite 808, Burjuman Business Tower";
    }else{
  	
  	  data["ADDRESS"] = "*"+customeraddress+"!"; //"Suite 808, Burjuman Business Tower";
    }
 	
    if(comments == 0 || comments == "0")
    	comments = "";
    data["comments"] = unescape(comments);
    data["HEADERS"] = ["Invoice#","Due Date","Due Amount","Amount Paid","Invoice Balance"];
    var totalamount = 0;    
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {
            if(j == 0)
            {
                //result[i][j] = parseInt(eval(result[i][j]));                
                result[i][j] = (result[i][j]).toString();                
            }
            if(j == 4 || j == 7 || j == 8 || j==3)
            {                
                if(result[i][j] != "")
                    result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
            }
                
            if(j==3)
                totalamount += eval(result[i][j]);
        }
    }
    data["data"] = result;
    if(isNaN(totalamount))
        totalamount = 0;
    totalamount = eval(parseFloat(totalamount)).toFixed(decimalplace);
    data["TOTAL"] = {"Amount Paid" : totalamount};
    data["printstatus"] = getPrintStatus();
    if(invheadermsg == 0 || invheadermsg == "0")
    	invheadermsg = "";
    data["invheadermsg"] = invheadermsg;
    if(excesspayment == 0 || excesspayment == "")
    	excesspayment = "";	    	
    if(excesspayment!="")
    	data["expayment"] = eval(excesspayment).toFixed(decimalplace);
    else
    	data["expayment"] = excesspayment

    
    
    console.log(JSON.stringify(data));  
    SetArray(ReportName.Collection);    
    ReportsToPrint(arrRpt,currpoint+1);  
}

function getAdvancePaymentInfo()
{
    data = {};
    var Qry = "SELECT cm.customercode,customername,customeraddress1 as address,typecode,amount,bankname,checkdate,checknumber,invoicenumber,comments,printstatus,(SELECT CASE cm.messagekey5 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey5) AS invheadermsg,IFNULL(excesspayment,'') AS excesspayment,cm.alternatecode FROM arheader arh JOIN customermaster cm ON cm.customercode = arh.customercode JOIN cashcheckdetail ccd ON ccd.visitkey = arh.visitkey LEFT JOIN bankmaster bm ON bm.bankcode = ccd.bankcode WHERE arh.invoicenumber = " + invoicenumber;
    console.log(Qry);                
    if (platform == 'iPad') {
        Cordova.exec(function(result) 
        {
            
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.customercode,item.customername, item.address,item.typecode,item.amount,item.bankname,item.checkdate,item.checknumber,item.invoicenumber,item.comments,item.printstatus,item.invheadermsg,item.excesspayment,item.alternatecode]];
                });
                customercode = (result[0][0]).toString();
                customername = (result[0][1]).toString();
                
                customeraddress = (result[0][2]).toString();
                data["PaymentType"] = (result[0][3]).toString();
                //var amount = (result[0][4]).toString();
                var amount = eval(parseFloat(result[0][4])).toFixed(decimalplace);
                data["Cash"] = {"Amount" : amount};
                if(result[0][6]!=''){
                	var d = new Date(result[0][6]);
		 			result[0][6] = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
                }
                data["Cheque"] = [{"Cheque Date" : (result[0][6]),"Cheque No" : (result[0][7]).toString(), "Bank" : result[0][5].toString(), "Amount" : (amount).toString()}]; 
                invoicenumber = (result[0][8]).toString();
                comments = result[0][9];
                printstatus = result[0][10];
                invheadermsg = result[0][11];
                excesspayment = result[0][12];
                alternatecode=result[0][13];
                getAdvanceePayment();
            }
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function getAdvanceePayment()
{    
	var Qry = "SELECT CASE WHEN '" + localStorage.getItem("alternatepending") + "' = '1' THEN ard.alternateinvoicenumber ELSE ard.invoicenumber END as invoicenumber,(SELECT STRFTIME('%d/%m/%Y',transactiondate) FROM customerinvoice WHERE invoicenumber=ard.invoicenumber)  AS invoicedate,ard.totalinvoiceamount,ard.amountpaid,(ard.invoicebalance) AS invoicebalance FROM ardetail ard JOIN arheader arh ON ard.visitkey = arh.visitkey WHERE arh.invoicenumber ="
		+ invoicenumber;           
    if (platform == 'iPad') {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)            
                result = [];
            ProcessAdvancePayemnt(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                	return [ [ item.invoicenumber, item.invoicedate,
   							item.totalinvoiceamount ] ];
                });
            }
            else
                result = [];
                
            ProcessAdvancePayemnt(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessAdvancePayemnt(result)
{    
    getCommonData();
    data["RECEIPT"] = invoicenumber;
    data["CUSTOMER"] = alternatecode + "   " + customername;
    data["ADDRESS"] = customeraddress;
    
    if(comments == 0 || comments == "0")
    	comments = "";
    data["comments"] = unescape(comments);
    data["HEADERS"] = ["Invoice#","Invoice Date","Invoice Amount"];
    var totalamount = 0;    
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {
            if(j == 0)
            {
                //result[i][j] = parseInt(eval(result[i][j]));                
                result[i][j] = (result[i][j]).toString();                
            }
            if(j == 2)
            {                
                if(result[i][j] != "")
                    result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
            }
                
            if(j==2)
                totalamount += eval(result[i][j]);
        }
    }
    data["data"] = result;
    if(isNaN(totalamount))
        totalamount = 0;
    totalamount = eval(parseFloat(totalamount)).toFixed(decimalplace);
    data["TOTAL"] = {"Amount Paid" : totalamount};
    data["printstatus"] = getPrintStatus();
    if(invheadermsg == 0 || invheadermsg == "0")
    	invheadermsg = "";
    data["invheadermsg"] = invheadermsg;
    if(excesspayment == 0 || excesspayment == "")
    	excesspayment = "";	    	
    if(excesspayment!="")
    	data["expayment"] = eval(excesspayment).toFixed(decimalplace);
    else
    	data["expayment"] = excesspayment

    
    
    console.log(JSON.stringify(data));  
    SetArray(ReportName.AdvancePayment);    
    ReportsToPrint(arrRpt,currpoint+1);  
}


function PrintDeposit(type)
{
    data = {};
    getCashDeposit(type);
}

function getCashDeposit(type)
{
    //var Qry = "SELECT (CASE transactiontype WHEN 1 THEN invnumber WHEN 2 THEN arnumber END) AS transactionnumber,(CASE transactiontype WHEN 1 THEN invcustcode WHEN 2 THEN arcustcode END) AS customercode, amount FROM(SELECT ccd.transactiontype,arh.invoicenumber AS arnumber,inv.invoicenumber AS invnumber,arh.customercode AS arcustcode,inv.customercode AS invcustcode,customername,ccd.amount,advancepaymentflag,(CASE ccd.transactiontype WHEN 1 THEN 1 WHEN 2 THEN (CASE advancepaymentflag WHEN 0 THEN 2 WHEN 1 THEN 3 END) END) as transactionorder FROM cashcheckdetail ccd LEFT JOIN invoiceheader inv ON inv.visitkey = ccd.hhctransactionkey and inv.voidflag IS NOT 1 LEFT JOIN arheader arh ON arh.visitkey = ccd.hhctransactionkey and arh.voidflag IS NOT 1 LEFT JOIN customermaster cm ON cm.customercode = inv.customercode OR cm.customercode = arh.customercode WHERE typecode = 0) group by transactionnumber having transactionnumber >0 ORDER BY transactionorder";
   // var Qry="SELECT ih.invoicenumber AS transactionnumber, ih.customercode AS customercode,  ccd.amount as amount FROM invoiceheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey AND ccd.typecode = 0 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode UNION SELECT ih.invoicenumber AS transactionnumber, ih.customercode AS customercode,  ccd.amount FROM arheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey  AND ccd.typecode = 0 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode";
    //var Qry="SELECT ih.invoicenumber AS transactionnumber, ih.customercode AS customercode,ccd.amount as amount  FROM invoiceheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey AND ccd.typecode = 0 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode UNION SELECT ih.invoicenumber AS transactionnumber, ih.customercode AS customercode,  ccd.amount FROM arheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey  AND ccd.typecode = 0 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode where ih.invoicenumber > 0";
	
	var Qry="SELECT ih.invoicenumber AS transactionnumber, cm.alternatecode AS customercode,cm.customername as customername,ccd.amount as amount  FROM invoiceheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey AND ccd.typecode = 0 AND ih.immediatepaid !=0 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode UNION SELECT ih.invoicenumber AS transactionnumber, cm.alternatecode AS customercode,cm.customername as customername,ccd.amount FROM arheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey  AND ccd.typecode = 0 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode where ih.invoicenumber > 0";
    console.log(Qry);        
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                result = [];
            ProcessCashDeposit(result,type);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.transactionnumber,item.customercode,item.customername, item.amount]];
                });
            }
            else
                result = [];
            ProcessCashDeposit(result,type);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessCashDeposit(result,type)
{  
    var Header = ["Transaction Number","Customer Code","Customer Name","Amount"];
    var Total = {"Amount" : "0"};                
    var CashTotal = 0;
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {
        	
        	
        	if(j == 3)
            {
                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                CashTotal = eval(CashTotal) + eval(result[i][j]);
            }
        }
    }
    if(isNaN(Total.Amount))
        Total.Amount = 0;
    Total.Amount = (eval(parseFloat(CashTotal)).toFixed(decimalplace)).toString();
   
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});                        
    GetChequeDeposit(CashTotal,type);
}

function GetChequeDeposit(CashTotal,type)
{
    //var Qry = "SELECT (CASE transactiontype WHEN 1 THEN invnumber WHEN 2 THEN arnumber END) AS transactionnumber, (CASE transactiontype WHEN 1 THEN invcustcode WHEN 2 THEN arcustcode END) AS customercode, checknumber,checkdate,bankname,amount FROM(SELECT ccd.transactiontype,arh.invoicenumber AS arnumber,inv.invoicenumber AS invnumber,arh.customercode AS arcustcode,inv.customercode AS invcustcode,customername,ccd.amount,ccd.checknumber,ccd.checkdate AS checkdate,bm.bankname,advancepaymentflag,(CASE ccd.transactiontype WHEN 1 THEN 1 WHEN 2 THEN (CASE advancepaymentflag WHEN 0 THEN 2 WHEN 1 THEN 3 END) END) as transactionorder FROM cashcheckdetail ccd LEFT JOIN invoiceheader inv ON inv.visitkey = ccd.hhctransactionkey and inv.voidflag IS NOT 1 LEFT JOIN arheader arh ON arh.visitkey = ccd.hhctransactionkey and arh.voidflag IS NOT 1 LEFT JOIN customermaster cm ON cm.customercode = inv.customercode OR cm.customercode = arh.customercode LEFT JOIN bankmaster bm ON bm.bankcode = ccd.bankcode WHERE typecode = 1) group by transactionnumber having transactionnumber >0 ORDER BY transactionorder";
    //var Qry = "SELECT ih.invoicenumber AS transactionnumber, ih.customercode AS customercode,  ccd.checknumber as checknumber , ccd.checkdate as checkdate, bm.bankname as bankname , ccd.amount as amount FROM invoiceheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey AND ccd.typecode = 1 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode INNER JOIN bankmaster bm ON ccd.bankcode = bm.bankcode UNION SELECT ih.invoicenumber AS transactionnumber, ih.customercode AS customercode,  ccd.checknumber, ccd.checkdate, bm.bankname, ccd.amount FROM arheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey  AND ccd.typecode = 1 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode INNER JOIN bankmaster bm ON ccd.bankcode = bm.bankcode";
	var Qry="SELECT ih.invoicenumber AS transactionnumber, cm.alternatecode AS customercode,  ccd.checknumber as checknumber , ccd.checkdate as checkdate, bm.bankname as bankname , ccd.amount as amount FROM invoiceheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey AND ccd.typecode = 1 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode LEFT OUTER JOIN bankmaster bm ON ccd.bankcode = bm.bankcode UNION SELECT ih.invoicenumber AS transactionnumber, cm.alternatecode AS customercode,  ccd.checknumber, ccd.checkdate, bm.bankname, ccd.amount FROM arheader ih INNER JOIN cashcheckdetail ccd ON ih.visitkey = ccd.visitkey  AND ccd.typecode = 1 AND ih.voidflag IS NOT 1 INNER JOIN customermaster cm ON ih.customercode = cm.customercode LEFT OUTER JOIN bankmaster bm ON ccd.bankcode = bm.bankcode";
    console.log(Qry);            
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                result = [];
            ProcessChequeDeposit(CashTotal,result,type);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.transactionnumber,item.customercode, item.checknumber,item.checkdate,item.bankname,item.amount]];
                });
            }
            else
                result = [];
            ProcessChequeDeposit(CashTotal,result,type);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessChequeDeposit(CashTotal,result,type)
{
    var Header = ["Transaction Number","Customer Code","Cheque No","Cheque Date","Bank Name","Cheque Amount"];
    var Total = {"Cheque Amount" : "0"};    
    var ChequeTotal = 0;
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {                                    
            if(j == 5)
            {
                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                ChequeTotal = (eval(ChequeTotal) + eval(result[i][j]));
            }
	    if(j==3)
	    {
		if(result[i][j]=='' || result[i][j]==0)
		{
		    result[i][j]=0;
		}
		else{
		    var d = new Date(result[i][j]);
		    result[i][j] = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
		}
	    }
   
        }
    }
    if(isNaN(CashTotal))
        CashTotal = 0;
    if(isNaN(ChequeTotal))
        ChequeTotal = 0;
    CashTotal = eval(parseFloat(CashTotal)).toFixed(decimalplace);
    ChequeTotal = eval(parseFloat(ChequeTotal)).toFixed(decimalplace);
    Total["Cheque Amount"] = ChequeTotal.toString();
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});
    getCommonData();
    var totaldeposit = eval(CashTotal) + eval(ChequeTotal);
    data["TOTAL DEPOSIT AMOUNT"] = eval(parseFloat(totaldeposit)).toFixed(decimalplace).toString();
    console.log(JSON.stringify(data));
    if(type == 1)
    {
        SetArray(ReportName.Deposit);
    }else 
    {
        SetArray(ReportName.DepositSlip);
    }    
    
    ////ReportsToPrint(arrRpt,currpoint+1); 
    getVarianceAmount();
}

function getVarianceAmount()
{
     var autoload = sessionStorage.getItem("autoload");
            if(autoload == 3 || autoload == 4)
            var field ="endstockqty";
            else
            var field ="unloadqty";
	    
	var calculateqty ="CASE WHEN "+field+" = 0 THEN 0 ELSE ((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))-ifnull(truckdamagedunloadqty,0)-ifnull("+field+",0)-ifnull(freshunloadqty,0)) END";
	var Qry = "select sum((("+calculateqty+"%unitspercase)*defaultsalesprice)+(("+calculateqty+"/unitspercase)*caseprice)) as amount from inventorysummarydetail join itemmaster on itemmaster.actualitemcode=inventorysummarydetail.itemcode";
	
	var Qry ="SELECT SUM(((id.quantity%im.unitspercase)*id.itemprice)+((id.quantity/im.unitspercase)*id.itemcaseprice)) as amount FROM inventorytransactiondetail id INNER JOIN itemmaster im WHERE im.actualitemcode = id.itemcode AND id.transactiontypecode = 9";
	
	
	console.log(Qry);
	if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                totalvaramount = 0;
		else
		totalvaramount = result[0][0];
		
		data["totalvaramount"] = eval(totalvaramount).toFixed(decimalplace);
    		ReportsToPrint(arrRpt,currpoint+1);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.amount]];
                });
                totalvaramount = result[0][0];
            }
            else
            {
            	
            	totalvaramount = 0;
            }	
        		
            
            
            getTotalVariance(totalvaramount);	
    		
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function getTotalVariance(totalvaramount)
{
	var calculateqty = "ifnull(badreturnvariance,0)";
	var varianceAmount=0;
	var CaseEnabled;
	if (sessionStorage.getItem("CaseEnabled") == "0" || sessionStorage.getItem("CaseEnabled") == null)
	{
		CaseEnabled = false;
	}else if (sessionStorage.getItem("CaseEnabled") == "1")
	{
	    CaseEnabled = true;
	}	
     
    if (CaseEnabled) {
        var Qry ="select sum((("+calculateqty+"%unitspercase)*defaultsalesprice)+(("+calculateqty+"/unitspercase)*caseprice)) as amount from transactiondetailtemp join itemmaster on itemmaster.actualitemcode=transactiondetailtemp.itemcode";
    }
    else
    {
        var Qry ="select sum((("+calculateqty+"/unitspercase)*caseprice)) as amount from transactiondetailtemp join itemmaster on itemmaster.actualitemcode=transactiondetailtemp.itemcode";
    }
   
    if (platform == 'iPad') {
        Cordova.exec(function(result) {
                     	//need to develope for ios
                      },
                      function(error) {
                      alert(error);
                      },
                      "PluginClass",
                      "GetdataMethod",
                      [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
									        	if(result.array != undefined)
									            {
		        			
		                                             var array = $.map(result.array, function(item, index) {
		                                                              
		                                            	 				return [[item.amount]];
		                                                             
		                                                               });
		                                             	varianceAmount=parseInt(CheckIsNaN(array[0][0]));
									            }else
									            {
									            		varianceAmount=0;
									            	
									            }
                                             	data["totalvaramount"] = (eval(totalvaramount)+ eval(varianceAmount)).toFixed(decimalplace);
                                        		ReportsToPrint(arrRpt,currpoint+1);
                                             },
                                             function() {
                                            	 	console.warn("Error calling plugin");
                                             });
    }
    
    

}


function PrintSalesReport()
{
    data = {};
    // sujee added to get the cust visited data 03/02/2020
    getCustomerVisit();
    //getSalesReportCash();
}

function getCustomerVisit(){
	
	var Qry = "select 1 ,(select  count(distinct customercode) as cnt from customeroperationscontrol where routekey="+sessionStorage.getItem("RouteKey")+") as custvisit, (select count(*) from customeroperationscontrol where routekey="+sessionStorage.getItem("RouteKey")+" and totaltransactions=1) as invoiced";
	console.log(Qry);
	
	if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
        	
        	//alert(Qry);
        	//alert(JSON.stringify(result));
        	//  alert(result.array[0].custvisit);
              //alert(result.array[0].invoiced)
            if(result.array != undefined)
            {
           	 data["CUSTVISIT"]  = result.array[0].custvisit;
           	data["INVOICED"]  = result.array[0].invoiced;
            }
            else{
            	data["CUSTVISIT"] =  [];
        	    data["INVOICED"]  = [];
            }
            
            getGrandTotal();
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
	
}

var voidcashamt =0;
var voidcreditamt =0;
var voidtcamt =0;
function getGrandTotal(){
	// 30/03/2020
	//var Qry="SELECT ifnull(sum(totalsalesamount),0) AS totalsalesamount,ifnull(sum(totalreturnamount),0) as totalreturnamount,ifnull(sum(totaldamagedamount),0)+ ifnull(sum(totalexpiryamount),0) as totaldamagedamount, ifnull(sum(totalpromoamount),0) as Discount,ifnull(sum(totalinvoiceamount),0) as totalinvoiceamount,ifnull(sum(immediatepaid),0) as Amtpaid   FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode where inv.routekey="+sessionStorage.getItem("RouteKey")+" ";
	
	// sujee commented 30/03/2020
	//var Qry="SELECT ifnull(sum(totalsalesamount),0) AS totalsalesamount,ifnull(sum(totalreturnamount),0) as totalreturnamount,ifnull(sum(totaldamagedamount),0)+ ifnull(sum(totalexpiryamount),0) as totaldamagedamount, ifnull(sum(totalpromoamount),0) as Discount,ifnull(sum(totalinvoiceamount),0) as totalinvoiceamount,ifnull(sum(immediatepaid),0) as Amtpaid   FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode where inv.voidflag is null and inv.routekey="+sessionStorage.getItem("RouteKey")+" ";
	
	var Qry="SELECT ifnull(sum(totalsalesamount),0) AS totalsalesamount,ifnull(sum(totalreturnamount),0) as totalreturnamount,ifnull(sum(totaldamagedamount),0)+ ifnull(sum(totalexpiryamount),0) as totaldamagedamount, ifnull(sum(totalpromoamount),0) as Discount,ifnull(sum(totalinvoiceamount),0) as totalinvoiceamount,ifnull(sum(immediatepaid),0) as Amtpaid,( select ifnull(sum(immediatepaid),0)  from invoiceheader ih inner join customermaster cmm on ih.customercode=cmm.customercode  where  voidflag=1 and cmm.invoicepaymentterms in(0,1)) as voidcashamt, ( select ifnull(sum(totalsalesamount),0)  from invoiceheader ih inner join customermaster cmm on ih.customercode=cmm.customercode  where  voidflag=1 and cmm.invoicepaymentterms =2) as voidcreditamt,  ( select ifnull(sum(totalsalesamount),0)  from invoiceheader ih inner join customermaster cmm on ih.customercode=cmm.customercode  where  voidflag=1 and cmm.invoicepaymentterms in(3,4)) as voidtcamt    FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode where inv.voidflag is null and inv.routekey="+sessionStorage.getItem("RouteKey")+" ";
	console.log(Qry);

	if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
        	
        	//alert(Qry);
        //	alert(JSON.stringify(result));
       
            if(result.array != undefined)
            {
            	    data["GRANDTOT"]  = eval(result.array[0].totalsalesamount).toFixed(decimalplace);
                	data["RETTOT"]  = eval(result.array[0].totalreturnamount).toFixed(decimalplace);
                	data["BADTOT"]  = eval(result.array[0].totaldamagedamount).toFixed(decimalplace);
                	data["DISTOT"]  = eval(result.array[0].Discount).toFixed(decimalplace);
                	data["INVTOT"]  = eval(result.array[0].totalinvoiceamount).toFixed(decimalplace);
                	data["AMTTOT"]  = eval(result.array[0].Amtpaid).toFixed(decimalplace);
                	voidcashamt = eval(result.array[0].voidcashamt).toFixed(decimalplace);
                	voidcreditamt = eval(result.array[0].voidcreditamt).toFixed(decimalplace);
                	voidtcamt  = eval(result.array[0].voidtcamt).toFixed(decimalplace);
            }
            else{
		            data["GRANDTOT"]  =  [];
		        	data["GRANDRETTOT"]  =  [];
		        	data["GRANDBADTOT"]  =  [];
		        	data["GRANDDISTOT"]  =  [];
		        	data["GRANDINVTOT"]  =  [];
		        	data["GRANDAMTTOT"]  =  [];
		        	voidcashamt =0;
		        	voidcreditamt =0;
		        	voidtcamt =0;
            }
            getSalesReportCash();
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function getSalesReportCash()
{
	// org qry sujee commented 30/01/2019
    //var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0) AS dreturnamount,IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE paymenttype in(0,4) and voidflag IS NOT 1 AND invoicepaymentterms not in(3,4)";    
    
	//sujee commented 18/11/2019 
  //  var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0) AS dreturnamount,IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE    voidflag IS NOT 1  and invoicepaymentterms in(0,1) ";
    
    var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,case when inv.voidflag is null then 'NET' else 'VOID' END as Type,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0)+IFNULL(totalexpiryamount,0) AS dreturnamount,IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount,inv.immediatepaid FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE     invoicepaymentterms in(0,1) and inv.voidflag is null ";
    
    //var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,case when inv.voidflag is null then 'NET' else 'VOID' END as Type,case when inv.voidflag is null  then IFNULL(totalsalesamount,0) else IFNULL( - totalsalesamount,0)  end  AS totalsalesamount,case when inv.voidflag is null  then IFNULL(totalreturnamount,0) else IFNULL(- totalreturnamount,0) end    AS greturnamount,case when inv.voidflag is null  then IFNULL(totaldamagedamount,0)+IFNULL(totalexpiryamount,0) else  IFNULL( - totaldamagedamount,0)+IFNULL(-totalexpiryamount,0) end  AS dreturnamount,case when inv.voidflag is null  then IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) else IFNULL(-totalpromoamount,0)+IFNULL(-totaldiscountamount,0) end  AS invoicediscount,IFNULL(totalinvoiceamount,0)  AS totalamount,inv.immediatepaid as immediatepaid FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE     invoicepaymentterms in(0,1) ";
    console.log(Qry);            
   
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                result = [];
            ProcessSalesReportCash(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
        	
        	//alert(Qry);
        	//alert(JSON.stringify(result));
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.transactionnumber,item.customercode,item.customername, item.Type,item.totalsalesamount,item.greturnamount,item.dreturnamount,item.invoicediscount,item.totalamount,item.immediatepaid]];
                });
            }
            else
                result = [];
            
           // alert(JSON.stringify(result));
            ProcessSalesReportCash(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessSalesReportCash(result)
{   
    var Header = ["Transaction Number","Customer Code","Customer Name","Type","Sales Amount","G.Return Amount","Bad.Return Amount","Invoice Discount","Total Amount","Amt Paid"];
    //var Total = {"Total Amount" : "0"};
    var Total = {"Sales Amount" : "0","G.Return Amount" : "0", "Bad.Return Amount" : "0", "Invoice Discount" : "0","Total Amount" : "0","Amt Paid" : "0"};
    var TotalAmount = 0;
    var SalesAmount = 0;
    var GReturnAmount = 0;
    var DReturnAmount = 0;
    var InvoiceAmount = 0;
    var AmtPaid=0;
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {                                    
            if(j > 3)                                    
                result[i][j] = (eval(result[i][j])).toFixed(decimalplace); 
            if(j == 4)
                SalesAmount = (eval(SalesAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 5)
                GReturnAmount = (eval(GReturnAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 6)
                DReturnAmount = (eval(DReturnAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 7)
                InvoiceAmount = (eval(InvoiceAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 8)
                TotalAmount = (eval(TotalAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 9)
            	AmtPaid = (eval(AmtPaid) + eval(result[i][j])).toFixed(decimalplace);
        }
    }
    if(isNaN(TotalAmount))
        TotalAmount = 0;
    if(isNaN(SalesAmount))
        SalesAmount = 0;
    if(isNaN(GReturnAmount))
        GReturnAmount = 0;
    if(isNaN(DReturnAmount))
        DReturnAmount = 0;
    if(isNaN(InvoiceAmount))
        InvoiceAmount = 0;
    if(isNaN(AmtPaid))
    	AmtPaid = 0;  
    // sujee commented 30/03/2020
    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
    	//TotalAmount = eval(parseFloat(TotalAmount) -  parseFloat(voidcashamt) ).toFixed(decimalplace);
    SalesAmount = eval(parseFloat(SalesAmount)).toFixed(decimalplace);
    GReturnAmount = eval(parseFloat(GReturnAmount)).toFixed(decimalplace);
    DReturnAmount = eval(parseFloat(DReturnAmount)).toFixed(decimalplace);
    InvoiceAmount = eval(parseFloat(InvoiceAmount)).toFixed(decimalplace);
    // sujee commented 30/03/2020
    AmtPaid = eval(parseFloat(AmtPaid)).toFixed(decimalplace);
    
   // AmtPaid = eval(parseFloat(AmtPaid) - parseFloat(voidcashamt) ).toFixed(decimalplace);
    

    Total["Total Amount"] = TotalAmount.toString();
    Total["Sales Amount"] = SalesAmount.toString();
    Total["G.Return Amount"] = GReturnAmount.toString();
    Total["Bad.Return Amount"] = DReturnAmount.toString();
    Total["Invoice Discount"] = InvoiceAmount.toString();
    Total["Amt Paid"] = AmtPaid.toString();
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});                        
    getSalesReportCredit()
}

function getSalesReportCredit()
{
	// org qry sujee commented 30/01/2019 
   // var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0) AS dreturnamount,IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE invoicepaymentterms = 2 and voidflag IS NOT 1";    
    
	// sujee commented 18/11/2019 
  //  var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0) AS dreturnamount,IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE invoicepaymentterms = 2 and voidflag IS NOT 1";
    
	// sujee commented 30/03/2020
    var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,case when inv.voidflag is null then 'NET' else 'VOID' END as Type,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0)+IFNULL(totalexpiryamount,0) AS dreturnamount,IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount,inv.immediatepaid FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE invoicepaymentterms = 2 and inv.voidflag is null";
    
    //var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,case when inv.voidflag is null then 'NET' else 'VOID' END as Type,case when inv.voidflag is null  then IFNULL(totalsalesamount,0) else IFNULL( - totalsalesamount,0)  end  AS totalsalesamount,case when inv.voidflag is null  then IFNULL(totalreturnamount,0) else IFNULL(- totalreturnamount,0) end    AS greturnamount,case when inv.voidflag is null  then IFNULL(totaldamagedamount,0)+IFNULL(totalexpiryamount,0) else  IFNULL( - totaldamagedamount,0)+IFNULL(-totalexpiryamount,0) end  AS dreturnamount,case when inv.voidflag is null  then IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) else IFNULL(-totalpromoamount,0)+IFNULL(-totaldiscountamount,0) end  AS invoicediscount,IFNULL(totalinvoiceamount,0)  AS totalamount,inv.immediatepaid as immediatepaid FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE invoicepaymentterms = 2 ";
    console.log(Qry);  
    //alert(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                result = [];
            ProcessSalesReportCredit(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.transactionnumber,item.customercode,item.customername,item.Type,item.totalsalesamount,item.greturnamount,item.dreturnamount,item.invoicediscount,item.totalamount,item.immediatepaid]];
                });
            }
            else
                result = [];
            ProcessSalesReportCredit(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessSalesReportCredit(result)
{   
    var Header = ["Transaction Number","Customer Code","Customer Name","Type","Sales Amount","G.Return Amount","Bad.Return Amount","Invoice Discount","Total Amount","Amt Paid"];
    //var Total = {"Total Amount" : "0"};   
    var Total = {"Sales Amount" : "0","G.Return Amount" : "0", "Bad.Return Amount" : "0", "Invoice Discount" : "0","Total Amount" : "0","Amt Paid" : "0"};
    var TotalAmount = 0;
    var SalesAmount = 0;
    var GReturnAmount = 0;
    var DReturnAmount = 0;
    var InvoiceAmount = 0;
    var AmtPaid=0;
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {                                    
            if(j > 3)                                    
                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
            if(j == 4)
                SalesAmount = (eval(SalesAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 5)
                GReturnAmount = (eval(GReturnAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 6)
                DReturnAmount = (eval(DReturnAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 7)
                InvoiceAmount = (eval(InvoiceAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 8)
                TotalAmount = (eval(TotalAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 9)
            	AmtPaid = (eval(AmtPaid) + eval(result[i][j])).toFixed(decimalplace);
        }
    }
    if(isNaN(TotalAmount))
        TotalAmount = 0;
    if(isNaN(SalesAmount))
        SalesAmount = 0;
    if(isNaN(GReturnAmount))
        GReturnAmount = 0;
    if(isNaN(DReturnAmount))
        DReturnAmount = 0;
    if(isNaN(InvoiceAmount))
        InvoiceAmount = 0;
    if(isNaN(AmtPaid))
    	AmtPaid = 0;
    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
    //TotalAmount = eval(parseFloat(TotalAmount) -  parseFloat(voidcreditamt) ).toFixed(decimalplace);
    //alert(TotalAmount);
    SalesAmount = eval(parseFloat(SalesAmount)).toFixed(decimalplace);
    GReturnAmount = eval(parseFloat(GReturnAmount)).toFixed(decimalplace);
    DReturnAmount = eval(parseFloat(DReturnAmount)).toFixed(decimalplace);
    InvoiceAmount = eval(parseFloat(InvoiceAmount)).toFixed(decimalplace);
    AmtPaid = eval(parseFloat(AmtPaid)).toFixed(decimalplace);
    //AmtPaid = eval(parseFloat(AmtPaid) - parseFloat(voidcreditamt)).toFixed(decimalplace);
    Total["Total Amount"] = TotalAmount.toString();
    Total["Sales Amount"] = SalesAmount.toString();
    Total["G.Return Amount"] = GReturnAmount.toString();
    Total["Bad.Return Amount"] = DReturnAmount.toString();
    Total["Invoice Discount"] = InvoiceAmount.toString();
    Total["Amt Paid"] = AmtPaid.toString();
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});                        
    getSalesReportTC()
}

function getSalesReportTC()
{
	// org qry sujee commented 30/01/2019 
    //var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0) AS dreturnamount,IFNULL(totalpromoamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE invoicepaymentterms in(3,4) and voidflag IS NOT 1";    
    
	// sujee commented 18/11/2019 
   // var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0) AS dreturnamount,IFNULL(totalpromoamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE invoicepaymentterms in(3,4) and voidflag IS NOT 1";
    
	//sujee ommented 30/03/2020
   var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,case when inv.voidflag is null then 'NET' else 'VOID' END as Type,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0) AS dreturnamount,IFNULL(totalpromoamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount,inv.immediatepaid FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE invoicepaymentterms in(3,4) and inv.voidflag is null ";
    
  //  var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,case when inv.voidflag is null then 'NET' else 'VOID' END as Type,case when inv.voidflag is null  then IFNULL(totalsalesamount,0) else IFNULL( - totalsalesamount,0)  end  AS totalsalesamount,case when inv.voidflag is null  then IFNULL(totalreturnamount,0) else IFNULL(- totalreturnamount,0) end    AS greturnamount,case when inv.voidflag is null  then IFNULL(totaldamagedamount,0)+IFNULL(totalexpiryamount,0) else  IFNULL( - totaldamagedamount,0)+IFNULL(-totalexpiryamount,0) end  AS dreturnamount,case when inv.voidflag is null  then IFNULL(totalpromoamount,0)+IFNULL(totaldiscountamount,0) else IFNULL(-totalpromoamount,0)+IFNULL(-totaldiscountamount,0) end  AS invoicediscount,IFNULL(totalinvoiceamount,0)  AS totalamount,inv.immediatepaid as immediatepaid FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE invoicepaymentterms in(3,4)  ";
    console.log(Qry);  
    //alert(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                result = [];
            ProcessSalesReportTC(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.transactionnumber,item.customercode,item.customername,item.Type, item.totalsalesamount,item.greturnamount,item.dreturnamount,item.invoicediscount,item.totalamount,item.immediatepaid]];
                });
            }
            else
                result = [];
            ProcessSalesReportTC(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessSalesReportTC(result)
{   
    var Header = ["Transaction Number","Customer Code","Customer Name","Type","Sales Amount","G.Return Amount","Bad.Return Amount","Invoice Discount","Total Amount","Amt Paid"];
    //var Total = {"Total Amount" : "0"};
    var Total = {"Sales Amount" : "0","G.Return Amount" : "0", "Bad.Return Amount" : "0", "Invoice Discount" : "0","Total Amount" : "0","Amt Paid" : "0"};
    var TotalAmount = 0;
    var SalesAmount = 0;
    var GReturnAmount = 0;
    var DReturnAmount = 0;
    var InvoiceAmount = 0;
    var AmtPaid=0;
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {                                    
            if(j > 3)                                    
                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
            if(j == 4)
                SalesAmount = (eval(SalesAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 5)
                GReturnAmount = (eval(GReturnAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 6)
                DReturnAmount = (eval(DReturnAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 7)
                InvoiceAmount = (eval(InvoiceAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 8)
                TotalAmount = (eval(TotalAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 9)
            	AmtPaid = (eval(AmtPaid) + eval(result[i][j])).toFixed(decimalplace);
        }
    }
    if(isNaN(TotalAmount))
        TotalAmount = 0;
    if(isNaN(SalesAmount))
        SalesAmount = 0;
    if(isNaN(GReturnAmount))
        GReturnAmount = 0;
    if(isNaN(DReturnAmount))
        DReturnAmount = 0;
    if(isNaN(InvoiceAmount))
        InvoiceAmount = 0;
    if(isNaN(AmtPaid))
    	AmtPaid = 0;
    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
    //TotalAmount = eval(parseFloat(TotalAmount) - parseFloat(voidtcamt) ).toFixed(decimalplace);
    SalesAmount = eval(parseFloat(SalesAmount)).toFixed(decimalplace);
    GReturnAmount = eval(parseFloat(GReturnAmount)).toFixed(decimalplace);
    DReturnAmount = eval(parseFloat(DReturnAmount)).toFixed(decimalplace);
    InvoiceAmount = eval(parseFloat(InvoiceAmount)).toFixed(decimalplace);
    AmtPaid = eval(parseFloat(AmtPaid)).toFixed(decimalplace);
    
    //AmtPaid = eval(parseFloat(AmtPaid)- parseFloat(voidtcamt) ).toFixed(decimalplace);
    Total["Total Amount"] = TotalAmount.toString();
    Total["Sales Amount"] = SalesAmount.toString();
    Total["G.Return Amount"] = GReturnAmount.toString();
    Total["Bad.Return Amount"] = DReturnAmount.toString();
    Total["Invoice Discount"] = InvoiceAmount.toString();
    Total["Amt Paid"] = AmtPaid.toString();
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});                        
    getSalesReportCollection();
}

function getSalesReportCollection()
{
    var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,cm.customername,checknumber,checkdate,bankname,ccd.amount FROM cashcheckdetail ccd JOIN arheader arh ON arh.visitkey = ccd.hhctransactionkey  and voidflag IS NOT 1 Left JOIN bankmaster bm ON bm.bankcode = ccd.bankcode JOIN customermaster cm where cm.customercode=arh.customercode";    
    console.log(Qry);            
    
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <=0)
                result = [];
            ProcessSalesReportCollection(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.transactionnumber,item.customercode,item.customername, item.checknumber,item.checkdate,item.bankname,item.amount]];
                });
            }
            else
                result = [];
            ProcessSalesReportCollection(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessSalesReportCollection(result)
{   
    var Header = ["Transaction Number","Customer Code","Customer Name","Check Number","Check Date","Bank Name","Total Amount"];
    var Total = {"Total Amount" : "0"};                
    var TotalAmount = 0;
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {
            if(j == 6)
            {
                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                TotalAmount = (eval(TotalAmount) + eval(result[i][j])).toFixed(decimalplace);
            }
	    if(j==4)
	    {
		if(result[i][j] == '' || result[i][j] == 0)
		{
		    result[i][j]=0;
		}
		else{
	     var d = new Date(result[i][j]);
	      result[i][j] = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
		}
	    }
        }
    }
    if(isNaN(TotalAmount))
        TotalAmount = 0;
    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
    Total["Total Amount"] = TotalAmount.toString();
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});
/*    getCommonData();
    console.log(JSON.stringify(data));
    SetArray(ReportName.SalesReport);    
    ReportsToPrint(arrRpt,currpoint+1); */
    getVoidinvoice();
   
}

function getVoidinvoice()
{
	var Qry = "SELECT invoicenumber AS transactionnumber,cm.alternatecode as customercode,customername,case when cm.invoicepaymentterms =0 then 'VOID-CASH' when  cm.invoicepaymentterms =2 then 'VOID-CREDIT' else 'VOID-TC' end as Type,IFNULL(totalsalesamount,0) AS totalsalesamount,IFNULL(totalreturnamount,0) AS greturnamount,IFNULL(totaldamagedamount,0) AS dreturnamount,IFNULL(totalpromoamount,0) AS invoicediscount,IFNULL(totalinvoiceamount,0) AS totalamount,inv.immediatepaid FROM invoiceheader inv JOIN customermaster cm ON cm.customercode = inv.customercode WHERE inv.voidflag=1 ";
	
	 if (platform == 'Android') {
	        window.plugins.DataBaseHelper.select(Qry, function(result) {
	            if(result.array != undefined)
	            {
	                result = $.map(result.array, function(item, index) {
	                        return [[item.transactionnumber,item.customercode,item.customername,item.Type, item.totalsalesamount,item.greturnamount,item.dreturnamount,item.invoicediscount,item.totalamount,item.immediatepaid]];
	                });
	            }
	            else
	                result = [];
	            ProcessVoidData(result);
	        },
	        function() {
	            console.warn("Error calling plugin");
	        });
	    }
}

function   ProcessVoidData(result)
{
	  var Header = ["Transaction Number","Customer Code","Customer Name","Type","Sales Amount","G.Return Amount","Bad.Return Amount","Invoice Discount","Total Amount","Amt Paid"];
	    //var Total = {"Total Amount" : "0"};   
	    var Total = {"Sales Amount" : "0","G.Return Amount" : "0", "Bad.Return Amount" : "0", "Invoice Discount" : "0","Total Amount" : "0","Amt Paid" : "0"};
	    var TotalAmount = 0;
	    var SalesAmount = 0;
	    var GReturnAmount = 0;
	    var DReturnAmount = 0;
	    var InvoiceAmount = 0;
	    var AmtPaid=0;
	    for(i=0;i<result.length;i++)
	    {
	        for(j=0;j<result[i].length;j++)
	        {                                    
	            if(j > 3)                                    
	                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
	            if(j == 4)
	                SalesAmount = (eval(SalesAmount) + eval(result[i][j])).toFixed(decimalplace);
	            if(j == 5)
	                GReturnAmount = (eval(GReturnAmount) + eval(result[i][j])).toFixed(decimalplace);
	            if(j == 6)
	                DReturnAmount = (eval(DReturnAmount) + eval(result[i][j])).toFixed(decimalplace);
	            if(j == 7)
	                InvoiceAmount = (eval(InvoiceAmount) + eval(result[i][j])).toFixed(decimalplace);
	            if(j == 8)
	                TotalAmount = (eval(TotalAmount) + eval(result[i][j])).toFixed(decimalplace);
	            if(j == 9)
	            	AmtPaid = (eval(AmtPaid) + eval(result[i][j])).toFixed(decimalplace);
	        }
	    }
	    if(isNaN(TotalAmount))
	        TotalAmount = 0;
	    if(isNaN(SalesAmount))
	        SalesAmount = 0;
	    if(isNaN(GReturnAmount))
	        GReturnAmount = 0;
	    if(isNaN(DReturnAmount))
	        DReturnAmount = 0;
	    if(isNaN(InvoiceAmount))
	        InvoiceAmount = 0;
	    if(isNaN(AmtPaid))
	    	AmtPaid = 0;
	    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
	  //  TotalAmount = eval(parseFloat(TotalAmount) -  parseFloat(voidcreditamt) ).toFixed(decimalplace);
	 
	    SalesAmount = eval(parseFloat(SalesAmount)).toFixed(decimalplace);
	    GReturnAmount = eval(parseFloat(GReturnAmount)).toFixed(decimalplace);
	    DReturnAmount = eval(parseFloat(DReturnAmount)).toFixed(decimalplace);
	    InvoiceAmount = eval(parseFloat(InvoiceAmount)).toFixed(decimalplace);
	    AmtPaid = eval(parseFloat(AmtPaid)).toFixed(decimalplace);
	    //AmtPaid = eval(parseFloat(AmtPaid) - parseFloat(voidcreditamt)).toFixed(decimalplace);
	    Total["Total Amount"] = TotalAmount.toString();
	    Total["Sales Amount"] = SalesAmount.toString();
	    Total["G.Return Amount"] = GReturnAmount.toString();
	    Total["Bad.Return Amount"] = DReturnAmount.toString();
	    Total["Invoice Discount"] = InvoiceAmount.toString();
	    Total["Amt Paid"] = AmtPaid.toString();
	    if(data["data"] == undefined)
	        data["data"] = [];
	    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});       
	    getCommonData();
	    console.log(JSON.stringify(data));
	    SetArray(ReportName.SalesReport);    
	    ReportsToPrint(arrRpt,currpoint+1); 
}
function QuantityTotal(obj,qtykey,Qty)
{
    try
    {    
    var oldqty = obj[qtykey].toString().split("/");
    var currqty = Qty.toString().split("/");
    var newqty = (eval(oldqty[0]) + eval(currqty[0])) + "/" +  (eval(oldqty[1]) + eval(currqty[1]))
    obj[qtykey] = newqty;
    
    }
    catch(ex)
    {
        alert(ex);
    }
    /*console.log("showing qty.....");
    console.log(oldqty[0] + "&" + oldqty[1] + " : " + currqty[0] + "&" + currqty[1] + " : " + newqty); */              
}
function QTotal(obj,qtykey,Qty)
{
    
    
    try
    {    
    var oldqty = obj[qtykey];
    var currqty = Qty;
    var newqty = (eval(oldqty) + eval(currqty));
    obj[qtykey] = newqty;
   
    
    }
    catch(ex)
    {
        alert(ex);
    }
    /*console.log("showing qty.....");
    console.log(oldqty[0] + "&" + oldqty[1] + " : " + currqty[0] + "&" + currqty[1] + " : " + newqty); */              
}
function getCommonData()
{
	//alert(sessionStorage.getItem("contactno"));
    var d = new Date();
    var dt = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
    data["companyname"] = sessionStorage.getItem("CompanyName");
    data["arbcompanyname"] = sessionStorage.getItem("ARBCompanyName");
    data["companyaddress"] = sessionStorage.getItem("CompanyAddress");
    data["companytaxregistrationnumber"]=sessionStorage.getItem("companytaxregistrationnumber");	
    data["taxcodeno"]=sessionStorage.getItem("taxcodeno");
    data["companycrno"]=sessionStorage.getItem("companycrno");
    data["contactinfo"] = sessionStorage.getItem("ContactInfo");
    data["companylogo"] = "/images/logo.jpg";
    data["addresssetting"] = "1";
    data["TourID"] = sessionStorage.getItem("TourID");
    data["displayupc"]="1";  //For want to display UPC or not in sales report
    data["ROUTE"] = sessionStorage.getItem("RouteCode") + "-" + sessionStorage.getItem("RouteName"); //"110-NEWMTS";
    if(sessionStorage.getItem("Language")=='en'){
    	 data["ROUTE"] = sessionStorage.getItem("RouteCode") + "-" + sessionStorage.getItem("RouteName");
	 }
	 else{
		 data["ROUTE"] = sessionStorage.getItem("RouteCode") + "-" + sessionStorage.getItem("ARBRouteName")+"";
		 
	 }
    if(sessionStorage.getItem("Language")=='en'){
    	data["SALESMAN"] = sessionStorage.getItem("ALTSalesmanCode") + "-" + sessionStorage.getItem("SalesmanName"); //"104-NEW MTS SALESMAN";
        
    }else{
    	
    	data["SALESMAN"] = sessionStorage.getItem("ALTSalesmanCode") + "-" + sessionStorage.getItem("SalesmanName")+""; //"104-NEW MTS SALESMAN";
        
    }
	data["LANG"] =sessionStorage.getItem("Language");
    data["CONTACTNO"] = sessionStorage.getItem("contactno");
    data["DOC DATE"] = dt //"12/03/2012";
    data["DATE"] = dt;
    data["TIME"] = getTime(); //(d.getHours() + ":" + d.getMinutes());
    //data["DOCUMENT NO"] = "1000000021";
    data["supervisorname"] =sessionStorage.getItem("supervisorname");
	data["supervisorno"] =sessionStorage.getItem("supervisorphone");
	var tripdate=sessionStorage.getItem("TripDate");
	if(tripdate!='' && tripdate!=null && tripdate!=undefined){
		data["TRIP START DATE"] = tripdate;
	}else{
		data["TRIP START DATE"] = dt;
		
	}
    if(tranferflag == 0){
    	 if(sessionStorage.getItem("Language")=='en'){
    		 data["TO ROUTE"] = sessionStorage.getItem("RouteCode") + "-" + sessionStorage.getItem("RouteName");
    	 }
    	 else{
    		 data["TO ROUTE"] = sessionStorage.getItem("RouteCode") + "-" + sessionStorage.getItem("ARBRouteName")+"";
    		 
    	 }
    }
    data["footeraddress1"] = sessionStorage.getItem("footeraddress1");
	data["footeraddress2"] = sessionStorage.getItem("footeraddress2");
	// sujee added 19/06/2019 
    data["currname"]=sessionStorage.getItem("currname");	
    //data["rpcustcode"]=    sessionStorage.getItem("customercode");
}

function getTime()
{
    var d = new Date();
    var H = d.getHours();
    var M = d.getMinutes();
    H = (H < 9) ? ('0' + H) : H;
    M = (M < 9) ? ('0' + M) : M;
    return (H + ":" + M);
}

function getPrintStatus()
{
	if(copyStatus==0){
		 return "ORIGINAL COPY";
		
	}else{
		return "DUPLICATE COPY";
		
	}
//    if(printstatus == 0)
//       
//    else if(printstatus == 1)
//      
//    else
//        return "DUPLICATE COPY";
}

function SetArray(Report)
{
    //alert(Report);
	if(platform == "iPad")    	
    	ArrPrint.push([{ "data" : data, "name": Report}]);
    else
    	ArrPrint.push([{ "mainArr" : data, "name": Report}]);
}
//---Start-------For Route Activity Log by Mirnah   
function PrintRouteActivityReport()
{
    //alert("test");
    data = {};
    getRouteActivity();
}

function getRouteActivity()
{
   // alert("test2");
    //var Qry = "SELECT ih.documentnumber as documentnumber, ih.invoicenumber as invoicenumber, coc.visitstarttime as visitstarttime, coc.visitendtime as visitendtime,coc.customercode as customercode, 'SALE' trantype, ih.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN invoiceheader ih ON ih.routekey = coc.routekey AND ih.visitkey = coc.visitkey UNION SELECT soh.documentnumber as documentnumber, soh.invoicenumber as invoicenumber, coc.visitstarttime as visitstarttime, coc.visitendtime as visitendtime,coc.customercode as customercode, 'ORDER' trantype, soh.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN salesorderheader soh ON soh.routekey = coc.routekey AND soh.visitkey = coc.visitkey UNION SELECT arh.documentnumber as documentnumber , arh.invoicenumber as invoicenumber, coc.visitstarttime as visitstarttime, coc.visitendtime as visitendtime,coc.customercode as customercode,'COLLECTION' trantype, arh.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN arheader arh ON arh.routekey = coc.routekey AND arh.visitkey = coc.visitkey UNION SELECT ith.documentnumber as documentnumber, ith.documentnumber as invoicenumber, ith.transactiontime as visitstarttime , ith.transactiontime as visitendtime,0,CASE WHEN ith.transactiontype = 1 THEN 'LOAD' WHEN ith.transactiontype = 2 THEN 'TRANSFER' WHEN ith.transactiontype = 3 THEN 'UNLOAD' ELSE 'REQUEST' END trantype, 0 FROM inventorytransactionheader ith ORDER BY 1";
    //var Qry = "SELECT ih.documentnumber as documentnumber,ih.invoicenumber as invoicenumber,coc.visitstarttime as visitstarttime,coc.visitendtime as visitendtime,coc.customercode as customercode,group_CONCAT('SALE',CASE WHEN ih.voidflag = 1 THEN '**VOIDED**' ELSE '' END) as trantype,ih.totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN invoiceheader ih ON ih.routekey = coc.routekey AND ih.visitkey = coc.visitkey UNION SELECT soh.documentnumber  as documentnumber, soh.invoicenumber as invoicenumber,coc.visitstarttime as visitstarttime,coc.visitendtime as visitendtime,coc.customercode as customercode,group_CONCAT('ORDER',CASE WHEN soh.voidflag = 1 THEN '**VOIDED**' ELSE '' END) as trantype, soh.totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN salesorderheader soh ON soh.routekey = coc.routekey AND soh.visitkey = coc.visitkey  UNION  SELECT arh.documentnumber as documentnumber, arh.invoicenumber as invoicenumber, coc.visitstarttime as visitstarttime, coc.visitendtime as visitendtime ,coc.customercode as customercode, group_CONCAT('RECEIPT',CASE WHEN arh.voidflag = 1 THEN '**VOIDED**' ELSE '' END) as trantype, arh.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN arheader arh ON arh.routekey = coc.routekey AND arh.visitkey = coc.visitkey UNION SELECT ith.documentnumber as documentnumber, ith.documentnumber as invoicenumber, ith.transactiontime as visitstarttime, ith.transactiontime as visitendtime,0,CASE WHEN ith.transactiontype = 1 THEN 'LOAD' WHEN ith.transactiontype = 2 THEN 'TRANSFER' WHEN ith.transactiontype = 3 THEN 'UNLOAD' ELSE 'REQUEST' END trantype, 0 FROM inventorytransactionheader ith ORDER BY 1";
   // var Qry = "SELECT ih.documentnumber as documentnumber,ih.invoicenumber as invoicenumber,coc.visitstarttime as visitstarttime,coc.visitendtime as visitendtime,coc.customercode as customercode,CASE WHEN ih.voidflag = 1 THEN 'SALE*VOIDED*' ELSE 'SALE' END as trantype,ih.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN invoiceheader ih ON ih.routekey = coc.routekey AND ih.visitkey = coc.visitkey UNION SELECT soh.documentnumber  as documentnumber, soh.invoicenumber as invoicenumber,coc.visitstarttime as visitstarttime,coc.visitendtime as visitendtime,coc.customercode as customercode,CASE WHEN soh.voidflag = 1 THEN 'ORDER*VOIDED*' ELSE 'ORDER' END as trantype, soh.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN salesorderheader soh ON soh.routekey = coc.routekey AND soh.visitkey = coc.visitkey  UNION  SELECT arh.documentnumber as documentnumber, arh.invoicenumber as invoicenumber, coc.visitstarttime as visitstarttime, coc.visitendtime as visitendtime ,coc.customercode as customercode, CASE WHEN arh.voidflag = 1 THEN 'RECEIPT*VOIDED*' ELSE 'RECEIPT' END as trantype, arh.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN arheader arh ON arh.routekey = coc.routekey AND arh.visitkey = coc.visitkey UNION SELECT ith.documentnumber as documentnumber, ith.documentnumber as invoicenumber, ith.transactiontime as visitstarttime, ith.transactiontime as visitendtime,0,CASE WHEN ith.transactiontype = 1 THEN 'LOAD' WHEN ith.transactiontype = 2 THEN 'TRANSFER' WHEN ith.transactiontype = 3 THEN 'UNLOAD' ELSE 'REQUEST' END trantype, 0 as totalinvoiceamount FROM inventorytransactionheader ith ORDER BY 1";
    //var Qry = "SELECT ih.documentnumber as documentnumber,ih.invoicenumber as invoicenumber,coc.visitstarttime as visitstarttime,coc.visitendtime as visitendtime,coc.customercode as customercode,CASE WHEN ih.voidflag = 1 THEN '**VOIDED**' ELSE 'SALE' END as trantype,ih.totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN invoiceheader ih ON ih.routekey = coc.routekey AND ih.visitkey = coc.visitkey ";
    //var Qry="SELECT ih.documentnumber AS documentnumber,ih.invoicenumber AS invoicenumber,coc.visitstarttime AS visitstarttime,coc.visitendtime AS visitendtime,coc.customercode AS customercode,CASE WHEN ih.voidflag = 1 THEN 'SALE*VOIDED*' ELSE 'SALE' END AS trantype,ih.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN invoiceheader ih ON ih.routekey = coc.routekey AND ih.visitkey = coc.visitkey UNION SELECT soh.documentnumber  AS documentnumber, soh.invoicenumber AS invoicenumber,coc.visitstarttime AS visitstarttime,coc.visitendtime AS visitendtime,coc.customercode AS customercode,CASE WHEN soh.voidflag = 1 THEN 'ORDER*VOIDED*' ELSE 'ORDER' END AS trantype, soh.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN salesorderheader soh ON soh.routekey = coc.routekey AND soh.visitkey = coc.visitkey UNION SELECT arh.documentnumber AS documentnumber, arh.invoicenumber AS invoicenumber, coc.visitstarttime AS visitstarttime, coc.visitendtime AS visitendtime,coc.customercode AS customercode,CASE WHEN arh.voidflag = 1 THEN 'RECEIPT*VOIDED*' ELSE 'RECEIPT' END AS trantype, arh.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN arheader arh ON arh.routekey = coc.routekey AND arh.visitkey = coc.visitkey UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'LOAD' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 1 AND itd.transactiontypecode = 1 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'TRANSFER IN' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN        inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 2 AND itd.transactiontypecode = 3 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'TRANSFER OUT' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith  INNER JOIN        inventorytransactiondetail itd ON ith.detailkey = itd.detailkey   INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 2 AND itd.transactiontypecode = 2 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'UNLOAD' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN        inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 3 AND itd.transactiontypecode IN (5,6) GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'LOAD REQUEST' trantype, 0 AS totalinvoiceamount FROM inventorytransactionheader ith WHERE ith.transactiontype = 4 ORDER BY 1";
	var Qry="SELECT ih.documentnumber AS documentnumber,ih.invoicenumber AS invoicenumber,coc.visitstarttime AS visitstarttime,coc.visitendtime AS visitendtime, cm.alternatecode AS customercode, cm.customername as customername, CASE WHEN ih.voidflag = 1 THEN 'SALE*VOIDED*' ELSE 'SALE' END AS trantype,ih.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN invoiceheader ih ON ih.routekey = coc.routekey AND ih.visitkey = coc.visitkey INNER JOIN customermaster cm ON cm.customercode = coc.customercode UNION SELECT soh.documentnumber AS documentnumber, soh.invoicenumber AS invoicenumber,coc.visitstarttime AS visitstarttime,coc.visitendtime AS visitendtime, cm.alternatecode AS customercode, cm.customername as customername, CASE WHEN soh.voidflag = 1 THEN 'ORDER*VOIDED*' ELSE 'ORDER' END AS trantype, soh.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN salesorderheader soh ON soh.routekey = coc.routekey AND soh.visitkey = coc.visitkey INNER JOIN customermaster cm ON cm.customercode = coc.customercode UNION SELECT arh.documentnumber AS documentnumber, arh.invoicenumber AS invoicenumber, coc.visitstarttime AS visitstarttime, coc.visitendtime AS visitendtime, cm.alternatecode AS customercode, cm.customername as customername,CASE WHEN arh.voidflag = 1 THEN 'RECEIPT*VOIDED*' ELSE 'RECEIPT' END AS trantype, arh.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN arheader arh ON arh.routekey = coc.routekey AND arh.visitkey = coc.visitkey INNER JOIN customermaster cm ON cm.customercode = coc.customercode UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','LOAD' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS SIGNED) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 1 AND itd.transactiontypecode = 1 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','TRANSFER IN' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS SIGNED) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 2 AND itd.transactiontypecode = 3 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','TRANSFER OUT' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS SIGNED) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 2 AND itd.transactiontypecode = 2 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','UNLOAD' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 3 AND itd.transactiontypecode IN (5,6) GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','LOAD REQUEST' trantype, 0 AS totalinvoiceamount FROM inventorytransactionheader ith WHERE ith.transactiontype = 4 ORDER BY 1";
	//var Qry="SELECT ih.documentnumber AS documentnumber,ih.invoicenumber AS invoicenumber,coc.visitstarttime AS visitstarttime,coc.visitendtime AS visitendtime, coc.customercode AS customercode, cm.customername as customername, CASE WHEN ih.voidflag = 1 THEN 'SALE*VOIDED*' ELSE 'SALE' END AS trantype,ih.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN invoiceheader ih ON ih.routekey = coc.routekey AND ih.visitkey = coc.visitkey INNER JOIN customermaster cm ON cm.customercode = coc.customercode UNION SELECT soh.documentnumber AS documentnumber, soh.invoicenumber AS invoicenumber,coc.visitstarttime AS visitstarttime,coc.visitendtime AS visitendtime, coc.customercode AS customercode, cm.customername as customername, CASE WHEN soh.voidflag = 1 THEN 'ORDER*VOIDED*' ELSE 'ORDER' END AS trantype, soh.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN salesorderheader soh ON soh.routekey = coc.routekey AND soh.visitkey = coc.visitkey INNER JOIN customermaster cm ON cm.customercode = coc.customercode UNION SELECT arh.documentnumber AS documentnumber, arh.invoicenumber AS invoicenumber, coc.visitstarttime AS visitstarttime, coc.visitendtime AS visitendtime, coc.customercode AS customercode, cm.customername as customername,CASE WHEN arh.voidflag = 1 THEN 'RECEIPT*VOIDED*' ELSE 'RECEIPT' END AS trantype, arh.totalinvoiceamount AS totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN arheader arh ON arh.routekey = coc.routekey AND arh.visitkey = coc.visitkey INNER JOIN customermaster cm ON cm.customercode = coc.customercode UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','LOAD' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS SIGNED) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 1 AND itd.transactiontypecode = 1 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','TRANSFER IN' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS SIGNED) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 2 AND itd.transactiontypecode = 3 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','TRANSFER OUT' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS SIGNED) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 2 AND itd.transactiontypecode = 2 GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','UNLOAD' trantype, SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice) AS totalinvoiceamount FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 3 AND itd.transactiontypecode IN (5,6) GROUP BY ith.documentnumber, ith.documentnumber, ith.transactiontime, ith.transactiontime UNION SELECT ith.documentnumber AS documentnumber, ith.documentnumber AS invoicenumber, ith.transactiontime AS visitstarttime, ith.transactiontime AS visitendtime,0,'-','LOAD REQUEST' trantype, 0 AS totalinvoiceamount FROM inventorytransactionheader ith WHERE ith.transactiontype = 4 ORDER BY 1";
    console.log(Qry);            
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                result = [];
           // ProcessSalesReportCash(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
       // alert("test3");
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                    return[[item.invoicenumber,item.visitstarttime,item.visitendtime,item.customercode,item.customername,item.trantype,item.totalinvoiceamount]];
                       
                });
            }
            else
                result = [];
            ProcessRouteActivity(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
function ProcessRouteActivity(result)
{   
//alert(result);

    var Header = ["Transaction No","Time In","Time Out","Customer Code","Customer Name","Transaction Type","Total Amount"];
    var Total = {"Total Amount" : "0"};
    var TotalAmountTrIn = 0;
    var TotalAmountTrOut = 0;
    var TotalAmountLoad = 0;
    var TotalAmountUnload = 0;
    var TotalAmountSale = 0;
    var TotalAmountReceipt = 0;
    var TotalAmount = 0;
    var totamt=0;
    for(i=0;i<result.length;i++)
    {
     //  alert("tsss");
      // alert(result[i]);
        for(j=0;j<result[i].length-1;j++)
        {                                    
             //alert(result[i][j]);
               if(j == 1)
                {                
                    result[i][j] = (result[i][j]).substring(0,5);
                }
               if(j == 2)
               {
                    result[i][j] = (result[i][j]).substring(0,5);
               }
               
               if(j >4 )
                {
                       result[i][6] = (eval(result[i][6]).toFixed(decimalplace));
                      if (result[i][5].indexOf('*') > -1)
                      {
                         // alert(result[i][4]);
                           //TotalAmount = 0;
                      }else if(result[i][5].indexOf('TRANSFER IN') > -1) 
                      {
                          // alert(result[i][4]);
                           TotalAmountTrIn += eval(result[i][6]);
                           }
                      else if(result[i][5].indexOf('TRANSFER OUT') > -1) 
                      {
                          // alert(result[i][4]);
                           TotalAmountTrOut += eval(result[i][6]);
                        }else if(result[i][5].indexOf('LOAD') > -1) 
                        {
                               // alert(result[i][4]);
                                TotalAmountLoad += eval(result[i][6]);
                         }else if(result[i][5].indexOf('UNLOAD') > -1) 
                         {
                                    // alert(result[i][4]);
                          TotalAmountUnload += eval(result[i][5]);
                          }else if(result[i][5].indexOf('SALE') > -1) 
                          {
                            // alert(result[i][4]);
                          TotalAmountSale += eval(result[i][6]);
                          }else if(result[i][5].indexOf('RECEIPT') > -1) 
                          {
                            // alert(result[i][4]);
                            TotalAmountReceipt += eval(result[i][6]);
                          }
                          else
                          {
                         // alert(result[i][4]);
                          TotalAmount += eval(result[i][6]);
                          }
                          
               }
            
        }
    }
    if(isNaN(TotalAmount))
        TotalAmount = 0;
    TotalAmount = eval(parseFloat(TotalAmount).toFixed(decimalplace));
    if(isNaN(TotalAmountSale))
        TotalAmountSale = 0;
    TotalAmountSale = eval(parseFloat(TotalAmountSale).toFixed(decimalplace));
    if(isNaN(TotalAmountReceipt))
        TotalAmountReceipt = 0;
    TotalAmountReceipt = eval(parseFloat(TotalAmountReceipt).toFixed(decimalplace));
    //alert(TotalAmount);
    
    //data['loadvalue']=eval((TotalAmountLoad + TotalAmountTrIn)-TotalAmountTrOut).toFixed(decimalplace);
    //data['salesvalue']=eval(TotalAmountSale + TotalAmountReceipt).toFixed(decimalplace);
   /// data['receipt']=;
    //Total["Total Amount"] = TotalAmount.toFixed(decimalplace);
    Total["Total Amount"] = eval(TotalAmountSale + TotalAmountReceipt).toFixed(decimalplace);
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});                      
    
    getCommonData();
    console.log(JSON.stringify(data));
    SetArray(ReportName.RouteActivity);
    getOdoReading();
//    ReportsToPrint(arrRpt,currpoint+1); 
}

//function ProcessRouteActivity(result)
//{   
////alert(result);
//
//    var Header = ["Transaction No","Time In","Time Out","Customer Code","Customer Name","Transaction Type","Total Amount"];
//    var Total = {"Total Amount" : "0"};
//    var TotalAmountTrIn = 0;
//    var TotalAmountTrOut = 0;
//    var TotalAmountLoad = 0;
//    var TotalAmountUnload = 0;
//    var TotalAmountSale = 0;
//    var TotalAmountReceipt = 0;
//    var TotalAmount = 0;
//    var totamt=0;
//    for(i=0;i<result.length;i++)
//    {
//     //  alert("tsss");
//      // alert(result[i]);
//        for(j=0;j<result[i].length-1;j++)
//        {                                    
//             //alert(result[i][j]);
//               if(j == 1)
//                {                
//                    result[i][j] = (result[i][j]).substring(0,5);
//                }
//               if(j == 2)
//               {
//                    result[i][j] = (result[i][j]).substring(0,5);
//               }
//               
//               if(j > 4 )
//                {
//                       result[i][6] = (eval(result[i][6]).toFixed(decimalplace));
//                      if (result[i][5].indexOf('*') > -1)
//                      {
//                         // alert(result[i][4]);
//                           //TotalAmount = 0;
//                      }else if(result[i][5].indexOf('TRANSFER IN') > -1) 
//                      {
//                          // alert(result[i][4]);
//                           TotalAmountTrIn += eval(result[i][6]);
//                           }
//                      else if(result[i][5].indexOf('TRANSFER OUT') > -1) 
//                      {
//                          // alert(result[i][4]);
//                           TotalAmountTrOut += eval(result[i][6]);
//                        }else if(result[i][5].indexOf('LOAD') > -1) 
//                        {
//                               // alert(result[i][4]);
//                                TotalAmountLoad += eval(result[i][6]);
//                         }else if(result[i][5].indexOf('UNLOAD') > -1) 
//                         {
//                                    // alert(result[i][4]);
//                          TotalAmountUnload += eval(result[i][6]);
//                          }else if(result[i][5].indexOf('SALE') > -1) 
//                          {
//                            // alert(result[i][4]);
//                          TotalAmountSale += eval(result[i][5]);
//                          }else if(result[i][5].indexOf('RECEIPT') > -1) 
//                          {
//                            // alert(result[i][4]);
//                            TotalAmountReceipt += eval(result[i][6]);
//                          }
//                          else
//                          {
//                         // alert(result[i][4]);
//                          TotalAmount += eval(result[i][6]);
//                          }
//                          
//               }
//            
//        }
//    }
//    if(isNaN(TotalAmount))
//        TotalAmount = 0;
//    TotalAmount = eval(parseFloat(TotalAmount).toFixed(decimalplace));
//    if(isNaN(TotalAmountSale))
//        TotalAmountSale = 0;
//    TotalAmountSale = eval(parseFloat(TotalAmountSale).toFixed(decimalplace));
//    if(isNaN(TotalAmountReceipt))
//        TotalAmountReceipt = 0;
//    TotalAmountReceipt = eval(parseFloat(TotalAmountReceipt).toFixed(decimalplace));
//    //alert(TotalAmount);
//    
//    //data['loadvalue']=eval((TotalAmountLoad + TotalAmountTrIn)-TotalAmountTrOut).toFixed(decimalplace);
//    //data['salesvalue']=eval(TotalAmountSale + TotalAmountReceipt).toFixed(decimalplace);
//   /// data['receipt']=;
//    //Total["Total Amount"] = TotalAmount.toFixed(decimalplace);
//    Total["Total Amount"] = eval(TotalAmountSale + TotalAmountReceipt).toFixed(decimalplace);
//    if(data["data"] == undefined)
//        data["data"] = [];
//    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});                      
//    
//    getCommonData();
//    console.log(JSON.stringify(data));
//    SetArray(ReportName.RouteActivity);
//    getOdoReading();
////    ReportsToPrint(arrRpt,currpoint+1); 
//}



function getOdoReading()
{
  //  alert("SFf");
     var Qry = "select routestartodometer,routeendodometer from startendday where routekey=" + sessionStorage.getItem("RouteKey");
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                totalvaramount = 0;
        else
        totalvaramount = result[0][0];
        
        data["totalvaramount"] = eval(totalvaramount).toFixed(decimalplace);
            ReportsToPrint(arrRpt,currpoint+1);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.routestartodometer,item.routeendodometer]];
                });
                statreading = result[0][0];
                endreading = result[0][1];
            }
            else 
                {
                statreading = 0;
                endreading =  0;
                }
            data["startreading"] = eval(statreading);
            data["endreading"] = eval(endreading);
            data["totalkm"]=eval(endreading - statreading);
            ReportsToPrint(arrRpt,currpoint+1);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
//--------End Route Activity Log
//---Start-------For Route Summary by Mirnah
function PrintRouteSummaryReport()
{
    data = {};
    getRouteSummary();
}

function getRouteSummary()
{
    
    var Qry = "SELECT ih.documentnumber as documentnumber, ih.invoicenumber as invoicenumber, coc.visitstarttime as visitstarttime, coc.visitendtime as visitendtime,coc.customercode as customercode, 'SALE' trantype, ih.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN invoiceheader ih ON ih.routekey = coc.routekey AND ih.visitkey = coc.visitkey UNION SELECT soh.documentnumber as documentnumber, soh.invoicenumber as invoicenumber, coc.visitstarttime as visitstarttime, coc.visitendtime as visitendtime,coc.customercode as customercode, 'ORDER' trantype, soh.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN salesorderheader soh ON soh.routekey = coc.routekey AND soh.visitkey = coc.visitkey UNION SELECT arh.documentnumber as documentnumber , arh.invoicenumber as invoicenumber, coc.visitstarttime as visitstarttime, coc.visitendtime as visitendtime,coc.customercode as customercode,'COLLECTION' trantype, arh.totalinvoiceamount as totalinvoiceamount FROM customeroperationscontrol coc INNER JOIN arheader arh ON arh.routekey = coc.routekey AND arh.visitkey = coc.visitkey UNION SELECT ith.documentnumber as documentnumber, ith.documentnumber as invoicenumber, ith.transactiontime as visitstarttime , ith.transactiontime as visitendtime,0,CASE WHEN ith.transactiontype = 1 THEN 'LOAD' WHEN ith.transactiontype = 2 THEN 'TRANSFER' WHEN ith.transactiontype = 3 THEN 'UNLOAD' ELSE 'REQUEST' END trantype, 0 FROM inventorytransactionheader ith ORDER BY 1";
    console.log(Qry);     
   // alert(Qry);
   // alert("ss");
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                result = [];
           // ProcessSalesReportCash(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result)
                {
            
                result = [];
            ProcessRouteSummary(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
function ProcessRouteSummary(result)
{   
      
    var Header = ["Transaction No","Time In","Time Out","Customer Code","Transaction Type","Total Amount"];
    var Total = {"Total Amount" : "0"};
    var TotalAmount = 0;
    if(isNaN(TotalAmount))
        TotalAmount = 0;
    TotalAmount = eval(parseFloat(TotalAmount)).toFixed(decimalplace);
    Total["Total Amount"] = TotalAmount.toString();
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});                  

    getCommonData();
   // getVisitDetails();
    //getInventoryReport();
    
   //alert(data);
    console.log(JSON.stringify(data));
    SetArray(ReportName.RouteSummary); 
    getCashReport();
 
}

function getVisitDetails()
{
    // var Qry = "select routestartodometer,routeendodometer from startendday where routekey=" + sessionStorage.getItem("RouteKey");
    var Qry = "SELECT 1 SlNo, 'SHEDULED VISITS' TranType, COUNT(DISTINCT(customercode)) TotalNo FROM routesequencecustomerstatus WHERE schelduledflag = 1  UNION SELECT 2 SlNo, 'UNSHEDULED VISITS' TranType, COUNT(DISTINCT(customercode)) TotalNo FROM routesequencecustomerstatus WHERE schelduledflag = 0  UNION SELECT 3 SlNo, 'SERVICED CUSTOMERS' TranType, COUNT(DISTINCT(customercode)) TotalNo FROM routesequencecustomerstatus WHERE servicedflag = 1 UNION SELECT 4 SlNo, 'UNSERVICED CUSTOMERS' TranType, COUNT(DISTINCT(customercode)) TotalNo FROM routesequencecustomerstatus WHERE servicedflag = 0 UNION select 5 SlNo, 'NONSCANNEDCUSTOMER' TranType,count(customercode) AS TotalNo from routesequencecustomerstatus where ((servicedflag <> 0) and (scannedflag = 0)) ";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                totalvaramount = 0;
        else
        totalvaramount = result[0][0];
        
        data["totalvaramount"] = eval(totalvaramount).toFixed(decimalplace);
            ReportsToPrint(arrRpt,currpoint+1);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.SlNo,item.TranType,item.TotalNo]];
                });
                data["SHEDULEDCUSTOMERS"] = result[0][2];
                data["CUSTOMERSERVICED"] = result[1][2];
                data["UNSHEDULEDVISITS"] = result[2][2];
                data["CUSTOMERNOTSERVICED"] = result[3][2];
                data["NONSCANNEDCUSTOMER"] = result[4][2];
                data["VOIDINVOICES"] = "0";
                /*alert(result[0][2]);
                alert(result[1][2]);
                alert(result[2][2]);
                alert(result[3][2]);
                alert(result[4][2]);*/
            }
            else       
                {
                 data["SHEDULEDCUSTOMERS"] = "0";
                 data["CUSTOMERSERVICED"] = "0";
                 data["UNSHEDULEDVISITS"] = "0";
                 data["CUSTOMERNOTSERVICED"] = "0";
                 data["NONSCANNEDCUSTOMERS"] = "0";
                 data["VOIDINVOICES"] = "0";
                }
            
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
    
function getInventoryReport()
{
    // var Qry = "SELECT 1 SlNo,'OPENING' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12 UNION SELECT 2 SlNo,'LOADED' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1 UNION SELECT 3 SlNo,'TRANSFER IN' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3 UNION SELECT 4 SlNo,'TRANSFER OUT' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2 UNION SELECT 5 SlNo,'SALES' TranType, COALESCE(SUM(itd.salesqty * itd.salesprice),0) Amount FROM invoicedetail itd UNION SELECT 5 SlNo,'FREEE' TranType, COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) Amount FROM invoicedetail itd UNION SELECT 6 SlNo,'TRUCK DAMAGES' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11 UNION SELECT 7 SlNo,'FRESH UNLOAD' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14 UNION SELECT 8 SlNo,'BAD RETURNS' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7 UNION SELECT 9 SlNo,'BAD RETURNS VARIENCE' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 UNION SELECT 10 SlNo,'UNLOAD' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5 UNION SELECT 11 SlNo,'UNLOAD VARIENCE' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9 UNION SELECT 12 SlNo,'TOTAL VARIENCE' TranType, COALESCE(SUM(itd.quantity * itd.itemprice),0) Amount FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)";
    var Qry="select 1 as inv,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) as opening,(select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) as loded,(Select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) as tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) as transferout,(Select COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) as sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) as free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) as truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) as freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) as badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )as badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) as unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) as unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) as totvariance";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                totalvaramount = 0;
        else
            {
                 totalvaramount = result[0][0];
            }
        data["totalvaramount"] = eval(totalvaramount).toFixed(decimalplace);
            ReportsToPrint(arrRpt,currpoint+1);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                  return [[item.inv,item.opening,item.loded,item.tranferin,item.transferout,item.sales,item.free,item.truckdamage,item.freshunload,item.badreturn,item.badreturnvariance,item.unload,item.unloadvariance,item.totalvariance,item.variance]];
                });
                
                open = result[0][1];
                load = result[0][2];
                transferin=result[0][3];
                transferout=result[0][4];
                sss=result[0][5];
                free1=result[0][6];
                truckdamage=result[0][7];
                freshunload=result[0][8];
                badreturn=result[0][9];
                badreturnvar=result[0][10];
                unload=result[0][11];
                unloadvar=result[0][12];
                //totvar=result[0][13];
             
                
             }
            else 
                {
                open="0.00";;
                load="0.00";;
                transferin="0.00";;
                transferout="0.00";;
                sss="0.00";;
                free1="0.00";;
                truckdamage="0.00";;
                freshunload="0.00";;
                badreturn="0.00";;
                badreturnvar="0.00";;
                unload="0.00";;
                unloadvar="0.00";;
                totvar="0.00";;
                }
            ///alert(data["Opening"]);
                                
            data["Opening"] = eval(open).toFixed(decimalplace);
            data["Loaded"] = eval(load).toFixed(decimalplace);
            data["Transferin"] = eval(transferin).toFixed(decimalplace);
            data["Transferout"] = eval(transferout).toFixed(decimalplace);
            data["salesfree"] = eval(sss + free1).toFixed(decimalplace);
            data["freshunload"] = eval(freshunload).toFixed(decimalplace);
            data["truckdamage"] =eval(truckdamage).toFixed(decimalplace);
            data["badreturn"] = eval(badreturn).toFixed(decimalplace);
            data["calculatedunload"] = eval(badreturnvar).toFixed(decimalplace);
            data["unload"] = eval(unload).toFixed(decimalplace);
            data["unloadvariance"] = eval(unloadvar).toFixed(decimalplace);
            data["calculatedbadreturn"] = "0.00";
            data["unloadbadreturn"] = "0.00";
            data["returnvarinace"] = "0.00";
            data["Totalinvvarince"] = "0.00";         
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
/*
function getCashReport()
{
     //var Qry = "SELECT 1 as cash, (Select COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih) as todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2) as cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2) as creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2) as tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih) as todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0) as cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 ) as chkar";
   // var Qry="SELECT 1 as cash, (Select  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 1) as SHEDULEDCUSTOMERS,  (SELECT  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 0) as UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 1) as  SERVICEDCUSTOMERS ,(SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 0 ) as UNSERVICEDCUSTOMERS,(select count(customercode)   from routesequencecustomerstatus where ((servicedflag <> 0) and (scannedflag = 0))) as NONSCANNEDCUSTOMER,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) as opening,(select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) as loded,(Select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) as tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) as transferout,(Select COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) as sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) as free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) as truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) as freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) as badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )as badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) as unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) as unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) as totvariance,(Select COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih) as todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2) as cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2) as creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2) as tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih) as todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0) as cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 ) as chkar";
    //var Qry="SELECT 1 as cash, (Select  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 1) as SHEDULEDCUSTOMERS,  (SELECT  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 0) as UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 1) as  SERVICEDCUSTOMERS ,(SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 0 ) as UNSERVICEDCUSTOMERS,(select count(customercode)   from routesequencecustomerstatus where ((servicedflag <> 0) and (scannedflag = 0))) as NONSCANNEDCUSTOMER,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) as opening,(select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) as loded,(Select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) as tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) as transferout,(Select COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) as sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) as free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) as truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) as freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) as badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )as badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) as unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) as unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) as totvariance,(Select COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih where voidflag is null) as todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 and  ih.voidflag is null) as cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 and  ih.voidflag is null) as creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 and  ih.voidflag is null) as tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih where   ih.voidflag is null) as todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) as cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 and ih.voidflag = 0) as chkar";
    //var Qry="SELECT 1 AS cash,(SELECT  COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS SHEDULEDVISITS,(SELECT  COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0) AS UNSHEDULEDVISITS,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 1) AS  SERVICEDCUSTOMERS ,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 0 ) AS UNSERVICEDCUSTOMERS,(SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 0))) AS NONSCANNEDCUSTOMER,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) AS Opening,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) AS loded,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) AS tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) AS transferout,(SELECT COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) AS sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) AS free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) AS truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) AS freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) AS badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )AS badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) AS unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) AS unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) AS totvariance,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih WHERE voidflag IS NULL) AS todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 AND  ih.voidflag IS NULL) AS cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 AND  ih.voidflag IS NULL) AS creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 AND  ih.voidflag IS NULL) AS tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih WHERE   ih.voidflag IS NULL) AS todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) AS cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 AND ih.voidflag = 0) AS chkar,(sed.routestarttime as STARTTIME,sed.routeendtime as ENDTIME,(sed.routeendodometer-sed.routestartodometer) AS KMSCOVERED,((SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) +  (SELECT COUNT(*) FROM arheader WHERE voidflag = 1)) AS void FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday))";
    //var Qry="SELECT 1 as cash, (Select  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 1) as SHEDULEDVISITS,  (SELECT  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 0) as UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 1) as  SERVICEDCUSTOMERS ,(SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 0 ) as UNSERVICEDCUSTOMERS,(select count(customercode)   from routesequencecustomerstatus where ((servicedflag <> 0) and (scannedflag = 0))) as NONSCANNEDCUSTOMER,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) as opening,(select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) as loded,(Select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) as tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) as transferout,(Select COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) as sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) as free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) as truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) as freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) as badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )as badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) as unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) as unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) as totvariance,(Select COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih where voidflag is null) as todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 and  ih.voidflag is null) as cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 and  ih.voidflag is null) as creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 and  ih.voidflag is null) as tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih where   ih.voidflag is null) as todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) as cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 and ih.voidflag = 0) as chkar,IFNULL(sed.routestarttime,0) as STARTTIME,IFNULL(sed.routeendtime,0) as ENDTIME,ifnull(sed.routeendodometer-sed.routestartodometer,0) AS KMSCOVERED,(SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) +  (SELECT COUNT(*) FROM arheader WHERE voidflag = 1) AS void FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday)";
   
   
    //var Qry="SELECT 1 AS cash,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS SHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0) AS UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 1) AS SERVICEDCUSTOMERS,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 0 ) AS UNSERVICEDCUSTOMERS,(SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 0))) AS NONSCANNEDCUSTOMER,(SELECT COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 12 AND itd.issync = 1) AS opening,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 1 AND itd.issync = 1) AS loded,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 3 AND itd.issync = 1) AS tranferin,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 2 AND itd.issync = 1) AS transferout,(SELECT COALESCE(SUM((CAST((itd.salesqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.salesqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS sales, (SELECT COALESCE(SUM((CAST((itd.freesampleqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.freesampleqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS free, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 11 AND itd.issync = 1) AS truckdamage,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 14 AND itd.issync = 1) AS freshunload,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 7 AND itd.issync = 1) AS badreturn,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 10 AND itd.issync = 1 )AS badreturnvariance,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 5 AND itd.issync = 1) AS unload,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 9 AND itd.issync = 1) AS unloadvariance,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode IN (9,10) AND itd.issync = 1) AS totvariance,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih WHERE voidflag IS NULL) AS todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 AND ih.voidflag IS NULL) AS cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 AND ih.voidflag IS NULL) AS creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 AND ih.voidflag IS NULL) AS tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih WHERE ih.voidflag IS NULL) AS todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) AS cashar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 AND ih.voidflag = 0) AS chkar,IFNULL(sed.routestarttime,0) AS STARTTIME,IFNULL(sed.routeendtime,0) AS ENDTIME,IFNULL(sed.routeendodometer-sed.routestartodometer,0) AS KMSCOVERED,(SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) + (SELECT COUNT(*) FROM arheader WHERE voidflag = 1) AS void FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday)";
	
	var Qry="SELECT 1 AS cash,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS SHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0) AS UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 1) AS SERVICEDCUSTOMERS,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 0 ) AS UNSERVICEDCUSTOMERS,(SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 0))) AS NONSCANNEDCUSTOMER,(SELECT COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 12 AND itd.issync = 1) AS opening,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 1 AND itd.issync = 1) AS loded,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 3 AND itd.issync = 1) AS tranferin,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 2 AND itd.issync = 1) AS transferout,(SELECT COALESCE(SUM((CAST((itd.salesqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.salesqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS sales, (SELECT COALESCE(SUM((CAST((itd.freesampleqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.freesampleqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS free, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 11 AND itd.issync = 1) AS truckdamage,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 14 AND itd.issync = 1) AS freshunload,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 7 AND itd.issync = 1) AS badreturn,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 10 AND itd.issync = 1 )AS badreturnvariance,(SELECT COALESCE(SUM(CAST((itd.quantity/im.unitspercase) AS SIGNED) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice),0) FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 3 AND itd.transactiontypecode IN (5,6)) AS unload,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 9 AND itd.issync = 1) AS unloadvariance,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode IN (9,10) AND itd.issync = 1) AS totvariance,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih WHERE voidflag IS NULL) AS todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 AND ih.voidflag IS NULL) AS cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 AND ih.voidflag IS NULL) AS creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 AND ih.voidflag IS NULL) AS tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih WHERE ih.voidflag IS NULL) AS todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) AS cashar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 AND ih.voidflag = 0) AS chkar,IFNULL(sed.routestarttime,0) AS STARTTIME,IFNULL(sed.routeendtime,0) AS ENDTIME,IFNULL(sed.routeendodometer-sed.routestartodometer,0) AS KMSCOVERED,(SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) + (SELECT COUNT(*) FROM arheader WHERE voidflag = 1) AS void FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday)";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                totalvaramount = 0;
        else
        totalvaramount = result[0][0];
        
        data["totalvaramount"] = eval(totalvaramount).toFixed(decimalplace);
            ReportsToPrint(arrRpt,currpoint+1);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.cash,item.SHEDULEDVISITS,item.UNSHEDULEDVISITS,item.SERVICEDCUSTOMERS,item.UNSERVICEDCUSTOMERS,item.NONSCANNEDCUSTOMER,item.opening,item.loded,item.tranferin,item.transferout,item.sales,item.free,item.truckdamage,item.freshunload,item.badreturn,item.badreturnvariance,item.unload,item.unloadvariance,item.totvariance,item.todaysales,item.cashsales,item.creditsales,item.tcsales,item.todayar,item.cashar,item.chkar,item.STARTTIME,item.ENDTIME,item.KMSCOVERED,item.void]];
                });
                
             
                data["SHEDULEDVISITS"] = result[0][1];
                data["UNSHEDULEDVISITS"] = result[0][2];
                data["SERVICEDCUSTOMERS"] = result[0][3];
                data["UNSERVICEDCUSTOMERS"] = result[0][4];
                data["NONSCANNEDCUSTOMERS"] = result[0][5];
                data["Opening"] = eval(result[0][6]).toFixed(decimalplace);
                data["Loaded"] = eval(result[0][7]).toFixed(decimalplace);
                data["Transferin"] = eval(result[0][8]).toFixed(decimalplace);
                data["Transferout"] = eval(result[0][9]).toFixed(decimalplace);
                data["salesfree"] = eval(eval(result[0][10]) + eval(result[0][11])).toFixed(decimalplace);
                data["freshunload"] = eval(result[0][13]).toFixed(decimalplace);
                data["truckdamage"] =eval(result[0][12]).toFixed(decimalplace);
                data["badreturn"] = eval(result[0][14]).toFixed(decimalplace);
                data["calculatedunload"] = eval(result[0][15]).toFixed(decimalplace);
                data["unload"] = eval(result[0][16]).toFixed(decimalplace);
                data["unloadvariance"] = eval(result[0][17]).toFixed(decimalplace);
                data["calculatedbadreturn"] = "0.00";
                data["unloadbadreturn"] = "0.00";
                data["returnvarinace"] = "0.00";
                data["Totalinvvarince"] = eval(result[0][18]).toFixed(decimalplace);
                data["todaysales"] = eval(result[0][19]).toFixed(decimalplace);
                data["cashsales"] = eval(result[0][20]).toFixed(decimalplace);
                data["creditsales"] = eval(result[0][21]).toFixed(decimalplace);
                data["tcsales"] = eval(result[0][22]).toFixed(decimalplace);
                data["collection"] = eval(result[0][23]).toFixed(decimalplace);
                data["cash"] = eval(result[0][24]).toFixed(decimalplace);
                data["cheque"] = eval(result[0][25]).toFixed(decimalplace);
                data["STARTTIME"] = result[0][26];
                
               
                data["ENDTIME"] = result[0][27];
                if(result[0][27]==undefined || result[0][27]=='' || result[0][27]==0){
                	 var cDate = new Date();
                     var dtime = cDate.getHours() +':'+cDate.getMinutes()+':'+cDate.getSeconds();
                	data["ENDTIME"] = tDate;
                }
                
                data["KMSCOVERED"] = result[0][28];
                data["void"] = result[0][29];
            	}
            	else 
                {
               
                data["SHEDULEDVISITS"] = "0";
                data["UNSHEDULEDVISITS"] = "0";
                data["SERVICEDCUSTOMERS"] = "0";
                data["UNSERVICEDCUSTOMERS"] = "0";
                data["NONSCANNEDCUSTOMERS"] = "0";
                //data["VOIDINVOICES"] = "0";
                data["Opening"] = 0;
                data["Loaded"] = 0;
                data["Transferin"] = 0;
                data["Transferout"] = 0;
                data["salesfree"] = 0;
                data["freshunload"] = 0;
                data["truckdamage"] =0;
                data["badreturn"] = 0;
                data["calculatedunload"] = 0;
                data["unload"] = 0;
                data["unloadvariance"] = 0;
                data["calculatedbadreturn"] = "0.00";
                data["unloadbadreturn"] = "0.00";
                data["returnvarinace"] = "0.00";
                data["Totalinvvarince"] = 0;
                data["todaysales"] =0;
                data["cashsales"] =0;
                data["creditsales"] =0;
                data["tcsales"] =0;
                data["collection"] =0;
                data["cash"] =0;
                data["cheque"] =0;
                data["STARTTIME"] = 0;
                data["ENDTIME"] = 0;
                data["KMSCOVERED"] =0;
                data["void"] = 0;
                }
                      
           ReportsToPrint(arrRpt,currpoint+1);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
*/
function getCashReport()
{
	//SELECT (SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 1))) AS ScannedCustomers, (SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 0))) AS NonScannedCustomers, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag = 0) AS NoCallCustomer, (SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) AS VoidInvoices, (SELECT COUNT(*) FROM arheader WHERE voidflag = 1) AS VoidCollections, IFNULL(sed.routestarttime,0) AS StartTime, IFNULL(sed.routeendtime,0) AS EndTime, IFNULL(sed.routeendodometer-sed.routestartodometer,0) AS TotalKmsRun, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS PlannedCalls, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) AS CallsMadePlanned, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0) AS CallsMadeUnPlanned, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0) AS ActualCallsMade, (SELECT COUNT(DISTINCT(customercode)) FROM invoiceheader WHERE COALESCE(voidflag,0)=0) AS InvoicedCalls, ((SELECT COUNT(DISTINCT(customercode)) FROM invoiceheader WHERE COALESCE(voidflag,0)=0)*100)/((SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0)) AS ProductiveCalls, (((SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0))*100)/(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS CoverageCalls, (SELECT COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 12 AND itd.issync = 1) AS opening, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 1 AND itd.issync = 1) AS loded, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 3 AND itd.issync = 1) AS tranferin, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 2 AND itd.issync = 1) AS transferout, (SELECT COALESCE(SUM((CAST((itd.salesqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.salesqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS sales, (SELECT COALESCE(SUM((CAST((itd.freesampleqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.freesampleqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS free, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 11 AND itd.issync = 1) AS truckdamage, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 14 AND itd.issync = 1) AS freshunload, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 7 AND itd.issync = 1) AS badreturn, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 10 AND itd.issync = 1 )AS badreturnvariance, (SELECT COALESCE(SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice),0) FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 3 AND itd.transactiontypecode IN (5,6)) AS unload, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 9 AND itd.issync = 1) AS unloadvariance, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode IN (9,10) AND itd.issync = 1) AS totvariance, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih WHERE voidflag IS NULL) AS todaysales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 AND ih.voidflag IS NULL) AS cashsales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 AND ih.voidflag IS NULL) AS creditsales , (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 AND ih.voidflag IS NULL) AS tcsales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih WHERE ih.voidflag IS NULL) AS todayar, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) AS cashar, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 AND ih.voidflag = 0) AS chkar FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday)//var Qry = "SELECT 1 as cash, (Select COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih) as todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2) as cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2) as creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2) as tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih) as todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0) as cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 ) as chkar";
    // var Qry="SELECT 1 as cash, (Select  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 1) as SHEDULEDCUSTOMERS,  (SELECT  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 0) as UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 1) as  SERVICEDCUSTOMERS ,(SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 0 ) as UNSERVICEDCUSTOMERS,(select count(customercode)   from routesequencecustomerstatus where ((servicedflag <> 0) and (scannedflag = 0))) as NONSCANNEDCUSTOMER,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) as opening,(select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) as loded,(Select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) as tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) as transferout,(Select COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) as sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) as free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) as truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) as freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) as badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )as badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) as unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) as unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) as totvariance,(Select COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih) as todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2) as cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2) as creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2) as tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih) as todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0) as cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 ) as chkar";
    //var Qry="SELECT 1 as cash, (Select  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 1) as SHEDULEDCUSTOMERS,  (SELECT  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 0) as UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 1) as  SERVICEDCUSTOMERS ,(SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 0 ) as UNSERVICEDCUSTOMERS,(select count(customercode)   from routesequencecustomerstatus where ((servicedflag <> 0) and (scannedflag = 0))) as NONSCANNEDCUSTOMER,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) as opening,(select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) as loded,(Select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) as tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) as transferout,(Select COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) as sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) as free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) as truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) as freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) as badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )as badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) as unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) as unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) as totvariance,(Select COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih where voidflag is null) as todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 and  ih.voidflag is null) as cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 and  ih.voidflag is null) as creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 and  ih.voidflag is null) as tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih where   ih.voidflag is null) as todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) as cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 and ih.voidflag = 0) as chkar";
    //var Qry="SELECT 1 AS cash,(SELECT  COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS SHEDULEDVISITS,(SELECT  COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0) AS UNSHEDULEDVISITS,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 1) AS  SERVICEDCUSTOMERS ,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 0 ) AS UNSERVICEDCUSTOMERS,(SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 0))) AS NONSCANNEDCUSTOMER,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) AS Opening,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) AS loded,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) AS tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) AS transferout,(SELECT COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) AS sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) AS free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) AS truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) AS freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) AS badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )AS badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) AS unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) AS unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) AS totvariance,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih WHERE voidflag IS NULL) AS todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 AND  ih.voidflag IS NULL) AS cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 AND  ih.voidflag IS NULL) AS creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 AND  ih.voidflag IS NULL) AS tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih WHERE   ih.voidflag IS NULL) AS todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) AS cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 AND ih.voidflag = 0) AS chkar,(sed.routestarttime as STARTTIME,sed.routeendtime as ENDTIME,(sed.routeendodometer-sed.routestartodometer) AS KMSCOVERED,((SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) +  (SELECT COUNT(*) FROM arheader WHERE voidflag = 1)) AS void FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday))";
    //var Qry="SELECT 1 as cash, (Select  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 1) as SHEDULEDVISITS,  (SELECT  COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE schelduledflag = 0) as UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 1) as  SERVICEDCUSTOMERS ,(SELECT COUNT(DISTINCT(customercode))  FROM routesequencecustomerstatus WHERE servicedflag = 0 ) as UNSERVICEDCUSTOMERS,(select count(customercode)   from routesequencecustomerstatus where ((servicedflag <> 0) and (scannedflag = 0))) as NONSCANNEDCUSTOMER,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 12) as opening,(select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 1) as loded,(Select COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 3) as tranferin,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 2) as transferout,(Select COALESCE(SUM(itd.salesqty * itd.salesprice),0)  FROM invoicedetail itd) as sales,(SELECT COALESCE(SUM(itd.freesampleqty * itd.salesprice),0) FROM invoicedetail itd) as free,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 11) as truckdamage,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 14) as freshunload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 7) as badreturn,(SELECT  COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 10 )as badreturnvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 5) as unload,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0) FROM inventorytransactiondetail itd WHERE itd.transactiontypecode = 9) as unloadvariance,(SELECT COALESCE(SUM(itd.quantity * itd.itemprice),0)  FROM inventorytransactiondetail itd WHERE itd.transactiontypecode IN (9,10)) as totvariance,(Select COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih where voidflag is null) as todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 and  ih.voidflag is null) as cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 and  ih.voidflag is null) as creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 and  ih.voidflag is null) as tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih where   ih.voidflag is null) as todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)  FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) as cashar,(SELECT  COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 and ih.voidflag = 0) as chkar,IFNULL(sed.routestarttime,0) as STARTTIME,IFNULL(sed.routeendtime,0) as ENDTIME,ifnull(sed.routeendodometer-sed.routestartodometer,0) AS KMSCOVERED,(SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) +  (SELECT COUNT(*) FROM arheader WHERE voidflag = 1) AS void FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday)";
   
   
    //var Qry="SELECT 1 AS cash,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS SHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0) AS UNSHEDULEDVISITS, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 1) AS SERVICEDCUSTOMERS,(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE servicedflag = 0 ) AS UNSERVICEDCUSTOMERS,(SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 0))) AS NONSCANNEDCUSTOMER,(SELECT COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 12 AND itd.issync = 1) AS opening,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 1 AND itd.issync = 1) AS loded,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 3 AND itd.issync = 1) AS tranferin,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 2 AND itd.issync = 1) AS transferout,(SELECT COALESCE(SUM((CAST((itd.salesqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.salesqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS sales, (SELECT COALESCE(SUM((CAST((itd.freesampleqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.freesampleqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS free, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 11 AND itd.issync = 1) AS truckdamage,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 14 AND itd.issync = 1) AS freshunload,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 7 AND itd.issync = 1) AS badreturn,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 10 AND itd.issync = 1 )AS badreturnvariance,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 5 AND itd.issync = 1) AS unload,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 9 AND itd.issync = 1) AS unloadvariance,(SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode IN (9,10) AND itd.issync = 1) AS totvariance,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih WHERE voidflag IS NULL) AS todaysales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 AND ih.voidflag IS NULL) AS cashsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 AND ih.voidflag IS NULL) AS creditsales ,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 AND ih.voidflag IS NULL) AS tcsales,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih WHERE ih.voidflag IS NULL) AS todayar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) AS cashar,(SELECT COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 AND ih.voidflag = 0) AS chkar,IFNULL(sed.routestarttime,0) AS STARTTIME,IFNULL(sed.routeendtime,0) AS ENDTIME,IFNULL(sed.routeendodometer-sed.routestartodometer,0) AS KMSCOVERED,(SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) + (SELECT COUNT(*) FROM arheader WHERE voidflag = 1) AS void FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday)";
	
	//org qry sujee commented 
	//var Qry="SELECT (SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 1))) AS ScannedCustomers, (SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 0))) AS NonScannedCustomers, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag = 0) AS NoCallCustomer, (SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) AS VoidInvoices, (SELECT COUNT(*) FROM arheader WHERE voidflag = 1) AS VoidCollections, IFNULL(sed.routestarttime,0) AS StartTime, IFNULL(sed.routeendtime,0) AS EndTime, IFNULL(sed.routeendodometer-sed.routestartodometer,0) AS TotalKmsRun, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS PlannedCalls, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) AS CallsMadePlanned, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0) AS CallsMadeUnPlanned, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0) AS ActualCallsMade, (SELECT COUNT(DISTINCT(customercode)) FROM invoiceheader WHERE COALESCE(voidflag,0)=0) AS InvoicedCalls, ((SELECT COUNT(DISTINCT(customercode)) FROM invoiceheader WHERE COALESCE(voidflag,0)=0)*100)/((SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0)) AS ProductiveCalls, (((SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0))*100)/(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS CoverageCalls, (SELECT COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 12 AND itd.issync = 1) AS opening, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 1 AND itd.issync = 1) AS loded, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 3 AND itd.issync = 1) AS tranferin, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 2 AND itd.issync = 1) AS transferout, (SELECT COALESCE(SUM((CAST((itd.salesqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.salesqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS sales, (SELECT COALESCE(SUM((CAST((itd.freesampleqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.freesampleqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS free, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 11 AND itd.issync = 1) AS truckdamage, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 14 AND itd.issync = 1) AS freshunload, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 7 AND itd.issync = 1) AS badreturn, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 10 AND itd.issync = 1 )AS badreturnvariance, (SELECT COALESCE(SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice),0) FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 3 AND itd.transactiontypecode IN (5,6)) AS unload, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 9 AND itd.issync = 1) AS unloadvariance, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode IN (9,10) AND itd.issync = 1) AS totvariance, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih WHERE voidflag IS NULL) AS todaysales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 AND ih.voidflag IS NULL) AS cashsales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 AND ih.voidflag IS NULL) AS creditsales , (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 AND ih.voidflag IS NULL) AS tcsales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih WHERE ih.voidflag IS NULL) AS todayar, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) AS cashar, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 AND ih.voidflag = 0) AS chkar FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday)";
	
	var Qry="SELECT (SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 1))) AS ScannedCustomers, (SELECT COUNT(customercode) FROM routesequencecustomerstatus WHERE ((servicedflag <> 0) AND (scannedflag = 0))) AS NonScannedCustomers, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag = 0) AS NoCallCustomer, (SELECT COUNT(*) FROM invoiceheader WHERE voidflag = 1) AS VoidInvoices, (SELECT COUNT(*) FROM arheader WHERE voidflag = 1) AS VoidCollections, IFNULL(sed.routestarttime,0) AS StartTime, IFNULL(sed.routeendtime,0) AS EndTime, IFNULL(sed.routeendodometer-sed.routestartodometer,0) AS TotalKmsRun, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS PlannedCalls, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) AS CallsMadePlanned, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0) AS CallsMadeUnPlanned, (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0) AS ActualCallsMade, (SELECT COUNT(DISTINCT(customercode)) FROM invoiceheader WHERE COALESCE(voidflag,0)=0) AS InvoicedCalls, ((SELECT COUNT(DISTINCT(customercode)) FROM invoiceheader WHERE COALESCE(voidflag,0)=0)*100)/((SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0)) AS ProductiveCalls, (((SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1 AND servicedflag > 0) + (SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 0 AND servicedflag > 0))*100)/(SELECT COUNT(DISTINCT(customercode)) FROM routesequencecustomerstatus WHERE schelduledflag = 1) AS CoverageCalls, (SELECT COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 12 AND itd.issync = 1) AS opening, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 1 AND itd.issync = 1) AS loded, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 3 AND itd.issync = 1) AS tranferin, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 2 AND itd.issync = 1) AS transferout, (SELECT COALESCE(SUM((CAST((itd.salesqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.salesqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS sales, (SELECT COALESCE(SUM((CAST((itd.freesampleqty/im.unitspercase) AS INT) * itd.stdsalescaseprice) + ((itd.freesampleqty % im.unitspercase) * itd.stdsalesprice)),0) FROM invoicedetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode) AS free, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 11 AND itd.issync = 1) AS truckdamage, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 14 AND itd.issync = 1) AS freshunload, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 7 AND itd.issync = 1) AS badreturn, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 10 AND itd.issync = 1 )AS badreturnvariance, (SELECT COALESCE(SUM(CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice + (itd.quantity%im.unitspercase) * itd.itemprice),0) FROM inventorytransactionheader ith INNER JOIN inventorytransactiondetail itd ON ith.detailkey = itd.detailkey INNER JOIN itemmaster im ON itd.itemcode = im.actualitemcode WHERE ith.transactiontype = 3 AND itd.transactiontypecode IN (5,6)) AS unload, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode = 9 AND itd.issync = 1) AS unloadvariance, (SELECT  COALESCE(SUM((CAST((itd.quantity/im.unitspercase) AS INT) * itd.itemcaseprice)+ ((itd.quantity % im.unitspercase) * itd.itemprice)),0) FROM inventorytransactiondetail itd INNER JOIN itemmaster im ON im.actualitemcode = itd.itemcode WHERE itd.transactiontypecode IN (9,10) AND itd.issync = 1) AS totvariance, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih WHERE voidflag IS NULL) AS todaysales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms < 2 AND ih.voidflag IS NULL) AS cashsales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms = 2 AND ih.voidflag IS NULL) AS creditsales , (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM invoiceheader ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode WHERE cm.invoicepaymentterms > 2 AND ih.voidflag IS NULL) AS tcsales, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih WHERE ih.voidflag IS NULL) AS todayar, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0) FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 0 AND ih.voidflag = 0) AS cashar, (SELECT COALESCE(SUM(ih.totalinvoiceamount),0)FROM arheader ih INNER JOIN cashcheckdetail ccd ON ccd.routekey = ih.routekey AND ccd.visitkey = ih.visitkey AND ccd.typecode = 1 AND ih.voidflag = 0) AS chkar,(select ifnull(CAST(sum(itd.quantity/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum(itd.quantity%im.unitspercase) AS INTEGER),0) from inventorytransactiondetail itd inner join itemmaster im on itd.itemcode=im.actualitemcode WHERE itd.transactiontypecode = 1) as loadedqty,(select ifnull(CAST(sum(itd.quantity/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum(itd.quantity%im.unitspercase) AS INTEGER),0) from inventorytransactiondetail itd inner join itemmaster im on itd.itemcode=im.actualitemcode WHERE itd.transactiontypecode = 3) as transferinqty,(select ifnull(CAST(sum(itd.quantity/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum(itd.quantity%im.unitspercase) AS INTEGER),0) from inventorytransactiondetail itd inner join itemmaster im on itd.itemcode=im.actualitemcode WHERE itd.transactiontypecode = 2) as transferoutqty,(select ifnull(CAST(sum(id.salesqty/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum(id.salesqty%im.unitspercase) AS INTEGER),0) from invoicedetail id inner join itemmaster im on id.itemcode=im.actualitemcode WHERE id.salesqty>0) as salesqty,(select ifnull(CAST(sum(id.damagedqty/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum(id.damagedqty%im.unitspercase) AS INTEGER),0) from invoicedetail id inner join itemmaster im on id.itemcode=im.actualitemcode WHERE id.salesqty>0) as damageqty,(select ifnull(CAST(sum(itd.quantity/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum(itd.quantity%im.unitspercase) AS INTEGER),0) from inventorytransactiondetail itd inner join itemmaster im on itd.itemcode=im.actualitemcode WHERE itd.transactiontypecode = 6) as unloadqty,(select ifnull(CAST(sum((id.freesampleqty+id.manualfreeqty)/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum((id.freesampleqty+id.manualfreeqty)%im.unitspercase) AS INTEGER),0) from invoicedetail id inner join itemmaster im on id.itemcode=im.actualitemcode WHERE id.salesqty>0) as freeqty,(select ifnull(CAST(sum(itd.quantity/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum(itd.quantity%im.unitspercase) AS INTEGER),0) from inventorytransactiondetail itd inner join itemmaster im on itd.itemcode=im.actualitemcode WHERE itd.transactiontypecode = 14) as freshunloadqty,(select ifnull(CAST(sum(itd.quantity/im.unitspercase) AS INTEGER),0) || '/' || ifnull(CAST(sum(itd.quantity%im.unitspercase) AS INTEGER),0) from inventorytransactiondetail itd inner join itemmaster im on itd.itemcode=im.actualitemcode WHERE itd.transactiontypecode = 11) as truckdamageqty,(select CAST(sum(id.salesqty/im.unitspercase) AS INTEGER) || '/' || CAST(sum(id.salesqty%im.unitspercase) AS INTEGER)  from invoiceheader  ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode  inner join invoicedetail id on ih.[transactionkey]=id.transactionkey inner join itemmaster im on id.itemcode=im.actualitemcode where  id.salesqty>0 and cm.invoicepaymentterms < 2 AND ih.voidflag IS NULL) as cashsalesqty,(select CAST(sum(id.salesqty/im.unitspercase) AS INTEGER) || '/' || CAST(sum(id.salesqty%im.unitspercase) AS INTEGER)  from invoiceheader  ih INNER JOIN customermaster cm ON cm.customercode = ih.customercode  inner join invoicedetail id on ih.[transactionkey]=id.transactionkey inner join itemmaster im on id.itemcode=im.actualitemcode where  id.salesqty>0 and cm.invoicepaymentterms = 2 AND ih.voidflag IS NULL) as creditsalesqty  FROM startendday sed WHERE sed.routekey = (SELECT MAX(routekey) FROM startendday)";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)
                totalvaramount = 0;
        else
        totalvaramount = result[0][0];
        
        data["totalvaramount"] = eval(totalvaramount).toFixed(decimalplace);
            ReportsToPrint(arrRpt,currpoint+1);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.ScannedCustomers,item.NonScannedCustomers,item.NoCallCustomer,item.VoidInvoices,item.VoidCollections,item.StartTime,item.EndTime,item.TotalKmsRun,item.PlannedCalls,item.CallsMadePlanned,item.CallsMadeUnPlanned,item.ActualCallsMade,item.InvoicedCalls,item.ProductiveCalls,item.CoverageCalls,item.opening,item.loded,item.tranferin,item.transferout,item.sales,item.free,item.truckdamage,item.freshunload,item.badreturn,item.badreturnvariance,item.unload,item.unloadvariance,item.totvariance,item.todaysales,item.cashsales,item.creditsales,item.tcsales,item.todayar,item.cashar,item.chkar,item.loadedqty,item.transferinqty,item.transferoutqty,item.salesqty,item.damageqty,item.unloadqty,item.freeqty,item.freshunloadqty,item.truckdamageqty,item.cashsalesqty,item.creditsalesqty]];
                });
                
             
                data["ScannedCustomers"] = result[0][0];
                data["NonScannedCustomers"] = result[0][1];
                data["NoCallCustomer"] = result[0][2];
                data["VoidInvoices"] = result[0][3];
                data["VoidCollections"] = result[0][4];
                data["StartTime"] = result[0][5];
                data["EndTime"] = result[0][6];
            
                if(result[0][6]==undefined ||result[0][6]=='' || result[0][6]==0){
                	 var cDate = new Date();
                	 var tDate;
                     var dtime = cDate.getHours() +':'+cDate.getMinutes()+':'+cDate.getSeconds();
                	data["EndTime"] = tDate;
                }
                
                data["TotalKmsRun"] = result[0][7];
                data["PlannedCalls"] = result[0][8];
                data["CallsMadePlanned"] = result[0][9];
                data["CallsMadeUnPlanned"] = result[0][10];
                data["ActualCallsMade"] = result[0][11];
                data["InvoicedCalls"] = result[0][12];
                data["ProductiveCalls"] = result[0][13];
                data["CoverageCalls"] = result[0][14];
                
                
                data["Opening"] = eval(result[0][15]).toFixed(decimalplace);
                data["Loaded"] = eval(result[0][16]).toFixed(decimalplace);
                data["Transferin"] = eval(result[0][17]).toFixed(decimalplace);
                data["Transferout"] = eval(result[0][18]).toFixed(decimalplace);
                // org line sujee commenetd and splitted in 2 lines
               // data["salesfree"] = eval(eval(result[0][19]) + eval(result[0][20])).toFixed(decimalplace);
                data["salesfree"] = eval(eval(result[0][19])).toFixed(decimalplace);
           
                data["freshunload"] = eval(result[0][22]).toFixed(decimalplace);
                data["truckdamage"] =eval(result[0][21]).toFixed(decimalplace);
                data["badreturn"] = eval(result[0][23]).toFixed(decimalplace);
                data["calculatedunload"] = eval(result[0][24]).toFixed(decimalplace);
                data["unload"] = eval(result[0][25]).toFixed(decimalplace);
                data["unloadvariance"] = eval(result[0][26]).toFixed(decimalplace);
                data["calculatedbadreturn"] = "0.00";
                data["unloadbadreturn"] = "0.00";
                data["returnvarinace"] = "0.00";
                data["Totalinvvarince"] = eval(result[0][27]).toFixed(decimalplace);
                data["todaysales"] = eval(result[0][28]).toFixed(decimalplace);
                data["cashsales"] = eval(result[0][29]).toFixed(decimalplace);
                data["creditsales"] = eval(result[0][30]).toFixed(decimalplace);
                data["tcsales"] = eval(result[0][31]).toFixed(decimalplace);
                data["collection"] = eval(result[0][32]).toFixed(decimalplace);
                data["cash"] = eval(result[0][33]).toFixed(decimalplace);
                data["cheque"] = eval(result[0][34]).toFixed(decimalplace);
                // sujee added for MPF 07/08/2018
                data["loadedqty"] = result[0][35];
                data["transferinqty"] = result[0][36];
                data["transferoutqty"] = result[0][37];
                data["salesqty"] = result[0][38];
                data["damageqty"] = result[0][39];
                data["unloadqty"] = result[0][40];
                data["freeqty"] = result[0][41]; 
                data["freshunloadqty"] = result[0][42]; 
                data["truckdamageqty"] = result[0][43]; 
                data["cashsalesqty"] = result[0][44]; 
                data["creditsalesqty"] = result[0][45];
            
            	}
            	else 
                {
            	  data["ScannedCustomers"] =  "0";;
                  data["NonScannedCustomers"] =  "0";;
                  data["NoCallCustomer"] = "0";;
                  data["VoidInvoices"] =  "0";;
                  data["VoidCollections"] =  "0";;
                  data["StartTime"] =  "0";;
                  data["EndTime"] =  "0";;
                  
	             
	              
	              data["TotalKmsRun"] ="0";
	              data["PlannedCalls"] = "0";
	              data["CallsMadePlanned"] = "0";
	              data["CallsMadeUnPlanned"] = "0";
	              data["ActualCallsMade"] = "0";
	              data["InvoicedCalls"] ="0";
	              data["ProductiveCalls"] = "0";
	              data["CoverageCalls"] ="0";
                //data["VOIDINVOICES"] = "0";
	              data["Opening"] = 0;
	              data["Loaded"] = 0;
	              data["Transferin"] = 0;
	              data["Transferout"] = 0;
	              data["salesfree"] = 0;
	              data["freshunload"] = 0;
	              data["truckdamage"] =0;
	              data["badreturn"] = 0;
	              data["calculatedunload"] = 0;
	              data["unload"] = 0;
	              data["unloadvariance"] = 0;
	              data["calculatedbadreturn"] = "0.00";
	              data["unloadbadreturn"] = "0.00";
	              data["returnvarinace"] = "0.00";
	              data["Totalinvvarince"] = 0;
	              data["todaysales"] =0;
	              data["cashsales"] =0;
	              data["creditsales"] =0;
	              data["tcsales"] =0;
	              data["collection"] =0;
	              data["cash"] =0;
	              data["cheque"] =0;
	              data["loadedqty"] = "0/0";
	              data["transferinqty"] = "0/0";
	              data["transferoutqty"] = "0/0";
	              data["salesqty"] = "0/0";
	              data["damageqty"] = "0/0";
	              data["unloadqty"] = "0/0";
	              data["freeqty"] = "0/0";
	              data["free"] = 0;
	              data["freshunloadqty"] = "0/0";
	              data["truckdamageqty"] = "0/0";
	              data["cashsalesqty"] = "0/0";
	              data["creditsalesqty"] = "0/0";
              
                }
            /*data["expense"] = "200.00";
            data["cashadj"] = "0.00";
            data["chkadj"] = "0.00";
            data["calculatedcashdue"] = "3190.00";
            data["cashvariance"] = "0.00";
            data["netcashdue"] = "3190.00";*/            
           ReportsToPrint(arrRpt,currpoint+1);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}




//--------End Route Summary     
//--------------Ending inv & Vanstock
function PrintEndInventoryReport()
{

    data = {};
    //GetSalesData('sales');
    getEndInventoryInfo();
   // alert("sel");
}
function getEndInventoryInfo()
{
   // alert("E1");
    var Qry = "SELECT documentnumber,printstatus FROM inventorytransactionheader WHERE detailkey = (SELECT MAX(invth.detailkey) FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey AND invtd.transactiontypecode IN(5,6) WHERE invth.istemp = 'false')";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {   
            if(result.length > 0)
            {
                documentnumber = parseInt(eval(result[0][0]));
                printstatus = result[0][1];
            }         
            data["printstatus"] = getPrintStatus();
            data["DOCUMENT NO"] = documentnumber;   
            PrintEndInventoryValue();
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        //alert("E2");
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
               // alert("E3");
                documentnumber = parseInt(eval(result.array[0].documentnumber));
                printstatus = result.array[0].printstatus;
            }    
            data["printstatus"] = getPrintStatus();
            data["DOCUMENT NO"] = documentnumber;   
            PrintEndInventoryValue();        
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
//----------------------Start
//----------Start
function PrintEndInventoryValue()
{  
    var autoload = sessionStorage.getItem("autoload");
    if(autoload == 3 || autoload == 4)
    var field ="endstockqty";
    else
    var field ="unloadqty";
    var Qry = "SELECT itemcode,CAST(((CAST((IFNULL((vanqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((vanqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as Available,CAST(((CAST((IFNULL((truckdamageqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((truckdamageqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as Unload,CAST(((CAST((IFNULL(("+field+")/unitspercase, 0)) AS INT) * caseprice) + (IFNULL(("+field+") % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as closevalue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription AS description,unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,IFNULL(unloadqty,0) as unloadqty,IFNULL(endstockqty,0) as endstockqty,caseprice,defaultsalesprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6)) order by itemcode";
      // alert(Qry);
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {
            if(result.length <= 0)
                result = [];
            
            PrintEndInventory();
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        var closevalue= 0;
        var AvailableVal=0;
        var UnloadVal=0;
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
            	result = $.map(result.array, function(item, index) {
                    return [[item.itemcode,item.Available,item.Unload,item.closevalue]];    
                });
                for (i = 0; i < result.length; i++) {
                    for (j = 0; j < result[i].length; j++) {    
                    	 if (j == 1)
                         {
                         
                         result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                         AvailableVal = (eval(AvailableVal) + eval(result[i][j])).toFixed(decimalplace);
                         }  
                    	 if (j == 2)
                         {
                         
                         result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                         UnloadVal = (eval(UnloadVal) + eval(result[i][j])).toFixed(decimalplace);
                         }  
                    	 if (j == 3)
                         {
                    		 	result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
                                closevalue = (eval(closevalue) + eval(result[i][j])).toFixed(decimalplace);
                         }           
                             
                    }
                }
                data["availvalue"] = AvailableVal.toString();
                data["unloadvalue"] = UnloadVal.toString();
                data["closevalue"] = closevalue.toString();
                
            }
            else
                result = [];           
            PrintEndInventory();
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
//-----------End
//----------------------End

function PrintEndInventory()
{
    
    //getroutedate();
    var autoload = sessionStorage.getItem("autoload");
    //alert(autoload);
            if(autoload == 3 || autoload == 4)
            var field ="endstockqty";
            else
            var field ="unloadqty";
           // alert(field);
               // var Qry = "SELECT 0 as sl,itemcode,description,unitspercase,(CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) AS truckstockqty,(CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) AS freshunloadqty,(CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) AS truckdamageqty,(CAST(("+field+")/unitspercase AS INT) || '/' || CAST(("+field+")%unitspercase AS INT)) as closingstock,(CAST((vanqty-freshunloadqty-truckdamageqty-"+field+")/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-"+field+")%unitspercase AS INT)) AS varianceqty,CAST(((CAST((IFNULL((endstockqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((endstockqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as EndValue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription AS description,unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,IFNULL(unloadqty,0) as unloadqty,IFNULL(endstockqty,0) as endstockqty,caseprice,defaultsalesprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6)) order by itemcode";
                  //var Qry = "SELECT itemcode,description,(CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) AS truckstockqty,(CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) AS freshunloadqty,(CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) AS truckdamageqty,(CAST((vanqty-freshunloadqty-truckdamageqty)/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty)%unitspercase AS INT)) as closingstock,(CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))/unitspercase AS INT) || '/' || CAST((vanqty-(freshunloadqty+truckdamageqty+(vanqty-freshunloadqty-truckdamageqty)))%unitspercase AS INT)) AS varianceqty FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription AS description,unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6)) order by itemcode";
            	//var Qry="SELECT 0 as sl,itemcode,description,unitspercase,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS truckstockqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(freshunloadqty AS INT)) ELSE (CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) END AS freshunloadqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(truckdamageqty AS INT)) ELSE (CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) END AS truckdamageqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(("+field+") AS INT)) ELSE (CAST(("+field+")/unitspercase AS INT) || '/' || CAST(("+field+")%unitspercase AS INT)) END as closingstock,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-"+field+") AS INT)) ELSE (CAST((vanqty-freshunloadqty-truckdamageqty-"+field+")/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-"+field+")%unitspercase AS INT)) END AS varianceqty,CAST(((CAST((IFNULL(("+field+")/unitspercase, 0)) AS INT) * caseprice) + (IFNULL(("+field+") % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as EndValue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemshortdescription END AS 'description',unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,IFNULL(unloadqty,0) as unloadqty,IFNULL(endstockqty,0) as endstockqty,caseprice,defaultsalesprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6) and IFNULL(unloadqty,0) + IFNULL(endstockqty,0) > 0) order by itemcode";
            
        // org qry sujee commented 04/12/2019 
           // var Qry="SELECT 0 as sl,itemcode,description,arbdescription,unitspercase,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS truckstockqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(freshunloadqty AS INT)) ELSE (CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) END AS freshunloadqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(truckdamageqty AS INT)) ELSE (CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) END AS truckdamageqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(("+field+") AS INT)) ELSE (CAST(("+field+")/unitspercase AS INT) || '/' || CAST(("+field+")%unitspercase AS INT)) END as closingstock,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-"+field+") AS INT)) ELSE (CAST((vanqty-freshunloadqty-truckdamageqty-"+field+")/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-"+field+")%unitspercase AS INT)) END AS varianceqty,CAST(((CAST((IFNULL(("+field+")/unitspercase, 0)) AS INT) * caseprice) + (IFNULL(("+field+") % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as EndValue FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,im.itemdescription AS 'description',im.arbitemshortdescription AS 'arbdescription',unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(truckdamagedunloadqty,0) as truckdamageqty,IFNULL(unloadqty,0) as unloadqty,IFNULL(endstockqty,0) as endstockqty,caseprice,defaultsalesprice FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6) and IFNULL(unloadqty,0) + IFNULL(endstockqty,0) > 0) order by itemcode";
            var Qry="SELECT 0 as sl,itemcode,description,unitspercase, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS invcalculate,'0/0' as rettostk, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(truckdamageqty AS INT)) ELSE (CAST(truckdamageqty/unitspercase AS INT) || '/' || CAST(truckdamageqty%unitspercase AS INT)) END AS truckdamageqty, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanqty AS INT)) ELSE (CAST(vanqty/unitspercase AS INT) || '/' || CAST(vanqty%unitspercase AS INT)) END AS truckstockqty, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(freshunloadqty AS INT)) ELSE (CAST(freshunloadqty/unitspercase AS INT) || '/' || CAST(freshunloadqty%unitspercase AS INT)) END AS nonsales, CASE WHEN unitspercase=1 THEN (0 || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty) AS INT)) ELSE (CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty)/unitspercase AS INT) || '/' || CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty)%unitspercase AS INT)) END AS varianceqty, printf('%.3f',((CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty)/unitspercase AS INT) * caseprice) +  (CAST((vanqty-freshunloadqty-truckdamageqty-endstockqty)%unitspercase AS INT) * defaultsalesprice)) ) AS varianceval,CAST(((CAST((IFNULL((endstockqty)/unitspercase, 0)) AS INT) * caseprice) + (IFNULL((endstockqty) % unitspercase, 0) * defaultsalesprice)) AS FLOAT )as EndValue  FROM (SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,im.itemdescription AS 'description',unitspercase,((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,'0/0' as rettostk,IFNULL(truckdamagedunloadqty,0) as truckdamageqty, ((ifnull(loadqty,0)+ifnull(loadadjustqty,0)+ifnull(beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0))) AS vanqty,IFNULL(freshunloadqty,0) as freshunloadqty,IFNULL(endstockqty,0) as endstockqty,caseprice,defaultsalesprice  FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invtd.transactiontypecode IN(5,6) and IFNULL(unloadqty,0) + IFNULL(endstockqty,0) > 0) order by itemcode ";
            	
            	
                console.log(Qry);
                //alert(Qry);
                if (platform == 'iPad') {
                    Cordova.exec(function(result) {
                        if(result.length <= 0)                        
                            result = [];                        
                       ProcessEndInventory(result);
                    },
                    function(error) {
                        alert(error);
                    },
                    "PluginClass",
                    "GetdataMethod",
                    [Qry]);
                }
                else if (platform == 'Android') {
                   
                    window.plugins.DataBaseHelper.select(Qry, function(result) {
                        if(result.array != undefined)
                        {
                           // alert("E6");
                            result = $.map(result.array, function(item, index) {
                                //alert(item.closingstock);
                                //return [[item.itemcode,item.description,item.unitspercase, item.truckstockqty, item.freshunloadqty,item.closingstock,item.varianceqty,item.arbdescription]];    
                            	 return [[item.itemcode,item.description,item.unitspercase, item.invcalculate, item.rettostk,item.truckdamageqty,item.truckstockqty,item.nonsales,item.varianceqty,item.varianceval,item.EndValue]];
                            });
                        }
                        else
                            result = [];
                        
                        
                        ProcessEndInventory(result);
                    },
                    function() {
                        console.warn("Error calling plugin");
                    });
                }
}

function ProcessEndInventory(result)
{   
   
  //  data["HEADERS"] = ["Item#","Description","UPC","Truck Stock","Fresh Unload","Closing Stock","Variance Qty"];
	data["HEADERS"] = ["Item#","Description","UPC","Inventory Calculated","Return Stock","Truck Spoil","Actual on Truck","Non Sales","Variance Qty","Variance Value","Total Value"];
   // var total = {"Truck Stock" : "0/0","Fresh Unload" : "0/0", "Closing Stock" : "0/0","Variance Qty" : "0/0"}; 
	 var total = {"Inventory Calculated" : "0/0","Return Stock" : "0/0", "Truck Spoil" : "0/0","Actual on Truck" : "0/0","Non Sales" : "0/0","Variance Qty" : "0/0","Variance Value" : "0.000"}; 
    var totalValue=0;
    for(i=0;i<result.length;i++)
    {        
        
        for(j=0;j<result[i].length;j++)
        {
            if(j == 0)
            {
                result[i][j] = (result[i][j]);
                result[i][j] = (result[i][j]).toString();
            }
            if(j >=3 && j<=8)
            {
                var qtykey = "";
                if(j == 3)
                    qtykey = "Inventory Calculated";
                if(j == 4)
                    qtykey = "Return Stock";
               
                if(j == 5)
                    qtykey = "Truck Spoil";
                if(j == 6)
                    qtykey = "Actual on Truck";
                if(j == 7)
                    qtykey = "Non Sales";
                if(j == 8)
                    qtykey = "Variance Qty";
                if(j == 9)
                    qtykey = "Variance Value";

                QuantityTotal(total,qtykey,result[i][j]);
            }
            if(j==10){
            	 result[i][j] = eval((result[i][j]).toString()).toFixed(decimalplace);
            	 totalValue = (eval(totalValue) + eval(result[i][j])).toFixed(decimalplace);
            }
        }
    }
    totalValue = eval(parseFloat(totalValue)).toFixed(decimalplace);
 
    total["Total Value"] = totalValue.toString();
    getCommonData();
            
    data["data"] = result;                        
    data["TOTAL"] = [total];                                              
    console.log(JSON.stringify(data));
   // alert("E9");
    SetArray(ReportName.EndInventoryReport);
    ReportsToPrint(arrRpt,currpoint+1);
   // PrintReport(ReportName.EndInventory,data);
}
//-------------Vanstock----Start
function PrintVanStockReport()
{
   // alert("test");

    data = {};
    //GetSalesData('sales');
    getVanReportInfo();
   // alert("sel");
}
function getVanReportInfo()
{
   // alert("test");
    var Qry = "SELECT documentnumber,printstatus FROM inventorytransactionheader WHERE detailkey = (SELECT MAX(detailkey) FROM inventorytransactionheader WHERE transactiontype = 1 AND istemp = 'false')";
    console.log(Qry);
    if (platform == 'iPad') 
    {
        Cordova.exec(function(result) {   
            if(result.length > 0)
            {
                documentnumber = parseInt(eval(result[0][0]));
                printstatus = result[0][1];
            }
            PrintReportVanStock();
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
               // alert("v1");
                documentnumber = parseInt(result.array[0].documentnumber);
                printstatus = result.array[0].printstatus;
            }    
            PrintReportVanStock();        
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}


function PrintReportVanStock()
{
    //alert("v2");
    //getroutedate();
    var routekey = sessionStorage.getItem("RouteKey");
    //  alert(sessionStorage.getItem("RouteKey"));
    //var availqty="ifnull(inventorysummarydetail.loadqty,0)+ifnull(inventorysummarydetail.loadadjustqty,0)+ifnull(inventorysummarydetail.beginstockqty,0)+ifnull(loadaddqty,0)-ifnull(loadcutqty,0)-ifnull(saleqty,0)+ifnull(returnqty,0)+ifnull(returnfreeqty,0)-ifnull(freesampleqty,0)";
   // Qry = "SELECT DISTINCT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode',itemdescription AS 'description',(IFNULL(CAST((loadqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((loadqty%unitspercase) AS INT),0)) AS 'loadqty',(IFNULL(CAST((loadadjustqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((loadadjustqty%unitspercase) AS INT),0)) AS 'soldqty', (CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))/unitspercase) AS INT) ||  '/' || CAST(((IFNULL(beginstockqty,0)+IFNULL(loadqty,0)+IFNULL(loadadjustqty,0))%unitspercase) AS INT)) AS 'avlqty' FROM inventorytransactionheader invth JOIN inventorytransactiondetail invtd ON invtd.detailkey = invth.detailkey JOIN inventorysummarydetail invsum ON invsum.itemcode = invtd.itemcode JOIN itemmaster im ON im.actualitemcode = invsum.itemcode WHERE invth.loadnumber=" + loadperiodnumber+" or invth.loadnumber=0" ;
    //Qry="select CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN itemmaster.alternatecode ELSE itemmaster.actualitemcode END as itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN itemdescription ELSE arbitemdescription END AS 'description',(IFNULL(CAST((("+availqty+")/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((("+availqty+")%unitspercase) AS INT),0)) AS 'avlqty',caseprice,defaultsalesprice,unitspercase as upc,itemmaster.actualitemcode from itemmaster left join inventorysummarydetail on itemmaster.actualitemcode=inventorysummarydetail.itemcode where routekey="+routekey+" and ("+availqty+") > 0 order by CASE printsequenceroute WHEN 0 THEN 100000 ELSE printsequenceroute END";
    //var Qry ="SELECT itemcode,description,(CAST(loadedqty/unitspercase AS INT) || '/' || CAST(loadedqty%unitspercase AS INT)) AS loadedqty,(CAST(transferqty/unitspercase AS INT) || '/' || CAST(transferqty%unitspercase AS INT)) AS transferqty,(CAST(invoicedqty/unitspercase AS INT) || '/' || CAST(invoicedqty%unitspercase AS INT)) AS saleqty ,(CAST(returnqty/unitspercase AS INT) || '/' || CAST(returnqty%unitspercase AS INT)) as returnqty,(CAST(vanstock /unitspercase AS INT) || '/' || CAST(vanstock %unitspercase AS INT)) AS vanstock FROM ( SELECT CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,itemdescription AS description,unitspercase,IFNULL((IFNULL(isd.beginstockqty,0)+IFNULL(isd.loadqty,0)),0) AS loadedqty,IFNULL((IFNULL(isd.loadaddqty,0) - IFNULL(isd.loadcutqty,0)),0) AS transferqty,IFNULL((IFNULL(isd.saleqty,0) + IFNULL(isd.freesampleqty,0)),0) AS invoicedqty,IFNULL((IFNULL(isd.returnqty,0) + IFNULL(isd.returnfreeqty,0)),0) AS returnqty,IFNULL((IFNULL(isd.beginstockqty,0) + IFNULL(isd.loadqty,0) + IFNULL(isd.loadaddqty,0) + IFNULL(isd.returnqty,0) + IFNULL(isd.returnfreeqty,0)),0) -IFNULL((IFNULL(isd.loadcutqty,0) + IFNULL(isd.saleqty,0) + IFNULL(isd.freesampleqty,0)),0) AS vanstock FROM inventorysummarydetail isd INNER JOIN itemmaster im ON im.actualitemcode = isd.itemcode where routekey = "+sessionStorage.getItem("RouteKey")+") order by itemcode";
    //var Qry="SELECT itemcode,description,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(loadedqty AS INT)) ELSE (CAST(loadedqty/unitspercase AS INT) || '/' || CAST(loadedqty%unitspercase AS INT)) END AS loadedqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(transferqty AS INT)) ELSE (CAST(transferqty/unitspercase AS INT) || '/' || CAST(transferqty%unitspercase AS INT)) END AS transferqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(invoicedqty AS INT)) ELSE (CAST(invoicedqty/unitspercase AS INT) || '/' || CAST(invoicedqty%unitspercase AS INT)) END AS saleqty ,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(returnqty AS INT)) ELSE (CAST(returnqty/unitspercase AS INT) || '/' || CAST(returnqty%unitspercase AS INT)) END as returnqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanstock AS INT)) ELSE (CAST(vanstock /unitspercase AS INT) || '/' || CAST(vanstock %unitspercase AS INT)) END AS vanstock,((CAST(vanstock /unitspercase AS INT)*caseprice)+  (CAST(vanstock %unitspercase AS INT)*defaultsalesprice)) AS total FROM ( SELECT CASE WHEN '"+ sessionStorage.getItem("CheckCode")+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemshortdescription END AS 'description',unitspercase,IFNULL((IFNULL(isd.beginstockqty,0)+IFNULL(isd.loadqty,0)),0) AS loadedqty,IFNULL((IFNULL(isd.loadaddqty,0) - IFNULL(isd.loadcutqty,0)),0) AS transferqty,IFNULL((IFNULL(isd.saleqty,0) + IFNULL(isd.freesampleqty,0)),0) AS invoicedqty,IFNULL((IFNULL(isd.returnqty,0) + IFNULL(isd.returnfreeqty,0)),0) AS returnqty,IFNULL((IFNULL(isd.beginstockqty,0) + IFNULL(isd.loadqty,0) + IFNULL(isd.loadaddqty,0) + IFNULL(isd.returnqty,0) + IFNULL(isd.returnfreeqty,0)),0) -IFNULL((IFNULL(isd.loadcutqty,0) + IFNULL(isd.saleqty,0) + IFNULL(isd.freesampleqty,0)),0) AS vanstock,im.caseprice as caseprice,im.defaultsalesprice as defaultsalesprice FROM inventorysummarydetail isd INNER JOIN itemmaster im ON im.actualitemcode = isd.itemcode where routekey ="+ sessionStorage.getItem("RouteKey") + " and vanstock>0) order by itemcode";
    // sujee commented 03/02/2020
    //var Qry="SELECT itemcode,description,arbitemdescription,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(loadedqty AS INT)) ELSE (CAST(loadedqty/unitspercase AS INT) || '/' || CAST(loadedqty%unitspercase AS INT)) END AS loadedqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(transferqty AS INT)) ELSE (CAST(transferqty/unitspercase AS INT) || '/' || CAST(transferqty%unitspercase AS INT)) END AS transferqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(invoicedqty AS INT)) ELSE (CAST(invoicedqty/unitspercase AS INT) || '/' || CAST(invoicedqty%unitspercase AS INT)) END AS saleqty ,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(returnqty AS INT)) ELSE (CAST(returnqty/unitspercase AS INT) || '/' || CAST(returnqty%unitspercase AS INT)) END as returnqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanstock AS INT)) ELSE (CAST(vanstock /unitspercase AS INT) || '/' || CAST(vanstock %unitspercase AS INT)) END AS vanstock,((CAST(vanstock /unitspercase AS INT)*caseprice)+  (CAST(vanstock %unitspercase AS INT)*defaultsalesprice)) AS total FROM ( SELECT CASE WHEN '"+ sessionStorage.getItem("CheckCode")+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,im.itemdescription  AS 'description',im.arbitemshortdescription  AS 'arbitemdescription',unitspercase,IFNULL((IFNULL(isd.beginstockqty,0)+IFNULL(isd.loadqty,0)),0) AS loadedqty,IFNULL((IFNULL(isd.loadaddqty,0) - IFNULL(isd.loadcutqty,0)),0) AS transferqty,IFNULL((IFNULL(isd.saleqty,0) + IFNULL(isd.freesampleqty,0)),0) AS invoicedqty,IFNULL((IFNULL(isd.returnqty,0) + IFNULL(isd.returnfreeqty,0)),0) AS returnqty,IFNULL((IFNULL(isd.beginstockqty,0) + IFNULL(isd.loadqty,0) + IFNULL(isd.loadaddqty,0) + IFNULL(isd.returnqty,0) + IFNULL(isd.returnfreeqty,0)),0) -IFNULL((IFNULL(isd.loadcutqty,0) + IFNULL(isd.saleqty,0) + IFNULL(isd.freesampleqty,0)),0) AS vanstock,im.caseprice as caseprice,im.defaultsalesprice as defaultsalesprice FROM inventorysummarydetail isd INNER JOIN itemmaster im ON im.actualitemcode = isd.itemcode where routekey ="+ sessionStorage.getItem("RouteKey") + " and vanstock>0) order by itemcode";
    
    var Qry="SELECT itemcode,description,arbitemdescription,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(loadedqty AS INT)) ELSE (CAST(loadedqty/unitspercase AS INT) || '/' || CAST(loadedqty%unitspercase AS INT)) END AS loadedqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(transferqty AS INT)) ELSE (CAST(transferqty/unitspercase AS INT) || '/' || CAST(transferqty%unitspercase AS INT)) END AS transferqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(invoicedqty AS INT)) ELSE (CAST(invoicedqty/unitspercase AS INT) || '/' || CAST(invoicedqty%unitspercase AS INT)) END AS saleqty ,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(returnqty AS INT)) ELSE (CAST(returnqty/unitspercase AS INT) || '/' || CAST(returnqty%unitspercase AS INT)) END as returnqty,CASE WHEN unitspercase=1 THEN (0 || '/' || CAST(vanstock AS INT)) ELSE (CAST(vanstock /unitspercase AS INT) || '/' || CAST(vanstock %unitspercase AS INT)) END AS vanstock,((CAST(vanstock /unitspercase AS INT)*caseprice)+  (CAST(vanstock %unitspercase AS INT)*defaultsalesprice)) AS total FROM ( SELECT CASE WHEN '"+ sessionStorage.getItem("CheckCode")+ "' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS itemcode,im.itemdescription  AS 'description',im.arbitemshortdescription  AS 'arbitemdescription',unitspercase,IFNULL((IFNULL(isd.beginstockqty,0)+IFNULL(isd.loadqty,0)),0) AS loadedqty,IFNULL((IFNULL(isd.loadaddqty,0) - IFNULL(isd.loadcutqty,0)),0) AS transferqty,IFNULL((IFNULL(isd.saleqty,0) + IFNULL(isd.freesampleqty,0)),0) AS invoicedqty,IFNULL((IFNULL(isd.returnqty,0) + IFNULL(isd.returnfreeqty,0)),0) AS returnqty,IFNULL((IFNULL(isd.beginstockqty,0) + IFNULL(isd.loadqty,0) + IFNULL(isd.loadaddqty,0) + IFNULL(isd.returnqty,0) + IFNULL(isd.returnfreeqty,0)),0) -IFNULL((IFNULL(isd.loadcutqty,0) + IFNULL(isd.saleqty,0) + IFNULL(isd.freesampleqty,0)),0) AS vanstock,im.caseprice as caseprice,im.defaultsalesprice as defaultsalesprice FROM inventorysummarydetail isd INNER JOIN itemmaster im ON im.actualitemcode = isd.itemcode where routekey ="+ sessionStorage.getItem("RouteKey") + " ) order by itemcode";
	// alert(Qry);
    console.log(Qry);
    if (platform == "iPad") {
        Cordova.exec(function(result) {
            if (result.length < 0)
                result = []
            ProcessReportVanStock(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == "Android") {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if (result.array != undefined) {  
                //alert("v2");
                result = $.map(result.array, function(item, index) {
                   // return [[item.itemcode,item.description,item.upc,item.avlqty,item.caseprice,item.defaultsalesprice]];
                    return [[item.itemcode,item.description,item.loadedqty,item.transferqty,item.saleqty,item.returnqty,item.vanstock,item.total,item.arbitemdescription]];
                });
            }
            else
                result = [];
            ProcessReportVanStock(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function ProcessReportVanStock(result) {
   // alert("v3");
   // alert(result);
    /*var Headers = ["Item#", "Description", "UPC", "Available Qty", "Case Price", "Unit Price"]
    var Total = {"Available Qty": "0/0"}*/
    var Headers = ["Item#","Description","Loaded Qty","Transfer Qty","Sale Qty","Return Qty","Truck Stock","Total"];
    var Total = {"Loaded Qty" : "0/0","Transfer Qty" : "0/0", "Sale Qty" : "0/0", "Return Qty" : "0/0","Truck Stock" : "0/0","Total":"0"};
    for (i = 0; i < result.length; i++) {
       // alert("v5");
        for (j = 0; j < result[i].length; j++) {
            
            if (j == 0) {
            	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                    result[i][j] = parseInt(result[i][j]);
            	
                result[i][j] = (result[i][j]).toString();
            }
            if(j >= 2 && j<=7)
            {
            var qtykey = "";
            if(j == 2)
                qtykey = "Loaded Qty";
            if(j == 3)
                qtykey = "Transfer Qty";
            if(j == 4)
                qtykey = "Sale Qty";
            if(j == 5)
                qtykey = "Return Qty";
            if(j == 6)
                qtykey = "Truck Stock";
            
            	
            if(j == 7){
				
				result[i][j] = eval(result[i][j]).toFixed(decimalplace);
				Total.Total = (eval(Total.Total) + eval(result[i][j])).toFixed(decimalplace);
			}
				
				if(j!=7)
				QuantityTotal(Total, qtykey, result[i][j]);
            }
            
        }
    }
    //data["Load Number"] = loadperiodnumber.toString();
    data["DOCUMENT NO"] = documentnumber;
    data["HEADERS"] = Headers;
    data["TOTAL"] = [Total];
    data["printstatus"] = getPrintStatus();
    data["data"] = result;
    getCommonData();
    console.log(JSON.stringify(data));
    //alert("v4");
    SetArray(ReportName.VanStockReport);
    ReportsToPrint(arrRpt,currpoint+1);
    
   // PrintReport(ReportName.VanStock,data);   
}
//-------------Vanstock-----End
//--------------End
function getroutedate()
{
     var qry = "select strftime('%d/%m/%Y',routestartdate) as routestartdate from startendday where routekey="+sessionStorage.getItem("RouteKey");
    if (platform == 'iPad') {
        Cordova.exec(function(result) 
        {
            var startdate = result[0][0];
         data["TRIP START DATE"] = startdate;
            
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.routestartdate]];
                });
		        var startdate = result[0][0];
		        data["TRIP START DATE"] = startdate;
            }
           
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
/*function PrintCreditSummaryReport()
{
    data = {};
    getCreditSummaryReport();
}

function getCreditSummaryReport()
{

    var query="SELECT cm.customercode Code, cm.customername Name,COALESCE((SELECT SUM(ob.invoicebalance) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) - COALESCE((SELECT SUM(ih.invoicebalance) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) - COALESCE((SELECT SUM(ah.amountpaid) FROM arheader ah WHERE ah.customercode = cm.customercode AND COALESCE(ah.voidflag, 0) = 0), 0) OpeningBalance,COALESCE((SELECT SUM(ih.invoicebalance) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) Sales,COALESCE((SELECT SUM(ah.amountpaid) FROM arheader ah WHERE ah.customercode = cm.customercode AND COALESCE(ah.voidflag, 0) = 0), 0) Collections,COALESCE((SELECT SUM(ob.invoicebalance) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) CurrentBalance  FROM customermaster cm WHERE cm.invoicepaymentterms = 2";
    //alert("in get Credit Summary Report");
    if (platform == 'iPad') 
    {
        //need to be developed in ios 
    
    }else if(platform=='Android')
    {
         window.plugins.DataBaseHelper.select(query, function(result) {
            if(result.array != undefined)
            {
              // alert("Array"+JSON.stringify(result.array));
                result = $.map(result.array, function(item, index) {
                    return [[item.Code,item.Name,item.OpeningBalance,item.Sales,item.Collections,item.CurrentBalance]];
                    
                });
            }else
            {
                result=[];
            }  
           processCreditSummaryReport(result); 
        },
        function() {
            console.warn("Error calling plugin");
        });
        
     
    }
}

function processCreditSummaryReport(result)
{
    
    var Headers = ["Customer#","Customer Name","Opening Balance","Sales Amount","Collection Amount","Current Balance"];
    var Total = {"Opening Balance" : "0.0","Sales Amount" : "0.0", "Collection Amount" : "0.0", "Current Balance" : "0.0"};
    var begin_bal = 0;
    var SalesAmount = 0;
    var CollectionAmount = 0;
    var CurrentAmount = 0;
    
    
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {                                    
            if(j > 1)                                    
                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
            if(j == 2)
                begin_bal = (eval(begin_bal) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 3)
                SalesAmount = (eval(SalesAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 4)
                CollectionAmount = (eval(CollectionAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 5)
                CurrentAmount = (eval(CurrentAmount) + eval(result[i][j])).toFixed(decimalplace);
        }
    }
    if(isNaN(begin_bal))
        begin_bal = 0;
    if(isNaN(SalesAmount))
        SalesAmount = 0;
    if(isNaN(CollectionAmount))
        CollectionAmount = 0;
    if(isNaN(CurrentAmount))
        CurrentAmount = 0;
    begin_bal = eval(parseFloat(begin_bal)).toFixed(decimalplace);
    SalesAmount = eval(parseFloat(SalesAmount)).toFixed(decimalplace);
    CollectionAmount = eval(parseFloat(CollectionAmount)).toFixed(decimalplace);
    CurrentAmount = eval(parseFloat(CurrentAmount)).toFixed(decimalplace);
    Total["Opening Balance"] = begin_bal.toString();
    Total["Sales Amount"] = SalesAmount.toString();
    Total["Collection Amount"] = CollectionAmount.toString();
    Total["Current Balance"] = CurrentAmount.toString();
    
    data["HEADERS"] = Headers;
    data["TOTAL"] = [Total];
    data["data"] = result;
    getCommonData();
    console.log(JSON.stringify(data));
    //alert("v4");
    SetArray(ReportName.CreditSummary);
    ReportsToPrint(arrRpt,currpoint+1);

}*/
function PrintCreditSummaryReport(type)
{
    data = {};
    getCreditSummaryReport(type);
}

function getCreditSummaryReport(type)
{
 var query;
 if(type=='1')
 {
	query="SELECT cm.customercode Code, cm.customername Name, COALESCE((SELECT SUM(ob.invoicebalance) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) - COALESCE((SELECT SUM(ih.invoicebalance) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) - COALESCE((SELECT SUM(ah.amountpaid) FROM arheader ah WHERE ah.customercode = cm.customercode AND COALESCE(ah.voidflag, 0) = 0), 0) OpeningBalance, COALESCE((SELECT SUM(ih.invoicebalance) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) Sales, COALESCE((SELECT SUM(ah.amountpaid) FROM arheader ah WHERE ah.customercode = cm.customercode AND COALESCE(ah.voidflag, 0) = 0), 0) Collections, COALESCE((SELECT SUM(ob.invoicebalance) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) CurrentBalance FROM customermaster cm WHERE cm.invoicepaymentterms = 2 AND cm.customercode IN (SELECT DISTINCT customercode FROM routesequencecustomerstatus)"; 
  //query="SELECT cm.customercode Code, cm.customername Name, CAST(COALESCE((SELECT SUM(ob.invoicebalance+ob.amountpaid) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) - COALESCE((SELECT SUM(ih.invoicebalance) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) AS VARCHAR) OpeningBalance, CAST(COALESCE((SELECT SUM(ih.invoicebalance) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) AS VARCHAR) Sales, CAST(COALESCE((SELECT SUM(ah.amountpaid) FROM arheader ah WHERE ah.customercode = cm.customercode AND COALESCE(ah.voidflag, 0) = 0), 0) AS VARCHAR) Collections, CAST(COALESCE((SELECT SUM(ob.invoicebalance-coalesce(ob.pdcbalance,0)) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) AS VARCHAR) CurrentBalance FROM customermaster cm WHERE cm.invoicepaymentterms = 2";
  
 }else{
	 
  //query="SELECT cm.customercode Code, cm.customername Name,COALESCE((SELECT SUM(ob.invoicebalance) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) - COALESCE((SELECT SUM(ih.invoicebalance) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) - COALESCE((SELECT SUM(ah.amountpaid) FROM arheader ah WHERE ah.customercode = cm.customercode AND COALESCE(ah.voidflag, 0) = 0), 0) OpeningBalance,COALESCE((SELECT SUM(ih.invoicebalance) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) Sales,COALESCE((SELECT SUM(ah.amountpaid) FROM arheader ah WHERE ah.customercode = cm.customercode AND COALESCE(ah.voidflag, 0) = 0), 0) Collections,COALESCE((SELECT SUM(ob.invoicebalance) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) CurrentBalance  FROM customermaster cm WHERE cm.invoicepaymentterms > 2";
  query="SELECT cm.customercode Code, cm.customername Name, COALESCE((SELECT SUM(ob.invoicebalance) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0 AND ob.transactiondate < (SELECT sed.routestartdate FROM startendday sed ORDER BY sed.routekey DESC LIMIT 1)), 0) + COALESCE((SELECT SUM(ard.amountpaid) FROM ardetail ard INNER JOIN arheader arh WHERE ard.transactionkey = arh.transactionkey AND arh.customercode = cm.customercode AND COALESCE(arh.voidflag, 0) = 0 AND ard.invoicedate < (SELECT sed.routestartdate FROM startendday sed ORDER BY sed.routekey DESC LIMIT 1)), 0) OpeningBalance, COALESCE((SELECT SUM(ih.totalinvoiceamount) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) Sales, COALESCE((SELECT SUM(ah.amountpaid) FROM arheader ah WHERE ah.customercode = cm.customercode AND COALESCE(ah.voidflag, 0) = 0), 0) + COALESCE((SELECT SUM(ih.immediatepaid) FROM invoiceheader ih WHERE ih.customercode = cm.customercode AND COALESCE(ih.voidflag, 0) = 0), 0) Collections, COALESCE((SELECT SUM(ob.invoicebalance) FROM customerinvoice ob WHERE ob.customercode = cm.customercode AND COALESCE(ob.voidflag, 0) = 0), 0) CurrentBalance FROM customermaster cm WHERE cm.invoicepaymentterms >2 GROUP BY cm.customercode, cm.customername HAVING OpeningBalance + CurrentBalance != 0";

 } 
  
    //alert("in get Credit Summary Report");
    if (platform == 'iPad') 
    {
        //need to be developed in ios 
    
    }else if(platform=='Android')
    {
         window.plugins.DataBaseHelper.select(query, function(result) {
            if(result.array != undefined)
            {
              // alert("Array"+JSON.stringify(result.array));
                result = $.map(result.array, function(item, index) {
                    return [[item.Code,item.Name,item.OpeningBalance,item.Sales,item.Collections,item.CurrentBalance]];
                    
                });
            }else
            {
                result=[];
            }  
           processCreditSummaryReport(result,type); 
        },
        function() {
            console.warn("Error calling plugin");
        });
        
     
    }
}

function processCreditSummaryReport(result,type)
{
    
    var Headers = ["Customer#","Customer Name","Opening Balance","Sales Amount","Collection Amount","Current Balance"];
    var Total = {"Opening Balance" : "0.0","Sales Amount" : "0.0", "Collection Amount" : "0.0", "Current Balance" : "0.0"};
    var begin_bal = 0;
    var SalesAmount = 0;
    var CollectionAmount = 0;
    var CurrentAmount = 0;
    
    
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {                                    
            if(j > 1)                                    
                result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
            if(j == 2)
                begin_bal = (eval(begin_bal) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 3)
                SalesAmount = (eval(SalesAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 4)
                CollectionAmount = (eval(CollectionAmount) + eval(result[i][j])).toFixed(decimalplace);
            if(j == 5)
                CurrentAmount = (eval(CurrentAmount) + eval(result[i][j])).toFixed(decimalplace);
        }
    }
    if(isNaN(begin_bal))
        begin_bal = 0;
    if(isNaN(SalesAmount))
        SalesAmount = 0;
    if(isNaN(CollectionAmount))
        CollectionAmount = 0;
    if(isNaN(CurrentAmount))
        CurrentAmount = 0;
    begin_bal = eval(parseFloat(begin_bal)).toFixed(decimalplace);
    SalesAmount = eval(parseFloat(SalesAmount)).toFixed(decimalplace);
    CollectionAmount = eval(parseFloat(CollectionAmount)).toFixed(decimalplace);
    CurrentAmount = eval(parseFloat(CurrentAmount)).toFixed(decimalplace);
    Total["Opening Balance"] = begin_bal.toString();
    Total["Sales Amount"] = SalesAmount.toString();
    Total["Collection Amount"] = CollectionAmount.toString();
    Total["Current Balance"] = CurrentAmount.toString();
    
    data["HEADERS"] = Headers;
    data["TOTAL"] = [Total];
    data["data"] = result;
    getCommonData();
    console.log(JSON.stringify(data));
    //alert("v4");
    if(type=='1')
    {
       SetArray(ReportName.CreditSummary);
    }else
    {
    	SetArray(ReportName.CreditTempSummary); 
    }
    ReportsToPrint(arrRpt,currpoint+1);

}


function PrintAgingAnalysis(customercode,name){
	data={};
	var Qry = "SELECT invoicenumber,strftime('%d-%m-%Y',transactiondate) transactiondate,ifnull(strftime('%d-%m-%Y',duedate),'NONE') duedate,invoicebalance as totalinvoiceamount ,remarks1,amountpaid,invoicebalance,ifnull(pdcbalance,0) pdcbalance,erpreferencenumber as erpnumber,salesmancode FROM customerinvoice WHERE invoicebalance <> 0  AND voidflag IS NOT 1 AND customercode = " + customercode +" ORDER BY strftime('%Y-%m-%d',duedate),invoicenumber";
    console.log(Qry);                
    if (platform == 'iPad') {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)            
                result = [];
            ProcessCollection(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.erpnumber,item.transactiondate,item.duedate, item.totalinvoiceamount,item.remarks1]];
                });
            }
            else
                result = [];
            
            parseAgingAnalysis(customercode,name,result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
	
	
	
}

function parseAgingAnalysis(customercode,name,result){
	
	getCommonData();
    
    if(sessionStorage.getItem("Language")=='en'){
    	
   	 data["CUSTOMER"] = customercode + "   " + name+""; //"5416-SWITZ MASTER BAKERS (Cr)";
    }else{
   	
   	 data["CUSTOMER"] = customercode + "   *" + name+"!"; //"5416-SWITZ MASTER BAKERS (Cr)";
   }
   
    data["HEADERS"] = ["Invoice#","Invoice Date","Due Date","Due Amount","Salesman"];
    var totalamount = 0;    
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {
            if(j == 0)
            {
                //result[i][j] = parseInt(eval(result[i][j]));                
                result[i][j] = (result[i][j]).toString();                
            }
            if(j==3)
            {                
                if(result[i][j] != "")
                    result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
            }
                
            if(j==3)
                totalamount += eval(result[i][j]);
        }
    }
    if(isNaN(totalamount))
        totalamount = 0;
    totalamount = eval(parseFloat(totalamount)).toFixed(decimalplace);
    data["TOTAL"] = {"Due Amount" : totalamount};
    data["data"] = result;
    
    console.log(JSON.stringify(data));  
    SetArray(ReportName.AgingAnalysis);    
    ReportsToPrint(arrRpt,currpoint+1);  
	
	
}


function getStockMoveReport()
{
	data={};
	PrintgetStockMove();
}

function PrintgetStockMove()
{
	
	// sujee commented 15/09/2020
/*	var Qry=" select CASE WHEN '"+sessionStorage.getItem("CheckCode")+"' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', "
						+ " 	 CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemdescription END AS 'description',  "
						+ "  cast((ifnull(inv.beginstockqty,0) + ifnull(inv.loadqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
						+ "  cast((ifnull(inv.beginstockqty,0) + ifnull(inv.loadqty,0)) % im.unitspercase as INT) as startinven,"
						+ "  cast((ifnull(inv.loadcutqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
						+ " cast((ifnull(inv.loadcutqty,0) ) % im.unitspercase as INT) as stocktransfer, "
						+ "  cast((ifnull(inv.saleqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
						+ "  cast((ifnull(inv.saleqty,0) ) % im.unitspercase as INT) as salesqty, "
						+ "  cast((ifnull(inv.freesampleqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
						+ "  cast((ifnull(inv.freesampleqty,0) ) % im.unitspercase as INT) as freegood, "
						+ "  cast((ifnull(inv.returnqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
						+ "  cast((ifnull(inv.returnqty,0) ) % im.unitspercase as INT) as goodret, "
						+ "  cast((ifnull(inv.returnfreeqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
						+ "  cast((ifnull(inv.returnfreeqty,0) ) % im.unitspercase as INT) as goodretfree, "
						+ "  cast((ifnull(inv.damageqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
						+ "  cast((ifnull(inv.damageqty,0) ) % im.unitspercase as INT) as damagedqty,'0/0' as rettowarehouse, "
						+ "  cast((ifnull(inv.endstockqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
						+ "  cast((ifnull(inv.endstockqty,0) ) % im.unitspercase as INT) as actstkonvan, '0/0' as variance "
						+ " from inventorysummarydetail inv inner join itemmaster im on inv.itemcode=im.actualitemcode  where routekey="+sessionStorage.getItem("RouteKey")+"  ";*/
	
	
	var Qry=" select CASE WHEN '"+sessionStorage.getItem("CheckCode")+"' = 'alternatecode' THEN im.alternatecode ELSE im.actualitemcode END AS 'itemcode', "
	+ " 	 CASE WHEN '" + sessionStorage.getItem("Language") + "' = 'en' THEN im.itemdescription ELSE im.arbitemdescription END AS 'description',  "
	+ "  cast((ifnull(inv.beginstockqty,0) + ifnull(inv.loadqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
	+ "  cast((ifnull(inv.beginstockqty,0) + ifnull(inv.loadqty,0)) % im.unitspercase as INT) as startinven,"
	+ "  cast((ifnull(inv.loadcutqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
	+ " cast((ifnull(inv.loadcutqty,0) ) % im.unitspercase as INT) as stocktransfer, "
	+ "  cast((ifnull(inv.saleqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
	+ "  cast((ifnull(inv.saleqty,0) ) % im.unitspercase as INT) as salesqty, "
	+ "  cast((ifnull(inv.freesampleqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
	+ "  cast((ifnull(inv.freesampleqty,0) ) % im.unitspercase as INT) as freegood, "
	+ "  cast((ifnull(inv.returnqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
	+ "  cast((ifnull(inv.returnqty,0) ) % im.unitspercase as INT) as goodret, "
	+ "  cast((ifnull(inv.expiryqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
	+ "  cast((ifnull(inv.expiryqty,0) ) % im.unitspercase as INT) as expiryqty, "
	+ "   cast((ifnull(inv.damageqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
	+ "  cast((ifnull(inv.damageqty,0))  % im.unitspercase as INT)  as damagedqty,'0/0' as rettowarehouse, "
	+ "  cast((ifnull(inv.endstockqty,0)) / im.unitspercase as INT)  || '/' ||   +  "
	+ "  cast((ifnull(inv.endstockqty,0) ) % im.unitspercase as INT) as actstkonvan, '0/0' as variance "
	+ " from inventorysummarydetail inv inner join itemmaster im on inv.itemcode=im.actualitemcode  where routekey="+sessionStorage.getItem("RouteKey")+"  ";
	console.log(Qry);
	//alert(Qry);
	
	   if (platform == 'iPad') {
	        Cordova.exec(function(result) 
	        {
	            if(result.length <= 0)            
	                result = [];
	            parseStockMove(result);
	        },
	        function(error) {
	            alert(error);
	        },
	        "PluginClass",
	        "GetdataMethod",
	        [Qry]);
	    }
	    else if (platform == 'Android') {
	        window.plugins.DataBaseHelper.select(Qry, function(result) {
	            if(result.array != undefined)
	            {
	                result = $.map(result.array, function(item, index) {
	                        return [[item.itemcode,item.description, item.startinven,item.stocktransfer,item.salesqty,item.freegood,item.goodret,item.expiryqty,item.damagedqty,item.rettowarehouse,item.actstkonvan,item.variance]];
	                });
	            }
	            else
	                result = [];
	            
	            parseStockMove(result);
	        },
	        function() {
	            console.warn("Error calling plugin");
	        });
	    }
}

function parseStockMove(result)
{

	getCommonData();
	var total = {"Start Inven" : "0/0","Stock Transfer" : "0/0","Sales" : "0/0","Free Goods" : "0/0","Goods Return" : "0/0","Expiry" : "0/0","Damage" : "0/0","Return to WH" : "0/0","Actual Trk" : "0/0","Variance" : "0/0"}; 
    var Header = ["Code","Description","Start Inven","Stock Transfer","Sales","Free Goods","Goods Return","Expiry","Damage","Return to WH","Actual Trk","Variance"];
    var totalValue=0;
    
	
	for(i=0;i<result.length;i++)
			{        
			
			for(j=0;j<result[i].length;j++)
			{
			if(j == 0)
			{
			result[i][j] = (result[i][j]);
			result[i][j] = (result[i][j]).toString();
			}
			if(j >=2 && j<=11)
			{
			var qtykey = "";
			if(j == 2)
			    qtykey = "Start Inven";
			if(j == 3)
			    qtykey = "Stock Transfer";
			if(j == 4)
			    qtykey = "Sales";
			if(j == 5)
			    qtykey = "Free Goods";
			if(j == 6)
			    qtykey = "Goods Return";
			if(j == 7)
			    qtykey = "Expiry";
			if(j == 8)
			    qtykey = "Damage";
			if(j == 9)
			    qtykey = "Return to WH";
			if(j == 10)
			    qtykey = "Actual Trk";
			if(j == 11)
			    qtykey = "Variance";
			
			
			QuantityTotal(total,qtykey,result[i][j]);
			}
	/*	if(j==11){
			 result[i][j] = eval((result[i][j]).toString()).toFixed(decimalplace);
			 totalValue = (eval(totalValue) + eval(result[i][j])).toFixed(decimalplace);
			}*/
			}
			}
    totalValue = eval(parseFloat(totalValue)).toFixed(decimalplace);
				    
   // total["Total Value"] = totalValue.toString();
    data["HEADERS"] = Header;
    data["TOTAL"] = [total];
    data["data"] = result;
    console.log(JSON.stringify(data));  
    //alert(JSON.stringify(data));
    SetArray(ReportName.StockMoveReport);    
    ReportsToPrint(arrRpt,currpoint+1);  
	
    
    
}

function getFreeSummary()
{

data={};
PrintFreeSummary();
}

function PrintFreeSummary()
{

	//var Qry = "select CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE id.itemcode END AS 'itemcode',im.itemdescription as itemdescription,CASE WHEN im.unitspercase=1 THEN (0 || '/' || SUM(CAST(id.manualfreeqty AS INT))) ELSE (SUM(CAST(id.manualfreeqty/im.unitspercase AS INT)) || '/' || SUM(CAST(id.manualfreeqty%im.unitspercase AS INT))) END AS quantity,((CAST(id.manualfreeqty/im.unitspercase AS INT) * id.salescaseprice) + CAST(id.manualfreeqty%im.unitspercase AS INT) * id.salesprice) as amount  from invoicedetail id inner join itemmaster im on id.itemcode=im.actualitemcode where routekey="+sessionStorage.getItem("RouteKey")+" and id.manualfreeqty>0 group by itemcode";
	var Qry = "select CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE id.itemcode END AS 'itemcode',im.itemdescription as itemdescription,CASE WHEN im.unitspercase=1 THEN (0 || '/' || SUM(CAST((ifnull(id.manualfreeqty,0)+ifnull(id.freesampleqty,0)) AS INT))) ELSE (SUM(CAST((ifnull(id.manualfreeqty,0)+ifnull(id.freesampleqty,0))/im.unitspercase AS INT)) || '/' || SUM(CAST((ifnull(id.manualfreeqty,0)+ifnull(id.freesampleqty,0))%im.unitspercase AS INT))) END AS quantity,((CAST((ifnull(id.manualfreeqty,0)+ifnull(id.freesampleqty,0))/im.unitspercase AS INT) * id.salescaseprice) + CAST((ifnull(id.manualfreeqty,0)+ifnull(id.freesampleqty,0))%im.unitspercase AS INT) * id.salesprice) as amount   from invoicedetail id inner join itemmaster im on id.itemcode=im.actualitemcode where routekey="+sessionStorage.getItem("RouteKey")+" and (id.manualfreeqty>0 or id.freesampleqty>0) group by itemcode";
	
	
	console.log(Qry);
	//alert(Qry);
    if (platform == 'iPad') {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)            
                result = [];
            parseFreeSummary(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.itemcode,item.itemdescription, item.quantity,item.amount]];
                });
            }
            else
                result = [];
            
            parseFreeSummary(result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
	
}

function parseFreeSummary(result)
{

	getCommonData();
	var total = {"Quantity" : "0/0","Total Value" : "0.000"}; 
    var Header = ["Item Code","Description","Quantity","Total Value"];
    var totalValue=0;
    
    /*for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {           
            if(j == 0)
            {
                result[i][j] = (result[i][j]);
                result[i][j] = (result[i][j]).toString();
            }   

            if(j == 2)
            {
                var qtykey = "Quantity";
                QuantityTotal(total,qtykey,result[i][j]);
            }
            if(j == 3)
            {
                var qtykey = "Total Value";
                QuantityTotal(total,qtykey,result[i][j]);
            }

    }
    /* if(j == 3)
        {
        	 result[i][j] = eval((result[i][j]).toString()).toFixed(decimalplace);
        	 totalValue = (eval(totalValue) + eval(result[i][j])).toFixed(decimalplace);
        }*/
    //}*/

  /*  totalValue = eval(parseFloat(totalValue)).toFixed(decimalplace);
    alert('totalValue' +totalValue);
    total["Total Value"] = totalValue.toString();*/
//    if(data["data"] == undefined)
   //     data["data"] = [];
    //data["data"].push({"DATA" : result, "HEADERS" : Header, "TOTAL" : total});
	
	for(i=0;i<result.length;i++)
			{        
			
			for(j=0;j<result[i].length;j++)
			{
			if(j == 0)
			{
			result[i][j] = (result[i][j]);
			result[i][j] = (result[i][j]).toString();
			}
			if(j >=2 && j<=2)
			{
			var qtykey = "";
			if(j == 2)
			    qtykey = "Quantity";
			if(j == 3)
			    qtykey = "Total Value";
			
			
			QuantityTotal(total,qtykey,result[i][j]);
			}
		if(j==3){
			 result[i][j] = eval((result[i][j]).toString()).toFixed(decimalplace);
			 totalValue = (eval(totalValue) + eval(result[i][j])).toFixed(decimalplace);
			}
			}
			}
    totalValue = eval(parseFloat(totalValue)).toFixed(decimalplace);
				    
    total["Total Value"] = totalValue.toString();
    data["HEADERS"] = Header;
    data["TOTAL"] = [total];
    data["data"] = result;
    console.log(JSON.stringify(data));  
   // alert(JSON.stringify(data));
    SetArray(ReportName.FreeSummary);    
    ReportsToPrint(arrRpt,currpoint+1);  
	
}

function getReturnSummary(){
	data={};
	PrintReturnSummary("damage");

}

function PrintReturnSummary(trans){
	
	if(trans=="damage"){
		var Qry = "select CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE id.itemcode END AS 'itemcode',im.itemdescription as itemdescription,CASE WHEN im.unitspercase=1 THEN (0 || '/' || SUM(CAST(id.quantity AS INT))) ELSE (SUM(CAST(id.quantity/im.unitspercase AS INT)) || '/' || SUM(CAST(id.quantity%im.unitspercase AS INT))) END AS quantity,id.expirydate as expirydate from invoicerxddetail id inner join itemmaster im on id.itemcode=im.actualitemcode where routekey="+sessionStorage.getItem("RouteKey")+" and itemtransactiontype=1 group by itemcode,expirydate";
	}else{
		var Qry = "select CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE id.itemcode END AS 'itemcode',im.itemdescription as itemdescription,CASE WHEN im.unitspercase=1 THEN (0 || '/' || SUM(CAST(id.quantity AS INT))) ELSE (SUM(CAST(id.quantity/im.unitspercase AS INT)) || '/' || SUM(CAST(id.quantity%im.unitspercase AS INT))) END AS quantity,id.expirydate as expirydate from invoicerxddetail id inner join itemmaster im on id.itemcode=im.actualitemcode where routekey="+sessionStorage.getItem("RouteKey")+" and itemtransactiontype=2 group by itemcode,expirydate";
	}
	
    console.log(Qry);                
    if (platform == 'iPad') {
        Cordova.exec(function(result) 
        {
            if(result.length <= 0)            
                result = [];
            ProcessCollection(result);
        },
        function(error) {
            alert(error);
        },
        "PluginClass",
        "GetdataMethod",
        [Qry]);
    }
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                        return [[item.itemcode,item.itemdescription, item.quantity,item.expirydate]];
                });
            }
            else
                result = [];
            
            parseReturnSummary(trans,result);
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
	
	
	
}

function parseReturnSummary(trans,result){
	
	getCommonData();
    
	var total = {"Quantity" : "0/0"}; 
    var Header = ["Item Code","Description","Quantity","Expiry Date"];
       
    for(i=0;i<result.length;i++)
    {
        for(j=0;j<result[i].length;j++)
        {
                           
            result[i][j] = (result[i][j]).toString();                
            if(j == 2)
            {
                var qtykey = "Quantity";
                QuantityTotal(total,qtykey,result[i][j]);
            }
           
        }
    }
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"TITLE":trans,"DATA" : result, "HEADERS" : Header, "TOTAL" : total});
    if(trans=="damage"){
    	PrintReturnSummary("return");
    }else{
    	console.log(JSON.stringify(data));  
        SetArray(ReportName.ReturnSummary);    
        ReportsToPrint(arrRpt,currpoint+1);  
    }
    
}


function PrintReport()
{
    var printdevice = true;
   
    if(printdevice)
    {
	//dic = [{"data" : dic, "name" : report_name}];
	if(platform == "iPad")
	{    
        Cordova.exec(function nativePluginResultHandler (result) {
                    //alert("SUCCESS: \r\n"+result );
                    PrintDone();
                 },
                 function nativePluginErrorHandler (error) {
                    //alert("ERROR: \r\n"+error );
                    PrintDone();
                 },
                 "PrintClass",
                 "nativeFunction",
                 [ArrPrint]);
     }
     else
     {
    	
         if(printval=='')
             getdecimal();
         //  alert("test");
         //  alert(printval);
         //	------Start
        
          if(printval == 3)
          {
          // alert("douut");
          window.plugins.DotmatHelper.print(ArrPrint,function(data)
                  {   
                      console.log(data.status);
                      console.log(data.isconnected);
                      
                      if(data.status && data.isconnected == 0)
                          PrintDone();
                      else
                      {
                    	  getLangText("Error In Printing");
                          PrintDone();
                      }
                      console.warn("SUCCESS");
                  },
                  function()
                  {           
                      PrintDone();
                      console.warn("Error");
                  });
          
          }else if(printval == 2)
              {
             //alert("dotpb2");
              window.plugins.PB51Helper.print(ArrPrint,function(data)
                      {   
                          console.log(data.status);
                          console.log(data.isconnected);
                          if(data.status && data.isconnected == 0)
                              PrintDone();
                          else
                          {
                        	  getLangText("Error In Printing");
                              PrintDone();
                          }
                          console.warn("SUCCESS");
                      },
                      function()
                      {           
                          PrintDone();
                          console.warn("Error");
                      });
              }else if(printval == 1)
              {
                 //alert("zeb");
                  window.plugins.ZebraHelper.print(ArrPrint,function(data)
                          {   
							  
                              console.log(data.status);
                              console.log(data.isconnected);
                              if(data.status && data.isconnected == 0)
                                  PrintDone();
                              else
                              {
                            	  getLangText("Error In Printing");
                                  PrintDone();
                              }
                              console.warn("SUCCESS");
                          },
                          function()
                          {           
                              PrintDone();
                              console.warn("Error");
                          });
              }else if(printval ==0){
					
					
					navigator.notification.confirm(
					   'Please Select Your Printer',
					    callBackFunction, 
					    'Printer Selection',
					    ['A4 Printer','4 Inch Printer']
					);
			
              }
       
     }
     }
     else
     {        
        PrintDone();
     }
}

function callBackFunction(b){
	  if(b == 1){
		  window.plugins.DotmatHelper.print(ArrPrint, function(data) {

				if (data.status && data.isconnected == 0)
					PrintDone(1);
				else {
					alert("Error In Printing");
					PrintDone(2);
				}
				console.warn("SUCCESS");
			}, function() {
				navigator.notification.alert("Error In Printing");
				PrintDone(2);
				console.warn("Error");
			});
		  
	  }
	  else {
		  window.plugins.PB51Helper.print(ArrPrint, function(data) {
				if (data.status && data.isconnected == 0)
					PrintDone(1);
				else {
					alert("Error In Printing");
					PrintDone(2);
				}
				console.warn("SUCCESS");
			}, function() {
				navigator.notification.alert("Error In Printing");
				PrintDone(2);
				console.warn("Error");
			});
		  
		  
	  }
	}

function printOrder()
{
    data = {};
    getOrderInfo();
}
function getOrderInfo()
{
	
	var Qry = "SELECT cm.customercode,(CASE WHEN (traname IS NULL OR traname='') THEN customername ELSE traname END)   AS 'customername',customeraddress1  AS 'address',"
		+"inv.paymenttype,IFNULL(totalpromoamount,0) totalpromoamount,(IFNULL(totalsalesamount,0) - IFNULL(totalreturnamount,0)"
		+"-IFNULL(totaldamagedamount,0)) "
		+"totalinvoiceamount,documentnumber,invoicenumber,comments,printstatus,(SELECT CASE cm.messagekey5 WHEN 0 THEN '' "
		+"ELSE messageline1 || messageline2 || messageline3 || messageline4 END FROM customermessages WHERE messagekey = messagekey5)"
		+" AS invheadermsg,(SELECT CASE cm.messagekey6 WHEN 0 THEN '' ELSE messageline1 || messageline2 || messageline3 || messageline4"
		+" END FROM customermessages WHERE messagekey = messagekey6) AS invtrailormsg,(SELECT (IFNULL(sum(amount),0) - IFNULL(sum(promoamount),0))"
		+" from salesorderdetail invt where invt.transactionkey = inv.transactionkey) as tcamount,cm.invoicepaymentterms,CAST(IFNULL(amount,0) "
		+"AS VARCHAR) amount ,ifnull(checknumber,0) as checknumber,(SELECT sum(IFNULL(promoamount,0)) from salesorderdetail "
		+"invt where invt.visitkey=inv.visitkey AND invt.routekey=" + sessionStorage.getItem("RouteKey")+") as promoamount,inv.transactionkey,"
		+"cm.outletsubtype,CAST(inv.totalinvoiceamount AS VARCHAR) as headerAmount,customeraddress2  AS 'customeraddress2',customeraddress3,"
		+"printoutletitemcode,cm.alternatecode,splitfree,0 as totalmanualfree,0 as totaldiscountamount,"
		+"arbcustomeraddress1,arbcustomeraddress2,arbcustomeraddress3,CAST(IFNULL(totalsalesamount,0) AS VARCHAR) tsalesamt,"
		+"CAST(IFNULL(totalreturnamount,0) AS VARCHAR) treturnsamt,CAST(IFNULL(totaldamagedamount,0) AS VARCHAR) tdamagesamt,"
		+"0 AS diffround,cm.invoicepriceprint "
		+"as invoicepriceprint,COALESCE(inv.totallineitemtax,0) as totallineitemtax,(select sum(salesqty) from salesorderdetail  invt where invt.transactionkey = inv.transactionkey) as totalSalesQty ,"
		+" (select sum(COALESCE(returnqty,0)) from salesorderdetail invt where invt.transactionkey = inv.transactionkey)  as totalReturnQty,"
		+" (select sum(COALESCE(damagedqty,0))  from salesorderdetail invt where invt.transactionkey = inv.transactionkey) as totalDamagedQty,"
		+" (select sum(COALESCE(promoqty,0))+sum(COALESCE(freesampleqty,0))+sum(COALESCE(manualfreeqty,0)) from salesorderdetail invt where invt.transactionkey = inv.transactionkey)  as totalFreeQty,ifnull(cm.customertaxidoptions,0) printtax,ifnull(cm.applytax,0) applytax,ifnull(cm.taxregistrationnumber,0) taxregistrationnumber,"
		+" (select sum(COALESCE(salesorderexcisetax,0)+COALESCE(salesordervat,0)) from salesorderdetail invt where invt.transactionkey = inv.transactionkey) as salestax,"
		+" (select sum(COALESCE(returnexcisetax,0)+COALESCE(returnvat,0)) from salesorderdetail invt where invt.transactionkey = inv.transactionkey) as returntax,"
		+" (select sum(COALESCE(damagedexcisetax,0)+COALESCE(damagedvat,0)) from salesorderdetail invt where invt.transactionkey = inv.transactionkey) as damagetax,"
		+" (select sum(COALESCE(fgexcisetax,0)+ COALESCE(fgvat,0)) from salesorderdetail invt where invt.transactionkey = inv.transactionkey) as freetax,ifnull((select amount from cashcheckdetail where routekey=" + sessionStorage.getItem("RouteKey") + " and visitkey=inv.visitkey and typecode=0),0) cashamt,ifnull((select amount from cashcheckdetail where routekey=" + sessionStorage.getItem("RouteKey") + " and visitkey=inv.visitkey and typecode=1),0) chequeamt,"
		+" (select sum(COALESCE(salesorderexcisetax,0)-COALESCE(returnexcisetax,0)-COALESCE(damagedexcisetax,0)+COALESCE(fgexcisetax,0)+COALESCE(promoexcisetax,0)) from salesorderdetail invt where invt.transactionkey = inv.transactionkey) as totExcTax, "
        +"(select sum(COALESCE(salesordervat,0)-COALESCE(returnvat,0)-COALESCE(damagedvat,0)+COALESCE(fgvat,0)+COALESCE(promovat,0)) from salesorderdetail invt where invt.transactionkey = inv.transactionkey) as totVatTax,ifnull(cm.taxcardno,' ') as taxcard,ifnull(cm.crno,' ') as crno "
        +"  FROM customermaster cm "
		+"JOIN salesorderheader inv ON cm.customercode = inv.customercode AND inv.invoicenumber=" + invoicenumber 
		+ " LEFT JOIN cashcheckdetail ccd ON ccd.visitkey = inv.visitkey AND ccd.routekey = inv.routekey";
    console.log(Qry+" -----");
    if (platform == 'iPad') 
    {
    }
    else if (platform == 'Android') {
    	var customeraddress1,customeraddress2,customeraddress3;
    	
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                result = $.map(result.array, function(item, index) {
                    return [[item.customercode,item.customername, item.address, item.paymenttype,item.totalpromoamount,
                             item.totalinvoiceamount,item.documentnumber,item.invoicenumber,item.comments,item.printstatus,
                             item.invheadermsg,item.invtrailormsg,item.tcamount,item.invoicepaymentterms,item.amount,
                             item.bankname,item.checkdate,item.checknumber,item.promoamount,item.transactionkey,
                             item.outletsubtype,item.headerAmount,item.customeraddress2,item.customeraddress3,
                             item.printoutletitemcode,item.alternatecode,item.splitfree,item.totalmanualfree,
                             item.totaldiscountamount,item.arbcustomeraddress1,item.arbcustomeraddress2,item.arbcustomeraddress3,
                             item.tsalesamt,item.treturnsamt,item.tdamagesamt,item.diffround,item.invoicepriceprint,item.cashamt,
                             item.chequeamt,item.totallineitemtax,item.totalSalesQty,item.totalReturnQty,item.totalDamagedQty,item.totalFreeQty,
                             item.printtax,item.applytax,item.taxregistrationnumber,item.salestax,item.returntax,item.damagetax,item.freetax,
                             item.cashamt,item.chequeamt,item.totExcTax,item.totVatTax,item.taxcard,item.crno]];    
                });
                customercode = result[0][0];
              
                customername = result[0][1];
                customeraddress1 =result[0][2];
                 
                paymenttype = result[0][3];                
                totalpromoamount = eval(result[0][4]).toFixed(decimalplace);
                totalinvoiceamount = eval(result[0][5]).toFixed(decimalplace);
                documentnumber = parseInt(eval(result[0][6]));
                invoicenumber = parseInt(eval(result[0][7]));
                comments = result[0][8];
                printstatus = result[0][9];
                invheadermsg = result[0][10];
                invtrailormsg = result[0][11];
                itempromoamount = eval(eval(result[0][18])).toFixed(decimalplace);
                transactionkey = parseInt(eval(result[0][19]));
                tcamount = eval(result[0][12]).toFixed(decimalplace) - eval(result[0][14]).toFixed(decimalplace);
				invoicepaymentterms = result[0][13];
				customerOutlet=result[0][20];
				headerAmount=result[0][21];
				customeraddress2=result[0][22];
				customeraddress3=result[0][23];
				printoutletitemcode=result[0][24];
				alternatecode=result[0][25];
				splitfree=result[0][26];
				var manualfree=result[0][27]
				manualfreediscount=eval(result[0][28]);
				 customeraddress = customeraddress1;
                 if(customeraddress2 !='')
                 customeraddress =customeraddress+", "+customeraddress2;
                 if(customeraddress3 !='')
                 customeraddress =customeraddress+", "+customeraddress3;
				
				if(customerOutlet==null || customerOutlet=='' || customerOutlet==undefined){
					
					customerOutlet=0;
					
				}
				
				printtax=eval(result[0][44]);
				applytax=eval(result[0][45]);
				taxregistrationnumber=result[0][46];
				
				if(result[0][44]==null || result[0][44]=='' || result[0][44]==undefined){
	                printtax=0;
                }
				if(result[0][45]==null || result[0][45]=='' || result[0][45]==undefined){
					applytax=0;
                }
				if(result[0][46]==null || result[0][46]=='' || result[0][46]==undefined){
					taxregistrationnumber=0;
                }
				
				
				/*data["TOTSALES"]=eval(result[0][32]);
				data["TOTGOOD"]=eval(result[0][33]);
				data["TOTBAD"]=eval(result[0][34]);
				data["TOTFREE"]=0;
				data["TOTTAX"]=Math.abs(eval(result[0][39]));
				*/
				data["TOTSALES"]=eval(result[0][32]).toFixed(decimalplace)-eval(result[0][18]).toFixed(decimalplace);
				data["TOTGOOD"]=eval(result[0][33]).toFixed(decimalplace);
				data["TOTBAD"]=eval(result[0][34]).toFixed(decimalplace);
				
				data["TOTRETURNAMT"] = eval(result[0][33]).toFixed(decimalplace)+eval(result[0][34]).toFixed(decimalplace)
				data["TOTFREE"]=0;
				data["TOTTAX"]=Math.abs(eval(result[0][39]).toFixed(decimalplace));
				
				
				
				
				data["totalSalesQty"]=eval(result[0][40]);
				data["totalReturnQty"]=eval(result[0][41]);
				data["totalDamagedQty"]=eval(result[0][42]);
				data["totalFreeQty"]=eval(result[0][43]);
				
				data["SALESTAX"]=eval(result[0][47]).toFixed(decimalplace);
				data["RETURNTAX"]=eval(result[0][48]).toFixed(decimalplace);
				data["DAMAGEDTAX"]=eval(result[0][49]).toFixed(decimalplace);
				data["FREETAX"]=eval(result[0][50]).toFixed(decimalplace);
				
				data["TOTEXC"]=eval(result[0][53]).toFixed(decimalplace);
				data["TOTVAT"]=eval(result[0][54]).toFixed(decimalplace);
				
				
				var arbcustomeraddress1=result[0][31];
                var arbcustomeraddress2=result[0][30];
				var arbcustomeraddress3=result[0][29];
				totalfinalamount=eval(result[0][32])-eval(result[0][33])-eval(result[0][34])+eval(result[0][35])+eval(result[0][39]);
				arbcustomeraddress = arbcustomeraddress1;
				invoicepriceprint=eval(result[0][36]);
                if(arbcustomeraddress2 !='')
                	arbcustomeraddress =arbcustomeraddress+", "+arbcustomeraddress2;
                if(arbcustomeraddress3 !='')
                	arbcustomeraddress =arbcustomeraddress+", "+arbcustomeraddress3;
				
                
                 data["Cash"] = {"Amount" : eval(result[0][14]).toFixed(decimalplace)};
				 
				 if(result[0][16]!=''){
					 var d = new Date(result[0][16]);
					 result[0][16] = (d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
				 }
               
                var cashamt=eval(result[0][37]).toFixed(decimalplace);
                var chequeamt=eval(result[0][38]).toFixed(decimalplace);
                if(cashamt==0){
                	
                	data["ptype"]=1;
                }else if(chequeamt==0){
                	data["ptype"]=0;
                	
                }else if(cashamt>0 && chequeamt>0){
                	data["ptype"]=2;
                	
                }
                
                
                  
                data["TAXCARDNO"] = result[0][55];
                data["CRNO"] = result[0][56];
                
                checkOrderHeader('order', '1',manualfree,splitfree);
            }
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}
function checkOrderHeader(trans, val,manualfree,splitfree){
	
	
	if(eval(manualfree)>0 && splitfree=='1'){
		
		GetOrderData('order', '1',true);
	}else{
		
		GetOrderData('order', '1',false);
	}
	
	
}
function GetOrderData(trans, val,isFree)
{ 	
    //Change for display UPC or discount ----Start
    if(val == '1')
    {
	    var total = {"QTY CAS/PCS" : "0/0","TOTAL PCS" : "0","DISCOUNT" : "0","AMOUNT" : "0" }; 
	    var Header = ["SL#","ITEM#","OUTLET CODE","DESCRIPTION","UPC","QTY CAS/PCS","TOTAL PCS","CASE PRICE","UNIT PRICE","DISCOUNT","EXC TAX","VAT","AMOUNT"];

    }
    else
    {
        var total = {"QTY CAS/PCS" : "0/0","DISCOUNT" : "0","AMOUNT" : "0" }; 
        var Header = ["SL#","ITEM#","OUTLET CODE","DESCRIPTION","QTY CAS/PCS","TOTAL PCS","CASE PRICE","UNIT PRICE","DISCOUNT","EXC TAX","VAT","AMOUNT"];

    }
     var HeaderFree = ["SL#","ITEM#","OUTLET CODE","DESCRIPTION","UPC","QTY CAS/PCS","TOTAL PCS","CASE PRICE","UNIT PRICE","DISCOUNT","EXC TAX","VAT","AMOUNT"]
     total["EXC TAX"]="0.00";
 	total["VAT"]="0.00";
      var QryOrder = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode"
      +" ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',"
      +"unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((salesqty/unitspercase) AS INT),0) ELSE "
      +"(IFNULL(CAST((salesqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((salesqty%unitspercase) AS INT),0)) END AS "
      +"'quantity',salesqty AS 'tqty',IFNULL(salescaseprice,0) AS caseprice,IFNULL(salesprice,0) AS unitprice,"
      +"IFNULL(promoamount,0) as discount,(((IFNULL(CAST((salesqty/unitspercase) AS INT),0)) * "
      +"IFNULL(salescaseprice,0)) + (IFNULL(CAST((salesqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0)) - IFNULL(promoamount,0)+COALESCE(salesorderexcisetax,0)+COALESCE(salesordervat,0)"
      +" AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,COALESCE(salesorderexcisetax,0) as excisetax,COALESCE(salesordervat,0) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat FROM salesorderdetail"
      +" inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.salesqty > 0 JOIN salesorderheader invh ON"
      +" inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " 
      + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="
      +customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2 order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
     
      var QryFree = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode"
      +" ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription AS 'description',"
      +"unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((manualfreeqty/unitspercase) AS INT),0)"
      +" ELSE (IFNULL(CAST((manualfreeqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((manualfreeqty%unitspercase) AS INT),0))"
      +" END AS 'quantity',manualfreeqty AS 'tqty',0 AS caseprice,0 AS unitprice,(((IFNULL(CAST((manualfreeqty/unitspercase) AS INT),0) * IFNULL(salescaseprice,0)) + (IFNULL(CAST((manualfreeqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0))) AS 'discount',COALESCE(fgexcisetax,0)+COALESCE(fgvat,0) AS 'amount',"
      +"arbitemshortdescription AS arbdescription,im.barcode1 as barcode,COALESCE(fgexcisetax,0) as excisetax,COALESCE(fgvat,0)  as vat FROM salesorderdetail inv JOIN itemmaster im ON "
      +"im.actualitemcode = inv.itemcode and inv.manualfreeqty > 0 JOIN salesorderheader invh ON inv.transactionkey = invh.transactionkey"
      +" and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN "
      +"outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2 order by CASE printsequencecust"
      +" WHEN 0 THEN 100000 ELSE printsequencecust END";
    
      var QryPromoFree = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode"
      +" ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',"
      +"unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((freesampleqty/unitspercase) AS INT),0) ELSE (IFNULL"
      +"(CAST((freesampleqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((freesampleqty%unitspercase) AS INT),0)) END AS 'quantity',"
      +"freesampleqty AS 'tqty',IFNULL(salescaseprice,0) AS caseprice,IFNULL(salesprice,0) AS unitprice,(((IFNULL(CAST((freesampleqty/unitspercase) AS INT),0) * IFNULL(salescaseprice,0)) + (IFNULL(CAST((freesampleqty%unitspercase) AS INT),0)) * IFNULL(salesprice,0))) AS 'discount',0 AS 'amount',"
      +"arbitemshortdescription AS arbdescription,im.barcode1 as barcode,COALESCE(promoexcisetax,0) as excisetax,COALESCE(promovat,0)  as vat FROM salesorderdetail inv JOIN itemmaster im ON "
      +"im.actualitemcode = inv.itemcode and freesampleqty > 0 JOIN salesorderheader invh ON inv.transactionkey = invh.transactionkey"
      +" and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2 LEFT JOIN outletitemcodes ocode on "
      +"ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" order by CASE printsequencecust WHEN 0 THEN 100000 ELSE"
      +" printsequencecust END";	
      
      var QryGood = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode"
      +" ELSE im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',"
      +"unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((returnqty/unitspercase) AS INT),0) ELSE "
      +"(IFNULL(CAST((returnqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((returnqty%unitspercase) AS INT),0)) END AS 'quantity',"
      +"returnqty AS 'tqty',IFNULL(goodreturncaseprice,0) AS caseprice,IFNULL(goodreturnprice,0) AS unitprice,IFNULL(promoamount,0)"
      +" as discount,(((IFNULL(CAST((returnqty/unitspercase) AS INT),0) * IFNULL(goodreturncaseprice,0)) + "
      +"(IFNULL(CAST((returnqty%unitspercase) AS INT),0)) * IFNULL(goodreturnprice,0)))+COALESCE(returnexcisetax,0)+COALESCE(returnvat,0)"
      +"  AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,COALESCE(returnexcisetax,0) as excisetax,COALESCE(returnvat,0) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat FROM salesorderdetail inv JOIN"
      +" itemmaster im ON im.actualitemcode = inv.itemcode and inv.returnqty > 0 JOIN salesorderheader invh ON inv.transactionkey = "
      +"invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 "
      +"LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2 order by CASE "
      +"printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
      
      
      var QryBuyBack ="SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE"
      +" im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription  AS 'description',"
      +"unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((returnfreeqty/unitspercase) AS INT),0) ELSE "
      +"(IFNULL(CAST((returnfreeqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((returnfreeqty%unitspercase) AS INT),0)) END AS 'quantity'"
      +",returnfreeqty AS 'tqty',IFNULL(goodreturncaseprice,0) AS caseprice,IFNULL(goodreturnprice,0) AS unitprice,IFNULL(promoamount,0)+"
      +"IFNULL(returnpromoamount,0) as discount,0 AS 'amount',arbitemshortdescription AS arbdescription,im.barcode1 as barcode,0 as excisetax,0 as vat FROM"
      +" invoicedetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode and inv.returnfreeqty > 0 JOIN invoiceheader invh"
      +" ON inv.transactionkey = invh.transactionkey and invh.transactionkey="+transactionkey+" AND invh.customercode = " 
      + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on ocode.itemcode=inv.itemcode and ocode.groupcode="
      +customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2 order by CASE printsequencecust WHEN 0 THEN 100000 ELSE printsequencecust END";
      
      var QryBad = "SELECT 0 as sl,CASE WHEN '" + sessionStorage.getItem("CheckCode") + "' = 'alternatecode' THEN im.alternatecode ELSE"
      +" im.actualitemcode END AS 'itemcode',IFNULL(ocode.outletitemcode,'-') AS 'outletcode',itemdescription AS 'description',"
      +"unitspercase as upc,CASE WHEN unitspercase ='1' THEN  '0/' || IFNULL(CAST((damagedqty/unitspercase) AS INT),0) ELSE "
      +"(IFNULL(CAST((damagedqty/unitspercase) AS INT),0) ||  '/' || IFNULL(CAST((damagedqty%unitspercase) AS INT),0)) END AS "
      +"'quantity',damagedqty AS 'tqty',IFNULL(inv.returncaseprice,0) AS caseprice,IFNULL(inv.returnprice,0) AS unitprice,"
      +"IFNULL(promoamount,0) as discount,(((IFNULL(CAST((damagedqty/unitspercase) AS INT),0) * "
      +"IFNULL(inv.returncaseprice,0)) + (IFNULL(CAST((damagedqty%unitspercase) AS INT),0)) * IFNULL(inv.returnprice,0)))+COALESCE(damagedexcisetax,0)+COALESCE(damagedvat,0) "
      +" AS 'amount',arbitemshortdescription"
      +" AS arbdescription,im.barcode1 as barcode,COALESCE(damagedexcisetax,0) as excisetax,COALESCE(damagedvat,0) || '(' || CASE WHEN tm.taxpercentage IS NULL THEN 0 ELSE  CAST(tm.taxpercentage  AS INT)  END || '%)' as vat FROM salesorderdetail inv JOIN itemmaster im ON im.actualitemcode = inv.itemcode "
      +"and inv.damagedqty > 0 JOIN salesorderheader invh ON inv.transactionkey = invh.transactionkey and invh.transactionkey="
      +transactionkey+" AND invh.customercode = " + customercode+" and im.itemtype=1 LEFT JOIN outletitemcodes ocode on "
      +"ocode.itemcode=inv.itemcode and ocode.groupcode="+customerOutlet+" left join taxmaster tm on tm.taxcode=im.itemtaxkey2 order by CASE printsequencecust WHEN 0 THEN "
      +"100000 ELSE printsequencecust END";
  
      var Qry = "";    
      if(trans == 'order')
        Qry = QryOrder;
      else if(trans == 'free')
      {
        Qry = QryFree;
        Header = HeaderFree;
        delete total["Discount"];
     }
     else if(trans == 'promofree')
     {
        Qry = QryPromoFree;
        Header = HeaderFree;
        delete total["Discount"];
    }
    else if(trans == 'good')
        Qry = QryGood;
    else if(trans == 'buyback')
        Qry = QryBuyBack;
    else if(trans == 'bad')
        Qry = QryBad;
    console.log(Qry);   
    
    if (platform == 'iPad') 
    {}
    else if (platform == 'Android') {
        window.plugins.DataBaseHelper.select(Qry, function(result) {
            if(result.array != undefined)
            {
                if(trans == 'free' || trans == 'promofree')
                {
                	
                	if(eval(printbarcode)==1){
                		result = $.map(result.array, function(item, index) {
                            return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc,item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.barcode]];    
                        });
                		
                	}else{
                		result = $.map(result.array, function(item, index) {
                            return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc,item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.arbdescription]];    
                        });
                		
                		
                	}
                	
                    
                }
                else
                {
                	if(eval(printbarcode)==1){
                		 result = $.map(result.array, function(item, index) {
                             //Start for display UPC or Discount
                             if(val == '1')
                             {
                                 return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc,item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.barcode]];
                             }
                             else
                             {
                                 return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc, item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.barcode]];                               
                             }
                		  });
                	}else{
                		 result = $.map(result.array, function(item, index) {
                             //Start for display UPC or Discount
                             if(val == '1')
                             {
                                 return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc,item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.arbdescription]];
                             }
                             else
                             {
                                 return [[item.sl,item.itemcode,item.outletcode,item.description,item.upc, item.quantity,item.tqty,item.caseprice, item.unitprice,item.discount,item.excisetax,item.vat,item.amount,item.arbdescription]];                               
                             }
                		  });
                	}
                	
                }
            }
            else
                result = [];
               
               
            SetOrderTransaction(trans, result, Header, total,isFree);
			if (trans == 'order')
				GetOrderData('good', '1',isFree); // Added for display UPC
			if (trans == 'good')
				GetOrderData('buyback', '1',isFree); // Added for display UPC
			if (trans == 'buyback')
				GetOrderData('bad', '1',isFree); // Added for display UPC
			
			if (trans == 'bad')
				GetOrderData('promofree', '1',isFree); // Added for display UPC
			
			if (trans == 'promofree'&& !isFree)
				GetOrderData('free', '1',isFree); // Added for display UPC
        },
        function() {
            console.warn("Error calling plugin");
        });
    }
}

function SetOrderTransaction(trans,result,Header,Total,isFree)
{   
	Total["EXC TAX"]=0;
	Total.VAT=0;
        for(i=0;i<result.length;i++)
        {
            for(j=0;j<result[i].length;j++)
            {
                if(j ==1)
                {
                	
                	if(sessionStorage.getItem("CheckCode")=='ancode' && !isNaN(result[i][j]))
                	{
                		result[i][j] = parseInt(result[i][j],10);
	                }
                	result[i][j] = (result[i][j]).toString();
                	
                }   
                if(j==2){
                	if(!isNaN(result[i][j]) && result[i][j]!='-')
                	{
                		result[i][j] = parseInt(eval(result[i][j]));
                		result[i][j] = (result[i][j]).toString();
                    }
                	else
                    {
                		result[i][j] = ("  ").toString();
                    }
                	
                }
                if(j == 5)
                {
                    var qtykey = "QTY CAS/PCS";
                    QuantityTotal(Total,qtykey,result[i][j]);
                }
                if(j == 6)
                {
                    var qtykey = "TOTAL PCS";
                    QTotal(Total,qtykey,result[i][j]);
                }
                
                else if(j == 9 && trans != 'free' && trans != 'promofree')
                    Total.DISCOUNT = (eval(Total.DISCOUNT) + eval(result[i][j])).toString();
            	else if (j == 10){
    				Total["EXC TAX"] = (eval(Total["EXC TAX"]) + eval(result[i][j]));
    				
    			}
    			else if (j == 11){
    				 if(result[i][j].indexOf("(")!=-1){
    					 var tax=result[i][j].substring(0,result[i][j].indexOf("("));
    					 var vatrate=result[i][j].substring(result[i][j].indexOf("("),result[i][j].length);
    					 
    					 tax = (eval(tax)).toFixed(decimalplace);
    					 result[i][j] = tax+vatrate;
    					 Total.TAX = (eval(Total.TAX) + eval(tax))
    							.toString();
    					Total["VAT"] = (eval(Total["VAT"]) + eval(tax));
    				}else{
    					result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
    					Total.TAX = (eval(Total.TAX) + eval(result[i][j]))
    							.toString();
    					Total["VAT"] = (eval(Total["VAT"]) + eval(result[i][j]));
    				} 
    			}
                else if(j == 12 && (trans == 'free'))
                    Total.AMOUNT = (eval(Total.AMOUNT) + eval(result[i][j])).toFixed(decimalplace).toString();
                else if(j == 12 && trans != 'free'){
                    Total.AMOUNT = (eval(Total.AMOUNT) + eval(result[i][j])).toFixed(decimalplace).toString();
                   
                }
                if(j > 6 && j<=12){
                	if(j!=11){
                  		 result[i][j] = (eval(result[i][j])).toFixed(decimalplace);
       				}
                }
                    
            }
        }
        if(isNaN(Total.DISCOUNT))
            Total.DISCOUNT = 0;
        if(isNaN(Total.AMOUNT))
            Total.AMOUNT = 0;
        if (isNaN( Total["EXC TAX"]))
   		 Total["EXC TAX"] = 0;
        if (isNaN(Total.VAT))
   		 Total.VAT = 0;
        Total.DISCOUNT = eval(parseFloat(Total.DISCOUNT)).toFixed(decimalplace);
        Total.AMOUNT = Total.AMOUNT = eval(parseFloat(Total.AMOUNT)).toFixed(decimalplace);
        Total["EXC TAX"] = eval(parseFloat(Total["EXC TAX"])).toFixed(decimalplace);
    	Total.VAT = eval(parseFloat(Total.VAT)).toFixed(decimalplace);
    //SetData(trans,result,Header,total);
    if(data["data"] == undefined)
        data["data"] = [];
    data["data"].push({"TITLE":trans,"DATA" : result, "HEADERS" : Header, "TOTAL" : Total});    
    if ((isFree && trans == "promofree") || trans=="free")
    {
    getCommonData();
    var invoicetype = ""
  
    var  companyTaxStng=sessionStorage.getItem("enabletax");
    
    if(companyTaxStng==1){
			invoicetype = "TAX ORDER ";
		
    }else{
    		invoicetype = "ORDER " + invoicenumber;
		
    }
    	
    
    
    data["invoicepaymentterms"]=invoicepaymentterms;	
    data["invoicenumber"]=invoicenumber;	
    data["INVOICETYPE"] = invoicetype;
   
    data["CUSTOMER"] = alternatecode + "   " + customername+""; //"5416-SWITZ MASTER BAKERS (Cr)";
	data["CUSTOMERID"] = alternatecode +"";
   	data["CUSTOMERNAME"] = customername+""; 
   	
    data["printbarcode"]=eval(printbarcode);
    data["ADDRESS"] = customeraddress; //"Suite 808, Burjuman Business Tower";
	data["ARBADDRESS"] = arbcustomeraddress.substring(0, 25);  //"Suite 808, Burjuman Business Tower";
	
    if(comments == 0 || comments == "0")
    	comments = "";
    data["comments"] = unescape(comments);
    data["printstatus"] = getPrintStatus();
    data["printoutletitemcode"]=printoutletitemcode;
    
    //data["arbcustomername"]=arbcustomername.substring(0, 25); 
    data["enabletax"]=sessionStorage.getItem("enabletax");		
    data["companytaxregistrationnumber"]=sessionStorage.getItem("companytaxregistrationnumber");
	 data["printtax"] = printtax;
    data["applytax"] = applytax;
    data["taxregistrationnumber"] = taxregistrationnumber;
   
    
    data["SUB TOTAL"] = eval(totalfinalamount).toFixed(
			decimalplace).toString();
  
	data["INVOICE DISCOUNT"] = (totalpromoamount - itempromoamount + eval(manualfreediscount))
	.toFixed(decimalplace).toString();
		
	
    data["NET AMOUNT"] = eval(headerAmount).toFixed(decimalplace).toString();
    data["DOCUMENT NO"] = documentnumber;
    if(invheadermsg == 0 || invheadermsg == "0")
    	invheadermsg = "";
    data["invheadermsg"] = invheadermsg;
    data["invoicepriceprint"]=invoicepriceprint;
    
    if(invtrailormsg == 0 || invtrailormsg == "0")
    	invtrailormsg = "";
    data["invtrailormsg"] = invtrailormsg;
    data["isTwice"] = "0";
    data["TCCHARGED"] = (tcamount-totalpromoamount).toFixed(decimalplace).toString();
  
	if(invoicepaymentterms > 2)
		data["TCALLOWED"] = "1";
	else
		data["TCALLOWED"] = "0";
    console.log(JSON.stringify(data)); 
    SetArray(ReportName.Order);
  
    if(isFree && trans == 'promofree'){
		
    	var paymentdata={};
		paymentdata["Cash"]=data["Cash"];
		paymentdata["Cheque"]=data["Cheque"];
		data={};
		data["Cash"]=paymentdata["Cash"];
		data["Cheque"]=paymentdata["Cheque"];
		paymentdata={};
		GetOrderData('free', '1',isFree);
		
	}
	if(trans=="free"){
		ReportsToPrint(arrRpt,currpoint+1); 
		
	}
    
    
    }
}



function addDaysToDate(date,days){
   	var curDate = new Date(date);
    var newdate = new Date(curDate);
    newdate.setDate(newdate.getDate() + days);
    
    
    var currentDate=new Date();
    
    if(newdate<currentDate){
    	newdate=currentDate;
    }
    
    var dd = newdate.getDate();
    var mm = newdate.getMonth() + 1;
    var y = newdate.getFullYear();

    var formatedDate = dd + '-' + mm + '-' + y;
    
    return formatedDate;
   
}

