package Security;

import Network.Useful.Constants;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class TokenProcess {

    private final static Set<String> tokenList = new HashSet<>();

    public static String createToken(String type){
        SecureRandom random = new SecureRandom();
        String token;
        do{
            byte[] randomBytes = new byte[8];
            random.nextBytes(randomBytes);
            StringBuilder hexString = new StringBuilder(2 * randomBytes.length);
            for (byte b : randomBytes) {
                hexString.append(String.format("%02x", b));
            }

            StringBuilder stringBuilder = new StringBuilder(hexString.toString());
            stringBuilder.replace(0,1, type);
            token = stringBuilder.toString();
        }while (tokenList.contains(token));

        tokenList.add(token);
        return token;
    }

    public static void removeToken(String token){
        tokenList.remove(token);
    }

    public static void verifyToken(String token) throws Exception {
        UserManager.getUser(token);
    }

    public static byte determineUserType(String token){
        char type = token.charAt(0);
        if(type == 'P'){
            return Constants.PRODUCER;
        } else if (type == 'C') {
            return Constants.CONSUMER;
        }else {

        }
        return Constants.CONSUMER;
    }
}
