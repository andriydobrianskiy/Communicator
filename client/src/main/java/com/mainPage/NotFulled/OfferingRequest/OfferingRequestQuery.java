package com.mainPage.NotFulled.OfferingRequest;

import com.mainPage.createRequest.Querys.Queries;

public class OfferingRequestQuery implements Queries {

    public String getOfferingRequestQuery(boolean top, int rowIndex,String requestID ) {
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT ");

        if (top) builder.append( " TOP " + rowIndex);

        builder.append(" [tbl_OfferingInRequestOffering].[ID] AS [ID], \n" +
                        "        ISNULL([tbl_OfferingInRequestOffering].[CreatedOn], '') AS [CreatedOn], \n" +
                        "        [tbl_OfferingInRequestOffering].[CreatedByID] AS [CreatedByID], \n" +
                        "        ISNULL([tbl_OfferingInRequestOffering].[ModifiedOn], '') AS [ModifiedOn], \n" +
                        "        [tbl_OfferingInRequestOffering].[ModifiedByID] AS [ModifiedByID], \n" +
                        "        [tbl_OfferingInRequestOffering].[OfferingID] AS [OfferingID], \n" +
                        "        [tbl_OfferingInRequestOffering].[RequestID] AS [RequestID], \n" +
                        "        ISNULL([tbl_Offering].[Index], 0) AS [Index], \n" +
                        "        ISNULL([tbl_Offering].[Skrut], '') AS [Skrut], \n" +
                        "        ISNULL([tbl_Offering].[Name], '') AS [OfferingName], \n" +
                        "        ISNULL([tbl_Offering].[DefaultOfferingCode], '') AS [DefaultOfferingCode], \n" +
                        "        ISNULL([tbl_OfferingInRequestOffering].[Quantity], 0) AS [Quantity], \n" +
                        "        ISNULL([tbl_OfferingInRequestOffering].[NewOfferingCode], '') AS [NewOfferingCode], \n" +
                        "        ISNULL([tbl_OfferingInRequestOffering].[NewDescription], '') AS [NewDescription] \n" +
                        "        FROM \n" +
                        "        [dbo].[tbl_OfferingInRequestOffering] AS [tbl_OfferingInRequestOffering] \n" +
                        "        LEFT OUTER JOIN \n" +
                        "        [dbo].[tbl_Offering] AS [tbl_Offering] ON [tbl_Offering].[ID] = [tbl_OfferingInRequestOffering].[OfferingID] \n" +
                        "        LEFT OUTER JOIN \n" +
                        "        [dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_OfferingInRequestOffering].[RequestID] \n"+
               "WHERE([tbl_OfferingInRequestOffering].[RequestID] = '"+requestID+"')"
        );

        return builder.toString();
    }

    @Override
    public String insertQuery() {
        return null;
    }

    @Override
    public String updateQuery() {
        return null;
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM [dbo].[tbl_OfferingInRequestOffering]\n" +
                "WHERE([tbl_OfferingInRequestOffering].[ID] = ?)";
    }
}
