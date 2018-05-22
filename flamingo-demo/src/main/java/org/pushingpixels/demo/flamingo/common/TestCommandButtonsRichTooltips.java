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
package org.pushingpixels.demo.flamingo.common;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.pushingpixels.demo.flamingo.svg.tango.transcoded.Address_book_new;
import org.pushingpixels.demo.flamingo.svg.tango.transcoded.Help_browser;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

public class TestCommandButtonsRichTooltips extends TestCommandButtons {

    @Override
    protected JPanel getButtonPanel() {
        JPanel result = super.getButtonPanel();
        apply(result, new Command() {
            public void apply(JCommandButton button) {
                button.setActionRichTooltip(new RichTooltip.RichTooltipBuilder()
                        .setTitle(resourceBundle.getString("Tooltip.textActionTitle"))
                        .addDescriptionSection(resourceBundle.getString("Tooltip.textParagraph1"))
                        .addDescriptionSection(resourceBundle.getString("Tooltip.textParagraph2"))
                        .setMainIcon(Address_book_new.of(32, 32))
                        .setFooterIcon(Help_browser.of(32, 32))
                        .addFooterSection(resourceBundle.getString("Tooltip.textFooterParagraph1"))
                        .build());

                button.setPopupRichTooltip(new RichTooltip.RichTooltipBuilder()
                        .setTitle(resourceBundle.getString("Tooltip.textPopupTitle"))
                        .addDescriptionSection(resourceBundle.getString("Tooltip.textParagraph1"))
                        .setFooterIcon(Help_browser.of(32, 32))
                        .addFooterSection(resourceBundle.getString("Tooltip.textFooterParagraph1"))
                        .build());
            };
        });
        return result;
    }

    private static interface Command {
        void apply(JCommandButton button);
    }

    private static void apply(Container cont, Command cmd) {
        for (int i = 0; i < cont.getComponentCount(); i++) {
            Component comp = cont.getComponent(i);
            if (comp instanceof JCommandButton) {
                JCommandButton cb = (JCommandButton) comp;
                cmd.apply(cb);
            }
            if (comp instanceof Container) {
                apply((Container) comp, cmd);
            }
        }
    }

    /**
     * Main method for testing.
     * 
     * @param args
     *            Ignored.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new MetalLookAndFeel());
            } catch (Exception e) {
            }
            TestCommandButtonsRichTooltips frame = new TestCommandButtonsRichTooltips();
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });
    }
}
