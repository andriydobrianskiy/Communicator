package com.mainPage.createRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class OfferingsGroup {
    public HashMap<String, String> map = new HashMap<>();


    public HashMap<String, String> getMap() {
        map.put("113C960D-CD99-4161-AA8C-575D1E5BDDCE", "Група 1");
        map.put("E92CEF11-EA8B-4BBC-A244-7F82FA5F9BAC", "Група 2");
        map.put("4869AFC3-E32B-4732-B370-ECDAFA9EF185", "Група 3");
        map.put("1FA8112D-A9B4-47F3-A89F-0C101BE07709", "Група 4");
        map.put("DE516376-8A47-44DA-9EBB-5137F928EA05", "Група 5");
        map.put("D3B12AE2-3445-489E-BA7B-E731DE67ABB0", "Група 6");
        map.put("C2A6935C-0F10-4827-A789-9D733EDDFFB4", "Група 7");
        map.put("480ABDAD-8006-4D34-8862-803924357598", "Група 8");
        map.put("71820A9D-95B6-4D65-A480-4F2C57AE9A4B", "Група 9");
        map.put("FA7F12EE-E5FC-4DA3-92C2-51C93FFF6355", "Група 10");
        map.put("92C6E142-0BBA-40CE-8C99-861E9257AA90", "Група 11");
        map.put("DDC21C62-4289-4EF0-88FA-D2D2FE8F9934", "Група 12");
        map.put("67569753-AFC7-493C-8291-EECA91B285D4", "Група 13");

        return  map;
    }


    public String getValueByKey(String ID) {
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        Iterator<HashMap.Entry<String, String>> i = entrySet.iterator();
        while(i.hasNext()){
            Map.Entry<String, String> element = i.next();
            System.out.println("Key: "+element.getKey()+" ,value: "+element.getValue());
            element.getKey();
            element.getValue();
        }
        return ID;
    }
}
