package conch.yaoms.utils.codecs;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import com.kkfun.utils.ByteUtils;

import conch.yaoms.utils.codecs.parser.BytesParserImp;
import conch.yaoms.utils.codecs.parser.IBytesParser;

/**
 * 更多游戏消息列表 20110803
 * 
 * @author yaoms
 * 
 */
public class MoreList extends Vector<ICodeable> implements ICodeable {

	private static final long serialVersionUID = 2652764781836752516L;

	@Override
	public String decode(byte[] bs, IBytesParser bp) throws Exception {
		if (bp == null) {
			bp = new BytesParserImp(bs, ByteUtils.BYTE_MODE_HH);
		}
		decodeImpl(bp);
		
		return toString();
	}

	private void decodeImpl(IBytesParser bp) {
		while (bp.getPos() < bp.getByteSize()) {
			GameItem item = new GameItem();
			try {
				item.decode(null, bp);
			} catch (Exception e) {
				e.printStackTrace();
				item = null;
			}
			if (item != null) {
				this.add(item);
			}
		}
	}

	@Override
	public void encode(ByteArrayOutputStream dataOutputStream) throws Exception {
		for (ICodeable codeable : this) {
			codeable.encode(dataOutputStream);
		}
	}

}