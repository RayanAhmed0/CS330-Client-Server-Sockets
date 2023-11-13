
import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serSocket = new ServerSocket(2022);
        while (true) {
            System.out.println("Waiting for A client to connect...");
            Socket sock = serSocket.accept();
            new ServerThread(sock).start();

        }
    }

}

class ServerThread extends Thread {

    Socket sock;
    public static HashMap<String, String> applicationTypes = new HashMap<String, String>();
    private static final String encryptionKey = "ArwTechTeam-2022";
    private static final String Algorithm = "AES";
    private static final String Encoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS5PADDING";

    public ServerThread(Socket sock) {
        this.sock = sock;
    }

    public void run() {

        try {
            System.out.println("A client has been connected.");
            InputStream in = sock.getInputStream();
            OutputStream out = sock.getOutputStream();
            byte[] buffer = new byte[1024];
            in.read(buffer);
            String request = decrypt(new String(buffer).trim());
            System.out.println("A query received from a client: " + request);
            String result = encrypt(getInfo(request));
            out.write(result.getBytes());
            sock.close();
        } catch (IOException ex) {
            System.err.println("IO Exception .. ");
        }

    }

    public String getInfo(String request) {
        applicationTypes.put("Small Application", "Price: 2000-8000SR Depending on the requirements \n Quality:Top \n Targeted platforms:IOS/Android \n Support:1 Year");
        applicationTypes.put("Medium Application", "Price: 8000-15000SR Depending on the requirements \n Quality:Top \n Targeted platforms:IOS/Android \n Support:2 Years");
        applicationTypes.put("Large Application", "Price: 15000+SR Depending on the requirements \n Quality:Top \n Targeted platforms:IOS/Android \n Support:3 Years");
        applicationTypes.put("Commercial Application", "Price: (Please call us on +966565700620 to get the price Depending on the requirements) \n Quality:Top \n Targeted platforms:IOS/Android \n Support:5 Years");
        applicationTypes.put("Government Application", "Price: Please call us on +966565700620 to get the price Depending on the requirements) \n Quality:Top \n Targeted platforms:IOS/Android \n Support:5 Years");

        switch (request) {
            case "Small Application":
                return applicationTypes.get("Small Application");
            case "Medium Application":
                return applicationTypes.get("Medium Application");
            case "Large Application":
                return applicationTypes.get("Large Application");
            case "Commercial Application":
                return applicationTypes.get("Commercial Application");
            case "Government Application":
                return applicationTypes.get("Government Application");
            default:
                return "Please Select from the available types of applications:\nSmall Application\nMedium Application\nLarge Application\nCommercial Application\nGovernment Application";
        }

    }

    public static String encrypt(String textToEncrypt) {
        String encryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(Encoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, Algorithm);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(textToEncrypt.getBytes("UTF8"));
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);
        } catch (Exception E) {
            System.err.println("Encryption Exception : " + E.getMessage());
        }
        return encryptedText;
    }

    public static String decrypt(String EncText) {
        String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(Encoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, Algorithm);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(EncText.getBytes("UTF8"));
            decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            System.err.println("Decryption Exception : " + E.getMessage());
        }
        return decryptedText;
    }
}
