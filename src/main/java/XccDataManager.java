import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class XccDataManager {

    private static final int Timeout = 20;
    private static Logger LOG = LoggerFactory.getLogger(XccDataManager.class);
    public String CLEAR_DATABASE_QUERY = "xdmp:node-delete(fn:doc());";
    public String SEARCH_QUERY = "cts:search(collection('snapshots'), cts:element-attribute-range-query(fn:QName('', 'revision_range'), fn:QName('', 'to'), '=', xs:long(2147483647)))";
    ContentSource contentSource;
    Configuration Config;
    String Database;

    private XccDataManager() {
        LOG.info("Creating the ContentSource for the first time");
        try {
            Config = new PropertiesConfiguration("config.properties");
            String xccUri = Config.getString("XCC_URI");
            Database = Config.getString("DATABASE");
            contentSource = ContentSourceFactory.newContentSource(new URI(xccUri));
        } catch (XccConfigException | URISyntaxException | ConfigurationException e) {
            LOG.error(String.format("[ %s ] Exception Caught: [ %s ]  \n", e.getClass().getName(), e.getMessage()), e);
        }
    }

    /* public ContentSource getContentSource() {
        return contentSource;
    }*/

    public static XccDataManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Session createSession() {
        Session s = contentSource.newSession(Database);
        try {
            s.setTransactionTimeout(Timeout);
        } catch (RequestException e) {
            LOG.error(String.format("[ %s ] Exception Caught: [ %s ]  \n", e.getClass().getName(), e.getMessage()), e);
        }
        return s;
    }

    public String createDocumentQueryWithId(long guid) {
        return String.format("xdmp:document-insert(\"/%d.xml\", <snapshot documentId=\"%d\">  <revision_range to=\"2147483647\" /></snapshot>, xdmp:default-permissions(), ('snapshots'))", guid, guid);
    }

    public String deleteDocumentQueryWithId(long guid) {
        return String.format("xdmp:document-delete(\"/%s.xml\")", guid);
    }

    public String lockIdForUpdate(long guid) {
        return String.format("xdmp:lock-for-update(\"/%s.xml\")", guid);
    }

    private static class LazyHolder {
        private static final XccDataManager INSTANCE = new XccDataManager();
    }
}