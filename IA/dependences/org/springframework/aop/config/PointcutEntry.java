// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.aop.config;

import org.springframework.beans.factory.parsing.ParseState;

public class PointcutEntry implements ParseState.Entry
{
    private final String name;
    
    public PointcutEntry(final String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Pointcut '" + this.name + "'";
    }
}
