package model;

public enum IncomeCategory implements AccountCategory {
	SALARY("\uD83D\uDCB0 월급"),
	BONUS("\uD83C\uDF89 보너스"),
	INVESTMENT("\uD83D\uDCC8 투자 수익"),
	ALLOWANCE("\uD83D\uDCB2 용돈");

	private final String description;

	IncomeCategory(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}
}