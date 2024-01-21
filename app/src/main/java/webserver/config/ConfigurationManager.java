package webserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import webserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if (myConfigurationManager == null) {
            myConfigurationManager = new ConfigurationManager();
        }
        return myConfigurationManager;
    }

    public void loadConfigurationFile(String filePath) throws HttpConfigurationException {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException fnfe) {
            throw new HttpConfigurationException(fnfe.getMessage());
        }

        StringBuffer sb = new StringBuffer();
        int i = 0;

        try {
            while ( (i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new HttpConfigurationException(ioe.getMessage());
        }

        JsonNode conf;
        try {
            conf = Json.parse(sb.toString());
        } catch (IOException ioe) {
            throw new HttpConfigurationException(ioe.getMessage());
        }

        try {
            myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException jpe) {
            throw new HttpConfigurationException(jpe.getMessage());
        }

    }

    public Configuration getCurrentConfiguration() {
        if (myCurrentConfiguration == null) {
            throw new HttpConfigurationException("No current configuration set");
        }

        return myCurrentConfiguration;
    }
}
