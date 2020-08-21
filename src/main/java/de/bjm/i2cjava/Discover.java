package de.bjm.i2cjava;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class Discover {

    public static void discoverDevices() throws Exception {
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

        int dev = 0x60;
        I2CDevice mul = bus.getDevice(0x77);

        for (int i = 0; i < 8; i++) {
            System.out.print("Checking " + dev+i + " = ");
            mul.write((byte)0x8);
            I2CDevice device = bus.getDevice(dev+i);
            try {
                device.read();
                System.out.println("Device found!");
            } catch (IOException e) {
                System.out.println("Device not there!");
            }
        }
    }

    public static void regTest(String[] args) throws Exception {
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

        I2CDevice mul = bus.getDevice(0x77);
        mul.write((byte)0x8);
        I2CDevice dev = bus.getDevice(0x67);

        byte[] write = new byte[args.length-1];

        for (int i = 1; i < args.length; i++) {
            write[i-1] = (byte)(int)Integer.decode(args[i]);
            //System.out.println("Writing: " + (int)Integer.decode(args[i]));
        }

        dev.write(write);
        byte[] output = new byte[2];
        dev.read(output, 0, 2);

        String sH = String.format("%8s", Integer.toBinaryString(output[0] & 0xFF)).replace(' ', '0');
        String sL = String.format("%8s", Integer.toBinaryString(output[1] & 0xFF)).replace(' ', '0');

        System.out.println(sH + " " + sL);
    }

}
