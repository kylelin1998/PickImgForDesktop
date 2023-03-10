package code.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ClipboardUtil {

    private final static String DefaultExtension = "png";
    private final static Clipboard Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    @Data
    public static class Image {
        private byte[] content;
        private String extension;
        private boolean hasExtension;
        private String baseName;
        private String name;

        public static Image create() {
            return new Image();
        }
    }

    private static byte[] toByteArray(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        return IOUtils.toByteArray(stream);
    }
    public static BufferedImage getImage(java.awt.Image image) {
        if(image instanceof BufferedImage) return (BufferedImage)image;
        Lock lock = new ReentrantLock();
        Condition size = lock.newCondition(), data = lock.newCondition();
        ImageObserver o = (img, infoflags, x, y, width, height) -> {
            lock.lock();
            try {
                if((infoflags&ImageObserver.ALLBITS)!=0) {
                    size.signal();
                    data.signal();
                    return false;
                }
                if((infoflags&(ImageObserver.WIDTH|ImageObserver.HEIGHT))!=0)
                    size.signal();
                return true;
            }
            finally { lock.unlock(); }
        };
        BufferedImage bi;
        lock.lock();
        try {
            int width, height=0;
            while( (width=image.getWidth(o))<0 || (height=image.getHeight(o))<0 )
                size.awaitUninterruptibly();
            bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bi.createGraphics();
            try {
                g.setBackground(new Color(0, true));
                g.clearRect(0, 0, width, height);
                while(!g.drawImage(image, 0, 0, o)) data.awaitUninterruptibly();
            } finally { g.dispose(); }
        } finally { lock.unlock(); }
        return bi;
    }

    private static byte[] toByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, DefaultExtension, outputStream);
        return outputStream.toByteArray();
    }

    public static Image getImage() {
        try {
            boolean dataFlavorAvailable = Clipboard.isDataFlavorAvailable(DataFlavor.javaFileListFlavor);
            if (dataFlavorAvailable) {
                File file = (File) ((List) Clipboard.getData(DataFlavor.javaFileListFlavor)).get(0);

                Image image = Image.create();
                image.setName(file.getName());
                image.setBaseName(FilenameUtils.getBaseName(file.getName()));
                image.setExtension(FilenameUtils.getExtension(file.getName()));
                image.setHasExtension(StringUtils.isNotBlank(FilenameUtils.getExtension(file.getName())));
                image.setContent(toByteArray(file));
                return image;
            }

            dataFlavorAvailable = Clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor);
            if (dataFlavorAvailable) {
                java.awt.Image bufferedImage = (java.awt.Image) Clipboard.getData(DataFlavor.imageFlavor);

                String baseName = UUID.randomUUID().toString().replaceAll("-", "");
                String name = baseName + "." + DefaultExtension;

                Image image = Image.create();
                image.setName(name);
                image.setBaseName(baseName);
                image.setExtension(DefaultExtension);
                image.setHasExtension(true);
                image.setContent(toByteArray(getImage(bufferedImage)));
                return image;
            }

        } catch (Exception e) {
            log.error(ExceptionUtil.getStackTraceWithCustomInfoToStr(e));
        }
        return null;
    }

    public static void setText(String text) {
        Clipboard.setContents(new StringSelection(text), null);
    }

}
