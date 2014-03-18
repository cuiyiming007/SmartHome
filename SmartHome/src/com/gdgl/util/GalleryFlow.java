package com.gdgl.util;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {
	private Camera mCamera = new Camera();// �����
	private int mMaxRotationAngle = 45;// ���ת���Ƕ�
	private int mMaxZoom = -300;// //�������ֵ
	private int mCoveflowCenter;// �뾶ֵ

	public GalleryFlow(Context context) {
		super(context);
		// ֧��ת�� ,ִ��getChildStaticTransformation����
		init();
	}

	public GalleryFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init()
	{
		this.setStaticTransformationsEnabled(true);
		this.setCallbackDuringFling(false);
	}
	
	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	public int getMaxZoom() {
		return mMaxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	// ����gallery��ÿ��ͼƬ����ת(��д��gallery�з���)
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		// ȡ�õ�ǰ��view�İ뾶ֵ
		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();
		// ��ת�Ƕ�
		int rotationAngle = 0;
		// ����ת��״̬
		t.clear();
		// ����ת������
		t.setTransformationType(Transformation.TYPE_MATRIX);
		// ���ͼƬλ������λ�ò���Ҫ������ת
		if (childCenter == mCoveflowCenter) {
			transformImageBitmap((ImageView) child, t, 0);
		} else {
			// ���ͼƬ��gallery�е�λ��������ͼƬ����ת�Ƕ�
			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);		
			// �����ת�ǶȾ��ֵ���������ת�Ƕȷ��أ�-mMaxRotationAngle��mMaxRotationAngle;��
			if (Math.abs(rotationAngle) > mMaxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
						: mMaxRotationAngle;
			}
			transformImageBitmap((ImageView) child, t, rotationAngle);
		}
		return true;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void transformImageBitmap(ImageView child, Transformation t,
			int rotationAngle) {
		// ��Ч����б���
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
		// ͼƬ�߶�
		final int imageHeight = child.getLayoutParams().height;
		// ͼƬ���
		final int imageWidth = child.getLayoutParams().width;

		// ������ת�Ƕȵľ��ֵ
		final int rotation = Math.abs(rotationAngle);

		// ��Z���������ƶ�camera���ӽǣ�ʵ��Ч��Ϊ�Ŵ�ͼƬ��
		// �����Y�����ƶ�����ͼƬ�����ƶ���X���϶�ӦͼƬ�����ƶ���
		mCamera.translate(0.0f, 0.0f, 100.0f);
		// As the angle of the view gets less, zoom in
		if (rotation < mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
		}
		// ��Y������ת����ӦͼƬ�������﷭ת��
		// �����X������ת�����ӦͼƬ�������﷭ת��
		mCamera.rotateY(rotationAngle);
		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		mCamera.restore();
	}
}
