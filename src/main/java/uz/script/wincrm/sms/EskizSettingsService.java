package uz.script.wincrm.sms;

public interface EskizSettingsService {
    EskizSettingsResponse getSettings();
    EskizSettingsResponse saveCredentials(EskizCredentialsDto dto);
}