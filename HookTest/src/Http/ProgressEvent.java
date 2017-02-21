package Http;

public class ProgressEvent {
	private int progress;
	private int total;
    public ProgressEvent(int progress,int total) {  
       this.progress = progress;
       this.total = total;
    }  
    public int getProgress(){
    	return progress;
    }
    public int getTotal(){  
        return total;  
    }  
}
