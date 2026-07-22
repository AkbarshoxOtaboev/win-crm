package uz.script.wincrm.payment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import uz.script.wincrm.utils.response.PageResponse;

/**
 * PageResponse<PaymentResponse> uchun faqat Swagger schema generatsiyasi maqsadida
 * yaratilgan konkret (generic bo'lmagan) subclass.
 * <p>
 * PageResponse<T> generic bo'lgani sababli, Swagger "content" maydonining haqiqiy
 * elementi (T) ni aniqlay olmaydi va uni "string" deb ko'rsatadi. Ushbu klass orqali
 * T = PaymentResponse sifatida qattiq belgilanadi, shunda Springdoc "content" massivi
 * ichida PaymentResponse obyektlari qaytishini to'g'ri chiqaradi.
 * <p>
 * E'TIBOR: bu klass faqat @Schema(implementation = ...) uchun ishlatiladi — kod ichida
 * haqiqiy qaytish turi hamon PageResponse<PaymentResponse> bo'lib qoladi (PageUtils.from orqali).
 */
@Schema(name = "Payment Page Response", description = "Sahifalangan to'lovlar ro'yxati")
public class PaymentPageResponse extends PageResponse<PaymentResponse> {
}