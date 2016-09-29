package com.example.devops;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by eseliavka on 8/26/16.
 */

//2014-02-08	64397	13535	95	K4TML	1375400	50575829680	112739500545

/**
* CREATE TABLE test_bin
*  (
*     hit_date DATE,
*     f1       INT,
*     f2       SMALLINT,
*     f3       TINYINT,
*     f4       VARCHAR(5),
*     f5       INT,
*     f6       BIGINT,
*     f7       BIGINT
*  );
 */
public class IBinaryGenerator {
    public static void main(String[] args) throws IOException {
        ByteBuffer row =ByteBuffer.allocate(41);
        row.order(ByteOrder.LITTLE_ENDIAN);
        row.putShort((short)39);
        row.put((byte)0);
        row.putInt(1140208);
        row.putInt(64379);
        row.putShort((short) 13535);
        row.put((byte)95);

        row.putShort((short) "K4TML".length());
        row.put("K4TML".getBytes());
        row.putInt(1375400);
        row.putLong(50575829680L);
        row.putLong(112739500545L);

        FileOutputStream fos = new FileOutputStream("/tmp/java.bin");
        fos.write(row.array());
        fos.close();
    }
}
