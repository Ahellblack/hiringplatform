package com.siti.utils.newaes.base64;

import java.util.Random;


public class Test {

	public static void main(String[] args) {

		String content="AES这是一段测试";
		String skey = "admin";
		try {
			String transaction_id="BSX0800D1800X20160809123212";
			String startTime=transaction_id.substring(3, 5)+":"+transaction_id.substring(5, 7);
			String endTime=transaction_id.substring(8, 10)+":"+transaction_id.substring(10, 12);
			/*skey=RandomCharString("ABC123DE465FGH789I0JKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", 16);
			System.out.println("加密方法一加密密钥："+skey);*/
			//加密
            Long cur=System.currentTimeMillis();
			byte[] encryptResultStrname = BackAES.encrypt(cur+"&"+"422214544711145682&"+cur, 1);
			byte[] encryptResultStrname1 = BackAES.encrypt("birthDate=2018-01-01&checkDate=2018-05-09&sex=1", 1);
			byte[] encryptResultStr = BackAES.encrypt(cur+"&"+"root&"+cur, 1);

			System.out.println("方法-加密后name："+new String(encryptResultStrname));
			System.out.println("方法-encryptResultStrname1："+new String(encryptResultStrname1));
			System.out.println("方法-加密后pwd："+new String(encryptResultStr));
			String decryptString=BackAES.decrypt(new String("Aswh6ksVtCauWRqbJeah6JsyDYolMdj59LIht9I9+39taNgxHNEU49i2GgetMOgv"), 1);
			System.out.println("方法-解密后："+decryptString);

			/**
			 * String parseByte2HexStr(byte buf[]) //**将二进制转换成16进制
			 *byte[] parseHexStr2Byte(String hexStr) //java将16进制转换为二进制
			 */

			String skey2 = RandomCharString("ABC123DE465FGH789I0JKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", 128);
			System.out.println("加密方法二加密密钥："+skey2);
			byte[] encryptResultStr2 = BackAES.newencrypt(content, skey2);
			System.out.println("方法二加密后："+new String(encryptResultStr2));
//			////.java将2进制数据转换成16进制parseHexStr2Byte
			String toByteString=BackAES.parseByte2HexStr(encryptResultStr2);//java将16进制转换为二进制
			System.out.println("方法二加密后转成16进制："+toByteString);

//			// /**将16进制转换成2进制
//
			byte[] ascToByte=BackAES.parseHexStr2Byte(toByteString);
			System.out.println("方法二解密密后转成二进制："+new String(ascToByte));
//
			byte[] decryptString2=BackAES.newdecrypt(ascToByte,skey2);
			System.out.println("方法二解密后："+new String(decryptString2));
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		// String result1 = Base64.encode(encryptResultStr, chartCode);
		// System.out.println("Base64转码后：" + result1);
		//
		// System.out.println("============================================");
		// String result2 = Base64.decode(result1, chartCode);
		// System.out.println("Base64解码后：" + result2);

	}
	/**
	 * 生成随机字符
	 * @author zuoysun
	 * @time 2016-07-29 v1.0
	 * @param len 随机字符的长度
	 * @param seeds 随机种子 如：ABC123DE465FGH789I0JKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
	 * @return 长度为len的随机字符
	 * */
	public static String RandomCharString(String seeds,int len) throws Exception {
		if(seeds==null || seeds.length()<=0){
			return null;
		}
        char[] c = seeds.toCharArray();
        Random random = new Random();
        String ret="";
        for( int i = 0; i < len; i ++) {
        	ret=ret+String.valueOf(c[random.nextInt(c.length)]);
        }
        return ret;
    }
}
