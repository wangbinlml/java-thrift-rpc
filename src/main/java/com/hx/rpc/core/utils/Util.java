package com.hx.rpc.core.utils;

import java.lang.Character.UnicodeBlock;
import java.util.Random;
import java.util.UUID;

public class Util {
	public static byte[] intToByte(int i) {
		byte[] abyte0 = new byte[5];
		abyte0[4] = (byte) (0xff & i);
		abyte0[3] = (byte) ((0xff00 & i) >> 8);
		abyte0[2] = (byte) ((0xff0000 & i) >> 16);
		abyte0[1] = (byte) ((0xff000000 & i) >> 24);
		abyte0[0] = (byte) (0x00);
		return abyte0;
	}

	public static int bytesToInt(byte[] bytes) {
		int addr = 0;
		if (bytes != null && bytes.length > 0) {
			addr = bytes[0] & 0xFF;
		}
		if (bytes != null && bytes.length > 1) {
			addr |= ((bytes[1] << 8) & 0xFF00);
		}
		if (bytes != null && bytes.length > 2) {
			addr |= ((bytes[2] << 16) & 0xFF0000);
		}
		if (bytes != null && bytes.length > 3) {
			addr |= ((bytes[3] << 24) & 0xFF000000);
		}
		return addr;
	}

	public static int bytesToInt2(byte[] bytes) {
		int addr = 0;
		if (bytes != null && bytes.length > 3) {
			addr = bytes[3] & 0xFF;
			addr |= ((bytes[2] << 8) & 0xFF00);
			addr |= ((bytes[1] << 16) & 0xFF0000);
			addr |= ((bytes[0] << 24) & 0xFF000000);
		}

		return addr;
	}

	// 将字节数组转换为十六进制字符串
	public static String byteArrayToHexString(byte[] bytearray) {
		String strDigest = "";
		for (int i = 0; i < bytearray.length; i++) {
			strDigest += byteToHexString(bytearray[i]);
		}
		return strDigest;
	}

