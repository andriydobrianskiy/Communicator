package com.mainPage.NotFulled;

import com.connectDatabase.DBConnection;
import com.mainPage.createRequest.Querys.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotFulledQuery implements Queries {
    private static Logger log = Logger.getLogger(NotFulledQuery.class.getName());

    private StringBuilder query;

    public String getMainNotFulled(Boolean top, int rowIndex, String createdByID, String offeringGroupID, Integer pricingType) {

        query = new StringBuilder();
        query.append("SELECT \n");




      //  if (top) query.append(" TOP " + rowIndex + "\n");

       query.append("[tbl_RequestOffering].[ID] AS [ID],\n" +
           //     "\t ROW_NUMBER() OVER ( ORDER BY  CreatedOn) AS RowNum, \n" +
                "\t[tbl_RequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                "\t[tbl_RequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                "\t[tbl_Contact].[Name] AS [CreatedBy],\n" +
                "\t[tbl_RequestOffering].[AccountID] AS [AccountID],\n" +
                "\t[tbl_Account].[Name] AS [AccountName],\n" +
                "\t[tbl_Account].[Code] AS [AccountCode],\n" +
                "\t[tbl_Account].[SaldoSel]\n" +
                "\n" +
                "\n" +
                "\n" +
                "/*( \n" +
                "(ISNULL((SELECT\n" +
                "     SUM(ISNULL(CashCredit.RemainPaid, 0))\n" +
                " FROM \n" +
                "     tbl_Cashflow as CashCredit\n" +
                " WHERE \n" +
                "    (CashCredit.TypeID = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND \n" +
                "     CashCredit.DestinationID <> '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' AND\n" +
                "     CashCredit.PayerID = tbl_Account.ID AND\n" +
                "     CashCredit.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')),0)\n" +
                ")\n" +
                "-\n" +
                "(ISNULL((SELECT\n" +
                "     SUM(ISNULL(CashPromice.RemainPaid, 0))\n" +
                " FROM \n" +
                "     tbl_Cashflow as CashPromice\n" +
                "WHERE (\n" +
                "    (CashPromice.TypeID = '{484C8429-DABF-482A-BC7B-4C75D1436A1B}' AND \n" +
                "     NOT CashPromice.DestinationID IN('{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}', '{0D6BE797-DDE0-477C-AE28-978D2C2C1EFD}') AND\n" +
                "     CashPromice.RecipientID = tbl_Account.ID AND\n" +
                "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}') \n" +
                "     OR\n" +
                "    (CashPromice.TypeID = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND \n" +
                "     CashPromice.DestinationID = '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' AND\n" +
                "     CashPromice.PayerID = tbl_Account.ID AND\n" +
                "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')\n" +
                "    )), 0) - \n" +
                "  \n" +
                "ISNULL((SELECT\n" +
                "     SUM(ISNULL(CashPromice.RemainPaid, 0))\n" +
                " FROM \n" +
                "     tbl_Cashflow as CashPromice\n" +
                "WHERE\n" +
                "    (CashPromice.TypeID = '{484C8429-DABF-482A-BC7B-4C75D1436A1B}' AND \n" +
                "     (CashPromice.DestinationID = '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' OR\n" +
                "     CashPromice.DestinationID = '{0D6BE797-DDE0-477C-AE28-978D2C2C1EFD}') AND\n" +
                "     CashPromice.RecipientID = tbl_Account.ID AND\n" +
                "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')),0)\n" +
                ")\n" +
                ") */ AS [AccountSaldo],\n" +
                "\t(CASE\n" +
                "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                "    THEN ''\n" +
                "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                "    THEN 'Не солідний'\n" +
                "    ELSE ''\n" +
                "END)\n" +
                "\n" +
                "\n" +
                "/*CASE WHEN [tbl_Account].[UnblockDate] > GETDATE() THEN\n" +
                "\t(SELECT TOP 1\n" +
                "\t\tN'Не солидный' AS [Exist]\n" +
                "\tFROM\n" +
                "\t\ttbl_Cashflow AS [Cashflow]\n" +
                "\tWHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND\n" +
                "\t\t[Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND\n" +
                "\t\t[Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND\n" +
                "\t\t([Cashflow].[PayDate] < (CAST(FLOOR(CAST(GETDATE() AS FLOAT)) AS DATETIME)))) \n" +
                "                   AND [tbl_Account].[UnblockDate] < GETDATE()))\n" +
                "\tELSE \n" +
                "\t(SELECT TOP 1\n" +
                "\t\tN'Не солидный' AS [Exist]\n" +
                "\tFROM\n" +
                "\t\ttbl_Cashflow AS [Cashflow]\n" +
                "\tWHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND\n" +
                "\t\t[Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND\n" +
                "\t\t[Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND\n" +
                "\t\t([Cashflow].[PayDate] < GETDATE()))))\n" +
                "END*/ AS [AccountIsSolid],\n" +
                "\t[tbl_RequestOffering].[Number] AS [Number],\n" +
                "\t[tbl_RequestOffering].[StatusID] AS [StatusID],\n" +
                "\t[tbl_RequestOfferingStatus].[Name] AS [Status],\n" +
                "\t[tbl_RequestOffering].[StoreCityID] AS [StoreCityID],\n" +
                "\t[tbl_StoreCity].[Name] AS [StoreCity],\n" +
                "\t[tbl_RequestOffering].[OfferingGroupID] AS [OfferingGroupID],\n" +
                "\t[OfferingGroup].[Name] AS [OfferingGroupName],\n" +
                "\t[tbl_RequestOffering].[OriginalGroupID] AS [OriginalGroupID],\n" +
                "\t(SELECT\n" +
                "\t\t[OriginalGroupName].[Name] AS [Name]\n" +
                "\tFROM\n" +
                "\t\t[dbo].[tbl_Contact] AS [OriginalGroupName]\n" +
                "\tWHERE([tbl_RequestOffering].[OriginalGroupID] = [OriginalGroupName].[ID])) AS [OriginalGroupName],\n" +
                "\t[tbl_RequestOffering].[IsNewMessage] AS [IsNewMessage],\n" +
                "\t[tbl_RequestOffering].[JointAnnulment] AS [JointAnnulment],\n" +
                "\t[tbl_RequestOffering].[Note] AS [Note],\n" +
                "\t[tbl_RequestOffering].[LastMessage] AS [LastMessage],\n" +
                "\t[tbl_RequestOffering].[GroupChangedByID] AS [GroupChangedByID],\n" +
                "\t(SELECT\n" +
                "\t\t[GroupChangedBy].[Name] AS [Name]\n" +
                "\tFROM\n" +
                "\t\t[dbo].[tbl_Contact] AS [GroupChangedBy]\n" +
                "\tWHERE([tbl_RequestOffering].[GroupChangedByID] = [GroupChangedBy].[ID])) AS [GroupChangedBy],\n" +
                "\t[tbl_RequestOffering].[IsReadMeassage] AS [IsReadMeassage],\n" +
                "\t[tbl_Account].[SpecialMarginTypeID] AS [SpecialMarginTypeID],\n" +
                "\t[SMT].[Name] AS [SpecialMarginTypeName],\n" +
                "\t[tbl_RequestOffering].[StateID] AS [StateID],\n" +
                "\t[tbl_RequestOfferingState].[Name] AS [StateName],\n" +
                "\t[tbl_RequestOffering].[CashType] AS [CashType],\n"+
                       "\t[tbl_RequestOffering].[PricingType] AS [PricingType],\n" +
                       "\t[tbl_RequestOffering].[PricingDescription] AS [PricingDescription]\n" +
                "FROM\n" +
                "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_StoreCity] AS [tbl_StoreCity] ON [tbl_StoreCity].[ID] = [tbl_RequestOffering].[StoreCityID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_Account] AS [tbl_Account] ON [tbl_Account].[ID] = [tbl_RequestOffering].[AccountID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_RequestOfferingStatus] AS [tbl_RequestOfferingStatus] ON [tbl_RequestOfferingStatus].[ID] = [tbl_RequestOffering].[StatusID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_Contact] AS [tbl_Contact] ON [tbl_Contact].[ID] = [tbl_RequestOffering].[CreatedByID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_Contact] AS [OfferingGroup] ON [OfferingGroup].[ID] = [tbl_RequestOffering].[OfferingGroupID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_SpecialMarginType] AS [SMT] ON [SMT].[ID] = [tbl_Account].[SpecialMarginTypeID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_RequestOfferingState] AS [tbl_RequestOfferingState] ON [tbl_RequestOfferingState].[ID] = [tbl_RequestOffering].[StateID]\n" +
                "WHERE(([tbl_RequestOffering].[CreatedByID] = '"+createdByID+"' OR\n" +
                "\t[tbl_RequestOffering].[OfferingGroupID] = '"+offeringGroupID+"' ) AND\n" +
               "([tbl_RequestOffering].[PricingType] = "+pricingType+") AND \n"+
                "\t[tbl_RequestOffering].[StatusID] = '{44C65A86-2DFA-4808-AADF-D7CFDEB11F3B}') \n"
                );


        return query.toString();
    }


    public String getMainNotFulledSearchSkrut(Boolean top, int rowIndex, String createdByID, String offeringGroupID, String requestID) {

        query = new StringBuilder();
        query.append("SELECT \n");

       // if (top) query.append(" TOP " + rowIndex + "\n");

        query.append("[tbl_RequestOffering].[ID] AS [ID],\n" +
                "\t[tbl_RequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                "\t[tbl_RequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                "\t[tbl_Contact].[Name] AS [CreatedBy],\n" +
                "\t[tbl_RequestOffering].[AccountID] AS [AccountID],\n" +
                "\t[tbl_Account].[Name] AS [AccountName],\n" +
                "\t[tbl_Account].[Code] AS [AccountCode],\n" +
                "\t[tbl_Account].[SaldoSel]\n" +
                "\n" +
                "\n" +
                "\n" +
                "/*( \n" +
                "(ISNULL((SELECT\n" +
                "     SUM(ISNULL(CashCredit.RemainPaid, 0))\n" +
                " FROM \n" +
                "     tbl_Cashflow as CashCredit\n" +
                " WHERE \n" +
                "    (CashCredit.TypeID = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND \n" +
                "     CashCredit.DestinationID <> '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' AND\n" +
                "     CashCredit.PayerID = tbl_Account.ID AND\n" +
                "     CashCredit.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')),0)\n" +
                ")\n" +
                "-\n" +
                "(ISNULL((SELECT\n" +
                "     SUM(ISNULL(CashPromice.RemainPaid, 0))\n" +
                " FROM \n" +
                "     tbl_Cashflow as CashPromice\n" +
                "WHERE (\n" +
                "    (CashPromice.TypeID = '{484C8429-DABF-482A-BC7B-4C75D1436A1B}' AND \n" +
                "     NOT CashPromice.DestinationID IN('{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}', '{0D6BE797-DDE0-477C-AE28-978D2C2C1EFD}') AND\n" +
                "     CashPromice.RecipientID = tbl_Account.ID AND\n" +
                "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}') \n" +
                "     OR\n" +
                "    (CashPromice.TypeID = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND \n" +
                "     CashPromice.DestinationID = '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' AND\n" +
                "     CashPromice.PayerID = tbl_Account.ID AND\n" +
                "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')\n" +
                "    )), 0) - \n" +
                "  \n" +
                "ISNULL((SELECT\n" +
                "     SUM(ISNULL(CashPromice.RemainPaid, 0))\n" +
                " FROM \n" +
                "     tbl_Cashflow as CashPromice\n" +
                "WHERE\n" +
                "    (CashPromice.TypeID = '{484C8429-DABF-482A-BC7B-4C75D1436A1B}' AND \n" +
                "     (CashPromice.DestinationID = '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' OR\n" +
                "     CashPromice.DestinationID = '{0D6BE797-DDE0-477C-AE28-978D2C2C1EFD}') AND\n" +
                "     CashPromice.RecipientID = tbl_Account.ID AND\n" +
                "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')),0)\n" +
                ")\n" +
                ") */ AS [AccountSaldo],\n" +
                "\t(CASE\n" +
                "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                "    THEN ''\n" +
                "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                "    THEN 'Не солідний'\n" +
                "    ELSE ''\n" +
                "END)\n" +
                "\n" +
                "\n" +
                "/*CASE WHEN [tbl_Account].[UnblockDate] > GETDATE() THEN\n" +
                "\t(SELECT TOP 1\n" +
                "\t\tN'Не солидный' AS [Exist]\n" +
                "\tFROM\n" +
                "\t\ttbl_Cashflow AS [Cashflow]\n" +
                "\tWHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND\n" +
                "\t\t[Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND\n" +
                "\t\t[Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND\n" +
                "\t\t([Cashflow].[PayDate] < (CAST(FLOOR(CAST(GETDATE() AS FLOAT)) AS DATETIME)))) \n" +
                "                   AND [tbl_Account].[UnblockDate] < GETDATE()))\n" +
                "\tELSE \n" +
                "\t(SELECT TOP 1\n" +
                "\t\tN'Не солидный' AS [Exist]\n" +
                "\tFROM\n" +
                "\t\ttbl_Cashflow AS [Cashflow]\n" +
                "\tWHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND\n" +
                "\t\t[Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND\n" +
                "\t\t[Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND\n" +
                "\t\t([Cashflow].[PayDate] < GETDATE()))))\n" +
                "END*/ AS [AccountIsSolid],\n" +
                "\t[tbl_RequestOffering].[Number] AS [Number],\n" +
                "\t[tbl_RequestOffering].[StatusID] AS [StatusID],\n" +
                "\t[tbl_RequestOfferingStatus].[Name] AS [Status],\n" +
                "\t[tbl_RequestOffering].[StoreCityID] AS [StoreCityID],\n" +
                "\t[tbl_StoreCity].[Name] AS [StoreCity],\n" +
                "\t[tbl_RequestOffering].[OfferingGroupID] AS [OfferingGroupID],\n" +
                "\t[OfferingGroup].[Name] AS [OfferingGroupName],\n" +
                "\t[tbl_RequestOffering].[OriginalGroupID] AS [OriginalGroupID],\n" +
                "\t(SELECT\n" +
                "\t\t[OriginalGroupName].[Name] AS [Name]\n" +
                "\tFROM\n" +
                "\t\t[dbo].[tbl_Contact] AS [OriginalGroupName]\n" +
                "\tWHERE([tbl_RequestOffering].[OriginalGroupID] = [OriginalGroupName].[ID])) AS [OriginalGroupName],\n" +
                "\t[tbl_RequestOffering].[IsNewMessage] AS [IsNewMessage],\n" +
                "\t[tbl_RequestOffering].[JointAnnulment] AS [JointAnnulment],\n" +
                "\t[tbl_RequestOffering].[Note] AS [Note],\n" +
                "\t[tbl_RequestOffering].[LastMessage] AS [LastMessage],\n" +
                "\t[tbl_RequestOffering].[GroupChangedByID] AS [GroupChangedByID],\n" +
                "\t(SELECT\n" +
                "\t\t[GroupChangedBy].[Name] AS [Name]\n" +
                "\tFROM\n" +
                "\t\t[dbo].[tbl_Contact] AS [GroupChangedBy]\n" +
                "\tWHERE([tbl_RequestOffering].[GroupChangedByID] = [GroupChangedBy].[ID])) AS [GroupChangedBy],\n" +
                "\t[tbl_RequestOffering].[IsReadMeassage] AS [IsReadMeassage],\n" +
                "\t[tbl_Account].[SpecialMarginTypeID] AS [SpecialMarginTypeID],\n" +
                "\t[SMT].[Name] AS [SpecialMarginTypeName],\n" +
                "\t[tbl_RequestOffering].[StateID] AS [StateID],\n" +
                "\t[tbl_RequestOfferingState].[Name] AS [StateName]\n" +
                "FROM\n" +
                "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_StoreCity] AS [tbl_StoreCity] ON [tbl_StoreCity].[ID] = [tbl_RequestOffering].[StoreCityID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_Account] AS [tbl_Account] ON [tbl_Account].[ID] = [tbl_RequestOffering].[AccountID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_RequestOfferingStatus] AS [tbl_RequestOfferingStatus] ON [tbl_RequestOfferingStatus].[ID] = [tbl_RequestOffering].[StatusID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_Contact] AS [tbl_Contact] ON [tbl_Contact].[ID] = [tbl_RequestOffering].[CreatedByID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_Contact] AS [OfferingGroup] ON [OfferingGroup].[ID] = [tbl_RequestOffering].[OfferingGroupID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_SpecialMarginType] AS [SMT] ON [SMT].[ID] = [tbl_Account].[SpecialMarginTypeID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_RequestOfferingState] AS [tbl_RequestOfferingState] ON [tbl_RequestOfferingState].[ID] = [tbl_RequestOffering].[StateID]\n" +
                "WHERE " +
                "(( [tbl_RequestOffering].[CreatedByID] = '"+createdByID+"' OR\n" +
                "\t[tbl_RequestOffering].[OfferingGroupID] = '"+offeringGroupID+"' ) AND\n" +
                "\t[tbl_RequestOffering].[StatusID] = '{44C65A86-2DFA-4808-AADF-D7CFDEB11F3B}') AND\n" +
                "\t[tbl_RequestOffering].[ID] = '"+requestID+"'\n" +
                "ORDER BY\n" +
                "\t6 ASC");


        return query.toString();
    }


    public long getMainNotFulledCount(String createdByID, String offeringGroupID) {

        query = new StringBuilder();
        long count = 0;
        try {
            query.append("SELECT COUNT([tbl_Account].ID) AS [rowsCount] FROM tbl_Account\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_AccountType] AS [tbl_AccountType] ON [tbl_AccountType].[ID] = [tbl_Account].[AccountTypeID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_VATPayer] AS [tbl_VATPayer] ON [tbl_VATPayer].[ID] = [tbl_Account].[VATPayerID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Taxation] AS [tbl_Taxation] ON [tbl_Taxation].[ID] = [tbl_Account].[TaxationID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_ContractReceived] AS [tbl_ContractReceivedFOP] ON [tbl_ContractReceivedFOP].[ID] = [tbl_Account].[ContractFOPReceivedID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_ContractReceived] AS [tbl_ContractReceivedFOPRepair] ON [tbl_ContractReceivedFOPRepair].[ID] = [tbl_Account].[ContractFOPRepairReceivedID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_ContractReceived] AS [tbl_ContractReceivedLLC] ON [tbl_ContractReceivedLLC].[ID] = [tbl_Account].[ContractLLCReceivedID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_PaymentType] AS [tbl_PaymentType] ON [tbl_PaymentType].[ID] = [tbl_Account].[PaymentTypeID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_AccountProblem] AS [tbl_AccountProblem] ON [tbl_AccountProblem].[ID] = [tbl_Account].[AccountProblemID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_AccountMedal] AS [tbl_AccountMedal] ON [tbl_AccountMedal].[ID] = [tbl_Account].[AccountMedalID]\n" //+
        //            "WHERE " +
           //         "(( [tbl_RequestOffering].[CreatedByID] = '"+createdByID+"' OR\n" +
           //         "\t[tbl_RequestOffering].[OfferingGroupID] = '"+offeringGroupID+"' ) AND\n" +
               //     "\t[tbl_RequestOffering].[StatusID] = '{44C65A86-2DFA-4808-AADF-D7CFDEB11F3B}')"
            );


            ResultSet rs = DBConnection.getDataSource().getConnection().createStatement().executeQuery(query.toString());

            while (rs.next()) {
                count = rs.getLong("rowsCount");
            }

        } catch (SQLException e) {
            log.log(Level.SEVERE, "Pages count exception: " + e);  DBConnection database = new DBConnection();
            database.reconnect();
        }

        return count;
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

        return "DELETE FROM tbl_RequestOffering WHERE ID = ?";
    }



}
