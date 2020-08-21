package de.bjm.i2cjava;// Distributed with a free-will license.
// Use it any way you want, profit or free, provided it fits in the licenses of its associated works.
// MCP3428
// This code is designed to work with the MCP3428_I2CADC I2C Mini Module available from ControlEverything.com.
// https://www.controleverything.com/content/Analog-Digital-Converters?sku=MCP3428_I2CADC#tabs-0-product_tabset-2

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class MCP3428_4Channel
{
    public static void main(String[] args) throws Exception
    {
        // Create I2C bus
        I2CBus Bus = I2CFactory.getInstance(I2CBus.BUS_1);
        // Get I2C device, MCP3428 I2C address is 0x68(104)

        // TODO I2CDevice mul = Bus.getDevice(0x77);
        I2CDevice dac = Bus.getDevice(0x60);
        I2CDevice adc = Bus.getDevice(0x68);
        Thread.sleep(300);

        // TODO mul.write((byte)0xB);

        System.out.println("Setting DAC to 10V");
        //                          \/ Config Byte    \/  &  \/  Data bytes (last 4 bits of last byte unused)
        byte[] write = new byte[]{(byte)0x40, (byte)0x00, (byte)0x00};
        dac.write(write);

        System.out.println("Attempting to read channel 2!");
        // TODO mul.write((byte)0x8);

        adc.write((byte)0x38);
        byte[] output = new byte[2];
        adc.read(output, 0, 2);

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < output.length; i++) {
            String s1 = String.format("%8s", Integer.toBinaryString(output[i] & 0xFF)).replace(' ', '0');
            System.out.println(s1); // 10000001
            str.append(s1);
        }

        long outputLong = Long.parseLong(str.toString(), 2);
        System.out.println("The output is: " + outputLong);





        /*
        // Select configuration register
        // Continuous conversion mode, Channel-1, 12-bit resolution
        System.out.print("Input write Byte > ");
        int in = Integer.decode(args[0]);
        device.write((byte)in);

        // Read 2 bytes of data
        // raw_adc msb, raw_adc lsb
        byte[] data1 = new byte[2];
        device.read(0x00, data1, 0, 2);

        // Convert the data to 12-bits
        int raw_adc1 = (((data1[0] & 0x0F) * 256) + (data1[1] & 0xFF));
        if(raw_adc1 > 2047)
        {
            raw_adc1 -= 4095;
        }

        // Output data to screen
        System.out.printf("Digital value of Analog Input on request: " + args[0] + " is: %d %n", raw_adc1);

         */
    }

}