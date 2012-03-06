package conch.yaoms.message;

public class DWMessageFactory {
	
	public static DWMessage createReMessage(byte[] datas) {
		if (datas == null || datas.length < 1) {
			return null;
		}
		
		DWMessage message = null;
		
		return message;
	}
}
