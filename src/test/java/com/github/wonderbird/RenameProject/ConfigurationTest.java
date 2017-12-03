package com.github.wonderbird.RenameProject;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationTest {
    @Test
    public void getConfiguration_calledTwice_returnsSameObject() throws Exception {
        Configuration first = Configuration.getConfiguration();
        Configuration second = Configuration.getConfiguration();

        assertSame("The Configuration instances should be the same.", first, second);
    }
}