package uz.script.wincrm.telegram.session;

public enum BotConversationState {
    AWAITING_PHONE,      // /start bosilgan, telefon raqami kutilyapti
    MAIN_MENU,           // ro'yxatdan o'tgan, asosiy menyu ko'rsatilgan
    AWAITING_ORDER_FOR_AKT // "Akt sverka" tanlangan, buyurtma tanlanishi kutilyapti
}