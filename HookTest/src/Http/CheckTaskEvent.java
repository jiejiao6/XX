package Http;

public class CheckTaskEvent {
	boolean succeed;
	public CheckTaskEvent(boolean succeed) {
		this.succeed = succeed;
	}
	public boolean isSucceed() {
		return succeed;
	}
	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}
	

}
