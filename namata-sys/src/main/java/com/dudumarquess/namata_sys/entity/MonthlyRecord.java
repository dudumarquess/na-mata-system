package com.dudumarquess.namata_sys.entity;

import com.dudumarquess.namata_sys.entity.enums.MonthStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "monthly_records",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_monthly_record_year_month",
                        columnNames = {"year_value", "month_value"}
                )
        }
)
public class MonthlyRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(2000)
    @Column(name = "year_value", nullable = false)
    private Integer year;

    @NotNull
    @Min(1)
    @Max(12)
    @Column(name = "month_value", nullable = false)
    private Integer month;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MonthStatus status;

    @OneToMany(mappedBy = "monthlyRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyRevenueEntry> dailyRevenueEntries = new ArrayList<>();

    @OneToMany(mappedBy = "monthlyRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntry> expenseEntries = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public MonthStatus getStatus() {
        return status;
    }

    public void setStatus(MonthStatus status) {
        this.status = status;
    }

    public List<DailyRevenueEntry> getDailyRevenueEntries() {
        return dailyRevenueEntries;
    }

    public void setDailyRevenueEntries(List<DailyRevenueEntry> dailyRevenueEntries) {
        this.dailyRevenueEntries = dailyRevenueEntries;
    }

    public List<ExpenseEntry> getExpenseEntries() {
        return expenseEntries;
    }

    public void setExpenseEntries(List<ExpenseEntry> expenseEntries) {
        this.expenseEntries = expenseEntries;
    }
}

