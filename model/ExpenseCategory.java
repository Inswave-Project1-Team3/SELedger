package model;

public enum ExpenseCategory implements AccountCategory {
	FOOD("\uD83C\uDF54 식비"),
	TRANSPORT("\uD83D\uDE97 교통비"),
	ENTERTAINMENT("\uD83C\uDFAC 문화생활"),
	DAILY_NECESSITIES("\uD83D\uDED2 생필품"),
	CLOTHING("\uD83D\uDC55 의류");

	private final String description;

	ExpenseCategory(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}
}