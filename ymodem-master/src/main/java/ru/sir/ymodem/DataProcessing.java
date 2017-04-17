package ru.sir.ymodem;

/**
 * Created by Administrator on 2017/4/17.
 */

public class DataProcessing {
	byte [] mFileData;
	byte [] [] mOutputData;
	boolean isSTX = false;
	private final static int SOH = 128;
	private final static int STX = 1024;
	int GroupNum = 1;

	public DataProcessing(byte[] fileData, boolean IsSTX) {
		mFileData = fileData;
		this.isSTX = IsSTX;

		int DataLength;
		if (isSTX) {
			DataLength = STX;
		}else {
			DataLength = SOH;
		}
		try {
			GroupNum = fileData.length / DataLength + 1;
			mOutputData = new byte[GroupNum][DataLength];

			int filelength = 0;
			while (filelength > fileData.length) {
				for (int i = 0; i < GroupNum; i++) {
					for (int j = 0; j < DataLength; j++) {
						mOutputData[i][j] = fileData[filelength];
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void setSTX(boolean STX) {
		isSTX = STX;
	}

	public byte[][] getOutputData() {
		return mOutputData;
	}
}
