package conch.yaoms.utils.codecs;

import java.io.ByteArrayOutputStream;

import conch.yaoms.utils.codecs.parser.BytesParserImp;
import conch.yaoms.utils.codecs.parser.IBytesParser;

public class Response implements ICodeable {
	
	private int length;
	
	@Override
	public String decode(byte[] bs, IBytesParser bp) throws Exception {
		if (bs != null) {
			if (bp == null) {
				bp = new BytesParserImp(bs, byteMode);
			}
			length = bp.getInt();
			if (bs.length >= length + 4) {
				while (bp.getPos() < bp.getByteSize()) {
					int command = bp.getShort();
					bp.back(2);
					switch (command) {
					case 21200:
						MoreListRespMessage message = new MoreListRespMessage();
						return toString() + message.decode(bs, bp);
						
					case 9100:
						ClientMessage clientMessage = new ClientMessage();
						return toString() + clientMessage.decode(bs, bp);

					default:
						return "command: " + command;
					}
				}
			} else {
				//TODO 长度有问题
				return "长度有问题" + new String(bs);
			}
		}
		
		return toString();
	}

	@Override
	public void encode(ByteArrayOutputStream dataOutputStream) throws Exception {
		
	}
	
	public String toString() {
			StringBuffer stringBuffer = new StringBuffer(100);
			stringBuffer.append("数据长度: ")
						.append(length)
						.append("\n数据内容:\n");
			return stringBuffer.toString();
	}

}
