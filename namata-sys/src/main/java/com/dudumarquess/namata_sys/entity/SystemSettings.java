package com.dudumarquess.namata_sys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "system_settings")
public class SystemSettings extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    @Column(name = "default_app_fee_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal defaultAppFeePercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDefaultAppFeePercentage() {
        return defaultAppFeePercentage;
    }

    public void setDefaultAppFeePercentage(BigDecimal defaultAppFeePercentage) {
        this.defaultAppFeePercentage = DecimalScaleNormalizer.normalize(defaultAppFeePercentage);
    }

    @PrePersist
    @PreUpdate
    private void normalizeMonetaryFields() {
        this.defaultAppFeePercentage = DecimalScaleNormalizer.normalize(this.defaultAppFeePercentage);
    }
}

