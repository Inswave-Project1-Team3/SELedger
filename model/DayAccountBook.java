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

import lombok.Getter;
import util.Timestamped;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class DayAccountBook implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID
    private String createDate;
    private String updateDate;
    private String memo;
    private List<TransactionAccountBook> transactionAccountBooks;

    public DayAccountBook(String memo, List<TransactionAccountBook> transactionAccountBooks) {
        this.createDate = LocalDateTime.now().withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updateDate = LocalDateTime.now().withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.memo = memo;
        this.transactionAccountBooks = transactionAccountBooks;
    }
}
