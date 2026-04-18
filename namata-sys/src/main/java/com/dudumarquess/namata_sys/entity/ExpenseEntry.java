package com.dudumarquess.namata_sys.entity;

import com.dudumarquess.namata_sys.entity.enums.ExpenseNature;
import com.dudumarquess.namata_sys.entity.enums.ExpenseSourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_entries")
public class ExpenseEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "monthly_record_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_expense_entry_monthly_record")
    )
    private MonthlyRecord monthlyRecord;

    @NotNull
    @Min(1)
    @Max(31)
    @Column(name = "day_value", nullable = false)
    private Integer day;

    @NotBlank
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "expense_nature", nullable = false, length = 20)
    private ExpenseNature expenseNature;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 30)
    private ExpenseSourceType sourceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fixed_expense_template_id",
            foreignKey = @ForeignKey(name = "fk_expense_entry_fixed_template")
    )
    private FixedExpenseTemplate fixedExpenseTemplate;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ExpenseNature getExpenseNature() {
        return expenseNature;
    }

    public void setExpenseNature(ExpenseNature expenseNature) {
        this.expenseNature = expenseNature;
    }

    public ExpenseSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(ExpenseSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public FixedExpenseTemplate getFixedExpenseTemplate() {
        return fixedExpenseTemplate;
    }

    public void setFixedExpenseTemplate(FixedExpenseTemplate fixedExpenseTemplate) {
        this.fixedExpenseTemplate = fixedExpenseTemplate;
    }
}

