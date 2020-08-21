package de.bjm.i2cjava;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class MCP9600 {

    static I2CBus bus;

    static I2CDevice mcp;

    public static void main(String[] args) throws Exception {
        bus = I2CFactory.getInstance(I2CBus.BUS_1);
        //mcp = bus.getDevice(Integer.decode(args[0]));
        mcp = bus.getDevice(0x67);

        I2CDevice mul = bus.getDevice(0x77);
        mul.write((byte)0x8);



        Thread.sleep(400);

        while (true) {
            read();
            Thread.sleep(500);
        }
    }

    public static void read() throws Exception {
        I2CDevice mul = bus.getDevice(0x77);
        mul.write((byte)0x8);
        byte[] output = new byte[2];

        mcp.read(output, 0, 2);

        int h = output[0] & 0xff;
        int l = output[1] & 0xff;
        //int r = output[2] & 0xff;

        String sH = String.format("%8s", Integer.toBinaryString(h & 0xFF)).replace(' ', '0');
        String sL = String.format("%8s", Integer.toBinaryString(l & 0xFF)).replace(' ', '0');
        //System.out.println(sH + " " + sL);

        calculateDegrees(h, l);
    }


    public static void calculateDegrees(int h, int l) {
        String sH = String.format("%8s", Integer.toBinaryString(h & 0xFF)).replace(' ', '0');
        String sL = String.format("%8s", Integer.toBinaryString(l & 0xFF)).replace(' ', '0');

        String output = sH + "" + sL;


        // calibration -0.6
        double deg = 0.6;

        int c = 15;
        for (int i = 0; i < output.length(); i++) {
            if (output.charAt(i) == "1".toCharArray()[0]) {
                deg = deg + getCorrespondingNumber(c);
            }
            c--;
        }


        //System.out.println("The temperature should be: " + deg);

        System.out.println(deg);
    }

    public static double getCorrespondingNumber(int position) {
        switch (position) {
            case 15:
                return 0.0;
            case 14:
                return 1024.0;
            case 13:
                return 512.0;
            case 12:
                return 256.0;
            case 11:
                return 128.0;
            case 10:
                return 64.0;
            case 9:
                return 32.0;
            case 8:
                return 16.0;
            case 7:
                return 8.0;
            case 6:
                return 4.0;
            case 5:
                return 2.0;
            case 4:
                return 1.0;
            case 3:
                return 0.5;
            case 2:
                return 0.25;
            case 1:
                return 0.125;
            case 0:
                return 0.0625;
        }
        return 0;
    }


}
