/*
 * DocDoku, Professional Open Source
 * Copyright 2006 - 2017 DocDoku SARL
 *
 * This file is part of DocDokuPLM.
 *
 * DocDokuPLM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DocDokuPLM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with DocDokuPLM.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.docdoku.plm.cli.commands;

import com.docdoku.plm.api.DocDokuPLMClientFactory;
import com.docdoku.plm.api.client.ApiClient;
import com.docdoku.plm.cli.helpers.AccountsManager;
import com.docdoku.plm.cli.helpers.CliOutput;
import com.docdoku.plm.cli.helpers.LangHelper;
import org.kohsuke.args4j.Option;

import java.io.Console;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * @author Florent Garin
 */

public abstract class BaseCommandLine extends AbstractCommandLine {

    @Option(name = "-P", aliases = "--port", metaVar = "<port>", usage = "port number to use for connection; default is 443 for SSL otherwise 80")
    protected int port = -1;

    @Option(name = "-h", aliases = "--host", metaVar = "<host>", usage = "host of the DocDokuPLM server to connect; default is docdokuplm.net")
    protected String host = "docdokuplm.net";

    @Option(name = "-C", aliases = "--context-path", metaVar = "<contextPath>", usage = "Context root for DocDokuPLM API, default is /docdoku-plm-server-rest")
    protected String contextPath = "/docdoku-plm-server-rest";

    @Option(name = "-p", aliases = "--password", metaVar = "<password>", usage = "password to log in")
    protected String password;

    @Option(name = "-S", aliases = "--ssl", usage = "use a SSL (TLS) connection")
    protected boolean ssl;

    @Option(name = "-dbg", aliases = "--debug", usage = "enable debug mode")
    protected boolean debug = false;

    protected ApiClient client;

    private void promptForUser() {
        Console c = System.console();
        if (c == null) {
            return;
        }

        user = c.readLine(langHelper.getLocalizedMessage("PromptUser") + " '" + host + "': ");
    }

    private void promptForPassword() {
        Console c = System.console();
        if (c == null) {
            return;
        }
        password = new String(c.readPassword(langHelper.getLocalizedMessage("PromptPassword") + " '" + user + "@" + host + "': "));
    }

    @Override
    public void exec() throws Exception {
        Locale userLocale = new AccountsManager().getUserLocale(user);
        langHelper = new LangHelper(userLocale);
        output = CliOutput.getOutput(format, userLocale);
        output.setDebug(debug);
        if(port == -1) {
            port = ssl ? 443 : 80;
        }
        if (user == null && format.equals(CliOutput.formats.HUMAN)) {
            promptForUser();
        }
        if (password == null && format.equals(CliOutput.formats.HUMAN)) {
            promptForPassword();
        }

        String apiBasePath = getServerURL().toString() + "/api";

        client = DocDokuPLMClientFactory.createJWTClient(apiBasePath, user, password);

        execImpl();

    }

    public URL getServerURL() throws MalformedURLException {
        return new URL(ssl ? "https" : "http", host, port, contextPath);
    }
}
