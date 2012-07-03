package org.libvirt;

import java.io.PrintStream;

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

    public static PrintStream errorOut = System.err;
    private static Error lastError = null;

    /**
     * Look for the latest error from libvirt not tied to a connection
     *
     * @param libvirt
     *            the active connection
     * @throws LibvirtException
     */
    public static void processError(Libvirt libvirt) throws LibvirtException {
        Error last = lastLibvirtError(libvirt);
        if (errorOut != null && last != null) {
            errorOut.println("Libvirt ERR: " + last.getMessage());
        }
        lastError = last;
    }

    public static Error lastError() {
        return lastError;
    }


    private static Error lastLibvirtError(Libvirt libvirt) {
        errorOut.println("lastLibvirtError called");
        virError vError = new virError();
        int errorCode = libvirt.virCopyLastError(vError);
        if (errorCode > 0) {
            errorOut.println("error found");
            Error error = new Error(vError);
            libvirt.virResetLastError();
            return error;
        }
        else {
            return null;
        }

    }


}
