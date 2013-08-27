package com.appsot.zqt.ip;

import java.io.File;
import java.io.IOException;

import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feedback.NotificationType;
import com.feedback.UMFeedbackService;
import com.mobclick.android.MobclickAgent;
import com.mobclick.android.ReportPolicy;

/**
 * 
 * @author vFISHv
 * @email vfishv@gmail.com
 *
 */
public class IPActivity extends Activity
{

	public static final String QQWryFileName = "QQWry.DAT";
	private static IPActivity instance;
	private EditText ipEt;
	
	public static IPActivity getInstance() {
		return instance;
	}
	
	/*
	 * 在程序入口点使用static代码块初始化AdManager.init
	 */
	static
	{
		// 第一个参数为您的应用发布Id
		// 第二个参数为您的应用密码
		// 第三个参数是请求广告的间隔，有效的设置值为30至200，单位为秒
		// 第四个参数是设置测试模式，设置为true时，可以获取测试广告，正式发布请设置此参数为false
		// 注意:3.0版本开始AdManager.init方法的参数改为四个，去掉了2.2版中的"版本号"参数
		//AdManager.init("defb8281314cfb2d", "bb72c5cd52943031", 30, false);
	}

	private Handler handler;
	private File DBFile = null;
	public static final int MSG_A = 1;
	public static final int MSG_IP = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		AdManager.init(this,"defb8281314cfb2d", "bb72c5cd52943031", 30, false);
		setContentView(R.layout.main);
		
		LinearLayout adViewLayout = (LinearLayout) findViewById(R.id.adViewLayout);
		adViewLayout.addView(new AdView(this),
		new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
		LinearLayout.LayoutParams.WRAP_CONTENT));
		
		MobclickAgent.setDefaultReportPolicy(this, ReportPolicy.REALTIME);
		MobclickAgent.onError(this);
		UMFeedbackService.enableNewReplyNotification(this, NotificationType.AlertDialog);//NotificationType.NotificationBar
		MobclickAgent.update(this);
		
		ipEt = (EditText) findViewById(R.id.ip);

		ipEt.setText(Util.getLocalIpAddress());

		final EditText info = (EditText) findViewById(R.id.info);
		final TextView databaseDate = (TextView) findViewById(R.id.databaseDate);
		Button query = (Button) findViewById(R.id.query);
		Button feedbackBtn = (Button) findViewById(R.id.feedbackBtn);
		feedbackBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				UMFeedbackService.openUmengFeedbackSDK(IPActivity.this);
			}
		});
		
		query.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handler.sendEmptyMessage(MSG_A);
			}
		});

		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				if (msg.what == MSG_A)
				{
					// 测试IP 58.20.43.13
					String ipAdrress = "116.76.172.84";
					ipAdrress = ipEt.getText().toString();
					// 指定纯真数据库的文件名，所在文件夹

					try
					{
						if (DBFile != null && DBFile.exists() && isCanUseSdCard())
						{

						}
						else
						{
							File externalCache = getExternalCacheDir();// Since: API Level 8
							File cache = getCacheDir();
							File externalStorageDir = Environment.getExternalStorageDirectory();
							
							if (cache != null)
								System.out.println("CacheDir:" + cache.getAbsolutePath());
							if (externalCache != null)
								System.out.println("ExternalCache:" + externalCache.getAbsolutePath());
							if (externalStorageDir != null)
								System.out.println("ExternalStorageDir:" + externalStorageDir.getAbsolutePath());
							
							if (externalCache != null && externalCache.exists() && isCanUseSdCard())
							{
								cache = externalCache;
								//Toast.makeText(IPActivity.this, "Usein ExternalCacheDir", Toast.LENGTH_SHORT).show();
							}
							else
							{
								
							}
							
							// File Path = getDir("Data", 0);//"Data"
							DBFile = new File(cache, QQWryFileName);// Path, "QQWry.Dat"
						}
						
						if (!DBFile.exists()) // Need to copy...
						{
							// Need to copy...
							Util.CopyDatabase(IPActivity.this, DBFile);
						}

						// IPActivity.this.getResources().getAssets().open(QQWryFileName);
						// RandomAccessFile raf = new RandomAccessFile(QQWryFileName, "r");
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					IPSeeker ip = new IPSeeker(DBFile.getAbsolutePath(), "file:///android_asset");

					IPLocation location = ip.getIPLocation(ipAdrress);
					String result = location.getCountry() + "\n" + location.getArea();

					// System.out.println(result);
					info.setText(result);
					if (TextUtils.isEmpty(databaseDate.getText()))
					{
						location = ip.getIPLocation("255.255.255.0");
						result = location.getCountry() + ":" + location.getArea();
						databaseDate.setText(result);
					}
				}
				else if (msg.what == MSG_IP)
				{
					// 测试IP 58.20.43.13
					String ipAdrress = (String) msg.obj;
					System.out.println("ip>>" + ipAdrress);
					
					if(TextUtils.isEmpty(ipAdrress))
					{
						return;
					}
					
					ipEt.setText(ipAdrress);
					// 指定纯真数据库的文件名，所在文件夹

					try
					{
						if (DBFile != null && DBFile.exists() && isCanUseSdCard())
						{

						}
						else
						{
							File externalCache = getExternalCacheDir();// Since: API Level 8
							File cache = getCacheDir();
							File externalStorageDir = Environment.getExternalStorageDirectory();
							
							if (cache != null)
								System.out.println("CacheDir:" + cache.getAbsolutePath());
							if (externalCache != null)
								System.out.println("ExternalCache:" + externalCache.getAbsolutePath());
							if (externalStorageDir != null)
								System.out.println("ExternalStorageDir:" + externalStorageDir.getAbsolutePath());
							
							if (externalCache != null && externalCache.exists() && isCanUseSdCard())
							{
								cache = externalCache;
								//Toast.makeText(IPActivity.this, "Usein ExternalCacheDir", Toast.LENGTH_SHORT).show();
							}
							else
							{
								
							}
							
							// File Path = getDir("Data", 0);//"Data"
							DBFile = new File(cache, QQWryFileName);// Path, "QQWry.Dat"
						}
						
						if (!DBFile.exists()) // Need to copy...
						{
							// Need to copy...
							Util.CopyDatabase(IPActivity.this, DBFile);
						}

						// IPActivity.this.getResources().getAssets().open(QQWryFileName);
						// RandomAccessFile raf = new RandomAccessFile(QQWryFileName, "r");
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					IPSeeker ip = new IPSeeker(DBFile.getAbsolutePath(), "file:///android_asset");

					IPLocation location = ip.getIPLocation(ipAdrress);
					String result = location.getCountry() + "\n" + location.getArea();

					// System.out.println(result);
					info.setText(result);
					//if (TextUtils.isEmpty(databaseDate.getText()))
					{
						location = ip.getIPLocation("255.255.255.0");
						result = location.getCountry() + ":" + location.getArea();
						databaseDate.setText(result);
					}
				}
			}
		};

		handler.sendEmptyMessage(MSG_A);

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		DBFile.delete();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	// sdcard是否可读写
	public boolean isCanUseSdCard()
	{
		try
		{
			return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	// sim卡是否可读
	public boolean isCanUseSim()
	{
		try
		{
			TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}
	
	public void sendMessage(Message msg,long delayMillis) {
		handler.sendMessageDelayed(msg, delayMillis);
	}
	
	public void sendMessageDelayed(Message msg,long delayMillis) {
		handler.sendMessageDelayed(msg, delayMillis);
	}
	
}