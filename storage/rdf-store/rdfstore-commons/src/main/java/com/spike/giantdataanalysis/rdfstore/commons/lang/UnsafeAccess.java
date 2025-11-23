package com.spike.giantdataanalysis.rdfstore.commons.lang;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public final class UnsafeAccess {

  private static final Logger LOG = LoggerFactory.getLogger(UnsafeAccess.class);

  public static final Unsafe theUnsafe;

  /** The offset to the first element in a byte array. */
  public static final int BYTE_ARRAY_BASE_OFFSET;

  static {
    theUnsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedAction<Object>() {
      @Override
      public Object run() {
        try {
          Field f = Unsafe.class.getDeclaredField("theUnsafe");
          f.setAccessible(true);
          return f.get(null);
        } catch (Throwable e) {
          LOG.warn("sun.misc.Unsafe is not accessible", e);
        }
        return null;
      }
    });

    if (theUnsafe != null) {
      BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
    } else {
      BYTE_ARRAY_BASE_OFFSET = -1;
    }
  }

  private UnsafeAccess() {
  }

  public static final boolean littleEndian =
      ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);
}