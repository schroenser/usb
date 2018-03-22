package de.schroenser.usb;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbHub;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestUsbHubs
{

    private UsbDevice usbDeviceOnRootHub;
    private UsbDevice usbDeviceOnNonRootHub;
    private UsbDevice firstOfTwoSimilarDevices;
    private UsbDevice secondOfTwoSimilarDevices;
    private UsbHub subUsbHub2;
    private UsbHub subUsbHub1;
    private UsbHub usbRootHub;

    @BeforeMethod
    public void setUp()
    {
        usbDeviceOnRootHub = mockUsbDevice(1337, 1);
        usbDeviceOnNonRootHub = mockUsbDevice(4711, 5);
        firstOfTwoSimilarDevices = mockUsbDevice(815, 42);
        secondOfTwoSimilarDevices = mockUsbDevice(815, 42);
        subUsbHub2 = mockUsbHub(secondOfTwoSimilarDevices);
        subUsbHub1 = mockUsbHub(usbDeviceOnNonRootHub, firstOfTwoSimilarDevices, subUsbHub2);
        usbRootHub = mockUsbHub(usbDeviceOnRootHub, subUsbHub1);
    }

    @Test
    public void testStreamsDevicesInCorrectOrder()
    {
        Stream<UsbDevice> usbDeviceStream = UsbHubs.flattenUsbDeviceTree(usbRootHub);

        Assertions.assertThat(usbDeviceStream).containsExactly(
            usbRootHub,
            usbDeviceOnRootHub,
            subUsbHub1,
            usbDeviceOnNonRootHub,
            firstOfTwoSimilarDevices,
            subUsbHub2,
            secondOfTwoSimilarDevices);
    }

    private UsbHub mockUsbHub(UsbDevice... attachedUsbDevices)
    {
        UsbHub usbHub = mock(UsbHub.class);
        when(usbHub.isUsbHub()).thenReturn(true);
        when(usbHub.getAttachedUsbDevices()).thenReturn(Arrays.asList(attachedUsbDevices));
        return usbHub;
    }

    private UsbDevice mockUsbDevice(int vendorId, int productId)
    {
        UsbDeviceDescriptor usbDeviceDescriptor = mockUsbDeviceDescriptor(vendorId, productId);

        UsbDevice usbDevice = mock(UsbDevice.class);
        when(usbDevice.isUsbHub()).thenReturn(false);
        when(usbDevice.getUsbDeviceDescriptor()).thenReturn(usbDeviceDescriptor);
        return usbDevice;
    }

    private UsbDeviceDescriptor mockUsbDeviceDescriptor(int vendorId, int productId)
    {
        UsbDeviceDescriptor usbDeviceDescriptor = mock(UsbDeviceDescriptor.class);
        when(usbDeviceDescriptor.idVendor()).thenReturn((short) vendorId);
        when(usbDeviceDescriptor.idProduct()).thenReturn((short) productId);
        return usbDeviceDescriptor;
    }
}