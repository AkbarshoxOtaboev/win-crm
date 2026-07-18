package uz.script.wincrm.inventory.service;

import uz.script.wincrm.inventory.request.StartInventoryCheckRequest;
import uz.script.wincrm.inventory.request.UpdateInventoryCheckItemRequest;
import uz.script.wincrm.inventory.response.InventoryCheckResponse;

import java.util.List;

public interface InventoryCheckService {

    InventoryCheckResponse findById(Long id);

    List<InventoryCheckResponse> fetchAll();

    List<InventoryCheckResponse> fetchByWarehouseId(Long warehouseId);

    /**
     * Berilgan ombor uchun yangi inventarizatsiya boshlaydi: shu ombordagi barcha
     * faol Stock qoldiqlarini "muzlatib" InventoryCheckItem sifatida saqlaydi.
     * Bitta ombor uchun bir vaqtning o'zida faqat bitta IN_PROGRESS inventarizatsiya bo'lishi mumkin.
     */
    InventoryCheckResponse start(StartInventoryCheckRequest request);

    /**
     * Jismonan sanalgan haqiqiy miqdorni kiritadi/yangilaydi va farqni qayta hisoblaydi.
     * Faqat IN_PROGRESS holatidagi inventarizatsiyada ishlaydi.
     */
    InventoryCheckResponse updateItem(Long inventoryCheckId, Long itemId, UpdateInventoryCheckItemRequest request);

    /**
     * Inventarizatsiyani tasdiqlaydi: har bir qator bo'yicha farq mavjud bo'lsa,
     * Stock qoldig'ini haqiqiy miqdorga moslab yangilaydi (StockService.increaseStock/decreaseStock
     * orqali) va shu bilan birga StockHistory'ga tuzatuv yozuvi avtomatik qo'shiladi.
     * Barcha qatorlar sanalgan (actualCount kiritilgan) bo'lishi shart, aks holda xatolik qaytadi.
     */
    InventoryCheckResponse confirm(Long inventoryCheckId);

    /**
     * Inventarizatsiyani bekor qiladi. Stockga hech qanday ta'sir qilmaydi.
     */
    InventoryCheckResponse cancel(Long inventoryCheckId);

    void delete(Long id);
}