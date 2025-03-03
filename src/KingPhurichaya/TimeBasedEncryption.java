package src.KingPhurichaya;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;


// TimeBasedEncryption ใช้ AES-GCM เพื่อเข้ารหัสและถอดรหัสวันหมดอายุของบัตร
// ใช้ Timestamp เป็น Salt เพื่อป้องกันการเดารหัส
//คลาสที่ใช้เข้ารหัสและถอดรหัสข้อมูลโดยอ้างอิงตาม Timestamp
public class TimeBasedEncryption {
    private static final String SECRET = "MySuperSecretKey"; // Key สำหรับผสมกับ timestamp
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128; // ใช้ AES-128
    private static final int GCM_IV_LENGTH = 12; // GCM IV 12 ไบต์
    private static final int GCM_TAG_LENGTH = 128; // ความยาว tag 128 บิต

    //Strategy Pattern → เลือกใช้กลยุทธ์เข้ารหัส-ถอดรหัสที่ต่างกัน
    public static String encrypt(String plainText) {
        // ซ่อนรายละเอียดการเข้ารหัส AES-GCM //Abstraction
        try {
            // เอา timestamp มาทำเป็น salt
            String timeStamp = String.valueOf(System.currentTimeMillis());

            // สร้างคีย์จาก SECRET + timestamp ด้วย PBKDF2
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(SECRET.toCharArray(), timeStamp.getBytes("UTF-8"), ITERATIONS, KEY_LENGTH);
            SecretKeySpec keySpec = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            // สร้าง IV แบบสุ่ม 12 ไบต์สำหรับ GCM
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
            String ivEncoded = Base64.getEncoder().encodeToString(iv);
            String cipherTextEncoded = Base64.getEncoder().encodeToString(encryptedBytes);

            // กลับในรูปแบบ timestamp:iv:ciphertext
            return timeStamp + ":" + ivEncoded + ":" + cipherTextEncoded;
        } catch (Exception e) {
            e.printStackTrace();
            return plainText; // fallback
        }
    }

    public static String decrypt(String cipherTextWithData) {
        // ซ่อนรายละเอียดการถอดรหัส
        try {
            String[] parts = cipherTextWithData.split(":", 3);
            if (parts.length != 3) {
                return cipherTextWithData; // ไม่ใช่รูปแบบเข้ารหัส ให้คืนค่าตามเดิม
            }
            String timeStamp = parts[0];
            String ivEncoded = parts[1];
            String cipherTextEncoded = parts[2];

            // สร้างคีย์จาก SECRET + timestamp
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(SECRET.toCharArray(), timeStamp.getBytes("UTF-8"), ITERATIONS, KEY_LENGTH);
            SecretKeySpec keySpec = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            byte[] iv = Base64.getDecoder().decode(ivEncoded);
            byte[] cipherBytes = Base64.getDecoder().decode(cipherTextEncoded);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            byte[] decrypted = cipher.doFinal(cipherBytes);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return cipherTextWithData; // fallback
        }
    }
}

