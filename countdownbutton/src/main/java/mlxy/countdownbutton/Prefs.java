package mlxy.countdownbutton;

import android.content.Context;
import android.content.SharedPreferences;

/** SharedPreferences CRUD. */
public class Prefs {
    /** <p>Put a <b>boolean</b>/<b>float</b>/<b>int</b>/<b>long</b>/<b>String</b> value into
     *  preferences file named as {@linkplain Context#getPackageName() packageName}.
     *
     *  @throws IllegalArgumentException If The type of <code>value</code> is not one of those.
     *  @see android.content.SharedPreferences.Editor
     *  */
    public static void put(Context context, String key, Object value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();

        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else {
            throw new IllegalArgumentException("Argument illegal, see JavaDocs.");
        }

        editor.commit();
    }

    /** <p>Retrieve a <b>boolean</b>/<b>float</b>/<b>int</b>/<b>long</b>/<b>String</b> value from preferences.
     *
     *  @return value corresponds to <code>key</code> if <code>key</code> exists,
     *  <code>defValue</code> otherwise.
     *  @see SharedPreferences
     *  @throws IllegalArgumentException If The type of <code>defValue</code> is not one of those.
     *  */
    @SuppressWarnings("unchecked")
    public static <P> P get(Context context, String key, P defValue) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        Object result;

        if (defValue instanceof Boolean) {
            result = prefs.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            result = prefs.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Integer) {
            result = prefs.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Long) {
            result = prefs.getLong(key, (Long) defValue);
        } else if (defValue instanceof String) {
            result = prefs.getString(key, (String) defValue);
        } else {
            throw new IllegalArgumentException("Argument illegal, see JavaDocs.");
        }

        return (P) result;
    }

    public static void delete(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        if (prefs.contains(key)) {
            prefs.edit().remove(key).commit();
        }
    }
}