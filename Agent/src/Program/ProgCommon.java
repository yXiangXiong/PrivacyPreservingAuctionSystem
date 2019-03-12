// Copyright (C) 2010 by Yan Huang <yh8h@virginia.edu>

package Program;

import java.io.*;

import YaoGC.*;

public abstract class ProgCommon {
    public static ObjectOutputStream oos        = null;              // socket output stream 输出流
    public static ObjectInputStream  ois        = null;              // socket input stream 输入流
    public static Circuit[] ccs;
}