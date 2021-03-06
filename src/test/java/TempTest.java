import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Base64;

public class TempTest {
    public static void exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            p.waitFor();
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        BigInteger integer = new BigInteger("-128");
        byte[] byteArray = integer.toByteArray();

        Base64.getDecoder().decode("");
        System.out.println(byteArray);
//        String commandStr = "ping www.taobao.com";
        //String commandStr = "ipconfig";
//        TempTest.exeCmd(commandStr);
    }
}
