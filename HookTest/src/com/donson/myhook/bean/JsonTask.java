package com.donson.myhook.bean;

import java.util.List;

public class JsonTask {
	int CODE;
	Data DATA;
	String MESSAGE;
	public static class Data{
		List<NetTask> RESULT;

		public List<NetTask> getRESULT() {
			return RESULT;
		}

		public void setRESULT(List<NetTask> rESULT) {
			RESULT = rESULT;
		}

		@Override
		public String toString() {
			return "Data [RESULT=" + RESULT + "]";
		}
		
	}
	public int getCODE() {
		return CODE;
	}
	public void setCODE(int cODE) {
		CODE = cODE;
	}
	public Data getDATA() {
		return DATA;
	}
	public void setDATA(Data dATA) {
		DATA = dATA;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	@Override
	public String toString() {
		return "JsonTask [CODE=" + CODE + ", DATA=" + DATA + ", MESSAGE="
				+ MESSAGE + "]";
	}
	

}
