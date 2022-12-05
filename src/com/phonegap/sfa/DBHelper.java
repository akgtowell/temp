package com.phonegap.sfa;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
	// -----For upload file
	int serverResponseCode = 0;
	ProgressDialog dialog = null;

	private static final String ALTER_ROUTE_ADDFORCESETTLMENT = 
		    "ALTER TABLE routemaster ADD forcesettlementdays INTEGER";
	private static final String ALTER_ROUTE_ALLOWMSL = 
		    "ALTER TABLE routemaster ADD allowmslloadrequest INTEGER";
	private static final String ALTER_ROUTE_ROUTECREDITLIMIT = 
		    "ALTER TABLE routemaster ADD routecreditlimit VARCHAR";
	private static final String ALTER_ROUTE_ROUTECREDITDAYS = 
		    "ALTER TABLE routemaster ADD routecreditlimitdays VARCHAR";
	private static final String ALTER_ROUTE_ROUTECREDITCHECK = 
		    "ALTER TABLE routemaster ADD routecreditcheck INTEGER";
	private static final String ALTER_ROUTE_ADDUPDATEGPS = 
		    "ALTER TABLE routemaster ADD updategps INTEGER";
	private static final String ALTER_ROUTE_ENFORCEGPS = 
		    "ALTER TABLE routemaster ADD enforcegps VARCHAR";
	private static final String ALTER_ROUTE_ENABLEGPS = 
		    "ALTER TABLE routemaster ADD enablegps VARCHAR";
	
	
	// String upLoadServerUri = null;
	private Logger l1 = new Logger();
	// ********** File Path *************/
	final String uploadFilePath = "/mnt/sdcard/download/";


	String upLoadServerUri = "http://shahifoods.co:8003/sfa/upload/upload.php?id=1";
	

	// ------End
	
	// used to define db version
	private final static int DATABASE_VERSION = 74; // 73
	
	private static String DB_NAME = "sfa";
	// used to store current context
	private Context context;
	// define object of SQLiteDatabase
	private String DB_PATH = "";

	// SQLLite instance
	private SQLiteDatabase db = null;

	// Variables which defines the table name
	// private String TABLE_CARD = "Card";

	private String TABLE_VEHICLE = "tbl_vehicle";
