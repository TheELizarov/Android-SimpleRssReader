package ru.fmore.rss.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtils {

	public static final String SEPARATOR = "\n";

	public static String readResourceAsString(Context context, int resourceId, String separator) {
		InputStream inputStream = context.getResources().openRawResource(resourceId);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String str;
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str).append(separator);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				inputStream.close();
				br.close();
			} catch (IOException ignored) {
			}
		}
		return sb.toString();
	}

    public static void executeSqlCommands(Context context, int resourceId, SQLiteDatabase db) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        try {
            while ((str = br.readLine()) != null) {
                str.replace("\n", "").trim().replace("\t", "");
                if (str.length() > 0) {
                    db.execSQL(str);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
                br.close();
            } catch (IOException ignore) {
            }
        }
    }

	public static String readResourceAsString(Context context, int resourceId) {
		return readResourceAsString(context, resourceId, SEPARATOR);
	}

	public static void closeQuietly(Reader input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ignore) {
		}
	}

	public static void closeQuietly(Writer output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ignore) {
		}
	}

	public static void closeQuietly(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ignore) {
		}
	}

	public static void closeQuietly(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ignore) {
		}
	}
}
