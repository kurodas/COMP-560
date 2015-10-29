package main;

public class Likelihood {
	String feature;
	public double spamLikelihood, hamLikelihood;
	
	public Likelihood(String f, double s, double h){
		feature = f;
		spamLikelihood = s;
		hamLikelihood = h;
				
	}
}
