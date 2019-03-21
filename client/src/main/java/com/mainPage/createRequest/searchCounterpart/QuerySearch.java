package com.mainPage.createRequest.searchCounterpart;

public class QuerySearch//implements Queries
 {
  /*  private static Logger log = Logger.getLogger(QuerySearch.class.getName());

    private static StringBuilder query;

    public String getMainAccount(Boolean top, int rowIndex) {

        query = new StringBuilder();
        query.append("SELECT \n");

        if(top) query.append(" TOP " + rowIndex + "\n");

        query.append( "[tbl_Offering].[ID] AS [ID],\n" +
                "\t[tbl_Offering].[ID] AS [OfferingID],\n" +
                "\t[tbl_Offering].[Name] AS [OfferingName],\n" +
                "\t[tbl_Offering].[CustomsCode] AS [CustomsCode],\n" +
                "\t[tbl_Offering].[Index] AS [Index],\n" +
                "\t[tbl_Offering].[Skrut] AS [Skrut],\n" +
                "\t(CASE\n" +
                "    WHEN ISNULL(HiddenSkrut,0) = 0 THEN tbl_Offering.Skrut\n" +
                "  ELSE ''\n" +
                "END) AS [IsHiddenSkrut],\n" +
                "\t(SELECT TOP 1\n" +
                "\t\t[tbl_OfferingPhoto].[OfferingPhoto] AS [OfferingPhoto]\n" +
                "\tFROM\n" +
                "\t\t[dbo].[tbl_OfferingPhoto] AS [tbl_OfferingPhoto]\n" +
                "\tWHERE([tbl_Offering].[ID] = [tbl_OfferingPhoto].[OfferingID])) AS [OfferingPhoto],\n" +
                "\t(SELECT\n" +
                "\t\tSUM((CASE\n" +
                "\t WHEN\n" +
                "\t  [OIMSaleByPeriod].[OfferingMovementTypeID] = '{4D331506-28C7-4EEF-BC36-93D26E369120}'\n" +
                "\t THEN\n" +
                "\t  [OIMSaleByPeriod].[Quantity] * -1\n" +
                "\t ELSE\n" +
                "\t  [OIMSaleByPeriod].[Quantity]\n" +
                "\tEND)) AS [Quantity]\n" +
                "\tFROM\n" +
                "\t\t[dbo].[tbl_OfferingInMovement] AS [OIMSaleByPeriod]\n" +
                "\tLEFT OUTER JOIN\n" +
                "\t\t[dbo].[tbl_OfferingMovement] AS [tbl_OfferingMovement] ON [tbl_OfferingMovement].[ID] = [OIMSaleByPeriod].[OfferingMovementID]\n" +
                "\tLEFT OUTER JOIN\n" +
                "\t\t[dbo].[tbl_Store] AS [S] ON [S].[ID] = [tbl_OfferingMovement].[StoreID]\n" +
                "\tWHERE([OIMSaleByPeriod].[OfferingID] = [tbl_Offering].[ID] AND\n" +
                "\t\t([OIMSaleByPeriod].[CreatedOn] >= DATEADD(Year, -1, CURRENT_TIMESTAMP) AND\n" +
                "\t\t[OIMSaleByPeriod].[CreatedOn] <= CURRENT_TIMESTAMP AND\n" +
                "\t\t(OIMSaleByPeriod.OfferingMovementTypeID = '{F6E5A2E6-A2D6-4C2F-99A7-7FEE75FF76F6}' OR \n" +
                "\tOIMSaleByPeriod.OfferingMovementTypeID = '{4D331506-28C7-4EEF-BC36-93D26E369120}')) AND\n" +
                "\t\t(OIMSaleByPeriod.OfferingMovementTypeID = '{F6E5A2E6-A2D6-4C2F-99A7-7FEE75FF76F6}' OR \n" +
                "\tOIMSaleByPeriod.OfferingMovementTypeID = '{4D331506-28C7-4EEF-BC36-93D26E369120}') AND\n" +
                "\t\tNOT [tbl_OfferingMovement].[StoreID] IN \n" +
                "\t('{A4DA5D81-B080-41FB-B0B9-B1F6AF1F5B0F}', \n" +
                "\t '{D5539755-B5AE-4507-B6EA-EC77619AE362}', \n" +
                "\t '{6414FB4E-08F0-4B6C-A760-EF7BA15D476C}') AND\n" +
                "\t\t[S].[IsActive] = 1)) AS [SaledByYear],\n" +
                "\t[tbl_Offering].[SeasonIndex] AS [SeasonIndex],\n" +
                "\t[tbl_OfferingPriceSale].[OfferingTypeID] AS [OfferingTypeID],\n" +
                "\t[OType].[Name] AS [OfferingTypeName],\n" +
                "\t[tbl_Offering].[OfferingBrandCode] AS [OfferingBrandCode],\n" +
                "\t\t(\tSELECT ISNULL(XX.QuantityRemain,0) - ISNULL(XX.Res,0)\n" +
                "\t\tFROM\t(\n" +
                "\t\t\t\tSELECT \n" +
                "\t\t\t\t\tX.QuantityRemain, \n" +
                "\t\t\t\t\t(SELECT\n" +
                "\t\t\t\t\tISNULL(SUM([tbl_ReservedOffering].[Quantity]),0) AS [Quantity]\n" +
                "\t\t\t\t\tFROM\n" +
                "\t\t\t\t\t[dbo].[tbl_ReservedOffering] AS [tbl_ReservedOffering]\n" +
                "\t\t\t\t\tWHERE([tbl_Offering].[ID] = [tbl_ReservedOffering].[OfferingID])) AS Res\n" +
                "\t\t\t\tFROM(\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\tSELECT\n" +
                "\t\t\t\t\t\tISNULL(SUM([oimMvm].[Quantity]),0) AS [QuantityRemain]\n" +
                "\t\t\t\t\t\tFROM\n" +
                "\t\t\t\t\t\t[dbo].[tbl_MovementInOM] AS [oimMvm]\n" +
                "\t\t\t\t\t\tWHERE([oimMvm].[OfferingID] = [tbl_Offering].[ID] AND\n" +
                "\t\t\t\t\t\tNOT [oimMvm].[StoreID] IN ('{103D67D3-7011-4FEE-B757-BE464B20B9C7}'))) AS X\n" +
                "\t\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\n" +
                "\t\t\t\t)\tAS  XX \n" +
                "\t) AS [Available]\n" +
                "FROM\n" +
                "\t[dbo].[tbl_Offering] AS [tbl_Offering]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_OfferingPriceSale] AS [tbl_OfferingPriceSale] ON [tbl_OfferingPriceSale].[OfferingID] = [tbl_Offering].[ID]\n" +
                "LEFT OUTER JOIN\n" +
                "\t[dbo].[tbl_OType] AS [OType] ON [OType].[ID] = [tbl_OfferingPriceSale].[OfferingTypeID]");
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
    }*/

}
