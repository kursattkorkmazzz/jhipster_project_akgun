package com.kursat.backend.repository;
import java.util.List;
import com.kursat.backend.domain.Applicants;
import com.kursat.backend.utils.DTO.FilterDTO;
import com.kursat.backend.utils.DTO.PagingDTO;
import com.kursat.backend.utils.DTO.SortingDTO;

interface CustomizedApplicantsRepository {
    List<Applicants> runSQL(String SQLquery);
    public String getPaginationSQLQuery(PagingDTO paging);
    public String getSortingSQLQuery(SortingDTO sorting);
    public String getFilterSQLQuery(FilterDTO filter);
}
