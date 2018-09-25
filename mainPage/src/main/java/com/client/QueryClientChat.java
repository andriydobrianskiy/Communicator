package com.client;


import com.mainPage.createRequest.Querys.Queries;

import java.util.logging.Logger;

public class QueryClientChat implements Queries {

        private static Logger log = Logger.getLogger(QueryClientChat.class.getName());

        private StringBuilder query;

    public String getMainAccount() {


        query = new StringBuilder();

        query.append(
                "SELECT\n" +
                        "\t[tbl_MessageInRequestOffering].[ID] AS [ID],\n" +
                        "\tISNULL([tbl_MessageInRequestOffering].[CreatedOn], '') AS [CreatedOn],\n" +
                        "\t[tbl_MessageInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                        "\tISNULL([tbl_MessageInRequestOffering].[Message], '') AS [Message],\n" +
                        "\t[tbl_MessageInRequestOffering].[RequestID] AS [RequestID]\n" +
                        "FROM\n" +
                        "\t[dbo].[tbl_MessageInRequestOffering] AS [tbl_MessageInRequestOffering]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_Contact] AS [CreatedBy] ON [CreatedBy].[ID] = [tbl_MessageInRequestOffering].[CreatedByID]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_MessageInRequestOffering].[RequestID]\n" +
                        "ORDER BY\n" +
                        "\t2 ASC"
        );



        return query.toString();
    }
    @Override
    public String insertQuery() {
        query = new StringBuilder();

        query.append("INSERT INTO [dbo].[tbl_MessageInRequestOffering] ([ID], [RequestID], [CreatedOn], [CreatedByID], [Message], [ModifiedOn])\n" +
        "VALUES (?, ?, " +
                "CURRENT_TIMESTAMP, ?, " +
                "?, getdate())");

        return query.toString();
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
