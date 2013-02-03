package com.flyingh.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
	public int index;
	public int block;
	public File file;
	public URL url;

	public DownloadThread(int index, int block, File file, URL url) {
		super();
		this.index = index;
		this.block = block;
		this.file = file;
		this.url = url;
	}

	@Override
	public void run() {
		int start = index * block;
		int end = (index + 1) * block - 1;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
			InputStream is = conn.getInputStream();
			int len = -1;
			byte[] buf = new byte[1024];
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
			accessFile.seek(start);
			while ((len = is.read(buf)) != -1) {
				accessFile.write(buf, 0, len);
			}
			accessFile.close();
			System.out.println("Thread " + (index + 1) + " download over!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}