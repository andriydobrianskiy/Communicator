package com.mainPage.createRequest;

import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.createRequest.Querys.Queries;

import java.util.logging.Logger;

public class RequestQuery implements Queries {
    private static Logger log = Logger.getLogger(RequestQuery.class.getName());
    private StringBuilder builderQuery;


    @Override
    public String insertQuery() {
        builderQuery = new StringBuilder();

        builderQuery.append(
                "{call dbo.tsp_RODefineOfferingGroup (?,?) }"
        );

        return builderQuery.toString();
    }


    public String insertSTOPerformersQuery(NotFulfilled performer) {
        return "";
    }

    public String insertCreateQuery() {
        builderQuery = new StringBuilder();



        builderQuery.append(
                "INSERT INTO [tbl_RequestOffering]\n" +
                        "(CreatedOn, \n" +
                        "CreatedByID, \n" +
                        "ModifiedOn, \n" +
                        "ModifiedByID, \n" +
                        "Name)\n" +
                        "\tVALUES( \n" +
                        "\tCURRENT_TIMESTAMP,\n" +
                        "\t?,\n" +
                        "\tCURRENT_TIMESTAMP,\n" +
                        "\t?,\n" +
                        "\t?)"

        );

        return builderQuery.toString();

    }
    public String getInsertCreateRequestQuery() {
        builderQuery = new StringBuilder();

        builderQuery.append("INSERT INTO [dbo].[tbl_RequestOffering] (CreatedOn, CreatedByID, ModifiedOn, ModifiedByID, [AccountID], StoreCityID, [StatusID], [OfferingGroupID], OriginalGroupID, [StateID])\n" +
                        "                      VALUES (CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?,  ?, ?, ?, ?, ?, ? )"
             /* "INSERT INTO [dbo].[tbl_RequestOffering] (ID, [AccountID], [StatusID], [StoreCityID], [OfferingGroupID], [StateID], [CreatedOn], [CreatedByID], [ModifiedOn], [ModifiedByID], [OriginalGroupID] )\n" +
                      "VALUES (?, ?, ?, ?, ?,?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?, ?)"*/
        );

        return builderQuery.toString();
    }
    public String insertCreateRequestInGroup() {
        builderQuery = new StringBuilder();

        builderQuery.append(
                "INSERT INTO [tbl_RequestOffering] \n" +
                        "\t(CreatedOn,\n" +
                        "\tModifiedOn,\n" +
                        "\tCreatedByID,\n" +
                        "\tModifiedByID,\n" +
                        "\tGroupID)\n" +

                        "\tVALUES(\n" +

                        "CURRENT_TIMESTAMP,\n" +
                        "CURRENT_TIMESTAMP,\n" +
                        "?,\n" +
                        "?,\n" +
                        "?)\n"
        );


        return builderQuery.toString();
    }
    @Override
    public String updateQuery() {
        return null;
    }

    @Override
    public String deleteQuery() {
        return null;
    }
}
