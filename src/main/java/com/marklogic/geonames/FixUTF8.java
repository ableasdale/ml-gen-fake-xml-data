package com.marklogic.geonames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by ableasdale on 19/12/2015.
 */
public class FixUTF8 {

    private static final Logger LOG = LoggerFactory.getLogger(FixUTF8.class);

    public static void main(String[] args) {

        String CSV_FILENAME = "src/main/resources/allCountries.txt";
        String CSV_OUTPUT_FILENAME = "src/main/resources/allCountriesb.txt";


        try {
            Reader reader = new InputStreamReader(new FileInputStream(CSV_FILENAME), "UTF-8");
            BufferedReader fin = new BufferedReader(reader);

            Writer writer = new OutputStreamWriter(new FileOutputStream(CSV_OUTPUT_FILENAME), "UTF-8");
            BufferedWriter fout = new BufferedWriter(writer);

            String s;
            while ((s = fin.readLine()) != null) {
                s = s.replace('”', '\'');
                s = s.replace('"', '\'');
                fout.write(s);
                fout.newLine();
            }

            fin.close();
            fout.close();

        } catch (IOException e) {
            LOG.error(String.format("[ %s ] Exception Caught: [ %s ]  \n", e.getClass().getName(), e.getMessage()), e);
        }

    }
}
