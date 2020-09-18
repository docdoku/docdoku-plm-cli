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

package com.docdoku.plm.cli.commands.common;

import com.docdoku.plm.api.models.ConversionDTO;
import com.docdoku.plm.api.services.PartApi;
import com.docdoku.plm.cli.commands.BaseCommandLine;
import org.kohsuke.args4j.Option;

import java.io.IOException;

/**
 *
 * @author Morgan Guimard
 */
public class ConversionCommand extends BaseCommandLine {

    @Option(name="-w", aliases = "--workspace", required = true, metaVar = "<workspace>", usage="workspace on which operations occur")
    protected String workspace;

    @Option(metaVar = "<partnumber>", required = true, name = "-o", aliases = "--part", usage = "the part number of the part to verify the existence of conversion")
    private String number;

    @Option(metaVar = "<revision>", required = true, name="-r", aliases = "--revision", usage="specify revision of the part to analyze ('A', 'B'...)")
    private String revision;

    @Option(name="-i", required = true, aliases = "--iteration", metaVar = "<iteration>", usage="specify iteration of the part to retrieve ('1','2', '24'...); default is the latest")
    private int iteration;

    @Override
    public void execImpl() throws Exception {
        PartApi partApi = new PartApi(client);
        ConversionDTO conversion = partApi.getConversionStatus(workspace, number, revision, iteration);
        output.printConversion(conversion);
    }

    @Override
    public String getDescription() throws IOException {
        return langHelper.getLocalizedMessage("ConversionCommandDescription");
    }
}
