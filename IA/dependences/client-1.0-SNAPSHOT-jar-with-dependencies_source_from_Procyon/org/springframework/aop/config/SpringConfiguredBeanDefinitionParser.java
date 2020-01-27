// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.aop.config;

import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.springframework.beans.factory.xml.BeanDefinitionParser;

class SpringConfiguredBeanDefinitionParser implements BeanDefinitionParser
{
    public static final String BEAN_CONFIGURER_ASPECT_BEAN_NAME = "org.springframework.context.config.internalBeanConfigurerAspect";
    private static final String BEAN_CONFIGURER_ASPECT_CLASS_NAME = "org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect";
    
    @Override
    public BeanDefinition parse(final Element element, final ParserContext parserContext) {
        if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.context.config.internalBeanConfigurerAspect")) {
            final RootBeanDefinition def = new RootBeanDefinition();
            def.setBeanClassName("org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect");
            def.setFactoryMethodName("aspectOf");
            def.setRole(2);
            def.setSource(parserContext.extractSource(element));
            parserContext.registerBeanComponent(new BeanComponentDefinition(def, "org.springframework.context.config.internalBeanConfigurerAspect"));
        }
        return null;
    }
}
