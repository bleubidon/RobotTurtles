// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

public interface WritableResource extends Resource
{
    boolean isWritable();
    
    OutputStream getOutputStream() throws IOException;
}
