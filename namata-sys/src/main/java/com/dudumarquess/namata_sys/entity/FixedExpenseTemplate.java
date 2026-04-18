package com.dudumarquess.namata_sys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "fixed_expense_templates")
public class FixedExpenseTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "default_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal defaultAmount;

    @NotNull
    @Min(1)
    @Max(31)
    @Column(name = "default_day", nullable = false)
    private Integer defaultDay;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @NotNull
    @Column(name = "auto_launch_enabled", nullable = false)
    private Boolean autoLaunchEnabled = Boolean.TRUE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public Integer getDefaultDay() {
        return defaultDay;
    }

    public void setDefaultDay(Integer defaultDay) {
        this.defaultDay = defaultDay;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getAutoLaunchEnabled() {
        return autoLaunchEnabled;
    }

    public void setAutoLaunchEnabled(Boolean autoLaunchEnabled) {
        this.autoLaunchEnabled = autoLaunchEnabled;
    }
}

