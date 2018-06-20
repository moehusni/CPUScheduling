
public class Process {
	private int id;
	private int arrivalTime;
	private int cpuBurst;
	private float waitTime;
	private float turnAround;
	private float response;
	
	Process() {
		this.id = 0;
		this.arrivalTime = 0;
		this.cpuBurst = 0;
		this.waitTime = 0;
	}

	Process(int id, int arrivalTime, int cpuBurst) {
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.cpuBurst = cpuBurst;
		this.waitTime = 0;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the arrivalTime
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @param arrivalTime
	 *            the arrivalTime to set
	 */
	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	/**
	 * @return the cpuBurst
	 */
	public int getCpuBurst() {
		return cpuBurst;
	}
	public void setCpuBurst() {
		cpuBurst -= 1;
	}
	public void SetTurnAround(int counter) {
		this.turnAround = counter- this.arrivalTime;
	}
	public float getTurnAround() {
		return this.turnAround;
	}
	
	public float getResponse() {
		return response;
	}
	
	public void setResponse(float response) {
		this.response = response - this.arrivalTime;
	}
	
	public float getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(float waitTime) {
		this.waitTime = waitTime;
	}

}

