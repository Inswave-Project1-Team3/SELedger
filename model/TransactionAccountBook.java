package model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAccountBook {

	private long dayAccountBookId;
	private boolean benefitCheck;
	private LocalDate createTime;
	private LocalDate updateTime;
	private IncomeCategory incomeCategory;
	private ExpenseCategory expenseCategory;
	private boolean benefit;
	private long price;
	
	public TransactionAccountBook(double amount, String category, String description) {
		// TODO Auto-generated constructor stub
	}

	public IncomeCategory getIncomeCategory() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ExpenseCategory getExpenseCategory() {
		return null;
	}

	public Integer getAmount() {
		// TODO Auto-generated method stub
		return null;
	}

}
