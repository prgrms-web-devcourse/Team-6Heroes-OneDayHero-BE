package com.sixheroes.onedayhero;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CalculatorTest {

    @Test
    void addTest() {
        Calculator calculator = new Calculator();

        int result = calculator.add(2, 3);

        assertThat(result).isEqualTo(5);
    }
}