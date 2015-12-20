package com.marklogic.geonames;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

/**
 * Created by ableasdale on 19/12/2015.
 */
public class FixUTF8 {

    private static final Logger LOG = LoggerFactory.getLogger(FixUTF8.class);
    private static final String SOURCE_DATASET_BASE_URL = "http://download.geonames.org/export/dump";
    private static final String RESOURCES_DIR = "src/main/resources";
    private static final String ZIP_FILE = "/AD.zip";

    private static final String SOURCE_DATASET_URL = SOURCE_DATASET_BASE_URL + ZIP_FILE;
    private static final String ZIP_FILE_PATH = RESOURCES_DIR + ZIP_FILE;

    private static final String CSV_FILENAME = RESOURCES_DIR + "/allCountries.txt";
    private static final String CSV_OUTPUT_FILENAME = RESOURCES_DIR + "/allCountriesb.txt";


    public static void main(String[] args) {

        // 1. Download ZIP
        try {
            FileUtils.copyURLToFile(
                    new URL(SOURCE_DATASET_URL),
                    new File(ZIP_FILE_PATH)
            );
        } catch (IOException e) {
            LOG.error(Utils.wrapException(e), e);
        }

        // 2. Unzip file
        try {
            ZipFile zipFile = new ZipFile(ZIP_FILE_PATH);
            zipFile.extractAll(RESOURCES_DIR);
        } catch (ZipException e) {
            LOG.error(Utils.wrapException(e), e);
        }

        // 3. Modify and re-save CSV ready for load
        try {
            Reader reader = new InputStreamReader(new FileInputStream(CSV_FILENAME), "UTF-8");
            BufferedReader in = new BufferedReader(reader);

            Writer writer = new OutputStreamWriter(new FileOutputStream(CSV_OUTPUT_FILENAME), "UTF-8");
            BufferedWriter out = new BufferedWriter(writer);

            String s;
            while ((s = in.readLine()) != null) {
                s = s.replace('”', '\'');
                s = s.replace('"', '\'');
                out.write(s);
                out.newLine();
            }

            in.close();
            out.close();

        } catch (IOException e) {
            LOG.error(Utils.wrapException(e), e);
        }

    }
}
