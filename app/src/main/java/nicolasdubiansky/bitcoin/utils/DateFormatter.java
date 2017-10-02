package nicolasdubiansky.bitcoin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Nicolas on 2/10/2017.
 */

public class DateFormatter {

    private static DateFormatter instance;
    private SimpleDateFormat simpleFormatForZuluTime;
    private SimpleDateFormat complexFormatForZuluTime;
    private SimpleDateFormat simpleFormat;


    private DateFormatter() {
        //reception date. Example: 2017-09-22T17:44:03Z
        simpleFormatForZuluTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        complexFormatForZuluTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleFormatForZuluTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        complexFormatForZuluTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    }

    public String getSimpleFormattedDate(String zuluDate) {
        try {
            Date zuluTimeformatted = simpleFormatForZuluTime.parse(zuluDate);
            return simpleFormat.format(zuluTimeformatted);
        } catch (ParseException e) {
            e.printStackTrace();
            Date zuluTimeformatted = null;
            try {
                zuluTimeformatted = complexFormatForZuluTime.parse(zuluDate);
                return simpleFormat.format(zuluTimeformatted);

            } catch (ParseException e1) {
                e1.printStackTrace();
                return zuluDate;
            }
        }
    }


    public static DateFormatter getInstance() {
        if (instance == null) {
            instance = new DateFormatter();
        }
        return instance;
    }

}
