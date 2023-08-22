package example.Advanced.Music.app.Util;

import example.Advanced.Music.app.validator.Validator;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class MaskingUtil {

	public static class Destruction {
		public static String defaultMasking(String source) {
			if (!Validator.isHaveDataString(source)) {
				return "";
			}
			return "*".repeat(source.length());
		}

		public static String emailMasking(String sourceEmail) {
			if (!Validator.isHaveDataString(sourceEmail)) {
				return "";
			}
			String[] parts = sourceEmail.split("@");
			if (parts.length != 2) {
				return sourceEmail;
			}
			String username = parts[0];
			String domain = parts[1];
			String[] partsDomain = domain.split("\\.");
			if (partsDomain.length > 0) {
				String maskedDomain = "";
				for (int i = 0; i < partsDomain.length; i++) {
					if (i < partsDomain.length - 1) {
						maskedDomain = maskedDomain + "*".repeat(partsDomain[i].length()) + ".";
					} else {
						maskedDomain = maskedDomain + partsDomain[i];
					}
				}
				domain = maskedDomain;
			}
			int usernameLength = username.length();
			if (usernameLength <= 2) {
				return "*".repeat(usernameLength) + "@" + domain;
			} else {
				String maskedUsername = username.charAt(0) + "*".repeat(usernameLength - 2)
						+ username.charAt(usernameLength - 1);
				return maskedUsername + "@" + domain;
			}
		}

		public static String defaultMaskingWith(String source, char value) {
			if (!Validator.isHaveDataString(source)) {
				return "";
			}
			return String.valueOf(value).repeat(source.length());
		}

		public static String defaultMaskingTo(String source, String value) {
			if (!Validator.isHaveDataString(source)) {
				return "";
			}
			if (!Validator.isHaveDataString(value)) {
				return "default";
			}
			return value;
		}
	}

	public static class Noise {
		public static String maskString(String source, long noiseLength) {
			if (noiseLength < 1) {
				return source;
			}
			SecureRandom random = new SecureRandom();
			byte[] noiseBytes = new byte[(int) noiseLength];
			random.nextBytes(noiseBytes);
			return source + Base64.getEncoder().encodeToString(noiseBytes);
		}

		public static Object maskNumber(Object source, float variance) {
			if (variance <= 0) {
				return source;
			}
			SecureRandom random = new SecureRandom();
			if (source instanceof Integer || source instanceof Long) {
				Long numSource = (Long) source;
				Long n1 = (long) (numSource - numSource * (variance / 100));
				if (numSource <= 0) {
					Long n2 = (long) (numSource * (variance / 100) - numSource);
					return random.nextLong(n1, n2);
				} else {
					Long n2 = (long) (numSource + numSource * (variance / 100));
					return random.nextLong(n1, n2);
				}
			}
			if (source instanceof Float || source instanceof Double) {
				Double numSource = (Double) source;
				Double n1 = numSource - numSource * (variance / 100);
				if (numSource <= 0) {
					Double n2 = numSource * (variance / 100) - numSource;
					return random.nextDouble(n1, n2);
				} else {
					Double n2 = numSource + numSource * (variance / 100);
					return random.nextDouble(n1, n2);
				}
			}
			return 0;
		}
	}

	public static class Randomization {
		public static <T> Object randomizeNumber(T from, T to) {
			Random rand = new Random();
			if (to instanceof Integer || to instanceof Long) {
				Long c1 = (long) from;
				Long c2 = (long) to;
				return rand.nextLong(c2 - c1) + c1;
			}
			if (to instanceof Float || to instanceof Double) {
				Double c1 = (double) from;
				Double c2 = (double) to;
				return rand.nextFloat() * (c2 - c1) + c1;
			}
			return 0;
		}

		public static String randomizeStringNumber(int length) {
			Random rand = new Random();
			char chars[] = new char[length];
			for (int i = 0; i < length; i++) {
				chars[i] = (char) (rand.nextInt(10) + '0');
			}
			return new String(chars);
		}

		public static String randomizeString(String source) {
			Random rand = new Random();
			char[] chars = source.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (Character.isLetter(chars[i])) {
					chars[i] = (char) (rand.nextInt(26) + 'a');
				}
			}
			return new String(chars);
		}

		public static <T> Object randomizeArray(List<T> arr) {
			if (!Validator.isHaveDataLs(arr)) {
				return 0;
			}
			Random rand = new Random();
			return arr.get(rand.nextInt(arr.size() - 1));
		}
	}

	public static class Faking {
		public static String fakeString(String data) {
			Random rand = new Random();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length(); i++) {
				char c = data.charAt(i);
				if (Character.isLetter(c)) {
					char fakeChar = ' ';
					if (Character.isUpperCase(c)) {
						fakeChar = (char) (rand.nextInt(26) + 'A');
					} else {
						fakeChar = (char) (rand.nextInt(26) + 'a');
					}
					sb.append(fakeChar);
				} else {
					char fakeChar = ' ';
					if (Character.isDigit(c)) {
						fakeChar = (char) (rand.nextInt(10) + '0');
					} else {
						fakeChar = c;
					}
					sb.append(fakeChar);
				}
			}
			return sb.toString();
		}
	}

