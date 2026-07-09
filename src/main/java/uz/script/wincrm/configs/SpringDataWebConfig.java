package uz.script.wincrm.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

/**
 * Page<T> obyektlarini to'g'ridan-to'g'ri JSON qilib qaytarish
 * ("Serializing PageImpl instances as-is is not supported") ogohlantirishini
 * bartaraf etadi. VIA_DTO rejimi Page'ni barqaror PagedModel strukturasiga
 * (content, page: {size, number, totalElements, totalPages}) o'giradi.
 * Controller'larda hech narsa o'zgartirish shart emas.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class SpringDataWebConfig {
}