// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.aop.aspectj;

import org.aspectj.weaver.ReferenceTypeDelegate;
import org.springframework.util.ClassUtils;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.internal.tools.MatchingContextBasedTest;
import org.aspectj.weaver.ast.HasAnnotation;
import org.aspectj.weaver.ast.FieldGetCall;
import org.aspectj.weaver.ast.Call;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Instanceof;
import org.aspectj.weaver.ast.Not;
import org.aspectj.weaver.ast.Or;
import org.aspectj.weaver.ast.And;
import org.aspectj.weaver.ast.ITestVisitor;
import org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate;
import org.aspectj.weaver.reflect.ReflectionVar;
import org.aspectj.weaver.reflect.ShadowMatchImpl;
import org.springframework.util.ReflectionUtils;
import org.aspectj.weaver.tools.ShadowMatch;
import org.aspectj.weaver.ast.Test;
import java.lang.reflect.Field;

class RuntimeTestWalker
{
    private static final Field residualTestField;
    private static final Field varTypeField;
    private static final Field myClassField;
    private final Test runtimeTest;
    
    public RuntimeTestWalker(final ShadowMatch shadowMatch) {
        try {
            ReflectionUtils.makeAccessible(RuntimeTestWalker.residualTestField);
            this.runtimeTest = (Test)RuntimeTestWalker.residualTestField.get(shadowMatch);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public boolean testsSubtypeSensitiveVars() {
        return this.runtimeTest != null && new SubtypeSensitiveVarTypeTestVisitor().testsSubtypeSensitiveVars(this.runtimeTest);
    }
    
    public boolean testThisInstanceOfResidue(final Class<?> thisClass) {
        return this.runtimeTest != null && new ThisInstanceOfResidueTestVisitor(thisClass).thisInstanceOfMatches(this.runtimeTest);
    }
    
    public boolean testTargetInstanceOfResidue(final Class<?> targetClass) {
        return this.runtimeTest != null && new TargetInstanceOfResidueTestVisitor(targetClass).targetInstanceOfMatches(this.runtimeTest);
    }
    
    static {
        try {
            residualTestField = ShadowMatchImpl.class.getDeclaredField("residualTest");
            varTypeField = ReflectionVar.class.getDeclaredField("varType");
            myClassField = ReflectionBasedReferenceTypeDelegate.class.getDeclaredField("myClass");
        }
        catch (NoSuchFieldException ex) {
            throw new IllegalStateException("The version of aspectjtools.jar / aspectjweaver.jar on the classpath is incompatible with this version of Spring: " + ex);
        }
    }
    
    private static class TestVisitorAdapter implements ITestVisitor
    {
        protected static final int THIS_VAR = 0;
        protected static final int TARGET_VAR = 1;
        protected static final int AT_THIS_VAR = 3;
        protected static final int AT_TARGET_VAR = 4;
        protected static final int AT_ANNOTATION_VAR = 8;
        
        public void visit(final And e) {
            e.getLeft().accept((ITestVisitor)this);
            e.getRight().accept((ITestVisitor)this);
        }
        
        public void visit(final Or e) {
            e.getLeft().accept((ITestVisitor)this);
            e.getRight().accept((ITestVisitor)this);
        }
        
        public void visit(final Not e) {
            e.getBody().accept((ITestVisitor)this);
        }
        
        public void visit(final Instanceof i) {
        }
        
        public void visit(final Literal literal) {
        }
        
        public void visit(final Call call) {
        }
        
        public void visit(final FieldGetCall fieldGetCall) {
        }
        
        public void visit(final HasAnnotation hasAnnotation) {
        }
        
        public void visit(final MatchingContextBasedTest matchingContextTest) {
        }
        
        protected int getVarType(final ReflectionVar v) {
            try {
                ReflectionUtils.makeAccessible(RuntimeTestWalker.varTypeField);
                return (int)RuntimeTestWalker.varTypeField.get(v);
            }
            catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
    
    private abstract static class InstanceOfResidueTestVisitor extends TestVisitorAdapter
    {
        private final Class<?> matchClass;
        private boolean matches;
        private final int matchVarType;
        
        public InstanceOfResidueTestVisitor(final Class<?> matchClass, final boolean defaultMatches, final int matchVarType) {
            this.matchClass = matchClass;
            this.matches = defaultMatches;
            this.matchVarType = matchVarType;
        }
        
        public boolean instanceOfMatches(final Test test) {
            test.accept((ITestVisitor)this);
            return this.matches;
        }
        
        @Override
        public void visit(final Instanceof i) {
            final int varType = this.getVarType((ReflectionVar)i.getVar());
            if (varType != this.matchVarType) {
                return;
            }
            Class<?> typeClass = null;
            final ResolvedType type = (ResolvedType)i.getType();
            if (type instanceof ReferenceType) {
                final ReferenceTypeDelegate delegate = ((ReferenceType)type).getDelegate();
                if (delegate instanceof ReflectionBasedReferenceTypeDelegate) {
                    try {
                        ReflectionUtils.makeAccessible(RuntimeTestWalker.myClassField);
                        typeClass = (Class<?>)RuntimeTestWalker.myClassField.get(delegate);
                    }
                    catch (IllegalAccessException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
            try {
                if (typeClass == null) {
                    typeClass = ClassUtils.forName(type.getName(), this.matchClass.getClassLoader());
                }
                this.matches = typeClass.isAssignableFrom(this.matchClass);
            }
            catch (ClassNotFoundException ex2) {
                this.matches = false;
            }
        }
    }
    
    private static class TargetInstanceOfResidueTestVisitor extends InstanceOfResidueTestVisitor
    {
        public TargetInstanceOfResidueTestVisitor(final Class<?> targetClass) {
            super(targetClass, false, 1);
        }
        
        public boolean targetInstanceOfMatches(final Test test) {
            return this.instanceOfMatches(test);
        }
    }
    
    private static class ThisInstanceOfResidueTestVisitor extends InstanceOfResidueTestVisitor
    {
        public ThisInstanceOfResidueTestVisitor(final Class<?> thisClass) {
            super(thisClass, true, 0);
        }
        
        public boolean thisInstanceOfMatches(final Test test) {
            return this.instanceOfMatches(test);
        }
    }
    
    private static class SubtypeSensitiveVarTypeTestVisitor extends TestVisitorAdapter
    {
        private final Object thisObj;
        private final Object targetObj;
        private final Object[] argsObjs;
        private boolean testsSubtypeSensitiveVars;
        
        private SubtypeSensitiveVarTypeTestVisitor() {
            this.thisObj = new Object();
            this.targetObj = new Object();
            this.argsObjs = new Object[0];
            this.testsSubtypeSensitiveVars = false;
        }
        
        public boolean testsSubtypeSensitiveVars(final Test aTest) {
            aTest.accept((ITestVisitor)this);
            return this.testsSubtypeSensitiveVars;
        }
        
        @Override
        public void visit(final Instanceof i) {
            final ReflectionVar v = (ReflectionVar)i.getVar();
            final Object varUnderTest = v.getBindingAtJoinPoint(this.thisObj, this.targetObj, this.argsObjs);
            if (varUnderTest == this.thisObj || varUnderTest == this.targetObj) {
                this.testsSubtypeSensitiveVars = true;
            }
        }
        
        @Override
        public void visit(final HasAnnotation hasAnn) {
            final ReflectionVar v = (ReflectionVar)hasAnn.getVar();
            final int varType = this.getVarType(v);
            if (varType == 3 || varType == 4 || varType == 8) {
                this.testsSubtypeSensitiveVars = true;
            }
        }
    }
}
