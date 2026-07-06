package uz.script.wincrm.goods.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.goods.dto.GoodsGroupDTO;
import uz.script.wincrm.goods.response.GoodsGroupResponse;

import java.util.List;

public interface GoodsGroupService {

    GoodsGroupResponse create(GoodsGroupDTO dto);

    GoodsGroupResponse update(Long id, GoodsGroupDTO dto);

    void delete(Long id);

    GoodsGroupResponse getById(Long id);

    List<GoodsGroupResponse> getAll();

    Page<GoodsGroupResponse> getAll(Pageable pageable);

}