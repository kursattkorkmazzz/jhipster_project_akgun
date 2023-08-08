package com.kursat.backend.utils.DTO;


public class SortingDTO {

    private String sort = "id:asc";

/*
 * Lets to sort to be ASC or DES.
 */
    public void setSort(String sort) {
       
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    public String getSortBy() {
         try{
            String[] sorting = sort.split(":");
            return sorting[0];
            
        }catch(Exception e){
            return "id";
        }
    }

    public String getSortType() {
         try{
            String[] sorting = sort.split(":");
            return sorting[1];
            
        }catch(Exception e){
            return "asc";
        }
    }

    @Override
    public String toString(){
        return "Sort is: " + sort;
    }
}
