package com.yarin.android.Examples_04_15;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity01 extends Activity {
	// 声明ImageView对象
	ImageView imageview;
	TextView textview;
	// ImageView的alpha值，
	int image_alpha = 255;

	Handler mHandler;// = new Handler();
	// 控件线程
	boolean isrung = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		isrung = true;

		// 获得ImageView的对象
		imageview = (ImageView) this.findViewById(R.id.ImageView01);
		textview = (TextView) this.findViewById(R.id.TextView01);

		// 设置imageview的图片资源。同样可以在xml布局中像下面这样写
		// android:src="@drawable/logo"
		imageview.setImageResource(R.drawable.logo);

		// 设置imageview的Alpha值
		// Tom Xue: 参数为透明度，取值范围为0~255，数值越大越透明
		imageview.setAlpha(image_alpha);

		// 开启一个线程来让Alpha值递减
		new Thread(new Runnable() {
			public void run() {
				while (isrung) {
					try {
						Thread.sleep(200);
						// 更新Alpha值
						updateAlpha();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		// 接受消息之后更新imageview视图
		// Tom Xue: whenever any message comes...
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				imageview.setAlpha(image_alpha);
				textview.setText("现在alpha值是：" + Integer.toString(image_alpha));
				// 更新显示的图片
				// Tom Xue: Invalidate the whole view. If the view is visible,
				// onDraw(android.graphics.Canvas) will be called at some point
				// in the future. This must be called from a UI thread.
				imageview.invalidate();
			}
		};
	}

	public void updateAlpha() {
		if (image_alpha - 7 >= 0) {
			image_alpha -= 7;
		} else {
			image_alpha = 0;
			isrung = false;
		}
		// 发送需要更新imageview视图的消息
		// Tom Xue: Returns a new Message from the global message pool.
		// so the message is arbitrary. Just for activating handleMessage()
		mHandler.sendMessage(mHandler.obtainMessage());
	}
}
