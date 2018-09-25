package com.mainPage.InProcessing.InProcessingRequest;

public class InProcessingRequestQuery  {
    public String getInProcessingRequestQuery(boolean top, int rowIndex, String requestID) {
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT ");

        if (top) builder.append( " TOP " + rowIndex + "\n");

        builder.append("[tbl_OfferingInRequestOffering].[ID] AS [ID],\n" +
                "\t[tbl_OfferingInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                "\t[tbl_OfferingInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                "\t[tbl_OfferingInRequestOffering].[ModifiedOn] AS [ModifiedOn],\n" +
                "\t[tbl_OfferingInRequestOffering].[ModifiedByID] AS [ModifiedByID],\n" +
                "\t[tbl_OfferingInRequestOffering].[OfferingID] AS [OfferingID],\n" +
                "\t[tbl_OfferingInRequestOffering].[RequestID] AS [RequestID],\n" +
                "\t[tbl_Offering].[Index] AS [Index],\n" +
                "\t[tbl_Offering].[Skrut] AS [Skrut],\n" +
                "\t[tbl_Offering].[Name] AS [OfferingName],\n" +
                "\t[tbl_Offering].[DefaultOfferingCode] AS [DefaultOfferingCode],\n" +
                "\t[tbl_OfferingInRequestOffering].[Quantity] AS [Quantity],\n" +
                "\t[tbl_OfferingInRequestOffering].[NewOfferingCode] AS [NewOfferingCode],\n" +
                "\t[tbl_OfferingInRequestOffering].[NewDescription] AS [NewDescription],\n" +
                "\t(SELECT TOP 1\n" +
                "\t\t'-' AS [Exist],\n" +
                "\t [tbl_OfferingInRequestOffering].SetColorOffering\n" +
                "\tFROM\n" +
                "\t\t[dbo].[tbl_OfferingAnalogue] AS [tbl_OfferingAnalogue]\n" +
                "\tWHERE([tbl_OfferingAnalogue].[OfferingID] = [tbl_OfferingInRequestOffering].[OfferingID])) AS [IsRoot],\n" +
                "\t [tbl_OfferingInRequestOffering].[ColorByOfferingSale] AS [ColorByOfferingSale] \n" +
                "FROM\n" +
                "\t[dbo].[tbl_OfferingInRequestOffering] AS [tbl_OfferingInRequestOffering]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_Offering] AS [tbl_Offering] ON [tbl_Offering].[ID] = [tbl_OfferingInRequestOffering].[OfferingID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_OfferingInRequestOffering].[RequestID]\n" +
                "WHERE [tbl_OfferingInRequestOffering].[RequestID] = '"+requestID+"' "

        );
        return builder.toString();
    }

}
