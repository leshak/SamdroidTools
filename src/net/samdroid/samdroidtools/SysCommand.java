package net.samdroid.samdroidtools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SysCommand {
    public static native int runSh();
	
    public void backup_user_apps() {
		FileWriter fstream;
		try {
			// create /data/data/net.samdroid.samdroidtools/samdroidcmd.sh
			fstream = new FileWriter("/data/data/net.samdroid.samdroidtools/samdroidcmd.sh");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("#!/system/bin/sh\n\n");
            out.write("if [ ! -d /sdcard/samdroidtools ]; then mkdir /sdcard/samdroidtools; fi\n");
            out.write("if [ ! -d /sdcard/samdroidtools/app ]; then mkdir /sdcard/samdroidtools/app; fi\n");
            out.write("if [ ! -d /sdcard/samdroidtools/app-prvate ]; then mkdir /sdcard/samdroidtools/app-private; fi\n");
            out.write("cp -f /data/app/* /sdcard/samdroidtools/app\n");
            out.write("cp -f /data/app-private/* /sdcard/samdroidtools/app-private\n");
			out.close();
			fstream.close();
			// run
			runSh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void restore_user_apps() {
		FileWriter fstream;
		try {
			// create /data/data/net.samdroid.samdroidtools/samdroidcmd.sh
			fstream = new FileWriter("/data/data/net.samdroid.samdroidtools/samdroidcmd.sh");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("#!/system/bin/sh\n\n");
            out.write("cp -f /sdcard/samdroidtools/app/* /data/app\n");
            out.write("cp -f /sdcard/samdroidtools/app-private/* /data/app-private\n");
			out.close();
			fstream.close();
			// run
			runSh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void cmd(String s_cmd) {
		FileWriter fstream;
		try {
			// create /data/data/net.samdroid.samdroidtools/samdroidcmd.sh
			fstream = new FileWriter("/data/data/net.samdroid.samdroidtools/samdroidcmd.sh");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("#!/system/bin/sh\n\n");
			out.write(s_cmd + "\n");
			out.close();
			fstream.close();
			// run
			runSh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reboot(int type) {
		FileWriter fstream;
		try {
			// create /data/data/net.samdroid.samdroidtools/samdroidcmd.sh
			fstream = new FileWriter("/data/data/net.samdroid.samdroidtools/samdroidcmd.sh");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("#!/system/bin/sh\n\n");
			out.write("sync\n");
			if (type == 0) out.write("reboot\n");
			else if (type == 1) out.write("reboot recovery\n");
			out.close();
			fstream.close();
			// run
			runSh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
