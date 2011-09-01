package conch.yaoms.utils.codecs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.kkfun.utils.ByteUtils;

import conch.yaoms.utils.codecs.parser.BytesParserImp;
import conch.yaoms.utils.codecs.parser.IBytesParser;

/**
 * 响应更新客户端文件模型
 * 
 * @author yaoms
 * 
 */
public class ClientMessage implements ICodeable {

	// 命令码
	private short command;
	// 客户端ID
	private int gameId;
	// 版本号
	private int version;
	// 类型（续传/更新/不传）
	private byte type;
	// 客户端、图片文件原始长度
	private int length;
	// 要发给客户端的长度
	private int updateLength;
	// 文件
	private byte[] fileDate;
	// 扩展位数
	private short expand;
	// 扩展值
	private byte expand1;

	public String decode(byte[] bs, IBytesParser bp) throws IOException {
		if (bp == null) {
			bp = new BytesParserImp(bs, byteMode);
		}
		decodeImp(bp);
		
		return toString();
	}

	private void decodeImp(IBytesParser bp) {
		setCommand(bp.getShort());
		setGameId(bp.getInt());
		setVersion(bp.getInt());
		setType(bp.getByte());
		setLength(bp.getInt());
		setUpdateLength(bp.getInt());
		setFileDate(bp.getBytes(getUpdateLength()));
		setExpand(bp.getShort());
		setExpand1(bp.getByte());
	}

	public void encode(ByteArrayOutputStream dataOutputStream)
			throws IOException {
		dataOutputStream.write(ByteUtils.short2Bytes(command, byteMode));// 命令
		dataOutputStream.write(ByteUtils.int2Bytes(gameId, byteMode));// 软件ID
		dataOutputStream.write(ByteUtils.int2Bytes(version, byteMode));// 版本
		dataOutputStream.write(type);// 操作类型
		dataOutputStream.write(ByteUtils.int2Bytes(length, byteMode));// 原始长度

		// 更新/续传
		if (type == ICodeable.TYPE_UPDATE || type == ICodeable.TYPE_GO_ON) {
			dataOutputStream.write(ByteUtils.int2Bytes(updateLength, byteMode));// 追加长度
			dataOutputStream.write(fileDate);
		} else {
			dataOutputStream.write(ByteUtils.int2Bytes(0, byteMode));// 没有可以传的字节

		}
		dataOutputStream.write(ByteUtils.short2Bytes(expand, byteMode));// 扩展
		dataOutputStream.write((byte) expand1);
	}

	public ClientMessage() {

	}

	public ClientMessage(short command, int gameId, int version, byte type,
			int length, int updateLength, byte[] fileDate, short expand,
			byte expand1) {
		super();
		this.command = command;
		this.gameId = gameId;
		this.version = version;
		this.type = type;
		this.length = length;
		this.updateLength = updateLength;
		this.fileDate = fileDate;
		this.expand = expand;
		this.expand1 = expand1;
	}

	public short getCommand() {
		return command;
	}

	public void setCommand(short command) {
		this.command = command;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getUpdateLength() {
		return updateLength;
	}

	public void setUpdateLength(int updateLength) {
		this.updateLength = updateLength;
	}

	public byte[] getFileDate() {
		return fileDate;
	}

	public void setFileDate(byte[] fileDate) {
		this.fileDate = fileDate;
	}

	public short getExpand() {
		return expand;
	}

	public void setExpand(short expand) {
		this.expand = expand;
	}

	public byte getExpand1() {
		return expand1;
	}

	public void setExpand1(byte expand1) {
		this.expand1 = expand1;
	}
	
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer(100);
		stringBuffer.append("命令: ")
					.append(getCommand())
					.append("; 游戏ID: ")
					.append(getGameId())
					.append("; 版本:")
					.append(getVersion())
					.append("; 总长度: ")
					.append(getLength())
					.append("; 传输长度: ")
					.append(getUpdateLength())
					.append("; 扩展位: ")
					.append(getExpand())
					.append("; 扩展位1: ")
					.append(getExpand1());
		return stringBuffer.toString();
	}
}
