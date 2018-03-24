package de.schroenser.usb.cdi;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbServices;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import de.schroenser.usb.UncheckedUsbException;

/**
 * Produces the {@link UsbServices} instance from the {@link UsbHostManager} for CDI.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsbServicesProducer
{
    @Produces
    @Singleton
    public UsbServices produce()
    {
        try
        {
            return UsbHostManager.getUsbServices();
        }
        catch (UsbException e)
        {
            throw new UncheckedUsbException("Could not resolve usb services", e);
        }
    }
}