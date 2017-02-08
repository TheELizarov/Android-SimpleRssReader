package ua.motofun.rss.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * @author Elizarov Sergey (elizarov1988@gmail.com)
 *         18.08.14, 21:15
 */
public class TypefaceUtils {
    private static final String ROBOTO_THIN = "Roboto-Thin.ttf";
    private static final String ROBOTO_LIGHT = "Roboto-Light.ttf";
    private static final String ROBOTO_MEDIUM = "Roboto-Medium.ttf";
    private static final String ROBOTO_REGULAR = "Roboto-Regular.ttf";
    private static final String ROBOTO_BOLD = "Roboto-Bold.ttf";

	public static SpannableString setRobotoBold(Context context, String text) {
		return setTypeface(context, text, ROBOTO_BOLD);
	}

	public static SpannableString setRobotoMedium(Context context, String text) {
		return setTypeface(context, text, ROBOTO_MEDIUM);
	}

	public static SpannableString setRobotoThin(Context context, String text) {
		return setTypeface(context, text, ROBOTO_THIN);
	}

	public static SpannableString setRobotoLight(Context context, String text) {
		return setTypeface(context, text, ROBOTO_LIGHT);
	}

	public static Typeface getRobotoLight(Context context) {
		return getTypeface(context, ROBOTO_LIGHT);
	}

	public static Typeface getRobotoBold(Context context) {
		return getTypeface(context, ROBOTO_MEDIUM);
	}

	public static Typeface getRobotoThin(Context context) {
		return getTypeface(context, ROBOTO_THIN);
	}

	public static Typeface getRobotoRegular(Context context) {
		return getTypeface(context, ROBOTO_REGULAR);
	}

	public static SpannableString setTypeface(Context context, String text, String typeface) {
		SpannableString s = new SpannableString(text);
		s.setSpan(new TypefaceSpan(context, typeface), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return s;
	}

	public static SpannableString setTypefaceToString(String text, Typeface typeface) {
		SpannableString s = new SpannableString(text);
		s.setSpan(typeface, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return s;
	}

	public static Typeface getTypeface(Context context, String typeface) {
		TypefaceSpan ts = new TypefaceSpan(context, typeface);
		return ts.getTypeface();
	}

	private static class TypefaceSpan extends MetricAffectingSpan {
		private Typeface mTypeface;

		public TypefaceSpan(Context context, String typefaceName) {
			mTypeface = Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s", typefaceName));
		}

		public Typeface getTypeface() {
			return mTypeface;
		}

		@Override
		public void updateMeasureState(TextPaint p) {
			p.setTypeface(mTypeface);
			p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}

		@Override
		public void updateDrawState(TextPaint tp) {
			tp.setTypeface(mTypeface);
			tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
	}
}
