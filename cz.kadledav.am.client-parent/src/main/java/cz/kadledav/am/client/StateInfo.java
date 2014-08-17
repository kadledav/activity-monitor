package cz.kadledav.am.client;

public class StateInfo {

	private final String title;
	private final String process;
	private final String user;
	private final long createdNanoTime;

	public StateInfo(String process, String title) {
		this(System.getProperty("user.name"), System.nanoTime(), process, title);
	}

	public StateInfo(String user, long createdNanoTime, String process, String title) {
		this.title = title;
		this.process = process;
		this.user = user;
		this.createdNanoTime = createdNanoTime;
	}

	public String getTitle() {
		return title;
	}

	public String getProcess() {
		return process;
	}

	public String getUser() {
		return user;
	}

	public long getCreatedNanoTime() {
		return createdNanoTime;
	}

}
