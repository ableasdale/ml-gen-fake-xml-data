import au.com.bytecode.opencsv.CSVReader;
import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Request;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Generate data from CSVs from:
 * <p/>
 * http://download.geonames.org/export/dump/
 * <p/>
 * Download cities5000.txt and place it in your src/main/resources dir
 */
public class CreateCityData {

    private static String createStringElem(String name, String value) {
        return "<" + name + ">" + value + "</" + name + ">";
    }

    private static final Logger LOG = LoggerFactory.getLogger(CreateCityData.class);

    public static void main(String[] args) {

        ContentSource cs = null;
        String fileName = "src/main/resources/cities5000.txt";
        List<String[]> allRows = null;

        // Setup ML ContentSource
        try {
            cs = ContentSourceFactory.newContentSource(new URI("xcc://q:q@localhost:8000"));
        } catch (XccConfigException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        try {
            CSVReader reader = new CSVReader(new FileReader(fileName), '\t', '"', 1);
            //Read all rows at once
            allRows = reader.readAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * geonameid         : integer id of record in geonames database
         * name              : name of geographical point (utf8) varchar(200)
         * asciiname         : name of geographical point in plain ascii characters, varchar(200)
         * alternatenames    : alternatenames, comma separated, ascii names automatically transliterated, convenience attribute from alternatename table, varchar(10000)
         * latitude          : latitude in decimal degrees (wgs84)
         * longitude         : longitude in decimal degrees (wgs84)
         * feature class     : see http://www.geonames.org/export/codes.html, char(1)
         * feature code      : see http://www.geonames.org/export/codes.html, varchar(10)
         * country code      : ISO-3166 2-letter country code, 2 characters
         * cc2               : alternate country codes, comma separated, ISO-3166 2-letter country code, 200 characters
         * admin1 code       : fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
         * admin2 code       : code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80)
         * admin3 code       : code for third level administrative division, varchar(20)
         * admin4 code       : code for fourth level administrative division, varchar(20)
         * population        : bigint (8 byte int)
         * elevation         : in meters, integer
         * dem               : digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
         * timezone          : the timezone id (see file timeZone.txt) varchar(40)
         * modification date : date of last modification in yyyy-MM-dd format
         */

        Session s = cs.newSession();

        for (String[] row : allRows) {



            StringBuilder sb = new StringBuilder();
            sb.append("<geoname>")
                    .append(createStringElem("geonameid", row[0]))
                    .append(createStringElem("name", row[1]))
                    .append(createStringElem("asciiname", row[2]))
                    .append(createStringElem("alternatenames", row[3]))
                    .append(createStringElem("latitude", row[4]))
                    .append(createStringElem("longitude", row[5]))
                    .append(createStringElem("featureClass", row[6]))
                    .append(createStringElem("featureCode", row[7]))
                    .append(createStringElem("countryCode", row[8]))
                    .append(createStringElem("cc2", row[9]))
                    .append(createStringElem("admin1Code", row[10]))
                    .append(createStringElem("admin2Code", row[11]))
                    .append(createStringElem("admin3Code", row[12]))
                    .append(createStringElem("admin4Code", row[13]))
                    .append(createStringElem("population", row[14]))
                    .append(createStringElem("elevation", row[15]))
                    .append(createStringElem("dem", row[16]))
                    .append(createStringElem("timezone", row[17]))
                    .append(createStringElem("modificationDate", row[18]))
                    .append("</geoname>");

            Request r = s.newAdhocQuery("xdmp:document-insert(\"/"+row[0]+".xml\","+sb.toString()+")");
            try {
                s.submitRequest(r);
            } catch (RequestException e) {
                e.printStackTrace();
            }
            //LOG.info(sb.toString());
        }
        s.close();
    }
}
