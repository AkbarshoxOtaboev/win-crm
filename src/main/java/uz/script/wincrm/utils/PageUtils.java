package uz.script.wincrm.utils;

import org.springframework.data.domain.Page;
import uz.script.wincrm.utils.response.PageInfo;
import uz.script.wincrm.utils.response.PageResponse;

public class PageUtils {

    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(
                        PageInfo.builder()
                                .size(page.getSize())
                                .number(page.getNumber())
                                .totalElements(page.getTotalElements())
                                .totalPages(page.getTotalPages())
                                .first(page.isFirst())
                                .last(page.isLast())
                                .hasNext(page.hasNext())
                                .hasPrevious(page.hasPrevious())
                                .build()
                )
                .build();
    }

    private PageUtils() {
    }
}