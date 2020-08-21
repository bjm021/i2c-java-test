package de.bjm.i2cjava;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class UniversalWrite {

    public static void main(String[] args) throws Exception {

        if (args[0].equalsIgnoreCase("mcp9600")) {
            MCP9600.main(args);
            return;
        } else if (args[0].equalsIgnoreCase("discover")) {
            Discover.discoverDevices();
            return;
        } else if (args[0].equalsIgnoreCase("regtest")) {
            Discover.regTest(args);
            return;
        }

        byte mulByte = (byte)(int) Integer.decode(args[0]);

        int addr = Integer.decode(args[1]);

        byte[] write = new byte[args.length];

        for (int i = 2; i < args.length; i++) {
            System.out.println("Writing: " + Integer.decode(args[i]));
            write[i] = (byte)(int) Integer.decode(args[i]);
        }

        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        //I2CDevice mul = bus.getDevice(0x77);
        //mul.write(mulByte);

        I2CDevice dev = bus.getDevice(addr);
        dev.write(write);

        Thread.sleep(400);

        byte[] out = new byte[2];
        dev.read(out, 0, 2);





        String sH = String.format("%8s", Integer.toBinaryString(out[0] & 0xFF)).replace(' ', '0');
        String sL = String.format("%8s", Integer.toBinaryString(out[1] & 0xFF)).replace(' ', '0');

        System.out.println(sH + " " + sL);

    }

}
