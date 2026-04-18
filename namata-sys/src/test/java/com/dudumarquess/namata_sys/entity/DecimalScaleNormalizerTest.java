package com.dudumarquess.namata_sys.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecimalScaleNormalizerTest {

    @Test
    void normalizeRoundsToTwoDecimalsUsingHalfUp() {
        assertEquals("10.01", DecimalScaleNormalizer.normalize(new BigDecimal("10.005")).toPlainString());
        assertEquals("10.02", DecimalScaleNormalizer.normalize(new BigDecimal("10.015")).toPlainString());
    }

    @Test
    void monetaryEntitySettersKeepTwoDecimalScale() {
        ExpenseEntry expenseEntry = new ExpenseEntry();
        expenseEntry.setAmount(new BigDecimal("12.345"));
        assertEquals("12.35", expenseEntry.getAmount().toPlainString());
        assertEquals(2, expenseEntry.getAmount().scale());

        FixedExpenseTemplate template = new FixedExpenseTemplate();
        template.setDefaultAmount(new BigDecimal("9.999"));
        assertEquals("10.00", template.getDefaultAmount().toPlainString());
        assertEquals(2, template.getDefaultAmount().scale());

        SystemSettings settings = new SystemSettings();
        settings.setDefaultAppFeePercentage(new BigDecimal("7.777"));
        assertEquals("7.78", settings.getDefaultAppFeePercentage().toPlainString());
        assertEquals(2, settings.getDefaultAppFeePercentage().scale());
    }

    @Test
    void dailyRevenueEntryCalculationsKeepTwoDecimalScale() {
        DailyRevenueEntry entry = new DailyRevenueEntry();
        entry.setCashAmount(new BigDecimal("10.005"));
        entry.setMultibancoAmount(new BigDecimal("20.333"));
        entry.setAppsGrossAmount(new BigDecimal("30.999"));
        entry.setAppFeePercentageUsed(new BigDecimal("2.345"));

        entry.calculateOfficialAmount();
        entry.calculateRealAmount();

        assertEquals("10.01", entry.getCashAmount().toPlainString());
        assertEquals("20.33", entry.getMultibancoAmount().toPlainString());
        assertEquals("31.00", entry.getAppsGrossAmount().toPlainString());
        assertEquals("2.35", entry.getAppFeePercentageUsed().toPlainString());
        assertEquals("61.34", entry.getOfficialAmount().toPlainString());
        assertEquals("60.61", entry.getRealAmount().toPlainString());
        assertEquals(2, entry.getOfficialAmount().scale());
        assertEquals(2, entry.getRealAmount().scale());
    }
}


