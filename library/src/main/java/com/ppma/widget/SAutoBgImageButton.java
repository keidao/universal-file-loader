package com.ppma.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Applies a pressed state color filter or disabled state alpha for the button's
 * background drawable.
 * 
 * http://shiki.me/blog/android-button-background-image-pressedhighlighted-and-disabled-states-without-using-multiple-images/
 * 
 * @author shiki
 */
public class SAutoBgImageButton extends ImageButton {

	public SAutoBgImageButton(Context context) {
		super(context);
	}

	public SAutoBgImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SAutoBgImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setBackground(Drawable d) {
//		SAutoBgButtonBackgroundDrawable layer = new SAutoBgButtonBackgroundDrawable(d);
		if (Build.VERSION.SDK_INT >= 16) {
			super.setBackground(d);
		} else {
			super.setBackgroundDrawable(d);
		}		
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		SAutoBgButtonBackgroundDrawable layer = new SAutoBgButtonBackgroundDrawable(drawable);
		super.setImageDrawable(layer);
	}

	/**
	 * The stateful LayerDrawable used by this button.
	 */
	protected class SAutoBgButtonBackgroundDrawable extends LayerDrawable {

		// The color filter to apply when the button is pressed
		protected ColorFilter _pressedFilter = new LightingColorFilter(Color.LTGRAY, Color.DKGRAY);
		// Alpha value when the button is disabled
		protected int _disabledAlpha = 100;
		protected int _enabledAlpha = 255; // ADD THIS LINE

		public SAutoBgButtonBackgroundDrawable(Drawable d) {
			super(new Drawable[] { d });
		}

		@Override
		protected boolean onStateChange(int[] states) {
			boolean enabled = false;
			boolean pressed = false;

			for (int state : states) {
				if (state == android.R.attr.state_enabled)
					enabled = true;
				else if (state == android.R.attr.state_pressed)
					pressed = true;
			}

			mutate();
			if (enabled && pressed) {
				setColorFilter(_pressedFilter);
			} else if (!enabled) {
				setColorFilter(null);
				setAlpha(_disabledAlpha);
			} else {
				setColorFilter(null);
				setAlpha(_enabledAlpha);
			}

			invalidateSelf();

			return super.onStateChange(states);
		}

		@Override
		public boolean isStateful() {
			return true;
		}
	}

}