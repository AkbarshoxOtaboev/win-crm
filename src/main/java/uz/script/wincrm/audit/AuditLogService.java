package uz.script.wincrm.audit;

import java.util.List;

public interface AuditLogService {

    void save(AuditLog auditLog);
    List<AuditResponse> getAll();
}
