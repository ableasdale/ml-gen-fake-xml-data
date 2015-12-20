package com.marklogic.geonames;

/**
 * Created by ableasdale on 19/12/2015.
 */
public class Utils {

    protected static String wrapException(Exception e){
        return String.format("[ %s ] Exception Caught: [ %s ]  \n", e.getClass().getName(), e.getMessage());
    }
}
