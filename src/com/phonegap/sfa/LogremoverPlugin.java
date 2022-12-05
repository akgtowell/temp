package com.phonegap.sfa;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Date;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

public class LogremoverPlugin extends Plugin {
	private String callbackId = "";
	private JSONObject status;

	@Override
	public PluginResult execute(String request, JSONArray querystring, String callbackId) {
		// TODO Auto-generated method stub
		PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
		result.setKeepCallback(true);

		try {
			this.callbackId = callbackId;
			Log.e("Called", "Called Log Remover");
			status = new JSONObject();
			Log.d("Init called", "init");
			status.put("state", true);
			showProgressDialog();
			File dir_log = new File(Environment.getExternalStorageDirectory() + "/sfa/");
			File dir_db = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());

			this.ClearLog(dir_log, dir_db);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public void ClearLog(File dir, File dir_db) {

		if (dir != null) {

			File[] fileList = dir.listFiles();
			if (fileList != null && fileList.length > 0) {
				int i = 0;
				for (File file : fileList) {
					i++;
					if (file.exists()) {
						Calendar time = Calendar.getInstance();
						time.add(Calendar.DAY_OF_YEAR, -15);

						Date lastModified = new Date(file.lastModified());
						if (lastModified.before(time.getTime())) {
							file.delete();
						}
					}

				}
			}
			this.ClearDB(dir_db);

		} else {
			this.ClearDB(dir_db);

		}
	}

	public void ClearDB(File dir) {

		if (dir != null) {

			File[] fileList = dir.listFiles(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return name.endsWith(".mp3") || name.endsWith(".MP3");
				}
			});
			if (fileList != null && fileList.length > 0) {
				int i = 0;
				for (File file : fileList) {
					i++;
					if (file.exists()) {
						Calendar time = Calendar.getInstance();
						time.add(Calendar.DAY_OF_YEAR, -10);

						Date lastModified = new Date(file.lastModified());
						if (lastModified.before(time.getTime())) {
							file.delete();
						}
					}
					

				}
			} 

				dismissProgressDialog();
				sendUpdate(status, true);
			

		} else {
			dismissProgressDialog();
			sendUpdate(status, true);
		}
	}

	private MyProgressDialog progressDialog;

	private void showMessage(String msg) {
		new AlertDialog.Builder(cordova.getActivity()).setTitle("SFA").setMessage(msg)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// InstallAPK(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
						// + "/TEStapk.apk");
					}
				})

				.setIcon(android.R.drawable.ic_dialog_alert).show();

	}

	/**
	 * Display custom progress dialog
	 */
	public void showProgressDialog() {
		cordova.getActivity().runOnUiThread(new Runnable() {

			public void run() {
				progressDialog = new MyProgressDialog(cordova.getActivity());
			}
		});

	}

	public void dismissProgressDialog() {

		cordova.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				// Log.e("Progress **:", "Progress ******");
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
					progressDialog = null;

				}
			}
		});
	}

	private void sendUpdate(JSONObject obj, boolean keepCallback) {
		if (callbackId != null) {

			PluginResult result = new PluginResult(PluginResult.Status.OK, obj);
			success(result, this.callbackId);
		}
	}
}
