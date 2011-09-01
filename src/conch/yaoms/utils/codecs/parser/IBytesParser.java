package conch.yaoms.utils.codecs.parser;


/**
 * 消息解析工具接口
 * @author Administrator
 *
 */
public interface IBytesParser {

	public void back(int bytes) throws Exception;
	
	public int getPos();

	public void setPos(int pos);

	public byte getByte();

	public short getShort();

	public int getInt();

	public String getString(int len) ;

	public byte[] getBytes(int size);
	//获取字节长度
	public int getByteSize();
	
	/**
	 * 解析UTF_8的字符串包括长度
	 * @return
	 */
	public String getUTFString();
}