	// 将字节转换为十六进制字符串
	public static String byteToHexString(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	/**
	 * bytes转换成十六进制字符串
	 */
	public static byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
		}
		return ret;
	}

	private static byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	/**
	 * 生成随机数
	 * 
	 * @param len
	 *            长度
	 * @return 随机数
	 */
	public static String genRandomNum(int len) {
		// 0开始，10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum));

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * bytes转换成十六进制字符串
	 */
	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 注1：手机号码编码格式采用左起每2位分割，最后一段不够2位则补”F”补足2位，并直接在每段数据前加”0x”，举例如下： 13912345678->
	 * 0x13 0x91 0x23 0x45 0x67 0x8F
	 * 
	 * @param _phone
	 * @return
	 */
	public static byte[] getLastPhone(byte[] _phone) {
		byte[] ret = new byte[6];
		short tmp1, tmp2;
		if (_phone.length != 11)
			return null;
		else {
			for (int i = 0; i < 6; i++) {
				tmp1 = toShort(_phone[2 * i]);
				if (i != 5) {
					tmp2 = (short) (toShort(_phone[2 * i + 1]) - 48);
				} else {
					tmp2 = 15;
				}
				ret[i] = (byte) (tmp1 * 16 + tmp2);
			}
		}
		return ret;
	}

	public static short toShort(byte b) {
		if (b >= 0)
			return (short) b;
		else
			return (short) (b + 256);
	}

	public static byte[] getAscBytes(byte[] _conts) {
		if (null != _conts) {
			byte[] bRet = new byte[_conts.length];
			for (int i = 0; i < bRet.length; i++) {
				bRet[i] = (byte) (_conts[i] - (byte) 48);
			}
			return bRet;
		}
		return null;
	}

	// 转为unicode
	public static String encodeUnicode(String value) {
		try {
			String unicode = gbEncoding(value);
			return unicode;
		} catch (Exception e) {
			return null;
		}
	}

	public static String gbEncoding(final String gbString) {
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			// unicodeBytes = unicodeBytes + "\\\\u" + hexB;
			unicodeBytes = unicodeBytes + hexB;
		}
		return unicodeBytes;
	}

	/*****************************************************
	 * 功能介绍:将unicode字符串转为汉字 输入参数:源unicode字符串 输出参数:转换后的字符串
	 *****************************************************/
	private String decodeUnicode(final String dataStr) {
		int start = 0;
		int end = 0;
		StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	/**********************************************************************************
	 * utf82gbk
	 * 
	 */

	public static String gbk2utf8(String gbk) {
		String l_temp = GBK2Unicode(gbk);
		l_temp = unicodeToUtf8(l_temp);

		return l_temp;
	}

	public static String utf82gbk(String utf) {
		String l_temp = utf8ToUnicode(utf);
		l_temp = Unicode2GBK(l_temp);

		return l_temp;
	}

	/**
	 * 
	 * @param str
	 * @return String
	 */

	public static String GBK2Unicode(String str) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char chr1 = (char) str.charAt(i);

			if (!isNeedConvert(chr1)) {
				result.append(chr1);
				continue;
			}

			result.append("\\u" + Integer.toHexString((int) chr1));
		}

		return result.toString();
	}

	/**
	 * 
	 * @param dataStr
	 * @return String
	 */

	public static String Unicode2GBK(String dataStr) {
		int index = 0;
		StringBuffer buffer = new StringBuffer();

		int li_len = dataStr.length();
		while (index < li_len) {
			if (index >= li_len - 1 || !"\\u".equals(dataStr.substring(index, index + 2))) {
				buffer.append(dataStr.charAt(index));

				index++;
				continue;
			}

			String charStr = "";
			charStr = dataStr.substring(index + 2, index + 6);

			char letter = (char) Integer.parseInt(charStr, 16);

			buffer.append(letter);
			index += 6;
		}

		return buffer.toString();
	}

	public static boolean isNeedConvert(char para) {
		return ((para & (0x00FF)) != para);
	}

	/**
	 * utf-8 转unicode
	 * 
	 * @param inStr
	 * @return String
	 */
	public static String utf8ToUnicode(String inStr) {
		char[] myBuffer = inStr.toCharArray();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inStr.length(); i++) {
			UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				sb.append(myBuffer[i]);
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				int j = (int) myBuffer[i] - 65248;
				sb.append((char) j);
			} else {
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				String unicode = "\\u" + hexS;
				sb.append(unicode.toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param theString
	 * @return String
	 */
	public static String unicodeToUtf8(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/**
	 * 将整形转换成长度为4的字节数组类型 Description：
	 * 
	 * @param i
	 * @return byte[]
	 * @author name：heaven
	 *         <p>
	 *         ============================================
	 *         </p>
	 *         Modified No： Modified By： Modified Date： Modified Description:
	 *         <p>
	 *         ============================================
	 *         </p>
	 *
	 */
	public static byte[] IntToBytes4(int i) {
		byte abyte0[] = new byte[4];
		abyte0[3] = (byte) (0xff & i);
		abyte0[2] = (byte) ((0xff00 & i) >> 8);
		abyte0[1] = (byte) ((0xff0000 & i) >> 16);
		abyte0[0] = (byte) ((0xff000000 & i) >> 24);

		return abyte0;
	}

	/**
	 * 生成随机数
	 * 
	 * @param len
	 *            长度
	 * @return 随机数
	 */
	public static String getTid(int len) {
		// 0开始，10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum));

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * 
	 * Description：根据 tid和rpc生成invokId,用于保留头部信息和作为短信发送的tid内容
	 * 
	 * @param tid
	 * @param rpcId
	 * @return String 短信发送的tid，长度16,内容为十六进制字符
	 * @author name：heaven
	 *         <p>
	 *         ============================================
	 *         </p>
	 *         Modified No： Modified By： Modified Date： Modified Description:
	 *         <p>
	 *         ============================================
	 *         </p>
	 *
	 */
	public static String generateInvokId(String tid, String rpcId) {
		String invokeId = tid + rpcId.replace(".", "");
		int len = 16 - invokeId.length();
		for (int i = 0; i < len; i++) {
			invokeId = invokeId + "0";
		}

		return invokeId;
	}

}
