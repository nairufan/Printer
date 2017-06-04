import javax.usb.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fannairu on 2017/5/23.
 */
public class Printer {

    public static void getDevices(UsbDevice device, List<UsbDevice> devices) {
        if (device.isUsbHub()) {
            final UsbHub hub = (UsbHub) device;
            for (UsbDevice child : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
                getDevices(child, devices);
            }
        } else {
            devices.add(device);
        }
    }

    public static UsbDevice getPrinterDevice(List<UsbDevice> devices){
        for (UsbDevice device : devices) {
            UsbConfiguration configuration = device.getActiveUsbConfiguration();
            if(configuration != null) {
                UsbInterface usbInterface = (UsbInterface) configuration.getUsbInterfaces().get(0);
                if (!usbInterface.isClaimed()) {
                    try {
                        usbInterface.claim(i -> true);
                    } catch (UsbException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

//    public void sendToPrinter(byte[] message) throws UsbException {
//        UsbDevice device = getPrinterDevice(); //find the usb using usb4java
//
//        UsbConfiguration configuration = device.getActiveUsbConfiguration();
//        UsbInterface iface = configuration.getUsbInterfaces().get(0); //There was only 1
//
//        if (!iface.isClaimed()) {
//            iface.claim(usbInterface -> true);
//        }
//
//        UsbEndpoint endpoint = (UsbEndpoint) iface.getUsbEndpoints().get(0);
//        UsbPipe pipe = endpoint.getUsbPipe();
//
//        pipe.open();
//
//        try {
//            LOG.info(Arrays.toString(message));
//            int sent = pipe.syncSubmit(message);
//            LOG.info("Bytes Sent: " + sent);
//        } finally {
//            pipe.close();
//        }
//
//        iface.release();
//    }

    public static void main(String[] args) throws UsbException {
        UsbServices services = UsbHostManager.getUsbServices();
        List<UsbDevice> devices = new ArrayList<UsbDevice>();
        getDevices(services.getRootUsbHub(), devices);
        System.out.println("length" + devices.size());
        getPrinterDevice(devices);
    }
}
