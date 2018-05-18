package com.mainPage.NotFulled.ProductAdd.ProductSearch;

import com.Utils.SearchType;

public enum ProductSearchType  implements SearchType {
    INDEX("Індекс"),
    ISHIDDENSKRUT("Скорочення"),
    SKRUT("Скрут"),
    OFFERINGNAME("Назва продукта (UA)");
   /* HASANALOGUE("Аналоги"),
    DEFAULTOFFERINGCODE("Код постачальника"),
    OFFERINGTYPEID("Тип"),
    SUPLIERID ("Постачальник"),
    OWNERID ("Відповідальний"),
    NAMEPOLISH("Назва продукту (INVOICE)"),
    REMARK("Зауваження"),
    OFFERINGVENDORID("Виробник 1"),
    OFFERINGVENDORNAME("Виробник 2"),
    PRICENOTFROMEUROID("Ціна не від євро"),
    CUSTOMSCODE("Митний код"),
    OFFERINGANALOGUE("Аналог"),
    NAMEWMD("Назва продукту (WMD)"),
    CLEANOFFERINGCODE("Чистий код"),
    OFFERINGPHOTO("Фото товару"),
    HIDDENSKRUT("Прихований скрут"),
    SEASONINDEX("Індекс сезону");*/


    private String label;

    ProductSearchType(String label) {
        this.label = label;
    }

    public String getSearchType() {
        return label;
    }

    public String toString() {
        return label;
    }
}
