package org.libvirt;

import org.libvirt.jna.ConnectionPointer;
import org.libvirt.jna.Libvirt;
import org.libvirt.jna.virError;

/**
 * Utility class which processes the last error from the libvirt library. It
 * turns errors into Libvirt Exceptions.
 *
 * @author bkearney
 */
public class ErrorHandler {

    /**
     * If the response was an error, retrieves the error and throws an exception
     *
     * @param libvirt The active connection
     * @param ret The response from the libvirt call
     * @throws LibvirtException
     */
    public static void processError(Libvirt libvirt, Object ret) throws LibvirtException {
        if (ret == null) {
            processError(libvirt);
        }
        else {
            libvirt.virResetLastError();
        }
    }

    /**
     * If the response was an error, retrieves the error and throws an exception
     *
     * @param libvirt The active connection
     * @param ret The response from the libvirt call
     * @throws LibvirtException
     */
    public static void processError(Libvirt libvirt, int ret) throws LibvirtException {
        if (ret == -1) {
            processError(libvirt);
        }
        else {
            libvirt.virResetLastError();
        }
    }

    private static void processError(Libvirt libvirt) throws LibvirtException {
        virError vError = new virError();
        int errorCode = libvirt.virCopyLastError(vError);
        if (errorCode > 0) {
            Error error = new Error(vError);
            libvirt.virResetLastError();
            /*
             * FIXME: Don't throw exceptions for VIR_ERR_WARNING
             * level errors
             */
            if (error.getLevel() == Error.ErrorLevel.VIR_ERR_ERROR) {
                throw new LibvirtException(error);
            }
        }
    }
}
