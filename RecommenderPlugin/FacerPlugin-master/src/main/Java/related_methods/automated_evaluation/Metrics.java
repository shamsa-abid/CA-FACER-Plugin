package related_methods.automated_evaluation;

public class Metrics {
float precision;
float recall;
public float getPrecision() {
	return precision;
}
public void setPrecision(float precision) {
	this.precision = precision;
}

	public float getRecall() {
		return recall;
	}
	public void setRecall(float recall) {
		this.recall = recall;
	}
public float getSuccess_rate() {
	return success_rate;
}
public void setSuccess_rate(float success_rate) {
	this.success_rate = success_rate;
}
public int getRank() {
	return rank;
}
public void setRank(int rank) {
	this.rank = rank;
}
float success_rate;
int rank;

}
