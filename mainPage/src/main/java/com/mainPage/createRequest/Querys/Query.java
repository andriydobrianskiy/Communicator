package com.mainPage.createRequest.Querys;

import com.connectDatabase.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Query implements Queries{
        private static Logger log = Logger.getLogger(Query.class.getName());

        private static StringBuilder query;

    public String getMainAccount(Boolean top, int rowIndex) {

        query = new StringBuilder();
        query.append("SELECT \n");

        if(top) query.append(" TOP " + rowIndex + "\n");

        query.append( "[tbl_Account].[ID] AS [ID], \n" +
                "                ISNULL([tbl_Account].[Name], '') AS [Name], \n" +
                "                ISNULL([tbl_Account].[Code], 0) AS [Code], \n" +
                "                [tbl_Account].[AccountCashboxID] AS [AccountCashboxID], \n" +
                "                ISNULL([tbl_AccountCashbox].[Identifier], '') AS [AccountCashboxName], \n" +
                "                CASE WHEN [tbl_Account].[UnblockDate] > GETDATE() THEN \n" +
                "                (SELECT TOP 1 \n" +
                "                N'Не солидный' AS [Exist] \n" +
                "                FROM \n" +
                "                tbl_Cashflow AS [Cashflow] \n" +
                "                WHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND \n" +
                "                [Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND \n" +
                "                [Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND \n" +
                "                                [Cashflow].[DestinationID] <> '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}'/*Advance*/ AND  \n" +
                "                ([Cashflow].[PayDate] < (CAST(FLOOR(CAST(GETDATE() AS FLOAT)) AS DATETIME))))  \n" +
                "                                   AND [tbl_Account].[UnblockDate] < GETDATE())) \n" +
                "                ELSE  \n" +
                "                (SELECT TOP 1 \n" +
                "                N'Не солидный' AS [Exist] \n" +
                "                FROM \n" +
                "                tbl_Cashflow AS [Cashflow] \n" +
                "                WHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND \n" +
                "                [Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND \n" +
                "                [Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND \n" +
                "                                [Cashflow].[DestinationID] <> '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}'/*Advance*/ AND \n" +
                "                (CAST([Cashflow].[PayDate] AS DATE) < CAST(CURRENT_TIMESTAMP AS DATE))))) \n" +
                "                END AS [IsSolid], \n" +
                "                [tbl_AccountCashbox].[CompanyIdentifier] AS [CompanyIdentifier], \n" +
                "                [tbl_Account].[AccountStateID] AS [AccountStateID], \n" +
                "                [tbl_AccountState].[Name] AS [AccountStateName], \n" +
                "                [tbl_Account].[AccountMedalID] AS [AccountMedalID], \n" +
                "                [tbl_AccountMedal].[Name] AS [AccountMedalName], \n" +
                "                [tbl_Account].[ActivitiesID] AS [ActivitiesID], \n" +
                "                [tbl_Activities].[Name] AS [ActivitiesName] \n" +
                "                FROM \n" +
                "                [dbo].[tbl_Account] AS [tbl_Account] \n" +
                "                LEFT OUTER JOIN \n" +
                "                [dbo].[vw_AccountCashbox] AS [tbl_AccountCashbox] ON [tbl_AccountCashbox].[ID] = [tbl_Account].[AccountCashboxID] \n" +
                "                LEFT OUTER JOIN \n" +
                "                [dbo].[tbl_AccountState] AS [tbl_AccountState] ON [tbl_AccountState].[ID] = [tbl_Account].[AccountStateID] \n" +
                "                LEFT OUTER JOIN \n" +
                "                [dbo].[tbl_AccountMedal] AS [tbl_AccountMedal] ON [tbl_AccountMedal].[ID] = [tbl_Account].[AccountMedalID] \n" +
                "                LEFT OUTER JOIN \n" +
                "                [dbo].[tbl_Activities] AS [tbl_Activities] ON [tbl_Activities].[ID] = [tbl_Account].[ActivitiesID]\n" +
                "\n");
        //"WHERE [tbl_Account].[AccountTypeID] <> '{12B90989-C863-42D4-A90B-DDD6024A7FED}' AND\n" +
        //"\t[tbl_Account].[AccountTypeID] <> '{68C86617-704F-49A1-934E-8D2252A20AEC}'");



        return query.toString();
    }





    public long getMainAccountCount() {

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
                    "\t[dbo].[tbl_AccountMedal] AS [tbl_AccountMedal] ON [tbl_AccountMedal].[ID] = [tbl_Account].[AccountMedalID]\n");
            //"\tWHERE(([tbl_Account].[AccountTypeID] <> '{12B90989-C863-42D4-A90B-DDD6024A7FED}' AND\n" +
            //"\t[tbl_Account].[AccountTypeID] <> '{68C86617-704F-49A1-934E-8D2252A20AEC}'))");

            ResultSet rs = DBConnection.getDataSource().getConnection().createStatement().executeQuery(query.toString());

            while(rs.next())
            {
                count = rs.getLong("rowsCount");
            }

        } catch (SQLException e) {
            log.log( Level.SEVERE, "Pages count exception: " + e);
        }

        return count;
    }






    @Override
    public String insertQuery() {
        query = new StringBuilder();

        query.append("\n" +
                "    INSERT INTO [tbl_Account]\n" +
                "\t(\n" +
                "[CreatedOn],\n" +
                "[ModifiedOn],\n" +
                "[CreatedByID],\n" +
                "[ModifiedByID],\n" +
                "[Name],\n" +
                "[OfficialAccountName],\n" +
                "[PaymentTypeID],\n" +
                "[BankInvoice],\n" +
                "[OwnerID],\n" +
                "[Communication1],\n" +
                "[Communication2],\n" +
                "[Communication3],\n" +
                "[Communication4],\n" +
                "[Communication5],\n" +
                "[AccountTypeID],\n" +
                "[Communication1TypeID],\n" +
                "[Communication2TypeID],\n" +
                "[Communication3TypeID],\n" +
                "[Communication4TypeID],\n" +
                "[Communication5TypeID],\n" +
                "[EvidenceRegistryNumber],\n" +
                "[IndividualOrdinalNumber],\n" +
                "[VATPayerID],\n" +
                "[EvidenceDocument],\n" +
                "[TaxationID],\n" +
                "[AccountDiscount],\n" +
                "[Delay],\n" +
                "[Limit],\n" +
                "[BankName],\n" +
                "[ContractFOPRepairFrom],\n" +
                "[ContractFOPRepairNumber],\n" +
                "[ContractFOPRepairReceivedID],\n" +
                "[ContractFOPFrom],\n" +
                "[ContractFOPNumber],\n" +
                "[ContractFOPReceivedID],\n" +
                "[ContractLLCFrom],\n" +
                "[ContractLLCNumber],\n" +
                "[ContractLLCReceivedID],\n" +
                "[ContractLLCTTFrom],\n" +
                "[ContractLLCTTNumber],\n" +
                "[ContractLLCTTReceivedID],\n" +
                "[SpecialMarginTypeID],\n" +
                "[AccountProblemDate],\n" +
                "[AccountProblemID],\n" +
                "[OwnerManagerID],\n" +
                "[DiscountSTO],\n" +
                "[AccountMedalID],\n" +
                "[Remark],\n" +
                "[Street],\n" +
                "[Locality],\n" +
                "[District],\n" +
                "[Region],\n" +
                "[ZIPCode],\n" +
                "[PromiceSel],\n" +
                "[CreditSel],\n" +
                "[SaldoSel],\n" +
                "[BranchCode]\n" +
                "\t) VALUES (\n" +
                "CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,?,?,?,?,?,?,?,?,\n" +
                "?,?,?,?,?,?,?,?,?,?,\n" +
                "?,?,?,?,?,?,?,?,?,?,\n" +
                "?,?,?,?,?,?,?,?,?,?,\n" +
                "?,?,?,?,?,?,?,?,?,?,\n" +
                "?,?,?,?,?" +
                "    )\n");

        return query.toString();
    }
    @Override
    public String updateQuery() {
        query = new StringBuilder();

        query.append("UPDATE [tbl_Account] " +
                "SET " +
                "[CreatedOn] = ?,\n" +
                "[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                "[CreatedByID] = ?,\n" +
                "[ModifiedByID] = ?,\n" +
                "[Name] = ?,\n" +
                "[OfficialAccountName] = ?,\n" +
                "[PaymentTypeID] = ?,\n" +
                "[BankInvoice] = ?,\n" +
                "[OwnerID] = ?,\n" +
                "[Communication1] = ?,\n" +
                "[Communication2] = ?,\n" +
                "[Communication3] = ?,\n" +
                "[Communication4] = ?,\n" +
                "[Communication5] = ?,\n" +
                "[AccountTypeID] = ?,\n" +
                "[Communication1TypeID] = ?,\n" +
                "[Communication2TypeID] = ?,\n" +
                "[Communication3TypeID] = ?,\n" +
                "[Communication4TypeID] = ?,\n" +
                "[Communication5TypeID] = ?,\n" +
                "[EvidenceRegistryNumber] = ?,\n" +
                "[IndividualOrdinalNumber] = ?,\n" +
                "[VATPayerID] = ?,\n" +
                "[EvidenceDocument] = ?,\n" +
                "[TaxationID] = ?,\n" +
                "[AccountDiscount] = ?,\n" +
                "[Delay] = ?,\n" +
                "[Limit] = ?,\n" +
                "[BankName] = ?,\n" +
                "[ContractFOPRepairFrom] = ?,\n" +
                "[ContractFOPRepairNumber] = ?,\n" +
                "[ContractFOPRepairReceivedID] = ?,\n" +
                "[ContractFOPFrom] = ?,\n" +
                "[ContractFOPNumber] = ?,\n" +
                "[ContractFOPReceivedID] = ?,\n" +
                "[ContractLLCFrom] = ?,\n" +
                "[ContractLLCNumber] = ?,\n" +
                "[ContractLLCReceivedID] = ?,\n" +
                "[ContractLLCTTFrom] = ?,\n" +
                "[ContractLLCTTNumber] = ?,\n" +
                "[ContractLLCTTReceivedID] = ?,\n" +
                "[SpecialMarginTypeID] = ?,\n" +
                "[AccountProblemDate] = ?,\n" +
                "[AccountProblemID] = ?,\n" +
                "[OwnerManagerID] = ?,\n" +
                "[DiscountSTO] = ?,\n" +
                "[AccountMedalID] = ?,\n" +
                "[Remark] = ?,\n" +
                "[Street] = ?,\n" +
                "[Locality] = ?,\n" +
                "[District] = ?,\n" +
                "[Region] = ?,\n" +
                "[ZIPCode] = ?,\n" +
                "[PromiceSel] = ?,\n" +
                "[CreditSel] = ?,\n" +
                "[SaldoSel] = ?,\n" +
                "[BranchCode] = ?\n" +
                "   WHERE ID = ?");

        return query.toString();
    }


    @Override
    public String deleteQuery() {
        return "DELETE FROM [tbl_Account] WHERE ID = ?";
    }

}
