/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.testng;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.impl.DeployableTest;
import org.jboss.arquillian.impl.Validate;
import org.jboss.arquillian.spi.TestResult;
import org.jboss.arquillian.spi.TestRunner;
import org.testng.TestNG;
import org.testng.internal.AnnotationTypeEnum;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * TestNGTestRunner
 *
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class TestNGTestRunner implements TestRunner
{
   private ExecutionMode executionMode = ExecutionMode.STANDALONE; 
   
   /* (non-Javadoc)
    * @see org.jboss.arquillian.spi.TestRunner#setExecutionMode(org.jboss.arquillian.spi.TestRunner.ExecutionMode)
    */
   @Override
   public void setExecutionMode(ExecutionMode executionMode)
   {
      Validate.notNull(executionMode, "ExecutionMode must be specified");
      this.executionMode = executionMode;
   }

   /* (non-Javadoc)
    * @see org.jboss.arquillian.spi.TestRunner#execute(java.lang.Class, java.lang.String)
    */
   @Override
   public TestResult execute(Class<?> testClass, String methodName)
   {
      setExecutionMode();

      TestListener resultListener = new TestListener();
      
      TestNG runner = new TestNG(false);
      runner.setVerbose(0);
      runner.addListener(resultListener);
      runner.setXmlSuites(
            Arrays.asList(createSuite(testClass, methodName)));
      
      runner.run();

      TestResult testResult = resultListener.getTestResult(); 
      
      resetExecutionMode();
      
      return testResult;
   }
   
   private XmlSuite createSuite(Class<?> className, String methodName)
   {
      XmlSuite suite = new XmlSuite();
      suite.setName("Arquillian");
      suite.setAnnotations(AnnotationTypeEnum.JDK.getName());

      XmlTest test = new XmlTest(suite);
      test.setName("Arquillian - " + className);
      List<XmlClass> testClasses = new ArrayList<XmlClass>();
      XmlClass testClass = new XmlClass(className);
      testClass.getIncludedMethods().add(methodName);
      testClasses.add(testClass);
      test.setXmlClasses(testClasses);
      return suite;
   }

   private void setExecutionMode() 
   {
      switch (executionMode)
      {
         case CONTAINER:
            DeployableTest.setInContainer(true);
            break;
      }
   }
   
   private void resetExecutionMode() 
   {
      switch (executionMode)
      {
         case CONTAINER:
            DeployableTest.setInContainer(false);
            break;
      }
   }
}
