package com.jeff.app.channel;

import java.io.BufferedReader;

import org.springframework.stereotype.Service;

import com.jeff.app.helper.ADBShellHelper;
import com.jeff.app.helper.ADBShellHelper.AdbShell;

@Service
public class ChannelHandler {
	AdbShell shell = null;
	AdbShell shellLog = null;
	public String channelUrl = "";

	public ChannelHandler() {
		try {
			shell = ADBShellHelper.singleton().getNewShell();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doTasks(String channelName) throws Exception {
		BufferedReader logs = getLogFromEmulator();
		for (String logLine = logs.readLine(); logLine != null; logLine = logs.readLine()) {
			System.out.println(logLine);
			doStep(logLine, channelName);
		}

	}

	private BufferedReader getLogFromEmulator() throws Exception {
		shellLog = ADBShellHelper.singleton().getNewShell();
		return shellLog.getLog();
	}

	private void searchChannel(String channelName) throws Exception {
		System.out.println("begin do step : search channel");
		shell.executeCommand("input text \"" + channelName.replace(" ", "%s") + "\"");
		shell.executeCommand("input keyevent 66"); // send message to press "enter key"
		System.out.println("finished step : search channel");
	}

	private void openChannel() throws Exception {
		System.out.println("begin do step : finish search and click open channel");
		shell.executeCommand("input tap 66 283");
		System.out.println("finished step : open channel");
	}

	private void closeAds() throws Exception { // send message to close ads
		System.out.println("begin do step : send message to close ads");
		shell.executeCommand("input keyevent 4");
		System.out.println("finished step : close ads");
	}

	private void getChannelLink(String message) throws Exception {
		channelUrl = message.split("OPEN FILE")[1];
		System.out.println("Finished step : getChannelLink[" + channelUrl + "]");
	}

	private void backEvent() throws Exception {
		shell.executeCommand("input keyevent 4");
		System.out.println("Finished step : backEvent");
	}

	private void backspaceEvent() throws Exception {
		shell.executeCommand("input keyevent 67");
	}

	private void clearInputSearch() throws Exception {
		shell.executeCommand("input tap 518 120");
		System.out.println("Finished :clearInputSearch");
	}

	private void backToSearchScreen() throws Exception {
		Thread.sleep(2000);
		System.out.println("Back to search screen");
		backEvent();
		Thread.sleep(2000);
		backEvent();
		Thread.sleep(2000);

	}

	private void doStep(String logLine, String channelName) throws Exception {
		if (logLine.indexOf("logcat -c;logcat | grep -E ") != -1) {
			clearInputSearch();
			searchChannel(channelName);
			openChannel();
		} else if (logLine.indexOf(
				"Displayed com.streams.androidnettv/com.startapp.android.publish.adsCommon.activities.OverlayActivity") != -1) {
			closeAds();
		} else if (logLine.indexOf(".m3u8") != -1) {
			getChannelLink(logLine);
			backToSearchScreen();
			closeSessionChannel();
		}
	}

	private void closeSessionChannel() throws Exception {
		shell.close();
		shellLog.close();
	}
}
