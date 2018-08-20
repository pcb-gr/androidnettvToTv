package com.jeff.app.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ADBShellHelper {

	private static ADBShellHelper ins = new ADBShellHelper();
	
	public static ADBShellHelper singleton() {
		return ins;
	}
	
	public  AdbShell getNewShell() throws Exception {
		return new AdbShell();
	}
	
	public class AdbShell {

		private Process p;
		
		AdbShell() throws IOException {
			 p = Runtime.getRuntime().exec("adb shell");
		}
		
		public void waitFor(int timeout) throws Exception {
			p.wait(timeout);
		}
		
		public void close() throws Exception {
			p.destroy();
		}
		
		public void executeCommand(String command) throws Exception {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			writer.write(command);
			writer.newLine();
			writer.flush();
		}
		
		public BufferedReader getLog() throws Exception {
			executeCommand("logcat -c;logcat | grep -E \"ActivityManager|OPEN FILE\"");
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			return reader;
		}
	}
	
}
