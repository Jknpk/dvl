package client;

public enum MoveRating {
	HIT(6), SAVE_1(5), SAVE_2(4), SAVE_3(3), SAVE_4(2), STAY(1), SAVE_5(0);

	private int value;

	private MoveRating(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
