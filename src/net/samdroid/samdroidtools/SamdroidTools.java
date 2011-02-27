package net.samdroid.samdroidtools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class SamdroidTools extends PreferenceActivity {
	SysCommand sysCmd = null;
    public static native int getFreeSpace(String s_path);

	public static String iniROOT = "net.samdroid.samdroidtools_preferences";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.samdroidtools);
        System.loadLibrary("samdroidtools");
        sysCmd = new SysCommand();
        
        this.findPreference(getString(R.string.hook_reboot))
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
	   			rebootWithConfirm(0);
				return false;
			}
        });
        
        this.findPreference(getString(R.string.hook_reboot_recovery))
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
	   			rebootWithConfirm(1);
				return false;
			}
        });

        this.findPreference(getString(R.string.module_ini_tun))
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				CreateAndSaveSamdroidtoolsSh();
				return false;
			}
        });

        this.findPreference(getString(R.string.module_ini_cifs))
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				CreateAndSaveSamdroidtoolsSh();
				return false;
			}
        });

        this.findPreference(getString(R.string.module_touch))
        .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			public boolean onPreferenceChange(Preference preference, Object newValue) {
				CreateAndSaveSamdroidtoolsSh();
				return true;
			}
        	/*
			public boolean onPreferenceClick(Preference preference) {
				CreateAndSaveSamdroidtoolsSh();
				Toast.makeText(getBaseContext(), "The changes will take effect only after rebooting the phone!", Toast.LENGTH_LONG).show();
				return false;
			}
			*/
        });
        
        this.findPreference(getString(R.string.apps2sd_ini_enable))
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				CreateAndSaveSamdroidtoolsSh();
				return false;
			}
        });

        this.findPreference(getString(R.string.apps2sd_ini_move_dalvik_cache))
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				CreateAndSaveSamdroidtoolsSh();
				return false;
			}
        });

        this.findPreference(getString(R.string.hook_backup_apps_to_sd))
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				BackupApps();
				return false;
			}
        });

        this.findPreference(getString(R.string.hook_restore_apps_from_sd))
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				RestoreApps();
				return false;
			}
        });
        
        showSize();
    }

    private void showSize() {
		if (getApplicationContext().getSharedPreferences(iniROOT,0).getBoolean(getString(R.string.apps2sd_ini_enable), false)) {
			this.findPreference(getString(R.string.hook_sd_size)).setSummary(getFreeSpace("/system/sd")/1024 + " KB free");
		}
		else {
			this.findPreference(getString(R.string.hook_sd_size)).setSummary("Enable apps2sd! for use SD");
		}
    	this.findPreference(getString(R.string.hook_int_size)).setSummary(getFreeSpace("/data")/1024 + " KB free");
    }
    
    private void rebootWithConfirm(final int type) {
   		if (getApplicationContext().getSharedPreferences(iniROOT,0).getBoolean(getString(R.string.reboot_ini_confirm), true)) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage(R.string.dlg_reboot_confirm)
	    	       .setCancelable(false)
	    	       .setPositiveButton(R.string.global_yes, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   sysCmd.reboot(type);
	    	           }
	    	       })
	    	       .setNegativeButton(R.string.global_no, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                dialog.cancel();
	    	           }
	    	       }).create();
	    	builder.show();
   		}
   		else 
   			sysCmd.reboot(type);
    }
    
    private void CreateAndSaveSamdroidtoolsSh() {
    	final ProgressDialog prDlg = ProgressDialog.show(this,      
                "Please wait...", "Switching...", true); 
        	new Thread() { 
        		public void run() { 
        			try{ 
        				doCreateAndSaveSamdroidtoolsSh();
        			} catch (Exception e) {  } 
        				// Dismiss the Dialog 
        				prDlg.dismiss(); 
        			} 
        		}.start();     	
    }
    
    private void doCreateAndSaveSamdroidtoolsSh() {
    	SharedPreferences set = getApplicationContext().getSharedPreferences(iniROOT,0);
    	try {
			FileWriter fstream = new FileWriter("/data/data/net.samdroid.samdroidtools/samdroidtools.sh");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("#!/system/bin/sh\n\n");
			out.write("# Autogenerated file by SamdroidTools (http://samdroid.net)\n");
			
			// Apps2SD
			out.write("\n#\n# Apps2sd settings...\n#\n");
			if (set.getBoolean(getString(R.string.apps2sd_ini_enable), false)) {
				out.write("setprop lk.apps2sd.enable 1\n");
				if (set.getBoolean(getString(R.string.apps2sd_ini_move_dalvik_cache), false)) {
					out.write("setprop lk.movedc.enable 1\n");
				}
			}			
			// Modules
			out.write("\n#\n# Load modules...\n#\n");
			
			// Multitouch
			String touchVer = set.getString("module_touch", "v2.4");
			if (!touchVer.equals("v2.4")) {
				out.write("rmmod qt5480\n");
				out.write("sleep 1\n");
				// load single touch
				if (touchVer.equals("ST")) {
					out.write("insmod /lib/modules/qt5480st.ko\n");
				}
				else if (touchVer.equals("v1.0")) {
					out.write("insmod /lib/modules/qt5480mt1.ko\n");
				}
				else if (touchVer.equals("UT")) {
					out.write("insmod /system/lib/modules/qt5480.ko\n");
				}
			}
			
			// tun.ko
			if (set.getBoolean(getString(R.string.module_ini_tun), false)) {
				out.write("/system/bin/insmod /lib/modules/tun.ko\n");
				sysCmd.cmd("/system/bin/insmod /lib/modules/tun.ko");
			}
			else {
				sysCmd.cmd("/system/bin/rmmod tun");
			}
			// cifs.ko
			if (set.getBoolean(getString(R.string.module_ini_cifs), false)) {
				out.write("/system/bin/insmod /lib/modules/cifs.ko\n");
				sysCmd.cmd("/system/bin/insmod /lib/modules/cifs.ko");
			}
			else {
				sysCmd.cmd("/system/bin/rmmod cifs");
			}
			
			out.close();
			fstream.close();
			
			// Copy to /data/samdroidtools.sh
			sysCmd.cmd("cat /data/data/net.samdroid.samdroidtools/samdroidtools.sh > /data/samdroidtools.sh");
			sysCmd.cmd("chmod 0755 /data/samdroidtools.sh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        showSize();
    }
    
    private void BackupApps() {
    	final ProgressDialog prDlg = ProgressDialog.show(this,      
            "Please wait...", "Backuping...", true); 
    	new Thread() { 
    		public void run() { 
    			try{ 
    				sysCmd.backup_user_apps();
    			} catch (Exception e) {  } 
    				// Dismiss the Dialog 
    				prDlg.dismiss(); 
    			} 
    		}.start();     	
    }

    private void RestoreApps() {
    	final ProgressDialog prDlg = ProgressDialog.show(this,      
            "Please wait...", "Restoring...", true); 
    	new Thread() { 
    		public void run() { 
    			try{ 
    				sysCmd.restore_user_apps();
    			} catch (Exception e) {  } 
    				// Dismiss the Dialog 
    				prDlg.dismiss(); 
    			} 
    		}.start();     	
    }
}
