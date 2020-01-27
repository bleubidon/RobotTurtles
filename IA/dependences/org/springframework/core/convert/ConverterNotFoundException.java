// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.core.convert;

public final class ConverterNotFoundException extends ConversionException
{
    private final TypeDescriptor sourceType;
    private final TypeDescriptor targetType;
    
    public ConverterNotFoundException(final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        super("No converter found capable of converting from type " + sourceType + " to type " + targetType);
        this.sourceType = sourceType;
        this.targetType = targetType;
    }
    
    public TypeDescriptor getSourceType() {
        return this.sourceType;
    }
    
    public TypeDescriptor getTargetType() {
        return this.targetType;
    }
}
