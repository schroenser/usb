package de.schroenser.usb.webmailnotifier;

import java.awt.Color;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;

/**
 * This is an interface for the <a href="http://dreamcheeky.com/webmail-notifier">Dream Cheeky WebMail Notifier</a>.<br>
 * <br> The byte values used in this class have been collected by searching through C++ and C# code from the internet
 * and trial and error. There is no official documentation for this device.
 */
public class WebMailNotifier
{
    private static final short VENDOR_ID = 0x1d34;
    private static final short PRODUCT_ID = 0x0004;

    public static boolean isDevice(UsbDevice usbDevice)
    {
        UsbDeviceDescriptor usbDeviceDescriptor = usbDevice.getUsbDeviceDescriptor();
        return usbDeviceDescriptor.idVendor() == VENDOR_ID && usbDeviceDescriptor.idProduct() == PRODUCT_ID;
    }

    /* Maximum for a Dream Cheeky color value. Higher values do not influence brightness or color. */
    private static final int DREAM_CHEEKY_MAXIMUM_COLOR_VALUE = 64;

    /* java.awt.Color uses 0-255 for its values */
    private static final int PARAMETER_MAXIMUM_VALUE = 255;

    private final UsbDevice usbDevice;

    public WebMailNotifier(UsbDevice usbDevice)
    {
        verifyCorrectDevice(usbDevice);
        this.usbDevice = usbDevice;
        initializeDevice();
    }

    private void verifyCorrectDevice(UsbDevice usbDevice)
    {
        if (!isDevice(usbDevice))
        {
            UsbDeviceDescriptor usbDeviceDescriptor = usbDevice.getUsbDeviceDescriptor();
            throw new IllegalArgumentException("Usb device with vendor " +
                usbDeviceDescriptor.idVendor() +
                " and product " +
                usbDeviceDescriptor.idProduct() +
                " is not compatible.");
        }
    }

    private void initializeDevice()
    {
        send((byte) 0x1f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03);
        send((byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04);
    }

    public void setColor(Color color)
    {
        setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setColor(int red, int green, int blue)
    {
        send(
            normalizeColor(red),
            normalizeColor(green),
            normalizeColor(blue),
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x05);
    }

    private byte normalizeColor(int color)
    {
        return (byte) (color * DREAM_CHEEKY_MAXIMUM_COLOR_VALUE / PARAMETER_MAXIMUM_VALUE);
    }

    private void send(byte... data)
    {
        UsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(
            (byte) (UsbConst.REQUESTTYPE_TYPE_CLASS | UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE),
            (byte) 0x09,
            (short) 0x200,
            (short) 0x00);
        usbControlIrp.setData(data);
        try
        {
            usbDevice.asyncSubmit(usbControlIrp);
        }
        catch (UsbException e)
        {
            throw new RuntimeException("Error sending data to the device", e);
        }
    }
}