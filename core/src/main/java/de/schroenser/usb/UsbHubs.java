package de.schroenser.usb;

import java.util.List;
import java.util.stream.Stream;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Provides convenience methods when dealing with {@link UsbHub}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UsbHubs
{
    /**
     * Suppresses the unchecked cast when accessing the untyped {@link List} of usb devices currently attached to the given
     * {@link UsbHub}.
     *
     * @see UsbHub#getAttachedUsbDevices()
     */
    @SuppressWarnings("unchecked")
    public static List<UsbDevice> getAttachedUsbDevices(UsbHub usbHub)
    {
        return (List<UsbDevice>) usbHub.getAttachedUsbDevices();
    }

    /**
     * Traverses the tree of usb devices attached to the given {@link UsbHub} and all USB hubs attached further down and
     * returns it in depth-first order, including the given USB hub.
     */
    public static Stream<UsbDevice> flattenUsbDeviceTree(UsbHub usbHub)
    {
        return Stream.concat(Stream.of(usbHub), getAttachedUsbDevices(usbHub).stream().flatMap(usbDevice -> {
            Stream<UsbDevice> result;
            if (usbDevice.isUsbHub())
            {
                result = flattenUsbDeviceTree((UsbHub) usbDevice);
            }
            else
            {
                result = Stream.of(usbDevice);
            }
            return result;
        }));
    }
}