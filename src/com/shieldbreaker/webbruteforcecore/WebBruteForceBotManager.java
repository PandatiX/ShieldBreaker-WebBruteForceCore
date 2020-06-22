package com.shieldbreaker.webbruteforcecore;

import com.shieldbreaker.bot.Bot;
import com.shieldbreaker.kernel.ShieldBreaker;
import com.shieldbreaker.webproxycore.WebProxyBotManager;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class WebBruteForceBotManager extends WebProxyBotManager {

    protected List<String> passwords;

    public WebBruteForceBotManager() {
        super();

        loadPasswords(parametersManager.getValue("file"));
        setProgressMax(passwords.size());
    }

    public void startBots(Class<? extends Bot> botClass) {
        displayStart();

        bots = new ArrayList<>();
        setFound(false);

        int passSize = passwords.size();
        int realNbThreads = Math.min(parametersManager.getNBTHREADS(), passSize);
        int width = passSize/realNbThreads;

        try {
            //Split passwords in ~equals sizes
            for (int i = 0; i < realNbThreads - 1; i++) {
                startBot(botClass, i*width, (i+1)*width);
            }
            startBot(botClass, (realNbThreads-1)*width, passSize);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            shieldBreaker.err("A fatal error occured while starting bots.", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
            System.exit(1);
        }
    }

    protected void startBot(Class<? extends Bot> botClass, int inf, int sup) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        WebBruteForceBot bot = (WebBruteForceBot)super.startBot(botClass);
        bot.setPasswords(passwords.subList(inf, sup));
    }

    private void loadPasswords(String passlist) {
        passwords = new ArrayList<>();

        try {
            File f = new File(passlist);
            if (!f.exists())
                throw new FileNotFoundException();
            InputStream is = Files.newInputStream(f.toPath());
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = buf.readLine()) != null) {
                if (!passwords.contains(line) && !line.isEmpty())
                    passwords.add(line);
            }
        } catch (FileNotFoundException e) {
            shieldBreaker.err("The specified file to use as a passlist seems not to exists. Please check [" + passlist + "].", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
        } catch (IOException e) {
            shieldBreaker.err("A fatal IOException was thrown while parsing " + passlist + ".", ShieldBreaker.OUT_PRIORITY.IMPORTANT);
        }
    }
}
