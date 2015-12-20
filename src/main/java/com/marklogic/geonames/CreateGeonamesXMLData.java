package com.marklogic.geonames;

import com.marklogic.xcc.Request;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
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
 * cities1000.txt should create 145,501 Geonames XML Docs
 * <p/>
 * allCountries.txt should create 10,881,605 Geonames XML Docs
 * <p/>
 * TODO - download, unzip and "modify" the file to enable easy CSV parsing...
 */
public class CreateGeonamesXMLData {

    private static final Logger LOG = LoggerFactory.getLogger(CreateGeonamesXMLData.class);
    private static final String CSV_FILENAME = "src/main/resources/allCountriesb.txt";

    private static String createStringElem(String name, Object value) {
        if (value == null) {
            return String.format("<%s/>", name);
        } else {
            return String.format("<%s>%s</%s>", name, StringEscapeUtils.escapeXml11(value.toString()), name);
        }
    }

    private static void readWithCsvListReader() throws Exception {

        ICsvListReader listReader = null;
        List<Object> customerList = null;
        Session session = null;

        try {
            listReader = new CsvListReader(new FileReader(CSV_FILENAME), CsvPreference.TAB_PREFERENCE);

            //listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
            final CellProcessor[] processors = new CellProcessor[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

            session = MarkLogicXCCDataManager.getInstance().createSession();

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
                        .append(createStringElem("modificationDate", customerList.get(18)))
                        .append("</geoname>");

                Request r = session.newAdhocQuery(String.format("xdmp:document-insert(\"/%s.xml\",%s)", customerList.get(0).toString(), sb.toString()));
                try {
                    session.submitRequest(r);
                } catch (RequestException e) {
                    LOG.error(Utils.wrapException(e), e);
                }
            }
        } catch (Exception e) {
            LOG.error(Utils.wrapException(e), e);
        } finally {

            if (listReader != null) {
                listReader.close();
            }
        }

        session.close();
    }

    public static void main(String[] args) throws Exception {
        try {
            readWithCsvListReader();
        } catch (SuperCsvException e) {
            LOG.error(Utils.wrapException(e), e);
        }
    }
}
