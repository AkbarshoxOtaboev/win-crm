package uz.script.wincrm.utils.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageInfo {

    private int size;

    private int number;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;

    private boolean hasNext;

    private boolean hasPrevious;
}