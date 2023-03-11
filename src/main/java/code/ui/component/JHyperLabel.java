package code.ui.component;

import code.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class JHyperLabel extends JLabel {

    private final String Html = "<html><a href=\"\">%s</a></html>";

    public static JHyperLabel create(String url) {
        return create(url, url);
    }

    public static JHyperLabel create(String name, String url) {
        JHyperLabel label = new JHyperLabel();

        label.setText(String.format(label.Html, name));
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e1) {
                    log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e1));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        return label;
    }

}
