package org.jboss.weld.deployments.stateful.bothviews;

import org.jboss.arquillian.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;

public class EarDeployedLocalCDIInjectionTest extends LocalCDIInjectionTest
{

   @Deployment
   public static EnterpriseArchive assemble()
   {
      return createEnterpriseArchive(WarDeployedLocalCDIInjectionTest.assemble().addClass(EarDeployedLocalCDIInjectionTest.class));
   }

}
