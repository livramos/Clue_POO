package model;

import java.util.Random;

class Dado {
	private Random random;
	
	Dado(){
		this.random = new Random();
	}
	
	int rolar() {
		return random.nextInt(6) + 1;
	}
	
}
