/*
 * Copyright (c) 2005-2018 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.flamingo.internal.ui.ribbon.appmenu;

import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.flamingo.api.ribbon.*;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * The main application menu button for {@link JRibbon} component placed in a
 * {@link JRibbonFrame}. This class is for internal use only and is intended for
 * look-and-feel layer customization.
 *
 * @author Kirill Grouchnikov
 */
public class JRibbonApplicationMenuButton extends JCommandButton {
    private final static CommandButtonDisplayState APP_MENU_BUTTON_STATE =
            new CommandButtonDisplayState("Ribbon Application Menu Button", 16) {
                @Override
                public CommandButtonLayoutManager createLayoutManager(AbstractCommandButton
                        commandButton) {
                    return new CommandButtonLayoutManager() {
                        @Override
                        public int getPreferredIconSize(AbstractCommandButton commandButton) {
                            return 0;
                        }

                        @Override
                        public CommandButtonLayoutInfo getLayoutInfo(AbstractCommandButton
                                commandButton, Graphics g) {
                            CommandButtonLayoutInfo result = new CommandButtonLayoutInfo();
                            result.actionClickArea = new Rectangle(0, 0, 0, 0);
                            result.popupActionRect = new Rectangle(0, 0, 0, 0);
                            result.isTextInActionArea = false;

                            FontMetrics fm = g.getFontMetrics();
                            int labelHeight = fm.getAscent() + fm.getDescent();

                            int availableWidth = commandButton.getWidth();
                            int textWidth = (int) fm.getStringBounds(
                                    commandButton.getText(), g).getWidth();

                            TextLayoutInfo lineLayoutInfo = new TextLayoutInfo();
                            lineLayoutInfo.text = commandButton.getText();
                            lineLayoutInfo.textRect = new Rectangle();
                            result.textLayoutInfoList = new ArrayList<>();
                            result.textLayoutInfoList.add(lineLayoutInfo);

                            lineLayoutInfo.textRect.x = (availableWidth - textWidth) / 2;
                            lineLayoutInfo.textRect.y = (commandButton.getHeight() - labelHeight)
                                    / 2;
                            lineLayoutInfo.textRect.width = textWidth;
                            lineLayoutInfo.textRect.height = labelHeight;

                            result.popupClickArea = new Rectangle(0, 0, availableWidth,
                                    commandButton.getHeight());

                            return result;
                        }

                        @Override
                        public Dimension getPreferredSize(
                                AbstractCommandButton commandButton) {
                            return new Dimension(40, 20);
                        }

                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                        }

                        @Override
                        public Point getKeyTipAnchorCenterPoint(
                                AbstractCommandButton commandButton) {
                            // center at the middle of the bottom edge to be consistent with
                            // the location of key tips of the task toggle buttons
                            return new Point(commandButton.getWidth() / 2,
                                    commandButton.getHeight());
                        }
                    };
                }
            };

    /**
     * Creates a new application menu button.
     *
     * @param ribbon Associated ribbon.
     */
    public JRibbonApplicationMenuButton(JRibbon ribbon) {
        super("", null);
        this.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
        this.setDisplayState(APP_MENU_BUTTON_STATE);
        this.setHorizontalAlignment(SwingUtilities.CENTER);

        this.setPopupCallback((JCommandButton commandButton) -> {
            RibbonApplicationMenu ribbonMenu = ribbon.getApplicationMenu();
            final JRibbonApplicationMenuPopupPanel menuPopupPanel =
                    new JRibbonApplicationMenuPopupPanel(ribbonMenu);
            menuPopupPanel.applyComponentOrientation(getComponentOrientation());
            menuPopupPanel.setCustomizer(() -> {
                boolean ltr = commandButton.getComponentOrientation().isLeftToRight();

                int pw = menuPopupPanel.getPreferredSize().width;
                int x = ltr ? ribbon.getLocationOnScreen().x
                        : ribbon.getLocationOnScreen().x + ribbon.getWidth() - pw;
                int y = commandButton.getLocationOnScreen().y + commandButton.getSize().height
                        + 2;

                // make sure that the menu popup stays in bounds
                Rectangle scrBounds = commandButton.getGraphicsConfiguration().getBounds();
                if ((x + pw) > (scrBounds.x + scrBounds.width)) {
                    x = scrBounds.x + scrBounds.width - pw;
                }
                int ph = menuPopupPanel.getPreferredSize().height;
                if ((y + ph) > (scrBounds.y + scrBounds.height)) {
                    y = scrBounds.y + scrBounds.height - ph;
                }

                return new Rectangle(x, y, menuPopupPanel.getPreferredSize().width,
                        menuPopupPanel.getPreferredSize().height);
            });
            return menuPopupPanel;
        });
    }
}
