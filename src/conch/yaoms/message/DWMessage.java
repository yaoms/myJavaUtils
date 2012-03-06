package conch.yaoms.message;

public interface DWMessage {
	
	// 消息编码
	public byte[] encode();

	// 消息解码
	public void decode(byte[] inData);	
	
}
