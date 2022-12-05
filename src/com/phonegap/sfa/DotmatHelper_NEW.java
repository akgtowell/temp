package com.phonegap.sfa;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ganesh.intermecarabic.Arabic6822;
import com.google.android.gms.internal.co;
import com.intermec.print.lp.LinePrinterException;
import com.zebra.android.printer.ZebraPrinter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class DotmatHelper_NEW extends Plugin {
	private JSONObject status;
	boolean isTwice = false;
	private HashMap<String, Integer> hashValues;
	private HashMap<String, Integer> hashArbPositions;
	private HashMap<String, Integer> hashPositions;
	private HashMap<String, String> hashArabVales;
	private JSONArray jArr;
	private ZebraPrinter printer;
	String resolution = "";
	String strFormat, strFormatBold, strFormatHeader, strFormatTitle, strPrintLeftBold, strUnderLine;
	private BluetoothAdapter mBtAdapter;
	private String callbackId = "";
	private ArrayList<DevicesData> arrData;
	ArrayAdapter<String> adapter, detectedAdapter;
	ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
	private boolean isExceptionThrown = false;
	BluetoothSocket btSocket = null;
	OutputStream outStream = null;
	String devicename = "";
	int startln = 4;
	
	int pageLength=47;
	int pageNo = 0;
	private boolean isEnglish = true;
	private String sMacAddr = "";
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	byte[] BoldOn = new byte[] { 0x1b, 0x47 };
	byte[] BoldOff = new byte[] { 0x1b, 0x48 };
	byte[] UnderlineOn = new byte[] { 0x1b, 0x2d, 0x01 };
	byte[] UnderlineOff = new byte[] { 0x1b, 0x2d, 0x00 };
	byte[] CompressOn = new byte[] { 0x1b, 0x21, 0x04 };
	byte[] CompressOff = new byte[] { 0x1b, 0x21, 0x00 };
	byte[] NewLine = new byte[] { 0x0d, 0x0a };
	byte[] DoubleHighOn = new byte[] { 0x1b, 0x21, 0x10 };
	byte[] DoubleHighOff = new byte[] { 0x1b, 0x21, 0x10 };
	byte[] DoubleWideOn = new byte[] { 0x1b, 0x21, 0x20 };
	byte[] DoubleWideOff = new byte[] { 0x1b, 0x21, 0x00 };
	byte[] resetprinter = new byte[] { 0x1b, 0x40 };
	byte[] CarriageReturn = new byte[] { 0x0D, 0x0A };
	byte[] formFeed = new byte[] {0x0d,0x0c};
	private int retryCount = 0;
	private ProgressDialog ProgressDialog;
	private android.app.ProgressDialog progressDialog;
	private Set<BluetoothDevice> pairedDevices = null;
	private ConnectTo connectTo=null;
	int  companyTaxStng=0;
	String args;
	
	@Override
	public PluginResult execute(String request, JSONArray querystring, String callbackId) {
		PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
		result.setKeepCallback(true);
		try {

			
			Log.e("Called", "Start of Plugin");
			status = new JSONObject();
			status.put("status", false);
			this.jArr = querystring;
			arrData = new ArrayList<DevicesData>();
			this.callbackId = callbackId;
			this.print();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return result;

	}

	private class asyncgetDevices extends AsyncTask<Void, Set<BluetoothDevice>, Set<BluetoothDevice>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			cordova.getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					showProgressDialog("Fetching paired List..");
				}
			});
		}
		@Override
		protected Set<BluetoothDevice> doInBackground(Void... params) {
			
			
			pairedDevices = mBtAdapter.getBondedDevices();
			return pairedDevices;
		}

		@Override
		protected void onPostExecute(Set<BluetoothDevice> pairedDevices) {
			super.onPostExecute(pairedDevices);
			cordova.getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					dismissProgress();
				}
			});
			if (pairedDevices != null) {
				if (pairedDevices.size() > 0) {

					for (BluetoothDevice device : pairedDevices) {
						Log.e("devices", device.getName() + "\n" + device.getAddress());
						System.out.println("devices" + device.getName() + "\n" + device.getAddress());
						DevicesData d1 = new DevicesData();
						d1.setAddress(device.getAddress());
						d1.setName(device.getName());
						arrData.add(d1);
					}
					showDialog(arrData);
				} else {
					Toast t = Toast.makeText(cordova.getActivity(), "No Devices Found!",
							Toast.LENGTH_SHORT);
					t.show();
					System.out.println("No devices");
					Log.e("devices", "No devices");
					try {
						status.put("status", false);
						status.put("isconnected", -7);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					sendUpdate(status, true);
				}
			}

		}

	}

	public void print() {
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		Log.e("Called", "get Adpapter");
		if(!mBtAdapter.isEnabled()){
			cordova.getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					Toast t = Toast.makeText(cordova.getActivity(), "Please Enable Bluetooth!",
							Toast.LENGTH_SHORT);
					t.show();
				}
			});
			
			
			System.out.println("No devices");
			Log.e("devices", "No devices");
			try {
				status.put("status", false);
				status.put("isconnected", -7);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			sendUpdate(status, true);
		}else{
			//new asyncgetDevices().execute();
			cordova.getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					showProgressDialog("Fetching paired List..");
				}
			});
			asyncShowPairedList();
		}
		

	}

	private void asyncShowPairedList(){
		
		Log.e("Called", "Called Pared List");
		//new asyncgetDevices().execute();
		
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
		// Get a set of currently paired devices
		
		if (pairedDevices.size() > 0) {

			for (BluetoothDevice device : pairedDevices) {
				Log.e("devices", device.getName() + "\n" + device.getAddress());
				System.out.println("devices" + device.getName() + "\n"
						+ device.getAddress());
				DevicesData d1 = new DevicesData();
				d1.setAddress(device.getAddress());
				d1.setName(device.getName());
				arrData.add(d1);
			}
			cordova.getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					dismissProgress();
				}
			});
			showDialog(arrData);
		} else {
			
			System.out.println("No devices");
			Log.e("devices", "No devices");
			cordova.getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					Toast t = Toast.makeText(cordova.getActivity()
							.getApplicationContext(), "No Devices Found!",
							Toast.LENGTH_SHORT);
					t.show();
					dismissProgress();
				}
			});
			try {
				status.put("status", false);
				status.put("isconnected", -7);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendUpdate(status, true);
		}
		
		
		
		
	}
	public void showDialog(final ArrayList<DevicesData> arrData) {

		final String[] arrDevices = new String[arrData.size()];
		for (int i = 0; i < arrData.size(); i++) {
			arrDevices[i] = arrData.get(i).getName() + "\n" + arrData.get(i).getAddress();

		}

		Runnable runnable = new Runnable() {

			public void run() {
				AlertDialog.Builder dialog = new AlertDialog.Builder(cordova.getActivity());
				dialog.setTitle("Choose Device To Pair");
				dialog.setItems(arrDevices, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						try {
							retryCount = 0;
							sMacAddr = arrData.get(which).getAddress();
							if (sMacAddr.contains(":") == false && sMacAddr.length() == 12) {
								// If the MAC address only contains hex
								// digits without the
								// ":" delimiter, then add ":" to the
								// MAC address string.
								char[] cAddr = new char[17];
								for (int i = 0, j = 0; i < 12; i += 2) {
									sMacAddr.getChars(i, i + 2, cAddr, j);
									j += 2;
									if (j < 17) {
										cAddr[j++] = ':';
									}
								}

								sMacAddr = new String(cAddr);
							}
							
							cordova.getActivity().runOnUiThread(new Runnable() {

								public void run() {
									Log.d("print","in connectiom");
								
									 connectTo=new ConnectTo();
									 connectTo.execute(sMacAddr);
									
								}
							});
							
							//startConnection(sMacAddr);
							

						} catch (Exception e) {
							e.printStackTrace();
						}
						Toast.makeText(cordova.getActivity(),
								" you have selected " + arrData.get(which).getName() + "", Toast.LENGTH_LONG).show();
					}
				});
				dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						try {
							status.put("status", false);
							status.put("isconnected", -1);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						sendUpdate(status, true);
					}
				});
				dialog.setCancelable(false);
				dialog.show();
			}
		};

		this.cordova.getActivity().runOnUiThread(runnable);

	}

	private void sendUpdate(JSONObject obj, boolean keepCallback) {
		if (this.callbackId != null) {
			Log.e("End of plugin", "true");
			try{
				if(connectTo!=null && !connectTo.isCancelled()){
					connectTo.cancel(true);
					connectTo=null;
					
				}
				cordova.getActivity().runOnUiThread(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						dismissProgress();
					}
				});
				
			}catch(Exception e){
				e.printStackTrace();
			}
			PluginResult result = new PluginResult(PluginResult.Status.OK, obj);
			result.setKeepCallback(false);
			this.success(result, this.callbackId);

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		try {
			if (btSocket != null)
				btSocket.close();

			if (outStream != null)
				outStream.close();
			if(connectTo!=null && !connectTo.isCancelled()){
				connectTo.cancel(true);
				connectTo=null;
				
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	private void doConnectionTest(final String address) throws JSONException {
		try {

			retryCount++;
			if (retryCount < 3) {
				Thread.sleep(200);
				cordova.getActivity().runOnUiThread(new Runnable() {

					public void run() {
						Log.d("print","in connectiom");
					
						 connectTo=new ConnectTo();
						 connectTo.execute(address);
						
					}
				});

			} else {
				dismissProgress();
				try {
					status.put("status", false);
					sendUpdate(status, true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showProgressDialog(String message) {
		progressDialog = ProgressDialog.show(cordova.getActivity(), "Please Wait", message, false);

	}

	private void dismissProgress() {

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();

		}

	}

	public void startConnection(final String address){
		try{
			cordova.getActivity().runOnUiThread(new Runnable() {
				
				public void run() {
					showProgressDialog("Connected To Printer");
					
				}
			});
			
			new Thread(new Runnable() {
				
				public void run() {
					
					checkConnection(address);
					cordova.getActivity().runOnUiThread(new Runnable() {
						
						public void run() {
							dismissProgress();
							
						}
					});
				}
			}).start();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	public class ConnectTo extends AsyncTask<String, Void, Boolean> {

		
		protected Boolean doInBackground(String... address) {

			checkConnection(address[0]);

			return true;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgress();
			this.cancel(true);
			

		}

		@Override
		protected void onPreExecute() {
			Log.d("print","in connectiom indisde");
			showProgressDialog("Connected to printer..");
		}

	}

	@SuppressLint("NewApi")
	private void checkConnection(String address){
		
		retryCount++;
		if (retryCount < 3) {	
				
				try{
					try {
						Log.d("print","in connectiom indisde");
						// Set up a pointer to the remote node using it's address.
						BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
						// btSocket =
						// device.createInsecureRfcommSocketToServiceRecord(MY_UUID); //
						// doesn't work on lenovo
						// btSocket =
						// device.createRfcommSocketToServiceRecord(MY_UUID);// works
						// // on
						// // lenovo
		
						if (btSocket != null && btSocket.isConnected()) {
		
							btSocket.close();
							btSocket = null;
						}
						
						//btSocket = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
						if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
							Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
							btSocket = (BluetoothSocket) m.invoke(device, 1);
						} else {
							btSocket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
		
						}
						devicename = device.getName();
		
					} catch (Exception e) {
						e.printStackTrace();
						checkConnection(address);
					}
		
					try {
		
						btSocket.connect();
						outStream = btSocket.getOutputStream();
						printReports(sMacAddr);	
					} catch (IOException e) {
						e.printStackTrace();
						checkConnection(address);
						
					}
					
				}catch(Exception e){
					e.printStackTrace();
					checkConnection(address);
				}
		
		
		
		}else{
			try {
				status.put("status", false);
				sendUpdate(status, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	@SuppressLint("NewApi")
	void printReports(String address) {

		String configLabel = null;
		String sResult = null;
		boolean isTwice = false;
		
		
		try {
			
			Log.d("Print Report", "" + jArr.toString());
			for (int j = 0; j < jArr.length(); j++) {
				JSONArray jInner = jArr.getJSONArray(j);
				for (int i = 0; i < jInner.length(); i++) {
					
					JSONObject jDict = jInner.getJSONObject(i);
					String request = jDict.getString("name");
					JSONObject jsnData = jDict.getJSONObject("mainArr");
					startln=4;
					pageLength=47;
					if(jsnData!=null && jsnData.has("printsetup") &&  jsnData.getString("printsetup").length()>0){
						
						String printSetup=jsnData.getString("printsetup");
						if(!TextUtils.isEmpty(printSetup) && printSetup.contains("*")){
							String[] setup=printSetup.split("[\\*]");
							startln=Integer.parseInt(setup[0]);
							pageLength=Integer.parseInt(setup[1]);
							
						}
							
					}
					if (request.equalsIgnoreCase("Transfer_In")) {
						// Checked
						parseLoadTransferResponse(jsnData, address);
					} else if (request.equalsIgnoreCase("Transfer_Out")) {
						// Checked
						parseLoadTransferResponse(jsnData, address);
					} else if (request.equalsIgnoreCase("LoadSummary")) {
						// Checked
						parseLoadSummaryResponse(jsnData, address);
					} else if (request.equalsIgnoreCase("LoadSummary2")) {
						// Checked
						parseLoadSummary2Response(jsnData, address);
					} else if (request.equalsIgnoreCase("Sales")) {
						String keyAccountReport=jsnData.getString("keyaccountprint");
						companyTaxStng=Integer.parseInt(jsnData.getString("enabletax"));
						
						if (!TextUtils.isEmpty(jsnData.getString("invoicepriceprint"))
								&& jsnData.getString("invoicepriceprint") != null
								&& jsnData.getString("invoicepriceprint").equals("0")) {

							printMiniSalesReport(jsnData, address);

						}else if(companyTaxStng==1){
							
							double totalexcise=jsnData.has("TOTEXC")?Double.parseDouble(jsnData.getString("TOTEXC")):0;
							if(totalexcise!=0){
								printSalesExciseTaxReport(jsnData, address);
							}else{
								printSalesTaxReport(jsnData, address);
							}
							
						}
						else {
							if(keyAccountReport.trim().equalsIgnoreCase("ALWAZZAN")){
								printSalesKeyAccountReport(jsnData, address);
							}else{
								printSalesReport(jsnData, address);
							}
							
						}
					} else if (request.equalsIgnoreCase("SalesReport")) {
						printSalesSummaryReport(jsnData, address);
					} else if (request.equalsIgnoreCase("Deposit")) {
						parseDepositResponse(jsnData, address);
						// parseDepositCashResponse(jsnData,address);

					} else if (request.equalsIgnoreCase("DepositSlip")) {
						// parseDepositResponse(jsnData,address);
						parseDepositCashResponse(jsnData, address);

					} else if (request.equalsIgnoreCase("UnloadDamage")) {
						// parseUnloadResponse(jsnData, address);
						parseUnloadDamageStales(jsnData, address);
					} else if (request.equalsIgnoreCase("Collection")) {
						parseCollectionResponse(jsnData, address);
					} else if (request.equalsIgnoreCase("AdvancePayment")) {
						parseAdvancePaymentResponse(jsnData, address);
					} else if (request.equalsIgnoreCase("EndInventory")) {
						// Checked
						parseEndInventory(jsnData, address);
					} else if (request.equalsIgnoreCase("RouteActivity")) {
						// Checked
						printRouteactivityReport(jsnData, address);
					} else if (request.equalsIgnoreCase("RouteSummary")) {
						// Checked
						printRouteSummaryReport(jsnData, address);
					} else if (request.equalsIgnoreCase("VanStock")) {
						// Checked
						printVanStockReport(jsnData, address);
					} else if (request.equalsIgnoreCase("EndInventoryReport")) {
						// Checked
						parseEndInventory(jsnData, address);
					} else if (request.equalsIgnoreCase("VanStockReport")) {
						// Checked
						printVanStockReport(jsnData, address);
					} else if (request.equalsIgnoreCase("LoadRequst")) {
						// Checked
						printLoadRequestReport(jsnData, address);
					} else if (request.equalsIgnoreCase("CreditSummary")) {
						printCreditSummaryReport(jsnData, address);
					} else if (request.equalsIgnoreCase("CreditTempSummary")) {
						printCreditTempSummaryReport(jsnData, address);
					} else if (request.equalsIgnoreCase("ItemSalesSummary")) {
						printItemSalesSummaryReport(jsnData, address);
					} else if (request.equalsIgnoreCase("ItemPriceSummary")) {
						printItemPriceSummaryReport(jsnData, address);
					} else if (request.equalsIgnoreCase("Order")) {
						companyTaxStng=Integer.parseInt(jsnData.getString("enabletax"));
						if(companyTaxStng==1){
							printOrderTaxReport(jsnData, address);
						}else{
							printOrderReport(jsnData, address);
						}
					} else if (request.equalsIgnoreCase("ReturnSummary")) {
						parseReturnSummarysResponse(jsnData, address);

					} else if (request.equalsIgnoreCase("AgingAnalysis")) {
						parseAgingAnalysisResponse(jsnData, address);

					} else if (request.equalsIgnoreCase("UnScheduledCustomer")) {
						parseUnScheduledCustomerResponse(jsnData, address);

					} else if (request.equalsIgnoreCase("DiscountReport")) {
						parseDiscountReport(jsnData, address);

					}else if(request.equalsIgnoreCase("InvoiceSummary")){
						printInvoiceSummary(jsnData, address);
					}

				}

			}
			Log.d("Print Report", "" + jArr.toString());

			// setStatus("Sending Data", Color.BLUE);

			/*
			 * if (zebraPrinterConnection instanceof BluetoothPrinterConnection)
			 * { String friendlyName = ((BluetoothPrinterConnection)
			 * zebraPrinterConnection) .getFriendlyName();
			 * setStatus(friendlyName, Color.MAGENTA);
			 * 
			 * }
			 */
		} /*
			 * catch (LinePrinterException ex) { sResult =
			 * "LinePrinterException: " + ex.getMessage(); }
			 */catch (Exception ex) {
			if (ex.getMessage() != null)
				sResult = "Unexpected exception: " + ex.getMessage();
			else
				sResult = "Unexpected exception.";
		} finally {
			if (outStream != null) {

				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			if (btSocket != null && btSocket.isConnected()) {
				try {
					btSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//
		} //

		/*
		 * } catch (Exception e) { sResult = "Unexpected exception: " +
		 * e.getMessage(); //setStatus(e.getMessage(), Color.RED); }
		 */

	}

	void parseLoadTransferResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {

			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int inventoryvalueprint = Integer.valueOf(object.getString("inventoryvalueprint"));
			String printLanguage = object.has("printlanguageflag") ? object.getString("printlanguageflag") : "2";
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Sl#", 4);
			if (printitemcode == 0) {
				hashValues.put("Item#", 0);
				
				if (printLanguage.equals("2")) {
					hashValues.put("Description", 48);
					hashValues.put("ArabDescription", 39);
					
				} else if (printLanguage.equals("1")) {
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 87);
					
				} else {
					hashValues.put("Description", 87);
					hashValues.put("ArabDescription", 0);
					
				}
				
				
			} else {
				hashValues.put("Item#", 8);
				
				if (printLanguage.equals("2")) {
					hashValues.put("Description", 40);
					hashValues.put("ArabDescription", 39);
					
				} else if (printLanguage.equals("1")) {
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 79);
					
				} else {
					hashValues.put("Description", 79);
					hashValues.put("ArabDescription", 0);
					
				}
			}

			//hashValues.put("ArabDescription", 39);
			hashValues.put("UOM", 4);

			hashValues.put("Qty", 0);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Sl#", 0);
			hashPositions.put("Item#", 0);
			hashPositions.put("Description", 0);
			hashPositions.put("ArabDescription", 2);
			hashPositions.put("UOM", 2);
			hashPositions.put("Transfer Qty", 2);
			hashPositions.put("Qty", 0);

			line(startln);
			headerinvprint(object, 2);
			boolean transferout = false;
			int position = 300;
			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (i == 1) {
					transferout = true;

				}
				if (jInnerData.length() > 0) {
					position = position + 60;
					switch (i) {
					case 0:
						outStream.write(BoldOn);
						outStream.write("       ".getBytes());
						outStream.write(UnderlineOn);
						printlines1("TRANSFER IN", 1, object, 1, args[0], 2);
						outStream.write(UnderlineOff);
						outStream.write(BoldOff);
						break;
					case 1:
						outStream.write(BoldOn);
						outStream.write("       ".getBytes());
						outStream.write(UnderlineOn);
						printlines1("TRANSFER OUT", 1, object, 1, args[0], 2);
						outStream.write(UnderlineOff);
						outStream.write(BoldOff);

						break;
					case 2:
						outStream.write(BoldOn);
						outStream.write("       ".getBytes());
						outStream.write(UnderlineOn);
						printlines1("DAMAGE TRANSFER OUT", 1, object, 1, args[0], 2);
						outStream.write(UnderlineOff);
						outStream.write(BoldOff);
						break;

					default:
						break;
					}

				}
				if (transferout) {
					if (inventoryvalueprint == 1) {
						hashValues.put("Transfer Qty", 13);
						hashValues.put("Net Qty", 13);
						hashValues.put("Value", 13);
						hashPositions.put("Net Qty", 1);
						hashPositions.put("Value", 2);
					} else {
						hashValues.put("Transfer Qty", 20);
						hashValues.put("Net Qty", 21);
						hashValues.put("Value", 0);
						hashPositions.put("Net Qty", 2);
						// hashPositions.put("Value", 2);
					}
				} else {
					if (inventoryvalueprint == 1) {
						hashValues.put("Transfer Qty", 13);
						hashValues.put("Net Qty", 13);
						hashValues.put("Value", 15);
						hashPositions.put("Net Qty", 1);
					} else {
						hashValues.put("Transfer Qty", 20);
						hashValues.put("Net Qty", 21);
						hashValues.put("Value", 0);
						hashPositions.put("Net Qty", 2);
					}

					hashPositions.put("Value", 2);
				}

				int MAXLEngth = 137;
				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / (headers.length() - 1);
				}

				String strheader = "", strHeaderBottom = "", strTotal = "";
				outStream.write(CompressOn);
				for (int j = 0; j < headers.length(); j++) {

					/*
					 * if(j==3){ strheader = strheader
					 * +printEmptySpace(hashValues.get("ArabDescription")); }
					 * else{
					 */

					strheader = strheader
							+ getAccurateText(
									j == 3 ? ""
											: (headers.getString(j).indexOf(" ") == -1) ? headers.getString(j)
													: headers.getString(j).substring(0,
															headers.getString(j).indexOf(" ")),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));
					/* } */
					strHeaderBottom = strHeaderBottom + getAccurateText(
							j == 3 ? ""
									: (headers.getString(j).indexOf(" ") == -1) ? ""
											: headers.getString(j)
													.substring(headers.getString(j).indexOf(" "),
															headers.getString(j).length())
													.trim(),
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(j).toString()) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					printlines1(strheader, 1, object, 1, args[0], 2);
					if (strHeaderBottom.length() > 0) {

						printlines1(strHeaderBottom, 1, object, 1, args[0], 2);

					}

					printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);

				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 0) {
							itemDescrion = (l + 1) + "";

						} else if (m == 3) {
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(m); // printing
																	// barcode
							} else {
								itemDescrion = "*" + jArr.getString(m) + "!";
							}

						}

						strData = strData + getAccurateText(itemDescrion,
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

						// if(m==3){
						// strData = strData + getAccurateText(m == 0 ? (l + 1)
						// + "" : jArr.getString(m),
						// hashValues.get(headers.getString(m).toString()) +
						// MAXLEngth,
						// hashPositions.get(headers.getString(m).toString()));
						// }else{
						// strData = strData + getAccurateText(m == 0 ? (l + 1)
						// + "" : jArr.getString(m),
						// hashValues.get(headers.getString(m).toString()) +
						// MAXLEngth,
						// hashPositions.get(headers.getString(m).toString()));
						// }

					}
					printlines1(strData, 1, object, 1, args[0], 1);

				}
				if (jInnerData.length() > 0) {

					printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);
					printlines1(strTotal, 1, object, 1, args[0], 2);

				}
				outStream.write(CompressOff);
			}
			// printlines1(getAccurateText("", 80, 1), 2, object, 1, args[0],
			// 2);
			// outStream.write(NewLine);
			if (inventoryvalueprint == 1) {
				outStream.write(BoldOn);
				printlines1(
						(getAccurateText("Net Value : ", 50, 2) + getAccurateText(object.getString("netvalue"), 12, 2)),
						2, object, 1, args[0], 2);
				outStream.write(BoldOff);
			}
			printlines1((getAccurateText("FROM SALESMAN_____________", 26, 1)
					+ getAccurateText("TO SALESMAN____________", 26, 1)
					+ getAccurateText("SUPERVISOR____________", 26, 1)), 2, object, 1, args[0], 2);
			printlines1(getAccurateText(object.getString("printstatus"), 80, 1), 2, object, 2, args[0], 2);

			// s1.insert(0,
			// "! 0 "+position+" "+position+" "+(position+10)+" 1\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

		// return String.valueOf(s1);
	}

	void parseLoadSummary2Response(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int inventoryvalueprint = Integer.valueOf(object.getString("inventoryvalueprint"));
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Sl#", 4);

			if (printitemcode == 0) {
				hashValues.put("Item#", 0);
				if(printLanguage.equals("2")){
					hashValues.put("Description", 48);
					hashValues.put("ArabDescription", 36);
				}else if(printLanguage.equals("1")){
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 84);
				}else{
					hashValues.put("Description", 84);
					hashValues.put("ArabDescription", 0);
				}
//				hashValues.put("Description", 48);
//				hashValues.put("ArabDescription", 36);
			} else {
				hashValues.put("Item#", 12);
				if(printLanguage.equals("2")){
					hashValues.put("Description", 36);
					hashValues.put("ArabDescription", 36);
				}else if(printLanguage.equals("1")){
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 72);
				}else{
					hashValues.put("Description", 72);
					hashValues.put("ArabDescription", 0);
				}
//				hashValues.put("Description", 40);
//				hashValues.put("ArabDescription", 36);
			}

			hashValues.put(barcode, 36);
			hashValues.put("UOM", 6);
			hashValues.put("Van Qty", 10);
			hashValues.put("Load Qty", 10);

			if (inventoryvalueprint == 0) {
				hashValues.put("Net Qty", 23);
				hashValues.put("VALUE", 0);
			} else {
				hashValues.put("Net Qty", 10);
				hashValues.put("VALUE", 13);
			}

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Sl#", 0);
			hashPositions.put("Item#", 0);
			hashPositions.put("Description", 0);
			hashPositions.put("ArabDescription", 2);
			hashPositions.put("UOM", 2);
			hashPositions.put("Van Qty", 2);
			hashPositions.put("Load Qty", 2);
			hashPositions.put("Net Qty", 2);
			hashPositions.put("VALUE", 2);
			hashPositions.put(barcode, 2);
			
			
			line(startln);
			headerinvprint(object, 1);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}
			for (int i = 0; i < headers.length(); i++) {

				// if(i==3){
				// strheader = strheader
				// +printEmptySpace(hashValues.get("ArabDescription"));
				// }
				//
				// else{
				String HeaderVal = "";

				HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(i));
				if (i == 3 && object.getString("printbarcode").equals("1")) {
					HeaderVal = barcode;
				}
				strheader = strheader + getAccurateText(
						(i==3 && object.getString("printbarcode").equals("0")) ? "DESCRIPTION"
								: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
										: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
						hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

				strHeaderBottom = strHeaderBottom
						+ getAccurateText(
								(i==3 && object.getString("printbarcode").equals("0")) ? ""
										: (HeaderVal.indexOf(" ") == -1) ? ""
												: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
														.trim(),
								hashValues.get(HeaderVal.toString()) + MAXLEngth,
								hashPositions.get(HeaderVal.toString()));

			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 1);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 1);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);
			outStream.write(CompressOff);
			JSONArray jData = object.getJSONArray("data");
			int position = 310;
			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					//if (j != 5) {
						String itemDescrion = jArr.getString(j);
						if (j == 0) {
							itemDescrion = (i + 1) + "";

						} else if (j == 3) {
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(j); // printing
																	// barcode
							} else {
								itemDescrion = "*" + jArr.getString(j) + "!";
							}

						}

						strData = strData + getAccurateText(itemDescrion,
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					//}
				}

				// position = position + 30;
				// s1.append(String.format(strFormat, position, strData) +
				// "\n");
				// count++;
				outStream.write(CompressOn);
				printlines1(strData, 1, object, 1, args[0], 1);
				outStream.write(CompressOff);
			}
			outStream.write(CompressOn);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);

			JSONArray jTotal = object.getJSONArray("TOTAL");
			for (int i = 0; i < jTotal.length(); i++) {
				JSONObject jTOBject = jTotal.getJSONObject(0);
				String strTotal = "";
				for (int j = 0; j < headers.length(); j++) {
					//if (j != 5) {
						if (jTOBject.has(headers.getString(j))) {
							strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(j).toString()),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));
						} else {

							strTotal = strTotal
									+ getAccurateText(headers.getString(j).equals("Description") ? "TOTAL" : "",
											hashValues.get(headers.getString(j)) + MAXLEngth, 1);
						}
					//}
				}

				printlines1(strTotal, 1, object, 1, args[0], 1);

			}
			outStream.write(CompressOff);
			printlines1(getAccurateText("", 80, 1), 1, object, 1, args[0], 1);
			outStream.write(BoldOn);

			if (inventoryvalueprint != 0) {
				printlines1(
						(getAccurateText("Net Value : ", 50, 2) + getAccurateText(
								object.getString("netvalue"), 12, 2)), 1, object,
						1, args[0], 2);
//				printlines1((getAccurateText("Load Value : ", 50, 2)
//						+ getAccurateText(object.getString("LoadValue"), 12, 2)), 1, object, 1, args[0], 2);
			}

			outStream.write(BoldOff);
			printlines1(getAccurateText("", 80, 1), 2, object, 1, args[0], 1);
			printlines1((getAccurateText("STORE KEEPER____________", 40, 1)
					+ getAccurateText("SALESMAN___________", 40, 1)), 2, object, 1, args[0], 1);
			printlines1(getAccurateText(object.getString("printstatus"), 80, 1), 2, object, 2, args[0], 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}

	// ------------company status report
	void printCreditSummaryReport(final JSONObject object, final String... args) {
		try {

			hashValues = new HashMap<String, Integer>();
			hashValues.put("Customer#", 20);
			hashValues.put("Customer Name", 57);
			hashValues.put("Opening Balance", 15);
			hashValues.put("Sales Amount", 15);
			hashValues.put("Collection Amount", 15);
			hashValues.put("Current Balance", 15);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Customer#", 0);
			hashPositions.put("Customer Name", 0);
			hashPositions.put("Opening Balance", 2);
			hashPositions.put("Sales Amount", 2);
			hashPositions.put("Collection Amount", 2);
			hashPositions.put("Current Balance", 2);

			line(startln);
			// printlines1(printSeprator(), 1, object, 1, args[0], 6);
			// headerinvprint(object, 6);
			headervanstockprint(object, 6);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}
			for (int i = 0; i < headers.length(); i++) {

				strheader = strheader + getAccurateText(
						(headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
								: headers.getString(i).substring(0, headers.getString(i).indexOf(" ")),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));
				strHeaderBottom = strHeaderBottom + getAccurateText(
						(headers.getString(i).indexOf(" ") == -1) ? ""
								: headers.getString(i).substring(headers.getString(i).indexOf(" "),
										headers.getString(i).length()),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));

			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 6);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 6);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 6);
			outStream.write(CompressOff);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					strData = strData + getAccurateText(jArr.getString(j),
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));
				}

				// position = position + 30;
				// s1.append(String.format(strFormat, position, strData) +
				// "\n");
				outStream.write(CompressOn);
				printlines1(strData, 1, object, 1, args[0], 6);
				outStream.write(CompressOff);
			}
			outStream.write(CompressOn);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 6);
			outStream.write(CompressOff);

			JSONArray jTotal = object.getJSONArray("TOTAL");
			for (int i = 0; i < jTotal.length(); i++) {
				JSONObject jTOBject = jTotal.getJSONObject(0);
				String strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					if (jTOBject.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}
				}
				outStream.write(CompressOn);
				printlines1(strTotal, 1, object, 1, args[0], 6);
				outStream.write(CompressOff);
			}
			printlines1("", 1, object, 2, args[0], 6);
			// outStream.write(BoldOn);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}

	// --------
	void printCreditTempSummaryReport(final JSONObject object, final String... args) {
		try {

			hashValues = new HashMap<String, Integer>();
			hashValues.put("Customer#", 20);
			hashValues.put("Customer Name", 57);
			hashValues.put("Opening Balance", 15);
			hashValues.put("Sales Amount", 15);
			hashValues.put("Collection Amount", 15);
			hashValues.put("Current Balance", 15);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Customer#", 0);
			hashPositions.put("Customer Name", 0);
			hashPositions.put("Opening Balance", 2);
			hashPositions.put("Sales Amount", 2);
			hashPositions.put("Collection Amount", 2);
			hashPositions.put("Current Balance", 2);

			line(startln);
			// printlines1(printSeprator(), 1, object, 1, args[0], 6);
			// headerinvprint(object, 6);
			headervanstockprint(object, 25);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}
			for (int i = 0; i < headers.length(); i++) {

				strheader = strheader + getAccurateText(
						(headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
								: headers.getString(i).substring(0, headers.getString(i).indexOf(" ")),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));
				strHeaderBottom = strHeaderBottom + getAccurateText(
						(headers.getString(i).indexOf(" ") == -1) ? ""
								: headers.getString(i).substring(headers.getString(i).indexOf(" "),
										headers.getString(i).length()),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));

			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 25);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 25);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 25);
			outStream.write(CompressOff);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					strData = strData + getAccurateText(jArr.getString(j),
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));
				}

				// position = position + 30;
				// s1.append(String.format(strFormat, position, strData) +
				// "\n");
				outStream.write(CompressOn);
				printlines1(strData, 1, object, 1, args[0], 25);
				outStream.write(CompressOff);
			}
			outStream.write(CompressOn);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 25);
			outStream.write(CompressOff);

			JSONArray jTotal = object.getJSONArray("TOTAL");
			for (int i = 0; i < jTotal.length(); i++) {
				JSONObject jTOBject = jTotal.getJSONObject(0);
				String strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					if (jTOBject.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}
				}
				outStream.write(CompressOn);
				printlines1(strTotal, 1, object, 1, args[0], 25);
				outStream.write(CompressOff);
			}
			printlines1("", 1, object, 2, args[0], 25);
			// outStream.write(BoldOn);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}

	// -------
	// --------
	void parseLoadSummaryResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();

		try {
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int loadoutadjustments = Integer.valueOf(object.getString("loadoutadjustments"));
			int inventoryvalueprint = Integer.valueOf(object.getString("inventoryvalueprint"));
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Sl#", 4);

			if (printitemcode == 0) {
				hashValues.put("Item#", 0);
				if(printLanguage.equals("2")){
					hashValues.put("Description", 46);
					hashValues.put("ArabDescription", 35);
				}else if(printLanguage.equals("1")){
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 81);
				}else{
					hashValues.put("Description", 81);
					hashValues.put("ArabDescription", 0);
				}
				
//				hashValues.put("Description", 46);
//				hashValues.put("ArabDescription", 35);
			} else {
				hashValues.put("Item#", 11);
				
				if(printLanguage.equals("2")){
					hashValues.put("Description", 35);
					hashValues.put("ArabDescription", 35);
				}else if(printLanguage.equals("1")){
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 70);
				}else{
					hashValues.put("Description", 70);
					hashValues.put("ArabDescription", 0);
				}
//				hashValues.put("Description", 35);
//				hashValues.put("ArabDescription", 35);
			}

			hashValues.put(barcode, 35);
			hashValues.put("UOM", 4);
			hashValues.put("Open Qty", 9);
			hashValues.put("Load Qty", 9);
			if (loadoutadjustments == 0) {
				hashValues.put("Adjust Qty", 0);
				hashValues.put("Net Qty", 14);
				hashValues.put("VALUE", 16);
			} else {
				hashValues.put("Adjust Qty", 10);
				hashValues.put("Net Qty", 9);
				hashValues.put("VALUE", 11);
			}

			if (inventoryvalueprint == 0) {
				hashValues.put("Net Qty", hashValues.get("Net Qty") + hashValues.get("VALUE"));
				hashValues.put("VALUE", 0);
			}
			// hashValues.put("Description", 40);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Sl#", 0);
			hashPositions.put("Item#", 0);
			hashPositions.put("Description", 0);
			hashPositions.put("ArabDescription", 2);
			hashPositions.put("UOM", 2);
			hashPositions.put("Open Qty", 2);
			hashPositions.put("Load Qty", 2);
			hashPositions.put("Adjust Qty", 1);
			hashPositions.put("Net Qty", 2);
			hashPositions.put("VALUE", 2);
			hashPositions.put(barcode, 2);
			// hashPositions.put("Description", 0);
			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);

			// headercommmanprint(object);
			headerinvprint(object, 1);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}
			for (int i = 0; i < headers.length(); i++) {

				String HeaderVal = "";

				HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(i));
				if (i ==3 && object.getString("printbarcode").equals("1")) {
					HeaderVal = barcode;
				}
				
				strheader = strheader + getAccurateText(
						(i==3 && object.getString("printbarcode").equals("0")) ? "DESCRIPTION"
								: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
										: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
						hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

				strHeaderBottom = strHeaderBottom
						+ getAccurateText(
								(i==3 && object.getString("printbarcode").equals("0")) ? ""
										: (HeaderVal.indexOf(" ") == -1) ? ""
												: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
														.trim(),
								hashValues.get(HeaderVal.toString()) + MAXLEngth,
								hashPositions.get(HeaderVal.toString()));


			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 1);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 1);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);
			outStream.write(CompressOff);
			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {

					String itemDescrion = jArr.getString(j);
					if (j == 0) {
						itemDescrion = (i + 1) + "";

					} else if (j == 3) {

						if (object.getString("printbarcode").equals("1")) {
							itemDescrion = jArr.getString(j); // printing
																// barcode
						} else {
							itemDescrion = "*" + jArr.getString(j) + "!";
						}

					}

					strData = strData
							+ getAccurateText(itemDescrion, hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));
				}
				outStream.write(CompressOn);
				// count++;
				printlines1(strData, 1, object, 1, args[0], 1);
				outStream.write(CompressOff);
			}

			outStream.write(CompressOn);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);
			JSONArray jTotal = object.getJSONArray("TOTAL");
			for (int i = 0; i < jTotal.length(); i++) {
				JSONObject jTOBject = jTotal.getJSONObject(0);
				String strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					if (jTOBject.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}
				}
				printlines1(strTotal, 1, object, 1, args[0], 1);

			}
			outStream.write(CompressOff);
			printlines1(" ", 2, object, 1, args[0], 1);
			outStream.write(BoldOn);
			

			if (inventoryvalueprint != 0) {
				 printlines1(
						 (getAccurateText("Opening Value : ", 50, 2) + getAccurateText(
						 object.getString("OpenValue"), 12, 2)), 1, object,
						 1, args[0], 2);
				printlines1((getAccurateText("Load Value : ", 50, 2)
						+ getAccurateText(object.getString("LoadValue"), 12, 2)), 1, object, 1, args[0], 2);
				 printlines1(
						 (getAccurateText("Net Value : ", 50, 2) + getAccurateText(
						 object.getString("netvalue"), 12, 2)), 1, object,
						 1, args[0], 2);
			}
			
			 
			outStream.write(NewLine);
			outStream.write(BoldOff);
			printlines1((getAccurateText("STORE KEEPER_____________", 40, 1)
					+ getAccurateText("SALESMAN____________", 40, 1)), 2, object, 1, args[0], 1);
			printlines1(" ", 1, object, 1, args[0], 1);
			printlines1(getAccurateText(object.getString("printstatus"), 80, 1), 2, object, 2, args[0], 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}

	//
	void parseUnloadResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			hashValues = new HashMap<String, Integer>();
			hashValues.put("ITEM#", 12);
			hashValues.put("DESCRIPTION", 42);
			hashValues.put("UPC", 7);
			hashValues.put("STALES OUT/PCS", 0);
			hashValues.put("STALES T.PCS", 0);
			hashValues.put("DAMAGE OUT/PCS", 0);
			hashValues.put("DAMAGE", 12);
			hashValues.put("OTHER OUT/PCS", 0);
			hashValues.put("OTHER T.PCS", 0);
			hashValues.put("VARIANCE", 12);
			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("ITEM#", 0);
			hashPositions.put("DESCRIPTION", 0);
			hashPositions.put("UPC", 1);
			hashPositions.put("STALES OUT/PCS", 2);
			hashPositions.put("STALES T.PCS", 2);
			hashPositions.put("DAMAGE OUT/PCS", 2);
			hashPositions.put("DAMAGE", 2);
			hashPositions.put("OTHER OUT/PCS", 2);
			hashPositions.put("OTHER T.PCS", 2);
			hashPositions.put("VARIANCE", 1);
			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);

			headerprint(object, 7);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";
			int MAXLEngth = 130;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}
			for (int i = 0; i < headers.length(); i++) {
				if (i != 3 || i != 4 || i != 5 || i != 7 || i != 8) {
					strheader = strheader + getAccurateText(
							(headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
									: headers.getString(i).substring(0, headers.getString(i).indexOf(" ")),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));
					strHeaderBottom = strHeaderBottom + getAccurateText(
							(headers.getString(i).indexOf(" ") == -1) ? ""
									: headers.getString(i).substring(headers.getString(i).indexOf(" "),
											headers.getString(i).length()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));
				}

			}
			outStream.write(CompressOn);
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 7, 7);
			printlines2(strheader, 1, object, 1, args[0], 7, 7);
			printlines2(strHeaderBottom, 1, object, 1, args[0], 7, 7);
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 7, 7);
			outStream.write(CompressOff);

			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					if (i != 3 || i != 4 || i != 5 || i != 7 || i != 8) {

						String itemDescrion = jArr.getString(j);
						if (j == 1) {
							if (object.getString("LANG").equals("en")) {
								itemDescrion = jArr.getString(j);
							} else {
								itemDescrion = "*" + jArr.getString(j) + "!";
							}
						}

						strData = strData + getAccurateText(itemDescrion,
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					}

				}

				outStream.write(CompressOn);
				printlines2(strData, 1, object, 1, args[0], 7, 7);
				outStream.write(CompressOff);
			}
			outStream.write(CompressOn);
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 7, 7);
			JSONArray jTotal = object.getJSONArray("TOTAL");
			for (int i = 0; i < jTotal.length(); i++) {
				JSONObject jTOBject = jTotal.getJSONObject(0);
				String strTotal = "";
				for (int j = 0; j < headers.length(); j++) {
					if (i != 3 || i != 4 || i != 5 || i != 7 || i != 8) {
						if (jTOBject.has(headers.getString(j))) {
							strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(j).toString()),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));
						} else {

							strTotal = strTotal
									+ getAccurateText(headers.getString(j).equals("Description") ? "TOTAL" : "",
											hashValues.get(headers.getString(j)) + MAXLEngth, 1);
						}
					}
				}

				printlines2(strTotal, 1, object, 1, args[0], 7, 7);

			}
			outStream.write(CompressOff);
			printlines2(" ", 2, object, 1, args[0], 7, 7);
			outStream.write(BoldOn);
			String totalAmt = "0";
			String varAmt = "0";
			// printlines2(
			// (getAccurateText("TOTAL EXPIRY VALUE", 60, 2) + getAccurateText(
			// object.has("TOTAL_EXPIRY_VALUE") ? object
			// .getString("TOTAL_EXPIRY_VALUE") : "0", 16,
			// 1)), 1, object, 1, args[0], 7, 7);
			printlines2(
					(getAccurateText("TOTAL DAMAGE VALUE", 60, 2) + getAccurateText(
							object.has("TOTAL_DAMAGE_VALUE") ? object.getString("TOTAL_DAMAGE_VALUE") : "0", 16, 1)),
					1, object, 1, args[0], 7, 7);
			// printlines2(
			// (getAccurateText("TOTAL OTHER VALUE", 60, 2) + getAccurateText(
			// object.has("TOTAL_OTHER_VALUE") ? object
			// .getString("TOTAL_OTHER_VALUE") : "0", 16,
			// 1)), 1, object, 1, args[0], 7, 7);
			// printlines2(
			// (getAccurateText("UNLOADED STALES VARIANCE", 60, 2) +
			// getAccurateText(
			// object.has("TOTAL_STALES_VAR") ? object
			// .getString("TOTAL_STALES_VAR") : "0", 16, 1)),
			// 1, object, 1, args[0], 7, 7);
			printlines2(
					(getAccurateText("UNLOADED DAMAGE VARIANCE", 60, 2) + getAccurateText(
							object.has("damagevariance") ? object.getString("damagevariance") : "0", 16, 1)),
					1, object, 1, args[0], 7, 7);

			outStream.write(BoldOff);
			printlines2(" ", 2, object, 1, args[0], 7, 7);
			printlines2(getAccurateText("SALESMAN______________", 80, 1), 1, object, 2, args[0], 7, 7);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}

	//
	// ----------Start LoadRequest By VB 9/1/2104
	void printLoadRequestReport(final JSONObject object, final String... args) {

		try {
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int inventoryvalueprint = Integer.valueOf(object.getString("inventoryvalueprint"));
			String cases = object.has("cases") ? object.getString("cases") : "Case";
			String pcs = object.has("pcs") ? object.getString("pcs") : "Unit";
			int CaseEnabled = object.has("CaseEnabled") ? Integer.parseInt(object.getString("CaseEnabled")) : 1;
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			String printLanguage = object.has("printlanguageflag") ? object.getString("printlanguageflag") : "2";
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Sl#", 4);
			if (printitemcode == 0) {
				hashValues.put("Item#", 0);
				if (printLanguage.equals("2")) {
					hashValues.put("Description", 47);
					hashValues.put("ArabDescription", 33);
					hashValues.put(barcode, 33);
				} else if (printLanguage.equals("1")) {
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 80);
					hashValues.put(barcode, 80);
				} else {
					hashValues.put("Description", 80);
					hashValues.put("ArabDescription", 0);
					hashValues.put(barcode, 0);
				}
				
			} else {
				hashValues.put("Item#", 10);
				if (printLanguage.equals("2")) {
					hashValues.put("Description", 37);
					hashValues.put("ArabDescription", 33);
					hashValues.put(barcode, 33);
				} else if (printLanguage.equals("1")) {
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 70);
					hashValues.put(barcode, 70);
				} else {
					hashValues.put("Description", 70);
					hashValues.put("ArabDescription", 0);
					hashValues.put(barcode, 0);
				}
				
			}

			
			hashValues.put("UOM", 5);
			if (inventoryvalueprint == 1) {
				if(CaseEnabled==1){
					hashValues.put(cases + " Price", 8);
					hashValues.put(pcs + " Price", 10);
				}else{
					hashValues.put(cases + " Price", 0);
					hashValues.put(pcs + " Price", 18);
				}
				
				hashValues.put("Avail. Qty", 10);
				hashValues.put("Remarks", 10);
				hashValues.put("Request Qty", 10);

			} else {
				hashValues.put(cases + " Price", 0);
				hashValues.put(pcs + " Price", 0);
				hashValues.put("Avail. Qty", 10);
				hashValues.put("Remarks", 10);
				hashValues.put("Request Qty", 10);
				hashValues.put("Description", hashValues.get("Description") + 18);

			}

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Sl#", 0);
			hashPositions.put("Item#", 0);
			hashPositions.put("Description", 0);
			hashPositions.put("ArabDescription", 2);
			hashPositions.put("UOM", 2);
			hashPositions.put(cases + " Price", 2);
			hashPositions.put(pcs + " Price", 2);
			hashPositions.put("Avail. Qty", 2);
			hashPositions.put("Remarks", 2);
			hashPositions.put("Request Qty", 2);
			hashPositions.put(barcode, 2);
			line(startln);
			// headerRequestprint(object);
			headerinvprint(object, 5);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";

			String strTotal = "";
			JSONArray jTotal = object.getJSONArray("TOTAL");
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			JSONObject jTOBject = jTotal.getJSONObject(0);
			for (int i = 0; i < headers.length(); i++) {

				try {

					// if(i==3){
					// strheader = strheader
					// +printEmptySpace(hashValues.get("ArabDescription"));
					// }else{
					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(i));
					if (i == 3 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					strheader = strheader + getAccurateText(
							(i==3 && object.getString("printbarcode").equals("0")) ? "DESCRIPTION"
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(i==3 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));

					if (jTOBject.has(headers.getString(i))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(i).toString()),
								hashValues.get(headers.getString(i).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(i).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(i).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(i)) + MAXLEngth, 1);
					}
				} catch (Exception e) {

				}
			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 5);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 5);
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);

			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";

				for (int j = 0; j < jArr.length(); j++) {

					String itemDescrion = jArr.getString(j);
					if (j == 0) {
						itemDescrion = (i + 1) + "";

					} else if (j == 2) {
						itemDescrion = jArr.getString(j);
					} else if (j == 3) {
						if (object.getString("printbarcode").equals("1")) {
							itemDescrion = jArr.getString(j); // printing
																// barcode
						} else {
							itemDescrion = "*" + jArr.getString(j) + "!";
						}
					}

					strData = strData
							+ getAccurateText(itemDescrion, hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));
				}
				
				
				outStream.write(UnderlineOn);
				printlines1(strData, 1, object, 1, args[0], 5);
				outStream.write(UnderlineOff);

			}
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
			printlines2(strTotal, 1, object, 1, args[0], 1, 1);

			outStream.write(CompressOff);

			printlines1(getAccurateText("", 80, 1), 2, object, 1, args[0], 5);
			if (inventoryvalueprint == 1) {
				outStream.write(BoldOn);
				printlines1(
						(getAccurateText("Net Value : ", 50, 2) + getAccurateText(object.getString("netvalue"), 12, 2)),
						3, object, 1, args[0], 5);
			}
			outStream.write(BoldOff);
			printlines1((getAccurateText("STORE KEEPER____________", 40, 1)
					+ getAccurateText("SALESMAN___________", 40, 1)), 2, object, 1, args[0], 5);
			printlines1(getAccurateText(object.getString("printstatus"), 80, 1), 2, object, 2, args[0], 5);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}

	// ---------End Vanstock Report
	// ----------Start Vanstock By VB 9/1/2104
	void printVanStockReport(final JSONObject object, final String... args) {

		try {

			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int inventoryvalueprint = Integer.valueOf(object.getString("inventoryvalueprint"));
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			String printLanguage = object.has("printlanguageflag") ? object.getString("printlanguageflag") : "2";
			
			hashValues = new HashMap<String, Integer>();

			if (printitemcode == 0) {
				hashValues.put("Item#", 0);
				
				if (printLanguage.equals("2")) {
					hashValues.put("Description", 44);
					hashValues.put("ArabDescription", 30);
					hashValues.put(barcode, 30);
				} else if (printLanguage.equals("1")) {
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 74);
					hashValues.put(barcode, 74);
				} else {
					hashValues.put("Description", 74);
					hashValues.put("ArabDescription", 0);
					hashValues.put(barcode, 0);
				}
			} else {
				hashValues.put("Item#", 14);
				
				if (printLanguage.equals("2")) {
					hashValues.put("Description", 30);
					hashValues.put("ArabDescription", 30);
					hashValues.put(barcode, 30);
				} else if (printLanguage.equals("1")) {
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 60);
					hashValues.put(barcode, 60);
				} else {
					hashValues.put("Description", 60);
					hashValues.put("ArabDescription", 0);
					hashValues.put(barcode, 0);
				}
				
			}

			/*hashValues.put("ArabDescription", 30);
			hashValues.put(barcode, 30);*/
			hashValues.put("Loaded Qty", 8);
			hashValues.put("Transfer Qty", 10);

			if (inventoryvalueprint == 0) {
				hashValues.put("Sale Qty", 15);
				hashValues.put("Return Qty", 15);
				hashValues.put("Truck Stock", 15);
				hashValues.put("Total", 0);
			} else {
				hashValues.put("Truck Stock", 10);
				hashValues.put("Sale Qty", 10);
				hashValues.put("Return Qty", 10);
				hashValues.put("Total", 15);
			}

			// hashValues.put("Description", 37);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Item#", 0);
			hashPositions.put("Description", 0);
			hashPositions.put("ArabDescription", 2);
			hashPositions.put("Loaded Qty", 2);
			hashPositions.put("Transfer Qty", 2);
			hashPositions.put("Sale Qty", 2);
			hashPositions.put("Return Qty", 2);
			hashPositions.put(barcode, 2);
			hashPositions.put("Truck Stock", 2);
			hashPositions.put("Total", 1);
			// hashPositions.put("Description", 0);
			// ---------Start
			// printconnect(args[0]);
			// ----------End

			line(startln);
			headervanstockprint(object, 4);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";

			String strTotal = "";
			JSONArray jTotal = object.getJSONArray("TOTAL");
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			JSONObject jTOBject = jTotal.getJSONObject(0);
			for (int i = 0; i < headers.length(); i++) {

				try {
					String HeaderVal = "";
					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(i));
					if (i == 2 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					
					// if(i==2){
					// strheader = strheader + " ";
					// }
					// else{
					strheader = strheader + getAccurateText(
							(i==2 && object.getString("printbarcode").equals("0")) ? "DESCRIPTION"
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(i==2 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));
					if (jTOBject.has(headers.getString(i))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(i).toString()),
								hashValues.get(headers.getString(i).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(i).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(i).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(i)) + MAXLEngth, 1);
					}
				} catch (Exception e) {

				}
			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 4);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 4);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);

			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {

					String itemDescrion = jArr.getString(j);
					if (j == 2) {
						if (object.getString("printbarcode").equals("1")) {
							itemDescrion = jArr.getString(j); // printing
																// barcode
						} else {
							itemDescrion = "*" + jArr.getString(j) + "!";
						}

					}

					strData = strData
							+ getAccurateText(itemDescrion, hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));
				}
				// count++;
				printlines1(strData, 1, object, 1, args[0], 4);

			}
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);
			printlines1(strTotal, 1, object, 1, args[0], 4);

			printlines1(printSepratorcomp(), 1, object, 2, args[0], 4);
			outStream.write(CompressOff);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}

	void parseUnScheduledCustomerResponse(final JSONObject object, final String... args) {

		try {

			hashValues = new HashMap<String, Integer>();
			hashValues.put("Customer Code", 10);
			hashValues.put("Customer Name", 45);
			hashValues.put("Customer Address", 25);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Customer Code", 0);
			hashPositions.put("Customer Name", 0);
			hashPositions.put("Customer Address", 2);

			line(startln);
			headervanstockprint(object, 11);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";

			int MAXLEngth = 80;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			for (int i = 0; i < headers.length(); i++) {

				try {
					strheader = strheader + getAccurateText(
							(headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
									: headers.getString(i).substring(0, headers.getString(i).indexOf(" ")),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));

					strHeaderBottom = strHeaderBottom + getAccurateText(
							(headers.getString(i).indexOf(" ") == -1) ? ""
									: headers.getString(i).substring(headers.getString(i).indexOf(" "),
											headers.getString(i).length()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));

				} catch (Exception e) {

				}
			}

			printlines1(strheader, 1, object, 1, args[0], 10);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 10);
			printlines1(printSeprator(), 1, object, 1, args[0], 10);

			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {

					String itemDescrion = jArr.getString(j);

					if (j == 1) {
						if (object.getString("LANG").equals("en")) {
							itemDescrion = jArr.getString(j);
						} else {
							itemDescrion = "*" + jArr.getString(j) + "!";
						}

					}

					strData = strData
							+ getAccurateText(itemDescrion, hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

				}

				printlines1(strData, 1, object, 1, args[0], 10);

			}
			printlines1(printSeprator(), 1, object, 1, args[0], 10);

			outStream.write(BoldOn);

			outStream.write(BoldOff);
			outStream.write(NewLine);
			outStream.write(NewLine);
			outStream.write(NewLine);
			printlines2(getAccurateText("SALESMAN_______________*" + ArabicTEXT.Salesman + "!", 80, 1), 2, object, 2,
					args[0], 1, 10);
			outStream.write(NewLine);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}

	void printItemSalesSummaryReport(final JSONObject object, final String... args) {

		try {
			int inventoryvalueprint = Integer.valueOf(object.getString("inventoryvalueprint"));
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Item#", 12);

			hashValues.put("Sale Qty", 10);
			hashValues.put("Return Qty", 10);
			hashValues.put("Damage Qty", 10);
			hashValues.put("Free Qty", 10);
			hashValues.put("Total Qty", 10);
			if (inventoryvalueprint == 1) {
				hashValues.put("Total", 10);
				if(printLanguage.equals("2")){
					hashValues.put("Description", 35);
					hashValues.put("ARBDESCRIPTION", 29);
				}else if(printLanguage.equals("1")){
					hashValues.put("Description", 0);
					hashValues.put("ARBDESCRIPTION", 64);
				}else{
					hashValues.put("Description", 64);
					hashValues.put("ARBDESCRIPTION", 0);
				}
			} else {
				hashValues.put("Total", 0);
				if(printLanguage.equals("2")){
					hashValues.put("Description", 45);
					hashValues.put("ARBDESCRIPTION", 29);
				}else if(printLanguage.equals("1")){
					hashValues.put("Description", 0);
					hashValues.put("ARBDESCRIPTION", 74);
				}else{
					hashValues.put("Description", 74);
					hashValues.put("ARBDESCRIPTION", 0);
				}
			}

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Item#", 0);
			hashPositions.put("Description", 0);
			hashPositions.put("ARBDESCRIPTION", 2);
			hashPositions.put("Sale Qty", 1);
			hashPositions.put("Return Qty", 1);
			hashPositions.put("Damage Qty", 1);
			hashPositions.put("Free Qty", 1);
			hashPositions.put("Total Qty", 1);
			hashPositions.put("Total", 2);
			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);
			headervanstockprint(object, 10);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";

			String strTotal = "";
			JSONArray jTotal = object.getJSONArray("TOTAL");
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			JSONObject jTOBject = jTotal.getJSONObject(0);
			for (int i = 0; i < headers.length(); i++) {

				try {
					strheader = strheader
							+ getAccurateText(
									i == 2 ? ""
											: (headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
													: headers.getString(i).substring(0,
															headers.getString(i).indexOf(" ")),
									hashValues.get(headers.getString(i).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(i).toString()));

					strHeaderBottom = strHeaderBottom + getAccurateText(
							i == 2 ? ""
									: (headers.getString(i).indexOf(" ") == -1) ? ""
											: headers.getString(i).substring(headers.getString(i).indexOf(" "),
													headers.getString(i).length()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));
					if (jTOBject.has(headers.getString(i))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(i).toString()),
								hashValues.get(headers.getString(i).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(i).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(i).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(i)) + MAXLEngth, 1);
					}
				} catch (Exception e) {

				}
			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 10);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 10);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 10);

			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {

					String itemDescrion = jArr.getString(j);

					if (j == 2) {
						itemDescrion = "";
						if (object.getString("printbarcode").equals("1")) {
							itemDescrion = jArr.getString(j);
						} else {

							try {
								itemDescrion = "*" + jArr.getString(j) + "!";
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}

					strData = strData
							+ getAccurateText(itemDescrion, hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

				}

				printlines1(strData, 1, object, 1, args[0], 10);

			}
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 10);
			printlines1(strTotal, 1, object, 1, args[0], 4);
			printlines1(printSepratorcomp(), 3, object, 1, args[0], 10);
			outStream.write(CompressOff);
			outStream.write(BoldOn);
			if (object.has("printlanguageflag") && !object.getString("printlanguageflag").equals("2")) {
				if (inventoryvalueprint == 1) {
					printlines2((getAccurateText("Total Amount", 20, 0) + getAccurateText(" : ", 3, 0)
							+ getAccurateText(object.getString("totalamount"), 12, 0) + getAccurateText(" : ", 3, 0)
							+ "*" + getAccurateText(ArabicTEXT.TOTAL, 15, 2) + "!"), 3, object, 1, args[0], 1, 10);
				}
				outStream.write(BoldOff);
				printlines2(getAccurateText("SALESMAN_______________*" + ArabicTEXT.Salesman + "!", 80, 1), 2, object,
						2, args[0], 1, 10);
			} else {
				if (inventoryvalueprint == 1) {
					printlines2(
							getAccurateText("Total Amount", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("totalamount"), 12, 0),
							3, object, 1, args[0], 1, 10);
				}
				outStream.write(BoldOff);
				printlines2(getAccurateText("SALESMAN_______________", 80, 1), 2, object, 2, args[0], 1, 10);

			}

			outStream.write(NewLine);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}
	void printItemPriceSummaryReport(final JSONObject object, final String... args) {

		try {
			
			hashValues = new HashMap<String, Integer>();
			hashValues.put("ITEM#", 17);
			hashValues.put("DESCRIPTION", 45);
			hashValues.put("ARBDESCRIPTION", 45);
			hashValues.put("CASH PRICE", 15);
			hashValues.put("CREDIT PRICE", 15);
			
			
			
			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("ITEM#", 0);
			hashPositions.put("DESCRIPTION", 0);
			hashPositions.put("ARBDESCRIPTION", 0);
			hashPositions.put("CASH PRICE", 1);
			hashPositions.put("CREDIT PRICE", 1);
			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);
			headervanstockprint(object, 27);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";

			String strTotal = "";
			JSONArray jTotal = object.getJSONArray("TOTAL");
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			JSONObject jTOBject = jTotal.getJSONObject(0);
			for (int i = 0; i < headers.length(); i++) {

				try {
					strheader = strheader
							+ getAccurateText(
									i == 2 ? "Barcode"
											: (headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
													: headers.getString(i).substring(0,
															headers.getString(i).indexOf(" ")),
									hashValues.get(headers.getString(i).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(i).toString()));

					strHeaderBottom = strHeaderBottom + getAccurateText(
							i == 2 ? ""
									: (headers.getString(i).indexOf(" ") == -1) ? ""
											: headers.getString(i).substring(headers.getString(i).indexOf(" "),
													headers.getString(i).length()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));
					if (jTOBject.has(headers.getString(i))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(i).toString()),
								hashValues.get(headers.getString(i).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(i).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(i).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(i)) + MAXLEngth, 1);
					}
				} catch (Exception e) {

				}
			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 27);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 27);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 27);

			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {

					String itemDescrion = jArr.getString(j);

					if (j == 2) {
						itemDescrion = "";
						if (object.getString("printbarcode").equals("1")) {
							itemDescrion = jArr.getString(j);
						} else {

							try {
								itemDescrion = "*" + jArr.getString(j) + "!";
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}

					strData = strData
							+ getAccurateText(itemDescrion, hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

				}

				printlines1(strData, 1, object, 1, args[0], 27);

			}
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 27);
			printlines1(strTotal, 1, object, 1, args[0], 27);
			printlines1(printSepratorcomp(), 3, object, 1, args[0], 27);
			outStream.write(CompressOff);
			outStream.write(BoldOn);
			if (object.has("printlanguageflag") && !object.getString("printlanguageflag").equals("2")) {
				
				outStream.write(BoldOff);
				printlines2(getAccurateText("SALESMAN_______________*" + ArabicTEXT.Salesman + "!", 80, 1), 2, object,
						2, args[0], 1, 27);
			} else {
				
				outStream.write(BoldOff);
				printlines2(getAccurateText("SALESMAN_______________", 80, 1), 2, object, 2, args[0], 1, 27);

			}

			outStream.write(NewLine);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return String.valueOf(s1);
	}
	void printMiniSalesReport(JSONObject object, String... args) {
		StringBuffer s1 = new StringBuffer();
		int printoultlet = 0;
		try {
			String cases = object.has("cases") ? object.getString("cases") : "CASE";
			String pcs = object.has("pcs") ? object.getString("pcs") : "PCS";
			String qty = object.has("qty") ? object.getString("qty") : "QTY CAS/PCS";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));

			if (object.getString("printoutletitemcode").length() > 0) {
				printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
			} else {
				printoultlet = 0;
			}

			hashValues = new HashMap<String, Integer>();
			hashValues.put("SL#", 4);

			if (printoultlet == 1) {
				hashValues.put("OUTLET CODE", 15);

				if (printitemcode == 0) {
					hashValues.put("ITEM#", 0);
					
					if(printLanguage.equals("2")){
						hashValues.put("DESCRIPTION", 47);
						hashValues.put("ARBDESCRIPTION", 40);
					}else if(printLanguage.equals("1")){
						hashValues.put("DESCRIPTION", 0);
						hashValues.put("ARBDESCRIPTION", 87);
					}else{
						hashValues.put("DESCRIPTION", 87);
						hashValues.put("ARBDESCRIPTION", 0);
					}
					
					
				} else {
					hashValues.put("ITEM#", 11);
					if(printLanguage.equals("2")){
						hashValues.put("DESCRIPTION", 36);
						hashValues.put("ARBDESCRIPTION", 40);
						
					}else if(printLanguage.equals("1")){
						hashValues.put("DESCRIPTION", 0);
						hashValues.put("ARBDESCRIPTION", 76);
						
					}else{
						hashValues.put("DESCRIPTION", 76);
						hashValues.put("ARBDESCRIPTION", 0);
						
					}
					
				}

			} else {
				hashValues.put("OUTLET CODE", 0);

				if (printitemcode == 0) {
					hashValues.put("ITEM#", 0);
					
					if(printLanguage.equals("2")){
						hashValues.put("DESCRIPTION", 62);
						hashValues.put("ARBDESCRIPTION", 40);
						
					}else if(printLanguage.equals("1")){
						hashValues.put("DESCRIPTION", 0);
						hashValues.put("ARBDESCRIPTION", 102);
						
					}else{
						hashValues.put("DESCRIPTION", 102);
						hashValues.put("ARBDESCRIPTION", 0);
						
					}
				} else {
					hashValues.put("ITEM#", 11);
					
					if(printLanguage.equals("2")){
						hashValues.put("DESCRIPTION", 51);
						hashValues.put("ARBDESCRIPTION", 40);
						
					}else if(printLanguage.equals("1")){
						hashValues.put("DESCRIPTION", 0);
						hashValues.put("ARBDESCRIPTION", 91);
						
					}else{
						hashValues.put("DESCRIPTION", 91);
						hashValues.put("ARBDESCRIPTION", 0);
						
					}
				}

			}

			hashValues.put(barcode, 40);
			hashValues.put("UOM", 7);
			hashValues.put("TOT. " + pcs, 0);
			hashValues.put(qty, 8);
			hashValues.put(cases + " PRICE", 8);
			hashValues.put(pcs + " PRICE", 8);
			hashValues.put("DISCOUNT", 0);
			hashValues.put("AMOUNT", 0);
			hashValues.put("TAX", 0);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("SL#", 0);
			hashPositions.put("ITEM#", 0);
			hashPositions.put("OUTLET CODE", 0);
			hashPositions.put("DESCRIPTION", 0);
			hashPositions.put("ARBDESCRIPTION", 2);
			hashPositions.put(barcode, 2);
			hashPositions.put("UOM", 1);
			hashPositions.put("TOT. " + pcs, 1);
			hashPositions.put(qty, 1);
			hashPositions.put(cases + " PRICE", 2);
			hashPositions.put(pcs + " PRICE", 2);
			hashPositions.put("DISCOUNT", 2);
			hashPositions.put("AMOUNT", 2);
			hashPositions.put("TAX", 2);

			// ---------Start

			// ----------End
			line(startln);
			// lp.newLine(5);
			headerprint(object, 1);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (jInnerData.length() > 0) {

					String header = mainJson.getString("TITLE").trim();
					String HeadTitle = "";
					if (header.equals("sales")) {
						HeadTitle = "SALES  *" + ArabicTEXT.Sales + "!";

					} else if (header.equals("free")) {

						HeadTitle = "TRADE DEAL  *" + ArabicTEXT.tradeDeal + "!";

					} else if (header.equals("bad")) {

						HeadTitle = "BAD RETURN  *" + ArabicTEXT.BadReturn + "!";

					} else if (header.equals("good")) {
						HeadTitle = "GOOD RETURN  *" + ArabicTEXT.GoodReturn + "!";

					} else if (header.equals("promofree")) {

						HeadTitle = "PROMOTION FREE  *" + ArabicTEXT.PromotionFree + "!";

					}
					outStream.write(BoldOn);

					outStream.write(UnderlineOn);

					outStream.write(NewLine);
					outStream.write("       ".getBytes());
					printlines2(HeadTitle, 1, object, 1, args[0], 1, 1);
					outStream.write(NewLine);
					outStream.write(UnderlineOff);
					outStream.write(BoldOff);

				}
				int MAXLEngth = 137;

				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}
				boolean isoutlet = false;
				String strheader = "", strHeaderBottom = "", strTotal = "";

				for (int j = 0; j < headers.length(); j++) {

					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(j));
					if (j == 4 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					strheader = strheader + getAccurateText(
							(j == 4 && object.getString("printbarcode").equals("0")) ? "DESCRIPTION"
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(j == 4 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("DESCRIPTION") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);

					// if (!object.getString("LANG").equals("en")) {
					/*
					 * if (strHeaderBottom.length() > 0) { String
					 * headerArabic=ArabicTEXT.getItemHeaders(printoultlet,
					 * false, true, true); printlines2(headerArabic, 1, object,
					 * 1, args[0], 1, 1); } String
					 * headerbottom=ArabicTEXT.getItemHeaders(printoultlet,
					 * true, true, true); printlines2(headerbottom.trim(), 1,
					 * object, 1, args[0], 1, 1);
					 */
					// } else {

					printlines2(strheader, 1, object, 1, args[0], 1, 1);
					if (strHeaderBottom.length() > 0) {
						printlines2(strHeaderBottom, 1, object, 1, args[0], 1, 1);
					}
					// }

					outStream.write(CompressOff);

					outStream.write(CompressOn);
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);
				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					boolean isoutletdata = false;
					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 0) {
							itemDescrion = (l + 1) + "";

						} else if (m == 4) {
							itemDescrion = "";
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(m);
							} else {

								try {
									itemDescrion = "*" + jArr.getString(m) + "!";
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
						strData = strData + getAccurateText(itemDescrion,
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

					}

					// lp.writeLine(strData);
					outStream.write(CompressOn);
					count++;
					printlines2(strData, 1, object, 1, args[0], 1, 1);

					outStream.write(CompressOff);
				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					printlines2(strTotal, 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);

				}

			}
			outStream.write(NewLine);
			outStream.write(BoldOn);
			if (object.getString("invoicepriceprint").equals("1")) {
				printlines2(
						(getAccurateText("SUB TOTAL", 20, 0) + getAccurateText(" : ", 3, 0)
								+ getAccurateText(object.getString("SUB TOTAL"), 12, 0) + getAccurateText(" : ", 3, 0)
								+ "*" + getAccurateText(ArabicTEXT.SubTotal, 15, 2) + "!"),
						1, object, 1, args[0], 1, 1);
				outStream.write(BoldOff);
				outStream.write(BoldOn);
				if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
					double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

					if (invoice > 0) {

						printlines2(
								(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 0)
										+ getAccurateText(" : ", 3, 0) + "*"
										+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
								1, object, 1, args[0], 1, 1);
					}
				}

				outStream.write(BoldOff);
				outStream.write(BoldOn);
				printlines2(
						(getAccurateText("NET SALES", 20, 0) + getAccurateText(" : ", 3, 0)
								+ getAccurateText(object.getString("NET SALES"), 12, 0) + getAccurateText(" : ", 3, 0)
								+ "*" + getAccurateText(ArabicTEXT.NetSales, 15, 2) + "!"),
						1, object, 1, args[0], 1, 1);
				outStream.write(BoldOff);
			}
			/*
			 * if (object.has("TCALLOWED") &&
			 * object.getString("TCALLOWED").toString().trim().length() > 0 &&
			 * object.getString("TCALLOWED").equals("1")) {
			 * 
			 * // printlines2(getAccurateText("TC CHARGED: //
			 * "+object.getString("TC //
			 * CHARGED"),80,1),1,object,1,args[0],1,1); printlines2(
			 * (getAccurateText("TC CHARGED", 20, 0) + getAccurateText(" : ", 3,
			 * 0) + getAccurateText(object.getString("TCCHARGED"), 12, 0) +
			 * getAccurateText(" : ", 3, 0) + "*" +
			 * getAccurateText(ArabicTEXT.TCcharged, 15, 2) + "!"), 1, object,
			 * 1, args[0], 1, 1);
			 * 
			 * } else { printlines2("", 2, object, 1, args[0], 1, 1); }
			 */

			if (object.has("PaymentType") && Integer.parseInt(object.getString("PaymentType")) < 2) {
				// position = position + 60;
				outStream.write(BoldOn);
				if (!object.getString("LANG").equals("en")) {

					printlines2(getAccurateText("*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args[0], 1,
							1);
				} else {
					printlines2(getAccurateText("PAYMENT DETAILS", 80, 1), 2, object, 1, args[0], 1, 1);
				}

				outStream.write(BoldOff);
				// lp.newLine(2);

				JSONArray jCheques = object.has("Cheque") ? object.getJSONArray("Cheque") : null;
				JSONObject jCash = object.has("Cash") ? object.getJSONObject("Cash") : null;
				int paymenttype = Integer.parseInt(object.getString("ptype"));
				switch (paymenttype) {
				case 0:

					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					outStream.write(BoldOff);
					break;
				case 1:
					outStream.write(BoldOn);
					// lp.write("CHEQUE");
					printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					// lp.newLine(2);
					printlines2(
							(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
									+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
										+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					printlines2("", 1, object, 1, args[0], 1, 1);
					// lp.newLine(2);
					break;
				case 2:
					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					printlines2("", 1, object, 1, args[0], 1, 1);
					printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					// lp.newLine(2);
					printlines2(
							(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
									+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
										+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					printlines2("", 1, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);

					break;
				default:
					break;
				}
			}
			if (object.getString("comments").toString().length() > 0) {

				printlines2("Comments:" + object.getString("comments"), 2, object, 1, args[0], 1, 1);

			}
			if (object.getString("invtrailormsg").toString().length() > 0) {
				printlines2(object.getString("invtrailormsg"), 2, object, 1, args[0], 1, 1);

			}
			Log.e("CountNOW", "" + count);
			printlines2("", 2, object, 1, args[0], 1, 1);
			printlines2(getAccurateText("CUSTOMER_____________*" + ArabicTEXT.Customer + "!    SALESMAN_______________*"
					+ ArabicTEXT.Salesman + "!", 80, 1), 2, object, 1, args[0], 1, 1);

			String copyStatus = "";
			if (object.getString("printstatus").equals("DUPLICATE COPY")) {

				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DuplicateCopy + "",
						80, 1) + "!";
			} else if (object.getString("printstatus").equals("ORIGINAL COPY")) {
				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.OriginalCopy + "!",
						80, 1);

			} else {
				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DraftCopy + "!", 80,
						1);

			}

			printlines2(copyStatus, 2, object, 2, args[0], 1, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	void printSalesTaxReport(JSONObject object, String... args) {
		StringBuffer s1 = new StringBuffer();
		int printoultlet = 0;

		try {
			String cases = object.has("cases") ? object.getString("cases") : "Case";
			String pcs = object.has("pcs") ? object.getString("pcs") : "Unit";
			String qty = object.has("qty") ? object.getString("qty") : "QTY CAS/PCS";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			int CaseEnabled = object.has("CaseEnabled") ? Integer.parseInt(object.getString("CaseEnabled")) : 1;
			if (object.getString("printoutletitemcode").length() > 0) {
				printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
			} else {
				printoultlet = 0;
			}
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int printtax = Integer.parseInt(object.getString("printtax"));
			int totaldiscount = object.has("discountprint") ? Integer.parseInt(object.getString("discountprint")) : 0;
			double totaldiscountval = object.has("TOTAL LINE DISCOUNT")
					? Double.parseDouble(object.getString("TOTAL LINE DISCOUNT")) : 0;
			String totalpcs = object.has("totalpcs") ? object.getString("totalpcs") : "0";
			
			JSONArray jDataNew = object.getJSONArray("data");
			double excTot=0,vatTot=0;
			for (int i = 0; i < jDataNew.length(); i++) {
				JSONObject mainJsonNew = jDataNew.getJSONObject(i);
				JSONObject jTotalNew = mainJsonNew.getJSONObject("TOTAL");
				 excTot = excTot + Double.parseDouble(jTotalNew.getString("EXCISE TAX"));
				 vatTot = vatTot + Double.parseDouble(jTotalNew.getString("VAT AMOUNT"));
			}
			
			if (object.getString("displayupc").equals("1")) {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 3);
				hashValues.put("ITEM#", 11);
				/*if (printoultlet == 1) {
					hashValues.put("OUTLET CODE", 15);
					
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 33);
						hashValues.put("ARBDESCRIPTION", 29);
					} else {
						hashValues.put("ITEM#", 10);
						hashValues.put("DESCRIPTION", 22);
						hashValues.put("ARBDESCRIPTION", 29);
					}
				} else {
					hashValues.put("OUTLET CODE", 0);
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 48);
						hashValues.put("ARBDESCRIPTION", 29);
						
					} else {
						hashValues.put("ITEM#", 10);
						hashValues.put("DESCRIPTION", 37);
						hashValues.put("ARBDESCRIPTION", 29);
					}
				}*/
				if (printoultlet == 1) {
					hashValues.put("OUTLET CODE", 10);
					
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 30);
						hashValues.put("ARBDESCRIPTION", 27);
						hashValues.put(barcode, 27);
					} else {
						hashValues.put("ITEM#", 11);
						hashValues.put("DESCRIPTION", 21);
						hashValues.put("ARBDESCRIPTION", 25);
						hashValues.put(barcode, 25);
					}
				} else {
					hashValues.put("OUTLET CODE", 0);
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 37);
						hashValues.put("ARBDESCRIPTION", 30);
						hashValues.put(barcode, 30);
					} else {
						hashValues.put("ITEM#", 11);
						hashValues.put("DESCRIPTION", 29);
						hashValues.put("ARBDESCRIPTION", 27);
						hashValues.put(barcode, 27);
					}
				}
				
				hashValues.put("UOM", 4);
				hashValues.put(qty, 9);
				hashValues.put("" + pcs, 0);
				hashValues.put("GROSS AMOUNT", 0);
				hashValues.put("NET PRICE", 0);
				if ((printtax > 0) || (companyTaxStng==1 && printtax==0)) {
					
					if(excTot>0&&vatTot>0){
                    	hashValues.put("EXCISE TAX", 8);
                    	hashValues.put("VAT AMOUNT", 12);
                    	hashValues.put("AMOUNT", 11);
                    }else if(excTot>0){
                    	hashValues.put("EXCISE TAX", 16);
                    	hashValues.put("VAT AMOUNT", 0);
                    	hashValues.put("AMOUNT", 15);
                    }else if(vatTot>=0){
                    	hashValues.put("EXCISE TAX", 0);
                    	hashValues.put("VAT AMOUNT", 11);
                    	hashValues.put("AMOUNT", 10);
                    	hashValues.put("GROSS AMOUNT", 10);
                    }else{
                    	hashValues.put("EXCISE TAX", 0);
                    	hashValues.put("VAT AMOUNT", 0);
                    	hashValues.put("AMOUNT", 31);
                    }
					
					if (totaldiscount == 0 && totaldiscountval != 0) {
						hashValues.put("DISCOUNT", 9);
						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 7);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);  //hiding case price
								hashValues.put(pcs + " PRICE", 14);
							}
							

						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 7);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 14);
							}
							
						}

					} else {
						hashValues.put("DISCOUNT", 0);

						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 15);
								hashValues.put(pcs + " PRICE", 8);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 23);
							}
							

						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 12);
								hashValues.put(pcs + " PRICE", 11);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 23);
							}
							
						}
					}
				} else {

					hashValues.put("EXCISE TAX", 0);
					hashValues.put("VAT AMOUNT", 0);
					hashValues.put("AMOUNT", 18);
					if (totaldiscount == 0 && totaldiscountval != 0) {
						hashValues.put("DISCOUNT", 17);
						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 4);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 8);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 15);
							}
							
						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 10);
								hashValues.put(pcs + " PRICE", 9);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 19);
							}
							
						}
					} else {
						hashValues.put("DISCOUNT", 0);

						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 4);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 17);
								hashValues.put(pcs + " PRICE", 15);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 32);
							}
							
						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 18);
								hashValues.put(pcs + " PRICE", 18);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 36);
							}
							
						}
					}
				}
				
				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("ARBDESCRIPTION", 2);
				hashPositions.put("UOM", 2);
				hashPositions.put("" + pcs, 1);
				hashPositions.put(qty, 2);
				hashPositions.put(cases + " PRICE", 2);
				hashPositions.put(pcs + " PRICE", 2);
				hashPositions.put(barcode, 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("GROSS AMOUNT", 2);
				hashPositions.put("EXCISE TAX", 2);
				hashPositions.put("VAT AMOUNT", 2);
				hashPositions.put("AMOUNT", 2);
				hashPositions.put("NET PRICE", 2);
			} else {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 4);
				hashValues.put("ITEM#", 8);
				hashValues.put("OUTLETCODE", 8);
				hashValues.put("DESCRIPTION", 36);
				hashValues.put("QTY", 3);
				hashValues.put("QTY", 3);
				if(CaseEnabled==1){
					hashValues.put("CA.PRICE", 7);
					hashValues.put("PC.ICE", 7);
				}else{
					hashValues.put("CA.PRICE", 0);
					hashValues.put("PC.PRICE", 14);
				}
				
				hashValues.put("DISCOUNT", 0);
				hashValues.put("AMOUNT", 8);
				hashValues.put("CA.PRICE", 7);
				hashValues.put("PC.PRICE", 7);
				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLETCODE", 0);
				 // hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("QTY", 2);
				hashPositions.put("CA.PRICE", 2);
				hashPositions.put("PC.PRICE", 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("AMOUNT", 2);
				hashPositions.put("CA.PRICE", 2);
				hashPositions.put("PC.PRICE", 2);
			}
			// ---------Start

			// ----------End
			
			if (!object.getString("invoiceformat").equals("1")) { // header is
																	// printing
																	// instead
				line(startln);
			}
			headerTaxprint(object, 1,args[0]);
            outStream.write(NewLine);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (jInnerData.length() > 0) {

					String header = mainJson.getString("TITLE").trim();
					String HeadTitle = "";
					if (header.equals("sales")) {
						HeadTitle = "SALES  *" + ArabicTEXT.Sales + "!";

					} else if (header.equals("free")) {

						HeadTitle = "TRADE DEAL  *" + ArabicTEXT.tradeDeal + "!";
					
					} else if (header.equals("bad")) {
						HeadTitle = "BAD RETURN  *" + ArabicTEXT.BadReturn + "!";

					} else if (header.equals("good")) {
						HeadTitle = "GOOD RETURN  *" + ArabicTEXT.GoodReturn + "!";

					} else if (header.equals("promofree")) {
						HeadTitle = "PROMOTION FREE  *" + ArabicTEXT.PromotionFree + "!";

					} else if (header.equals("buyback")) {
						HeadTitle = "BUYBACK FREE  *" + ArabicTEXT.BuybackFree + "!";

					}
					outStream.write(BoldOn);
					outStream.write("       ".getBytes());
					outStream.write(UnderlineOn);
					printlines2(HeadTitle, 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOff);
					outStream.write(BoldOff);

				}
				int MAXLEngth = 137;

				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				String strheader = "",strArbheader="", strHeaderBottom = "", strTotal = "", arabicHeaderBottom="";

				//char l2R = '\u202A';
				//strArbheader =l2R+strArbheader;
				for (int j = 0; j < headers.length(); j++) {

					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(j)); //AS arabic is printing from RTL,
																				//taking columns from right to left
					if (j == 4 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					
					String arabicHeader=  headers.getString(headers.length()-j-1); //AS arabic is printing from RTL,
					//taking columns from right to left
					String arabicHeaderVal=ArabicTEXT.getArabicHeaderVal(arabicHeader,cases,pcs,qty,true); //header top string
					String arabicHeaderBottomVal=ArabicTEXT.getArabicHeaderBottomVal(arabicHeader, cases, pcs, qty,false);//header bottom string
					 strArbheader =strArbheader+ getAccurateText(
								arabicHeaderVal,
								hashValues.get(arabicHeader.toString()) + MAXLEngth,1);
					 arabicHeaderBottom=arabicHeaderBottom+ getAccurateText(
							 arabicHeaderBottomVal,
								hashValues.get(arabicHeader.toString()) + MAXLEngth,1);
					
					strheader = strheader + getAccurateText(
							(j == 4 && object.getString("printbarcode").equals("0")) ? "ARBDESCRIPTION"
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, 
							hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(j == 4 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(headers.getString(j).equals("EXCISE TAX")?"":jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("DESCRIPTION") ? "TOTAL *"+ArabicTEXT.TOTAL+"!" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					//String strArbheader = ArabicTEXT.getArabicHeaderDotmat(excTot,vatTot,totaldiscount);
					printlines2("* "+strArbheader+" !", 1, object, 1, args[0], 1, 1);
					printlines2("* "+arabicHeaderBottom+" !", 1, object, 1, args[0], 1, 1);
					
					printlines2(strheader, 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOn);
					if (strHeaderBottom.length() > 0) {
						printlines2(strHeaderBottom, 1, object, 1, args[0], 1, 1);
					}
					outStream.write(UnderlineOff);
					outStream.write(CompressOff);
				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";

					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 0) {
							itemDescrion = (l + 1) + "";

						} else if (m == 4) {
							itemDescrion = "";
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(m);
							} else {

								try {
									Arabic6822Length arabic6822Length=new Arabic6822Length();
									itemDescrion = "*" + arabic6822Length.ConvertLength(jArr.getString(m), false) + "!";
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}

						strData = strData + getAccurateText(itemDescrion,
								m == 3 ? hashValues.get(headers.getString(m).toString()) + MAXLEngth
										: hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

					}

					// lp.writeLine(strData);
					outStream.write(CompressOn);
					isCompressModeOn = true;
					if (!object.getString("printlanguageflag").equals("2")) {
						// count++;
					}
					//
					if(l==jInnerData.length()-1){
						outStream.write(UnderlineOn);
					}
					printlines2(strData.trim(), 1, object, 1, args[0], 1, 1);
					if(l==jInnerData.length()-1){
						outStream.write(UnderlineOff);
					}
					outStream.write(CompressOff);
					isCompressModeOn = false;
				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					isCompressModeOn = true;
					//printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					printlines2(strTotal, 1, object, 1, args[0], 1, 1);
					isCompressModeOn = false;
					outStream.write(CompressOff);
				}

			}
			outStream.write(NewLine);
			
			int taxSetting=0;
			
			if(taxSetting==1){
				
				printlines2(getAccurateText("  ", 15, 1) + getAccurateText("TOTAL ", 15, 1)+
						getAccurateText("TAX " , 15, 1) +getAccurateText("TOTAL AMOUNT" , 15, 1), 1, object, 1, args[0], 1, 1 );
				outStream.write(NewLine);
				
				printlines2(getAccurateText("________________________________________________________________________________________   ", 60, 1), 2, object, 1, args[0], 1, 1);

				
				double salesamnt=0,retrnamnt=0,damageamnt=0,freeamnt=0;
				double salestax=0,retrntax=0,damagetax=0,freetax=0;
				
				if(Integer.parseInt(object.getString("totalSalesQty"))>0){
					salesamnt=Double.parseDouble(object.getString("TOTSALES"));
					salestax=Double.parseDouble(object.getString("SALESTAX"));
					printlines2(getAccurateText("SALES", 15, 1) + getAccurateText(object.getString("TOTSALES"), 15, 1)+
							getAccurateText(object.getString("SALESTAX") , 15, 1) +getAccurateText(String.valueOf(salesamnt+salestax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalFreeQty"))>0){
					freeamnt=Double.parseDouble(object.getString("TOTFREE"));
					freetax=Double.parseDouble(object.getString("FREETAX"));
					printlines2(getAccurateText("FREE", 15, 1) + getAccurateText(object.getString("TOTFREE"), 15, 1)+
							getAccurateText(object.getString("FREETAX") , 15, 1) +getAccurateText(String.valueOf(freeamnt+freetax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalReturnQty"))>0){
					retrnamnt=Double.parseDouble(object.getString("TOTGOOD"));
					retrntax=Double.parseDouble(object.getString("RETURNTAX"));
					printlines2(getAccurateText("GOOD RETURN", 15, 1) + getAccurateText(object.getString("TOTGOOD"), 15, 1)+
							getAccurateText(object.getString("RETURNTAX") , 15, 1) +getAccurateText(String.valueOf(retrnamnt+retrntax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalDamagedQty"))>0){
					damageamnt=Double.parseDouble(object.getString("TOTBAD"));
					damagetax=Double.parseDouble(object.getString("DAMAGEDTAX"));
					printlines2(getAccurateText("BAD RETURN", 15, 1) + getAccurateText(object.getString("TOTBAD"), 15, 1)+
							getAccurateText(object.getString("DAMAGEDTAX") , 15, 1) +getAccurateText(String.valueOf(damageamnt+damagetax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				
				printlines2(getAccurateText("________________________________________________________________________________________   ", 60, 1), 2, object, 1, args[0], 1, 1);

				
				printlines2(getAccurateText("TOTAL", 15, 1) + getAccurateText(String.valueOf(salesamnt+retrnamnt+damageamnt+freeamnt), 15, 1)+
						getAccurateText(object.getString("TOTTAX") , 15, 1) +getAccurateText(String.valueOf(salesamnt+salestax+retrnamnt+retrntax+damageamnt+damagetax+freeamnt+freetax) , 15, 1), 1, object, 1, args[0], 1, 1 );
				outStream.write(NewLine);
				outStream.write(NewLine);
				
				if (object.has("TOTEXC")) {
					
					if(Double.parseDouble(object.getString("TOTEXC"))!=0){
						int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
						if(companyTaxStng==1&&taxSetting!=1){
							printlines2(
									(getAccurateText("TOTAL EXCISE TAX", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTEXC"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalExcTax, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}

				}
				
				if (object.has("TOTVAT")) {
					
					if(Double.parseDouble(object.getString("TOTVAT"))!=0){
						int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
						if(companyTaxStng==1&&taxSetting!=1){
							printlines2(
									(getAccurateText("TOTAL VAT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTVAT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}
					
					
				}
				
				
				if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
					double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

					if (invoice != 0) {
						if (!object.getString("printlanguageflag").equals("2")) {
							printlines2(
									(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						} else {
							printlines2(
									(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}
				}
				
				printlines2(
						(getAccurateText("NET INVOICE AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
								+ getAccurateText(object.getString("NET SALES"), 12, 0) + getAccurateText(" : ", 3, 0)
								+ "*" + getAccurateText(ArabicTEXT.NetInvoiceAmount, 20, 2) + "!"),
						1, object, 1, args[0], 1, 1);
				
				
				
			}else{
				
				 if (object.has("TOTSALES")&&Integer.parseInt(object.getString("totalSalesQty"))>0) {
						
						
						if (!object.getString("printlanguageflag").equals("2")) {

							printlines2(
									(getAccurateText("TOTAL SALES AMOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTSALES"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						} else {
							printlines2(
									(getAccurateText("TOTAL SALES AMOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTSALES"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}

					

					if (!object.getString("printlanguageflag").equals("2")) {

						if (object.has("TOTGOOD")&&Integer.parseInt(object.getString("totalReturnQty"))>0) {
							
							
							double TOTGOOD=Float.parseFloat(object.getString("TOTGOOD"));
							printlines2(
									(getAccurateText("TOTAL GOOD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
									+getAccurateText(TOTGOOD>0?
											"-"+object.getString("TOTGOOD")
											:object.getString("TOTGOOD"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalGoodReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}
						
						
						if (object.has("TOTBAD")&&Integer.parseInt(object.getString("totalDamagedQty"))>0) {
							double TOTBAD=Float.parseFloat(object.getString("TOTBAD"));

							printlines2(
									(getAccurateText("TOTAL BAD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(TOTBAD>0?
													"-"+object.getString("TOTBAD")
													:object.getString("TOTBAD"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalBadReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}
						if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
							double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

							if (invoice != 0) {
								if (!object.getString("printlanguageflag").equals("2")) {
									printlines2(
											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								} else {
									printlines2(
											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}
						}
						if (object.has("TOTEXC")) {
							
							if(Double.parseDouble(object.getString("TOTEXC"))!=0){
								int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
								if(companyTaxStng==1&&taxSetting!=1){
									printlines2(
											(getAccurateText("TOTAL EXCISE TAX"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTEXC"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalExcTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}

						}
						
						if (object.has("TOTVAT")) {
							
							if(Double.parseDouble(object.getString("TOTVAT"))!=0){
								int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
								if(companyTaxStng==1&&taxSetting!=1){
									printlines2(
											(getAccurateText("TOTAL VAT"+object.getString("currencyprint"), 40,2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTVAT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}else{
								if(companyTaxStng==1 && printtax==0)
								{
									printlines2(
											(getAccurateText("TOTAL VAT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTVAT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
									
								}
										
							}

						}
						
						printlines2(
								(getAccurateText("NET INVOICE AMOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("NET SALES"), 12, 2) + getAccurateText(" : ", 3, 0)
										+ "*" + getAccurateText(ArabicTEXT.NetInvoiceAmount, 20, 2) + "!"),
								1, object, 1, args[0], 1, 1);

					} else {

						if (object.has("TOTGOOD")&& Integer.parseInt(object.getString("totalReturnQty"))>0) {
							
							double TOTGOOD=Float.parseFloat(object.getString("TOTGOOD"));
							
							printlines2(
									(getAccurateText("TOTAL GOOD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(TOTGOOD>0
											?"-"+object.getString("TOTGOOD")
											:object.getString("TOTGOOD"), 12,2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalGoodReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}
						if (object.has("TOTBAD") && Integer.parseInt(object.getString("totalDamagedQty"))>0) {
							
							double TOTBAD=Float.parseFloat(object.getString("TOTBAD"));
							printlines2(
									(getAccurateText("TOTAL BAD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(TOTBAD>0
											?"-"+object.getString("TOTBAD")
											:object.getString("TOTBAD"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalBadReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}
						if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
							double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

							if (invoice != 0) {
								if (!object.getString("printlanguageflag").equals("2")) {
									printlines2(
											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								} else {
									printlines2(
											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}
						}
						if (object.has("TOTEXC")) {
							
							if(Double.parseDouble(object.getString("TOTEXC"))!=0){
								int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
								if(companyTaxStng==1&&taxSetting!=1){
									printlines2(
											(getAccurateText("TOTAL EXCISE TAX"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTEXC"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalExcTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}

						}
						
						if (object.has("TOTVAT")) {
							
							if(Double.parseDouble(object.getString("TOTVAT"))!=0){
								int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
								if(companyTaxStng==1&&taxSetting!=1){
									printlines2(
											(getAccurateText("TOTAL VAT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTVAT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}

						}
						
						outStream.write(BoldOn);

						printlines2(
								(getAccurateText("NET INVOICE AMOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("NET SALES"), 12, 2) + getAccurateText(" : ", 3, 0)
										+ "*" + getAccurateText(ArabicTEXT.NetInvoiceAmount, 20, 2) + "!"),
								1, object, 1, args[0], 1, 1);
						outStream.write(BoldOff);

					}
			}

		
			printlines2("", 1, object, 1, args[0], 1, 1);
			if (object.has("PaymentType") && Integer.parseInt(object.getString("PaymentType")) < 2) {
				// position = position + 60;
				outStream.write(BoldOn);
				outStream.write(UnderlineOn);
				if (!object.getString("LANG").equals("en")) {

					printlines2(getAccurateText("PAYMENT DETAILS "+"*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args[0], 1,
							1);
				} else {
					printlines2(getAccurateText("PAYMENT DETAILS "+"*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args[0], 1, 1);

				}
				outStream.write(UnderlineOff);
				outStream.write(BoldOff);
				

				JSONArray jCheques = object.has("Cheque") ? object.getJSONArray("Cheque") : null;
				JSONObject jCash = object.has("Cash") ? object.getJSONObject("Cash") : null;
				int paymenttype = Integer.parseInt(object.getString("ptype"));
				
				switch (paymenttype) {
				case 0:

					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("CASH : " + jCash.getString("Amount")+object.getString("currencyprint")+ " : *" + ArabicTEXT.Cash + "! ", 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH : " + jCash.getString("Amount")+object.getString("currencyprint")+ " : *" + ArabicTEXT.Cash + "! ", 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					outStream.write(BoldOff);
					break;
				case 1:
					outStream.write(BoldOn);
					printlines2(getAccurateText("CHEQUE "+"*" + ArabicTEXT.cheque + "! ", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					
					printlines2(
							(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
									+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOn);
					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
										+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2)),
								1, object, 1, args[0], 1, 1);

					}
					//printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOff);
					printlines2("", 1, object, 1, args[0], 1, 1);
					break;
				case 2:
					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("CASH : " + jCash.getString("Amount")+ " : *" + ArabicTEXT.Cash + "! ", 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH : " + jCash.getString("Amount")+ " : *" + ArabicTEXT.Cash + "! ", 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					printlines2(getAccurateText("CHEQUE "+"*" + ArabicTEXT.cheque + "! ", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					outStream.write(UnderlineOn);
					
					printlines2(
							(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
									+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
							1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOff);
					outStream.write(UnderlineOn);
					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
										+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					outStream.write(UnderlineOff);

					printlines2("", 1, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);

					break;
				default:
					break;
				}
			}

			if (object.has("invoicepaymentterms") && Integer.parseInt(object.getString("invoicepaymentterms")) >= 2) {

				if (object.has("invoicebalance") && object.getString("invoicebalance").length() > 0) {
					double invoicebalance = Double.parseDouble(object.getString("invoicebalance"));
//					if (invoicebalance > 0) {
//						printlines2((getAccurateText("INVOICE BALANCE", 40, 2) + getAccurateText(" : ", 3, 0)
//						+ getAccurateText(object.getString("invoicebalance") + "", 12, 2)
//						+ getAccurateText(" : ", 3, 0) + "*"
//						+ getAccurateText(ArabicTEXT.InvoiceBalance, 20, 2) + "!"), 1, object, 1, args[0], 1, 1);
//					}
				}

			}
			
			if(object.has("crates")){
				printlines2(getAccurateText("Crates Delivered : "+object.getString("cratesDelivered"), 40, 0)
						+ getAccurateText("Crates Picked Up : "
				+object.getString("cratesPikedUp"), 40, 0), 2, object, 1, args[0], 1, 1);
			}
			
			
			if (object.getString("comments").toString().length() > 0) {
				if (object.getString("LANG").equals("en")) {
					printlines2("Comments:(*"+ArabicTEXT.Comment+"!): " + object.getString("comments"), 2, object, 1, args[0], 1, 1);
				} else {
					printlines2("Comments:(*"+ArabicTEXT.Comment+"!): " + object.getString("comments") + "!", 2, object, 1, args[0], 1, 1);

				}
			}

			if (object.has("DeliveryNumber") && object.getString("DeliveryNumber").toString().length() > 0) {

				printlines2("Delivery Number : " + object.getString("DeliveryNumber"), 2, object, 1, args[0], 1, 1);

			}

			if (object.getString("invtrailormsg").toString().length() > 0) {
				printlines2(object.getString("invtrailormsg"), 2, object, 1, args[0], 1, 1);

			}
			
			String amountpaid=object.has("amountpaid")?object.getString("amountpaid"):"0";
			if(amountpaid!=null && !TextUtils.isEmpty(amountpaid) && Double.parseDouble(amountpaid)==0)
			{
				String invoiceamount=object.getString("NET SALES"); 
				double netsales=0;
				if(invoiceamount!=null && !TextUtils.isEmpty(invoiceamount)){
					netsales=Double.parseDouble(invoiceamount);
				}
				/*String salesmemo=object.has("salesmemo")?object.getString("salesmemo"):"";
				if(salesmemo!=null && !TextUtils.isEmpty(salesmemo) && salesmemo.length()>0 && netsales>0){
					String[] tokens=salesmemo.split("[\\#]");
					if(tokens!=null && tokens.length>0){
						
						
						count=count+2;
						printlines2("", 1, object, 1, args[0], 1, 1);
						String token1=tokens[0].replace("***", object.getString("cname"));
						printlines2(token1, 1, object, 1, args[0], 1, 1);
						printlines2(tokens[1], 1, object, 1, args[0], 1, 1);
						printlines2(tokens[2], 1, object, 1, args[0], 1, 1);
					}
					
				}*/
				
				
			}
			
			
			
			Log.e("CountNOW", "" + count);
			printlines2("", 3, object, 1, args[0], 1, 1);
			if (object.getString("printlanguageflag").equals("2")) {

				if (object.has("TOTAL BAD RETURN") || object.has("totalFreeQty")) {
					
					printlines2(getAccurateText("______________________   ", 26, 1)+getAccurateText("______________________   ", 25, 1)
							+ getAccurateText("______________________   ", 25, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *" + ArabicTEXT.SALESMAN+ "!", 26, 1) +getAccurateText("REC. BY *" + ArabicTEXT.Customer+ "!", 25, 1)+ getAccurateText("SIGN./STAMP *" + ArabicTEXT.SIGN+ "!", 25, 1), 2, object,
							1, args[0], 1, 1);
				} else {
					printlines2(getAccurateText("______________________   ", 26, 1)+getAccurateText("______________________   ", 25, 1)
					+ getAccurateText("______________________   ", 25, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *" + ArabicTEXT.SALESMAN+ "!", 26, 1) +getAccurateText("REC. BY *" + ArabicTEXT.Customer+ "!", 25, 1)+ getAccurateText("SIGN./STAMP *" + ArabicTEXT.SIGN+ "!", 25, 1), 2, object,
					1, args[0], 1, 1);
				}

			} else {

				if (object.has("TOTAL BAD RETURN") || object.has("totalFreeQty")) {
					
					printlines2(getAccurateText("______________________   ", 23, 1)+getAccurateText("______________________   ", 27, 1)
							+ getAccurateText("______________________   ", 28, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *"+ ArabicTEXT.SALESMAN
							+ "!", 23, 1)+getAccurateText("REC. BY *" + ArabicTEXT.Customer+ "!", 27, 1) + getAccurateText("SIGN./STAMP *" + ArabicTEXT.SIGN+ "!", 28, 1), 2, object,
							1, args[0], 1, 1);
					
					
					outStream.write(CompressOff);
				} else {
					printlines2(getAccurateText("______________________   ", 23, 1)+getAccurateText("______________________   ", 27, 1)
					+ getAccurateText("______________________   ", 28, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *" + ArabicTEXT.SALESMAN
					+ "!", 23, 1)+getAccurateText("REC. BY *" + ArabicTEXT.Customer
							+ "!", 27, 1) + getAccurateText("SIGN./STAMP *" + ArabicTEXT.SIGN+ "!", 28, 1), 2, object,
					1, args[0], 1, 1);
				}

			}
			
			String copyStatus = "";
			if (object.getString("printstatus").equals("DUPLICATE COPY")) {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1) + " ";
				} else {
					copyStatus = getAccurateText(
							object.getString("printstatus") + "  *" + ArabicTEXT.DuplicateCopy + "", 80, 1) + "!";
				}
			} else if (object.getString("printstatus").equals("ORIGINAL COPY")) {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1) + " ";
				} else {
					copyStatus = getAccurateText(
							object.getString("printstatus") + "  *" + ArabicTEXT.OriginalCopy + "!", 80, 1);
				}

			} else {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1);
				} else {
					copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DraftCopy + "!",
							80, 1);
				}

			}

			printlines2(copyStatus, 2, object, 2, args[0], 1, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	void printInvoiceSummary(JSONObject object,String...args){
		StringBuffer s1 = new StringBuffer();
		try {
			if (!object.getString("invoiceformat").equals("1")) { // header is  printing instead
				
			line(startln);
			}
			int printtax = Integer.parseInt(object.getString("printtax"));
			headerTaxSummaryPrint(object, 1, args);
			outStream.write(NewLine);
			//JSONArray jData = object.getJSONArray("data");

				if (object.has("TOTSALES")) {


							printlines2(
									(getAccurateText("NET SALES AMOUNT "+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTSALES"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
					}


				if (object.has("totalcreditamount")) {


							double totalcreditamount=Float.parseFloat(object.getString("totalcreditamount"));
							printlines2(
									(getAccurateText("TOTAL CREDIT AMOUNT "+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
									+getAccurateText(totalcreditamount>0?
											"-"+object.getString("totalcreditamount")
											:object.getString("totalcreditamount"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.CREDITAMOUNT, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}

//				if (object.has("TOTBAD")) {
//							double TOTBAD=Float.parseFloat(object.getString("TOTBAD"));
//
//							printlines2(
//									(getAccurateText("TOTAL BAD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
//											+ getAccurateText(TOTBAD>0?
//													"-"+object.getString("TOTBAD")
//													:object.getString("TOTBAD"), 12, 2)
//											+ getAccurateText(" : ", 3, 0) + "*"
//											+ getAccurateText(ArabicTEXT.TotalBadReturn, 20, 2) + "!"),
//									1, object, 1, args[0], 1, 1);
//
//						}
				int taxSetting=0;
//				if (object.has("TOTVAT")) {
//				
//					if(Double.parseDouble(object.getString("TOTVAT"))!=0){
//						int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
//						
//						if(companyTaxStng==1&&taxSetting!=1){
//							printlines2(
//									(getAccurateText("TOTAL VAT"+object.getString("currencyprint"), 40,2) + getAccurateText(" : ", 3, 0)
//											+ getAccurateText(object.getString("TOTVAT"), 12, 2)
//											+ getAccurateText(" : ", 3, 0) + "*"
//											+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
//									1, object, 1, args[0], 1, 1);
//						}
//					}else{
//						if(companyTaxStng==1 && printtax==0)
//						{
//							printlines2(
//									(getAccurateText("TOTAL VAT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
//											+ getAccurateText(object.getString("TOTVAT"), 12, 2)
//											+ getAccurateText(" : ", 3, 0) + "*"
//											+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
//									1, object, 1, args[0], 1, 1);
//
//						}
//
//					}
//
//				}

//				if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
//							double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));
//
//							if (invoice != 0) {
//								
//									printlines2(
//											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
//													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
//													+ getAccurateText(" : ", 3, 0) + "*"
//													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
//											1, object, 1, args[0], 1, 1);
//								
//							}
//						}

				printlines2((getAccurateText("NET  AMOUNT "+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("NET SALES"), 12, 2) + getAccurateText(" : ", 3, 0)
										+ "*" + getAccurateText(ArabicTEXT.NetInvoiceAmount, 20, 2) + "!"),
								2, object, 2, args[0], 1, 1);

			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void printSalesExciseTaxReport(JSONObject object, String... args) {
		StringBuffer s1 = new StringBuffer();
		int printoultlet = 0;

		try {
			String cases = object.has("cases") ? object.getString("cases") : "Case";
			String pcs = object.has("pcs") ? object.getString("pcs") : "Unit";
			String qty = object.has("qty") ? object.getString("qty") : "QTY CAS/PCS";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			int CaseEnabled = object.has("CaseEnabled") ? Integer.parseInt(object.getString("CaseEnabled")) : 1;
			if (object.getString("printoutletitemcode").length() > 0) {
				printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
			} else {
				printoultlet = 0;
			}
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int printtax = Integer.parseInt(object.getString("printtax"));
			int totaldiscount = object.has("discountprint") ? Integer.parseInt(object.getString("discountprint")) : 0;
			double totaldiscountval = object.has("TOTAL LINE DISCOUNT")
					? Double.parseDouble(object.getString("TOTAL LINE DISCOUNT")) : 0;
			String totalpcs = object.has("totalpcs") ? object.getString("totalpcs") : "0";
			
			JSONArray jDataNew = object.getJSONArray("data");
			double excTot=0,vatTot=0;
			for (int i = 0; i < jDataNew.length(); i++) {
				JSONObject mainJsonNew = jDataNew.getJSONObject(i);
				JSONObject jTotalNew = mainJsonNew.getJSONObject("TOTAL");
				 excTot = excTot + Double.parseDouble(jTotalNew.getString("EXCISE TAX"));
				 vatTot = vatTot + Double.parseDouble(jTotalNew.getString("VAT AMOUNT"));
			}
			
			if (object.getString("displayupc").equals("1")) {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 3);
				hashValues.put("ITEM#", 11);
				/*if (printoultlet == 1) {
					hashValues.put("OUTLET CODE", 15);
					
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 33);
						hashValues.put("ARBDESCRIPTION", 29);
					} else {
						hashValues.put("ITEM#", 10);
						hashValues.put("DESCRIPTION", 22);
						hashValues.put("ARBDESCRIPTION", 29);
					}
				} else {
					hashValues.put("OUTLET CODE", 0);
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 48);
						hashValues.put("ARBDESCRIPTION", 29);
						
					} else {
						hashValues.put("ITEM#", 10);
						hashValues.put("DESCRIPTION", 37);
						hashValues.put("ARBDESCRIPTION", 29);
					}
				}*/
				if (printoultlet == 1) {
					hashValues.put("OUTLET CODE", 10);
					
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 31);
						hashValues.put("ARBDESCRIPTION", 27);
						hashValues.put(barcode, 27);
					} else {
						hashValues.put("ITEM#", 11);
						hashValues.put("DESCRIPTION", 22);
						hashValues.put("ARBDESCRIPTION", 25);
						hashValues.put(barcode, 25);
					}
				} else {
					hashValues.put("OUTLET CODE", 0);
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 38);
						hashValues.put("ARBDESCRIPTION", 30);
						hashValues.put(barcode, 30);
					} else {
						hashValues.put("ITEM#", 11);
						hashValues.put("DESCRIPTION", 30);
						hashValues.put("ARBDESCRIPTION", 27);
						hashValues.put(barcode, 27);
					}
				}
				
				hashValues.put("UOM", 4);
				hashValues.put(qty, 9);
				hashValues.put("" + pcs, 0);
				hashValues.put("GROSS AMOUNT", 0);
				hashValues.put("NET PRICE", 0);
				if (printtax > 0) {
					
					if(excTot>0&&vatTot>0){
                    	hashValues.put("EXCISE TAX", 7);
                    	hashValues.put("VAT AMOUNT", 12);
                    	hashValues.put("AMOUNT", 10);
                    }else if(excTot>0){
                    	hashValues.put("EXCISE TAX", 15);
                    	hashValues.put("VAT AMOUNT", 0);
                    	hashValues.put("AMOUNT", 14);
                    }else if(vatTot>0){
                    	hashValues.put("EXCISE TAX", 0);
                    	hashValues.put("VAT AMOUNT", 12);
                    	hashValues.put("AMOUNT", 9);
                    	hashValues.put("GROSS AMOUNT", 8);
                    }else{
                    	hashValues.put("EXCISE TAX", 0);
                    	hashValues.put("VAT AMOUNT", 0);
                    	hashValues.put("AMOUNT", 29);
                    }
					
					if (totaldiscount == 0 && totaldiscountval != 0) {
						hashValues.put("DISCOUNT", 9);
						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 8);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);  //hiding case price
								hashValues.put(pcs + " PRICE", 15);
							}
							

						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 8);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 15);
							}
							
						}

					} else {
						hashValues.put("DISCOUNT", 0);

						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 15);
								hashValues.put(pcs + " PRICE", 9);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 24);
							}
							

						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								if(excTot>0){
									hashValues.put("NET PRICE", 12);
									hashValues.put(cases + " PRICE", 12);
									hashValues.put(pcs + " PRICE", 0);
								}else{
									hashValues.put(cases + " PRICE", 12);
									hashValues.put(pcs + " PRICE", 12);
								}
								
							}else{
								if(excTot>0){
									hashValues.put("NET PRICE", 8);
									hashValues.put(cases + " PRICE", 0);
									hashValues.put(pcs + " PRICE", 16);
								}else{
									hashValues.put(cases + " PRICE", 0);
									hashValues.put(pcs + " PRICE", 24);
								}
								
							}
							
						}
					}
				} else {

					hashValues.put("EXCISE TAX", 0);
					hashValues.put("VAT AMOUNT", 0);
					hashValues.put("AMOUNT", 17);
					if (totaldiscount == 0 && totaldiscountval != 0) {
						hashValues.put("DISCOUNT", 17);
						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 4);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 8);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 15);
							}
							
						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 10);
								hashValues.put(pcs + " PRICE", 9);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 19);
							}
							
						}
					} else {
						hashValues.put("DISCOUNT", 0);

						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 4);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 17);
								hashValues.put(pcs + " PRICE", 15);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 32);
							}
							
						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 18);
								hashValues.put(pcs + " PRICE", 18);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 36);
							}
							
						}
					}
				}

				

				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("ARBDESCRIPTION", 2);
				hashPositions.put("UOM", 2);
				hashPositions.put("" + pcs, 1);
				hashPositions.put(qty, 2);
				hashPositions.put(cases + " PRICE", 2);
				hashPositions.put(pcs + " PRICE", 2);
				hashPositions.put(barcode, 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("GROSS AMOUNT", 2);
				hashPositions.put("EXCISE TAX", 2);
				hashPositions.put("VAT AMOUNT", 2);
				hashPositions.put("AMOUNT", 2);
				hashPositions.put("NET PRICE", 2);
			} else {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 4);
				hashValues.put("ITEM#", 8);
				hashValues.put("OUTLETCODE", 8);
				hashValues.put("DESCRIPTION", 36);
				hashValues.put("QTY", 3);
				hashValues.put("QTY", 3);
				if(CaseEnabled==1){
					hashValues.put("CA.PRICE", 7);
					hashValues.put("PC.ICE", 7);
				}else{
					hashValues.put("CA.PRICE", 0);
					hashValues.put("PC.PRICE", 14);
				}
				
				hashValues.put("DISCOUNT", 0);
				hashValues.put("AMOUNT", 8);
				hashValues.put("CA.PRICE", 7);
				hashValues.put("PC.PRICE", 7);
				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLETCODE", 0);
				 // hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("QTY", 2);
				hashPositions.put("CA.PRICE", 2);
				hashPositions.put("PC.PRICE", 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("AMOUNT", 2);
				hashPositions.put("CA.PRICE", 2);
				hashPositions.put("PC.PRICE", 2);
			}
			// ---------Start

			// ----------End

			if (!object.getString("invoiceformat").equals("1")) { // header is
																	// printing
																	// instead
				line(startln);
			}
			headerTaxprint(object, 1,args[0]);
            outStream.write(NewLine);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (jInnerData.length() > 0) {

					String header = mainJson.getString("TITLE").trim();
					String HeadTitle = "";
					if (header.equals("sales")) {
						HeadTitle = "SALES  *" + ArabicTEXT.Sales + "!";

					} else if (header.equals("free")) {

						HeadTitle = "TRADE DEAL  *" + ArabicTEXT.tradeDeal + "!";
					
					} else if (header.equals("bad")) {
						HeadTitle = "BAD RETURN  *" + ArabicTEXT.BadReturn + "!";

					} else if (header.equals("good")) {
						HeadTitle = "GOOD RETURN  *" + ArabicTEXT.GoodReturn + "!";

					} else if (header.equals("promofree")) {
						HeadTitle = "PROMOTION FREE  *" + ArabicTEXT.PromotionFree + "!";

					} else if (header.equals("buyback")) {
						HeadTitle = "BUYBACK FREE  *" + ArabicTEXT.BuybackFree + "!";

					}
					outStream.write(BoldOn);
					outStream.write("       ".getBytes());
					outStream.write(UnderlineOn);
					printlines2(HeadTitle, 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOff);
					outStream.write(BoldOff);

				}
				int MAXLEngth = 137;

				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				String strheader = "", strHeaderBottom = "", strTotal = "";

				for (int j = 0; j < headers.length(); j++) {

					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(j));
					if (j == 4 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					strheader = strheader + getAccurateText(
							(j == 4 && object.getString("printbarcode").equals("0")) ? "ARBDESCRIPTION"
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
									:HeaderVal.equals("EXCISE TAX")?cases
									:HeaderVal.equals("VAT AMOUNT")?cases
									:HeaderVal.equals("NET PRICE")?cases+" NET"
									:HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(j == 4 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													:HeaderVal.equals("EXCISE TAX")?"EXCISE":HeaderVal.equals("VAT AMOUNT")?"VAT":HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText((headers.getString(j).equals("EXCISE TAX") || headers.getString(j).equals("VAT AMOUNT") || headers.getString(j).equals("AMOUNT"))?"":jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("DESCRIPTION") ? "TOTAL *"+ArabicTEXT.TOTAL+"!" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0 ) {
					outStream.write(CompressOn);
					String strArbheader = ArabicTEXT.getArabicHeaderDotmat(excTot,vatTot,totaldiscount);
					printlines2(strArbheader, 1, object, 1, args[0], 1, 1);
					
					printlines2(strheader, 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOn);
					if (strHeaderBottom.length() > 0) {
						printlines2(strHeaderBottom, 1, object, 1, args[0], 1, 1);
					}
					outStream.write(UnderlineOff);
					outStream.write(CompressOff);
				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";

					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 0) {
							itemDescrion = (l + 1) + "";

						} else if (m == 4) {
							itemDescrion = "";
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(m);
							} else {

								try {
									itemDescrion = "*" + jArr.getString(m) + "!";
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}

						strData = strData + getAccurateText(itemDescrion,
								m == 3 ? hashValues.get(headers.getString(m).toString()) + MAXLEngth
										: hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

					}

					// lp.writeLine(strData);
					outStream.write(CompressOn);
					isCompressModeOn = true;
					if (!object.getString("printlanguageflag").equals("2")) {
						// count++;
					}
					//
					if(l==jInnerData.length()-1){
						outStream.write(UnderlineOn);
					}
					printlines2(strData.trim(), 1, object, 1, args[0], 1, 1);
					if(l==jInnerData.length()-1){
						outStream.write(UnderlineOff);
					}
					outStream.write(CompressOff);
					isCompressModeOn = false;
				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					isCompressModeOn = true;
					//printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					printlines2(strTotal, 1, object, 1, args[0], 1, 1);
					isCompressModeOn = false;
					outStream.write(CompressOff);
				}

			}
			outStream.write(NewLine);
			
			int taxSetting=0;
			
			if(taxSetting==1){
				
				printlines2(getAccurateText("  ", 15, 1) + getAccurateText("TOTAL ", 15, 1)+
						getAccurateText("TAX " , 15, 1) +getAccurateText("TOTAL AMOUNT" , 15, 1), 1, object, 1, args[0], 1, 1 );
				outStream.write(NewLine);
				
				printlines2(getAccurateText("________________________________________________________________________________________   ", 60, 1), 2, object, 1, args[0], 1, 1);

				
				double salesamnt=0,retrnamnt=0,damageamnt=0,freeamnt=0;
				double salestax=0,retrntax=0,damagetax=0,freetax=0;
				
				if(Integer.parseInt(object.getString("totalSalesQty"))>0){
					salesamnt=Double.parseDouble(object.getString("TOTSALES"));
					salestax=Double.parseDouble(object.getString("SALESTAX"));
					printlines2(getAccurateText("SALES", 15, 1) + getAccurateText(object.getString("TOTSALES"), 15, 1)+
							getAccurateText(object.getString("SALESTAX") , 15, 1) +getAccurateText(String.valueOf(salesamnt+salestax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalFreeQty"))>0){
					freeamnt=Double.parseDouble(object.getString("TOTFREE"));
					freetax=Double.parseDouble(object.getString("FREETAX"));
					printlines2(getAccurateText("FREE", 15, 1) + getAccurateText(object.getString("TOTFREE"), 15, 1)+
							getAccurateText(object.getString("FREETAX") , 15, 1) +getAccurateText(String.valueOf(freeamnt+freetax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalReturnQty"))>0){
					retrnamnt=Double.parseDouble(object.getString("TOTGOOD"));
					retrntax=Double.parseDouble(object.getString("RETURNTAX"));
					printlines2(getAccurateText("GOOD RETURN", 15, 1) + getAccurateText(object.getString("TOTGOOD"), 15, 1)+
							getAccurateText(object.getString("RETURNTAX") , 15, 1) +getAccurateText(String.valueOf(retrnamnt+retrntax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalDamagedQty"))>0){
					damageamnt=Double.parseDouble(object.getString("TOTBAD"));
					damagetax=Double.parseDouble(object.getString("DAMAGEDTAX"));
					printlines2(getAccurateText("BAD RETURN", 15, 1) + getAccurateText(object.getString("TOTBAD"), 15, 1)+
							getAccurateText(object.getString("DAMAGEDTAX") , 15, 1) +getAccurateText(String.valueOf(damageamnt+damagetax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				
				printlines2(getAccurateText("________________________________________________________________________________________   ", 60, 1), 2, object, 1, args[0], 1, 1);

				
				printlines2(getAccurateText("TOTAL", 15, 1) + getAccurateText(String.valueOf(salesamnt+retrnamnt+damageamnt+freeamnt), 15, 1)+
						getAccurateText(object.getString("TOTTAX") , 15, 1) +getAccurateText(String.valueOf(salesamnt+salestax+retrnamnt+retrntax+damageamnt+damagetax+freeamnt+freetax) , 15, 1), 1, object, 1, args[0], 1, 1 );
				outStream.write(NewLine);
				outStream.write(NewLine);
				
				if (object.has("TOTEXC")) {
					
					if(Double.parseDouble(object.getString("TOTEXC"))!=0){
						int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
						if(companyTaxStng==1&&taxSetting!=1){
							printlines2(
									(getAccurateText("TOTAL EXCISE TAX", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTEXC"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalExcTax, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}

				}
				
				if (object.has("TOTVAT")) {
					
					if(Double.parseDouble(object.getString("TOTVAT"))!=0){
						int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
						if(companyTaxStng==1&&taxSetting!=1){
							printlines2(
									(getAccurateText("TOTAL VAT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTVAT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}

				}
				
				
				if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
					double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

					if (invoice != 0) {
						if (!object.getString("printlanguageflag").equals("2")) {
							printlines2(
									(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						} else {
							printlines2(
									(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}
				}
				
				printlines2(
						(getAccurateText("NET INVOICE AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
								+ getAccurateText(object.getString("NET SALES"), 12, 0) + getAccurateText(" : ", 3, 0)
								+ "*" + getAccurateText(ArabicTEXT.NetInvoiceAmount, 20, 2) + "!"),
						1, object, 1, args[0], 1, 1);
				
				
				
			}else{
				
				 if (object.has("TOTSALES")&&Integer.parseInt(object.getString("totalSalesQty"))>0) {
						
						
						if (!object.getString("printlanguageflag").equals("2")) {

							printlines2(
									(getAccurateText("TOTAL SALES AMOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTSALES"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						} else {
							printlines2(
									(getAccurateText("TOTAL SALES AMOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTSALES"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}

					

					if (!object.getString("printlanguageflag").equals("2")) {

						if (object.has("TOTGOOD")&&Integer.parseInt(object.getString("totalReturnQty"))>0) {
							
							
							
							printlines2(
									(getAccurateText("TOTAL GOOD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText("-"+object.getString("TOTGOOD"), 12,2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalGoodReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}else if (object.has("TOTGOOD")&&Integer.parseInt(object.getString("totalReturnQty"))>0) {
							
							printlines2(
									(getAccurateText("TOTAL GOOD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText("-"+object.getString("TOTGOOD"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalGoodReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}
						
						
						if (object.has("TOTBAD")&&Integer.parseInt(object.getString("totalDamagedQty"))>0) {
							
							printlines2(
									(getAccurateText("TOTAL BAD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText("-"+object.getString("TOTBAD"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalBadReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}
						if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
							double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

							if (invoice != 0) {
								if (!object.getString("printlanguageflag").equals("2")) {
									printlines2(
											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								} else {
									printlines2(
											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}
						}
						if (object.has("TOTEXC")) {
							
							if(Double.parseDouble(object.getString("TOTEXC"))!=0){
								int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
								if(companyTaxStng==1&&taxSetting!=1){
									printlines2(
											(getAccurateText("TOTAL EXCISE TAX"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTEXC"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalExcTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}

						}
						
						if (object.has("TOTVAT")) {
							
							if(Double.parseDouble(object.getString("TOTVAT"))!=0){
								int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
								if(companyTaxStng==1&&taxSetting!=1){
									printlines2(
											(getAccurateText("TOTAL VAT"+object.getString("currencyprint"), 40,2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTVAT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}

						}
						
						printlines2(
								(getAccurateText("NET INVOICE AMOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("NET SALES"), 12, 2) + getAccurateText(" : ", 3, 0)
										+ "*" + getAccurateText(ArabicTEXT.NetInvoiceAmount, 20, 2) + "!"),
								1, object, 1, args[0], 1, 1);

					} else {

						if (object.has("TOTGOOD")&& Integer.parseInt(object.getString("totalReturnQty"))>0) {
							
							
							printlines2(
									(getAccurateText("TOTAL GOOD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText("-"+object.getString("TOTGOOD"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalGoodReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}else if (object.has("TOTGOOD")&&Integer.parseInt(object.getString("totalReturnQty"))>0) {
							printlines2(
									(getAccurateText("TOTAL GOOD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText("-"+object.getString("TOTGOOD"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalGoodReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}
						if (object.has("TOTBAD") && Integer.parseInt(object.getString("totalDamagedQty"))>0) {
							
							
							printlines2(
									(getAccurateText("TOTAL BAD RETURN"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText("-"+object.getString("TOTBAD"), 12, 2)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalBadReturn, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);

						}
						if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
							double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

							if (invoice != 0) {
								if (!object.getString("printlanguageflag").equals("2")) {
									printlines2(
											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								} else {
									printlines2(
											(getAccurateText("INVOICE DISCOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}
						}
						if (object.has("TOTEXC")) {
							
							if(Double.parseDouble(object.getString("TOTEXC"))!=0){
								int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
								if(companyTaxStng==1&&taxSetting!=1){
									printlines2(
											(getAccurateText("TOTAL EXCISE TAX"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTEXC"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalExcTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}

						}
						
						if (object.has("TOTVAT")) {
							
							if(Double.parseDouble(object.getString("TOTVAT"))!=0){
								int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
								if(companyTaxStng==1&&taxSetting!=1){
									printlines2(
											(getAccurateText("TOTAL VAT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
													+ getAccurateText(object.getString("TOTVAT"), 12, 2)
													+ getAccurateText(" : ", 3, 0) + "*"
													+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
											1, object, 1, args[0], 1, 1);
								}
							}

						}
						
						outStream.write(BoldOn);

						printlines2(
								(getAccurateText("NET INVOICE AMOUNT"+object.getString("currencyprint"), 40, 2) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("NET SALES"), 12,2) + getAccurateText(" : ", 3, 0)
										+ "*" + getAccurateText(ArabicTEXT.NetInvoiceAmount, 20, 2) + "!"),
								1, object, 1, args[0], 1, 1);
						outStream.write(BoldOff);

					}
			}

		
			printlines2("", 1, object, 1, args[0], 1, 1);
			if (object.has("PaymentType") && Integer.parseInt(object.getString("PaymentType")) < 2) {
				// position = position + 60;
				outStream.write(BoldOn);
				outStream.write(UnderlineOn);
				if (!object.getString("LANG").equals("en")) {

					printlines2(getAccurateText("PAYMENT DETAILS "+"*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args[0], 1,
							1);
				} else {
					printlines2(getAccurateText("PAYMENT DETAILS "+"*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args[0], 1, 1);

				}
				outStream.write(UnderlineOff);
				outStream.write(BoldOff);
				

				JSONArray jCheques = object.has("Cheque") ? object.getJSONArray("Cheque") : null;
				JSONObject jCash = object.has("Cash") ? object.getJSONObject("Cash") : null;
				int paymenttype = Integer.parseInt(object.getString("ptype"));
				
				switch (paymenttype) {
				case 0:

					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("CASH : " + jCash.getString("Amount")+ " : *" + ArabicTEXT.Cash + "! ", 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH : " + jCash.getString("Amount")+ " : *" + ArabicTEXT.Cash + "! ", 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					outStream.write(BoldOff);
					break;
				case 1:
					outStream.write(BoldOn);
					printlines2(getAccurateText("CHEQUE "+"*" + ArabicTEXT.cheque + "! ", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					
					printlines2(
							(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
									+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOn);
					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
										+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2)),
								1, object, 1, args[0], 1, 1);

					}
					//printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOff);
					printlines2("", 1, object, 1, args[0], 1, 1);
					break;
				case 2:
					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("CASH : " + jCash.getString("Amount")+ " : *" + ArabicTEXT.Cash + "! ", 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH : " + jCash.getString("Amount")+ " : *" + ArabicTEXT.Cash + "! ", 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					printlines2(getAccurateText("CHEQUE "+"*" + ArabicTEXT.cheque + "! ", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					outStream.write(UnderlineOn);
					
					printlines2(
							(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
									+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
							1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOff);
					outStream.write(UnderlineOn);
					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
										+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					outStream.write(UnderlineOff);

					printlines2("", 1, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);

					break;
				default:
					break;
				}
			}

			if (object.has("invoicepaymentterms") && Integer.parseInt(object.getString("invoicepaymentterms")) >= 2) {

				if (object.has("invoicebalance") && object.getString("invoicebalance").length() > 0) {
					double invoicebalance = Double.parseDouble(object.getString("invoicebalance"));
					if (invoicebalance > 0) {
						printlines2((getAccurateText("INVOICE BALANCE", 40, 2) + getAccurateText(" : ", 3, 0)
						+ getAccurateText(object.getString("invoicebalance") + "", 12, 2)
						+ getAccurateText(" : ", 3, 0) + "*"
						+ getAccurateText(ArabicTEXT.InvoiceBalance, 20, 2) + "!"), 1, object, 1, args[0], 1, 1);
					}
				}

			}
			
			if(object.has("crates")){
				printlines2(getAccurateText("Crates Delivered : "+object.getString("cratesDelivered"), 40, 0)
						+ getAccurateText("Crates Picked Up : "
				+object.getString("cratesPikedUp"), 40, 0), 2, object, 1, args[0], 1, 1);
			}
			
			
			if (object.getString("comments").toString().length() > 0) {
				if (object.getString("LANG").equals("en")) {
					printlines2("Comments:(*"+ArabicTEXT.Comment+"!): " + object.getString("comments"), 2, object, 1, args[0], 1, 1);
				} else {
					printlines2("Comments:(*"+ArabicTEXT.Comment+"!): " + object.getString("comments") + "!", 2, object, 1, args[0], 1, 1);

				}
			}

			if (object.has("DeliveryNumber") && object.getString("DeliveryNumber").toString().length() > 0) {

				printlines2("Delivery Number : " + object.getString("DeliveryNumber"), 2, object, 1, args[0], 1, 1);

			}

			if (object.getString("invtrailormsg").toString().length() > 0) {
				printlines2(object.getString("invtrailormsg"), 2, object, 1, args[0], 1, 1);

			}
			
			String amountpaid=object.has("amountpaid")?object.getString("amountpaid"):"0";
			if(amountpaid!=null && !TextUtils.isEmpty(amountpaid) && Double.parseDouble(amountpaid)==0)
			{
				String invoiceamount=object.getString("NET SALES"); 
				double netsales=0;
				if(invoiceamount!=null && !TextUtils.isEmpty(invoiceamount)){
					netsales=Double.parseDouble(invoiceamount);
				}
				/*String salesmemo=object.has("salesmemo")?object.getString("salesmemo"):"";
				if(salesmemo!=null && !TextUtils.isEmpty(salesmemo) && salesmemo.length()>0 && netsales>0){
					String[] tokens=salesmemo.split("[\\#]");
					if(tokens!=null && tokens.length>0){
						
						
						count=count+2;
						printlines2("", 1, object, 1, args[0], 1, 1);
						String token1=tokens[0].replace("***", object.getString("cname"));
						printlines2(token1, 1, object, 1, args[0], 1, 1);
						printlines2(tokens[1], 1, object, 1, args[0], 1, 1);
						printlines2(tokens[2], 1, object, 1, args[0], 1, 1);
					}
					
				}*/
				
				
			}
			
			
			
			Log.e("CountNOW", "" + count);
			printlines2("", 3, object, 1, args[0], 1, 1);
			if (object.getString("printlanguageflag").equals("2")) {

				if (object.has("TOTAL BAD RETURN") || object.has("totalFreeQty")) {
					
					printlines2(getAccurateText("______________________   ", 26, 1)+getAccurateText("______________________   ", 25, 1)
							+ getAccurateText("______________________   ", 25, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *" + ArabicTEXT.SALESMAN+ "!", 26, 1) +getAccurateText("REC. BY *" + ArabicTEXT.Customer+ "!", 25, 1)+ getAccurateText("SIGN./STAMP *" + ArabicTEXT.SIGN+ "!", 25, 1), 2, object,
							1, args[0], 1, 1);
				} else {
					printlines2(getAccurateText("______________________   ", 26, 1)+getAccurateText("______________________   ", 25, 1)
					+ getAccurateText("______________________   ", 25, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *" + ArabicTEXT.SALESMAN+ "!", 26, 1) +getAccurateText("REC. BY *" + ArabicTEXT.Customer+ "!", 25, 1)+ getAccurateText("SIGN./STAMP *" + ArabicTEXT.SIGN+ "!", 25, 1), 2, object,
					1, args[0], 1, 1);
				}

			} else {

				if (object.has("TOTAL BAD RETURN") || object.has("totalFreeQty")) {
					
					printlines2(getAccurateText("______________________   ", 23, 1)+getAccurateText("______________________   ", 27, 1)
							+ getAccurateText("______________________   ", 28, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *"+ ArabicTEXT.SALESMAN
							+ "!", 23, 1)+getAccurateText("REC. BY *" + ArabicTEXT.Customer+ "!", 27, 1) + getAccurateText("SIGN./STAMP *" + ArabicTEXT.SIGN+ "!", 28, 1), 2, object,
							1, args[0], 1, 1);
					
					
					outStream.write(CompressOff);
				} else {
					printlines2(getAccurateText("______________________   ", 23, 1)+getAccurateText("______________________   ", 27, 1)
					+ getAccurateText("______________________   ", 28, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *" + ArabicTEXT.SALESMAN
					+ "!", 23, 1)+getAccurateText("REC. BY *" + ArabicTEXT.Customer
							+ "!", 27, 1) + getAccurateText("SIGN./STAMP *" + ArabicTEXT.SIGN+ "!", 28, 1), 2, object,
					1, args[0], 1, 1);
				}

			}
			
			String copyStatus = "";
			if (object.getString("printstatus").equals("DUPLICATE COPY")) {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1) + " ";
				} else {
					copyStatus = getAccurateText(
							object.getString("printstatus") + "  *" + ArabicTEXT.DuplicateCopy + "", 80, 1) + "!";
				}
			} else if (object.getString("printstatus").equals("ORIGINAL COPY")) {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1) + " ";
				} else {
					copyStatus = getAccurateText(
							object.getString("printstatus") + "  *" + ArabicTEXT.OriginalCopy + "!", 80, 1);
				}

			} else {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1);
				} else {
					copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DraftCopy + "!",
							80, 1);
				}

			}

			printlines2(copyStatus, 2, object, 2, args[0], 1, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	void printSalesReport(JSONObject object, String... args) {
		StringBuffer s1 = new StringBuffer();
		int printoultlet = 0;

		try {
			
			
			
			String cases = object.has("cases") ? object.getString("cases") : "Case";
			String pcs = object.has("pcs") ? object.getString("pcs") : "Unit";
			String qty = object.has("qty") ? object.getString("qty") : "QTY CAS/PCS";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			int CaseEnabled = object.has("CaseEnabled") ? Integer.parseInt(object.getString("CaseEnabled")) : 1;
			if (object.getString("printoutletitemcode").length() > 0) {
				printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
			} else {
				printoultlet = 0;
			}
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int printtax = Integer.parseInt(object.getString("printtax"));
			int totaldiscount = object.has("discountprint") ? Integer.parseInt(object.getString("discountprint")) : 0;
			double totaldiscountval = object.has("TOTAL LINE DISCOUNT")
					? Double.parseDouble(object.getString("TOTAL LINE DISCOUNT")) : 0;
			String totalpcs = object.has("totalpcs") ? object.getString("totalpcs") : "0";
			if (object.getString("displayupc").equals("1")) {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 3);
				hashValues.put("ITEM#", 11);
				if (printoultlet == 1) {
					hashValues.put("OUTLET CODE", 15);
					
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						if(printLanguage.equals("2")){
							hashValues.put("DESCRIPTION", 33);
							hashValues.put("ARBDESCRIPTION", 29);
						}else if(printLanguage.equals("1")){
							hashValues.put("DESCRIPTION", 0);
							hashValues.put("ARBDESCRIPTION", 62);
						}else{
							hashValues.put("DESCRIPTION", 62);
							hashValues.put("ARBDESCRIPTION", 0);
						}
						
					} else {
						hashValues.put("ITEM#", 11);
						if(printLanguage.equals("2")){
							hashValues.put("DESCRIPTION", 22);
							hashValues.put("ARBDESCRIPTION", 29);
						}else if(printLanguage.equals("1")){
							hashValues.put("DESCRIPTION", 0);
							hashValues.put("ARBDESCRIPTION", 51);
						}else{
							hashValues.put("DESCRIPTION", 51);
							hashValues.put("ARBDESCRIPTION", 0);
						}
						
					}
				} else {
					hashValues.put("OUTLET CODE", 0);
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						if(printLanguage.equals("2")){
							hashValues.put("DESCRIPTION", 48);
							hashValues.put("ARBDESCRIPTION", 29);
						}else if(printLanguage.equals("1")){
							hashValues.put("DESCRIPTION", 0);
							hashValues.put("ARBDESCRIPTION", 77);
						}else{
							hashValues.put("DESCRIPTION", 77);
							hashValues.put("ARBDESCRIPTION", 0);
						}
						
					} else {
						hashValues.put("ITEM#", 11);
						if(printLanguage.equals("2")){
							hashValues.put("DESCRIPTION", 37);
							hashValues.put("ARBDESCRIPTION", 29);
						}else if(printLanguage.equals("1")){
							hashValues.put("DESCRIPTION", 0);
							hashValues.put("ARBDESCRIPTION", 66);
						}else{
							hashValues.put("DESCRIPTION", 66);
							hashValues.put("ARBDESCRIPTION", 0);
						}
						
					}
				}
			
				hashValues.put(barcode, 29);
				hashValues.put("UOM", 5);
				hashValues.put(qty, 8);
				hashValues.put("" + pcs, 0);
				hashValues.put("GROSS AMOUNT", 0);
					hashValues.put("EXCISE TAX", 0);
					hashValues.put("NET PRICE", 0);
					hashValues.put("VAT AMOUNT", 0);
					if (totaldiscount == 0 && totaldiscountval > 0) {
						hashValues.put("DISCOUNT", 17);
						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 4);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 6);
								hashValues.put(pcs + " PRICE", 6);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 12);
							}
							

						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 8);
								hashValues.put(pcs + " PRICE", 8);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 16);
							}
							
						}
					} else {
						hashValues.put("DISCOUNT", 0);

						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 4);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 15);
								hashValues.put(pcs + " PRICE", 14);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 29);
							}
							
						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 16);
								hashValues.put(pcs + " PRICE", 17);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 33);
							}
							
						}
					}
				

				hashValues.put("AMOUNT", 10);

				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("ARBDESCRIPTION", 2);
				hashPositions.put("UOM", 1);
				hashPositions.put("" + pcs, 1);
				hashPositions.put(qty, 1);
				hashPositions.put(cases + " PRICE", 2);
				hashPositions.put(pcs + " PRICE", 2);
				hashPositions.put(barcode, 2);
				hashPositions.put("GROSS AMOUNT", 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("EXCISE TAX", 2);
				hashPositions.put("VAT AMOUNT", 2);
				hashPositions.put("NET PRICE", 2);
				hashPositions.put("AMOUNT", 2);

			} else {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 4);
				hashValues.put("ITEM#", 8);
				hashValues.put("OUTLET CODE", 8);
				hashValues.put("DESCRIPTION", 36);
				hashValues.put("QTY CAS/PCS", 3);
				hashValues.put("QTY OUT/PCS", 3);
				if(CaseEnabled==1){
					hashValues.put("CASE PRICE", 7);
					hashValues.put("UNIT PRICE", 7);
				}else{
					hashValues.put("CASE PRICE", 0);
					hashValues.put("UNIT PRICE", 14);
				}
				
				hashValues.put("DISCOUNT", 0);
				hashValues.put("AMOUNT", 8);
				hashValues.put("OUTER PRICE", 7);
				hashValues.put("PCS PRICE", 7);
				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				 // hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("QTY CAS/PCS", 2);
				hashPositions.put("CASE PRICE", 2);
				hashPositions.put("UNIT PRICE", 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("AMOUNT", 2);
				hashPositions.put("OUTER PRICE", 2);
				hashPositions.put("PCS PRICE", 2);
			}
			// ---------Start

			// ----------End

			if (!object.getString("invoiceformat").equals("1")) { // header is
																	// printing
																	// instead
				line(startln);
			}
			headerprint(object, 1);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (jInnerData.length() > 0) {

					String header = mainJson.getString("TITLE").trim();
					String HeadTitle = "";
					if (header.equals("sales")) {
						if (object.getString("printlanguageflag").equals("2")) {
							HeadTitle = "SALES ";
						} else {

							HeadTitle = "SALES  *" + ArabicTEXT.Sales + "!";
						}

					} else if (header.equals("free")) {
						if (!object.getString("printlanguageflag").equals("2")) {

							HeadTitle = "TRADE DEAL  *" + ArabicTEXT.tradeDeal + "!";
						} else {
							HeadTitle = "TRADE DEAL";
						}

					} else if (header.equals("bad")) {

						if (!object.getString("printlanguageflag").equals("2")) {

							HeadTitle = "BAD RETURN  *" + ArabicTEXT.BadReturn + "!";
						} else {
							HeadTitle = "BAD RETURN";
						}

					} else if (header.equals("good")) {
						if (!object.getString("printlanguageflag").equals("2")) {

							HeadTitle = "GOOD RETURN  *" + ArabicTEXT.GoodReturn + "!";
						} else {
							HeadTitle = "GOOD RETURN";
						}

					} else if (header.equals("promofree")) {
						if (!object.getString("printlanguageflag").equals("2")) {

							HeadTitle = "PROMOTION FREE  *" + ArabicTEXT.PromotionFree + "!";
						} else {
							HeadTitle = "PROMOTION FREE";
						}

					} else if (header.equals("buyback")) {
						if (!object.getString("printlanguageflag").equals("2")) {
							HeadTitle = "BUYBACK FREE  *" + ArabicTEXT.BuybackFree + "!";
						} else {
							HeadTitle = "BUYBACK FREE";
						}

					}
					outStream.write(BoldOn);
					// outStream.write(NewLine);
					outStream.write("       ".getBytes());
					outStream.write(UnderlineOn);
					printlines2(HeadTitle, 1, object, 1, args[0], 1, 1);
					// outStream.write(NewLine);
					outStream.write(UnderlineOff);
					outStream.write(BoldOff);

				}
				int MAXLEngth = 137;

				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				String strheader = "", strHeaderBottom = "", strTotal = "";

				for (int j = 0; j < headers.length(); j++) {

					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(j));
					if (j == 4 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					strheader = strheader + getAccurateText(
							(j == 4 && object.getString("printbarcode").equals("0")) ? "DESCRIPTION"
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(j == 4 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("DESCRIPTION") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);


					printlines2(strheader, 1, object, 1, args[0], 1, 1);
					if (strHeaderBottom.length() > 0) {
						printlines2(strHeaderBottom, 1, object, 1, args[0], 1, 1);
					}
					// }

					outStream.write(CompressOff);

					outStream.write(CompressOn);
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);
				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";

					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 0) {
							itemDescrion = (l + 1) + "";

						} else if (m == 4) {
							itemDescrion = "";
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(m);
							} else {

								try {
									Arabic6822Length arabic6822Length=new Arabic6822Length();
									itemDescrion = "*" + arabic6822Length.ConvertLength(jArr.getString(m), false) + "!";
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
							
						strData = strData + getAccurateText(itemDescrion,
								m == 3 ? hashValues.get(headers.getString(m).toString()) + MAXLEngth
										: hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

					}

					// lp.writeLine(strData);
					outStream.write(CompressOn);
					isCompressModeOn = true;
					if (!object.getString("printlanguageflag").equals("2")) {
						// count++;
					}
					//
					printlines2(strData.trim(), 1, object, 1, args[0], 1, 1);

					outStream.write(CompressOff);
					isCompressModeOn = false;
				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					isCompressModeOn = true;
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					printlines2(strTotal, 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);
					isCompressModeOn = false;

				}

			}
			outStream.write(NewLine);
			
		 if (object.has("TOTAL SALES AMOUNT")&&object.has("TOTAL PROMO FREE AMOUNT")&&object.has("TOTAL FREE AMOUNT")) {
				
				double totalAmnt = Double.parseDouble(object.getString("TOTAL SALES AMOUNT"))+
						Double.parseDouble(object.getString("TOTAL PROMO FREE AMOUNT"))+
						Double.parseDouble(object.getString("TOTAL FREE AMOUNT"));
				
				String strAmnt = String.valueOf(totalAmnt);
				
				if (!object.getString("printlanguageflag").equals("2")) {

					printlines2(
							(getAccurateText("TOTAL SALES AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(strAmnt, 12, 0)
									+ getAccurateText(" : ", 3, 0) + "*"
									+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
							1, object, 1, args[0], 1, 1);
				} else {
					printlines2(
							(getAccurateText("TOTAL SALES AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(strAmnt, 12, 0)
									+ getAccurateText(" : ", 3, 0) + "*"
									+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
							1, object, 1, args[0], 1, 1);
				}
			}else if (object.has("TOTAL SALES AMOUNT")&&object.has("TOTAL PROMO FREE AMOUNT")) {
				
				double totalAmnt = Double.parseDouble(object.getString("TOTAL SALES AMOUNT"))+
						Double.parseDouble(object.getString("TOTAL PROMO FREE AMOUNT"));
				
				String strAmnt = String.valueOf(totalAmnt);
				
				if (!object.getString("printlanguageflag").equals("2")) {

					printlines2(
							(getAccurateText("TOTAL SALES AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(strAmnt, 12, 0)
									+ getAccurateText(" : ", 3, 0) + "*"
									+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
							1, object, 1, args[0], 1, 1);
				} else {
					printlines2(
							(getAccurateText("TOTAL SALES AMOUNT", 65, 2) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(strAmnt, 12, 2)),
							1, object, 1, args[0], 1, 1);
				}
			}else if (object.has("TOTAL SALES AMOUNT")) {
				if (!object.getString("printlanguageflag").equals("2")) {

					printlines2(
							(getAccurateText("TOTAL SALES AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("TOTAL SALES AMOUNT"), 12, 0)
									+ getAccurateText(" : ", 3, 0) + "*"
									+ getAccurateText(ArabicTEXT.TotalSalesAmount, 15, 2) + "!"),
							1, object, 1, args[0], 1, 1);
				} else {
					printlines2(
							(getAccurateText("TOTAL SALES AMOUNT", 65, 2) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("TOTAL SALES AMOUNT"), 12, 2)),
							1, object, 1, args[0], 1, 1);
				}
			}

			

			if (!object.getString("printlanguageflag").equals("2")) {

				if (object.has("TOTAL GOOD RETURN")&&object.has("TOTAL BUYBACK AMOUNT")) {
					
					double totalAmnt = Double.parseDouble(object.getString("TOTAL GOOD RETURN"))+
							Double.parseDouble(object.getString("TOTAL BUYBACK AMOUNT"));
					
					String strAmnt = String.valueOf(totalAmnt);
					
					
					printlines2(
							(getAccurateText("TOTAL GOOD RETURN", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(strAmnt, 12, 0)
									+ getAccurateText(" : ", 3, 0) + "*"
									+ getAccurateText(ArabicTEXT.TotalGoodReturn, 20, 2) + "!"),
							1, object, 1, args[0], 1, 1);

				}else if (object.has("TOTAL GOOD RETURN")) {
					printlines2(
							(getAccurateText("TOTAL GOOD RETURN", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("TOTAL GOOD RETURN"), 12, 0)
									+ getAccurateText(" : ", 3, 0) + "*"
									+ getAccurateText(ArabicTEXT.TotalGoodReturn, 20, 2) + "!"),
							1, object, 1, args[0], 1, 1);

				}
				
				
				if (object.has("TOTAL BAD RETURN")) {
					printlines2(
							(getAccurateText("TOTAL BAD RETURN", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("TOTAL BAD RETURN"), 12, 0)
									+ getAccurateText(" : ", 3, 0) + "*"
									+ getAccurateText(ArabicTEXT.TotalBadReturn, 20, 2) + "!"),
							1, object, 1, args[0], 1, 1);

				}
				if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
					double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

					if (invoice != 0) {
						if (!object.getString("printlanguageflag").equals("2")) {
							printlines2(
									(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						} else {
							printlines2(
									(getAccurateText("INVOICE DISCOUNT", 65, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)),
									1, object, 1, args[0], 1, 1);
						}
					}
				}
				printlines2(
						(getAccurateText("NET INVOICE AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
								+ getAccurateText(object.getString("NET SALES"), 12, 0) + getAccurateText(" : ", 3, 0)
								+ "*" + getAccurateText(ArabicTEXT.NetInvoiceAmount, 20, 2) + "!"),
						1, object, 1, args[0], 1, 1);

			} else {

		if (object.has("TOTAL GOOD RETURN")&&object.has("TOTAL BUYBACK AMOUNT")) {
					
					double totalAmnt = Double.parseDouble(object.getString("TOTAL GOOD RETURN"))+
							Double.parseDouble(object.getString("TOTAL BUYBACK AMOUNT"));
					
					String strAmnt = String.valueOf(totalAmnt);
					
					
					printlines2(
							(getAccurateText("TOTAL GOOD RETURN", 65, 2) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(strAmnt, 12, 2)),
							1, object, 1, args[0], 1, 1);

				}else if (object.has("TOTAL GOOD RETURN")) {
					printlines2(
							(getAccurateText("TOTAL GOOD RETURN", 65, 2) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("TOTAL GOOD RETURN"), 12, 2)),
							1, object, 1, args[0], 1, 1);

				}
				if (object.has("TOTAL BAD RETURN")) {
					printlines2(
							(getAccurateText("TOTAL BAD RETURN", 65, 2) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("TOTAL BAD RETURN"), 12, 2)),
							1, object, 1, args[0], 1, 1);

				}
				if (object.has("INVOICE DISCOUNT") && object.getString("INVOICE DISCOUNT").toString().length() > 0) {
					double invoice = Double.parseDouble(object.getString("INVOICE DISCOUNT"));

					if (invoice != 0) {
						if (!object.getString("printlanguageflag").equals("2")) {
							printlines2(
									(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						} else {
							printlines2(
									(getAccurateText("INVOICE DISCOUNT", 65, 2) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("INVOICE DISCOUNT"), 12, 2)),
									1, object, 1, args[0], 1, 1);
						}
					}
				}
				outStream.write(BoldOn);

				printlines2((getAccurateText("NET INVOICE AMOUNT", 65, 2) + getAccurateText(" : ", 3, 0)
						+ getAccurateText(object.getString("NET SALES"), 12, 2)), 1, object, 1, args[0], 1, 1);

				outStream.write(BoldOff);

			}

		
			printlines2("", 1, object, 1, args[0], 1, 1);
			if (object.has("PaymentType") && Integer.parseInt(object.getString("PaymentType")) < 2) {
				// position = position + 60;
				outStream.write(BoldOn);
				if (!object.getString("LANG").equals("en")) {

					printlines2(getAccurateText("*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args[0], 1,
							1);
				} else {
					printlines2(getAccurateText("PAYMENT DETAILS", 80, 1), 2, object, 1, args[0], 1, 1);

				}
				outStream.write(BoldOff);
				// lp.newLine(2);
				
				printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

				JSONArray jCheques = object.has("Cheque") ? object.getJSONArray("Cheque") : null;
				JSONObject jCash = object.has("Cash") ? object.getJSONObject("Cash") : null;
				int paymenttype = Integer.parseInt(object.getString("ptype"));

				switch (paymenttype) {
				case 0:

					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					outStream.write(BoldOff);
					break;
				case 1:
					outStream.write(BoldOn);
					// lp.write("CHEQUE");
					printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					// lp.newLine(2);
					printlines2(
							(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
									+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
										+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					printlines2("", 1, object, 1, args[0], 1, 1);
					// lp.newLine(2);
					break;
				case 2:
					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					printlines2("", 1, object, 1, args[0], 1, 1);
					printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					// lp.newLine(2);
					printlines2(
							(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
									+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
										+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					printlines2("", 1, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);

					break;
				default:
					break;
				}
			}

			
			if(object.has("crates")){
				printlines2(getAccurateText("Crates Delivered : "+object.getString("cratesDelivered"), 40, 0)
						+ getAccurateText("Crates Picked Up : "
				+object.getString("cratesPikedUp"), 40, 0), 2, object, 1, args[0], 1, 1);
			}
			
			
			if (object.getString("comments").toString().length() > 0) {
				if (object.getString("LANG").equals("en")) {
					printlines2("Comments:" + object.getString("comments"), 2, object, 1, args[0], 1, 1);
				} else {
					printlines2("Comments:*" + object.getString("comments") + "!", 2, object, 1, args[0], 1, 1);

				}
			}

			if (object.has("DeliveryNumber") && object.getString("DeliveryNumber").toString().length() > 0) {

				printlines2("Delivery Number : " + object.getString("DeliveryNumber"), 2, object, 1, args[0], 1, 1);

			}

			if (object.getString("invtrailormsg").toString().length() > 0) {
				printlines2(object.getString("invtrailormsg"), 2, object, 1, args[0], 1, 1);

			}
			
			String amountpaid=object.has("amountpaid")?object.getString("amountpaid"):"0";
			if(amountpaid!=null && !TextUtils.isEmpty(amountpaid) && Double.parseDouble(amountpaid)==0)
			{
				String invoiceamount=object.getString("NET SALES"); 
				double netsales=0;
				if(invoiceamount!=null && !TextUtils.isEmpty(invoiceamount)){
					netsales=Double.parseDouble(invoiceamount);
				}
		/*		String salesmemo=object.has("salesmemo")?object.getString("salesmemo"):"";
				if(salesmemo!=null && !TextUtils.isEmpty(salesmemo) && salesmemo.length()>0 && netsales>0){
					String[] tokens=salesmemo.split("[\\#]");
					if(tokens!=null && tokens.length>0){
						
						
						count=count+2;
						printlines2("", 1, object, 1, args[0], 1, 1);
						String token1=tokens[0].replace("***", object.getString("cname"));
						printlines2(token1, 1, object, 1, args[0], 1, 1);
						printlines2(tokens[1], 1, object, 1, args[0], 1, 1);
						printlines2(tokens[2], 1, object, 1, args[0], 1, 1);
					}
					
				}*/
				
				
			}
			
			
			
			Log.e("CountNOW", "" + count);
			printlines2("", 3, object, 1, args[0], 1, 1);
			if (object.getString("printlanguageflag").equals("2")) {

				if (object.has("TOTAL BAD RETURN") || object.has("totalFreeQty")) {
					
					printlines2(getAccurateText("______________________   ", 26, 1)+getAccurateText("______________________   ", 25, 1)
							+ getAccurateText("______________________   ", 25, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN", 26, 1) +getAccurateText("RECEIVED BY", 25, 1)+ getAccurateText("SIGN./STAMP", 25, 1), 2, object,
							1, args[0], 1, 1);
				} else {
					printlines2(getAccurateText("______________________   ", 26, 1)+getAccurateText("______________________   ", 25, 1)
					+ getAccurateText("______________________   ", 25, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN", 26, 1) +getAccurateText("RECEIVED BY", 25, 1)+ getAccurateText("SIGN./STAMP", 25, 1), 2, object,
					1, args[0], 1, 1);
				}

			} else {

				if (object.has("TOTAL BAD RETURN") || object.has("totalFreeQty")) {
					
					printlines2(getAccurateText("______________________   ", 23, 1)+getAccurateText("______________________   ", 27, 1)
							+ getAccurateText("______________________   ", 28, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *" + ArabicTEXT.SALESMAN
							+ "!", 23, 1)+getAccurateText("RECEIVED BY *" + ArabicTEXT.Customer+ "!", 27, 1) + getAccurateText("SIGNATURE/STAMP", 28, 1), 2, object,
							1, args[0], 1, 1);
					
					
					outStream.write(CompressOff);
				} else {
					printlines2(getAccurateText("______________________   ", 23, 1)+getAccurateText("______________________   ", 27, 1)
					+ getAccurateText("______________________   ", 28, 1), 2, object, 1, args[0], 1, 1);

					printlines2(getAccurateText("SALESMAN *" + ArabicTEXT.SALESMAN
					+ "!", 23, 1)+getAccurateText("RECEIVED BY *" + ArabicTEXT.Customer
							+ "!", 27, 1) + getAccurateText("SIGNATURE/STAMP", 28, 1), 2, object,
					1, args[0], 1, 1);
				}

			}
			
			String copyStatus = "";
			if (object.getString("printstatus").equals("DUPLICATE COPY")) {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1) + " ";
				} else {
					copyStatus = getAccurateText(
							object.getString("printstatus") + "  *" + ArabicTEXT.DuplicateCopy + "", 80, 1) + "!";
				}
			} else if (object.getString("printstatus").equals("ORIGINAL COPY")) {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1) + " ";
				} else {
					copyStatus = getAccurateText(
							object.getString("printstatus") + "  *" + ArabicTEXT.OriginalCopy + "!", 80, 1);
				}

			} else {
				if (object.getString("printlanguageflag").equals("2")) {
					copyStatus = getAccurateText(object.getString("printstatus"), 80, 1);
				} else {
					copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DraftCopy + "!",
							80, 1);
				}

			}

			printlines2(copyStatus, 2, object, 2, args[0], 1, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void printSalesKeyAccountReport(JSONObject object, String... args) {
		StringBuffer s1 = new StringBuffer();
		int printoultlet = 0;

		try {
			
			
			
			String cases = object.has("cases") ? object.getString("cases") : "Case";
			String pcs = object.has("pcs") ? object.getString("pcs") : "Unit";
			String qty = object.has("qty") ? object.getString("qty") : "QTY CAS/PCS";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			int CaseEnabled = object.has("CaseEnabled") ? Integer.parseInt(object.getString("CaseEnabled")) : 1;
			if (object.getString("printoutletitemcode").length() > 0) {
				printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
			} else {
				printoultlet = 0;
			}
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int printtax = Integer.parseInt(object.getString("printtax"));
			int totaldiscount = object.has("discountprint") ? Integer.parseInt(object.getString("discountprint")) : 0;
			double totaldiscountval = object.has("TOTAL LINE DISCOUNT")
					? Double.parseDouble(object.getString("TOTAL LINE DISCOUNT")) : 0;
			String totalpcs = object.has("totalpcs") ? object.getString("totalpcs") : "0";
			if (object.getString("displayupc").equals("1")) {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 0);
				hashValues.put("ITEM#", 23);
				hashValues.put("OUTLET CODE", 0);
				if(printLanguage.equals("2") || printLanguage.equals("1") ){
					hashValues.put("DESCRIPTION", 0);
					hashValues.put("ARBDESCRIPTION", 47);
				}else{
					hashValues.put("DESCRIPTION", 47);
					hashValues.put("ARBDESCRIPTION", 0);
				}
				
				
				hashValues.put(barcode, 0);
				hashValues.put("UOM", 9);
				hashValues.put(qty, 11);
				hashValues.put("" + pcs, 0);
				hashValues.put("GROSS AMOUNT", 0);
				hashValues.put("EXCISE TAX", 0);
				hashValues.put("NET PRICE", 0);
				hashValues.put("VAT AMOUNT", 0);
				hashValues.put("DISCOUNT",13);
				hashValues.put(cases + " PRICE", 0);
				hashValues.put(pcs + " PRICE", 13);
				hashValues.put("AMOUNT", 19);
					

				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 1);
				hashPositions.put("OUTLET CODE", 0);
				hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("ARBDESCRIPTION", 2);
				hashPositions.put("UOM", 1);
				hashPositions.put("" + pcs, 1);
				hashPositions.put(qty, 1);
				hashPositions.put(cases + " PRICE", 2);
				hashPositions.put(pcs + " PRICE", 1);
				hashPositions.put(barcode, 2);
				hashPositions.put("GROSS AMOUNT", 2);
				hashPositions.put("DISCOUNT", 1);
				hashPositions.put("EXCISE TAX", 2);
				hashPositions.put("VAT AMOUNT", 2);
				hashPositions.put("NET PRICE", 2);
				hashPositions.put("AMOUNT", 1);

			} else {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 4);
				hashValues.put("ITEM#", 8);
				hashValues.put("OUTLET CODE", 8);
				hashValues.put("DESCRIPTION", 36);
				hashValues.put("QTY CAS/PCS", 3);
				hashValues.put("QTY OUT/PCS", 3);
				if(CaseEnabled==1){
					hashValues.put("CASE PRICE", 7);
					hashValues.put("UNIT PRICE", 7);
				}else{
					hashValues.put("CASE PRICE", 0);
					hashValues.put("UNIT PRICE", 14);
				}
				
				hashValues.put("DISCOUNT", 0);
				hashValues.put("AMOUNT", 8);
				hashValues.put("OUTER PRICE", 7);
				hashValues.put("PCS PRICE", 7);
				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				 // hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("QTY CAS/PCS", 2);
				hashPositions.put("CASE PRICE", 2);
				hashPositions.put("UNIT PRICE", 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("AMOUNT", 2);
				hashPositions.put("OUTER PRICE", 2);
				hashPositions.put("PCS PRICE", 2);
			}
			// ---------Start

			// ----------End

			if (!object.getString("invoiceformat").equals("1")) { // header is
																	// printing
																	// instead
				line(startln);
			}
			
			outStream.write(CompressOff);
			headerKeyAccountprint(object, 1);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (jInnerData.length() > 0) {

					String header = mainJson.getString("TITLE").trim();
					String HeadTitle = "";
					if (header.equals("sales")) {
						if (object.getString("printlanguageflag").equals("2")) {
							HeadTitle = "SALES ";
						} else {
							HeadTitle = "SALES  *" + ArabicTEXT.Sales + "!";
						}

					} else if (header.equals("free")) {
						if (!object.getString("printlanguageflag").equals("2")) {

							HeadTitle = "TRADE DEAL  *" + ArabicTEXT.tradeDeal + "!";
						} else {
							HeadTitle = "TRADE DEAL";
						}

					} else if (header.equals("bad")) {

						if (!object.getString("printlanguageflag").equals("2")) {

							HeadTitle = "BAD RETURN  *" + ArabicTEXT.BadReturn + "!";
						} else {
							HeadTitle = "BAD RETURN";
						}

					} else if (header.equals("good")) {
						if (!object.getString("printlanguageflag").equals("2")) {

							HeadTitle = "GOOD RETURN  *" + ArabicTEXT.GoodReturn + "!";
						} else {
							HeadTitle = "GOOD RETURN";
						}

					} else if (header.equals("promofree")) {
						if (!object.getString("printlanguageflag").equals("2")) {

							HeadTitle = "PROMOTION FREE  *" + ArabicTEXT.PromotionFree + "!";
						} else {
							HeadTitle = "PROMOTION FREE";
						}

					} else if (header.equals("buyback")) {
						if (!object.getString("printlanguageflag").equals("2")) {
							HeadTitle = "BUYBACK FREE  *" + ArabicTEXT.BuybackFree + "!";
						} else {
							HeadTitle = "BUYBACK FREE";
						}

					}
					outStream.write(BoldOn);
					// outStream.write(NewLine);
					outStream.write("       ".getBytes());
					outStream.write(UnderlineOn);
					printlines3(HeadTitle, 1, object, 1, args[0], 1, 1);
					// outStream.write(NewLine);
					outStream.write(UnderlineOff);
					outStream.write(BoldOff);

				}
				int MAXLEngth = 137;

				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				String strheader = "", strHeaderBottom = "", strTotal = "";

				for (int j = 0; j < headers.length(); j++) {

					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(j));
					if (j == 4 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					strheader = strheader + getAccurateText(
							(j == 4 && object.getString("printbarcode").equals("0")) ? "DESCRIPTION"
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(j == 4 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("DESCRIPTION") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);


					/*printlines2(strheader, 1, object, 1, args[0], 1, 1);
					if (strHeaderBottom.length() > 0) {
						printlines2(strHeaderBottom, 1, object, 1, args[0], 1, 1);
					}
					// }

					outStream.write(CompressOff);

					outStream.write(CompressOn);
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);*/
				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";

					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 0) {
							itemDescrion = (l + 1) + "";

						} else if (m == 4) {
							itemDescrion = "";
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(m);
							} else {

								try {
									String arbDescription=jArr.getString(m);
									if(arbDescription.length()>45){
										arbDescription=arbDescription.substring(0,45);
									}
									
									
									Arabic6822Length arabic6822Length=new Arabic6822Length();
									itemDescrion = "*" + arabic6822Length.ConvertLength(arbDescription, false) + "!";
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
							
						strData = strData + getAccurateText(itemDescrion,
								m == 3 ? hashValues.get(headers.getString(m).toString()) + MAXLEngth
										: hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

					}

					// lp.writeLine(strData);
					outStream.write(CompressOn);
					isCompressModeOn = true;
					if (!object.getString("printlanguageflag").equals("2")) {
						// count++;
					}
					//
					printlines3(strData, 1, object, 1, args[0], 1, 1);

					outStream.write(CompressOff);
					isCompressModeOn = false;
				}
				if (jInnerData.length() > 0) {
					/*outStream.write(CompressOn);
					isCompressModeOn = true;
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					printlines2(strTotal, 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);
					isCompressModeOn = false;*/

				}

			}
			outStream.write(NewLine);
			
			printlines3("", 2, object, 2, args[0], 1, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private String printSepratorcomp() {
		String seprator = "";
		for (int i = 0; i < 137; i++) {
			seprator = seprator + "-";
		}
		return seprator;
	}

	private String printSeprator() {
		String seprator = "";
		for (int i = 0; i < 80; i++) {
			seprator = seprator + "-";
		}
		return seprator;
	}

	private String printSepratorCompress() {
		String seprator = "";
		for (int i = 0; i < 137; i++) {
			seprator = seprator + "-";
		}
		return seprator;
	}

	private String printEmptySpace(int count) {
		String space = "";
		for (int i = 0; i <= count; i++) {
			space = space + " ";
		}
		return space;
	}

	void parseCollectionResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Invoice#", 12);
			hashValues.put("Invoice Date", 14);
			hashValues.put("Due Date", 14);
			hashValues.put("Due Amount", 10);
			hashValues.put("Discount", 0);
			hashValues.put("Invoice Balance", 10);
			hashValues.put("Amount Paid", 10);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Invoice#", 0);
			hashPositions.put("Invoice Date", 2);
			hashPositions.put("Due Date", 2);
			hashPositions.put("Due Amount", 2);
			hashPositions.put("Discount", 2);
			hashPositions.put("Invoice Balance", 2);
			hashPositions.put("Amount Paid", 2);

			hashArbPositions = new HashMap<String, Integer>();
			hashArbPositions.put("Invoice#", 2);
			hashArbPositions.put("Invoice Date", 2);
			hashArbPositions.put("Due Date", 2);
			hashArbPositions.put("Due Amount", 0);
			hashArbPositions.put("Discount", 0);
			hashArbPositions.put("Invoice Balance", 0);
			hashArbPositions.put("Amount Paid", 0);

			hashArabVales = new HashMap<String, String>();
			hashArabVales.put("Invoice#", ArabicTEXT.Invoice);
			hashArabVales.put("Invoice Date", ArabicTEXT.InvoiceDate);
			hashArabVales.put("Due Date", ArabicTEXT.DueDate);
			hashArabVales.put("Due Amount", ArabicTEXT.InvoiceAmount);
			hashArabVales.put("Discount", ArabicTEXT.Discount);
			hashArabVales.put("Invoice Balance", ArabicTEXT.InvoiceBalance);
			hashArabVales.put("Amount Paid", ArabicTEXT.AmountPaid);

			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);
			headerprint(object, 2);

			JSONArray headers = object.getJSONArray("HEADERS");

			String strheader = "", strTotal = "", strHeaderBottom = "";
			int MAXLEngth = 80;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			JSONObject jTOBject = object.getJSONObject("TOTAL");
			for (int i = 0; i < headers.length(); i++) {

				String HeaderVal = "";

				if (!object.getString("LANG").equals("en")) {
					HeaderVal = hashArabVales.get(headers.getString(i));
					strheader = getAccurateText(
							(HeaderVal.indexOf(" ") == -1) ? HeaderVal : HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashArbPositions.get(headers.getString(i).toString())) + strheader;
					strHeaderBottom = getAccurateText(
							(HeaderVal.indexOf(" ") == -1) ? ""
									: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashArbPositions.get(headers.getString(i).toString())) + strHeaderBottom;
				} else {
					HeaderVal = headers.getString(i);

					strheader = strheader + getAccurateText(
							(HeaderVal.indexOf(" ") == -1) ? HeaderVal : HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));
					strHeaderBottom = strHeaderBottom + getAccurateText(
							(HeaderVal.indexOf(" ") == -1) ? ""
									: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));

				}

				if (jTOBject.has(headers.getString(i))) {
					strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(i).toString()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));
				} else {

					strTotal = strTotal + getAccurateText(headers.getString(i).equals("Due Date") ? "TOTAL" : "",
							hashValues.get(headers.getString(i)) + MAXLEngth, 1);
				}
			}
			Log.e("Header", "" + strheader);
			Log.e("Bottom", "" + strHeaderBottom);
			if (!object.getString("LANG").equals("en")) {
				printlines2("*" + strHeaderBottom + "!", 1, object, 1, args[0], 2, 2);
				printlines2("*" + strheader + "!", 1, object, 1, args[0], 2, 2);

			} else {
				printlines2(strheader, 1, object, 1, args[0], 2, 2);
				printlines2(strHeaderBottom, 1, object, 1, args[0], 2, 2);
			}
			printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					strData = strData + getAccurateText(jArr.getString(j),
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));
				}
				// position = position + 30;

				// s1.append(String.format(strFormat, position, strData) +
				// "\n");
				printlines2(strData, 1, object, 1, args[0], 2, 2);

			}
			printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);
			printlines2(strTotal, 2, object, 1, args[0], 2, 2);
			if (!object.getString("LANG").equals("en")) {
				printlines2(getAccurateText("*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args[0], 2, 2);

			} else {
				printlines2(getAccurateText("PAYMENT DETAILS", 80, 1), 2, object, 1, args[0], 2, 2);
			}

			// 0 Check only
			// 1 Cash Only
			// 2 Both

			JSONArray jCheques = object.has("Cheque") ? object.getJSONArray("Cheque") : null;
			JSONObject jCash = object.has("Cash") ? object.getJSONObject("Cash") : null;
			int paymenttype = Integer.parseInt(object.getString("ptype"));

			switch (paymenttype) {
			case 0:
				outStream.write(BoldOn);
				if (!object.getString("LANG").equals("en")) {

					printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1), 1,
							object, 1, args[0], 2, 2);
				} else {
					printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 1, object, 1, args[0], 2,
							2);
				}

				outStream.write(BoldOff);

				break;
			case 1:
				outStream.write(BoldOn);
				printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 2, 2);
				outStream.write(BoldOff);
				printlines2(
						(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
								+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
						1, object, 1, args[0], 2, 2);
				printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);

				for (int j = 0; j < jCheques.length(); j++) {
					JSONObject jChequeDetails = jCheques.getJSONObject(j);
					printlines2(
							getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
									+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
									+ getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
									+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2),
							1, object, 1, args[0], 2, 2);

				}
				printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);
				String receiptmemo=object.has("receiptmemo")?object.getString("receiptmemo"):"";
				if(receiptmemo!=null && !TextUtils.isEmpty(receiptmemo) && receiptmemo.length()>0){
					printlines2("", 1, object, 1, args[0], 2, 2);
					printlines2(receiptmemo, 1, object, 1, args[0], 2, 2);
				}
				break;
			case 2:

				outStream.write(BoldOn);
				if (!object.getString("LANG").equals("en")) {

					printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1), 1,
							object, 1, args[0], 2, 2);
				} else {
					printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 1, object, 1, args[0], 2,
							2);
				}

				printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 2, 2);

				printlines2(
						(getAccurateText("Cheque Date:", 15, 0) + getAccurateText("Cheque No:", 15, 0)
								+ getAccurateText("Bank:", 35, 0) + getAccurateText("Amount:", 15, 2)),
						1, object, 1, args[0], 2, 2);
				printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);

				for (int j = 0; j < jCheques.length(); j++) {
					JSONObject jChequeDetails = jCheques.getJSONObject(j);
					printlines2(
							getAccurateText(jChequeDetails.getString("Cheque Date"), 15, 0)
									+ getAccurateText(jChequeDetails.getString("Cheque No"), 15, 0)
									+getAccurateText(!object.getString("LANG").equals("en")?"*"+jChequeDetails.getString("Bank")+"!":jChequeDetails.getString("Bank"), 35, 0)
									+ getAccurateText(jChequeDetails.getString("Amount"), 15, 2),
							1, object, 1, args[0], 2, 2);

				}
				printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);
				
				String receiptmemo1=object.has("receiptmemo")?object.getString("receiptmemo"):"";
				if(receiptmemo1!=null && !TextUtils.isEmpty(receiptmemo1) && receiptmemo1.length()>0){
					printlines2("", 1, object, 1, args[0], 2, 2);
					printlines2(receiptmemo1, 1, object, 1, args[0], 2, 2);
					printlines2("", 1, object, 1, args[0], 2, 2);
					printlines2("", 1, object, 1, args[0], 2, 2);
				}
				
				outStream.write(BoldOff);
				break;

			default:
				break;
			}
			printlines2("", 1, object, 1, args[0], 2, 2);
			String exPayment = object.has("expayment") ? object.getString("expayment") : "";

			if (exPayment != null && exPayment.toString().trim().length() > 0) {

				printlines2(getAccurateText("Excess Payment : " + exPayment, 80, 0), 1, object, 1, args[0], 2, 2);
			}
			if (object.has("customerbalance") && object.getString("customerbalance").length() > 0) {
				double invoicebalance = Double.parseDouble(object.getString("customerbalance"));
				if (invoicebalance > 0) {
					printlines2(
							(getAccurateText("CUSTOMER BALANCE", 20, 0) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("customerbalance"), 12, 0)),
							1, object, 1, args[0], 2, 2);
				}
			}
			
			if (object.getString("comments").toString().length() > 0) {
				printlines2(getAccurateText("Comments: " + object.getString("comments"), 80, 0), 3, object, 1, args[0],
						2, 2);

			} else {
				printlines2(" ", 2, object, 1, args[0], 2, 2);
			}
			printlines2(getAccurateText("CUSTOMER SIGN________*" + ArabicTEXT.Customer + "!    SALESMAN_______________*"
					+ ArabicTEXT.Salesman + "!", 80, 1), 2, object, 1, args[0], 2, 2);

			String copyStatus = "";
			if (object.getString("printstatus").equals("DUPLICATE COPY")) {

				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DuplicateCopy + "",
						80, 1) + "!";
			} else if (object.getString("printstatus").equals("ORIGINAL COPY")) {
				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.OriginalCopy + "!",
						80, 1);

			} else {
				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DraftCopy + "!", 80,
						1);

			}
			printlines2("", 1, object, 1, args[0], 2, 2);
			printlines2(copyStatus, 2, object, 2, args[0], 2, 2);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// return String.valueOf(s1);
	}

	void parseAgingAnalysisResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Invoice#", 14);
			hashValues.put("Invoice Date", 12);
			hashValues.put("Due Date", 12);
			hashValues.put("Due Amount", 15);
			hashValues.put("Salesman", 15);
			hashValues.put("PDC Date", 10);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Invoice#", 0);
			hashPositions.put("Invoice Date", 0);
			hashPositions.put("Due Date", 1);
			hashPositions.put("Due Amount", 2);
			hashPositions.put("Salesman", 1);
			hashPositions.put("PDC Date", 1);

			line(startln);
			headerprint(object, 8);

			JSONArray headers = object.getJSONArray("HEADERS");

			String strheader = "", strTotal = "", strHeaderBottom = "";
			int MAXLEngth = 80;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			JSONObject jTOBject = object.getJSONObject("TOTAL");
			for (int i = 0; i < headers.length(); i++) {

				String HeaderVal = "";

				HeaderVal = headers.getString(i);

				strheader = strheader + getAccurateText(
						(HeaderVal.indexOf(" ") == -1) ? HeaderVal : HeaderVal.substring(0, HeaderVal.indexOf(" ")),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));
				strHeaderBottom = strHeaderBottom + getAccurateText(
						(HeaderVal.indexOf(" ") == -1) ? ""
								: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length()),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));

				if (jTOBject.has(headers.getString(i))) {
					strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(i).toString()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));
				} else {

					strTotal = strTotal + getAccurateText(headers.getString(i).equals("Due Date") ? "TOTAL" : "",
							hashValues.get(headers.getString(i)) + MAXLEngth, 1);
				}
			}
			Log.e("Header", "" + strheader);
			Log.e("Bottom", "" + strHeaderBottom);

			printlines2(strheader, 1, object, 1, args[0], 2, 2);
			printlines2(strHeaderBottom, 1, object, 1, args[0], 2, 2);

			printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					strData = strData + getAccurateText(jArr.getString(j),
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));
				}

				printlines2(strData, 1, object, 1, args[0], 2, 2);

			}
			printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);
			printlines2(strTotal, 2, object, 1, args[0], 2, 2);

			printlines2(getAccurateText("CUSTOMER_____________*" + ArabicTEXT.Customer + "!    SALESMAN_______________*"
					+ ArabicTEXT.Salesman + "!", 80, 1), 2, object, 2, args[0], 2, 2);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void parseAdvancePaymentResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Invoice#", 25);
			hashValues.put("Invoice Date", 25);
			hashValues.put("Invoice Amount", 25);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Invoice#", 0);
			hashPositions.put("Invoice Date", 0);
			hashPositions.put("Invoice Amount", 2);

			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);
			headerprint(object, 2);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strTotal = "", strHeaderBottom = "";
			int MAXLEngth = 80;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			JSONObject jTOBject = object.getJSONObject("TOTAL");
			for (int i = 0; i < headers.length(); i++) {

				strheader = strheader + getAccurateText(
						(headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
								: headers.getString(i).substring(0, headers.getString(i).indexOf(" ")),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));
				strHeaderBottom = strHeaderBottom + getAccurateText(
						(headers.getString(i).indexOf(" ") == -1) ? ""
								: headers.getString(i).substring(headers.getString(i).indexOf(" "),
										headers.getString(i).length()),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));

				if (jTOBject.has(headers.getString(i))) {
					strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(i).toString()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));
				} else {

					strTotal = strTotal + getAccurateText(headers.getString(i).equals("Invoice Date") ? "TOTAL" : "",
							hashValues.get(headers.getString(i)) + MAXLEngth, 1);
				}
			}

			printlines2(strheader, 1, object, 1, args[0], 2, 2);
			printlines2(strHeaderBottom, 1, object, 1, args[0], 2, 2);
			printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					strData = strData + getAccurateText(jArr.getString(j),
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));
				}
				// position = position + 30;

				// s1.append(String.format(strFormat, position, strData) +
				// "\n");
				printlines2(strData, 1, object, 1, args[0], 2, 2);

			}
			printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);
			// printlines2(strTotal, 2, object, 1, args[0], 2, 2);

			printlines2(getAccurateText("PAYMENT DETAILS", 80, 1), 2, object, 1, args[0], 2, 2);

			// 0 Check only
			// 1 Cash Only
			// 2 Both

			JSONArray jCheques = object.has("Cheque") ? object.getJSONArray("Cheque") : null;
			JSONObject jCash = object.has("Cash") ? object.getJSONObject("Cash") : null;

			switch (Integer.parseInt(object.getString("PaymentType"))) {
			case 0:
				outStream.write(BoldOn);
				printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 1, object, 1, args[0], 2, 2);
				outStream.write(BoldOff);

				break;
			case 1:
				outStream.write(BoldOn);
				printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 2, 2);
				outStream.write(BoldOff);
				printlines2(
						(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0)
								+ getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2)),
						1, object, 1, args[0], 2, 2);
				printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);

				for (int j = 0; j < jCheques.length(); j++) {
					JSONObject jChequeDetails = jCheques.getJSONObject(j);
					printlines2(
							getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0)
									+ getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0)
									+ getAccurateText(jChequeDetails.getString("Bank"), 20, 0)
									+ getAccurateText(jChequeDetails.getString("Amount"), 20, 2),
							1, object, 1, args[0], 2, 2);

				}
				printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);

				break;
			case 2:

				outStream.write(BoldOn);
				printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0], 2, 2);
				outStream.write(BoldOff);
				outStream.write(BoldOn);
				printlines2(getAccurateText("CHEQUE", 80, 1), 1, object, 2, args[0], 2, 2);
				outStream.write(BoldOff);
				printlines2(
						getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0)
								+ getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2),
						1, object, 1, args[0], 2, 2);

				printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);

				for (int j = 0; j < jCheques.length(); j++) {
					JSONObject jChequeDetails = jCheques.getJSONObject(j);
					printlines2(
							getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0)
									+ getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0)
									+ getAccurateText(jChequeDetails.getString("Bank"), 20, 0)
									+ getAccurateText(jChequeDetails.getString("Amount"), 20, 2),
							1, object, 1, args[0], 2, 2);

				}
				printlines2(printSeprator(), 1, object, 1, args[0], 2, 2);
				break;

			default:
				break;
			}

			String exPayment = object.has("expayment") ? object.getString("expayment") : "";

			if (exPayment != null && exPayment.toString().trim().length() > 0) {

				printlines2(getAccurateText("Excess Payment : " + exPayment, 80, 0), 1, object, 1, args[0], 2, 2);
			}
			if (object.getString("comments").toString().length() > 0) {
				printlines2(getAccurateText("Comments: " + object.getString("comments"), 80, 0), 3, object, 1, args[0],
						2, 2);

			} else {
				printlines2(" ", 2, object, 1, args[0], 2, 2);
			}
			printlines2(getAccurateText(("CUSTOMER_________________             SALESMAN_______________ "), 80, 1), 2,
					object, 1, args[0], 2, 2);
			printlines2(getAccurateText(object.getString("printstatus"), 80, 1), 2, object, 2, args[0], 2, 2);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// return String.valueOf(s1);
	}

	void parseEndInventory(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {

			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int inventoryvalueprint = Integer.valueOf(object.getString("inventoryvalueprint"));
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Sl#", 4);

			if (printitemcode == 0) {
				hashValues.put("Item#", 0);
				
				if(printLanguage.equals("2")){
					hashValues.put("Description", 49);
					hashValues.put("ArabDescription", 43);
				}else if(printLanguage.equals("1")){
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 92);
				}else{
					hashValues.put("Description", 92);
					hashValues.put("ArabDescription", 0);
				}
				
//				hashValues.put("Description", 49);
//				hashValues.put("ArabDescription", 43);
			} else {
				hashValues.put("Item#", 10);
				
				if(printLanguage.equals("2")){
					hashValues.put("Description", 39);
					hashValues.put("ArabDescription", 43);
				}else if(printLanguage.equals("1")){
					hashValues.put("Description", 0);
					hashValues.put("ArabDescription", 82);
				}else{
					hashValues.put("Description", 82);
					hashValues.put("ArabDescription", 0);
				}
				
//				hashValues.put("Description", 39);
//				hashValues.put("ArabDescription", 43);
			}

			hashValues.put(barcode, 43);
			hashValues.put("UOM", 0);
			hashValues.put("Truck Stock", 10);
			hashValues.put("Fresh Unload", 10);
			hashValues.put("Truck Damage", 0);
			hashValues.put("Closing Stock", 10);
			hashValues.put("Variance Qty", 10);
			hashValues.put("Total Value", 0);
			// hashValues.put("Description", 40);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Sl#", 0);
			hashPositions.put("Item#", 0);
			hashPositions.put("Description", 0);
			hashPositions.put("ArabDescription", 2);
			hashPositions.put("UOM", 2);
			hashPositions.put("Truck Stock", 2);
			hashPositions.put("Fresh Unload", 2);
			hashPositions.put("Truck Damage", 2);
			hashPositions.put("Closing Stock", 2);
			hashPositions.put("Variance Qty", 2);
			hashPositions.put("Total Value", 2);
			hashPositions.put("Description", 0);
			hashPositions.put(barcode, 2);
			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);
			headerinvprint(object, 3);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";
			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}
			String strTotal = "";
			JSONArray jTotal = object.getJSONArray("TOTAL");
			JSONObject jTOBject = jTotal.getJSONObject(0);
			for (int i = 0; i < headers.length(); i++) {

				try {
					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(i));
					if (i == 3 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					strheader = strheader + getAccurateText(
							(i==3 && object.getString("printbarcode").equals("0")) ? "DESCRIPTION"
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(i==3 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));
					if (jTOBject.has(headers.getString(i))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(i).toString()),
								hashValues.get(headers.getString(i).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(i).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(i).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(i)) + MAXLEngth, 1);
					}
				} catch (Exception e) {

				}
			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 3);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 3);
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 1);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					String itemDescrion = jArr.getString(j);

					if (j == 0) {
						itemDescrion = (i + 1) + "";
					}

					if (j == 3) {
						if (object.getString("printbarcode").equals("1")) {
							itemDescrion = jArr.getString(j); // printing
																// barcode
						} else {
							itemDescrion = "*" + jArr.getString(j) + "!";
						}

					}

					strData = strData
							+ getAccurateText(itemDescrion, hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));
				}

				// s1.append(String.format(strFormat, position, strData) +
				// "\n");
				// count++;
				printlines1(strData, 1, object, 1, args[0], 3);

			}
			printlines1(printSepratorcomp(), 1, object, 1, args[0], 3);

			printlines1(strTotal, 1, object, 1, args[0], 3);
			outStream.write(CompressOff);
			outStream.write(BoldOn);
			outStream.write(NewLine);

			if (inventoryvalueprint != 0) {
				printlines1((getAccurateText("END INVENTORY VALUE : ", 70, 2)
						+ getAccurateText(object.getString("closevalue"), 10, 2)), 1, object, 1, args[0], 2);
				
				printlines1((getAccurateText("FRESH UNLOAD VALUE : ", 70, 2)
						+ getAccurateText(object.getString("freshunloadvalue"), 10, 2)), 1, object, 1, args[0], 2);
				
				
				
				printlines1((getAccurateText("VARIANCE VALUE : ", 70, 2)
						+ getAccurateText(object.getString("variancevalue"), 10, 2)), 1, object, 1, args[0], 2);
				outStream.write(NewLine);
			}

			/*
			 * printlines1((getAccurateText("Available Inventory : ", 40, 2) +
			 * getAccurateText(object.getString("availvalue"), 30, 1)), 1,
			 * object, 1, args[0], 2); printlines1((getAccurateText(
			 * "Unload Inventory : ", 40, 2) +
			 * getAccurateText(object.getString("unloadvalue"), 30, 1)), 1,
			 * object, 1, args[0], 2); printlines1(printSeprator(), 1, object,
			 * 1, args[0], 3); printlines1((getAccurateText(
			 * "Calculated Inventory : ", 40, 2) +
			 * getAccurateText(object.getString("closevalue"), 30, 1)), 1,
			 * object, 1, args[0], 2); printlines1(printSeprator(), 1, object,
			 * 1, args[0], 3);
			 */
			outStream.write(BoldOff);
			// s1.append(String.format(strFormat, position + 30,
			// printSeprator())+ "\n");
			printlines1(" ", 2, object, 1, args[0], 3);
			printlines1(
					getAccurateText("STORE KEEPER___________", 40, 0) + getAccurateText("SALESMAN__________", 40, 2), 2,
					object, 1, args[0], 3);
			printlines1(getAccurateText(object.has("printstatus") ? object.getString("printstatus") : "", 80, 1), 2,
					object, 2, args[0], 3);

		} catch (Exception e) {

		}

	}

	/*
	 * void parseDepositResponse(final JSONObject object, final String... args)
	 * { StringBuffer s1 = new StringBuffer(); try { hashValues = new
	 * HashMap<String, Integer>(); hashValues.put("Transaction Number", 12);
	 * hashValues.put("Customer Code", 12); hashValues.put("Customer Name", 25);
	 * hashValues.put("Cheque No", 12); hashValues.put("Cheque Date", 11);
	 * hashValues.put("Bank Name", 14); hashValues.put("Cheque Amount", 12);
	 * hashValues.put("Amount", 10); hashPositions = new HashMap<String,
	 * Integer>(); hashPositions.put("Transaction Number", 0);
	 * hashPositions.put("Customer Code", 0); hashPositions.put("Customer Name",
	 * 0); hashPositions.put("Cheque No", 0); hashPositions.put("Cheque Date",
	 * 0); hashPositions.put("Bank Name", 0); hashPositions.put("Cheque Amount",
	 * 2); hashPositions.put("Amount", 2);
	 * 
	 * // ---------Start // printconnect(args[0]); // ----------End
	 * 
	 * line(startln); headerprint(object, 3);
	 * 
	 * JSONArray jData = object.getJSONArray("data"); for (int i = 0; i <
	 * jData.length(); i++) { JSONObject mainJson = jData.getJSONObject(i);
	 * JSONArray jInnerData = mainJson.getJSONArray("DATA"); JSONArray headers =
	 * mainJson.getJSONArray("HEADERS"); JSONObject jTotal =
	 * mainJson.getJSONObject("TOTAL");
	 * 
	 * if (jInnerData.length() > 0) {
	 * 
	 * switch (i) { case 0: outStream.write(BoldOn); outStream.write("       "
	 * .getBytes()); outStream.write(UnderlineOn); printlines2("CASH", 1,
	 * object, 1, args[0], 3, 3); outStream.write(UnderlineOff);
	 * outStream.write(BoldOff);
	 * 
	 * break; case 1: outStream.write(BoldOn); outStream.write("       "
	 * .getBytes()); outStream.write(UnderlineOn); printlines2("CHEQUE", 1,
	 * object, 1, args[0], 3, 3); outStream.write(UnderlineOff);
	 * outStream.write(BoldOff); break; default: break; } } int MAXLEngth = 80;
	 * for (int k = 0; k < headers.length(); k++) {
	 * 
	 * MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());
	 * } if (MAXLEngth > 0) { MAXLEngth = (int) MAXLEngth / headers.length(); }
	 * 
	 * String strheader = "", strHeaderBottom = "", strTotal = ""; for (int j =
	 * 0; j < headers.length(); j++) {
	 * 
	 * strheader = strheader + getAccurateText( (headers.getString(j).indexOf(
	 * " ") == -1) ? headers.getString(j) : headers.getString(j).substring(0,
	 * headers.getString(j).indexOf(" ")) .trim(),
	 * hashValues.get(headers.getString(j).toString()) + MAXLEngth,
	 * hashPositions.get(headers.getString(j).toString()));
	 * 
	 * strHeaderBottom = strHeaderBottom + getAccurateText(
	 * (headers.getString(j).indexOf(" ") == -1) ? "" : headers.getString(j)
	 * .substring(headers.getString(j).indexOf(" "),
	 * headers.getString(j).length()) .trim(),
	 * hashValues.get(headers.getString(j).toString()) + MAXLEngth,
	 * hashPositions.get(headers.getString(j).toString()));
	 * 
	 * if (jTotal.has(headers.getString(j))) { strTotal = strTotal +
	 * getAccurateText(jTotal.getString(headers.getString(j).toString()),
	 * hashValues.get(headers.getString(j).toString()) + MAXLEngth,
	 * hashPositions.get(headers.getString(j).toString())); } else {
	 * 
	 * strTotal = strTotal + getAccurateText( headers.getString(j).equals(i == 0
	 * ? "Customer Code" : "Cheque Date") ? "SUB TOTAL" : "",
	 * hashValues.get(headers.getString(j)) + MAXLEngth, 1); }
	 * 
	 * } if (jInnerData.length() > 0) { printlines2(strheader, 1, object, 1,
	 * args[0], 3, 3); printlines2(strHeaderBottom, 1, object, 1, args[0], 3,
	 * 3); printlines2(printSeprator(), 1, object, 1, args[0], 3, 3);
	 * 
	 * }
	 * 
	 * for (int l = 0; l < jInnerData.length(); l++) { JSONArray jArr =
	 * jInnerData.getJSONArray(l); String strData = ""; for (int m = 0; m <
	 * jArr.length(); m++) { strData = strData +
	 * getAccurateText(jArr.getString(m),
	 * hashValues.get(headers.getString(m).toString()) + MAXLEngth,
	 * hashPositions.get(headers.getString(m).toString())); }
	 * printlines2(strData, 1, object, 1, args[0], 3, 3);
	 * 
	 * } if (jInnerData.length() > 0) { printlines2(printSeprator(), 1, object,
	 * 1, args[0], 3, 3); printlines2(strTotal, 2, object, 1, args[0], 3, 3); }
	 * 
	 * } outStream.write(BoldOn); String totalAmt = object.getString(
	 * "TOTAL DEPOSIT AMOUNT"); String varAmt =
	 * object.getString("totalvaramount"); String cashvariance =
	 * object.getString("cashvariance"); printlines2((getAccurateText(
	 * "TOTAL DEPOSIT AMOUNT", 67, 2) + getAccurateText(totalAmt, 16, 1)), 1,
	 * object, 1, args[0], 3, 3); printlines2((getAccurateText(
	 * "INVENTORY VARIANCE AMOUNT", 67, 2) + getAccurateText(varAmt, 16, 1)), 1,
	 * object, 1, args[0], 3, 3); printlines2((getAccurateText(
	 * "CASH VARIANCE AMOUNT", 67, 2) + getAccurateText(cashvariance, 16, 1)),
	 * 1, object, 1, args[0], 3, 3); if (totalAmt.length() > 0 &&
	 * varAmt.length() > 0) { float totalCount = Float.parseFloat(totalAmt) +
	 * Float.parseFloat(varAmt);
	 * 
	 * int decimal_count = totalAmt.substring(totalAmt.indexOf(".") + 1,
	 * totalAmt.length()).length(); printlines2( getAccurateText(
	 * "NET DUE AMOUNT", 67, 2) + getAccurateText(String.format("%." +
	 * decimal_count + "f", totalCount), 16, 1), 1, object, 1, args[0], 3, 3); }
	 * 
	 * outStream.write(BoldOff); printlines2(" ", 2, object, 1, args[0], 3, 3);
	 * printlines2(getAccurateText("SALES REP", 26, 0) +
	 * getAccurateText("SUPERVISOR", 26, 0) + getAccurateText("ACCOUNTANT", 26,
	 * 0), 1, object, 1, args[0], 3, 3);
	 * 
	 * printlines2(" ", 2, object, 1, args[0], 3, 3);
	 * 
	 * printlines2(getAccurateText("Name______________", 26, 0) +
	 * getAccurateText("Name______________", 26, 0) +
	 * getAccurateText("Name______________", 26, 0), 1, object, 1, args[0], 3,
	 * 3);
	 * 
	 * printlines2(" ", 2, object, 1, args[0], 3, 3);
	 * 
	 * printlines2(getAccurateText("Date______________", 26, 0) +
	 * getAccurateText("Date______________", 26, 0) +
	 * getAccurateText("Date______________", 26, 0), 1, object, 1, args[0], 3,
	 * 3);
	 * 
	 * printlines2(" ", 2, object, 1, args[0], 3, 3);
	 * 
	 * printlines2("Deposited On : ", 2, object, 1, args[0], 1, 1); printlines2(
	 * "Bank : ", 2, object, 2, args[0], 1, 1);
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * // return String.valueOf(s1); }
	 */
	void parseDepositResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Transaction Number", 15);
			hashValues.put("Customer Code", 15);
			hashValues.put("Customer Name", 90);
			hashValues.put("Cheque No", 0);
			hashValues.put("Cheque Date", 0);
			hashValues.put("Bank Name", 0);
			hashValues.put("Cheque Amount", 0);
			hashValues.put("Amount", 17);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Transaction Number", 0);
			hashPositions.put("Customer Code", 0);
			hashPositions.put("Customer Name", 0);
			hashPositions.put("Cheque No", 0);
			hashPositions.put("Cheque Date", 0);
			hashPositions.put("Bank Name", 0);
			hashPositions.put("Cheque Amount", 2);
			hashPositions.put("Amount", 2);

			// ---------Start
			// printconnect(args[0]);
			// ----------End

			line(startln);

			headerprint(object, 3);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");

				if (jInnerData.length() > 0) {

					switch (i) {
					case 0:
						outStream.write(BoldOn);
						outStream.write("       ".getBytes());
						outStream.write(UnderlineOn);
						printlines2("CASH", 1, object, 1, args[0], 3, 3);
						outStream.write(UnderlineOff);
						outStream.write(BoldOff);
						hashValues.put("Customer Name", 90);
						hashValues.put("Cheque No", 0);
						hashValues.put("Cheque Date", 0);
						hashValues.put("Bank Name", 0);
						hashValues.put("Cheque Amount", 0);
						hashValues.put("Amount", 17);

						break;
					case 1:
						outStream.write(BoldOn);
						outStream.write("       ".getBytes());
						outStream.write(UnderlineOn);
						printlines2("CHEQUE", 1, object, 1, args[0], 3, 3);
						outStream.write(UnderlineOff);
						outStream.write(BoldOff);
						hashValues.put("Customer Name", 50);
						hashValues.put("Cheque No", 10);
						hashValues.put("Cheque Date", 15);
						hashValues.put("Bank Name", 15);
						hashValues.put("Cheque Amount", 16);
						hashValues.put("Amount", 0);
						break;
					default:
						break;
					}
				}
				int MAXLEngth = 137;
				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());
				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}
				outStream.write(CompressOn);
				String strheader = "", strHeaderBottom = "", strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					strheader = strheader
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? headers.getString(j)
											: headers.getString(j).substring(0, headers.getString(j).indexOf(" "))
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? ""
											: headers.getString(j)
													.substring(headers.getString(j).indexOf(" "),
															headers.getString(j).length())
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal
								+ getAccurateText(
										headers.getString(j).equals(i == 0 ? "Customer Code" : "Cheque Date")
												? "SUB TOTAL" : "",
										hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					printlines2(strheader, 1, object, 1, args[0], 3, 3);
					printlines2(strHeaderBottom, 1, object, 1, args[0], 3, 3);
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 3, 3);

				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					for (int m = 0; m < jArr.length(); m++) {
						strData = strData + getAccurateText(jArr.getString(m),
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));
					}
					printlines2(strData, 1, object, 1, args[0], 3, 3);

				}
				if (jInnerData.length() > 0) {
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 3, 3);
					printlines2(strTotal, 2, object, 1, args[0], 3, 3);
				}

			}
			outStream.write(CompressOff);

			boolean isPrint = false;
			if (isPrint) {
				JSONArray paytype = object.getJSONArray("PAYTYPE");
				double totalAmount = 0;
				for (int i = 0; i < paytype.length(); i++) {
					String cashDes = "", cash = "";
					JSONArray jArr = paytype.getJSONArray(i);
					cashDes = jArr.getString(0);
					cash = jArr.getString(1);
					if (cash == null && cash.toString().length() > 0) {
						cash = "0";
					}
					totalAmount = totalAmount + Double.parseDouble(cash);
					printlines2((getAccurateText(cashDes, 40, 2) + getAccurateText(" X    " + cash + " ", 38, 0)), 1,
							object, 1, args[0], 3, 3);

				}
				printlines2(printSeprator(), 1, object, 1, args[0], 3, 3);
				printlines2(
						(getAccurateText("TOTAL CASH", 40, 2) + getAccurateText(" X    " + totalAmount + " ", 38, 0)),
						1, object, 1, args[0], 3, 3);

				printlines2(printSeprator(), 1, object, 1, args[0], 3, 3);
				printlines2(" ", 1, object, 1, args[0], 3, 3);
				outStream.write(BoldOn);
				String totalAmt = object.getString("TOTAL DEPOSIT AMOUNT");
				String varAmt = object.getString("totalvaramount");
				printlines2((getAccurateText("TOTAL DEPOSIT AMOUNT", 67, 2) + getAccurateText(totalAmt, 16, 1)), 1,
						object, 1, args[0], 3, 3);
				if (varAmt != null && Integer.parseInt(varAmt) > 0) {
					printlines2((getAccurateText("TOTAL VARIENCE AMOUNT", 67, 2) + getAccurateText(varAmt, 16, 1)), 1,
							object, 1, args[0], 3, 3);
				}
				if (totalAmt.length() > 0 && varAmt.length() > 0) {
					float totalCount = Float.parseFloat(totalAmt) + Float.parseFloat(varAmt);

					int decimal_count = totalAmt.substring(totalAmt.indexOf(".") + 1, totalAmt.length()).length();
					printlines2(
							getAccurateText("NET DUE AMOUNT", 67, 2)
									+ getAccurateText(String.format("%." + decimal_count + "f", totalCount), 16, 1),
							1, object, 1, args[0], 3, 3);
				}
			}
			outStream.write(BoldOff);
			printlines2(" ", 2, object, 1, args[0], 3, 3);
			printlines2(getAccurateText("SALES REP______________", 26, 0)
					+ getAccurateText("SUPERVISOR______________", 26, 0)
					+ getAccurateText("ACCOUNTANT______________", 26, 0), 1, object, 2, args[0], 3, 3);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// return String.valueOf(s1);
	}

	// --------------
	void parseDepositCashResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Transaction Number", 12);
			hashValues.put("Customer Code", 12);
			hashValues.put("Customer Name", 90);
			hashValues.put("Cheque No", 0);
			hashValues.put("Cheque Date", 0);
			hashValues.put("Bank Name", 0);
			hashValues.put("Cheque Amount", 0);
			hashValues.put("Amount", 17);
			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Transaction Number", 0);
			hashPositions.put("Customer Code", 0);
			hashPositions.put("Customer Name", 0);
			hashPositions.put("Cheque No", 0);
			hashPositions.put("Cheque Date", 0);
			hashPositions.put("Bank Name", 0);
			hashPositions.put("Cheque Amount", 2);
			hashPositions.put("Amount", 2);

			// ---------Start
			// printconnect(args[0]);
			// ----------End

			line(startln);
			headerprint(object, 3);
			
			double totalAmountexpense = 0;
			double totalAmountcash = 0;

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");

				if (jInnerData.length() > 0) {

					switch (i) {
					case 0:
						outStream.write(BoldOn);
						printlines2(" ", 1, object, 1, args[0], 3, 3);
						outStream.write(UnderlineOn);
						printlines2("CASH", 1, object, 1, args[0], 3, 3);
						outStream.write(UnderlineOff);
						outStream.write(BoldOff);
						hashValues.put("Customer Name", 90);
						hashValues.put("Cheque No", 0);
						hashValues.put("Cheque Date", 0);
						hashValues.put("Bank Name", 0);
						hashValues.put("Cheque Amount", 0);
						hashValues.put("Amount", 17);

						break;
					case 1:
						outStream.write(BoldOn);
						printlines2(" ", 1, object, 1, args[0], 3, 3);
						outStream.write(CompressOff);
						outStream.write(UnderlineOn);

						printlines2("CHEQUE", 1, object, 1, args[0], 3, 3);
						outStream.write(UnderlineOff);
						outStream.write(BoldOff);
						hashValues.put("Customer Name", 50);
						hashValues.put("Cheque No", 10);
						hashValues.put("Cheque Date", 15);
						hashValues.put("Bank Name", 15);
						hashValues.put("Cheque Amount", 16);
						hashValues.put("Amount", 0);
						break;
					default:
						break;
					}
				}
				int MAXLEngth = 137;
				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());
				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				outStream.write(CompressOn);
				String strheader = "", strHeaderBottom = "", strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					strheader = strheader
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? headers.getString(j)
											: headers.getString(j).substring(0, headers.getString(j).indexOf(" "))
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? ""
											: headers.getString(j)
													.substring(headers.getString(j).indexOf(" "),
															headers.getString(j).length())
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						String toatlString=i==0?"TOTAL CASH":"TOTAL CHECQUE";
						strTotal = strTotal + getAccurateText(
								headers.getString(j).equals(i == 0 ? "Customer Code" : "Cheque Date") ? toatlString : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (i != 0) {
					if (jInnerData.length() > 0) {
						printlines2(strheader, 1, object, 1, args[0], 3, 3);
						printlines2(strHeaderBottom, 1, object, 1, args[0], 3, 3);
						printlines2(printSepratorcomp(), 1, object, 1, args[0], 3, 3);

					}
				}
				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					for (int m = 0; m < jArr.length(); m++) {
						strData = strData + getAccurateText(jArr.getString(m),
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));
					}
					if (i != 0) {
						printlines2(strData, 1, object, 1, args[0], 3, 3);
					}

				}

				if (jInnerData.length() > 0) {
					if (i != 0) {
						printlines2(printSepratorcomp(), 1, object, 1, args[0], 3, 3);
					}
					
					//if (i != 0){ 
						outStream.write(CompressOff);
					//}
					printlines2(strTotal, 2, object, 1, args[0], 3, 3);
					if (i == 0) {
						printlines2(printSeprator(), 1, object, 1, args[0], 3, 3);
						JSONArray paytypecash = object.getJSONArray("PAYTYPECASH");

						
						for (int k = 0; k < paytypecash.length(); k++) {
							String cashDes = "", cash = "";
							JSONArray jArr = paytypecash.getJSONArray(k);
							cashDes = jArr.getString(0);
							cash = jArr.getString(1);
							if (cash == null && cash.toString().length() > 0) {
								cash = "0";
							}
							totalAmountcash = totalAmountcash + Double.parseDouble(cash);
							printlines2(
									(getAccurateText(cashDes, 40, 2) + getAccurateText(" X    " + cash + " ", 38, 0)),
									1, object, 1, args[0], 3, 3);

						}
						printlines2(printSeprator(), 1, object, 1, args[0], 3, 3);
						printlines2(
								(getAccurateText("TOTAL CASH RECEIVED : ", 40, 2)
										+ getAccurateText("    " + totalAmountcash + " ", 38, 0)),
								1, object, 1, args[0], 3, 3);
						printlines2(" ", 1, object, 1, args[0], 3, 3);
						outStream.write(BoldOn);
						String totalAmt = object.getString("TOTAL DEPOSIT AMOUNT");
						String varAmt = object.getString("totalvaramount");
						double totalDeposit =totalAmountcash;
						
						   DecimalFormat df = new DecimalFormat("#.###");
					    //    System.out.print(df.format(d));
						
						double cashVarience =Double.parseDouble(df.format(Double.parseDouble(totalAmt)-totalAmountcash));
						
						printlines2((getAccurateText("CASH VARIANCE : ", 40, 2) + getAccurateText("    " +String.valueOf(cashVarience), 38, 0)), 
								1, object,1, args[0], 3, 3);
						outStream.write(BoldOff);
						
						
				/*---------------------------------------------------------------------		*/
						
						

					}
					
					
					//outStream.write(CompressOn);

				}

			}
			outStream.write(CompressOff);

			//if((jData.length()>1 && i==1)||(jData.length()==0)){ // prints expense after cheque
				
			if(object.has("PAYTYPEEXPENSE")){
			JSONArray paytypeexpense = object.getJSONArray("PAYTYPEEXPENSE");
			if(paytypeexpense != null && paytypeexpense.length() > 0 ){

				outStream.write(BoldOn);
				printlines2(" ", 1, object, 1, args[0], 3, 3);
				outStream.write(UnderlineOn);
				printlines2("EXPENSE", 1, object, 1, args[0], 3, 3);
				outStream.write(UnderlineOff);
				outStream.write(BoldOff);
				
				printlines2(printSeprator(), 1, object, 1, args[0], 3, 3);
				

				
				for (int k = 0; k < paytypeexpense.length(); k++) {
					String cashDes = "", cash = "";
					JSONArray jArr = paytypeexpense.getJSONArray(k);
					cashDes = jArr.getString(0);
					cash = jArr.getString(1);
					if (cash == null && cash.toString().length() > 0) {
						cash = "0";
					}
					totalAmountexpense = totalAmountexpense + Double.parseDouble(cash);
					printlines2(
							(getAccurateText(cashDes, 40, 2) + getAccurateText(" X    " + cash + " ", 38, 0)),
							1, object, 1, args[0], 3, 3);

				}
				printlines2(printSeprator(), 1, object, 1, args[0], 3, 3);
				printlines2(
						(getAccurateText("TOTAL EXPENSE", 40, 2)
								+ getAccurateText(" :    " + totalAmountexpense + " ", 38, 0)),
						1, object, 1, args[0], 3, 3);
				//printlines2(" ", 1, object, 1, args[0], 3, 3);
				}
			}
			
			//}

			outStream.write(CompressOff);

			outStream.write(BoldOn);
			printlines2(" ", 1, object, 1, args[0], 3, 3);
			String totalAmt = object.getString("TOTAL DEPOSIT AMOUNT");
			String varAmt = object.getString("totalvaramount");
			double totalDeposit =totalAmountcash;
			double cashVarience = Double.parseDouble(totalAmt)-totalAmountcash;
//			
//			printlines2((getAccurateText("CASH VARIANCE", 67, 2) + getAccurateText(String.valueOf(cashVarience), 16, 1)), 1, object,
//					1, args[0], 3, 3);
			
			printlines2((getAccurateText("TOTAL DEPOSIT AMOUNT : ", 67, 2) + getAccurateText(String.valueOf(totalDeposit), 16, 1)), 1, object,
					1, args[0], 3, 3);

			if (varAmt != null && Double.parseDouble(varAmt) != 0) {
				printlines2((getAccurateText("INVENTORY VARIANCE AMOUNT : ", 67, 2) + getAccurateText(varAmt, 16, 1)), 1,
						object, 1, args[0], 3, 3);
			}

			if (totalAmt.length() > 0 && varAmt != null && Double.parseDouble(varAmt) > 0) {
				float totalCount = (float) (Float.parseFloat(totalAmt) + Float.parseFloat(varAmt)-cashVarience);

				int decimal_count = totalAmt.substring(totalAmt.indexOf(".") + 1, totalAmt.length()).length();
				printlines2(
						getAccurateText("NET DUE AMOUNT : ", 67, 2)
								+ getAccurateText(String.format("%." + decimal_count + "f", totalCount), 16, 1),
						1, object, 1, args[0], 3, 3);
			}

			outStream.write(BoldOff);
			printlines2(" ", 2, object, 1, args[0], 3, 3);
			printlines2(getAccurateText("SALESMAN______________", 23, 0)
					+ getAccurateText("SUPERVISOR______________", 23, 0)
					+ getAccurateText("ACCOUNT/CASHIER______________", 32, 0), 1, object, 2, args[0], 3, 3);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// return String.valueOf(s1);
	}

	// --------------
	void parseUnloadDamage(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			hashValues = new HashMap<String, Integer>();
			hashValues.put("ITEM#", 12);
			hashValues.put("DESCRIPTION", 12);
			hashValues.put("UPC", 4);
			hashValues.put("STALES Qty", 11);
			hashValues.put("T1.UNITS", 14);
			hashValues.put("DAMAGE Qty", 12);
			hashValues.put("T2.UNITS", 10);
			hashValues.put("OTHER Qty", 12);
			hashValues.put("T3.UNITS", 10);
			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("ITEM#", 0);
			hashPositions.put("DESCRIPTION", 0);
			hashPositions.put("UPC", 0);
			hashPositions.put("STALES Qty", 0);
			hashPositions.put("T1.UNITS", 0);
			hashPositions.put("DAMAGE Qty", 2);
			hashPositions.put("T2.UNITS", 2);
			hashPositions.put("OTHER Qty", 2);
			hashPositions.put("T3.UNITS", 2);

			// ---------Start
			// printconnect(args[0]);
			// ----------End

			line(startln);
			headerprint(object, 7);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");

				int MAXLEngth = 130;
				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());
				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				String strheader = "", strHeaderBottom = "", strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					strheader = strheader
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? headers.getString(j)
											: headers.getString(j).substring(0, headers.getString(j).indexOf(" "))
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? ""
											: headers.getString(j)
													.substring(headers.getString(j).indexOf(" "),
															headers.getString(j).length())
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal
								+ getAccurateText(headers.getString(j).equals("DESCRIPTION") ? "TOTAL QUANTITY" : "",
										hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					printlines2(printSeprator(), 1, object, 1, args[0], 7, 7);
					printlines2((getAccurateText("UNLOADED STALES", 67, 2) + getAccurateText("UNLOADED DAMAGE", 16, 1)),
							1, object, 1, args[0], 7, 7);
					printlines2(strheader, 1, object, 1, args[0], 7, 7);
					printlines2(strHeaderBottom, 1, object, 1, args[0], 7, 7);
					printlines2(printSeprator(), 1, object, 1, args[0], 7, 7);

				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					for (int m = 0; m < jArr.length(); m++) {
						strData = strData + getAccurateText(jArr.getString(m),
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));
					}
					printlines2(strData, 1, object, 1, args[0], 7, 7);

				}
				if (jInnerData.length() > 0) {
					printlines2(printSeprator(), 1, object, 1, args[0], 7, 7);
					printlines2(strTotal, 2, object, 1, args[0], 7, 7);
				}

			}
			outStream.write(CompressOff);
			outStream.write(BoldOn);
			String totalAmt = "0";
			String varAmt = "0";
			printlines2((getAccurateText("UNLOADED STALES VARIANCE", 67, 2) + getAccurateText(totalAmt, 16, 1)), 1,
					object, 1, args[0], 7, 7);
			printlines2((getAccurateText("UNLOADED DAMAGE VARIANCE", 67, 2) + getAccurateText(varAmt, 16, 1)), 1,
					object, 1, args[0], 7, 7);

			outStream.write(BoldOff);
			printlines2(" ", 2, object, 1, args[0], 7, 7);
			printlines2(getAccurateText("SALESMAN______________", 80, 1), 1, object, 2, args[0], 7, 7);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// return String.valueOf(s1);
	}

	void parseUnloadDamageStales(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			
			hashValues = new HashMap<String, Integer>();
			hashValues.put("ITEM#", 15);
			
			if(printLanguage.equals("2")){
				hashValues.put("DESCRIPTION", 20);
				hashValues.put("ARBDESCRIPTION", 28);
			}else if(printLanguage.equals("1")){
				hashValues.put("DESCRIPTION", 0);
				hashValues.put("ARBDESCRIPTION", 48);
			}else{
				hashValues.put("DESCRIPTION", 48);
				hashValues.put("ARBDESCRIPTION", 0);
			}
			
//			hashValues.put("DESCRIPTION", 24);
//			hashValues.put("ARBDESCRIPTION", 24);
			
			hashValues.put("UPC", 7);
			hashValues.put("STALES CASE/PCS", 12);
			hashValues.put("STALES T.UNITS", 10);
			hashValues.put("DAMAGE CASE/PCS", 12);
			hashValues.put("DAMAGE T.UNITS", 10);
			hashValues.put("OTHER CASE/PCS", 12);
			hashValues.put("OTHER T.UNITS", 10);
			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("ITEM#", 0);
			hashPositions.put("DESCRIPTION", 0);
			hashPositions.put("ARBDESCRIPTION", 2);
			hashPositions.put("UPC", 1);
			hashPositions.put("STALES CASE/PCS", 2);
			hashPositions.put("STALES T.UNITS", 2);
			hashPositions.put("DAMAGE CASE/PCS", 2);
			hashPositions.put("DAMAGE T.UNITS", 2);
			hashPositions.put("OTHER CASE/PCS", 2);
			hashPositions.put("OTHER T.UNITS", 2);

			// ---------Start
			// printconnect(args[0]);
			// ----------End

			line(startln);
			headerprint(object, 7);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";
			int MAXLEngth = 136;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}
			for (int i = 0; i < headers.length(); i++) {

				strheader = strheader + getAccurateText(
						i==2?"DESCRIPTION":
						(headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
								: headers.getString(i).substring(0, headers.getString(i).indexOf(" ")),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));
				strHeaderBottom = strHeaderBottom + getAccurateText(
						(headers.getString(i).indexOf(" ") == -1) ? ""
								: headers.getString(i).substring(headers.getString(i).indexOf(" "),
										headers.getString(i).length()),
						hashValues.get(headers.getString(i).toString()) + MAXLEngth,
						hashPositions.get(headers.getString(i).toString()));

			}
			outStream.write(CompressOn);

			printlines2(strheader, 1, object, 1, args[0], 7, 7);
			printlines2(strHeaderBottom, 1, object, 1, args[0], 7, 7);
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 7, 7);
			outStream.write(CompressOff);

			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {
					String itemDescrion = jArr.getString(j);
					if (j == 2) {
						itemDescrion = "";
						
							try {
								itemDescrion = "*" + jArr.getString(j) + "!";
							} catch (Exception e) {
								e.printStackTrace();
							}
						

					}	

					strData = strData + getAccurateText(itemDescrion,
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));

				}

				outStream.write(CompressOn);
				printlines2(strData, 1, object, 1, args[0], 7, 7);
				outStream.write(CompressOff);
			}
			outStream.write(CompressOn);
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 7, 7);
			JSONArray jTotal = object.getJSONArray("TOTAL");
			for (int i = 0; i < jTotal.length(); i++) {
				JSONObject jTOBject = jTotal.getJSONObject(0);
				String strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					if (jTOBject.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTOBject.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}
				}

				printlines2(strTotal, 1, object, 1, args[0], 7, 7);

			}
			outStream.write(CompressOff);
			outStream.write(BoldOn);
			printlines2(" ", 2, object, 1, args[0], 7, 7);
			String totalAmt = "0";
			String varAmt = "0";
			printlines2(
					(getAccurateText("TOTAL EXPIRY VALUE", 60, 2) + getAccurateText(
							object.has("TOTAL_EXPIRY_VALUE") ? object.getString("TOTAL_EXPIRY_VALUE") : "0", 16, 1)),
					1, object, 1, args[0], 7, 7);
			printlines2(
					(getAccurateText("TOTAL DAMAGE VALUE", 60, 2) + getAccurateText(
							object.has("TOTAL_DAMAGE_VALUE") ? object.getString("TOTAL_DAMAGE_VALUE") : "0", 16, 1)),
					1, object, 1, args[0], 7, 7);
			printlines2(
					(getAccurateText("TOTAL OTHER VALUE", 60, 2) + getAccurateText(
							object.has("TOTAL_OTHER_VALUE") ? object.getString("TOTAL_OTHER_VALUE") : "0", 16, 1)),
					1, object, 1, args[0], 7, 7);
			printlines2(
					(getAccurateText("UNLOADED STALES VARIANCE", 60, 2) + getAccurateText(
							object.has("TOTAL_STALES_VAR") ? object.getString("TOTAL_STALES_VAR") : "0", 16, 1)),
					1, object, 1, args[0], 7, 7);
			printlines2(
					(getAccurateText("UNLOADED DAMAGE VARIANCE", 60, 2) + getAccurateText(
							object.has("damagevariance") ? object.getString("damagevariance") : "0", 16, 1)),
					1, object, 1, args[0], 7, 7);

			outStream.write(BoldOff);
			printlines2(" ", 2, object, 1, args[0], 7, 7);
			printlines2(getAccurateText("SALESMAN______________", 80, 1), 1, object, 2, args[0], 7, 7);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// return String.valueOf(s1);
	}

	void printSalesSummaryReport(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			int totaldiscount = object.has("discountprint") ? Integer.parseInt(object.getString("discountprint")) : 0;
			
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Transaction Number", 25);
			hashValues.put("Customer Code", 15);
			
			hashValues.put("Sales Amount", 15);
			hashValues.put("G.Return Amount", 15);
		//	hashValues.put("Type", 10);
			if(totaldiscount==0){
				hashValues.put("Customer Name", 27);
				hashValues.put("Invoice Discount", 10);
				hashValues.put("D.Return Amount", 10);
			}else{
				hashValues.put("Customer Name", 47);
				hashValues.put("Invoice Discount", 0);
				hashValues.put("D.Return Amount", 0);
			}
			
			
			hashValues.put("Total Amount", 10);
			hashValues.put("Check Number", 10);
			hashValues.put("Check Date", 10);
			hashValues.put("Bank Name", 10);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Transaction Number", 0);
			hashPositions.put("Customer Code", 0);
			hashPositions.put("Customer Name", 0);
			hashPositions.put("Sales Amount", 2);
			//hashPositions.put("Type", 1);
			hashPositions.put("G.Return Amount", 2);
			hashPositions.put("D.Return Amount", 2);
			hashPositions.put("Invoice Discount", 2);
			hashPositions.put("Total Amount", 2);
			hashPositions.put("Check Number", 0);
			hashPositions.put("Check Date", 0);
			hashPositions.put("Bank Name", 0);
			
			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);
			headerprint(object, 4);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {
				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");

				if (jInnerData.length() > 0) {

					switch (i) {
					case 0:

						outStream.write(BoldOn);
						
						outStream.write(DoubleHighOn);
						outStream.write(UnderlineOn);
						printlines2("CASH INVOICE", 1, object, 1, args[0], 4, 4);
						outStream.write(UnderlineOff);
						outStream.write(DoubleHighOff);
						
						outStream.write(BoldOff);

						break;
					case 1:

						outStream.write(BoldOn);
						printlines2(" ", 1, object, 1, args[0], 4, 4);
						
						outStream.write(DoubleHighOn);
						outStream.write(UnderlineOn);
						printlines2("CREDIT INVOICE",1, object, 1, args[0], 4, 4);
						outStream.write(UnderlineOff);
						outStream.write(DoubleHighOff);
						
						outStream.write(BoldOff);

						break;
					case 2:

						outStream.write(BoldOn);
						printlines2(" ", 1, object, 1, args[0], 4, 4);
						
						outStream.write(DoubleHighOn);
						outStream.write(UnderlineOn);
						if (object.getString("salesflag").equals("0")) {
							printlines2("TC INVOICE (CASH)", 1, object, 1, args[0], 4, 4);

						} else {
							printlines2("COLLECTION (CASH)", 1, object, 1, args[0], 4, 4);

						}
						outStream.write(UnderlineOff);
						outStream.write(DoubleHighOff);
						
						outStream.write(BoldOff);

						break;
					case 3:

						outStream.write(BoldOn);
						printlines2(" ", 1, object, 1, args[0], 4, 4);
						
						outStream.write(DoubleHighOn);
						outStream.write(UnderlineOn);
						if (object.getString("salesflag").equals("0")) {
							printlines2("TC INVOICE (CREDIT)", 1, object, 1, args[0], 4, 4);
						}else{
							printlines2("COLLECTION (CHEQUE)", 1, object, 1, args[0], 4, 4);
						}
						outStream.write(UnderlineOff);
						outStream.write(DoubleHighOff);
						
						outStream.write(BoldOff);

						break;
					case 4:

						outStream.write(BoldOn);
						printlines2(" ", 1, object, 1, args[0], 4, 4);
						
						outStream.write(DoubleHighOn);
						outStream.write(UnderlineOn);
						printlines2("COLLECTION (CASH)", 1, object, 1, args[0], 4, 4);
						outStream.write(UnderlineOff);
						outStream.write(DoubleHighOff);
						
						outStream.write(BoldOff);

						break;
					case 5:

						outStream.write(BoldOn);
						printlines2(" ", 1, object, 1, args[0], 4, 4);
						
						outStream.write(DoubleHighOn);
						outStream.write(UnderlineOn);
						printlines2("COLLECTION (CHEQUE)", 1, object, 1, args[0], 4, 4);
						outStream.write(UnderlineOff);
						outStream.write(DoubleHighOff);
						
						outStream.write(BoldOff);

						break;
					default:
						break;
					}
				}

				int MAXLEngth = 137;
				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());
				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				String strheader = "", strHeaderBottom = "", strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					strheader = strheader
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? headers.getString(j)
											: headers.getString(j).substring(0, headers.getString(j).indexOf(" "))
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? ""
											: headers.getString(j)
													.substring(headers.getString(j).indexOf(" "),
															headers.getString(j).length())
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal
								+ getAccurateText(headers.getString(j).equals("Customer Code") ? "Total Amount" : "",
										hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					printlines2(strheader, 1, object, 1, args[0], 4, 4);
					printlines2(strHeaderBottom, 1, object, 1, args[0], 4, 4);
					printlines2(printSepratorCompress(), 1, object, 1, args[0], 4, 4);
					outStream.write(CompressOff);
				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					for (int m = 0; m < jArr.length(); m++) {
						strData = strData + getAccurateText(jArr.getString(m),
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));
					}
					outStream.write(CompressOn);
					printlines2(strData, 1, object, 1, args[0], 4, 4);
					outStream.write(CompressOff);
				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					printlines2(printSepratorCompress(), 1, object, 1, args[0], 4, 4);
					outStream.write(CompressOff);
					outStream.write(CompressOn);
					printlines2(strTotal, 1, object, 1, args[0], 4, 4);
					outStream.write(CompressOff);

				}

			}

			printlines2(" ", 4, object, 1, args[0], 4, 4);
			printlines2(getAccurateText(("SALESMAN_______________ "), 40, 1)+getAccurateText(("SUPERVISOR_______________ "), 40, 1), 2, object, 2, args[0], 4, 4);

		} catch (Exception e) {
			e.printStackTrace();

		}

		// return String.valueOf(s1).getBytes();
	}

	// -------------start Route Activity Developer By VB 5/1/2014
	void printRouteactivityReport(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {

			hashValues = new HashMap<String, Integer>();
			hashValues.put("Transaction No", 20);
			hashValues.put("Time In", 10);
			hashValues.put("Time Out", 10);
			hashValues.put("Customer Code", 15);
			hashValues.put("Customer Name", 35);
			hashValues.put("Transaction Type", 24);
			hashValues.put("Total Amount", 15);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Transaction No", 0);
			hashPositions.put("Time In", 0);
			hashPositions.put("Time Out", 0);
			hashPositions.put("Customer Code", 0);
			hashPositions.put("Customer Name", 0);
			hashPositions.put("Transaction Type", 0);
			hashPositions.put("Total Amount", 2);

			/*
			 * hashValues = new HashMap<String, Integer>(); hashValues.put(
			 * "Transaction Number", 11); hashValues.put("Time In", 6);
			 * hashValues.put("Time Out", 6); hashValues.put("Customer Code",
			 * 10); hashValues.put("Transaction Type", 14);
			 * hashValues.put("Amount", 8);
			 * 
			 * hashPositions = new HashMap<String, Integer>(); hashValues.put(
			 * "Transaction Number",0); hashValues.put("Time In",0);
			 * hashValues.put("Time Out",0); hashValues.put("Customer Code",0);
			 * hashValues.put("Transaction Type",0); hashValues.put("Amount",
			 * 2);
			 */

			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);
			headerprint(object, 5);
			outStream.write(CompressOn);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				int MAXLEngth = 137;
				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());
				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				String strheader = "", strHeaderBottom = "", strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					strheader = strheader
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? headers.getString(j)
											: headers.getString(j).substring(0, headers.getString(j).indexOf(" "))
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(headers.getString(j).indexOf(" ") == -1) ? ""
											: headers.getString(j)
													.substring(headers.getString(j).indexOf(" "),
															headers.getString(j).length())
													.trim(),
									hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(
								headers.getString(j).equals("Customer Name") ? "TOTAL SALES&RECEIPT" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);

					}

				}
				if (jInnerData.length() > 0) {

					printlines2(strheader, 1, object, 1, args[0], 5, 5);
					printlines2(strHeaderBottom, 1, object, 1, args[0], 5, 5);
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 5, 5);

				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					for (int m = 0; m < jArr.length(); m++) {
						strData = strData + getAccurateText(jArr.getString(m),
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));
					}

					printlines2(strData, 1, object, 1, args[0], 5, 5);

				}
				if (jInnerData.length() > 0) {

					printlines2(printSepratorcomp(), 1, object, 1, args[0], 5, 5);

					printlines2(strTotal, 1, object, 1, args[0], 5, 5);

				}
				if (jInnerData.length() > 0) {

					printlines2(printSepratorcomp(), 2, object, 1, args[0], 5, 5);

				}

			}
			outStream.write(CompressOff);
			outStream.write(BoldOn);
			printlines2(getAccurateText("ENDING ODOMETER READING", 40, 2)
					+ getAccurateText(object.getString("endreading"), 10, 2), 2, object, 1, args[0], 5, 5);
			printlines2(getAccurateText("STARTING ODOMETER READING", 40, 2)
					+ getAccurateText(object.getString("startreading"), 10, 2), 2, object, 1, args[0], 5, 5);
			printlines2(getAccurateText("TOTAL KILOMETRS", 40, 2) + getAccurateText(object.getString("totalkm"), 10, 2),
					2, object, 1, args[0], 5, 5);
			outStream.write(BoldOff);
			printlines2(" ", 2, object, 1, args[0], 5, 5);
			printlines2(getAccurateText(("SALESMAN_______________ "), 80, 1), 2, object, 2, args[0], 5, 5);

		} catch (Exception e) {
			e.printStackTrace();

		}

		// return String.valueOf(s1);
	}

	// ---------End
	// -------------start Route Summary Report Developed By VB 7/1/2014
	void printRouteSummaryReport(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			// ---------Start
			// printconnect(args[0]);
			// ----------End
			line(startln);
			headerprint(object, 6);

			// JSONArray jData = object.getJSONArray("data");
			outStream.write(BoldOn);
			outStream.write("       ".getBytes());
			outStream.write(UnderlineOn);
			printlines2("VISIT DETAIL", 1, object, 1, args[0], 6, 6);
			outStream.write(UnderlineOff);
			outStream.write(BoldOff);

			// ------------
			printlines2(getAccurateText("SCANNED CUSTOMERS: ", 30, 0)
					+ getAccurateText(object.getString("ScannedCustomers"), 10, 0)
					+ getAccurateText("PLANNED CALLS:", 30, 0)
					+ getAccurateText(object.getString("PlannedCalls"), 10, 0), 1, object, 1, args[0], 6, 6);

			printlines2(

					getAccurateText("NON SCANNED CUSTOMER", 30, 0)
							+ getAccurateText(object.getString("NonScannedCustomers"), 10, 0)
							+ getAccurateText("CALLS MADE(PLANNED)", 30, 0)
							+ getAccurateText(object.getString("CallsMadePlanned"), 10, 0),
					1, object, 1, args[0], 6, 6);

			printlines2(
					getAccurateText("NO CALLS CUSTOMERS", 30, 0)
							+ getAccurateText(object.getString("NoCallCustomer"), 10, 0)
							+ getAccurateText("CALLS MADE(UNPLANNED)", 30, 0)
							+ getAccurateText(object.getString("CallsMadeUnPlanned"), 10, 0),
					1, object, 1, args[0], 6, 6);

			printlines2(
					getAccurateText("VOID INVOICES", 30, 0) + getAccurateText(object.getString("VoidInvoices"), 10, 0)
							+ getAccurateText("ACTUAL CALLS MADE", 30, 0)
							+ getAccurateText(object.getString("ActualCallsMade"), 10, 0),
					1, object, 1, args[0], 6, 6);

			printlines2(getAccurateText("START TIME", 30, 0) + getAccurateText(object.getString("StartTime"), 10, 0)
					+ getAccurateText("INVOICED CALLS", 30, 0)
					+ getAccurateText(object.getString("InvoicedCalls"), 10, 0), 1, object, 1, args[0], 6, 6);

			printlines2(
					getAccurateText("END TIME", 30, 0) + getAccurateText(object.getString("EndTime"), 10, 0)
							+ getAccurateText("PRODUCTIVE CALLS", 30, 0)
							+ getAccurateText(object.getString("ProductiveCalls") + "%", 10, 0),
					1, object, 1, args[0], 6, 6);

			printlines2(
					getAccurateText("TOTAL KMS RUN", 30, 0) + getAccurateText(object.getString("TotalKmsRun"), 10, 0)
							+ getAccurateText("COVERAGE", 30, 0)
							+ getAccurateText(object.getString("CoverageCalls") + "%", 10, 0),
					2, object, 1, args[0], 6, 6);
			// ------------

			outStream.write(BoldOn);
			outStream.write("       ".getBytes());
			outStream.write(UnderlineOn);
			printlines2("INVENTORY ", 1, object, 1, args[0], 6, 6);
			outStream.write(UnderlineOff);
			outStream.write(BoldOff);

			printlines2(getAccurateText("OPENING", 30, 0) + getAccurateText(object.getString("Opening"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(getAccurateText("LOADED", 30, 0) + getAccurateText(object.getString("Loaded"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(
					getAccurateText("TRANSFERED IN", 30, 0) + getAccurateText(object.getString("Transferin"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(
					getAccurateText("TRANSFERED OUT", 30, 0) + getAccurateText(object.getString("Transferout"), 10, 2),
					1, object, 1, args[0], 6, 6);
			printlines2(getAccurateText("SALES & FREE", 30, 0) + getAccurateText(object.getString("salesfree"), 10, 2),
					1, object, 1, args[0], 6, 6);
			printlines2(
					getAccurateText("FRESH UNLOAD", 30, 0) + getAccurateText(object.getString("freshunload"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(
					getAccurateText("TRUCK DAMAGES", 30, 0) + getAccurateText(object.getString("truckdamage"), 10, 2),
					1, object, 1, args[0], 6, 6);
			printlines2(getAccurateText("BAD RETURN", 30, 0) + getAccurateText(object.getString("badreturn"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(getAccurateText("DISCOUNT GIVEN", 30, 0)
					+ getAccurateText(object.getString("discountgiven"), 10, 2), 1, object, 1, args[0], 6, 6);

			printlines2(
					getAccurateText("CALCULATED UNLOAD", 30, 0)
							+ getAccurateText(object.getString("calculatedunload"), 10, 2),
					1, object, 1, args[0], 6, 6);
			printlines2(getAccurateText("UNLOAD", 30, 0) + getAccurateText(object.getString("unload"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(getAccurateText("UNLOAD VARIENCE", 30, 0)
					+ getAccurateText(object.getString("unloadvariance"), 10, 2), 1, object, 1, args[0], 6, 6);
			/*
			 * printlines2( getAccurateText("CALCULATED BAD RETURN", 30, 0) +
			 * getAccurateText( object.getString("calculatedunload"), 45, 2), 1,
			 * object, 1, args[0], 6, 6); printlines2( getAccurateText(
			 * "UNLOADED BAD RETURN", 30, 0) + getAccurateText(
			 * object.getString("unloadbadreturn"), 45, 2), 1, object, 1,
			 * args[0], 6, 6); printlines2( getAccurateText("RETURN VARIANCE",
			 * 30, 0) + getAccurateText( object.getString("returnvarinace"), 45,
			 * 2), 1, object, 1, args[0], 6, 6);
			 */
			printlines2(getAccurateText("TOTAL INVENTORY VARIANCE", 30, 0)
					+ getAccurateText(object.getString("Totalinvvarince"), 10, 2), 2, object, 1, args[0], 6, 6);

			outStream.write(BoldOn);
			outStream.write("       ".getBytes());
			outStream.write(UnderlineOn);
			printlines2("SALES/COLLECTION", 1, object, 1, args[0], 6, 6);
			outStream.write(UnderlineOff);
			outStream.write(BoldOff);
			printlines2(getAccurateText("TODAYS SALES", 20, 0) + getAccurateText(object.getString("todaysales"), 35, 2),
					1, object, 1, args[0], 6, 6);
			printlines2(
					getAccurateText("    CASH SALES", 20, 0) + getAccurateText(object.getString("cashsales"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(getAccurateText("    CREDIT SALES", 20, 0)
					+ getAccurateText(object.getString("creditsales"), 10, 2), 1, object, 1, args[0], 6, 6);
			
			if(object.has("tcsales")){
				 printlines2(getAccurateText("    TC SALES", 20, 0) +
						  getAccurateText(object.getString("tcsales"), 10, 2), 1, object,
						  1, args[0], 6, 6);
			}
			 
			 
			printlines2(getAccurateText("COLLECTIONS", 20, 0) + getAccurateText(object.getString("collection"), 35, 2),
					1, object, 1, args[0], 6, 6);
			printlines2(getAccurateText("    CASH", 20, 0) + getAccurateText(object.getString("cash"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(getAccurateText("    CHEQUE", 20, 0) + getAccurateText(object.getString("cheque"), 10, 2), 1,
					object, 1, args[0], 6, 6);
			printlines2(getAccurateText("TOTAL VAT APPLIED", 20, 0) + getAccurateText(object.getString("vatapplied"), 35, 2),
					1, object, 1, args[0], 6, 6);
			
			/*
			 * printlines2(getAccurateText("EXPENSES", 20, 0)+
			 * getAccurateText(object.getString("expense"),35,
			 * 2),1,object,1,args[0],6,6); printlines2(getAccurateText(
			 * "CASH ADJUSTMENT", 20, 0)+
			 * getAccurateText(object.getString("cashadj"), 10,
			 * 2),1,object,1,args[0],6,6); printlines2(getAccurateText(
			 * "CHEQUE ADJUSTMENT", 20, 0)+
			 * getAccurateText(object.getString("chkadj"), 10,
			 * 2),1,object,1,args[0],6,6); printlines2(getAccurateText(
			 * "CALCULATED CASH DUE", 20, 0)+
			 * getAccurateText(object.getString("calculatedcashdue"), 55,
			 * 2),1,object,1,args[0],6,6); printlines2(getAccurateText(
			 * "CASH VARIANCE", 20, 0)+
			 * getAccurateText(object.getString("cashvariance"), 55,
			 * 2),1,object,1,args[0],6,6); printlines2(getAccurateText(
			 * "NET CASH DUE", 20, 0)+
			 * getAccurateText(object.getString("netcashdue"), 55,
			 * 2),3,object,1,args[0],6,6);
			 */
			printlines2(" ", 2, object, 1, args[0], 6, 6);
			printlines2(getAccurateText("SALESMAN_______________ ", 80, 1), 2, object, 2, args[0], 6, 6);

		} catch (Exception e) {
			e.printStackTrace();

		}

		// return String.valueOf(s1);
	}

	// ---------End
	private int count = 0;
	private boolean isCompressModeOn = false;

	// -----------Start for testing
	/*
	 * Printline1 function arguments definition String Data = Passed string int
	 * ln = how many line want to skip after printing one line JSONObject = json
	 * object for retrieving JSON data int sts = its argument of the status 0=
	 * start,1=continue,2=end String adr = useful of print sales report for
	 * duplicate copy passed mac address of printer int tran = argument use full
	 * for transcation type 1= inventory,2=invoice,3=vanstock int type =
	 * argument for passing subtype of invoice and inventory
	 */

	// For Inventory
	private void printlines1(String data, int ln, JSONObject object, int sts, String adr, int tp)
			throws JSONException, IOException, LinePrinterException {

		count += ln;
		int cnt=0;
		
		int pln = pageLength-cnt;
		
		boolean isEnd = false;
		if (sts == 2 && count != 0) {
			printArabic(data);
			isEnd = true;
			outStream.write(CarriageReturn);
			outStream.write(formFeed);
			count = 0;
			try {
				Thread.sleep(5000);
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			status.put("status", true);
			status.put("isconnected", 0);
			sendUpdate(status, true);

		}
		if (!isEnd) {
			printArabic(data);
			for (int i = 0; i < ln; i++) {
				try {

					outStream.write(NewLine);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (count % 10 == 0) {
				try {
					Thread.sleep(5000);
					// lp.flush()
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (count > pln) {
				Log.e("Count 1 time", "Count " + count);
				outStream.write(CompressOff);
				count = 0;
				try {
					outStream.write(CarriageReturn);
					outStream.write(formFeed);
					line(startln);
					
					if (tp == 4) {
						headervanstockprint(object, tp);
					} else if (tp == 6 || tp == 25 || tp == 10) {
						headervanstockprint(object, tp);
					} else if(tp ==27 ){
						headervanstockprint(object, tp);
						
					}else {
						headerinvprint(object, tp);
					}
					// outStream.write(NewLine);
					outStream.write(printSeprator().getBytes());
					if(tp ==27 ){
						outStream.write(CompressOn);
					}
					count = count + 1;

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	
	// For Tranasactions
	private void printlines2(String data, int ln, JSONObject object, int sts, String adr, int tran, int tp)
			throws JSONException, IOException, LinePrinterException {
		count += ln;
		int cnt=0;
		
		int pln = pageLength-cnt;
		boolean isEnd = false;
		if (sts == 2 && count != 0) {
			printArabic(data);
			int lnno;
			int lnno1;
			Log.e("salessummary count", "" + count);
			Log.e("salessummary pln", "" + pln);
			lnno = pln - count;
			isEnd = true;


			outStream.write(NewLine);
			if (object.has("invoiceformat") && object.getString("invoiceformat").equals("1")) {

				outStream.write(CompressOn);
				printArabic(getAccurateText(object.getString("footeraddress1"), 137, 1));
				outStream.write(NewLine);
				printArabic(getAccurateText(object.getString("footeremail"), 137, 1));

				printArabic(getAccurateText(object.getString("footeraddress2"), 137, 1));
				outStream.write(CompressOff);
				
			}
			
			outStream.write(CarriageReturn);
			outStream.write(formFeed);
			count = 0;
			
			try {
				Thread.sleep(5000);
				// lp.flush()
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			status.put("status", true);
			status.put("isconnected", 0);
			sendUpdate(status, true);

		}

		if (!isEnd) {
			printArabic(data);

			for (int i = 0; i < ln; i++) {
				try {

					outStream.write(NewLine);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (count % 10 == 0) {
				try {
					Thread.sleep(5000);
					// lp.flush()
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Log.d("Count", "Count1" + count);
			if (count > pln) {
				count = 0;
				Log.d("Count", "Count1" + count);
				Log.d("Pln", "PLn" + pln);
				try {
					outStream.write(CarriageReturn);
					outStream.write(formFeed);
					line(startln);
					outStream.write(CompressOff);
					if (tran == 2) {
						headerprint(object, tp);
					} else if (tran == 1) {
						if(companyTaxStng==1){
							headerTaxprint(object, 1,"");
						}else{
							headerprint(object, tp);
						}
						
					} else if (tran == 5) {
						headerprint(object, tp);
					} else if (tran == 4) {
						headerprint(object, tp);
					} else if (tran == 3) {
						headerprint(object, tp);
					} else if (tran == 6) {
						headerprint(object, tp);
					} else if (tran == 7) {
						headerprint(object, tp);
					}
					

					if (tran == 5 || tran == 4 || isCompressModeOn) {
						outStream.write(CompressOn);

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
	// For Key Account Tranasactions
	private void printlines3(String data, int ln, JSONObject object, int sts, String adr, int tran, int tp)
				throws JSONException, IOException, LinePrinterException {
			count += ln;
			int cnt=0;
			
			int pln = 35-cnt;
			boolean isEnd = false;
			if (sts == 2 && count != 0) {
				printArabic(data);
				int lnno;
				int lnno1;
				Log.e("salessummary count", "" + count);
				Log.e("salessummary pln", "" + pln);
				lnno = pln - count;
				isEnd = true;

				lnno1 = lnno+12; //For Key Account 

				outStream.write(NewLine);
				
				for (int i = 0; i < lnno1; i++) {
					try {
						if (i % 10 == 0) {
							try {
								Thread.sleep(1000);
								// lp.flush()
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(i==lnno1-7){
							keyAccountFooter(object, tp, true);
						}else if(i==lnno1-9){
							keyAccountFooter(object, tp, false);
						}
						outStream.write(NewLine);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				outStream.write(CarriageReturn);
				outStream.write(formFeed);
			
				count = 0;
				// lp.disconnect(); // Disconnects from the printer
				// outStream.write(resetprinter);

				try {
					Thread.sleep(5000);
					// lp.flush()
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// if (outStream != null)
				//
				// try {
				// outStream.close();
				//
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// // e.printStackTrace();
				// Log.v("osERROR", e.getMessage());
				// }

				status.put("status", true);
				status.put("isconnected", 0);
				sendUpdate(status, true);

			}

			if (!isEnd) {
				printArabic(data);

				for (int i = 0; i < ln; i++) {
					try {

						outStream.write(NewLine);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (count % 10 == 0) {
					try {
						Thread.sleep(5000);
						// lp.flush()
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Log.d("Count", "Count1" + count);
				if (count > pln) {
					
					Log.d("Count", "Count1" + count);
					Log.d("Pln", "PLn" + pln);
					try {
						outStream.write(CompressOff);
						//Footer SKIPPING
						for (int i = 0; i < 11; i++) {
							
							try {
								
								if(i==2){
									keyAccountFooter(object, tp, false);
								}else if(i==4){
									keyAccountFooter(object, tp, true);
								}
								outStream.write(NewLine);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//Foooter SKIPPING
						outStream.write(CarriageReturn);
						outStream.write(formFeed);
						count = 0;
						line(startln);
						
						if (tran == 1) {
							headerKeyAccountprint(object, tp);
						} 

						if (tran == 5 || tran == 4 || isCompressModeOn) {
							outStream.write(CompressOn);

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
	
	private void printArabic(String data) {
		try {
			if (data.indexOf("*") != -1 && data.indexOf("!") != -1) {
				String start = data.substring(0, data.indexOf("*"));
				String middle = data.substring(data.indexOf("*") + 1, data.indexOf("!"));
				String end = data.substring(data.indexOf("!") + 1, data.length());

				Log.e("start", start);
				Log.e("middle", middle);
				Log.e("end", end);

				Arabic6822 Arabic = null;
				Arabic = new Arabic6822();
				byte[] printbyte = Arabic.Convert(middle, false);

				outStream.write(start.getBytes());

				outStream.write(printbyte);
				outStream.write("  ".getBytes());
				if (end.indexOf("*") != -1 && end.indexOf("!") != -1) {
					String startbet = end.substring(0, end.indexOf("*"));
					String middlebet = end.substring(end.indexOf("*") + 1, end.indexOf("!"));

					String endbet = end.substring(end.indexOf("!") + 1, end.length());
					byte[] printmidbyte = Arabic.Convert(middlebet, false);
					outStream.write(startbet.getBytes());

					outStream.write(printmidbyte);
					outStream.write("  ".getBytes());
					
					if (endbet.indexOf("*") != -1 && endbet.indexOf("!") != -1) {
						String startthird = endbet.substring(0, endbet.indexOf("*"));
						String middlethird = endbet.substring(endbet.indexOf("*") + 1, endbet.indexOf("!"));

						String endthird = endbet.substring(endbet.indexOf("!") + 1, endbet.length());
						byte[] printmidthirdbyte = Arabic.Convert(middlethird, false);
						outStream.write(startthird.getBytes());

						outStream.write(printmidthirdbyte);
						outStream.write("  ".getBytes());
						outStream.write(endthird.getBytes());
					}else{
						outStream.write(endbet.getBytes());
					}
					
					
				} else {
					outStream.write(end.getBytes());
				}

			} else {
				outStream.write(data.getBytes());

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void headerTaxprint(JSONObject object, int type, String... args) throws JSONException {
		try {
			
			
			int TotQty=object.has("totalQty")?Integer.parseInt(object.getString("totalQty")):0;
			if (type == 1) {
				
				outStream.write(BoldOn);
				int printtax=Integer.parseInt(object.getString("printtax"));
				
				if(TotQty>0){
					printheaders(getAccurateText("TAX INVOICE "+"*" + ArabicTEXT.taxInv + "! ", 80, 1), true, 1);
					/*if(printtax>0){
						printheaders(getAccurateText("TAX INVOICE "+"*" + ArabicTEXT.taxInv + "! ", 80, 1), true, 1);
					}else{
						printheaders(getAccurateText("INVOICE "+"*" + ArabicTEXT.taxInv + "! ", 80, 1), true, 1);
					}*/
					
					
				}else{
					printheaders(getAccurateText("TAX CREDIT NOTE "+"*" + ArabicTEXT.taxInv + "! ", 80, 1), true, 1);
					/*if(printtax>0){
						printheaders(getAccurateText("TAX CREDIT NOTE "+"*" + ArabicTEXT.taxInv + "! ", 80, 1), true, 1);
					}else{
						printheaders(getAccurateText("CREDIT NOTE "+"*" + ArabicTEXT.taxInv + "! ", 80, 1), true, 1);
					}*/
				}
				if (object.has("companytaxregistrationnumber")){
					
					 printheaders(getAccurateText("Tax Registration No:", 30, 2)+getAccurateText(object.getString("companytaxregistrationnumber"), 20, 1)+getAccurateText(" :*" + ArabicTEXT.taxdetail + "! ", 30, 0), true, 1);
					 
				}else{
						if (object.has("invoiceformat") && object.getString("invoiceformat").equals("1")) {
							printheaders(getAccurateText(object.getString("companyname"), 40, 1), true, 1);
						}
				}
				if(object.has("EXCISENUMBER")){
					printheaders(getAccurateText("Excise No:", 30, 2)+getAccurateText(object.getString("EXCISENUMBER"), 20, 1)+getAccurateText(" :*" + ArabicTEXT.exciseNumber + "! ", 30, 0), true, 1);
				}
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(BoldOn);
				
				if(TotQty>0){
					outStream.write(DoubleWideOn);
					printheaders(getAccurateText(
							"INVOICE NO." + object.getString("invoicenumber")+" "+"*" + ArabicTEXT.invNo + "! ", 40, 1), true, 2);
					outStream.write(DoubleWideOff);
				}else{
					printheaders(getAccurateText(
							"CREDIT NOTE NO. " + object.getString("invoicenumber")+" "+"*" + ArabicTEXT.creditNoteNo + "! ", 80, 1), true, 2);
				
				}
				
				outStream.write(BoldOff);
				
				
			}else if (type == 2){
				
				outStream.write(BoldOn);
				
				printheaders(getAccurateText("TAX ORDER "+"*" + ArabicTEXT.taxOrder + "! ", 80, 1), true, 1);
				
				if (object.has("companytaxregistrationnumber")){
						
					 printheaders(getAccurateText("Tax Registration No:", 30, 2)+getAccurateText(object.getString("companytaxregistrationnumber"), 20, 1)+getAccurateText(" :*" + ArabicTEXT.taxdetail + "! ", 30, 0), true, 1);
					 
				}else{
						if (object.has("invoiceformat") && object.getString("invoiceformat").equals("1")) {
							printheaders(getAccurateText(object.getString("companyname"), 40, 1), true, 1);
						}
				}
				outStream.write(NewLine);
				outStream.write(BoldOff);
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText(
							"ORDER NO. " + object.getString("invoicenumber")+" "+"*" + ArabicTEXT.orderNo + "! ", 40, 1), true, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				//count = count + 3;
			}
			
				outStream.write(BoldOn);
				outStream.write(UnderlineOn);
				int paymenttype = Integer.parseInt(object.getString("ptype"));
				
				if(paymenttype==0){
					printheaders(getAccurateText("(CASH  *" + ArabicTEXT.Cash + "!) ", 80, 1), true, 1);
				}else if(paymenttype==1){
					printheaders(getAccurateText("(CREDIT  *" + ArabicTEXT.credit + "!) ", 80, 1), true, 1);
				}else if(paymenttype==2){
					printheaders(getAccurateText("(CASH  *" + ArabicTEXT.Cash + "!) ", 80, 1), true, 1);
				}
				
				outStream.write(UnderlineOff);
				
				outStream.write(NewLine);
				outStream.write(BoldOn);
				printheaders(getAccurateText("CustomerId:"+object.getString("CUSTOMERID"), 40, 0) + 
						getAccurateText(object.getString("CUSTOMERID")+":*" + ArabicTEXT.customerid + "!", 40, 2), true, 1);
				outStream.write(NewLine);
				printheaders(getAccurateText("Name :"+object.getString("CUSTOMERNAME"), 80, 0), true, 1);
				outStream.write(NewLine);
				if(object.has("arbcustomername") && object.getString("arbcustomername").length()>0){
					printheaders(getAccurateText("*" +object.getString("arbcustomername")+ "!"+":*" + ArabicTEXT.customername + "!", 80, 2), true, 1);
					outStream.write(NewLine);
				}
				if (object.has("ARBADDRESS")&&object.getString("ARBADDRESS").length()>0){
					 
					 	printheaders(getAccurateText("Address :"+object.getString("ADDRESS"), 80, 0), true, 1);
					 	outStream.write(NewLine);
						 printheaders(getAccurateText("*" +object.getString("ARBADDRESS")+ "!"+":*" + ArabicTEXT.address + "! ", 80, 2) , true, 1);
					 }else if(object.has("ADDRESS")){
						 printheaders(getAccurateText("Address :"+object.getString("ADDRESS"), 80, 0), true, 1);
					 }
//				 if (object.has("ARBADDRESS")){
//					 printheaders(getAccurateText("Address :"+object.getString("ADDRESS"), 40, 0) + 
//								getAccurateText("*" +object.getString("ARBADDRESS")+ "!"+":*" + ArabicTEXT.address + "! ", 40, 2), true, 1);
//				 }else if(object.has("ADDRESS")){
//					 printheaders(getAccurateText("Address :"+object.getString("ADDRESS"), 40, 0) + getAccurateText(":*" + ArabicTEXT.address + "! ", 40, 2), true, 1);
//				 }
				outStream.write(NewLine);
				 if (object.has("contactDetail")){
					 printheaders(getAccurateText("Contact:"+object.getString("contactDetail"), 40, 0) + 
								getAccurateText(object.getString("contactDetail")+":*" + ArabicTEXT.contact + "! ", 40, 2), true, 1);
				 }
				outStream.write(NewLine);
				outStream.write(UnderlineOn);
			
				 if (object.has("taxregistrationnumber")){
					 printheaders(getAccurateText("TRN Number :"+object.getString("taxregistrationnumber"), 40, 0) + 
								getAccurateText(object.getString("taxregistrationnumber")+":*" + ArabicTEXT.taxRegNo + "! ", 40,2), true, 1);
				 }
				
				outStream.write(NewLine);
				outStream.write(UnderlineOff);
				outStream.write(BoldOff);
				if (object.getString("LANG").equals("en")) {
					/*printheaders(getAccurateText("ROUTE", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("ROUTE"), 27, 0)+ 
							    getAccurateText("INVOICE DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 1), true, 1);
					outStream.write(NewLine);
					
					printheaders(getAccurateText("SALESMAN", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("SALESMAN") + "-" + object.getString("CONTACTNO"), 27, 0)+ 
						    getAccurateText("DELIVERY DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 1), true, 1);
					
					outStream.write(NewLine);
					outStream.write(UnderlineOn);
					
					printheaders(getAccurateText("SUPERVISOR", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("supervisorname") + "-" + object.getString("supervisorno"), 27, 0)+ 
						    getAccurateText("TRIP ID", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TourID"), 18, 2), true, 1);
					
					outStream.write(NewLine);
					outStream.write(UnderlineOff);*/
					outStream.write(CompressOn);
					if(TotQty>0){
					printheaders(getAccurateText("ROUTE", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TO ROUTE"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.routeArabic+"!", 25, 2)+ 
							    getAccurateText("INVOICE DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.invoicedate+"!", 30, 2), true, 1);
					}else{
						printheaders(getAccurateText("ROUTE", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TO ROUTE"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.routeArabic+"!", 25, 2)+ 
							    getAccurateText("CREDIT NOTE DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.invoicedate+"!", 30, 2), true, 1);
					
					}
					outStream.write(CompressOff);
					outStream.write(NewLine);
					outStream.write(CompressOn);
					printheaders(getAccurateText("SALESMAN", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("SALESMAN") + "-" 
					+ object.getString("CONTACTNO"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.Salesman+"!", 25, 2)+
					(TotQty>0?(getAccurateText("DELIVERY DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.deliveryDate+"!", 30, 2)):""), true, 1);
					outStream.write(CompressOff);
					outStream.write(NewLine);
					outStream.write(CompressOn);
					outStream.write(UnderlineOn);
					
					printheaders(getAccurateText("SUPERVISOR", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("supervisorname") + "-" + object.getString("supervisorno"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.SUPERVISOR+"!", 25, 2)+
						    getAccurateText("TRIP ID", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TourID"), 18, 2)+getAccurateText(":*"+ArabicTEXT.tourId+"!", 30, 2), true, 1);
					outStream.write(UnderlineOff);
					outStream.write(NewLine);
					
					outStream.write(CompressOff);
				}else{
					/*printheaders(getAccurateText("ROUTE", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("ROUTE")+"!", 27, 0)+ 
						    getAccurateText("INVOICE DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 1), true, 1);
					outStream.write(NewLine);
					
					printheaders(getAccurateText("SALESMAN", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("SALESMAN")+"!" + "-" + object.getString("CONTACTNO"), 27, 0)+ 
						    getAccurateText("DELIVERY DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 1), true, 1);
					
					outStream.write(NewLine);
					outStream.write(UnderlineOn);
					
					printheaders(getAccurateText("SUPERVISOR", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("supervisorname")+"!" + "-" + object.getString("supervisorno"), 27, 0)+ 
						    getAccurateText("TRIP ID", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TourID"), 18, 2), true, 1);
					
					outStream.write(NewLine);
					outStream.write(UnderlineOff);*/
					outStream.write(CompressOn);
					if(TotQty>0){
						printheaders(getAccurateText("ROUTE", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("TO ROUTE")+"!", 31, 0)+ getAccurateText(":*"+ArabicTEXT.routeArabic+"!", 25, 2)+ 
							    getAccurateText("INVOICE DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.deliveryDate+"!", 28, 2), true, 1);
				
					}else{
						printheaders(getAccurateText("ROUTE", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("TO ROUTE")+"!", 31, 0)+ getAccurateText(":*"+ArabicTEXT.routeArabic+"!", 25, 2)+ 
							    getAccurateText("CREDIT NOTE DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.deliveryDate+"!", 28, 2), true, 1);
				
					}
						outStream.write(CompressOff);
					outStream.write(NewLine);
					outStream.write(CompressOn);
					printheaders(getAccurateText("SALESMAN", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("SALESMAN")+"!"+ "-" + object.getString("CONTACTNO"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.Salesman+"!", 25, 2)+
							(TotQty>0?(getAccurateText("DELIVERY DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.invoicedate+"!", 28, 2)):"")
						    , true, 1);
					outStream.write(CompressOff);
					outStream.write(NewLine);
					outStream.write(CompressOn);
					outStream.write(UnderlineOn);
					
					printheaders(getAccurateText("SUPERVISOR", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("supervisorname")+"!" + "-" + object.getString("supervisorno"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.SUPERVISOR+"!", 25, 2)+
						    getAccurateText("TRIP ID", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TourID"), 18, 2)+getAccurateText(":*"+ArabicTEXT.tourId+"!", 28, 2), true, 1);
					outStream.write(UnderlineOff);
					outStream.write(NewLine);
					
					outStream.write(CompressOff);
					
				}

				
				try {
					

				} catch (Exception e) {
						e.printStackTrace();
				}
				
	
			 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void headerTaxSummaryPrint(JSONObject object,int type,String ...args)throws JSONException{
		try {
			
			
			if (type == 1) {
				
				outStream.write(BoldOn);
				int printtax=Integer.parseInt(object.getString("printtax"));
				
					printheaders(getAccurateText("TAX INVOICE"+"*" + ArabicTEXT.taxInv + "! ", 80, 1), true, 1);
					
				if (object.has("companytaxregistrationnumber")){
					
					 printheaders(getAccurateText("Tax Registration No:", 30, 2)+getAccurateText(object.getString("companytaxregistrationnumber"), 20, 1)+getAccurateText(" :*" + ArabicTEXT.taxdetail + "! ", 30, 0), true, 1);
					 
				}else{
						if (object.has("invoiceformat") && object.getString("invoiceformat").equals("1")) {
							printheaders(getAccurateText(object.getString("companyname"), 40, 1), true, 1);
						}
				}
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(BoldOn);
				
				
				
				outStream.write(BoldOff);
				
				
			}
				
				outStream.write(NewLine);
				printheaders(getAccurateText("CustomerId:"+object.getString("CUSTOMERID"), 40, 0) + 
						getAccurateText(object.getString("CUSTOMERID")+":*" + ArabicTEXT.customerid + "!", 40, 2), true, 1);
				outStream.write(NewLine);
				printheaders(getAccurateText("Name :"+object.getString("CUSTOMERNAME"), 80, 0), true, 1);
				outStream.write(NewLine);
				if(object.has("arbcustomername") && object.getString("arbcustomername").length()>0){
					printheaders(getAccurateText("*" +object.getString("arbcustomername")+ "!"+":*" + ArabicTEXT.customername + "!", 80, 2), true, 1);
					outStream.write(NewLine);
				}
				if (object.has("ARBADDRESS")&&object.getString("ARBADDRESS").length()>0){
					 
					 printheaders(getAccurateText("Address :"+object.getString("ADDRESS"), 80, 0), true, 1);
					 outStream.write(NewLine);
						 printheaders(getAccurateText("*" + ArabicTEXT.address + "! "+object.getString("ADDRESS"), 80, 2) , true, 1);
					 }else if(object.has("ADDRESS")){
						 printheaders(getAccurateText("Address :"+object.getString("ADDRESS"), 80, 0), true, 1);
					 }

				outStream.write(NewLine);
				 if (object.has("contactDetail")){
					 printheaders(getAccurateText("Contact:"+object.getString("contactDetail"), 40, 0) + 
								getAccurateText(object.getString("contactDetail")+":*" + ArabicTEXT.contact + "! ", 40, 2), true, 1);
				 }
				outStream.write(NewLine);
				outStream.write(UnderlineOn);
			
				 if (object.has("taxregistrationnumber")){
					 printheaders(getAccurateText("TRN Number :"+object.getString("taxregistrationnumber"), 40, 0) + 
								getAccurateText(object.getString("taxregistrationnumber")+":*" + ArabicTEXT.taxRegNo + "! ", 40,2), true, 1);
				 }
				
				outStream.write(NewLine);
				outStream.write(UnderlineOff);
				outStream.write(BoldOff);
				if (object.getString("LANG").equals("en")) {
			
					outStream.write(CompressOn);
					
						printheaders(getAccurateText("ROUTE", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TO ROUTE"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.routeArabic+"!", 25, 2)+ 
							    getAccurateText("CREDIT NOTE DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.invoicedate+"!", 30, 2), true, 1);
					
					outStream.write(CompressOff);
					outStream.write(NewLine);
					outStream.write(CompressOn);
					printheaders(getAccurateText("SALESMAN", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("SALESMAN") + "-" 
					+ object.getString("CONTACTNO"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.Salesman+"!", 25, 2)+
					((getAccurateText("DELIVERY DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.deliveryDate+"!", 30, 2))), true, 1);
					outStream.write(CompressOff);
					outStream.write(NewLine);
					outStream.write(CompressOn);
					outStream.write(UnderlineOn);
					
					printheaders(getAccurateText("SUPERVISOR", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("supervisorname") + "-" + object.getString("supervisorno"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.SUPERVISOR+"!", 25, 2)+
						    getAccurateText("TRIP ID", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TourID"), 18, 2)+getAccurateText(":*"+ArabicTEXT.tourId+"!", 30, 2), true, 1);
					outStream.write(UnderlineOff);
					outStream.write(NewLine);
					
					outStream.write(CompressOff);
				}else{
					
					outStream.write(CompressOn);
					
						printheaders(getAccurateText("ROUTE", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("TO ROUTE")+"!", 31, 0)+ getAccurateText(":*"+ArabicTEXT.routeArabic+"!", 25, 2)+ 
							    getAccurateText("CREDIT NOTE DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.deliveryDate+"!", 28, 2), true, 1);
				
						outStream.write(CompressOff);
					outStream.write(NewLine);
					outStream.write(CompressOn);
					printheaders(getAccurateText("SALESMAN", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("SALESMAN")+"!"+ "-" + object.getString("CONTACTNO"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.Salesman+"!", 25, 2)+
							((getAccurateText("DELIVERY DATE", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 18, 2)+getAccurateText(":*"+ArabicTEXT.invoicedate+"!", 28, 2)))
						    , true, 1);
					outStream.write(CompressOff);
					outStream.write(NewLine);
					outStream.write(CompressOn);
					outStream.write(UnderlineOn);
					
					printheaders(getAccurateText("SUPERVISOR", 10, 0) + getAccurateText(" : ", 3, 0)+ getAccurateText("*"+object.getString("supervisorname")+"!" + "-" + object.getString("supervisorno"), 31, 0)+ getAccurateText(":*"+ArabicTEXT.SUPERVISOR+"!", 25, 2)+
						    getAccurateText("TRIP ID", 17, 2) + getAccurateText(" : ", 3, 0)+ getAccurateText(object.getString("TourID"), 18, 2)+getAccurateText(":*"+ArabicTEXT.tourId+"!", 28, 2), true, 1);
					outStream.write(UnderlineOff);
					outStream.write(NewLine);
					
					outStream.write(CompressOff);
					
				}

			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void headerKeyAccountprint(JSONObject object, int type) throws JSONException {
		
		try{
			line(6);
			count=count+6;
			outStream.write(BoldOn);
			String arbShortDescription=object.getString("arbshortname");
			if(arbShortDescription.length()>28){
				arbShortDescription=arbShortDescription.substring(0,28);
			}
			printheaders(getAccurateText("", 12, 0)+ getAccurateText("" + object.getString("invoicenumber") + "", 16, 0)+getAccurateText("", 52, 0), true, 0);
			printheaders(getAccurateText("", 12, 0)+ getAccurateText("", 16, 0)+getAccurateText("", 17, 0)+getAccurateText(arbShortDescription.length()>0?"*"+arbShortDescription+"!":"", 35, 0), true, 0);
			//line(1);
			outStream.write(NewLine);
			if(object.getString("LANG").equals("en")){
				printheaders(getAccurateText("", 12, 0)+ getAccurateText("" + object.getString("DOC DATE") + "", 16, 0)+getAccurateText("", 7, 0)+getAccurateText(object.getString("cname"), 45, 0), false, 1);
			}else{
				String arbDescription=object.getString("arbcustomername");
				if(arbDescription.length()>40){
					arbDescription=arbDescription.substring(0,40);
				}
				printheaders(getAccurateText("", 12, 0)+ getAccurateText("" + object.getString("DOC DATE") + "", 16, 0)+getAccurateText("", 7, 0)+getAccurateText("*"+arbDescription+"!", 45, 0), true, 1);
			}
			//line(1);
			outStream.write(NewLine);
			if(object.getString("LANG").equals("en")){
				printheaders(getAccurateText("", 12, 0)+ getAccurateText("", 16, 0)+getAccurateText("", 7, 0)+getAccurateText(object.getString("ADDRESS"), 45, 0), false, 1);
			}else{
				String arbAddress=object.getString("ARBADDRESS");
				if(arbAddress.length()>25){
					arbAddress=arbAddress.substring(0,25);
				}
				printheaders(getAccurateText("", 12, 0)+ getAccurateText("", 16, 0)+getAccurateText("", 7, 0)+getAccurateText("*"+arbAddress+"!", 45, 0), true, 1);
			}
			outStream.write(NewLine);
			if(object.getString("LANG").equals("en")){
				printheaders(getAccurateText("", 12, 0)+ getAccurateText("" + object.getString("CUSTOMERID") + "", 16, 0)+getAccurateText("", 7, 0)+getAccurateText("Contact:"+object.getString("contactDetail"), 45, 0), false, 1);
			}else{
				
				printheaders(getAccurateText("", 12, 0)+ getAccurateText("" + object.getString("CUSTOMERID") + "", 16, 0)+getAccurateText("", 7, 0)+getAccurateText("Contact:"+object.getString("contactDetail"), 45, 0), true, 1);
			}
			line(1);
			outStream.write(NewLine);
			printheaders(getAccurateText("", 12, 0)+ getAccurateText("", 16, 0)+getAccurateText("", 52, 0), false, 1);
			line(2);
			outStream.write(NewLine);
			outStream.write(BoldOff);
			count=count+4;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	private void keyAccountFooter(JSONObject object, int type,boolean netAmount) throws JSONException {
		
		try{
			
			String netSales=object.has("NET SALES")?object.getString("NET SALES"):"0";
			String invoiceDiscount=object.has("INVOICE DISCOUNT")?object.getString("INVOICE DISCOUNT"):"0";
			if(netAmount){
				printheaders(getAccurateText("", 60, 0)+getAccurateText(netSales, 20, 1), false, 1);
				
			}else{
				double totalamount=Double.parseDouble(netSales)-Double.parseDouble(invoiceDiscount);
				String decimalplace="3";
				if(object.has("decimalplace") && object.getString("decimalplace").length()>0){
					decimalplace= object.getString("decimalplace");
				}
				printheaders(getAccurateText("", 60, 0)+getAccurateText(String.format("%."+decimalplace+"f", totalamount), 20, 1), false, 1);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void headerprint(JSONObject object, int type) throws JSONException {
		try {

			
			if(type==1){
				
				if(object.has("customerprintout") && object.getString("customerprintout").equals("1")){
					outStream.write(BoldOn);
					
					String arbCustomerName = object.getString("arbcustomername");
					if (!TextUtils.isEmpty(arbCustomerName)) {
					
						printheaders(getAccurateText(("*" + arbCustomerName + "   !"),80,2), true,1);

					}
					
			/*		if (object.getString("LANG").equals("en")) {
						String customername = object.getString("CUSTOMER");
						printheaders(getAccurateText(customername+"   ",80,2), false, 2);

						
					}else{
						String arbCustomerName = object.getString("arbcustomername");
						if (!TextUtils.isEmpty(arbCustomerName)) {
						
							printheaders(getAccurateText(("*" + arbCustomerName + "   !"),80,2), true,1);

						}
					}*/
				
					outStream.write(NewLine);
					printlines2("", 1, object, 1, "", 1, 1);
					outStream.write(BoldOff);
					
				}
				
			}
			
			outStream.write(BoldOn);
			if (object.has("invoiceformat") && object.getString("invoiceformat").equals("1")) {

				outStream.write(DoubleWideOn);
				printArabic(getAccurateText(object.getString("companyname"), 40, 1));
				outStream.write(NewLine);
				outStream.write(DoubleWideOff);
				printArabic(getAccurateText(object.getString("companyaddress"), 80, 1));
				outStream.write(NewLine);
				outStream.write(getAccurateText(object.getString("arbcompanyname"), 80, 1).getBytes());
				outStream.write(NewLine);
				outStream.write(NewLine);

			}
			//object.getString("ROUTECODE") +
			if (object.getString("LANG").equals("en")) {
				printheaders(
						getAccurateText("ROUTE: "+ object.getString("ROUTECODE") +  object.getString("ROUTE"), 40, 0)
								+ getAccurateText(
										"DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")",
										40, 2),
						true, 1);
			} else {
				printheaders(getAccurateText(
						"ROUTE: " + object.getString("ROUTECODE") + "*" + object.getString("ROUTE") + "!", 40, 0)
						+ getAccurateText(
								"DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 40, 2),
						true, 1);
			}

			outStream.write(NewLine);
			String salesmanNo = object.getString("CONTACTNO") != "" ? "(" + object.getString("CONTACTNO") + ")" : "";
			if (object.getString("LANG").equals("en")) {
				printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMANCODE")
						+ object.getString("SALESMAN") + salesmanNo + "", 60, 0)
						+ getAccurateText("TRIP ID: " + object.getString("TourID"), 20, 2), true, 1);
			} else {

				printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMANCODE") + "*"
						+ object.getString("SALESMAN") + "!" + salesmanNo, 60, 0)
						+ getAccurateText("TRIP ID: " + object.getString("TourID"), 20, 2), true, 1);
			}
			outStream.write(NewLine);
			if ((object.has("supervisorname") && object.getString("supervisorname").length() > 0)
					|| (object.has("supervisorno") && object.getString("supervisorno").length() > 0)) {
				printheaders(getAccurateText("SUPERVISOR :" + object.getString("supervisorname") + "("
						+ object.getString("supervisorno") + ")", 40, 0), true, 1);
				outStream.write(NewLine);
			}

			// if (type == 3 || type == 5 || type == 6 || type == 4 || type ==
			// 7) {
			//
			// printheaders((getAccurateText("TRIP START DATE:" +
			// object.getString("TRIP START DATE"), 40, 0)
			// + getAccurateText("TOUR ID:" + object.getString("TourID"), 40,
			// 2)), false, 1);
			// } else {
			// if(type!=1){
			// printheaders((getAccurateText("TOUR ID:" +
			// object.getString("TourID"), 80, 0)), false, 1);
			// }
			// }
			outStream.write(BoldOff);
			// outStream.write(NewLine);
			outStream.write(NewLine);

			if (type != 3 || type != 6 || type != 4 || type != 5 || type != 7) {
				if (object.has("invheadermsg") && object.getString("invheadermsg").length() > 0) {
					printheaders(object.getString("invheadermsg"), false, 3);
					outStream.write(NewLine);
					outStream.write(NewLine);
				}
			}
			// lp.newLine(2);

			if (type == 1) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				if (!object.getString("LANG").equals("en")) {

					if (object.getString("invoicepaymentterms").contains("2")) {
						printheaders(getAccurateText(
								"*" + ArabicTEXT.Creditinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true,
								2);

					} else if (object.getString("invoicepaymentterms").contains("0")
							|| object.getString("invoicepaymentterms").contains("1")) {
						printheaders(
								getAccurateText("*" + ArabicTEXT.Cashinvoice + "!:" + object.getString("invoicenumber"),
										40, 1),
								true, 2);

					} else {
						printheaders((getAccurateText(object.getString("INVOICETYPE"), 40, 1)), false, 2);
					}

				} else {
					printheaders((getAccurateText(object.getString("INVOICETYPE"), 40, 1)), false, 2);
				}
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(NewLine);

			
				try {
					
					//if(object.has("customerprintout") && !object.getString("customerprintout").equals("1")){
						outStream.write(BoldOn);
						String customername = object.getString("CUSTOMER");
						String customertype=object.has("customertype")?object.getString("customertype"):"";
						if(!TextUtils.isEmpty(customertype)){
							printheaders(("CUSTOMER: " + customername + " ("+customertype+")"), false, 2);
							
						}else{
							printheaders(("CUSTOMER: " + customername + ""), false, 2);	
						}
						
	
						if (!object.getString("printlanguageflag").equals("2")) {
	
							String arbCustomerName = object.getString("arbcustomername");
							if (!TextUtils.isEmpty(arbCustomerName)) {
								outStream.write(NewLine);
								printheaders(("*" + arbCustomerName + "!"), true, 1);
	
							}
	
						}
						outStream.write(NewLine);
						outStream.write(BoldOff);
					//}
						printheaders(("ADDRESS: " + object.getString("ADDRESS") + ""), false, 1);
					outStream.write(NewLine);
					printheaders((object.getString("CONTACTNAME") + ""), false, 1);
					
					outStream.write(NewLine);
					if (!object.getString("printlanguageflag").equals("2")) {
						if (!TextUtils.isEmpty(object.getString("ARBADDRESS"))) {
							printheaders(("          *" + object.getString("ARBADDRESS") + "!"), true, 1);
							outStream.write(NewLine);
						}
					}
					outStream.write(NewLine);
				} catch (Exception e) {

				}

				count = count + 2;

			} else if (type == 2) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				if (!object.getString("LANG").equals("en")) {
					printheaders(
							getAccurateText("*" + ArabicTEXT.Receipt + "!:" + object.getString("RECEIPT") + "", 40, 1),
							true, 2);

				} else {
					printheaders(getAccurateText("RECEIPT: " + object.getString("RECEIPT"), 40, 1), false, 2);
				}
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(NewLine);
				outStream.write(BoldOn);
				/*if (!object.getString("LANG").equals("en")) {
					printheaders("CUSTOMER: *" + object.getString("CUSTOMER") + "!", true, 1);
				}else{
					printheaders("CUSTOMER: " + object.getString("CUSTOMER") + "", false, 1);
				}*/
				printheaders("CUSTOMER: " + object.getString("CUSTOMER") + "", false, 1);
				if (object.has("printlanguageflag") && !object.getString("printlanguageflag").equals("2")) {
					
					String arbCustomerName = object.getString("arbcustomername");
					if (!TextUtils.isEmpty(arbCustomerName)) {
						outStream.write(NewLine);
						printheaders(("*" + arbCustomerName + "!"), true, 1);

					}

				}

				outStream.write(NewLine);
				outStream.write(BoldOff);
				printheaders("ADDRESS :" + object.getString("ADDRESS") + "", true, 1);
				outStream.write(NewLine);
				outStream.write(NewLine);
				count = count + 1;
			} else if (type == 3) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				if (object.has("isDetail") && object.getString("isDetail").equals("1")) {
					printheaders(getAccurateText("CASH/CHEQUE ONHAND REPORT", 40, 1), false, 2);
				} else {
					printheaders(getAccurateText("DEPOSIT SUMMARY", 40, 1), false, 2);
					count = count + 2;
				}

				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(NewLine);
				count = count + 1;
			} else if (type == 4) {

				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("SALES SUMMARY", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(NewLine);
				count = count + 1;
			} else if (type == 5) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("ROUTE ACTIVITY LOG", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(NewLine);
				count = count + 1;
			} else if (type == 6) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("ROUTE SUMMARY", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(NewLine);

			} else if (type == 7) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("STALES/DAMAGE SUMMARY", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(NewLine);

			} else if (type == 8) {
				outStream.write(BoldOn);
				if (!object.getString("LANG").equals("en")) {
					printheaders("CUSTOMER: *" + object.getString("CUSTOMER") + "!", true, 1);
				}else{
					printheaders("CUSTOMER: " + object.getString("CUSTOMER") + "", false, 1);
				}
				outStream.write(NewLine);
				outStream.write(BoldOff);

				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("STATEMENT OF ACCOUNT", 40, 1), false, 3);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				outStream.write(NewLine);
				outStream.write(NewLine);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printheaders(String line, boolean isArabic, int pluscount) {

		if (!isArabic) {

			try {
				outStream.write(line.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			printArabic(line);
		}
		count = count + pluscount;
	}

	// Header inventory Start
	private void headerinvprint(JSONObject object, int invtype) throws JSONException {
		try {
			outStream.write(BoldOn);

			if (object.getString("LANG").equals("en")) {
				printheaders(
						getAccurateText("ROUTE: " + object.getString("ROUTECODE") + object.getString("ROUTE"), 40, 0)
								+ getAccurateText(
										"DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")",
										40, 2),
						true, 1);
			} else {
				printheaders(getAccurateText(
						"ROUTE: " + object.getString("ROUTECODE") + "*" + object.getString("ROUTE") + "!", 40, 0)
						+ getAccurateText(
								"DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 40, 2),
						true, 1);
			}

			outStream.write(NewLine);
			String salesmanNo = object.getString("CONTACTNO") != "" ? "(" + object.getString("CONTACTNO") + ")" : "";
			if (object.getString("LANG").equals("en")) {
				printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMANCODE")
						+ object.getString("SALESMAN") + salesmanNo + "", 40, 0)
						+ getAccurateText("TRIP ID: " + object.getString("TourID"), 40, 2), true, 1);
			} else {

				printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMANCODE") + "*"
						+ object.getString("SALESMAN") + "!" + salesmanNo, 40, 0)
						+ getAccurateText("TRIP ID: " + object.getString("TourID"), 40, 2), true, 1);
			}
			outStream.write(NewLine);

			if ((object.has("supervisorname") && object.getString("supervisorname").length() > 0)
					|| (object.has("supervisorno") && object.getString("supervisorno").length() > 0)) {
				String supervisorno = object.getString("supervisorno") != ""
						? "(" + object.getString("supervisorno") + ")" : "";
				printheaders(
						getAccurateText("SUPERVISOR NAME:" + object.getString("supervisorname") + supervisorno, 40, 0)
								+ getAccurateText("DOCUMENT NO: " + object.getString("DOCUMENT NO"), 40, 2),
						true, 1);
			} else {

				printheaders(getAccurateText("DOCUMENT NO: " + object.getString("DOCUMENT NO"), 40, 0), true, 1);
			}

			// if (object.getString("LANG").equals("en")) {
			// printheaders(
			// getAccurateText("ROUTE: " + object.getString("ROUTE"), 40,
			// 0)
			// + getAccurateText(
			// "DATE:" + object.getString("DOC DATE")
			// + " (" + object.getString("TIME")
			// + ")", 40, 2), true, 1);
			// }else{
			// printheaders(
			// getAccurateText("ROUTE: *" + object.getString("ROUTE")+"!", 40,
			// 0)
			// + getAccurateText(
			// "DATE:" + object.getString("DOC DATE")
			// + " (" + object.getString("TIME")
			// + ")", 40, 2), true, 1);
			// }
			//
			//
			// outStream.write(NewLine);
			// if (object.getString("LANG").equals("en")) {
			// printheaders(getAccurateText("SALESMAN: " +
			// object.getString("SALESMAN") + "", 40, 0)
			// + getAccurateText("SALESMAN NO: " +
			// object.getString("CONTACTNO"), 40, 2), true, 1);
			// }else{
			// printheaders(getAccurateText("SALESMAN: *" +
			// object.getString("SALESMAN") + "!", 40, 0)
			// + getAccurateText("SALESMAN NO: " +
			// object.getString("CONTACTNO"), 40, 2), true, 1);
			// }
			//
			// try {
			// printheaders(
			// (getAccurateText("DOCUMENT NO: " + object.getString("DOCUMENT
			// NO"), 40, 0)
			// + getAccurateText("TRIP START DATE:" + object.getString("TRIP
			// START DATE"), 40, 2)),
			// false, 1);
			// outStream.write(NewLine);
			// } catch (Exception e) {
			//
			// e.printStackTrace();
			// }
			// printheaders(getAccurateText("SUPERVISOR NAME:" +
			// object.getString("supervisorname"), 40, 0)
			// + getAccurateText("SUPERVISOR NO: " +
			// object.getString("supervisorno"), 40, 2), true, 1);
			// outStream.write(NewLine);
			// printheaders(getAccurateText("TOUR ID:" +
			// object.getString("TourID"), 80, 0), false, 2);
			outStream.write(BoldOff);
			outStream.write(NewLine);
			outStream.write(NewLine);
			outStream.write(BoldOn);
			outStream.write(DoubleWideOn);
			if (invtype == 1) {
				printheaders(getAccurateText("NEW LOAD SUMMARY - LOAD: " + object.getString("Load Number"), 40, 1),
						false, 2);
			} else if (invtype == 2) {
				printheaders(getAccurateText("LOAD TRANSFER SUMMARY", 40, 1), false, 2);
			} else if (invtype == 3) {
				printheaders(getAccurateText("END INVENTORY SUMMARY", 40, 1), false, 2);
			} else if (invtype == 5) {
				printheaders(getAccurateText("LOAD REQUEST", 40, 1), false, 2);
			} else if (invtype == 6) {
				printheaders(getAccurateText("COMPANY CREDIT SUMMARY", 40, 1), false, 2);
			}

			outStream.write(DoubleWideOff);
			outStream.write(BoldOff);
			outStream.write(NewLine);
			outStream.write(NewLine);
			if (invtype == 2) {
				JSONArray jData = object.getJSONArray("data");
				if (jData.getJSONObject(0).getJSONArray("DATA").length() > 0
						&& (jData.getJSONObject(1).getJSONArray("DATA").length() > 0
								|| jData.getJSONObject(2).getJSONArray("DATA").length() > 0)) {
					printheaders(getAccurateText("FROM & TO ROUTE: " + object.getString("TO ROUTE"), 80, 0), false, 1);
					outStream.write(NewLine);

				} else if (jData.getJSONObject(0).getJSONArray("DATA").length() > 0) {
					printheaders(getAccurateText("FROM ROUTE: " + object.getString("TO ROUTE"), 80, 0), false, 1);
					outStream.write(NewLine);

				} else if (jData.getJSONObject(1).getJSONArray("DATA").length() > 0) {
					printheaders(getAccurateText("TO ROUTE: " + object.getString("TO ROUTE"), 80, 0), false, 1);
					outStream.write(NewLine);

				} else if (jData.getJSONObject(2).getJSONArray("DATA").length() > 0) {
					printheaders(getAccurateText("TO ROUTE: " + object.getString("TO ROUTE"), 80, 0), false, 1);
					outStream.write(NewLine);

				}

			}
			if (invtype == 5) {
				// JSONArray jData = object.getJSONArray("data");
				// if (jData.getJSONObject(0).getJSONArray("DATA").length() > 0
				// && (jData.getJSONObject(1).getJSONArray("DATA").length() > 0
				// || jData.getJSONObject(2).getJSONArray("DATA").length() > 0))
				{
					printheaders(getAccurateText("Requested Delivery Date : " + object.getString("Requestdate"), 80, 0),
							false, 1);
					outStream.write(NewLine);
					outStream.write(NewLine);

				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void headervanstockprint(JSONObject object, int type) throws JSONException {
		try {
			outStream.write(BoldOn);

			if (object.getString("LANG").equals("en")) {
				printheaders(
						getAccurateText("ROUTE: " + object.getString("ROUTECODE") +  object.getString("ROUTE"), 40, 0)
								+ getAccurateText(
										"DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")",
										40, 2),
						true, 1);
			} else {
				printheaders(getAccurateText(
						"ROUTE: " + object.getString("ROUTECODE") + "*" + object.getString("ROUTE") + "!", 40, 0)
						+ getAccurateText(
								"DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 40, 2),
						true, 1);
			}

			outStream.write(NewLine);
			String salesmanNo = object.getString("CONTACTNO") != "" ? "(" + object.getString("CONTACTNO") + ")" : "";
			if (object.getString("LANG").equals("en")) {
				printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMANCODE")
						+ object.getString("SALESMAN") + salesmanNo + "", 40, 0)
						+ getAccurateText("TRIP ID: " + object.getString("TourID"), 40, 2), true, 1);
			} else {

				printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMANCODE") + "*"
						+ object.getString("SALESMAN") + "!" + salesmanNo, 40, 0)
						+ getAccurateText("TRIP ID: " + object.getString("TourID"), 40, 2), true, 1);
			}
			outStream.write(NewLine);
			if ((object.has("supervisorname") && object.getString("supervisorname").length() > 0)
					|| (object.has("supervisorno") && object.getString("supervisorno").length() > 0)) {
				String supervisorno = object.getString("supervisorno") != ""
						? "(" + object.getString("supervisorno") + ")" : "";
				printheaders(
						getAccurateText("SUPERVISOR NAME:" + object.getString("supervisorname") + supervisorno, 40, 0),
						true, 1);
				outStream.write(NewLine);
			}

			outStream.write(BoldOff);
			outStream.write(NewLine);
			outStream.write(NewLine);
			count = count + 1;
			if (type == 4) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("VAN STOCK SUMMARY ", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);

			} else if (type == 10) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("ITEM SALES SUMMARY ", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				count = count + 1;
			} else if (type == 27) {
				String customername = object.getString("Customercode");
				printheaders(getAccurateText("CUSTOMER: "+customername,80,0), false, 1);

				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("ITEM PRICE SUMMARY ", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				count = count + 1;
			}  else if (type == 11) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("TODAYS UNSERVICED ", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				count = count + 1;
			} else if (type == 6) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);
				printheaders(getAccurateText("COMPANY CREDIT SUMMARY", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				count = count + 1;

			} else if (type == 25) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);

				printheaders(getAccurateText("TEMPORARY CREDIT SUMMARY", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				count = count + 1;
			} else if (type == 26) {
				outStream.write(BoldOn);
				outStream.write(DoubleWideOn);

				printheaders(getAccurateText("DISCOUNT REPORT", 40, 1), false, 2);
				outStream.write(DoubleWideOff);
				outStream.write(BoldOff);
				count = count + 1;
			}
			outStream.write(NewLine);
			outStream.write(NewLine);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void headerRequestprint(JSONObject object) throws JSONException {
		try {
			outStream.write(BoldOn);

			outStream.write((getAccurateText("ROUTE: " + object.getString("ROUTE"), 40, 0)
					+ getAccurateText("DATE:" + object.getString("DOC DATE"), 40, 2)).getBytes());
			outStream.write((getAccurateText("SALESMAN: " + object.getString("SALESMAN"), 40, 0)
					+ getAccurateText("TIME:" + object.getString("TIME"), 40, 2)).getBytes());

			outStream.write((getAccurateText("DOCUMENT NO: " + object.getString("DOCUMENT NO"), 40, 0)
					+ getAccurateText("TRIP START DATE:" + object.getString("TRIP START DATE"), 40, 2)).getBytes());
			outStream.write(BoldOff);
			outStream.write(NewLine);
			outStream.write(NewLine);
			outStream.write(BoldOn);
			outStream.write(DoubleWideOn);
			outStream.write(getAccurateText("LOAD REQUEST", 40, 1).getBytes());
			outStream.write(DoubleWideOff);
			outStream.write(BoldOff);
			outStream.write(NewLine);
			outStream.write(NewLine);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// End
	// Printing Line
	private void line(int ln) {
		for (int i = 0; i < ln; i++) {
			
			try {
				outStream.write(NewLine);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//

	public void pairDevice(BluetoothDevice device, boolean clearSystemNotification) {
		String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
		Intent intent = new Intent(ACTION_PAIRING_REQUEST);
		String EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE";
		intent.putExtra(EXTRA_DEVICE, device);
		String EXTRA_PAIRING_VARIANT = "android.bluetooth.device.extra.PAIRING_VARIANT";
		int PAIRING_VARIANT_PIN = 1234;
		intent.putExtra(EXTRA_PAIRING_VARIANT, PAIRING_VARIANT_PIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.cordova.getActivity().startActivity(intent);
		if (clearSystemNotification) {
			clearSystemNotification();
		}

	}

	// -----------------------------------------------------------
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Message msg = Message.obtain();
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();

				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (arrayListBluetoothDevices.size() < 1) // this checks if the
															// size of bluetooth
															// device is 0,then
															// add the
				{ // device to the arraylist.
					detectedAdapter.add(device.getName() + "\n" + device.getAddress());
					arrayListBluetoothDevices.add(device);
					detectedAdapter.notifyDataSetChanged();
				} else {
					boolean flag = true; // flag to indicate that particular
											// device is already in the arlist
											// or not
					for (int i = 0; i < arrayListBluetoothDevices.size(); i++) {
						if (device.getAddress().equals(arrayListBluetoothDevices.get(i).getAddress())) {
							flag = false;
						}
					}
					if (flag == true) {
						detectedAdapter.add(device.getName() + "\n" + device.getAddress());
						arrayListBluetoothDevices.add(device);
						detectedAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	};
	private final BroadcastReceiver mReceiverRequiresPin = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				BluetoothDevice newDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Class<?> btDeviceInstance = Class.forName(BluetoothDevice.class.getCanonicalName());

				Method convert = btDeviceInstance.getMethod("convertPinToBytes", String.class);

				byte[] pin = (byte[]) convert.invoke(newDevice, "1234");

				Method setPin = btDeviceInstance.getMethod("setPin", byte[].class);
				boolean success = (Boolean) setPin.invoke(newDevice, pin);

				Log.e("Success", "success" + success);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	// -----------------------------------------------------------
	private NotificationManager notificationManager = null;

	public void clearSystemNotification() {
		try {
			if (notificationManager == null) {
				notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			}
			notificationManager.cancel(android.R.drawable.stat_sys_data_bluetooth);
		} catch (Exception e) {
		}
	}

	private NotificationManager getSystemService(String notificationService) {
		// TODO Auto-generated method stub
		return null;
	}

	private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
		public void onReceive1(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
				final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE,
						BluetoothDevice.ERROR);

				if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
					// showToast("Paired");
					Toast t = Toast.makeText(cordova.getActivity().getApplicationContext(), "Paired",
							Toast.LENGTH_SHORT);
					t.show();
				} else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
					// showToast("Unpaired");
					Toast t = Toast.makeText(cordova.getActivity().getApplicationContext(), "Unpaired",
							Toast.LENGTH_SHORT);
					t.show();
				}

			}
		}

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub

		}
	};

	//
	private void printconnect(String... args) {
		String sMacAddr = args[0];
		try {

			// ------------Start
			// Get Mac Add

			if (sMacAddr.contains(":") == false && sMacAddr.length() == 12) {
				// If the MAC address only contains hex digits without the
				// ":" delimiter, then add ":" to the MAC address string.
				char[] cAddr = new char[17];
				for (int i = 0, j = 0; i < 12; i += 2) {
					sMacAddr.getChars(i, i + 2, cAddr, j);
					j += 2;
					if (j < 17) {
						cAddr[j++] = ':';
					}
				}

				sMacAddr = new String(cAddr);
			}

			// String sPrinterURI = "bt://00:13:E0:D6:B2:3F";
			String sResult = null;
			String sPrinterURI = "bt://" + sMacAddr;
			BluetoothDevice device = mBtAdapter.getRemoteDevice(sMacAddr);

			try {

				btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);// works
																				// on
																				// lenovo
				devicename = device.getName();

			} catch (IOException e) {
				Toast t = Toast.makeText(cordova.getActivity().getApplicationContext(), "Socket create failed",
						Toast.LENGTH_SHORT);
				t.show();
				// return address[0] +"\nSocket create failed\n";
			} catch (IllegalArgumentException ioe) {
				Toast t = Toast.makeText(cordova.getActivity().getApplicationContext(),
						"is not a valid Bluetooth Address", Toast.LENGTH_SHORT);
				t.show();
				// return "is not a valid Bluetooth Address";
			}

			// btSocket =
			// device.createInsecureRfcommSocketToServiceRecord(MY_UUID); //
			// doesn't work on lenovo
			// --------
			int numtries = 0;
			int maxretry = 4;
			while (numtries < maxretry) {
				try {
					Thread.sleep(4000);
					btSocket.connect();
					if (device.getBondState() == BluetoothDevice.BOND_NONE) {
						pairDevice(mBtAdapter.getRemoteDevice(sMacAddr), true);
					}
					// final String actionPinRequested =
					// "android.bluetooth.device.action.PAIRING_REQUEST";
					// IntentFilter intentFilterPinRequested = new
					// IntentFilter(actionPinRequested);
					// this.cordova.getActivity().registerReceiver(mReceiverRequiresPin,
					// intentFilterPinRequested);
					// IntentFilter intent = new
					// IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
					// registerReceiver(mPairReceiver, intent);
					// Intent intent = null;
					// final String actionPinRequested =
					// "android.bluetooth.device.action.PAIRING_REQUEST";
					// IntentFilter intentFilterPinRequested = new IntentFilter(
					// actionPinRequested);

					// DotmatHelper.this.registerReceiver(mReceiverRequiresPin,
					// intentFilterPinRequested);

					outStream = btSocket.getOutputStream(); // Connects to the
															// printer
					break;
				} catch (IOException e) {
					// btSocket.close();
					numtries++;
					// Thread.sleep(2000);
					Toast t = Toast.makeText(cordova.getActivity().getApplicationContext(),
							"Error connecting to Socket", Toast.LENGTH_SHORT);
					// t.show();
					// return address[0] +"\n"+devicename+
					// "\nError connecting to Socket\n";

				}
			}
			/*
			 * if (numtries == maxretry) {
			 * 
			 * btSocket.connect(); if(device.getBondState() ==
			 * BluetoothDevice.BOND_NONE) {
			 * pairDevice(mBtAdapter.getRemoteDevice(sMacAddr),true); }
			 * outStream = btSocket.getOutputStream();
			 * 
			 * }
			 */

			// --------

		} catch (Exception e) {

			// TODO Auto-generated catch block
			// pairDevice(mBtAdapter.getRemoteDevice(sMacAddr),true);

			e.printStackTrace();
			try {

				status.put("status", false);
				status.put("isconnected", -2);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			sendUpdate(status, true);
		}
	}

	private String getAccurateText(String text, int width, int position) {
		String finalText = "";
		if (text.length() == width) {
			Log.d("Matched String", text);
			finalText = text;
		} else if (text.length() > width) {

			finalText = text.substring(0, width);
		} else {
			finalText = text;
			Log.d("String", finalText);
			Log.d("length", "" + finalText.length());
			switch (position) {
			case 0:
				for (int i = 0; i < (width - text.length()); i++) {
					finalText = finalText.concat(" ");
				}

				break;
			case 1:
				for (int i = 0; i < (width - text.length()); i++) {
					if (i < (width - text.length()) / 2) {
						finalText = " " + finalText;
					} else {
						finalText = finalText + " ";
					}

				}

				break;
			case 2:
				for (int i = 0; i < (width - text.length()); i++) {
					finalText = " " + finalText;
				}
				break;
			default:
				break;
			}

		}

		return finalText;

	}
	
	void printOrderTaxReport(JSONObject object, String... args) {
		StringBuffer s1 = new StringBuffer();
		int printoultlet = 0;
		try {
			
			String cases = object.has("cases") ? object.getString("cases") : "CASE";
			String pcs = object.has("pcs") ? object.getString("pcs") : "PCS";
			String qty = object.has("qty") ? object.getString("qty") : "QTY CAS/PCS";
			int CaseEnabled = object.has("CaseEnabled") ? Integer.parseInt(object.getString("CaseEnabled")) : 1;
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			String printLanguage=object.has("printlanguageflag")?object.getString("printlanguageflag"):"2";
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));
			int printtax = object.has("printtax")?Integer.parseInt(object.getString("printtax")):0;
			int totaldiscount = object.has("discountprint") ? Integer.parseInt(object.getString("discountprint")) : 0;
			double totaldiscountval = object.has("TOTAL LINE DISCOUNT")
					? Double.parseDouble(object.getString("TOTAL LINE DISCOUNT")) : 0;
			String totalpcs = object.has("totalpcs") ? object.getString("totalpcs") : "0";
			
			JSONArray jDataNew = object.getJSONArray("data");
			double excTot=0,vatTot=0;
			for (int i = 0; i < jDataNew.length(); i++) {
				JSONObject mainJsonNew = jDataNew.getJSONObject(i);
				JSONObject jTotalNew = mainJsonNew.getJSONObject("TOTAL");
				 excTot = excTot + Double.parseDouble(jTotalNew.getString("EXCISE TAX"));
				 vatTot = vatTot + Double.parseDouble(jTotalNew.getString("VAT AMOUNT"));
			}
			
			
			// -------------------START
			// Set font style to Bold + Double Wide + Double High.
			if (object.getString("printoutletitemcode").length() > 0) {
				printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
			} else {
				printoultlet = 0;
			}

			if (object.getString("displayupc").equals("1")) {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 3);
				hashValues.put("ITEM#", 11);
				if (printoultlet == 1) {
					hashValues.put("OUTLET CODE", 10);
					
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 31);
						hashValues.put("ARBDESCRIPTION", 27);
						hashValues.put(barcode, 27);
					} else {
						hashValues.put("ITEM#", 11);
						hashValues.put("DESCRIPTION", 22);
						hashValues.put("ARBDESCRIPTION", 25);
						hashValues.put(barcode, 25);
					}
				} else {
					hashValues.put("OUTLET CODE", 0);
					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 38);
						hashValues.put("ARBDESCRIPTION", 30);
						hashValues.put(barcode, 30);
						
					} else {
						hashValues.put("ITEM#", 11);
						hashValues.put("DESCRIPTION", 30);
						hashValues.put("ARBDESCRIPTION", 27);
						hashValues.put(barcode, 27);
					}
				}
			
				
				hashValues.put("UOM", 4);
				hashValues.put(qty, 9);
				hashValues.put("" + pcs, 0);
				hashValues.put("GROSS AMOUNT", 0);
				if (printtax > 0) {
					if(excTot>0&&vatTot>0){
                    	hashValues.put("EXCISE TAX", 7);
                    	hashValues.put("VAT AMOUNT", 12);
                    	hashValues.put("AMOUNT", 10);
                    }else if(excTot>0){
                    	hashValues.put("EXCISE TAX", 15);
                    	hashValues.put("VAT AMOUNT", 0);
                    	hashValues.put("AMOUNT", 14);
                    }else if(vatTot>0){
                    	hashValues.put("EXCISE TAX", 0);
                    	hashValues.put("VAT AMOUNT", 12);
                    	hashValues.put("AMOUNT", 9);
                    	hashValues.put("GROSS AMOUNT", 8);
                    }else{
                    	hashValues.put("EXCISE TAX", 0);
                    	hashValues.put("VAT AMOUNT", 0);
                    	hashValues.put("AMOUNT", 29);
                    }
					
					if (totaldiscount == 0 && totaldiscountval > 0) {
						hashValues.put("DISCOUNT", 9);
						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 8);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);  //hiding case price
								hashValues.put(pcs + " PRICE", 15);
							}
							

						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 8);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 15);
							}
							
						}

					} else {
						hashValues.put("DISCOUNT", 0);

						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 15);
								hashValues.put(pcs + " PRICE", 9);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 24);
							}
							

						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 12);
								hashValues.put(pcs + " PRICE", 12);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 24);
							}
							
						}
					}
				} else {

					hashValues.put("EXCISE TAX", 0);
					hashValues.put("VAT AMOUNT", 0);
					hashValues.put("AMOUNT", 17);
					if (totaldiscount == 0 && totaldiscountval > 0) {
						hashValues.put("DISCOUNT", 17);
						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 4);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 8);
								hashValues.put(pcs + " PRICE", 7);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 15);
							}
							

						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 10);
								hashValues.put(pcs + " PRICE", 9);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 19);
							}
							
						}
					} else {
						hashValues.put("DISCOUNT", 0);

						if (totalpcs.equals("1")) {
							hashValues.put("" + pcs, 4);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 17);
								hashValues.put(pcs + " PRICE", 15);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 32);
							}
							
						} else {
							hashValues.put("" + pcs, 0);
							if(CaseEnabled==1){
								hashValues.put(cases + " PRICE", 18);
								hashValues.put(pcs + " PRICE", 18);
							}else{
								hashValues.put(cases + " PRICE", 0);
								hashValues.put(pcs + " PRICE", 36);
							}
							
						}
					}
				}

				

				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("ARBDESCRIPTION", 2);
				hashPositions.put("UOM", 1);
				hashPositions.put("" + pcs, 1);
				hashPositions.put(qty, 1);
				hashPositions.put(cases + " PRICE", 2);
				hashPositions.put(pcs + " PRICE", 2);
				hashPositions.put(barcode, 2);
				hashPositions.put("GROSS AMOUNT", 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("EXCISE TAX", 2);
				hashPositions.put("VAT AMOUNT", 2);
				hashPositions.put("AMOUNT", 2);

			} else {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 4);
				hashValues.put("ITEM#", 8);
				hashValues.put("OUTLET CODE", 8);
				hashValues.put("DESCRIPTION", 36);
				hashValues.put("QTY CAS/PCS", 3);
				hashValues.put("QTY OUT/PCS", 3);
				if(CaseEnabled==1){
					hashValues.put("CASE PRICE", 7);
					hashValues.put("UNIT PRICE", 7);
				}else{
					hashValues.put("CASE PRICE", 0);
					hashValues.put("UNIT PRICE", 14);
				}
				
				hashValues.put("DISCOUNT", 0);
				hashValues.put("AMOUNT", 8);
				hashValues.put("OUTER PRICE", 7);
				hashValues.put("PCS PRICE", 7);
				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				 // hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("QTY CAS/PCS", 2);
				hashPositions.put("CASE PRICE", 2);
				hashPositions.put("UNIT PRICE", 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("AMOUNT", 2);
				hashPositions.put("OUTER PRICE", 2);
				hashPositions.put("PCS PRICE", 2);
			}
			// ---------Start

			// ----------End
			line(startln);
			// lp.newLine(5);
			
			headerTaxprint(object, 2,args[0]);
			outStream.write(NewLine);
			Log.e("Printoutletitemcode", "" + printoultlet);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (jInnerData.length() > 0) {

					String header = mainJson.getString("TITLE").trim();
					String HeadTitle = "";

					if (object.has("printlanguageflag") && object.getString("printlanguageflag").equals("2")) {
						if (header.equals("order")) {
							HeadTitle = "ORDER  *" + ArabicTEXT.Order + "!";

						} else if (header.equals("free")) {

							HeadTitle = "TRADE DEAL  *" + ArabicTEXT.tradeDeal + "!";

						} else if (header.equals("bad")) {

							HeadTitle = "BAD RETURN  *" + ArabicTEXT.BadReturn + "!";

						} else if (header.equals("good")) {
							HeadTitle = "GOOD RETURN  *" + ArabicTEXT.GoodReturn + "!";

						} else if (header.equals("promofree")) {

							HeadTitle = "PROMOTION FREE  *" + ArabicTEXT.PromotionFree + "!";

						} else if (header.equals("buyback")) {

							HeadTitle = "BUYBACK FREE  *" + ArabicTEXT.BuybackFree + "!";

						}
					} else {
						if (header.equals("order")) {
							HeadTitle = "ORDER  *" + ArabicTEXT.Order + "!";

						} else if (header.equals("free")) {

							HeadTitle = "TRADE DEAL  *" + ArabicTEXT.tradeDeal + "!";

						} else if (header.equals("bad")) {

							HeadTitle = "BAD RETURN  *" + ArabicTEXT.BadReturn + "!";

						} else if (header.equals("good")) {
							HeadTitle = "GOOD RETURN  *" + ArabicTEXT.GoodReturn + "!";

						} else if (header.equals("promofree")) {

							HeadTitle = "PROMOTION FREE  *" + ArabicTEXT.PromotionFree + "!";

						} else if (header.equals("buyback")) {

							HeadTitle = "BUYBACK FREE  *" + ArabicTEXT.BuybackFree + "!";

						}
					}

					outStream.write(BoldOn);
					outStream.write(UnderlineOn);
					outStream.write("       ".getBytes());
					printlines2(HeadTitle, 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOff);
					outStream.write(BoldOff);

				}
				int MAXLEngth = 137;

				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}
				boolean isoutlet = false;
				String strheader = "", strHeaderBottom = "", strTotal = "";
				String barcodeVal = "";

				for (int j = 0; j < headers.length(); j++) {

					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(j));
					if (j == 4 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					strheader = strheader + getAccurateText(
							(j == 4 && object.getString("printbarcode").equals("0")) ? ""
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(j == 4 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("DESCRIPTION") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					String strArbheader = ArabicTEXT.getArabicHeaderDotmat(excTot,vatTot,totaldiscount);
					printlines2(strArbheader, 1, object, 1, args[0], 1, 1);
					
					printlines2(strheader, 1, object, 1, args[0], 1, 1);
					outStream.write(UnderlineOn);
					if (strHeaderBottom.length() > 0) {
						printlines2(strHeaderBottom, 1, object, 1, args[0], 1, 1);
					}
					outStream.write(CompressOff);
					outStream.write(UnderlineOff);

				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					boolean isoutletdata = false;
					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 0) {
							itemDescrion = (l + 1) + "";

						} else if (m == 4) {
							itemDescrion = "";
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(m);
							} else {

								try {
									itemDescrion = "*" + jArr.getString(m) + "!";
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}

						strData = strData + getAccurateText(itemDescrion,
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

					}

					// lp.writeLine(strData);
					outStream.write(CompressOn);
					/* count++; */
					if(l==jInnerData.length()-1){
						outStream.write(UnderlineOn);
					}
					printlines2(strData.trim(), 1, object, 1, args[0], 1, 1);
					if(l==jInnerData.length()-1){
						outStream.write(UnderlineOff);
					}
					outStream.write(CompressOff);
				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					//printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					printlines2(strTotal, 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);

				}

			}
			
			int taxSetting=0;
			
			if(taxSetting==1){
				
				printArabic(getAccurateText("  ", 15, 1) + getAccurateText("TOTAL ", 15, 1)+
						getAccurateText("TAX " , 15, 1) +getAccurateText("TOTAL AMOUNT" , 15, 1) );
				outStream.write(NewLine);
				
				printlines2(getAccurateText("________________________________________________________________________________________   ", 60, 1), 2, object, 1, args[0], 1, 1);

				
				double salesamnt=0,retrnamnt=0,damageamnt=0,freeamnt=0;
				double salestax=0,retrntax=0,damagetax=0,freetax=0;
				
				if(Integer.parseInt(object.getString("totalSalesQty"))>0){
					salesamnt=Double.parseDouble(object.getString("TOTSALES"));
					salestax=Double.parseDouble(object.getString("SALESTAX"));
					printlines2(getAccurateText("SALES", 15, 1) + getAccurateText(object.getString("TOTSALES"), 15, 1)+
							getAccurateText(object.getString("SALESTAX") , 15, 1) +getAccurateText(String.valueOf(salesamnt+salestax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalFreeQty"))>0){
					freeamnt=Double.parseDouble(object.getString("TOTFREE"));
					freetax=Double.parseDouble(object.getString("FREETAX"));
					printlines2(getAccurateText("FREE", 15, 1) + getAccurateText(object.getString("TOTFREE"), 15, 1)+
							getAccurateText(object.getString("FREETAX") , 15, 1) +getAccurateText(String.valueOf(freeamnt+freetax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalReturnQty"))>0){
					retrnamnt=Double.parseDouble(object.getString("TOTGOOD"));
					retrntax=Double.parseDouble(object.getString("RETURNTAX"));
					printlines2(getAccurateText("GOOD RETURN", 15, 1) + getAccurateText(object.getString("TOTGOOD"), 15, 1)+
							getAccurateText(object.getString("RETURNTAX") , 15, 1) +getAccurateText(String.valueOf(retrnamnt+retrntax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				if(Integer.parseInt(object.getString("totalDamagedQty"))>0){
					damageamnt=Double.parseDouble(object.getString("TOTBAD"));
					damagetax=Double.parseDouble(object.getString("DAMAGEDTAX"));
					printlines2(getAccurateText("BAD RETURN", 15, 1) + getAccurateText(object.getString("TOTBAD"), 15, 1)+
							getAccurateText(object.getString("DAMAGEDTAX") , 15, 1) +getAccurateText(String.valueOf(damageamnt+damagetax) , 15, 1), 1, object, 1, args[0], 1, 1 );
					outStream.write(NewLine);
				}
				
				
				
				printlines2(getAccurateText("________________________________________________________________________________________   ", 60, 1), 2, object, 1, args[0], 1, 1);

				
				printArabic(getAccurateText("TOTAL", 15, 1) + getAccurateText(String.valueOf(salesamnt+retrnamnt+damageamnt+freeamnt), 15, 1)+
						getAccurateText(object.getString("TOTTAX") , 15, 1) +getAccurateText(String.valueOf(salesamnt+salestax+retrnamnt+retrntax+damageamnt+damagetax+freeamnt+freetax) , 15, 1) );
				outStream.write(NewLine);
				
              if (object.has("TOTEXC")) {
					
					if(Double.parseDouble(object.getString("TOTEXC"))!=0){
						int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
						if(companyTaxStng==1&&taxSetting!=1){
							printlines2(
									(getAccurateText("TOTAL EXCISE TAX", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTEXC"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalExcTax, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}

				}
				
				if (object.has("TOTVAT")) {
					
					if(Double.parseDouble(object.getString("TOTVAT"))!=0){
						int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
						if(companyTaxStng==1&&taxSetting!=1){
							printlines2(
									(getAccurateText("TOTAL VAT", 20, 0) + getAccurateText(" : ", 3, 0)
											+ getAccurateText(object.getString("TOTVAT"), 12, 0)
											+ getAccurateText(" : ", 3, 0) + "*"
											+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
									1, object, 1, args[0], 1, 1);
						}
					}

				}
				
				outStream.write(NewLine);
				
			}
			
			outStream.write(NewLine);
			outStream.write(BoldOn);
			printlines2((getAccurateText("TOTAL ORDER AMOUNT", 40, 2) + getAccurateText(" : ", 3, 0)
					+ getAccurateText(object.getString("SUB TOTAL") , 12, 2) + getAccurateText(" : ", 3, 0) + "*"
					+ getAccurateText(ArabicTEXT.SubTotal, 15, 2) + "!"), 1, object, 1, args[0], 1, 1);
			outStream.write(BoldOff);
		
			outStream.write(BoldOn);
			if (object.has("TOTEXC")) {
				
				if(Double.parseDouble(object.getString("TOTEXC"))!=0){
					int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
					if(companyTaxStng==1&&taxSetting!=1){
						printlines2(
								(getAccurateText("TOTAL EXCISE TAX", 40,2) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("TOTEXC"), 12, 2)
										+ getAccurateText(" : ", 3, 0) + "*"
										+ getAccurateText(ArabicTEXT.TotalExcTax, 20, 2) + "!"),
								1, object, 1, args[0], 1, 1);
					}
				}

			}
			outStream.write(BoldOn);
			if (object.has("ORDER DISCOUNT") && object.getString("ORDER DISCOUNT").toString().length() > 0) {
				double invoice = Double.parseDouble(object.getString("ORDER DISCOUNT"));

				if (invoice != 0) {

					printlines2(
							(getAccurateText("ORDER DISCOUNT", 40, 2) + getAccurateText(" : ", 3, 0)
									+ getAccurateText(object.getString("ORDER DISCOUNT"), 12, 2)
									+ getAccurateText(" : ", 3, 0) + "*"
									+ getAccurateText(ArabicTEXT.OrderDiscount, 15, 2) + "!"),
							1, object, 1, args[0], 1, 1);
				}
			}

			outStream.write(BoldOff);
			if (object.has("TOTVAT")) {
				
				if(Double.parseDouble(object.getString("TOTVAT"))!=0){
					int  companyTaxStng=Integer.parseInt(object.getString("enabletax"));
					if(companyTaxStng==1&&taxSetting!=1){
						printlines2(
								(getAccurateText("TOTAL VAT", 40, 2) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("TOTVAT"), 12, 2)
										+ getAccurateText(" : ", 3, 0) + "*"
										+ getAccurateText(ArabicTEXT.TotalVatTax, 20, 2) + "!"),
								1, object, 1, args[0], 1, 1);
					}
				}

			}
			
			outStream.write(BoldOff);
			
			outStream.write(BoldOn);
			printlines2(
					(getAccurateText("NET AMOUNT", 40, 2) + getAccurateText(" : ", 3, 0)
							+ getAccurateText(object.getString("NET AMOUNT"), 12, 2)
							+ getAccurateText(" : ", 3, 0) + "*" + getAccurateText(ArabicTEXT.NetAmount, 15, 2) + "!"),
					1, object, 1, args[0], 1, 1);
			outStream.write(BoldOff);

			/*if (object.has("TCALLOWED") && object.getString("TCALLOWED").toString().trim().length() > 0
					&& object.getString("TCALLOWED").equals("1")) {

				printlines2(
						(getAccurateText("TC CHARGED", 20, 0) + getAccurateText(" : ", 3, 0)
								+ getAccurateText(object.getString("TCCHARGED"), 12, 0) + getAccurateText(" : ", 3, 0)
								+ "*" + getAccurateText(ArabicTEXT.TCcharged, 15, 2) + "!"),
						1, object, 1, args[0], 1, 1);

			} else {
				printlines2("", 2, object, 1, args[0], 1, 1);
			}*/
			printlines2("", 1, object, 1, args[0], 1, 1);
			if (object.has("PaymentType") && Integer.parseInt(object.getString("PaymentType")) < 2) {
				// position = position + 60;
				outStream.write(BoldOn);
				if (!object.getString("LANG").equals("en")) {
					printlines2(getAccurateText("PAYMENT DETAILS   " + "*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2,
							object, 1, args[0], 1, 1);
				} else {
					printlines2(getAccurateText("PAYMENT DETAILS", 80, 1), 2, object, 1, args[0], 1, 1);
				}

				outStream.write(BoldOff);
				// lp.newLine(2);

				JSONArray jCheques = object.has("Cheque") ? object.getJSONArray("Cheque") : null;
				JSONObject jCash = object.has("Cash") ? object.getJSONObject("Cash") : null;
				int paymenttype = Integer.parseInt(object.getString("ptype"));

				switch (paymenttype) {
				case 0:

					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(
								getAccurateText("CASH:  " + jCash.getString("Amount") + "   :*" + ArabicTEXT.Cash + "!",
										80, 1),
								2, object, 1, args[0], 1, 1);

					} else {
						printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					outStream.write(BoldOff);
					break;
				case 1:
					outStream.write(BoldOn);
					// lp.write("CHEQUE");
					printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					// lp.newLine(2);
					printlines2(
							(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0)
									+ getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Bank"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 20, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					printlines2("", 1, object, 1, args[0], 1, 1);
					// lp.newLine(2);
					break;
				case 2:
					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(
								getAccurateText("CASH:  " + jCash.getString("Amount") + "   :*" + ArabicTEXT.Cash + "!",
										80, 1),
								2, object, 1, args[0], 1, 1);

					} else {
						printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0],
								1, 1);
					}
					printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					// lp.newLine(2);
					printlines2(
							(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0)
									+ getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Bank"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 20, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					printlines2("", 1, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);

					break;

				default:
					break;
				}
			}
			if (object.getString("comments").toString().length() > 0) {

				printlines2("Comments:" + object.getString("comments"), 2, object, 1, args[0], 1, 1);

			}
			if (object.has("lpoNumber") && object.getString("lpoNumber").toString().length() > 0) {

				printlines2("LPO Number:" + object.getString("lpoNumber"), 2, object, 1, args[0], 1, 1);

			}
			if (object.getString("invtrailormsg").toString().length() > 0) {
				printlines2(object.getString("invtrailormsg"), 2, object, 1, args[0], 1, 1);

			}
			
			if(object.has("applytax") && object.getString("applytax").equals("0")){
				outStream.write(CompressOn);
				printlines2(getAccurateText("The amount is exclusive of VAT " + "*" + ArabicTEXT.orderexVat + "!", 136, 1), 1,
						object, 1, args[0], 1, 1);
				outStream.write(CompressOff);
			}
			
			printlines2("", 3, object, 1, args[0], 1, 1);
			printlines2(
					getAccurateText("CUSTOMER____________*" + ArabicTEXT.Customer+ "!    SALESMAN_____________*" + ArabicTEXT.Salesman + "!", 80, 1),
					2, object, 1, args[0], 1, 1);
			outStream.write(NewLine);
			String copyStatus = "";
			if (object.getString("printstatus").equals("DUPLICATE COPY")) {

				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DuplicateCopy + "",
						80, 1) + "!";
			} else if (object.getString("printstatus").equals("ORIGINAL COPY")) {
				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.OriginalCopy + "!",
						80, 1);

			} else {
				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DraftCopy + "!", 80,
						1);

			}
			printlines2(copyStatus, 2, object, 2, args[0], 1, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void printOrderReport(JSONObject object, String... args) {
		StringBuffer s1 = new StringBuffer();
		int printoultlet = 0;
		try {
			String cases = object.has("cases") ? object.getString("cases") : "CASE";
			String pcs = object.has("pcs") ? object.getString("pcs") : "PCS";
			String qty = object.has("qty") ? object.getString("qty") : "QTY CAS/PCS";
			String barcode = object.has("barcode") ? object.getString("barcode") : "Barcode";
			int CaseEnabled = object.has("CaseEnabled") ? Integer.parseInt(object.getString("CaseEnabled")) : 1;


			if (object.getString("printoutletitemcode").length() > 0) {
				printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
			} else {
				printoultlet = 0;
			}
			int printitemcode = Integer.valueOf(object.getString("printitemcode"));

			int totaldiscount = object.has("discountprint") ? Integer.parseInt(object.getString("discountprint")) : 0;

			double totaldiscountval = object.has("TOTAL LINE DISCOUNT")
					? Double.parseDouble(object.getString("TOTAL LINE DISCOUNT")) : 0;

			if (object.getString("displayupc").equals("1")) {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 3);

				if (printoultlet == 1) {
					hashValues.put("OUTLET CODE", 15);

					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 33);
					} else {
						hashValues.put("ITEM#", 11);
						hashValues.put("DESCRIPTION", 22);
					}
				} else {
					hashValues.put("OUTLET CODE", 0);

					if (printitemcode == 0) {
						hashValues.put("ITEM#", 0);
						hashValues.put("DESCRIPTION", 42);
					} else {
						hashValues.put("ITEM#", 11);
						hashValues.put("DESCRIPTION", 31);
					}
				}
				hashValues.put("ARBDESCRIPTION", 48);
				hashValues.put(barcode, 31);
				hashValues.put("UOM", 5);
				hashValues.put(qty, 8);
				hashValues.put("GROSS AMOUNT", 0);
				if (totaldiscount == 0 && totaldiscountval != 0) {
					hashValues.put("DISCOUNT", 11);
					if(CaseEnabled==1){
						hashValues.put(cases + " PRICE", 7);
						hashValues.put(pcs + " PRICE", 2);
					}else{
						hashValues.put(cases + " PRICE", 0);
						hashValues.put(pcs + " PRICE", 9);
					}
					
				} else {
					hashValues.put("DISCOUNT", 0);
					if(CaseEnabled==1){
						hashValues.put(cases + " PRICE", 12);
						hashValues.put(pcs + " PRICE", 8);
					}else{
						hashValues.put(cases + " PRICE", 0);
						hashValues.put(pcs + " PRICE", 20);
					}
					
				}

				hashValues.put("EXCISE TAX", 0);
				hashValues.put("VAT AMOUNT", 0);
				hashValues.put("AMOUNT", 10);

				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("ARBDESCRIPTION", 2);
				hashPositions.put(barcode, 2);
				hashPositions.put("UOM", 1);
				hashPositions.put(qty, 1);
				hashPositions.put(cases + " PRICE", 2);
				hashPositions.put(pcs + " PRICE", 2);
				hashPositions.put("DISCOUNT", 2);
				hashPositions.put("GROSS AMOUNT", 2);
				hashPositions.put("EXCISE TAX", 2);
				hashPositions.put("VAT AMOUNT", 2);
				hashPositions.put("AMOUNT", 2);
			} else {
				hashValues = new HashMap<String, Integer>();
				hashValues.put("SL#", 4);
				hashValues.put("ITEM#", 8);
				hashValues.put("OUTLET CODE", 8);
				hashValues.put("DESCRIPTION", 36);
				hashValues.put("QTY CAS/PCS", 3);
				hashValues.put("QTY OUT/PCS", 3);
				hashValues.put("CASE PRICE", 7);
				hashValues.put("UNIT PRICE", 7);
				hashValues.put("DISCOUNT", 0);
				hashValues.put("AMOUNT", 8);
				hashValues.put("OUTER PRICE", 7);
				hashValues.put("PCS PRICE", 7);
				hashPositions = new HashMap<String, Integer>();
				hashPositions.put("SL#", 0);
				hashPositions.put("ITEM#", 0);
				hashPositions.put("OUTLET CODE", 0);
				// hashPositions.put("DESCRIPTION", 0);
				hashPositions.put("QTY CAS/PCS", 2);
				hashPositions.put("CASE PRICE", 2);
				hashPositions.put("UNIT PRICE", 2);
				hashPositions.put("DISCOUNT", 2);
				hashValues.put("EXCISE TAX", 2);
				hashValues.put("VAT AMOUNT", 2);
				hashPositions.put("AMOUNT", 2);
				hashPositions.put("OUTER PRICE", 2);
				hashPositions.put("PCS PRICE", 2);
			}
			// ---------Start

			// ----------End
			line(startln);
			// lp.newLine(5);
			headerprint(object, 1);

			Log.e("Printoutletitemcode", "" + printoultlet);
			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (jInnerData.length() > 0) {

					String header = mainJson.getString("TITLE").trim();
					String HeadTitle = "";

					if (object.has("printlanguageflag") && object.getString("printlanguageflag").equals("2")) {
						if (header.equals("order")) {
							HeadTitle = "ORDER";

						} else if (header.equals("free")) {

							HeadTitle = "TRADE DEAL";

						} else if (header.equals("bad")) {

							HeadTitle = "BAD RETURN";

						} else if (header.equals("good")) {
							HeadTitle = "GOOD RETURN";

						} else if (header.equals("promofree")) {

							HeadTitle = "PROMOTION FREE";

						} else if (header.equals("buyback")) {

							HeadTitle = "BUYBACK FREE";

						}
					} else {
						if (header.equals("order")) {
							HeadTitle = "ORDER  *" + ArabicTEXT.Order + "!";

						} else if (header.equals("free")) {

							HeadTitle = "TRADE DEAL  *" + ArabicTEXT.tradeDeal + "!";

						} else if (header.equals("bad")) {

							HeadTitle = "BAD RETURN  *" + ArabicTEXT.BadReturn + "!";

						} else if (header.equals("good")) {
							HeadTitle = "GOOD RETURN  *" + ArabicTEXT.GoodReturn + "!";

						} else if (header.equals("promofree")) {

							HeadTitle = "PROMOTION FREE  *" + ArabicTEXT.PromotionFree + "!";

						} else if (header.equals("buyback")) {

							HeadTitle = "BUYBACK FREE  *" + ArabicTEXT.BuybackFree + "!";

						}
					}

					outStream.write(BoldOn);

					outStream.write(UnderlineOn);

					outStream.write(NewLine);
					outStream.write("       ".getBytes());
					printlines2(HeadTitle, 1, object, 1, args[0], 1, 1);
					outStream.write(NewLine);
					outStream.write(UnderlineOff);
					outStream.write(BoldOff);

				}
				int MAXLEngth = 137;

				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}
				boolean isoutlet = false;
				String strheader = "", strHeaderBottom = "", strTotal = "";
				String barcodeVal = "";

				for (int j = 0; j < headers.length(); j++) {

					String HeaderVal = "";

					HeaderVal = ArabicTEXT.getHeaderVal(headers.getString(j));
					if (j == 4 && object.getString("printbarcode").equals("1")) {
						HeaderVal = barcode;
					}
					strheader = strheader + getAccurateText(
							(j == 4 && object.getString("printbarcode").equals("0")) ? ""
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(HeaderVal.toString()) + MAXLEngth, hashPositions.get(HeaderVal.toString()));

					strHeaderBottom = strHeaderBottom
							+ getAccurateText(
									(j == 4 && object.getString("printbarcode").equals("0")) ? ""
											: (HeaderVal.indexOf(" ") == -1) ? ""
													: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length())
															.trim(),
									hashValues.get(HeaderVal.toString()) + MAXLEngth,
									hashPositions.get(HeaderVal.toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("DESCRIPTION") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);

					// if (!object.getString("LANG").equals("en")) {
					/*
					 * if (strHeaderBottom.length() > 0) {
					 * 
					 * printlines2(ArabicTEXT.headerDotmatbottomrevereArab, 1,
					 * object, 1, args[0], 1, 1); }
					 * printlines2(ArabicTEXT.headerDotmatrevereseArabic.trim(),
					 * 1, object, 1, args[0], 1, 1);
					 */
					// } else {

					printlines2(strheader, 1, object, 1, args[0], 1, 1);
					if (strHeaderBottom.length() > 0) {
						printlines2(strHeaderBottom, 1, object, 1, args[0], 1, 1);
					}
					// }

					outStream.write(CompressOff);

					outStream.write(CompressOn);
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);
				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";
					boolean isoutletdata = false;
					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 0) {
							itemDescrion = (l + 1) + "";

						} else if (m == 4) {
							itemDescrion = "";
							if (object.getString("printbarcode").equals("1")) {
								itemDescrion = jArr.getString(m);
							} else {

								try {
									itemDescrion = "*" + jArr.getString(m) + "!";
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}

						strData = strData + getAccurateText(itemDescrion,
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

					}

					// lp.writeLine(strData);
					outStream.write(CompressOn);
					/* count++; */
					printlines2(strData.trim(), 1, object, 1, args[0], 1, 1);

					outStream.write(CompressOff);
				}
				if (jInnerData.length() > 0) {
					outStream.write(CompressOn);
					printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
					printlines2(strTotal, 1, object, 1, args[0], 1, 1);
					outStream.write(CompressOff);

				}

			}

			if (!object.getString("printlanguageflag").equals("2")) {
				outStream.write(NewLine);
				outStream.write(BoldOn);

				printlines2(
						(getAccurateText("TOTAL ORDER AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
								+ getAccurateText(object.getString("SUB TOTAL"), 12, 0) + getAccurateText(" : ", 3, 0)
								+ "*" + getAccurateText(ArabicTEXT.SubTotal, 15, 2) + "!"),
						1, object, 1, args[0], 1, 1);
				outStream.write(BoldOff);
				outStream.write(BoldOn);
				if (object.has("ORDER DISCOUNT") && object.getString("ORDER DISCOUNT").toString().length() > 0) {
					double invoice = Double.parseDouble(object.getString("ORDER DISCOUNT"));

					if (invoice != 0) {

						printlines2(
								(getAccurateText("ORDER DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("ORDER DISCOUNT"), 12, 0)
										+ getAccurateText(" : ", 3, 0) + "*"
										+ getAccurateText(ArabicTEXT.OrderDiscount, 15, 2) + "!"),
								1, object, 1, args[0], 1, 1);
					}
				}

				outStream.write(BoldOff);
				outStream.write(BoldOn);
				printlines2(
						(getAccurateText("NET AMOUNT", 20, 0) + getAccurateText(" : ", 3, 0)
								+ getAccurateText(object.getString("NET AMOUNT"), 12, 0) + getAccurateText(" : ", 3, 0)
								+ "*" + getAccurateText(ArabicTEXT.NetAmount, 15, 2) + "!"),
						1, object, 1, args[0], 1, 1);
				outStream.write(BoldOff);
			} else {
				outStream.write(NewLine);
				outStream.write(BoldOn);

				printlines2((getAccurateText("TOTAL ORDER AMOUNT", 65, 2) + getAccurateText(" : ", 3, 0)
						+ getAccurateText(object.getString("SUB TOTAL"), 12, 2)), 1, object, 1, args[0], 1, 1);
				outStream.write(BoldOff);
				outStream.write(BoldOn);
				if (object.has("ORDER DISCOUNT") && object.getString("ORDER DISCOUNT").toString().length() > 0) {
					double invoice = Double.parseDouble(object.getString("ORDER DISCOUNT"));

					if (invoice != 0) {

						printlines2(
								(getAccurateText("ORDER DISCOUNT", 65, 2) + getAccurateText(" : ", 3, 0)
										+ getAccurateText(object.getString("ORDER DISCOUNT"), 12, 2)),
								1, object, 1, args[0], 1, 1);
					}
				}

				outStream.write(BoldOff);
				outStream.write(BoldOn);
				printlines2((getAccurateText("NET AMOUNT", 65, 2) + getAccurateText(" : ", 3, 0)
						+ getAccurateText(object.getString("NET AMOUNT"), 12, 2)), 1, object, 1, args[0], 1, 1);
				outStream.write(BoldOff);

			}
			/*
			 * if (object.has("TCALLOWED") &&
			 * object.getString("TCALLOWED").toString().trim().length() > 0 &&
			 * object.getString("TCALLOWED").equals("1")) {
			 * 
			 * // printlines2(getAccurateText("TC CHARGED: //
			 * "+object.getString("TC //
			 * CHARGED"),80,1),1,object,1,args[0],1,1); printlines2(
			 * (getAccurateText("TC CHARGED", 20, 0) + getAccurateText(" : ", 3,
			 * 0) + getAccurateText(object.getString("TCCHARGED"), 12, 0) +
			 * getAccurateText(" : ", 3, 0) + "*" +
			 * getAccurateText(ArabicTEXT.TCcharged, 15, 2) + "!"), 1, object,
			 * 1, args[0], 1, 1);
			 * 
			 * } else { printlines2("", 2, object, 1, args[0], 1, 1); }
			 */

			if (object.has("PaymentType") && Integer.parseInt(object.getString("PaymentType")) < 2) {
				// position = position + 60;
				outStream.write(BoldOn);
				if (!object.getString("LANG").equals("en")) {

					printlines2(getAccurateText("*" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args[0], 1,
							1);
				} else {
					printlines2(getAccurateText("PAYMENT DETAILS", 80, 1), 2, object, 1, args[0], 1, 1);
				}

				outStream.write(BoldOff);
				// lp.newLine(2);

				JSONArray jCheques = object.has("Cheque") ? object.getJSONArray("Cheque") : null;
				JSONObject jCash = object.has("Cash") ? object.getJSONObject("Cash") : null;
				int paymenttype = Integer.parseInt(object.getString("ptype"));

				switch (paymenttype) {
				case 0:

					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					outStream.write(BoldOff);
					break;
				case 1:
					outStream.write(BoldOn);
					// lp.write("CHEQUE");
					printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					// lp.newLine(2);
					printlines2(
							(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0)
									+ getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Bank"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 20, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					printlines2("", 1, object, 1, args[0], 1, 1);
					// lp.newLine(2);
					break;
				case 2:
					outStream.write(BoldOn);
					if (!object.getString("LANG").equals("en")) {

						printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1),
								2, object, 1, args[0], 1, 1);
					} else {
						printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, object, 1, args[0],
								1, 1);
					}

					printlines2("", 1, object, 1, args[0], 1, 1);
					printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);
					// lp.newLine(2);
					printlines2(
							(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0)
									+ getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2)),
							1, object, 1, args[0], 1, 1);
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					for (int j = 0; j < jCheques.length(); j++) {
						JSONObject jChequeDetails = jCheques.getJSONObject(j);
						printlines2(
								(getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Bank"), 20, 0)
										+ getAccurateText(jChequeDetails.getString("Amount"), 20, 2)),
								1, object, 1, args[0], 1, 1);

					}
					// lp.writeLine(printSeprator());
					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

					printlines2("", 1, object, 1, args[0], 1, 1);
					outStream.write(BoldOff);

					break;
				default:
					break;
				}
			}
			if (object.getString("comments").toString().length() > 0) {
				if (object.getString("LANG").equals("en")) {
					printlines2("Comments:" + object.getString("comments"), 2, object, 1, args[0], 1, 1);
				} else {
					printlines2("Comments:*" + object.getString("comments") + "!", 2, object, 1, args[0], 1, 1);

				}
			}
			if (object.has("lpoNumber") && object.getString("lpoNumber").toString().length() > 0) {

				printlines2("LPO Number:" + object.getString("lpoNumber"), 2, object, 1, args[0], 1, 1);

			}
			if (object.getString("invtrailormsg").toString().length() > 0) {
				printlines2(object.getString("invtrailormsg"), 2, object, 1, args[0], 1, 1);

			}
			Log.e("CountNOW", "" + count);
			printlines2("", 5, object, 1, args[0], 1, 1);
			printlines2(getAccurateText("CUSTOMER_____________*" + ArabicTEXT.Customer + "!    SALESMAN_______________*"
					+ ArabicTEXT.Salesman + "!", 80, 1), 2, object, 1, args[0], 1, 1);

			String copyStatus = "";
			if (object.getString("printstatus").equals("DUPLICATE COPY")) {

				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DuplicateCopy + "",
						80, 1) + "!";
			} else if (object.getString("printstatus").equals("ORIGINAL COPY")) {
				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.OriginalCopy + "!",
						80, 1);

			} else {
				copyStatus = getAccurateText(object.getString("printstatus") + "  *" + ArabicTEXT.DraftCopy + "!", 80,
						1);

			}

			printlines2(copyStatus, 2, object, 2, args[0], 1, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void parseReturnSummarysResponse(final JSONObject object, final String... args) {
		StringBuffer s1 = new StringBuffer();
		try {
			hashValues = new HashMap<String, Integer>();
			hashValues.put("Item Code", 10);
			hashValues.put("Description", 25);
			hashValues.put("ARBDESCRIPTION", 25);
			hashValues.put("Quantity", 10);
			hashValues.put("Expiry Date", 10);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Item Code", 0);
			hashPositions.put("Description", 0);
			hashPositions.put("ARBDESCRIPTION", 0);
			hashPositions.put("Quantity", 1);
			hashPositions.put("Expiry Date", 1);

			line(startln);
			headerprint(object, 9);

			JSONArray jData = object.getJSONArray("data");
			for (int i = 0; i < jData.length(); i++) {

				JSONObject mainJson = jData.getJSONObject(i);
				JSONArray jInnerData = mainJson.getJSONArray("DATA");
				JSONArray headers = mainJson.getJSONArray("HEADERS");
				JSONObject jTotal = mainJson.getJSONObject("TOTAL");
				if (jInnerData.length() > 0) {

					String header = mainJson.getString("TITLE").trim();
					String HeadTitle = "";
					if (header.equals("damage")) {
						HeadTitle = "DAMAGE SUMMARY :";

					} else if (header.equals("return")) {

						HeadTitle = "RETURN SUMMARY :";

					}
					outStream.write(BoldOn);

					outStream.write(UnderlineOn);

					outStream.write(NewLine);

					printlines2(HeadTitle, 1, object, 1, args[0], 1, 1);
					outStream.write(NewLine);
					outStream.write(UnderlineOff);
					outStream.write(BoldOff);

				}
				int MAXLEngth = 80;

				for (int k = 0; k < headers.length(); k++) {

					MAXLEngth = MAXLEngth - hashValues.get(headers.getString(k).toString());

				}
				if (MAXLEngth > 0) {
					MAXLEngth = (int) MAXLEngth / headers.length();
				}

				String strheader = "", strHeaderBottom = "", strTotal = "";
				for (int j = 0; j < headers.length(); j++) {

					String HeaderVal = "";

					HeaderVal = headers.getString(j);

					strheader = strheader + getAccurateText(
							j == 2 ? ""
									: (HeaderVal.indexOf(" ") == -1) ? HeaderVal
											: HeaderVal.substring(0, HeaderVal.indexOf(" ")),
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));

					strHeaderBottom = strHeaderBottom + getAccurateText(
							(HeaderVal.indexOf(" ") == -1) ? ""
									: HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length()).trim(),
							hashValues.get(headers.getString(j).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(j).toString()));

					if (jTotal.has(headers.getString(j))) {
						strTotal = strTotal + getAccurateText(jTotal.getString(headers.getString(j).toString()),
								hashValues.get(headers.getString(j).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(j).toString()));
					} else {

						strTotal = strTotal + getAccurateText(headers.getString(j).equals("Description") ? "TOTAL" : "",
								hashValues.get(headers.getString(j)) + MAXLEngth, 1);
					}

				}
				if (jInnerData.length() > 0) {

					printlines2(strheader, 1, object, 1, args[0], 1, 1);

					if (strHeaderBottom.length() > 0) {

						printlines2(strHeaderBottom, 1, object, 1, args[0], 1, 1);

					}
					// }

					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);

				}

				for (int l = 0; l < jInnerData.length(); l++) {
					JSONArray jArr = jInnerData.getJSONArray(l);
					String strData = "";

					for (int m = 0; m < jArr.length(); m++) {

						String itemDescrion = jArr.getString(m);
						if (m == 2) {
							try {
								itemDescrion = "*" + jArr.getString(m) + "!";
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						strData = strData + getAccurateText(itemDescrion,
								hashValues.get(headers.getString(m).toString()) + MAXLEngth,
								hashPositions.get(headers.getString(m).toString()));

					}
					printlines2(strData, 1, object, 1, args[0], 1, 1);
					// lp.writeLine(strData);

				}
				if (jInnerData.length() > 0) {

					printlines2(printSeprator(), 1, object, 1, args[0], 1, 1);
					printlines2(strTotal, 1, object, 1, args[0], 1, 1);

				}

			}

			printlines2("", 1, object, 1, args[0], 1, 1);
			printlines2("", 1, object, 1, args[0], 1, 1);

			printlines2(getAccurateText("             SALESMAN_______________*" + ArabicTEXT.Salesman + "!", 80, 1), 2,
					object, 2, args[0], 2, 2);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// return String.valueOf(s1);
	}

	private void parseDiscountReport(final JSONObject object, final String... args) {
		try {

			hashValues = new HashMap<String, Integer>();
			hashValues.put("Invoice Number", 17);
			hashValues.put("Customer Code", 45);
			hashValues.put("Customer Name", 25);
			hashValues.put("Invoice Amount", 25);
			hashValues.put("Discount Amount", 25);

			hashPositions = new HashMap<String, Integer>();
			hashPositions.put("Invoice Number", 1);
			hashPositions.put("Customer Code", 1);
			hashPositions.put("Customer Name", 2);
			hashPositions.put("Invoice Amount", 2);
			hashPositions.put("Discount Amount", 2);

			line(startln);
			headervanstockprint(object, 26);

			JSONArray headers = object.getJSONArray("HEADERS");
			String strheader = "", strHeaderBottom = "";

			int MAXLEngth = 137;
			for (int i = 0; i < headers.length(); i++) {

				MAXLEngth = MAXLEngth - hashValues.get(headers.getString(i).toString());
			}
			if (MAXLEngth > 0) {
				MAXLEngth = (int) MAXLEngth / headers.length();
			}

			for (int i = 0; i < headers.length(); i++) {

				try {
					strheader = strheader + getAccurateText(
							(headers.getString(i).indexOf(" ") == -1) ? headers.getString(i)
									: headers.getString(i).substring(0, headers.getString(i).indexOf(" ")),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));

					strHeaderBottom = strHeaderBottom + getAccurateText(
							(headers.getString(i).indexOf(" ") == -1) ? ""
									: headers.getString(i).substring(headers.getString(i).indexOf(" "),
											headers.getString(i).length()),
							hashValues.get(headers.getString(i).toString()) + MAXLEngth,
							hashPositions.get(headers.getString(i).toString()));

				} catch (Exception e) {

				}
			}
			outStream.write(CompressOn);
			printlines1(strheader, 1, object, 1, args[0], 10);
			printlines1(strHeaderBottom, 1, object, 1, args[0], 10);
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);

			JSONArray jData = object.getJSONArray("data");

			for (int i = 0; i < jData.length(); i++) {
				JSONArray jArr = jData.getJSONArray(i);
				String strData = "";
				for (int j = 0; j < jArr.length(); j++) {

					String itemDescrion = jArr.getString(j);

					strData = strData
							+ getAccurateText(itemDescrion, hashValues.get(headers.getString(j).toString()) + MAXLEngth,
									hashPositions.get(headers.getString(j).toString()));

				}

				printlines1(strData, 1, object, 1, args[0], 10);

			}
			printlines2(printSepratorcomp(), 1, object, 1, args[0], 1, 1);
			outStream.write(CompressOff);
			outStream.write(BoldOn);

			outStream.write(BoldOff);
			outStream.write(NewLine);
			outStream.write(NewLine);
			outStream.write(NewLine);
			printlines2(getAccurateText("SALESMAN_______________*" + ArabicTEXT.Salesman + "!", 80, 1), 2, object, 2,
					args[0], 1, 10);
			outStream.write(NewLine);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * ArabicNormalizer class
	 * @author Ibrabel
	 */
	public final class ArabicNormalizer {

	    private String input;
	    private final String output;

	    /**
	     * ArabicNormalizer constructor
	     * @param input String
	     */
	    public ArabicNormalizer(String input){
	        this.input=input;
	        this.output=normalize();
	    }

	    /**
	     * normalize Method
	     * @return String
	     */
	    private String normalize(){

	        //Remove honorific sign
	        input=input.replaceAll("\u0610", "");//ARABIC SIGN SALLALLAHOU ALAYHE WA SALLAM
	        input=input.replaceAll("\u0611", "");//ARABIC SIGN ALAYHE ASSALLAM
	        input=input.replaceAll("\u0612", "");//ARABIC SIGN RAHMATULLAH ALAYHE
	        input=input.replaceAll("\u0613", "");//ARABIC SIGN RADI ALLAHOU ANHU
	        input=input.replaceAll("\u0614", "");//ARABIC SIGN TAKHALLUS

	        //Remove koranic anotation
	        input=input.replaceAll("\u0615", "");//ARABIC SMALL HIGH TAH
	        input=input.replaceAll("\u0616", "");//ARABIC SMALL HIGH LIGATURE ALEF WITH LAM WITH YEH
	        input=input.replaceAll("\u0617", "");//ARABIC SMALL HIGH ZAIN
	        input=input.replaceAll("\u0618", "");//ARABIC SMALL FATHA
	        input=input.replaceAll("\u0619", "");//ARABIC SMALL DAMMA
	        input=input.replaceAll("\u061A", "");//ARABIC SMALL KASRA
	        input=input.replaceAll("\u06D6", "");//ARABIC SMALL HIGH LIGATURE SAD WITH LAM WITH ALEF MAKSURA
	        input=input.replaceAll("\u06D7", "");//ARABIC SMALL HIGH LIGATURE QAF WITH LAM WITH ALEF MAKSURA
	        input=input.replaceAll("\u06D8", "");//ARABIC SMALL HIGH MEEM INITIAL FORM
	        input=input.replaceAll("\u06D9", "");//ARABIC SMALL HIGH LAM ALEF
	        input=input.replaceAll("\u06DA", "");//ARABIC SMALL HIGH JEEM
	        input=input.replaceAll("\u06DB", "");//ARABIC SMALL HIGH THREE DOTS
	        input=input.replaceAll("\u06DC", "");//ARABIC SMALL HIGH SEEN
	        input=input.replaceAll("\u06DD", "");//ARABIC END OF AYAH
	        input=input.replaceAll("\u06DE", "");//ARABIC START OF RUB EL HIZB
	        input=input.replaceAll("\u06DF", "");//ARABIC SMALL HIGH ROUNDED ZERO
	        input=input.replaceAll("\u06E0", "");//ARABIC SMALL HIGH UPRIGHT RECTANGULAR ZERO
	        input=input.replaceAll("\u06E1", "");//ARABIC SMALL HIGH DOTLESS HEAD OF KHAH
	        input=input.replaceAll("\u06E2", "");//ARABIC SMALL HIGH MEEM ISOLATED FORM
	        input=input.replaceAll("\u06E3", "");//ARABIC SMALL LOW SEEN
	        input=input.replaceAll("\u06E4", "");//ARABIC SMALL HIGH MADDA
	        input=input.replaceAll("\u06E5", "");//ARABIC SMALL WAW
	        input=input.replaceAll("\u06E6", "");//ARABIC SMALL YEH
	        input=input.replaceAll("\u06E7", "");//ARABIC SMALL HIGH YEH
	        input=input.replaceAll("\u06E8", "");//ARABIC SMALL HIGH NOON
	        input=input.replaceAll("\u06E9", "");//ARABIC PLACE OF SAJDAH
	        input=input.replaceAll("\u06EA", "");//ARABIC EMPTY CENTRE LOW STOP
	        input=input.replaceAll("\u06EB", "");//ARABIC EMPTY CENTRE HIGH STOP
	        input=input.replaceAll("\u06EC", "");//ARABIC ROUNDED HIGH STOP WITH FILLED CENTRE
	        input=input.replaceAll("\u06ED", "");//ARABIC SMALL LOW MEEM

	        //Remove tatweel
	        input=input.replaceAll("\u0640", "");

	        //Remove tashkeel
	        input=input.replaceAll("\u064B", "");//ARABIC FATHATAN
	        input=input.replaceAll("\u064C", "");//ARABIC DAMMATAN
	        input=input.replaceAll("\u064D", "");//ARABIC KASRATAN
	        input=input.replaceAll("\u064E", "");//ARABIC FATHA
	        input=input.replaceAll("\u064F", "");//ARABIC DAMMA
	        input=input.replaceAll("\u0650", "");//ARABIC KASRA
	        input=input.replaceAll("\u0651", "");//ARABIC SHADDA
	        input=input.replaceAll("\u0652", "");//ARABIC SUKUN
	        input=input.replaceAll("\u0653", "");//ARABIC MADDAH ABOVE
	        input=input.replaceAll("\u0654", "");//ARABIC HAMZA ABOVE
	        input=input.replaceAll("\u0655", "");//ARABIC HAMZA BELOW
	        input=input.replaceAll("\u0656", "");//ARABIC SUBSCRIPT ALEF
	        input=input.replaceAll("\u0657", "");//ARABIC INVERTED DAMMA
	        input=input.replaceAll("\u0658", "");//ARABIC MARK NOON GHUNNA
	        input=input.replaceAll("\u0659", "");//ARABIC ZWARAKAY
	        input=input.replaceAll("\u065A", "");//ARABIC VOWEL SIGN SMALL V ABOVE
	        input=input.replaceAll("\u065B", "");//ARABIC VOWEL SIGN INVERTED SMALL V ABOVE
	        input=input.replaceAll("\u065C", "");//ARABIC VOWEL SIGN DOT BELOW
	        input=input.replaceAll("\u065D", "");//ARABIC REVERSED DAMMA
	        input=input.replaceAll("\u065E", "");//ARABIC FATHA WITH TWO DOTS
	        input=input.replaceAll("\u065F", "");//ARABIC WAVY HAMZA BELOW
	        input=input.replaceAll("\u0670", "");//ARABIC LETTER SUPERSCRIPT ALEF

	        //Replace Waw Hamza Above by Waw
	        input=input.replaceAll("\u0624", "\u0648");

	        //Replace Ta Marbuta by Ha
	        input=input.replaceAll("\u0629", "\u0647");

	        //Replace Ya
	        // and Ya Hamza Above by Alif Maksura
	        input=input.replaceAll("\u064A", "\u0649");
	        input=input.replaceAll("\u0626", "\u0649");

	        // Replace Alifs with Hamza Above/Below
	        // and with Madda Above by Alif
	        input=input.replaceAll("\u0622", "\u0627");
	        input=input.replaceAll("\u0623", "\u0627");
	        input=input.replaceAll("\u0625", "\u0627");

	        return input;
	    }

	    /**
	     * @return the output
	     */
	    public String getOutput() {
	        return output;
	    }

	   
	}
	

}
