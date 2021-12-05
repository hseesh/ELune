package com.yatoufang.service;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.yatoufang.templet.Application;
import com.yatoufang.config.ProjectSearchScope;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author hse
 */
public class PersistentService {


    public static void storeHeaders(String value) {
        PropertiesComponent properties = PropertiesComponent.getInstance(Application.project);
        String key = "com.yatoufang.Headers";
        properties.setValue(key, value);
    }

    public static void storeCookies(String value) {
        PropertiesComponent properties = PropertiesComponent.getInstance(Application.project);
        String key = "com.yatoufang.cookies";
        properties.setValue(key, value);
    }

    public static String getCookies() {
        PropertiesComponent properties = PropertiesComponent.getInstance(Application.project);
        String key = "com.yatoufang.cookies";
        return properties.getValue(key);
    }

    public static void clearAll() {
        CredentialAttributes credentialAttributes = createCredentialAttributes(Application.project.getName());
        PasswordSafe.getInstance().set(credentialAttributes, null);
        PropertiesComponent properties = PropertiesComponent.getInstance(Application.project);
        properties.unsetValue("com.yatoufang.Headers");
        properties.unsetValue("com.yatoufang.cookies");
    }

    public static String getHeaders() {
        PropertiesComponent properties = PropertiesComponent.getInstance(Application.project);
        String key = "com.yatoufang.Headers";
        return properties.getValue(key);
    }

    public static void storeHostInfo(String value) {
        System.out.println("value = " + value);
        CredentialAttributes credentialAttributes = createCredentialAttributes(Application.project.getName());
        Credentials credentials = new Credentials(Application.project.getName(), value);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    private static CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("yatoufang.durable", key));
    }

    public static String getHostInfo() {
        CredentialAttributes credentialAttributes = createCredentialAttributes(Application.project.getName());
        return PasswordSafe.getInstance().getPassword(credentialAttributes);
    }

    /**
     * read spring boot project config
     *
     * @param file         config file name
     * @param highAccuracy whether need highAccuracy result(used by My API Doc and My Post module)
     * @return host url
     */
    @SuppressWarnings("rawtypes")
    public static String readYaml(String file, boolean highAccuracy) {
        file = file == null ? "application.yml" : file;
        String port = null, path = null;
        PsiFile[] contextFiles = FilenameIndex.getFilesByName(Application.project, file, new ProjectSearchScope());
        if (contextFiles.length != 0) {
            for (PsiFile psiFile : contextFiles) {
                if (psiFile.getName().contains(".yml")) {
                    Yaml yaml = new Yaml();
                    Map map;
                    try {
                        map = yaml.loadAs(psiFile.getVirtualFile().getInputStream(), Map.class);

                        if (map != null) {
                            LinkedHashMap springs = (LinkedHashMap) map.get("spring");
                            if (springs != null) {
                                LinkedHashMap profiles = (LinkedHashMap) springs.get("profiles");
                                if (profiles != null) {
                                    String environment = (String) profiles.get("active");
                                    if (environment != null) {
                                        return readYaml("application-" + environment + ".yml", highAccuracy);
                                    }
                                }
                            }
                            LinkedHashMap server = (LinkedHashMap) map.get("server");
                            if (server != null) {
                                port = String.valueOf(server.get("port"));
                                LinkedHashMap servlet = (LinkedHashMap) server.get("servlet");
                                if (servlet != null) {
                                    path = (String) servlet.get("context-path");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (path == null && port == null) {
                        return readProperties(null, highAccuracy);
                    } else {
                        path = path == null ? "" : path;
                        port = port == null ? "" : port;
                        path = path.startsWith("/") ? path : "/" + path;
                        path = path.length() == 1 ? "" : path;
                        return "http://localhost:" + port + path;
                    }
                }
            }

        } else {
            return readProperties(null, highAccuracy);
        }
        if (highAccuracy) {
            return null;
        } else {
            return "http://localhost:8080" + Application.project.getName() + "_war_exploded";
        }

    }

    private static String readProperties(String file, boolean highAccuracy) {
        file = file == null ? "application.properties" : file;
        String port = null, path = null;
        PsiFile[] contextFiles = FilenameIndex.getFilesByName(Application.project, file, new ProjectSearchScope());

        for (PsiFile contextFile : contextFiles) {
            if (contextFile.getName().contains(".properties")) {
                Properties properties = new Properties();
                try {
                    properties.load(contextFile.getVirtualFile().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String environment = properties.getProperty("spring.profiles.active", "");
                if (!"".equals(environment)) {
                    return readProperties("application-" + environment + ".properties", highAccuracy);
                }

                path = properties.getProperty("server.servlet.context-path", "com.yatoufang");
                port = properties.getProperty("server.port", "com.yatoufang");
                break;
            }
        }

        if (highAccuracy && "com.yatoufang".equals(path) && "com.yatoufang".equals(port)) {
            return null;
        } else {
            port = port == null ? "8080" : port;
            path = path == null ? Application.project.getName() + "_war_exploded" : path;
            path = path.startsWith("/") ? path : "/" + path;
            return "http://localhost:" + port + path;
        }
    }

}
