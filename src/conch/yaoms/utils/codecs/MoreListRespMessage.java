package conch.yaoms.utils.codecs;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import com.kkfun.utils.ByteUtils;

import conch.yaoms.utils.codecs.parser.BytesParserImp;
import conch.yaoms.utils.codecs.parser.IBytesParser;

/**
 * 更多列表响应消息体 20110803
 * 
 * @author yaoms
 * 
 */
public class MoreListRespMessage implements ICodeable {

	// 命令码
	private short command;
	// 版本号
	private int version;
	// 数量
	private byte count;
	// 列表
	private Vector<ICodeable> vector;

	public MoreListRespMessage(int command, IBytesParser parser) {

		try {
			setCommand((short) command);
			setVersion(parser.getInt());
			setCount(parser.getByte());
			MoreList list = new MoreList();
			for (int i = 0; i < getCount(); i++) {
				GameItem item = new GameItem();
				item.decode(null, parser);
				list.add(item);
			}
			setVector(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[\n\t命令:\t" + command + "\n");
		sb.append("\t版本:\t" + version + "\n");
		sb.append("\t节点数:\t" + count + "\n\t[\n");

		for (ICodeable codeable : vector) {
			sb.append("\t\t[" + codeable.toString() + "]\n");
		}
		
		sb.append("\t]\n]");

		return sb.toString();
	}

	public MoreListRespMessage() {
	}

	@Override
	public String decode(byte[] bs, IBytesParser bp) throws Exception {
		if (bp == null) {
			bp = new BytesParserImp(bs, byteMode);
		}
		decodeImp(bp);
		
		return toString();
	}

	private void decodeImp(IBytesParser bp) {
		setCommand(bp.getShort());
		setVersion(bp.getInt());
		setCount(bp.getByte());
		Vector<ICodeable> vector = new Vector<ICodeable>();
		for (int i = 0; i < getCount(); i++) {
			GameItem item = new GameItem();
			try {
				item.decode(null, bp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			vector.add(item);
		}
		setVector(vector);
	}

	@Override
	public void encode(ByteArrayOutputStream dataOutputStream) throws Exception {
		dataOutputStream.write(ByteUtils.short2Bytes(command, byteMode));
		dataOutputStream.write(ByteUtils.int2Bytes(version, byteMode));
		dataOutputStream.write(count);
		if (vector != null && vector.size() != 0) {
			for (ICodeable codeable : vector) {
				codeable.encode(dataOutputStream);
			}
		}
	}

	public short getCommand() {
		return command;
	}

	public void setCommand(short command) {
		this.command = command;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public byte getCount() {
		return count;
	}

	public void setCount(byte count) {
		this.count = count;
	}

	public Vector<ICodeable> getVector() {
		return vector;
	}

	public void setVector(Vector<ICodeable> vector) {
		this.vector = vector;
	}
}