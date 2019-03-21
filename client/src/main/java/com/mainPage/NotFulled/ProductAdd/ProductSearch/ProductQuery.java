package com.mainPage.NotFulled.ProductAdd.ProductSearch;

import com.connectDatabase.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductQuery implements Queries {
    private static Logger log = Logger.getLogger(ProductQuery.class.getName());

    private StringBuilder query;

    public String getMainProductSearch(Boolean top, int rowIndex) {

        query = new StringBuilder();
        query.append("SELECT \n");

        if (top) query.append(" TOP " + rowIndex + "\n");

        query.append("[tbl_Offering].[ID] AS [ID],\n" +
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
                "\t (SELECT\n" +
                "\t\tISNULL (SUM  ((CASE\n" +
                "\t WHEN\n" +
                "\t  [OIMSaleByPeriod].[OfferingMovementTypeID] = '{4D331506-28C7-4EEF-BC36-93D26E369120}'\n" +
                "\t THEN\n" +
                "\t  [OIMSaleByPeriod].[Quantity] * -1\n" +
                "\t ELSE\n" +
                "\t  [OIMSaleByPeriod].[Quantity]\n" +
                "\tEND)), 0) AS [Quantity]\n" +
                "\tFROM\n" +
                "\t\t[dbo].[tbl_OfferingInMovement] AS [OIMSaleByPeriod]\n" +
                "\tLEFT OUTER JOIN\n" +
                "\t\t[dbo].[tbl_OfferingMovement] AS [tbl_OfferingMovement] ON [tbl_OfferingMovement].[ID] = [OIMSaleByPeriod].[OfferingMovementID]\n" +
                "\tLEFT OUTER JOIN\n" +
                "\t\t[dbo].[vw_Store] AS [S] ON [S].[ID] = [tbl_OfferingMovement].[StoreID]\n" +
                "\tWHERE([OIMSaleByPeriod].[OfferingID] = [tbl_Offering].[ID] AND\n" +
                "\t\t([OIMSaleByPeriod].[CreatedOn] >= DATEADD(Year, -1, CURRENT_TIMESTAMP) AND\n" +
                "\t\t[OIMSaleByPeriod].[CreatedOn] <= CURRENT_TIMESTAMP AND\n" +
                "\t\t(OIMSaleByPeriod.OfferingMovementTypeID = '{F6E5A2E6-A2D6-4C2F-99A7-7FEE75FF76F6}' OR \n" +
                "\tOIMSaleByPeriod.OfferingMovementTypeID = '{4D331506-28C7-4EEF-BC36-93D26E369120}')) AND\n" +
                "\t\t(OIMSaleByPeriod.OfferingMovementTypeID = '{F6E5A2E6-A2D6-4C2F-99A7-7FEE75FF76F6}' OR \n" +
                "\t OIMSaleByPeriod.OfferingMovementTypeID = '{4D331506-28C7-4EEF-BC36-93D26E369120}') AND\n" +
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
                "\t[dbo].[tbl_OType] AS [OType] ON [OType].[ID] = [tbl_OfferingPriceSale].[OfferingTypeID] ");


        return query.toString();
    }



    public long getMainProductSearchCount() {

        query = new StringBuilder();
        long count = 0;
        try {


            query.append("SELECT COUNT([tbl_Offering].ID) AS [rowsCount] FROM tbl_Offering \n" +
                    "      LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_OfferingPriceSale] AS [tbl_OfferingPriceSale] ON [tbl_OfferingPriceSale].[OfferingID] = [tbl_Offering].[ID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_OType] AS [OType] ON [OType].[ID] = [tbl_OfferingPriceSale].[OfferingTypeID]");

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
        return null;
    }
}
