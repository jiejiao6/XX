package imei.util;

public class MtkDoubleInfo {
	int SimId_1;
	int SimId_2;
	String imei_1;
	String imei_2;
	String imsi_1;
	String imsi_2;
	boolean mtkDoubleSim;
	int phoneType_1;
	int phoneType_2;
	String defaultImsi;
	public int getSimId_1() {
		return SimId_1;
	}
	public void setSimId_1(int simId_1) {
		SimId_1 = simId_1;
	}
	public int getSimId_2() {
		return SimId_2;
	}
	public void setSimId_2(int simId_2) {
		SimId_2 = simId_2;
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
	public String getImsi_1() {
		return imsi_1;
	}
	public void setImsi_1(String imsi_1) {
		this.imsi_1 = imsi_1;
	}
	public String getImsi_2() {
		return imsi_2;
	}
	public void setImsi_2(String imsi_2) {
		this.imsi_2 = imsi_2;
	}
	public boolean isMtkDoubleSim() {
		return mtkDoubleSim;
	}
	public void setMtkDoubleSim(boolean mtkDoubleSim) {
		this.mtkDoubleSim = mtkDoubleSim;
	}
	public int getPhoneType_1() {
		return phoneType_1;
	}
	public void setPhoneType_1(int phoneType_1) {
		this.phoneType_1 = phoneType_1;
	}
	public int getPhoneType_2() {
		return phoneType_2;
	}
	public void setPhoneType_2(int phoneType_2) {
		this.phoneType_2 = phoneType_2;
	}
	public String getDefaultImsi() {
		return defaultImsi;
	}
	public void setDefaultImsi(String defaultImsi) {
		this.defaultImsi = defaultImsi;
	}
	@Override
	public String toString() {
		return "MtkDoubleInfo [SimId_1=" + SimId_1 + ", SimId_2=" + SimId_2
				+ ", imei_1=" + imei_1 + ", imei_2=" + imei_2 + ", imsi_1="
				+ imsi_1 + ", imsi_2=" + imsi_2 + ", mtkDoubleSim="
				+ mtkDoubleSim + ", phoneType_1=" + phoneType_1
				+ ", phoneType_2=" + phoneType_2 + ", defaultImsi="
				+ defaultImsi + "]";
	}
	
}
