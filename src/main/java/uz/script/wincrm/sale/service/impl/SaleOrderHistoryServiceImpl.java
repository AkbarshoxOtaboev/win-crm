package uz.script.wincrm.sale.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.SaleOrderHistory;
import uz.script.wincrm.sale.dto.SaleOrderHistoryDTO;
import uz.script.wincrm.sale.mapper.SaleOrderHistoryMapper;
import uz.script.wincrm.sale.repository.SaleOrderHistoryRepository;
import uz.script.wincrm.sale.repository.SaleOrderRepository;
import uz.script.wincrm.sale.response.SaleOrderHistoryResponse;
import uz.script.wincrm.sale.service.SaleOrderHistoryService;
import uz.script.wincrm.users.User;
import uz.script.wincrm.users.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleOrderHistoryServiceImpl implements SaleOrderHistoryService {

    private final SaleOrderHistoryRepository repository;
    private final SaleOrderRepository saleOrderRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SaleOrderHistoryResponse> fetchBySaleOrderId(Long saleOrderId) {
        if (!saleOrderRepository.existsById(saleOrderId)) {
            throw new ResourceNotFoundException("Sale order not found with id: " + saleOrderId);
        }

        return repository.findBySaleOrder_IdOrderByCreatedAtAsc(saleOrderId)
                .stream()
                .map(SaleOrderHistoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void recordHistory(SaleOrderHistoryDTO dto) {
        SaleOrder saleOrder = saleOrderRepository.findById(dto.getSaleOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sale order not found with id: " + dto.getSaleOrderId()));

        SaleOrderHistory history = SaleOrderHistory.builder()
                .saleOrder(saleOrder)
                .fromStatus(dto.getFromStatus())
                .toStatus(dto.getToStatus())
                .changedByUser(resolveCurrentUser())
                .comment(dto.getComment())
                .build();

        repository.save(history);
    }

    /**
     * Joriy autentifikatsiyadan o'tgan foydalanuvchini qaytaradi.
     * Agar authentication mavjud bo'lmasa (masalan tizim/scheduled job tomonidan
     * chaqirilsa) - xatolik tashlamay, shunchaki null qaytaradi.
     */
    private User resolveCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }
}