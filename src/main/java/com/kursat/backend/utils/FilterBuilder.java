package com.kursat.backend.utils;

import java.util.HashMap;
import java.util.Map;

public class FilterBuilder {
    private FilterBuilder(){} //Make it singleton.

    private static  Map<String,String> operationList = new HashMap<>();

    static{
        operationList.put("eq","=");
        operationList.put("like","LIKE");
        operationList.put("gr",">");
        operationList.put("gt",">=");   
        operationList.put("le","<");  
        operationList.put("lt","<=");  
    }


    public static  String getSQLFromFilter(String filter){

        try{
            if(filter == null){
                return "There is no filter string.";
            }

            String[] queryPart = filter.split(":");

            if(queryPart[0].equals(null)){
                return "There is no key element. ";
            }
            if(!operationList.containsKey(queryPart[1])){
                return "There is no such comparator element: " + queryPart[1];
            }

            if(operationList.get(queryPart[1]).equals("LIKE")){
                return " LOWER("+queryPart[0]+") " + operationList.get(queryPart[1]) + " '%" + queryPart[2] + "%' ";
            }

            return queryPart[0] + " " + operationList.get(queryPart[1]) + " " + queryPart[2];
        }catch(Exception e){
            return e.toString();
        }
    }

}
