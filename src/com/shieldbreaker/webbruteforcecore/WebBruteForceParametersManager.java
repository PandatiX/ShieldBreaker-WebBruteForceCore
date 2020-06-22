package com.shieldbreaker.webbruteforcecore;

import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.webproxycore.WebProxyParametersManager;
import com.shieldbreaker.webproxycore.exceptions.DualityProxyException;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.Option;

import java.util.Arrays;

public abstract class WebBruteForceParametersManager extends WebProxyParametersManager {

    protected final String defaultProtocol;
    protected final String defaultUserAgent;
    protected final int defaultPort;

    public WebBruteForceParametersManager() {
        super();

        defaultProtocol = "https";
        defaultUserAgent = "Hidden";
        defaultPort = 443;
        supportedProtocols = new String[]{"http", "https"};
    }

    @Override
    protected void createCliOptions() {
        Option domain = new Option("d", "domain", true, "Domain to brute force.");
        domain.setArgName("URL");
        addOption(domain, ParametersManager.KEYS.GUI_KEY);

        Option cookie = new Option("c", "cookie", true, "Cookie to use (i.e. PHPSESSID).");
        addOption(cookie, ParametersManager.KEYS.NO_KEY);

        Option userAgent = new Option("ua", "userAgent", true, "Set the User-Agent (requests headers).");
        addOption(userAgent, defaultUserAgent, ParametersManager.KEYS.NO_KEY);

        Option username = new Option("u", "username", true, "Username to test for.");
        addOption(username, ParametersManager.KEYS.GUI_KEY);

        Option protocol = new Option("pt", "protocol", true, "Set HTTP protocol (default is https).");
        protocol.setArgName("http/https");
        addOption(protocol, defaultProtocol, ParametersManager.KEYS.NO_KEY);

        Option port = new Option("pr", "port", true, "Set server port (default is 443).");
        port.setArgName("PORT");
        addOption(port, String.valueOf(defaultPort), ParametersManager.KEYS.NO_KEY);

        Option file = new Option("f", "file", true, "Path to the password list.");
        addOption(file, ParametersManager.KEYS.GUI_KEY);
    }

    @Override
    protected void checkParameters() throws DualityProxyException {
        super.checkParameters();
    }

    @Override
    public void setValue(@NotNull String key, String value) {
        if (key.equals("port"))
            setPort(value);
        else if (key.equals("protocol"))
            setProtocol(value);
        else
            super.setValue(key, value);
    }

    private void setPort(String port) {
        int value = (port.matches("\\d+") ? Integer.parseInt(port) : defaultPort);
        value = (value > 0 ? value : defaultPort);
        super.setValue("port", String.valueOf(value));
    }

    private void setProtocol(String protocol) {
        String p;
        if (Arrays.asList(supportedProtocols).contains(protocol)) {
            p = protocol;
        } else {
            p = defaultProtocol;
        }
        super.setValue("protocol", p);
    }
}