//	public static class Pseudonymization {
//		public static String encrypt(String source, EncriptAlgorithmEnum type) throws NoSuchAlgorithmException {
//			// Khởi tạo đối tượng MessageDigest với thuật toán mã hóa
//			MessageDigest md = MessageDigest.getInstance(type.getValue());
//			md.update(source.getBytes());
//			// Chuyển đổi kết quả mã hóa sang chuỗi hexa
//			byte[] byteData = md.digest();
//			StringBuilder sb = new StringBuilder();
//			for (byte b : byteData) {
//				sb.append(String.format("%02x", b));
//			}
//			String encryptedPassword = sb.toString();
//			return encryptedPassword;
//		}
//	}

	public static class Scrambling {
		public static String scrambleString(String str) {
			char[] charArray = str.toCharArray();
			Random random = new Random();
			for (int i = 0; i < charArray.length; i++) {
				int randomIndex = random.nextInt(charArray.length);
				char temp = charArray[i];
				charArray[i] = charArray[randomIndex];
				charArray[randomIndex] = temp;
			}
			return new String(charArray);
		}
	}

	public static class Truncation {
		public static String truncate(String source, int length) {
			if (!Validator.isHaveDataString(source)) {
				return "";
			}
			if (source.length() <= length) {
				return source;
			}
			return source.substring(0, length);
		}
	}

	public static class MaskingFormat {
		public static String mask(String source, char format, int lengthMask) {
			if (!Validator.isHaveDataString(source)) {
				return "";
			}
			if (source.length() <= lengthMask) {
				return String.valueOf(format).repeat(source.length());
			} else {
				return source.substring(0, source.length() - lengthMask) + String.valueOf(format).repeat(lengthMask);
			}
		}
	}

	public static class Encription {
		public static class AESEncryption {
			private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
			private static final String ALGORITHM_ENCRYPT = "AES";
			private static final int IV_SIZE = 16;

			public static String generateRandomIV() throws Exception {
				SecureRandom random = new SecureRandom();
				byte[] ivBytes = new byte[IV_SIZE];
				random.nextBytes(ivBytes);
				return ivToString(ivBytes);
//				return new String(ivBytes, StandardCharsets.UTF_8);
			}

			public static String ivToString(byte[] iv) {
				return bytesToHex(iv).substring(0, 16);
			}

			private static String bytesToHex(byte[] bytes) {
				StringBuilder sb = new StringBuilder();
				for (byte b : bytes) {
					sb.append(String.format("%02x", b));
				}
				return sb.toString();
			}

			// mã hóa AES với độ dài tùy ý key có độ dài 16 byte, thì đây là mã hóa AES
			// 128-bit; nếu mảng byte key có độ dài 32 byte, thì đây là mã hóa AES 256-bit.
			public static String encrypt(String plaintext, String key, String iv) throws Exception {
				SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_ENCRYPT);
				IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
				byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
				return Base64.getEncoder().encodeToString(encrypted);
			}

			public static String decrypt(String cipherText, String key, String iv) throws Exception {
				System.out.println(cipherText);
				SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_ENCRYPT);
				IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
				byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
				return new String(decrypted, StandardCharsets.UTF_8);
			}
		}

		public static class DESEncryption {

			private static final String DES_ALGORITHM = "DES";
			private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
			private static final int IV_SIZE = 8;

			public static String generateRandomIV() throws Exception {
				SecureRandom random = new SecureRandom();
				byte[] ivBytes = new byte[IV_SIZE];
				random.nextBytes(ivBytes);
				return ivToString(ivBytes);
//				return new String(ivBytes, StandardCharsets.UTF_8);
			}

			public static String ivToString(byte[] iv) {
				return bytesToHex(iv).substring(0, 8);
			}

			private static String bytesToHex(byte[] bytes) {
				StringBuilder sb = new StringBuilder();
				for (byte b : bytes) {
					sb.append(String.format("%02x", b));
				}
				return sb.toString();
			}

			public static String encrypt(String plaintext, String key, String iv) throws Exception {
				SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), DES_ALGORITHM);
				IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
				byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
				return Base64.getEncoder().encodeToString(encrypted);
			}

			public static String decrypt(String cipherText, String key, String iv) throws Exception {
				SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), DES_ALGORITHM);
				IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
				byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
				return new String(decrypted, StandardCharsets.UTF_8);
			}
		}

		public static class RSAEncryption {
			private final static int bitLength = 2048;
			private final static int smallLength = 16;

			public static String generateKey() {
				Random random = new Random();
				// tạo 2 số nguyên tố ngẫu nhiên p và q
				BigInteger p = BigInteger.probablePrime(bitLength / 2, random);
				BigInteger q = BigInteger.probablePrime(bitLength / 2, random);
				// p*q
				BigInteger n = p.multiply(q);
				// phiN = (p - 1) * (q - 1)
				// e < phiN //Chọn một số nguyên tố e tương đối nhỏ phi(n)(16 bit).
				BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
				Random rnd = new Random();
				BigInteger prime = BigInteger.probablePrime(smallLength, rnd);
				BigInteger e = new BigInteger(prime.toString());
				// d * e mod phi(n) = 1
				BigInteger d = e.modInverse(phiN);
				String key = e.toString() + "," + n.toString() + "," + d.toString();
				return key;
			}

			public static String encrypt(String plaintext, String publicKey) {
				byte[] inputData = plaintext.getBytes(StandardCharsets.UTF_8);
				BigInteger e, n;
				try {
					String[] key = publicKey.split("\\,");
					e = new BigInteger(key[0]);
					n = new BigInteger(key[1]);
				} catch (Exception ex) {
					throw ex;
				}
				BigInteger m = new BigInteger(inputData);
				BigInteger c = m.modPow(e, n);
				return c.toString();
			}

			public static String decrypt(String ciphertext, String privateKey) {
				byte[] inputData = new BigInteger(ciphertext).toByteArray();
				BigInteger d, n;
				try {
					String[] key = privateKey.split("\\,");
					d = new BigInteger(key[0]);
					n = new BigInteger(key[1]);
				} catch (Exception e) {
					throw e;
				}
				BigInteger c = new BigInteger(inputData);
				BigInteger m = c.modPow(d, n);
				return new String(m.toByteArray());
			}
		}
	}
}
