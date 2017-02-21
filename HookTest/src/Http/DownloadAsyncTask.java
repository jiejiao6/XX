package Http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.greenrobot.eventbus.EventBus;

import com.donson.config.Logger;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ProgressBar;

public class DownloadAsyncTask extends AsyncTask<String, Integer, String> {
	private static final int CONNECTTIME = 40*1000;
	private static final String SUCCEED = "SUCCEED";
	String mUrl;
	String mPath;
	public DownloadAsyncTask(String url,String path) {
		mUrl = url;
		mPath = path;
	}
	@Override
	protected String doInBackground(String... params) {
		if(TextUtils.isEmpty(mUrl)){
			EventBus.getDefault().post(new ProgressEvent(-1,-1));
			return "the url is null";
		}
		String backup = params[0];
		File backUp = null;
		if(!TextUtils.isEmpty(backup)){
			backUp = new File(backup);
			File dir = backUp.getParentFile();
			if(!dir.exists()) dir.mkdirs();
			if(!backUp.exists())
				try {
					backUp.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		URL url;
		InputStream is = null;
		 OutputStream output = null;
		 OutputStream backOs = null;
		 int total = 0;
		try {
			File file = new File(mPath);
			File dir = file.getParentFile();
			if(!dir.exists()){
				dir.mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			url = new URL(mUrl);
			Logger.i("DOWNLOAD","DOWNLOAD-----------mUrl--:"+mUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(CONNECTTIME);
			connection.setReadTimeout(CONNECTTIME);
			connection.setRequestMethod("GET");
			Logger.i("DOWNLOAD","DOWNLOAD-----------CODE--:"+connection.getResponseCode());
			if(connection.getResponseCode()==200){
				total = connection.getContentLength(); 
				is = connection.getInputStream();  
				output = new FileOutputStream(file);
				
				int tmp = -1;
				int value = 0;
				byte[] buff = new byte[1024];
				if(TextUtils.isEmpty(backup)){
					while ((tmp = is.read(buff))!=-1) {
						output.write(buff, 0, tmp);
						value+=tmp;
						publishProgress(value,total);
					}
				}else {
					backOs = new FileOutputStream(backUp);
					while ((tmp = is.read(buff))!=-1) {
						if (isCancelled()) {
		                    is.close();
		                    // mHandler.sendEmptyMessageDelayed(DownloadDialog.MSG_SET_COMPLETE,
		                    // 1000);
		                    EventBus.getDefault().post(new ProgressEvent(-1,-1));
		                    return "取消下载";
		                }
						output.write(buff, 0, tmp);
						backOs.write(buff,0,tmp);
						value+=tmp;
						publishProgress(value,total);
					}
				}
				output.flush();
				if(backOs!=null)
					backOs.flush();
				return SUCCEED;
			}else {
				EventBus.getDefault().post(new ProgressEvent(-1,-1));
				return "response code is:"+connection.getResponseCode();
			}
		} catch (Exception e) {
			Logger.i("DOWNLOAD","DOWNLOAD----------e---:"+e);
			e.printStackTrace();
			EventBus.getDefault().post(new ProgressEvent(-1,-1));
			return e.toString();
		} finally {
			try {
				if(is!=null) is.close();
				if(output!=null) output.close();
			} catch (IOException e) {
				EventBus.getDefault().post(new ProgressEvent(-1,-1));
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(callback!=null){
			if(result.equals(SUCCEED)){
				callback.onSucceed();
			}else {
				callback.onFailure(result);
			}
		}
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		int value = values[0];
		int total = values[1];
		EventBus.getDefault().post(new ProgressEvent(value/1024, total/1024));
		Logger.i("DOWNLOAD","DOWNLOAD-----------value--:"+value+"  total:"+total+"  "+((float)value*100/total)/100);
		System.out.println("value:"+value+"  total:"+total+"  "+((float)value*100/total)/100);
	}
	public void setCallback(DownCallback callback) {
		this.callback = callback;
	}
	DownCallback callback = null;
	public interface DownCallback{
		void onSucceed();
		void onFailure(String err);
	}
	
}
