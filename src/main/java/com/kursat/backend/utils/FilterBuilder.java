package com.kursat.backend.utils;

import java.util.HashMap;
import java.util.Map;

public class FilterBuilder {
    private FilterBuilder(){} //Make it singleton.

    private static  Map<String,String> operationList = new HashMap<>();
    private static  Map<String,String> comparisionElementList = new HashMap<>();

    

    static{
        operationList.put("eq","=");
        operationList.put("like","LIKE");   

        comparisionElementList.put("name","name");
        comparisionElementList.put("age","age");   
    }



 


    

    public static  String getSQLFromFilter(String filter){

        try{
            if(filter == null){
                return "There is no filter string.";
            }

            String[] queryPart = filter.split(":");

            if(!comparisionElementList.containsKey(queryPart[0])){
                return "There is no such key element: " + queryPart[0];
            }

            if(!operationList.containsKey(queryPart[1])){
                return "There is no such comparator element: " + queryPart[1];
            }

            if(operationList.get(queryPart[1]).equals("LIKE")){
                return comparisionElementList.get(queryPart[0]) + " " + operationList.get(queryPart[1]) + " '%" + queryPart[2] + "%' ";
            }

            return comparisionElementList.get(queryPart[0]) + " " + operationList.get(queryPart[1]) + " " + queryPart[2];
        }catch(Exception e){
            return e.toString();
        }
    }

}
