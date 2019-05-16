package com.github.wonderbird.RenameProjectUi;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MainTest {

    @Test
    public void main_InAnyCase_ShowsUi() {
        final String[] args = new String[0];
        final UiManager uiManager = mock(UiManager.class);
        Main.setUiManager(uiManager);

        Main.main(args);
        verify(uiManager).runUi(args);
    }
}