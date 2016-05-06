package app.buzzboy.com.moneymanagement.Utils;

import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Dhruv Thakkar on 4/18/2016.
 */
public class Constants {

    public static String FIREBASE_URL = "https://managemymoney.firebaseio.com/";

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$",
            Pattern.CASE_INSENSITIVE);

    public static final Locale LOC_USA = Locale.US;

    //ic_health
    //ic_friends
    //ic_education
    //ic_debt
    Map<String, Drawable> cat_images = new HashMap<String, Drawable>();

}
