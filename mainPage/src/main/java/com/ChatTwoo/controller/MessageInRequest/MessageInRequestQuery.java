package com.ChatTwoo.controller.MessageInRequest;

public class MessageInRequestQuery  {


    public String getMessageInRequestQuery() {
        StringBuilder builder = new StringBuilder();



        builder.append("SELECT\n" +
                "\t[tbl_MessageInRequestOffering].[ID] AS [ID],\n" +
                "\t[tbl_MessageInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                "\t[tbl_MessageInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                "\t[Sender].[Name] AS [Sender],\n" +
                "\t[tbl_MessageInRequestOffering].[RecipientID] AS [RecipientID],\n" +
                "\t[tbl_MessageInRequestOffering].[Message] AS [Message],\n" +
                "\t[tbl_MessageInRequestOffering].[RequestID] AS [RequestID]\n" +
                "FROM\n" +
                "\t[dbo].[tbl_MessageInRequestOffering] AS [tbl_MessageInRequestOffering]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_Contact] AS [Sender] ON [Sender].[ID] = [tbl_MessageInRequestOffering].[CreatedByID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_MessageInRequestOffering].[RequestID]\n" +
                //  "WHERE([tbl_MessageInRequestOffering].[RequestID] = '{91AF10BC-D96D-4434-A4C5-2B02C42D9CD3}')\n" +
                "ORDER BY\n" +
                "\t2 ASC"
        );

        return builder.toString();
    }
}
