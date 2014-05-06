package com.stephen.spritecreation2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Button;

public class SpriteCreation2 extends Activity {
	Bitmap spaceShip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		class AnimationView extends SurfaceView implements Runnable, SurfaceHolder.Callback{
			
			Thread animation = null;
			private boolean running;
			
			public AnimationView(Context context) {
				super(context);
				spaceShip = BitmapFactory.decodeResource(getResources(), R.drawable.craftmain);
				getHolder().addCallback(this);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// start the animation thread once the surface has been created
				
				animation = new Thread(this);
				running = true;
				
				animation.start(); // start a new thread to handle this activities animation
				
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				running = false;
				if(animation != null)
				{
					try {
						animation.join();  // finish the animation thread and let the animation thread die a natural death
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
			}
			
			int xPos = 0;
			int yPos = 0;
			
			@Override
			public void run() {
				while(running)
				{
					Canvas canvas = null;
					SurfaceHolder holder = getHolder();
					
					synchronized(holder)
					{
						canvas = holder.lockCanvas();								
						canvas.scale(1, 1);
						canvas.drawColor(Color.BLACK);
						canvas.drawBitmap(spaceShip, xPos, yPos, null);
						xPos+=5;
						yPos+=5;
					}
					
//					try 
//					{
//						Thread.sleep(40);
//					}
//					catch(InterruptedException e)
//					{
//						e.printStackTrace();
//					}
					holder.unlockCanvasAndPost(canvas);
					
				}
				
			}
			
        }
		requestWindowFeature(Window.FEATURE_NO_TITLE); // remove the title bar

        Display display = getWindowManager().getDefaultDisplay();
        
        DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		
		setContentView(new AnimationView(getApplicationContext()));
	}
}
