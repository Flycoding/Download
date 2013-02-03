package com.flyingh.download;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Download {
	public static void main(String[] args) throws MalformedURLException, IOException {
		download("http://10.1.79.23:8080/News/BC_Setup.exe", 5);
	}

	private static void download(String path, int totalThreadNumber) throws MalformedURLException, IOException {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() != 200) {
			System.out.println("error:" + conn.getResponseCode());
			return;
		}
		int len = conn.getContentLength();
		int block = len % totalThreadNumber == 0 ? len / totalThreadNumber : len / totalThreadNumber + 1;
		File file = new File(getFileName(path));
		RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
		accessFile.setLength(len);
		accessFile.close();
		for (int index = 0; index < totalThreadNumber; index++) {
			new DownloadThread(index, block, file, url).start();
		}
	}

	private static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
	}
}
