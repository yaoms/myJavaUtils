package conch.yaoms.utils.codecs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.kkfun.utils.ByteUtils;

import conch.yaoms.utils.codecs.parser.IBytesParser;

/**
 * 消息声明接口
 * 
 * @author yaoms
 * 
 */
public interface ICodeable {
	
	// 模型类型
	public static final int byteMode = ByteUtils.BYTE_MODE_HH;

	// 传输模式
	// 更新
	public static final byte TYPE_UPDATE = 1;
	// 续传
	public static final byte TYPE_GO_ON = 2;
	// 不传
	public static final byte TYPE_NOTRANSMISSION = 0;

	/**
	 * 解码
	 * 
	 * @param dataInputStream
	 * @param bp
	 * @throws IOException
	 */
	public String decode(byte[] bs, IBytesParser bp) throws Exception;

	/**
	 * 编码
	 * 
	 * @param dataInputStream
	 * @throws IOException
	 */
	public void encode(ByteArrayOutputStream dataOutputStream) throws Exception;

}