package model;

import java.time.LocalDate;
public class TransactionAccountBook {

	private long dayAccountBookId;
	private boolean benefitCheck;
	private LocalDate createTime;
	private LocalDate updateTime;
	private IncomeCategory incomeCategory;
	private ExpenseCategory expenseCategory;
	private boolean benefit;
	private long price;
	

	public TransactionAccountBook(long dayAccountBookId, boolean benefitCheck, LocalDate createTime,
			LocalDate updateTime, IncomeCategory incomeCategory, ExpenseCategory expenseCategory, boolean benefit,
			long price) {
		this.dayAccountBookId = dayAccountBookId;
		this.benefitCheck = benefitCheck;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.incomeCategory = incomeCategory;
		this.expenseCategory = expenseCategory;
		this.benefit = benefit;
		this.price = price;
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

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class TransactionAccountBook implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID
    private String createDate;
    private String updateDate;
    private boolean benefit;
    private long money;

    public TransactionAccountBook(boolean benefit, long money) {
        this.createDate = LocalDateTime.now().withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updateDate = LocalDateTime.now().withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.benefit = benefit;
        this.money = money;
    }
}
