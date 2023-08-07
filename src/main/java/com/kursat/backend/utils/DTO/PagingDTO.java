package com.kursat.backend.utils.DTO;

public class PagingDTO {
    private Integer page = 0;
    private Integer size = 3;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String toString(){
        return "Page: " + page + "\nSize: " + size;
    }

}

