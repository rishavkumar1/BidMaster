package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String standardDateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String istDateFormat = "yyyy-MM-dd'T'HH:mm:ss+05:30";
    public static String format(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
