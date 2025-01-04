package com.WeAre.BeatGenius.api.dto.responses.page;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageResponse<T> {
  private List<T> content;
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private boolean isLast;

  public static <T> PageResponse<T> from(Page<T> page) {
    PageResponse<T> response = new PageResponse<>();
    response.setContent(page.getContent());
    response.setPageNumber(page.getNumber());
    response.setPageSize(page.getSize());
    response.setTotalElements(page.getTotalElements());
    response.setTotalPages(page.getTotalPages());
    response.setLast(page.isLast());
    return response;
  }
}
