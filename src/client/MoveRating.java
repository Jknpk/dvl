package client;

public enum MoveRating {
	HIT(5), SAVE_1(4), SAVE_2(3), SAVE_3(2), SAVE_4(1), STAY(0); 
	
	
    private int value;
    
    private MoveRating(int value) {
        this.value = value;
    }
   
    public int getValue() {
        return value;
    }
}
