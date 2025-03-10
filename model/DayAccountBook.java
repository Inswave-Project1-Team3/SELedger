package model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayAccountBook {
	private LocalDate createDate;
	private LocalDate updateDate;
	private boolean benefit;
	private long DayMoney;
	private String memo;
	private Map<Integer, TransactionAccountBook> TransactionDetails;
	private List<Comment> comments;
	
	public DayAccountBook(LocalDate date) {
		// TODO Auto-generated constructor stub
	}

	public double getTotalIncome() {
		// TODO Auto-generated method stub
		return 0;
	}

	public TransactionAccountBook[] getTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addTransaction(TransactionAccountBook transaction) {
		// TODO Auto-generated method stub
		
	}

	public double getTotalExpense() {
		// TODO Auto-generated method stub
		return 0;
	}


}
