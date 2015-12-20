package com.marklogic.geonames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ableasdale on 19/12/2015.
 */
public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    protected static final String isoDateFormat = "yyyy-MM-dd";
    protected static final String alternateDateFormat = "dd/MM/yyyy";
    protected static String wrapException(Exception e){
        return String.format("[ %s ] Exception Caught: [ %s ]  \n", e.getClass().getName(), e.getMessage());
    }

    /**
     * For a valid entry into our date range indexes these need to be of the format YYYY-MM-DD
     *
     * @param date
     * @return
     */
    protected static String parseAndFixDate(String date) {
        if(date.contains("/")){
            // Looks like some of these CSVs use the format "dd/MM/yyyy" so we handle this here
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(alternateDateFormat);
                SimpleDateFormat targetDateFormat = new SimpleDateFormat(isoDateFormat);
                Date parsedDate = sdf.parse(date);
                return targetDateFormat.format(parsedDate);
            } catch (ParseException e) {
                LOG.error(Utils.wrapException(e), e);
            }
        } else {
            return date;
        }
    return date;
    }
}
