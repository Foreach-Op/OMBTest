package Network.Useful;

import java.util.zip.CRC32;

public class HashProducer {

    public static long calculateHash(byte[] bytes) {
        // 8 bytes
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }
}