//	private String CREATE_INVOICE_TABELE ="CREATE TABLE invoicedetail(roundsalesamount	Float(18,4),"
//			+"diffround	Float(18,4),"
//			+"amount	FLOAT(19,4),"
//			+"returnpromovalue	FLOAT(19,4),"
//			+"returnpromoamount	FLOAT(19,4),"
//			+"mdat	datetime,"
//			+"istemp	bool,"
//			+"routekey	FLOAT(28,0),"
//			+"visitkey	FLOAT(28,0),"
//			+"transactionkey	FLOAT(28,0),"
//			+"itemcode	FLOAT(18,0),"
//			+"salesqty	FLOAT(18,0),"
//			+"returnqty	FLOAT(18,0),"
//			+"damagedqty	FLOAT(18,0),"
//			+"freesampleqty	FLOAT(18,0),"
//			+"salesprice	FLOAT(18,6),"
//			+"returnprice	FLOAT(18,6),"
//			+"stdsalesprice	FLOAT(18,6),"
//			+"stdreturnprice	FLOAT(18,6),"
//			+"promoqty	FLOAT(18,0),"
//			+"salesitemexcisetax	FLOAT(19,4),"
//			+"salesitemgsttax	FLOAT(19,4),"
//			+"returnitemexcisetax	FLOAT(19,4),"
//			+"returnitemgsttax	FLOAT(19,4),"
//			+"damageditemexcisetax	FLOAT(19,4),"
//			+"damageditemgsttax	FLOAT(19,4),"
//			+"fgitemexcisetax	FLOAT(19,4),"
//			+"fgitemgsttax	FLOAT(19,4),"
//			+"promoitemexcisetax	FLOAT(19,4),"
//			+"promoitemgsttax	FLOAT(19,4),"
//			+"coopid	CHAR(10),"
//			+"batchdetailkey	FLOAT(18,0),"
//			+"salescaseprice	FLOAT(18,6),"
//			+"returncaseprice	FLOAT(18,6),"
//			+"stdsalescaseprice	FLOAT(18,6),"
//			+"stdreturncaseprice	FLOAT(18,6),"
//			+"goodreturnprice	FLOAT(18,6),"
//			+"goodreturncaseprice	FLOAT(18,6),"
//			+"stdgoodreturncaseprice	FLOAT(18,6),"
//			+"stdgoodreturnprice	FLOAT(18,6),"
//			+"expiryqty	FLOAT(18,0),"
//			+"currencycode	FLOAT(18,0),"
//			+"returnfreeqty	FLOAT(18,0),"
//			+"manualfreeqty	FLOAT(18,0),"
//			+"limitedfreeqty	FLOAT(18,0),"
//			+"rebaterentqty	FLOAT(18,0),"
//			+"fixedrentqty	FLOAT(18,0),"
//			+"pricechgindicator	FLOAT(18,0),"
//			+"discountamount	FLOAT(19,4),"
//			+"discountpercentage	FLOAT(18,6),"
//			+"promoamount	FLOAT(19,4),"
//			+"replacementqty	FLOAT(18,0),"
//			+"replacementprice	FLOAT(18,6),"
//			+"replacementcaseprice	FLOAT(18,6),"
//			+"promovalue	FLOAT(19,4),"
//			+"issync	INTEGER,"
//			+"displayitem	INTEGER,  unique(routekey,visitkey,transactionkey,itemcode));";
//			 
//			private String DROP_INVOICE_TABLE="DROP TABLE invoicedetail";
	/*
	 * private String CREATE_TABLE_MILEAGE = "CREATE TABLE tbl_mileagedata(" +
	 * "fld_srno INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
	 * "fld_vid INTEGER," + "fld_cost NUMBER," + "fld_totalGallon NUMBER," +
	 * "fld_tank NUMBER," + "fld_odoreading NUMBER," + "fld_note TEXT," +
	 * "fld_average NUMBER," + "fld_date DATETIME," + "fld_totalCost NUMBER," +
	 * "fld_odoSetup NUMBER," + "fld_lat DOUBLE," + "fld_lon DOUBLE," +
	 * "fld_address TEXT," + "fld_favorite NUMBER," + "fld_mileage_date DATE," +
	 * "fld_vunit NUMBER, fld_liter NUMBER,fld_currency TEXT," +
	 * "fld_lifetimemileage TEXT, fld_tmp_odoreading NUMBER)";
	 */

	
	
	public DBHelper(Context context) {

		super(context, DB_NAME, null, DATABASE_VERSION);

		this.context = context;
		Log.d("package name", "" + context.getPackageName());
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		open();
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion,final int newVersion) {
		String[] Tbls = {"customermaster", "itemmaster", "startingloaddetail",
				"itemgroup", "startendday", "routesequence",
				"discountkeydetail", "discountkeyheader", "salescalender",
				"pricingdetail1", "promoplandetail", "promoplanheader",
				"promokeydetail", "promokeyheader",
				"promotionassignmentadvanced", "productgroupdetail",
				"productgroupheader", "bankmaster", "customerposlimit",
				"customerposinventory", "POSmaster", "posinstructions",
				"nonservreasons", "expreasons", "expiryreturnreasons",
				"retitmreasons", "freegoodreasons", "voidreasons",
				"customersurveydefinition", "customersurveykey",
				"customersurveyplan", "customersurveydefassign",
				"customersurveykeyplan", "lookupindexdetail",
				"customerpricing1", "vanmaster", "salesmanmessages",
				"itempackagemaster", "cashdesc", "inventorylocation",
				"distributionkeydetails", "suggestedsalesinvoice",
				"inventorytransactiondetail", "customer_foc_balance",
				"customer_foc_detail", "journeyplancreditlimit",
				"invoicedetail", "invoiceheader", "invoicerxddetail",
				"promotiondetail", "customerinvoice", "salesorderdetail",
				"salesorderheader", "batchexpirydetail", "arheader",
				"ardetail", "cashcheckdetail", "inventorytransactionheader",
				"inventorysummarydetail", "nonservicedcustomer",
				"surveyauditdetail", "posequipmentchangedetail",
				"sigcapturedata", "customeroperationscontrol",
				"routesequencecustomerstatus", "customerinventorydetail",
				"routegoal", "nosalesheader", "batchmaster", "batchmaster",
				"batchmaster_temp", "transactiondetailtemp" };
		
			
		Log.d("version",""+ newVersion);
		Log.e("on Upgrade Called", "On Upgrad Called");
		
		/*
		try {
			
			existsColumnInTable(db,"routemaster");
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		if(oldVersion < newVersion) {
			// Execute Db Queries Here
			/*for (int i = 0; i < Tbls.length; i++) {
				Log.e("Plugin", "DELETE FROM "+Tbls[i]);
				db.execSQL("DELETE FROM "+Tbls[i]);
				
			}*/
			if(context.deleteDatabase(DB_NAME))
		        Log.i(DB_NAME, "Database Deleted....");
		    else
		        Log.i(DB_NAME, "Database Not Deleted..");
		}
		
		
	}
	private void existsColumnInTable(SQLiteDatabase inDatabase, String inTable) {
	    Cursor mCursor = null;
	    try {
	        // Query 1 row 
	        mCursor = inDatabase.rawQuery("SELECT * FROM " + inTable + " LIMIT 0", null);
	        		
	        // getColumnIndex() gives us the index (0 to ...) of the column - otherwise we get a -1
	        if (mCursor.getColumnIndex("forcesettlementdays") == -1)
	        	db.execSQL(ALTER_ROUTE_ADDFORCESETTLMENT);
	        if (mCursor.getColumnIndex("allowmslloadrequest") == -1)
	        	db.execSQL(ALTER_ROUTE_ALLOWMSL);
	        if (mCursor.getColumnIndex("routecreditlimit") == -1)
	        	db.execSQL(ALTER_ROUTE_ROUTECREDITLIMIT);
	        if (mCursor.getColumnIndex("routecreditlimitdays") == -1)
	        	db.execSQL(ALTER_ROUTE_ROUTECREDITDAYS);
	        if (mCursor.getColumnIndex("routecreditcheck") == -1)
	        	db.execSQL(ALTER_ROUTE_ROUTECREDITCHECK);
	        if (mCursor.getColumnIndex("enablegps") == -1)
	        	db.execSQL(ALTER_ROUTE_ENABLEGPS);
	        if (mCursor.getColumnIndex("updategps") == -1)
	        	db.execSQL(ALTER_ROUTE_ADDUPDATEGPS);
	        if (mCursor.getColumnIndex("enforcegps") == -1)
	        	db.execSQL(ALTER_ROUTE_ENFORCEGPS);

	    } catch (Exception Exp) {
	        // Something went wrong. Missing the database? The table?
	        Log.d("... - existsColumnInTable", "When checking whether a column exists in the table, an error occurred: " + Exp.getMessage());
	       
	    } finally {
	        if (mCursor != null) mCursor.close();
	    }
	}
	/**
	 * This function is used to check whether file is exist or not
	 */
	private boolean isDataBaseExist() {

		File dbFile = new File(DB_PATH + DB_NAME);

		return dbFile.exists();
	}

	/**
	 * This function is use to copy database from one file(from assets) to
	 * another(application)
	 * 
	 * @throws IOException
	 */

	private void copyDataBase() throws IOException {

		try {
			// Open your local db as the input stream
			InputStream myInput = context.getAssets().open(
					"databases/" + DB_NAME + ".mp3");

			// Path to the just created empty db
			String outFileName = DB_PATH + DB_NAME;

			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {

			Log.e("Error in copy DB", e.toString());

		}

	}

	public void copy2SD(String routecode) {
		try {
			Log.e("Error in copy DB", routecode);
			String uploadFileName = routecode + "_" + new Date().getTime()
					+ ".mp3";
			Log.e("DB", "DATABASE COPIED");
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				// String currentDBPath =
				// "/data/data/com.phonegap.sfa/databases/sfa.mp3";
				// String backupDBPath = "sfa";
				// File currentDB = new File(data, currentDBPath);
				// File backupDB = new File(sd, backupDBPath);
				// --
				// File backupDB = new
				// File(Environment.getExternalStorageDirectory(),
				// uploadFileName); // for example "my_data_backup.db"
				File backupDB = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
						uploadFileName);
				File currentDB = context.getDatabasePath("sfa"); // databaseName=your
																	// current
																	// application
																	// database
																	// name, for
																	// example
																	// "my_data.db"
				// --
				File root = new File(Environment.getExternalStorageDirectory()
						+ "/sfa/");
				File gpsfile = new File(root, "sfa_log.txt");

				Log.e("DB", "DATABASE COPIED");
				if (currentDB.exists()) {
					// ---
					FileInputStream fis = new FileInputStream(currentDB);
					FileOutputStream fos = new FileOutputStream(backupDB);
					fos.getChannel().transferFrom(fis.getChannel(), 0,
							fis.getChannel().size());
					fis.close();
					fos.close();
					// Uploading db on sever
					uploadFile(false,uploadFilePath + "" + uploadFileName);
					//uploadFile(true,gpsfile.getAbsolutePath());
					Log.e("DB", "DATABASE COPIED to SDCARD");
				} else {
					Log.e("DB", "DATABASE not found ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------Upload Start
	// -----------
	public int uploadFile(boolean isDelete,String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			

			Log.e("uploadFile", "Source File not exist :" + uploadFilePath + ""
					+ fileName);
			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				// dos.writeBytes("Content-Disposition: form-data; name="+
				// +";filename="+ fileName + "" + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="
						+ fileName + "" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		
				
				
				/*------ TO get response from server -------- sujee added */
				InputStream responseStream = new 
					    BufferedInputStream(conn.getInputStream());
				BufferedReader responseStreamReader = 
					    new BufferedReader(new InputStreamReader(responseStream));
				
				String line = "";
				StringBuilder stringBuilder = new StringBuilder();
				while ((line = responseStreamReader.readLine()) != null) {
				    stringBuilder.append(line).append("\n");
				}
				responseStreamReader.close();

				String response = stringBuilder.toString();
				
				Log.i("response ", "response Response is : "
						+ response + ": " + response);
				
				
				/*--------------------------END---------------------------------*/ 
				
				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);
				
				Log.i("serverResponseMessage ", "serverResponseMessage Response is : "
						+ serverResponseMessage + ": " + serverResponseMessage);
				

				if (serverResponseCode == 200) {

					Log.e("File Uploaded", "File Upload Complete.");
					
				}
				
				

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				// dialog.dismiss();
				ex.printStackTrace();
				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				// dialog.dismiss();
				e.printStackTrace();

				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}finally
			{
				if (serverResponseCode == 200 && isDelete) {

					Log.e("File Uploaded", "File Upload Complete.");
					boolean deleted = sourceFile.delete();

					Log.e("source file deleted ","" + deleted);
				}
			

			}
			// dialog.dismiss();
			return serverResponseCode;

		} // End else block
	}

	// -----------
	// -----------End
	public DBHelper open() throws SQLException {
		try {

			Log.e("open", "Open called");
			// check that db is exist or not
			boolean isExist = isDataBaseExist();

			if (isExist == false) {

				db = getWritableDatabase();
				// copy db from assets
				copyDataBase();
				Log.d("inCreate Database", "Creat Database");
				/*
				 * db.openOrCreateDatabase(DB_PATH + DB_NAME, null);
				 * //UpgradeDatabase(); db.execSQL(CREATE_TABLE_VEHICLE);
				 */

				if (db.isOpen())
					db.close();
			}

			db = getWritableDatabase();

		} catch (Exception e) {
			Log.e("error in open db", "" + e.toString());
		}
		return this;
	}

	public void close() {
		db.close();
	}

	private boolean IsTableExists(String tableName) {
		Cursor c = null;
		try {
			String SQL = "SELECT count(*) FROM sqlite_master where name = '"
					+ tableName + "' and type='table'";
			c = db.rawQuery(SQL, null);
			int tableCount = 0;
			if (c != null) {
				c.moveToFirst();
				tableCount = c.getInt(0);
			}
			if (tableCount == 0)
				return false;
			else
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return true;
	}

	
	/*
	 * public boolean insertVehicle(VehicleData data) {
	 * 
	 * try {
	 * 
	 * ContentValues initialValue = new ContentValues();
	 * initialValue.put("fld_vname", data.getvName());
	 * initialValue.put("fld_vunits", data.getvUnit());
	 * initialValue.put("fld_voctane", data.getvOctane());
	 * initialValue.put("fld_vodometer", data.getvOdometer());
	 * initialValue.put("fld_vdate", data.getvDate());
	 * initialValue.put("fld_vcurrency", data.getvCurrency());
	 * 
	 * db.insert(TABLE_VEHICLE, null, initialValue);
	 * 
	 * return true;
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return false; }
	 */
	public synchronized void execInsertQuery(String sql) {
		try {
			db.execSQL(sql);
			Log.d("SQL", sql);
		} catch (Exception e) {
			Log.e("Err", e.getMessage());
		} finally {
			// closeDb();
		}
	}

	public synchronized JSONObject execSelectQuery(String sql) {

		Cursor cursor = null;
		String[][] SelData = null;
		String value = "";
		JSONObject j1 = new JSONObject();
		JSONArray jarr1 = new JSONArray();
		try {

			cursor = db.rawQuery(sql, null);

			Log.d("SQL", sql);
			Log.d("SQL", "" + cursor.getCount());
			Log.d("SQL", "" + cursor.getColumnCount());
			// SelData = new String[cursor.getCount()][cursor.getColumnCount()];
			if (cursor != null && cursor.getCount() > 0) {
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					JSONObject j11 = new JSONObject();
					for (int j = 0; j < cursor.getColumnCount(); j++) {
						// SelData[i][j] = cursor.getString(j);
						try {
							// Log.d("SQL", cursor.getColumnName(j));
							// Log.d("SQL", ""+cursor.getString(j));
							
							if (cursor.getString(j) == null)
								value = "0";
							else
								value = cursor.getString(j);
							
							j11.put(cursor.getColumnName(j), value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					jarr1.put(j11);
				}
				j1.put("array", jarr1);
			}
			Log.d("SQL", sql);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Err", e.getMessage());
		} finally {
			// closeDb();
			if (cursor != null)
				cursor.close();
		}

		return j1;
	}
	/*
	 * public synchronized JSONObject execSelectQuery(String sql) {
	 * 
	 * Cursor cursor = null; String[][] SelData = null;
	 * 
	 * JSONObject j1=new JSONObject(); JSONArray jarr1=new JSONArray(); try {
	 * 
	 * cursor = db.rawQuery(sql, null); JSONArray jarr2=new JSONArray();
	 * JSONObject j11=new JSONObject(); Log.d("count",""+cursor.getCount()); if
	 * (cursor != null && cursor.getCount() >= 0) { for (int i = 0; i <
	 * cursor.getCount(); i++) { cursor.moveToPosition(i);
	 * 
	 * for (int j = 0; j < cursor.getColumnCount(); j++) {
	 * 
	 * 
	 * j11.put(cursor.getColumnName(j), cursor.getString(j)); jarr2.put(j11);
	 * 
	 * } jarr1.put(jarr2); } j1.put("array",jarr1); } Log.d("SQL", sql); } catch
	 * (Exception e) { e.printStackTrace(); Log.e("Err", e.getMessage()); }
	 * finally { // closeDb(); if (cursor != null) cursor.close(); }
	 * 
	 * return j1; }
	 */

}

/*
 * public void deleteUpdateFavorateLocation(int SrNo) { try {
 * 
 * db.delete(TABLE_MILEAGE, "station_id="+station_id, null);
 * 
 * } catch (Exception e) { Log.e("Exception on query", e.toString()); } }
 */
/*
 * public ArrayList<CardData> getCardDetailImages(){ Cursor cursor = null;
 * ArrayList<CardData> lstCardDetail = new ArrayList<CardData>(); CardData data
 * = null; try{
 * 
 * cursor = db.rawQuery("SELECT * FROM \""+TABLE_CARD+
 * "\" WHERE LiveCardID>29 order by CardTitle", null);
 * 
 * if(cursor!=null && cursor.getCount()>0){ for(int
 * i=0;i<cursor.getCount();i++){ cursor.moveToPosition(i); data = new
 * CardData();
 * 
 * data.setUrl_of_Card_Icon(cursor.getString(4));
 * data.setCardImageIcon(getBitmapFromDB(data.getUrl_of_Card_Icon(),
 * TABLE_ImageTableIcon)); lstCardDetail.add(data); } } } catch(Exception e){
 * e.printStackTrace(); } finally{ if(cursor!=null) cursor.close(); } return
 * lstCardDetail; }
 */

/*
 * public ArrayList<CardData> getCardDetail(String table){
 * 
 * Cursor cursor = null; ArrayList<CardData> lstCardDetail = new
 * ArrayList<CardData>(); CardData data = null; try{ if(table.equals("0")){
 * cursor =
 * db.rawQuery("SELECT * FROM \""+TABLE_CARD+"\" order by LiveCardID LIMIT 0,6"
 * , null); }else{ cursor =
 * db.rawQuery("SELECT * FROM \""+TABLE_CARD+"\" order by CardTitle", null); }
 * 
 * if(cursor!=null && cursor.getCount()>0){ for(int
 * i=0;i<cursor.getCount();i++){
 * 
 * cursor.moveToPosition(i); data = new CardData();
 * 
 * data.setId(cursor.getString(0));
 * data.setUrl_of_Card_Big_Image(cursor.getString(1));
 * data.setCardDetail(ValueSelling.setServerString(cursor.getString(2)));
 * data.setUrl_of_Card_Icon(cursor.getString(4));
 * data.setCardTitle(ValueSelling.setServerString(cursor.getString(5)));
 * data.setCardImage(getBitmapFromDB(data.getUrl_of_Card_Big_Image(),
 * TABLE_ImageTableBigImage));
 * data.setCardImageIcon(getBitmapFromDB(data.getUrl_of_Card_Icon(),
 * TABLE_ImageTableIcon));
 * 
 * int categroyId = getCategeryIDFromCardID(data.getId());
 * 
 * if(categroyId != -1 ){ data.setCategroyID(String.valueOf(categroyId)); String
 * categaryName = getCategeryName(categroyId); if(categaryName != null &&
 * !categaryName.equals("")){ data.setCardCategory(categaryName); } }
 * lstCardDetail.add(data); }
 * 
 * } } catch (Exception e) { e.printStackTrace(); } finally{ if(cursor!=null)
 * cursor.close(); } return lstCardDetail; }
 * 
 * public CardData getCardDetail(int cardID){
 * 
 * Cursor cursor = null; CardData data = null; try{
 * 
 * cursor =
 * db.rawQuery("SELECT * FROM "+TABLE_CARD+" WHERE LiveCardID = "+cardID, null);
 * if(cursor!=null && cursor.getCount()>0){ for(int
 * i=0;i<cursor.getCount();i++){
 * 
 * cursor.moveToPosition(i); data = new CardData();
 * data.setId(cursor.getString(0));
 * data.setUrl_of_Card_Big_Image(cursor.getString(1));
 * data.setCardDetail(ValueSelling.setServerString(cursor.getString(2)));
 * data.setUrl_of_Card_Icon(cursor.getString(4));
 * data.setCardTitle(ValueSelling.setServerString(cursor.getString(5)));
 * data.setCardImage(getBitmapFromDB(data.getUrl_of_Card_Big_Image(),
 * TABLE_ImageTableBigImage));
 * data.setCardImageIcon(getBitmapFromDB(data.getUrl_of_Card_Icon(),
 * TABLE_ImageTableIcon));
 * 
 * int categroyId = getCategeryID(data.getCardCategory()); if(categroyId != -1){
 * data.setCategroyID(String.valueOf(categroyId)); String categaryName =
 * getCategeryName(categroyId); if(categaryName != null &&
 * !categaryName.equals("")){ data.setCardCategory(categaryName); } }
 * 
 * } } } catch (Exception e) { e.printStackTrace(); } finally{ if(cursor!=null)
 * cursor.close(); } return data; }
 */

/*
 * public int getLastID(){ int id=0; Cursor cursor = null; try{ cursor =
 * db.rawQuery
 * ("SELECT CardID FROM "+TABLE_CARD+" order by CardID desc limit 0,1", null);
 * if(cursor!=null && cursor.getCount()>0){ for(int
 * i=0;i<cursor.getCount();i++){ cursor.moveToPosition(i); id =
 * cursor.getInt(0);
 * 
 * } } } catch(Exception e){ Log.e("getLastID", e.toString()); } finally{
 * if(cursor!=null) cursor.close(); } return id; };
 */

/*
 * public String[] getCardDetailfromCategroyID(int cagegaryID){ Cursor cursor =
 * null; String[] ClientID=null;
 * 
 * try{
 * 
 * cursor = db.rawQuery("SELECT CardId,CategoryId FROM "+TABLE_CARD_CATEGROY+
 * " WHERE CategoryId ="+cagegaryID , null); if(cursor!=null &&
 * cursor.getCount()>0){
 * 
 * ClientID = new String[cursor.getCount()];
 * 
 * for(int i=0;i<cursor.getCount();i++){
 * 
 * cursor.moveToPosition(i); ClientID[i] = cursor.getString(0); } } } catch
 * (Exception e) { Log.e("Error in getCardData", e.toString());
 * e.printStackTrace(); }finally{
 * 
 * if(cursor != null && !cursor.isClosed()){ cursor.close(); } } return
 * ClientID; }
 */

/*
 * public ArrayList<CardSetData> getCardCategeryList(){ Cursor cursor = null;
 * ArrayList<CardSetData> lstCardCategory = new ArrayList<CardSetData>();
 * CardSetData data = null; try{
 * 
 * cursor = db.rawQuery("SELECT * FROM \""+TABLE_CATEGORY+"\"" , null);
 * if(cursor!=null && cursor.getCount()>0){ for(int
 * i=0;i<cursor.getCount();i++){
 * 
 * cursor.moveToPosition(i); data = new CardSetData();
 * 
 * data.setId(cursor.getString(0));
 * data.setCategory_name(ValueSelling.setServerString(cursor.getString(1)));
 * data.setImageCode(-999);
 * 
 * lstCardCategory.add(data); } } } catch (Exception e) {
 * Log.e("Error in getCardData", e.toString()); e.printStackTrace(); }finally{
 * 
 * if(cursor != null && !cursor.isClosed()){ cursor.close(); } } return
 * lstCardCategory; }
 */

/*
 * public boolean insertCard(CardData data){
 * 
 * try{ ContentValues initialValue = new ContentValues();
 * initialValue.put("CardBigImage", data.getUrl_of_Card_Big_Image());
 * initialValue.put("CardDetail", data.getCardDetail());
 * initialValue.put("CardID", data.getId()); initialValue.put("CardIcon",
 * data.getUrl_of_Card_Icon()); initialValue.put("CardTitle",
 * data.getCardTitle());
 * 
 * db.insert(TABLE_CARD, null, initialValue);
 * 
 * insertCategroy(data.getCardCategory()); int categroyId =
 * getCategeryID(data.getCardCategory());
 * 
 * if(categroyId != -1){ int cardID = getCardID(data.getId(),
 * data.getCardTitle()); if(cardID>0) insertCardCategroy(cardID, categroyId); }
 * 
 * 
 * insertImageInBigImageTable(data.getUrl_of_Card_Big_Image(),
 * ValueSelling.getimage(data.getUrl_of_Card_Big_Image()));
 * insertImageInIconImageTable(data.getUrl_of_Card_Icon(),
 * ValueSelling.getimage(data.getUrl_of_Card_Icon()));
 * 
 * return true;
 * 
 * } catch (Exception e) { e.printStackTrace(); } return false; } public boolean
 * insertCardCategroy(int cardId, int categroyId){
 * 
 * try{ if(!checkCardCategroyExist(cardId,categroyId)){ ContentValues
 * initialValue = new ContentValues(); initialValue.put("CardId", cardId);
 * initialValue.put("CategoryId", categroyId); db.insert(TABLE_CARD_CATEGROY,
 * null, initialValue); } return true; } catch(Exception e){
 * Log.e("InsertCardCategroy", e.toString()); } return false;
 * 
 * }
 * 
 * public boolean insertCategroy(String category){
 * 
 * try{
 * 
 * if(!checkCategroyExist(category)){ ContentValues contentValues = new
 * ContentValues(); contentValues.put("CategoryName", category);
 * db.insert(TABLE_CATEGORY, null, contentValues); return true; }
 * 
 * } catch(Exception e){ Log.e("InsertCategroy", e.toString()); } return false;
 * 
 * }
 * 
 * 
 * public void insertImageInIconImageTable(String URL,Bitmap image){
 * 
 * try { ContentValues contentValues = new ContentValues(); byte[] imageBytes =
 * getByteImage(image); contentValues.put("image_url", URL);
 * contentValues.put("image_bitmap", imageBytes); long i =
 * db.insert(TABLE_ImageTableIcon, null, contentValues);
 * Log.i("Image inserted in Images_table", "Image Inserted at position =>"+i); }
 * catch (Exception e) { e.printStackTrace(); } } public void
 * insertImageInBigImageTable(String URL,Bitmap image){
 * 
 * try { ContentValues contentValues = new ContentValues(); byte[] imageBytes =
 * getByteImage(image); contentValues.put("image_url", URL);
 * contentValues.put("image_bitmap", imageBytes); long i =
 * db.insert(TABLE_ImageTableBigImage, null, contentValues);
 * Log.i("Image inserted in Images_table", "Image Inserted at position =>"+i); }
 * catch (Exception e) { e.printStackTrace(); } }
 * 
 * private byte[] getByteImage(Bitmap image) { byte[] byteImage = null; Bitmap
 * bitmapImage = image; ByteArrayOutputStream baos = new
 * ByteArrayOutputStream(); bitmapImage.compress(Bitmap.CompressFormat.PNG, 100,
 * baos); byteImage = baos.toByteArray(); return byteImage; }
 * 
 * public boolean deleteCardFromCard(int CardID, int CategoryID){
 * 
 * Cursor cursor = null;
 * 
 * try{
 * 
 * cursor = db.rawQuery("DELETE FROM \""+TABLE_CARD_CATEGROY+"\" WHERE CardId="
 * +CardID+ " AND CategoryId="+CategoryID , null); if(cursor!=null &&
 * cursor.getCount()>0){
 * 
 * return true; }
 * 
 * } catch (Exception e) { Log.e("Error in getCardData", e.toString());
 * e.printStackTrace(); }finally{
 * 
 * if(cursor != null && !cursor.isClosed()){ cursor.close(); } } return false; }
 * public boolean deleteCardCategory(int CategoryID){
 * 
 * Cursor cursor = null;
 * 
 * try{
 * 
 * cursor =
 * db.rawQuery("DELETE FROM \""+TABLE_CARD_CATEGROY+"\" WHERE CategoryId="
 * +CategoryID , null); if(cursor!=null && cursor.getCount()>0){
 * 
 * return true; }
 * 
 * } catch (Exception e) { Log.e("Error in getCardData", e.toString());
 * e.printStackTrace(); }finally{
 * 
 * if(cursor != null && !cursor.isClosed()){ cursor.close(); } } return false; }
 * public boolean deleteCategory(int CategoryID){
 * 
 * Cursor cursor = null;
 * 
 * try{
 * 
 * cursor = db.rawQuery("DELETE FROM \""+TABLE_CATEGORY+"\" WHERE CategoryId="
 * +CategoryID, null); if(cursor!=null && cursor.getCount()>0){
 * 
 * return true; }
 * 
 * } catch (Exception e) { Log.e("Error in getCardData", e.toString());
 * e.printStackTrace(); }finally{
 * 
 * if(cursor != null && !cursor.isClosed()){ cursor.close(); } } return false; }
 * 
 * public Bitmap getBitmapFromDB(String url,String table){
 * 
 * Bitmap bitmap = null; Cursor c = null; boolean tableBigImageFlag = false;
 * if(table.equals(TABLE_ImageTableBigImage)){ tableBigImageFlag = true; }else{
 * tableBigImageFlag = false; }
 * 
 * try { c = db.rawQuery("SELECT * FROM "+table+" WHERE image_url=\""+url+"\"",
 * null);
 * 
 * if(c != null && c.getCount()>0){ c.moveToFirst(); byte[] byteImage =
 * c.getBlob(1);
 * 
 * if(byteImage != null){
 * 
 * ByteArrayInputStream imageStream = new ByteArrayInputStream(byteImage);
 * 
 * BitmapFactory.Options options=new BitmapFactory.Options();
 * options.inSampleSize = 2; options.inPurgeable = true; bitmap
 * =BitmapFactory.decodeStream(imageStream, null, options); byteImage = null;
 * 
 * }else{ if(ConstantData.isConnected){ if(tableBigImageFlag)
 * insertImageInBigImageTable(url, bitmap); else
 * insertImageInIconImageTable(url, bitmap); }else{ bitmap =
 * BitmapFactory.decodeResource(context.getResources(), R.drawable.icon); } }
 * }else{ if(ConstantData.isConnected){ bitmap = ValueSelling.getimage(url);
 * if(tableBigImageFlag) insertImageInBigImageTable(url, bitmap); else
 * insertImageInIconImageTable(url, bitmap);
 * 
 * }else{ bitmap = BitmapFactory.decodeResource(context.getResources(),
 * R.drawable.icon); } }
 * 
 * } catch (Exception e) { Log.e("Error in getImageFromDB", e.toString());
 * return bitmap; }finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } }
 * 
 * return bitmap; }
 * 
 * public String getCategeryName(int categeryID){ String categroyName = "";
 * Cursor c = null; try{
 * 
 * c = db.rawQuery("SELECT CategoryId,CategoryName FROM "+TABLE_CATEGORY+
 * " WHERE CategoryId="+categeryID, null); if(c != null && c.getCount()>0){
 * c.moveToFirst(); categroyName = c.getString(1); } } catch(Exception e){
 * Log.e("Error", e.toString()); e.printStackTrace(); } finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } } return categroyName; }
 * 
 * public int getCategeryIDFromCardID(String cardID){ int categroyID = -1;
 * Cursor c = null; try{ c =
 * db.rawQuery("SELECT * FROM "+TABLE_CARD_CATEGROY+" WHERE CardId="+cardID,
 * null); if(c != null && c.getCount()>0){ c.moveToFirst(); categroyID =
 * c.getInt(1); } } catch(Exception e){ Log.e("Error", e.toString());
 * e.printStackTrace(); } finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } } return categroyID; }
 * 
 * public int getCardID(String CardID,String CardTitle){
 * 
 * int idCard = Integer.parseInt(CardID); int cardID = -1; Cursor c = null; try{
 * c = db.rawQuery("SELECT * FROM "+TABLE_CARD+" WHERE CardID ="+
 * idCard+" AND CardTitle=\""+CardTitle+"\"", null); if(c != null &&
 * c.getCount()>0){ c.moveToFirst(); cardID = c.getInt(0); } } catch(Exception
 * e){ Log.e("Error", e.toString()); e.printStackTrace(); } finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } } return cardID; }
 * 
 * public int getCategeryID(String categoryName){ int categroyID = -1; Cursor c
 * = null; try{ c =
 * db.rawQuery("SELECT * FROM "+TABLE_CATEGORY+" WHERE CategoryName=\""
 * +categoryName+"\"", null); if(c != null && c.getCount()>0){ c.moveToFirst();
 * categroyID = c.getInt(0); } } catch(Exception e){ Log.e("Error",
 * e.toString()); e.printStackTrace(); } finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } } return categroyID; }
 * 
 * public boolean checkCardExist(int cardID){ Cursor c = null; try{ c =
 * db.rawQuery
 * ("SELECT DISTINCT * FROM "+TABLE_CARD+" WHERE LiveCardID="+cardID, null);
 * if(c != null && c.getCount()>0){ return true; } } catch(Exception e){
 * Log.e("Error", e.toString()); e.printStackTrace(); } finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } } return false; }
 * 
 * public boolean checkCategroyExist(String categroyName){ Cursor c = null; try{
 * c =
 * db.rawQuery("SELECT DISTINCT * FROM "+TABLE_CATEGORY+" WHERE CategoryName=\""
 * +categroyName+"\"", null); if(c != null && c.getCount()>0){ return true; } }
 * catch(Exception e){ Log.e("Error", e.toString()); e.printStackTrace(); }
 * finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } } return false; } public boolean
 * checkCardCategroyExist(int cardID,int categoryID){ Cursor c = null; try{ c =
 * db.rawQuery("SELECT DISTINCT * FROM "+TABLE_CARD_CATEGROY+" WHERE CardId="
 * +cardID+" AND CategoryId="+categoryID, null); if(c != null &&
 * c.getCount()>0){ return true; } } catch(Exception e){ Log.e("Error",
 * e.toString()); e.printStackTrace(); } finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } } return false; }
 * 
 * public boolean checkIconBitmapDB(String url){
 * 
 * Cursor c = null;
 * 
 * try { c = db.rawQuery("SELECT DISTINCT * FROM "+TABLE_ImageTableIcon+
 * " WHERE image_url=\""+url+"\"", null);
 * 
 * if(c != null && c.getCount()>0){
 * 
 * return true; }
 * 
 * } catch (Exception e) { Log.e("Error in checkImageFromDB", e.toString());
 * return false; }finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } }
 * 
 * return false; }
 * 
 * public boolean checkBigBitmapDB(String url){
 * 
 * Cursor c = null;
 * 
 * try { c = db.rawQuery("SELECT DISTINCT * FROM "+TABLE_ImageTableBigImage+
 * " WHERE image_url=\""+url+"\"", null);
 * 
 * if(c != null && c.getCount()>0){
 * 
 * return true; }
 * 
 * } catch (Exception e) { Log.e("Error in checkImageFromDB", e.toString());
 * return false; }finally{
 * 
 * if(c != null && !c.isClosed()){ c.close(); } }
 * 
 * return false; }
 * 
 * public boolean deleteAllCard(){
 * 
 * try{
 * 
 * db.delete(TABLE_CARD, null, null); return true;
 * 
 * } catch (Exception e) { e.printStackTrace(); } return false; }
 * 
 * public boolean deleteAllCardCategory(){
 * 
 * try{
 * 
 * db.delete(TABLE_CARD_CATEGROY, null, null); return true;
 * 
 * } catch (Exception e) { e.printStackTrace(); } return false; } public boolean
 * deleteAllCategory(){
 * 
 * try{
 * 
 * db.delete(TABLE_CARD, null, null); return true;
 * 
 * } catch (Exception e) { e.printStackTrace(); } return false; }
 */

