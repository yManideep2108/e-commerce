package com.ecommerce.manideep.sb.ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private List<CategoryDTO> content ;
    private Integer pageNumber ;
    private Integer pageSize ;
    private Long  totalElements ;
    private Integer totalPage ;
    private Boolean lastPage ;
}
