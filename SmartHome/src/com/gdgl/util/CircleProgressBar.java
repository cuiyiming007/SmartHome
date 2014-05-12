package com.gdgl.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
 
public class CircleProgressBar extends View {
	private int maxProgress = 250;
	private int progress = 250;
	private int progressStrokeWidth = 20;
	//画圆所在的距形区域
	RectF oval;
	Paint paint;
	String end_message;
	public CircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		oval = new RectF();
		paint = new Paint();
	}
 
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO 自动生成的方法存根
		super.onDraw(canvas);
		int width = this.getWidth();
		int height = this.getHeight();
 
		if(width!=height)
		{
			int min=Math.min(width, height);
			width=min;
			height=min;
		}
 
		paint.setAntiAlias(true); // 设置画笔为抗锯齿
		paint.setColor(0xff8B8989); // 设置画笔颜色
		canvas.drawColor(Color.TRANSPARENT); // 白色背景
		paint.setStrokeWidth(progressStrokeWidth); //线宽
		paint.setStyle(Style.STROKE);
 
		oval.left = progressStrokeWidth / 2; // 左上角x
		oval.top = progressStrokeWidth / 2; // 左上角y
		oval.right = width - progressStrokeWidth / 2; // 左下角x
		oval.bottom = height - progressStrokeWidth / 2; // 右下角y
 
		canvas.drawArc(oval, -90, 360, false, paint); // 绘制白色圆圈，即进度条背景
		paint.setColor(0xff20B2AA);
//		canvas.drawArc(oval, -90, ((float) progress / maxProgress) * 360, false, paint); // 绘制进度圆弧，这里是蓝色
		canvas.drawArc(oval, -90, 360-((float) progress / maxProgress) * 360, false, paint); // 绘制进度圆弧，这里是蓝色
		paint.setStrokeWidth(1);
		int textHeight = height / 4;
		String text = progress + "s";
		if(progress==0){
			if(null!=end_message){
				text=end_message;
				textHeight=height / 8;
			}
		}
		paint.setTextSize(textHeight);
		paint.setColor(0xffffffff);
		int textWidth = (int) paint.measureText(text, 0, text.length());
		paint.setStyle(Style.FILL);
		canvas.drawText(text, width / 2 - textWidth / 2, height / 2 +textHeight/2, paint);
 
	}
 
	public void setText(String s){
		if(null!=s && !s.trim().equals("")){
			end_message=s;
		}
	}
 
	public int getMaxProgress() {
		return maxProgress;
	}
 
	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}
 
	public void setProgress(int progress) {
		this.progress = progress;
		this.invalidate();
	}
 
	/**
	 * 非ＵＩ线程调用
	 */
	public void setProgressNotInUiThread(int progress) {
		this.progress = progress;
		this.postInvalidate();
	}
}
