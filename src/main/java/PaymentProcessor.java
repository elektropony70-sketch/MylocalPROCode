public class PaymentProcessor {
    private final AccountRegistry registry;
    private final SecurityAlertSystem alertSystem;
    private final double MAX_LIMIT = 500.0;

    public PaymentProcessor(AccountRegistry registry, SecurityAlertSystem alertSystem) {
        this.registry = registry;
        this.alertSystem = alertSystem;
    }

    public boolean processPayment(String accountId, double amount) {
        // 1. Проверяем, активен ли аккаунт
        if (!registry.isAccountActive(accountId)) {
            return false;
        }

        // 2. Проверяем превышение лимита в 500.0
        if (amount > MAX_LIMIT) {
            alertSystem.reportSuspiciousActivity(accountId, amount);
            return false;
        }

        // 3. Если аккаунт активен и лимит не превышен — платеж успешен
        return true;
    }
}