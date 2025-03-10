package DTO;

import model.ExpenseCategory;
import model.IncomeCategory;
import java.time.LocalDate;

//가계부 데이터를 주고받기 위한 DTO (Data Transfer Object)
public class AccountBookDTO {
    private LocalDate date;
    private double amount;
    private IncomeCategory incomecategory;
    private ExpenseCategory expensecategory;
    
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public IncomeCategory getIncomecategory() {
		return incomecategory;
	}
	public void setIncomecategory(IncomeCategory incomecategory) {
		this.incomecategory = incomecategory;
	}
	public ExpenseCategory getExpensecategory() {
		return expensecategory;
	}
	public void setExpensecategory(ExpenseCategory expensecategory) {
		this.expensecategory = expensecategory;
	}
    
    
    
}

