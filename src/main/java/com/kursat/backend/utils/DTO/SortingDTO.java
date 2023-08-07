package com.kursat.backend.utils.DTO;


public class SortingDTO {

    private String sort = "ASC";

/*
 * Lets to sort to be ASC or DES.
 */
    public void setSort(String sort) {
        if(sort.toUpperCase().equals("ASC") || sort.toUpperCase().equals("DES")){
            this.sort = sort.toUpperCase();
        }else{
            this.sort = "ASC";
        }
    }

    public String getSort() {
        return sort;
    }

    @Override
    public String toString(){
        return "Sort Type is: " + sort;
    }
}
