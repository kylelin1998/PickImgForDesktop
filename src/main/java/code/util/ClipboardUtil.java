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
import java.io.*;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ClipboardUtil {

    private final static String DefaultExtension = "png";
    private final static Clipboard Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    @Data
    public static class Image {
        public byte[] content;
        public String extension;
        public boolean hasExtension;
        public String baseName;
        public String name;

        public static Image create() {
            return new Image();
        }
    }

    private static byte[] toByteArray(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        return IOUtils.toByteArray(stream);
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
                BufferedImage bufferedImage = (BufferedImage) Clipboard.getData(DataFlavor.imageFlavor);
                String baseName = UUID.randomUUID().toString().replaceAll("-", "");
                String name = baseName + "." + DefaultExtension;

                Image image = Image.create();
                image.setName(name);
                image.setBaseName(baseName);
                image.setExtension(DefaultExtension);
                image.setHasExtension(true);
                image.setContent(toByteArray(bufferedImage));
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
