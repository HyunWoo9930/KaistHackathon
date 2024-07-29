package org.example.factorial.domain;

public enum SearchCategory {
	POLITICS("정치"),
	ECONOMY("경제"),
	SOCIETY("사회"),
	CULTURE("문화"),
	SCIENCE("과학"),
	WORLD("세계");

	private final String value;

	SearchCategory(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static SearchCategory fromValue(String value) {
		for (SearchCategory category : values()) {
			if (category.value.equals(value)) {
				return category;
			}
		}
		throw new IllegalArgumentException("Unknown category: " + value);
	}
}
