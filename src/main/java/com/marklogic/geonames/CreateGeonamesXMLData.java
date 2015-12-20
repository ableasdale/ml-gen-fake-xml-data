package com.marklogic.geonames;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.util.List;

/**
 * Created by ableasdale on 19/12/2015.
 * <p/>
 * Source files obtained from: http://download.geonames.org/export/dump/
 * <p/>
 * cities5000.txt should create 15,118 Geonames XML Docs (there are more in this doc but there are a large number of tabs on line 15119 which confuse the CSV parser).
 * <p/>
 * cities1000.txt should create 145,501 Geonames XML Docs
 * <p/>
 * allCountries.txt should create 10,881,605 Geonames XML Docs
 * <p/>
 * TODO - download, unzip and "modify" the file to enable easy CSV parsing...
 */
public class CreateGeonamesXMLData {

    private static final int TXN_BATCH_SIZE = 2500;
    private static final Logger LOG = LoggerFactory.getLogger(CreateGeonamesXMLData.class);

    // TODO - hard coded!
    private static final String CSV_FILENAME = "src/main/resources/working-files/allCountriesb.txt";

    private static String createStringElem(String name, Object value) {
        if (value == null) {
            return String.format("<%s/>", name);
        } else {
            return String.format("<%s>%s</%s>", name, StringEscapeUtils.escapeXml11(value.toString()), name);
        }
    }

    private static void readWithCsvListReader() throws Exception {

        ICsvListReader listReader = null;
        List<Object> customerList;
        StringBuilder batch = new StringBuilder();
        int tally = 0;

        try {
            listReader = new CsvListReader(new FileReader(CSV_FILENAME), CsvPreference.TAB_PREFERENCE);
            //listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
            final CellProcessor[] processors = new CellProcessor[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

            int batchCount = 0;

            while ((customerList = listReader.read(processors)) != null) {

                StringBuilder sb = new StringBuilder();
                sb.append("<geoname>")
                        .append(createStringElem("geonameid", customerList.get(0)))
                        .append(createStringElem("name", customerList.get(1)))
                        .append(createStringElem("asciiname", customerList.get(2)))
                        .append(createStringElem("alternatenames", customerList.get(3)))
                        .append(createStringElem("latitude", customerList.get(4)))
                        .append(createStringElem("longitude", customerList.get(5)))
                        .append(createStringElem("featureClass", customerList.get(6)))
                        .append(createStringElem("featureCode", customerList.get(7)))
                        .append(createStringElem("countryCode", customerList.get(8)))
                        .append(createStringElem("cc2", customerList.get(9)))
                        .append(createStringElem("admin1Code", customerList.get(10)))
                        .append(createStringElem("admin2Code", customerList.get(11)))
                        .append(createStringElem("admin3Code", customerList.get(12)))
                        .append(createStringElem("admin4Code", customerList.get(13)))
                        .append(createStringElem("population", customerList.get(14)))
                        .append(createStringElem("elevation", customerList.get(15)))
                        .append(createStringElem("dem", customerList.get(16)))
                        .append(createStringElem("timezone", customerList.get(17)))
                        .append(createStringElem("modificationDate", Utils.parseAndFixDate(customerList.get(18).toString())))
                        .append("</geoname>");

                String query = String.format("xdmp:document-insert(\"/%s.xml\",%s)", customerList.get(0).toString(), sb.toString());

                if (batchCount == 0) {
                    batch.append(query);
                } else {
                    batch.append(",").append(query);
                }
                batchCount++;

                if (batchCount == TXN_BATCH_SIZE) {
                    tally += TXN_BATCH_SIZE;
                    LOG.info(String.format("Batch ready - Size: [ %d ] Last id: [ %s ] total: [ %d ]", TXN_BATCH_SIZE, customerList.get(0), tally));
                    MarkLogicXCCDataManager.actionTask(batch.toString());
                    batchCount = 0;
                    batch = new StringBuilder();
                }
            }
        } catch (Exception e) {
            LOG.error(Utils.wrapException(e), e);
        } finally {

            if (listReader != null) {
                listReader.close();
            }
        }
        MarkLogicXCCDataManager.actionTask(batch.toString());
    }

    public static void main(String[] args) throws Exception {
        try {
            readWithCsvListReader();
        } catch (SuperCsvException e) {
            LOG.error(Utils.wrapException(e), e);
        }
    }
}
