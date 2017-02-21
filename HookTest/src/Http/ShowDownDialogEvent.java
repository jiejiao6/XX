package Http;

public class ShowDownDialogEvent {
	private String title;
    public ShowDownDialogEvent() {  
    }
    public ShowDownDialogEvent(String title) {  
    	this.title = title;
    }
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}  
    
}
