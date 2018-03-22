package de.schroenser.usb.cdi;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import de.schroenser.usb.UncheckedUsbException;

/**
 * Produces the root {@link UsbHub} instance from the {@link UsbServices} for CDI.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RootUsbHubProducer
{
    @Produces
    @Singleton
    public UsbHub produce(UsbServices usbServices)
    {
        try
        {
            return usbServices.getRootUsbHub();
        }
        catch (UsbException e)
        {
            throw new UncheckedUsbException("Could not resolve usb root hub", e);
        }
    }
}