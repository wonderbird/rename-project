package com.github.wonderbird.RenameProject.Models;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class ConfigurationTest
{
   @Test
   public void getConfiguration_calledTwice_returnsSameObject() throws Exception
   {
      final Configuration first = Configuration.getConfiguration();
      final Configuration second = Configuration.getConfiguration();

      assertSame("The Configuration instances should be the same.", first, second);
   }
}