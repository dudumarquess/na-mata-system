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

    public void setId(Long id) {
        this.id = id;
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
        this.cashAmount = cashAmount;
    }

    public BigDecimal getMultibancoAmount() {
        return multibancoAmount;
    }

    public void setMultibancoAmount(BigDecimal multibancoAmount) {
        this.multibancoAmount = multibancoAmount;
    }

    public BigDecimal getAppsGrossAmount() {
        return appsGrossAmount;
    }

    public void setAppsGrossAmount(BigDecimal appsGrossAmount) {
        this.appsGrossAmount = appsGrossAmount;
    }

    public BigDecimal getAppFeePercentageUsed() {
        return appFeePercentageUsed;
    }

    public void setAppFeePercentageUsed(BigDecimal appFeePercentageUsed) {
        this.appFeePercentageUsed = appFeePercentageUsed;
    }

    public BigDecimal getOfficialAmount() {
        return officialAmount;
    }

    public void setOfficialAmount(BigDecimal officialAmount) {
        this.officialAmount = officialAmount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }
}

