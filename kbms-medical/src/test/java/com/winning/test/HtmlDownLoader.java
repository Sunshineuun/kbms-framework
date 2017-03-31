package com.winning.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlDownLoader {
	
	public static final String URL_STRING = "http://yp.120ask.com/manual/";
	public static final String FLODER = "H:/work/120ask/";

	public static void main(String[] args) {
		HtmlDownLoader.DownLoaderRunner runner01 = new HtmlDownLoader().new DownLoaderRunner(1,1,10000);
		HtmlDownLoader.DownLoaderRunner runner02 = new HtmlDownLoader().new DownLoaderRunner(10001,2,20000);
		HtmlDownLoader.DownLoaderRunner runner03 = new HtmlDownLoader().new DownLoaderRunner(20001,3,30000);
		HtmlDownLoader.DownLoaderRunner runner04 = new HtmlDownLoader().new DownLoaderRunner(30001,4,40000);
		HtmlDownLoader.DownLoaderRunner runner05 = new HtmlDownLoader().new DownLoaderRunner(40001,5,50000);
		HtmlDownLoader.DownLoaderRunner runner06 = new HtmlDownLoader().new DownLoaderRunner(50001,6,60000);
		HtmlDownLoader.DownLoaderRunner runner07 = new HtmlDownLoader().new DownLoaderRunner(60001,7,70000);
		new Thread(runner01).start();
		new Thread(runner02).start();
		new Thread(runner03).start();
		new Thread(runner04).start();
		new Thread(runner05).start();
		new Thread(runner06).start();
		new Thread(runner07).start();

	}

	class DownLoaderRunner implements Runnable {

		private int bIdx;
		private int fIdx;
		private int maxIdx;

		public DownLoaderRunner() {

		}

		public DownLoaderRunner(int bIdx, int fIdx, int maxIdx) {
			this.bIdx = bIdx;
			this.fIdx = fIdx;
			this.maxIdx = maxIdx;
		}

		@Override
		public void run() {
			createFolder(fIdx);
			FileWriter fw = null;
			while(true){
				String htmlContent = HttpContentGeter.getHtmlContent(URL_STRING+bIdx+"/");
				if(null!=htmlContent){
					try {
						fw = new FileWriter(new File(
								FLODER + fIdx + "/" + bIdx + ".htm"));
						fw.write(htmlContent);
						fw.flush();
						fw.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				bIdx++;
				if(bIdx >= maxIdx)
					break;
				if (bIdx % 10000 == 0)
				{
					fIdx++;
					createFolder(fIdx);
				}
				
			}
		}
		
		public  void createFolder(int j){
			File folder = new File(FLODER + j);
			if (!folder.exists())
				folder.mkdirs();
		}

	}
}
