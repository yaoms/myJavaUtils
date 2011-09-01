package conch.yaoms.utils.codecs.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.kkfun.utils.ByteUtils;

public class BytesParserImp implements IBytesParser {

	private byte[] data;
	private int byteMode;
	private int pos;

	public BytesParserImp(byte[] data, int byteMode) {
		this.data = data;
		this.byteMode = byteMode;
	}

	public int getPos() {
		return this.pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public byte getByte() {
		return this.data[(this.pos++)];
	}

	public short getShort() {
		short ret = ByteUtils
				.bytes2Short(this.data, this.pos, 2, this.byteMode);
		this.pos += 2;

		return ret;
	}

	public int getInt() {
		int ret = ByteUtils.bytes2Int(this.data, this.pos, 4, this.byteMode);
		this.pos += 4;

		return ret;
	}

	public String getString(int len) {
		String ret = new String(this.data, this.pos, len);
		this.pos += len;

		return ret;
	}

	public byte[] getBytes(int size) {
		byte[] ret = new byte[size];
		System.arraycopy(this.data, this.pos, ret, 0, size);
		this.pos += size;

		return ret;
	}

	public int getByteSize() {
		return data.length;
	}

	/**
	 * 解析UTF_8的字符串包括长度
	 * 
	 * @return
	 */
	public String getUTFString() {

		byte len = getByte();
		String string = null;
		try {
			string = new String(data, getPos(), len, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		setPos(getPos() + len);
		return string;
	}

	/**
	 * 写入一个长度为short的字符串
	 * 
	 * @param dataOutputStream
	 *            要写入的流
	 * @param value
	 *            字符串
	 * @throws IOException
	 */
	public static void writeShortString(ByteArrayOutputStream dataOutputStream,
			String value) throws IOException {
		if (dataOutputStream != null && value != null && value.length() != 0) {
			byte[] bs = ByteUtils.str2UTF8Bytes(value);
			dataOutputStream.write(ByteUtils.short2Bytes((short) bs.length,
					ByteUtils.BYTE_MODE_HH));
			dataOutputStream.write(bs);
		}
	}

	/**
	 * 写入一个长度为byte的字符串
	 * 
	 * @param dataOutputStream
	 *            要写入的流
	 * @param value
	 *            字符串
	 * @throws IOException
	 */
	public static void writeByteString(ByteArrayOutputStream dataOutputStream,
			String value) throws IOException {
		if (dataOutputStream != null && value != null && value.length() != 0) {
			byte[] bs = ByteUtils.str2UTF8Bytes(value);
			dataOutputStream.write((byte) bs.length);
			dataOutputStream.write(bs);
		}
	}

	/**
	 * 写入长度为short的byte数组
	 * 
	 * @param dataOutputStream
	 *            要写入的流
	 * @param value
	 *            byte数组
	 * @throws IOException
	 */
	public static void writeShortByteArray(
			ByteArrayOutputStream dataOutputStream, byte[] value)
			throws IOException {
		if (dataOutputStream != null) {
			short valueTemp = ByteUtils.bytes2Short(value,
					ByteUtils.BYTE_MODE_HH);
			dataOutputStream.write(ByteUtils.short2Bytes(valueTemp,
					ByteUtils.BYTE_MODE_HH));

		}
	}

	/**
	 * 写入长度为int的byte数组
	 * 
	 * @param dataOutputStream
	 *            要写入的流
	 * @param value
	 *            byte数组
	 * @throws IOException
	 */
	public static void writeIntByteArray(
			ByteArrayOutputStream dataOutputStream, byte[] value)
			throws IOException {
		if (dataOutputStream != null) {
			int valueLength = 0;
			if (value != null) {
				valueLength = value.length;

			}
			dataOutputStream.write(ByteUtils.int2Bytes(valueLength,
					ByteUtils.BYTE_MODE_HH));
			if (value != null && valueLength != 0)
				dataOutputStream.write(value);
		}
	}

	@Override
	public void back(int bytes) throws Exception {
			if (bytes > 0 && bytes <= pos) {
				this.pos -= bytes;
			} else {
				throw new Exception("回退字节数异常：" + bytes + "，当前 pos == " + pos);
			}
	}

}