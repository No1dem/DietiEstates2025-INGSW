package it.unina.dietiEstates25.db;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfiguration {

    private static final String CONFIGURATION="config.properties";

    private DatabaseConfiguration(){}

    public static String getDbUrl() {
        try {
            InputStream is = DatabaseConfiguration.class.getClassLoader().getResourceAsStream(CONFIGURATION);
            if (is == null) {
                return "";
            }
            Properties p = new Properties();
            p.load(is);
            return p.getProperty("db.url");
        }catch (Exception e){
            return "";
        }
    }

    public static String getClientSecret() {
        try {
            InputStream is = DatabaseConfiguration.class.getClassLoader().getResourceAsStream(CONFIGURATION);
            if (is == null) {
                return "";
            }
            Properties p = new Properties();
            p.load(is);
            return p.getProperty("client.secret");
        }catch (Exception e){
            return "";
        }
    }

    public static String getSecret() {
        try {
            InputStream is = DatabaseConfiguration.class.getClassLoader().getResourceAsStream(CONFIGURATION);
            if (is == null) {
                return "";
            }
            Properties p = new Properties();
            p.load(is);
            return p.getProperty("secret.key");
        }catch (Exception e){
            return "";
        }
    }



}
