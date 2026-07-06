package uz.script.wincrm.goods.service;

import uz.script.wincrm.goods.dto.GoodsDTO;
import uz.script.wincrm.goods.response.GoodsResponse;

import java.util.List;

public interface GoodsService {
    boolean existsByBarcode(String barcode);
    GoodsResponse create(GoodsDTO dto);
    GoodsResponse findById(Long id);
    List<GoodsResponse> fetchAllGoods();
    GoodsResponse update(Long id, GoodsDTO dto);
    void delete(Long id);
}