package de.schroenser.usb;

import javax.usb.UsbException;

/**
 * Allows to re-throw an {@link UsbException} as runtime exception.
 */
public class UncheckedUsbException extends RuntimeException
{
    public UncheckedUsbException(String message, UsbException cause)
    {
        super(message, cause);
    }

    public UncheckedUsbException(UsbException cause)
    {
        super(cause);
    }

    public UncheckedUsbException(
        String message, UsbException cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}