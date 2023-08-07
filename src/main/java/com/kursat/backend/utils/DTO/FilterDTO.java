package com.kursat.backend.utils.DTO;

import java.util.ArrayList;
import java.util.List;

import com.kursat.backend.utils.FilterBuilder;



public class FilterDTO {

    private List<String> filter = new ArrayList<>();


    public void setFilter(List<String>  filter) {
        this.filter = filter;
    }

    public List<String>  getFilter() {
        return filter;
    }

    public String getFilterSQLQuery(){

        String SQLQuery = "";
        Integer count = 0;
        for (String query : filter) {
           
            SQLQuery += " " + FilterBuilder.getSQLFromFilter(query);
            if(count < filter.size()-1){
                SQLQuery += " AND ";
            }
            count++;
        }
        
        return SQLQuery;
    }

    @Override
    public String toString(){

        return filter.toString();
    }

}





