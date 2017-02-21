package imei.util;

public class GaotongDoubleInfo {
	String imei_1;
	String imei_2;
	boolean gaotongDoubleSim;
	public boolean isGaotongDoubleSim() {
		return gaotongDoubleSim;
	}
	public void setGaotongDoubleSim(boolean gaotongDoubleSim) {
		this.gaotongDoubleSim = gaotongDoubleSim;
	}
	public String getImei_1() {
		return imei_1;
	}

	public void setImei_1(String imei_1) {
		this.imei_1 = imei_1;
	}

	public String getImei_2() {
		return imei_2;
	}

	public void setImei_2(String imei_2) {
		this.imei_2 = imei_2;
	}
	@Override
	public String toString() {
		return "GaotongDoubleInfo [imei_1=" + imei_1 + ", imei_2=" + imei_2
				+ ", gaotongDoubleSim=" + gaotongDoubleSim + "]";
	}
	
}
