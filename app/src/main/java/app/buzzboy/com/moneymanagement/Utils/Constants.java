package app.buzzboy.com.moneymanagement.Utils;

import java.util.regex.Pattern;

/**
 * Created by Dhruv Thakkar on 4/18/2016.
 */
public class Constants {

    public static final String myFireBRef = "https://managemymoney.firebaseio.com/";

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$",
            Pattern.CASE_INSENSITIVE);


}
