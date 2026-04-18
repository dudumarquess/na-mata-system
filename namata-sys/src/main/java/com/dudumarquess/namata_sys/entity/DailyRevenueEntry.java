package com.dudumarquess.namata_sys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(
        name = "daily_revenue_entries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_revenue_month_day",
                        columnNames = {"monthly_record_id", "day_value"}
                )
        }
)
public class DailyRevenueEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "monthly_record_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_daily_revenue_monthly_record")
    )
    private MonthlyRecord monthlyRecord;

    @NotNull
    @Min(1)
    @Max(31)
    @Column(name = "day_value", nullable = false)
    private Integer day;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "cash_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal cashAmount;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "multibanco_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal multibancoAmount;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "apps_gross_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal appsGrossAmount;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "other_income_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal otherIncomeAmount;

    @NotNull
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    @Column(name = "app_fee_percentage_used", nullable = false, precision = 5, scale = 2)
    private BigDecimal appFeePercentageUsed;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "official_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal officialAmount;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "real_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal realAmount;

    public Long getId() {
        return id;
    }

    public MonthlyRecord getMonthlyRecord() {
        return monthlyRecord;
    }

    public void setMonthlyRecord(MonthlyRecord monthlyRecord) {
        this.monthlyRecord = monthlyRecord;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = DecimalScaleNormalizer.normalize(cashAmount);
    }

    public BigDecimal getMultibancoAmount() {
        return multibancoAmount;
    }

    public void setMultibancoAmount(BigDecimal multibancoAmount) {
        this.multibancoAmount = DecimalScaleNormalizer.normalize(multibancoAmount);
    }

    public BigDecimal getAppsGrossAmount() {
        return appsGrossAmount;
    }

    public void setAppsGrossAmount(BigDecimal appsGrossAmount) {
        this.appsGrossAmount = DecimalScaleNormalizer.normalize(appsGrossAmount);
    }

    public BigDecimal getOtherIncomeAmount() {
        return otherIncomeAmount;
    }

    public void setOtherIncomeAmount(BigDecimal otherIncomeAmount) {
        this.otherIncomeAmount = DecimalScaleNormalizer.normalize(otherIncomeAmount);
    }

    public BigDecimal getAppFeePercentageUsed() {
        return appFeePercentageUsed;
    }

    public void setAppFeePercentageUsed(BigDecimal appFeePercentageUsed) {
        this.appFeePercentageUsed = DecimalScaleNormalizer.normalize(appFeePercentageUsed);
    }

    public BigDecimal getOfficialAmount() {
        return officialAmount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void calculateOfficialAmount() {
        this.officialAmount = DecimalScaleNormalizer.normalize(
                this.cashAmount.add(this.multibancoAmount).add(this.appsGrossAmount).add(this.otherIncomeAmount)
        );
    }

    public void calculateRealAmount() {
        BigDecimal appFee = DecimalScaleNormalizer.normalize(
                this.appsGrossAmount.multiply(this.appFeePercentageUsed).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        );
        this.realAmount = DecimalScaleNormalizer.normalize(this.officialAmount.subtract(appFee));
    }

}
