package conch.yaoms.utils.codecs;

import java.io.ByteArrayOutputStream;

import com.kkfun.utils.ByteUtils;

import conch.yaoms.utils.codecs.parser.BytesParserImp;
import conch.yaoms.utils.codecs.parser.IBytesParser;

/**
 * 游戏列表项目，用于更多列表 20110803
 * 
 * @author yaoms
 * 
 */
public class GameItem implements ICodeable {

	/**
	 * 下载ID
	 */
	private int downloadID;

	/**
	 * 产品ID
	 */
	private int productID;

	/**
	 * 计费类型
	 */
	private int feeTypeID;

	/**
	 * 计费点
	 */
	private int feePointID;

	/**
	 * 计费金额
	 */
	private int feePrice;

	/**
	 * 游戏名称
	 */
	private String productName;

	/**
	 * 图片大小 单位：byte
	 */
	private int picSize;

	/**
	 * 图片字节流
	 */
	private byte[] picData;

	public GameItem() {
	}

	public GameItem(int downloadID, int productID, int feeTypeID,
			int feePointID, int feePrice, String productName, int picSize,
			byte[] picData) {
		setDownloadID(downloadID);
		setProductID(productID);
		setFeeTypeID(feeTypeID);
		setFeePointID(feePointID);
		setFeePrice(feePrice);
		setProductName(productName);
		setPicSize(picSize);
		if (picSize > 0 && picData != null) {
			setPicData(picData);
		}
	}

	@Override
	public String decode(byte[] bs, IBytesParser bp) throws Exception {
		if (bp == null) {
			bp = new BytesParserImp(bs, ByteUtils.BYTE_MODE_HH);
		}
		decodeImpl(bp);
		return toString();
	}

	private void decodeImpl(IBytesParser bp) {
		setDownloadID(bp.getInt());
		setProductID(bp.getInt());
		setFeeTypeID(bp.getShort());
		setFeePointID(bp.getShort());
		setFeePrice(bp.getShort());
		setProductName(bp.getUTFString());
		setPicSize(bp.getInt());
		if (getPicSize() > 0) {
			setPicData(bp.getBytes(getPicSize()));
		}
	}

	@Override
	public void encode(ByteArrayOutputStream dataOutputStream) throws Exception {
		dataOutputStream.write(ByteUtils.int2Bytes(getDownloadID(),
				ByteUtils.BYTE_MODE_HH));
		dataOutputStream.write(ByteUtils.int2Bytes(getProductID(),
				ByteUtils.BYTE_MODE_HH));
		dataOutputStream.write(ByteUtils.short2Bytes((short) getFeeTypeID(),
				ByteUtils.BYTE_MODE_HH));
		dataOutputStream.write(ByteUtils.short2Bytes((short) getFeePointID(),
				ByteUtils.BYTE_MODE_HH));
		dataOutputStream.write(ByteUtils.short2Bytes((short) getFeePrice(),
				ByteUtils.BYTE_MODE_HH));
		byte[] nameData = ByteUtils.str2UTF8Bytes(getProductName());
		dataOutputStream.write(nameData.length);
		dataOutputStream.write(nameData);
		dataOutputStream.write(ByteUtils.int2Bytes(getPicSize(),
				ByteUtils.BYTE_MODE_HH));
		if (getPicSize() > 0 && getPicData() != null) {
			dataOutputStream.write(getPicData());
		}
	}

	public int getDownloadID() {
		return downloadID;
	}

	public void setDownloadID(int downloadID) {
		this.downloadID = downloadID;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getFeeTypeID() {
		return feeTypeID;
	}

	public void setFeeTypeID(int feeTypeID) {
		this.feeTypeID = feeTypeID;
	}

	public int getFeePointID() {
		return feePointID;
	}

	public void setFeePointID(int feePointID) {
		this.feePointID = feePointID;
	}

	public int getFeePrice() {
		return feePrice;
	}

	public void setFeePrice(int feePrice) {
		this.feePrice = feePrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getPicSize() {
		return picSize;
	}

	public void setPicSize(int picSize) {
		this.picSize = picSize;
	}

	public byte[] getPicData() {
		return picData;
	}

	public void setPicData(byte[] picData) {
		this.picData = picData;
	}
	
	public String toString() {
		return "下载ID:" + downloadID + ", 产品ID: " + productID + ", 产品名称: " + productName + ", 计费类型: " + feeTypeID + ", 计费点ID: " + feePointID + ", 计费金额: " + feePrice;
	}

}