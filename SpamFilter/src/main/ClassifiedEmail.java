package main;

public class ClassifiedEmail {

	private String emailFileName, emailClassification;
	private Double spamProbabilityLog, hamProbabilityLog;
	
	public ClassifiedEmail(String fileName, String emailClass, Double spamProb, Double hamProb){
		emailFileName = fileName;
		if(emailClass.equalsIgnoreCase("HAM") || emailClass.equalsIgnoreCase("SPAM")){
			emailClassification = emailClass;
		}
		spamProbabilityLog = spamProb;
		hamProbabilityLog = hamProb;
	}
	
	public String getEmailFileName() {
		return emailFileName;
	}

	public String getEmailClassification() {
		return emailClassification;
	}

	public Double getSpamProbabilityLog() {
		return spamProbabilityLog;
	}

	public Double getHamProbabilityLog() {
		return hamProbabilityLog;
	}
}
