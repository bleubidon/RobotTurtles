// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.cglib.core;

import org.springframework.asm.Label;

public interface ProcessSwitchCallback
{
    void processCase(final int p0, final Label p1) throws Exception;
    
    void processDefault() throws Exception;
}
