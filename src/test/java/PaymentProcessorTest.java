import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;



public class PaymentProcessorTest {

    private AccountRegistry registryMock;
    private SecurityAlertSystem alertSystemMock;
    private PaymentProcessor processor;

    @BeforeEach
    void setUp() {
        registryMock = mock(AccountRegistry.class);
        alertSystemMock = mock(SecurityAlertSystem.class);

        processor = new PaymentProcessor(registryMock, alertSystemMock);
    }

    @Test
    void shouldApprovePayment_WhenAccountIsActiveAndUnderLimit() {
        //  Тест 1. Успешный платеж.
        // Подсказка: используй when(...).thenReturn(...) для настройки мока
        when(registryMock.isAccountActive("babels")).thenReturn(true);

        boolean result = processor.processPayment("babels", 450);

        assertThat(result).isTrue();

        verifyNoInteractions (alertSystemMock);
    }

    @Test
    void shouldRejectPayment_WhenAccountIsInactive() {
        //  Тест 2. Неактивный аккаунт.
        when(registryMock.isAccountActive("blocked-user")).thenReturn(false);

        boolean result = processor.processPayment ("blocked-user",50);

        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectPaymentAndAlertSecurity_WhenAmountExceedsLimit() {
        //  Тест 3. Превышение лимита + проверка вызова метода через verify(...)

        when(registryMock.isAccountActive("hacker")).thenReturn(true);

        boolean result = processor.processPayment("hacker",700);

        assertThat(result).isFalse();

        verify(alertSystemMock,times (1)).reportSuspiciousActivity("hacker",700);
    }
}
