package de.bjm.i2cjava;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class DACSet {

    public static void main(String[] args) throws Exception {

        if (args[0].equalsIgnoreCase("read")) {
            readADC(args);
            return;
        }

        // Create I2C bus
        I2CBus Bus = I2CFactory.getInstance(I2CBus.BUS_1);
        // Get I2C device, MCP3428 I2C address is 0x68(104)

        I2CDevice mul = Bus.getDevice(0x77);
        I2CDevice dac = Bus.getDevice(Integer.decode(args[1]));
        Thread.sleep(300);

        int v = Integer.parseInt(args[2]);
        if (v > 10000) {
            System.err.println("Only 0-10000 allowed!");
            return;
        }
        int i = (int) Math.round((v)/2.44);

        short l = Short.parseShort(Integer.toBinaryString(i), 2);
        ByteBuffer bytes = ByteBuffer.allocate(2).putShort(l);
        byte[] write = bytes.array();
        for (int i1 = 0; i1 < write.length; i1++) {
            System.out.println(String.format("%8s", Integer.toBinaryString(write[i1] & 0xFF)).replace(' ', '0'));
        }






        int mulByte = Integer.decode(args[0]);
        mul.write((byte)mulByte);
        System.out.println("Setting DAC....");
        //                          \/ Config Byte    \/  &  \/  Data bytes (last 4 bits of last byte unused)
        byte[] out = new byte[]{(byte)0x40, write[0], write[1]};
        dac.write(write);
    }

    public static void readADC(String[] args) throws Exception {
        // Create I2C bus
        I2CBus Bus = I2CFactory.getInstance(I2CBus.BUS_1);
        // Get I2C device, MCP3428 I2C address is 0x68(104)

        I2CDevice mul = Bus.getDevice(0x77);
        I2CDevice adc = Bus.getDevice(Integer.decode(args[1]));

        mul.write((byte)0x8);
        adc.write((byte)((int)Integer.decode(args[2])));
        Thread.sleep(500);

        byte[] read = new byte[2];
        adc.read(read, 0, 2);

        System.out.println(convertBytesToLong(read));
    }

    private static long convertBytesToLong(byte[] output) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < output.length; i++) {
            String stmp = String.format("%8s", Integer.toBinaryString(output[i] & 0xFF)).replace(' ', '0');
            System.out.println(stmp); // 10000001
            str.append(stmp);
        }

        return Long.parseLong(str.toString(), 2);
    }



}
