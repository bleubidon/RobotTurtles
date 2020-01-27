// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.core.type.filter;

import org.springframework.core.type.ClassMetadata;
import org.springframework.util.Assert;
import java.util.regex.Pattern;

public class RegexPatternTypeFilter extends AbstractClassTestingTypeFilter
{
    private final Pattern pattern;
    
    public RegexPatternTypeFilter(final Pattern pattern) {
        Assert.notNull(pattern, "Pattern must not be null");
        this.pattern = pattern;
    }
    
    @Override
    protected boolean match(final ClassMetadata metadata) {
        return this.pattern.matcher(metadata.getClassName()).matches();
    }
}
