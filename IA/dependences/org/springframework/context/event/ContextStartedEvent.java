// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

public class ContextStartedEvent extends ApplicationContextEvent
{
    public ContextStartedEvent(final ApplicationContext source) {
        super(source);
    }
}
