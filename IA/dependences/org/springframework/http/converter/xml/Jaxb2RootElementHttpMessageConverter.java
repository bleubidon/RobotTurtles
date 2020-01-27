// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.http.converter.xml;

import javax.xml.bind.PropertyException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MarshalException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.ClassUtils;
import javax.xml.transform.Result;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.InputSource;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import org.springframework.http.converter.HttpMessageConversionException;
import javax.xml.bind.UnmarshalException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import javax.xml.transform.Source;
import org.springframework.http.HttpHeaders;
import org.springframework.core.annotation.AnnotationUtils;
import javax.xml.bind.annotation.XmlType;
import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.http.MediaType;

public class Jaxb2RootElementHttpMessageConverter extends AbstractJaxb2HttpMessageConverter<Object>
{
    private boolean processExternalEntities;
    
    public Jaxb2RootElementHttpMessageConverter() {
        this.processExternalEntities = false;
    }
    
    public void setProcessExternalEntities(final boolean processExternalEntities) {
        this.processExternalEntities = processExternalEntities;
    }
    
    @Override
    public boolean canRead(final Class<?> clazz, final MediaType mediaType) {
        return (clazz.isAnnotationPresent((Class<? extends Annotation>)XmlRootElement.class) || clazz.isAnnotationPresent((Class<? extends Annotation>)XmlType.class)) && this.canRead(mediaType);
    }
    
    @Override
    public boolean canWrite(final Class<?> clazz, final MediaType mediaType) {
        return AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) != null && this.canWrite(mediaType);
    }
    
    @Override
    protected boolean supports(final Class<?> clazz) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Object readFromSource(final Class<?> clazz, final HttpHeaders headers, Source source) throws IOException {
        try {
            source = this.processSource(source);
            final Unmarshaller unmarshaller = this.createUnmarshaller(clazz);
            if (clazz.isAnnotationPresent((Class<? extends Annotation>)XmlRootElement.class)) {
                return unmarshaller.unmarshal(source);
            }
            final JAXBElement<?> jaxbElement = (JAXBElement<?>)unmarshaller.unmarshal(source, (Class)clazz);
            return jaxbElement.getValue();
        }
        catch (UnmarshalException ex) {
            throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), (Throwable)ex);
        }
        catch (JAXBException ex2) {
            throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex2.getMessage(), (Throwable)ex2);
        }
    }
    
    protected Source processSource(final Source source) {
        if (source instanceof StreamSource) {
            final StreamSource streamSource = (StreamSource)source;
            final InputSource inputSource = new InputSource(streamSource.getInputStream());
            try {
                final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                final String featureName = "http://xml.org/sax/features/external-general-entities";
                xmlReader.setFeature(featureName, this.processExternalEntities);
                return new SAXSource(xmlReader, inputSource);
            }
            catch (SAXException ex) {
                this.logger.warn("Processing of external entities could not be disabled", ex);
                return source;
            }
        }
        return source;
    }
    
    @Override
    protected void writeToResult(final Object o, final HttpHeaders headers, final Result result) throws IOException {
        try {
            final Class<?> clazz = ClassUtils.getUserClass(o);
            final Marshaller marshaller = this.createMarshaller(clazz);
            this.setCharset(headers.getContentType(), marshaller);
            marshaller.marshal(o, result);
        }
        catch (MarshalException ex) {
            throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), (Throwable)ex);
        }
        catch (JAXBException ex2) {
            throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex2.getMessage(), (Throwable)ex2);
        }
    }
    
    private void setCharset(final MediaType contentType, final Marshaller marshaller) throws PropertyException {
        if (contentType != null && contentType.getCharSet() != null) {
            marshaller.setProperty("jaxb.encoding", (Object)contentType.getCharSet().name());
        }
    }
}
