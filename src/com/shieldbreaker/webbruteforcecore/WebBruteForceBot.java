package com.shieldbreaker.webbruteforcecore;

import com.shieldbreaker.bot.Bot;
import com.shieldbreaker.bot.BotManager;
import com.shieldbreaker.webproxycore.WebProxyBot;

import java.util.ArrayList;
import java.util.List;

public abstract class WebBruteForceBot extends WebProxyBot {

    protected List<String> passwords;

    //CLI parameters
    protected String domain;
    protected String cookie;
    protected String userAgent;
    protected String username;
    protected String protocol;
    protected int port;

    public WebBruteForceBot(BotManager manager) {
        super(manager);

        passwords = new ArrayList<>();

        //Set CLI parameters
        domain = parametersManager.getValue("domain");
        cookie = parametersManager.getValue("cookie");
        userAgent = parametersManager.getValue("userAgent");
        username = parametersManager.getValue("username");
        protocol = parametersManager.getValue("protocol");
        port = Integer.parseInt(parametersManager.getValue("port"));
    }

    public void setPasswords(List<String> passwords) {
        this.passwords = passwords;
    }

}
